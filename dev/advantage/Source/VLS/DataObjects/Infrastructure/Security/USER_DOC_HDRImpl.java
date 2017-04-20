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
import com.ams.csf.base.*;
import com.ams.csf.auth.*;
import com.ams.csf.common.*;
/*
**  USER_DOC_HDR*/

//{{COMPONENT_RULES_CLASS_DECL
public class USER_DOC_HDRImpl extends  USER_DOC_HDRBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /** The attributes to exclude when copying to and from R_SC_USER_INFO */
   private static Vector USER_INFO_EXCLUDE = new Vector( 4 ) ;
   /** The attribute to to sort by when querying from R_SC_USER_ROLE_LNK */
   private static Vector ROLE_LNK_SORT     = new Vector( 1 ) ;

   /** This is the user's new password that will be e-mailed after commit */
   private String msEmailPassword = null ;

   /* Variable to store Old Password for an existing User */
   private String msOldPswd = null;

   //BGN ADVHR00037535
   private boolean mboolUserAdded = false;
   //END ADVHR00037535

   /**
     * User Information record inserted or updated by the document.
     */
      private R_SC_USER_INFOImpl moUserInfoRec = null;

   static
   {
      USER_INFO_EXCLUDE.add( ATTR_OV_ERR_CD ) ;
      USER_INFO_EXCLUDE.add( "PSWD_TXT" ) ;
      USER_INFO_EXCLUDE.add( "EMAIL_PSWD_TXT" ) ;
      ROLE_LNK_SORT.add( "PREC_NO" ) ;
   } /* end static */

//{{COMP_CLASS_CTOR
public USER_DOC_HDRImpl (){
	super();
}

public USER_DOC_HDRImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   encryptPasswords() ;
}

