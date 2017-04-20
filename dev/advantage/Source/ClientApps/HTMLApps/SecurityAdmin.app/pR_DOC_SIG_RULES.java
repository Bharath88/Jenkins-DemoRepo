//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSStringUtil;
import advantage.CVL_DOC_SIG_TYPImpl;
import advantage.CVL_WF_OPRSImpl;
import com.amsinc.gems.adv.vfc.html.AMSComboBoxElement;
/*
**  pR_DOC_SIG_RULES
*/

//{{FORM_CLASS_DECL
public class pR_DOC_SIG_RULES extends pR_DOC_SIG_RULESBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pR_DOC_SIG_RULES ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_pR_DOC_SIG_RULES_beforeGenerate
   void pR_DOC_SIG_RULES_beforeGenerate(HTMLDocumentModel foDocModel, VSOutParam foCancel , VSOutParam foOutput)
   {      
      if(mboolFirstTime)
      {
         mboolFirstTime = false;
         //Remove the None signature type. This is important as none is not supported
         //In case if user wants to disable the signature then he needs to simply
         //remove the entry from the page.
         AMSComboBoxElement loSigTypeElement = (AMSComboBoxElement) getElementByName("txtT1DOC_SIG_TYP");
         loSigTypeElement.removeElement("None");
      }

      
}
//END_EVENT_pR_DOC_SIG_RULES_beforeGenerate}}
//{{EVENT_T1R_DOC_SIG_RULES_afterSave
void T1R_DOC_SIG_RULES_afterSave(VSRow foRow )
{
   //Refresh the row so that the combined signature rule will be inferred again.
   foRow.refresh();
}
//END_EVENT_T1R_DOC_SIG_RULES_afterSave}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	T1R_DOC_SIG_RULES.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pR_DOC_SIG_RULES_beforeGenerate(docModel, cancel, output);
		}
	}
	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1R_DOC_SIG_RULES) {
			T1R_DOC_SIG_RULES_afterSave(row );
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

   boolean mboolFirstTime = true;
}