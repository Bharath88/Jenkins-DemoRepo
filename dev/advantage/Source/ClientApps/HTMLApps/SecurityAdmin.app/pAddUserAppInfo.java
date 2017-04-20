//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.*;
import java.util.*;
import advantage.AMSStringUtil;
import org.apache.commons.logging.*;
/*
**  pAddUserAppInfo*/

//{{FORM_CLASS_DECL
public class pAddUserAppInfo extends pAddUserAppInfoBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   /* This is the logger object for the class */
   private static Log moLog = AMSLogger.getLog( pAddUserAppInfo.class,
            AMSLogConstants.FUNC_AREA_SECURITY ) ;
   private boolean mboolExistLDAPUser = false ;
   private boolean mboolFirstGenerate = true ;
   private ComboBoxElement    moUserSecRealm ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pAddUserAppInfo ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_pAddUserAppInfo_beforeActionPerformed
void pAddUserAppInfo_beforeActionPerformed( ActionElement foActElem,
      PageEvent foPageEvt, PLSRequest foPLSReq )
{
   String lsAction = foActElem.getAction() ;
   String lsActnNm = foActElem.getName() ;
   String lsExistLDAPUser = foPLSReq.getParameter( "EXIST_LDAP_USER_FL" ) ;

   if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP &&
        lsExistLDAPUser != null && lsExistLDAPUser.length() > 0 )
   {
      mboolExistLDAPUser = true ;
   }
   else
   {
      mboolExistLDAPUser = false ;
   }

   if ( lsAction.equals( ActionElement.db_saveall ) )
   {
      saveUserInfo( foPageEvt, foPLSReq ) ;
      foPageEvt.setNewPage( this ) ;
      foPageEvt.setCancel( true ) ;
   } /* end if ( lsAction.equals( ActionElement.db_saveall ) ) */
   else if ( foActElem.getName().equals( "btnT2pUserRoleLink" ) )
   {
      boolean lboolSaveOK ;

      lboolSaveOK = saveUserInfo( foPageEvt, foPLSReq ) ;

      if ( !lboolSaveOK )
      {
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end if ( !lboolSaveOK ) */
   } /* end else if ( foActElem.getName().equals( "btnT2pUserRoleLink" ) ) */
}
//END_EVENT_pAddUserAppInfo_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pAddUserAppInfo_beforeActionPerformed( ae, evt, preq );
		}
	}
