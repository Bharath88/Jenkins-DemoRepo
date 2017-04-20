//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	import com.amsinc.gems.adv.vfc.html.*;
	import java.util.*;
	import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
	import java.rmi.*;
	import java.rmi.RemoteException;
	import com.amsinc.gems.adv.common.* ;
	import advantage.AMSStringUtil;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;
	/*
	**  User_AgentParm_Grid
	*/
	
	//{{FORM_CLASS_DECL
	public class User_AgentParm_Grid extends User_AgentParm_GridBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSBatchConstants
	{
	   // Declarations for instance variables used in the form
	   // will be initialzed if the job being added has
	   // custom parameter page assigned
	   private String msCustomParmAppName = null;
	   private String msCustomParmPgName = null;
	   private long mlCurrentJobID;
	   private String msChainJobLabel = null;
	
	   public static String msJobIDProperty ="RSAA_JOBID";
	
      /** This is the logger object */
      private static Log moAMSLog = AMSLogger.getLog( User_AgentParm_Grid.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public User_AgentParm_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }
	
	
	
	//{{EVENT_CODE
	//{{EVENT_User_AgentParm_Grid_beforeActionPerformed
void User_AgentParm_Grid_beforeActionPerformed( ActionElement foActnElem, PageEvent foPageEvent, PLSRequest foPLSReq )
{
   // custom job parameter screen invoked
   if ( foActnElem.getName().equals( "Custom_Parameter" ) )
   {
      AMSDynamicTransition loDynTran ;
      VSPage               loCustParmPage ;

      // Save the current page before returning
      // the next page (parameter page)
      savePage();

      // Check to see if there are any errors while saving
      if ( getHighestSeverityLevel() >= SEVERITY_LEVEL_ERROR )
      {
         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;

         return ;
      }

      //sets the value of job id for the current page which
      //may be used from the custom job parameter page
      setJobID();

      try
      {
         //set a session property that has the value of the job id
         getParentApp().getSession().getORBSession().setProperty( RSAA_JOBID,
               String.valueOf( getJobID() ) );
         
         //If Custom Page name is pRefDAPAM then set session property of CTLG_LABEL.
         if(AMSStringUtil.strEqual( msCustomParmPgName, "pRefDAPARM" ))
         {
            Chain_Job_list loSource = (Chain_Job_list)getSourcePage();
            String lsLabel = loSource.getChainJobLabel();
            getParentApp().getSession().getORBSession().setProperty( "CTLG_LABEL" ,
                  lsLabel  );
         }
      }
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to set message properties",
               SEVERITY_LEVEL_SEVERE ) ;

         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;

         return ;
      }

      loDynTran = new AMSDynamicTransition( msCustomParmPgName, "", msCustomParmAppName );
      loDynTran.setSourcePage( this) ;

      loCustParmPage = loDynTran.getVSPage( getParentApp(), getSessionId() ) ;

      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( loCustParmPage ) ;
   }
}
//END_EVENT_User_AgentParm_Grid_beforeActionPerformed}}
// DELETED_BEGIN 
//{{EVENT_T2User_AgentParm_Grid_beforePageNavigation
void T2User_AgentParm_Grid_beforePageNavigation( PageNavigation foPageNav, VSOutParam foCancel, VSOutParam foNewPage )
{
   foPageNav.setTargetPageName( msCustomParmAppName+"."+msCustomParmPgName);
   foPageNav.setMetaQueryName(" ");
   foPageNav.setRelnFromParent(false);
   foPageNav.setTargetPageCaption(" ");
}
//END_EVENT_T2User_AgentParm_Grid_beforePageNavigation}}
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
			User_AgentParm_Grid_beforeActionPerformed( ae, evt, preq );
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
	    * Returns if there exists a custom parameter
	    * page designated for the entry in the catalog table,
	    * R_BS_CATALOG, against which the present job is being added
	    *
	    */
	   public boolean hasCustomParmPage(String fsCtlgID)
	   {
	      if (fsCtlgID ==null)
	      {
	         return false;
	      }
	
	      VSResultSet loRS =null;
	      try
	      {
	         AMSSecurityObject loSecObj ;
	         SearchRequest     searchReq = new SearchRequest();
	         searchReq.add("CTLG_ID =" +fsCtlgID );
	         VSQuery loQuery = new VSQuery(getParentApp().getSession(),"R_BS_CATALOG",searchReq,null);
	         loRS =loQuery.execute();
	
	         VSRow loRow = loRS.first();
	         if (loRow == null) // could not locate corresponding tuple in R_BS_CATALOG
	         {
	            return false;
	         }
	
	         loSecObj = (AMSSecurityObject)getParentApp().getSession().getORBSession().getServerSecurityObject() ;
	         if ( !loSecObj.isUserLoggedIntoApp( loRow.getData( "APPL_ID" ).getLong() ) )
	         {
	            return false ;
	         } /* end if ( !loSecObj.isUserLoggedIntoApp( loRow.getData( "APPL_ID" ).getLong() ) ) */
	
	         // see if corresponding rows are non empty
	         if ((loRow.getData("CSTM_PARM_APPL_NM").getString() != null) &&
	              (loRow.getData("CSTM_PARM_PG_NM").getString() != null) )
	         {
	            msCustomParmAppName = loRow.getData("CSTM_PARM_APPL_NM").getString().trim();
	            msCustomParmPgName  = loRow.getData("CSTM_PARM_PG_NM").getString().trim();
	
	            if (msCustomParmAppName.length() > 0 &&
	                 msCustomParmPgName.length()  >0 )
	            {
	                return true;
	            }
	            else
	            {
	                 return false;
	            }
	         } // end if
	
	      } //try
	      catch(Throwable loExcp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

	         return false;
	      }
	      finally
	      {
	         if(loRS!=null)
	         {
	             loRS.close();
	         }
	      } // finally
	          return false;
	   }  /* end of method hasCustomParmPage(String fsCtlgID) */
	
	
	   /**
	    * Sets the div containing the custom parameter page
	    * link visible if the catalog entry against which the current
	    * job is being run has a custom parameter page defined
	    */
	   public String generate()
	   {
	      DivElement loBlockToSetVisible;
	      loBlockToSetVisible = (DivElement)getElementByName("Custom_ParmPage");
	
	
	      /*
	       * the catalog id for which we determine if custom job parm
	       * exists
	       */
	      String lsCtlgID =null;
	
	      try
	      {
	         if (getSourcePage() instanceof Chain_Job_list)
	         {
	            Chain_Job_list loSource = (Chain_Job_list)getSourcePage();
	            lsCtlgID = loSource.getParentID();
	
	            /*
	             * When the source page is Chain_Job_List, the current pages
	             * title sjould reflect the job label of the chain job element
	             * selected.  The variable, msChainJobLabel has its value set
	             * in this 'if' block and set to null in the else balock (ie the
	             * source page is other than Chain_Job_List).
	             * This fact is used in the method, setTitle to correctly set the
	             * current page title.
	             */
	            msChainJobLabel = loSource.getChainJobLabel();
	         }
	         else  // the page can be related one to one with the catalog id- RSAA_NODE_ID
	         {
	            /*
	             * Make sure the chain job label is set to null as the source page
	             * is not Chain_Job_list
	             */
	            msChainJobLabel = null;
	
	            VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
	            lsCtlgID= loCurrentSession.getProperty("RSAA_NODE_ID");
	         }
	      } // end try
	      catch(Throwable loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	      }
	
	      if (hasCustomParmPage(lsCtlgID)==true)  // the job has custom parm page
	      {
	         loBlockToSetVisible.setVisible(true);
	      }
	      else
	      {
	         loBlockToSetVisible.setVisible(false);
	      }
	
	      /*
	       *  set page title based on job label
	       */
	      setTitle(getCustomTitle());
	
	      return super.generate();
	   } /* end method generate() */
	
	
	   /**
	    * Returns the current job ID
	    */
	   public long getJobID()
	   {
	      return mlCurrentJobID;
	   }
	
	
	   /**
	    * Sets the value of the currentJob ID
	    * which is obtained from the page from which
	    * current page transitioned from
	    */
	   public void setJobID()
	   {
	      VSPage loAddPage = getSourcePage();
	
	      if (loAddPage instanceof Add_New_Job)
	      {
	         Add_New_Job loJobPage = (Add_New_Job)loAddPage;
	         mlCurrentJobID = loJobPage.getJobID();
	      }
	      else if (loAddPage instanceof Job_Edit_User)
	      {
	         Job_Edit_User loJobPage = (Job_Edit_User)loAddPage;
	         mlCurrentJobID = loJobPage.getJobID();
	      }
	      else if (loAddPage instanceof Job_Edit_Admin)
	      {
	         Job_Edit_Admin loJobPage = (Job_Edit_Admin)loAddPage;
	         mlCurrentJobID = loJobPage.getJobID();
	      }
	      else if (loAddPage instanceof Chain_Job_list)
	      {
	          Chain_Job_list loJobPage = (Chain_Job_list)loAddPage;
	          mlCurrentJobID = loJobPage.getJobID();
	      }
	   }  /* end of method setJobID() */
	
	   /**
	    * sets title that reflects the job label
	    */
	   private String getCustomTitle()
	   {
	      String lsTitle = "Job Parameters";
	      String lsLabel = null;
	      try
	      {
	         if (msChainJobLabel ==null)
	         {
	            // source page is not Chain_Job_List
	            // access session property that contains the id of the node clicked
	            lsLabel = parentApp.getSession().getORBSession().getProperty(RSAA_CTLG_LBL);
	         }
	         else
	         {
	            lsLabel = msChainJobLabel;
	         }
	
	
	         // append job label to std title prefix
	         if (lsLabel !=null && lsLabel.length() > 0 )
	         {
	            lsTitle = lsTitle +
	               " for " +
	               lsLabel;
	         }
	      } //try
	      catch(Exception loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	      }
	            return lsTitle;
	   } // end of method getCustomTitle
	}
	
	