//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
	
	/*
	**  pR_SC_FGN_ORG
	*/
	
	//{{FORM_CLASS_DECL
	public class pR_SC_FGN_ORG extends pR_SC_FGN_ORGBase
	
	//END_FORM_CLASS_DECL}}
	{
		// Declarations for instance variables used in the form
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public pR_SC_FGN_ORG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
	
		//{{EVENT_CODE
		
		//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
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
    * This method is used to disable the virtual resultset feature.
    * 
    * @param foDataSource    page data source
    * @return boolean
    */
   public boolean useVirtualResultSet(AMSDataSource foDataSource)
   {
      return false;
   } /* end useVirtualResultSet() */

	
	
	}