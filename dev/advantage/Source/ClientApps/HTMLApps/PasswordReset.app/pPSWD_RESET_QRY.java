//{{IMPORT_STMTS
package advantage.PasswordReset;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.*;

/*
**  pPSWD_RESET_QRY
*/

//{{FORM_CLASS_DECL
public class pPSWD_RESET_QRY extends pPSWD_RESET_QRYBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pPSWD_RESET_QRY ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
   setAllowHistory( false ) ;
	}


	//{{EVENT_CODE
	//{{EVENT_pPSWD_RESET_QRY_beforeActionPerformed
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
void pPSWD_RESET_QRY_beforeActionPerformed( ActionElement foActnElem,
      PageEvent foPgEvt, PLSRequest foPLSReq )
{
	//Write Event Code below this line
      DataSource     loDataSource = getRootDataSource() ;
      VSRow          loRow = loDataSource.getCurrentRow() ;
      String lsPswdReply     = null ;

      if ( foActnElem.getName().equalsIgnoreCase( "resetPassword" ) )
      {
         lsPswdReply = foPLSReq.getParameter( "passwordReply" ) ;

         if ( ( lsPswdReply == null ) || ( lsPswdReply.length() <= 0 ) )
         {
            raiseException( "Password Hint Reply not found.",
                  SEVERITY_LEVEL_ERROR ) ;
         }
         else
         {
            loRow.getData( "PSWD_QUERY_ANSW" ).setString( lsPswdReply ) ;
            loRow.getData( "RESET_FL" ).setBoolean( true ) ;
            loDataSource.updateDataSource() ;

            if ( getHighestSeverityLevel() < AMSPage.SEVERITY_LEVEL_ERROR )
            {
               raiseException( "Your new password has been emailed to the email address on record.",
                     SEVERITY_LEVEL_WARNING ) ;
            }
         }

      } /* end if ( foActnElem.getName().equalsIgnoreCase( "resetPassword" ) ) */
}
//END_EVENT_pPSWD_RESET_QRY_beforeActionPerformed}}

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
			pPSWD_RESET_QRY_beforeActionPerformed( ae, evt, preq );
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


}