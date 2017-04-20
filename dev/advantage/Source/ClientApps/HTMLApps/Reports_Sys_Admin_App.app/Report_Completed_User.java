//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import versata.vls.*;
import versata.common.*;
import com.amsinc.gems.adv.common.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;

import org.apache.commons.logging.Log;
/*
 **  Report_Completed_User
 */

//{{FORM_CLASS_DECL
public class Report_Completed_User extends Report_Completed_UserBase

//END_FORM_CLASS_DECL}}
implements AMSBatchConstants
{
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Report_Completed_User.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // Declarations for instance variables used in the form
   // remote reference to Job Manager
   private boolean mboolIsLogFileTrans  = false;
         // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Report_Completed_User ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }
//{{EVENT_CODE
//{{EVENT_T2Jobs_Pending_User_beforePageNavigation
   void T2Jobs_Pending_User_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
   {
      try
      {
         /* Initialize to dummy value */
         long llCtlgID = -1;
         String lsNodeId = null;
         VSSession loCurrSession = getParentApp().getSession();
         try
         {
            VSORBSession loORBSession = loCurrSession.getORBSession();
            /* Get Catalog ID */
            lsNodeId = loORBSession.getProperty("RSAA_NODE_ID");
            llCtlgID = Long.parseLong(lsNodeId);
         }//end try
         catch( Exception loExcp )
         {
            /* Failed to obtain Catalog ID from Session properties */
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

            raiseException( "Failed to obtain Job information from Session properties",
               SEVERITY_LEVEL_ERROR );
            cancel.setValue(true);
            newPage.setValue(this);
            return;
         }

         String lsUserID = AMSPLSUtil.getCurrentUserID( loCurrSession );

         /* Check if the User is authorized to View Pending Jobs as per
            Security settings */
         if( !AMSPLSUtil.isJobActnAuthorizedForUser( loCurrSession,
            lsUserID, llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_PENDING,
            true, this ) )
         {
            /* User is not authorized to View Pending Jobs, hence the cancel
               the navigation to Pending Jobs page */
            cancel.setValue(true);
            newPage.setValue(this);
            return;
         }

         StringBuffer lsbWhereClause = new StringBuffer(160);
         lsbWhereClause.append("(PNT_CTLG_ID=")
            .append(lsNodeId)
            .append(")");

         /* Check if the User is authorized to View Others' Jobs as per
            Security settings */
         if( !AMSPLSUtil. isJobActnAuthorizedForUser( loCurrSession,
            lsUserID, llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_OTHERS,
            false, this) )
         {
            /* User is not authorized to View Others' Jobs, hence apply filter
               such that the User can view his/her jobs only. */
            lsbWhereClause.append(" AND (USID=")
               .append(AMSSQLUtil.getANSIQuotedStr( lsUserID, true ))
               .append(")");
         }
         lsbWhereClause.append(" AND (ITM_TYP=")
            .append(AMSBatchConstants.REPORT)
            .append(") AND (RUN_STA<>")
            .append(AMSBatchConstants.STATUS_COMPLETE)
            .append(")");
         nav.setDevWhere(lsbWhereClause.toString());
      }//end try
      catch( Exception loExcp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException( "Error occurred during processing:"
            + loExcp.getMessage(), SEVERITY_LEVEL_ERROR );
         cancel.setValue(true);
         newPage.setValue(this);
         return;
      }
   }




//END_EVENT_T2Jobs_Pending_User_beforePageNavigation}}
//{{EVENT_Report_Completed_User_beforeActionPerformed
   void Report_Completed_User_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      try
      {
         /*
         * This variable is used in the case if a transition to job log file
         * page is called in the sendingResponse adapter.  It should be false otherwise.
         * The value is marke dto be true in the beforePageNav adapter method where it is
         * determined if the transition should be to log entries (table page) or log file page
         */
         mboolIsLogFileTrans = false;

         syncRecordCurrency(getRootDataSource(),preq);

         //if job is complete with status 'FAILED' then
         // reset job to be restarted pending certain checks e.g. checkpoint value not null
         if (ae.getName() !=null &&
            ae.getName().equals("RestartJob"))
         {
            String lsCheckPoint =null;
            int    liRetCode    =0;
            VSRow  loCurrentRow = getRootDataSource().getCurrentRow();
            long   llJobID  =0;
            int    liReturnCode = 0;

            if (loCurrentRow ==null)
            {
               raiseException("No row selected",SEVERITY_LEVEL_ERROR);
               return;
            }

            try
            {
               // read current saved checkpoint value and job return code
               if (loCurrentRow !=null)
               {
                  lsCheckPoint = loCurrentRow.getData("CHK_PT").getString();
                  llJobID = loCurrentRow.getData("AGNT_ID").getLong();
                  liReturnCode = loCurrentRow.getData("RET_CD").getInt();
               }

               // if job sucessfully completed, raise Exception
               if (liReturnCode== AMSBatchConstants.RET_CODE_SUCCESSFUL)
               {
                  raiseException("Job sucessfully completed: cannot be restarted",
                     SEVERITY_LEVEL_ERROR);
                  return;
               }

            } // try
            catch(Throwable loExp)
            {
               raiseException("Error: " +loExp.getMessage() ,SEVERITY_LEVEL_ERROR);
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);

               return;
            }

            if (deleteCorrespondingLogEntries(llJobID) ==true)
            {
               // mark job to be restarted
               loCurrentRow.getData("RUN_STA").setInt(AMSBatchConstants.STATUS_SUBMITTED);
               loCurrentRow.getData("RET_CD").setString("");
               loCurrentRow.getData("RUN_STRT_TS").setString("");
               loCurrentRow.getData("RUN_END_TS").setString("");
               try
               {
                  /* Security checks are done on BS_AGENTImpl save and exceptions
                     may occur that should be caught and dealt appropriately. */
                  loCurrentRow.save();
               }//end try
               catch( Exception loExcp )
               {

                  // Add exception log to logger object
                  moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

                  raiseException("Error occurred during processing:"
                     + loExcp.getMessage(), SEVERITY_LEVEL_ERROR );
                  //Should undo change and refresh row, else page is unstable
                  loCurrentRow.undo();
                  loCurrentRow.refresh();
                  evt.setNewPage(this);
                  evt.setCancel(true);
                  return;
               }
               raiseException("Job submitted to be restarted",SEVERITY_LEVEL_INFO);
            }

         } /* if ae.getName("RestartJob") */
         else if (ae.getName() != null && ae.getName().equals("btnT3Add_New_Job"))
         {
            /* Initialize to dummy value */
            long llCtlgID = -1;
            VSSession loCurrSession = getParentApp().getSession();
            String lsUserID = AMSPLSUtil.getCurrentUserID( loCurrSession );
            try
            {
               VSORBSession loORBSession = loCurrSession.getORBSession();
               /* Get Catalog ID */
               llCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
            }//end try
            catch( Exception loExcp )
            {
               /* Failed to obtain Catalog ID from Session properties */
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

               raiseException( "Failed to obtain Job information from Session properties",
                  SEVERITY_LEVEL_ERROR );
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

            /* Check if the User is authorized to Schedule New Job as per security
               settings */
            if( !AMSPLSUtil. isJobActnAuthorizedForUser( loCurrSession,
               lsUserID, llCtlgID, AMSCommonConstants.JOB_ACTN_SCHEDULE,
               true, this ) )
            {
               /* Case where the User is not authorized to Schedule New Job.
                  Prevent the navigation to the page that allows to schedule new job. */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }
            /*
             * This logic ensures that a new page (Add New Job or corresponding
             * Page for Chain Jobs) is generated again and never cached
             */
            PageNavigation loNav = ae.getPageNavigation();
            VSPage loTargetPage = null;
            if (loNav !=null)
            {
               loTargetPage =loNav.getTargetPage();
            }

            if (loTargetPage  !=null)
            {
               loTargetPage.close(false,false,false);
               loTargetPage =null;
            }
         } //else if (ae.getName() != null && ae.getName().equals("btnT3Add_New_Job"))
      }//end try
      catch( Exception loExcp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException( "Error occurred during processing:" + loExcp.getMessage(),
            SEVERITY_LEVEL_ERROR );
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }
  }



