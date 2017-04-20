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
	**  Report_Driver_Frameset*/
	
	//{{FORM_CLASS_DECL
	public class Report_Driver_Frameset extends Report_Driver_FramesetBase
	
	//END_FORM_CLASS_DECL}}
	{
	   // Agent ID for a report
	   private String msAgentID = null ;
	
	   // Declarations for instance variables used in the form
	
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Report_Driver_Frameset ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }
	
	
	
	//{{EVENT_CODE
	//{{EVENT_Report_Driver_Frameset_beforeActionPerformed
void Report_Driver_Frameset_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
}
//END_EVENT_Report_Driver_Frameset_beforeActionPerformed}}

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
			Report_Driver_Frameset_beforeActionPerformed( ae, evt, preq );
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
	
	   public void setAgentID( String fsAgentID )
	   {
	      msAgentID = fsAgentID ;
	   } /* end setAgentID() */
	
	   public String getAgentID()
	   {
	      return msAgentID;
	   } /* end getPrimaryGroupID() */
	
	}