//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;


import org.apache.commons.logging.Log;

/*
**  View_Restart_Chain_Job_List
*/

//{{FORM_CLASS_DECL
public class View_Restart_Chain_Job_List extends View_Restart_Chain_Job_ListBase

//END_FORM_CLASS_DECL}}
      implements AMSBatchConstants
{
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( View_Restart_Chain_Job_List.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   
   // Declarations for instance variables used in the form

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public View_Restart_Chain_Job_List ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_View_Restart_Chain_Job_List_requestReceived
void View_Restart_Chain_Job_List_requestReceived( PLSRequest req, PageEvent evt )
{
      if (req.getParameter("refresh_page") != null)
      {
         getRootDataSource().executeQuery();
         evt.setCancel(true);
         evt.setNewPage(this);
      } /* end if ( req.getParameter( "refresh_page" ) != null ) */
   }
//END_EVENT_View_Restart_Chain_Job_List_requestReceived}}
//{{EVENT_View_Restart_Chain_Job_List_beforeActionPerformed
void View_Restart_Chain_Job_List_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
      //Write Event Code below this line
      String lsActionName = ae.getName();
      /*
       * If job is complete with status 'FAILED' then
       * reset job to be restarted pending certain checks e.g. checkpoint value not null
       */
      if (lsActionName != null && lsActionName.equals("RestartJob"))
      {
         String lsCheckPoint = null;

         VSRow loCurrentRow = getRootDataSource().getCurrentRow();
         long llJobID = 0;
         int liReturnCode = 0;
         VSSession loCurrSession = getParentApp().getSession();
         VSORBSession loCurrentSession = loCurrSession.getORBSession();

         try
         {
            if (loCurrentRow == null)
            {
               raiseException("No row selected", SEVERITY_LEVEL_ERROR);
               return;
            }

            // security check
            long llNodeID = Long.parseLong(loCurrentSession
                  .getProperty(AMSBatchConstants.RSAA_NODE_ID));

            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession,
                  AMSPLSUtil.getCurrentUserID(loCurrSession), llNodeID,
                  AMSCommonConstants.JOB_ACTN_RESTART, true, this))
            {
               /* Case where the User is not authorized to restart Job.
                Prevent the navigation to the page that allows to restart job. */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

            // read current saved checkpoint value and job id
            lsCheckPoint = loCurrentRow.getData("CHK_PT").getString();
            llJobID = loCurrentRow.getData("AGNT_ID").getLong();
            liReturnCode = loCurrentRow.getData("RET_CD").getInt();

            // if job sucessfully completed, raise Exception
            if (liReturnCode == AMSBatchConstants.RET_CODE_SUCCESSFUL)
            {
               raiseException("Job sucessfully completed: cannot be restarted",
                     SEVERITY_LEVEL_ERROR);
               return;
            }

         } // try
         catch (Throwable loExp)
         {
            raiseException("Error: " + loExp.getMessage(), SEVERITY_LEVEL_ERROR);
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            return;
         }

         // Valid CheckPoint information must be saved for job to be restarted
         if (lsCheckPoint == null || lsCheckPoint.trim().length() == 0)
         {
            raiseException("No valid checkpoint data saved for job (Id:"
                  + llJobID + ")", SEVERITY_LEVEL_ERROR);
            return;
         }

         if (deleteCorrespondingLogEntries(llJobID) == true)
         {
            // mark job to be restarted
            loCurrentRow.getData("RUN_STA").setInt(
                  AMSBatchConstants.STATUS_SUBMITTED);
            loCurrentRow.getData("RET_CD").setString("");
            loCurrentRow.getData("RUN_STRT_TS").setString("");
            loCurrentRow.getData("RUN_END_TS").setString("");

            try
            {
               /* Security checks are done on BS_AGENTImpl save and exceptions
                may occur that should be caught and dealt appropriately. */
               loCurrentRow.save();
            }//end try
            catch (Exception loExcp)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

               raiseException("Error occurred during processing:"
                     + loExcp.getMessage(), SEVERITY_LEVEL_ERROR);
               //Should undo change and refresh row, else page is unstable
               loCurrentRow.undo();
               loCurrentRow.refresh();
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }
            raiseException("Job submitted to be restarted", SEVERITY_LEVEL_INFO);
         }

      } // if ae.getName("RestartJob")

   }
//END_EVENT_View_Restart_Chain_Job_List_beforeActionPerformed}}

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
			View_Restart_Chain_Job_List_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			View_Restart_Chain_Job_List_beforeActionPerformed( ae, evt, preq );
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

   public String generate()
   {
      setTitle(getCustomTitle());
      return super.generate();
   } /* end method generate() */

   /**
    * Method deletes the log entries corresponding to the job id
    * passed
    *
    * @return  if log entries were sucesssfully deleted
    */
   private boolean deleteCorrespondingLogEntries(long flJobID)
   {
      VSSession loCurrentSession = getParentApp().getSession();
      SearchRequest loSrReq = new SearchRequest();
      VSResultSet loRS = null;
      try
      {
         loSrReq.add("AGNT_ID = " + flJobID);
         VSQuery loQuery = new VSQuery(loCurrentSession, "BS_AGENT_LOG",
               loSrReq, null);
         loRS = loQuery.execute();
         VSRow loRow = loRS.last();
         int cnt = loRS.getRowCount();
         for (int i = 1; i <= cnt; i++)
         {
            VSRowBasic loJobLogRow = (VSRowBasic) loRS.getRowAt(i);
            loJobLogRow.deleted(true);
            loJobLogRow.save();
         }
      } //end try
      catch (Throwable loExp)
      {
         if (AMS_PLS_DEBUG)
         {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         }
         return false;
      }
      finally
      {
         if (loRS != null)
         {
            loRS.close();
         }
      }
      return true; // Log entries sucessfully deleted
   } /* end method deleteCorrespondingLogEntries(String)  */

   /**
    * sets title that reflects the job label
    */
   private String getCustomTitle()
   {
      String lsTitle = "Chain Job Steps";
      String lsLabel = null;
      String lsJobId = null;

      try
      {
         // access session property that contains the catalog label of the node clicked
         lsLabel = parentApp.getSession().getORBSession().getProperty(
               RSAA_CTLG_LBL);

         // append job label to std title prefix

         // access session property that contains the job id of the node clicked
         lsJobId = parentApp.getSession().getORBSession().getProperty(
               RSAA_JOBID);

         if (lsLabel != null && lsLabel.length() > 0)
         {
            lsTitle = lsTitle + " for " + lsLabel;
         }

         if (lsJobId != null && lsJobId.length() > 0)
         {
            lsTitle = lsTitle + " (" + lsJobId + ")";
         }
      }
      catch (Exception loExp)
      {
         if (AMS_PLS_DEBUG)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         }
      }

      return lsTitle;
   } // end of method getCustomTitle

}
