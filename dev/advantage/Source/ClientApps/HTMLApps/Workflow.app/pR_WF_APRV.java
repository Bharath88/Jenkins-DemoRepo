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
import advantage.AMSStringUtil;

   /*
   **  pR_WF_APRV
   */

//{{FORM_CLASS_DECL
public class pR_WF_APRV extends pR_WF_APRVBase

//END_FORM_CLASS_DECL}}
   {


       /*
      * value of seq_no for approval levels 1 - 10
      */
        public  final static String APR_LEVL_SEQ_NO_1 = "1";

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

         //BGN ADVHR00037821
         //Page First load Flag  - Use when first time page is loaded.
         private boolean mbPageFirstLoadFlg = true;

         //Old Document Code.
         private String msOldDocCd = null;
         /* Constant indicating the header component suffix*/
         private static final String HDR_SUFFIX= "_DOC_HDR";

         //Constant for Data Size of document header fields
         private static final int C_DATA_SIZE = 16;

         //END ADVHR00037821

      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code. To customize paint
      // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pR_WF_APRV ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
               setAllowHistory( false ) ;
      }



//{{EVENT_CODE
//{{EVENT_pR_WF_APRV_beforeHTMLDefinitionLoaded
void pR_WF_APRV_beforeHTMLDefinitionLoaded(String filename)
{
   //Write Event Code below this line
}
//END_EVENT_pR_WF_APRV_beforeHTMLDefinitionLoaded}}
//{{EVENT_T2pR_WF_APRV_Grid_beforePageNavigation
void T2pR_WF_APRV_Grid_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line

      VSRow       loCurrApLvlRow;
      String      lsSeqNo = APR_LEVL_SEQ_NO_1 ;

      //save the data for approval level 11 - 20
      getRootDataSource().updateDataSource();

      //display the previous page
      newPage.setValue(getSourcePage());
      cancel.setValue(true);


}
//END_EVENT_T2pR_WF_APRV_Grid_beforePageNavigation}}
//{{EVENT_pR_WF_APRV_afterActionPerformed
void pR_WF_APRV_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line

}
//END_EVENT_pR_WF_APRV_afterActionPerformed}}
//{{EVENT_T1R_WF_APRV_afterQuery
void T1R_WF_APRV_afterQuery(VSResultSet rs )
{
   //Write Event Code below this line



}
//END_EVENT_T1R_WF_APRV_afterQuery}}
//{{EVENT_T3pR_WF_APRV_COND_beforePageNavigation
void T3pR_WF_APRV_COND_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line

   try
   {
      String lsDocCd = getRootDataSource().getCurrentRow().getData("DOC_CD").getString();
      String lsWhereClause = "DOC_CD = " + AMSSQLUtil.getANSIQuotedStr(lsDocCd,true) ;
      nav.setDevWhere(lsWhereClause);
   }
   catch (Exception loEcxcp)
   {
      return;
   }
}
//END_EVENT_T3pR_WF_APRV_COND_beforePageNavigation}}
//{{EVENT_T4pR_WF_APRV_FLD_beforePageNavigation
void T4pR_WF_APRV_FLD_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line
   try
   {
      String lsDocCd = getRootDataSource().getCurrentRow().getData("DOC_CD").getString();
      String lsWhereClause = "DOC_CD = " + AMSSQLUtil.getANSIQuotedStr(lsDocCd,true) ;

      nav.setDevWhere(lsWhereClause);
   }
   catch (Exception loEcxcp)
   {
      return;
   }
}
//END_EVENT_T4pR_WF_APRV_FLD_beforePageNavigation}}
//{{EVENT_pR_WF_APRV_beforeActionPerformed
void pR_WF_APRV_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
}
//END_EVENT_pR_WF_APRV_beforeActionPerformed}}

//END_EVENT_CODE}}

      public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T2pR_WF_APRV_Grid.addPageNavigationListener(this);
	T1R_WF_APRV.addDBListener(this);
	T3pR_WF_APRV_COND.addPageNavigationListener(this);
	T4pR_WF_APRV_FLD.addPageNavigationListener(this);
//END_EVENT_ADD_LISTENERS}}
      }

