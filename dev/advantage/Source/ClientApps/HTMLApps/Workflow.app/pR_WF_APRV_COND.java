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
	import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;


import org.apache.commons.logging.Log;
	/*
	**  pR_WF_APRV_COND
	*/

	//{{FORM_CLASS_DECL
	public class pR_WF_APRV_COND extends pR_WF_APRV_CONDBase
	
	//END_FORM_CLASS_DECL}}
	{
	   // Declarations for instance variables used in the form

	      public DataSource moDataSource = null ;
          //Stores the number when LHS value is changed.
	      private String msRefreshColumn=null;
	      /* Constant indicating dot character*/
	      private static final char DOT_CHAR = '.';
	      /* Constant indicating under score character*/
	      private static final char UNDER_SCORE_CHAR = '_';
	      /* Constant indicating the header component suffix*/
	      private static final String  HDR_SUFFIX= "_DOC_HDR";
	      /** This is the logger object */
	      private static Log moAMSLog = AMSLogger.getLog( pR_WF_APRV_COND.class,
	         AMSLogConstants.FUNC_AREA_WORKFLOW ) ;



	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pR_WF_APRV_COND ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_pR_WF_APRV_COND_beforeActionPerformed
void pR_WF_APRV_COND_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
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
//END_EVENT_pR_WF_APRV_COND_beforeActionPerformed}}

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
			pR_WF_APRV_COND_beforeActionPerformed( ae, evt, preq );
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

	   public void setDataSource( DataSource foDataSource )
	      {
	         moDataSource = foDataSource ;
	   }

	   /**
       * This method populates the LHS condition combo boxes and based on the selected value in
       * LHS condition combo boxes the RHS field combo box will be populated.
	    * @param foThisPage the current page
	    *
	    * @author Michael Levy
	    */

	   public void beforeGenerate()
	   {
	      VSSession loSession = null;
	      String lsDocCd = null;
          DataSource loCondDataSource=null;
          VSRow loCondRow =null;
          try
          {
             loCondDataSource = getRootDataSource();
             loCondRow = loCondDataSource.getCurrentRow();

	         loSession = loCondDataSource.getSession();
	         lsDocCd = loCondRow.getData("DOC_CD").getString().trim();
	      }
	      catch (Exception loEx)
	      {
	         return;       // blank page
	      }

	      try
	      {
	         if ((lsDocCd == null) || lsDocCd.equals(""))
	         {
	            return;    // blank page
	         }
	         else
	         {
	            VSResultSet loFldResultSet = null;
	            VSRow loFldRow = null;

	            VSQuery loFldQuery = new VSQuery(loSession, "R_WF_APRV_FLD",
	                                             "DOC_CD = " +
	                                             AMSSQLUtil.getANSIQuotedStr(lsDocCd,true), "");
	            String[] lsFldArray = null;
	            int liFldArrayLength = 0;

	            loFldResultSet = loFldQuery.execute();
	            loFldResultSet.last();
	            liFldArrayLength = loFldResultSet.getRowCount();
	            lsFldArray = new String[liFldArrayLength + 1];
	            loFldRow = loFldResultSet.first();
	            lsFldArray[0]="";

	            for (int liFldCnt = 1; loFldRow != null; liFldCnt++)
	            {
	               lsFldArray[liFldCnt] = loFldRow.getData("COMB_DOC_AT").getString();
	               loFldRow = loFldResultSet.next();
	            }
	            loFldResultSet.close();

	            if (liFldArrayLength >= 0)
	            {
	               ComboBoxElement[] loComboBox = new ComboBoxElement[5];

	               for (int liAndCnt = 0; liAndCnt < 5; liAndCnt++)
	               {
	                  loComboBox[liAndCnt] = (ComboBoxElement)
	                        getElementByName("txtT1AND_COND_LHS_" + (liAndCnt + 1));
	                  if (loComboBox[liAndCnt] != null)
	                  {
	                     loComboBox[liAndCnt].removeAllElements();
	                     for (int liFldCnt = 0; liFldCnt <= liFldArrayLength; liFldCnt++)
	                     {
	                        if (loComboBox[liAndCnt].addElement(lsFldArray[liFldCnt]) ==
	                            null)
	                        {
	                           break;
	                        }
	                     } // end for
	                  }
	               } // end for

                   if(!AMSStringUtil.strIsEmpty(msRefreshColumn))
                   {
                     /*This stores the right hand side field values corresponding
                      *to a left hand side value
                      */
                     System.err.println("msRefreshColumn = " + msRefreshColumn);

                     String[] lsRHSFields=null;
                     //This is the left hand side field value.
                     String lsLHS=loCondRow.getData("AND_COND_LHS_"+
                                                   Integer.parseInt(msRefreshColumn)).getString();
                     /*This contains the values for right hand side field based on the component
                      *selected in left hand side.
                     */
                     ComboBoxElement loRHSComboBox=(ComboBoxElement)getElementByName(
                                                   "txtT1AND_COND_RHS_FLD_"+
                                                   Integer.parseInt(msRefreshColumn));
                     loRHSComboBox.removeAllElements();
                     //get left hand side component.
                     String lsLHSComp=getLHSComp(lsLHS);
                     String lsHdrCompName=getHDRCompFromLHSComp(lsLHSComp);
                     StringBuffer lsbWhereClause=new StringBuffer(100);
                     lsbWhereClause.append(" DOC_CD = ");
                     lsbWhereClause.append(AMSSQLUtil.getANSIQuotedStr(lsDocCd,true));
                     lsbWhereClause.append(" AND (");
                     if(!AMSStringUtil.strIsEmpty(lsLHSComp) && !lsLHSComp.equals(lsHdrCompName))
                     {
                        lsbWhereClause.append("  DOC_COMP= ");
                        lsbWhereClause.append(AMSSQLUtil.getANSIQuotedStr(lsLHSComp,true));
                        lsbWhereClause.append(" OR ");
                     }
                     lsbWhereClause.append("  DOC_COMP= ");
                     lsbWhereClause.append(AMSSQLUtil.getANSIQuotedStr(lsHdrCompName,true));
                     lsbWhereClause.append(" ) ");

                     try
                     {
                        //This query retrieves all the field related to a component plus header component.
                        VSQuery loAllFldQuery = new VSQuery(loSession, "R_WF_APRV_FLD",lsbWhereClause.toString(), "");
                        loFldResultSet=loAllFldQuery.execute();
                        lsRHSFields=getFields(loFldResultSet,"COMB_DOC_AT");
                     }
                     finally
                     {
                        if(loFldResultSet!=null)
                        {
                           loFldResultSet.close();
                        }
                     }
                     for(int liRHSFieldsCnt=0;liRHSFieldsCnt<lsRHSFields.length;liRHSFieldsCnt++)
                     {
                        if (loRHSComboBox.addElement(lsRHSFields[liRHSFieldsCnt])==null)
                        {
                           break;
                        }
                     }//end for
                  }//end if(msRefreshColumn!=null)
	            } // end if (liFldArrayLength ...
	         } // end else
	      }
	      catch (Exception loEx)
	      {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loEx);

            raiseException("Error populating left hand side/right hand side condition lists.",
	                        SEVERITY_LEVEL_ERROR);
	      }

	   } // end beforeGenerate()

	   /**
      * This method returns the header component of the document from the some other component.eg
      * CR_DOC_ACTG is the component name passed as a parameter then the header component will be CR_DOC_HDR.
      *
      *
      * @param lsCompName this is the some other component of the doc.
      * @return the header component of the document.
      */
      private String getHDRCompFromLHSComp(String fsCompName)
      {
         String lsHDRComp=null;
         int liFirstIndex=-1;

         liFirstIndex=fsCompName.indexOf(UNDER_SCORE_CHAR);
         lsHDRComp=fsCompName.substring(0,liFirstIndex)+HDR_SUFFIX;
         return lsHDRComp;
      }//end getHDRCompFromLHSComp()

      /**
       * This method returns the Left Hand Side Field Component from the Left Hand Side Field.
       * @param fsLHS - Left Hand Side Field
       * @return - the Left Hand Side Field Component
      */
      private String getLHSComp(String fsLHS)
      {
         int lindex=fsLHS.indexOf(DOT_CHAR);
         if(lindex>0)
         {
            return fsLHS.substring(0,lindex);
         }
         return null;
      }//end getLHSComp()

      /**
       * This method return all the data from  a particular Fields (Table Column).
       * @param foFldResultSet - Result set from a already executed query.
       * @param foField - Table Column for which data will be retrieved.
       * @return - all the data from  a particular Field.
       */
      private String[] getFields(VSResultSet foFldResultSet,String fsField)
      {
         String[] lsFldArray = null;
         VSRow loFldRow = null;
         int liFldArrayLength = 0;
         foFldResultSet.last();
         liFldArrayLength = foFldResultSet.getRowCount();
         lsFldArray = new String[liFldArrayLength + 1];
         loFldRow = foFldResultSet.first();
         for (int liFldCnt = 0; loFldRow != null; liFldCnt++)
         {
            lsFldArray[liFldCnt] = loFldRow.getData(fsField).getString();
            loFldRow = foFldResultSet.next();
         }
         return lsFldArray;
      }//end getFields()


	   /**
	    * This method returns true so that changing the document code
	    * will cause an accept data.
	    */
	   public boolean acceptDataOnSyncCurrency( DataSource foDSSync, PLSRequest foPLSReq )
	   {
	      return true ;
	   } /* end acceptDataOnSyncCurrency() */

      /**
       * This method is used to store the index of the LHS field that has been modified.
       * @param foPLSReq
       * @param foPageEvent
       */
      public void requestReceived( PLSRequest foPLSReq, PageEvent foPageEvent )
      {
         super.requestReceived(foPLSReq,foPageEvent);
         msRefreshColumn=foPLSReq.getParameter("RHSRefreshColumn");
      } /* end requestReceived() */

	}