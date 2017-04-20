/*
 * @(#)View_Pending_Chain_Jobs.java 1.0 Dec 15, 2006
 *
 * Copyright (C) 2006, 2006 by CGI-AMS Inc., 4050 Legato Road, Fairfax,
 * Virginia, U.S.A. All rights reserved.
 *
 * This software is the confidential and proprietary information of CGI-AMS Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with CGI-AMS Inc.
 *
 * Modification log:
 *
 */

// {{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
// END_IMPORT_STMTS}}

import com.amsinc.gems.adv.client.batch.AMSJobManagerClient;
import com.amsinc.gems.adv.client.batch.AMSJobManagerMsg;
import com.amsinc.gems.adv.client.batch.AMSPLSBatchUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSUser;
import java.util.Vector;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import advantage.AMSBatchParmUtil;
import advantage.AMSUtil;
import versata.vls.Session;
import advantage.AMSDataObject;
import versata.vls.ServerEnvironment;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSParams;

import org.apache.commons.logging.Log;


/*
 * * View_Pending_Chain_Jobs
 */

// {{FORM_CLASS_DECL
public class View_Pending_Chain_Jobs extends View_Pending_Chain_JobsBase

// END_FORM_CLASS_DECL}}
      implements AMSBatchConstants
{

   // Job Manager Client to 'talk' to different job managers
   private AMSJobManagerClient moJobManagerClient = null;
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( View_Pending_Chain_Jobs.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
   // {{FORM_CLASS_CTOR
   public View_Pending_Chain_Jobs(PLSApp parentApp)
                                                   throws VSException,
                                                   java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE
   // {{EVENT_View_Pending_Chain_Jobs_beforeActionPerformed
   void View_Pending_Chain_Jobs_beforeActionPerformed(ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      // Write Event Code below this line

      AMSJobManagerMsg loRetObj = null;
      VSSession loCurrSession = getParentApp().getSession();
      VSORBSession loCurrentSession = loCurrSession.getORBSession();
      VSRow loCurrentRow = getRootDataSource().getCurrentRow();
      VSResultSet loVSResultSet = null;
      VSQuery loVSQuery = null;
      String lsName = ae.getName();
      String lsAction = ae.getAction();

      if (lsName.equals("EditJob_User") || lsName.equals("EditJob_SysAdmin"))
      {
         int liCurrStatus;

         if (loCurrentRow == null)
         {
            evt.setNewPage(this);
            evt.setCancel(true);
            raiseException("No row selected", SEVERITY_LEVEL_ERROR);
            return;
         }

         /* Check if the User is authorized to Edit Job as per security settings */
         if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
               .getCurrentUserID(loCurrSession), loCurrentRow.getData(
               "PNT_CTLG_ID").getLong(), AMSCommonConstants.JOB_ACTN_EDIT,
               true, this))
         {
            /*
             * Case where the User is not authorized to Edit Job. Cancel the
             * navigation to Edit Job page.
             */
            evt.setNewPage(this);
            evt.setCancel(true);
            return;
         }

         liCurrStatus = loCurrentRow.getData("RUN_STA").getInt();

         /**
          * if ready, running or completed job, cancel page transition
          */
         if ((liCurrStatus == AMSBatchConstants.STATUS_READY)
               || (liCurrStatus == AMSBatchConstants.STATUS_RUNNING)
               || (liCurrStatus == AMSBatchConstants.STATUS_COMPLETE))
         {
            evt.setNewPage(this);
            evt.setCancel(true);
            raiseException("Job already running - cannot be edited",
                  SEVERITY_LEVEL_ERROR);
         }
         else
         {
            try
            {
               loCurrentSession.setProperty(AMSBatchParmUtil.UPDATE_JOB_STEPS,
                     AMSBatchParmUtil.UPDATE_JOB_STEPS);

               loCurrentRow.getData("RUN_STA").setInt(
                     AMSBatchConstants.STATUS_TO_BE_SUBMITTED);
               getRootDataSource().updateDataSource();
            }
            catch (Throwable loExp)
            {
               raiseException("Error: " + loExp.getMessage(),
                     SEVERITY_LEVEL_ERROR);
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);

               evt.setNewPage(this); // cancel page transition if run status
                                       // cannot be updated
               evt.setCancel(true);
            }
         }
      }
      /*
       * action delete- record deleted if confirmed by the user
       */
      else if (lsAction.equals("delete"))
      {
         String lsConfirmDelete = preq.getParameter("ConfirmDelete"); // in
                                                                        // JS/batch.js
         long llJobID = 0;

         if (lsConfirmDelete.equals("No"))
         {
            evt.setNewPage(this);
            evt.setCancel(true);
         }
         else
         // job with status running or ready not deleted
         {

            int liCurrStatus;

            if (loCurrentRow == null)
            {
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("No row selected", SEVERITY_LEVEL_ERROR);
               return;
            }

            /*
             * Check if the User is authorized to Delete Job as per security
             * settings
             */
            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession,
                  AMSPLSUtil.getCurrentUserID(loCurrSession), loCurrentRow
                        .getData("PNT_CTLG_ID").getLong(),
                  AMSCommonConstants.JOB_ACTN_DELETE, true, this))
            {
               /*
                * Case where the User is not authorized to Delete Job. Cancel
                * the PageEvent and prevent the deletion of job.
                */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

            liCurrStatus = loCurrentRow.getData("RUN_STA").getInt();
            String lsServerID = loCurrentRow.getData("APPL_SRV_NM").getString();
            // read current job id
            llJobID = loCurrentRow.getData("AGNT_ID").getLong();

            /*
             * if ServerID not specified by user or if not already set when job
             * picked up allow job to be deleted. If Server specified but
             * current run status still not ready or running- allow job to be
             * deleted as job has not yet been picked up
             */
            if (lsServerID != null && lsServerID.trim().length() > 1
                  && !(liCurrStatus == STATUS_READY)
                  && !(liCurrStatus == STATUS_RUNNING))
            {
               try
               {
                  /*
                   * connect to job manager - job row has server Id where the
                   * job is either assigned to run or the value is updated by
                   * the job manager that picks up the job to run
                   */

                  int liJobMgrId = connectToJobManager(loCurrentRow);
                  if (liJobMgrId == -1)
                  {
                     evt.setNewPage(this);
                     evt.setCancel(true);
                     raiseException(
                           "Could not connect to job manager to delete job",
                           SEVERITY_LEVEL_ERROR);
                     return;
                  }

                  loRetObj = moJobManagerClient.isJobAlive(liJobMgrId, llJobID,
                        AMSPLSUtil.getCurrentUserID(this), AMSPLSUtil
                              .getPassword(this));

                  // check to see if job alive
                  if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
                  {
                     Boolean loObj = (Boolean) loRetObj.getValue();
                     if (loObj.booleanValue() == true)
                     {
                        evt.setNewPage(this);
                        evt.setCancel(true);
                        raiseException("Job (Id :" + llJobID
                              + ") is still running", SEVERITY_LEVEL_ERROR);
                        return;
                     }
                  }
                  else
                  // remote op failed
                  {
                     evt.setNewPage(this);
                     evt.setCancel(true);
                     raiseException("Could not connect to job manager (Id:"
                           + liJobMgrId + ")", SEVERITY_LEVEL_ERROR);
                     return;
                  }
               } // try
               catch (Throwable loExp)
               {
                  raiseException("Error: " + loExp.getMessage(),
                        SEVERITY_LEVEL_ERROR);
                  evt.setNewPage(this);
                  evt.setCancel(true);
                 // Add exception log to logger object
                 moAMSLog.error("Unexpected error encountered while processing. ", loExp);

                  return;
               } // catch
            } // end if (lsServerID !=null || lsServerID.trim().length() > 1)
            else if (liCurrStatus == STATUS_READY
                  || liCurrStatus == STATUS_RUNNING)
            {
               try
               {
                  /*
                   * The job has the status ready - or running check if the job
                   * is actually running- if yes dont delete
                   */
                  int liJobMgrId = connectToJobManager(loCurrentRow);

                  if (liJobMgrId == -1)
                  {
                     evt.setNewPage(this);
                     evt.setCancel(true);
                     raiseException(
                           "Could not connect to job manager to delete job",
                           SEVERITY_LEVEL_ERROR);
                     return;
                  }

                  loRetObj = moJobManagerClient.isJobAlive(liJobMgrId, llJobID,
                        AMSPLSUtil.getCurrentUserID(this), AMSPLSUtil
                              .getPassword(this));

                  // check to see if job alive
                  if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
                  {
                     Boolean loObj = (Boolean) loRetObj.getValue();
                     if (loObj.booleanValue() == true)
                     {
                        evt.setNewPage(this);
                        evt.setCancel(true);
                        raiseException("Job (Id :" + llJobID
                              + ") is still running", SEVERITY_LEVEL_ERROR);
                        return;
                     }
                  }
                  else
                  // remote op failed
                  {
                     evt.setNewPage(this);
                     evt.setCancel(true);
                     raiseException("Could not connect to job manager (Id:"
                           + liJobMgrId + ")", SEVERITY_LEVEL_ERROR);
                     return;
                  }
               } // try
               catch (Throwable loExp)
               {
                  raiseException("Error: " + loExp.getMessage(),
                        SEVERITY_LEVEL_ERROR);
                  evt.setNewPage(this);
                  evt.setCancel(true);
                  // Add exception log to logger object
                  moAMSLog.error("Unexpected error encountered while processing. ", loExp);

                  return;
               } // catch

            } // else if (liCurrStatus==STATUS_READY &&
               // liCurrStatus==STATUS_RUNNING)

            try
            {
               // need to delete child jobs as well
               StringBuffer lsbQueryWhereClause = new StringBuffer(16);
               lsbQueryWhereClause.append("ID=").append(AMSSQLUtil.getANSIQuotedStr(Long.toString(llJobID), true));

               loVSQuery = new VSQuery(getParentApp().getSession(),
                     getRootDataSource().getMetaQueryName(),
                     lsbQueryWhereClause.toString(), "");

               loVSResultSet = loVSQuery.execute();

               if (loVSResultSet != null)
               {
                  loVSResultSet.last();

                  if (loVSResultSet.getRowCount() != -1)
                  {
                     loVSResultSet.delete();
                  }
               }
            }
            finally
            {
               if (loVSResultSet != null)
               {
                  loVSResultSet.close();
               }
            }

         } // end else if (lsConfirmDelete.equals("No"))

      } /* end if (ae.getAction().equals("delete")) */
      /**
       * Set Job status = AMSBatchConstants.STATUS_APPROVED if the current
       * status is AMSBatchConstants.STATUS_PENDING_APPROVAL The action is
       * relevant only when the page is generated by super use
       */
      if (lsName.equals("ApproveJob"))
      {
         int liCurrStatus;

         long llJobID = loCurrentRow.getData("AGNT_ID").getLong();
         if (loCurrentRow == null)
         {
            raiseException("No row selected", SEVERITY_LEVEL_ERROR);
            return;
         }

         // Security check

         if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
               .getCurrentUserID(loCurrSession), loCurrentRow.getData(
               "PNT_CTLG_ID").getLong(), AMSCommonConstants.JOB_ACTN_APPROVE,
               true, this))
         {
            /*
             * Case where the User is not authorized to approve Job. Cancel the
             * PageEvent and prevent the deletion of job.
             */
            evt.setNewPage(this);
            evt.setCancel(true);
            return;
         }

         liCurrStatus = loCurrentRow.getData("RUN_STA").getInt();
         if (liCurrStatus == AMSBatchConstants.STATUS_PENDING_APPROVAL)
         {
            try
            {

               loCurrentRow.getData("RUN_STA").setInt(
                     AMSBatchConstants.STATUS_APPROVED);
               loCurrentRow.getData("RQRS_APRV").setBoolean(false);
               getRootDataSource().updateDataSource();

               // Update for child jobs as well
               StringBuffer lsbQueryWhereClause = new StringBuffer(16);
               lsbQueryWhereClause.append("ID=").append(llJobID);

               loVSQuery = new VSQuery(getParentApp().getSession(),
                     getRootDataSource().getMetaQueryName(),
                     lsbQueryWhereClause.toString(), "");

               loVSResultSet = loVSQuery.execute();

               if (loVSResultSet != null)
               {
                  VSRow loCurrRow = null;
                  while ((loCurrRow = loVSResultSet.next()) != null)
                  {
                     loCurrRow.getData("RUN_STA").setInt(
                           AMSBatchConstants.STATUS_APPROVED);
                     loCurrRow.getData("RQRS_APRV").setBoolean(false);
                  }
                  loVSResultSet.updateDataSource();
               }

               // end of update for child jobs
            }
            catch (Throwable loExp)
            {
               raiseException("Error: " + loExp.getMessage(),
                     SEVERITY_LEVEL_ERROR);
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            }
            finally
            {
               if (loVSResultSet != null)
               {
                  loVSResultSet.close();
               }

            }
         } // if (liCurrStatus == AMSBatchConstants.STATUS_PENDING_APPROVAL)
         else
         {
            // job cannot have its run status set to
            // - AMSBatchConstants.STATUS_APPROVED
            raiseException(
                  "Only jobs pending approval can be approved to be run",
                  SEVERITY_LEVEL_ERROR);
         }
      } /* end if (ae.getName().equals("ApproveJob")) */
      /**
       * set Job status = AMSBatchConstants.STATUS_SUBMITTED if the current
       * status is AMSBatchConstants.STATUS_TO_BE_SUBMITTED The action is
       * relevant only when the page is generated by super use
       */
      else if (lsName.equals("SubmitJob"))
      {
         int liCurrStatus;

         if (loCurrentRow == null)
         {
            raiseException("No row selected", SEVERITY_LEVEL_ERROR);
            return;
         }

         // Security check
         if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
               .getCurrentUserID(loCurrSession), loCurrentRow.getData(
               "PNT_CTLG_ID").getLong(), AMSCommonConstants.JOB_ACTN_SUBMIT,
               true, this))
         {
            /*
             * Case where the User is not authorized to sumbit Job. Cancel the
             * PageEvent and prevent the submission of job.
             */
            evt.setNewPage(this);
            evt.setCancel(true);
            return;
         }

         liCurrStatus = loCurrentRow.getData("RUN_STA").getInt();

         if (liCurrStatus == AMSBatchConstants.STATUS_TO_BE_SUBMITTED
               || liCurrStatus == AMSBatchConstants.STATUS_INACTIVE)
         {
            try
            {

               loCurrentSession.setProperty(AMSBatchParmUtil.UPDATE_JOB_STEPS,
                     AMSBatchParmUtil.UPDATE_JOB_STEPS);

               loCurrentRow.getData("RUN_STA").setInt(
                     AMSBatchConstants.STATUS_SUBMITTED);
               loCurrentRow.save();

            }
            catch (Throwable loExp)
            {
               raiseException("Error: " + loExp.getMessage(),
                     SEVERITY_LEVEL_ERROR);
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            }
         }
         else
         {
            raiseException(
                  "Only 'To Be Submitted' or 'Inactive' jobs- can be submitted to run",
                  SEVERITY_LEVEL_ERROR);
         }

      } /* end if (ae.getName().equals("SubmitJob")) */
      /**
      * Determine if job is actually running.  In some cases, such as when a
      * a database connection goes down, the BS_AGENT table may indicate that a job is
      * running when it actually has failed.  When this link is clicked, it
      * will call isJobAlive() method if BS_AGENT indicates the job is running.
      * If the job is not really running, it will set the job to
      * completed / system failure.
      */
      else if (ae.getName().equals("VerifyJob"))
      {
         long llJobID = 0;
         String lsServerId = null;
         StringBuffer lsSql = new StringBuffer(500);
         StringBuffer lsSql2 = new StringBuffer(500);
         int liRunStatus = 0;
         VSResultSet loBsAgentRS = null;

         if (loCurrentRow == null)
         {
            raiseException("No row selected", SEVERITY_LEVEL_WARNING);
            return;
         }

         try
         {
            // Security check
            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
                  .getCurrentUserID(loCurrSession), loCurrentRow.getData(
                  "PNT_CTLG_ID").getLong(), AMSCommonConstants.ACTN_VERIFYJOB,
                  true, this))
            {
               /*
                * Case where the User is not authorized to run Verify Job Running. Cancel the
                * PageEvent and prevent the action.
                */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

			if(!AMSStringUtil.strEqual(AMSParams.msPrimaryApplication,
			   Integer.toString(loCurrentRow.getData("APPL_ID").getInt())))
			{
				raiseException("Can only verify jobs submitted from your primary application",SEVERITY_LEVEL_WARNING);
				return;
			}

            if (loCurrentRow !=null)
            {
			   // Get agnt_id which is primary key
               llJobID = loCurrentRow.getData("AGNT_ID").getLong();
            }

			/* Retrieve row from DB again.  Status could have changed from
			   what is on page and we might take inappropriate action.  One
			   example is if the page shows the job is running but the job
			   just completed, we could end up changing the status to
			   system failure because job manager would say it is not running.
			*/
         	VSQuery loQuery = new VSQuery(loCurrSession,
            	"BS_AGENT", " AGNT_ID = " + llJobID, "AGNT_ID ASC");

            loBsAgentRS = loQuery.execute();

            if (loBsAgentRS.first() != null)
            {
			    liRunStatus = loBsAgentRS.first().getData("RUN_STA").getInt();
				lsServerId = loBsAgentRS.first().getData("APPL_SRV_NM").getString();
			}
			else
			{
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("Error retrieving Job (Id :" + llJobID +") from BS_AGENT",
                      SEVERITY_LEVEL_ERROR);
               return;
			}

            // if job not picked up and server id not set
            if (lsServerId == null || lsServerId.trim().length() <1)
            {
               raiseException("This job is not assigned to a job manager yet",
                  SEVERITY_LEVEL_WARNING);
               return;
            }

            // only ready or running jobs can be killed
            if (!(liRunStatus == AMSBatchConstants.STATUS_RUNNING))
            {
			   loCurrentRow.refresh();
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("Only 'Running' jobs can be verified",
                  SEVERITY_LEVEL_WARNING);
               return;
		    }


            /*
             * connect  to job manager - job row has server Id
             * where the job is either assigned to run or
             * the value is updated by the job manager that picks
             * up the job to run
             */
            int liJobMgrId = connectToJobManager(loCurrentRow);

            if (liJobMgrId ==-1)
            {
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("Could not connect to job manager to verify running status",
                  SEVERITY_LEVEL_ERROR);
               return;
            }

            // check to see if job alive
            loRetObj = moJobManagerClient.isJobAlive(liJobMgrId,llJobID,
                AMSPLSUtil.getCurrentUserID(this),AMSPLSUtil.getPassword(this));
            if (loRetObj.getStatus()==AMSJobManagerMsg.OP_SUCCESSFUL)
            {
               Boolean loObj = (Boolean)loRetObj.getValue();
               if ( loObj.booleanValue()==false)
               {
                  // Change row on BS_AGENT to indicate job system failure
                  String lsDataSrvr = loCurrentSession.getDataServerForObject("BS_AGENT");
                  Session loSession = null;
                  loSession = ServerEnvironment.getServer()
            				.getExistingSession(loCurrentSession.getSessionID());
                  VSDate loTmSt = new VSDate();
                  lsSql.append("UPDATE ");
                  lsSql.append(AMSDataObject.getSchema("BS_AGENT", loSession));
                  lsSql.append(".BS_AGENT SET RUN_STA = ");
                  lsSql.append(AMSBatchConstants.STATUS_COMPLETE);
                  lsSql.append(", RET_CD = ");
                  lsSql.append(AMSBatchConstants.RET_CODE_SYSTEM_FAILURE);
                  lsSql.append(", RUN_END_TS = ");
                  lsSql.append(AMSSQLUtil.getAMSDate(loTmSt, null,
                               DataConst.TIMESTAMP, getDatabaseType()));
                  lsSql.append(" WHERE AGNT_ID = ");
                  lsSql.append(llJobID);
                  lsSql.append(" OR (ID = '");
                  lsSql.append(llJobID);
                  lsSql.append("' AND RUN_STA = ");
                  lsSql.append(AMSBatchConstants.STATUS_RUNNING);
                  lsSql.append(")");
			      AMSUtil.executeUpdate(lsSql.toString(), lsDataSrvr, loSession);
			      // Set remaining jobs in chain to inactive
                  lsSql2.append("UPDATE ");
                  lsSql2.append(AMSDataObject.getSchema("BS_AGENT", loSession));
                  lsSql2.append(".BS_AGENT SET RUN_STA = ");
                  lsSql2.append(AMSBatchConstants.STATUS_INACTIVE);
                  lsSql2.append(" WHERE ID = '");
                  lsSql2.append(llJobID);
                  lsSql2.append("' AND RUN_STA = ");
                  lsSql2.append(AMSBatchConstants.STATUS_SUBMITTED);
			      AMSUtil.executeUpdate(lsSql2.toString(), lsDataSrvr, loSession);
                  loCurrentRow.refresh();
                  evt.setNewPage(this);
                  evt.setCancel(true);
                  raiseException("Job (Id :" + llJobID +") is not running: setting to 'System Failure'",
                     SEVERITY_LEVEL_WARNING);
               }
               else
               {
				  evt.setNewPage(this);
			      evt.setCancel(true);
			      raiseException("Job (Id :" + llJobID +") is running",SEVERITY_LEVEL_INFO);
				  return;
			   }
            }
            else // remote op failed
            {
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("Could not connect to job manager (Id:"+liJobMgrId+")" ,
                  SEVERITY_LEVEL_ERROR);
               return;
            }
	     } // end try
         catch(Throwable loExp)
         {
            raiseException("Error:" +loExp.getMessage() ,SEVERITY_LEVEL_ERROR);
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            return;
         }
         finally
		 {
			if (loBsAgentRS != null)
			{
			   loBsAgentRS.close();
			}
         }
      } // end if (ae.getName().equals("VerifyJob"))

   }

   // END_EVENT_View_Pending_Chain_Jobs_beforeActionPerformed}}

   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      addPageListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         View_Pending_Chain_Jobs_beforeActionPerformed(ae, evt, preq);
      }
   }

   // END_EVENT_ADAPTER_CODE}}

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
    * Custom Implementation- sets title
    */
   public String generate()
   {
      /*
       * set page title based on job label
       */
      setTitle(getCustomTitle());

      DivElement loBlockToSetVisible;

      VSSession loCurrSession = getParentApp().getSession();

      /*
       * Case of User with role of ADMN or a role that is listed as
       * BatchAdminRoles on ADV30Params.ini.
       */
      if (AMSPLSUtil.isJobAdminRole(AMSPLSUtil.getCurrentUserID(loCurrSession)))
      {
         loBlockToSetVisible = (DivElement) getElementByName("SysAdminLinks");
         loBlockToSetVisible.setVisible(true);
         loBlockToSetVisible = (DivElement) getElementByName("UserLinks");
         loBlockToSetVisible.setVisible(false);
      }
      else
      {
         loBlockToSetVisible = (DivElement) getElementByName("SysAdminLinks");
         loBlockToSetVisible.setVisible(false);
         loBlockToSetVisible = (DivElement) getElementByName("UserLinks");
         loBlockToSetVisible.setVisible(true);
      }

      /* Assign dummy value */
      long llCtlgID = -1;
      try
      {
         VSORBSession loORBSession = loCurrSession.getORBSession();
         /* Get Catalog ID */
         llCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
      }// end try
      catch (Exception loExcp)
      {
         /* Failed to obtain Catalog ID from Session properties */
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException(
               "Failed to obtain Job information from Session properties",
               SEVERITY_LEVEL_ERROR);
         return super.generate();
      }

      DivElement loUserIdField = (DivElement) getElementByName("USER_ID_FIELD");
      DivElement loUserIdLabel = (DivElement) getElementByName("USER_ID_LABEL");

      /*
       * Check if the User is authorized to View Others' Jobs as per Security
       * settings
       */
      if (AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
            .getCurrentUserID(loCurrSession), llCtlgID,
            AMSCommonConstants.JOB_ACTN_VIEW_OTHERS, false, this))
      {
         /*
          * User is authorized to View Others' Jobs, hence allow search by User
          * ID
          */
         if (loUserIdField != null)
         {
            loUserIdField.setVisible(true);
         }

         if (loUserIdLabel != null)
         {
            loUserIdLabel.setVisible(true);
         }
      }
      else
      {
         /*
          * User is not authorized to View Others' Jobs, hence do not allow
          * search by User ID
          */
         if (loUserIdField != null)
         {
            loUserIdField.setVisible(false);
         }
         if (loUserIdLabel != null)
         {
            loUserIdLabel.setVisible(false);
         }
      }

      return super.generate();
   } // end of method generate

   /**
    * sets title that reflects the job label
    */
   private String getCustomTitle()
   {
      String lsTitle = "Pending Chain Jobs";
      String lsLabel = null;

      try
      {
         // access session property that contains the id of the node clicked
         lsLabel = parentApp.getSession().getORBSession().getProperty(
               RSAA_CTLG_LBL);
         // append job label to std title prefix
         if (lsLabel != null && lsLabel.length() > 0)
         {
            lsTitle = lsTitle + " for " + lsLabel;
         }
      }
      catch (Exception loExp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      }

      return lsTitle;
   } // end of method getCustomTitle

   /**
    * 'Pings' the Job Manager corresponding to the selected row. If successful,
    * the job manager id is returned. If connection cannot be made the value -1
    * is returned indicating failed connection
    *
    * @param foCurrRow
    *           Current Row
    * @return The current Job Manager ID if 'pinged' successfully, -1 otherwise
    */
   private int connectToJobManager(VSRow foCurrRow)
   {
      AMSJobManagerMsg loRetObj = null;
      int liJobMgrId = -1;
      try
      {
         VSRow loCurrRow = null;

         // if no row passed - get current Row
         if (foCurrRow == null)
         {
            loCurrRow = getRootDataSource().getCurrentRow();
         }
         else
         {
            loCurrRow = foCurrRow;
         }

         if (loCurrRow == null)
         {
            return -1;
         }
         liJobMgrId = loCurrRow.getData("APPL_SRV_NM").getInt();
         // initialize JobManagerClient if null
         if (moJobManagerClient == null)
         {
            moJobManagerClient = new AMSJobManagerClient(getParentApp()
                  .getSession());
         }
         loRetObj = moJobManagerClient.connectToJobManager(liJobMgrId,
               AMSPLSUtil.getCurrentUserID(this), AMSPLSUtil.getPassword(this));

         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            throw new Exception(loRetObj.getMessage());
         }

         return liJobMgrId;
      } // of try
      catch (Exception loExp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         return -1; // could not make a connection
      }
   } // end method connectToJobManager

}
