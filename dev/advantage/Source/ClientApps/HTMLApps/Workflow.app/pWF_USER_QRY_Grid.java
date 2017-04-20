//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	import java.util.*;
	import java.rmi.RemoteException;
	import com.amsinc.gems.adv.workflow.*;
	import com.amsinc.gems.adv.workflow.aprv.*;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.*;

import org.apache.commons.logging.Log;
	/*
	**  pR_SC_USER_DIR_INFO_Grid
	*/

	//{{FORM_CLASS_DECL
	public class pWF_USER_QRY_Grid extends pWF_USER_QRY_GridBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSWorkflowUserQueryPage
	{
	   // Declarations for instance variables used in the form
	   /** The integer CASE, whether manual route or reassignment */
	   private int miCase ;

	   
        private static Log moAMSLog = AMSLogger.getLog( 
              pWF_USER_QRY_Grid.class,
               AMSLogConstants.FUNC_AREA_WORKFLOW ) ;

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pWF_USER_QRY_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_pWF_USER_QRY_Grid_beforeActionPerformed
void pWF_USER_QRY_Grid_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
    //  System.err.println("sync record source ");
   syncRecordCurrency( T1WF_USER_QRY, preq ) ;
}
//END_EVENT_pWF_USER_QRY_Grid_beforeActionPerformed}}

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
			pWF_USER_QRY_Grid_beforeActionPerformed( ae, evt, preq );
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
	    * This method will be called when the user returns from reassigning.
	    * After selecting a new user, the user will now populate the current
	    * assignee on the worklist.
	    *
	    * Modification Log : Mark Farrell - 05-26-00
	    *                                 - inital version
	    *                    Indrajit Ghosh - 06-05-02
	                                      - moved from AMSHyperlinkActionElement
	    * @return String The generated HTML page
	    */
	   public String selectUser( PLSRequest foPLSReq )
	   {
	      int               liCase        = getCase() ;
	      String            lsSessionID   = getSessionId() ;
	      PLSApp            loParentApp   = getParentApp() ;
	      DataSource        loRootDS      = getRootDataSource() ;
	      VSORBSession      loORBSession  = loRootDS.getSession().session ;
	      String            lsNewUser     = null ;
	      boolean           lboolUser ;
	      AMSPage           loWorklist  = (AMSPage)getSourcePage() ;
	      /*
	       * Resultset to fetch rows from Worklist table. This is needed only when the
	       * Workflow action is performed from the Worklist Details page.
	       */
	      VSResultSet loRS_WF = null;

	      if ( loRootDS != null )
	      {
	         VSRow  loRow        = loRootDS.getCurrentRow() ;
	         VSData loSourceData ;
	         VSData loTargetData ;
	         VSRow   loResultRow = null ;

	         if ( loRow != null )
	         {
	            loSourceData = loRow.getData( "USER_ID" ) ;
	            lsNewUser    = loSourceData.getString() ;

	            /*
	             * Since select manual route from list of users only, we
	             * may set the assignee flag to false
	             * (if the assignee was a role, the flag would be true).
	             */
	            lboolUser = false ;

	            DataSource   loWorklistDS   = loWorklist.getRootDataSource() ;

	            if ( loWorklist instanceof AMSWorklistPage )
	            {
	               VSResultSet  loWorklistRS   = loWorklistDS.getResultSet() ;
	               Enumeration  leSelRows ;
	               TableElement loTableElement = ( (AMSWorklistPage) loWorklist ).getWorklistTableElement() ;

	               /*
	                * Since multi Select Table, we must grab the first row
	                * if more than 1 row selected, only first row selected will be used
	                */
	               if ( loTableElement != null )
	               {
	                  leSelRows = loTableElement.getSelectedRows() ;
	                  if ( leSelRows != null )
	                  {
	                     if ( leSelRows.hasMoreElements() )
	                     {
	                        Integer liRowIdx    = (Integer)leSelRows.nextElement() ;
	                        loResultRow = loWorklistRS.getRowAt( liRowIdx.intValue(), false ) ;

	                        /*
	                         * Worklist Details is a child of Worklist. Any updates done on Worklist page
	                         * propagate to the child Worklist Details page through the relationship.
	                         * However, the vice versa is not true, i.e. updates from the Worklist Details page
	                         * do not propagate to its parent Worklist.
	                         * Hence, if current page is Worklist Details page, then no updates should be
	                         * done on it.
	                         * Instead, get a handle to the corresponding row of the parent Worklist table
	                         * and update it. This update will then propagate to the child Worklist Details.
	                         */
	                        if ( loWorklist instanceof pWF_APRV_WRK_LST_DET )
	                        {
	                           SearchRequest lsrSheet = new SearchRequest();
	                           lsrSheet.add("WRK_LST_ID=" + loResultRow.getData("WRK_LST_ID").getString());
	                           VSQuery loQuery = new VSQuery(parentApp.getSession(), "WF_APRV_WRK_LST", lsrSheet, null);
	                           loRS_WF = loQuery.execute();
	                           loResultRow = loRS_WF.first();
	                        }

	                     } /* end if ( leSelRows.hasMoreElements() ) */
	                  } /* end if ( leSelRows != null ) */
	               } /* end if ( loTableElement != null ) */
	            } /* emd if ( loWorklist instanceof AMSWorklistPage ) */
	            else
	            {
	               loResultRow = loWorklistDS.getCurrentRow();
	            } /* end else */

	            /*
	             * For each row, get the assignee from worklist grid, and
	             * set the data to the user id from the user info page.
	             * Also, set the ASSIGNEE_FL to false since this is a
	             * user assignment and no longer role assignment.
	             */
	            if ( loResultRow != null )
	            {
	               /*
	                * If the case is reassignment, just reset the assignee
	                * value, and the assignee flag value
	                */
	               if ( liCase == AMSHyperlinkActionElement.WL_REASSIGN )
	               {
	                  loTargetData = loResultRow.getData( AMSHyperlinkActionElement.ASSIGNEE ) ;
	                  loTargetData.setData( loSourceData ) ;
	                  loTargetData = loResultRow.getData( AMSHyperlinkActionElement.ASSIGNEE_FL ) ;
	                  loTargetData.setBoolean( false ) ;
	                  loTargetData = loResultRow.getData( "LOCK_USID" ) ;
	                  loTargetData.setString( ""  ) ;
	                  loResultRow.save();
	               } /* end if ( liCase == WL_REASSIGN ) */
	               else
	               {
	                  /*
	                   * if case is manual route, will insert new row
	                   * in the worklist table.
	                   */
	                  //BGN ADVHR00003783, ADVHR00003297, ADVHR00003296
	                  //create hidden data source T2WF_APRV_WRK_LST to pass it
	                  AMSAprvWorkflowToolKit.manualRouteInsert( loResultRow,
	                  lsNewUser, T2WF_APRV_WRK_LST, lboolUser ) ;
	                  //END ADVHR00003783, ADVHR00003297, ADVHR00003296
	                  //END ADVHR00003783
	               } /* end else */

	               try
	               {
	                  // reset the 3 properties in the session to blank
	                  if(liCase==AMSHyperlinkActionElement.WL_REASSIGN)
	                  {
	                     loORBSession.setProperty(ATTR_DI_ACTN_CD, Integer.toString(DOC_ACTN_REASSIGN));
	                  }
	                  else
	                  {
	                     loORBSession.setProperty( ATTR_DI_ACTN_CD, "" ) ;
	                  }
	                  loORBSession.setProperty( ATTR_DI_MSG_TXT, "" ) ;
	                  loORBSession.setProperty( ATTR_DI_RESP_TXT, "" ) ;
	               } /* end try */
	               catch( RemoteException foExp )
	               {
                       // Add exception log to logger object
                       moAMSLog.error("Unexpected error encountered while processing. ", foExp);

	                  loWorklist.raiseException( "Unable to set message properties",
	                        AMSPage.SEVERITY_LEVEL_SEVERE ) ;
	                  return loWorklist.generate() ;
	               } /* end catch( RemoteException foExp ) */

	               loWorklistDS.executeQuery() ;

	               if (loRS_WF != null)
	               {
	                  loRS_WF.close();
	               }
	            } /* end if ( loResultRow != null ) */
	         } /* end if ( loRow != null ) */
	      } /* end if ( loRootDS != null ) */

	      if ( loWorklist != null )
	      {
	         return loWorklist.generate() ;
	      } /* end if ( loWorkflowPage != null ) */
	      else
	      {
	         AMSDynamicTransition loDynTran = new AMSDynamicTransition() ;

	         loDynTran.setApplName( "Workflow" ) ;
	         loDynTran.setDestination( "pWF_ADM_Grid" ) ;
	         loDynTran.setTargetFrame( "_self" ) ;
	         return loDynTran.generateHTMLPage( loParentApp, lsSessionID, foPLSReq ) ;
	      } /* end else */
	   } /* end selectUser() */


	   public void setCase( int fiCase )
	   {
	      miCase = fiCase ;
	   } /* end setCase() */

	   public int getCase()
	   {
	      return miCase ;
	   } /* end getCase() */


	   /**
	    * This method gets called from the address book.  The
	    * selected addresses are incorporated into the specified field.
	    * @param fsField the field to receive the address(es)
	    * @param fsValue the additional address(es)
	    */
	   public void setEmailInfo( String fsField, String fsValue )
	   {
	   //This should not be implemented here, this is for pWF_NEW_MSG,
	   // do nothing or throw a severe error.
	   }

	}
