// {{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
// END_IMPORT_STMTS}}

import versata.vls.*;
import java.util.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;

/*
 * * Admin_User_Chain_Job_Sch
 */

// {{FORM_CLASS_DECL
public class Admin_User_Chain_Job_Sch extends Admin_User_Chain_Job_SchBase

// END_FORM_CLASS_DECL}}
      implements AMSBatchConstants
{
   // Declarations for instance variables used in the form

   public static final String FOLDER_CLASS = "Folder";

   public static final String JADE_REPORT_CLASS = "Report";

   public static final String ACTUATE_REPORT_CLASS = "ActuateReport";

   public static final String SYSTEM_BATCH_CLASS = "SystemBatch";

   public static final String CHAIN_JOB_CLASS = "ChainJob";
   
   /** This is the logger object for the class */
   private static Log moAMSLog = AMSLogger.getLog( Admin_User_Chain_Job_Sch.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
   // {{FORM_CLASS_CTOR
public Admin_User_Chain_Job_Sch ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}

   }

   // {{EVENT_CODE
   // {{EVENT_Admin_User_Chain_Job_Sch_beforeActionPerformed
   void Admin_User_Chain_Job_Sch_beforeActionPerformed(ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      try
      {
         String lsName = ae.getName();

         if (lsName != null
               && lsName.equals("btnT2pChain_Job_Steps_Grid"))
         {
            /* Initialize to dummy value */
            long llCtlgID = -1;
            VSSession loCurrSession = getParentApp().getSession();
            try
            {
               VSORBSession loORBSession = loCurrSession.getORBSession();
               /* Get Catalog ID */
               llCtlgID = Long.parseLong(loORBSession
                     .getProperty("RSAA_NODE_ID"));
            }// end try
            catch (Exception loExcp)
            {
               /* Failed to obtain Catalog ID from Session properties */
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

               raiseException(
                     "Failed to obtain Job information from Session properties",
                     SEVERITY_LEVEL_ERROR);
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }
            /*
             * Check if the User is authorized to Schedule New Job as per
             * security settings
             */
            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession,
                  AMSPLSUtil.getCurrentUserID(loCurrSession), llCtlgID,
                  AMSCommonConstants.JOB_ACTN_SCHEDULE, true, this))
            {
               /*
                * Case where the User is not authorized to Schedule New Job.
                * Prevent the navigation to the page that allows to schedule new
                * job.
                */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }
            //Refresh(delete and insert) R_BS_CATALOG_TEMP entries
            if( !prepareCatalogTempEntries( llCtlgID ) )
            {
               /* If delete and insert of R_BS_CATALOG_TEMP failed, then
                  do not navigate to the page that allows the user to Schedule
                  new Chain Job(because R_BS_CATALOG_TEMP entries weren't
                  built correctly). */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

            StringBuffer lsbWhere = new StringBuffer(64);
            lsbWhere.append("USID='");
            lsbWhere.append(AMSSQLUtil.getANSIQuotedStr(getParentApp()
                  .getSession().getLogin()));
            lsbWhere.append("' AND CHN_JOB_ID=");
            lsbWhere.append(llCtlgID);

            T2pChain_Job_Steps_Grid.setDevWhere(lsbWhere.toString());
            T2pChain_Job_Steps_Grid.setOrderBy("SEQ_NO ASC");

            /*
             * This logic ensures that a new page (Add New Job or corresponding
             * Page for Chain Jobs- Chain_Job_Schedule) is generated again and
             * never cached
             */
            PageNavigation loNav = ae.getPageNavigation();
            VSPage loTargetPage = null;
            if (loNav != null)
            {
               loTargetPage = loNav.getTargetPage();
            }

            if (loTargetPage != null)
            {
               loTargetPage.close(false, false, false);
               loTargetPage = null;
            }
         }
      }// end try
      catch (Exception loExcp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException("Error occurred during processing:"
               + loExcp.getMessage(), SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }
   }

   // END_EVENT_Admin_User_Chain_Job_Sch_beforeActionPerformed}}
   // {{EVENT_T3View_Pending_Chain_Jobs_beforePageNavigation
   void T3View_Pending_Chain_Jobs_beforePageNavigation(PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage)
   {
      // Write Event Code below this line

      try
      {
         VSSession loCurrSession = getParentApp().getSession();

         VSORBSession loCurrentSession = loCurrSession.getORBSession();
         String lsNodeId = loCurrentSession.getProperty("RSAA_NODE_ID");

         String lsUserID = AMSPLSUtil.getCurrentUserID(loCurrSession);
         long llCtlgID = Long.parseLong(lsNodeId);

         /*
          * Check if the User is authorized to View Pending Jobs as per Security
          * settings
          */
         if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, lsUserID,
               llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_PENDING, true, this))
         {
            /*
             * User is not authorized to View Pending Jobs, hence the cancel the
             * navigation to Pending Jobs page
             */
            cancel.setValue(true);
            newPage.setValue(this);
            return;
         }

         StringBuffer lsbWhereClause = new StringBuffer(160);
         lsbWhereClause.append("(PNT_CTLG_ID=").append(lsNodeId).append(")");

         /*
          * Check if the User is authorized to View Others' Jobs as per Security
          * settings
          */
         if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, lsUserID,
               llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_OTHERS, false, this))
         {
            /*
             * User is not authorized to View Others' Jobs, hence apply filter
             * such that the User can view his/her jobs only.
             */
            lsbWhereClause.append(" AND (USID=").append(
                  AMSSQLUtil.getANSIQuotedStr(lsUserID, true)).append(")");
         }
         lsbWhereClause.append(" AND (ITM_TYP=").append(
               AMSBatchConstants.CHAIN_JOB).append(") AND (RUN_STA<>").append(
               AMSBatchConstants.STATUS_COMPLETE).append(")");
         nav.setDevWhere(lsbWhereClause.toString());

         nav.setOrderBy("AGNT_ID DESC");

      } /* end try */
      catch (RemoteException foExp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException("Unable to get message properties",
               SEVERITY_LEVEL_SEVERE);
         return;
      } /* end catch( RemoteException foExp ) */

   }

   // END_EVENT_T3View_Pending_Chain_Jobs_beforePageNavigation}}
   // {{EVENT_T1BS_AGENT_beforeQuery
   void T1BS_AGENT_beforeQuery(VSQuery query, VSOutParam resultset)
   {
      // Write Event Code below this line
      // Write Event Code below this line
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
         }// end try
         catch (Exception loExcp)
         {
            /* Failed to obtain Catalog ID from Session properties */
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

            raiseException(
                  "Failed to obtain Job information from Session properties",
                  SEVERITY_LEVEL_ERROR);
            resultset.setValue(query.getNewResultSet());
            return;
         }

         SearchRequest loSrchRqst = new SearchRequest();
         StringBuffer lsbFilter = new StringBuffer(32);
         String lsUserID = AMSPLSUtil.getCurrentUserID(loCurrSession);
         /*
          * Check if the User is authorized to View Completed Jobs as per
          * security settings
          */
         if (!AMSPLSUtil
               .isJobActnAuthorizedForUser(loCurrSession, lsUserID, llCtlgID,
                     AMSCommonConstants.JOB_ACTN_VIEW_COMPLETED, true, this))
         {
            /*
             * Case where the User is not authorized to View Completed Jobs.
             * Modify the query such that no records are returned.
             */
            lsbFilter.append(" AND (PNT_CTLG_ID=-1)");
            loSrchRqst.add(lsbFilter.toString());
         }
         else
         {
            /* User is authorized to View Completed Jobs */
            /*
             * Check if the User is authorized to View Others Jobs as per
             * security settings
             */
            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, lsUserID,
                  llCtlgID, AMSCommonConstants.JOB_ACTN_VIEW_OTHERS, false,
                  this))
            {
               /*
                * Case where the User is not authorized to View Others Jobs.
                * Hence add filter so that the User can only see his/her jobs.
                */
               lsbFilter.append(" AND (USID= ").append(
                     AMSSQLUtil.getANSIQuotedStr(lsUserID, true)).append(")");
               loSrchRqst.add(lsbFilter.toString());
            }// end if View Others Jobs check
         }// end if View Completed Jobs check

         /* Use loSrchRqst only if it has been set with some filter */
         if (lsbFilter.length() != 0)
         {
            /* Modify query as per User's privileges */
            query.addFilter(loSrchRqst);
         }// end if filter applied
      }// end try
      catch (Exception loExcp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException("Error occurred during processing:"
               + loExcp.getMessage(), SEVERITY_LEVEL_ERROR);
         resultset.setValue(query.getNewResultSet());
         return;
      }

   }

   // END_EVENT_T1BS_AGENT_beforeQuery}}
   // DELETED_BEGIN
   // {{EVENT_T2Chain_Job_Schedule_beforePageNavigation
   void T2Chain_Job_Schedule_beforePageNavigation(PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage)
   {

   }

   // END_EVENT_T2Chain_Job_Schedule_beforePageNavigation}}
   // DELETED_END

   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      addPageListener(this);
      T3View_Pending_Chain_Jobs.addPageNavigationListener(this);
      T1BS_AGENT.addDBListener(this);
      // END_EVENT_ADD_LISTENERS}}

   }

   // {{EVENT_ADAPTER_CODE

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         Admin_User_Chain_Job_Sch_beforeActionPerformed(ae, evt, preq);
      }
   }

   public void beforePageNavigation(PageNavigation obj, VSOutParam cancel,
         VSOutParam newPage)
   {
      Object source = obj;
      if (source == T3View_Pending_Chain_Jobs)
      {
         T3View_Pending_Chain_Jobs_beforePageNavigation(obj, cancel, newPage);
      }
   }

   public void beforeQuery(DataSource obj, VSQuery query, VSOutParam resultset)
   {
      Object source = obj;
      if (source == T1BS_AGENT)
      {
         T1BS_AGENT_beforeQuery(query, resultset);
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

   /*
    * For debugging
    */
   private void db(String fsMsg)
   {
      if (AMS_PLS_DEBUG)
      {
         System.err.println(fsMsg);
      }
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
      return super.generate();
   } // end of method generate

   /**
    * sets title that reflects the job label
    */
   private String getCustomTitle()
   {
      String lsTitle = "Chain Job Summary";
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
   * Deletes all existing entries on R_BS_CATALOG_TEMP for given
   * Catalog ID and current User ID. Then copies R_BS_CATALOG entries for the
   * child jobs for the given Chain job into R_BS_CATALOG_TEMP for current User ID.
   * @param flCtlgID - Catalog ID of the Chain header job.
   * @return - Returns true when R_BS_CATALOG_TEMP entries are deleted and
   *     inserted successfully, else false.
   */
   private boolean prepareCatalogTempEntries( long flCtlgID )
   {
      VSResultSet   loFromRS    = null ;
      VSResultSet   loToRS      = null ;
      VSSession     loSession   = getParentApp().getSession() ;
      String        lsUserID    = AMSPLSUtil.getCurrentUserID(loSession);
      SearchRequest loSrchReq ;
      VSQuery       loFromQry ;
      VSQuery       loToQry ;
      VSRow         loFirstRow ;

      try
      {
         /*
          * Delete any previous entries from the temp table- R_BS_CATALOG_TEMP
          * corresponding to the current chain chain - catalog id and current User ID
          */
         if( !deletePreviousCatalogTempEntries( flCtlgID ) )
         {
            //Case where delete of R_BS_CATALOG_TEMP entries failed
            return false;
         }

         loSrchReq = new SearchRequest() ;
         loSrchReq.add( "PNT_ID=" + flCtlgID ) ;
         loFromQry = new VSQuery( loSession, "R_BS_CATALOG", loSrchReq, null ) ;
         loFromRS = loFromQry.execute() ;

         loFirstRow = loFromRS.first() ;

         if ( loFirstRow == null )
         {
            return true;
         } /* end if ( loFirstRow == null ) */

         /* Copy R_BS_CATALOG entries for the child jobs for the given Chain
            job into R_BS_CATALOG_TEMP for current User ID */
         loSrchReq = new SearchRequest() ;
         loToQry = new VSQuery( loSession, "R_BS_CATALOG_TEMP", loSrchReq, null ) ;
         loToRS = loToQry.execute() ;

         for ( VSRow loParmRow = loFirstRow; loParmRow !=null ; loParmRow = loFromRS.next() )
         {
            VSRow  loCtlgRow   = loFromRS.current() ;
            VSRow  loNewRow    = loToRS.insert() ;
            String lsCtlgID    = loCtlgRow.getData( "CTLG_ID" ).getString() ;
            String lsClsName   = loCtlgRow.getData( "CLS_NM" ).getString() ;
            String lsPkgName   = loCtlgRow.getData( "PKG_NM" ).getString() ;
            String lsRqAp      = loCtlgRow.getData( "RQRS_APRV" ).getString() ;
            String lsItemTyp   = loCtlgRow.getData( "ITM_TYP" ).getString() ;
            String lsAppServer = loCtlgRow.getData( "APPL_SRV_NM" ).getString() ;
            String lsCtlabel   = loCtlgRow.getData( "CTLG_NM" ).getString() ;
            String lsPntID     = loCtlgRow.getData( "PNT_ID" ).getString() ;
            String lsSeqNo     = loCtlgRow.getData( "SEQ_NO" ).getString() ;
            String lsRetCd     = loCtlgRow.getData( "PRE_CN_RET_CD" ).getString() ;


            loNewRow.getData( "CHN_JOB_ID" ).setLong( flCtlgID ) ;
            loNewRow.getData( "USID" ).setString( lsUserID ) ;
            loNewRow.getData( "CTLG_ID" ).setString( lsCtlgID ) ;
            loNewRow.getData( "CLS_NM" ).setString( lsClsName ) ;
            loNewRow.getData( "PKG_NM" ).setString( lsPkgName ) ;
            loNewRow.getData( "RQRS_APRV" ).setString( lsRqAp ) ;
            loNewRow.getData( "ITM_TYP" ).setString( lsItemTyp ) ;
            loNewRow.getData( "APPL_SRV_NM" ).setString( lsAppServer ) ;
            loNewRow.getData( "CTLG_NM" ).setString( lsCtlabel ) ;
            loNewRow.getData( "PNT_ID" ).setString( lsPntID ) ;
            loNewRow.getData( "SEQ_NO" ).setString( lsSeqNo ) ;
            loNewRow.getData( "PRE_CN_RET_CD" ).setString( lsRetCd ) ;
         } //end for

         loToRS.updateDataSource() ;
      }//end try
      catch( Exception foExp )
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Failed to setup Chain Job", SEVERITY_LEVEL_ERROR ) ;
         //Case of failure
         return false;
      } /* end catch( RemoteException foExp ) */
      finally
      {
         if ( loToRS != null )
         {
            loToRS.close() ;
         } /* end if ( loToRS != null ) */

         if ( loFromRS != null )
         {
           loFromRS.close() ;
         } /* end if ( loFromRS != null ) */
      } /* end finally */
      //Case of successful processing
      return true;
   }//end of method


  /**
   * Deletes all existing entries on R_BS_CATALOG_TEMP for given
   * Catalog ID and current User ID.
   * @param flCtlgID - Catalog ID of the Chain header job.
   * @return - Returns true when delete was successful, else false.
   */
   private boolean deletePreviousCatalogTempEntries( long flChainJobCatalogId )
   {
      VSResultSet loRs =null;
      int rowCount =0;
      try
      {
         VSSession loSession = parentApp.getSession();
         String lsUserID = AMSPLSUtil.getCurrentUserID(loSession);
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("USID = " + AMSSQLUtil.getANSIQuotedStr(lsUserID,true) );
         searchReq.add("AND CHN_JOB_ID=" + flChainJobCatalogId + "" );
         VSQuery loQuery = new VSQuery(loSession,"R_BS_CATALOG_TEMP",searchReq,null);
         loRs = loQuery.execute();
         loRs.last();
         rowCount= loRs.getRowCount();

         // delete the rows
         for(int i =1; i <= rowCount; i++ )
         {
            VSRow loRowToDelete = loRs.getRowAt(i);
            loRowToDelete.deleted(true);
            loRowToDelete.save();
         }

      }// end try
      catch(Exception loExcp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException( "Failed to setup Chain Job",SEVERITY_LEVEL_ERROR );
         //Case of failure
         return false;
      }
      finally
      {
         if (loRs !=null)
         {
            loRs.close();
         }
      } // finally
      //Case of successful processing
      return true;
   }//end of method


}
