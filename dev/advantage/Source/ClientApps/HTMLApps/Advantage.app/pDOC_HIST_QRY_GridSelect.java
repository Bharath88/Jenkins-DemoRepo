//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.*;
import java.rmi.RemoteException;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;
import org.apache.commons.logging.Log;

/*
**  pDOC_HIST_QRY_GridSelect*/

//{{FORM_CLASS_DECL
public class pDOC_HIST_QRY_GridSelect extends pDOC_HIST_QRY_GridSelectBase

//END_FORM_CLASS_DECL}}
implements com.amsinc.gems.adv.common.AMSCommonConstants
{

   /**
    *  customization on page
    *
    *     <a href="Advantage" vsaction="back" name="T1DOC_HISTback">Back</a>&nbsp;</td>
    *
    */

    //
    // DOC_HIST Attribute names.
    //
    public static final String SATTR_ARCH_FL  = "ARCH_FL";
    public static final String SATTR_DOC_ACTU_AM = "DOC_ACTU_AM";
    public static final String SATTR_DOC_NUM  = "DOC_NUM";

    //
    // SQL constants
    //
    public static final String TOK_TABLE_DOC_HDR = "DOC_HDR";
    public static final String TOK_TABLE_ARCHIVE = "IN_DOC_ARCH_CTLG";
    public static final String TOK_SELECT = "SELECT";
    public static final String TOK_SELECT_DISTINCT = "SELECT DISTINCT";
    public static final String TOK_WHERE  = "WHERE";
    public static final String TOK_FROM   = "FROM";
    public static final String TOK_NULL   = "NULL";
    public static final String TOK_AND    = "AND";
    public static final String TOK_OR     = "OR";
    public static final String TOK_CONCAT = "||";
    public static final String TOK_EQ     = "=";
    public static final String TOK_LTE    = "<=";
    public static final String TOK_GTE    = ">=";
    public static final String TOK_LT     = "<";
    public static final String TOK_GT     = ">";
    public static final String TOK_LP     = "(";
    public static final String TOK_RP     = ")";
    public static final String TOK_QUAL   = ".";
    public static final String TOK_SEP    = ", ";
    public static final String TOK_EMPTY  = "";
    public static final String TOK_SPACE  = " ";
    public static final String TOK_APOST  = "'";
    public static final String TOK_SPCR_AND = " " + TOK_AND + " ";
    public static ActionElement mae = null;
    public static String msDocCode = null;
    public static String msDocDept = null;
    public static String msDocID   = null;
    private static final String SELECTSQL = "DHISTSelectSQL";
    private static final String COUNTSQL = "DHISTCountSQL";

    private boolean mboolFirstTime = true;
    
    private static Log moAMSLog = AMSLogger.getLog(pDOC_HIST_QRY_GridSelect.class,
          AMSLogConstants.FUNC_AREA_DFLT);

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pDOC_HIST_QRY_GridSelect ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

   }



