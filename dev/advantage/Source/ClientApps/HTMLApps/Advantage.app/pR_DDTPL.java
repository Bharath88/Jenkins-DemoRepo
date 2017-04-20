//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import advantage.AMSStringUtil;
import advantage.IN_APP_CTRLImpl;

/*
 **  pR_DTPL
 */

//{{FORM_CLASS_DECL
public class pR_DDTPL extends pR_DDTPLBase

//END_FORM_CLASS_DECL}}
{  
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pR_DDTPL ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }
   
   
//{{EVENT_CODE
//{{EVENT_pR_DDTPL_beforeGenerate
   void pR_DDTPL_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel  
      ,VSOutParam output)
   {
      //Write Event Code below this line
      if(AMSStringUtil.strEqual(getLastAction(),ActionElement.db_insert))
      {
         String lsMaxDlLines;
         lsMaxDlLines = AMSPLSUtil.getApplParamValue(IN_APP_CTRLImpl.C_IN_APP_MAX_DL_LINES, 
            getParentApp().getSession());
         T1R_DTPL.getCurrentRow().getData("MAX_DL_LINES").
            setInt(Integer.parseInt(lsMaxDlLines));
      }
   }
   
//END_EVENT_pR_DDTPL_beforeGenerate}}

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
			pR_DDTPL_beforeGenerate(docModel, cancel, output);
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
