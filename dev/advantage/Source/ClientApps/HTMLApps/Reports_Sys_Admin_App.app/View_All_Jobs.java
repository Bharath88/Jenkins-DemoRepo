//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSecurityObject ;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSPage ;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import advantage.AMSUtil;
import advantage.AMSStringUtil;
import versata.vls.Session;
import advantage.AMSDataObject;
import versata.vls.ServerEnvironment;
import com.amsinc.gems.adv.client.batch.AMSJobManagerClient;
import com.amsinc.gems.adv.client.batch.AMSJobManagerMsg;
import com.amsinc.gems.adv.client.batch.AMSPLSBatchUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSParams;

import org.apache.commons.logging.Log;


/*
**  View_All_Jobs*/

//{{FORM_CLASS_DECL
public class View_All_Jobs extends View_All_JobsBase

//END_FORM_CLASS_DECL}}
{
   private AMSJobManagerClient moJobManagerClient = null;

   // Declarations for instance variables used in the form
              // The log file might be on the file system or be table driven
       private boolean mboolIsLogFileTrans  = false;
   //Initialized to true
   private boolean mboolIsFirstQueryEvent = true;
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( View_All_Jobs.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

         // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public View_All_Jobs ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
              }



//{{EVENT_CODE
//{{EVENT_T1VIEW_ENT_JOBS_QRY_beforeQuery
void T1VIEW_ENT_JOBS_QRY_beforeQuery( VSQuery query ,VSOutParam resultset )
{
   try
   {
      /* Only execute for the first time */
      if (mboolIsFirstQueryEvent )
     {
         String lsUserId = AMSPLSUtil.getCurrentUserID( this );

         SearchRequest loSrch = new SearchRequest();
         StringBuffer lsbWhereClause = new StringBuffer(64) ;
         StringBuffer lsbFirstWhereClause = new StringBuffer(64) ;
         String lsDevWhere = getRootDataSource().getQueryInfo().getDevWhere();
         String lsQryWhere = getRootDataSource().getOnScreenQueryText();

         if ((!AMSStringUtil.strIsEmpty(lsDevWhere)
               || !AMSStringUtil.strIsEmpty(lsQryWhere)))
         {
            lsbFirstWhereClause.append(" AND ");
         }
         /* Get all jobs except individual jobs in Chain.
            Include Chain job Header entries too. */
         lsbWhereClause.append("(V_BS_AGENT.ID IS NULL)");

         /* A User that has ADMN role or a role that is listed as
            BatchAdminRoles on ADV30Params.ini, would be able to see
            all jobs on this page. For rest of the Users, filter
            by User ID and Application ID will be applied which
            means they would only be able to see their own jobs for the
            applications they are authorized to. */
         if( !AMSPLSUtil.isJobAdminRole( lsUserId ) )
         {
            String lsAppWhere = null ;
            VSORBSession loORBSession = getParentApp().getSession()
               .getORBSession();
            AMSSecurityObject loSecObj = null ;

            /* Retrieve the application where clause */
            try
            {
               loSecObj   = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
               lsAppWhere = AMSSecurityObject.getApplicationAuthWhere( loSecObj,
                  true, "V_BS_AGENT" ) ;
            }
            catch ( RemoteException loRemExp )
            {
               raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
               resultset.setValue( query.getNewResultSet() ) ;
               return ;
            }

            lsbWhereClause.append( " AND ((V_BS_AGENT.USID=" ) ;
            lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsUserId, true) ) ;
            lsbWhereClause.append( ") AND (" ) ;
            lsbWhereClause.append( lsAppWhere ) ;
            lsbWhereClause.append( ")) " ) ;
         }//not isJobAdminRole
         lsbFirstWhereClause.append(lsbWhereClause.toString());
         loSrch.add( lsbFirstWhereClause.toString() ) ;
         query.addFilter(loSrch);
         /* Note that first time mboolIsFirstQueryEvent is true and
            above block will take care of modifying the query.
            Call setQueryInfo to modify the query of the datasource permanently
            so that for non-first times( attempts after first time)
            the modified query is used instead of framing it on every beforeQuery.
            Side Note: For the first time setQueryInfo is too late to modify the
            query and hence query.addFilter method is used for first time. */
         getRootDataSource().setQueryInfo( getRootDataSource().getMetaQueryName(),
            lsbWhereClause.toString(), AMSCommonConstants.EMPTY_STR,
            AMSCommonConstants.EMPTY_STR ) ;

         /* Reset so that it this block is not executed next time */
         mboolIsFirstQueryEvent = false;
      } //end if (mboolIsFirstQueryEvent )
   }//end try
   catch( Exception loExcp )
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Error occurred during beforeQuery event",
         AMSPage.SEVERITY_LEVEL_ERROR ) ;
      resultset.setValue( query.getNewResultSet() ) ;
      return ;
   }//end catch
}
//END_EVENT_T1VIEW_ENT_JOBS_QRY_beforeQuery}}
//{{EVENT_View_All_Jobs_beforeActionPerformed
void View_All_Jobs_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line
  /**
  * Determine if job is actually running.  In some cases, such as when a
  * a database connection goes down, the BS_AGENT table may indicate that a job is
  * running when it actually has failed.  When this link is clicked, it
  * will call isJobAlive() method if BS_AGENT indicates the job is running.
  * If the job is not really running, it will set the job to
  * completed / system failure.
  */
  if (ae.getName().equals("VerifyJob"))
  {
	 AMSJobManagerMsg loRetObj = null;
     VSRow loCurrentRow = getRootDataSource().getCurrentRow();
     VSResultSet loBsAgentRS = null;
     VSResultSet loVBsAgentRS = null;
     String lsServerId = null;
     String lsAgntClsNm = null;
     StringBuffer lsSql = new StringBuffer(500);
     StringBuffer lsSql2 = new StringBuffer(500);
     long llJobID = 0;
     long llPntJobID = 0;
     int liRunStatus = 0;
     int liServerId = 0;

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

		    liRunStatus = loBsAgentRS.first().getData("RUN_STA").getInt();
			lsServerId = loBsAgentRS.first().getData("APPL_SRV_NM").getString();
			liServerId = loBsAgentRS.first().getData("APPL_SRV_NM").getInt();
			lsAgntClsNm = loBsAgentRS.first().getData("AGNT_CLS_NM").getString();
		}
		else
		{
		   VSQuery loQuery2 = new VSQuery(loCurrSession,
		   "V_BS_AGENT", " AGNT_ID = " + llJobID, "AGNT_ID ASC");
		   loVBsAgentRS = loQuery2.execute();
		   if (loVBsAgentRS.first() != null)
		   {
			  if(!AMSStringUtil.strEqual(AMSParams.msPrimaryApplication,
			     Integer.toString(loVBsAgentRS.first().getData("APPL_ID").getInt())))
			  {
				 raiseException("Can only verify jobs submitted from your primary application",
				                 SEVERITY_LEVEL_WARNING);
				 return;
			  }
		      else
		      {
                 evt.setNewPage(this);
                 evt.setCancel(true);
                 raiseException("Error retrieving Job (Id :" + llJobID +") from V_BS_AGENT",
                                 SEVERITY_LEVEL_ERROR);
                 return;
		      }
		   }
		   else
		   {
              evt.setNewPage(this);
              evt.setCancel(true);
              raiseException("Error retrieving Job (Id :" + llJobID +") from BS_AGENT",
                    SEVERITY_LEVEL_ERROR);
              return;
		   }
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
        int liJobMgrId = connectToJobManager(liServerId);

        if (liJobMgrId ==-1)
        {
           evt.setNewPage(this);
           evt.setCancel(true);
           raiseException("Could not connect to job manager to verify running status",
              SEVERITY_LEVEL_ERROR);
           return;
        }

        // check to see if job or chain alive
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
              if (AMSStringUtil.strEqual(lsAgntClsNm, "Chain Job"))
              {
				 // Set Chain job and running job in chain to complete/system failure
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
			  }
			  else
			  {
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
			     AMSUtil.executeUpdate(lsSql.toString(), lsDataSrvr, loSession);
		      }
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
		if (loVBsAgentRS != null)
		{
		   loVBsAgentRS.close();
		}
     }
  } // end if (ae.getName().equals("VerifyJob"))

}
//END_EVENT_View_All_Jobs_beforeActionPerformed}}
// DELETED_BEGIN 
//{{EVENT_T4Job_Log_Entries_beforePageNavigation