//END_EVENT_Report_Completed_User_beforeActionPerformed}}
//{{EVENT_Report_Completed_User_sendingResponse
   void Report_Completed_User_sendingResponse( String originalResponse, VSOutParam cancel, VSOutParam newResponse )
   {
      if ( mboolIsLogFileTrans ==true)
      {
         // transition to jog log file display page
         newResponse.setValue(T6Job_Output_Files.startPage().generate());
         cancel.setValue(true);
         mboolIsLogFileTrans =false;
      }

   }


//END_EVENT_Report_Completed_User_sendingResponse}}
//{{EVENT_T4Job_Log_Entries_beforePageNavigation
   void T4Job_Log_Entries_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
   {
      try
      {
         VSSession loCurrSession = getParentApp().getSession();
         /* Assign dummy value */
         long llCtlgID = -1;
         try
         {
            VSORBSession loORBSession = loCurrSession.getORBSession();
            /* Get Catalog ID */
            llCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
         }//end try
         catch( Exception loExcp )
         {
            /* Failed to obtain Catalog ID from Session properties */
           // Add exception log to logger object
           moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

            raiseException( "Failed to obtain Job information from Session properties",
               SEVERITY_LEVEL_ERROR );
            cancel.setValue(true);
            newPage.setValue(this);
            return;
         }

         /* Check if the User is authorized to View Job Log as per
            Security settings */
         if( !AMSPLSUtil.isJobActnAuthorizedForUser( loCurrSession,
            AMSPLSUtil.getCurrentUserID( loCurrSession ), llCtlgID,
            AMSCommonConstants.JOB_ACTN_VIEW_LOG, true, this ) )
         {
            /* User is not authorized to View Job Log, hence the cancel
               the navigation to View Job Log page */
            cancel.setValue(true);
            newPage.setValue(this);
            return;
         }
      }//end try
      catch( Exception loExcp )
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException( "Error occurred during processing:" + loExcp.getMessage(),
            SEVERITY_LEVEL_ERROR );
         cancel.setValue(true);
         newPage.setValue(this);
         return;
      }
      /**
      * To set conditional navigation
      * If the batch log file exists, go to page Job_Output_Files
      * else, go to Job_Log_Entries.  set variable, mboolIsLogFileTrans to
      * true if transition to log file is required
      */

      try
      {
         if(doesJobLogFileExist())
         {
            mboolIsLogFileTrans =true;
            return;
         }
         else
         {
            mboolIsLogFileTrans =false;
            return;
         }
      } // try
      catch(Throwable loExp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } // catch


   }