//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
   encryptPasswords() ;
   //BGN ADVHR00025681
   checkNoSecurityRolesAssigned();

   //END ADVHR00025681



}
//END_COMP_EVENT_beforeUpdate}}

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static USER_DOC_HDRImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new USER_DOC_HDRImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * Disable modifications and cancellations.  Also,
    * add the HOME_ organizational elements to the security
    * org prefix list.
    */
   public void myInitializations()
   {
      mboolIsFinalDocModValid = false ;
      mboolIsFinalDocCancelValid = false ;
      setSecurityOrgPrefixes( "HOME" ) ;
      setExcludeAttribute( DOC_ACTN_COPY, "PSWD_TXT" ) ;
      setExcludeAttribute( DOC_ACTN_COPY, "EMAIL_PSWD_TXT" ) ;
      setExcludeAttribute( DOC_ACTN_PRINT, "PSWD_TXT" ) ;
      setExcludeAttribute( DOC_ACTN_PRINT, "EMAIL_PSWD_TXT" ) ;
      setDocEvent(DOC_ACTN_AFTER_SUBMIT);
   } /* end myInitializations */

   /**
    * Based on the COMP_ACTN_CD, this method performs the actual
    * update logic for the document.  Namely, it updates the
    * R_SC_USER_DIR_INFO, R_SC_USER_INFO, and IN_USER_PROFILE tables.
    */
   protected void maintainUserInfo()
   {
      String lsEncPswd  = null ;
      String lsPassword = null ;
      int    liAction   = getCOMP_ACTN_CD() ;

      if ( ( ( getDOC_ACTN_CD() == DOC_ACTN_SUBMIT ) ||
             ( getDOC_ACTN_CD() == DOC_ACTN_VALIDATE ) ) &&
               getGEN_NEW_PSWD_FL())
      {
         lsPassword = AMSSecurity.generateNewPassword() ;
      } /* end if ( ( ( getDOC_ACTN_CD() == DOC_ACTN_SUBMIT ) || */
      else
      {
         lsEncPswd = getPSWD_TXT() ;
         if ( lsEncPswd != null )
         {
            lsPassword = decryptPasswordString( getPSWD_TXT() ) ;
         } /* end if ( lsEncPswd != null ) */
      } /* end else */

      if ( ( getDOC_ACTN_CD() == DOC_ACTN_SUBMIT ) && ( getRESET_PSWD_FL() ) )
      {
         storeEmailPswdForNtfy( lsPassword ) ;
      } /* end if ( ( getDOC_ACTN_CD() == DOC_ACTN_SUBMIT ) && ( getRESET_PSWD_FL() ) ) */

      //BGN ADVHR00037535
      if ( liAction == CVL_DOC_COMP_ACTNImpl.ADD && !mboolUserAdded )
      {
         processAdd( lsPassword ) ;
      } /* end if ( liAction == CVL_DOC_COMP_ACTNImpl.ADD ) */
      //END ADVHR00037535
      else if ( liAction == CVL_DOC_COMP_ACTNImpl.UPDATE )
      {
         processUpdate( lsPassword ) ;
      } /* end else if ( liAction == CVL_DOC_COMP_ACTNImpl.UPDATE ) */
   } /* end maintainUserInfo() */

   /**
    * Stores the user's e-mail password so that it can
    * be e-mailed to the user after commit.
    *
    * @param fsPassword The user's unencrypted password
    */
   private void storeEmailPswdForNtfy( String fsPassword )
   {
      msEmailPassword = fsPassword ;
   } /* end storeEmailPswdForNtfy() */

   /**
    * This method performs the logic of adding the new user
    * to the system.  It adds the R_SC_USER_DIR_INFO,
    * R_SC_USER_INFO, and IN_USER_PROFILE records for the
    * user and populates them with the values from the
    * document.
    *
    * @param fsPassword The new user's password
    */
   private void processAdd( String fsPassword )
   {
      String lsUserID = getUSER_ID() ;
      //BGN ADVHR00037535
      mboolUserAdded = true;
      //END ADVHR00037535
      if ( ( lsUserID != null ) && ( lsUserID.trim().length() > 0 ) )
      {
         R_SC_USER_DIR_INFOImpl loUserDirInfo ;
         R_SC_USER_INFOImpl     loUserInfo ;
         IN_USER_PROFILEImpl    loUserProfile ;
         SearchRequest          loSrchReq ;
         Session                loSession     = getSession() ;
         int                    liObjCnt = 0 ;

         /* Process the R_SC_USER_DIR_INFO record */
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "R_SC_USER_DIR_INFO", "USER_ID", lsUserID ) ;

         if ( AMSParams.miDirectoryInfoType != AMSParams.TYPE_LDAP )
         {
            liObjCnt = R_SC_USER_DIR_INFOImpl.getObjectCount( loSrchReq, loSession ) ;
         }


         if ( liObjCnt > 0 )
         {
            raiseException( "%c:Q0011,p:USER_ID%", null, "USER_ID" ) ;
         } /* end if ( liObjCnt > 0 ) */
         else
         {
            loUserDirInfo = R_SC_USER_DIR_INFOImpl.getNewObject( loSession, true ) ;
            loUserDirInfo.copyCorresponding( this, null, true ) ;
            loUserDirInfo.save() ;
         } /* end else */

         /* Process the R_SC_USER_INFO record */
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "R_SC_USER_INFO", "USER_ID", lsUserID ) ;
         liObjCnt = R_SC_USER_INFOImpl.getObjectCount( loSrchReq, loSession ) ;

         if ( liObjCnt > 0 )
         {
            raiseException( "%c:Q0012,p:USER_ID%", null, "USER_ID" ) ;
         } /* end if ( liObjCnt > 0 ) */
         else
         {
            String lsEmailPassword = getEMAIL_PSWD_TXT() ;
            loUserInfo = R_SC_USER_INFOImpl.getNewObject( loSession, true ) ;
            loUserInfo.copyCorresponding( this, USER_INFO_EXCLUDE, true ) ;

            loUserInfo.setEXT_DIR_ID(getEXT_DIR_ID());

            //Set the EXPIRE_NEW_PSWD_FL flag of R_SC_USER_INFO object.
            loUserInfo.setEXPIRE_NEW_PSWD_FL(getEXPIRE_PSWD_FL());

          if ( getEXPIRE_PSWD_FL() || AMSSecurity.getExpireNewPassword() )
          {
             /*
              * Prevent triggering of new password expiry email notification logic from R_SC_USER_INFO.
              * Instead the logic will be triggered in this class later on when UDOC
              * goes to Final from Draft or Pending phase(multiple emails should not be sent).
              */
               loUserInfo.setDONT_EXPIRE_PSWD_FL(true);
            }
            if ( fsPassword != null )
            {
               loUserInfo.setPSWD_TXT( fsPassword ) ;
               setPasswordChangeDate( loUserInfo ) ;
            } /* end if ( fsPassword != null ) */

            if ( lsEmailPassword != null )
            {
               loUserInfo.setEMAIL_PSWD_TXT(
                   decryptPasswordString( lsEmailPassword ) ) ;
            } /* end if ( lsEmailPassword != null ) */

            /*
             * The override error code for the user is named
             * USER_OV_ERR_CD since OV_ERR_CD is already
             * reserved by the document model.
             */
            loUserInfo.getData( ATTR_OV_ERR_CD ).setValue( getData( "USER_OV_ERR_CD" ).getValue() ) ;
            loUserInfo.save() ;
            moUserInfoRec = loUserInfo;
         } /* end else */

         /* Process the IN_USER_PROFILE record */
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", lsUserID ) ;
         loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
         if ( loUserProfile != null )
         {
            /*
             * R_SC_USER_DIR_INFO will create an IN_USER_PROFILE record
             * after insert.  Therefore, we check if the retrieved
             * user profile record is being inserted.  If so, then we
             * do not raise the error.  Otherwise, the record previously
             * existed, so an error condition exists.
             */
            if ( !loUserProfile.isInserted() )
            {
               raiseException( "%c:Q0013,p:USER_ID%", null, "USER_ID" ) ;
            } /* end if ( !loUserProfile.isInserted() ) */
            else
            {
               loUserProfile.copyCorresponding( this, null, true ) ;
               loUserProfile.save() ;
            } /* end else */
         } /* end if ( loUserProfile != null ) */
         else
         {
            loUserProfile = IN_USER_PROFILEImpl.getNewObject( loSession, true ) ;
            loUserProfile.copyCorresponding( this, null, true ) ;
            loUserProfile.save() ;
         } /* end else */
      } /* end if ( ( lsUserID != null ) && ( lsUserID.trim().length() > 0 ) ) */
   } /* end processAdd() */

   /**
    * This method performs the logic of updating the exising
    * user's information in the system.  It updates the
    * R_SC_USER_DIR_INFO, R_SC_USER_INFO, and IN_USER_PROFILE
    * records for the user and populates them with the values
    * from the document.
    *
    * @param fsPassword The new user's password
    */
   private void processUpdate( String fsPassword )
   {
      String lsUserID = getUSER_ID() ;

      if ( ( lsUserID != null ) && ( lsUserID.trim().length() > 0 ) )
      {
         R_SC_USER_DIR_INFOImpl loUserDirInfo ;
         R_SC_USER_INFOImpl     loUserInfo ;
         IN_USER_PROFILEImpl    loUserProfile ;
         SearchRequest          loSrchReq ;
         Session                loSession     = getSession() ;
         VSDate                 loUserDt ;
         VSDate                 loDocDt       = getDOC_CREA_DT() ;
         String                 lsEmailPassword ;


         /* Process the R_SC_USER_DIR_INFO record */
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "R_SC_USER_DIR_INFO", "USER_ID", lsUserID ) ;
         loUserDirInfo = (R_SC_USER_DIR_INFOImpl)R_SC_USER_DIR_INFOImpl.getObjectByKey( loSrchReq, loSession ) ;
         if ( loUserDirInfo == null )
         {
            raiseException( "%c:Q0019,p:USER_ID%", null, "USER_ID" ) ;
         } /* end if ( loUserDirInfo == null ) */
         else
         {
            loUserDt = loUserDirInfo.getLAST_UPDATE_DT() ;
            if ( ( loUserDt != null ) && ( loDocDt != null ) )
            {
               if ( loUserDt.after( loDocDt ) )
               {
                  raiseException( "%c:Q0020,p:USER_ID%", null, "USER_ID" ) ;
               } /* end if ( loUserDt.after( loDocDt ) ) */
            } /* end if ( ( loUserDt != null ) && ( loDocDt != null ) ) */

            loUserDirInfo.copyCorresponding( this, null, true ) ;
            loUserDirInfo.save() ;
         } /* end else */

         /* Process the R_SC_USER_INFO record */
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "R_SC_USER_INFO", "USER_ID", lsUserID ) ;
         loUserInfo = (R_SC_USER_INFOImpl)R_SC_USER_INFOImpl.getObjectByKey( loSrchReq, loSession ) ;
         if ( loUserInfo == null )
         {
            raiseException( "%c:Q0021,p:USER_ID%", null, "USER_ID" ) ;
            loUserInfo = R_SC_USER_INFOImpl.getNewObject( loSession, true ) ;
         } /* end if ( loUserInfo == null ) */
         else
         {
            loUserDt = loUserInfo.getLAST_UPDATE_DT() ;
            msOldPswd = loUserInfo.getPSWD_TXT();

            if ( ( loUserDt != null ) && ( loDocDt != null ) )
            {
               if ( loUserDt.after( loDocDt ) )
               {
                  raiseException( "%c:Q0022,p:USER_ID%", null, "USER_ID" ) ;
               } /* end if ( loUserDt.after( loDocDt ) ) */
            } /* end if ( ( loUserDt != null ) && ( loDocDt != null ) ) */
         } /* end else */

         loUserInfo.copyCorresponding( this, USER_INFO_EXCLUDE, true ) ;
         //Set the EXPIRE_NEW_PSWD_FL flag of R_SC_USER_INFO object.
         loUserInfo.setEXPIRE_NEW_PSWD_FL(getEXPIRE_PSWD_FL());

         loUserInfo.setEXT_DIR_ID(getEXT_DIR_ID());

         if ( getEXPIRE_PSWD_FL() || AMSSecurity.getExpireNewPassword() )
         {
           /*
            * Prevent triggering of new password expiry email notification logic from R_SC_USER_INFO.
            * Instead the logic will be triggered in this class later on when UDOC
            * goes to Final from Draft or Pending phase(multiple emails should not be sent).
            */
            loUserInfo.setDONT_EXPIRE_PSWD_FL(true);
         }

         lsEmailPassword = getEMAIL_PSWD_TXT() ;
         if ( fsPassword != null )
         {
            loUserInfo.setPSWD_TXT( fsPassword ) ;
            setPasswordChangeDate( loUserInfo ) ;
         } /* end if ( fsPassword != null ) */
         if ( lsEmailPassword != null )
         {
            loUserInfo.setEMAIL_PSWD_TXT(
                  decryptPasswordString( lsEmailPassword ) ) ;
         } /* end if ( lsEmailPassword != null ) */

         /*
          * The override error code for the user is named
          * USER_OV_ERR_CD since OV_ERR_CD is already
          * reserved by the document model.
          */
         loUserInfo.getData( ATTR_OV_ERR_CD ).setValue( getData( "USER_OV_ERR_CD" ).getValue() ) ;
         loUserInfo.save() ;
         moUserInfoRec = loUserInfo;

         /* Process the IN_USER_PROFILE record */
         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", lsUserID ) ;
         loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
         if ( loUserProfile == null )
         {
            raiseException( "%c:Q0023,p:USER_ID%", null, "USER_ID" ) ;
            loUserProfile = IN_USER_PROFILEImpl.getNewObject( loSession, true ) ;
         } /* end if ( loUserProfile == null ) */
         loUserProfile.copyCorresponding( this, null, true ) ;
         loUserProfile.save() ;

      } /* end if ( ( lsUserID != null ) && ( lsUserID.trim().length() > 0 ) ) */
   } /* end processUpdate() */

   /**
    * This method encrypts the password field values, if they
    * are not encrypted before saving them to the database.
    */
   private void encryptPasswords()
   {
      String lsAppPwd ;
      String lsEmailPwd ;

      if ( ( ( lsAppPwd = getPSWD_TXT() ) != null ) &&
           ( lsAppPwd.length() > 0 ) &&
           ( lsAppPwd.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
      {
         lsAppPwd = encryptPasswordString(lsAppPwd);
         setPSWD_TXT( lsAppPwd ) ;
      } /* end if ( ( ( lsAppPwd = getPSWD_TXT() ) != null ) && . . . */

      if ( ( ( lsEmailPwd = getEMAIL_PSWD_TXT() ) != null ) &&
           ( lsEmailPwd.length() > 0 ) &&
           ( lsEmailPwd.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
      {
         lsEmailPwd = encryptPasswordString( lsEmailPwd ) ;
         setEMAIL_PSWD_TXT( lsEmailPwd ) ;
      } /* end if ( ( ( lsEmailPwd = getEMAIL_PSWD_TXT() ) != null ) && . . . */

   } /* end encryptPasswords() */

   /**
    * This method sets the password change date.  If the user's
    * password is to be initially expired, then it sets the
    * date back appropriately.
    */
   private void setPasswordChangeDate( R_SC_USER_INFOImpl foUserInfo )
   {
      // use new instance of VSDate. Otherwise will be passed by reference
      // and will set the value object directly, losing the actual Old Value and
      // hence incorrect modified() return value.
      VSDate loNewPswdDt;
      VSDate loOriginalDate = foUserInfo.getPSWD_CHG_DT();
      if ( loOriginalDate == null )
      {
         loOriginalDate = new VSDate() ;
      } // end if ( loPswdDt == null )
      loNewPswdDt = new VSDate(loOriginalDate);
      if ( getEXPIRE_PSWD_FL() )
      {
         loNewPswdDt.advanceDays( ( AMSSecurity.getPasswordExpirationDays() + 1 ) * -1 ) ;
      } // end if ( getEXPIRE_PSWD_FL() )
      foUserInfo.setPSWD_CHG_DT( loNewPswdDt ) ;
   } /* end setPasswordChangeDate() */

   /**
    * This method populates a new document with the existing information
    * from the user.  It copies in the data from R_SC_USER_DIR_INFO,
    * R_SC_USER_INFO, and IN_USER_PROFILE to form the header.  It then
    * populates the lines based on the R_SC_USER_ROLE_LNK, R_WF_USER_ROLE,
    * and IN_USER_WKGP tables.  If no R_SC_USER_DIR_INFO record exists for
    * the given user id or if the user id is not yet populated, then an
    * exception is raised.  Otherwise, the header and all newly added lines
    * are marked with the COMP_ACTN_CD of update.  This method does not
    * reconcile any lines that may already exist (e.g. duplicate security
    * roles) in the document.  Also, it overwrites any information in the
    * header.
    */
   protected void populateExistingUser()
   {
      if ( getDOC_PHASE_CD() == DOC_PHASE_DRAFT )
      {
         boolean lboolAuthorized = false ;

         try
         {
            /*
             * Ensure that the user can edit the document
             * since we will be populating the document.
             */
            lboolAuthorized = AMSSecurity.actionAuthorized( getSession().getUserID(),
                  getData( ATTR_DOC_CD ).getString(), DOC_ACTN_EDIT ) ;
         } /* end try */
         catch( AMSSecurityException foExp )
         {
            moAMSLog.error( "Exception while checking action security", foExp ) ;
         } /* end catch( AMSSecurityException foExp ) */
         finally
         {
            if ( !lboolAuthorized )
            {
               AMSSecurityLogControl.logDocUnauthorizedAction(
                     this, "Not authorized to perform selected action.",
                     DOC_ACTN_EDIT ) ;
               raiseException( "Not authorized to perform selected action." ) ;
               if ( AMS_DEBUG )
               {
                  System.err.println( "USER_DOC_HDRImpl::Not authorized to perform action: "
                        + DOC_ACTN_EDIT ) ;
               } /* end if ( AMS_DEBUG ) */
               return ;
            } /* end if ( !lboolAuthorized ) */
         } /* end finally */

         String lsUserID = getUSER_ID() ;
         if ( ( lsUserID != null ) && ( lsUserID.trim().length() > 0 ) )
         {
            populateHeader( lsUserID ) ;
            populateSecurityRoles( lsUserID ) ;
            populateWorkflowRoles( lsUserID ) ;
            populateWorkgroups( lsUserID ) ;
         } /* end if ( ( lsUserID != null ) && ( lsUserID.trim().length() > 0 ) ) */
         else
         {
            raiseException( "%c:Q0010%", null, "USER_ID" ) ;
         } /* else */
      } /* end if ( getDOC_PHASE_CD() == DOC_PHASE_DRAFT ) */
      else
      {
         raiseException( "%c:Q0018%", null, (String)null ) ;
      } /* end else */

   } /* end populateExistingUser() */

   /**
    * This method populates the header from the R_SC_USER_DIR_INFO,
    * R_SC_USER_INFO, and IN_USER_PROFILE records for the given user.
    *
    * @param The user ID
    */
   private void populateHeader( String fsUserID )
   {
      R_SC_USER_DIR_INFOImpl loUserDirInfo ;
      R_SC_USER_INFOImpl     loUserInfo ;
      IN_USER_PROFILEImpl    loUserProfile ;
      SearchRequest          loSrchReq ;
      Session                loSession     = getSession() ;

      /* Set the header action code to update */
      setCOMP_ACTN_CD( CVL_DOC_COMP_ACTNImpl.UPDATE ) ;

      /* Process the R_SC_USER_DIR_INFO record */
      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_DIR_INFO", "USER_ID", fsUserID ) ;
      loUserDirInfo = (R_SC_USER_DIR_INFOImpl)R_SC_USER_DIR_INFOImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserDirInfo == null )
      {
         raiseException( "%c:Q0019,p:USER_ID%", null, "USER_ID" ) ;
      } /* end if ( loUserDirInfo == null ) */
      else
      {
         this.copyCorresponding( loUserDirInfo, null, true ) ;
      } /* end else */

      /* Process the R_SC_USER_INFO record */
      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_INFO", "USER_ID", fsUserID ) ;
      loUserInfo = (R_SC_USER_INFOImpl)R_SC_USER_INFOImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserInfo != null )
      {
         this.copyCorresponding( loUserInfo, USER_INFO_EXCLUDE, true ) ;
         getData( "USER_OV_ERR_CD" ).setValue( loUserInfo.getData( ATTR_OV_ERR_CD ).getValue() ) ;

         try
         {
            getData("EXT_DIR_ID").setValue(AMSSecurityUtil.getExternalIdFromUserId(fsUserID));
         }
         catch( AMSSecurityException foExp )
         {
            moAMSLog.error( "Exception while getting External Directory Information ", foExp ) ;
         } /* end catch( AMSSecurityException foExp ) */
      } /* end if ( loUserInfo != null ) */

      /* Process the IN_USER_PROFILE record */
      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", fsUserID ) ;
      loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserProfile != null )
      {
         this.copyCorresponding( loUserProfile, null, true ) ;
      } /* end else */
   } /* end populateHeader() */

   /**
    * This method populates the security role lines from the
    * R_SC_USER_ROLE_LNK records for the given user.
    *
    * @param The user ID
    */
   private void populateSecurityRoles( String fsUserID )
   {
      Enumeration            leUserRoleLnks ;
      R_SC_USER_ROLE_LNKImpl loUserRoleLnk ;
      USER_DOC_SCROLEImpl    loDocRoleLnk ;
      SearchRequest          loSrchReq ;
      Session                loSession      = getSession() ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", fsUserID ) ;
      leUserRoleLnks = R_SC_USER_ROLE_LNKImpl.getObjectsSorted( loSrchReq,
            loSession, ROLE_LNK_SORT, AMSDataObject.SORT_ASC ) ;

      if ( leUserRoleLnks != null )
      {
         while ( leUserRoleLnks.hasMoreElements() )
         {
            loUserRoleLnk = (R_SC_USER_ROLE_LNKImpl)leUserRoleLnks.nextElement() ;
            loDocRoleLnk = (USER_DOC_SCROLEImpl)USER_DOC_SCROLEImpl.getNewObject(
                  loSession, true ) ;
            populateNewLine( loDocRoleLnk ) ;
            loDocRoleLnk.copyCorresponding( loUserRoleLnk, null, true ) ;
            loDocRoleLnk.setCOMP_ACTN_CD( CVL_DOC_COMP_ACTNImpl.UPDATE ) ;
            loDocRoleLnk.save() ;
         } /* end while ( leUserRoleLnks.hasMoreElements() ) */
      } /* end if ( leUserRoleLnks != null ) */
   } /* end populateSecurityRoles() */

   /**
    * This method populates the workflow role lines from the
    * R_WF_USER_ROLE records for the given user.
    *
    * @param The user ID
    */
   private void populateWorkflowRoles( String fsUserID )
   {
      Enumeration            leUserRoles ;
      R_WF_USER_ROLEImpl     loUserRole ;
      USER_DOC_WFROLEImpl    loDocRoleLnk ;
      SearchRequest          loSrchReq ;
      Session                loSession      = getSession() ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "USID", fsUserID ) ;
      leUserRoles = R_WF_USER_ROLEImpl.getObjects( loSrchReq, loSession ) ;

      if ( leUserRoles != null )
      {
         while ( leUserRoles.hasMoreElements() )
         {
            loUserRole = (R_WF_USER_ROLEImpl)leUserRoles.nextElement() ;
            loDocRoleLnk = (USER_DOC_WFROLEImpl)USER_DOC_WFROLEImpl.getNewObject(
                  loSession, true ) ;
            populateNewLine( loDocRoleLnk ) ;

            loDocRoleLnk.setWF_ROLE_ID( loUserRole.getROLEID() ) ;
            loDocRoleLnk.setWF_ROLE_MGR_FL( loUserRole.getROLE_MGR_FL() ) ;
            loDocRoleLnk.setCOMP_ACTN_CD( CVL_DOC_COMP_ACTNImpl.UPDATE ) ;
            loDocRoleLnk.setDISP_SEQ_NO( loUserRole.getDISP_SEQ_NO() );
            loDocRoleLnk.save() ;
         } /* end while ( leUserRoles.hasMoreElements() ) */
      } /* end if ( leUserRoles != null ) */
   } /* end populateWorkflowRoles() */

   /**
    * This method populates the workgroup lines from the
    * IN_USER_WKGP records for the given user.  It also
    * marks the default workgroup based on the user's
    * IN_USER_PROFILE record.
    *
    * @param The user ID
    */
   private void populateWorkgroups( String fsUserID )
   {
      Enumeration            leUserWkgps ;
      IN_USER_PROFILEImpl    loUserProfile ;
      IN_USER_WKGPImpl       loUserWkgp ;
      USER_DOC_WKGPImpl      loDocWkgp ;
      SearchRequest          loSrchReq ;
      Session                loSession     = getSession() ;
      long                   llDfltWkgp    = 0L ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_PROFILE", "USER_ID", fsUserID ) ;
      loUserProfile = (IN_USER_PROFILEImpl)IN_USER_PROFILEImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserProfile != null )
      {
         llDfltWkgp = loUserProfile.getDFLT_WKGP() ;
      } /* end if ( loUserProfile != null ) */

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "IN_USER_WKGP", "USER_ID", fsUserID ) ;
      leUserWkgps = IN_USER_WKGPImpl.getObjects( loSrchReq, loSession ) ;

      if ( leUserWkgps != null )
      {
         while ( leUserWkgps.hasMoreElements() )
         {
            loUserWkgp = (IN_USER_WKGPImpl)leUserWkgps.nextElement() ;
            loDocWkgp = (USER_DOC_WKGPImpl)USER_DOC_WKGPImpl.getNewObject(
                  loSession, true ) ;
            populateNewLine( loDocWkgp ) ;
            loDocWkgp.copyCorresponding( loUserWkgp, null, true ) ;
            loDocWkgp.setCOMP_ACTN_CD( CVL_DOC_COMP_ACTNImpl.UPDATE ) ;
            if ( ( loUserProfile != null ) && ( llDfltWkgp == loUserWkgp.getWKGP_UNID() ) )
            {
               loDocWkgp.setDFLT_WKGP_FL( true ) ;
            } /* end if ( ( loUserProfile != null ) && ( llDfltWkgp == loUserWkgp.getWKGP_UNID() ) ) */
            loDocWkgp.save() ;
         } /* end while ( leUserWkgps.hasMoreElements() ) */
      } /* end if ( leUserWkgps != null ) */
   } /* end populateWorkgroups() */

   /**
    * This method sets the necessary values from the header
    * into the newly created line.
    *
    * @param foNewLine The new document line
    */
   private void populateNewLine( AMSDocComponent foNewLine )
   {
      foNewLine.getData( ATTR_DOC_CD ).setData( getData( ATTR_DOC_CD ) ) ;
      foNewLine.getData( ATTR_DOC_DEPT_CD ).setData( getData( ATTR_DOC_DEPT_CD ) ) ;
      foNewLine.getData( ATTR_DOC_ID ).setData( getData( ATTR_DOC_ID ) ) ;
      foNewLine.getData( ATTR_DOC_VERS_NO ).setData( getData( ATTR_DOC_VERS_NO ) ) ;
      foNewLine.getData( ATTR_DOC_CAT ).setData( getData( ATTR_DOC_CAT ) ) ;
      foNewLine.getData( ATTR_DOC_TYP ).setData( getData( ATTR_DOC_TYP ) ) ;
      foNewLine.getData( ATTR_DOC_FUNC_CD ).setData( getData( ATTR_DOC_FUNC_CD ) ) ;
      foNewLine.getData( ATTR_DOC_PHASE_CD ).setData( getData( ATTR_DOC_PHASE_CD ) ) ;
   } /* end populateNewLine() */


   /**
    * This method populates the document with default blank lines (based on the
    * document code) when the document is being created.  It creates components
    * based on the R_SC_USER_DOC_COMP entries for the document code.
    */
   protected void populateDocument()
   {
      if ( !isTargetOfAction( DOC_ACTN_NEW ) )
      {
         return ;
      } /* end if ( !isTargetOfAction( DOC_ACTN_NEW ) ) */
      else
      {
         Enumeration            leCompReqs ;
         R_SC_USER_DOC_COMPImpl loCompReq ;
         AMSDocComponent        loDocLine ;
         SearchRequest          loSrchReq ;
         Session                loSession = getSession() ;

         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "R_SC_USER_DOC_COMP", ATTR_DOC_CD, getDOC_CD() ) ;
         leCompReqs = R_SC_USER_DOC_COMPImpl.getObjects( loSrchReq, loSession ) ;

         if ( leCompReqs != null )
         {
            int liNumReq ;

            while ( leCompReqs.hasMoreElements() )
            {
               loCompReq = (R_SC_USER_DOC_COMPImpl)leCompReqs.nextElement() ;
               liNumReq  = loCompReq.getCREA_NEW_TOT() ;
               if ( liNumReq > 0 )
               {
                  for ( int liCtr = 0 ; liCtr < liNumReq ; liCtr++ )
                  {
                     try
                     {
                        loDocLine = (AMSDocComponent)AMSDataObject.getNewObject(
                              loCompReq.getDOC_COMP(), loSession, true ) ;
                        populateNewLine( loDocLine ) ;
                        loDocLine.save() ;
                     } /* end try */
                     catch( Exception foExp )
                     {
                        raiseException( "%c:Q0067,v:" + loCompReq.getDOC_COMP()
                              + ",v:" + foExp.getMessage() + "%" ) ;
                     } /* end catch( Exception foExp ) */
                  } /* end for ( int liCtr = 0 ; liCtr < liNumReq ; liCtr++ ) */
               } /* end if ( liNumReq > 0 ) */
            } /* end while ( leCompReqs.hasMoreElements() ) */
         } /* end if ( leCompReqs != null ) */
      } /* end else */
   } /* end populateDocument() */

   /**
    * This method verifies that the document contains the minimum number of
    * required components based on the document code.
    */
   protected void checkComponentPopulation()
   {
      Enumeration            leCompReqs ;
      R_SC_USER_DOC_COMPImpl loCompReq ;
      SearchRequest          loSrchReq ;
      Session                loSession = getSession() ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_DOC_COMP", ATTR_DOC_CD, getDOC_CD() ) ;
      leCompReqs = R_SC_USER_DOC_COMPImpl.getObjects( loSrchReq, loSession ) ;

      if ( leCompReqs != null )
      {
         int           liMinReq ;
         int           liNumExist ;
         SearchRequest loLnSrchReq ;
         String        lsDocComp ;

         while ( leCompReqs.hasMoreElements() )
         {
            loCompReq = (R_SC_USER_DOC_COMPImpl)leCompReqs.nextElement() ;
            liMinReq  = loCompReq.getPROC_MIN_REQ() ;
            if ( liMinReq > 0 )
            {
               lsDocComp = loCompReq.getDOC_COMP() ;
               loLnSrchReq = new SearchRequest() ;
               loLnSrchReq.addParameter( lsDocComp, ATTR_DOC_CD, getDOC_CD() ) ;
               loLnSrchReq.addParameter( lsDocComp, ATTR_DOC_DEPT_CD, getDOC_DEPT_CD() ) ;
               loLnSrchReq.addParameter( lsDocComp, ATTR_DOC_ID, getDOC_ID() ) ;
               loLnSrchReq.addParameter( lsDocComp, ATTR_DOC_VERS_NO,
                     getData( ATTR_DOC_VERS_NO ).getString() ) ;
               try
               {
                  liNumExist = AMSDataObject.getObjectCount( lsDocComp, loLnSrchReq, loSession ) ;
                  if ( liNumExist < liMinReq )
                  {
                     raiseException( "%c:" + loCompReq.getPROC_MIN_MSG_CD() + "%" ) ;
                  } /* end if ( liNumExist < liMinReq ) */
               } /* end try */
               catch( Exception foExp )
               {
                  raiseException( "%c:Q0068,v:" + loCompReq.getDOC_COMP()
                        + ",v:" + foExp.getMessage() + "%" ) ;
               } /* end catch( Exception foExp ) */
            } /* end if ( liMinReq > 0 ) */
         } /* end while ( leCompReqs.hasMoreElements() ) */
      } /* end if ( leCompReqs != null ) */
   } /* end checkComponentPopulation() */

   /**
    * This method is used to encrypt the password string that was passed
    *
    * @param fsPassword Password to be encrypted
    * @return String encrypted password
    */
   private String encryptPasswordString( String fsPassword )
   {
      String lsDocID = getDOC_ID();
      try
      {

         CSFISymCipher loSymCipher = CSFManager.getInstance().getSymCipher();
         CSFIEncoder loCoder = CSFManager.getInstance().getEncoder();
         byte[] loCipherText;
         String lsDocIdPadded = setupDocIdForEncryptDecrypt(lsDocID);
         String lsPswdPadded = setupPasswordForEncryptDecrypt(lsDocIdPadded.length(),
                                    fsPassword);

         /*
          * Setup the cipher to encrypt the password
          */
         loSymCipher.init();
         loSymCipher.encrypt(lsDocIdPadded.getBytes());
         loSymCipher.doFinal(lsPswdPadded.toString());
         loCipherText = loSymCipher.getCipherText();

         /*
          * BASE64 encode the encrypted password and return the same
          */
         return loCoder.encode(loCipherText);
      }
      catch (CSFException loCSFExcep)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loCSFExcep);

         raiseException("Encountered an error while encrypting the password");
         return null;
      }
   } /* end encryptPasswordString */

   /**
    * This method is used to decrypt the password string that was passed
    *
    * @param fsPassword Password to be decrypted
    * @return String decrypted password
    */
   private String decryptPasswordString( String fsPassword )
   {
      String lsDocID = getDOC_ID();
      try
      {
         CSFISymCipher loSymCipher = CSFManager.getInstance().getSymCipher();
         CSFIEncoder loCoder = CSFManager.getInstance().getEncoder();
         byte[] loCipherText;
         String lsDocIdPadded = setupDocIdForEncryptDecrypt(lsDocID);

         /*
          * BASE64 decode the encrypted password and return the same
          */
         loCipherText = loCoder.decode(fsPassword);

         /*
          * Setup the cipher to decrypt the password
          */
         loSymCipher.init() ;
         loSymCipher.decrypt(lsDocIdPadded.getBytes());
         loSymCipher.doFinal(loCipherText);
         loCipherText = loSymCipher.getCipherText();
         return new String(loCipherText);
      }
      catch (CSFException loCSFExcep)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loCSFExcep);

         raiseException("Encountered an error while decrypting the password");
         return null;
      }
   } /* end encryptPasswordString */

   private String setupDocIdForEncryptDecrypt(String fsDocID)
   {
      StringBuffer loDocIdBuf;
      int liDocIdMaxLen = getData(
             AMSCommonConstants.ATTR_DOC_ID).getMetaColumn().getSize();

      // Pad the document id to the max length
      loDocIdBuf = new StringBuffer(liDocIdMaxLen);

      loDocIdBuf.append(fsDocID);

      for ( int liEmptyLen = fsDocID.length() ;
            liEmptyLen < liDocIdMaxLen ; liEmptyLen++ )
      {
         loDocIdBuf.append( ' ' ) ;
      } /* end for ( int liEmptyLen = lsDocID.length() ; ... */

      return loDocIdBuf.toString();
   } /* end setupForEncryptDecrypt */

   private String setupPasswordForEncryptDecrypt(int fiDocIDLen,
                                                 String fsPassword)
   {
      StringBuffer loPswdBuf;
      /*
       * Increment the doc id length to make it a multiple of 8 and
       * set this as the length of the password. This is required for
       * the encryption algorithm to work correctly
       */
      int liPswdMaxLen = fiDocIDLen + (8 - (fiDocIDLen%8));

      // Pad the password to the max length
      loPswdBuf = new StringBuffer(liPswdMaxLen);

      loPswdBuf.append(fsPassword);

      for ( int liEmptyLen = fsPassword.length() ;
            liEmptyLen < liPswdMaxLen ; liEmptyLen++ )
      {
         loPswdBuf.append( ' ' ) ;
      } /* end for ( int liEmptyLen = lsDocID.length() ; ... */
      return loPswdBuf.toString();
   } /* end setupPasswordForEncryptDecrypt */


   /**
     * Expire the new password only after UDOC goes to Final from Draft or Pending phase.
     * @param foSession Session
     */
   public void myAfterCommit( Session foSession )
   {
      /*
       * Expire the new password and send email only when UDOC goes to Final from Draft or
       * Pending phase. Other conditions required are (Expire New Password flag is checked
       * OR Expire New Password flag on Security Configuration is true) and
       * Password is entered or generated for the User. Note with the introduction of
       * Expire New Password flag the field Send Email Notification(EMAIL_USER_FL) is no longer
       * being used.
       */
       if( ( getDOC_PHASE_CD() == DOC_PHASE_FINAL ) &&
           ( getEXPIRE_PSWD_FL()|| AMSSecurity.getExpireNewPassword() ) &&
           ( msEmailPassword != null ) )
       {
          moUserInfoRec.expireNewPasswordWithNotification( (getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.ADD) ) ;
       }

   }

   /*
    * Method to check if authentication type is LDAP.
    */
   protected boolean isAuthTypeLDAP()
   {
      if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP )
      {
         return true ;
      }

      return false ;
   }

   /**
    * Method will be executed after the document is submitted.
    */
   public void afterSubmit()
   {
      AMSSQLConnection loConnection= null;
      try
      {
         /* Before document is finalized, if password of an existing
            User has been changed, then add the old password to Password
            History table */
         if( (getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.UPDATE)
            && getRESET_PSWD_FL() && !AMSStringUtil.strIsEmpty( getPSWD_TXT() )
            && !AMSStringUtil.strIsEmpty( msOldPswd )
            && (getData( ATTR_DOC_PHASE_CD ).getint() == DOC_PHASE_FINAL )
               && (AMSMsgUtil.getHighestSeverityLevel(getSession())
                  <= AMSMsgUtil.SEVERITY_LEVEL_WARNING ) )
         {
            loConnection= AMSUtil.getSQLConnection( "SC_PSWD_HIST" ) ;
            loConnection.setAutoCommit(false);
            AMSSecurity.updatePswdHistory( getUSER_ID(), msOldPswd, true,
                  decryptPasswordString(getPSWD_TXT()), loConnection, false );
            loConnection.commit();
         }
      }//end try
      catch(Exception loExcp)
      {


         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException(loExcp.toString());
         try
         {
            loConnection.rollback();
         }
         catch(Exception loExcp1)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExcp1);

            raiseException(loExcp1.toString());
         }
      }//end catch
      finally
      {
         if( loConnection!= null )
         {
            try
            {
               loConnection.close();
            }//end try
            catch(Exception loExcp2)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp2);

               raiseException(loExcp2.toString());
            }
         }
      }//end finally
   }/* end of afterSubmit() */

   //BGN ADVHR00025681
   /**
    * This method is used to check if there are no
    * Security Roles assigned to the user
    * ADVHR00074743 - This constraint should check only for ESSUSER document and 
    * For UDOC and HRDOC, If ESS user Type and MSS User Types are not populated and security roles lines are zero then raise this error
    * ADVHR00074743 - This constraint should check only for ESSUSER document and
    * For UDOC and HRDOC, If ESS user Type and MSS User Types are not populated and security roles lines are zero then raise this error
    */
   public void checkNoSecurityRolesAssigned()
   {
       if ( ((!AMSStringUtil.strEqual(getDOC_TYP(), "ESSUSER") && getMSS_USER_TYP() ==0 && getESS_USER_TYP() ==0)  ||
                AMSStringUtil.strEqual(getDOC_TYP(), "ESSUSER") )&& 
                ( ( getDOC_ACTN_CD() == DOC_ACTN_SUBMIT ) || ( getDOC_ACTN_CD() == DOC_ACTN_VALIDATE ) ))
       {
            SearchRequest loSearchRequest = new SearchRequest();
            loSearchRequest.addParameter("USER_DOC_SCROLE", "DOC_CD", getDOC_CD());
            loSearchRequest.addParameter("USER_DOC_SCROLE", "DOC_DEPT_CD", getDOC_DEPT_CD());
            loSearchRequest.addParameter("USER_DOC_SCROLE", "DOC_ID", getDOC_ID());
            loSearchRequest.addParameter("USER_DOC_SCROLE", "DOC_VERS_NO", "" + getDOC_VERS_NO());
            int liLineCount = USER_DOC_SCROLEImpl.getObjectCount( loSearchRequest, getSession() );
            if ( liLineCount < 1 )
            {
                SearchRequest loSrchReq = new SearchRequest() ;
                loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", getUSER_ID() ) ;
                int liUserRoleLnkCount = R_SC_USER_ROLE_LNKImpl.getObjectCount( loSrchReq, getSession() ) ;
                if ( liUserRoleLnkCount<1 )
                {
                    raiseException( "Security Role Assignment does not exist for User ID : " + getUSER_ID() ,getSession() ) ;
                }
            }
       }
   }
   //END ADVHR00025681

   @Override
   public HashMap getCustomVisibleFields()
   {
      try
      {
         SearchRequest loSrchRq = new SearchRequest();
         loSrchRq.add(" APPL_ID > 0 ");

         Vector loSortAttr = new Vector();
         loSortAttr.add("APPL_ID");

         Enumeration <R_SC_APPLImpl> loEnumApplList = R_SC_APPLImpl.getObjectsSorted(loSrchRq, getSession(),
               loSortAttr, AMSDataObject.SORT_ASC);
         String lsApplNm = null;
         int liApplID = -1;
         long llUserAppList = getAPPL_AUTH();
         String lsValue;
         String [][] lsaFields = new String[50][3];
         int liCnt = 0;
         while(loEnumApplList.hasMoreElements())
         {
            lsValue = "false";
            R_SC_APPLImpl loApp = loEnumApplList.nextElement();
            liApplID = loApp.getAPPL_ID();
            lsApplNm = loApp.getAPPL_NM();
            if ( ( llUserAppList & ( (long)1 << ( liApplID - 1 ) ) ) > 0 )
            {
               lsValue = "true" ;
            }
            lsaFields[++liCnt][0] = lsApplNm + ": ";
            lsaFields[liCnt][1] = lsValue;
            lsaFields[liCnt][2] = "true";
         }

         int liMaxFieldsCols =-1;

         if((liCnt % 3) == 0)
         {
            liMaxFieldsCols = liCnt / 3;
         }
         else
         {
            liMaxFieldsCols = (liCnt / 3) + 1;
         }

         String[][][] lsaApplistArray = new String[3][liMaxFieldsCols][3];
         int liCurrCol = 0;
         for(int liFldCnt = 1; liFldCnt <= liCnt; liFldCnt++)
         {
            liCurrCol = (liFldCnt - 1) % 3;

            lsaApplistArray[liCurrCol][(liFldCnt - 1)/3] = lsaFields[liFldCnt];
         }
         HashMap loMap = new HashMap(2);
         loMap.put("Applications", lsaApplistArray);
         return loMap;
      }
      catch (Exception foExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         return null;
      }
   }


}
