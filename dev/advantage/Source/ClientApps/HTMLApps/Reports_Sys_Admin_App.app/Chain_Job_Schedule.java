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
import java.text.*;
import java.util.*;
import java.rmi.RemoteException;

import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSUser;

import advantage.AMSBatchParmUtil;
import org.apache.commons.logging.Log;

/*
 * * Chain_Job_Schedule
 */

// {{FORM_CLASS_DECL
public class Chain_Job_Schedule extends Chain_Job_ScheduleBase

// END_FORM_CLASS_DECL}}
      implements AMSBatchConstants
{
   // Declarations for instance variables used in the form
   long mlCurrentJobID;
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Chain_Job_Schedule.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   /*
    * To enable only one insert operation for parameters
    */
   boolean mboolInsertDone = false;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
   // {{FORM_CLASS_CTOR
public Chain_Job_Schedule ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE
   // {{EVENT_T1BS_AGENT_afterSave
   void T1BS_AGENT_afterSave(VSRow row)
   {
      /*
       * The first 'after save' event is invoked on a successful insert. Record
       * this fact
       */
      if (!mboolInsertDone)
      {
         mboolInsertDone = true;
      }

      try
      {
         /*
          * Enables the link to parameter page
          */
         HyperlinkActionElement loParametersLink = (HyperlinkActionElement) getDocumentModel()
               .getElementByName("Setup_Parameters");
         loParametersLink.setEnabled(true);

         // get the current job Id for this chain job header
         mlCurrentJobID = row.getData("AGNT_ID").getLong();

      } /* end try */
      catch (Exception foExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException("Failed to setup chain job", SEVERITY_LEVEL_SEVERE);
         return;
      } /* end catch( RemoteException foExp ) */

   }

   // END_EVENT_T1BS_AGENT_afterSave}}
   // {{EVENT_T3Chain_Job_list_beforePageNavigation
   void T3Chain_Job_list_beforePageNavigation(PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage)
   {
      // Write Event Code below this line

      String lsWhereClause = "ID ='" + mlCurrentJobID + "'";
      nav.setDevWhere(lsWhereClause);

   }

   // END_EVENT_T3Chain_Job_list_beforePageNavigation}}
   // {{EVENT_Chain_Job_Schedule_beforeActionPerformed
   void Chain_Job_Schedule_beforeActionPerformed(ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      // delete all child job entries fron BS_AGENT if any have been inserted
      String lsAction = ae.getAction();
      String lsName = ae.getName();

      if (lsAction.equals("cancel")
            && lsAction.equals("T1BS_AGENTdelete"))
      {
         String lsConfirmDelete = preq.getParameter("ConfirmDelete"); // done
                                                                        // in
                                                                        // batch.js
         if (lsConfirmDelete.equals("No"))
         {
            evt.setNewPage(this);
            evt.setCancel(true);
         }
         else
         // delete action
         {
            removeChildJob(mlCurrentJobID);
         }
      } /* end if(lsAction.equals("cancel") */

      // the job is being submitted
      if (lsName.equalsIgnoreCase("btnT5Admin_User_Chain_Job_Sch"))
      {

         try
         {
            VSSession loCurrSession = getParentApp().getSession();
            VSORBSession loCurrentSession = loCurrSession.getORBSession();
            long llNodeID = Long.parseLong(loCurrentSession
                  .getProperty(AMSBatchConstants.RSAA_NODE_ID));

            if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession,
                  AMSPLSUtil.getCurrentUserID(loCurrSession), llNodeID,
                  AMSCommonConstants.JOB_ACTN_SUBMIT, true, this))
            {
               /*
                * Case where the User is not authorized to Submit New Job.
                * Prevent the navigation to the page that allows to submit new
                * job.
                */
               evt.setNewPage(this);
               evt.setCancel(true);
               return;
            }

            /*
             * set property to enable the job steps to clone this chain job
             * header's scheduling information
             */

            loCurrentSession.setProperty(AMSBatchParmUtil.UPDATE_JOB_STEPS,
                  AMSBatchParmUtil.UPDATE_JOB_STEPS);

            VSRow loRow = getRootDataSource().getCurrentRow();
            if (loRow != null)
            {
               loRow.getData("RUN_STA").setInt(
                     AMSBatchConstants.STATUS_SUBMITTED);
               loRow.save();
            }

            StringBuffer lsbWhere = new StringBuffer(100);
            T5Admin_User_Chain_Job_Sch.setOrderBy("AGNT_ID DESC");

            lsbWhere.append("(");
            lsbWhere.append("PNT_CTLG_ID=");
            lsbWhere.append(llNodeID);
            lsbWhere.append(") AND (");
            lsbWhere.append("ITM_TYP=");
            lsbWhere.append(AMSBatchConstants.CHAIN_JOB);
            lsbWhere.append(") AND (RUN_STA=");
            lsbWhere.append(AMSBatchConstants.STATUS_COMPLETE);
            lsbWhere.append(")");

            T5Admin_User_Chain_Job_Sch.setDevWhere(lsbWhere.toString());

         }// try
         catch (Exception foExp)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

             raiseException("Unable to set message properties",
                  SEVERITY_LEVEL_SEVERE);
             return;

         } // catch
      }

   }

   // END_EVENT_Chain_Job_Schedule_beforeActionPerformed}}
   // {{EVENT_Chain_Job_Schedule_pageCreated
   void Chain_Job_Schedule_pageCreated()
   {
      /*
       * Initially disable the link to parameters page
       */
      HyperlinkActionElement loParametersLink = (HyperlinkActionElement) getDocumentModel()
            .getElementByName("Setup_Parameters");
      loParametersLink.setEnabled(false);

   }

   // END_EVENT_Chain_Job_Schedule_pageCreated}}
   // {{EVENT_T1BS_AGENT_beforeSave
   void T1BS_AGENT_beforeSave(VSRow row, VSOutParam cancel)
   {

      /*
       * being used as a surrogate for beforeinsert. Done only once i.e. a
       * successful insert has not been recorded which is marked by the first
       * time the aftersave event adpater is called
       */
      if (!mboolInsertDone)
      {
         processAfterInsert(row);
      }

      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession()
               .getORBSession();

         /*
          * This property lets the job row data object to update the child job
          * step's column values to be 'cloned' to the current chain job headers
          * values
          */
         loCurrentSession.setProperty(AMSBatchParmUtil.UPDATE_JOB_STEPS,
               AMSBatchParmUtil.UPDATE_JOB_STEPS);

      } /* end try */
      catch (Exception foExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException("Failed to setup chain job", SEVERITY_LEVEL_SEVERE);
         return;
      } /* end catch( RemoteException foExp ) */

   }

   // END_EVENT_T1BS_AGENT_beforeSave}}

   // END_EVENT_CODE}}
   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      T1BS_AGENT.addDBListener(this);
      T3Chain_Job_list.addPageNavigationListener(this);
      addPageListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE

   public void afterSave(DataSource obj, VSRow row)
   {
      Object source = obj;
      if (source == T1BS_AGENT)
      {
         T1BS_AGENT_afterSave(row);
      }
   }

   public void beforeSave(DataSource obj, VSRow row, VSOutParam cancel)
   {
      Object source = obj;
      if (source == T1BS_AGENT)
      {
         T1BS_AGENT_beforeSave(row, cancel);
      }
   }

   public void pageCreated(VSPage obj)
   {
      Object source = obj;
      if (source == this)
      {
         Chain_Job_Schedule_pageCreated();
      }
   }

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         Chain_Job_Schedule_beforeActionPerformed(ae, evt, preq);
      }
   }

   public void beforePageNavigation(PageNavigation obj, VSOutParam cancel,
         VSOutParam newPage)
   {
      Object source = obj;
      if (source == T3Chain_Job_list)
      {
         T3Chain_Job_list_beforePageNavigation(obj, cancel, newPage);
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

   private String getCurrentUserID()
   {
      return getParentApp().getSession().getLogin();
   }

   /**
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
    * Remove all the job items that are tagged as chain job. The parent chain
    * job entry has AGNT_ID field equal to flJobToRemove while all child jobs
    * have their ID field equal to flJobToRemove field
    *
    * @param flJobToRemove
    *           unique job id of the job to remove
    */
   private void removeChildJob(long flJobToRemove)
   {
      VSResultSet loRS = null;
      try
      {
         VSSession loCurrentSession = getParentApp().getSession();
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("ID = '" + flJobToRemove + "' OR AGNT_ID ="
               + flJobToRemove);
         VSQuery loQuery = new VSQuery(loCurrentSession, "BS_AGENT", searchReq,
               null);
         loRS = loQuery.execute();
         VSRow loRow = loRS.last();
         int cnt = loRS.getRowCount();
         for (int i = 1; i <= cnt; i++)
         {
            VSRowBasic loJobRow = (VSRowBasic) loRS.getRowAt(i);
            loJobRow.deleted(true);
            loJobRow.save();
         }
      } // try
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } // catch
      finally
      {
         if (loRS != null)
         {
            loRS.close();
         }
      } // finally
   } /* end removeChildJob(long flJobToRemove) */

   /**
    * Abstracts the actions after the initial insert goes through for the row.
    * This includes setting the property that enables copying of job parameters
    */
   private final void processAfterInsert(VSRow foNewRow)
   {
      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession()
               .getORBSession();
         String lsCtlgId = loCurrentSession.getProperty("RSAA_NODE_ID");

         foNewRow.getData("AGNT_CLS_NM").setString("Chain Job");
         foNewRow.getData("PNT_CTLG_ID").setString(lsCtlgId);
         foNewRow.getData("USID").setString(getCurrentUserID());
         foNewRow.getData("RUN_STA").setInt(
               AMSBatchConstants.STATUS_TO_BE_SUBMITTED);

         /*
          * This property lets the job row data object to insert catalog
          * parameters for each of the chld job steps
          */
         loCurrentSession.setProperty(AMSBatchParmUtil.INSERT_JOB_PARAMETERS,
               AMSBatchParmUtil.INSERT_JOB_PARAMETERS);
      } /* end try */
      catch (RemoteException foExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException("Unable to set message properties",
               SEVERITY_LEVEL_SEVERE);
         return;
      } /* end catch( RemoteException foExp ) */
   }

}
