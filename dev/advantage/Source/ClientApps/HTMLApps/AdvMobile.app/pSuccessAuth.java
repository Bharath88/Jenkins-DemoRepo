//{{IMPORT_STMTS
package advantage.AdvMobile;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.Enumeration;
import net.sf.json.JSONObject;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;

/*
**  pSuccessAuth
*/

//{{FORM_CLASS_DECL
public class pSuccessAuth extends pSuccessAuthBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pSuccessAuth ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_pSuccessAuth_beforeActionPerformed
void pSuccessAuth_beforeActionPerformed( ActionElement foActionElem, PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if(foActionElem != null)
   {
      String lsAction = foActionElem.getAction();
      AMSDynamicTransition loDynTran = new AMSDynamicTransition(
            "pWorklist",
            "", "AdvMobile");
      loDynTran.setSourcePage(this);
      foPageEvent.setNewPage(loDynTran.getVSPage(getParentApp(), getSessionId()));
      foPageEvent.setCancel(true);
      
   }
   
}
//END_EVENT_pSuccessAuth_beforeActionPerformed}}

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
			pSuccessAuth_beforeActionPerformed( ae, evt, preq );
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


   @Override
   public String generate()
   {
      super.generate();
      Enumeration< BoundElement > loElemEnum = getAllElements();
      JSONObject loResponseObj = new JSONObject();
      while(loElemEnum.hasMoreElements())
      {
         BoundElement loElement = loElemEnum.nextElement();
         if(loElement instanceof HiddenElement)
         {
            loResponseObj.element( loElement.getName(), ( (HiddenElement)loElement ).getValue() );
         }
      }
      
      return loResponseObj.toString();
   }


}