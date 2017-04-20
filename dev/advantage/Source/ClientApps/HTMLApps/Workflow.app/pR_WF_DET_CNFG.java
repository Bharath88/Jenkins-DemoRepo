//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import advantage.AMSStringUtil;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;

import org.apache.commons.logging.Log;

/*
**  pR_WF_DET_CNFG
*/

//{{FORM_CLASS_DECL
public class pR_WF_DET_CNFG extends pR_WF_DET_CNFGBase

//END_FORM_CLASS_DECL}}
{

   private static final String COPY_DOC_CD = "COPY_DOC_CD";
   private static Log moLog = AMSLogger.getLog( pR_WF_DET_CNFG.class,
         AMSLogConstants.FUNC_AREA_WORKFLOW ) ;

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pR_WF_DET_CNFG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T3R_WF_XML_DATA_BeforePickQuery
void T3R_WF_XML_DATA_BeforePickQuery(Pick obj, DataSource foDataSource, VSOutParam foWhrClause, VSOutParam orderBy, VSOutParam cancel)
{
	//Write Event Code below this line

   /*
    * Called before the Pick page query is executed.
    *
    * Before the Pick Grid is displayed, remove any where clause for attribute DOC_FLD.
    * If it is present in the query then only one record will be shown in the Pick Grid.
    */
   String lsSQL = foWhrClause.stringValue();
   int liIndex = lsSQL.indexOf("AND DOC_FLD");
   if(liIndex != -1)
   {
      lsSQL = lsSQL.substring(0, liIndex) + ")";
   }
   foWhrClause.setValue(lsSQL);
}
//END_EVENT_T3R_WF_XML_DATA_BeforePickQuery}}
//{{EVENT_T3R_WF_XML_DATA_BeforePickFill
void T3R_WF_XML_DATA_BeforePickFill(Pick obj, DataSource foDataSource, VSRow foPickRow,VSOutParam cancel)
{
	//Write Event Code below this line

   /**
    * Called whenever a row from the Pick Grid is selected.
    *
    * The Field Caption is by default inherited based on the Document Field.
    * However, the Field Caption value can also be manually set.
    * In case the value is manually set, it stays and is not replaced.
    * Even if a new Document Field value is selected from the Pick, the new Field Caption
    * is not selected because the old Manual value takes precedence.
    *
    * Hence, in this method whenever a Document Field is selected from the Pick, the
    * corresponding Field Caption is explicitly populated onto the Field Caption.
    */
   VSRow loRow = foDataSource.getCurrentRow();
   if(loRow != null && foPickRow != null)
   {
      loRow.getData("DOC_FLD_NM").setData(foPickRow.getData("DOC_FLD_NM"));
   }
}
//END_EVENT_T3R_WF_XML_DATA_BeforePickFill}}
//{{EVENT_pR_WF_DET_CNFG_beforeActionPerformed
void pR_WF_DET_CNFG_beforeActionPerformed( ActionElement foAction, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line

   /**
    * The Configure Worklist Details page is available at the granular level of Document Code.
    * For some Documents of same Document Type, the same configuration might be needed.
    * Hence the user can configure the Worklist Details for 1 Document Code and then copy the
    * configuration to another Document COde of same Document Type.
    *
    * Since the configuration of a Document involves more than 1 row, the default Copy/ Paste
    * will not work.
    * Hence Custom Actions of "Copy Config" and "Paste Config" have been added.
    *
    * Whenever a "Copy Config" action is performed, the Document Code is stored in the session.
    * Whenever a "Paste Config" action is performed, fetch the source Document Code from the
    * session and copy its configuration to the current Document record.
    */

   VSRow loRow = T1R_DOC_CD.getCurrentRow();
   if(loRow == null)
   {
      return;
   }

   String lsDestDocCd = loRow.getData("DOC_CD").getString();
   if(AMSStringUtil.strIsEmpty(lsDestDocCd))
   {
      return;
   }

   try
   {
      if(AMSStringUtil.strInsensitiveEqual(foAction.getAction(), "CopyConfig"))
      {
         parentApp.getSession().getORBSession().setProperty(COPY_DOC_CD, lsDestDocCd);
      }
      if(AMSStringUtil.strInsensitiveEqual(foAction.getAction(), "PasteConfig"))
      {
         String lsSrcDocCd = parentApp.getSession().getORBSession().getProperty(COPY_DOC_CD);
         if(AMSStringUtil.strIsEmpty(lsSrcDocCd))
         {
            raiseException("No record to be pasted", SEVERITY_LEVEL_ERROR);
            return;
         }
         copyConfig(lsSrcDocCd, lsDestDocCd);
         refreshDataSource(T2R_WF_DET_CNFG);
      }
      if(AMSStringUtil.strInsensitiveEqual(foAction.getAction(), "delete"))
      {

         // Call function to check if the Worklist Details Configuration row has to deleted or not.
         if(!deleteConfig())
         {
            // Worklist Details Configuration row has to be retained to be later
		    // processed and deleted by the batch job so cancel the event for 'Delete' action.
		    evt.setCancel(true);
            evt.setNewPage(this);

         }
      }
   }
   catch (Exception loExcp)
   {
      // Add exception log to logger object
      moLog.error("Unexpected error encountered while processing. ", loExcp);

   }
}
//END_EVENT_pR_WF_DET_CNFG_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T3R_WF_XML_DATA.addPickListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T3R_WF_XML_DATA) {
			T3R_WF_XML_DATA_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_DET_CNFG_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void BeforePickFill(Pick obj, DataSource receiverDataSource, VSRow supplierRow,VSOutParam cancel){
		Object source = obj;
		if (source == T3R_WF_XML_DATA) {
			T3R_WF_XML_DATA_BeforePickFill(obj, receiverDataSource, supplierRow, cancel);
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
	 * Copies the Worklist Details configuration from one Document to another.
	 *
	 * The Configure Worklist Details page is available at the granular level of Document Code.
	 * For some Documents of same Document Type, the same configuration might be needed.
	 * Hence the user can configure the Worklist Details for 1 Document Code and then copy the
	 * configuration to another Document COde of same Document Type.
	 *
	 * Since the configuration of a Document involves more than 1 row, the default Copy/ Paste
	 * will not work.
	 * Hence Custom Actions of "Copy Config" and "Paste Config" have been added.
	 *
	 * Whenever a "Paste Config" action is performed, this method is called which gets the
	 * source Document Code from the session and copies it's configuration to the current
	 * Document record.
	 *
	 * @param fsSrcDocCd Source Document Code whose configuration is to be copied.
	 * @param fsDestDocCd Destination Document code to which the configuration is to be added.
	 */
	private void copyConfig(String fsSrcDocCd, String fsDestDocCd)
	{
	   VSResultSet loSourceRS = null;
	   VSResultSet loDestRS = null;
	   AMSDataSource loDataSource = null;

	   try
	   {
	      SearchRequest lsrSheet = new SearchRequest();
	      lsrSheet.add("DOC_CD='" + AMSSQLUtil.getANSIQuotedStr(fsSrcDocCd) + "'");

	      VSQuery loQuery = new VSQuery(getParentApp().getSession(), "R_WF_DET_CNFG", lsrSheet, null);
	      loSourceRS = loQuery.execute();
	      VSRow loSrcRow = loSourceRS.first();

	      loDataSource = new AMSDataSource();
	      loDataSource.setName("R_WF_DET_CNFG");
	      loDataSource.setSession( parentApp.getSession() );
	      loDataSource.setQueryInfo("R_WF_DET_CNFG", "", "", "", false);
	      loDataSource.setPage(this);
	      loDataSource.setAllowInsert(true);
	      loDataSource.setAllowDelete(true);
	      loDataSource.setAllowUpdate(true);
	      loDataSource.setSaveMode(DataSource.SAVE_BUFFERED);
	      loDataSource.executeQuery();

	      loDestRS = loDataSource.getResultSet();

	      /*
	       * Iterate through all
	       *
	       */
	      while ( loSrcRow != null )
	      {
	         // If no Field found for current row, it means no configuration and hence continue.
	         String lsColCode = loSrcRow.getData("DOC_FLD").getString();
	         if(AMSStringUtil.strIsEmpty(lsColCode))
	         {
	            loSrcRow = loSourceRS.next();
	            continue;
	         }

	         VSRow loDestRow = loDestRS.insert();
	         loDestRow.getData("DOC_CD").setString(fsDestDocCd);
	         loDestRow.getData("ROW_NO").setData(loSrcRow.getData("ROW_NO"));
	         loDestRow.getData("DOC_COMP").setData(loSrcRow.getData("DOC_COMP"));
	         loDestRow.getData("DOC_FLD").setData(loSrcRow.getData("DOC_FLD"));
	         loDestRow.getData("DOC_FLD_NM").setData(loSrcRow.getData("DOC_FLD_NM"));
	         loDestRow.getData("DATA_TYP").setData(loSrcRow.getData("DATA_TYP"));
	         loDestRow.getData("DIRTY_FL").setBoolean(true);
	         loDestRow.save();

	         loSrcRow = loSourceRS.next();
	      }
	      loDataSource.updateDataSource();
	   }
	   catch (Exception leExcp)
	   {
          // Add exception log to logger object
          moLog.error("Unexpected error encountered while processing. ", leExcp);

	   }
	   finally
	   {
	      if(loSourceRS!=null)
	      {
	         loSourceRS.close();
	      }
	      if(loDestRS!=null)
	      {
	         loDestRS.close();
	      }
	      if(loDataSource!=null)
	      {
	         loDataSource.close();
	      }
	   }
	}

	/**
	 * Gets the Current Worklist Details Configuration and checks for the values in Old
	 * Document Component and Old Document Field.
	 * <ul>
	 *  <li> If values are not null, blanks out the Document Component, Document Field,
	 *       Document Field and Data Type fields and retains the record to be later processed
       *       and deleted by batch job.
	 *  <li> If values are null, then deletes the record as there is no processing required to
       *       be done here by the batch job.
	 * </ul>
	 */
	private boolean deleteConfig()
	{
	   // Get the current WorkList Details Configuration Row
	   VSRow loDelConfigRow = T2R_WF_DET_CNFG.getCurrentRow();

	   // Check if Old Document Component and Old Document Field values are not null

	   String lsOldDocComp = loDelConfigRow.getData("OLD_DOC_COMP").getString();
	   String lsOldDocFld = loDelConfigRow.getData("OLD_DOC_FLD").getString();

	   // Case where Old Document Component and Old Document Field values are not null
	   // and the record has to be retained.
	   if( !(AMSStringUtil.strIsEmpty(lsOldDocComp ) ) &&
	       !(AMSStringUtil.strIsEmpty(lsOldDocFld) ) )
	   {
	      // Set Document Component and Document Field values null and save the records to be
	      // later processed and deleted by batch job.

	      loDelConfigRow.getData("DOC_COMP").setString(null);
	      loDelConfigRow.getData("DOC_FLD").setString(null);
	      loDelConfigRow.getData("DOC_FLD_NM").setString(null);
		  T2R_WF_DET_CNFG.updateDataSource();
	      return false;
 	  }

	 // Case where Old Document Component and Old Document Field values are null
	 // and the record has to be deleted.
	  return true;
	}

}