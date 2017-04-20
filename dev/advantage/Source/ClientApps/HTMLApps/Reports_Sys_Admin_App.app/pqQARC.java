//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSComboBoxElement;

    /*
    **  pqQARC
    */

	//{{FORM_CLASS_DECL
	public class pqQARC extends pqQARCBase
	
	//END_FORM_CLASS_DECL}}
    {
      boolean mboolComboBoxIntialized = false;
      //Constants holding values of facilitator status
      //should match with CVL_FCLTR_STATImpl's values
      public static final String DOC_READY_PRINT = "20";
      public static final String DOC_PRINTING = "21";
      public static final String DOC_PRINT_COMP = "22";

        // This is the constructor for the generated form. This also constructs
        // all the controls on the form. Do not alter this code.
		//{{FORM_CLASS_CTOR
		public pqQARC ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
        }


		//{{EVENT_CODE
		//{{EVENT_pqQARC_beforeGenerate
void pqQARC_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
    //Write Event Code below this line
   if (!mboolComboBoxIntialized)
   {
      AMSComboBoxElement loStatusCombo = (AMSComboBoxElement)getElementByName("txtrs01STATUS");
      if (loStatusCombo!=null)
      {
         int liSize = loStatusCombo.getSize();
         for (int liCount=0; liCount<liSize; liCount++)
         {
            Option loOption = loStatusCombo.getElementAt(liCount);
            if (loOption.getValue().equals(DOC_READY_PRINT) ||
                  loOption.getValue().equals(DOC_PRINTING) ||
                  loOption.getValue().equals(DOC_PRINT_COMP))
            {
               //remove any of the batch-print-related status
               loStatusCombo.removeElement(loOption);
               //once removed, decrement size and count accordingly to avoid NPE
               liCount--;
               liSize--;
            }
         }
      }
      //combobox initialized - not to do it again
      mboolComboBoxIntialized = true;
   }
}
//END_EVENT_pqQARC_beforeGenerate}}

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
			pqQARC_beforeGenerate(docModel, cancel, output);
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