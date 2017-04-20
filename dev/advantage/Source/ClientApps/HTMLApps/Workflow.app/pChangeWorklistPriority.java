//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.AMSPage;

import java.rmi.RemoteException;
import java.util.Enumeration;
import advantage.AMSStringUtil;

import org.apache.commons.logging.Log;

/*
**  pChangeWorklistPriority
*/

//{{FORM_CLASS_DECL
public class pChangeWorklistPriority extends pChangeWorklistPriorityBase

//END_FORM_CLASS_DECL}}
{

   // True if Change Priority has successfully happened.
   private boolean mboolSave = false;

   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pChangeWorklistPriority.class,
      AMSLogConstants.FUNC_AREA_WORKFLOW ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pChangeWorklistPriority ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pChangeWorklistPriority_beforeActionPerformed
void pChangeWorklistPriority_beforeActionPerformed( ActionElement foAction, PageEvent evt, PLSRequest foRequest )
{
   //Write Event Code below this line

   /*
    * When Action is Save, the Priority and Priority Reason values are to be applied to the
    * selected Worklist records.
    * So if the current action is save, proceed with changing the Priority values.
    */
   if("SavePriority".equalsIgnoreCase(foAction.getAction()))
   {
      String lsNewPriority = foRequest.getParameter("txtT1PRIORITY");
      String lsPriorityReason = foRequest.getParameter("txtT1PRIORITY_REASON");
      int liMaxLimit = 200;

     if(lsPriorityReason.length() > liMaxLimit)
     {
       raiseException("Priority Reason can not exceed 200 characters",SEVERITY_LEVEL_ERROR);
       return;
     }

      /*
       * The Priority Reason value is also added as Comment on the Document Comments table.
       * On Document Comments, the Comment is a required field.
       * Hence making this a required field on Change Priority page.
       */
      if(AMSStringUtil.strIsEmpty(lsPriorityReason))
      {

         ( (ComboBoxElement)this.getElementByName("txtT1PRIORITY")).setValue(lsNewPriority);
         raiseException("Priority Reason is required",SEVERITY_LEVEL_ERROR);

      }
      else
      {
         // Get handle to the Worklist table.
         AMSPage loPrevPage = (AMSPage) getParentApp().getPreviousPage(this);
         VSORBSession loORBSession = getParentApp().getSession().getORBSession();
         DataSource loRootDataSource = loPrevPage.getRootDataSource() ;
         VSResultSet loResultSet = loRootDataSource.getResultSet() ;
         TableElement loTableElement = loPrevPage.getTableElement( "tbl" + loRootDataSource.getName() ) ;
         VSRow loRow ;
         VSResultSet loRS = null;
         String lsOldPriority;

      try
      {
         if ( loTableElement != null )
         {
            Enumeration leSelRows ;
            Integer     liRowIdx ;
            int         liNumRows = 0;

            leSelRows = loTableElement.getSelectedRows() ;

            while ( leSelRows.hasMoreElements() )
            {
               liRowIdx = (Integer)leSelRows.nextElement() ;
               loRow = loResultSet.getRowAt( liRowIdx.intValue() ) ;
               if ( loRow != null )
               {
                  lsOldPriority = loRow.getData("PRIORITY").getString();
                  processPriorityAction(loRow, loRootDataSource, loORBSession,
                        lsOldPriority, lsNewPriority, lsPriorityReason);
               } /* if ( loRow != null ) */

            } /* end while ( leSelRows.hasMoreElements() ) */
         }/* end if ( loTableElement != null ) */

         loPrevPage.refreshDataSource(loRootDataSource);

         if(getHighestSeverityLevel( loORBSession ) <= SEVERITY_LEVEL_WARNING)
         {
            mboolSave = true;
         }
      }// end try
      catch(Exception leExcp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", leExcp);
      }
      finally
      {
         if(loRS!=null)
         {
            loRS.close();
         }
      }
     }//end of else

   }/* end if("SavePriority".equalsIgnoreCase(foAction.getAction())) */

}

//END_EVENT_pChangeWorklistPriority_beforeActionPerformed}}
//{{EVENT_pChangeWorklistPriority_beforeGenerate
void pChangeWorklistPriority_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
    AMSPage loPrevPage = (AMSPage) getParentApp().getPreviousPage(this);

    DataSource loRootDataSource = loPrevPage.getRootDataSource() ;
    VSResultSet loResultSet = loRootDataSource.getResultSet() ;

    TableElement loTableElement = loPrevPage.getTableElement( "tbl" + loRootDataSource.getName() ) ;

    VSRow loRow  ;
    String lsOldPriority;

    Enumeration leSelRows ;
    Integer     liRowIdx ;

    if ( loTableElement != null && loResultSet != null )
      {
          leSelRows = loTableElement.getSelectedRows() ;
          liRowIdx = (Integer)leSelRows.nextElement() ;;
          loRow = loRow = loResultSet.getRowAt( liRowIdx.intValue() ) ;;

          if ( loRow != null )
         {
            lsOldPriority = loRow.getData("PRIORITY").getString();
            ( (ComboBoxElement)this.getElementByName("txtT1PRIORITY")).setValue(lsOldPriority);

         }


      }

}
//END_EVENT_pChangeWorklistPriority_beforeGenerate}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pChangeWorklistPriority_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pChangeWorklistPriority_beforeActionPerformed( ae, evt, preq );
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
    * Called at the time of page generation.
    * If save is successful, this Change Priority page is to be closed and the previous
    * Worklist page is to be opened.
    * Hence generate the previous Worklist page and issue Information message of action completed.
    */
   public String generate()
   {
      if(mboolSave)
      {
         AMSPage loPrevPage = (AMSPage) getParentApp().getPreviousPage(this);
         loPrevPage.raiseException("Change Priority action completed.", SEVERITY_LEVEL_INFO);
         return loPrevPage.generate();
      }

      return super.generate();
   }


   /**
    *
    * @param foRow Current Worklist row being processed.
    * @param foDataSource Handle to Root Data Source of Worklist page.
    * @param foSession Handle to current Session
    * @param fsOldPriority The old Priority value of Worklist item.
    * @param fsNewPriority The new Priority value that is being assigned to the Worklist item.
    * @param fsPriorityReas The reason for Change Priority.
    * @return
    */
   private boolean processPriorityAction(VSRow foRow, DataSource foDataSource, VSORBSession foSession,
         String fsOldPriority, String fsNewPriority, String fsPriorityReas)
   {
      // The action to be performed
      String               lsDI_ACTN_CD = Integer.toString(WL_CHANGE_PRIORITY);

      // Temporary VSData variables needed to force update on the data source.
      VSData loData;

      /* for the current row, set up message text and action code */
      /* and response to be added to the session                  */
      if ( foRow != null )
      {

         try
         {
            // set the 3 properties in the session
            foSession.setProperty(ATTR_DI_ACTN_CD, lsDI_ACTN_CD);
            foSession.setProperty(WL_PRIORITY_VAL, fsOldPriority + "~" + fsNewPriority);
            foSession.setProperty(WL_PRIORITY_REASON_VAL, fsPriorityReas);
         } /* end try */
         catch( RemoteException loExp )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);
            AMSPage.raiseException( foSession, "Unable to set message properties",
               AMSPage.SEVERITY_LEVEL_SEVERE ) ;
            return false;
         } /* end catch( RemoteException foExp ) */



         // update the datasource
         // catch any exception in this method without letting it trickle down
         // to the exception handler of the perform action method. By coding this way
         // ensure that all selected records will be processed.
         try
         {

            /* Temporary Solution to update the datasource */
            if (foRow != null)
            {
               loData = foRow.getData("FWD_USID");
               String lsDataString = loData.getString() ;
               lsDataString += "temp" ;
               loData.setString(lsDataString);
            }
            /* end of temporary solution to force update */

            // update the datasource
            // catch any exception in this method without letting it trickle down
            // to the exception handler of the calling method. By coding this way
            // ensure that all selected records will be processed.
            foDataSource.updateDataSource();
         }
         catch (Exception loExp)
         {
            // Add exception log to logger object
           moAMSLog.error("Unexpected error encountered while processing. ", loExp);
         }
      }


      try
      {
         // reset the property in the session to blank.
         foSession.setProperty(ATTR_DI_ACTN_CD, "");
         foSession.setProperty(WL_PRIORITY_VAL, "");
         foSession.setProperty(WL_PRIORITY_REASON_VAL, "");

      } /* end try */
      catch( RemoteException loExp )
      {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);
         AMSPage.raiseException( foSession, "Unable to set message properties",
            AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return false;
      } /* end catch( RemoteException foExp ) */

      //Successful, return true;
      return true;
   }//end of method

}