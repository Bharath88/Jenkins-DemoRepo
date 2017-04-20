//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.amsinc.gems.adv.vfc.html.*;
import advantage.AMSStringUtil;
import java.rmi.RemoteException;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import infoAdv.ReportUtil;
import versata.vls.ServerEnvironment;
import versata.vls.VSORBSessionImpl;
import org.apache.commons.logging.Log;

/*
**  pBOReportListing 
*/

//{{FORM_CLASS_DECL
public class pBOReportListing extends pBOReportListingBase

//END_FORM_CLASS_DECL}}
{

   /** Get the logger instance */
   private static Log moLog = AMSLogger.getLog(pBOReportListing.class,
         AMSLogConstants.FUNC_AREA_INFO_REPORTS);

	//This is the  Report ID for the Root folder.
	public static int ROOT_ID = 0;

	//Hidden fields to store certain fields which can be accessed by GWT.
	public static String ROOT_ID_CHILDREN = "RootTreeChildren";
	public static String SEARCH_TEXT_VAL = "RepoSearchValue";
	public static String REPORT_TREE_HISTORY = "RepoTreeHistory";
	//Variables to store parameters which are set or obtained from GWT.
	private String msSearchValue = null;
	private String msTreeViewHistory = null;
	
	//Search Action which is set from GWT code and is caught here.
	private static String SEARCH_REPORTS = "searchReports";
	
	//Action used to open an InfoAdvantage Report. 
	private static String OPEN_INFO_REPORTS = "OpenInfoReport";
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pBOReportListing ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_pBOReportListing_beforeActionPerformed
void pBOReportListing_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{


	if ( ae.getName().equals(SEARCH_REPORTS))
	{		
		/*
		 * This stores  the search value so that when the user goes back 
		 * to the Report Listing page after opening the report from a 
		 * Grid view the user should go back to the original record
		 * in the tree where the Report link was clicked.  
		 */
	   String lsUIRequest = preq.getParameter("UIRequest");
		JSONObject lsParams = (JSONObject) JSONSerializer.toJSON(lsUIRequest);		
		JSONObject loSearchValue = lsParams.getJSONObject("params");
		
		String lsRestoreState = loSearchValue.getString("RESTORE_STATE");
		msSearchValue ="";
		
		if ( AMSStringUtil.strEqual("true", lsRestoreState))
		{
		   msSearchValue = loSearchValue.getString("SEARCH_VAL");
		}
	}
	if ( ae.getName().equals(OPEN_INFO_REPORTS))
	{
		/*This stores the DOM for GWT Tree which gets generated so that if 
		the user clicks on the back link after opening the report 	   
	   the user should be able to see the original Tree structure.*/
	   msTreeViewHistory = preq.getParameter(REPORT_TREE_HISTORY);
	}
  
}
//END_EVENT_pBOReportListing_beforeActionPerformed}}
//{{EVENT_pBOReportListing_requestReceived
void pBOReportListing_requestReceived( PLSRequest req, PageEvent evt )
{
} 
//END_EVENT_pBOReportListing_requestReceived}}

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
			pBOReportListing_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pBOReportListing_beforeActionPerformed( ae, evt, preq );
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

	public void afterPageInitialize() {
		super.afterPageInitialize();
		//Write code here for initializing your own control
		//or creating new control.

	}


   /**
    * This method is overridden in this page java file to
	* set the hidden field "RootTreeChildren" with the initial
	* set of children in a JSON formart.
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
      catch (RemoteException foExeption)
      {
         moLog.error("Unexpected error encountered while processing. ",
               foExeption);
         raiseException("Security validations failed.",
               AMSPage.SEVERITY_LEVEL_SEVERE);
      }

      String lsReportListJsonData = ReportUtil.generateReportListJsonData(
				loSession, ROOT_ID, true);
  
      HiddenElement loRootSibblings = (HiddenElement) getElementByName(ROOT_ID_CHILDREN);
      loRootSibblings.setValue(lsReportListJsonData);
      HiddenElement loSearchValue = (HiddenElement) getElementByName(SEARCH_TEXT_VAL);
		if ( !AMSStringUtil.strIsEmpty(msSearchValue) )
		{
			loSearchValue.setValue(msSearchValue);
			msSearchValue="";			
		}
		else
		{
		   loSearchValue.setValue("");
		}

		if ( !AMSStringUtil.strIsEmpty(msTreeViewHistory) )
		{		
			HiddenElement loHistoryValue = (HiddenElement) getElementByName(REPORT_TREE_HISTORY);
			loHistoryValue.setValue(msTreeViewHistory);
			msTreeViewHistory="";
		}
      String lsHTML = super.generate();

      return lsHTML;
   }



}