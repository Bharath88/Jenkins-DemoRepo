//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.client.batch.*;
import versata.vls.*;
import java.util.*;
import java.io.File;
import java.lang.reflect.*;
import com.amsinc.gems.adv.vfc.html.*;
import advantage.AMSBatchUtil;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.AMSSecurityObject;

import org.apache.commons.logging.Log;

/*
 **  Admin_Panel1
 */

//{{FORM_CLASS_DECL
public class Admin_Panel1 extends Admin_Panel1Base

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   private AMSJobManagerClient moJobManagerClient = null;
      private boolean mboolServerStatus ;
   private int miMaxThreadCount ;
   private int miApplID ;
   private int miJobPollRate ;
   private int miJobActivateRate ;
   private int miCurrentThreadCount ;
   private boolean mboolPickTargetedJobsOnly;

   private boolean mboolIsDefaultJobQueueActive;
   private boolean mboolIsNightlyJobQueueActive;
   private boolean mboolIsApplDateJobQueueActive;

   private VSCodeTable moApplCdTbl = null ;

   /** This is the logger object for the class */
   private static Log moLog = AMSLogger.getLog( Admin_Panel1.class,
      AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;




         // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Admin_Panel1 ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }
//{{EVENT_CODE
//{{EVENT_Admin_Panel1_beforeActionPerformed
   void Admin_Panel1_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      VSSession loSession = parentApp.getSession();
      syncRecordCurrency(getRootDataSource(),preq);


      // If update btn clicked
      if (ae.getName().equals("updateBtn"))
      {
         /*
          * we will check if the current user has enough privileges to
          * update the job manager properties
          */
         if ( ! AMSBatchUtil.checkIfJobCtrlRole(getUserID(),getPassword() ) )
         {
            raiseException("Not authorized to edit job manager properties",SEVERITY_LEVEL_ERROR);
            return;
         }

         if (connectToJobManager() == -1) // connection could not be established
         {
            resetValues();
         }
         else  // available job manager stub
         {
            String lsErrorMsg = updateValuesFromInput(preq);
            writeFieldValues();
            if (lsErrorMsg.length() >0) // Error in input
            {
               setStatusMessage(lsErrorMsg);
            }
            else
            {
               setStatusMessage("Values updated");

            }
         } // else if (connectToJobManager() == -1)
      } // ae.getName().equals("updateBtn")


      /*
      * If start polling link clicked
      */
      if (ae.getName().equals("startReg") )
      {
         try
         {
            /*
             * we will check if the current user has enough privileges to
             * update the job manager properties
             */
            if ( ! AMSBatchUtil.checkIfJobCtrlRole(getUserID(),getPassword()) )
            {
               raiseException("Not authorized to start job polling",SEVERITY_LEVEL_ERROR);
               return;
            }

            int liJobMgrId = connectToJobManager();
            if (liJobMgrId == -1) // connection could not be established
            {
               raiseException("Could not 'ping' selected job manager",SEVERITY_LEVEL_ERROR);
               resetValues();
            }
            else  // connection established, moJobManager initialzed
            {
               if (moJobManagerClient ==null)
               {
                  // get handle to instance of AMSJobManagerClient
                  moJobManagerClient = new AMSJobManagerClient(getParentApp().getSession());
               }

               AMSJobManagerMsg loRetObj= moJobManagerClient.startRegistration(liJobMgrId,getUserID(),getPassword());

               // if failed show error and return
               if (loRetObj.getStatus()==AMSJobManagerMsg.OP_FAILED)
               {
                  raiseException("Could not start polling on selected job manager",SEVERITY_LEVEL_ERROR);
                  return;
               }


            } // else if (connectToJobManager() == -1) // connection could not be established

         } //try
         catch(Exception loExp)
         {
            // Add exception log to logger object
            moLog.error("Unexpected error encountered while processing. ", loExp);

         } // catch

      } // of (ae.getName().equals("startReg") )


      /*
      * If stop polling link clicked
      */
      if (ae.getName().equals("stopReg") )
      {
         try
         {
            /*
             * we will check if the current user has enough privileges to
             * update the job manager properties
             */
            if ( ! AMSBatchUtil.checkIfJobCtrlRole(getUserID(),getPassword()) )
            {
               raiseException("Not authorized to stop job polling",SEVERITY_LEVEL_ERROR);
               return;
            }

            int liJobMgrId = connectToJobManager();
            if (liJobMgrId == -1) // connection could not be established
            {
               raiseException("Could not 'ping' selected job manager",SEVERITY_LEVEL_ERROR);
               resetValues();
            }
            else  // connection established, moJobManager initialzed
            {
               if (moJobManagerClient ==null)
               {
                  // get handle to instance of AMSJobManagerClient
                  moJobManagerClient = new AMSJobManagerClient(getParentApp().getSession());
               }

               AMSJobManagerMsg loRetObj= moJobManagerClient.stopRegistration(liJobMgrId,getUserID(),getPassword());

               // if failed show error and return
               if (loRetObj.getStatus()==AMSJobManagerMsg.OP_FAILED)
               {
                  raiseException("Could not stop polling on selected job manager",SEVERITY_LEVEL_ERROR);
                  return;
               }


            } // else if (connectToJobManager() == -1) // connection could not be established

         } //try
         catch(Exception loExp)
         {
            // Add exception log to logger object
            moLog.error("Unexpected error encountered while processing. ", loExp);

         } // catch

      } // of (ae.getName().equals("stopReg") )

   }