//END_EVENT_T4Job_Log_Entries_beforePageNavigation}}
//{{EVENT_T1BS_AGENT_beforeQuery
void T1BS_AGENT_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   //Write Event Code below this line
   try
   {
      VSSession loCurrSession = getParentApp().getSession();
      /* Initialize to dummy value */
      long llCtlgID = -1;
      try
      {
         VSORBSession loORBSession = loCurrSession.getORBSession();
         /* Get Catalog ID */
         llCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
      }//end try
      catch( Exception loExcp )
      {
         /* Failed to obtain Catalog ID from Session properties */
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException( "Failed to obtain Job information from Session properties",
            SEVERITY_LEVEL_ERROR );
         resultset.setValue( query.getNewResultSet() ) ;
         return;
      }

      SearchRequest loSrchRqst = new SearchRequest();;
      StringBuffer lsbFilter = new StringBuffer(64);
      String lsUserID = AMSPLSUtil.getCurrentUserID( loCurrSession );

      /* Check if the User is authorized to View Completed Jobs as per security
         settings */
      if( !AMSPLSUtil. isJobActnAuthorizedForUser( loCurrSession,
         lsUserID, llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_COMPLETED, true, this ) )
      {
         /* Case where the User is not authorized to View Completed Jobs.
            Modify the query such that no records are returned. */
         lsbFilter.append(" AND (PNT_CTLG_ID=-1)");
         loSrchRqst.add(lsbFilter.toString());
      }
      else
      {
         /* User is authorized to View Completed Jobs */
         /* Check if the User is authorized to View Others Jobs as per security
         settings */
         if( !AMSPLSUtil. isJobActnAuthorizedForUser( loCurrSession,
            lsUserID, llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_OTHERS, false, this ) )
         {
            /* Case where the User is not authorized to View Others Jobs.
               Hence add filter so that the User can only see his/her jobs
               along with Report jobs those have Viewable by All Users flag
               marked as true. */
            lsbFilter.append(" AND (USID=")
               .append(AMSSQLUtil.getANSIQuotedStr( lsUserID, true))
               .append(" OR PUBLIC_FL=1)");
            loSrchRqst.add(lsbFilter.toString());
         }//end if View Others Jobs check
      }//end if View Completed Jobs check

      /* Use loSrchRqst only if it has been set with some filter */
      if( lsbFilter.length() != 0 )
      {
         /* Modify query as per User's privileges */
         query.addFilter( loSrchRqst );
      }
   }//end try
   catch( Exception loExcp )
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Error occurred during processing:" + loExcp.getMessage(),
         SEVERITY_LEVEL_ERROR );
      resultset.setValue( query.getNewResultSet() ) ;
      return;
   }
}
//END_EVENT_T1BS_AGENT_beforeQuery}}
//{{EVENT_T5Reports_Display_Pg_beforePageNavigation
void T5Reports_Display_Pg_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line
   try
   {
      VSSession loCurrSession = getParentApp().getSession();
      /* Assign dummy value */
      long llCtlgID = -1;
      try
      {
         VSORBSession loORBSession = loCurrSession.getORBSession();
         /* Get Catalog ID */
         llCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
      }//end try
      catch( Exception loExcp )
      {
         /* Failed to obtain Catalog ID from Session properties */
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException( "Failed to obtain Job information from Session properties",
            SEVERITY_LEVEL_ERROR );
         cancel.setValue(true);
         newPage.setValue(this);
         return;
      }

      /* Check if the User is authorized to View Job Report as per
         Security settings */
      if( !AMSPLSUtil.isJobActnAuthorizedForUser( loCurrSession,
         AMSPLSUtil.getCurrentUserID( loCurrSession ), llCtlgID,
         AMSCommonConstants.JOB_ACTN_VIEW_REPORT, true, this ) )
      {
         /* User is not authorized to View Job Report, hence the cancel
            the navigation to View Job Report page */
         cancel.setValue(true);
         newPage.setValue(this);
         return;
      }
   }//end try
   catch( Exception loExcp )
   {
      /* Failed to obtain Catalog ID from Session properties */
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Error occurred during processing:" + loExcp.getMessage(),
         SEVERITY_LEVEL_ERROR );
      cancel.setValue(true);
      newPage.setValue(this);
      return;
   }
}
//END_EVENT_T5Reports_Display_Pg_beforePageNavigation}}

