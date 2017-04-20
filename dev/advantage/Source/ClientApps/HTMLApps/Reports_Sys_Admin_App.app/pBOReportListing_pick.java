//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import org.apache.commons.logging.Log;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.AMSConfigParentPage;
import infoAdv.ReportUtil;
import versata.vls.ServerEnvironment;
import versata.vls.VSORBSessionImpl;

/*
**  pBOReportListing_pick
*/

//{{FORM_CLASS_DECL
public class pBOReportListing_pick extends pBOReportListing_pickBase

//END_FORM_CLASS_DECL}}
{
   /** Get the logger instance */
   private static Log moLog = AMSLogger.getLog(pBOReportListing_pick.class,
         AMSLogConstants.FUNC_AREA_INFO_REPORTS);

   //This is the  Report ID for the Root folder.
   public static int ROOT_ID = 0;

   //Hidden field ID
   public static String ROOT_ID_CHILDREN = "RootTreeChildren";

   //Hidden field ID
   public static String REPORT_PICK_VALUE = "ReportPickValue";


   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pBOReportListing_pick ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pBOReportListing_pick_beforeActionPerformed
void pBOReportListing_pick_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
      //Write Event Code below this line   
      if ( ae.getName().equals( "returnSelectedReport" ) )
      {
         VSPage loNewPage = ((AMSConfigParentPage) this.getSourcePage()).newItem(preq);
         evt.setCancel(true);
         evt.setNewPage(loNewPage);
      }

}
//END_EVENT_pBOReportListing_pick_beforeActionPerformed}}
//{{EVENT_pBOReportListing_pick_requestReceived
void pBOReportListing_pick_requestReceived( PLSRequest req, PageEvent evt )
{
   //Write Event Code below this line
   acceptData( req ) ;
}
//END_EVENT_pBOReportListing_pick_requestReceived}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pBOReportListing_pick_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pBOReportListing_pick_beforeActionPerformed( ae, evt, preq );
		}
	}
//END_EVENT_ADAPTER_CODE}}

   public HTMLDocumentSpec getDocumentSpecification()
   {
      return getDefaultDocumentSpecification();
   }

   public String getFileName()
   {
      return getDefaultFileName();
   }

   public String getFileLocation()
   {
      return getPageTemplatePath();
   }

   public void afterPageInitialize()
   {
      super.afterPageInitialize();
      // Write code here for initializing your own control
      // or creating new control.

   }

   /**
    * This method is overridden in this page java file to set the hidden field
    * "RootTreeChildren" with the initial set of children in a JSON format.
    */
   public String generate()
   {
      VSORBSessionImpl loSession = null;
      try
      {
         loSession = (VSORBSessionImpl) ServerEnvironment.getServer()
               .getExistingSession(
                     parentApp.getSession().getORBSession().getSessionID());
      }
      catch (Exception foException)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ",
               foException);
      }
      String lsReportListJsonData = ReportUtil.generateReportListJsonData(
            loSession, ROOT_ID, true);

      HiddenElement loRootSibblings = (HiddenElement) getElementByName(ROOT_ID_CHILDREN);
      loRootSibblings.setValue(lsReportListJsonData);

      String lsHTML = super.generate();

      return lsHTML;
   }
}