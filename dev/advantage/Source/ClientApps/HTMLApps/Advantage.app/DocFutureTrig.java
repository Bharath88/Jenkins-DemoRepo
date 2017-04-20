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
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.vfc.html.*;
import java.util.*;
import advantage.AMSStringUtil;
import org.apache.commons.logging.Log;


/*
**  DocFutureTrig*/

//{{FORM_CLASS_DECL
public class DocFutureTrig extends DocFutureTrigBase

//END_FORM_CLASS_DECL}}
implements AMSDocAppConstants,AMSCommonConstants,AMSBatchConstants
{
   // Declarations for instance variables used in the form
      private String        msDocCd;
      private String        msDocDeptCd;
      private String        msDocId;
      private int           miDocVersNo;
      private boolean       mboolFirstTime = true;

      public static final String ATTR_ACTN_CD     = "ACTN_CD";
      public static final String ATTR_RUN_STATUS  = "FDT_RUN_STA";

      public static final String ATTR_DOC_HLD_FL   = "DOC_HLD_FL";
      public static final String ATTR_DOC_AM_FL    = "AMT_CRY_FWD_FL";
      public static final String ATTR_DOC_STRT_DT  = "FDT_STRT_DT";
      public static final String ATTR_DOC_END_DT   = "FDT_END_DT";
      public static final String ATTR_EXPIRE_DT    = "FDT_EXPIRE_DT";

      public static final String INVALID_DATE_MSG        =
             "Invalid Future Trigger Date";

      public static final String REQ_FREQ_TYP2_AND3 =
             "Frequency Type 2 and 3 fields are required for the selected Frequency Type 1";

      public static final String INVALID_FREQ_TYP2 =
             "Frequency Type 2 is invalid for the selected Frequency Type 1";

      public static final String INVALID_FREQ_TYP3 =
             "Frequency Type 3 is invalid for the selected Frequency Type 1";

      public static final String NO_SELECTED_DATE        =
             "No trigger date could be calculated between specified "
             + "start and end dates";
      public static final String ATTR_DOC_S_ACTN         = "DOC_S_ACTN_CD";
      public static final String ATTR_FREQ_TYP           = "FDT_FREQ_TYP";
      public static final String ATTR_FREQ_VL1           = "FDT_RUN_VL1";
      public static final String ATTR_FREQ_VL2           = "FDT_RUN_VL2";
      public static final String STRT_DT_INVALID         =
              "Start date less than current sytem date";
      public static final String PAST_SEARCH_END_YEAR    = "Past search end date";
      public static final String PAST_END_DATE           = "Past end date";
      public static final String PARM_TO_DOC_DEPT_CD     = "TO_DOC_DEPT_CD";
      public static final String PARM_TO_DOC_ID          = "TO_DOC_ID";
      public static final String PARM_TO_DOC_UNIT_CD     = "TO_DOC_UNIT_CD";
      public static final String PARM_TO_AUTO_DOC_NO     = "TO_AUTO_DOC_NO";

      public static final String ATTR_JOB_ID             = "AGENT_ID";
      public static final String ATTR_PARM_NM            = "PARM_NM";
      public static final String ATTR_PARM_VL            = "PARM_VL";
      public static final String ATTR_OV_FL              = "OV_FL";
      public static final String ATTR_PARM_DSCR          = "PARM_DSCR";
      public static final String ATTR_USID               ="USID";

      private static final String VIEW_LOG_ACTION        ="viewlog";

      //these constants correspond to CVL_JOB_STATUS_2 values used
      // by the FDT process
      private static final int STATUS_READY = 4;
      private static final int STATUS_RUNNING = 5;
      private static final int STATUS_COMPLETE = 3;

      private boolean mboolChangeStatus = true;

      //variable stores initial schedule date:  has to be rest by user to trigger job
      VSDate moInitialRunDt = null;


      // The year after which the search for the next trigger date should expire
      // to avoid accidental date searches e.g 5th Feb of Year 2978 with start date as 2001
      public static final int    SEARCH_END_YEAR   =2010;
      
      private static Log moAMSLog = AMSLogger.getLog( DocFutureTrig.class,
            AMSLogConstants.FUNC_AREA_DFLT ) ;

      VSDate moCurrentDate =null;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public DocFutureTrig ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_DocFutureTrig_beforeActionPerformed
void DocFutureTrig_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   VSRow loCurrRow = getRootDataSource().getCurrentRow();
   String lsAction = ae.getAction();

   if(loCurrRow != null)
   {
      TextFieldElement loNextTrigDate = (TextFieldElement) getDocumentModel().
                                         getElementByName("txtT1RUN_DT_TM");

      if(lsAction.equals(ActionElement.db_queryMode))
      {
         loNextTrigDate.setValue("");
         loNextTrigDate.getHtmlElement().removeAttribute( "ams_readonly" ) ;
      }
   }

   if(lsAction.equals(ActionElement.db_delete))
   {
      if (loCurrRow != null)
      {
         int liJobStatus = loCurrRow.getData(ATTR_RUN_STATUS).getInt();

         /*
          * Check if job is currently running or has
          * been picked up to be run
          */
         if (( (liJobStatus == AMSBatchConstants.STATUS_RUNNING) ||
             (liJobStatus == AMSBatchConstants.STATUS_COMPLETE))&&
             !isDocDiscarded())

         {
            evt.setNewPage(this);
            evt.setCancel(true);    // delete not allowed
            raiseException(
              "Trigger has started or already been executed, deletion is not possible",
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
      loWhereClauseBuf.append("AGNT_ID");

      long llAgntId = loCurrRow.getData(ATTR_JOB_ID).getLong();
      if(llAgntId > 0)
      {
         loWhereClauseBuf.append(" = ");
         loWhereClauseBuf.append(llAgntId);
      }
      else
      {
         raiseException("No job log is available since Future Document Trigger job "
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
   else if(lsAction.equals(ActionElement.db_saveall))
   {
      String lsDocCode       = preq.getParameter( "txtT1DOC_CD" ) ;
      String lsDocDeptCode   = preq.getParameter( "txtT1DOC_DEPT_CD" ) ;
      String lsDocId         = preq.getParameter( "txtT1DOC_ID" ) ;
      String lsDocVersion    = preq.getParameter( "txtT1DOC_VERS_NO" ) ;

      if ( lsDocCode == null || ( lsDocCode.trim().length() <= 0 ) )
      {
         raiseException(this.getColumnCaption( getRootDataSource(), ATTR_DOC_CD ) +
                        " is required.", SEVERITY_LEVEL_ERROR);
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
         raiseException(this.getColumnCaption( getRootDataSource(), ATTR_DOC_ID ) +
                        " is required.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
      }

      if ( lsDocVersion == null || ( lsDocVersion.trim().length() <= 0 ) )
      {
         loCurrRow.getData("DOC_VERS_NO").setInt(1);
      }


   }
   else if(lsAction.equals(ActionElement.db_queryMode))
   {
      appendOnloadString("DOC_SetFDTQueryMode(true)");
   }
   else if(lsAction.equals(ActionElement.db_queryMode))
   {
      appendOnloadString("DOC_SetFDTQueryMode(false)");
   }
   else if(lsAction.equals(ActionElement.pg_back))
   {
      VSPage loSrcPage = getSourcePage() ;

      if ( ( loSrcPage != null ) && ( loSrcPage instanceof AMSDocTabbedPage ) )
      {
         ((AMSDocTabbedPage)loSrcPage).setGenTabPage( true ) ;
      }
   }

}
//END_EVENT_DocFutureTrig_beforeActionPerformed}}
//{{EVENT_DocFutureTrig_afterActionPerformed
void DocFutureTrig_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   mboolChangeStatus = true;
}
//END_EVENT_DocFutureTrig_afterActionPerformed}}
//{{EVENT_DocFutureTrig_pageCreated
void DocFutureTrig_pageCreated()
{
   moInitialRunDt = new VSDate();
   moInitialRunDt.setHours(0);
   moInitialRunDt.setMinutes(0);
   moInitialRunDt.setSeconds(0);
   moInitialRunDt.advanceYears(40);
}
//END_EVENT_DocFutureTrig_pageCreated}}
//{{EVENT_T1FDT_WKLD_error
void T1FDT_WKLD_error(VSException dbException ,VSOutParam display, VSOutParam newPage )
{
   //Write Event Code below this line
}
//END_EVENT_T1FDT_WKLD_error}}
//{{EVENT_T1FDT_WKLD_beforeSave
void T1FDT_WKLD_beforeSave(VSRow row ,VSOutParam cancel )
{

   moCurrentDate = getTodaysDate();

   int liFreqType = row.getData(ATTR_FREQ_TYP).getInt();
   int liFreq2    = row.getData(ATTR_FREQ_VL1).getInt();
   int liFreq3    = row.getData(ATTR_FREQ_VL2).getInt();

   VSDate loStartDate = null;
   if (row.getData(ATTR_DOC_STRT_DT).getString() != null )
   {
      loStartDate = row.getData(ATTR_DOC_STRT_DT).getVSDate();
   }

   VSDate loEndDate   = null;
   if (row.getData(ATTR_DOC_END_DT).getString() != null)
   {
      loEndDate= row.getData(ATTR_DOC_END_DT).getVSDate();
   }

   //valid values for both start and end dates needed
   if(loStartDate == null)
   {
      raiseException("Valid start date is required",
                     SEVERITY_LEVEL_ERROR);
      cancel.setValue(true);
      return;
   }

   // proceed only if next future trigger date is in 'future'
   if(loStartDate.before(moCurrentDate))
   {
      raiseException(STRT_DT_INVALID,SEVERITY_LEVEL_ERROR);
      cancel.setValue(true);
      return;
   }

   if (loEndDate !=null)
   {
      // start date should be before end date
      if (!loStartDate.onOrBefore(loEndDate))
      {
         raiseException("Start date should be before end Date",
                      SEVERITY_LEVEL_ERROR);
         cancel.setValue(true);
         return;
      }
   }


   if (liFreqType ==1) // one time
   {
      setOneTime(loStartDate);
   }
   else if(liFreqType ==2) // weekly
   {
      if (!setWeekly(liFreqType,liFreq2,liFreq3,loStartDate,loEndDate))
      {
         cancel.setValue(true);
         return;
      }
   }
   else if(liFreqType ==3) // monthly by day
   {
      if (!setMonthlyByDay(liFreqType,liFreq2,liFreq3,loStartDate,loEndDate))
      {
         cancel.setValue(true);
         return;
      }
   }
   else if(liFreqType ==4) // monthly by date
   {
      if(!setMonthlyByDate(liFreqType,liFreq2,liFreq3,loStartDate,loEndDate))
      {
         cancel.setValue(true);
         return;
      }
   }
   else
   {
      raiseException("A frequency type has to be selected",
                     SEVERITY_LEVEL_ERROR);
      cancel.setValue(true);
      return;
   }



   VSDate loTriggerDate = row.getData(ATTR_DOC_STRT_DT).getVSDate();
   VSDate loExpireDate = row.getData(ATTR_EXPIRE_DT).getVSDate();

   /*
    *  Calculate the job expiry date i.e 6 months from trigger date
    */
   if ( loExpireDate == null )
   {
      loExpireDate = new VSDate(loTriggerDate);
      loExpireDate.advanceMonths(6);
      row.getData(ATTR_EXPIRE_DT).setVSDate(loExpireDate);
   }
   else
   {
      /**
       * As one last check, if the expiration date is after the trigger date
       * or not
       */
      if (loTriggerDate.onOrAfter(loExpireDate))
      {
         raiseException("The trigger date cannot be after the expiration date",
                        SEVERITY_LEVEL_ERROR);
         cancel.setValue(true);
         return;
      }
   }


   // if carry forward amount flag not checked, held flag is checked
   if(!row.getData(ATTR_DOC_AM_FL).getBoolean())
   {
      row.getData(ATTR_DOC_HLD_FL).setBoolean(true);
   }



}
//END_EVENT_T1FDT_WKLD_beforeSave}}
//{{EVENT_T1FDT_WKLD_rowChanged
void T1FDT_WKLD_rowChanged(VSRow row )
{
   //Write Event Code below this line
}
//END_EVENT_T1FDT_WKLD_rowChanged}}
//{{EVENT_T1FDT_WKLD_beforeInsert
void T1FDT_WKLD_beforeInsert(VSRow newRow ,VSOutParam cancel )
{


   newRow.getData(ATTR_DOC_S_ACTN).setInt(DOC_SUB_ACTN_RECURRING);
  // newRow.getData(ATTR_DOC_HLD_FL).setBoolean(true);
   newRow.getData(ATTR_RUN_STATUS).setInt(STATUS_READY);



}
//END_EVENT_T1FDT_WKLD_beforeInsert}}
//{{EVENT_T1FDT_WKLD_afterInsert
void T1FDT_WKLD_afterInsert(VSRow newRow )
{

   if ( msDocCd != null )
   {
      newRow.getData(ATTR_DOC_CD).setString(msDocCd);
   }
   if ( msDocDeptCd != null )
   {
      newRow.getData(ATTR_DOC_DEPT_CD).setString(msDocDeptCd);
   }
   if ( msDocId != null )
   {
      newRow.getData(ATTR_DOC_ID).setString(msDocId);
   }
   if ( !(miDocVersNo == 0) )
   {
      newRow.getData(ATTR_DOC_VERS_NO).setInt(miDocVersNo);
   }
   newRow.getData(ATTR_ACTN_CD).setInt(DOC_ACTN_OTHER);
   newRow.getData(ATTR_DOC_STRT_DT).setVSDate(moInitialRunDt);
   newRow.getData(ATTR_EXPIRE_DT).setVSDate(null);

}
//END_EVENT_T1FDT_WKLD_afterInsert}}
//{{EVENT_T1FDT_WKLD_afterQuery
void T1FDT_WKLD_afterQuery(VSResultSet rs )
{
   if (mboolFirstTime)
   {
      String lsRelnWhere = T1FDT_WKLD.getQueryInfo().getRelnWhere();
      String lsOrderBy   = T1FDT_WKLD.getQueryInfo().getOrderBy();
      StringBuffer loDevWhere = new StringBuffer(256);

      loDevWhere.append("DOC_S_ACTN_CD IN (");
      loDevWhere.append(AMSDocAppConstants.DOC_SUB_ACTN_RECURRING);
      loDevWhere.append(',');
      loDevWhere.append(AMSDocAppConstants.DOC_SUB_ACTN_JVREVERSAL);
      loDevWhere.append(',');
      loDevWhere.append(
                          AMSDocAppConstants.DOC_SUB_ACTN_RECLASSIFICATION);
      loDevWhere.append(") ");

      T1FDT_WKLD.setQueryInfo(T1FDT_WKLD.getMetaQueryName(),
                              loDevWhere.toString(),
                              lsRelnWhere, lsOrderBy);
      mboolFirstTime = false;
   }
}
//END_EVENT_T1FDT_WKLD_afterQuery}}
//{{EVENT_T1FDT_WKLD_afterSave
void T1FDT_WKLD_afterSave(VSRow foRow )
{
   mboolChangeStatus = false;
   getRootDataSource().updateDataSource();

}
//END_EVENT_T1FDT_WKLD_afterSave}}
//{{EVENT_T1FDT_WKLDfirstpage_onGenerate
void T1FDT_WKLDfirstpage_onGenerate(ActionElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_T1FDT_WKLDfirstpage_onGenerate}}
//{{EVENT_T1FDT_WKLDfirstpage_onAction
void T1FDT_WKLDfirstpage_onAction(ActionElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_T1FDT_WKLDfirstpage_onAction}}
//{{EVENT_T1FDT_WKLDlastpage_onGenerate
void T1FDT_WKLDlastpage_onGenerate(ActionElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_T1FDT_WKLDlastpage_onGenerate}}
//{{EVENT_T1FDT_WKLDlastpage_onAction
void T1FDT_WKLDlastpage_onAction(ActionElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_T1FDT_WKLDlastpage_onAction}}
//{{EVENT_InitPageTransaction_onGenerate
void InitPageTransaction_onGenerate(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_InitPageTransaction_onGenerate}}
//{{EVENT_InitPageTransaction_onAcceptData
void InitPageTransaction_onAcceptData(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_InitPageTransaction_onAcceptData}}
//{{EVENT_MsgDispContent_onGenerate
void MsgDispContent_onGenerate(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_MsgDispContent_onGenerate}}
//{{EVENT_MsgDispContent_onAcceptData
void MsgDispContent_onAcceptData(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_MsgDispContent_onAcceptData}}
//{{EVENT_tblT1FDT_WKLD_onGenerate
void tblT1FDT_WKLD_onGenerate(TableElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_tblT1FDT_WKLD_onGenerate}}
//{{EVENT_tblT1FDT_WKLD_onAcceptData
void tblT1FDT_WKLD_onAcceptData(TableElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_tblT1FDT_WKLD_onAcceptData}}
//{{EVENT_txtT1DOC_CD_onGenerate
void txtT1DOC_CD_onGenerate(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_txtT1DOC_CD_onGenerate}}
//{{EVENT_txtT1DOC_CD_onAcceptData
void txtT1DOC_CD_onAcceptData(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_txtT1DOC_CD_onAcceptData}}
//{{EVENT_txtT1FDT_RUN_VL2_onGenerate
void txtT1FDT_RUN_VL2_onGenerate(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_txtT1FDT_RUN_VL2_onGenerate}}
//{{EVENT_txtT1FDT_RUN_VL2_onAcceptData
void txtT1FDT_RUN_VL2_onAcceptData(ScalarElement elem, ElementEvent evt)
{
   //Write Event Code below this line
}
//END_EVENT_txtT1FDT_RUN_VL2_onAcceptData}}

