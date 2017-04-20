//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import versata.vfc.html.TableElement.TableColumnElement;
import versata.vls.ServerEnvironment;
import versata.vls.Session;
import versata.vls.VSORBSessionImpl;
import java.util.ArrayList;
import java.util.HashMap;
import advantage.AMSDataObject;
import advantage.AMSStringUtil;


import javax.swing.text.MutableAttributeSet;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.vfc.html.AMSPage;
import com.amsinc.gems.adv.vfc.html.AMSTableElement;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

import org.apache.commons.logging.Log;

/*
**  pWF_APRV_WRK_LST_DET
*/

//{{FORM_CLASS_DECL
public class pWF_APRV_WRK_LST_DET extends pWF_APRV_WRK_LST_DETBase

//END_FORM_CLASS_DECL}}
{
   // Holds a list of all the Custom Columns on the page.
   private static ArrayList<String> moCustomCols = new ArrayList<String>();
   
   
   
   private static Log moAMSLog = AMSLogger.getLog( 
         pWF_APRV_WRK_LST_DET.class,
         AMSLogConstants.FUNC_AREA_WORKFLOW ) ;

   /**
    * List of columns that are currently to be hidden. This is determined by the configuration
    * setup on the Configure Worklist Details page.
    * The Configure Worklist Details page has 5 rows. Each row is backed up by 3 data types.
    * Thus based on the rows populated with the Custom Fields and its data type, a list of
    * columns is selected to be displayed and the remaining are hidden.
    *
    * E.g. Rows 1 and 2 are configured with Custom Fields while remaining are blank.
    * Thus all the Custom Fields of CUSTOM_XXX_3, CUSTOM_XXX_4 & CUSTOM_XXX_5 are to be hidden.
    * Row 1 has field with data type as String. So the other two fields i.e  CUSTOM_DATE_1 &
    * CUSTOM_NUMBER_1 will be hidden.
    * Similarly for Row 2 the String and Number ones will be hidden.
    */
   private ArrayList<String> moHideCols;

   /**
    * Holds a list of the Custom Caption for all Columns that are displayed.
    * The Captions are fetched from the Configure Worklist Details page.
    * It is built as JavaScript array with the Column Name and Caption as its elements.
    * E.g. ['DATE_1~Scheduled Date', 'NUMBER_2~Paid Amount']
    *
    * The JavaScript array is passed to method changeCaptions() on the Worklist Details
    * page which will change the Captions as defined.
    */
   private StringBuilder msbCaptions = null;

   // List of Custom Columns is constant hence initializing in a static block.
   static
   {
      moCustomCols.add("CUSTOM_STRING_1");
      moCustomCols.add("CUSTOM_DATE_1");
      moCustomCols.add("CUSTOM_NUMBER_1");
      moCustomCols.add("CUSTOM_STRING_2");
      moCustomCols.add("CUSTOM_DATE_2");
      moCustomCols.add("CUSTOM_NUMBER_2");
      moCustomCols.add("CUSTOM_STRING_3");
      moCustomCols.add("CUSTOM_DATE_3");
      moCustomCols.add("CUSTOM_NUMBER_3");
      moCustomCols.add("CUSTOM_STRING_4");
      moCustomCols.add("CUSTOM_DATE_4");
      moCustomCols.add("CUSTOM_NUMBER_4");
      moCustomCols.add("CUSTOM_STRING_5");
      moCustomCols.add("CUSTOM_DATE_5");
      moCustomCols.add("CUSTOM_NUMBER_5");
   }

   /**
    * Stores list of CUSTOM_NUMBER_X columns which represent Integers.
    * CUSTOM_NUMBER_X fields store both floating points and integers. The Column type is
    * NUMBER(19,2) i.e. float.
    * So when the values are displayed on the page they always show up with decimal point.
    * In case of Integers the display of decimal point may not be acceptable. Hence, this list
    * is maintained so that for columns with integer types the decimal can be trimmed.
    */
   private ArrayList<String> moIntCols;

   /** 
    * Stores list of CUSTOM_NUMBER_X CVL columns and its CVL table names. CUSTOM_NUMBER_X as a
    * key and its CVL table name as value i.e., < CUSTOM_NUMBER_X, CVL_XXX_XXX >
    */
   private HashMap<String, String> mhIntCVLCols;
   
   /**
    * Stores list of CUSTOM_NUMBER_X CVL columns and all CVL code and display values for each
    * CVL table. i.e., < CVL_XXX_XX, <{1=Held, 2=Ready, 3=Rejected}>, 
    *                    CVL_XX_XXX, <{1=Check, 2=Cash, 3=Draft}>,..>
    */
   private HashMap<String, HashMap<Integer, String>> mhCVLColVals; 
   /**
    * Stores list of CUSTOM_NUMBER_X columns which represent Bits.
    */
   private ArrayList<String> moBitCols;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pWF_APRV_WRK_LST_DET ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


	//{{EVENT_CODE
	//{{EVENT_T1WF_APRV_WRK_LST_DET_beforeQuery
void T1WF_APRV_WRK_LST_DET_beforeQuery(VSQuery foQuery,VSOutParam foResultSet)
{
   //Write Event Code below this line

   /*
    * The modified Query is created for the Worklist table (WF_APRV_WRK_LST). Since the
    * current page is the Worklist Details page, replace all instances of WF_APRV_WRK_LST
    * table with that of WF_APRV_WRK_LST_DET in the SQL.
    */
   modifyQuery( foQuery, foResultSet ) ;

   /**
    * The Worklist Details page displays custom columns from the Documents as configured on the
    * Configure Worklist Details page.
    * The Configure Worklist Details page used for configuring what specific Document fields
    * need to be shown along with the Worklist. It allows upto 5 configurable fields.
    *
    * Based on the Document Code entered by the user, the Custom Fields configuration is fetched
    * from the configuration page. The Columns to be shown are determined, the appropriate captions
    * are fetched and the Worklist Details page is modified to display only those columns.
    *
    * The data values for the custom fields is already populated in the Worklist Details table and
    * hence data from Documents is not needed to be fetched.
    */

   String lsDocCode = T1WF_APRV_WRK_LST_DET.getQBFDataForElement("txtT1DOC_CD");
   if(AMSStringUtil.strIsEmpty(lsDocCode))
   {
      raiseException("Document Code is required.", SEVERITY_LEVEL_ERROR);

      /*
       * When Document Code is not entered, no records should be shown.
       * Hence, modify the query such that no records are returned.
       */
      SearchRequest lsbFilter = new SearchRequest();
      StringBuilder lsbSQL = new StringBuilder();
      lsbSQL.append(" AND APRV_LVL=-1 ");
      lsbFilter.add(lsbSQL.toString());
      foQuery.addFilter(lsbFilter);

      return;
   }

   VSResultSet loRS = null;
   try
   {
      // Get the Custom Fields configuration.
      SearchRequest loSearch = new SearchRequest();
      loSearch.add(" DOC_CD ='" + AMSSQLUtil.getANSIQuotedStr(lsDocCode) + "' ");
      VSQuery loQuery = new VSQuery(getParentApp().getSession(), "R_WF_DET_CNFG", loSearch, null);
      loRS = loQuery.execute();
      VSRow loRow = loRS.first();

      /*
       * moHideCols is initially populated with all the Custom Columns. Based on the configuration
       * found, any Column to be displayed is removed from the list.
       */
      moHideCols = new ArrayList<String>(moCustomCols);
      msbCaptions = new StringBuilder("[");
      moIntCols =  new ArrayList<String>();
      mhIntCVLCols = new HashMap<String, String>();
      mhCVLColVals = new HashMap<String, HashMap<Integer, String>>();
      moBitCols =  new ArrayList<String>();

      /*
       * Whenever the configuration is changed, the Dirty Flag is set and the old Field Caption
       * and Data Type is backed up in the OLD_DOC_FLD_NM and OLD_DATA_TYP columns respectively.
       * This is done because in case a user open the Worklist Details page without running the
       * Worklist Details Sync Batch Job, it should at least display the data as per the old
       * configuration.
       * Hence these two Strings decide whether to use the Old or Current Field Captions based
       * on the Dirty Flag.
       */
      String lsColFldNm;
      String lsColDataTyp;

      /* Flag to track if Exception due to Dirty Flag has already been raised. */
      boolean lboolDirtyException = false;

      while (loRow != null)
      {
         /*
          * The Dirty check takes place in this while loop and hence for each row if a Dirty Flag
          * is found an Exception is raised.
          * In order to prevent multiple similar Information Messages, this check is added.
          * If Exception for Dirty Flag has already been raised, then do no raise an additional
          * exception.
          */
         if(loRow.getData("DIRTY_FL").getBoolean())
         {
            if(!lboolDirtyException)
            {
               lboolDirtyException = true;
               raiseException("Worklist Details configuration has changed for this Document Code." +
                     " Please run the Worklist Details Synchronization batch job.",
                     SEVERITY_LEVEL_INFO);
            }
            lsColFldNm = "OLD_DOC_FLD_NM";
            lsColDataTyp = "OLD_DATA_TYP";
            // If no Old Field Name found for current row, it means no old configuration and hence continue.
            String lsColCode = loRow.getData(lsColFldNm).getString();
            if(AMSStringUtil.strIsEmpty(lsColCode))
            {
               loRow = loRS.next();
               continue;
            }
         }
         else
         {
            lsColFldNm = "DOC_FLD_NM";
            lsColDataTyp = "DATA_TYP";
            // If no Field found for current row, it means no configuration and hence continue.
            String lsColCode = loRow.getData("DOC_FLD").getString();
            if(AMSStringUtil.strIsEmpty(lsColCode))
            {
               loRow = loRS.next();
               continue;
            }
         }


         String lsDataType = "CUSTOM_";


         String lsColCaption = loRow.getData(lsColFldNm).getString();
         int liColDataTyp = loRow.getData(lsColDataTyp).getInt();

         // Determine the Data Type of the field.
         if(liColDataTyp == DataConst.CHAR || liColDataTyp == DataConst.VARCHAR)
         {
            lsDataType += "STRING_";
         }
         else if(liColDataTyp == DataConst.DATE || liColDataTyp == DataConst.TIMESTAMP)
         {
            lsDataType += "DATE_";
         }
         else if(liColDataTyp == DataConst.BIGINT || liColDataTyp == DataConst.DECIMAL ||
               liColDataTyp == DataConst.DOUBLE || liColDataTyp == DataConst.FLOAT ||
               liColDataTyp == DataConst.INTEGER || liColDataTyp == DataConst.NUMERIC ||
               liColDataTyp == DataConst.BIT )
         {
            lsDataType += "NUMBER_";
            // If this column represents Integers, add it to the list.
            if(liColDataTyp == DataConst.BIGINT || liColDataTyp == DataConst.INTEGER)
            {
              /* if a column is CVL type, then get its CVL table name and get all the CVL display 
               * values and Store in a HashMap which can be used later while displaying
               */
              if( liColDataTyp == DataConst.INTEGER && 
                      loRow.getData("CVL_TBL_NM").getString() != null )
              {
                  String lsCVLColNm = lsDataType+loRow.getData("ROW_NO").getInt();
                  String lsCVLTblNm = loRow.getData("CVL_TBL_NM").getString();

                  if( !AMSStringUtil.strIsEmpty(lsCVLTblNm) )
                  {
                     mhIntCVLCols.put(lsCVLColNm, lsCVLTblNm);  
                     VSQuery loCVLQry = new VSQuery(getParentApp().getSession(),
                           lsCVLTblNm, "", null);
                     loRS = loCVLQry.execute();
                     VSRow loCVLRow = loRS.first();
                     VSORBSession loVSORBSession = 
                           this.getParentApp().getSession().getORBSession();
                     VSORBSessionImpl loSession = ServerEnvironment.getServer().
                        getExistingSession(loVSORBSession.getSessionID());

                     VSMetaQuery loVSMetaQry = AMSDataObject.getMetaQuery(lsCVLTblNm, 
                           loSession );
                     if( loCVLRow != null && loVSMetaQry != null )
                     {
                        VSMetaTable loVSMetaTable = 
                                loVSMetaQry.getBaseTableByTableName( lsCVLTblNm );
                        if (loVSMetaTable != null)
                        {
                           String lsStoredColNm = loVSMetaTable.getStoredColumn();
                           String lsDisplayColNm = loVSMetaTable.getDisplayColumn();
                           mhCVLColVals.put(lsCVLTblNm, new HashMap<Integer, String>());

                           while( loCVLRow != null )
                           {
                              mhCVLColVals.get( lsCVLTblNm ).put(loCVLRow.getData(lsStoredColNm).getInt(), 
                              loCVLRow.getData(lsDisplayColNm).getString().trim());
                              loCVLRow = loRS.next();
                           }
                        }
                     }
                 }
                 else
                 {
                    moIntCols.add(lsDataType+loRow.getData("ROW_NO").getInt());
                 }
              }
              else
              {
                 moIntCols.add(lsDataType+loRow.getData("ROW_NO").getInt());
              }
            }
            // If this column represents Bit, add it to the list.
            else if(liColDataTyp == DataConst.BIT )
            {
               moBitCols.add(lsDataType+loRow.getData("ROW_NO").getInt());
            }
         }
         else
         {
            loRow = loRS.next();
            continue;
         }

         /*
          * Since the Field configuration is found for this row, remove the appropriate
          * Field from the list of columns to be hidden.
          * E.g. For Field # 1, a Date field is found, hence remove CUSTOM_DATE_1 from the list.
          * Also add the Column Name and Caption to the Captions array.
          */
         moHideCols.remove(lsDataType+loRow.getData("ROW_NO").getInt());
         msbCaptions.append("'").append(lsDataType+loRow.getData("ROW_NO").getInt()).append("~"+lsColCaption+"',");

         loRow = loRS.next();

      }
      msbCaptions.replace(msbCaptions.length()-1, msbCaptions.length() , "]");

      // Add a JavaScript call for onload event to change the Captions.
      appendOnloadString("changeCaptions("+ msbCaptions +");");

   } // end try
   catch(Exception loExcp)
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

   }
   finally
   {
      if(loRS != null)
      {
         loRS.close();
      }
   }
}
//END_EVENT_T1WF_APRV_WRK_LST_DET_beforeQuery}}
//{{EVENT_pWF_APRV_WRK_LST_DET_requestReceived
void pWF_APRV_WRK_LST_DET_requestReceived( PLSRequest foReq, PageEvent foEvent )
{
	//Write Event Code below this line
   super.worklistRequestReceived( foReq, foEvent ) ;
}
//END_EVENT_pWF_APRV_WRK_LST_DET_requestReceived}}

   //{{EVENT_pWF_APRV_WRK_LST_DET_afterActionPerformed
   void pWF_APRV_WRK_LST_DET_afterActionPerformed( ActionElement ae, PLSRequest preq )
   {
      //Write Event Code below this line
      VSORBSession loORBSession = getParentApp().getSession().getORBSession();

      try
      {
         loORBSession.setProperty( "WRK_LST_DET_ACTN", "false" );
      }
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } /* End Try */
   }
   //END_EVENT_pWF_APRV_WRK_LST_DET_afterActionPerformed}}

   //{{EVENT_pWF_APRV_WRK_LST_DET_beforeActionPerformed
   void pWF_APRV_WRK_LST_DET_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      VSORBSession loORBSession = getParentApp().getSession().getORBSession();

      try
      {
         loORBSession.setProperty( "WRK_LST_DET_ACTN", "true" );
      }
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } /* End Try */

   }
   //END_EVENT_pWF_APRV_WRK_LST_DET_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1WF_APRV_WRK_LST_DET.addDBListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_APRV_WRK_LST_DET_afterActionPerformed( ae, preq );
		}
	}
	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pWF_APRV_WRK_LST_DET_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_APRV_WRK_LST_DET_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1WF_APRV_WRK_LST_DET) {
			T1WF_APRV_WRK_LST_DET_beforeQuery(query , resultset );
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
	 * Called before every data cell of the grid is generated.
	 *
	 * In the beforeQuery method, the list of Columns to be hidden is determined.
	 * Based on that list the corresponding Data Cells are hidden using this method.
	 *
	 * For each Data Cell in grid it is determined if that Column is present in the list
	 * of hidden columns and if so it is hidden.
	 *
	 * @param foTableElement Handle to the table.
	 * @param foRow The current row in the grid being processed.
	 * @param foData The current value of the data cell.
	 * @param foColumn The current grid column.
	 * @param fiOffset
	 * @return ElementSpec
	 */
	public ElementSpec beforeColumnGeneration(AMSTableElement foTableElement,
	      VSRow foRow, VSData foData, TableColumnElement foColumn, int fiOffset)
	{
	   ElementSpec loElemSpec = null;
	   MutableAttributeSet loMAS = null;
	   String lsDataField = null;
	   if(foRow != null && foRow.getMetaQuery() != null )
	   {
	      loElemSpec = foColumn.getElementSpec( foRow, foRow.getRowIndex() );
	      loMAS = (MutableAttributeSet)loElemSpec.getAttributes();
	      if(loMAS== null)
	      {
	         return loElemSpec;
	      }

	      /**
	       * CUSTOM_NUMBER_X fields store both floating points and integers. The Column type is
	       * NUMBER(19,2) i.e. float.
	       * So when the values are displayed on the page they always show up with decimal point.
	       * In case of Integers the display of decimal point may not be acceptable. Hence, check
	       * if the current column is present in the list and then trim the decimal.
	       */
	      if(foData != null && !AMSStringUtil.strIsEmpty(foData.getString()) &&
	            moIntCols.contains(foData.getMetaColumn().getName()))
	      {
	         String lsTemp = foData.getString();
	         if(lsTemp.contains(".00"))
	         {
	            lsTemp = lsTemp.split(".00")[0];
	            char[] laryData = lsTemp.toCharArray();
	            loElemSpec = new ElementSpec(loMAS, (short)3, laryData, 0, laryData.length);
	         }
	      }
          /* If any CUSTOM_NUMBER_X is mapped to CVL field then get display value for its CVL   
           * code and create loElemSpec for display value to show.
           */
          if( mhIntCVLCols != null && 
                ( foData != null && !AMSStringUtil.strIsEmpty(foData.getString()) ))
          {
             String lsCodeTableNm = (String)mhIntCVLCols.get(
             foData.getMetaColumn().getName() );
             if( lsCodeTableNm != null )
             {
                HashMap<Integer, String> loCVLCDValsTbl = mhCVLColVals.get( lsCodeTableNm );
                if( loCVLCDValsTbl != null )
                {
                   String lsCVLDispVal = loCVLCDValsTbl.get(foData.getInt());
                   moAMSLog.debug("CVL Column: +  foData.getMetaColumn().getName()"
                        + "  CVL Table name: "+lsCodeTableNm + " CVL code value:"+foData.getInt()+ 
                           " CVLDisplay Value: "+ lsCVLDispVal);

                   if( lsCVLDispVal != null )
                   {
                      char[] laryData = lsCVLDispVal.toCharArray();
                      loElemSpec = new ElementSpec(loMAS, (short)3, laryData, 0, laryData.length);
                   }
                }
             }
          }

	      /*
	       * If CUSTOM_NUMBER_X is a BIT Field then replace the values for 1/0 with true/false
	       * or Yes/No accordingly.
	       */
	      if(foData != null && !AMSStringUtil.strIsEmpty(foData.getString()) &&
	            moBitCols.contains(foData.getMetaColumn().getName()))
	      {
	         String lsTemp = foData.getString();
	         boolean lboolResDis = (lsTemp.contains("1")) ? true:false;
	         if(AMSParams.mboolDefaultYesNoDisplay)
	         {
	            lsTemp = (lboolResDis==true?"Yes":"No");
	         }
	         else
	         {
	            lsTemp = (lboolResDis==true?"true":"false");
	         }
	         char[] laryData = lsTemp.toCharArray();
	         loElemSpec = new ElementSpec(loMAS, (short)3, laryData, 0, laryData.length);
	      }

	      /*
	       * Only the Custom Columns cells contain this attribute, so if the attribute is absent
	       * it means it is some other Column and is to be ignored.
	       */
	      lsDataField = (String) loMAS.getAttribute("field-column");
	      if(lsDataField==null)
	      {
	         return loElemSpec;
	      }


	      /*
	       * To hide the data cells, the CSS class "hidden" is used.
	       * The existing CSS attribute is fetched.
	       * If it is to be hidden, then add the "hidden" class if not already present.
	       * If it is to be shown, then remove the "hidden" class if present.
	       */
	      String lsClass = (String) loMAS.getAttribute(HTML.Attribute.CLASS);
	      if(lsClass == null)
	      {
	         lsClass = "";
	      }
	      if ( moHideCols.contains(lsDataField) && !lsClass.contains("hidden") )
	      {
	         loMAS.removeAttribute(HTML.Attribute.CLASS);
	         loMAS.addAttribute(HTML.Attribute.CLASS, "hidden "+lsClass);
	      }
	      else if(!moHideCols.contains(lsDataField) && lsClass.contains("hidden"))
	      {
	         loMAS.removeAttribute(HTML.Attribute.CLASS);
	         loMAS.addAttribute(HTML.Attribute.CLASS, lsClass.replace("hidden", "").trim());
	      }

	   }
	   return loElemSpec;
	}

	/**
	 * Called at the time of page generation.
	 *
	 * Whenever the page is generated, if the list of Captions is present, then pass the
	 * JavaScript call of changing the Captions. This is needed because the original call
	 * present in beforeQuery method will not necessarily get called.
	 * Sometimes page may be regenerated without making a call to beforeQuery.
	 *
	 * Hence making an additional call here.
	 */
	public String generate()
	{
	   if(msbCaptions != null)
	   {
	      appendOnloadString("changeCaptions("+ msbCaptions +");");
	   }
	   return super.generate();
	}



   /**
    * Overrides the superclass method to return to return the Worklist Details table name.
    *
    * @return Worklist Details table name.
    */
   protected String getTableName()
   {
      return "WF_APRV_WRK_LST_DET";
   }

}