//END_EVENT_CODE}}
   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T2Jobs_Pending_User.addPageNavigationListener(this);
	addPageListener(this);
	T4Job_Log_Entries.addPageNavigationListener(this);
	T1BS_AGENT.addDBListener(this);
	T5Reports_Display_Pg.addPageNavigationListener(this);
//END_EVENT_ADD_LISTENERS}}
   }
//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			Report_Completed_User_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T2Jobs_Pending_User) {
			T2Jobs_Pending_User_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T4Job_Log_Entries) {
			T4Job_Log_Entries_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T5Reports_Display_Pg) {
			T5Reports_Display_Pg_beforePageNavigation( obj, cancel, newPage );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_beforeQuery(query , resultset );
		}
	}
	public void sendingResponse ( VSPage obj, String originalResponse, VSOutParam cancel, VSOutParam newResponse ){
		Object source = obj;
		if (source == this ) {
			Report_Completed_User_sendingResponse( originalResponse, cancel, newResponse );
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
   * Method deletes the log entries corresponding to the job id
   * passed
   *
   * @return  if log entries were sucesssfully deleted
   */
   private boolean deleteCorrespondingLogEntries(long flJobID)
   {
      VSSession loCurrentSession = getParentApp().getSession();
      SearchRequest loSrReq = new SearchRequest();
      VSResultSet   loRS    =null;
      try
      {
         loSrReq.add("AGNT_ID = " + flJobID);
         VSQuery loQuery = new VSQuery(loCurrentSession,"BS_AGENT_LOG",loSrReq,null);
         loRS =loQuery.execute();
         VSRow loRow = loRS.last();
         int cnt = loRS.getRowCount();
         for(int i=1; i<= cnt;i++)
         {
            VSRowBasic loJobLogRow = (VSRowBasic)loRS.getRowAt(i);
            loJobLogRow.deleted(true);
            loJobLogRow.save();
         }
      } //end try
      catch(Throwable loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         if (loRS != null)  // close ResultSet
         {
            loRS.close();
         }
         return false;
      }
      try
      {
         if (loRS != null)
         {
            loRS.close();
         }
      }//try
      catch(Exception loExp1)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp1);

      }
      return true;   // Log entries sucessfully deleted
   }  /* end method deleteCorrespondingLogEntries(String)  */

  /**
   * sets title that reflects the job label
   */
   private String getCustomTitle()
   {
      String lsTitle = "Report Summary";
      String lsLabel = null;
      try
      {
         // access session property that contains the id of the node clicked
         lsLabel = parentApp.getSession().getORBSession().getProperty(RSAA_CTLG_LBL);
         // append job label to std title prefix
         if (lsLabel !=null && lsLabel.length() > 0 )
         {
            lsTitle = lsTitle +
               " for " +
               lsLabel;
         }
      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      }
      return lsTitle;
   } // end of method getCustomTitle

  /**
   * Custom Implementation
   */
   public String generate()
   {
      /*
       * The div element containing the 'Back' btn is only generated
       * if the source page is 'Admin_User_Chain_Job_Sch'(the chain job
       * summary page
       */
      DivElement loBackBtnBlock= (DivElement)getElementByName("BackBtnBlock");
      if (loBackBtnBlock !=null)
      {
         if (getSourcePage() instanceof Admin_User_Chain_Job_Sch)
         {
            loBackBtnBlock.setVisible(true);
         }
         else
         {
            loBackBtnBlock.setVisible(false);
         }
      }

      /*
      * The div element containing the 'NormalJobBlock' element is a link
      * to schedule new jobs.  It is disabled when the page is viewed from
      * within the chain job - i.e. individual job steps of a chain cannot be
      * scheduled
      */
      DivElement loScheduleJobLinkBlock= (DivElement)getElementByName("ScheduleJobLinkBlock");
      if (loScheduleJobLinkBlock !=null)
      {
         if (getSourcePage() instanceof Admin_User_Chain_Job_Sch)
         {
            // transitioning from chain job page
            loScheduleJobLinkBlock.setVisible(false);
         }
         else
         {
            loScheduleJobLinkBlock.setVisible(true);
         }
      } //if (loScheduleJobLinkBlock !=null)


      /*
       *  set page title based on job label
       */
      setTitle(getCustomTitle());
      return super.generate();
   }

   /**
   * For debugging
   */
   private void db(String fsMsg)
   {
      if( AMS_PLS_DEBUG )
      {
         System.err.println(fsMsg);
      }
   }

  /**
   * method for debugging purposes
   */
   private String printAllSessionProperties(VSORBSession foSession)
   {
      try
      {
         StringBuffer loBuffer = new StringBuffer(512);
         Property[] loProps = foSession.getPropertyList();
         loBuffer.append("\n*** Property List ****");
         for(int i=0; i < loProps.length ; i++)
         {
            loBuffer.append(loProps[i].name + "  =  " + loProps[i].value+"\n");
         }
         loBuffer.append("\n*** END Property List ****\n");
         return loBuffer.toString();
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         return "" ;
      } /* end catch( RemoteException foExp ) */
   } /* end method printAllSessionProperties(Session) */

   /**
   * This method is used to determine if a job batch log file
   * corresponding to the selected job row, exists.
   * This is used to select the transition to either the
   * job log entries (database) page or the page that
   * displays text output from the batch job log file
   */
   private boolean doesJobLogFileExist()
   {
      try
      {
         VSRow loCurrentRow  = getRootDataSource().getCurrentRow();
         String lsFileSeparator = File.separator;
                  if (loCurrentRow !=null)
         {
            String lsOutputDirectory = AMSParams.msAdvantageLogFolder + lsFileSeparator +
               AMSBatchConstants.LOG_OUTPUT_DIR+ lsFileSeparator +
               loCurrentRow.getData("AGNT_ID").getString()  + "_" +
               loCurrentRow.getData("BASE_TMPL_PATH").getString() +
               AMSBatchConstants.LOG_FILE_EXT;
            File loFile = new File(lsOutputDirectory);
            if (loFile.exists())
            {
               return true;
            }
            else
            {
               return false;
            }
         } // if (loCurrentRow !=null)
         else
         {
            return false;
         }
      } // of try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         return false;
      } // catch
   } // of doesJobLogFileExist()

 }
