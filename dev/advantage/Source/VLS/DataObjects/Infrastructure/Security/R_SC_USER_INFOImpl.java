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

import com.cgi.infoadvantage.integration.*;
import javax.naming.*;
import javax.naming.directory.*;

/*
**  R_SC_USER_INFO
*/

/*
 *  KEYWORD: UPD_LDAP_PWD
 *
 *  While creating or updating a user password, following properties are set on LDAP server:
 *  unicodePwd, userAccountControl
 *
 *  userAccountControl is also set(with a different value) when Expire New Password flag is selected
 *  during password creation or update.
 *
 *  Property values set:
 *  unicodePwd = <actual password>
 *    This property is set with the actual password of the user, only applicable for ADS.
 *
 *  userAccountControl = EXPIRED_PWD_ATTRIBUTE_VALUE OR EXPIRED_PWD_ATTRIBUTE_RESET_VALUE
 *    Depending on the Expire New Password flag selected in Advantage during User password creation
 *    or update, either of the 2 values can be used:
 *  EXPIRED_PWD_ATTRIBUTE_VALUE (8388608):      Used when Expire New Password flag is true.
 *       Expires the user password. The user has to change password on first login.
 *  EXPIRED_PWD_ATTRIBUTE_RESET_VALUE (66048) : Used when Expire New Password flag is false
 *       and the password is changed. It only updates the User's password.
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_USER_INFOImpl extends  R_SC_USER_INFOBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /** The LDAP password text to be remembered during insert logic and set before commit */
   private String msNewLDAPPassword = null ;
   private boolean mboolDeleteLDAPUser = false;

   /** The clear text password to be remembered. It is used to send notification email to user. */
   private String msClrTxtPassword = null ;

   /**Variable is set to true if password has been modified. */
   private boolean mboolAppPwdMod  = false;

   /**Variable to remember if new user is being inserted */
   private boolean mboolIsUserInserted = false;

   /**Variable to remember if existing user record is being updated*/
   private boolean mboolIsUserUpdated = false;
//{{COMP_CLASS_CTOR
public R_SC_USER_INFOImpl (){
	super();
}

