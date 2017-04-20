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
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import java.rmi.RemoteException;

/*
**  pIM_SRVC_FLOW
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_FLOW_Pending extends pIM_SRVC_FLOW_PendingBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolFirstLoad = true;
   private String  msPageTitle    = null;
   private String  msFlowName     = null;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_FLOW_Pending ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_T1IM_SRVC_FLOW_beforeQuery
void T1IM_SRVC_FLOW_beforeQuery(VSQuery foQuery, VSOutParam foResultSet)
{
   SearchRequest loSearchRequest = null;

   // Save original page title
   if (msPageTitle == null)
   {
      msPageTitle = getTitle();
      msFlowName  = "";
   }

   // Filter query by Flow ID when transitions began at Service Catalog page.
   if (getSourcePage() instanceof pIM_SRVC_FLOW_Schedule ||
       getSourcePage().getTitle().indexOf("Service Flow ") > 0)
   {
      foQuery.addFilter(filterByFlowID(foQuery.getSQLWhereClause()));
   }

   // Sort by Flow Sequence in descending order when page is opened.
   if (mboolFirstLoad)
   {
      loSearchRequest = new SearchRequest();
      loSearchRequest.add("SEQ_NO DESC");

      foQuery.replaceSortingCriteria(loSearchRequest);

      mboolFirstLoad = false;
   }
}
//END_EVENT_T1IM_SRVC_FLOW_beforeQuery}}
//{{EVENT_pIM_SRVC_FLOW_Pending_beforeActionPerformed
void pIM_SRVC_FLOW_Pending_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   boolean      lboolAuth    = false;
   int          liAction     = 0;
   String       lsAction     = null;
   String       lsName       = null;
   String       lsResourceID = null;
   String       lsUserID     = null;
   VSORBSession loSession    = null;
   VSRow        loCurrentRow = null;

   // Security check for "Integration Resources" link.
   lsName = ae.getName();

   if (AMSStringUtil.strEqual(lsName, "btnT4pIM_SRVC_FLOW_RSRC_Generic"))
   {
      liAction = AMSDocAppConstants.DOC_SUB_ACTN_IM_VIEW_INTG_RSRC;
      lsAction = "View Integration Resources";
   }
   else
   {
      liAction = 0;
   }

   if (liAction > 0)
   {
      // Authorization check for Schedule action on Resource ID (Service ID)
      loSession    = getParentApp().getSession().getORBSession();
      loCurrentRow = getRootDataSource().getCurrentRow();

      if (loCurrentRow == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Resource ID - for Integration Manager, use Service ID.
      lsResourceID = loCurrentRow.getData("FLOW_ID").getString();

      if (lsResourceID == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Get User ID.
      try
      {
         lsUserID = loSession.getUserID();
      }
      catch (RemoteException foException)
      {
         raiseException("Unable to get User ID from session.  " +
               "Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      try
      {
         lboolAuth = AMSSecurity.actionAuthorized(lsUserID, lsResourceID, liAction);
      }
      catch (AMSSecurityException foException)
      {
         raiseException("Cannot access resource for this service (Resource ID " + lsResourceID +
               ").  Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      if (!lboolAuth)
      {
         raiseException("User is not authorized to perform the " + lsAction + " action for this " +
               "service (Resource ID " + lsResourceID +").", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }
   } // end if (liAction > 0)
}
//END_EVENT_pIM_SRVC_FLOW_Pending_beforeActionPerformed}}
//{{EVENT_pIM_SRVC_FLOW_Pending_afterActionPerformed
void pIM_SRVC_FLOW_Pending_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line

}
//END_EVENT_pIM_SRVC_FLOW_Pending_afterActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IM_SRVC_FLOW.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_FLOW_Pending_afterActionPerformed( ae, preq );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_FLOW_Pending_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IM_SRVC_FLOW) {
			T1IM_SRVC_FLOW_beforeQuery(query , resultset );
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
    * Custom implmentation of ancestor sets title
    */
   public String generate()
   {
      // Add Flow Name to page title
      if (msPageTitle != null &&
          msFlowName  != null)
      {
         setTitle(msPageTitle + msFlowName);
      }
      else if (msPageTitle != null)
      {
         setTitle(msPageTitle);
      }

      return super.generate();
   }


   /**
    * Filter query by Flow ID.
    *
    * @return String - Flow Name
    */
   public SearchRequest filterByFlowID(String fsWhereClause)
   {
      long          llFlowID        = 0;
      SearchRequest loSearchRequest = null;
      String        lsFilter        = null;
      VSData        loData          = null;
      VSPage        loSourcePage    = null;
      VSRow         loRow           = null;

      // Filter by Service Flow when source page is Service Flow
      loSourcePage    = getSourcePage();
      loSearchRequest = new SearchRequest();
      lsFilter        = "";

      loRow = loSourcePage.getRootDataSource().getCurrentRow();

      if (loRow == null)
      {
         return loSearchRequest;
      }

      loData = loRow.getData("FLOW_NM");

      if (loData == null)
      {
         return loSearchRequest;
      }

      if (!AMSStringUtil.strIsEmpty(fsWhereClause))
      {
         lsFilter = " AND";
      }

      msFlowName = " for Service Flow " + loData.getString();
      llFlowID   = loRow.getData("FLOW_ID").getLong();
      lsFilter   = lsFilter + " FLOW_ID = " + llFlowID;

      loSearchRequest.add(lsFilter);

      return loSearchRequest;
   } // end filterByFlowID()
}