//END_EVENT_T4Job_Log_Entries_beforePageNavigation}}
 // DELETED_END 

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1VIEW_ENT_JOBS_QRY.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			View_All_Jobs_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1VIEW_ENT_JOBS_QRY) {
			T1VIEW_ENT_JOBS_QRY_beforeQuery(query , resultset );
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
    * 'Pings' the Job Manager corresponding to the selected row. If successful,
    * the job manager id is returned. If connection cannot be made the value -1
    * is returned indicating failed connection
    *
    * @param foCurrRow
    *           Current Row
    * @return The current Job Manager ID if 'pinged' successfully, -1 otherwise
    */
   private int connectToJobManager(int liServerId)
   {
      AMSJobManagerMsg loRetObj = null;

      try
      {
         // initialize JobManagerClient if null
         if (moJobManagerClient == null)
         {
            moJobManagerClient = new AMSJobManagerClient(getParentApp()
                  .getSession());
         }
         loRetObj = moJobManagerClient.connectToJobManager(liServerId,
               AMSPLSUtil.getCurrentUserID(this), AMSPLSUtil.getPassword(this));

         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            throw new Exception(loRetObj.getMessage());
         }

         return liServerId;
      } //of try
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         return -1; // could not make a connection
      }
   } // end method connectToJobManager

   }