//{{EVENT_CODE
//{{EVENT_T1DOC_HIST_beforeQuery
void T1DOC_HIST_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   /**
    *  Generate SELECT
    */
   String lsSELECT = this.createSELECT();


   StringBuffer lsbSelectSQL = new StringBuffer(lsSELECT.length() + 32);
   StringBuffer lsbCountSQL = new StringBuffer(lsSELECT.length() + 64);

   lsbSelectSQL.append(lsSELECT).append( " ORDER BY DOC_NUM ASC " );

   lsbCountSQL.append(" SELECT COUNT(");
   lsbCountSQL.append(ATTR_DOC_CD);
   lsbCountSQL.append(") FROM (");
   lsbCountSQL.append(lsSELECT);
   lsbCountSQL.append(") A");

   VSORBSession loSession = getParentApp().getSession().getORBSession() ;

   try
   {
     loSession.setProperty(SELECTSQL, lsbSelectSQL.toString() ) ;
     loSession.setProperty(COUNTSQL, lsbCountSQL.toString() ) ;

   } /* end try */
   catch( RemoteException foExp )
   {
        // Add exception log to logger object
        moAMSLog.error("Unexpected error encountered while processing. ", foExp);

        raiseException( "Unable to set Session property",
            SEVERITY_LEVEL_SEVERE ) ;
   } /* end catch( RemoteException foExp ) */
}
//END_EVENT_T1DOC_HIST_beforeQuery}}
//{{EVENT_pDOC_HIST_QRY_GridSelect_beforeActionPerformed
void pDOC_HIST_QRY_GridSelect_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   mae = ae;
   //Write Event Code below this line

    if (ae.getAction() == ActionElement.pg_back)
   {
      // Let's check if the previous page is an instance of a document
      VSPage loPage = getSourcePage();
      if (loPage instanceof AMSDocTabbedPage)
      {
         ((AMSDocTabbedPage) loPage).setGenTabPage(true);
      }
   }

    String lsDocCode = T1DOC_HIST.getQBFDataForElement( "txtT1DOC_CD" );
    String lsDocDept = T1DOC_HIST.getQBFDataForElement( "txtT1DOC_DEPT_CD" );
    String lsDocID   = T1DOC_HIST.getQBFDataForElement( "txtT1DOC_ID" );

    if(ae.getAction().equals("query"))
    {
      if( AMSStringUtil.strIsEmpty( lsDocCode ) ||
          AMSStringUtil.strIsEmpty( lsDocDept ) ||
          AMSStringUtil.strIsEmpty( lsDocID )
        )
        {
           raiseException( "Unable to perform the search with given criteria."+
                            "All the 3 fields are required for search.",AMSPage.SEVERITY_LEVEL_SEVERE);
        }
    }

   if (ae.getName().equals( "AMSBrowse" ))
   {
      if(AMSStringUtil.strIsEmpty(preq.getParameter("txtT1DOC_CD")) ||
         AMSStringUtil.strIsEmpty(preq.getParameter("txtT1DOC_DEPT_CD")))
      {
         String lsDocCodeCaption = AMSPage.getColumnCaption("DOC_HIST","DOC_CD",this);
         String lsDocDeptCaption = AMSPage.getColumnCaption("DOC_HIST","DOC_DEPT_CD",this);
         raiseException(lsDocCodeCaption + " and " + lsDocDeptCaption
                        +" is required", SEVERITY_LEVEL_SEVERE);
      }
   }
}
//END_EVENT_pDOC_HIST_QRY_GridSelect_beforeActionPerformed}}
//{{EVENT_T1DOC_HIST_afterQuery
void T1DOC_HIST_afterQuery(VSResultSet rs )
{
   //Write Event Code below this line

   VSORBSession loSession = getParentApp().getSession().getORBSession() ;

   try
   {
     loSession.setProperty(SELECTSQL, "" ) ;
     loSession.setProperty(COUNTSQL, "" ) ;
   } /* end try */
   catch( RemoteException foExp )
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", foExp);

      raiseException( "Unable to set Session property",
               SEVERITY_LEVEL_SEVERE ) ;
   } /* end catch( RemoteException foExp ) */

}
//END_EVENT_T1DOC_HIST_afterQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1DOC_HIST.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pDOC_HIST_QRY_GridSelect_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1DOC_HIST) {
			T1DOC_HIST_beforeQuery(query , resultset );
		}
	}
	public void afterQuery( DataSource obj, VSResultSet rs ){
		Object source = obj;
		if (source == T1DOC_HIST) {
			T1DOC_HIST_afterQuery(rs );
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

   private void appendWHERE(StringBuffer fs, String fsWHERE )
      {
        if(fs.length() > 0)
        {
            fs.append( TOK_SPCR_AND );
        }
        fs.append( TOK_LP );
        fs.append( fsWHERE );
        fs.append( TOK_RP );
      }

      /**
       * Construct an item for the SELECT list.  For example, "R_COMM_CD.COMM_CD COMM_CD"
       * @param fsTable Name of table.  Pass null for no table qualification.
       * @param fsCol Name of physical column.
       * @param fsAs Name of alias.
       * @return String to be used in the SELECT list.
       */
      public static String getSelect( String fsTable, String fsCol, String fsAs )
      {
        return ( ((fsTable==null)?TOK_EMPTY:fsTable + TOK_QUAL) + fsCol + TOK_SPACE + fsAs );
      }

      /**
       * Construct an item for the SELECT list.  For example, "R_COMM_CD.COMM_CD"
       * @param fsTable Name of table.  Pass null for no table qualification.
       * @param fsCol Name of physical column.
       * @return String to be used in the SELECT list.
       */
      public static String getSelect( String fsTable, String fsCol )
      {
        return ( ((fsTable==null)?TOK_EMPTY:fsTable + TOK_QUAL) + fsCol );
      }

      /**
       * Concatenate an array of SELECT list items into a single String to be used
       * after "SELECT".
       * @param fsCols Array of SELECT list items.  Gaps allowed.
       * @return String containing SELECT list.
       */
      public static String getColumns(String fsCols [])
      {
        StringBuffer ls = new StringBuffer(200);

        for(int li = 0; li < fsCols.length; ++li)
        {
            if(fsCols[li] != null)
            {
                if(ls.length() > 0)
                {
                    ls.append( TOK_SEP );
                }
                ls.append(fsCols[li]);
            }
        }
        return ls.toString();
      }


      /**
       *  SELECT DISTINCT
       *     tbl1.x x,
       *     tbl1.y y,
       *     z,
       *     a,
       *     b,
       *     c
       * FROM
       *     tbl1
       * WHERE
       *     user search conditions
    *
       * UNION
    *
       * SELECT DISTINCT
       *     x,
       *     y,
       *     z,
       *     a,
       *     b,
       *     c
       * FROM
       *     tbl1
       * WHERE
       *     user search conditions
       */


      /**
       * Build a SELECT statement for DOC_HIST.
       */
      private String createSELECT()
      {
         StringBuffer lsSELECT = new StringBuffer(2000);

         // "SELECT DISTINCT "
         lsSELECT.append( TOK_SELECT_DISTINCT + TOK_SPACE  );

         // Added all columns from DOC_HIST
      String lsDocHdrCols[] = this.createSelectFromDocHdr();
         lsSELECT.append( getColumns(lsDocHdrCols));

         // Append FROM
         lsSELECT.append( " FROM DOC_HDR " );

      // Append the WHERE conditions
         StringBuffer lsDocHdrWHERE = new StringBuffer(500);
         String lsDocCode = T1DOC_HIST.getQBFDataForElement( "txtT1DOC_CD" );
         String lsDocDept = T1DOC_HIST.getQBFDataForElement( "txtT1DOC_DEPT_CD" );
         String lsDocID   = T1DOC_HIST.getQBFDataForElement( "txtT1DOC_ID" );

         if (!mboolFirstTime)
         {

            if(lsDocCode != null && lsDocCode.trim().length() > 0)
            {
               String lsQBFText = this.getScalarElement("txtT1DOC_CD").getQBFText(lsDocCode);
               // lsQBFText now has your WHERE snippet!
               this.appendWHERE(lsDocHdrWHERE, lsQBFText );
            }

            if(lsDocDept != null && lsDocDept.trim().length() > 0)
            {
               String lsQBFText = this.getScalarElement("txtT1DOC_DEPT_CD").getQBFText(lsDocDept);
               // lsQBFText now has your WHERE snippet!
               this.appendWHERE(lsDocHdrWHERE, lsQBFText );
            }

            if(lsDocID != null && lsDocID.trim().length() > 0)
            {
               String lsQBFText = this.getScalarElement("txtT1DOC_ID").getQBFText(lsDocID);
               // lsQBFText now has your WHERE snippet!
               this.appendWHERE(lsDocHdrWHERE, lsQBFText );
            }

            //added a getSourcePage == null to deal with pages that have lapsed from memory
            //and allow for correct version results to be returned.
            if (lsDocHdrWHERE.length() == 0 &&
               (getSourcePage() == null || getSourcePage() instanceof AMSDocTabbedPage))
            {
               lsDocHdrWHERE.append(T1DOC_HIST.getQueryInfo().getDevWhere());
            }
         }
         else
         {
            lsDocHdrWHERE.append(T1DOC_HIST.getQueryInfo().getDevWhere());
            mboolFirstTime = false;
         }

         if(lsDocHdrWHERE.length() > 0)
      {
         lsSELECT.append( "WHERE " );
            lsSELECT.append( lsDocHdrWHERE.toString() );
      }
      //if as long as one of the fields from the page are > 0, then there needs to be a generated where clause
      else if( lsDocHdrWHERE.length() == 0 && (lsDocID.length() > 0 || lsDocCode.length() > 0 || lsDocDept.length() > 0) )
      {
         int liMultipleElements = 0;

         lsSELECT.append ("WHERE " );

         if( lsDocID != null && lsDocID.trim().length() > 0 )
         {
            lsSELECT.append(this.getScalarElement("txtT1DOC_ID").getQBFText(lsDocID));
            liMultipleElements = liMultipleElements + 1;
         }

         if( lsDocCode != null && lsDocCode.trim().length() > 0 )
         {
            if( liMultipleElements > 0 )
            {
               lsSELECT.append(" AND ");
            }
            lsSELECT.append(this.getScalarElement("txtT1DOC_CD").getQBFText(lsDocCode));
            liMultipleElements = liMultipleElements + 1;
         }

         if( lsDocDept != null && lsDocDept.trim().length() > 0 )
         {
            if( liMultipleElements > 0 )
            {
               lsSELECT.append(" AND ");
            }

            lsSELECT.append(this.getScalarElement("txtT1DOC_DEPT_CD").getQBFText(lsDocDept));
            liMultipleElements = liMultipleElements + 1;
         }
      }

      // Append UNION
      lsSELECT.append ( " UNION " );

         // "SELECT DISTINCT "
         lsSELECT.append( TOK_SELECT_DISTINCT + TOK_SPACE  );

         // Added all columns from IN_DOC_ARCH_CTLG
      String lsArchCols[] = this.createSelectFromArchive();
         lsSELECT.append( getColumns(lsArchCols));

         // Append FROM
         lsSELECT.append( " FROM IN_DOC_ARCH_CTLG " );

         if(lsDocHdrWHERE.length() > 0)
      {
         lsSELECT.append( "WHERE " );
            lsSELECT.append( lsDocHdrWHERE.toString() );
      }

      //if as long as one of the fields from the page are > 0, then there needs to be a generated where clause
      else if( lsDocHdrWHERE.length() == 0 && (lsDocID.length() > 0 || lsDocCode.length() > 0 || lsDocDept.length() > 0) )
      {
         int liMultipleElements = 0;

         lsSELECT.append ("WHERE " );

         if( lsDocID != null && lsDocID.trim().length() > 0)
         {
            lsSELECT.append(this.getScalarElement("txtT1DOC_ID").getQBFText(lsDocID));
            liMultipleElements = liMultipleElements + 1;
         }

         if( lsDocCode != null && lsDocCode.trim().length() > 0 )
         {
            if( liMultipleElements > 0 )
            {
               lsSELECT.append(" AND ");
            }

            lsSELECT.append(this.getScalarElement("txtT1DOC_CD").getQBFText(lsDocCode));
            liMultipleElements = liMultipleElements + 1;
         }

         if( lsDocDept != null && lsDocDept.trim().length() > 0 )
         {
            if( liMultipleElements > 0 )
            {
               lsSELECT.append(" AND ");
            }

            lsSELECT.append(this.getScalarElement("txtT1DOC_DEPT_CD").getQBFText(lsDocDept));
            liMultipleElements = liMultipleElements + 1;
         }
      }



      return lsSELECT.toString();
      }


   /**
    * create the select statement for DOC_HDR
    */
   private String [] createSelectFromDocHdr()
   {
        VSMetaQuery lvsmq = T1DOC_HIST.getMetaQuery();
        StringBuffer lsbDocNum = new StringBuffer();
        int liDatabaseType = getDatabaseType(); // this method comes from AMSPage

        String lsCols [] = new String [lvsmq.getColumnCount() + 1];
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_CD)]        = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_CD,         ATTR_DOC_CD);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_DEPT_CD)]   = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_DEPT_CD,    ATTR_DOC_DEPT_CD);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_ID)]        = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_ID,         ATTR_DOC_ID);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_VERS_NO)]   = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_VERS_NO,    ATTR_DOC_VERS_NO);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_FUNC_CD)]   = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_FUNC_CD,    ATTR_DOC_FUNC_CD );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_STA_CD)]    = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_STA_CD,     ATTR_DOC_STA_CD );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_LAST_USID)] = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_LAST_USID,  ATTR_DOC_LAST_USID );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_LAST_DT)]   = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_LAST_DT,    ATTR_DOC_LAST_DT );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_APPL_LAST_USID)] = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_APPL_LAST_USID,  ATTR_DOC_APPL_LAST_USID );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_APPL_LAST_DT)]   = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_APPL_LAST_DT,    ATTR_DOC_APPL_LAST_DT );
        lsCols[lvsmq.getColumnIndex(SATTR_DOC_ACTU_AM)]   = getSelect(TOK_TABLE_DOC_HDR, SATTR_DOC_ACTU_AM,   SATTR_DOC_ACTU_AM );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_PHASE_CD)]  = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_PHASE_CD,   ATTR_DOC_PHASE_CD );

        String lsConcapOp = AMSSQLUtil.getAMSSQLConcatOpText( liDatabaseType );
        lsbDocNum.append("'[' ").append(lsConcapOp)
            .append(" DOC_HDR.DOC_CD ").append(lsConcapOp).append(" ',' " )
            .append(lsConcapOp).append(" DOC_HDR.DOC_DEPT_CD " )
            .append(lsConcapOp).append(" ',' " )
            .append(lsConcapOp).append("DOC_HDR.DOC_ID " )
            .append(lsConcapOp).append(" ',' " )
            .append(lsConcapOp);

         /* For DB2 database type AMSSQLUtil.getAMSNumber is not being used because
          * Doc Version was getting converted to Double. As a result it was displayed
          * something like "1.0E0". To avoid the same, special handling is being done
          * for DB2 in getAMSInteger(...) method.
          */
         lsbDocNum.append( AMSSQLUtil.getAMSChar(10,AMSSQLUtil.getAMSInteger(
            "DOC_HDR.DOC_VERS_NO",liDatabaseType),liDatabaseType));
         lsbDocNum.append(lsConcapOp).append(" ']' DOC_NUM");
         lsCols[lvsmq.getColumnIndex(SATTR_DOC_NUM)] = lsbDocNum.toString();

         lsCols[lvsmq.getColumnIndex(SATTR_ARCH_FL)]       = "0 " + SATTR_ARCH_FL;

         lsCols[lvsmq.getColumnIndex(ATTR_DOC_CMNT_FL)]  = getSelect(TOK_TABLE_DOC_HDR, ATTR_DOC_CMNT_FL,   ATTR_DOC_CMNT_FL );
      return lsCols;
   }


   /**
    * create the select statement for IN_DOC_ARCH_CTLG
    */
   private String [] createSelectFromArchive()
   {
        VSMetaQuery lvsmq = T1DOC_HIST.getMetaQuery();
        int liDatabaseType = getDatabaseType(); // this method comes from AMSPage
        StringBuffer lsbDocNum = new StringBuffer();

        String lsCols [] = new String [lvsmq.getColumnCount() + 1];
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_CD)]        = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_CD,         ATTR_DOC_CD);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_DEPT_CD)]   = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_DEPT_CD,    ATTR_DOC_DEPT_CD);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_ID)]        = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_ID,         ATTR_DOC_ID);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_VERS_NO)]   = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_VERS_NO,    ATTR_DOC_VERS_NO);
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_FUNC_CD)]   = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_FUNC_CD,    ATTR_DOC_FUNC_CD );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_STA_CD)]    = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_STA_CD,     ATTR_DOC_STA_CD );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_LAST_USID)] = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_LAST_USID,  ATTR_DOC_LAST_USID );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_LAST_DT)]   = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_LAST_DT,    ATTR_DOC_LAST_DT );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_APPL_LAST_USID)] = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_APPL_LAST_USID,  ATTR_DOC_APPL_LAST_USID );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_APPL_LAST_DT)]   = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_APPL_LAST_DT,    ATTR_DOC_APPL_LAST_DT );
        lsCols[lvsmq.getColumnIndex(SATTR_DOC_ACTU_AM)]   = getSelect(TOK_TABLE_ARCHIVE, SATTR_DOC_ACTU_AM,   SATTR_DOC_ACTU_AM );
        lsCols[lvsmq.getColumnIndex(ATTR_DOC_PHASE_CD)]  = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_PHASE_CD,   ATTR_DOC_PHASE_CD );

        String lsConcapOp = AMSSQLUtil.getAMSSQLConcatOpText( liDatabaseType );
        lsbDocNum.append("'[' ").append(lsConcapOp)
            .append(" IN_DOC_ARCH_CTLG.DOC_CD ").append(lsConcapOp).append(" ',' " )
            .append(lsConcapOp).append(" IN_DOC_ARCH_CTLG.DOC_DEPT_CD " )
            .append(lsConcapOp).append(" ',' " )
            .append(lsConcapOp).append("IN_DOC_ARCH_CTLG.DOC_ID " )
            .append(lsConcapOp).append(" ',' " )
            .append(lsConcapOp);

         /* For DB2 database type AMSSQLUtil.getAMSNumber is not being used because
          * Doc Version was getting converted to Double. As a result it was displayed
          * something like "1.0E0". To avoid the same, special handling is being done
          * for DB2 in getAMSInteger(...) method.
          */
         lsbDocNum.append(AMSSQLUtil.getAMSChar(10,AMSSQLUtil.getAMSInteger(
            "IN_DOC_ARCH_CTLG.DOC_VERS_NO",liDatabaseType),liDatabaseType));
         lsbDocNum.append(lsConcapOp).append(" ']' DOC_NUM");
         lsCols[lvsmq.getColumnIndex(SATTR_DOC_NUM)] = lsbDocNum.toString();

     lsCols[lvsmq.getColumnIndex(SATTR_ARCH_FL)]       = "1 " + SATTR_ARCH_FL;

         lsCols[lvsmq.getColumnIndex(ATTR_DOC_CMNT_FL)]  = getSelect(TOK_TABLE_ARCHIVE, ATTR_DOC_CMNT_FL,   ATTR_DOC_CMNT_FL );
      return lsCols;
   }

   public String generate()
   {
      return super.generate();
   }

   public boolean isUseSwing()
   {
      return true ;
   } /* end isUseSwing() */

   public boolean useVirtualResultSet (com.amsinc.gems.adv.vfc.html.AMSDataSource foDataSource)
   {
      return false;
   }

    /**
    * Overrides a super class method.  Called before HTML is generated.
    */
   public void beforeGenerate()
   {
      super.beforeGenerate();

      String lsAction = null;

      if (mae != null)
      {
         lsAction = mae.getAction();
      }

      if (lsAction == null || !lsAction.equals(ActionElement.db_clearQuery))
      {
          VSRow loVSRow = T1DOC_HIST.getCurrentRow();
          if (loVSRow != null)
          {
              String lsDOC_CD      = loVSRow.getData( "DOC_CD" ).getString();
              String lsDOC_DEPT_CD = loVSRow.getData( "DOC_DEPT_CD" ).getString();
              String lsDOC_ID      = loVSRow.getData( "DOC_ID" ).getString();
              T1DOC_HIST.setQBFDataForElement( "txtT1DOC_CD", lsDOC_CD );
              T1DOC_HIST.setQBFDataForElement( "txtT1DOC_DEPT_CD", lsDOC_DEPT_CD );
              T1DOC_HIST.setQBFDataForElement( "txtT1DOC_ID", lsDOC_ID );
          }
          else
          {
               if (msDocCode != null)
               {
                   T1DOC_HIST.setQBFDataForElement( "txtT1DOC_CD", msDocCode );
                   T1DOC_HIST.setQBFDataForElement( "txtT1DOC_DEPT_CD", msDocDept );
                   T1DOC_HIST.setQBFDataForElement( "txtT1DOC_DEPT_CD", msDocID );
                }
          }
      }
   }

}
