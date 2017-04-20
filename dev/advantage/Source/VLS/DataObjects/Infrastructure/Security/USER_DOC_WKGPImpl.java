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

/*
**  USER_DOC_WKGP*/

//{{COMPONENT_RULES_CLASS_DECL
public class USER_DOC_WKGPImpl extends  USER_DOC_WKGPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   //BGn ADVHR00037535
   private boolean mboolUserAdded = false;
   //END ADVHR00037535
//{{COMP_CLASS_CTOR
public USER_DOC_WKGPImpl (){
	super();
}

public USER_DOC_WKGPImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static USER_DOC_WKGPImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new USER_DOC_WKGPImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * This component does not have an associated catalog,
    * so disable mediation.
    */
   public void myInitializations()
   {
      mboolMediateDocComponent = false ;
   } /* end myInitializations */

   /**
    * This method returns the context for the document component.
    *
    * @return The context
    */
   public String getContext()
   {
      if ( msContext == null )
      {
          msContext = createContext( "WKGP" ) ;
      } /* end if ( msContext == null ) */
      return msContext ;
   } /* end getContext() */

   /**
    * If the workgroup is marked as default, then this method
    * checks if any other workgroup lines are also marked as
    * default.  If so, then it raises an exception.
    */
   protected void verifyUniqueDfltWkgp()
   {
      SearchRequest loSrchReq ;
      Session       loSession = getSession() ;
      int           liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_CD, getDOC_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_DEPT_CD, getDOC_DEPT_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_ID, getDOC_ID() ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_VERS_NO, String.valueOf( getDOC_VERS_NO() ) ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", "DFLT_WKGP_FL", "true" ) ;
      liObjCnt = USER_DOC_WKGPImpl.getObjectCount( loSrchReq, loSession ) ;

      if ( liObjCnt > 1 )
      {
         raiseException( "%c:Q0017%", null, "DFLT_WKGP_FL" ) ;
      } /* end if ( liObjCnt > 1 ) */
   } /* end verifyUniqueDfltWkgp() */

   /**
    * This method checks if any other workgroup lines also use
    * the same workgroup.  If so, then it raises an exception.
    */
   protected void verifyUniqueWorkgroup()
   {
      SearchRequest loSrchReq ;
      Session       loSession = getSession() ;
      int           liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_CD, getDOC_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_DEPT_CD, getDOC_DEPT_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_ID, getDOC_ID() ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", ATTR_DOC_VERS_NO, String.valueOf( getDOC_VERS_NO() ) ) ;
      loSrchReq.addParameter( "USER_DOC_WKGP", "WKGP_UNID", String.valueOf( getWKGP_UNID() ) ) ;
      liObjCnt = USER_DOC_WKGPImpl.getObjectCount( loSrchReq, loSession ) ;

      if ( liObjCnt > 1 )
      {
         raiseException( "%c:Q0033,p:WKGP_NM%", null, "WKGP_UNID" ) ;
      } /* end if ( liObjCnt > 1 ) */
   } /* end verifyUniqueWorkgroup() */

   /**
    * Based on the COMP_ACTN_CD, this method performs the actual
    * update logic for the document component.  Namely, it updates
    * the IN_USER_WKGP and IN_USER_PROFILE tables.
    */
   protected void maintainWorkgroup()
   {
      USER_DOC_HDRImpl loDocHdr = (USER_DOC_HDRImpl)getDocumentHeader() ;
      String           lsUserID = loDocHdr.getUSER_ID() ;

      if ( lsUserID != null )
      {
         int liAction = getCOMP_ACTN_CD() ;

         //BGN ADVHR00037535
         if ( liAction == CVL_DOC_COMP_ACTNImpl.ADD && !mboolUserAdded )
         {
            processAdd( lsUserID ) ;
         } /* end if ( liAction == CVL_DOC_COMP_ACTNImpl.ADD ) */
         else if ( liAction == CVL_DOC_COMP_ACTNImpl.UPDATE )
         {
            if ( loDocHdr.getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.ADD && !mboolUserAdded )
            {
               /*
                * If the header action is add, then treat the
                * update line action as an Add also.
                */
               processAdd( lsUserID ) ;
            } /* end if ( loDocHdr.getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.ADD ) */
            //END ADVHR00037535
            else
            {
               processUpdate( lsUserID ) ;
            } /* end else */
         } /* end else if ( liAction == CVL_DOC_COMP_ACTNImpl.UPDATE ) */
         else if ( liAction == CVL_DOC_COMP_ACTNImpl.DELETE )
         {
            /*
             * If the header action is add, then ignore the
             * delete line action.  Otherwise, process the delete.
             */
            if ( loDocHdr.getCOMP_ACTN_CD() != CVL_DOC_COMP_ACTNImpl.ADD )
            {
               processDelete( lsUserID ) ;
            } /* end if ( loDocHdr.getCOMP_ACTN_CD() != CVL_DOC_COMP_ACTNImpl.ADD ) */
         } /* end else if ( liAction == CVL_DOC_COMP_ACTNImpl.DELETE ) */
      } /* end if ( lsUserID != null ) */
   } /* end maintainWorkgroup() */

   /**
    * This method performs the logic of adding the new user
    * workgroup link to the system.  It adds the IN_USER_WKGP
    * record for the user/role combination and populates it
    * with the values from the document line.  It also
    * sets the user's default workgroup id on the IN_USER_PROFILE
    * record.
    *
    * @param fsUserID The user id
    */
   private void processAdd( String fsUserID )
   {
      //BGN ADVHR00037535
      mboolUserAdded = true;
      //END ADVHR00037535
      IN_USER_WKGPImpl loUserWkgp ;
      SearchRequest    loSrchReq ;
      Session          loSession  = getSession() ;
      int              liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_WKGP", "USER_ID", fsUserID ) ;
      loSrchReq.addParameter( "IN_USER_WKGP", "WKGP_UNID", String.valueOf( getWKGP_UNID() ) ) ;
      liObjCnt = IN_USER_WKGPImpl.getObjectCount( loSrchReq, loSession ) ;
      if ( liObjCnt > 0 )
      {
         raiseException( "%c:Q0028,v:" + fsUserID + ",p:WKGP_NM%", null, "WKGP_UNID" ) ;
      } /* end if ( liObjCnt > 0 ) */
      else
      {
         loUserWkgp = IN_USER_WKGPImpl.getNewObject( loSession, true ) ;
         loUserWkgp.copyCorresponding( this, null, true ) ;
         loUserWkgp.setUSER_ID( fsUserID ) ;
         loUserWkgp.save() ;
      } /* end else */

      if ( getDFLT_WKGP_FL() )
      {
         IN_USER_PROFILEImpl loUserProfile ;

         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", fsUserID ) ;
         loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
         if ( loUserProfile == null )
         {
            loUserProfile = IN_USER_PROFILEImpl.getNewObject( loSession, true ) ;
            loUserProfile.setUSER_ID( fsUserID ) ;
         } /* end if ( loUserProfile == null ) */
         loUserProfile.setDFLT_WKGP( getWKGP_UNID() ) ;
         loUserProfile.save() ;
      } /* end if ( getDFLT_WKGP_FL() ) */
   } /* end processAdd() */

   /**
    * This method performs the logic of updating the user role
    * link in the system.  It updates the IN_USER_WKGP
    * record for the user/role combination and populates it
    * with the values from the document line.  It also
    * sets the user's default workgroup id on the IN_USER_PROFILE
    * record.
    *
    * @param fsUserID The user id
    */
   private void processUpdate( String fsUserID )
   {
      IN_USER_WKGPImpl    loUserWkgp ;
      IN_USER_PROFILEImpl loUserProfile ;
      SearchRequest       loSrchReq ;
      Session             loSession  = getSession() ;
      VSDate              loUserWkgpDt ;
      VSDate              loDocDt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_WKGP", "USER_ID", fsUserID ) ;
      loSrchReq.addParameter( "IN_USER_WKGP", "WKGP_UNID", String.valueOf( getWKGP_UNID() ) ) ;
      loUserWkgp = (IN_USER_WKGPImpl)IN_USER_WKGPImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserWkgp == null )
      {
         raiseException( "%c:Q0029,v:" + fsUserID + ",p:WKGP_NM%", null, "WKGP_UNID" ) ;
      } /* end if ( loUserWkgp == null ) */
      else
      {
         loDocDt = getDOC_CREA_DT() ;
         loUserWkgpDt = loUserWkgp.getLAST_UPDATE_DT() ;
         if ( ( loUserWkgpDt != null ) && ( loDocDt != null ) )
         {
            if ( loUserWkgpDt.after( loDocDt ) )
            {
               raiseException( "%c:Q0030,v:" + fsUserID + ",p:WKGP_NM%", null, "WKGP_UNID" ) ;
            } /* end if ( loUserWkgpDt.after( loDocDt ) ) */
         } /* end if ( ( loUserWkgpDt != null ) && ( loDocDt != null ) ) */
         loUserWkgp.copyCorresponding( this, null, true ) ;
         loUserWkgp.save() ;
      } /* end else */

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", fsUserID ) ;
      loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserProfile == null )
      {
         raiseException( "%c:Q0023,p:USER_ID%", null, "USER_ID" ) ;
         loUserProfile = IN_USER_PROFILEImpl.getNewObject( loSession, true ) ;
         loUserProfile.setUSER_ID( fsUserID ) ;
         if ( getDFLT_WKGP_FL() )
         {
            loUserProfile.setDFLT_WKGP( getWKGP_UNID() ) ;
         } /* end if ( getDFLT_WKGP_FL() ) */
      } /* end if ( loUserProfile == null ) */
      else
      {
         if ( getDFLT_WKGP_FL() )
         {
            loUserProfile.setDFLT_WKGP( getWKGP_UNID() ) ;
         } /* end if ( getDFLT_WKGP_FL() ) */
         else
         {
            if ( loUserProfile.getData( "DFLT_WKGP" ).getValue() != null )
            {
               long llDfltWkgp = loUserProfile.getDFLT_WKGP() ;

               if ( llDfltWkgp == getWKGP_UNID() )
               {
                  loUserProfile.getData( "DFLT_WKGP" ).setValue( null ) ;
               } /* end if ( llDfltWkgp == getWKGP_UNID() ) */
            } /* end if ( loUserProfile.getData( "DFLT_WKGP" ).getValue() != null ) */
         } /* end else */
      } /* end else */
      loUserProfile.save() ;
   } /* end processUpdate() */


   /**
    * This method performs the logic of deleting the user role
    * link from the system.  It deletes the IN_USER_WKGP
    * record for the user/role combination.  It also sets the
    * user's default workgroup id to null on the IN_USER_PROFILE
    * record if the workgroup is marked as the user's default.
    *
    * @param fsUserID The user id
    */
   private void processDelete( String fsUserID )
   {
      IN_USER_WKGPImpl    loUserWkgp ;
      IN_USER_PROFILEImpl loUserProfile ;
      SearchRequest       loSrchReq ;
      Session             loSession  = getSession() ;
      VSDate              loUserWkgpDt ;
      VSDate              loDocDt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_WKGP", "USER_ID", fsUserID ) ;
      loSrchReq.addParameter( "IN_USER_WKGP", "WKGP_UNID", String.valueOf( getWKGP_UNID() ) ) ;
      loUserWkgp = (IN_USER_WKGPImpl)IN_USER_WKGPImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserWkgp == null )
      {
         raiseException( "%c:Q0029,v:" + fsUserID + ",p:WKGP_NM%", null, "WKGP_UNID" ) ;
      } /* end if ( loUserWkgp == null ) */
      else
      {
         loDocDt = getDOC_CREA_DT() ;
         loUserWkgpDt = loUserWkgp.getLAST_UPDATE_DT() ;
         if ( ( loUserWkgpDt != null ) && ( loDocDt != null ) )
         {
            if ( loUserWkgpDt.after( loDocDt ) )
            {
               raiseException( "%c:Q0030,v:" + fsUserID + ",p:WKGP_NM%", null, "WKGP_UNID" ) ;
            } /* end if ( loUserWkgpDt.after( loDocDt ) ) */
         } /* end if ( ( loUserWkgpDt != null ) && ( loDocDt != null ) ) */


         //Set the Default Workgroup to Null in IN_USER_PROFILE before deleting IN_USER_WKGP record.
         //Else the Parent Child relationship between the IN_USER_WKGP and IN_USER_PROFILE will prevent
         //deletion of IN_USER_WKGP record.
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", fsUserID ) ;
         loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
         if ( loUserProfile != null )
         {
            if ( loUserProfile.getData( "DFLT_WKGP" ).getValue() != null )
            {
               long llDfltWkgp = loUserProfile.getDFLT_WKGP() ;

               if ( llDfltWkgp == getWKGP_UNID() )
               {
                  loUserProfile.getData( "DFLT_WKGP" ).setValue( null ) ;
                  loUserProfile.save() ;
               } /* end if ( llDfltWkgp == getWKGP_UNID() ) */
            } /* end if ( loUserProfile.getData( "DFLT_WKGP" ).getValue() != null ) */
         } /* end if ( loUserProfile != null ) */

         loUserWkgp.setDelete() ;
         loUserWkgp.save() ;
      } /* end else */


   } /* end processDelete() */

}

