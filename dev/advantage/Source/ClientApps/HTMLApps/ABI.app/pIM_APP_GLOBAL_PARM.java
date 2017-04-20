//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  pIM_APP_GLOBAL_PARM
*/

//{{FORM_CLASS_DECL
public class pIM_APP_GLOBAL_PARM extends pIM_APP_GLOBAL_PARMBase

//END_FORM_CLASS_DECL}}
{

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_APP_GLOBAL_PARM ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_APP_GLOBAL_PARM_beforeGenerate
void pIM_APP_GLOBAL_PARM_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   TextAreaElement  loTextAreaElement  = null;
   TextFieldElement loTextFieldElement = null;
   CheckboxElement  loCheckboxElement  = null;

   // Set length of Name from 20 to 48.
   loTextFieldElement = (TextFieldElement) getElementByName("txtT1PARM_NM");
   if (loTextFieldElement != null)
   {
      loTextFieldElement.setSize(48);
   }

   // Set length of Short Description from 20 to 48.
   loTextFieldElement = (TextFieldElement) getElementByName("txtT1PARM_SH_DESC");
   if (loTextFieldElement != null)
   {
      loTextFieldElement.setSize(48);
   }

   // Set columns of Value from 35 to 45.
   loTextAreaElement = (TextAreaElement) getElementByName("txtT1PARM_VL");
   if (loTextAreaElement != null)
   {
      loTextAreaElement.setCols(45);
   }

   // Set columns of Description from 35 to 45.
   loTextAreaElement = (TextAreaElement) getElementByName("txtT1PARM_DESC");
   if (loTextAreaElement != null)
   {
      loTextAreaElement.setCols(45);
   }

}
//END_EVENT_pIM_APP_GLOBAL_PARM_beforeGenerate}}

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
			pIM_APP_GLOBAL_PARM_beforeGenerate(docModel, cancel, output);
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