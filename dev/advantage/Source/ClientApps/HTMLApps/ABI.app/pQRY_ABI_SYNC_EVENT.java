//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import advantage.AMSStringUtil;
import advantage.CVL_EVENT_STATUSImpl;

/*
**  pQRY_ABI_SYNC_EVENT
*/

//{{FORM_CLASS_DECL
public class pQRY_ABI_SYNC_EVENT extends pQRY_ABI_SYNC_EVENTBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pQRY_ABI_SYNC_EVENT ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_pQRY_ABI_SYNC_EVENT_beforeActionPerformed
void pQRY_ABI_SYNC_EVENT_beforeActionPerformed( ActionElement foActnElem, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line

   // If Refresh link is clicked, just update the page with latest data.
   if("T1QRY_ABI_SYNC_EVENT_Refresh".equals(foActnElem.getName()))
   {
      refreshDataSource(T1QRY_ABI_SYNC_EVENT);
   }
   
   /*
    * If Event has already completed successfully or has been scheduled for processing,
    * then we should not allow to change the Status.
    */
   VSRow loRow = T1QRY_ABI_SYNC_EVENT.getCurrentRow();
   if(   loRow != null &&  ( AMSStringUtil.strEqual(CVL_EVENT_STATUSImpl.SUCCESS, 
         loRow.getData("EVENT_STATUS").getString()) ||
         AMSStringUtil.strEqual(CVL_EVENT_STATUSImpl.READY_FOR_PROCESSING, 
               loRow.getData("EVENT_STATUS").getString()) )   )
   {
      getElementByName("txtT1EVENT_STATUS").getHtmlElement()
            .addAttribute("ams_readonly", "ams_readonly");
   }
   else
   {
      getElementByName("txtT1EVENT_STATUS").getHtmlElement()
            .removeAttribute("ams_readonly");
   }

}
//END_EVENT_pQRY_ABI_SYNC_EVENT_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pQRY_ABI_SYNC_EVENT_beforeActionPerformed( ae, evt, preq );
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