//{{COMPONENT_IMPORT_STMTS
package advantage;
import java.util.Enumeration;
import java.util.Vector;
import versata.common.*;
import versata.common.vstrace.*;
import versata.vls.cache.*;
import versata.vls.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.amsinc.gems.adv.common.*;
import com.versata.util.logging.*;
import org.apache.commons.logging.*;

//END_COMPONENT_IMPORT_STMTS}}

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
**  IN_OBJ_ATT_CTLG*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_OBJ_ATT_CTLGImpl extends  IN_OBJ_ATT_CTLGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   private int miMsgIndex = 0 ;

//{{COMP_CLASS_CTOR
public IN_OBJ_ATT_CTLGImpl (){
	super();
}

public IN_OBJ_ATT_CTLGImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static IN_OBJ_ATT_CTLGImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new IN_OBJ_ATT_CTLGImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeDelete
public void beforeDelete(DataObject obj, Response response)
{
   String lsMsgTxt = getSession().getProperty( OBJ_ATT_MSG_TXT ) ;
   int liNumberOfAttachments;

   if ( ( lsMsgTxt != null ) && ( !lsMsgTxt.equals( "" ) ) )
   {
      String          lsAttchType ;

      getSession().setProperty( OBJ_ATT_MSG_TXT, "" ) ;

      miMsgIndex = 2 ;

      lsAttchType = getMsgValue( lsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsAttchType.length() + 2 ;

      if(lsAttchType.equals(OBJ_ATT_TYPE_REF_TBL))
      {
         String   lsTableName = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsTableName.length() + 2 ;

         /* this code is to find the number of key value pairs. 6 values are
          * already retrieved at this point, so subtract 6 from total count.
          */
         int liKeyIndexCount = Integer.parseInt( lsMsgTxt.substring(0, 2 ) ) - 2 ;

         /*
          * decrement the number of attachment count by 1.
          */

         AMSDataObject loRefTableObj = null ;
         SearchRequest loSearchReq = new SearchRequest();
         String lsKeyCol ;
         String lsKeyValue ;

         for (int liCount=0 ; liCount <  liKeyIndexCount; liCount+=2)
         {
            lsKeyCol = getMsgValue( lsMsgTxt, miMsgIndex ) ;
            miMsgIndex += lsKeyCol.length() + 2 ;
            lsKeyValue = getMsgValue( lsMsgTxt, miMsgIndex ) ;
            miMsgIndex += lsKeyValue.length() + 2 ;

            loSearchReq.addParameter(lsTableName,lsKeyCol,lsKeyValue);
         }

         try
         {
            loRefTableObj =(AMSDataObject)AMSDataObject.getObjectByKey(
                           lsTableName, loSearchReq, getSession() );
         }
         catch ( Exception foExp )
         {
            if ( AMS_DEBUG )
            {
			   System.err.println( "Exception while getting reference table object: "
                                 + foExp.getMessage() ) ;

               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", foExp);


            } /* end if ( AMS_DEBUG ) */




            raiseException( "Fatal error while getting reference table object: " +
                               foExp.getMessage(),
                               AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;
         } /* end catch ( Exception foExp ) */

         if (loRefTableObj == null)
         {
            raiseException( "Could not fetch data object for update.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         }
         else
         {
            /* Check whether the user has update permission on reference data row */
            loRefTableObj.checkUpdateSecurity() ;

            /* do not delete the attachment catalog entry, delete the attachment storage. */
            Enumeration loEnum = getAttachmentData() ;

            if ( loEnum.hasMoreElements() )
            {
               /**
                * Delete the attachment store.
                */
               IN_OBJ_ATT_STORImpl loAttData =
                  (IN_OBJ_ATT_STORImpl)loEnum.nextElement() ;
               loAttData.setDelete() ;
               loAttData.save() ;

            }

            getData( ATTR_OBJ_ATT_ST ).setint( OBJ_ATT_ST_DELETED ) ;
            getData( ATTR_OBJ_ATT_DEL_USID ).setString( getUser() ) ;
            getData( ATTR_OBJ_ATT_DEL_DT ).setVSDate( getDate() ) ;

            setUpdate() ;
            save() ;
            /*
             * If the ref table supports forms assembly and the attachment type is
             * WordML (rich text) then clear the rich text file name from the
             * row
             */

            if ((loRefTableObj instanceof FormsAssemblyAttachment)  &&
                 (obj.getData("OBJ_ATT_TYP").getString().equals(
                  String.valueOf(AMSCommonConstants.OBJ_ATT_TYPE_WORDML) )  ) )
            {
               FormsBaseUtil.deleteAttachInfo(loRefTableObj);
            }

            liNumberOfAttachments = loRefTableObj.getData( ATTR_OBJ_ATT_PG_TOT ).getint() ;
            loRefTableObj.getData( ATTR_OBJ_ATT_PG_TOT ).setint( liNumberOfAttachments - 1 ) ;
            loRefTableObj.save() ;
            return ;

         }//if (loRefTableObj == null)
      }
      else if ( lsAttchType.equals( OBJ_ATT_TYPE_DOC_VIEW ) )
      {
         AMSDocHeader    loDocHeader    = null ;
         AMSDocComponent loDocComp      = null ;
         int             liPriGrpTot ;
         int             liSecGrpTot ;
         String          lsSecGrp ;
         String          lsIsDocComp ;
         String          lsDocCompNm ;
         String          lsDocIntLnNoNm ;
         String          lsDocIntLnNo ;

         loDocHeader = getDocHeader( lsMsgTxt ) ;

         if ( loDocHeader == null )
         {
            raiseException( "Unable to find parent document.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( loDocHeader == null ) */

         if ( loDocHeader.getData( ATTR_DOC_PHASE_CD ).getint() != DOC_PHASE_DRAFT )
         {
            raiseException( "Object attachments can only be deleted from documents in Draft phase.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( loDocHeader.getData( ATTR_DOC_PHASE_CD ).getint() != DOC_PHASE_DRAFT ) */
         else if (( loDocHeader.getData( ATTR_DOC_FUNC_CD ).getint() == DOC_FUNC_CANCELED )
                 && ( getData( ATTR_OBJ_ATT_ST ).getint() != OBJ_ATT_ST_NEW ))
         {
            raiseException( "Object attachments cannot be deleted from cancellation documents in Draft phase.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end of else if (loDocHeader.getData( ATTR_DOC_FUNC_CD ).getint() == DOC_FUNC_CANCELED ) */

         if ( !loDocHeader.objectAttachmentsAuthorized() )
         {
            raiseException( "Not authorized to delete object attachment.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( !loDocHeader.objectAttachmentsAuthorized() ) */

         if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_PEND_DEL )
         {
            raiseException( "Pending deletion attachments can not be deleted again.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end of else if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_PEND_DEL ) */
         else if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_DELETED )
         {
            raiseException( "Deleted attachments can not be deleted again.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end of else if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_DELETED ) */
         lsIsDocComp = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsIsDocComp.length() + 2 ;

         lsDocCompNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocCompNm.length() + 2 ;

         lsDocIntLnNoNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocIntLnNoNm.length() + 2 ;

         lsDocIntLnNo = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocIntLnNo.length() + 2 ;

         if ( lsIsDocComp.equals( "Y" ) )
         {
            loDocComp = getDocComponent( loDocHeader, lsDocCompNm,
                                         lsDocIntLnNoNm, lsDocIntLnNo ) ;

            /*
             * If the document supports forms assembly and the attachment type is
             * WordML (rich text) then clear the rich text file name from the
             * row
             */

            if ((loDocComp instanceof FormsAssemblyAttachment)  &&
                 (obj.getData("OBJ_ATT_TYP").getString().equals(
                  String.valueOf(AMSCommonConstants.OBJ_ATT_TYPE_WORDML) )  ) )
            {
               FormsBaseUtil.deleteAttachInfo(loDocComp);
            }

            if ( loDocComp == null )
            {
               raiseException( "Unable to find parent document line.",
                               AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
               response.reject() ;
               return ;
            } /* end if ( loDocComp == null ) */
            liSecGrpTot = loDocComp.getData( ATTR_OBJ_ATT_SG_TOT ).getint() ;
            loDocComp.getData( ATTR_OBJ_ATT_SG_TOT ).setint( liSecGrpTot - 1 ) ;
            loDocComp.save() ;
         } /* end if ( lsIsDocComp.equals( "Y" ) ) */
         else
         {
            liSecGrpTot = loDocHeader.getData( ATTR_OBJ_ATT_SG_TOT ).getint() ;
            loDocHeader.getData( ATTR_OBJ_ATT_SG_TOT ).setint( liSecGrpTot - 1 ) ;
         } /* end else */

         liPriGrpTot = loDocHeader.getData( ATTR_OBJ_ATT_PG_TOT ).getint() ;
         loDocHeader.getData( ATTR_OBJ_ATT_PG_TOT ).setint( liPriGrpTot - 1 ) ;
         loDocHeader.save() ;

         /* Check if the document is a modification draft. If it is, do not delete
          * if attachment status is active, change status of Active attachments
          * to Pending Delete status.
          */
         if ( loDocHeader.getData( ATTR_DOC_FUNC_CD ).getint() == DOC_FUNC_MODIFIED )
         {
            if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_ACTIVE )
            {
               getData( ATTR_OBJ_ATT_ST ).setint( OBJ_ATT_ST_PEND_DEL ) ;
               getData( ATTR_OBJ_ATT_DEL_USID ).setString( getUser() ) ;
               getData( ATTR_OBJ_ATT_DEL_DT ).setVSDate( getDate() ) ;
               setUpdate() ;
               save() ;

               return ;
            } /* end of if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_ACTIVE ) ) */
         } /* end if ( loDocHeader.getData( ATTR_DOC_FUNC_CD ).getint() == DOC_FUNC_MODIFIED ) */
         else if ( loDocHeader.getData( ATTR_DOC_FUNC_CD ).getint() == DOC_FUNC_NEW )
         {
           /* Check if the document is a new draft. If it is, do not delete
            * if attachment status is deleted, change status of New attachments
            * to Deleted status.
            */
            if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_NEW )
            {
               getData( ATTR_OBJ_ATT_ST ).setint( OBJ_ATT_ST_DELETED ) ;
               getData( ATTR_OBJ_ATT_DEL_USID ).setString( getUser() ) ;
               getData( ATTR_OBJ_ATT_DEL_DT ).setVSDate( getDate() ) ;
               setUpdate() ;
               save() ;

               /* do not delete the attachment catalog entry, delete the attachment storage. */
               Enumeration loEnum = getAttachmentData() ;

               if ( loEnum.hasMoreElements() )
               {
                  /**
                   * Delete the attachment store.
                   */
                  IN_OBJ_ATT_STORImpl loAttData =
                        (IN_OBJ_ATT_STORImpl)loEnum.nextElement() ;
                  loAttData.setDelete() ;
                  loAttData.save() ;
               }
               return ;
            } /* end of if ( getData( ATTR_OBJ_ATT_ST ).getint() == OBJ_ATT_ST_NEW ) ) */
         } /* end if ( loDocHeader.getData( ATTR_DOC_FUNC_CD ).getint() == DOC_FUNC_NEW ) */
         else if ( getData( ATTR_OBJ_ATT_ALT_UNID ).getObject() != null )
         {
            /* check if the attachment has alternate ID, then do not delete. */
            getData( ATTR_OBJ_ATT_ST ).setint( OBJ_ATT_ST_DELETED ) ;
            getData( ATTR_OBJ_ATT_DEL_USID ).setString( getUser() ) ;
            getData( ATTR_OBJ_ATT_DEL_DT ).setVSDate( getDate() ) ;

            setUpdate() ;
            save() ;

            return ;
         } /* end if ( getData( ATTR_OBJ_ATT_ALT_UNID ).getObject() != null ) */
      } /* end if(lsAttchType.equals(OBJ_ATT_TYPE_DOC_VIEW)) */
      else if ( lsAttchType.equals( OBJ_ATT_TYPE_ALT_VIEW ) )
      {
         String        lsRsrcNm ;
         String        lsDocCat ;
         String        lsDocType ;
         String        lsDocCode ;
         String        lsDocDept ;
         String        lsDocID  ;

         lsRsrcNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsRsrcNm.length() + 2 ;

         lsDocCat = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocCat.length() + 2 ;

         lsDocType = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocType.length() + 2 ;

         lsDocCode = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocCode.length() + 2 ;

         lsDocDept = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocDept.length() + 2 ;

         lsDocID = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocID.length() + 2 ;

         /**
          * this code is to find the number of key value pairs. 16 values are
          * already retrieved at this point, so subtract 16 from total count.
          */
         int liKeyIndexCount = Integer.parseInt( lsMsgTxt.substring(0, 2 ) ) - 16 ;

         AMSDataObject loEntityObj = null ;
         SearchRequest loEntitySearchReq = new SearchRequest();
         String lsKeyCol ;
         String lsKeyValue ;

         for (int liCount=0 ; liCount <  liKeyIndexCount; liCount+=2)
         {
            lsKeyCol = getMsgValue( lsMsgTxt, miMsgIndex ) ;
            miMsgIndex += lsKeyCol.length() + 2 ;
            lsKeyValue = getMsgValue( lsMsgTxt, miMsgIndex ) ;
            miMsgIndex += lsKeyValue.length() + 2 ;

            loEntitySearchReq.addParameter(lsRsrcNm, lsKeyCol, lsKeyValue);
         }

         try
         {
            loEntityObj = (AMSDataObject)AMSDataObject.getObjectByKey(
                            lsRsrcNm, loEntitySearchReq, getSession() );
         }
         catch ( Exception foExp )
         {
            if ( moAMSLog.isFatalEnabled() )
            {
               moAMSLog.fatal( "Exception while getting parent entity object: " +
                            foExp.getMessage(), foExp ) ;
            } /* end if ( moAMSLog.isFatalEnabled() ) */

            raiseException( "%c:Q0034%" ) ;
         } /* end catch ( Exception foExp ) */

         if (loEntityObj == null)
         {
            raiseException( "%c:Q0035%" ) ;
         } /* end if (loEntityObj == null) */
         else
         {
            Session             loSession = getSession() ;
            SearchRequest       loSrchReq ;
            IN_OBJ_ATT_STORImpl loAttStor ;

            /**
             * Check security to validate the delete action for
             * alternate view attachments for the current user
             */
            loEntityObj.checkAMSSecurity( ACTN_DELETE_ALT_ATT ) ;


            loSrchReq = new SearchRequest() ;
            loSrchReq.addParameter( "IN_OBJ_ATT_STOR", ATTR_OBJ_ATT_UNID, String.valueOf( getOBJ_ATT_UNID() ) ) ;
            loAttStor = (IN_OBJ_ATT_STORImpl)IN_OBJ_ATT_STORImpl.getObjectByKey( loSrchReq, loSession ) ;

            loSrchReq = new SearchRequest() ;
            loSrchReq.addParameter( "DOC_HDR", ATTR_DOC_CD, lsDocCode ) ;
            loSrchReq.addParameter( "DOC_HDR", ATTR_DOC_DEPT_CD, lsDocDept ) ;
            loSrchReq.addParameter( "DOC_HDR", ATTR_DOC_ID, lsDocID ) ;

            /*
             * Check if any version of the document with this
             * attachment exists in the catalog. If any version
             * exists, just set the alternate view fields to null
             * and cancel the event performing delete.
             * If none exist, then the attachment is deleted.
             */
            if ( DOC_HDRImpl.getObjectCount( loSrchReq, loSession ) <= 0 )
            {
               loAttStor.setDelete() ;
               loAttStor.save() ;
            } /* end if ( DOC_HDRImpl.getObjectCount( loSrchReq, loSession ) <= 0 ) */
            setOBJ_ATT_ALT_ST( OBJ_ATT_ST_DELETED ) ;

            setUpdate() ;
            save() ;

            response.cancel() ;

         } /* end else */

      } /* end if ( lsAttchType.equals(OBJ_ATT_TYPE_ALT_VIEW ) ) */

   } /* end if ( ( lsMsgTxt != null ) && ( !lsMsgTxt.equals( "" ) ) ) */
}
//END_COMP_EVENT_beforeDelete}}

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   validateAttachmentName(); // ADVHR00020276
   //check attachment type to see if it can be attached
   doCommonAtchTypeCheck(obj, response);

   if ( isNull( "OBJ_ATT_UNID" ) )
   {
      try
      {
         setOBJ_ATT_UNID( AMSUniqNum.getUniqNum( "IN_OBJ_ATT_CTLG" ) ) ;
      } /* end try */
      catch( AMSUniqNumException foExp )
      {

         if ( AMS_DEBUG )
         {
            System.err.println( "Unable to generate attachment ID autonumber: "
                                + foExp.getMessage() ) ;

            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         } /* end if ( AMS_DEBUG ) */

         raiseException( "Error assigning object attachment ID.",
                         AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;
         response.reject() ;
         return ;
      } /* end catch( AMSUniqNumException foExp ) */
   } /* end if ( isNull( "OBJ_ATT_UNID" ) ) */
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
    validateAttachmentName(); // ADVHR00020276

   /* This portion of the code checks for restore action from
    * PLS for document attachments. In this case, add the
    * primary and secondary count as appropriate for the document
    * header and component bacause the "PENDING_DELETE" status is
    * changed to "ACTIVE".
    */
   String lsMsgTxt = getSession().getProperty( OBJ_ATT_MSG_TXT ) ;


   if ( ( lsMsgTxt != null ) && ( !lsMsgTxt.equals( "" ) ) )
   {
      String          lsAction    ;
      String          lsDocType ;
      getSession().setProperty( OBJ_ATT_MSG_TXT, "" ) ;
      miMsgIndex = 2 ;

      lsAction = getMsgValue( lsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsAction.length() + 2 ;

      lsDocType = getMsgValue( lsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocType.length() + 2 ;

      if ( (lsAction.equals( ACTION_ATT_RESTORE ))
            && (lsDocType.equals( OBJ_ATT_TYPE_DOC_VIEW )) )
      {
         //check attachment type to see if it can be attached
         doCommonAtchTypeCheck(obj, response);

         AMSDocHeader    loDocHeader    = null ;
         AMSDocComponent loDocComp      = null ;
         int             liPriGrpTot ;
         int             liSecGrpTot ;
         String          lsSecGrp ;
         String          lsIsDocComp ;
         String          lsDocCompNm ;
         String          lsDocIntLnNoNm ;
         String          lsDocIntLnNo ;

         loDocHeader = getDocHeader( lsMsgTxt ) ;

         if ( loDocHeader == null )
         {
            raiseException( "Unable to find parent document.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( loDocHeader == null ) */

         lsIsDocComp = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsIsDocComp.length() + 2 ;

         lsDocCompNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocCompNm.length() + 2 ;

         lsDocIntLnNoNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocIntLnNoNm.length() + 2 ;

         lsDocIntLnNo = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocIntLnNo.length() + 2 ;

         if ( lsIsDocComp.equals( "Y" ) )
         {
            loDocComp = getDocComponent( loDocHeader, lsDocCompNm,
                                         lsDocIntLnNoNm, lsDocIntLnNo ) ;
            if ( loDocComp == null )
            {
               raiseException( "Unable to find parent document line.",
                               AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
               response.reject() ;
               return ;
            } /* end if ( loDocComp == null ) */
            liSecGrpTot = loDocComp.getData( ATTR_OBJ_ATT_SG_TOT ).getint() ;
            loDocComp.getData( ATTR_OBJ_ATT_SG_TOT ).setint( liSecGrpTot + 1 ) ;
            loDocComp.save() ;
         } /* end if ( lsIsDocComp.equals( "Y" ) ) */
         else
         {
            liSecGrpTot = loDocHeader.getData( ATTR_OBJ_ATT_SG_TOT ).getint() ;
            loDocHeader.getData( ATTR_OBJ_ATT_SG_TOT ).setint( liSecGrpTot + 1 ) ;
         } /* end else */

         liPriGrpTot = loDocHeader.getData( ATTR_OBJ_ATT_PG_TOT ).getint() ;
         loDocHeader.getData( ATTR_OBJ_ATT_PG_TOT ).setint( liPriGrpTot + 1 ) ;
         loDocHeader.save() ;

      } /* end if(lsDocType.equals(OBJ_ATT_TYPE_DOC_VIEW)) */
   } /* end if ( ( lsMsgTxt != null ) && ( !lsMsgTxt.equals( "" ) ) ) */

}
//END_COMP_EVENT_beforeUpdate}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

   private AMSDocHeader getDocHeader( String fsMsgTxt )
   {
      String        lsDocCat ;
      String        lsDocType ;
      String        lsDocCode ;
      String        lsDocDept ;
      String        lsDocID  ;
      String        lsDocVersNo  ;
      String        lsDataObjName  ;
      SearchRequest loSrchReq     = new SearchRequest() ;
      AMSDocHeader  loDocHeader   = null ;

      lsDocCat = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocCat.length() + 2 ;

      lsDocType = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocType.length() + 2 ;

      lsDocCode = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocCode.length() + 2 ;

      lsDocDept = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocDept.length() + 2 ;

      lsDocID = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocID.length() + 2 ;

      lsDocVersNo = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocVersNo.length() + 2 ;

      lsDataObjName = getMsgValue( fsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDataObjName.length() + 2 ;

      loSrchReq.addParameter( lsDataObjName, ATTR_DOC_CAT, lsDocCat ) ;
      loSrchReq.addParameter( lsDataObjName, ATTR_DOC_TYP, lsDocType ) ;
      loSrchReq.addParameter( lsDataObjName, ATTR_DOC_CD, lsDocCode ) ;
      loSrchReq.addParameter( lsDataObjName, ATTR_DOC_DEPT_CD, lsDocDept ) ;
      loSrchReq.addParameter( lsDataObjName, ATTR_DOC_ID, lsDocID ) ;
      loSrchReq.addParameter( lsDataObjName, ATTR_DOC_VERS_NO, lsDocVersNo ) ;

      try
      {
         loDocHeader = (AMSDocHeader)getObjectByKey( lsDataObjName, loSrchReq, session ) ;
      } /* end try */
      catch( ClassCastException foClassCastExp )
      {
         raiseException( "Object attachments are only permitted on documents.",
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         return null ;
      } /* end catch( ClassCastException foClassCastExp ) */
      catch( ClassNotFoundException foClassNotFoundExp )
      {
         raiseException( "Unable to locate parent document type.",
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         return null ;
      } /* end catch( ClassCastException foClassExp ) */
      catch( Exception foExp )
      {
         raiseException( "Error retrieving parent document. " + foExp.getMessage(),
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         return null ;
      } /* end catch( Exception foExp ) */

      return loDocHeader ;
   } /* end getDocHeader() */

   private AMSDocComponent getDocComponent( AMSDocHeader foDocHeader, String fsDocCompNm,
                                            String fsIntLnNoNm, String fsIntLnNo )
   {
      SearchRequest   loSrchReq = new SearchRequest() ;
      AMSDocComponent loDocComp = null ;

      loSrchReq.addParameter( fsDocCompNm, ATTR_DOC_CAT,
                              foDocHeader.getData( ATTR_DOC_CAT ).getString() ) ;
      loSrchReq.addParameter( fsDocCompNm, ATTR_DOC_TYP,
                              foDocHeader.getData( ATTR_DOC_TYP ).getString() ) ;
      loSrchReq.addParameter( fsDocCompNm, ATTR_DOC_CD,
                              foDocHeader.getData( ATTR_DOC_CD ).getString() ) ;
      loSrchReq.addParameter( fsDocCompNm, ATTR_DOC_DEPT_CD,
                              foDocHeader.getData( ATTR_DOC_DEPT_CD ).getString() ) ;
      loSrchReq.addParameter( fsDocCompNm, ATTR_DOC_ID,
                              foDocHeader.getData( ATTR_DOC_ID ).getString() ) ;
      loSrchReq.addParameter( fsDocCompNm, ATTR_DOC_VERS_NO,
                              foDocHeader.getData( ATTR_DOC_VERS_NO ).getString() ) ;
      loSrchReq.addParameter( fsDocCompNm, fsIntLnNoNm, fsIntLnNo ) ;

      try
      {
         loDocComp = (AMSDocComponent)getObjectByKey( fsDocCompNm, loSrchReq, session ) ;
      } /* end try */
      catch( ClassCastException foClassCastExp )
      {
         raiseException( "Object attachments are only permitted on documents.",
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         return null ;
      } /* end catch( ClassCastException foClassCastExp ) */
      catch( ClassNotFoundException foClassNotFoundExp )
      {
         raiseException( "Unable to locate parent document line type.",
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         return null ;
      } /* end catch( ClassCastException foClassExp ) */
      catch( Exception foExp )
      {
         raiseException( "Error retrieving parent document line. " + foExp.getMessage(),
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         return null ;
      } /* end catch( Exception foExp ) */

      return loDocComp ;
   } /* end getDocComponent() */


   /**
    * This method will return IN_OBJ_ATT_CTLG data objects
    * that belong to the same primary group ID.
    *
    * @param fsPrimGrpId the primary group ID
    * @param foSession the current Session object
    * @return Enumeration an Enumeration of IN_OBJ_ATT_CTLGImpl objects
    */
   public static Enumeration getAttachmentInfoByPrimGrp( String fsPrimGrpId,
                                                         Session foSession )
   {
      SearchRequest loSearchReq = new SearchRequest() ;

      loSearchReq.addParameter( "IN_OBJ_ATT_CTLG",
                                "OBJ_ATT_PG_UNID",
                                fsPrimGrpId ) ;

      return IN_OBJ_ATT_CTLGImpl.getObjects( loSearchReq,
                                             foSession ) ;
   }


   /**
    * This method will return IN_OBJ_ATT_CTLG data objects
    * that belong to the same primary and secondary group ID.
    *
    * @param fsPrimGrpId the primary group ID
    * @param flSecGrpId the secondary group ID
    * @param foSession the current Session object
    * @return Enumeration an Enumeration of IN_OBJ_ATT_CTLGImpl objects
    */
   public static Enumeration getAttachmentInfoBySecGrp( String fsPrimGrpId,
                                                        long flSecGrpId,
                                                        Session foSession )
   {
      SearchRequest loSearchReq = new SearchRequest() ;

      loSearchReq.addParameter( "IN_OBJ_ATT_CTLG",
                                "OBJ_ATT_PG_UNID",
                                fsPrimGrpId ) ;

      loSearchReq.addParameter( "IN_OBJ_ATT_CTLG",
                                "OBJ_ATT_SG_UNID",
                                Long.toString( flSecGrpId ) ) ;

      return IN_OBJ_ATT_CTLGImpl.getObjects( loSearchReq,
                                             foSession ) ;
   }


   /**
    * This method imports the document object attachments, if they
    * exist for the document version being restored. First, the PGID
    * is obtained from the name of the zip file. Based on the PGID,
    * a search is done on the object attachment catalog to return the
    * attachment catalog for the document. For each catlog entry, then
    * it extracts the corresponding file from the zip archive and
    * restores it in the attachment store.
    *
    * @param fsFullZipFileName full path of the zip file name
    * @param foSession the current Session object
    */
   public static boolean importDocumentObjectAttachments( String fsFullZipFileName,
                                                          Session foSession,
                                                          int [] fiAttachmentTypes )
   {
      /**
       * Get all of the attachments from the imported file
       */
      AMSZipFileArchiveStorage loArchStorage = null ;
      IN_OBJ_ATT_CTLGImpl      loAttCtlg     = null ;
      Enumeration              loAttCtlgEnum = null ;
      String                   lsAttCtlgPGID = null ;
      SearchRequest            loSearchReq   = new SearchRequest() ;
      boolean                  lboolImportOk = false ;
      ZipFile loZipFile = null ;

      try
      {
         /**
          * Get the PGID from the name of the zip file
          */
         int liLastSeparatorIndex = fsFullZipFileName.lastIndexOf( File.separator ) ;
         String lsZipFileName     = fsFullZipFileName.substring( liLastSeparatorIndex + 1 ) ;

         int liDotIndex = lsZipFileName.lastIndexOf( "." ) ;
         lsAttCtlgPGID  = lsZipFileName.substring( 0, liDotIndex ) ;

         /**
          * Do a search on PGID and get the attachment catalog
          */
         loSearchReq.addParameter( "IN_OBJ_ATT_CTLG",
                                   ATTR_OBJ_ATT_PG_UNID,
                                   lsAttCtlgPGID ) ;

         /**
          * Get only the specified attachment types
          */
         if ( fiAttachmentTypes != null )
         {
            StringBuffer loBuffer = new StringBuffer(256) ;

            loBuffer.append( " OBJ_ATT_TYP IN " ) ;
            loBuffer.append( "(" ) ;
            for (int liIndex = 0; liIndex < fiAttachmentTypes.length; liIndex++)
            {
               loBuffer.append( fiAttachmentTypes[liIndex] ) ;
               if ( fiAttachmentTypes.length > 1 && liIndex < fiAttachmentTypes.length - 1 )
               {
                  loBuffer.append( "," ) ;
               }
            }
            loBuffer.append( ") " ) ;

            loSearchReq.add( loBuffer.toString() ) ;
         }


         loAttCtlgEnum = IN_OBJ_ATT_CTLGImpl.getObjects( loSearchReq,
                                                         foSession ) ;

         /**
          * For each attachment catalog entry, extract the attachment entry
          * from the zip file and store the attachment data
          */
         while ( loAttCtlgEnum.hasMoreElements() )
         {
            loAttCtlg = (IN_OBJ_ATT_CTLGImpl)loAttCtlgEnum.nextElement() ;

            //******** VSS SPECIFIC CUSTOMIZATION (if statement)
            // Ignore entry if it has been deleted in IN_OBJ_ATT_STOR
            if (loAttCtlg.getOBJ_ATT_ST() != 3)
            {
               long llAttCtlgSeqNum = loAttCtlg.getOBJ_ATT_SEQ_NO() ;
               long llAttCtlgUNID   = loAttCtlg.getOBJ_ATT_UNID() ;

               /**
                * Find and delete the storage attachment entry before
                * inserting a new one. Every attachment gets inserted
                * even if present in the DB already.
                */
               SearchRequest loStorSearchReq = new SearchRequest() ;
               loStorSearchReq.addParameter( "IN_OBJ_ATT_STOR",
                                             ATTR_OBJ_ATT_UNID,
                                             String.valueOf( llAttCtlgUNID ) ) ;

               IN_OBJ_ATT_STORImpl loOldAttData =
                  (IN_OBJ_ATT_STORImpl)IN_OBJ_ATT_STORImpl.getObjectByKey( loStorSearchReq, foSession ) ;

               if ( loOldAttData != null)
               {
                  loOldAttData.setDelete() ;
                  loOldAttData.save() ;
               }

               /**
                * Get the attachment data from the archive
                */
               if ( loArchStorage == null )
               {
                  loArchStorage = new AMSZipFileArchiveStorage( foSession ) ;

                  if ( !loArchStorage.getArchive( fsFullZipFileName,
                        AMSArchiveStorage.ARCHIVE_TYPE_OBJ_ATT ) )
                  {
                     System.err.println( "Could not find import attachments data." ) ;
                     return false ;
                  }
               }

               /**
                * Now restore back the attachment data
                */
               IN_OBJ_ATT_STORImpl loAttData =
                  (IN_OBJ_ATT_STORImpl)IN_OBJ_ATT_STORImpl.getNewObject( foSession, true ) ;

               loAttData.setAttachmentCtlgEntry( loAttCtlg ) ;

               /**
                * Get the archive data from the storage
                */
               if ( loArchStorage != null )
               {
                     ZipEntry loEntry = null ;
                     File loFile = new File( fsFullZipFileName ) ;
                     loZipFile = new ZipFile( loFile ) ;
                     loEntry = loZipFile.getEntry( llAttCtlgSeqNum+".DAT" ) ;

                     if ( loEntry != null )
                     {
                        InputStream loIS = loArchStorage.getArchiveData( llAttCtlgSeqNum ) ;

                       /**
                        * Create a byte array output stream with an intial size of 5K bytes
                        */
                        ByteArrayOutputStream loByteOS = new ByteArrayOutputStream( 1024 * 5 ) ;

                        int liByte = 0 ;

                        if (loIS != null)
                        {
                           while ( ( liByte = loIS.read() ) != -1 )
                           {
                              loByteOS.write( liByte ) ;
                           }
                           loIS.close();
                        }
                        /**
                         * Get the data from the byte array and store it in the data store
                         */
                         byte [] loData = loByteOS.toByteArray() ;

                         loAttData.getData( "OBJ_ATT_DATA" ).setbytes( loData ) ;

                         loAttData.save() ;
                         loByteOS.close();
                     }
                     loZipFile.close();
                     loZipFile = null ;
                  }

            }/*end of if statement -- End of VSS CUSTOMIZATION */
      }/* end of the while loop */

      lboolImportOk = true;
      }
      catch ( AMSArchiveStorageException loArchiveException )
      {
         raiseException( "Error while importing document attachment(s) archive: " +
                         loArchiveException.getMessage(), foSession, AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         lboolImportOk = false ;
      }
      catch ( Exception loException )
      {
         raiseException( "Error while importing document attachment(s): " +
                         loException.getMessage(), foSession, AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         lboolImportOk = false ;
      }
      finally
      {
         try
         {
            if ( loArchStorage != null )
            {
               loArchStorage.closeArchive() ;
            }
         }
         catch ( AMSArchiveStorageException loArchiveException )
         {
            raiseException( "Error while closing document attachment(s) archive: " +
                            loArchiveException.getMessage(), foSession, AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            lboolImportOk = false ;
         }


      }

   return lboolImportOk;
   } /* end of importDocumentObjectAttachments method */

    /*
    * This static method returns a new primary group ID as a string
    */
   public static String getNewObjAttPrimGrpID( Session foSession )
   {
      String lsPrimGrpID = null ;

      try
      {
         long llPriGrpID = AMSUniqNum.getUniqNum( ATTR_OBJ_ATT_PG_UNID ) ;
         lsPrimGrpID = AMSParams.msAttachmentSystemName + Long.toString(llPriGrpID) ;
      }
      catch ( AMSUniqNumException foExp )
      {
            if ( AMS_DEBUG )
            {
               System.err.println( "Exception while getting attachments primary group num: "
                                      + foExp.getMessage() ) ;
            
			  // Add exception log to logger object
              moAMSLog.error("Unexpected error encountered while processing. ", foExp);

            } /* end if ( AMS_DEBUG ) */
            AMSDataObject.raiseException( "Fatal error while getting object attachments primary group number: " +
                               foExp.getMessage(), foSession,
                               AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;

      } /* end catch ( AMSUniqNumException foExp ) */

      return lsPrimGrpID ;
   }

   /*
    * This static method returns a new secondary group ID as long.
    */
   public static long getNewObjAttSecGrpID( Session foSession )
   {
      long llSecGrpID = -1 ;

      try
      {
         llSecGrpID = AMSUniqNum.getUniqNum( ATTR_OBJ_ATT_SG_UNID ) ;
      }
      catch ( AMSUniqNumException foExp )
      {
         if ( AMS_DEBUG )
         {



            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);
            System.err.println( "Exception while getting attachments secondary group num: "
                                 + foExp.getMessage() ) ;

         } /* end if ( AMS_DEBUG ) */

             AMSDataObject.raiseException( "Fatal error while getting object attachments secondary group number: " +
                               foExp.getMessage(), foSession,
                               AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;

      } /* end catch ( AMSUniqNumException foExp ) */

      return llSecGrpID ;
   } /* end of getNewObjAttSecGrpID() method */

   /*
    * This static method returns a new alternate ID as an int
    */
   public static long getNewObjAttAltID( Session foSession )
   {
      long llAltGrpID = -1 ;

      try
      {
         llAltGrpID = AMSUniqNum.getUniqNum( ATTR_OBJ_ATT_ALT_UNID ) ;
      }
      catch ( AMSUniqNumException foExp )
      {
         if ( AMS_DEBUG )
         {
		    System.err.println( "Exception while getting attachments alternate group num: "
                                 + foExp.getMessage() ) ;

            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         } /* end if ( AMS_DEBUG ) */

             AMSDataObject.raiseException( "Fatal error while getting object attachments alternate number: " +
                               foExp.getMessage(), foSession,
                               AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;

      } /* end catch ( AMSUniqNumException foExp ) */

      return llAltGrpID ;
   } /* end of getNewObjAttAltID() method */

   /*
    * Checks the attachment types limitation flags to see
    * if this particular attachment type can only have one
    * type of its attachment
    * @DataObject - source calling data object
    * @Response - source calling response
    */
   private void doCommonAtchTypeCheck(DataObject foobj, Response foresponse)
   {
      SearchRequest loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "CVL_OBJ_ATT_TYP", "CVL_OBJ_ATT_TYP_SV", new Integer(getOBJ_ATT_TYP()).toString()) ;
      CVL_OBJ_ATT_TYPImpl loCvlObj = null;

      try
      {
         loCvlObj =(CVL_OBJ_ATT_TYPImpl)AMSDataObject.getObjectByKey(
                                       "CVL_OBJ_ATT_TYP", loSrchReq, getSession() );
      }
      catch ( Exception foExp )
      {
         if ( AMS_DEBUG )
         {
		 
		    System.err.println( "Exception while getting reference table object: "
                                 + foExp.getMessage() ) ;
           // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         } /* end if ( AMS_DEBUG ) */

         raiseException( "Fatal error while getting reference table object: " +
                          foExp.getMessage(),
                          AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;
      } /* end catch ( Exception foExp ) */

      if(loCvlObj != null && loCvlObj.getPG_ATT_LTD_FL())
      {
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_PG_UNID", getOBJ_ATT_PG_UNID() ) ;
         loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_TYP", new Integer(getOBJ_ATT_TYP()).toString() ) ;

         //ensure that the object attachment status is not 'Deleted' or 'Pending Delete'.
         loSrchReq.add( "OBJ_ATT_ST NOT IN ( " + AMSCommonConstants.OBJ_ATT_ST_DELETED + "," + AMSCommonConstants.OBJ_ATT_ST_PEND_DEL + " )" );
         if(loCvlObj.getSG_ATT_LTD_FL())
         {
            if((new Long(getOBJ_ATT_SG_UNID())).intValue() == 0)
            {

               loSrchReq.add( " AND ( OBJ_ATT_SG_UNID is null OR OBJ_ATT_SG_UNID = " + getOBJ_ATT_SG_UNID() + " ) ") ;
            }
            else
            {
               loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_SG_UNID", new Long(getOBJ_ATT_SG_UNID()).toString() ) ;
            }
         }

         if(getObjectCount( loSrchReq, getSession() ) > 0 )
         {
            raiseException( "Only one attachment with this attachment type is" +
                            " permitted on this record.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;

            foresponse.reject() ;
         }
      }


      return;

   }

   /*
    * Checks the attachment types limitation flags to see
    * if this particular attachment type can only have one
    * type of its attachment
    * @Response - source calling response
    */
   protected boolean doCommonAtchTypeCheck()
   {
      SearchRequest loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "CVL_OBJ_ATT_TYP", "CVL_OBJ_ATT_TYP_SV", new Integer(getOBJ_ATT_TYP()).toString()) ;
      CVL_OBJ_ATT_TYPImpl loCvlObj = null;

      try
      {
         loCvlObj =(CVL_OBJ_ATT_TYPImpl)AMSDataObject.getObjectByKey(
                                       "CVL_OBJ_ATT_TYP", loSrchReq, getSession() );
      }
      catch ( Exception foExp )
      {
         if ( AMS_DEBUG )
         {
		    System.err.println( "Exception while getting reference table object: "
                                 + foExp.getMessage() ) ;

            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         } /* end if ( AMS_DEBUG ) */

         raiseException( "Fatal error while getting reference table object: " +
                          foExp.getMessage(),
                          AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;
      } /* end catch ( Exception foExp ) */

      if(loCvlObj != null && loCvlObj.getPG_ATT_LTD_FL())
      {
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_PG_UNID", getOBJ_ATT_PG_UNID() ) ;
         loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_TYP", new Integer(getOBJ_ATT_TYP()).toString() ) ;

         //ensure that the object attachment status is not 'Deleted' or 'Pending Delete'.
         loSrchReq.add( "OBJ_ATT_ST NOT IN ( " + AMSCommonConstants.OBJ_ATT_ST_DELETED + "," + AMSCommonConstants.OBJ_ATT_ST_PEND_DEL + " )" );


         if(loCvlObj.getSG_ATT_LTD_FL())
         {
            if((new Long(getOBJ_ATT_SG_UNID())).intValue() == 0)
            {

               loSrchReq.add( " AND ( OBJ_ATT_SG_UNID is null OR OBJ_ATT_SG_UNID = " + getOBJ_ATT_SG_UNID() + " ) ") ;
            }
            else
            {
               loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_SG_UNID", new Long(getOBJ_ATT_SG_UNID()).toString() ) ;
            }
         }


         if(getObjectCount( loSrchReq, getSession() ) >= 1 )
         {
            raiseException( "Only one attachment with this attachment type is" +
                            " permitted on this record.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            return true ;
         }
      }


      return false;

   }






    // ADVHR00020276 BEGIN

    /**
     * Validates the filename of the attachment. <br><br>
     *
     * For Employee Photo, we only allow the extension of jpg, gif, bmp.
     *
     */
    protected void validateAttachmentName()
    {
        int liAttTyp = getOBJ_ATT_TYP();
        // for empl photo, the filename extension must be one of jpg, gif, bmp
        if (liAttTyp == OBJ_ATT_TYPE_EMPL_PHOTO)
        {
            String lsFilename = getOBJ_ATT_NM();
            if (!AMSStringUtil.strIsEmpty(lsFilename))
            {
                lsFilename = lsFilename.toLowerCase();
                if ( !(  lsFilename.endsWith(GRAPHIC_FILE_JPG)
                      || lsFilename.endsWith(GRAPHIC_FILE_GIF)
                      || lsFilename.endsWith(GRAPHIC_FILE_BMP)) )
                {
                    raiseException("%c:Q0143,v:"
                            +GRAPHIC_FILE_JPG+", "+GRAPHIC_FILE_GIF+", "+GRAPHIC_FILE_BMP
                            +"%");
                }
            }
        }
        return;
    } // end of validateAttachmentName()

    // ADVHR00020276 END


    /**
     * This method will return all Attachment Catalog(IN_OBJ_ATT_CTLG) entries for
     * attachments with a State of Active if the DataObject has
     * Reference Table or Alternate View attachments.
     *
     * @param foDataObj The data object being exported
     * @param foSession The Session object.
     * @return The list of attachments as IN_OBJ_ATT_CTLGImpl instances
     */
     public static Enumeration getObjectAttachments( AMSDataObject foDataObj, Session foSession )
     {
        String lsObjAttType = foDataObj.getObjectAttType() ;

        if ( lsObjAttType != null )
        {
           SearchRequest loSrchReq = new SearchRequest() ;

           if ( lsObjAttType.equals( AMSCommonConstants.OBJ_ATT_TYPE_REF_TBL ) )
           {
              String lsPriGrp = foDataObj.getData( AMSCommonConstants.ATTR_OBJ_ATT_PG_UNID ).getString() ;

              if ( lsPriGrp != null )
              {
                 loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_PG_UNID", lsPriGrp ) ;
                 loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_ST",
                    Integer.toString( AMSCommonConstants.OBJ_ATT_ST_ACTIVE ) ) ;
              } /* end if ( lsPriGrp != null ) */
              else
              {
                 return null ;
              } /* end else */
           } /* end if ( lsObjAttType.equals( AMSCommonConstants.OBJ_ATT_TYPE_REF_TBL ) ) */
           else if ( lsObjAttType.equals( AMSCommonConstants.OBJ_ATT_TYPE_ALT_VIEW ) )
           {
              String lsAltGrp = foDataObj.getData( AMSCommonConstants.ATTR_OBJ_ATT_ALT_UNID ).getString() ;

              if ( lsAltGrp != null )
              {
                 loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_ALT_UNID", lsAltGrp ) ;
                 loSrchReq.addParameter( "IN_OBJ_ATT_CTLG", "OBJ_ATT_ALT_ST",
                    Integer.toString( AMSCommonConstants.OBJ_ATT_ST_ACTIVE ) ) ;
              } /* end if ( lsAltGrp != null ) */
              else
              {
                 return null ;
              } /* end else */
           } /* end else if ( lsObjAttType.equals( AMSCommonConstants.OBJ_ATT_TYPE_ALT_VIEW ) ) */
           return IN_OBJ_ATT_CTLGImpl.getObjects( loSrchReq, foSession ) ;
        } /* end if ( lsObjAttType != null ) */
        return null ;
     } /* end getObjectAttachments() */


}