public R_SC_USER_INFOImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_SC_USER_INFOImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_SC_USER_INFOImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
public void beforeInsert( DataObject obj, Response response )
{
   String lsCombOrgCd ;
   String lsAppPwd ;
   String lsEmailPwd ;
   String lsHintReply ;
   String lsUserID = getUSER_ID();

   lsCombOrgCd = AMSSecurity.formCombOrgCode(
                       getHOME_GOVT_BRN_CD(), getHOME_CAB_CD(), getHOME_DEPT_CD(),
                       getHOME_DIV_CD(), getHOME_GP_CD(), getHOME_SECT_CD(),
                       getHOME_DSTC_CD(), getHOME_BUR_CD(), getHOME_UNIT_CD() ) ;
   setCOMB_ORG_CD( lsCombOrgCd ) ;

   setUserSecRealm();
   try
   {

      if ( ( ( lsAppPwd = getPSWD_TXT() ) != null ) &&
           ( lsAppPwd.length() > 0 ) &&
           ( lsAppPwd.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
      {
          msClrTxtPassword = lsAppPwd;
         if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP )
         {
            /*
             * Since the user may not exist yet on the LDAP server,
             * remember the new password so that it can be set
             * in the beforeCommit event.
             */
            msNewLDAPPassword = lsAppPwd ;
            /* Keyword: SET_PSWD_ON_DB_FOR_LDAP
             * Commented out below line because it does not allow to set the password on databse
             * when Authentication is set to LDAP. In certain situations it is desired to set
             * the password in the database also even though Autentication is set to LDAP.
             * For e.g.: A client had a situation where they intended to use LDAP in production,
             * they defined LDAP users in Advantage when Authentication was set to LDAP and then
             * for pre-production testing they switched Authentication to DB (just for temporary period
             * until the system can go into production with LDAP) and they found that Users
             * defined on Advantage could not log in because they had null password set on database.
             * Please note once the application is in production with LDAP then switching back and
             * forth between LDAP and DB is not allowed.
             */
            //setPSWD_TXT( getOldPSWD_TXT() ) ;
            //Added this line  for reasons explained above.
            setPSWD_TXT( AMSSecurity.encryptPassword( lsAppPwd )  ) ;

            String lsUserSecRealm = getUSER_SEC_REALM();

            /* If ExtenalDirectoryEnabled=true.Send External Directory ID to LDAP 
             * instead of USER_ID */
            if (AMSParams.mboolExternalDirectoryIdEnabled 
                    && !AMSStringUtil.strIsEmpty(getEXT_DIR_ID()))
            {
               lsUserID = getEXT_DIR_ID();
            }
            boolean lboolUserExistsOnLDAP = AMSSecurity.checkLDAPUser( lsUserID, 
                                                                       lsUserSecRealm );

            if(AMSParams.mboolExternalDirectoryIdEnabled && !lboolUserExistsOnLDAP)
            {
               raiseException("Valid External Directory Information has to be specified");
            }


            /* The Database is case sensitive but LDAP is not, so we can have an instance of
             * two different cased user ids in Database but single record in LDAP. To avoid
             * that situation issue an error if the user id exists in the Database and we
             * are trying to add new id with different case.
             */
            if ( doesUserInfoExist(getUSER_ID().toUpperCase()) )
            {
               raiseException("User ID already exists. Please choose different User ID.");
            }

            if ( ( AMSParams.miDirectoryInfoType == AMSParams.TYPE_DB ) &&
                 ( !lboolUserExistsOnLDAP ) )
            {
               if ( AMSStringUtil.strIsEmpty( msNewLDAPPassword ) )
               {
                  raiseException("Password is required for new user.");
               }
               else
               {
                  AMSSecurity.addLDAPUser( getUSER_ID(), msNewLDAPPassword, lsUserSecRealm );
                  // This flag will determine that the LDAP User is newly created and if
                  // the transaction will roll-back we want to delete that LDAP User.
                  mboolDeleteLDAPUser = true;
               }
            }


            // Commenting the code below. This code is no longer required as the updates
            // are being moved from myBeforeCommit to afterCommit which will be performed
            // only when the transaction was successful.

            /* Based on whether msNewLDAPPassword is set or not, we update the password
             * in LDAP before committing. In the case when we are adding new Users to LDAP,
             * we don't want to change the password again. Doing so will cause issues if
             * password policy is enabled on the LDAP (the old password and new password
             * being the same)
             */
            //if ( !lboolUserExistsOnLDAP )
            //{
            //   msNewLDAPPassword = null;
            //}
         }
         else /* end if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP ) */
         {
            try
            {
               AMSSecurity.validatePasswordRules( lsAppPwd, getUSER_ID() ) ;
               lsAppPwd = AMSSecurity.encryptPassword( lsAppPwd ) ;
               setPSWD_TXT( lsAppPwd ) ;
               if ( !getData( "PSWD_CHG_DT" ).modified() )
               {
                  setPSWD_CHG_DT( (VSDate)getDate() ) ;
               } /* end if ( !getData( "PSWD_CHG_DT" ).modified() ) */
            } /* end try */
            catch( AMSSecurityException foExp )
            {
               raiseException( foExp.getMessage() ) ;
            } /* end catch( AMSSecurityException foExp ) */
         } /* end else */
      } /* end if ( ( ( lsAppPwd = getPSWD_TXT() ) != null ) && . . . */
      else if (lsAppPwd == null)
      {
         if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP )
         {
            String lsUserSecRealm = getUSER_SEC_REALM();
            /* If ExtenalDirectoryEnabled=true.Send External Directory ID to LDAP 
             * instead of USER_ID */
            if (AMSParams.mboolExternalDirectoryIdEnabled 
                    && !AMSStringUtil.strIsEmpty(getEXT_DIR_ID()))
            {
               lsUserID = getEXT_DIR_ID();
            }
            boolean lboolUserExistsOnLDAP = AMSSecurity.checkLDAPUser( lsUserID, 
                                                                       lsUserSecRealm );

            if(AMSParams.mboolExternalDirectoryIdEnabled && !lboolUserExistsOnLDAP)
            {
               raiseException("Valid External Directory Information has to be specified");
            }
            else if( !lboolUserExistsOnLDAP )
            {
               raiseException("A valid password has to be specified");
            }
         }
         else
         {
            raiseException("A valid password has to be specified");
         }
      }

      if ( ( ( lsEmailPwd = getEMAIL_PSWD_TXT() ) != null ) &&
           ( lsEmailPwd.length() > 0 ) &&
           ( lsEmailPwd.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
      {
         lsEmailPwd =  AMSSecurity.encryptUserData( lsEmailPwd ) ;
         setEMAIL_PSWD_TXT( lsEmailPwd ) ;
      } /* end if ( ( ( lsEmailPwd = getEMAIL_PSWD_TXT() ) != null ) && . . . */

      if ( ( ( lsHintReply = getPSWD_QUERY_ANSW() ) != null ) &&
           ( lsHintReply.length() > 0 ) &&
           ( lsHintReply.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
      {
         lsHintReply =  AMSSecurity.encryptPassword( lsHintReply ) ;
         setPSWD_QUERY_ANSW( lsHintReply ) ;
      } /* end if ( ( ( lsHintReply = getPSWD_QUERY_ANSW() ) != null ) && . . . */
      if ( !AMSStringUtil.strIsEmpty(getEXT_DIR_ID()))
      {
        insertUserAttr();
      }
   }
   catch (AMSSecurityException loExp)
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      raiseException( loExp.getMessage() );
   }

   /*
    * Insert new / Update Business Objects user if integration enabled
    */
   updateBOUser();

}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate( DataObject obj, Response response )
{
   String lsCombOrgCd ;
   String lsAppPwd = getPSWD_TXT() ;
   boolean lboolEmailPwdMod = false;
   boolean lboolHintReplyMod = false;
   boolean lboolValidatePswd = false;
   String lsEmailPwd = getEMAIL_PSWD_TXT();
   String lsHintReply = getPSWD_QUERY_ANSW();
   String lsClearBOPwd = null;
   String lsCAPassword = getCA_PSWD_TXT();
   Byte lbBdLgnCt = 0;  
   int liOldLockFlag = getOldLOCK_FL();
   int liLockFlag = getLOCK_FL();

   lsCombOrgCd = AMSSecurity.formCombOrgCode(
                       getHOME_GOVT_BRN_CD(), getHOME_CAB_CD(), getHOME_DEPT_CD(),
                       getHOME_DIV_CD(), getHOME_GP_CD(), getHOME_SECT_CD(),
                       getHOME_DSTC_CD(), getHOME_BUR_CD(), getHOME_UNIT_CD() ) ;
   setCOMB_ORG_CD( lsCombOrgCd ) ;

   if ( ( getData( "PSWD_TXT" ).modified() ) &&
        ( lsAppPwd != null ) &&
        ( lsAppPwd.length() > 0 )  &&
        ( lsAppPwd.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
   {
      mboolAppPwdMod = true;
   } /* end if ( ( getData( "PSWD_TXT" ).modified() ) && ... */

   if ( ( getData( "EMAIL_PSWD_TXT" ).modified() ) &&
        ( lsEmailPwd != null ) &&
        ( lsEmailPwd.length() > 0 ) &&
        ( lsEmailPwd.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
   {
      lboolEmailPwdMod = true;
   } /* end if ( ( getData( "EMAIL_PSWD_TXT" ).modified() ) && */

   if ( ( getData( "PSWD_QUERY_ANSW" ).modified() ) &&
        ( lsHintReply != null ) &&
        ( lsHintReply.length() > 0 ) &&
        ( lsHintReply.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
   {
      lboolHintReplyMod = true ;
   }

   if ( ( getData( "PSWD_QUERY" ).modified() ||
          getData( "PSWD_QUERY_ANSW" ).modified() ) &&
          AMSStringUtil.strIsEmpty(lsAppPwd) )
   {
      raiseException("Password is required when Security Question/Answer is changed.");
   }
   else if ( ( getData( "PSWD_QUERY" ).modified() ||
               getData( "PSWD_QUERY_ANSW" ).modified() )  &&
               !AMSStringUtil.strIsEmpty(lsAppPwd) )

   {
      // Validate the password if Security Question or Answer Changed
      if (getData( "PSWD_QUERY_ANSW" ).modified())
      {
         // Query Answer is found to be changed just make sure the encrypted
         // values are different. If encrypted values are found to be different
         // validate the password
         String lsPswdQueryAnsw = null;
         if ( ( ( lsPswdQueryAnsw = getPSWD_QUERY_ANSW() ) != null ) &&
              ( lsPswdQueryAnsw.length() > 0 ) &&
              ( lsPswdQueryAnsw.length() <= AMSUser.PASSWORD_MAX_LENGTH ) )
         {
            try
            {
               lsPswdQueryAnsw =  AMSSecurity.encryptPassword( lsPswdQueryAnsw );
               if (!AMSStringUtil.strEqual(getOldPSWD_QUERY_ANSW(),lsPswdQueryAnsw) )
               {
                  lboolValidatePswd = true;
               }
            }
            catch (AMSSecurityException loExp)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);





               raiseException( loExp.getMessage() );
            }

         }

      }
      else
      {
         lboolValidatePswd = true;
      }
   }


   if ( mboolAppPwdMod || lboolEmailPwdMod || lboolHintReplyMod || lboolValidatePswd)
   {
      /*
       * Only Users with roles ADMN or User Management Roles can change User's passwords.
       */
      AMSUser loUser = AMSSecurity.getUser(getSession().getUserID());
      Vector lvSecGrps = loUser.getSecurityGroups();
      String lsTempRole;
      boolean lboolAuth = false;
      SearchRequest loSrchRqst = new SearchRequest();
      Enumeration loPswdHistEnum;
      SC_PSWD_HISTImpl loPswdHist;
      Vector lvPswdHistList = null;

      for (int liIndex = 0; liIndex < lvSecGrps.size(); liIndex++)
      {
         lsTempRole = (String) lvSecGrps.elementAt(liIndex);
         if ( lsTempRole.equals(AMSSecurity.ADMIN_SEC_ROLE) ||
              AMSSecurity.roleEquals(lsTempRole, AMSParams.msUserMgmtRoles) )
         {
            lboolAuth = true;
            break;
         } /* end if ( lsTempRole.equals(AMSSecurity.ADMIN_SEC_ROLE ... */
      } /* end for (int liIndex = 0; liIndex < lvSecGrps.size(); ... */

      if (!lboolAuth)
      {
         StringBuffer lsbMsg = new StringBuffer(128);
         lsbMsg.append("You have to belong to the ADMN Role ");

         if ( (AMSParams.msUserMgmtRoles != null)
               && AMSParams.msUserMgmtRoles.length > 0 )
         {
            lsbMsg.append(" or any of the User Management Roles (if defined in the system)");
         }
         lsbMsg.append(" to change user's passwords");
         raiseException(lsbMsg.toString());

         return;
      } /* end if (!lboolAuth) */

      try
      {
         if ( lboolValidatePswd )
         {
            AMSSecurity.authenticateUser(getUSER_ID(),lsAppPwd,true,null);
         }

      }
      catch(Exception loException)
      {
         //If existing user, and user entered incorrect password.
         mboolAppPwdMod = false;

         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loException);


         raiseException("%c:V224%");
      }

      try
      {
         if ( mboolAppPwdMod )
         {
            // Perform a search request to retieve all the password
            // history entries from the password history table and
            // convert them as well
            if ( AMSParams.miAuthenticationType == AMSParams.TYPE_DB )
            {
               AMSSecurity.validatePasswordRules( lsAppPwd, getUSER_ID() ) ;
               setPSWD_TXT( AMSSecurity.encryptPassword( lsAppPwd ) ) ;
            } /* end if (AMSParams.miAuthenticationType == AMSParams.TYPE_DB) */
            else if (AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP)
            {
               //Search for keyword SET_PSWD_ON_DB_FOR_LDAP in this file to understand
               //why below line is commented out and then next line is added.
               //setPSWD_TXT( getOldPSWD_TXT() ) ;
               setPSWD_TXT( AMSSecurity.encryptPassword( lsAppPwd ) ) ;

               msNewLDAPPassword = lsAppPwd;
            } /* end else if (AMSParams.miAuthenticationType == AMSParams.TYPE_DB) */

            if ( !getData( "PSWD_CHG_DT" ).modified() )
            {
               setPSWD_CHG_DT( (VSDate)getDate() ) ;
            } /* end if ( !getData( "PSWD_CHG_DT" ).modified() ) */

          msClrTxtPassword = lsAppPwd;
         } /* end if (mboolAppPwdMod) */

         if ( ( lboolEmailPwdMod ) &&  ( lsEmailPwd != null ) )
         {
            setEMAIL_PSWD_TXT( AMSSecurity.encryptUserData( lsEmailPwd ) ) ;
         } /* end if ( ( lboolEmailPwdMod ) &&  ( lsEmailPwd != null ) ) */

         if ( ( lboolHintReplyMod ) &&  ( lsHintReply != null ) )
         {
            setPSWD_QUERY_ANSW( AMSSecurity.encryptPassword( lsHintReply ) ) ;
         } /* end if ( ( lboolHintReplyMod ) &&  ( lsHintReply != null )  */
      } /* end try */
      catch (AMSSecurityException loSecEx)
      {
         raiseException(loSecEx.getMessage());
      } /* end catch (AMSSecurityException loSecEx) */
   } /* end if ( mboolAppPwdMod || lboolEmailPwdMod || lboolHintReplyMod ) */
   if ((liLockFlag == CVL_USER_LOCK_STAImpl.ACTIVE ) && (liOldLockFlag ==CVL_USER_LOCK_STAImpl.LOCKED) && isChanged("LOCK_FL"))
   {
      setBAD_LGN_CT(lbBdLgnCt);
   }


   /*
    * Insert new / Update Business Objects user if integration enabled
    */
   updateBOUser();


     insertUserAttr();

}
//END_COMP_EVENT_beforeUpdate}}

//{{COMP_EVENT_afterCommit
public void afterCommit(Session session)
//@SuppressAbstract
{
    /* KEYWORD: UPD_LDAP_PWD
     * Password update on LDAP should only happen on successful commit. For password
     * updates happening from UDOC, there are chances that transaction is rolled back.
     */
     if ((AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP)
            && (!AMSStringUtil.strIsEmpty(msNewLDAPPassword)))
      {
         try
         {
            /* Search for keyword UPD_LDAP_PWD in this class to understand the updates
               happening on LDAP server for password change(no password expiry) */
            AMSSecurity.changeLDAPUserPswd(getUSER_ID(), msNewLDAPPassword,
                  getUSER_SEC_REALM());
         }
         catch (AMSSecurityException foExp)
         {
            moAMSLog.error( "Unable to change LDAP user password", foExp ) ;
            raiseException(foExp.getMessage());

         }
         finally
         {
            msNewLDAPPassword = null;
         }
      }

   /*
    * When "Expire New Password" field is selected on the page initiating this transaction or
    * "Expire New Password" is true on Security Configuration page, then expire the password of
    * the User(Bad Logins Count is also reset to 0). Additionally send an email notifying
    * the User of the same. For exceptional cases this behavior can be turned off by
    * setting DONT_EXPIRE_PSWD_FL field value to true(only meant from UDOC because it executes
    * this logic from USER_DOC_HDRIml.java in a different way, i.e., only when UDOC goes to Final
    * and not during Validate action).
    * A scenario demonstrating this functionality would be the case where Administrator sets
    * a new password for the User on UDOC or on User Information page(or any other pages where
    * facility to set password and select "Expire New Password" flag is available) and wants
    * to notify the User of the new password, as well as when User logs in first time using
    * the new password, the password should expire and prompt the User for a newer password(that
    * way User can choose the password of his/her preference).
    */
   if( ( AMSParams.miAuthenticationType == AMSParams.TYPE_DB )
         && !getDONT_EXPIRE_PSWD_FL()
         &&  (getEXPIRE_NEW_PSWD_FL() || AMSSecurity.getExpireNewPassword()))
   {
     //Execute password expiry logic only when password is changed for existing User or
     //added for new User.Call expireNewPassword with value of isNewUser flag as true.
     if (mboolIsUserInserted)
     {
        expireNewPasswordWithNotification(true);
     }

     // user record was updated. The Password also was changed.
     //Call expireNewPassword with value of isNewUser flag as false.
     if (mboolIsUserUpdated && mboolAppPwdMod )
     {
        expireNewPasswordWithNotification(false);
     }
    }
   //reset password text - done with expireNewPassword() so no longer needed
   //msClrTxtPassword = null; ADVHR00038463
   AMSSecurity.removeUserCache( getUSER_ID() ) ;
   super.afterCommit(session);

}
//END_COMP_EVENT_afterCommit}}

//{{COMP_EVENT_beforeDelete
public void beforeDelete(DataObject obj, Response response)
{
   deleteBOUser() ;
}
//END_COMP_EVENT_beforeDelete}}

//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
    //Write Event Code below this line
    mboolIsUserInserted = true;

   validateMSSUserTyp();
   validateESSUserTyp();
   //update INTG_SC_USER_INFO table to integrate with infoAdvantage or other security
   INTG_SC_USER_INFOImpl.updateSecurityUserInformation( getSession(), this, INTG_SC_USER_INFOImpl.INSERT_ACTION ) ;
}
//END_COMP_EVENT_afterInsert}}

//{{COMP_EVENT_afterUpdate
public void afterUpdate(DataObject obj)
{
    //Write Event Code below this line
   /* If password is changed and expire new password flag is set or global expire password flag(security config)
   is set and dont expire passsword flag is set to false then we need to expire password and send user
   email notification.*/
    mboolIsUserUpdated = true;

   validateMSSUserTyp();
   validateESSUserTyp();
   //update INTG_SC_USER_INFO table to integrate with infoAdvantage or other security
   INTG_SC_USER_INFOImpl.updateSecurityUserInformation( getSession(), this, INTG_SC_USER_INFOImpl.UPDATE_ACTION ) ;


}
//END_COMP_EVENT_afterUpdate}}

//{{COMP_EVENT_afterDelete
public void afterDelete(DataObject obj)
{
    //Write Event Code below this line

   //update INTG_SC_USER_INFO table to integrate with infoAdvantage or other security
   INTG_SC_USER_INFOImpl.updateSecurityUserInformation( getSession(), this, INTG_SC_USER_INFOImpl.DELETE_ACTION ) ;
}
//END_COMP_EVENT_afterDelete}}

//{{COMP_EVENT_afterRollback
   public void afterRollback(Session session)
   {
      try
      {
         if ( mboolDeleteLDAPUser &&
            (AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP) )
         {
            AMSSecurity.removeLDAPUser( getUSER_ID(), getUSER_SEC_REALM() );
            mboolDeleteLDAPUser = false;
         }
      }
      catch (Exception loExp)
      {
         raiseException("Failed to delete user from the LDAP server");
      }
   }


//END_COMP_EVENT_afterRollback}}

//{{COMP_EVENT_beforeResultsetFill

  /**
    * This method is called to set the non persistent
    * column EXT_DIR_ID on load.
    */
   public static void beforeResultsetFill(DataRow rowToBeAdded, Response response)
   {
      //Write Event Code below this line
      SearchRequest loSearchReq = new SearchRequest();
      loSearchReq.addParameter("R_SC_USER_ATTR", "USER_ID" ,
                     rowToBeAdded.getData("USER_ID").getString() );
      loSearchReq.addParameter("R_SC_USER_ATTR", "ATTR_NM" ,
                     AMSCommonConstants.ATTR_NM_EXTARNAL_ID);
      R_SC_USER_ATTRImpl loUserAttr =
         (R_SC_USER_ATTRImpl)R_SC_USER_ATTRImpl.getObjectByKey(loSearchReq,rowToBeAdded.getSession() );

      if ( loUserAttr != null)
      {
         rowToBeAdded.getData("EXT_DIR_ID").setString(loUserAttr.getATTR_VAL());
      }
   }

//END_COMP_EVENT_beforeResultsetFill}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
	addTransactionEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

   public void myInitializations()
   {
      setSecurityOrgPrefixes( "HOME" ) ;
   }



   /**
    * Checks if a password is given if either reporting modes
    * are enabled and Business Objects integration is enabled.
    */
   public void checkBOPwdRequired()
   {

   }

   /**
    * Returns true if any of the Org. codes are modified.
    */
   protected boolean isHomeOrgCodesModified()
   {
      return ( getData( "HOME_GOVT_BRN_CD" ).modified()  ||
               getData( "HOME_CAB_CD" ).modified()  ||
               getData( "HOME_DEPT_CD" ).modified() ||
               getData( "HOME_DIV_CD" ).modified()  ||
               getData( "HOME_GP_CD" ).modified()   ||
               getData( "HOME_SECT_CD" ).modified() ||
               getData( "HOME_DSTC_CD" ).modified() ||
               getData( "HOME_BUR_CD" ).modified()  ||
               getData( "HOME_UNIT_CD" ).modified() ) ;
   }


   /**
    * Updates the profile of the Business Objects User for example
    * change in primary group information, standard or power user
    * reporting flag.  If the user does not exist in the Business
    * Objects Repository, a new one with the passed profile and
    * group information is created.
    */
   private final void updateBOUser()
   {
      /*
       * If BusinessObjects integration enabled, update user
       * in Business Objects system
       */
      if ( AMSParams.mboolIsBOIntegrationEnabled )
      {

         /*
          * Only if a standard or power reporting user flag enabled
          * Standard reporting user = BO profile User
          * Power reporting user    = BO profile User/ Designer
          */
         if ( getSTD_RPT_USER_FL() || getPWR_RPT_USER_FL() )
         {
            try
            {
               /*
                * start with at least profile - USER
                * if power reporting user - upgrade to DESIGNER_USER
                */
               int liReportingProfile =
                 AMSBusinessObjectsIntegrationConstants.BO_PROFILE_USER;


               if( getPWR_RPT_USER_FL() )
               {
                  liReportingProfile =
                    AMSBusinessObjectsIntegrationConstants.BO_PROFILE_DESIGNER;
               }

               BOUser loNewUser = new BOUser(getUSER_ID(),
                                             liReportingProfile,
                                             getCvlRptGpDisplayNewGroup(getPRIM_RPT_GP()),
                                             getCvlRptGpDisplayOldGroup(getOldPRIM_RPT_GP()));

               String lsBaseBOWebServerURL = AMSParams.msBOServerName;


               /*
                * Create a new user in the infoADVANTAGE system
                */
               InfoADVResponse loResponse = InfoADVClientUtil. createUser(loNewUser,
                                                                    lsBaseBOWebServerURL,
                                                                    getSAMLToken(getSession()));


               if (loResponse.getStatus() ==InfoADVResponse.STATUS_FAILED)
               {
                   raiseException("Could not create new infoADVANTAGE user in the " +
                   " Business Objects Server:"+ loResponse.getRetMsg()) ;
               } //if (loR.getStatus() ==InfoADVResponse.STATUS_FAILED)


            }//try
            catch(Exception loExp)
            {
                raiseException("Could not create new infoADVANTAGE user in the " +
                   " Business Objects Server:"+ loExp.getMessage());
            }
         }//if ( getSTD_RPT_USER_FL() || getPWR_RPT_USER_FL() )

      }//if ( AMSParams.mboolIsBOIntegrationEnabled )
   } // of method updateBOUser


   private final void deleteBOUser()
   {
      /*
       * If BusinessObjects integration enabled, delete user
       * in Business Objects system
       */
      if ( AMSParams.mboolIsBOIntegrationEnabled )
      {

         /*
          * Only if a standard or power reporting user flag enabled
          * Standard reporting user = BO profile User
          * Power reporting user    = BO profile User/ Designer
          */
         if ( getSTD_RPT_USER_FL() || getPWR_RPT_USER_FL() )
         {
            try
            {
               /*
                * start with at least profile - USER
                * if power reporting user - upgrade to DESIGNER_USER
                */
               int liReportingProfile =
                 AMSBusinessObjectsIntegrationConstants.BO_PROFILE_USER;


               if( getPWR_RPT_USER_FL() )
               {
                  liReportingProfile =
                    AMSBusinessObjectsIntegrationConstants.BO_PROFILE_DESIGNER;
               }

              BOUser loNewUser = new BOUser(getUSER_ID(),
                                             liReportingProfile,
                                             getCvlRptGpDisplayNewGroup(getPRIM_RPT_GP()),
                                             getCvlRptGpDisplayOldGroup(getOldPRIM_RPT_GP()));
               String lsBaseBOWebServerURL = AMSParams.msBOServerName;


               /*
                * Delete the user in the infoADVANTAGE system
                */
               InfoADVResponse loResponse = InfoADVClientUtil. deleteUser(loNewUser,
                                                                    lsBaseBOWebServerURL,
                                                                    getSAMLToken(getSession()));
               if (loResponse.getStatus() ==InfoADVResponse.STATUS_FAILED)
               {
                   raiseException("Could not delete infoADVANTAGE user in the " +
                   " Business Objects Server:"+ loResponse.getRetMsg()) ;
               } //if (loR.getStatus() ==InfoADVResponse.STATUS_FAILED)


            }//try
            catch(Exception loExp)
            {
                raiseException("Could not delete infoADVANTAGE user in the " +
                   " Business Objects Server:"+ loExp.getMessage());
            }
         }//if ( getSTD_RPT_USER_FL() || getPWR_RPT_USER_FL() )

      }//if ( AMSParams.mboolIsBOIntegrationEnabled )
   } // of method deleteBOUser


   /**
    * Returns the SAML token corresponding to the session
    */
   private final String getSAMLToken( Session foSession )
         throws Exception
   {

      try
      {
         AMSSecurityObject      loSecObj =null;

         loSecObj =
         (AMSSecurityObject)((VSORBSessionImpl)foSession).getServerSecurityObject() ;

         return loSecObj.createCSFToken();
      }
      catch(Exception loExp)
      {

         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);


      }

      return null;

   } //getSAMLToken

   /*
    *This function read in key value of CVL table CVL_RPT_GP and get it's Display value
    */
   private String getCvlRptGpDisplayOldGroup(int fiRptGpId)
   {
      String lsRptGpID = fiRptGpId + "";

      if ((lsRptGpID == null) || (lsRptGpID.trim().length() == 0) || (lsRptGpID.trim().equals("0")))
      {
         return "NEWUSER";
      }

      String lsPrimRptGpDisplayValue = "";
      CVL_RPT_GPImpl loCvlRptGp = null;
      SearchRequest lsr = new SearchRequest();

      lsr.addParameter("CVL_RPT_GP","RPT_GP_ID",Integer.toString(fiRptGpId));
      loCvlRptGp = (CVL_RPT_GPImpl)CVL_RPT_GPImpl.getObjectByKey(lsr,getSession());
      if(loCvlRptGp != null)
      {
         lsPrimRptGpDisplayValue = loCvlRptGp.getRPT_GP_NM();
      }

      return lsPrimRptGpDisplayValue;
   }

   /*
    *This function read in key value of CVL table CVL_RPT_GP and get it's Display value
    */
   private String getCvlRptGpDisplayNewGroup(int fiRptGpId)
   {
      String lsRptGpID = fiRptGpId + "";

      if ((lsRptGpID == null) || (lsRptGpID.trim().length() == 0) || (lsRptGpID.trim().equals("0")))
      {
         raiseException("Please select a Primary Reporting Group for the user");
      }

      String lsPrimRptGpDisplayValue = "";
      CVL_RPT_GPImpl loCvlRptGp = null;
      SearchRequest lsr = new SearchRequest();

      lsr.addParameter("CVL_RPT_GP","RPT_GP_ID",Integer.toString(fiRptGpId));
      loCvlRptGp = (CVL_RPT_GPImpl)CVL_RPT_GPImpl.getObjectByKey(lsr,getSession());
      if(loCvlRptGp != null)
      {
         lsPrimRptGpDisplayValue = loCvlRptGp.getRPT_GP_NM();
      }

      return lsPrimRptGpDisplayValue;
   }

   /**
    * Expires the password of the User(Bad Logins Count is also reset to 0).
    * Additionally sends an email notifying the User of the same, provided there is an email
    * address for the User.
    * @param fboolIsNewUser - True, if new user. False if existing user is being updated.
    */
    public void expireNewPasswordWithNotification(boolean fboolIsNewUser)
    {
      try
      {
         if( AMSParams.miAuthenticationType == AMSParams.TYPE_DB )
         {
            R_SC_USER_DIR_INFOImpl loUserDirInfo =getUsersDirectoryInformation();

            //Retrieve email address of user.
            String lsEmailAddr = loUserDirInfo.getEMAIL_AD_TXT() ;

            //If email address not specified for user, raise exception.
            if (AMSStringUtil.strIsEmpty(lsEmailAddr))
            {
               raiseException("Email notification cannot be sent since email address does not exist",
                     AMSMsgUtil.SEVERITY_LEVEL_INFO);
            }

            AMSSecurity.expireNewPassword(loUserDirInfo.getUSER_ID(),msClrTxtPassword,lsEmailAddr,
                  fboolIsNewUser, getSession() );
            if (!getEXPIRE_NEW_PSWD_FL() )
            {
               raiseException("The new password set shall be expired as "
                     + "Expire New Password flag has been checked on the SCCNFG table",
                     AMSMsgUtil.SEVERITY_LEVEL_INFO);
            }
         }
         else if( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP )
         {
            AMSSecurity.expireNewLDAPPassword(getUSER_ID());
         }//end if

      } /* end try */
      catch( Exception foExp )
      {
        moAMSLog.error( "Unable to send user e-mail", foExp ) ;
      } /* end catch( Exception foExp ) */
    } /* end expireNewPassword() */

    /**
    * Formula for field EXPIRE_NEW_PSWD_FL
    */
    public boolean getInputFromUser()
    {
        /** This field is non persisten and we will be using column index
        * to get its value. We cant use getColumnName method because it will
        * go into infinite loop and we dont want to return dummy value because
        * if value is set from page then it will be overwritten by dummy value.
        */
          return getData(miEXPIRE_NEW_PSWD_FL).getboolean();
    }

    /**
    * Formula for field DONT_EXPIRE_PSWD_FL
    */
    public boolean getDontExpirePswd()
    {
        /** This field is non persisten and we will be using column index
        * to get its value. We cant use getColumnName method because it will
        * go into infinite loop and we dont want to return dummy value because
        * if value is set from page then it will be overwritten by dummy value.
        */
          return getData(miDONT_EXPIRE_PSWD_FL).getboolean();
    }
   /**
    * This method sets the user security realm field to the default
    * value if its value is not set.
    * Date: 1/6/06
    * Hung Giang
    *
    */
   private void setUserSecRealm()
   {
      try
      {
         String lsUserSecurityRealm = getUSER_SEC_REALM();

         if ( lsUserSecurityRealm == null )
         {
            setUSER_SEC_REALM( AMSSecurity.getDefaultSecurityRealm() );
         }
      }
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);



         raiseException( loExp.getMessage() ) ;
      }
   }//end of setUserSecRealm()


   /*
   * This method will check whether the record for particular user Id exist or not.
   */
   private boolean doesUserInfoExist ( String fsUserId )
   {
      SearchRequest lsrUserInfoRec  = null;
      int liRecCount = 0;
      boolean lboolIsRecExist = false;

      if( !AMSStringUtil.strIsEmpty(fsUserId) )
      {
         lsrUserInfoRec = new SearchRequest();
         lsrUserInfoRec.addParameter("R_SC_USER_INFO", "USER_ID_UP", fsUserId);
         liRecCount = R_SC_USER_INFOImpl.getObjectCount(lsrUserInfoRec, getSession());

         if ( liRecCount >= 1 )
         {
            lboolIsRecExist = true;
         }
      }
      return lboolIsRecExist ;
   }

   /**
    * If either MSS User Type or MSS Application (in the application list) is selected the other is selected. If not raise an error
    *
    */
   private void validateMSSUserTyp()
   {
      if (getMSS_USER_TYP() == 0 &&  (( getAPPL_AUTH() & ( 1 << ( AMSCommonConstants.MSS_APPL - 1 ) ) ) == AMSCommonConstants.APPL_AUTH_MSS) ||
         getMSS_USER_TYP() != 0 && (( getAPPL_AUTH() & ( 1 << ( AMSCommonConstants.MSS_APPL - 1 ) ) ) != AMSCommonConstants.APPL_AUTH_MSS)
         )
      {
         raiseException("%c:Q0173%");
      }

   }

   /**
    * If the ESS User Type field is populated, then the ESS application must also be selected and vice versa else raise an error.
    *
    */
   private void validateESSUserTyp()
   {
      if (getESS_USER_TYP() == 0 &&  (( getAPPL_AUTH() & ( 1 << ( AMSCommonConstants.ESS_APPL - 1 ) ) ) == AMSCommonConstants.APPL_AUTH_ESS) ||
               getESS_USER_TYP() != 0 && (( getAPPL_AUTH() & ( 1 << ( AMSCommonConstants.ESS_APPL - 1 ) ) ) != AMSCommonConstants.APPL_AUTH_ESS)
         )
      {
         raiseException("%c:Q0174%");
      }

   }


   /**
    * MSS User Type on User Information record can be mapped to multiple Security Roles on MSS
    * Security Roles page.
    * When a MSS User Type is associated with a particular User for the first time, then the User
    * is assigned all the Security Role(s) that is associated with the MSS User Type on MSS
    * Security Roles page.
    * When MSS User Type is changed for a particular User, then all the Security Roles associated
    * with the old MSS User Type are revoked from the User and all the Security Roles associated
    * with the new MSS User Type are assigned to the User. Assignment and revocation of Security
    * Roles to the User in all these scenarios should be logged on Historical Tracking table.
    * Each Historical Tracking record for R_SC_MSS_ROLE_LNK tracks the assignment & revocation
    * of a Security Role to a User.
   */
   protected void updateHistTrackerForMSSRoleChanges()
   {
      /*
       * In order to enable logging assignment/revocation actions on Historical Tracking table,
       * all of the following conditions should be met:
       * 1. Application Control parameter IN_HIST_TRACK should be true(case insensitive)
       * 2. Extended Property HIST_TRACKING on R_SC_MSS_ROLE_LNK DataObject is set to Y
       *    (case-insensitive).
       */
      boolean lboolTrack = "true".equalsIgnoreCase(
                  IN_APP_CTRLImpl.getParameterValue("IN_HIST_TRACK", getSession())) &&
            "Y".equalsIgnoreCase(
                  R_SC_MSS_ROLE_LNKImpl.getMetaQuery().getProperty(PROPERTY_HIST_TRACKING));

      if(lboolTrack)
      {

         // Get Revoke Reason from the Session. The value is only populated when action revokes
         // Security Role from a User.
         String lsRevokeReason = getSession().getProperty(SESSION_REVOKE_REASON);

         //Case where User Information record had old value on MSS User Type
         if( getOldMSS_USER_TYP() != 0 )
         {
              //Get all the Security Roles mapped to the old MSS User Type from MSS Security Roles table
              Enumeration<R_SC_MSS_ROLE_LNKImpl>  lenumRole = R_SC_MSS_ROLE_LNKImpl.getMSSRolesForMSSUserType(
                    getOldMSS_USER_TYP(), getSession());

            /*
             * For each Security Role associated with the old MSS User Type, get existing
             * Historical Tracking record that tracked the assignment of the Security Role
             * to the User and update the Revocation details on it.
             */
            while( lenumRole.hasMoreElements() )
            {
               String lsSecRole = lenumRole.nextElement().getSEC_ROLE_ID();
               SearchRequest lsrSearch = new SearchRequest();
               StringBuffer lsbSearch = new StringBuffer();
               lsbSearch.append(" ENTITY_ID ").append(AMSSQLUtil.getANSIQuotedStr(getUSER_ID(), AMSSQLUtil.EQUALS_OPER));
               lsbSearch.append(" AND RESOURCE_ID ").append( AMSSQLUtil.getANSIQuotedStr(lsSecRole, AMSSQLUtil.EQUALS_OPER));
               lsbSearch.append(" AND TABLE_NAME ").append(AMSSQLUtil.getANSIQuotedStr("R_SC_MSS_ROLE_LNK", AMSSQLUtil.EQUALS_OPER));
               lsbSearch.append(" AND TO_DT IS NULL ");
               lsrSearch.add(lsbSearch.toString());
               IN_HIST_TRACKImpl loHist = (IN_HIST_TRACKImpl) IN_HIST_TRACKImpl
                     .getObjectByKey(lsrSearch, getSession());

               /*
                * While revocation, if no record exists with blank TO_DT, then it means that
                * 1. The Tracking was not enabled at the time of Day Zero setup or
                * 2. Tracking was enabled at Day Zero but was disabled for sometime.
                * Hence in this case, the Assignment happened at a time when Tracking was turned off.
                * So now, insert a new record with blank FROM_DT and ASSIGN_ADMN_ID.
                */
               if(loHist == null)
               {
                  loHist = IN_HIST_TRACKImpl.getNewObject(getSession(), true);
                  loHist.setENTITY_ID(getUSER_ID());
                  loHist.setRESOURCE_ID(lsSecRole);
                  loHist.setTABLE_NAME("R_SC_MSS_ROLE_LNK");
               }

               //Update Revocation details on the Historical Tracking record
               loHist.setREVOKE_ADMN_ID(getUser());
               loHist.setTO_DT(getDate());
               if(!AMSStringUtil.strIsEmpty(lsRevokeReason))
               {
                  loHist.setREASON(lsRevokeReason);
                  getSession().removeProperty(SESSION_REVOKE_REASON);
               }
               loHist.save();
            }//end looping through Security Roles

         }//end if Old MSS User Type is not blank


         //Case where a new MSS User Type is associated with the User
         if( getMSS_USER_TYP() != 0 )
         {
            //Get all the Security Roles mapped to the new MSS User Type from MSS Security Roles table
            Enumeration<R_SC_MSS_ROLE_LNKImpl>  lenumRole = R_SC_MSS_ROLE_LNKImpl.getMSSRolesForMSSUserType(
                  getMSS_USER_TYP(), getSession());

            /*
             * For each Security Role associated with the new MSS User Type, add a new
             * Historical Tracking record that will store the assignment details of the
             * Security Role to the User.
             */
            while( lenumRole.hasMoreElements() )
            {
               IN_HIST_TRACKImpl loHist = IN_HIST_TRACKImpl.getNewObject(getSession(), true);
               loHist.setENTITY_ID( getUSER_ID() );
               loHist.setRESOURCE_ID( lenumRole.nextElement().getSEC_ROLE_ID() );
               loHist.setDEPT_CD( getHOME_DEPT_CD() );
               loHist.setASSIGN_ADMN_ID( getUser() );
               loHist.setFROM_DT( getDate() );
               loHist.setTABLE_NAME( "R_SC_MSS_ROLE_LNK" );
               if(!AMSStringUtil.strIsEmpty(lsRevokeReason))
               {
                  loHist.setREASON(lsRevokeReason);
                  getSession().removeProperty(SESSION_REVOKE_REASON);
               }
               loHist.save();

            }//end looping through Security Roles
         }//end if new value of MSS User Type is not blank
      }//end if Historical Tracking is enabled

   }//end of method

   /**
    * This method is the main method which inserts a record
    * into the R_SC_USER_ATTR table.
    */
   public void insertUserAttr()
   {
      try
      {
         SearchRequest loSearchReq = new SearchRequest();
         loSearchReq.addParameter("R_SC_USER_ATTR", "USER_ID" , this.getUSER_ID() );
         loSearchReq.addParameter("R_SC_USER_ATTR", "ATTR_NM" , AMSCommonConstants.ATTR_NM_EXTARNAL_ID );

         R_SC_USER_ATTRImpl loUserAttr =
            (R_SC_USER_ATTRImpl)R_SC_USER_ATTRImpl.getObjectByKey(loSearchReq , this.getSession());

         if ( loUserAttr == null)
         {
             loUserAttr =
               (R_SC_USER_ATTRImpl)R_SC_USER_ATTRImpl.getNewObject(this.getSession(),true);
            loUserAttr.setUSER_ID(this.getUSER_ID());
            loUserAttr.setATTR_NM(AMSCommonConstants.ATTR_NM_EXTARNAL_ID);
            loUserAttr.setATTR_VAL(this.getEXT_DIR_ID());

         }
         else
         {
            loUserAttr.setATTR_VAL(this.getEXT_DIR_ID());

         }
         loUserAttr.save();
      }
      catch(Exception foexception)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foexception);

      }
   }

   /**
    * This method is added to stop the Formula call to
    * go on an endless loop.
    */
   public String getExternalId()
   {
      return getData(miEXT_DIR_ID).getString();
   }

   /**
    * Checks if the MSS User Type assigned to the current User has Security Roles that is defined
    * as restricted on Restricted Security Role Pair table. If yes, logs error.
    */

   protected void validateSecurityRolesforMSSUserTyp()
   {
      // Call method to check if the MSS User type has any Restriceted Security Role Pair
      if( !R_SC_MSS_ROLE_LNKImpl.checkResRolePairsForMSSUserTyp( getSession(), getMSS_USER_TYP() ) )
      {
         raiseException( "%c:Q0203%" );
      }
   }
