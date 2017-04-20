//{{IMPORT_STMTS
package advantage.Admin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  pR_DOC_COMP_RQMTS_Generic*/

//{{FORM_CLASS_DECL
public class pR_DOC_COMP_RQMTS_Generic extends pR_DOC_COMP_RQMTS_GenericBase

//END_FORM_CLASS_DECL}}
{
	// Declarations for instance variables used in the form

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pR_DOC_COMP_RQMTS_Generic ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}



	//{{EVENT_CODE
	//{{EVENT_T1R_DOC_COMP_RQMTS_afterSave
void T1R_DOC_COMP_RQMTS_afterSave(VSRow row )
{
	//Write Event Code below this line
	row.refresh();
}
//END_EVENT_T1R_DOC_COMP_RQMTS_afterSave}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1R_DOC_COMP_RQMTS.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1R_DOC_COMP_RQMTS) {
			T1R_DOC_COMP_RQMTS_afterSave(row );
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