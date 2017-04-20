//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import versata.vls.Session;
import java.util.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import com.amsinc.gems.adv.common.*;
import advantage.AMSBatchParmUtil;

import org.apache.commons.logging.Log;

/*
 **  Add_New_Job
 */

//{{FORM_CLASS_DECL
public class Add_New_Job extends Add_New_JobBase

//END_FORM_CLASS_DECL}}
implements AMSBatchConstants
{
   // Declarations for instance variables used in the form
   // will be initialzed if the job being added has
   // custom parameter page assigned
   String msCustomParmAppName = null;
   String msCustomParmPgName = null;
   long mlCurrentJobID;

   /*
    * To enable only one insert operation for parameters
    */
   boolean mboolInsertDone = false;
   
   /**
    * The code logging object
    */
   private static Log moAMSLog = AMSLogger.getLog(Add_New_Job.class,
         AMSLogConstants.FUNC_AREA_DFLT);


   // This page is accessed from an add transition so a job row is added to
   // table bs_agent even though the user might decide to return to another page
   // without 'saving' the displayed job.  Entries are removed from bs_agent if
   // the user does not invoke the 'save' option  e.g. invokes back option without
   // saving the record
   boolean mboolSaved = false;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Add_New_Job ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }
//{{EVENT_CODE
//{{EVENT_T1BS_AGENT_afterSave
   void T1BS_AGENT_afterSave(VSRow row )
   {


      /*
       * The first 'after save' event is invoked on a successful
       * insert.  Record this fact
       */
      if (!mboolInsertDone )
      {
         mboolInsertDone = true;
      }



      try
      {
         mlCurrentJobID = row.getData("AGNT_ID").getLong();
         HyperlinkActionElement loParametersLink = (HyperlinkActionElement)getDocumentModel().getElementByName("btnT3User_AgentParm_Grid");
         if(loParametersLink !=null)
         {
            loParametersLink.setEnabled(true);
         }

         if (getLastAction().equals("ok"))
         {
            VSRow loCurrentRow = getDataSource("T1BS_AGENT").getCurrentRow();
            if (loCurrentRow!=null)
            {
               loCurrentRow.getData("RUN_STA").setInt(AMSBatchConstants.STATUS_SUBMITTED);
               loCurrentRow.save();
            }
         }

         mboolSaved =true;
      } /* end try */
      catch(Exception foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to get message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */
   }



//END_EVENT_T1BS_AGENT_afterSave}}
//{{EVENT_Add_New_Job_pageCreated
   void Add_New_Job_pageCreated()
   {
      /*
       * disable the link to parameter page initially
       */
      HyperlinkActionElement loParametersLink = (HyperlinkActionElement)getDocumentModel().getElementByName("btnT3User_AgentParm_Grid");
      if(loParametersLink !=null)
      {
         loParametersLink.setEnabled(false);
      }
   }



//END_EVENT_Add_New_Job_pageCreated}}
//{{EVENT_Add_New_Job_beforeActionPerformed
   void Add_New_Job_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      // controls visibility of custom run field - run interval

      if (ae.getAction().equals("ok") && mboolSaved==true )
      {
         VSRow loCurrentRow = getDataSource("T1BS_AGENT").getCurrentRow();
         if (loCurrentRow!=null)
         {
            loCurrentRow.getData("RUN_STA").setInt(AMSBatchConstants.STATUS_SUBMITTED);
            loCurrentRow.save();
         }
      }

      /* the link pressed is delete
      *  action in html page is cancel. if chosen
      * then delete is done using method 'removeJob'
      * and the action cancel goes to return focus to source page
      */
      if (ae.getAction().equals("cancel"))
      {
         String lsConfirmDelete = preq.getParameter("ConfirmDelete"); // in batch.js

         if (lsConfirmDelete.equals("No"))
         {
            evt.setNewPage(this);
            evt.setCancel(true);
         }
         else
         {
            if ( mlCurrentJobID !=0)
            {
               removeJob(mlCurrentJobID);
            }
         }
      }

      // if current job not saved then delete the added job
      if (ae.getAction().equals("back") && mboolSaved==false)
      {
         VSRowBasic currRow = (VSRowBasic)T1BS_AGENT.getCurrentRow();

         currRow.deleted(true);
         currRow.save();
      }
   }



//END_EVENT_Add_New_Job_beforeActionPerformed}}
//{{EVENT_T3User_AgentParm_Grid_beforePageNavigation
   void T3User_AgentParm_Grid_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
   {
      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
         String lsCatalogId = loCurrentSession.getProperty("RSAA_NODE_ID");
         String lsWhereClause = "AGNT_ID="+ mlCurrentJobID ;
         nav.setDevWhere(lsWhereClause);
      }
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to get message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */
   }



