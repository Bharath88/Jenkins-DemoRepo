//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import org.apache.commons.logging.Log;

import com.ams.csf.common.CSFException;
import com.ams.csf.common.CSFManager;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.AMSTextContentElement;	

	/*
	**  pR_SC_SEC_CNFG_Scalar
	*/
	
	//{{FORM_CLASS_DECL
	public class pR_SC_SEC_CNFG_Scalar extends pR_SC_SEC_CNFG_ScalarBase
	
	//END_FORM_CLASS_DECL}}
	{
      /** This is the logger object for the class */
      private static Log moLog = AMSLogger.getLog( pCloneUserPage.class, AMSLogConstants.FUNC_AREA_SECURITY ) ;

      public void beforeGenerate()
      {
         try
         {
            AMSTextContentElement loPswdSymbolFlag = (AMSTextContentElement)getElementByName("pwdrqdflag");
            loPswdSymbolFlag.setCustomValue("(" +(String) CSFManager.getInstance().getPreferences().
                  getPreference("SYSTEM", "PASSWORD_SPECIAL_CHARSET") + ")" +" : " );

         }
         catch (CSFException loCSFException)
         {
            moLog.error("Error in obtaining password special character set");
            if(moLog.isDebugEnabled())
            {
               moLog.debug("Error in obtaining password special character set", loCSFException);
            }
         }
      }
		// Declarations for instance variables used in the form
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public pR_SC_SEC_CNFG_Scalar ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
	
	
	}