/**
    * This method returns User Information records for Users granted the specified Security Roles.
    * The records can be optionally limited to only users in active status.
    * @param foSession - The current session
    * @param fsSecurityRoleID - Security Role ID granted to Users
    * @param fboolLimitToActiveUsers - Indicates whether selection should be limited to active Users
    * @return Iterator of Users granted the specificed Security Roles.
    */
   protected static Iterator getUsersForRole( Session foSession, String fsSecurityRoleID, boolean fboolLimitToActiveUsers )
   {
      Vector lvUsersForRole = new Vector() ;
      R_SC_USER_ROLE_LNKImpl loUserRoleLink = null ;
      R_SC_USER_INFOImpl loUserInfo = null ;
      SearchRequest loSearchReq = new SearchRequest();
      loSearchReq.addParameter( "R_SC_USER_ROLE_LNK", "SEC_ROLE_ID", fsSecurityRoleID ) ;
      Enumeration loUsersForRole = loUserRoleLink.getObjects( loSearchReq, foSession ) ;

      while ( loUsersForRole.hasMoreElements() )
      {
         loUserRoleLink
            = ( R_SC_USER_ROLE_LNKImpl ) loUsersForRole.nextElement() ;
         loUserInfo
            = getUserInfoRec(
                  foSession,
                  loUserRoleLink.getUSER_ID() ) ;
         if ( loUserInfo != null
             && ( loUserInfo.getLOCK_FL() == CVL_USER_LOCK_STAImpl.ACTIVE
                 || !fboolLimitToActiveUsers
                )
            )
         {
            lvUsersForRole.add( loUserInfo ) ;
         }
      }
      return lvUsersForRole.iterator() ;
   }

   /**
    * This method will return an instance of User Information
    * record for the given User ID.
    * It will return null when passed User is invalid and
    * could not be found on User Information.
    * @param foSession - The current session
    * @param fsUserID - User ID for lookup
    * @return R_SC_USER_INFOImpl
    */
   public static R_SC_USER_INFOImpl getUserInfoRec( Session foSession,
      String fsUserID )
   {
      if( fsUserID == null )
      {
         return null ;
      }
      SearchRequest loSearchRequest = new SearchRequest() ;
      loSearchRequest.addParameter("R_SC_USER_INFO", "USER_ID", fsUserID ) ;
      return( (R_SC_USER_INFOImpl)R_SC_USER_INFOImpl.getObjectByKey( loSearchRequest, foSession) );
   }

}
