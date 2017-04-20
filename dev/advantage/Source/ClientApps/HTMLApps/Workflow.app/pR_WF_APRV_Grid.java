//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

	import com.amsinc.gems.adv.vfc.html.*;
	import com.amsinc.gems.adv.common.*;

	import java.util.*;

	import advantage.*;
	import com.amsinc.gems.adv.client.dbitem.*;
import org.apache.commons.logging.Log;
import advantage.AMSStringUtil;
	/*
	**  pR_WF_APRV_Grid
	*/

	//{{FORM_CLASS_DECL
	public class pR_WF_APRV_Grid extends pR_WF_APRV_GridBase
	
	//END_FORM_CLASS_DECL}}
	{

            private static Log moLog = AMSLogger.getLog( pR_WF_APRV_Grid.class,
                 AMSLogConstants.FUNC_AREA_WORKFLOW ) ;
	   /**
	   * Flag for identifying 'Paste Action'
	   */
	   private boolean mboolPasteActnFlg = false;

	   /*
	   * value of seq_no for approval levels 11 -20
	   */
	   public  final static String APR_LEVL_SEQ_NO_2 = "2";

	   /**
	    * Suffix to identify columns for the second record
	    */
	   public final static String ARPV_LVL_SEQ_2_SUFFIX = "_APRV_SEQ_2";


	   /**
	   * The left hand side and condition column name - without the numeral 1-5
	   */
	   public final static String AND_COND_LHS = "AND_COND_LHS_";

	   /**
	   * The right hand side and condition column name - without the numeral 1-5
	   */
	   public final static String AND_COND_RHS = "AND_COND_RHS_";

	   /**
	   * The operator column name - without the numeral 1-5
	   */
	   public final static String AND_COND_OPR = "AND_COND_OPR_";

	   /**
	   * The operator display column name
	   */
	   public final static String APRV_OPR_DISP_COL = "APRV_OPR_DISP";

	   /**
	   * The operator coded values list table name
	   */
	   public final static String CVL_WF_OPRS = "CVL_WF_OPRS";

	   /**
	   * The operator number column name
	   */
	   public final static String APRV_OPR_NO_COL = "APRV_OPR_NO";

	   /**
	   * The condition query name
	   */
	   public final static String R_WF_APRV_COND = "R_WF_APRV_COND";

	   /**
	   * The condition id column name
	   */
	   public final static String COND_ID_COL = "COND_ID";

	   /**
	   * The or condition column name, without the x and y
	   */
	   public final static String OR_COND_COL = "OR_COND_";

	   /**
	   * The or condition column name, for the first condition for each approval
	   * level, without the approval level numeral.
	   */
	   public final static String OR_COND_1_COL = "OR_COND_1_";

	   /**
	   * main name for the ordinal number / routing sequence
	   */
	   private static final String ORD_NO_COL = "ORD_NO_";

	   // Declarations for instance variables used in the form
	   private String msDocCd;
	   private String msBrnCd;
	   private String msCabCd;
	   private String msDeptCd;
	   private String msDivCd;
	   private String msGpCd;
	   private String msSectCd;
	   private String msDstcCd;
	   private String msBurCd;
	   private String msUnitCd;

      //BGN ADVHR00037821
      //Page First load Flag  - Use when first time page is loaded.
      private boolean mbPageFirstLoadFlg = true;

      //Old Document Code.
      private String msOldDocCd = null;
      /* Constant indicating the header component suffix*/
      private static final String HDR_SUFFIX= "_DOC_HDR";

      //Constant for Data Size of document header fields
      private static final int C_DATA_SIZE = 16;

      //Document Code key used when 2 rows with same code are entered.
      private String msOldDocKey = null;
      //END ADVHR00037821


	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pR_WF_APRV_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_T2pR_WF_APRV_beforePageNavigation
void T2pR_WF_APRV_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line

   VSRow       loCurrApLvlRow;
   String      lsSeqNo = APR_LEVL_SEQ_NO_2;
   SearchRequest loSearchReq ;
   long     llSeqNo ;
   Parameter   loParam ;
   String      lsParamValue;
   VSQuery  loSeqNoQry ;
   VSResultSet loQryRs;
   DataSource  loDataSource;
   VSRow       loNewRow;
   VSData      loColData;
   VSData      loNewColData;
   String      lsColStr;
   VSData      loHashColData = null;
   Hashtable   lhCopyRow ;
   AMSStartupPage loStartup = null;
   PLSORBSessionImpl loOrbSession = null;
   String lsKeyName = null;
   String lsHashColStr = null;

   //save the values for approval level 1-10
   getRootDataSource().updateDataSource();

   VSSession lsSession = getParentApp().getSession();

   //get the current value of primary key
   VSRow loCurrentRow = getRootDataSource().getCurrentRow();

   if (loCurrentRow != null)
   {
      String lsDocCd    = loCurrentRow.getData("DOC_CD").getString();
      String lsBrnCd    = loCurrentRow.getData("GOVT_BRN_CD").getString();
      String lsCabCd    = loCurrentRow.getData("CAB_CD").getString();
      String lsDeptCd   = loCurrentRow.getData("DEPT_CD").getString();
      String lsDivCd    = loCurrentRow.getData("DIV_CD").getString();
      String lsGpCd     = loCurrentRow.getData("GP_CD").getString();
      String lsSectCd   = loCurrentRow.getData("SECT_CD").getString();
      String lsDstcCd   = loCurrentRow.getData("DSTC_CD").getString();
      String lsBurCd    = loCurrentRow.getData("BUR_CD").getString();
      String lsUnitCd   = loCurrentRow.getData("UNIT_CD").getString();

      //search for data for approval levels 11 -20 with the primary key from approval level 1-10 screen
      loSearchReq = new SearchRequest();
      llSeqNo = 2L;
      lsParamValue =  Long.toString(llSeqNo);
      loParam = new Parameter("R_WF_APRV","SEQ_NO",lsParamValue);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","DOC_CD",lsDocCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","GOVT_BRN_CD",lsBrnCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","CAB_CD",lsCabCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","DEPT_CD",lsDeptCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","DIV_CD",lsDivCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","GP_CD",lsGpCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","SECT_CD",lsSectCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","DSTC_CD",lsDstcCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","BUR_CD",lsBurCd);
      loSearchReq .add( loParam );

      loParam = new Parameter("R_WF_APRV","UNIT_CD",lsUnitCd);
      loSearchReq .add( loParam );


      loSeqNoQry = new VSQuery(lsSession,"R_WF_APRV", loSearchReq ,null  );
      // execute query into resultset
      loQryRs  = loSeqNoQry.execute();

      // if record with seq_no is not found , insert a new record for seq_no =2
      if ((loCurrApLvlRow = loQryRs.next()) == null)
      {
         loDataSource = getRootDataSource();
         loDataSource.insert();

         loNewRow = loDataSource.getCurrentRow();

         loNewRow.getData("DOC_CD").setString(lsDocCd);
         loNewRow.getData("GOVT_BRN_CD").setString(lsBrnCd);
         loNewRow.getData("CAB_CD").setString(lsCabCd);
         loNewRow.getData("DEPT_CD").setString(lsDeptCd);
         loNewRow.getData("DIV_CD").setString(lsDivCd);
         loNewRow.getData("GP_CD").setString(lsGpCd);
         loNewRow.getData("SECT_CD").setString(lsSectCd);
         loNewRow.getData("DSTC_CD").setString(lsDstcCd);
         loNewRow.getData("BUR_CD").setString(lsBurCd);
         loNewRow.getData("UNIT_CD").setString(lsUnitCd);
         loNewRow.getData("SEQ_NO").setString(lsSeqNo);
         loDataSource.updateDataSource();
      }

      if (loQryRs != null)
      {
         loQryRs.close();
      } /* end if (loQryRS != null */

      try
      {
          StringBuffer lsbWhereClause = new StringBuffer(100) ;
          lsbWhereClause.append( "DOC_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsDocCd,true) ) ;
          lsbWhereClause.append( " AND GOVT_BRN_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsBrnCd,true) ) ;
          lsbWhereClause.append( " AND CAB_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsCabCd,true) ) ;
          lsbWhereClause.append( " AND DEPT_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsDeptCd,true) ) ;
          lsbWhereClause.append( " AND DIV_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsDivCd,true) ) ;
          lsbWhereClause.append( " AND GP_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsGpCd,true) ) ;
          lsbWhereClause.append( " AND SECT_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsSectCd,true) ) ;
          lsbWhereClause.append( " AND DSTC_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsDstcCd,true) ) ;
          lsbWhereClause.append( " AND BUR_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsBurCd,true) ) ;
          lsbWhereClause.append( " AND UNIT_CD = " ) ;
          lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsUnitCd,true) ) ;
          lsbWhereClause.append( " AND SEQ_NO = " ) ;
          lsbWhereClause.append( lsSeqNo ) ;

          nav.setDevWhere(lsbWhereClause.toString());
      }
      catch (Exception loEcxcp)
      {
         return;
      }

   }

}
//END_EVENT_T2pR_WF_APRV_beforePageNavigation}}
//{{EVENT_pR_WF_APRV_Grid_requestReceived
void pR_WF_APRV_Grid_requestReceived( PLSRequest req, PageEvent evt )
{
   //Write Event Code below this line
}
//END_EVENT_pR_WF_APRV_Grid_requestReceived}}
//{{EVENT_pR_WF_APRV_Grid_afterActionPerformed
void pR_WF_APRV_Grid_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line

   VSRow          loCurrApLvlRow = null;
   AMSStartupPage loStartup = null;
   Hashtable      lhCopyRow;
   PLSORBSessionImpl loOrbSession;
   int liAction =0;

   String lsAction = ae.getAction();
   VSRow loCurrentRow = getRootDataSource().getCurrentRow();
   VSSession loSession = getParentApp().getSession();

   try
   {
      liAction = Integer.valueOf( lsAction ).intValue() ;
   }
   catch ( NumberFormatException foNumException )
   {
   }

   //After COPY action
   switch ( liAction )
   {
      case AMSHyperlinkActionElement.COPY_RECORD:
      {
         String      lsSeqNo = APR_LEVL_SEQ_NO_2;
         VSData      loNewColData;
         String      lsKeyName;
         VSData      loColData;

         if (loCurrentRow == null)
         {
            break;
         }
         //copy the values for approval level 11-20 in a hash table
         //this table will be used to paste the values when the approval level 11-20 screen is displayed
         //after the paste action is performed

         String lsDocCd  = loCurrentRow.getData("DOC_CD").getString();
         String lsBrnCd  = loCurrentRow.getData("GOVT_BRN_CD").getString();
         String lsCabCd  = loCurrentRow.getData("CAB_CD").getString();
         String lsDeptCd = loCurrentRow.getData("DEPT_CD").getString();
         String lsDivCd  = loCurrentRow.getData("DIV_CD").getString();
         String lsGpCd   = loCurrentRow.getData("GP_CD").getString();
         String lsSectCd = loCurrentRow.getData("SECT_CD").getString();
         String lsDstcCd = loCurrentRow.getData("DSTC_CD").getString();
         String lsBurCd  = loCurrentRow.getData("BUR_CD").getString();
         String lsUnitCd = loCurrentRow.getData("UNIT_CD").getString();

         //search for corresponding record with seq_no =2 in the database
         SearchRequest loSearchReq  = new SearchRequest();

         long llSeqNo = 2L;
         String lsParamValue = Long.toString(llSeqNo);
         Parameter param = new Parameter("R_WF_APRV","SEQ_NO",lsParamValue);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","DOC_CD",lsDocCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","GOVT_BRN_CD",lsBrnCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","CAB_CD",lsCabCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","DEPT_CD",lsDeptCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","DIV_CD",lsDivCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","GP_CD",lsGpCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","SECT_CD",lsSectCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","DSTC_CD",lsDstcCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","BUR_CD",lsBurCd);
         loSearchReq.add( param );

         param = new Parameter("R_WF_APRV","UNIT_CD",lsUnitCd);
         loSearchReq.add( param );

         VSQuery loSeqNoQry = new VSQuery(loSession,"R_WF_APRV", loSearchReq,null  );

         // execute query into resultset
         VSResultSet loQryRs = loSeqNoQry.execute();
         VSRow loTmpRow = loQryRs.last();

         loCurrentRow = null;

         loStartup = AMSStartupPage.getStartupPage( getParentApp() ) ;
         lhCopyRow = loStartup.getCopyRow();

         //if the record for approval level 11-20 is found in the database
         //copy the values in hash table with key name = column name suffixed by ARPV_LVL_SEQ_2_SUFFIX
         if(loQryRs != null)
         {
            loCurrentRow = loQryRs.getRowAt( 1 ) ;

            for ( int liColumnCtr = 1;
                  liColumnCtr <= loCurrentRow.getColumnCount();
                  liColumnCtr++ )
            {
                // get the value for the selected column from the resultset
                loColData = loCurrentRow.getData(liColumnCtr);
                //key name for storing values in hash table = column name suffixed by ARPV_LVL_SEQ_2_SUFFIX
                lsKeyName = loColData.getName() + ARPV_LVL_SEQ_2_SUFFIX;

                // add the key name & it's value to hash table
                lhCopyRow.put(lsKeyName, loColData.getString());
            }

            // update the row hashtable for the user
            loStartup.setCopyRow(lhCopyRow);

            //this flag indicates that the last action performed by the user was 'paste'
            mboolPasteActnFlg = false;

            if (loQryRs != null)
            {
               loQryRs.close();
            } /* end if (loQryRs != null) */

         }

         break;

      }
      //After Paste Action PASTE_ACTN
      case AMSHyperlinkActionElement.PASTE_RECORD:

      {
         //set the flag to indicate that the last action performed was paste
         mboolPasteActnFlg = true;
         break;
      }

      default:
      break;
   }

   //After 'delete' Action DELETE_ACTN
   if (lsAction == ActionElement.db_delete)
   {
      //re-set the paste flag
      mboolPasteActnFlg = false;
   }

   //After 'insert' Action INSERT_ACTN
   if (lsAction == ActionElement.db_insert)
   {
      //re-set the paste flag
      mboolPasteActnFlg = false;
   }

}
//END_EVENT_pR_WF_APRV_Grid_afterActionPerformed}}
//{{EVENT_pR_WF_APRV_Grid_beforeActionPerformed
void pR_WF_APRV_Grid_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
// Write Event Code below this line
   String lsAction = ae.getAction();

   if ( lsAction != null )
   {
      if ( lsAction.equals( String.valueOf( AMSHyperlinkActionElement.APRV_FLD_COMBOBOX ) ) )
      {
         //The following line will flush the pending changes to the result set.
         acceptData(preq);
         evt.setNewPage(this);
         evt.setCancel(true);
      }
   }

}
//END_EVENT_pR_WF_APRV_Grid_beforeActionPerformed}}
//{{EVENT_T1R_WF_APRV_afterSave
void T1R_WF_APRV_afterSave(VSRow foRow )
{
   //Write Event Code below this line

   VSRow       loCurrApLvlRow;
   String      lsSeqNo = APR_LEVL_SEQ_NO_2;
   VSSession loSession = getParentApp().getSession();

   /*
    * If severity level is greater than error, return.
    * as the second row in this case should not be added
    */
   if ( getHighestSeverityLevel() >= SEVERITY_LEVEL_ERROR )
   {
      return;
   } /* end if ( getHighestSeverityLevel() >= SEVERITY_LEVEL_ERROR ) */

   SearchRequest loSearchReq = new SearchRequest();

   long llSeqNo = 2L;
   String lsParamValue = Long.toString(llSeqNo);
   Parameter loParam = new Parameter("R_WF_APRV","SEQ_NO",lsParamValue);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","DOC_CD",msDocCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","GOVT_BRN_CD",msBrnCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","CAB_CD",msCabCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","DEPT_CD",msDeptCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","DIV_CD",msDivCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","GP_CD",msGpCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","SECT_CD",msSectCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","DSTC_CD",msDstcCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","BUR_CD",msBurCd);
   loSearchReq.add( loParam );

   loParam = new Parameter("R_WF_APRV","UNIT_CD",msUnitCd);
   loSearchReq.add( loParam );

   VSQuery loSeqNoQry = new VSQuery(loSession,"R_WF_APRV", loSearchReq,null  );
   // execute query into resultset
   VSResultSet loQryRs  = loSeqNoQry.execute();

   VSRow loRow = loQryRs.last();

   int liRowCnt = loQryRs.getRowCount();

   // if record with seq_no is found , just update the record for seq_no =2
   if(liRowCnt > 0)
   {
      loRow.getData("DOC_CD").setString(foRow.getData("DOC_CD").getString());
      loRow.getData("GOVT_BRN_CD").setString(foRow.getData("GOVT_BRN_CD").getString());
      loRow.getData("CAB_CD").setString(foRow.getData("CAB_CD").getString());
      loRow.getData("DEPT_CD").setString(foRow.getData("DEPT_CD").getString());
      loRow.getData("DIV_CD").setString(foRow.getData("DIV_CD").getString());
      loRow.getData("GP_CD").setString(foRow.getData("GP_CD").getString());
      loRow.getData("SECT_CD").setString(foRow.getData("SECT_CD").getString());
      loRow.getData("DSTC_CD").setString(foRow.getData("DSTC_CD").getString());
      loRow.getData("BUR_CD").setString(foRow.getData("BUR_CD").getString());
      loRow.getData("UNIT_CD").setString(foRow.getData("UNIT_CD").getString());
      loRow.getData("SLF_APRV_RSCT").setString(foRow.getData("SLF_APRV_RSCT").getString());
      loRow.getData("SEQ_NO").setString(lsSeqNo);
      loRow.getData("EFBGN_DT").setVSDate(foRow.getData("EFBGN_DT").getVSDate());
      loRow.getData("EFEND_DT").setVSDate(foRow.getData("EFEND_DT").getVSDate());
   }
   // if record with seq_no is not found , insert a new record for seq_no =2
   else if(liRowCnt <= 0)
   {
      VSRow loNewRow = loQryRs.insert();

      loNewRow.getData("DOC_CD").setString(foRow.getData("DOC_CD").getString());
      loNewRow.getData("GOVT_BRN_CD").setString(foRow.getData("GOVT_BRN_CD").getString());
      loNewRow.getData("CAB_CD").setString(foRow.getData("CAB_CD").getString());
      loNewRow.getData("DEPT_CD").setString(foRow.getData("DEPT_CD").getString());
      loNewRow.getData("DIV_CD").setString(foRow.getData("DIV_CD").getString());
      loNewRow.getData("GP_CD").setString(foRow.getData("GP_CD").getString());
      loNewRow.getData("SECT_CD").setString(foRow.getData("SECT_CD").getString());
      loNewRow.getData("DSTC_CD").setString(foRow.getData("DSTC_CD").getString());
      loNewRow.getData("BUR_CD").setString(foRow.getData("BUR_CD").getString());
      loNewRow.getData("UNIT_CD").setString(foRow.getData("UNIT_CD").getString());
      loNewRow.getData("SLF_APRV_RSCT").setString(foRow.getData("SLF_APRV_RSCT").getString());
      loNewRow.getData("SEQ_NO").setString(lsSeqNo);
      loNewRow.getData("EFBGN_DT").setVSDate(foRow.getData("EFBGN_DT").getVSDate());
      loNewRow.getData("EFEND_DT").setVSDate(foRow.getData("EFEND_DT").getVSDate());
   }

   //update the record in the database
   loQryRs.updateDataSource();

   if (loQryRs != null)
   {
      loQryRs.close();
   } /* end if (loQryRs != null) */
   AMSDataSource loRootDS = (AMSDataSource)getRootDataSource();

   //get current row's key information.
   //this would be used later on to reposition the cursor after data source is refreshed
   String lsRepositionWhere = getRepositionWhere(loRootDS);

   //execute query again to refresh the data (including inferred fields)
   loRootDS.executeQuery();

   //reset cursor position
   //after datasource is refreshed, cursor position will always be at the beginning
   //thus, reset it to previous position
   resetCursorPosition(loRootDS, lsRepositionWhere);
}
//END_EVENT_T1R_WF_APRV_afterSave}}
//{{EVENT_T1R_WF_APRV_beforeSave
void T1R_WF_APRV_beforeSave(VSRow row ,VSOutParam cancel )
{
   //Write Event Code below this line
   // Get the primary key values for the row if already exists
   VSRow       loCurrentRow = row;
   msDocCd  = (String) loCurrentRow.getData("DOC_CD").getOriginalObject();
   msBrnCd  = (String) loCurrentRow.getData("GOVT_BRN_CD").getOriginalObject();
   msCabCd  = (String) loCurrentRow.getData("CAB_CD").getOriginalObject();
   msDeptCd = (String) loCurrentRow.getData("DEPT_CD").getOriginalObject();
   msDivCd  = (String) loCurrentRow.getData("DIV_CD").getOriginalObject();
   msGpCd   = (String) loCurrentRow.getData("GP_CD").getOriginalObject();
   msSectCd = (String) loCurrentRow.getData("SECT_CD").getOriginalObject();
   msDstcCd = (String) loCurrentRow.getData("DSTC_CD").getOriginalObject();
   msBurCd  = (String) loCurrentRow.getData("BUR_CD").getOriginalObject();
   msUnitCd = (String) loCurrentRow.getData("UNIT_CD").getOriginalObject();
}
//END_EVENT_T1R_WF_APRV_beforeSave}}
//{{EVENT_T1R_WF_APRV_beforeQuery
void T1R_WF_APRV_beforeQuery(VSQuery query ,VSOutParam resultset )
{


   //Write Event Code below this line
   SearchRequest loSearchReq = new SearchRequest();

   String lsFinalClause = " (SEQ_NO = 1)" ;

   String lsOldWhereClause = query.getSQLWhereClause() ;

   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsFinalClause = " AND " + lsFinalClause ;
   }
   loSearchReq.add(lsFinalClause);
   //this is used to display all records with seq_no =1 within the grid part of the page
   query.addFilter(loSearchReq);
}
//END_EVENT_T1R_WF_APRV_beforeQuery}}
//{{EVENT_T2pR_WF_APRV_afterPageNavigation
void T2pR_WF_APRV_afterPageNavigation( PageNavigation nav )
{
   //Write Event Code below this line

}
//END_EVENT_T2pR_WF_APRV_afterPageNavigation}}
//{{EVENT_T1R_WF_APRV_afterDelete
void T1R_WF_APRV_afterDelete(VSRow row )
{
   //Write Event Code below this line

   //when the user clicks on 'delete' within approval level 1-10 page
   //all records for that document will be deleted from R_WF_APRV table
   //this part of the code deletes the corresponding record with seq_no =2

   VSRow loCurrentRow = null;

   /*
    * If severity level is greater than error, return.
    * as the second row in this case should not be deleted
    */
   if ( getHighestSeverityLevel() >= SEVERITY_LEVEL_ERROR )
   {
      return;
   } /* end if ( getHighestSeverityLevel() >= SEVERITY_LEVEL_ERROR ) */

   loCurrentRow = row;

   if (loCurrentRow != null)
   {
      VSSession loSession = getParentApp().getSession();

      String lsDocCd   = loCurrentRow.getData("DOC_CD").getString();
      String lsBrnCd   = loCurrentRow.getData("GOVT_BRN_CD").getString();
      String lsCabCd   = loCurrentRow.getData("CAB_CD").getString();
      String lsDeptCd  = loCurrentRow.getData("DEPT_CD").getString();
      String lsDivCd   = loCurrentRow.getData("DIV_CD").getString();
      String lsGpCd    = loCurrentRow.getData("GP_CD").getString();
      String lsSectCd  = loCurrentRow.getData("SECT_CD").getString();
      String lsDstcCd  = loCurrentRow.getData("DSTC_CD").getString();
      String lsBurCd   = loCurrentRow.getData("BUR_CD").getString();
      String lsUnitCd  = loCurrentRow.getData("UNIT_CD").getString();

      SearchRequest loSearchReq = new SearchRequest();

      long llSeqNo = 2L;
      String lsParamValue = Long.toString(llSeqNo);
      Parameter loParam = new Parameter("R_WF_APRV","SEQ_NO",lsParamValue);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","DOC_CD",lsDocCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","GOVT_BRN_CD",lsBrnCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","CAB_CD",lsCabCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","DEPT_CD",lsDeptCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","DIV_CD",lsDivCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","GP_CD",lsGpCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","SECT_CD",lsSectCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","DSTC_CD",lsDstcCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","BUR_CD",lsBurCd);
      loSearchReq.add( loParam );

      loParam = new Parameter("R_WF_APRV","UNIT_CD",lsUnitCd);
      loSearchReq.add( loParam );

      VSQuery loQry = new VSQuery(loSession,"R_WF_APRV", loSearchReq,null  );

      // execute query into resultset
      VSResultSet loQryRs  = loQry.execute();
      VSRow loRow = loQryRs.last();

      int liRowCnt = loQryRs.getRowCount();

      if(liRowCnt > 0)//delete row with seq_no = 2 if found in the database
      {
         loQryRs.delete();
      }

      if (loQryRs != null)
      {
         loQryRs.close();
      } /* end if (loQryRs != null) */
   }

}
//END_EVENT_T1R_WF_APRV_afterDelete}}
//{{EVENT_T1R_WF_APRV_beforeDelete
void T1R_WF_APRV_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
{
   //Write Event Code below this line

}
//END_EVENT_T1R_WF_APRV_beforeDelete}}
//{{EVENT_T4pR_WF_APRV_FLD_beforePageNavigation
void T4pR_WF_APRV_FLD_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line
   try
   {
      String lsDocCd = getRootDataSource().getCurrentRow().getData("DOC_CD").getString();
      String lsWhereClause = "DOC_CD = '" + AMSSQLUtil.getANSIQuotedStr(lsDocCd) + "'" ;

      nav.setDevWhere(lsWhereClause);
   }
   catch (Exception loEcxcp)
   {
      return;
   }
}
//END_EVENT_T4pR_WF_APRV_FLD_beforePageNavigation}}
//{{EVENT_T3pR_WF_APRV_COND_beforePageNavigation
void T3pR_WF_APRV_COND_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line
   try
   {
      String lsDocCd = getRootDataSource().getCurrentRow().getData("DOC_CD").getString();
      String lsWhereClause = "DOC_CD = '" + AMSSQLUtil.getANSIQuotedStr(lsDocCd) + "'" ;
      nav.setDevWhere(lsWhereClause);
   }
   catch (Exception loEcxcp)
   {
      return;
   }
}
//END_EVENT_T3pR_WF_APRV_COND_beforePageNavigation}}
//{{EVENT_T1R_WF_APRV_beforeInsert
void T1R_WF_APRV_beforeInsert(VSRow newRow ,VSOutParam cancel )
{
   //Write Event Code below this line
   String lsCurrent = newRow.getData("SLF_APRV_RSCT").getString();
   if(getLastAction()==ActionElement.db_insert && AMSStringUtil.strIsEmpty(lsCurrent))
   {
      String lsParam = AMSPLSUtil.getApplParamValue("SELF-APPROVAL_DEFAULT_VALUE", getParentApp().getSession());
      if(!AMSStringUtil.strIsEmpty(lsParam))
      {
         SearchRequest loSearch = new SearchRequest();
         VSResultSet loResultSet = null;
         VSQuery loQuery = null;
         loSearch.add(new Parameter("CVL_SLF_APRV_RST","CVL_SLF_APRV_SV", lsParam));
         loQuery = new VSQuery( getParentApp().getSession(), "CVL_SLF_APRV_RST",loSearch , null ) ;
         try
         {
            loResultSet = loQuery.execute() ;
            if(loResultSet.first()==null)
            {
               raiseException("Application parameter SELF-APPROVAL_DEFAULT_VALUE has invalid value.",SEVERITY_LEVEL_WARNING);
            }
            else
            {
               newRow.getData("SLF_APRV_RSCT").setString(lsParam);
            }
         }//end try
         finally
         {
            if( loResultSet!= null )
            {
               loResultSet.close();
            }
         }//end finally
      }
   }// end if(getLastAction()==ActionElement.db_insert && AMSStringUtil.strIsEmpty(lsCurrent))
}
//END_EVENT_T1R_WF_APRV_beforeInsert}}

	//END_EVENT_CODE}}

	   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T2pR_WF_APRV.addPageNavigationListener(this);
	addPageListener(this);
	T1R_WF_APRV.addDBListener(this);
	T4pR_WF_APRV_FLD.addPageNavigationListener(this);
	T3pR_WF_APRV_COND.addPageNavigationListener(this);
	//END_EVENT_ADD_LISTENERS}}
	   }

	//{{EVENT_ADAPTER_CODE
	
	public void afterPageNavigation( PageNavigation obj ){
		Object source = obj;
		if (source == T2pR_WF_APRV) {
			T2pR_WF_APRV_afterPageNavigation( obj );
		}
	}
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_APRV_Grid_afterActionPerformed( ae, preq );
		}
	}
	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_afterSave(row );
		}
	}
	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pR_WF_APRV_Grid_requestReceived( req, evt );
		}
	}
	public void afterDelete( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_afterDelete(row );
		}
	}
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_beforeSave(row ,cancel );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_APRV_Grid_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeInsert( DataSource obj, VSRow newRow ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_beforeInsert(newRow ,cancel );
		}
	}
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T2pR_WF_APRV) {
			T2pR_WF_APRV_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T4pR_WF_APRV_FLD) {
			T4pR_WF_APRV_FLD_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T3pR_WF_APRV_COND) {
			T3pR_WF_APRV_COND_beforePageNavigation( obj, cancel, newPage );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_beforeQuery(query , resultset );
		}
	}
	public void beforeDelete( DataSource obj, VSRow row ,VSOutParam cancel ,VSOutParam response ){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_beforeDelete(row ,cancel ,response );
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
	   * This method overrides the super class generate method.
	   *
	   * Modification Log : Siddhartha Das  - 03/21/02
	   *                                    - inital version
	   */
	   public String generate()
	   {
	      /**
	       * Assignee picks trigger the hidden userid/roleid picks
	       * which cause problems when setting focus after the pick
	       * window is closed. The equivalent assignee fields are
	       * instead being substituted as the actual pick field on
	       * the startup page to prevent this problem.
	       */
	      String lsLastAction = getLastAction();
	      if ( ( lsLastAction != null ) &&
	           ( lsLastAction.equals( ActionElement.db_pick ) ) )
	      {
	         appendOnloadString("WFAPRV_AdjustPickField();");
	      }
         myBeforeGenerate() ;
	      return super.generate() ;
	   } /* End of generate () */

	   public boolean getPasteFlag()
	   {
	      return mboolPasteActnFlg;
	   }

	   public boolean setPasteFlag(boolean fboolPasteActnFlg)
	   {
	      mboolPasteActnFlg = fboolPasteActnFlg;
	      return(mboolPasteActnFlg);
	   }
      private void resetCursorPosition(AMSDataSource foDataSource, String fsRepositionWhere)
      {
         VSResultSet loResultSet = foDataSource.getResultSet();
         if (loResultSet == null)
         {
            return;
         }

         if (fsRepositionWhere != null && fsRepositionWhere.length()>0)
         {
            int liIndex = loResultSet.findFirst(fsRepositionWhere);
            if (liIndex>-1)
            {
               //set resultset cursor to the found record
               loResultSet.getRowAt(liIndex);
               //reset reposition where clause
               fsRepositionWhere=null;
            }
         }
      }

      private String getRepositionWhere(AMSDataSource foDS)
      {
         if (foDS == null)
         {
            return null;
         }

         // get the primary keys of the DataSource
         VSRow loRow = foDS.getCurrentRow();
         if (loRow != null)
         {
            Iterator loIdentityCols = null;
            try
            {
               loIdentityCols = foDS.getMetaQuery().getIdentityColumns();
            }
            catch (VSApiException loAPIException)
            {
               if (AMS_PLS_DEBUG)
               {
                  // Add exception log to logger object
                  moLog.error("Unexpected error encountered while processing. ", loAPIException);

               }
               //if identity column is not defined, no need to set currency of current rows
               //after document is closed and returns to the activity folder, currency will not be set.
               return null;
            }
            if (loIdentityCols!=null)
            {
               try
               {
                  StringBuffer lsbRepositionWhere = new StringBuffer();
                  while (loIdentityCols.hasNext())
                  {
                     VSQueryColumnDefinition loCol = (VSQueryColumnDefinition) loIdentityCols.next();
                     String lsColName = loCol.getName();

                     //get the value for the column in the current row
                     VSData loKeyData = loRow.getData(lsColName);
                     String lsParam = AMSMultiTableDataPage.constructRepositionParam (loKeyData);
                     if (lsParam == null) // will be null if loKeyData is null
                     {
                        //if identity column missing, cannot relocate the correct record, just break
                        moLog.error("Data for identity column: " + lsColName + " is null. Will not reposition for this data source");
                        break;
                     }
                     if (lsbRepositionWhere.length()>0)
                     {
                        lsbRepositionWhere.append(" AND ");
                     }
                     lsbRepositionWhere.append(lsParam);
                  }

                  if (lsbRepositionWhere.length()>0)
                  {
                     // set the reposition where for this data source into the datasource itself
                     return lsbRepositionWhere.toString();
                   }
               }
               catch (ClassCastException loE)
               {
                  moLog.error ("pR_WF_APRV_Grid.addDataSourceRepositionWhere() Exception: " + loE);
               }
            }
         }
         return null;
      }

   //BGN ADVHR00037821
   /**
    * This method returns true so that changing the document component
    * will cause an accept data.
    */
   public boolean acceptDataOnSyncCurrency( DataSource foDSSync, PLSRequest foPLSReq )
   {
      return true ;
   } /* end acceptDataOnSyncCurrency() */



   /**
     * This method gets the current row, and retrieves doc code and doc component.
     *
     *
     */

   private void myBeforeGenerate()
   {
      String lsDocCode = null ;
      String lsDocComp = null ;
      DataSource loDataSource = getRootDataSource() ;
      VSRow loRow = loDataSource.getCurrentRow();
      String lsDocKey = null;
      VSResultSet loResultSet =null;

      try
      {
         if ( loRow != null )
         {
            lsDocCode = loRow.getData( AMSCommonConstants.ATTR_DOC_CD ).getString() ;
            lsDocKey = lsDocCode + loRow.getData("APRV_RULE_ID").getInt();

        	SearchRequest loSearchReq = new SearchRequest();
        	loSearchReq.addParameter("R_DOC_CD", AMSCommonConstants.ATTR_DOC_CD,lsDocCode);
        	VSQuery loQuery =
                new VSQuery( getParentApp().getSession(), "R_DOC_CD",loSearchReq, null);
            loResultSet = loQuery.execute();

            String lsDoctyp = "";
            if ( loResultSet != null )
            {
               VSRow loRow1 = loResultSet.first();
               if ( loRow1 !=null)
               {
                  lsDoctyp = loRow1.getData(ATTR_DOC_TYP).getString() ;
               }
            }
            lsDocComp = lsDoctyp + HDR_SUFFIX ;
         } /* if ( loRow != null ) */
      }
      catch (RuntimeException loR_WF_APRV_GridException)
      {
         raiseException("Error retrieving field information",
               SEVERITY_LEVEL_ERROR);
         return;
      } /* end try - catch */
      finally
      {
         if ( loResultSet!=null)
         {
            loResultSet.close();
         }
      }

      if(!AMSStringUtil.strIsEmpty(lsDocCode) && (mbPageFirstLoadFlg || (!AMSStringUtil.strEqual(msOldDocCd, lsDocCode))
            || (!AMSStringUtil.strEqual(msOldDocKey, lsDocKey))))
      {
         VSSession  loSession      = loDataSource.getSession() ;
         VSCodeTable loCodeTbl = new VSCodeTable();
         String       lsCaption ;
         String       lsName ;
         VSQuery      loQuery ;
         VSResultSet  loRsltSet = null ;
         StringBuffer lsbWhere  = new StringBuffer( 64 ) ;
         int          liNumRows ;

         mbPageFirstLoadFlg = false;
         msOldDocCd = lsDocCode;

         try
         {
            /*Query R_WF_XML_DATA to get Document Field names
             * using document code, docuemnt component, data type and data size
             */

            lsbWhere.append( "DOC_CD" ) ;
            lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsDocCode, AMSSQLUtil.EQUALS_OPER ) ) ;
            lsbWhere.append( " AND DOC_COMP" ) ;
            lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsDocComp, AMSSQLUtil.EQUALS_OPER ) ) ;
            lsbWhere.append(" AND (DATA_TYP = " + DataConst.VARCHAR);
            lsbWhere.append(" OR DATA_TYP = " + DataConst.CHAR + ")");
            lsbWhere.append(" AND DATA_SIZE <= " + C_DATA_SIZE);

            loQuery = new VSQuery( loSession, "R_WF_XML_DATA", lsbWhere.toString(), "DOC_FLD_NM_UP, DOC_FLD" ) ;
            loRsltSet = loQuery.execute() ;
            loRsltSet.last() ;
            liNumRows = loRsltSet.getRowCount() ;
            loCodeTbl.put("", "");

            /*
             * For each column, get the name and the caption
             * to populate the VSCodeTable
             */
            for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ )
            {
               loRow = loRsltSet.getRowAt( liRowCtr ) ;
               lsName =  loRow.getData("DOC_FLD").getString();// fetch DOC_FLD
               lsCaption = loRow.getData("DOC_FLD_NM").getString();//fetch DOC_FLD_NM;
               loCodeTbl.put(lsName, lsName + " - " + lsCaption);
               loCodeTbl.setName("");
            } /* end for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ ) */
         } /* end try */
         catch( RuntimeException foExp )
         {
            raiseException( "Error creating list of fields", SEVERITY_LEVEL_ERROR ) ;
            return;
         } /* end catch( RuntimeException foExp ) */
         finally
         {
            if ( loRsltSet != null )
            {
               loRsltSet.close() ;
            } /* end if ( loRsltSet != null ) */
         } /* end finally */

         AMSComboBoxElement loComboBox = null;

            for(int liCombo = 1 ; liCombo <=10; liCombo++)
            {
               loComboBox = (AMSComboBoxElement) getElementByName("txtT1ASSGN_FLD_" + liCombo);
               loComboBox.removeAllElements();
               if ( loCodeTbl.size() > 0)
               {
                  loComboBox.setCodeTable(loCodeTbl);
               }

            }
      }//End if(!AMSStringUtil.strIsEmpty(lsDocCode) && (mbPageFirstLoadFlg || (!AMSStringUtil.strEqual(msOldDocCd, lsDocCode))))
      if ( AMSStringUtil.strIsEmpty(lsDocCode) )
      {
         AMSComboBoxElement loComboBox = null;

         for(int liCombo = 1 ; liCombo <=10; liCombo++)
         {
            loComboBox = (AMSComboBoxElement) getElementByName("txtT1ASSGN_FLD_" + liCombo);
            loComboBox.removeAllElements() ;
            loComboBox.addElement("");
         }
      }
   }//End myBeforeGenerate
   //END ADVHR00037821
}

