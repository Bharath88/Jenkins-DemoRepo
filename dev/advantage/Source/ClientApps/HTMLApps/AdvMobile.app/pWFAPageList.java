//{{IMPORT_STMTS
package advantage.AdvMobile;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSComboBoxElement;
import com.amsinc.gems.adv.workflow.aprv.AMSAprvWorkflowToolKit;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import com.amsinc.gems.adv.common.AMSSecurityObject;
import com.amsinc.gems.adv.vfc.html.AMSPage;
import com.amsinc.gems.adv.common.AMSSQLUtil;

/*
**  pWFAPageList
*/

//{{FORM_CLASS_DECL
public class pWFAPageList extends pWFAPageListBase

//END_FORM_CLASS_DECL}}
{

   /**
    * Represents the Workflow role selected on the page list when it is pressed on the mobile app.
    */
   private String msWorkflowRole = "";

   /**
    * Represents if the Workflow role selected on the page list was the first element in the list.
    */
   private boolean mboolCurrUser = false;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pWFAPageList ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pWFAPageList_beforeGenerate
void pWFAPageList_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line
   VSSession loSession = getParentApp().getSession() ;
   String    lsUserID  = loSession.getLogin() ;

   String[] lsWorklistNames = AMSAprvWorkflowToolKit.createTabNames(loSession, lsUserID);
   String[] lsWorklistIDs   = AMSAprvWorkflowToolKit.createTabIds(loSession, lsUserID);

   AMSComboBoxElement loWrkLstCombo = (AMSComboBoxElement)getElementByName("txtT1WorkLists");
   if(loWrkLstCombo != null)
   {
      loWrkLstCombo.removeAllElements();
      String lsCntVl = "";
      for ( int liCtr = 0; liCtr < lsWorklistNames.length; liCtr++)
      {
         loWrkLstCombo.addElement( lsWorklistNames[liCtr], lsWorklistIDs[liCtr] ) ;
         lsCntVl = lsCntVl + getWorkflowRoleDocCount(lsWorklistIDs[liCtr], liCtr== 0) + ";";
      } /* end for ( int liCtr = 0 ; liCtr < msWorklistNames.length ; liCtr++ ) */

      HiddenElement loCntElem = (HiddenElement) getElementByName("RoleCounts");
      loCntElem.setValue(lsCntVl);
   } /* end if ( moWrkLstCombo != null ) */
}
//END_EVENT_pWFAPageList_beforeGenerate}}
//{{EVENT_pWFAPageList_beforeActionPerformed
void pWFAPageList_beforeActionPerformed( ActionElement foAe, PageEvent foEvt, PLSRequest foPreq )
{
   //Write Event Code below this line
   String lsAmsAct = foPreq.getParameter("ams_action");
   if(AMSStringUtil.strEqual(lsAmsAct, Integer.toString(AMSHyperlinkActionElement.OPEN_PAGE)))
   {
      VSPage loPageTrans = AdvMobile.getMobileGlobalTransition(getParentApp(), getSessionId(), foPreq);
      loPageTrans.setSourcePage(this);
      foEvt.setCancel(true) ;
      foEvt.setNewPage(loPageTrans);
      return;
   }

   mboolCurrUser = Boolean.parseBoolean(foPreq.getParameter("hidCurrUser"));
   msWorkflowRole = foPreq.getParameter("roleSelected");
}
//END_EVENT_pWFAPageList_beforeActionPerformed}}

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
			pWFAPageList_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWFAPageList_beforeActionPerformed( ae, evt, preq );
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

   /**
    * Returns the workflow role that was indicated on the page list page.
    * @return String representing the workflow role that was selected.
    */
   public String getWorkflowRole()
   {
      return msWorkflowRole;
   }

   /**
    * Returns whether or not the current user role was indicated on the page list page.
    * @return boolean representing true if the the workflow role that was selected was current user.
    */
   public boolean getCurrUser()
   {
      return mboolCurrUser;
   }

   /**
    * Get the workflow count for the role specified.
	* @param fsRoleToCount represents the role to count the number of documents.
	* @param fboolCurrUserRole represents whether this is the current user role.
	* @return int representing the number of rows found for the role.
	*/
   private int getWorkflowRoleDocCount(String fsRoleToCount, boolean fboolCurrUserRole)
   {
      SearchRequest        loSrchReq = new SearchRequest();
      StringBuffer         lsSQLAppend = new StringBuffer(200) ;
      String               lsApplWhere = null ;
      VSORBSession         loORBSession = getParentApp().getSession().getORBSession() ;
      AMSSecurityObject    loSecObj = null ;
      String               lsTable = "WF_APRV_WRK_LST";
      VSResultSet loRsCnt = null;
      int liNumRows = 0;

      if (fboolCurrUserRole)
      {
         /* The current worklist is the user's, so retrieve based on the user id */
         lsSQLAppend.append( "((").append(lsTable).append(".ASSIGNEE_FL=0 AND ")
               .append(lsTable).append(".ASSIGNEE=" ) ;
         lsSQLAppend.append( AMSSQLUtil.getANSIQuotedStr(fsRoleToCount,true) ) ;
         lsSQLAppend.append( ") OR (").append(lsTable).append(".LOCK_USID=" ) ;
         lsSQLAppend.append( AMSSQLUtil.getANSIQuotedStr(fsRoleToCount ,true)) ;
         lsSQLAppend.append( "))" ) ;
      }
      else
      {
         /* The current worklist is a role, so retrieve based on that role */
         lsSQLAppend.append( "((").append(lsTable).append(".ASSIGNEE_FL=1 AND ")
               .append(lsTable).append(".ASSIGNEE=" ) ;
         lsSQLAppend.append( AMSSQLUtil.getANSIQuotedStr(fsRoleToCount, true) ) ;
         lsSQLAppend.append( ") AND (").append(lsTable).append(".LOCK_USID IS NULL))" ) ;
      } /* end else */

      /* This portion of the code adds a new filter for showing only those records related
       * to the applications that the user is currently logged into successfully.
       */
      try
      {
         loSecObj    = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
         lsApplWhere = AMSSecurityObject.getApplicationWhere( loSecObj, lsTable ) ;
      }
      catch (Exception loRemExp )
      {
         raiseException("Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE);
         return liNumRows;
      }

      if ( lsApplWhere != null )
      {
         lsSQLAppend.append( " AND " ) ;
         lsSQLAppend.append( lsApplWhere ) ;
      } /* if ( lsApplWhere != null ) */


      loSrchReq.add( lsSQLAppend.toString());

      VSQuery loQuery = new VSQuery(getParentApp().getSession(), "WF_WRK_LST_QRY", loSrchReq, null);
      loQuery.setPreFetchRowCount(true);

      try
      {
         loRsCnt = loQuery.execute() ;
         liNumRows = loRsCnt.getRowCount();
      }
      catch(Exception loEx)
      {
         raiseException("Unable to get worklist record count.", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
      }
      finally
      {
         if(loRsCnt != null)
         {
            loRsCnt.close();
         }
      }

      return liNumRows;
   }
}