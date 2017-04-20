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
import advantage.CVL_IM_FLOW_STAImpl;
import advantage.IntegrationManagerClient;
import adv.common.MessageConstants;
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import java.util.Enumeration;
import java.util.HashMap;
import java.rmi.RemoteException;
import versata.vls.Session;

/*
**  pIM_SRVC_FLOW_All
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_FLOW_All extends pIM_SRVC_FLOW_AllBase

//END_FORM_CLASS_DECL}}
{

   private String msPageTitle = null;
   private String msFlowName  = null;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_FLOW_All ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_SRVC_FLOW_All_beforeGenerate
void pIM_SRVC_FLOW_All_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   Enumeration  loRows      = null;
   HashMap      loJobStaMap = null;
   int          liJobSta    = 0;
   long         llSeqNo     = 0;
   String       lsComma     = null;
   String       lsJobStatus = null;
   String       lsSeqNo     = null;
   StringBuffer loWhere     = null;
   VSQuery      loQuery     = null;
   VSResultSet  loResultSet = null;
   VSRow        loRow       = null;
   VSSession    loSession   = null;

   /*
    * Get list of Sequence values from current block, build where clause
    */
   loSession = getParentApp().getSession();
   loRows    = T1IM_SRVC_FLOW.getBlockOfRows();
   loWhere   = new StringBuffer(100);
   lsComma   = "";

   loWhere.append("FLOW_SEQ_NO IN (");

   while (loRows.hasMoreElements())
   {
      loRow   = (VSRow) loRows.nextElement();
      llSeqNo = loRow.getData("SEQ_NO").getLong();

      loWhere.append(lsComma + llSeqNo);

      lsComma = ",";
   }

   // End here if no records were found.
   if (lsComma.equals(""))
   {
      return;
   }

   loWhere.append(") AND PARM_NM = 'JobID.Status'");

   /*
    * Get Job Status parameters for the list of Sequence values
    */
   loQuery     = new VSQuery(loSession, "IM_SRVC_FLOW_PARM", loWhere.toString(), "");
   loResultSet = loQuery.execute();
   loRow       = loResultSet.first();
   loJobStaMap = new HashMap();

   while (loRow != null)
   {
      lsSeqNo     = loRow.getData("FLOW_SEQ_NO").getString();
      lsJobStatus = loRow.getData("PARM_VL").getString();

      loJobStaMap.put(lsSeqNo, lsJobStatus);

      loRow = loResultSet.next();
   }

   loResultSet.close();

   /*
    * Set Job Status for each Sequence that a parameter was found.
    */
   loRows = T1IM_SRVC_FLOW.getBlockOfRows();
   T1IM_SRVC_FLOW.setAllowUpdate(true);

   while (loRows.hasMoreElements())
   {
      loRow   = (VSRow) loRows.nextElement();
      lsSeqNo = loRow.getData("SEQ_NO").getString();

      if (loJobStaMap.containsKey(lsSeqNo))
      {
         lsJobStatus = (String) loJobStaMap.get(lsSeqNo);
         liJobSta    = new Integer(lsJobStatus).intValue();

         loRow.getData("JOB_STA").setInt(liJobSta);
         loRow.getData("JOB_STA").modified(false);
      }
   }
}
//END_EVENT_pIM_SRVC_FLOW_All_beforeGenerate}}
//{{EVENT_T1IM_SRVC_FLOW_beforeQuery
void T1IM_SRVC_FLOW_beforeQuery(VSQuery foQuery, VSOutParam foResultset )
{
   SearchRequest loSearchRequest = null;

   // Filter query by Flow ID when transitions began at Service Catalog page.
   if (getSourcePage() instanceof pIM_DOC_INTG_LOG ||
       getSourcePage() instanceof pIM_SRVC_FLOW_Schedule)
   {
      foQuery.addFilter(filterByFlowID(foQuery.getSQLWhereClause()));
   }

   // Always sort by Flow Sequence in descending order.
   loSearchRequest = new SearchRequest();
   loSearchRequest.add("SEQ_NO DESC");

   foQuery.replaceSortingCriteria(loSearchRequest);
}
//END_EVENT_T1IM_SRVC_FLOW_beforeQuery}}
//{{EVENT_pIM_SRVC_FLOW_All_beforeActionPerformed
void pIM_SRVC_FLOW_All_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   boolean      lboolAuth    = false;
   int          liAction     = 0;
   int          liFlowSta    = 0;
   long         llFlowSeqNo  = 0;
   String       lsAction     = null;
   String       lsName       = null;
   String       lsResourceID = null;
   String       lsResponseCd = null;
   String       lsUserID     = null;
   VSORBSession loSession    = null;
   VSRow        loCurrentRow = null;

   // Security check for "Logs" and "Integration Resources" links.
   lsName = ae.getName();

   if (AMSStringUtil.strEqual(lsName, "btnT6pIM_SRVC"))
   {
      // Action - Send Stop Request
      loCurrentRow = getRootDataSource().getCurrentRow();

      if (loCurrentRow == null)
      {
         raiseException("Please select a row before selecting this action.", SEVERITY_LEVEL_ERROR);
         evt.setCancel(true);
         evt.setNewPage(this);
         return;
      }

      liFlowSta = loCurrentRow.getData("FLOW_STA").getInt();

      if (liFlowSta != CVL_IM_FLOW_STAImpl.Running)
      {
         raiseException("This job is not running.", SEVERITY_LEVEL_ERROR);
         evt.setCancel(true);
         evt.setNewPage(this);
         return;
      }

      llFlowSeqNo = loCurrentRow.getData("SEQ_NO").getLong();

      if (llFlowSeqNo < 1)
      {
         evt.setCancel(true);
         evt.setNewPage(this);
         return;
      }

      lsResponseCd = IntegrationManagerClient.requestServiceFlowStop(llFlowSeqNo);

      if (AMSStringUtil.strEqual(lsResponseCd, MessageConstants.RESP_CD_OK))
      {
         raiseException("Service Flow stop completed.", SEVERITY_LEVEL_INFO);
      }
      else if (AMSStringUtil.strEqual(lsResponseCd, MessageConstants.RESP_CD_ACCEPTED))
      {
          raiseException("Service Flow stop requested.", SEVERITY_LEVEL_INFO);
      }
      else
      {
         raiseException("Service Flow stop request failed.  See ABI Server Log for details.",
               SEVERITY_LEVEL_ERROR);
      }

      evt.setCancel(true);
      evt.setNewPage(this);
   }
   else if (AMSStringUtil.strEqual(lsName, "btnT2pIM_SRVC_LVL_LOG_Grid"))
   {
      liAction = AMSDocAppConstants.DOC_SUB_ACTN_IM_VIEW_LOGS;
      lsAction = "View Logs";
   }
   else if (AMSStringUtil.strEqual(lsName, "btnT4pIM_SRVC_FLOW_RSRC_Generic"))
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
//END_EVENT_pIM_SRVC_FLOW_All_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1IM_SRVC_FLOW.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_FLOW_All_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_FLOW_All_beforeActionPerformed( ae, evt, preq );
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
         setTitle(msFlowName);
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

      if (msPageTitle == null)
      {
         msPageTitle = getTitle();
      }

      // Filter by Service Flow when source page is Service Flow
      loSourcePage    = getSourcePage();
      loSearchRequest = new SearchRequest();
      lsFilter        = "";

      loRow = loSourcePage.getRootDataSource().getCurrentRow();

      if (loRow == null)
      {
         return loSearchRequest;
      }

      if (getSourcePage() instanceof pIM_DOC_INTG_LOG)
      {
         loData = loRow.getData("RUN_ID");
      }
      else if (getSourcePage() instanceof pIM_SRVC_FLOW_Schedule)
      {
         loData = loRow.getData("FLOW_NM");
      }

      if (loData == null)
      {
         return loSearchRequest;
      }

      if (!AMSStringUtil.strIsEmpty(fsWhereClause))
      {
         lsFilter = " AND ";
      }

      if (getSourcePage() instanceof pIM_DOC_INTG_LOG)
      {
         msFlowName = "Service Flows for Flow ID " + loData.getString();
         llFlowID   = loData.getLong();
         lsFilter   = lsFilter + "RUN_ID = " + llFlowID;
      }
      else if (getSourcePage() instanceof pIM_SRVC_FLOW_Schedule)
      {
         msFlowName = "Service Flows for Service Flow " + loData.getString();
         llFlowID   = loRow.getData("FLOW_ID").getLong();
         lsFilter   = lsFilter + " FLOW_ID = " + llFlowID;
      }

      loSearchRequest.add(lsFilter);

      return loSearchRequest;
   } // end filterByFlowID()
}