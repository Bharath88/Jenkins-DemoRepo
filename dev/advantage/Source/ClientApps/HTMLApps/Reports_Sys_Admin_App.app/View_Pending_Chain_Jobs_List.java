/*
 * @(#)View_Pending_Chain_Jobs_List.java 1.0 Dec 15, 2006
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
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSUtil;
import versata.vls.Session;
import advantage.AMSDataObject;
import versata.vls.ServerEnvironment;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSParams;

import org.apache.commons.logging.Log;


/*
 * * View_Pending_Chain_Jobs_List
 */

// {{FORM_CLASS_DECL
public class View_Pending_Chain_Jobs_List extends
      View_Pending_Chain_Jobs_ListBase

// END_FORM_CLASS_DECL}}
      implements AMSBatchConstants
{

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.

   // Job Manager Client to 'talk' to different job managers
   private AMSJobManagerClient moJobManagerClient = null;
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( View_Pending_Chain_Jobs_List.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // {{FORM_CLASS_CTOR
   public View_Pending_Chain_Jobs_List(PLSApp parentApp)
                                                        throws VSException,
                                                        java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE
   // {{EVENT_View_Pending_Chain_Jobs_List_beforeActionPerformed
   void View_Pending_Chain_Jobs_List_beforeActionPerformed(ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      // Write Event Code below this line

      AMSJobManagerMsg loRetObj = null;

      if (ae.getName().equals("KillJob"))
      {
         String lsConfirmKill = preq.getParameter("ConfirmKill"); // in
                                                                  // JS/batch.js

         if (lsConfirmKill.equals("No"))
         {
            evt.setNewPage(this);
            evt.setCancel(true);
            return;
         }

         VSRow loCurrentRow = getRootDataSource().getCurrentRow();
         long llJobID = 0;
         String lsServerId = null;
         int liRunStatus = 0;

         if (loCurrentRow == null)
         {
            raiseException("No row selected", SEVERITY_LEVEL_ERROR);
            return;
         }

         try
         {
            // Security check

            VSSession loCurrSession = getParentApp().getSession();
            VSORBSession loCurrentSession = loCurrSession.getORBSession();
            long llNodeID = Long.parseLong(loCurrentSession
                  .getProperty(AMSBatchConstants.RSAA_NODE_ID));

            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession,
                  AMSPLSUtil.getCurrentUserID(loCurrSession), llNodeID,
                  AMSCommonConstants.JOB_ACTN_KILL, true, this))
            {
               /*
                * Case where the User is not authorized to kill Job. Prevent the
                * navigation to the page that allows to kill job.
                */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

            // initialize values
            if (loCurrentRow != null)
            {
               llJobID = loCurrentRow.getData("AGNT_ID").getLong();
               lsServerId = loCurrentRow.getData("APPL_SRV_NM").getString();
               liRunStatus = loCurrentRow.getData("RUN_STA").getInt();
            }

            // if job not picked up and server id not set
            if (lsServerId == null || lsServerId.trim().length() < 1)
            {
               raiseException("This job is yet to be run: cannot be killed",
                     SEVERITY_LEVEL_ERROR);
               return;
            }

            // only ready or running jobs can be killed
            if (!(liRunStatus == AMSBatchConstants.STATUS_READY
                  || liRunStatus == AMSBatchConstants.STATUS_RUNNING))
            {
               raiseException("Only jobs picked up to be run can be 'killed'",
                     SEVERITY_LEVEL_ERROR);
               return;

            }

            /*
             * connect to job manager - job row has server Id where the job is
             * either assigned to run or the value is updated by the job manager
             * that picks up the job to run
             */
            int liJobMgrId = connectToJobManager(loCurrentRow);

            if (liJobMgrId == -1)
            {
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("Could not connect to job manager to 'kill' job",
                     SEVERITY_LEVEL_ERROR);
               return;
            }

            // check to see if job alive
            loRetObj = moJobManagerClient.isJobAlive(liJobMgrId, llJobID,
                  AMSPLSUtil.getCurrentUserID(this), AMSPLSUtil
                        .getPassword(this));
            if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
            {
               Boolean loObj = (Boolean) loRetObj.getValue();
               if (loObj.booleanValue() == false)
               {
                  evt.setNewPage(this);
                  evt.setCancel(true);
                  raiseException("Job (Id :" + llJobID
                        + ") is not running: cannot be 'killed'",
                        SEVERITY_LEVEL_ERROR);
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

            // destroy job
            loRetObj = moJobManagerClient.destroyJob(liJobMgrId, llJobID,
                  AMSPLSUtil.getCurrentUserID(this), AMSPLSUtil
                        .getPassword(this));
            if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
            {
               Boolean loObj = (Boolean) loRetObj.getValue();
               if (loObj.booleanValue() == false)
               {
                  // job could not be killed
                  evt.setNewPage(this);
                  evt.setCancel(true);
                  raiseException("Job '" + llJobID
                        + "' could not be terminated", SEVERITY_LEVEL_ERROR);
                  return;
               }
               else
               {
                  // job killed
                  raiseException(
                        "A notification has been sent to terminate job '"
                              + llJobID + "'", SEVERITY_LEVEL_INFO);
               }
            }
            else
            // remote op failed
            {
               evt.setNewPage(this);
               evt.setCancel(true);
               raiseException("Could not connect to job manager '" + liJobMgrId
                     + "'", SEVERITY_LEVEL_ERROR);
               return;
            }
         } // try
         catch (Throwable loExp)
         {
            raiseException("Error:" + loExp.getMessage(), SEVERITY_LEVEL_ERROR);
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            return;
         }
      } // end if (ae.getName().equals("KillJob"))

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
         VSRow loCurrentRow = getRootDataSource().getCurrentRow();
         long llJobID = 0;
         long llPntJobID = 0;
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
            VSSession loCurrSession = getParentApp().getSession();
            VSORBSession loCurrentSession = loCurrSession.getORBSession();

            if (loCurrentRow !=null)
            {
			   // Get agnt_id which is primary key
               llJobID = loCurrentRow.getData("AGNT_ID").getLong();
               // Get Chain job id too
               llPntJobID = loCurrentRow.getData("ID").getLong();
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

            // Security check
            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
                  .getCurrentUserID(loCurrSession), loBsAgentRS.first().getData(
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
			   Integer.toString(loBsAgentRS.first().getData("APPL_ID").getInt())))
			{
				raiseException("Can only verify jobs submitted from your primary application",SEVERITY_LEVEL_WARNING);
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
                  // Also change Chain Job status (AGNT_ID chain = ID of individual job)
                  if (llPntJobID > 0)
                  {
                     lsSql.append(" OR AGNT_ID = ");
                     lsSql.append(llPntJobID);
				  }
                  AMSUtil.executeUpdate(lsSql.toString(), lsDataSrvr, loSession);
                  // Change any remaining submitted items in chain to invalid
                  lsSql2.append("UPDATE ");
                  lsSql2.append(AMSDataObject.getSchema("BS_AGENT", loSession));
                  lsSql2.append(".BS_AGENT SET RUN_STA = ");
                  lsSql2.append(AMSBatchConstants.STATUS_INACTIVE);
                  lsSql2.append(" WHERE ID = '");
                  lsSql2.append(llPntJobID);
                  lsSql2.append("' AND RUN_STA = ");
                  lsSql2.append(AMSBatchConstants.STATUS_SUBMITTED);
			      AMSUtil.executeUpdate(lsSql2.toString(), lsDataSrvr, loSession);
                  getRootDataSource().executeQuery();
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

   // END_EVENT_View_Pending_Chain_Jobs_List_beforeActionPerformed}}
   // {{EVENT_View_Pending_Chain_Jobs_List_requestReceived
   void View_Pending_Chain_Jobs_List_requestReceived(PLSRequest req,
         PageEvent evt)
   {
      // Write Event Code below this line

      if (req.getParameter("refresh_page") != null)
      {
         getRootDataSource().executeQuery();
         evt.setCancel(true);
         evt.setNewPage(this);
         /* If the current frame is not equal to target frame then set Current
            frame to target frame */
         if( ! (getParentApp().getCurrentFrameName().equals(getTargetFrame()) ) )
         {
            getParentApp().setCurrentFrameName(getTargetFrame());
         }
      } /* end if ( req.getParameter( "refresh_page" ) != null ) */
   }

   // END_EVENT_View_Pending_Chain_Jobs_List_requestReceived}}

   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      addPageListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE

   public void requestReceived(VSPage obj, PLSRequest req, PageEvent evt)
   {
      Object source = obj;
      if (source == this)
      {
         View_Pending_Chain_Jobs_List_requestReceived(req, evt);
      }
   }

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         View_Pending_Chain_Jobs_List_beforeActionPerformed(ae, evt, preq);
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
    * methods sets one of the two div blocks visible One of the div blocks has
    * links relevant to the system admin; the other div block is relevant to
    * normal user. The super class is subclassed here to fetch user / resource
    * specific info
    */
   public String generate()
   {
      // set title
      setTitle(getCustomTitle());

      return super.generate();
   } /* end method generate() */

   /**
    * sets title that reflects the job label
    */
   private String getCustomTitle()
   {
      String lsTitle = "Pending Job Steps";
      String lsLabel = null;

      try
      {
         // access session property that contains the id of the node clicked
         lsLabel = parentApp.getSession().getORBSession().getProperty(
               RSAA_CTLG_LBL);
         // append job label to std title prefix
         if (lsLabel != null && lsLabel.length() > 0)
         {
            lsTitle = lsTitle + " for " + lsLabel + " Chain ";
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
      } //of try
      catch (Exception loExp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         return -1; // could not make a connection
      }
   } // end method connectToJobManager

}