//END_EVENT_CODE}}
   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1FDT_WKLD.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocFutureTrig_afterActionPerformed( ae, preq );
		}
	}
	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_afterSave(row );
		}
	}
	public void afterInsert( DataSource obj, VSRow newRow ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_afterInsert(newRow );
		}
	}
	public void error( DataSource obj, VSException dbException ,VSOutParam display, VSOutParam newPage ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_error(dbException ,display, newPage );
		}
	}
	public void rowChanged( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_rowChanged(row );
		}
	}
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_beforeSave(row ,cancel );
		}
	}
	public void pageCreated ( VSPage obj ){
		Object source = obj;
		if (source == this ) {
			DocFutureTrig_pageCreated();
		}
	}
	public void onGenerate(BoundElement elem, ElementEvent evt){
		if (elem.getName().equals("T1FDT_WKLDfirstpage")) {
			T1FDT_WKLDfirstpage_onGenerate((ActionElement)elem, evt);
		}
	
		if (elem.getName().equals("T1FDT_WKLDlastpage")) {
			T1FDT_WKLDlastpage_onGenerate((ActionElement)elem, evt);
		}
	
		if (elem.getName().equals("InitPageTransaction")) {
			InitPageTransaction_onGenerate((ScalarElement)elem, evt);
		}
	
		if (elem.getName().equals("MsgDispContent")) {
			MsgDispContent_onGenerate((ScalarElement)elem, evt);
		}
	
		if (elem.getName().equals("tblT1FDT_WKLD")) {
			tblT1FDT_WKLD_onGenerate((TableElement)elem, evt);
		}
	
		if (elem.getName().equals("txtT1DOC_CD")) {
			txtT1DOC_CD_onGenerate((ScalarElement)elem, evt);
		}
	
		if (elem.getName().equals("txtT1FDT_RUN_VL2")) {
			txtT1FDT_RUN_VL2_onGenerate((ScalarElement)elem, evt);
		}
	}
	public void onAction(ActionElement elem, ElementEvent evt){
		if (elem.getName().equals("T1FDT_WKLDfirstpage")) {
			T1FDT_WKLDfirstpage_onAction(elem, evt);
		}
	
		if (elem.getName().equals("T1FDT_WKLDlastpage")) {
			T1FDT_WKLDlastpage_onAction(elem, evt);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocFutureTrig_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeInsert( DataSource obj, VSRow newRow ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_beforeInsert(newRow ,cancel );
		}
	}
	public void afterQuery( DataSource obj, VSResultSet rs ){
		Object source = obj;
		if (source == T1FDT_WKLD) {
			T1FDT_WKLD_afterQuery(rs );
		}
	}
	public void onAcceptData(BoundElement elem, ElementEvent evt){
		if (elem.getName().equals("InitPageTransaction")) {
			InitPageTransaction_onAcceptData((ScalarElement)elem, evt);
		}
	
		if (elem.getName().equals("MsgDispContent")) {
			MsgDispContent_onAcceptData((ScalarElement)elem, evt);
		}
	
		if (elem.getName().equals("tblT1FDT_WKLD")) {
			tblT1FDT_WKLD_onAcceptData((TableElement)elem, evt);
		}
	
		if (elem.getName().equals("txtT1DOC_CD")) {
			txtT1DOC_CD_onAcceptData((ScalarElement)elem, evt);
		}
	
		if (elem.getName().equals("txtT1FDT_RUN_VL2")) {
			txtT1FDT_RUN_VL2_onAcceptData((ScalarElement)elem, evt);
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


   /** method overrides the method in the superclass to introduce
    *  custom behavior
    */
   public String generate()
   {

      int    liSubActn;
      int    liRunSta;
      VSRow  loCurrRow = getRootDataSource().getCurrentRow();
      VSPage loSrcPage ;

      if (loCurrRow == null)
      {
         return super.generate();
      }

      HiddenElement loHiddenElem = (HiddenElement)getElementByName("freqType");
      loHiddenElem.setValue(loCurrRow.getData(ATTR_FREQ_TYP).getString());

      loHiddenElem = (HiddenElement)getElementByName("freqVl1");
      loHiddenElem.setValue(loCurrRow.getData(ATTR_FREQ_VL1).getString());
      loHiddenElem = (HiddenElement)getElementByName("freqVl2");
      loHiddenElem.setValue(loCurrRow.getData(ATTR_FREQ_VL2).getString());

      liSubActn = loCurrRow.getData( ATTR_DOC_S_ACTN ).getInt();

      if (getRootDataSource().getQueryMode() == DataSource.MODE_QBF)
      {
         setRadioElements(0);
      }
      else
      {
         setRadioElements(liSubActn);
      }

      ActionElement loActionElement = getCurrentAction();

      if ( ( AMSStringUtil.strIsEmpty(msDocCd) ||
            AMSStringUtil.strIsEmpty(msDocDeptCd) ||
            AMSStringUtil.strIsEmpty(msDocId) )&& ( ( loActionElement != null &&
            loActionElement.getAction().equals("insert") ) || ( getLastAction() != null &&
              getLastAction().equals("insert") )  )  )
      {
         enableDocFields();
      }
      else if(getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR)
      {
         disableDocFields();
      }
      liRunSta = loCurrRow.getData( ATTR_RUN_STATUS ).getInt();

      if (liRunSta == STATUS_COMPLETE ||
            liRunSta == STATUS_RUNNING)
      {
         disableSave();
         disableAllFields();
      }
      else
      {
         if(liSubActn ==DOC_SUB_ACTN_JVREVERSAL)
         {
            /**
            * This condition was introduced for JV Reversal scenario (when Sub action is JV Reversal and Run Status is 'Ready'),
            * as in this case 'Save'/'Undo' should be enabled.
            * Also existing method disableOtherFields() was disabling 'Active'/'Carry Forward Amount'/'Hold Document' fields, which
            * infact should be enabled for JV Reversal. Other fields that should also be enabled for JV Reversal are 'Start Date' and
            * 'Expire Date', hence a new method was created enableFieldsForJVReversal() to enable fields that are needed for JV Reversal.
            */
            if(liRunSta == STATUS_READY)
            {
               enableSave();
               enableFieldsForJVReversal();
            }
            else
            {
               disableSave();
               disableOtherFields();
            }
         }
         else if(liSubActn ==DOC_SUB_ACTN_RECLASSIFICATION)
         {
            disableSave();
            disableOtherFields();
         }
         else // RECURRING
         {
            enableSave();
            enableOtherFields();
         }

         /**
         * This condition was introduced because for JV Reversal, 'End Date' and 'Frequency Type 1-3' fields should be
         * disabled. But existing method enableFreqFields() would enable other fields AND the 'End Date'/'Frequency Type 1-3'
         * fields too. Hence a new method was created disableFreqFieldsForJVReversal() to specifically disable above mentioned fields.
         * This scenario is only valid when Sub action is JV Reversal and Run Status is 'Ready'.
         */
         if((liSubActn == DOC_SUB_ACTN_JVREVERSAL) && (liRunSta == STATUS_READY))
         {
            disableFreqFieldsForJVReversal();
         }
         else
         {
            enableFreqFields();
         }
      }
      appendOnloadString("DOC_FDTSetOnLoad();DOC_FDTScheduling()");

      loSrcPage = getSourcePage() ;

      /*
       * do not show next trigger date value if its still the initial Trigger Date
       */
      VSDate loCurrRunDate = loCurrRow.getData(ATTR_DOC_STRT_DT).getVSDate();
      if ( ( loCurrRunDate == null ) ||
           ( loCurrRunDate.equals( moInitialRunDt ) ) )
      {
         loCurrRow.getData( ATTR_DOC_STRT_DT ).setVSDate( null );
      }

      return super.generate();
   } /* end method generate() */

   /** method set the radio group based on input
    *  @param fiSubActn The Sub Action code
    */
   public void setRadioElements(int fiSubActn)
   {
      AMSRadioButtonElement loRadioElem =null;
      RadioButtonGroup  loRadioGroup =null;

      loRadioGroup = (RadioButtonGroup) getElementByName("TriggerType");
      String lsValue;

      for(Enumeration loEnum = loRadioGroup.getElements();
                              loEnum.hasMoreElements();)
      {
         loRadioElem = (AMSRadioButtonElement) loEnum.nextElement();
         lsValue = loRadioElem.getValueProperty();

         if ((fiSubActn == DOC_SUB_ACTN_RECURRING
             && lsValue.equals("0")) ||
             (fiSubActn == DOC_SUB_ACTN_JVREVERSAL
             && lsValue.equals("1")) ||
             (fiSubActn == DOC_SUB_ACTN_RECLASSIFICATION
             && lsValue.equals("2")))
         {
            loRadioElem.setChecked(true);
            loRadioElem.setEnabled(false);
         }
         else if (fiSubActn == 0)
         {
            loRadioElem.setChecked(false);
            loRadioElem.setEnabled(true);
         }
         else
         {
            loRadioElem.setChecked(false);
            loRadioElem.setEnabled(false);
         }
      }
   } /* end method setRadioElements */


   /** method disables the Document specific 'Input' fields on the page
    */
   public void disableDocFields()
   {
      getElementByName("txtT1DOC_CD").addAttribute("ams_readonly", "true");
      getElementByName("txtT1DOC_ID").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1DOC_DEPT_CD").addAttribute("ams_readonly", "true");
   } /* end method disableDocFields */


   /** method disables the End Date, Frequency Type 1 -3 fields on the page
    */
   private void disableFreqFieldsForJVReversal()
   {
      getElementByName("txtT1FDT_END_DT").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_FREQ_TYP").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_RUN_VL1").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_RUN_VL2").addAttribute("ams_readonly", "true");
   }


   private void disableFreqFields()
   {
      getElementByName("txtT1FDT_STRT_DT").addAttribute("ams_readonly", "true");
      getElementByName("txtT1FDT_END_DT").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_FREQ_TYP").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_RUN_VL1").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_RUN_VL2").addAttribute("ams_readonly", "true");
      getElementByName(
         "txtT1FDT_EXPIRE_DT").addAttribute("ams_readonly", "true");
   }

   private void enableFreqFields()
   {
      getElementByName("txtT1FDT_STRT_DT").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName("txtT1FDT_END_DT").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
         "txtT1FDT_FREQ_TYP").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
         "txtT1FDT_RUN_VL1").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
         "txtT1FDT_RUN_VL2").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
         "txtT1FDT_EXPIRE_DT").getHtmlElement().removeAttribute("ams_readonly");
   }

   /** method disables the Document specific 'Input' fields on the page
    */
   public void enableDocFields()
   {
      getElementByName(
             "txtT1DOC_CD").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
             "txtT1DOC_ID").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
       "txtT1DOC_DEPT_CD").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
       "txtT1DOC_VERS_NO").getHtmlElement().removeAttribute("ams_readonly");
   } /* end method enableDocFields() */


   /** disable DOC_HLD_FL and DOC_AM_FL fields
    */
   public void disableOtherFields()
   {
      AMSCheckboxElement loScalar =null;
      loScalar = (AMSCheckboxElement)getElementByName("txtT1AMT_CRY_FWD_FL");
      loScalar.setEnabled(false);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1DOC_HLD_FL");
      loScalar.setEnabled(false);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1ACT_FL");
      loScalar.setEnabled(false);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1BYPS_APRV_FL");
      loScalar.setEnabled(false);
      getElementByName("txtT1DOC_VERS_NO").addAttribute("ams_readonly", "true");
      getElementByName("txtT1DOC_PFX").addAttribute("ams_readonly", "true");
   } /* end method disableOtherFields */

   /** enable DOC_HLD_FL and DOC_AM_FL fields
    */
   public void enableOtherFields()
   {
      AMSCheckboxElement loScalar =null;
      loScalar = (AMSCheckboxElement)getElementByName("txtT1AMT_CRY_FWD_FL");
      loScalar.setEnabled(true);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1DOC_HLD_FL");
      loScalar.setEnabled(true);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1ACT_FL");
      loScalar.setEnabled(true);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1BYPS_APRV_FL");
      loScalar.setEnabled(true);
      getElementByName("txtT1DOC_VERS_NO").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName("txtT1DOC_PFX").getHtmlElement().removeAttribute("ams_readonly");
   } /* end method enableOtherFields() */


   /** enable AMT_CRY_FWD_FL, DOC_HLD_FL,ACT_FL, FDT_STRT_DT and FDT_EXPIRE_DT fields
    */
   public void enableFieldsForJVReversal()
   {
      AMSCheckboxElement loScalar =null;
      loScalar = (AMSCheckboxElement)getElementByName("txtT1AMT_CRY_FWD_FL");
      loScalar.setEnabled(true);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1DOC_HLD_FL");
      loScalar.setEnabled(true);
      loScalar = (AMSCheckboxElement)getElementByName("txtT1ACT_FL");
      loScalar.setEnabled(true);
      getElementByName("txtT1FDT_STRT_DT").getHtmlElement().removeAttribute("ams_readonly");
      getElementByName(
         "txtT1FDT_EXPIRE_DT").getHtmlElement().removeAttribute("ams_readonly");
   } /* end method enableFieldsForJVReversal() */


   /** disable save Undo actions
    */
   public void disableSave()
   {
      AMSHyperlinkActionElement loAction =null;
      loAction = (AMSHyperlinkActionElement)getElementByName("T1FDT_WKLDSaveAll");
      loAction.setEnabled(false);
      loAction = (AMSHyperlinkActionElement)getElementByName("T1FDT_WKLDUndoAll");
      loAction.setEnabled(false);
   } /* end method disableSave() */


   /** enable save Undo actions
    */
   public void enableSave()
   {
      AMSHyperlinkActionElement loAction =null;
      loAction = (AMSHyperlinkActionElement)getElementByName("T1FDT_WKLDSaveAll");
      loAction.setEnabled(true);
      loAction = (AMSHyperlinkActionElement)getElementByName("T1FDT_WKLDUndoAll");
      loAction.setEnabled(true);
   } /* end method enableSave() */

   /** method sets the next trigger date for scheduling policy: one time
    *  The value is also saved to the schedule time field of the job row
    *  @param foDate The next trigger date
    *  @return String The String representation of the next trigger
    *                  date
    */
   public String setOneTime(VSDate foDate)
   {
      if(moCurrentDate.onOrBefore(foDate))
      {
         setScheduleDate(foDate);
         // PENDING SET TXT FIELD REP RUN DT TM
         return returnDateFormat(foDate);
      }
      else
      {
          return INVALID_DATE_MSG;
      }
   } /* end method setOneTime(VSDate) */


   /** method sets the next trigger date for scheduling policy: Weekly
    *  The value is also saved to the schedule time field of the job row
    *  @param fiFreqTyp Integer value for type of Scheduling - weekly,Monthly by Day
    *                   or other
    *  @param fiFreq1 First frequency detail per CVL_FDT_FREQ2
    *  @param fiFreq2 Second frequency detail per CVL_FDT_FREQ3
    *  @param foDate The Start date
    *  @param foEndDate The End Date
    *  @return boolean Flag to indicate if the date was calculated
    *                  successfully or not
    */
   public boolean setWeekly(int fiFreqTyp, int fiFreq1, int fiFreq2,
                        VSDate foDate, VSDate foEndDate)
   {
      int liStrtDay =0;
      int liNextDay =0;
      long llRunInterval =0;
      VSDate loNTD = null;  // next Trigger Date

      if (fiFreqTyp != 2)
      {
         raiseException(INVALID_DATE_MSG,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency type 2 and 3 are provided
      if (fiFreq1 < 1 || fiFreq2< 1)
      {
         raiseException(REQ_FREQ_TYP2_AND3,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency values in range CVL_FDT_FREQ2
      if (fiFreq1 > 8)
      {
         raiseException(INVALID_FREQ_TYP2,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency values in range CVL_FDT_FREQ3
      if (fiFreq2 < 67 || fiFreq2 > 73)
      {
         raiseException(INVALID_FREQ_TYP3,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // calculate next trigger date
      liStrtDay  = foDate.getDay();
      liNextDay  = getDayAsVSDayFreq3(fiFreq2);

      if (liStrtDay <=liNextDay)
      {
         loNTD = new VSDate(foDate); //anchor at start date
         loNTD.advanceDays(liNextDay -liStrtDay);
         if(foEndDate !=null)
         {
             if (!loNTD.onOrBefore(foEndDate))
             {
                raiseException(NO_SELECTED_DATE,SEVERITY_LEVEL_ERROR);
                return false;
             }
         }
         setScheduleDate(loNTD);
         return true;
      }
      else
      {
         loNTD = new VSDate(foDate); //anchor at start date
         loNTD.advanceDays((liNextDay+7) -liStrtDay);// following week
         if(foEndDate !=null)
         {
             if (!loNTD.onOrBefore(foEndDate))
             {
                raiseException(NO_SELECTED_DATE,SEVERITY_LEVEL_ERROR);
                return false;
             }
         }
         setScheduleDate(loNTD);
         return true;
      }
   } /* end method setWeekly(VSDate) */

   /** method sets the next trigger date for scheduling policy: monthly by date
    *  The value is also saved to the schedule time field of the job row
    *  @param fiFreqTyp Integer value for type of Scheduling - weekly,Monthly by Day
    *                   or other
    *  @param fiFreq1 First frequency detail per CVL_FDT_FREQ2
    *  @param fiFreq2 Second frequency detail per CVL_FDT_FREQ3
    *  @param foDate The Start date
    *  @param foEndDate The End Date
    *  @return boolean Flag to indicate if the date was calculated
    *                  successfully or not
    */
   public boolean setMonthlyByDate(int fiFreqTyp, int fiFreq1, int fiFreq2,
                        VSDate foDate,VSDate foEndDate)
   {
      int liStrtDate =0;
      int liNextDate =0;
      VSDate loNTD = null;  // next Trigger Date

      if (fiFreqTyp != 4)
      {
         raiseException(INVALID_DATE_MSG, SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency type 2 and 3 are provided
      if (fiFreq1 < 1 || fiFreq2 < 1)
      {
         raiseException(REQ_FREQ_TYP2_AND3,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency values in range CVL_FDT_FREQ2
      if (fiFreq1 < 9 || fiFreq1 > 20)
      {
         raiseException(INVALID_FREQ_TYP2,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency values in range CVL_FDT_FREQ3
      if (fiFreq2 < 36 || fiFreq2 > 66)
      {
         raiseException(INVALID_FREQ_TYP3,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // calculate next trigger date
      liStrtDate  = foDate.getDate();

      //get the next date
      liNextDate  = getDateFromFreq3(fiFreq2,foDate);

      if (liStrtDate <=liNextDate)  //e.g startdate 12th, next trig 18th
      {
         loNTD = new VSDate(foDate);    // begin with Strt date
         loNTD.advanceDays(liNextDate -liStrtDate);
         if(foEndDate !=null)
         {
             if (!loNTD.onOrBefore(foEndDate))
             {
                raiseException(NO_SELECTED_DATE,SEVERITY_LEVEL_ERROR);
                return false;
             }
         }
         setScheduleDate(loNTD);
         return true;
      }
      else // e.g start date 28th, next trig 12th
      {
         loNTD = new VSDate(foDate);
         loNTD.setDate(liNextDate); // roll back to next Date of same month
         loNTD.advanceMonths(1);    // advance to next month
         if(foEndDate !=null)
         {
             if (!loNTD.onOrBefore(foEndDate))
             {
                raiseException(NO_SELECTED_DATE,SEVERITY_LEVEL_ERROR);
                return false;
             }
         }
         setScheduleDate(loNTD);
         return true;
      }
   } /* end method setMonthlyByDate() */

   /** method sets the next trigger date for scheduling policy: monthly by date
    *  The value is also saved to the schedule time field of the job row
    *  @param fiFreqTyp Integer value for type of Scheduling - weekly,Monthly by Day
    *                   or other
    *  @param fiFreq1 First frequency detail per CVL_FDT_FREQ2
    *  @param fiFreq2 Second frequency detail per CVL_FDT_FREQ3
    *  @param foDate The Start date
    *  @param foEndDate  The End Date
    *  @return boolean Flag to indicate if the date was set correctly or not
    */
   public boolean setMonthlyByDay(int fiFreqTyp, int fiFreq1, int fiFreq2,
                        VSDate foStrtDate,VSDate foEndDate)
   {
       int liStrtDate =0;
       int liNextDate =0;
       int liDayOfWeek = getDayFromFreq3(fiFreq2); // VSDate constants 0 Sun; 1 Mon..
       int liWeekOfMonth =getWeekOfMonthFreq3(fiFreq2); //1st,2nd..5th

       // next Trigger Date initialized as startdate
       VSDate loNTD = new VSDate(foStrtDate);

       if (fiFreqTyp != 3)
       {
          raiseException(INVALID_DATE_MSG,SEVERITY_LEVEL_ERROR);
          return false;
       }

      // check if frequency type 2 and 3 are provided
      if (fiFreq1 < 1 || fiFreq2 < 1)
      {
         raiseException(REQ_FREQ_TYP2_AND3,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency values in range CVL_FDT_FREQ2
      if (fiFreq1 < 9 || fiFreq1 > 20)
      {
         raiseException(INVALID_FREQ_TYP2,SEVERITY_LEVEL_ERROR);
         return false;
      }

      // check if frequency values in range CVL_FDT_FREQ3
      if (fiFreq2 > 35)
      {
         raiseException(INVALID_FREQ_TYP3,SEVERITY_LEVEL_ERROR);
         return false;
      }

       while( true )  // loop till search ends
       {
          if(foEndDate !=null)
          {
             if (!loNTD.onOrBefore(foEndDate))
             {
                raiseException(NO_SELECTED_DATE, SEVERITY_LEVEL_ERROR);
                return false;
             }
          }

          VSDate loRetDate = getDayOfMonth(loNTD,liWeekOfMonth,liDayOfWeek);

          if(loRetDate ==null)  // unable to find a VSDate matching condition
          {
             loNTD.advanceMonths(1); // search for a suitable match in next month
             continue;
          }
          else if (loRetDate.before(foStrtDate))
          {
             loNTD.advanceMonths(1);
             continue;
          }
          else
          {
              if(foEndDate !=null)
              {
                 if (!loRetDate.onOrBefore(foEndDate))
                 {
                    raiseException(NO_SELECTED_DATE,SEVERITY_LEVEL_INFO);
                    return false;
                 }
              }

              setScheduleDate(loRetDate);
              return true;
          }
      }  // end while
   } /* end method setMonthlyByDay() */


   /** method return a VSDate for the required parameters like 3rd Tuesday of month
    *  @param foDate  The VSDate Object wrapping the date for which corresponding VSDate sought
    *  @param fiWeekOfMonth  Week of the month for passed date object, 1st,2nd,3rd
    *  @param fiDayOfWeek   Day of the Week Sunday 0, Monday 1..
    *  @return VSDate  Sought date if it exists, null otherwise
    */
   public VSDate getDayOfMonth(VSDate foDate, int fiWeekOfMonth,int fiDayOfWeek)
   {
      int liFirstDay = 0;
      int liNumDaysInMonth = getNumDaysInMonth(foDate);
      int liTargetDate =0;
      // operate on clone - set to 1st of month
      VSDate loDate = new VSDate(foDate);
      loDate.setDate(1);
      liFirstDay = loDate.getDay();

      if (liFirstDay == fiDayOfWeek)
      {
         liTargetDate = 1+ (fiWeekOfMonth-1)*7;
         if (liTargetDate <= liNumDaysInMonth) // valid for the month
         {
            loDate.setDate(liTargetDate);
            return loDate;
         }
         else  // the target date crosses into the following month (minus 7 days)
         {
            loDate.setDate(liTargetDate-7);
            return loDate;
         }
      } // end if (liFirstDay == fiDayOfWeek)
      else if (liFirstDay < fiDayOfWeek)
      {
         liTargetDate = 1 +(fiDayOfWeek-liFirstDay)+ (fiWeekOfMonth-1)*7;
         if (liTargetDate <= liNumDaysInMonth) // valid for the month
         {
            loDate.setDate(liTargetDate);
            return loDate;
         }
         else  // the target date crosses into the following month
         {
            loDate.setDate(liTargetDate-7);
            return loDate;
         }
      } // end else if (liFirstDay < fiDayOfWeek)
      else // liFirstDay > fiDayOfWeek
      {
         liTargetDate = 1 + ((fiDayOfWeek +7)-liFirstDay)+ (fiWeekOfMonth-1)*7;
         if (liTargetDate <= liNumDaysInMonth) // valid for the month
         {
            loDate.setDate(liTargetDate);
            return loDate;
         }
         else  // the target date crosses into the following month
         {
            loDate.setDate(liTargetDate-7);
            return loDate;
         }
      }
   } /* end method getDayOfMonth(VSDate,int,int) */




      /** method returns VSDate in the readable date format
       */
      private String returnDateFormat(VSDate foDate)
      {
         return foDate.toString();
      } /* end method returnDateFormat(VSDate foDate) */


      /** returns the Scheduled run date time field of the current job row
       */
      private VSData getCurrRowField(String fsColumnName)
      {
         try
         {
            return getRootDataSource().getCurrentRow().getData(fsColumnName);
         }
         catch(Exception loExp)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            return null;
         }
      } /* end method getCurrRowField(String fsColumnName)*/


      /** method return day as VSDay 0 Sunday - 6 on Monday
       *  @param fiFreq3 Integer value as per CVL_FDT_FREQ3 values 66-72
       */
      private int getDayAsVSDayFreq3(int fiFreq3)
      {
         if (fiFreq3 == 73)
         {
            return 0; //SUNDAY
         }
         else return (fiFreq3-66);
      }


      /** method return day as VSDay 0 Sunday - 6 on Monday used for Month by Day
       *  @param fiFreq3 Integer value as per CVL_FDT_FREQ3 values 1-35
       */
      private int getDayFromFreq3(int fiFreq3)
      {
         if (fiFreq3 <=5)
         {
            return 1; //MONDAY
         }
         else if (fiFreq3 <=10)
         {
            return 2; //TUESDAY
         }
         else if (fiFreq3 <=15)
         {
            return 3; //WEDNESDAY
         }
         else if (fiFreq3 <=20)
         {
            return 4; //THURSDAY
         }
         else if (fiFreq3 <=25)
         {
            return 5; //FRIDAY
         }
         else if (fiFreq3 <=30)
         {
            return 6; //SATURDAY
         }
         return 0;  //SUNDAY
      } /* end method getDayFromFreq3(int) */



      /** method returns week of month: used for Month by Day scheduling
       *  @param fiFreq3 Integer value as per CVL_FDT_FREQ3 values 1-35
       */
      private int getWeekOfMonthFreq3(int fiFreq3)
      {
         int liWeekOfMonth = fiFreq3%5;  // modulus
         if(liWeekOfMonth==0)  // per CVL_FDT_FREQ3
         {
            return 5;  // fifth week
         }
         else
         {
            return liWeekOfMonth;
         }
      } /* end method getWeekOfMonth(int) */



      /** method return date 1-31
       *  @param fiFreq3 Integer value as per CVL_FDT_FREQ3 values 36-66
       */
      private int getDateFromFreq3(int fiFreq3,VSDate foDate)
      {
         if (fiFreq3 <=63)
         {
            return (fiFreq3-35);
         }
         else if (fiFreq3 ==64) // 1st & 15th
         {
            if ( (foDate.getDate() == 1 ) || (foDate.getDate() > 15) )
            {
                return 1;
            }
            else
            {
                return 15;
            }
         }
         else if (fiFreq3 ==65) // 15th & Last Date
         {
            if (foDate.getDate() <= 15)
            {
                return 15;
            }
            else
            {
                return getNumDaysInMonth(foDate);
            }
         }
         else if (fiFreq3 ==66) // Last Date
         {
            return getNumDaysInMonth(foDate);
         }
         return -1;  //invalid input
      } /* end method getDateAsVSDateFreq3 */


      /** method return the number of days in a month for the VSDate
       *  object passed
       *  @param foDate The VSDate Object that wraps the current month
       *                 0 - Jan : 11- Dec
       *  @return int The number of days in the month
       */
      public int getNumDaysInMonth(VSDate foDate )
      {
         int liMonth = foDate.getMonth();

         if (liMonth ==0 ||
             liMonth ==2 ||
             liMonth ==4 ||
             liMonth ==6 ||
             liMonth ==7 ||
             liMonth ==9 ||
             liMonth ==11 )
         {
            return 31; // months with 31 days
         }
         else if (liMonth ==1) //Feb
         {
            if (isLeapYear(foDate.getYear()))
            {
               return 29; // is a leap year
            }
            else
            {
               return 28;
            }
         }
         return 30; // month with 30 days
      } /* end method getNumDaysInMonth(VSDate) */



      /** method to determine if a year is a leap year or not
       *  @param The yera as an integer value
       *  @return boolean  If the year is leap or not
       */
      public boolean isLeapYear(int fiYear)
      {
         if ((fiYear%4 == 0) && ((fiYear%100 != 0) || (fiYear%400 == 0)))
         {
            return true;
         }
         return false;
      }

      /** method sets the next trigger date in the job row
       *  @param foDate The next Trigger Date to be set
       */
      public void setScheduleDate(VSDate foDate)
      {
         getCurrRowField(ATTR_DOC_STRT_DT).setVSDate(foDate);
      } /* end method setScheduleDate(VSDate) */



  /** returns boolean if a set of parameters
   * exist for the job at the in the job parameter table
   *
   * @param flAgentID the unique job id
   */
   public boolean doParamsExist(long flAgentID)
   {
      boolean lbFlag =true;
      VSResultSet loRS =null;
      try
      {
         VSSession loCurrentSession = getParentApp().getSession();
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("AGENT_ID =" +flAgentID);
                VSQuery loQuery = new VSQuery(loCurrentSession,"BS_AGENT_PARM",searchReq,null);
         loRS =loQuery.execute();
         VSRow loRow = loRS.first();
         if (loRow ==null)
         {
            lbFlag = false;
         }
      } // end try
      catch(Throwable loExcp)
      {
              lbFlag =true;
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      }
      finally
      {
         if(loRS !=null)
         {
            loRS.close();
         }
      }
      return lbFlag ;
   } /* end  doParamsExist */




      /* method returns VSDate object with locality at todays Date and time to 12:00 am */
      private VSDate getTodaysDate()
      {
         VSDate loDate = new VSDate();
         loDate.setHours(0);
         loDate.setMinutes(0);
         loDate.setSeconds(0);
         return loDate;
      } /* end method getTodaysDate() */



   /* method to format VSDate as MM-dd-yyyy, the format used for all dates on the
    *  current page
    */
   private static String getDateAsDefaultString(VSDate foDate)
   {
      StringBuffer loDtBuf = new StringBuffer();
                //Jan 0 , Dec 11
      int liMonth = foDate.getMonth() +1 ;
         int liDate =foDate.getDate();
                   if (liMonth < 10)
          {
             loDtBuf.append("0"+liMonth);
          }
          else
          {
             loDtBuf.append("" + liMonth);
          }
                if (liDate < 10)
          {
             loDtBuf.append("-").append("0"+liDate);
          }
          else
          {
             loDtBuf.append("-").append(liDate);
          }
                       loDtBuf.append("-").append(""+(foDate.getYear()+ 1900));
                 return loDtBuf.toString();
   } /* end method getDateAsDefaultString(VSDate) */

   /* debug method */
   private void db(String msg)
   {
      if(AMS_PLS_DEBUG)
      {
         System.err.println(msg);
      }
   }


   /* debug method */
   private void printAllBoundElements(Enumeration e)
   {
      while(e.hasMoreElements())
      {
         BoundElement loElement =(BoundElement)e.nextElement();
         db("");
         db("***");
         db("Found " + loElement.getName());
         db("Java Class: " + loElement.getClass().getName());
         db("");
         db("***");
      }
   }


      /** method sets the current document identifier fields
       *  @param fsDocCd  The document code
       *  @param fsDocDeptCd  The document department code
       *  @param fsDocId  The document Id
       *  @param fiDocVersNo  The document version number
       */
      public void setDocumentIdentifier(String fsDocCd, String fsDocDeptCd,
                                        String fsDocId, int fiDocVersNo)
      {
         msDocCd     = fsDocCd;
         msDocDeptCd = fsDocDeptCd;
         msDocId     = fsDocId;
         miDocVersNo = fiDocVersNo;
      }

     /* method to remove list of labels (Strings in Vector) from comboBox element */
     private void removeFromFrequencyList(Vector foList, AMSComboBoxElement foElement)
     {

        db("Removing from " + foElement.getName());
        for (Enumeration loList = foList.elements(); loList.hasMoreElements() ;)
        {
           String lsStr = (String)loList.nextElement();
           foElement.removeElement(lsStr);
        }
        foList.removeAllElements();

     }

   private void disableAllFields()
   {
      disableDocFields();
      disableOtherFields();
      disableFreqFields();
   }


   public boolean isDocDiscarded()
   {
      boolean lbool = true;
      VSResultSet loRS =null;
      VSRow loRow = null;
      try
      {
         VSRow loFDTRow = getRootDataSource().getCurrentRow();
         if (loFDTRow != null)
         {
         String lsDocId = loFDTRow.getData("DOC_ID").getString();
         String lsDocCd = loFDTRow.getData("DOC_CD").getString();
         String lsDocDeptCd = loFDTRow.getData("DOC_DEPT_CD").getString();
         String lsDocVersNo = loFDTRow.getData("DOC_VERS_NO").getString();
         VSSession loCurrentSession = getParentApp().getSession();

         SearchRequest lsrDocHdr = new SearchRequest();
         lsrDocHdr.addParameter("DOC_HDR","DOC_ID",lsDocId);
         lsrDocHdr.addParameter("DOC_HDR","DOC_CD",lsDocCd);
         lsrDocHdr.addParameter("DOC_HDR","DOC_DEPT_CD",lsDocDeptCd);
         lsrDocHdr.addParameter("DOC_HDR","DOC_VERS_NO",lsDocVersNo);
         VSQuery loQuery = new VSQuery(loCurrentSession,"DOC_HDR",lsrDocHdr,null);
         loRS =loQuery.execute();
         loRow = loRS.first();
         if (loRow ==null)
         {
            lbool = true;
         }

         else
         {
            lbool = false;
         }

         if(loRS !=null)
         {
            loRS.close();
         }
      }//End of if (loFDTRow != null)
      return lbool ;
      } // end try
      catch(Throwable loExcp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

         if(loRS !=null)
         {
            loRS.close();
         }
         return false ;
      }
      finally
      {
         if(loRS !=null)
         {
            loRS.close();
         }
      }
   }

}
