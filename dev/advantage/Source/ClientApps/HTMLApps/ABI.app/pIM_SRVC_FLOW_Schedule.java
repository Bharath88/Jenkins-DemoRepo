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
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import java.rmi.RemoteException;

/*
**  pIM_SRVC_FLOW_Schedule
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_FLOW_Schedule extends pIM_SRVC_FLOW_ScheduleBase

//END_FORM_CLASS_DECL}}
{

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_FLOW_Schedule ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_SRVC_FLOW_Schedule_beforeActionPerformed
void pIM_SRVC_FLOW_Schedule_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   boolean      lboolAuth    = false;
   int          liAction     = 0;
   String       lsAction     = null;
   String       lsName       = null;
   String       lsUserID     = null;
   String       lsResourceID = null;
   VSORBSession loSession    = null;
   VSPage       loTarget     = null;
   VSRow        loCurrentRow = null;

   lsName = ae.getName();

   if (AMSStringUtil.strEqual(lsName, "btnT2pScheduleFlow"))
   {
      // Authorization check for Schedule action on Resource ID (Service ID)
      liAction = AMSDocAppConstants.DOC_SUB_ACTN_IM_SCHED_FLOW;
      lsAction = "Schedule Flow";

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

      // Action can proceed.
      loTarget = pScheduleFlow.scheduleFlow(this, preq, loCurrentRow);

      if (loTarget != null)
      {
         evt.setNewPage(loTarget);
      }
      else
      {
         raiseException("Unable to connect to Integration Manager server, server may not be running.  " +
               "Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
      }

      evt.setCancel(true);
   } // end if (AMSStringUtil.strEqual(lsName, "btnT2pScheduleFlow"))
}
//END_EVENT_pIM_SRVC_FLOW_Schedule_beforeActionPerformed}}
//{{EVENT_T1IM_SRVC_beforeQuery
void T1IM_SRVC_beforeQuery(VSQuery foQuery, VSOutParam resultset )
{
   boolean       lboolActiveFilter = true;
   SearchRequest loSearchRequest   = null;
   String        lsWhereClause     = null;
   StringBuffer  loFilter          = null;

   // Default filter by Status 'Active' unless user specifies otherwise.
   loSearchRequest = new SearchRequest();
   lsWhereClause   = foQuery.getSQLWhereClause();
   loFilter        = new StringBuffer(64);

   if (lsWhereClause != null &&
       lsWhereClause.trim().length() > 0)
   {
      if (lsWhereClause.indexOf("FLOW_STA") != -1)
      {
         // If filtering by Status already, don't filter later.
         lboolActiveFilter = false;
      }
      else
      {
         // Not filtering by Status, so add AND for later join.
         loFilter.append(" AND ");
      }
   }

   if (lboolActiveFilter)
   {
      loFilter.append("FLOW_STA IN (1)");
      loSearchRequest.add(loFilter.toString());
      foQuery.addFilter(loSearchRequest);
   }

   // Always sort by Service Name
   loSearchRequest = new SearchRequest();
   loSearchRequest.add("FLOW_NM, FLOW_ID");

   foQuery.replaceSortingCriteria(loSearchRequest);
}
//END_EVENT_T1IM_SRVC_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1IM_SRVC.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_FLOW_Schedule_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IM_SRVC) {
			T1IM_SRVC_beforeQuery(query , resultset );
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