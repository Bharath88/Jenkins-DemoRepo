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
import com.amsinc.gems.adv.common.AMSUniqNum;
import com.amsinc.gems.adv.common.AMSUniqNumException;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSSQLUtil;

/*
**  IN_OBJ_ATT_STOR*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_OBJ_ATT_STORImpl extends  IN_OBJ_ATT_STORBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   private int miMsgIndex = 0 ;
   String[] loECMParameters;
//{{COMP_CLASS_CTOR
public IN_OBJ_ATT_STORImpl (){
	super();
}

public IN_OBJ_ATT_STORImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static IN_OBJ_ATT_STORImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new IN_OBJ_ATT_STORImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
public void beforeInsert( DataObject obj, Response response )
{
   /*
    * Session property "OBJ_ATT_MSG_TXT" is being set from PLS side. 
    * For example : When we are uploading an attachment on a reference table through the 
    * Upload attachment page. This session property will not be set when attachment is 
    * being added through some offline process ( say Assemble document ).
    */
   String lsMsgTxt = getSession().getProperty( OBJ_ATT_MSG_TXT ) ;
   HashMap<String,String> loECMKeyValues = new HashMap<String,String>();

   if ( ( lsMsgTxt != null ) && ( !lsMsgTxt.equals( "" ) ) )
   {
      int             liPriGrpTot ;
      int             liSecGrpTot ;
      int             liNumberOfAttachments;
      int[]           loMsgIndex    = new int[1];
      String          lsDocType ;
      String          lsIsDocComp ;
      String          lsDocCompNm ;
      String          lsDocIntLnNoNm ;
      String          lsDocIntLnNo ;
      String          lsPriGrp ;
      String          lsSecGrp ;
      String          lsFileName ;
      String          lsDescription ;
      String          lsAttType ;
      AMSDataObject   loCtlgEntry ;
      AMSDocHeader    loDocHeader   = null ;
      AMSDocComponent loDocComp     = null ;
      // If attachment will be stored in ECM ,then set ECM Attachment Flag to true
      if(AMSStringUtil.strInsensitiveEqual(AMSParams.msUseECMForAttachments, "true"))
      {
         setECM_ATTACH_FL(true);
      }

      miMsgIndex = 2 ;

      lsDocType = getMsgValue( lsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsDocType.length() + 2 ;

      lsPriGrp = getMsgValue( lsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsPriGrp.length() + 2 ;

      /*
       * Call getParmVal method to get the File name String. It also returns the index where
       * the next string's size can be read
       */
      loMsgIndex[0] = miMsgIndex;
      lsFileName = getParmVal( lsMsgTxt, loMsgIndex);
      miMsgIndex = loMsgIndex[0];

      /*
       * Call getParmVal method to get the Description String. It also returns the index where
       * the next string's size can be read
       */
      lsDescription = getParmVal( lsMsgTxt, loMsgIndex);
      miMsgIndex = loMsgIndex[0];

      if ( lsDescription.length() == 0 )
      {
         lsDescription = null ;
      }

      lsAttType = getMsgValue( lsMsgTxt, miMsgIndex ) ;
      miMsgIndex += lsAttType.length() + 2 ;

      if(lsDocType.equals(OBJ_ATT_TYPE_REF_TBL))
      {
         loECMParameters = new String[3];
         loECMParameters[0] = "Y";
         String   lsTableName = getMsgValue( lsMsgTxt, miMsgIndex ) ;

         //Description string is populated in array in atleast one position even in case of no description too.
         int liNumOfDescStrings = 1;
         miMsgIndex += lsTableName.length() + 2 ;

         if(lsDescription != null)
         {
            liNumOfDescStrings = arraySizeForLargeParm(lsDescription);
         }

         int liNumOfFileNmParts = arraySizeForLargeParm(lsFileName);

         loECMParameters[1] = lsTableName;

         loECMParameters[2] = lsFileName;

         // get the total number of message text and subtract 6, gives the key
         // column value pair count.
         int liKeyIndexCount = Integer.parseInt( lsMsgTxt.substring(0, 2 ) ) - 4 -
               liNumOfDescStrings - liNumOfFileNmParts;

         /* Get the reference table data object */
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
            loECMKeyValues.put(lsKeyCol, lsKeyValue);

            loSearchReq.addParameter(lsTableName,lsKeyCol,lsKeyValue);
         }

         try
         {
            loRefTableObj =(AMSDataObject)AMSDataObject.getObjectByKey(
                           lsTableName, loSearchReq, getSession() );
           /*
             * If the ref table supports forms assembly and the attachment type is
             * WordML (rich text) then add the rich text file name from the
             * row
             */
            if ((loRefTableObj instanceof FormsAssemblyAttachment)  &&
                 (lsAttType.equals(
                  String.valueOf(AMSCommonConstants.OBJ_ATT_TYPE_WORDML) )  ) )
            {
               FormsBaseUtil.setAttachInfo(loRefTableObj, lsFileName, lsDescription,
                                       new VSDate(), getSession().getUserID());
            }
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
                            AMSMsgUtil.SEVERITY_LEVEL_SEVERE ) ;
            return ;
         }
         else
         {
             /* Check whether the user has update permission on reference data row */
             loRefTableObj.checkUpdateSecurity() ;
         }

         if (lsPriGrp == null || lsPriGrp.trim().length() == 0)
         {
            lsPriGrp = IN_OBJ_ATT_CTLGImpl.getNewObjAttPrimGrpID( getSession() ) ;
         }

         loCtlgEntry = createCatalogEntry( lsPriGrp, null, lsFileName,
                                    lsDescription, lsAttType, OBJ_ATT_ST_ACTIVE,
                                    null, null) ;

         if(loCtlgEntry == null)
         {
            response.reject();
            return;
         }

         this.setOBJ_ATT_UNID( loCtlgEntry.getData( "OBJ_ATT_UNID" ).getlong() ) ;

         /* Try to set the primary group ID on the reference table data object if one does not
          * exist and also increment the number of attachment count by 1.
          */
        if ( loRefTableObj.getData( ATTR_OBJ_ATT_PG_UNID ).getObject() == null )
         {
            loRefTableObj.getData( ATTR_OBJ_ATT_PG_UNID ).setValue(lsPriGrp) ;
         }//if (loRefTableObj != null)

        liNumberOfAttachments = loRefTableObj.getData( ATTR_OBJ_ATT_PG_TOT ).getint() ;
        loRefTableObj.getData( ATTR_OBJ_ATT_PG_TOT ).setint( liNumberOfAttachments + 1 ) ;
        loRefTableObj.save() ;

         getSession().setProperty( OBJ_ATT_MSG_TXT, lsPriGrp ) ;
      }
      else if(lsDocType.equals( OBJ_ATT_TYPE_DOC_VIEW ))
      {
         loECMParameters = new String[12];
         loECMParameters [0] = "N";
         String lsCompDesc = null;
         String lsCompName = null;

         getSession().setProperty( OBJ_ATT_MSG_TXT, "" ) ;

         lsSecGrp = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsSecGrp.length() + 2 ;
         if ( lsSecGrp.length() == 0 )
         {
            lsSecGrp = null ;
         }

         loDocHeader = getDocHeader( lsMsgTxt ) ;
         if ( loDocHeader == null )
         {
            raiseException( "Unable to find parent document.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( loDocHeader == null ) */

         lsCompDesc = loDocHeader.getComponentDescription();
         lsCompName = loDocHeader.getComponentName();

         loECMParameters[6] = lsCompName;

         if ( loDocHeader.getData( ATTR_DOC_PHASE_CD ).getint() != DOC_PHASE_DRAFT )
         {
            raiseException( "Object attachments can only be added to documents in Draft phase.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( loDocHeader.getData( ATTR_DOC_PHASE_CD ).getint() != DOC_PHASE_DRAFT ) */

         if ( !loDocHeader.objectAttachmentsAuthorized() )
         {
            raiseException( "Not authorized to create object attachment.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( !loDocHeader.objectAttachmentsAuthorized() ) */

   //      if ( !loDocHeader.processDocumentAction( DOC_ACTN_EDIT ) )
   //      {
   //         raiseException( "Not authorized to create object attachment.",
   //                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
   //         response.reject() ;
   //         return ;
   //      } /* end if ( !loDocHeader.processDocumentAction( DOC_ACTN_EDIT ) ) */

         lsIsDocComp = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsIsDocComp.length() + 2 ;

         lsDocCompNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocCompNm.length() + 2 ;

         lsDocIntLnNoNm = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocIntLnNoNm.length() + 2 ;

         lsDocIntLnNo = getMsgValue( lsMsgTxt, miMsgIndex ) ;
         miMsgIndex += lsDocIntLnNo.length() + 2 ;

         loECMParameters[7] = lsIsDocComp;
         loECMParameters[8] = lsDocCompNm;
         loECMParameters[9] = lsDocIntLnNoNm;
         loECMParameters[10] = lsDocIntLnNo;
         loECMParameters[11] = lsFileName;

         if (moAMSLog.isDebugEnabled())
         {
            moAMSLog.debug("Attachment File Name: " + lsFileName);
         }

         if ( ( lsPriGrp == null ) || ( lsPriGrp.trim().length() <= 0 ) )
         {
            raiseException( "No object attachments group defined.",
                            AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
            response.reject() ;
            return ;
         } /* end if ( ( lsPriGrp == null ) || ( lsPriGrp.trim().length() <= 0 ) ) */

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

            lsCompDesc = loDocComp.getComponentDescription();
            lsCompName = loDocComp.getComponentName();
         } /* end if ( lsIsDocComp.equals( "Y" ) ) */
         else
         {
            liSecGrpTot = loDocHeader.getData( ATTR_OBJ_ATT_SG_TOT ).getint() ;
            loDocHeader.getData( ATTR_OBJ_ATT_SG_TOT ).setint( liSecGrpTot + 1 ) ;
         } /* end else */

         liPriGrpTot = loDocHeader.getData( ATTR_OBJ_ATT_PG_TOT ).getint() ;
         loDocHeader.getData( ATTR_OBJ_ATT_PG_TOT ).setint( liPriGrpTot + 1 ) ;
         loDocHeader.save() ;

         loCtlgEntry = createCatalogEntry( lsPriGrp, lsSecGrp, lsFileName,
                                       lsDescription, lsAttType, OBJ_ATT_ST_NEW,
                                       lsCompName, lsCompDesc ) ;

         if(loCtlgEntry == null)
         {
            response.reject();
            return;
         }

         long llAttId = loCtlgEntry.getData( "OBJ_ATT_UNID" ).getlong() ;
         this.setOBJ_ATT_UNID( llAttId ) ;
         // create a document attachment reference entry in IN_OBJ_ATT_DOC_REF
         createAttDocRefEntry ( llAttId, loDocHeader ) ;

         /*
          * If the document supports forms assembly and the attachment type is
          * WordML (rich text) then add the rich text file name from the
          * row
          */
         if ((loDocComp instanceof FormsAssemblyAttachment)  &&
              (lsAttType.equals(
               String.valueOf(AMSCommonConstants.OBJ_ATT_TYPE_WORDML) )  ) )
         {
            FormsBaseUtil.setAttachInfo(loDocComp, lsFileName, lsDescription,
                                       new VSDate(), getSession().getUserID());
         }

      } /* end of if(lsDocType.equals( OBJ_ATT_TYPE_DOC_VIEW )) */
	  
      /*
       * Save Component information to ECM message property in session
       * This property will be read by AdvDocumentumHandler during Attachment processing
       */
      String lsMessageText  = createMessageText( loECMParameters ) ;
      getSession().setProperty(ECM_MSG_TXT, lsMessageText);

      if (moAMSLog.isDebugEnabled())
      {
         moAMSLog.debug("Message Text:  " + lsMessageText);
      }

      getSession().setPropertyAsObject(ECM_TABLE_KEYS, loECMKeyValues);

   } /* end if ( ( lsMsgTxt != null ) && ( !lsMsgTxt.equals( "" ) ) ) */

}
//END_COMP_EVENT_beforeInsert}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

   private IN_OBJ_ATT_CTLGImpl createCatalogEntry( String fsPriGrp, String fsSecGrp,
                       String fsFileName, String fsDscr, String fsAttType, int fiAttStatus,
                       String fsCompName, String foCompDesc)
   {
      IN_OBJ_ATT_CTLGImpl loCtlgEntry ;

      loCtlgEntry = IN_OBJ_ATT_CTLGImpl.getNewObject( session, true ) ;
      loCtlgEntry.getData( "OBJ_ATT_PG_UNID" ).setString( fsPriGrp ) ;
      if ( ( fsSecGrp != null ) && ( fsSecGrp.trim().length() > 0 ) )
      {
         loCtlgEntry.getData( "OBJ_ATT_SG_UNID" ).setString( fsSecGrp ) ;
      } /* end if ( ( fsSecGrp != null ) && ( fsSecGrp.trim().length() > 0 ) ) */
      if ( ( fsFileName != null ) && ( fsFileName.trim().length() > 0 ) )
      {
         loCtlgEntry.setOBJ_ATT_NM( fsFileName ) ;
      } /* end if ( ( fsFileName != null ) && ( fsFileName.trim().length() > 0 ) ) */
      if ( ( fsDscr != null ) && ( fsDscr.trim().length() > 0 ) )
      {
         loCtlgEntry.setOBJ_ATT_DSCR( fsDscr ) ;
      } /* end if ( ( fsDscr != null ) && ( fsDscr.trim().length() > 0 ) ) */
      if ( ( fsAttType != null ) && ( fsAttType.trim().length() > 0 ) )
      {
         loCtlgEntry.getData( "OBJ_ATT_TYP" ).setString( fsAttType ) ;
      } /* end if ( ( fsAttType != null ) && ( fsAttType.trim().length() > 0 ) ) */

      loCtlgEntry.getData( ATTR_OBJ_ATT_ST ).setint( fiAttStatus  ) ;

      boolean lboolrejected = loCtlgEntry.doCommonAtchTypeCheck();
      if(lboolrejected)
      {
         return null;
      }

      long llMaxSeqNum = 1;

      SearchRequest loSearchReq = new SearchRequest() ;

      loSearchReq.addParameter( "IN_OBJ_ATT_CTLG",
                                "OBJ_ATT_PG_UNID",
                                fsPriGrp ) ;
      Vector lvSortAttrs = new Vector() ;
      lvSortAttrs.addElement( "OBJ_ATT_SEQ_NO" ) ;

      try
      {
         Enumeration loEnum = getObjectsSorted( "IN_OBJ_ATT_CTLG", loSearchReq, getSession(), lvSortAttrs,
                                             AMSDataObject.SORT_DSC ) ;

         if(loEnum.hasMoreElements())
         {
            IN_OBJ_ATT_CTLGImpl loCtlg = (IN_OBJ_ATT_CTLGImpl)loEnum.nextElement();
            llMaxSeqNum = loCtlg.getOBJ_ATT_SEQ_NO() + 1;
         }
      }
      catch( Exception foExp )
      {
         raiseException( "%c:Q0038%" ) ;
         return null;
      } /* end catch( Exception foExp ) */

      loCtlgEntry.setOBJ_ATT_SEQ_NO(llMaxSeqNum) ;
      loCtlgEntry.setOBJ_ATT_COMP_DESC(foCompDesc);
      loCtlgEntry.setOBJ_ATT_COMP_NM(fsCompName);
      loCtlgEntry.save() ;

      return loCtlgEntry ;
   } /* end createCatalogEntry() */

   private IN_OBJ_ATT_DOC_REFImpl createAttDocRefEntry( long flAttId, AMSDocHeader foDocHdr )
   {
      IN_OBJ_ATT_DOC_REFImpl loAttDocRefEntry ;

      if( new Long(flAttId) == null || foDocHdr == null )
      {
         raiseException( "Error in adding attachment document reference entry. Param is null.",
                         AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
      }
      else
      {
         loAttDocRefEntry = IN_OBJ_ATT_DOC_REFImpl.getNewObject( session, true ) ;

         loAttDocRefEntry.getData( "OBJ_ATT_UNID" ).setlong( flAttId ) ;
         loAttDocRefEntry.getData( "DOC_CAT" ).setData( foDocHdr.getData("DOC_CAT")) ;
         loAttDocRefEntry.getData( "DOC_TYP" ).setData( foDocHdr.getData("DOC_TYP")) ;
         loAttDocRefEntry.getData( "DOC_CD" ).setData( foDocHdr.getData("DOC_CD")) ;
         loAttDocRefEntry.getData( "DOC_DEPT_CD" ).setData( foDocHdr.getData("DOC_DEPT_CD")) ;
         loAttDocRefEntry.getData( "DOC_ID" ).setData( foDocHdr.getData("DOC_ID")) ;
         loAttDocRefEntry.getData( "DOC_VERS_NO" ).setData( foDocHdr.getData("DOC_VERS_NO")) ;

         loAttDocRefEntry.save() ;
            return loAttDocRefEntry ;
      }
      return null ;
   } /* end createAttDocRefEntry() */

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
      loECMParameters[1] = lsDocCode;
      loECMParameters[2] = lsDocDept;
      loECMParameters[3] = lsDocID;
      loECMParameters[4] = lsDocVersNo;
      loECMParameters[5] = lsDataObjName;

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
    * This method checks the size of the new attachment file.
    * If the maximum file size has been set and the file size
    * exceeds that size, then it raises an error.
    */
   protected void checkAttachmentSize()
   {
      if ( AMSParams.mlMaxAttachmentSize > 0 && getData( "OBJ_ATT_DATA" ).getbytes() != null)
      {
         long llFileSize = getData( "OBJ_ATT_DATA" ).getbytes().length ;

         if ( llFileSize > AMSParams.mlMaxAttachmentSize )
         {
         raiseException( "Maximum attachment file size ("
               + AMSParams.msMaxAttachmentSizeKB
               + "KB) exceed", AMSMsgUtil.SEVERITY_LEVEL_ERROR ) ;
         } /* end if ( llFileSize > AMSParams.mlMaxAttachmentSize ) */
      } /* end if ( AMSParams.mlMaxAttachmentSize > 0 ) */
   } /* end checkAttachmentSize() */

      /**
      * This method creates the message text that is passed to the server
      * An array of strings is passed to the method, based on which the
      * message is created
      *
      * @param String [] Array of parameters
      * @return String The message text
      */
      public String createMessageText(String [] lsParams)
      {
         StringBuffer loDI_MSG_TXT = new StringBuffer();

         // Add the Number of paramters
         if (lsParams.length > 9)
         {
            loDI_MSG_TXT.append(lsParams.length);
         }
         else
         {
            loDI_MSG_TXT.append("0" + (lsParams.length));
         }

         // Now add the rest of the paramters
         for(int liIndex=0;liIndex < lsParams.length;liIndex++)
         {
            addMsgValuePair(loDI_MSG_TXT, lsParams[liIndex]);
         }

         // Return the message text
         return loDI_MSG_TXT.toString();
   }
}
