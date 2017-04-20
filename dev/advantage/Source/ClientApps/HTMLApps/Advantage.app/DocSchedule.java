//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import java.util.*;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import advantage.IN_APP_CTRLImpl;

/*
**  DocSchedule
*/

//{{FORM_CLASS_DECL
public class DocSchedule extends DocScheduleBase

//END_FORM_CLASS_DECL}}

implements AMSDocAppConstants, AMSCommonConstants, AMSBatchConstants
{
   // Declarations for instance variables used in the form



      public static final String DOC_SCHED        = "0";
      public static final String DOC_FDT          = "1";
      public static final String AGENT_CLASS_NM   = "SysManUtil";
      public static final String PACKAGE_NAME     = "advantage";
      public static final int    RUN_STATUS       =  STATUS_TO_BE_SUBMITTED;
      public static final int    ITM_TYP          =  SYSTEM_BATCH;
      public static final int    RUN_PLCY_ONCE    =  RUNPLCY_ONCE;
      public static final String ATTR_AGNT_CLS_NM = "AGNT_CLS_NM";
      public static final String ATTR_PKG_NM      = "PKG_NM";
      public static final String ATTR_ITM_TYP     = "ITM_TYP";
      public static final String ATTR_DOC_HLD_FL  = "DOC_HLD_FL";
      public static final String ATTR_ACTN_CD     = "ACTN_CD";
      public static final String ATTR_DOC_AM_FL   = "DOC_AM_FL";
      public static final String ATTR_AGNT_ID     = "AGNT_ID";
      public static final String ATTR_RUN_STA     = "RUN_STA";
      public static final String ATTR_RUN_PLCY    = "RUN_PLCY";
      public static final String ATTR_PNT_CTLG_ID = "PNT_CTLG_ID";
      public static final String ATTR_USID        = "USID";
      public static final String ATTR_EXPIRE_DT   = "RUN_EXPR_TM";

      // Paramters for IN_SYS_MAN_PARM
      public static final int AGNT_ID_IDX           = 1;
      public static final int ACTN_CD_IDX           = 2;
      public static final int DOC_S_ACTN_CD_IDX     = 3;
      public static final int PARM_FILE_IDX         = 4;
      public static final int GENERATE_STATS_IDX    = 5;
      public static final int DOC_TYP_IDX           = 6;
      public static final int DOC_DEPT_CD_IDX       = 7;
      public static final int DOC_VERS_NO_IDX       = 8;
      public static final int DOC_PHASE_CD_IDX      = 9;
      public static final int DOC_CREA_USID_IDX     = 10;
      public static final int DOC_CD_IDX            = 11;
      public static final int DOC_ID_IDX            = 12;
      public static final int DOC_UNIT_CD_IDX       = 13;
      public static final int DOC_STA_CD_IDX        = 14;
      public static final int TBL_NM_IDX            = 15;
      public static final int FM_KEY_IDX            = 16;
      public static final int TO_KEY_IDX            = 17;
      public static final int FILE_NM_IDX           = 18;
      public static final int APPLY_OVERRIDES_IDX   = 19;
      public static final int ERROR_FILE_NM_IDX     = 20;
      public static final int BYPASS_APPROVAL_IDX   = 21;
      public static final int EXCEP_REP_IND_IDX     = 22;
      public static final int EXCEP_REP_FILE_NM_IDX = 23;
      public static final int MAX_ERRORS_IDX        = 24;
      public static final int RESTART_FL_IDX        = 25;
      public static final int COMMIT_BLOCK_IDX      = 25;

      private static final String VIEW_LOG_ACTION   = "viewlog";
      private static final String CURRENCY_ACTION   = "Currency";

      private String        msDocSchedFDT = DOC_SCHED;
      private String        msDocCd;
      private String        msDocDeptCd;
      private String        msDocId;
      private int           miDocVersNo;
      private boolean       mboolFirstTime = true;
      private SearchRequest moDocSchedSrchReq;
      private SearchRequest moDocFDTSrchReq;
      private boolean       mboolDoInsert = false;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public DocSchedule ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}




// To ignore the nav panel
setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE );

   }

