//END_EVENT_ADAPTER_CODE}}

      public HTMLDocumentSpec getDocumentSpecification() {
            return getDefaultDocumentSpecification();
      }

        public String getFileName() {
          return getDefaultFileName();
       }
      public String getFileLocation() {
         return getPageTemplatePath();
          }

   public void afterPageInitialize() {
      super.afterPageInitialize();
      //Write code here for initializing your own control
      //or creating new control.

   }

   /**
    * This method will save both the new R_SC_USER_DIR_INFO and
    * R_SC_USER_INFO records in one transaction to avoid possible
    * problems if the R_SC_USER_INFO record is not saved but the
    * R_SC_USER_DIR_INFO record is saved.
    *
    * @param foPageEvt The page event
    * @param foPLSReq The PLS request
    * @return Indicates if the save was successful
    */
   private boolean saveUserInfo( PageEvent foPageEvt, PLSRequest foPLSReq )
   {
      boolean         lboolSaveOK    = false ;
      String          lsUserID       = null;
      String          lsAppPwd       = foPLSReq.getParameter( "txtT1PSWD_TXT" ) ;
      String          lsConfAppPwd   = foPLSReq.getParameter( "txtT1PSWD_TXT_CNFRM" ) ;
      String          lsEmailPwd     = foPLSReq.getParameter( "txtT1EMAIL_PSWD_TXT" ) ;
      String          lsConfEmailPwd = foPLSReq.getParameter( "txtT1EMAIL_PSWD_TXT_CNFRM" ) ;
      String          lsBOPwd        = foPLSReq.getParameter( "txtT1BO_PSWD_TXT" ) ;
      String          lsConfBOPwd    = foPLSReq.getParameter( "txtT1BO_PSWD_TXT_CNFRM" ) ;
      String          lsHomeDeptCd   = foPLSReq.getParameter( "txtT1HOME_DEPT_CD" ) ;
      AMSPage         loSrcPage      = (AMSPage)getSourcePage() ;
      pAddUserDirInfo loDirInfoPage  = null ;

      if ( (loSrcPage==null) || (!(loSrcPage instanceof pAddUserDirInfo) ) )
      {
         raiseException( "Unable to retrieve user directory information", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
         return false ;
      } /* end if ( ( loSrcPage == null ) || ( ! ( loSrcPage instanceof pAddUserDirInfo ) ) ) */
      else
      {
         loDirInfoPage = (pAddUserDirInfo)loSrcPage ;
         if ( loSrcPage.isClosed() )
         {
            raiseException( "Unable to save user directory information", SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
            return false ;
         } /* end if ( loSrcPage.isClosed() ) */
      } /* end else */
      //Code to check if user Exists in R_SC_USER_DIR_INFO

      lsUserID  = loDirInfoPage.T1R_SC_USER_DIR_INFO.getCurrentRow().getData("USER_ID").getString();
      if(AMSStringUtil.strIsEmpty(lsUserID))
      {
         raiseException( "User Id is required", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      }

      if ( ( lsEmailPwd != null ) && ( lsEmailPwd.trim().length() > 0 ) )
      {
         if ( ( lsConfEmailPwd == null ) || ( lsConfEmailPwd.trim().length() < 1 ) )
         {
            raiseException( "Please confirm the email password.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end if ( ( lsConfEmailPwd == null ) || ( lsConfEmailPwd.trim().length() < 1 ) ) */
         else if ( !lsEmailPwd.equals( lsConfEmailPwd ) )
         {
            raiseException( "The email password does not match with the confirmation.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end else if ( !lsEmailPwd.equals( lsConfEmailPwd ) ) */
      } /* end if ( ( lsEmailPwd != null ) && ( lsEmailPwd.trim().length() > 0 ) ) */

      if ( ( lsBOPwd != null ) && ( lsBOPwd.trim().length() > 0 ) )
      {
         if ( ( lsConfBOPwd == null ) || ( lsConfBOPwd.trim().length() < 1 ) )
         {
            raiseException( "Please confirm the infoADVANTAGE password.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end if ( ( lsConfBOPwd == null ) || ( lsConfBOPwd.trim().length() < 1 ) ) */
         else if ( !lsBOPwd.equals( lsConfBOPwd ) )
         {
            raiseException( "The infoADVANTAGE password does not match with the confirmation.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end else if ( !lsEmailPwd.equals( lsConfEmailPwd ) ) */
      } /* end if ( ( lsBOPwd != null ) && ( lsBOPwd.trim().length() > 0 ) ) */

      if(AMSStringUtil.strIsEmpty(lsHomeDeptCd))
      {
         raiseException("Department is requried",SEVERITY_LEVEL_ERROR);
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      }/* end if(AMSStringUtil.strIsEmpty(lsHomeDeptCd)) */
      
      if ( mboolExistLDAPUser )
      {
         if ( AMSParams.miAuthenticationType != AMSParams.TYPE_LDAP )
         {
            raiseException( "Existing LDAP user flag cannot be checked.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         }

         if ( ( lsAppPwd == null && lsConfAppPwd != null ) ||
              ( lsAppPwd != null && lsConfAppPwd == null ) )
         {
            raiseException( "The password does not match with the confirmation.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end else if ( !lsAppPwd.equals( lsConfAppPwd ) ) */
         else if ( lsAppPwd != null && lsConfAppPwd != null &&
                   !lsAppPwd.equals( lsConfAppPwd ) )
         {
            raiseException( "The password does not match with the confirmation.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end else if ( lsAppPwd != null && lsConfAppPwd != null &&... */

      }
      else if ( !mboolExistLDAPUser )
      {
         if ( ( lsAppPwd == null ) || ( lsAppPwd.trim().length() < 1 ) )
         {
            raiseException( "Please specify the password.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end if ( ( lsAppPwd == null ) || ( lsAppPwd.trim().length() < 1 ) ) */
         else if ( ( lsConfAppPwd == null ) || ( lsConfAppPwd.trim().length() < 1 ) )
         {
            raiseException( "Please confirm the password.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end else if ( ( lsConfAppPwd == null ) || ( lsConfAppPwd.trim().length() < 1 ) ) */
         else if ( !lsAppPwd.equals( lsConfAppPwd ) )
         {
            raiseException( "The password does not match with the confirmation.",
                            SEVERITY_LEVEL_ERROR ) ;
            foPageEvt.setCancel( true ) ;
            foPageEvt.setNewPage( this ) ;
         } /* end else if ( !lsAppPwd.equals( lsConfAppPwd ) ) */
      }

      /* If the event has not been cancelled, then proceed with the save */
      if ( !foPageEvt.isCancel() )
      {
         VSSession loSession       = getParentApp().getSession() ;
         boolean   lboolBeganTrans = false ;
         VSRow loSrcRow = null;
         String lsUserSecRealm = null;
         String lsExpireNewPassword = null;
         try
         {
            if ( !loSession.isTransactionInProgress() )
            {
               lboolBeganTrans = true ;
               loSession.beginTrans() ;
            } /* end if ( !loSession.isTransactionInProgress() ) */

             VSRow loDirInfoData = loDirInfoPage.T1R_SC_USER_DIR_INFO.getCurrentRow();

             //An Info Advantage Requirement Check
             String lsStdRptUserFl =
                foPLSReq.getParameter("txtT1STD_RPT_USER_FL");
             String lsPwrRptUserFl =
                foPLSReq.getParameter("txtT1PWR_RPT_USER_FL");

            //adding code to set the security realm
            loSrcRow = T1R_SC_USER_INFO.getCurrentRow();
            lsUserSecRealm   = foPLSReq.getParameter("txtT1USER_SEC_REALM");
            loSrcRow.getData("USER_SEC_REALM").setString(lsUserSecRealm);
            lsExpireNewPassword = foPLSReq.getParameter("txtT1EXPIRE_NEW_PSWD_FL");

            if (lsExpireNewPassword!=null)
            {
               expireNewPswd();
            }

             /*
             * The below check is given for checking the value of the
             * Standard Reporting User and Power Reporting User.
             * The value of Standard Reporting User and Power Reporting User
             * will return null if they are not checked and it will return the
             * field name(i.e. STD_RPT_USER_FL or PWR_RPT_USER_FL)
             * if any is checked.
             */
             if(!AMSStringUtil.strIsEmpty(lsStdRptUserFl) ||
                !AMSStringUtil.strIsEmpty(lsPwrRptUserFl))
             {
                String lsLastNmTxt  =
                   loDirInfoData.getData("LAST_NM_TXT").getString();
                String lsFrstNmTxt  =
                   loDirInfoData.getData("FRST_NM_TXT").getString();
                String lsEmailAdTxt =
                   loDirInfoData.getData("EMAIL_AD_TXT").getString();

                if(AMSStringUtil.strIsEmpty(lsLastNmTxt) ||
                   AMSStringUtil.strIsEmpty(lsFrstNmTxt) ||
                      AMSStringUtil.strIsEmpty(lsEmailAdTxt))
                {
                   raiseException("Last Name, First Name, and Email Address " +
                      "are required in order to access the infoAdvantage portal."
                      ,SEVERITY_LEVEL_ERROR);
                }//End if()
             }//End if()
             //end of Info Advantage Requirement Check

            loDirInfoPage.T1R_SC_USER_DIR_INFO.updateDataSource() ;
            T1R_SC_USER_INFO.updateDataSource() ;

            if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) )
            {
               loSession.commit() ;
            }//* end if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) ) */
            lboolSaveOK = true ;
         } /* end try */
         catch( Exception loException)
         {
            raiseException( "Unable to add new user", SEVERITY_LEVEL_ERROR ) ;
            moLog.error( "Unable to add new user",
                  loException ) ;
         } /* end catch( Exception loException) */
         finally
         {
            if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) )
            {
               loSession.rollback() ;
            } /* end if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) ) */
         } /* end finally */

      } /* end if ( !foPageEvt.isCancel() ) */

      return lboolSaveOK ;
   } /* end saveUserInfo() */

   /**
    * This method will set the confirm password value to be the password value
    * if password exists. Then generate the html page
    */
   public String generate()
   {
      if ( mboolFirstGenerate )
      {
         mboolFirstGenerate = false ;
         moUserSecRealm = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM" ) ;
      }

      VSRow loCurrentRow = T1R_SC_USER_INFO.getCurrentRow();
      CheckboxElement loCBEExistLDAPUser;

      if(loCurrentRow!=null)
      {
         VSData loPassword = loCurrentRow.getData("PSWD_TXT");
         if(loPassword!=null)
         {
            String lsPassword = loPassword.getString();

            if(lsPassword.length()>0)
            {
               Enumeration loEn = getDocumentModel().getAllElements();
               TextElement loConfPassElem = (TextElement)getElementByName("txtT1PSWD_TXT_CNFRM");
               loConfPassElem.setValue(lsPassword);
            }
         }
      }
      // If the application authentication type is LDAP, then the
      // combobox should be enabled else disabled
      if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
      {
         fetchUserSecRealm(); //populate the combobox
      }
      else
      {
         moUserSecRealm.setEnabled( false );
      }

      loCBEExistLDAPUser = (CheckboxElement) getElementByName( "EXIST_LDAP_USER_FL" );

      // Check or uncheck the EXIST_LDAP_USER_FL checkbox based on
      // the value of the mboolExistLDAPUser variable
      if ( mboolExistLDAPUser )
      {
         loCBEExistLDAPUser.setChecked( true );
      }
      else
      {
         loCBEExistLDAPUser.setChecked( false );
      }

      // If the application authentication type is LDAP, then the
      // checkbox should be enabled else disabled
      if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
      {
         loCBEExistLDAPUser.setEnabled( true );
      }
      else
      {
         loCBEExistLDAPUser.setEnabled( false );
      }

      return super.generate();
   }

   /**
    * This method expires the new user password by setting the PSWD_CHG_DT to the
    * earliest time.
    * @author hgiang
    *
    * TODO To change the template for this generated type comment go to
    * Window - Preferences - Java - Code Style - Code Templates
    */
   private void expireNewPswd()
   {
      VSRow loSrcRow = T1R_SC_USER_INFO.getCurrentRow() ;
      java.util.Date loDate = new java.util.Date();
      loDate.setTime(0);
      loSrcRow.getData("PSWD_CHG_DT").setDate(loDate);
   }

   /**
    * This method fetches the user security realm combo box with values
    * available as specified in the CSF.Properties file
    * @author hgiang
    *
    * TODO To change the template for this generated type comment go to
    * Window - Preferences - Java - Code Style - Code Templates
    */
   private void fetchUserSecRealm()
   {
      String lsUserSecRealms = "";
      String lsUserSecRealm  = "";
      StringTokenizer lstSecurityRealms = null;

      try
      {
         if ( moUserSecRealm.getSize() == 0 )
         {
            lsUserSecRealms = AMSSecurity.getSecurityRealms();
            if ( ( lsUserSecRealms != null ) && ( lsUserSecRealms.trim().length() > 0 ) )
            {
               lstSecurityRealms = new StringTokenizer( lsUserSecRealms, "," );
               while ( lstSecurityRealms.hasMoreTokens() )
               {
                  lsUserSecRealm = lstSecurityRealms.nextToken();
                  moUserSecRealm.addElement(lsUserSecRealm,lsUserSecRealm);
               } //end while
            } //end if ( ( lsUserSecRealms != null )...
            else
            {
               /*
                * when there is no property set for security realm we need to
                * set the IgnoreSetValue flag to true so that it does not set empty string in
                * the comboBox's option tags.
                */
               ((AMSComboBoxElement)moUserSecRealm).setIgnoreSetValue(true);
            }
         } //end if ( moUserSecRealm.getSize() == 0 )
      } //end try
      catch (AMSSecurityException loExp)
      {
         raiseException( "Unable to get the user security realm list", SEVERITY_LEVEL_ERROR ) ;
      } //end catch
   } //end of fetchUserSecRealm()
}