//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_APRV_afterActionPerformed( ae, preq );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_APRV_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T2pR_WF_APRV_Grid) {
			T2pR_WF_APRV_Grid_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T3pR_WF_APRV_COND) {
			T3pR_WF_APRV_COND_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T4pR_WF_APRV_FLD) {
			T4pR_WF_APRV_FLD_beforePageNavigation( obj, cancel, newPage );
		}
	}
	public void beforeHTMLDefinitionLoaded ( VSPage obj, String filename ){
		Object source = obj;
		if (source == this ) {
			pR_WF_APRV_beforeHTMLDefinitionLoaded(filename);
		}
	}
	public void afterQuery( DataSource obj, VSResultSet rs ){
		Object source = obj;
		if (source == T1R_WF_APRV) {
			T1R_WF_APRV_afterQuery(rs );
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
      *                                    - reset pick field to
      *                                      Assignee
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

         //if last action was 'paste' before the transition then the copied values
         // need to be pasted to approval level 11-20 screen
         if (((pR_WF_APRV_Grid)getSourcePage()).getPasteFlag())
         {
            DataSource     loDataSource;
            VSRow          loNewRow;
            VSData         loColData;
            VSData         loNewColData;
            String         lsColStr;
            String         lsHashColData = null;
            Hashtable      lhCopyRow ;
            AMSStartupPage loStartup = null;
            String         lsKeyName = null;
            String         lsHashColStr = null;

            // get old column values from hash table
            loStartup = AMSStartupPage.getStartupPage( getParentApp() ) ;
            lhCopyRow = loStartup.getCopyRow() ;

            if ( lhCopyRow != null && !lhCopyRow.isEmpty() )
            {
               loNewRow = getRootDataSource().getCurrentRow();

               for ( int liColumnCtr = 15;liColumnCtr
                          <= loNewRow.getColumnCount(); liColumnCtr++ )
               {
                  loNewColData = loNewRow.getData(liColumnCtr);

                  if (loNewColData.getMetaColumn().isNotAlterable())
                  {
                     continue;
                  }

                  lsKeyName = loNewColData.getName() +
                                 pR_WF_APRV_Grid.ARPV_LVL_SEQ_2_SUFFIX;

                  //retrieve values from hash table with the key
                  lsHashColData = (String) lhCopyRow.get(lsKeyName);

                  if(lsHashColData != null)
                  {
                     loNewColData.setString(lsHashColData);
                     loNewColData.modified(true);
                  }
               }
            }
            ((pR_WF_APRV_Grid)getSourcePage()).setPasteFlag(false);//reset the 'paste' flag
         }

         myBeforeGenerate();//BGN ADVHR00037821
         return super.generate();
      }

   //BGN ADVHR00037821
   /**
    * This method gets the current row, and retrieves doc code and doc component.
    *
    * @author Bhavin Thaker
    */

   private void myBeforeGenerate()
   {
      String lsDocCode = null ;
      DataSource loDataSource = getRootDataSource() ;
      VSRow loRow = loDataSource.getCurrentRow();
      String lsDocComp = null ;
      VSResultSet loResultSet =null;

      try
      {
         if ( loRow != null )
         {
        	lsDocCode = loRow.getData( AMSCommonConstants.ATTR_DOC_CD ).getString() ;
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

      if(!AMSStringUtil.strIsEmpty(lsDocCode) && (mbPageFirstLoadFlg || (!AMSStringUtil.strEqual(msOldDocCd, lsDocCode))))
      {
         VSSession  loSession = loDataSource.getSession() ;
         VSCodeTable loCodeTbl = new VSCodeTable();
         String       lsCaption ;
         String       lsName ;
         VSQuery      loQuery ;
         VSResultSet  loRsltSet = null ;
         StringBuffer lsbWhere  = new StringBuffer(64) ;
         int          liNumRows ;

         mbPageFirstLoadFlg = false;
         msOldDocCd = lsDocCode;

         try
         {
            /*Query R_WF_XML_DATA to get Document Field names
             * using document code, docuemnt component, data type and data size
             *
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
               loCodeTbl.setSortCodeTableOnServerSide(true);
            } /* end for (liCount=1; liCount<liColumnCount; liCount++) */

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
         for(int liCombo = 1 ; liCombo <=5; liCombo++)
         {
            loComboBox = (AMSComboBoxElement) getElementByName("txtT1ASSGN_FLD_" + liCombo);
            loComboBox.removeAllElements() ;
            loComboBox.setCodeTable(loCodeTbl);
            loComboBox.addElement("");
         }
      }//End if(!AMSStringUtil.strIsEmpty(lsDocCode) && (mbPageFirstLoadFlg || (!AMSStringUtil.strEqual(msOldDocCd, lsDocCode))))
      if ( AMSStringUtil.strIsEmpty(lsDocCode))
      {
         AMSComboBoxElement loComboBox = null;
         for(int liCombo = 1 ; liCombo <= 5; liCombo++)
         {
            loComboBox = (AMSComboBoxElement) getElementByName("txtT1ASSGN_FLD_" + liCombo);
            loComboBox.removeAllElements();
            loComboBox.addElement("");
         }
      }
   }//End myBeforeGenerate
   //END ADVHR00037821
}