//END_EVENT_Admin_Panel1_beforeActionPerformed}}
// DELETED_BEGIN 
//{{EVENT_Admin_Panel_beforeActionPerformed



//END_EVENT_Admin_Panel_beforeActionPerformed}}
 // DELETED_END 

//END_EVENT_CODE}}
   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }
//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			Admin_Panel1_beforeActionPerformed( ae, evt, preq );
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
   /*
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
   * Resets the scalar field values
   * this is required when a connection to a selected VLS on a
   * machine cannot be achieved
   */
   private void resetValues()
   {
      try
      {
         // reset all field values
         TextFieldElement loServerStatus = (TextFieldElement )getDocumentModel().getElementByName("getAgentServerStatusField");
         loServerStatus.setValue("");
                  TextFieldElement loMaxThread = (TextFieldElement )getDocumentModel().getElementByName("setMaxThreadCountField");
         loMaxThread.setValue("");
                  TextFieldElement loJobPollRate = (TextFieldElement )getDocumentModel().getElementByName("setJobPollRateField");
         loJobPollRate.setValue("");
                  TextFieldElement loCurrThreadCount =(TextFieldElement )getDocumentModel().getElementByName("getCurrentThreadCountField");
         loCurrThreadCount.setValue("");
                  TextFieldElement loJobActivateRate = (TextFieldElement )getDocumentModel().getElementByName("setJobActivateRateField");
         loJobActivateRate.setValue("");
                  TextFieldElement loMsgBox = (TextFieldElement )getDocumentModel().getElementByName("messageBox");
         loJobActivateRate.setValue("");
                  AMSCheckboxElement loScalar =(AMSCheckboxElement)getElementByName("pickTargetedJobsField");
         loScalar.setEnabled(false);


                  loScalar =(AMSCheckboxElement)getElementByName("isDefaultJobQueueActive");
         loScalar.setEnabled(false);

                  loScalar =(AMSCheckboxElement)getElementByName("isNightlyJobQueueActive");
         loScalar.setEnabled(false);

                  loScalar =(AMSCheckboxElement)getElementByName("isApplDateJobQueueActive");
         loScalar.setEnabled(false);


      }
      catch(Exception loExp)
      {
         db("could not reset values: Reports_Sys_Admin_App.Admin_Panel1");
            // Add exception log to logger object
            moLog.error("Unexpected error encountered while processing. ", loExp);

      }
   } // of resetValues
   /**
   * Retrieves the Job field values from the Job Manager
   * corresponding to the selected AppServer row
   *
   * @return If field values were successfully fetched
   */
   private boolean getFieldValues()
   {
      AMSJobManagerMsg loRetObj = null;
      int liJobMgrId            =-1;
      try
      {
         //first 'ping' job manager corresponding to selected row
         liJobMgrId = connectToJobManager();
         if (liJobMgrId ==-1)
         {
            // reset values
            mboolServerStatus = false;
            miMaxThreadCount  = 0;
            miApplID          = -1;
            miJobPollRate     = 0;
            miJobActivateRate =0;
            miCurrentThreadCount = 0;
            mboolPickTargetedJobsOnly = false;
            return false;
         }
         if (moJobManagerClient ==null)
         {
            moJobManagerClient = new AMSJobManagerClient(getParentApp().getSession());
         }
         db("Retrieving job manager property value, ID:" + liJobMgrId);

         // update if selected job manager polling or not
         loRetObj = moJobManagerClient.getAgentServerStatus(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Boolean loRetValue = (Boolean)loRetObj.getValue();
            mboolServerStatus = loRetValue.booleanValue();
            db("Job polling on: " + loRetValue.booleanValue());
         }
         else
         {
            mboolServerStatus =false;
            db("Could not retrieve value for property: Polling On ?");
         }
                  // update if selected job manager is picking targeted jobs only
         loRetObj = moJobManagerClient.getPickTargetedJobsOnly(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Boolean loRetValue = (Boolean)loRetObj.getValue();
            mboolPickTargetedJobsOnly = loRetValue.booleanValue();
            db("Pick targeted jobs only: " + loRetValue.booleanValue());
         }
         else
         {
            mboolPickTargetedJobsOnly =false;
            db("Could not retrieve value for property: Pick targeted jobs only ?");
         }

          // update the maximum thread count
         loRetObj = moJobManagerClient.getMaxThreadCount(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Integer loRetValue = (Integer)loRetObj.getValue();
            miMaxThreadCount = loRetValue.intValue();
            db("Max thread count: " + miMaxThreadCount);
         }
         else
         {
            miMaxThreadCount =0;
            db("Could not retrieve value for property: Max thread count");
         }

         loRetObj = moJobManagerClient.getApplicationID(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Integer loRetValue = (Integer)loRetObj.getValue();
            miApplID = loRetValue.intValue();
            db("Application ID: " + miApplID);
         }
         else
         {
            miApplID = -1;
            db("Could not retrieve value for property: Application ID");
         }

         // update the Job Poll Rate
         loRetObj = moJobManagerClient.getJobPollRate(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Integer loRetValue = (Integer)loRetObj.getValue();
            miJobPollRate = loRetValue.intValue();
            db("Job Poll Rate (seconds): " + miJobPollRate);
         }
         else
         {
            miJobPollRate =0;
            db("Could not retrieve value for property: Job Poll Rate (seconds)");
         }

         // update the Job Activate Rate
         loRetObj = moJobManagerClient.getJobActivateRate(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Integer loRetValue = (Integer)loRetObj.getValue();
            miJobActivateRate = loRetValue.intValue();
            db("Job Activate Rate (seconds): " + miJobActivateRate);
         }
         else
         {
            miJobActivateRate =0;
            db("Could not retrieve value for property: Job Activate Rate (seconds)");
         }

         // update the current thread count
         loRetObj = moJobManagerClient.getThreadCount(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Integer loRetValue = (Integer)loRetObj.getValue();
            miCurrentThreadCount = loRetValue.intValue();
         }
         else
         {
            miCurrentThreadCount =0;
            db("Could not retrieve value for property: Number of active jobs");
         }


         // update if selected job manager is set with default job queue active
         loRetObj = moJobManagerClient.isDefaultJobQueueActive(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Boolean loRetValue = (Boolean)loRetObj.getValue();
            mboolIsDefaultJobQueueActive = loRetValue.booleanValue();
            db("Is Default Job Queue Active:" + loRetValue.booleanValue());
         }
         else
         {
            mboolIsDefaultJobQueueActive =false;
            db("Could not retrieve value for property: Is Default Job Queue Active ?");
         }



         // update if selected job manager is set with nightly job queue active
         loRetObj = moJobManagerClient.isNightlyJobQueueActive(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Boolean loRetValue = (Boolean)loRetObj.getValue();
            mboolIsNightlyJobQueueActive = loRetValue.booleanValue();
            db("Is Nightly Job Queue Active: " + loRetValue.booleanValue());
         }
         else
         {
            mboolIsNightlyJobQueueActive =false;
            db("Could not retrieve value for property: Is Nightly Job Queue Active ?");
         }


         // update if selected job manager is set with application date job queue active
         loRetObj = moJobManagerClient.isApplDateJobQueueActive(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_SUCCESSFUL)
         {
            // value successfully retrieved
            Boolean loRetValue = (Boolean)loRetObj.getValue();
            mboolIsApplDateJobQueueActive = loRetValue.booleanValue();
            db("Is Application Date Job Queue Active: " + loRetValue.booleanValue());
         }
         else
         {
            mboolIsApplDateJobQueueActive =false;
            db("Could not retrieve value for property: Is Application Date Job Queue Active ?");
         }



      } // try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         return false;      // could not retrieve values
      }
      return true;  // field value retrieved
   } // getFieldValues



   /**
    * User input values are retrieved from the
    * request object and corresponding member field updated
    *
    * @return the error String
    */
   private String updateValuesFromInput(PLSRequest preq)
   {
      int liJobMgrId = -1;
      StringBuffer loErrorMsg =new StringBuffer(256);
      AMSJobManagerMsg loRetObj = null;
      if (moJobManagerClient ==null)
      {
         moJobManagerClient = new AMSJobManagerClient(getParentApp().getSession());
      }
      /*
      * Retrieve the Job Manager Id and then use the Job Manager
      * Client instance to make the updates
      */
      try
      {
         VSRow loCurrRow = getRootDataSource().getCurrentRow();
         if (loCurrRow ==null)
         {
            throw new Exception("No AppServer selected");
         }
         liJobMgrId  = loCurrRow.getData("SEQ_NO").getInt();
      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         return loExp.getMessage();
      }
                  // update max thread count value for the identified job manager
      try
      {
         String lsMaxThreadRawValue = preq.getParameter("setMaxThreadCountField");
         db("Input Max Thread Value " + lsMaxThreadRawValue);
         miMaxThreadCount= Integer.parseInt(lsMaxThreadRawValue.trim());
         loRetObj = moJobManagerClient.setMaxThreadCount(liJobMgrId,miMaxThreadCount,
                                                         getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            loErrorMsg.append("Could not update field: Max thread count " + loRetObj.getMessage());
         }
         else
         {
            loErrorMsg.append("Max thread count value updated successfully: ");
         }
      }// try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         loErrorMsg.append("Could not update field: Max thread count " + loExp.getMessage());
      }// catch
                  // update Job Poll rate
      try
      {
         String lsJobPollRateRawValue = preq.getParameter("setJobPollRateField");
         db("Input Job Poll Rate Value " + lsJobPollRateRawValue );
         miJobPollRate = Integer.parseInt(lsJobPollRateRawValue.trim());
         loRetObj = moJobManagerClient.setJobPollRate(liJobMgrId,miJobPollRate,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            loErrorMsg.append("Could not update field: Job Poll Rate " + loRetObj.getMessage());
         }
         else
         {
            loErrorMsg.append("Job Poll Rate value updated successfully: ");
         }
      }// try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         loErrorMsg.append("Could not update field: Job Poll Rate " + loExp.getMessage());
      }// catch
                  // update Job Activate Rate Field
      try
      {
         String lsJobActivateRateRawValue = preq.getParameter("setJobActivateRateField");
         db("Input Job Activate Rate Value " + lsJobActivateRateRawValue );
         miJobActivateRate = Integer.parseInt(lsJobActivateRateRawValue.trim());
         loRetObj = moJobManagerClient.setJobActivateRate(liJobMgrId,miJobActivateRate,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            loErrorMsg.append("Could not update field: Job Activate Rate " + loRetObj.getMessage());
         }
         else
         {
            loErrorMsg.append("Job Activate Rate value updated successfully: ");
         }
      }// try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         loErrorMsg.append("Could not update field: Job Activate Rate " + loExp.getMessage());
      }// catch
                   // update Pick Targeted jobs only
      try
      {
         String lsPickTargetedJobsOnly = preq.getParameter("pickTargetedJobsField");
                  // if non null value- checkbox on
         if (lsPickTargetedJobsOnly !=null)
         {
           mboolPickTargetedJobsOnly =true;
         }
         else
         {
           mboolPickTargetedJobsOnly =false;
         }

         loRetObj = moJobManagerClient.setPickTargetedJobsOnly(liJobMgrId, mboolPickTargetedJobsOnly,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            loErrorMsg.append("Could not update field: Process assigned jobs only " + loRetObj.getMessage());
         }
         else
         {
            loErrorMsg.append("Process assigned jobs only field update successfully: ");
         }
      }// try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         loErrorMsg.append("Could not update field: Process assigned jobs only " + loExp.getMessage());
      }// catch


      /*
       * update the active job queue values
       */
      try
      {
         String isApplDateQActive = preq.getParameter("isApplDateJobQueueActive");
         String isDefaultQActive = preq.getParameter("isDefaultJobQueueActive");
         String isNightlyQActive = preq.getParameter("isNightlyJobQueueActive");



          // if non null value- checkbox on
         if (isApplDateQActive !=null)
         {
            mboolIsApplDateJobQueueActive =true;
         }
         else
         {
            mboolIsApplDateJobQueueActive =false;
         }



         if (isDefaultQActive !=null)
         {
            mboolIsDefaultJobQueueActive =true;
         }
         else
         {
            mboolIsDefaultJobQueueActive =false;
         }


         if (isNightlyQActive !=null)
         {
            mboolIsNightlyJobQueueActive =true;
         }
         else
         {
            mboolIsNightlyJobQueueActive =false;
         }

         /*
          * build the active job queue list as comma separated string
          */
         String lsActiveQList = "";

         if (mboolIsDefaultJobQueueActive)
         {
            if ( lsActiveQList.trim().length()==0 )
            {
               lsActiveQList = lsActiveQList + AMSCommonConstants.DEFAULT_JOB_QUEUE_ID;
            }
            else
            {
               lsActiveQList = lsActiveQList + "," +
                  AMSCommonConstants.DEFAULT_JOB_QUEUE_ID;
            }
         }

         if (mboolIsApplDateJobQueueActive)
         {
            if ( lsActiveQList.trim().length()==0 )
            {
               lsActiveQList = lsActiveQList + AMSCommonConstants.APPL_DT_JOB_QUEUE_ID;
            }
            else
            {
               lsActiveQList = lsActiveQList + "," +
                  AMSCommonConstants.APPL_DT_JOB_QUEUE_ID;
            }
         }


         if (mboolIsNightlyJobQueueActive)
         {
            if ( lsActiveQList.trim().length()==0 )
            {
               lsActiveQList = lsActiveQList + AMSCommonConstants.NIGHTLY_JOB_QUEUE_ID;
            }
            else
            {
               lsActiveQList = lsActiveQList + "," +
                  AMSCommonConstants.NIGHTLY_JOB_QUEUE_ID;
            }
         }

         loRetObj = moJobManagerClient.setActiveJobQueueLst(liJobMgrId,
                                         lsActiveQList,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            loErrorMsg.append("Could not update field: Active Job Queue List " +
                            loRetObj.getMessage());
         }
         else
         {
            loErrorMsg.append("Updated Active Job Queue List successfully: ");
         }
      }// try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         loErrorMsg.append("Could not update Active Job Queue List: " +
                         loExp.getMessage());
      }// catch


      db(loErrorMsg.toString());
      return loErrorMsg.toString();
   } // end method updateFieldValues()




   /**
   * Updates the UI from the member variables for the
   * connected Job Manager
   *
   * @return if document model fields were updated
   */
   private boolean writeFieldValues()
   {
      try
      {
         TextFieldElement loServerStatus = (TextFieldElement )getDocumentModel().getElementByName("getAgentServerStatusField");
         if (mboolServerStatus)
         {
            loServerStatus.setValue("Yes");
         }
         else
         {
            loServerStatus.setValue("No");
         }
         // write values for fields
         TextFieldElement loMaxThread = (TextFieldElement )getDocumentModel().getElementByName("setMaxThreadCountField");
         loMaxThread.setValue(String.valueOf(miMaxThreadCount));
                  TextFieldElement loJobPollRate = (TextFieldElement )getDocumentModel().getElementByName("setJobPollRateField");
         loJobPollRate.setValue(String.valueOf(miJobPollRate));
                  TextFieldElement loCurrThreadCount =(TextFieldElement )getDocumentModel().getElementByName("getCurrentThreadCountField");
         loCurrThreadCount.setValue(String.valueOf(miCurrentThreadCount));
                  TextFieldElement loJobActivateRate = (TextFieldElement )getDocumentModel().getElementByName("setJobActivateRateField");
         loJobActivateRate.setValue(String.valueOf(miJobActivateRate));

         AMSCheckboxElement loScalar =(AMSCheckboxElement)getElementByName("pickTargetedJobsField");
         loScalar.setEnabled(true);
         loScalar.setChecked(mboolPickTargetedJobsOnly);


         loScalar =(AMSCheckboxElement)getElementByName("isDefaultJobQueueActive");
         loScalar.setEnabled(true);
         loScalar.setChecked(mboolIsDefaultJobQueueActive);

         loScalar =(AMSCheckboxElement)getElementByName("isApplDateJobQueueActive");
         loScalar.setEnabled(true);
         loScalar.setChecked(mboolIsApplDateJobQueueActive);

         loScalar =(AMSCheckboxElement)getElementByName("isNightlyJobQueueActive");
         loScalar.setEnabled(true);
         loScalar.setChecked(mboolIsNightlyJobQueueActive);

         AMSComboBoxElement loApplID = (AMSComboBoxElement)getElementByName( "ApplicationID" ) ;
         if ( miApplID != -1 )
         {
            VSCodeElement loCodeElem ;

            if ( moApplCdTbl == null )
            {
               moApplCdTbl = VSMetaManager.getCodeTable( getParentApp().getSession(), "R_SC_APPL" ) ;
            } /* end if ( moApplCdTbl == null ) */
            loCodeElem = moApplCdTbl.getByCode( String.valueOf( miApplID ) ) ;
            if ( loCodeElem != null )
            {
               loApplID.setValue( loCodeElem.value.toString() ) ;
            } /* end if ( loCodeElem != null ) */
            else
            {
               loApplID.setValue( "Unknown" ) ;
            } /* end else */
         } /* end if ( miApplID != -1 ) */
         else
         {
            loApplID.setValue( "" ) ;
         } /* end else */

      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

          return false;
      }
      return true;
   } // of writeFieldValues

  /**
   * 'Pings' the Job Manager corresponding to the selected row.  If successful,
   * the job manager id is returned.  If connection cannot be made the value
   * -1 is returned indicating failed connection
   *
   * @return The current Job Manager ID if 'pinged' successfully, -1 otherwise
   */
   private int connectToJobManager()
   {
      AMSJobManagerMsg loRetObj = null;
      int liJobMgrId = -1;
      try
      {
         VSRow loCurrRow = getRootDataSource().getCurrentRow();
         if (loCurrRow ==null)
         {
            return -1;
         }
         liJobMgrId = loCurrRow.getData("SEQ_NO").getInt();
         //initialize JobManagerClient if null
         if (moJobManagerClient==null)
         {
            moJobManagerClient = new AMSJobManagerClient(getParentApp().getSession());
         }
         loRetObj = moJobManagerClient.connectToJobManager(liJobMgrId,getUserID(),getPassword());
         if (loRetObj.getStatus() == AMSJobManagerMsg.OP_FAILED)
         {
            throw new Exception(loRetObj.getMessage());
         }
         return liJobMgrId;
      } //of try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

         setConnectionMessage("Could not connect to selected Server : " +  loExp.getMessage());

         return -1; // could not make a connection
      }
   } // end method connectToJobManager
   /**
   * Sets the value of TextFieldElement corresponding to the connection status field
   * as the passed string parameter
   */
   private void setConnectionMessage(String fsMsg)
   {
      try
      {
         //TextFieldElement loConnectionStatus = (TextFieldElement )getDocumentModel().getElementByName("connectionStatus");
         //loConnectionStatus.setValue(fsMsg );
         raiseException(fsMsg,SEVERITY_LEVEL_INFO);
      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

      }
   } // of setConnectionMessage
   /**
   * Sets the value of TextFieldElement corresponding to the message field
   * as the passed string parameter
   */
   private void setStatusMessage(String fsMsg)
   {
      try
      {
         //TextFieldElement loConnectionStatus = (TextFieldElement )getDocumentModel().getElementByName("messageBox");
         //loConnectionStatus.setValue(fsMsg );
         raiseException(fsMsg,SEVERITY_LEVEL_INFO);
      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExp);

      }
   } // of setStatusMessage

   /**
   * for debugging
   */
   private void printAllBoundElements(Enumeration e)
   {
      while(e.hasMoreElements())
      {
         BoundElement loElement = (BoundElement)e.nextElement();
         db("");
         db("***");
         db("Found " + loElement.getName());
         db("");
         db("***");
      }
   }// end printAllBoundElements

   /**
   * Override method in superclass
   *
   */
   public String generate()
   {
      if (connectToJobManager() == -1) // connection could not be established
      {
         resetValues();
      }
      else  // connection established, moJobManager initialzed
      {
         if (!getFieldValues())  // the JobManager stub could not fetch values
         {
            setConnectionMessage("Not all values could be retrieved");
            resetValues();
         }
         else  // values fetched, fields are now updated
         {
            writeFieldValues();
         }
      }
      return super.generate();
   } // of generate

   /**
    * Return the password associated with the current session
    */
   private String getPassword()
   {
      return null;
   }

   /**
    * Return the CSF token associated with the current session.
    * Explicit passwords are not used for remote invocations.
    */
   public String getUserID()
   {
      try
      {

         String lsCSFToken = ((AMSSecurityObject)(getParentApp().getSession().getSecurityObject())).createCSFToken();
         moLog.debug("User token: " + lsCSFToken);
         return lsCSFToken;

      }
      catch(Exception loExc)
      {
         moLog.error("Exception ocurred while getting user token",loExc);
         return null;
      }
   }

} // of Admin_Panel1