//END_EVENT_T3User_AgentParm_Grid_beforePageNavigation}}
//{{EVENT_T1BS_AGENT_beforeSave
void T1BS_AGENT_beforeSave(VSRow row ,VSOutParam cancel )
{
   /*
    * being used as a surrogate for beforeinsert.  Done only
    * once i.e. a successful insert has not been recorded which
    * is marked by the first time the aftersave event adpater is
    * called
    */
   if (!mboolInsertDone )
   {
      processAfterInsert(row );
   }
}
//END_EVENT_T1BS_AGENT_beforeSave}}

//END_EVENT_CODE}}
   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1BS_AGENT.addDBListener(this);
	addPageListener(this);
	T3User_AgentParm_Grid.addPageNavigationListener(this);
//END_EVENT_ADD_LISTENERS}}
   }
//{{EVENT_ADAPTER_CODE

	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_afterSave(row );
		}
	}
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_beforeSave(row ,cancel );
		}
	}
	public void pageCreated ( VSPage obj ){
		Object source = obj;
		if (source == this ) {
			Add_New_Job_pageCreated();
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			Add_New_Job_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T3User_AgentParm_Grid) {
			T3User_AgentParm_Grid_beforePageNavigation( obj, cancel, newPage );
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
    * Returns current usre Id
    */
   private String getCurrentUserID()
   {
      return getParentApp().getSession().getLogin();
   } // of getCurrentUserID

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
         db("Java Class: " + loElement.getClass().getName());
         db("");
         db("***");
      }
   } // of printAllBoundElements

  /**
   * returns the current job id
   */
   public long getJobID()
   {
      return mlCurrentJobID;
   }

   /**
    *  remove all the job item with the job id
    */
   private void removeJob(long flId)
   {
      VSResultSet loRS =null;
      try
      {
         VSSession loCurrentSession = getParentApp().getSession();
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("AGNT_ID =" +flId );
         VSQuery loQuery = new VSQuery(loCurrentSession,"BS_AGENT",searchReq,null);
         loRS =loQuery.execute();
         VSRow loRow = loRS.last();
         int cnt = loRS.getRowCount();
         for(int i=1; i<= cnt;i++)
         {
            VSRowBasic loJobRow = (VSRowBasic)loRS.getRowAt(i);
            loJobRow.deleted(true);
            loJobRow.save();
         }
      }
      catch(Throwable loExcp)
      {
         db("Error deleting job: Reports_Sys_Admin_App.Add_New_Job.java " + loExcp.toString() );
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      }
      finally
      {
         if(loRS!=null)
         {
            loRS.close();
         }
      }
   }  /* end of removeJob */


   public String generate()
   {
      /* Only Report Item Type will be able to see the public viawable flag
      */
      DivElement loPublicFlField =(DivElement)getElementByName("PUBLIC_FL_FIELD");
      DivElement loPublicFlLabel =(DivElement)getElementByName("PUBLIC_FL_LABEL");
      if (isReportItemType())
      {
         if (loPublicFlField !=null)
         {
            loPublicFlField.setVisible(true);
         }
         if (loPublicFlLabel !=null)
         {
            loPublicFlLabel.setVisible(true);
         }
      }
      else
      {
         if (loPublicFlField !=null)
         {
            loPublicFlField.setVisible(false);
         }
         if (loPublicFlLabel!=null)
         {
            loPublicFlLabel.setVisible(false);
         }
      }
            return super.generate();
   }/* end of generate */


   private boolean isReportItemType()
   {
      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
         if ( loCurrentSession != null )
         {
            String lsItemType = loCurrentSession.getProperty(RSAA_ITM_TYP);
            if ( lsItemType != null && lsItemType.equals(REPORT_CLASS ) )
            {
               return true;
            }
            else
            {
               return false;
            }
         }
         return false;
      }
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         return false ;
      } /* end catch( RemoteException foExp ) */
   }/* end of isReportItemType */

   /**
    * Abstracts the actions after the initial insert goes through
    * for the row.  This includes setting the property that enables
    * copying of job parameters
    */
   private final void processAfterInsert(VSRow foNewRow)
   {
      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
         String lsCtlgID = loCurrentSession.getProperty("RSAA_NODE_ID");
         foNewRow.getData("USID").setString(getCurrentUserID());
         foNewRow.getData("PNT_CTLG_ID").setString(lsCtlgID);
         foNewRow.getData("RUN_STA").setInt(AMSBatchConstants.STATUS_TO_BE_SUBMITTED);

         /*
          * This property is set so that the job row data object
          * can proceed with inserting any job parameters from catalog parameter
          * table to job parameter table (if any exist)
          */
         loCurrentSession.setProperty(AMSBatchParmUtil.INSERT_JOB_PARAMETERS,
                                   AMSBatchParmUtil.INSERT_JOB_PARAMETERS);
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to get message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */
   }//processAfterInsert
 }
