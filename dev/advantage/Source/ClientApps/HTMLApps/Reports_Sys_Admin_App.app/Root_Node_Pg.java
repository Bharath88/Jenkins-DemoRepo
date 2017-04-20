//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	/*
	**  Root_Node_Pg
	*/
	
	//{{FORM_CLASS_DECL
	public class Root_Node_Pg extends Root_Node_PgBase
	
	//END_FORM_CLASS_DECL}}
	{
		// Declarations for instance variables used in the form
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public Root_Node_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
	
		//{{EVENT_CODE
		//{{EVENT_T1BS_CATALOG_PARM_beforeSave
void T1BS_CATALOG_PARM_beforeSave(VSRow row ,VSOutParam cancel )
{
	//Write Event Code below this line
	row.getData("CTLG_ID").setString("0");
}
//END_EVENT_T1BS_CATALOG_PARM_beforeSave}}

		//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	T1BS_CATALOG_PARM.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1BS_CATALOG_PARM) {
			T1BS_CATALOG_PARM_beforeSave(row ,cancel );
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