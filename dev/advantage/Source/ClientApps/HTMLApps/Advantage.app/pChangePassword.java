//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.common.AMSUser;
import java.rmi.RemoteException;

/*
**  pChangePassword
*/

//{{FORM_CLASS_DECL
public class pChangePassword extends pChangePasswordBase

//END_FORM_CLASS_DECL}}
{
	// Declarations for instance variables used in the form

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pChangePassword ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
		setMenuName("Scalar");
	}



	//{{EVENT_CODE
	//{{EVENT_pChangePassword_beforeActionPerformed
void pChangePassword_beforeActionPerformed( ActionElement ae, PageEvent evt,
	                                         PLSRequest preq )
{
   String lsOldPwd = null ;
   String lsNewPwd = null ;
   String lsVfyPwd = null ;

   if ( ae.getName().equalsIgnoreCase( "changePassword" ) )
   {
      lsOldPwd = preq.getParameter( "oldPassword" ) ;
      lsNewPwd = preq.getParameter( "newPassword" ) ;
      lsVfyPwd = preq.getParameter( "verifyNewPassword" ) ;

      changePassword( lsOldPwd, lsNewPwd, lsVfyPwd,
                      AMSSecurity.CHANGE_USER_PWD, "User" ) ;

   } /* end if ( ae.getName().equalsIgnoreCase( "changePassword" ) ) */
   else if ( ae.getName().equalsIgnoreCase( "changeEmailPassword" ) )
   {
      lsOldPwd = preq.getParameter( "oldEmailPassword" ) ;
      lsNewPwd = preq.getParameter( "newEmailPassword" ) ;
      lsVfyPwd = preq.getParameter( "verifyNewEmailPassword" ) ;

      changePassword( lsOldPwd, lsNewPwd, lsVfyPwd,
                      AMSSecurity.CHANGE_EMAIL_PWD, "Email" ) ;

   } /* end if ( ae.getName().equalsIgnoreCase( "changeEmailPassword" ) ) */
   else if ( ae.getName().equalsIgnoreCase( "changeBOPassword" ) )
   {
      lsOldPwd = preq.getParameter( "oldBOPassword" ) ;
      lsNewPwd = preq.getParameter( "newBOPassword" ) ;
      lsVfyPwd = preq.getParameter( "verifyNewBOPassword" ) ;

      changePassword( lsOldPwd, lsNewPwd, lsVfyPwd,
                      AMSSecurity.CHANGE_BO_PWD, "infoADVANTAGE" ) ;

   } /* end if ( ae.getName().equalsIgnoreCase( "changeBOPassword" ) ) */
   else if ( ae.getName().equalsIgnoreCase( "cancel" ) ||
             ae.getName().equalsIgnoreCase( "cancelEmailPassword" ) ||
             ae.getName().equalsIgnoreCase( "cancelBOPassword" ) )
   {
      AMSDynamicTransition loDynTran = null ;
      VSPage               loHomePage ;

      loDynTran = new AMSDynamicTransition( "HomePage", "", "Advantage" ) ;

      loHomePage = loDynTran.getVSPage( getParentApp(), getSessionId() ) ;

      evt.setNewPage( loHomePage ) ;
      evt.setCancel( true ) ;
   } /* end else if ( ae.getName().equalsIgnoreCase( "cancel" ) ) */
}
//END_EVENT_pChangePassword_beforeActionPerformed}}

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
			pChangePassword_beforeActionPerformed( ae, evt, preq );
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

   public String generate()
   {
      HiddenElement loShowEmailPwd =
         (HiddenElement)getElementByName( "ShowEmailPwd" ) ;
      HiddenElement loShowBOPwd =
         (HiddenElement)getElementByName( "ShowBOPwd" ) ;

      /*
       * Enable Email password area. This should be changed
       * later on to reflect current user's email capabilities
       * when this information becomes available in the system.
       */
      if ( loShowEmailPwd != null )
      {
         loShowEmailPwd.setValue( "Y" ) ;
      }

      /*
       * Enable Business Objects password area only if integration
       * is enable and user is a reporting user
       */
      if ( loShowBOPwd != null )
      {
         VSMapSecurityInfo loSecInfo = null;

         try
         {
            loSecInfo =
               (VSMapSecurityInfo)parentApp.getSession().getORBSession().getServerSecurityObject();
         }
         catch ( RemoteException loRemExp )
         {
            raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
            return super.generate() ;
         }

         AMSUser loUser = AMSSecurity.getUser( loSecInfo.getLogin() ) ;

         if ( ( loUser != null ) &&
              ( AMSParams.mboolIsBOIntegrationEnabled ) &&
              ( loUser.getStdRptUserFl() || loUser.getPwrRptUserFl() ) )
         {
            loShowBOPwd.setValue( "Y" ) ;
         }
         else
         {
            loShowBOPwd.setValue( "N" ) ;
         }
      }

      return super.generate() ;
   }

   private void changePassword( String fsOldPwd, String fsNewPwd,
                                String fsVfyPwd, int fiChangePwdType,
                                String fsAppName )
   {
      VSMapSecurityInfo loSecInfo = null;
      String lsErrMsg = null ;

      try
      {
         loSecInfo =
            (VSMapSecurityInfo)parentApp.getSession().getORBSession().getServerSecurityObject();
      }
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return ;
      }

      if ( ( fsNewPwd != null ) && ( fsNewPwd.length() > 0 ) &&
           ( fsVfyPwd != null ) && ( fsVfyPwd.length() > 0 ) )
      {
         if ( fsNewPwd.equals( fsVfyPwd ) )
         {
            try
            {
               if ( fiChangePwdType == AMSSecurity.CHANGE_USER_PWD )
               {
                  AMSSecurity.changePassword( loSecInfo.getLogin(),
                                              fsOldPwd, fsNewPwd, null ) ;
               }
               else if ( fiChangePwdType == AMSSecurity.CHANGE_EMAIL_PWD )
               {
                  AMSSecurity.changeEmailPassword( loSecInfo.getLogin(),
                                                   fsOldPwd, fsNewPwd ) ;
               }
               else if ( fiChangePwdType == AMSSecurity.CHANGE_BO_PWD )
               {
                  AMSSecurity.changeBOPassword( loSecInfo.getLogin(),
                                                fsOldPwd, fsNewPwd ) ;
               }
               else
               {
                  raiseException( "Invalid change password type.",
                                  AMSPage.SEVERITY_LEVEL_ERROR ) ;
                  return ;
               }

               raiseException( fsAppName + " password was successfully changed.",
                               SEVERITY_LEVEL_INFO ) ;
            } /* end try */
            catch( AMSSecurityException foExp )
            {
               raiseException( foExp.getMessage(), AMSPage.SEVERITY_LEVEL_ERROR ) ;
            } /* end catch( AMSSecurityException foExp ) */
         } /* end if ( lsNewPwd.equals( lsCnfmNewPwd ) ) */
         else
         {
            raiseException( AMSSecurityException.ERR_PWD_CHG_FAIL
                            + " New password and confirm new password do not match.",
                            SEVERITY_LEVEL_ERROR ) ;
         } /* end else */
      } /* end if ( ( lsNewPwd != null ) && ( lsNewPwd.length() > 0 ) && */
      else
      {
         raiseException( AMSSecurityException.ERR_PWD_CHG_FAIL
                         + " A new password was not entered and/or confirmed.",
                         SEVERITY_LEVEL_ERROR ) ;
      } /* end else */
   }
}