//{{EVENT_CODE
//{{EVENT_DocSchedule_beforeActionPerformed
void DocSchedule_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   VSRow loCurrRow = getRootDataSource().getCurrentRow();

   String lsAction = ae.getAction();

   if (lsAction.equals(ActionElement.db_saveall))
   {
      String lsDocCode       = preq.getParameter( "txtT1DOC_CD" ) ;
      String lsDocDeptCode   = preq.getParameter( "txtT1DOC_DEPT_CD" ) ;
      String lsDocId         = preq.getParameter( "txtT1DOC_ID" ) ;
      String lsDocVersion    = preq.getParameter( "txtT1DOC_VERS_NO" ) ;
      String lsSchedDateTime = preq.getParameter( "txtT1RUN_DT_TM" ) ;
      String lsActionCode    = preq.getParameter( "txtT1ACTN_CD" ) ;

      if ( lsDocCode == null || ( lsDocCode.trim().length() <= 0 ) )
      {
         raiseException("Document Code is required.",
                        SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }

      if ( lsDocDeptCode == null || ( lsDocDeptCode.trim().length() <= 0 ) )
      {
         raiseException(this.getColumnCaption( getRootDataSource(), ATTR_DOC_DEPT_CD ) +
                        " is required.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }

      if ( lsDocId == null || ( lsDocId.trim().length() <= 0 ) )
      {
         raiseException("Document Id is required.",
                        SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }

      if ( lsDocVersion == null || ( lsDocVersion.trim().length() <= 0 ) )
      {
         raiseException("Document Version is required.",
                        SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }

      if ( lsSchedDateTime == null || ( lsSchedDateTime.trim().length() <= 0 ) )
      {
         raiseException("Schedule Date and Time is required.",
                        SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }

      if ( lsActionCode == null || ( lsActionCode.trim().length() <= 0 ) )
      {
         raiseException("Action is required to schedule the document.",
                        SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }
   }
   else if(lsAction.equals(ActionElement.db_delete))
   {
      if (loCurrRow != null)
      {
         int liJobStatus = loCurrRow.getData(ATTR_RUN_STA).getInt();

         /*
          * Check if job is currently running or has
          * been picked up to be run
          */
         if ((liJobStatus == AMSBatchConstants.STATUS_READY) ||
             (liJobStatus == AMSBatchConstants.STATUS_RUNNING) ||
             (liJobStatus == AMSBatchConstants.STATUS_COMPLETE))
         {
            evt.setNewPage(this);
            evt.setCancel(true);    // delete not allowed
            raiseException(
              "The action has started or already been executed, deletion is not possible",
              SEVERITY_LEVEL_ERROR);
         }
      }
   }
   else if (lsAction.equals(VIEW_LOG_ACTION))
   {
      PLSApp loParentApp    = getParentApp() ;
      String lsSessionID    = getSessionId() ;
      StringBuffer loWhereClauseBuf = new StringBuffer(128);

      if (loCurrRow == null)
      {
         raiseException(
            "Please select a row to perform this action",
            SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      /**
       * Let's navigate to view the batch log using AMSDynamicTransition
       */
      loWhereClauseBuf.append(ATTR_AGNT_ID);
      long llAgntId = loCurrRow.getData(ATTR_AGNT_ID).getLong();
      if(llAgntId > 0)
      {
         loWhereClauseBuf.append(" = ");
         loWhereClauseBuf.append(llAgntId);
      }
      else
      {
         raiseException("No job log is available since Document Schedule job "
               +"is not yet saved/scheduled.",SEVERITY_LEVEL_INFO);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }
      AMSDynamicTransition loDynTran = new AMSDynamicTransition(
                      "Job_Log_Entries",loWhereClauseBuf.toString(),
                      "Reports_Sys_Admin_App");

      loDynTran.setSourcePage(this);
      evt.setNewPage(loDynTran.getVSPage(
                           getParentApp(), getSessionId()));
      evt.setCancel(true);
   }
   else if(lsAction.equals(ActionElement.pg_back))
   {
      if (getSourcePage() instanceof AMSDocTabbedPage)
      {
         (( AMSDocTabbedPage) getSourcePage()).setGenTabPage(true);
      }
   }
   else if(lsAction.equals(CURRENCY_ACTION))
   {
      if (preq.getParameter("refreshScalar") != null)
      {
         msDocSchedFDT = preq.getParameter("DocSchedFDT");
         syncRecordCurrency(getDataSource(preq.getParameter("vsds")), preq);
         evt.setNewPage(this);
         evt.setCancel(true);
      }
   }

}
//END_EVENT_DocSchedule_beforeActionPerformed}}
//{{EVENT_DocSchedule_requestReceived
void DocSchedule_requestReceived( PLSRequest req, PageEvent evt )
{
}
//END_EVENT_DocSchedule_requestReceived}}
//{{EVENT_T1BS_AGENT_afterInsert
void T1BS_AGENT_afterInsert(VSRow newRow )
{
   /*
    *  create a default job expiry date
    */
   VSDate loExpireDate = new VSDate();
   loExpireDate.advanceMonths(2);

   // Set the document identifier
   newRow.getData(ATTR_DOC_CD).setString(msDocCd);
   newRow.getData(ATTR_DOC_DEPT_CD).setString(msDocDeptCd);
   newRow.getData(ATTR_DOC_ID).setString(msDocId);
   newRow.getData(ATTR_DOC_VERS_NO).setInt(miDocVersNo);
   newRow.getData(ATTR_DOC_HLD_FL).setBoolean(true);
   newRow.getData(ATTR_AGNT_CLS_NM).setString(AGENT_CLASS_NM);
   newRow.getData(ATTR_PKG_NM).setString(PACKAGE_NAME);
   newRow.getData(ATTR_ITM_TYP).setInt(ITM_TYP);
   newRow.getData(ATTR_RUN_PLCY).setInt(RUN_PLCY_ONCE);
   newRow.getData(ATTR_RUN_STA).setInt(AMSBatchConstants.STATUS_TO_BE_SUBMITTED);
   newRow.getData(ATTR_PNT_CTLG_ID).setLong(getFDTJobParentID( msDocCd, this ));
   newRow.getData(ATTR_USID).setString(getParentApp().getSession().getLogin());
   newRow.getData(ATTR_EXPIRE_DT).setVSDate(loExpireDate);
}
//END_EVENT_T1BS_AGENT_afterInsert}}
//{{EVENT_T1BS_AGENT_beforeQuery
void T1BS_AGENT_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   moDocSchedSrchReq = query.getFilter();

   /*
    * lboolHasOnScreenQueryText variable will be true when a search is triggered on the page
    * and will be used to decide whether we 'AND' the rest of the filter
    * or not
    */
   String lsQryStr = T1BS_AGENT.getOnScreenQueryText();

   boolean lboolHasOnScreenQueryText = (lsQryStr !=null && lsQryStr.trim().length() >0 );

   if (mboolFirstTime && msDocCd != null)
   {
      SearchRequest loSrchReq = new SearchRequest();
      /*
       * The order of the query changed from previous version of this class.  It now uses
       * the index on parent catalog Id of the job row
       * We are escaping the recurring FDT jobs from being selected (NOT IN (..) )
       * here too
       */
      if ( msDocCd == null )
      {
         loSrchReq.add( ATTR_PNT_CTLG_ID+"=" + getFDTJobParentID(
               getSourcePage().getRootDataSource().getCurrentRow().getData( ATTR_DOC_CD ).getString(),
                  this ) + " AND " ) ;
      } /* end if ( msDocCd == null ) */
      else
      {
         loSrchReq.add( ATTR_PNT_CTLG_ID+"=" +getFDTJobParentID( msDocCd, this ) + " AND " ) ;
      } /* end else */
      loSrchReq.add( ATTR_DOC_CD +
                     AMSSQLUtil.getANSIQuotedStr(msDocCd,AMSSQLUtil.EQUALS_OPER) +
                    " AND ");

      loSrchReq.add( ATTR_DOC_ID +
                     AMSSQLUtil.getANSIQuotedStr(msDocId,AMSSQLUtil.EQUALS_OPER) +
                    " AND ");


      loSrchReq.add( ATTR_DOC_DEPT_CD +
                     AMSSQLUtil.getANSIQuotedStr(msDocDeptCd,AMSSQLUtil.EQUALS_OPER) +
                    " AND ");

      loSrchReq.add( ATTR_DOC_VERS_NO+"=" +miDocVersNo + " AND ");

      loSrchReq.add( " (DOC_S_ACTN_CD IS NULL OR DOC_S_ACTN_CD NOT IN (" + AMSDocAppConstants.DOC_SUB_ACTN_RECURRING +
                          "," +AMSDocAppConstants.DOC_SUB_ACTN_JVREVERSAL +
                          "," +AMSDocAppConstants.DOC_SUB_ACTN_RECLASSIFICATION + "))" );
      // Add the search request
      query.addFilter(loSrchReq);
   }
   else
   {
      if (moDocSchedSrchReq == null)
      {
         moDocSchedSrchReq = new SearchRequest();

         if (lboolHasOnScreenQueryText)
         {
           // the on screen query is prepended
           moDocSchedSrchReq.add(" AND ");
         }

         if ( msDocCd == null && getSourcePage().getRootDataSource().getCurrentRow().getData( ATTR_DOC_CD ) != null)
         {
            moDocSchedSrchReq.add( ATTR_PNT_CTLG_ID+"=" +getFDTJobParentID( getSourcePage().getRootDataSource().getCurrentRow().getData( ATTR_DOC_CD ).getString(), this ) + " AND ");
         } /* end if ( msDocCd == null ) */
         else
         {
            moDocSchedSrchReq.add( ATTR_PNT_CTLG_ID+"=" +getFDTJobParentID( msDocCd, this ) + " AND ");
         } /* end else */

         moDocSchedSrchReq.add( " DOC_ID IS NOT NULL  AND ( DOC_S_ACTN_CD IS NULL OR DOC_S_ACTN_CD NOT IN (" + AMSDocAppConstants.DOC_SUB_ACTN_RECURRING +
                             "," +AMSDocAppConstants.DOC_SUB_ACTN_JVREVERSAL +
                    "," +AMSDocAppConstants.DOC_SUB_ACTN_RECLASSIFICATION + "))" );
      }
      else
      {
         if ( msDocCd == null )
         {
            moDocSchedSrchReq.add( " AND " + ATTR_PNT_CTLG_ID+"=" +getFDTJobParentID( getSourcePage().getRootDataSource().getCurrentRow().getData( ATTR_DOC_CD ).getString(), this ) + " AND ");
         } /* end if ( msDocCd == null ) */
         else
         {
            moDocSchedSrchReq.add( " AND " + ATTR_PNT_CTLG_ID+"=" +getFDTJobParentID( msDocCd, this ) + " AND ");
         } /* end else */

         moDocSchedSrchReq.add( " DOC_ID IS NOT NULL  AND (DOC_S_ACTN_CD IS NULL OR DOC_S_ACTN_CD NOT IN (" + AMSDocAppConstants.DOC_SUB_ACTN_RECURRING +
                            "," +AMSDocAppConstants.DOC_SUB_ACTN_JVREVERSAL +
                           "," +AMSDocAppConstants.DOC_SUB_ACTN_RECLASSIFICATION + "))" );
      }
      query.addFilter(moDocSchedSrchReq);
   }
}
//END_EVENT_T1BS_AGENT_beforeQuery}}
//{{EVENT_T1BS_AGENT_afterSave
void T1BS_AGENT_afterSave(VSRow row )
{
   if ( !mboolDoInsert )
   {
      insertJobParams( row );
      row.getData( ATTR_RUN_STA ).setInt( AMSBatchConstants.STATUS_SUBMITTED );
      T1BS_AGENT.updateDataSource();
      return;
   }

   updateJobParams( row );
}
//END_EVENT_T1BS_AGENT_afterSave}}
//{{EVENT_T1BS_AGENT_beforeDelete
void T1BS_AGENT_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
{
   deleteJobParams( row );
}
//END_EVENT_T1BS_AGENT_beforeDelete}}

//END_EVENT_CODE}}
















   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1BS_AGENT.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}















   }

//{{EVENT_ADAPTER_CODE

	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_afterSave(row );
		}
	}
	public void afterInsert( DataSource obj, VSRow newRow ){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_afterInsert(newRow );
		}
	}
	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			DocSchedule_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocSchedule_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_beforeQuery(query , resultset );
		}
	}
	public void beforeDelete( DataSource obj, VSRow row ,VSOutParam cancel ,VSOutParam response ){
		Object source = obj;
		if (source == T1BS_AGENT) {
			T1BS_AGENT_beforeDelete(row ,cancel ,response );
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

   public void setDocumentIdentifier(String fsDocCd, String fsDocDeptCd,
                                     String fsDocId, int fiDocVersNo)
   {
      msDocCd     = fsDocCd;
      msDocDeptCd = fsDocDeptCd;
      msDocId     = fsDocId;
      miDocVersNo = fiDocVersNo;
   }


   public String generate()
   {
      if (mboolFirstTime)
      {
         T1BS_AGENT.executeQuery();
         mboolFirstTime = false;
      }

      // Disable the fields for completed jobs
      VSRow loCurrRow = getRootDataSource().getCurrentRow();
      if (loCurrRow != null)
      {
         Enumeration loScalarElemEnum = getDocumentModel().getScalarElements();
         int liJobStatus = loCurrRow.getData(ATTR_RUN_STA).getInt();

         /*
          * Check if job is complete
          */
         if ((liJobStatus == AMSBatchConstants.STATUS_COMPLETE))
         {
            while ( loScalarElemEnum.hasMoreElements() )
            {
               ScalarElement loScalarElem = (ScalarElement) loScalarElemEnum.nextElement();

               // Make the element "read only"
               loScalarElem.addAttribute( "ams_readonly", "ams_readonly" ) ;
            }

            disableSave();
         }
         else
         {
            while ( loScalarElemEnum.hasMoreElements() )
            {
               ScalarElement loScalarElem = (ScalarElement) loScalarElemEnum.nextElement();

               // Make the element writable
               loScalarElem.getHtmlElement().removeAttribute( "ams_readonly" ) ;
            }

            enableSave();
         }
      }

      return super.generate();
   }

   /** disable save Undo actions
    */
   public void disableSave()
   {
      AMSHyperlinkActionElement loAction =null;
      loAction = (AMSHyperlinkActionElement)getElementByName("T1BS_AGENTSaveAll");
      loAction.setEnabled(false);
      loAction = (AMSHyperlinkActionElement)getElementByName("T1BS_AGENTUndoAll");
      loAction.setEnabled(false);
   } /* end method disableSave() */


   /** enable save Undo actions
    */
   public void enableSave()
   {
      AMSHyperlinkActionElement loAction =null;
      loAction = (AMSHyperlinkActionElement)getElementByName("T1BS_AGENTSaveAll");
      loAction.setEnabled(true);
      loAction = (AMSHyperlinkActionElement)getElementByName("T1BS_AGENTUndoAll");
      loAction.setEnabled(true);
   } /* end method enableSave() */

   private void insertJobParams(VSRow foRow)
   {
      VSRow loParmRow;

      // Check if parameters have to be inserted
      mboolDoInsert = true;

      AMSDataSource loDataSource = getParmDataSource();
      loDataSource.executeQuery();
      VSResultSet loResultSet = null;

      try
      {
         loResultSet = loDataSource.getResultSet();
         loResultSet.last();

         int liRowCount = loResultSet.getRowCount();

         if (loDataSource != null)
         {
            String lsJobID = foRow.getData(ATTR_AGNT_ID).getString();

            // Insert document info as params
            loDataSource.insert();
            loParmRow = loDataSource.getCurrentRow();
            loParmRow.getData(AGNT_ID_IDX).setString(lsJobID);
            loParmRow.getData(DOC_CD_IDX).setString(
                               foRow.getData(ATTR_DOC_CD).getString());
            loParmRow.getData(DOC_DEPT_CD_IDX).setString(
                               foRow.getData(ATTR_DOC_DEPT_CD).getString());
            loParmRow.getData(DOC_ID_IDX).setString(
                               foRow.getData(ATTR_DOC_ID).getString());
            loParmRow.getData(DOC_VERS_NO_IDX).setString(
                               foRow.getData(ATTR_DOC_VERS_NO).getString());
            loParmRow.getData(ACTN_CD_IDX).setString(
                               foRow.getData(ATTR_ACTN_CD).getString());
            loParmRow.getData(DOC_S_ACTN_CD_IDX).setString(
                               foRow.getData(ATTR_DOC_S_ACTN_CD).getString());
         }

         loDataSource.updateDataSource();
         loDataSource.executeQuery();
      }
      finally
      {
         if(loResultSet !=null)
         {
            loResultSet.close();
            loResultSet = null;
         }
      }
   }

   private void updateJobParams(VSRow foRow)
   {
      VSRow loParmRow;

      AMSDataSource loDataSource = getParmDataSource();
      loDataSource.executeQuery();
      VSResultSet loResultSet = null;

      try
      {
         loResultSet = loDataSource.getResultSet();
         loResultSet.last();

         int liRowCount = loResultSet.getRowCount();

         // If the records are already present then update the values
         if (liRowCount > 0)
         {
            String lsJobID = foRow.getData(ATTR_AGNT_ID).getString();
            String lsColName;

            for(int liIndex = 1; liIndex <= liRowCount; liIndex++)
            {
               loParmRow = loResultSet.getRowAt(liIndex);
               lsColName = loParmRow.getData(AGNT_ID_IDX).getString();

               if ( lsColName.equals( lsJobID ) )
               {
                  loParmRow = loDataSource.getCurrentRow();
                  loParmRow.getData(DOC_CD_IDX).setString(
                                     foRow.getData(ATTR_DOC_CD).getString());
                  loParmRow.getData(DOC_DEPT_CD_IDX).setString(
                                     foRow.getData(ATTR_DOC_DEPT_CD).getString());
                  loParmRow.getData(DOC_ID_IDX).setString(
                                     foRow.getData(ATTR_DOC_ID).getString());
                  loParmRow.getData(DOC_VERS_NO_IDX).setString(
                                     foRow.getData(ATTR_DOC_VERS_NO).getString());
                  loParmRow.getData(ACTN_CD_IDX).setString(
                                     foRow.getData(ATTR_ACTN_CD).getString());
                  loParmRow.getData(DOC_S_ACTN_CD_IDX).setString(
                                     foRow.getData(ATTR_DOC_S_ACTN_CD).getString());
                  break;
               }
            }
         }

         loDataSource.updateDataSource();
         loDataSource.executeQuery();
      }
      finally
      {
         if(loResultSet!= null)
         {
            loResultSet.close();
            loResultSet = null;
         }
      }
   }

   private void deleteJobParams(VSRow foRow)
   {
      VSRow loParmRow;

      AMSDataSource loDataSource = getParmDataSource();
      loDataSource.executeQuery();
      VSResultSet loResultSet = null;

      try
      {
         loResultSet = loDataSource.getResultSet();
         loResultSet.last();

         int liRowCount = loResultSet.getRowCount();

         // If the records are already present then update the values
         if (liRowCount > 0)
         {
            String lsJobID = foRow.getData(ATTR_AGNT_ID).getString();
            String lsColName;

            for(int liIndex = 1; liIndex <= liRowCount; liIndex++)
            {
               loParmRow = loResultSet.getRowAt(liIndex);
               lsColName = loParmRow.getData(AGNT_ID_IDX).getString();

               if ( lsColName.equals( lsJobID ) )
               {
                  loDataSource.delete();
                  break;
               }
            }
         }

         loDataSource.updateDataSource();
         loDataSource.executeQuery();
      }
      finally
      {
         if(loResultSet!= null)
         {
            loResultSet.close();
            loResultSet = null;
         }
      }
   }

   private AMSDataSource getParmDataSource()
   {
      AMSDataSource loDataSource = new AMSDataSource();

      loDataSource.setName("IN_SYS_MAN_PARM");
      loDataSource.setSession( parentApp.getSession() );
      loDataSource.setQueryInfo("IN_SYS_MAN_PARM", "", "", "", false);
      loDataSource.setPage(this);
      loDataSource.setDataDependency(false, false);
      loDataSource.setAllowInsert(true);
      loDataSource.setAllowDelete(true);
      loDataSource.setAllowUpdate(true);
      loDataSource.setNumRowsPerPage(10);
      loDataSource.setPreFetchRowCount(false);

      loDataSource.setMaxRowsPerFetch(16);
      loDataSource.setSaveMode(loDataSource.SAVE_IMMEDIATE);

      return loDataSource;

   }

   /**
    * Given a document this method schedules the document
    *
    * @param foDocScheduleRow the document to be scheduled
    * @param foActnElem The action element that started the schedule
    * @param foRequest The PLS request
    * @return The generated print page
    */
   public static String scheduleDocPage( VSRow foDocScheduleRow,
         AMSHyperlinkActionElement foActnElem, PLSRequest foRequest )
   {
      VSPage               loSrcPage   = foActnElem.getPage() ;
      DocSchedule          loDocSchedule ;
      AMSDynamicTransition loDynTran ;
      String               lsHTML ;
      int                  liPhaseCd ;

      /* If no row is available then we can return */
      if ( foDocScheduleRow == null )
      {
         ( (AMSPage) loSrcPage).raiseException(
               "A row has to be selected to perform this action",
               AMSPage.SEVERITY_LEVEL_ERROR ) ;
         return loSrcPage.generate() ;
      } /* end if ( foDocScheduleRow == null ) */

      liPhaseCd = foDocScheduleRow.getData( ATTR_DOC_PHASE_CD ).getInt() ;

      if ( liPhaseCd == DOC_PHASE_TEMPLATE )
      {
         ( (AMSPage) loSrcPage).raiseException(
               "Template phase documents cannot be scheduled",
               AMSPage.SEVERITY_LEVEL_ERROR ) ;
         return loSrcPage.generate() ;
      } /* end if ( liPhaseCd == DOC_PHASE_TEMPLATE ) */

      if ( !foDocScheduleRow.getData( ATTR_DOC_ACT_FL ).getBoolean() )
      {
         ( (AMSPage) loSrcPage ).raiseException(
               "Inactive documents cannot be scheduled",
               AMSPage.SEVERITY_LEVEL_ERROR ) ;
         return loSrcPage.generate() ;
      } /* end if ( liPhaseCd == DOC_PHASE_TEMPLATE ) */

      /*
       * Any additional where clause will be added in the beforeQuery() event
       * only.  This will optimize the filter (starts with indexed columns)
       */
      loDynTran = new AMSDynamicTransition( "DocSchedule",null , "Advantage" ) ;

      loDynTran.setSourcePage( loSrcPage ) ;

      loDocSchedule = (DocSchedule) loDynTran.getVSPage(
            loSrcPage.getParentApp(), loSrcPage.getSessionId() ) ;

      /* Set the document identifier */
      loDocSchedule.setDocumentIdentifier(
              foDocScheduleRow.getData( ATTR_DOC_CD      ).getString(),
              foDocScheduleRow.getData( ATTR_DOC_DEPT_CD ).getString(),
              foDocScheduleRow.getData( ATTR_DOC_ID      ).getString(),
              foDocScheduleRow.getData( ATTR_DOC_VERS_NO ).getInt() ) ;

      lsHTML = loDocSchedule.generate() ;
      foActnElem.getPage().getParentApp().getPageExpireAlg().pageNavigatedTo( loDocSchedule ) ;

      return lsHTML ;
   } /* end scheduleDocPage() */

   protected static long getFDTJobParentID( String fsDocCd, AMSPage foPage )
   {

      VSQuery      loQuery ;
      VSResultSet  loRsltSet = null ;
      long         llApplID  = -1 ;
      StringBuffer lsbWhere ;

      if (fsDocCd == null)
         return -1;

      loQuery = new VSQuery( foPage.getParentApp().getSession(), "R_GEN_DOC_CTRL",
            AMSCommonConstants.ATTR_DOC_CD + "="
            + AMSSQLUtil.getANSIQuotedStr( fsDocCd, true ), "" ) ;

      try
      {
         VSRow  loDocRow ;

         loRsltSet = loQuery.execute() ;
         loRsltSet.last() ;
         loDocRow = loRsltSet.current() ;
         if ( loDocRow == null )
         {
            foPage.raiseException( "Document control record not found for " + fsDocCd,
                  SEVERITY_LEVEL_SEVERE ) ;
            return -1 ;
         } /* end if ( loDocRow == null ) */
         llApplID = loDocRow.getData( "APPL_ID" ).getLong() ;
      } /* end try */
      finally
      {
         if ( loRsltSet != null )
         {
            loRsltSet.close() ;
         } /* end if ( loRsltSet != null ) */
      } /* end finally */

      lsbWhere = new StringBuffer( 64 ) ;
      lsbWhere.append( "(CLS_NM " ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( "SysManUtil", AMSSQLUtil.EQUALS_OPER ) ) ;
      lsbWhere.append( ") AND (PKG_NM " ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( "advantage", AMSSQLUtil.EQUALS_OPER ) ) ;
      lsbWhere.append( ") AND (APPL_ID=" ) ;
      lsbWhere.append( llApplID ) ;
      lsbWhere.append( ") AND (Q_ID=" ) ;
      lsbWhere.append(getFDTQID(foPage));
      lsbWhere.append( ")" ) ;

      loQuery = new VSQuery( foPage.getParentApp().getSession(), "R_BS_CATALOG",
      lsbWhere.toString(), "CTLG_ID" ) ;

      try
      {
         VSRow  loCtlgRow ;

         loRsltSet = loQuery.execute() ;
         loRsltSet.last() ;
         loCtlgRow = loRsltSet.current() ;
         if ( loCtlgRow == null )
         {
            foPage.raiseException( "Unable to locate FDT job for application " + llApplID,
                  SEVERITY_LEVEL_SEVERE ) ;
            return -1 ;
         } /* end if ( loCtlgRow == null ) */
         return loCtlgRow.getData( "CTLG_ID" ).getLong() ;
      } /* end try */
      finally
      {
         if ( loRsltSet != null )
         {
            loRsltSet.close() ;
         } /* end if ( loRsltSet != null ) */
      } /* end finally */
   } /* end getFDTJobParentID() */

   public static int getFDTQID(AMSPage foPage)
   {
      int    liFDTQID = AMSCommonConstants.APPL_DT_JOB_QUEUE_ID;
      String lsTemp = null;
      int    liTemp = -1;

      try
      {
         lsTemp = AMSPLSUtil.getApplParamValue(IN_APP_CTRLImpl.FDT_Q_ID, foPage.getParentApp().getSession());
         liTemp = Integer.parseInt(lsTemp);

         if(validateCVLValue("CVL_JOB_Q_DEF", "JOB_Q_ID", liTemp, foPage))
      {
            liFDTQID = liTemp;
         }
      }
      catch (Exception loExcep)
      {
         //we don't need to do any thing.  Just Use Application Date
      }

      return liFDTQID;
   }

   public static boolean validateCVLValue(String fsTableName, String fsColumnName, int fsCVLValue, AMSPage foPage)
   {
   VSQuery      loQuery;
      VSResultSet  loRsltSet = null;
      VSRow        loDocRow ;
      boolean      lbValidCVLValue = false;

      loQuery = new VSQuery( foPage.getParentApp().getSession(), fsTableName,
           fsColumnName + " = " + fsCVLValue, "" );

      try
      {
         loRsltSet = loQuery.execute() ;
         loDocRow = loRsltSet.first() ;

         if ( loDocRow != null )
         {
            lbValidCVLValue = true;
         }
      } /* end try */
      finally
      {
         if ( loRsltSet != null )
         {
            loRsltSet.close() ;
         } /* end if ( loRsltSet != null ) */
      } /* end finally */

      return lbValidCVLValue;
   }

}
