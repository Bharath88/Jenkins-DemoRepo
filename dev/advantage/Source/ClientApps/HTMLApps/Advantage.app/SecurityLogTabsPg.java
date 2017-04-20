//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  SecurityLogTabsPg*/

//{{FORM_CLASS_DECL
public class SecurityLogTabsPg extends SecurityLogTabsPgBase

//END_FORM_CLASS_DECL}}
{
	// Declarations for instance variables used in the form

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public SecurityLogTabsPg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}



	//{{EVENT_CODE
	//{{EVENT_T3TableSecurityLog_beforePageNavigation
void T3TableSecurityLog_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
  /*
   * Only 'LOGIN' violations are recorded
   */
  nav.setDevWhere(" (RSRC_ID ='LOGIN') ");
}
//END_EVENT_T3TableSecurityLog_beforePageNavigation}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T3TableSecurityLog.addPageNavigationListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T3TableSecurityLog) {
			T3TableSecurityLog_beforePageNavigation( obj, cancel, newPage );
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