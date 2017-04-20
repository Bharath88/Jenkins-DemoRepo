//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.client.dbitem.AMSWorkspace;
/*
**  WkgpCustomize
*/

//{{FORM_CLASS_DECL
public class WkgpCustomize extends WkgpCustomizeBase

//END_FORM_CLASS_DECL}}
{
	// Declarations for instance variables used in the form
	

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public WkgpCustomize ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}



	//{{EVENT_CODE
	//{{EVENT_T3pIN_WKSP_afterPageNavigation
void T3pIN_WKSP_afterPageNavigation( PageNavigation nav )
{
	//Write Event Code below this line
	// set Type to System Workspaces
	((pIN_WKSP)nav.getTargetPage()).setType(AMSWorkspace.WKSP_TYPE_SYSTEM);

}
//END_EVENT_T3pIN_WKSP_afterPageNavigation}}
//{{EVENT_T2pIN_WKSP_afterPageNavigation
void T2pIN_WKSP_afterPageNavigation( PageNavigation nav )
{
	//Write Event Code below this line
	// set Type to Workgroup Workspaces
	((pIN_WKSP)nav.getTargetPage()).setType(AMSWorkspace.WKSP_TYPE_WORKGROUP);
	
}
//END_EVENT_T2pIN_WKSP_afterPageNavigation}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T3pIN_WKSP.addPageNavigationListener(this);
	T2pIN_WKSP.addPageNavigationListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void afterPageNavigation( PageNavigation obj ){
		Object source = obj;
		if (source == T3pIN_WKSP) {
			T3pIN_WKSP_afterPageNavigation( obj );
		}
	
		if (source == T2pIN_WKSP) {
			T2pIN_WKSP_afterPageNavigation( obj );
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