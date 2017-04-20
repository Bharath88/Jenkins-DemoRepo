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
**  pChangePswdHint
*/

//{{FORM_CLASS_DECL
public class pChangePswdHint extends pChangePswdHintBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pChangePswdHint ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
		setMenuName("Scalar");
	}


	//{{EVENT_CODE
	//{{EVENT_pChangePswdHint_beforeActionPerformed
/**
 * Method verifies the values entered and ensures that
 * password resetting is permitted. If password
 * resetting is not permitted, then it throws an exception.
 *
 * Modification Log : Kiran Hiranandani  - 04/03/2003
 *                                      - initial version
 *
 * @param foActnElem the action element clicked
 * @param foPgEvt the page event
 * @param foPLSReq the PLS Request object
 */
void pChangePswdHint_beforeActionPerformed( ActionElement foActnElem,
      PageEvent foPgEvt, PLSRequest foPLSReq )
{
	//Write Event Code below this line
      String lsPswd     = null ;
      String lsPswdHint = null ;
      String lsNewReply = null ;
      String lsVfyReply = null ;

      if ( foActnElem.getName().equalsIgnoreCase( "changePasswordHint" ) )
      {
         lsPswd     = foPLSReq.getParameter( "userPassword" ) ;
         lsPswdHint = foPLSReq.getParameter( "passwordHint" ) ;
         lsNewReply = foPLSReq.getParameter( "reply" ) ;
         lsVfyReply = foPLSReq.getParameter( "verifyReply" ) ;

         if ( ( lsPswd == null ) || ( lsPswd.length() <= 0 ) )
         {
            raiseException( AMSSecurityException.ERR_PWD_HINT_CHG_FAIL
                  + " Password information not found.",
                  SEVERITY_LEVEL_ERROR ) ;
         }

         if ( ( lsPswdHint == null ) || ( lsPswdHint.length() <= 0 ) )
         {
            raiseException( AMSSecurityException.ERR_PWD_HINT_CHG_FAIL
                  + " Password Hint information not found.",
                  SEVERITY_LEVEL_ERROR ) ;
         }

         changePasswordHint( lsPswd, lsPswdHint, lsNewReply, lsVfyReply ) ;

      } /* end if ( foActnElem.getName().equalsIgnoreCase( "changePasswordHint" ) ) */
      else if ( foActnElem.getName().equalsIgnoreCase( "cancel" ) )
      {
         AMSDynamicTransition loDynTran = null ;
         VSPage               loHomePage ;

         loDynTran = new AMSDynamicTransition( "HomePage", "", "Advantage" ) ;

         loHomePage = loDynTran.getVSPage( getParentApp(), getSessionId() ) ;

         foPgEvt.setNewPage( loHomePage ) ;
         foPgEvt.setCancel( true ) ;
   } /* end else if ( foActnElem.getName().equalsIgnoreCase( "cancel" ) ) */

}
//END_EVENT_pChangePswdHint_beforeActionPerformed}}
//{{EVENT_pChangePswdHint_beforeGenerate
/**
 * Method checks the password resetting is permitted. If
 * password resetting is not permitted, then it throws an
 * exception.
 *
 * Modification Log : Kiran Hiranandani  - 04/03/2003
 *                                      - initial version
 *
 * @param fsDocMode the document model instance
 * @param foCancel for cancelling and returning output set by user
 * @param foOutput returns newly generated output
 */
void pChangePswdHint_beforeGenerate(HTMLDocumentModel foDocModel,
      VSOutParam foCancel , VSOutParam foOutput)
{
	//Write Event Code below this line
   if ( !AMSSecurity.mboolAllowPasswordReset )
   {
      raiseException( "Password resetting is not permitted.", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
      return ;
   } /* end if ( !AMSSecurity.mboolAllowPasswordReset ) */

}
//END_EVENT_pChangePswdHint_beforeGenerate}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pChangePswdHint_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pChangePswdHint_beforeActionPerformed( ae, evt, preq );
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
    * This method calls AMSSecurity.changePasswordHint()
    * once it does some initial verifications
    *
    * Modification Log : Kiran Hiranandani  - 04/03/2003
    *                                      - initial version
    *
    * @param fsPswd  The user password
    * @param fsPswdHint The user's password hint
    * @param fsReply The reply to the password hint
    * @param fsVfyReply The confirmation of the reply
    */
   private void changePasswordHint( String fsPswd, String fsPswdHint,
         String fsReply, String fsVfyReply )
   {
      VSMapSecurityInfo loSecInfo = null;
      String lsErrMsg = null ;

      try
      {
         loSecInfo =
            (VSMapSecurityInfo)parentApp.getSession().getORBSession().getServerSecurityObject();
      } /* end try */
      catch( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException loRemExp ) */

      if ( ( fsReply != null ) && ( fsReply.length() > 0 ) &&
           ( fsVfyReply != null ) && ( fsVfyReply.length() > 0 ) )
      {
         if ( fsReply.equals( fsVfyReply ) )
         {
            try
            {
               AMSSecurity.changePasswordHint( loSecInfo.getLogin(),
                     fsPswd, fsPswdHint, fsReply ) ;

               raiseException( "Password hint was successfully changed.",
                     SEVERITY_LEVEL_INFO ) ;
            } /* end try */
            catch( AMSSecurityException foExp )
            {
               raiseException( foExp.getMessage(), AMSPage.SEVERITY_LEVEL_ERROR ) ;
            } /* end catch( AMSSecurityException foExp ) */
         } /* end if ( fsReply.equals( fsVfyReply ) ) */
         else
         {
            raiseException( AMSSecurityException.ERR_PWD_HINT_CHG_FAIL
                            + " Password Hint Reply and confirm reply do not match.",
                            SEVERITY_LEVEL_ERROR ) ;
         } /* end else */
      } /* end if ( ( lsNewPwd != null ) && ( lsNewPwd.length() > 0 ) && */
      else
      {
         raiseException( AMSSecurityException.ERR_PWD_HINT_CHG_FAIL
                         + " A new password hint was not entered and/or confirmed.",
                         SEVERITY_LEVEL_ERROR ) ;
      } /* end else */
   } /* end changePasswordHint() */

}