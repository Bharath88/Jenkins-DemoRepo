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
import advantage.IntegrationManagerClient;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.AMSUniqNum;
import com.amsinc.gems.adv.common.AMSUniqNumException;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import java.rmi.RemoteException;

/*
**  pScheduleFlow
*/

//{{FORM_CLASS_DECL
public class pScheduleFlow extends pScheduleFlowBase

//END_FORM_CLASS_DECL}}
{

   public long mlRunId = 0;
   public long mlJobId = 0;
   public long mlSeqNo = 0;
   VSRow moFlowRunRow = null;
   VSRow moRow = null;
   public boolean mboolSubmitRec = false;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pScheduleFlow ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_T1BS_AGENT_beforeQuery
void T1BS_AGENT_beforeQuery(VSQuery query, VSOutParam resultset)
{
   SearchRequest loSearchRequest  = null;
   String        lsOr             = null;

   loSearchRequest = new SearchRequest();

   lsOr = query.getSQLWhereClause();

   if (lsOr != null &&
       lsOr.trim().length() > 0)
   {
      lsOr = " OR ";
   }
   else
   {
      lsOr = "";
   }

   if (mlJobId != 0)
   {
      // loSearchRequest.add(" AGNT_ID =  "+ mlJobId +
      // " AND CHK_PT = 'IM_SRVC_FLOW:"+ mlRunId +"'");
      loSearchRequest.add(lsOr + "AGNT_ID = " + mlJobId);
   }
   else
   {
      loSearchRequest.add(lsOr + "AGNT_ID = -1");
   }

   query.addFilter(loSearchRequest);
}
//END_EVENT_T1BS_AGENT_beforeQuery}}
//{{EVENT_T1BS_AGENT_afterSave
void T1BS_AGENT_afterSave(VSRow row)
{
   //Write Event Code below this line
   createFlowRun(row, row.getData("AGNT_ID").getLong());

   if (getBsAgentParmRec(row.getData("AGNT_ID").getLong(), "FLOW_ID") == null)
   {
      AMSPLSUtil.addNewParameter(row.getData("AGNT_ID").getLong(),
            "FLOW_ID", Long.toString(mlSeqNo),this);
   }

   if (getBsAgentParmRec(row.getData("AGNT_ID").getLong(), "APP_SRVR_ID") == null
      && !AMSStringUtil.strIsEmpty( row.getData("CHK_PT").getString()))
   {
      AMSPLSUtil.addNewParameter(row.getData("AGNT_ID").getLong(),
             "APP_SRVR_ID", row.getData("CHK_PT").getString(), this);
   }

   if (mboolSubmitRec)
   {
      row.getData("RUN_STA").setInt(AMSBatchConstants.STATUS_SUBMITTED);
   }
} // end T1BS_AGENT_afterSave()
//END_EVENT_T1BS_AGENT_afterSave}}
//{{EVENT_pScheduleFlow_beforeActionPerformed
void pScheduleFlow_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq)
{
   boolean      lboolAuth        = false;
   int          liAction         = 0;
   long         llAgntID         = 0;
   String       lsAction         = null;
   String       lsName           = null;
   String       lsResourceID     = null;
   String       lsUserID         = null;
   VSORBSession loSession        = null;
   VSPage       loTarget         = null;
   VSRow        loCurrentRow     = null;
   VSRow        loBSAgentParmRow = null;

   lsName = ae.getName();

   if (AMSStringUtil.strEqual(lsName, "btnT2pIM_SRVC_FLOW_PARM_Grid"))
   {
      // Edit Parameters
      loTarget = pIM_SRVC_FLOW_PARM_Grid.editParameters(this, preq, moFlowRunRow);

      evt.setCancel(true);
      evt.setNewPage(loTarget);
   }
   else if (AMSStringUtil.strEqual(lsName, "Ok"))
   {
      // Authorization check for Submit action on Resource ID (Service ID)
      liAction = AMSDocAppConstants.DOC_SUB_ACTN_IM_SUBMIT_JOB;
      lsAction = "Submit";

      loSession    = getParentApp().getSession().getORBSession();
      loCurrentRow = getRootDataSource().getCurrentRow();

      if (loCurrentRow == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Resource ID - for Integration Manager, use Service ID.
      lsResourceID = moFlowRunRow.getData("FLOW_ID").getString();
      
      if (lsResourceID == null)
      {
         raiseException("Unable to get Flow ID.  " +
               "Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
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
         raiseException("Unable to get User Security for the " + lsAction + " action for this " +
               "service (Resource ID " + lsResourceID +").", SEVERITY_LEVEL_ERROR);
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

      // Submit job and return to Service Catalog
      mboolSubmitRec = true;
   }
   else if (AMSStringUtil.strEqual(lsName, "DeleteJob"))
   {
      // Delete job and return to Service Catalog
      loCurrentRow = getRootDataSource().getCurrentRow();

      if (loCurrentRow != null)
      {
         llAgntID = loCurrentRow.getData("AGNT_ID").getLong();

         if (llAgntID > 0)
         {
            deleteJob(llAgntID);
         }
      }
   }
}
//END_EVENT_pScheduleFlow_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1BS_AGENT.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_afterSave(row );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pScheduleFlow_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_beforeQuery(query , resultset );
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
    * Performs a lookup for job parameter.
    *
    * @param flAgentId Batch Job ID
    * @param fsParmNm Parameter Name
    */
   private VSRow getBsAgentParmRec(long flAgentId, String fsParmNm)
   {
      SearchRequest lsr = new SearchRequest();
      lsr.add("AGNT_ID =" + flAgentId + " and PARM_NM ='" + fsParmNm +"'");
      VSQuery loQuery = new VSQuery(getParentApp().getSession(),
            "BS_AGENT_PARM", lsr, null);
      VSResultSet loRes = loQuery.execute();
      VSRow loRow = loRes.first();
      return (loRow != null) ? loRow : null;
   }


   /**
    * Adds a new Batch job and Integration Manager run.
    */
   public static VSPage scheduleFlow(VSPage foSrcPage, PLSRequest foRequest,
         VSRow foRow)
   {
      pScheduleFlow        loScheduleFlow = null;
      AMSDynamicTransition loDynTran      = null;
      long                 llRunID        = 0;
      String               lsHTML         = null;
      String               lsServiceID    = null;

      /*
       * Any additional where clause will be added in the beforeQuery() event
       * only. This will optimize the filter (starts with indexed columns)
       */
      loDynTran = new AMSDynamicTransition("pScheduleFlow", null, "ABI");
      loDynTran.setSourcePage(foSrcPage);
      loScheduleFlow = (pScheduleFlow) loDynTran.getVSPage(foSrcPage.getParentApp(), foSrcPage.getSessionId());

      try
      {
         lsServiceID = foRow.getData("FLOW_ID").getString();
         llRunID = IntegrationManagerClient.getUniqNum(lsServiceID);
         loScheduleFlow.setRunId(llRunID);
      }
      catch (Exception foException)
      {
         // Might be that the IM servers are all down.
         return null;
      }

      // Do not continue if Unique ID assignment was unavailable.
      if (llRunID < 1)
      {
         return null;
      }

      loScheduleFlow.setCurrentRow(foRow);
      long liAgentId = loScheduleFlow.createBsAgentRecord(foRow);
      loScheduleFlow.generate();

      return loScheduleFlow;
   } // end scheduleFlow()


   /**
    * Private setter for Run ID.
    */
   private void setRunId(long flRunId)
   {
      mlRunId = flRunId;
   }


   /**
    * Public setter for IM_SRVC_FLOW record.
    */
   public void setCurrentRow(VSRow foRow)
   {
      moRow = foRow;
   }


   /**
    * Creates a new Integration Manager run.
    */
   public long createFlowRun(VSRow foBsAgentRow, long flJobId)
   {
      long llCycleRunID = 0;
      VSRow loRow = this.getRootDataSource().getCurrentRow();

      AMSDataSource loDataSource = new AMSDataSource();
      loDataSource.setName("IM_SRVC_FLOW");
      loDataSource.setSession(parentApp.getSession());
      loDataSource.setQueryInfo("IM_SRVC_FLOW", "SEQ_NO=" + mlSeqNo, "", "", false);
      loDataSource.setPage(this);
      loDataSource.setAllowInsert(true);
      loDataSource.setSaveMode(DataSource.SAVE_BUFFERED);
      loDataSource.executeQuery();

      VSRow loCurrentRow = null;

      if (mlSeqNo == 0)
      {
         loDataSource.insert();
         loCurrentRow = loDataSource.getCurrentRow();
         try
         {
            llCycleRunID = IntegrationManagerClient.getUniqNum("CYCLE_RUN_ID");
            mlSeqNo = IntegrationManagerClient.getUniqNum("IM_SRVC_FLOW");

            loCurrentRow.getData("SEQ_NO").setLong(mlSeqNo);
            loCurrentRow.getData("CYCLE_RUN_ID").setLong(llCycleRunID);
            loCurrentRow.getData("FLOW_ID").setLong(moRow.getData("FLOW_ID").getLong());
            loCurrentRow.getData("FLOW_STA").setInt(1);
            loCurrentRow.getData("FLOW_NM").setString(moRow.getData("FLOW_NM").getString());
            loCurrentRow.getData("CYCLE_NM").setString("New cycle");
            loCurrentRow.getData("RUN_ID").setLong(mlRunId);
            loCurrentRow.getData("RUN_AS_USER").setString(moRow.getData("RUN_AS_USER").getString());
            loCurrentRow.getData("APPL_SRV_ID").setString(foBsAgentRow.getData("CHK_PT").getString());
            loCurrentRow.getData("JOB_ID").setLong(flJobId);
         } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

         // T1BS_AGENT.setQueryInfo("BS_AGENT", "AGNT_ID = " + flJobId, "", "", false);
         long llRunId = loCurrentRow.getData("RUN_ID").getLong();
      }
      else
      {
         loDataSource.setAllowUpdate(true);
         loCurrentRow = loDataSource.getCurrentRow();

         loCurrentRow.getData("APPL_SRV_ID").setString(
               foBsAgentRow.getData("CHK_PT").getString());
         loCurrentRow.getData("RUN_DT_TM").setDate(
               foBsAgentRow.getData("RUN_DT_TM").getDate());
      }

      loCurrentRow.save();
      moFlowRunRow = loCurrentRow;
      loDataSource.updateDataSource();

      return loCurrentRow.getData("RUN_ID").getLong();
   } // end createFlowRun()


   public long createBsAgentRecord(VSRow foRow)
   {
      String lsUserId = AMSPLSUtil.getCurrentUserID( this );
      long liJobId = addNewJob("FlowJobScheduler", "advantage", lsUserId,
            AMSBatchConstants.RUNPLCY_IMMEDIATE, new java.util.Date(),
            new java.util.Date(), foRow.getData("RUN_ITVL").getLong(), 0,
            AMSBatchConstants.JOB_PRTY_1, null, 0, 0, 1, this);
      mlJobId = liJobId;

      return liJobId;
   }


   public long addNewJob(String fsJobClassName, String fsPackageName,
         String fsUserId, int fiRunPolicy, java.util.Date foScheduleDate,
         java.util.Date foExpireDate, long flRunInterval,
         int fiRepeatNumber, int fiPriority, String fsMailingList,
         int flPreconditionID, int fiPreCnReturnCode,
         int fiOutputExpirationPlcy, VSPage foVsPage)
   {
      long llReturnJobID = -1; // default value
      DataSource loDataSource;
      VSRow loRow;

      PLSApp loParentApp = foVsPage.getParentApp();

      VSSession moVsSession = loParentApp.getSession();

      T1BS_AGENT.setSaveMode(T1BS_AGENT.SAVE_IMMEDIATE);
      /**
       * Insert a record into the data source
       */
      T1BS_AGENT.insert();
      loRow = T1BS_AGENT.getCurrentRow();

      if (loRow == null)
      {
        return -1;
      }

      /**
       * Get the catalog id for the specified job class name and package name
       */
      long llCatalogID = AMSPLSUtil.getCatalogID(fsJobClassName,
            fsPackageName, foVsPage);

      loRow.getData("PNT_CTLG_ID").setString(String.valueOf(llCatalogID));
      loRow.getData("RUN_DT_TM").setDate(foScheduleDate);
      loRow.getData("RUN_EXPR_TM").setDate(foExpireDate);
      loRow.getData("USID").setString(fsUserId);
      loRow.getData("PRTY").setInt(fiPriority);
      loRow.getData("RUN_PLCY").setInt(fiRunPolicy);
      loRow.getData("SBMT_TS").setDate(new java.util.Date());

      // valid mailing list
      if (fsMailingList != null && fsMailingList.trim().length() > 0)
      {
         loRow.getData("MAIL_LST").setString(fsMailingList);
      }
      loRow.getData("RPET_NO").setInt(fiRepeatNumber);

      // field PRE_CN not to be populated unless valid
      if (flPreconditionID != 0) {
         loRow.getData("PRE_CN").setString(String.valueOf(flPreconditionID));
         loRow.getData("PRE_CN_RET_CD").setInt(fiPreCnReturnCode);
      }

      loRow.getData("OTPT_EXPR_PLCY").setInt(fiOutputExpirationPlcy);
      loRow.getData("RUN_ITVL").setString(
            String.valueOf(flRunInterval * 60000));

      // Job not set to be picked up by the AgentServer as yet
      loRow.getData("RUN_STA").setInt(AMSBatchConstants.STATUS_TO_BE_SUBMITTED);
      T1BS_AGENT.updateDataSource();
      T1BS_AGENT.setSaveMode(T1BS_AGENT.SAVE_IMMEDIATE);
      mlJobId = T1BS_AGENT.getCurrentRow().getData("AGNT_ID").getLong();
      this.refreshDataSource(T1BS_AGENT);

      return T1BS_AGENT.getCurrentRow().getData("AGNT_ID").getLong();
   } // end addnewJob()


   /**
    * Deletes from Batch Job Manager and Integration Manager
    *
    * @param flJobID - Job ID
    */
   public void deleteJob(long flJobID)
   {
      long  llFlowSeqID      = 0;
      VSRow loBSAgentParmRow = null;

      // Get Flow ID from batch parameter
      loBSAgentParmRow = getBsAgentParmRec(flJobID, "FLOW_ID");

      if (loBSAgentParmRow != null)
      {
         llFlowSeqID = loBSAgentParmRow.getData("PARM_VL").getLong();
      }

      // Delete Batch Manager job parameters
      deleteFromTable("BS_AGENT_PARM", " AGNT_ID = " + flJobID);

      // Delete Batch Manager job
      deleteFromTable("BS_AGENT",      " AGNT_ID = " + flJobID);

      if (llFlowSeqID > 0)
      {
         // Delete Integration Manager flow parameters
         deleteFromTable("IM_SRVC_FLOW_PARM", " FLOW_SEQ_NO = " + llFlowSeqID);

         // Delete Integration Manager flow
         deleteFromTable("IM_SRVC_FLOW",      " SEQ_NO = "      + llFlowSeqID);
      }
   }


   /**
    * Deletes from Batch Job Manager and Integration Manager
    *
    */
   public void deleteFromTable(String fsTableName, String fsWhereClause)
   {
      int           liRowCount      = 0;
      VSResultSet   loResultSet     = null;
      SearchRequest loSearchRequest = null;
      VSQuery       loQuery         = null;

      loSearchRequest = new SearchRequest();
      loSearchRequest.add(fsWhereClause);

      loQuery = new VSQuery(getParentApp().getSession(), fsTableName, loSearchRequest, null);

      loQuery.setPreFetchRowCount(true);

      loResultSet = loQuery.execute();
      liRowCount  = loResultSet.getRowCount();

      for (int liIndex = 1; liIndex <= liRowCount; liIndex++)
      {
         loResultSet.getRowAt(liIndex);
         loResultSet.delete();
      }

      if (loResultSet != null)
      {
         loResultSet.close();
         loResultSet = null;
      }
   } // end deleteFromTable()
}