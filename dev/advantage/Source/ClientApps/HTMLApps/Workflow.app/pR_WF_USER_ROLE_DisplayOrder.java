//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.AMSReorderingPage;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;
import java.util.Hashtable;
/*
**  pR_WF_USER_ROLE_DisplayOrder
*/

//{{FORM_CLASS_DECL
public class pR_WF_USER_ROLE_DisplayOrder extends pR_WF_USER_ROLE_DisplayOrderBase

//END_FORM_CLASS_DECL}}
      implements AMSReorderingPage
{
   /**
    * Hashtable that will store key as Role ID and value as the new value for
    * Display Sequence Number.
    */
   private Hashtable mhtRecsToUpdate = new Hashtable();

   /**
    * Variable to decide whether database query should be fired for DataSource T2R_WF_USER_ROLE.
    * Note T2R_WF_USER_ROLE is used for secondary purposes, hence there are some cases when
    * db query should be fired for them and some case where db query should not be fired.
    */
    private boolean mboolFetchT2R_WF_USER_ROLE = false;

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pR_WF_USER_ROLE_DisplayOrder ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T1R_WF_USER_ROLE_beforeQuery
void T1R_WF_USER_ROLE_beforeQuery(VSQuery query ,VSOutParam resultset )
{
	//Write Event Code below this line
   String lsWhereClause = query.getSQLWhereClause();
   StringBuffer lsbFilter = new StringBuffer(32);
   VSSession loSession = getParentApp().getSession();

   /*
    * Filter R_WF_USER_ROLE records for currently logged in User and sort them
    * in ascending order of Display Sequence Number and Role Name.
    * Records will be displayed as it would appear on 'Select Worklist' dropdown
    * combobox on Worklist page.
    */
   if (lsWhereClause != null && lsWhereClause.trim().length() > 0)
   {
     lsbFilter.append(" AND ");
   }
   lsbFilter.append(" USID ");
   lsbFilter.append(AMSSQLUtil.getANSIQuotedStr( loSession.getLogin() ,
         AMSSQLUtil.EQUALS_OPER));

   SearchRequest loSrchReq = new SearchRequest();
   loSrchReq.add( lsbFilter.toString() );
   query.addFilter( loSrchReq );

   SearchRequest loOrderBy = new SearchRequest();
   loOrderBy.add("DISP_SEQ_NO,ROLE_NM");
   query.replaceSortingCriteria(loOrderBy);
}
//END_EVENT_T1R_WF_USER_ROLE_beforeQuery}}
//{{EVENT_pR_WF_USER_ROLE_DisplayOrder_beforeActionPerformed
void pR_WF_USER_ROLE_DisplayOrder_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line
   if( AMSStringUtil.strEqual( ae.getName(),"T1R_WF_USER_ROLEResetAll" ) )
   {
      //Update Display Sequence Number for all R_WF_USER_ROLE records to 99.
      resetAll();
      //Re-query to refresh the data from database
      T1R_WF_USER_ROLE.executeQuery();
   }
}
//END_EVENT_pR_WF_USER_ROLE_DisplayOrder_beforeActionPerformed}}
//{{EVENT_pR_WF_USER_ROLE_DisplayOrder_afterActionPerformed
void pR_WF_USER_ROLE_DisplayOrder_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
	//Write Event Code below this line
   //On Save action
   if( AMSStringUtil.strEqual( ae.getName(), "T1R_WF_USER_ROLESaveAll" ) )
   {
      /*
       * If source page is Worklist page, then make sure 'Select Worklist'
       * dropdown combobox on the Worklist page is re-populated with R_WF_USER_ROLE records
       * with the new display order as derived from Display Sequence Number.
       */
      reQuerySrcPage();
   }
}
//END_EVENT_pR_WF_USER_ROLE_DisplayOrder_afterActionPerformed}}
//{{EVENT_T2R_WF_USER_ROLE_beforeQuery
void T2R_WF_USER_ROLE_beforeQuery(VSQuery query ,VSOutParam resultset )
{
	//Write Event Code below this line
   //Case where database query for DataSource T2R_WF_USER_ROLE should not be fired
   if( !mboolFetchT2R_WF_USER_ROLE )
   {
      //Do not fire selection query on T2R_WF_USER_ROLE DataSource.
      resultset.setValue(true);
   }
   else
   {
      //Reset
      mboolFetchT2R_WF_USER_ROLE = false;
   }//end if

}
//END_EVENT_T2R_WF_USER_ROLE_beforeQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1R_WF_USER_ROLE.addDBListener(this);
	addPageListener(this);
	T2R_WF_USER_ROLE.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_USER_ROLE_DisplayOrder_afterActionPerformed( ae, preq );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_WF_USER_ROLE_DisplayOrder_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_WF_USER_ROLE) {
			T1R_WF_USER_ROLE_beforeQuery(query , resultset );
		}
	
		if (source == T2R_WF_USER_ROLE) {
			T2R_WF_USER_ROLE_beforeQuery(query , resultset );
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
    * Sets Display Sequence Number of current row to 1.
    * @return boolean
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet();
      VSRow loCurrRow = loResultSet.current();

      /*
       * Do nothing when any of the following condition is true:
       * 1. Current row is null
       * 2. Display Sequence Number of current row is already 1 and is on the top.
       */
      if ( ( loCurrRow == null ) || ( loCurrRow.getData("DISP_SEQ_NO").getInt()==1 ) )
      {
         return false;
      }

      /*
       * To summarize: Since Display Sequence Number on current row will be set to 1, increment
       * Display Sequence Number on rest of existing records to a higher value, if required
       * to maintain the order, i.e., get R_WF_USER_ROLE records for current User in
       * ascending order of Display Sequence Number, increment Display Sequence Number to 2 on record(s)
       * with Display Sequence Number = 1, increment Display Sequence Number to 3 on
       * record(s) with Display Sequence Number = 2 and so on. If the next highest Display Sequence Number
       * for records was 5, then no need to increment them by 1 or the rest because order is still
       * maintained without incrementing them.
       * 1->2(incremented), 2->3(incremented), 5->5(no need to increment), no need to increment rest.
       */

      /*
       * First populate a Hashtable with key as Role ID and value as the new value for
       * Display Sequence Number as explained above.
       */
      setNewDisplaySequenceNo();
      //On the Hashtable set Display Sequence Number of current row to 1.
      mhtRecsToUpdate.put( loCurrRow.getData("ROLEID").getString(), 1 );
      //Lookup Hashtable and update the new Display Sequence Number on records in database
      updateNewDisplaySequenceNo();

      //Re-query to refresh the data from database
      T1R_WF_USER_ROLE.executeQuery();

      /*
       * If source page is Worklist page, then make sure 'Select Worklist'
       * dropdown combobox on the Worklist page is re-populated with R_WF_USER_ROLE records
       * with the new display order as derived from Display Sequence Number.
       */
      reQuerySrcPage();
      return true;

   }//end of method

   /**
    * Swaps Display Sequence Number of current row with Display Sequence Number of previous row.
    * @return boolean
    */
   public boolean moveItemUp()
   {
      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet();
      VSRow loCurrRow = loResultSet.current();

      //Do nothing when current row is null
      if ( ( loCurrRow == null )  )
      {
         return false;
      }

      //Get previous row
      VSRow loPrevRow= loResultSet.previous();
      //When previous row is null then re-position cursor to current row and return.
      if( loPrevRow == null )
      {
         //Need to explicity re-position cursor to current row else cursor at position 0
         //for single row case
         loResultSet.getRowAt( loCurrRow.getRowIndex() );
         return false;
      }

      int liCurrDispSeqNo = loCurrRow.getData("DISP_SEQ_NO").getInt();
      int liPrevDispSeqNo = loPrevRow.getData("DISP_SEQ_NO").getInt();

      //When Display Sequence Number of current and previous rows are same then
      //re-position cursor at current row and return.
      if( liCurrDispSeqNo == liPrevDispSeqNo )
      {
         loResultSet.getRowAt( loCurrRow.getRowIndex() );
         return false;
      }

      //Swap Display Sequence Number between current and previous rows.
      loCurrRow.getData("DISP_SEQ_NO").setInt( liPrevDispSeqNo );
      loPrevRow.getData("DISP_SEQ_NO").setInt( liCurrDispSeqNo );
      T1R_WF_USER_ROLE.updateDataSource();

      //Re-query to refresh the data from database
      T1R_WF_USER_ROLE.executeQuery();
      /*
       * If source page is Worklist page, then make sure 'Select Worklist'
       * dropdown combobox on the Worklist page is re-populated with R_WF_USER_ROLE records
       * with the new display order as derived from Display Sequence Number.
       */
      reQuerySrcPage();
      return true;

   }//end of method

   /**
    * Swaps Display Sequence Number of current row with Display Sequence Number of next row.
    * @return boolean
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet();
      VSRow       loCurrRow   = loResultSet.current();
      VSRow       loNextRow   = loResultSet.next();

      //Do nothing when current or next row is null
      if( ( loCurrRow == null ) || ( loNextRow == null ) )
      {
         return false;
      }

      int liCurrDispSeqNo = loCurrRow.getData("DISP_SEQ_NO").getInt();
      int liNextDispSeqNo = loNextRow.getData("DISP_SEQ_NO").getInt();

      //When Display Sequence Number of current and next rows are same then
      //re-position cursor at current row and return.
      if( liCurrDispSeqNo == liNextDispSeqNo )
      {
         loResultSet.getRowAt( loCurrRow.getRowIndex()) ;
         return false;
      }

      //Swap Display Sequence Number between current and next rows.
      loCurrRow.getData("DISP_SEQ_NO").setInt( liNextDispSeqNo );
      loNextRow.getData("DISP_SEQ_NO").setInt( liCurrDispSeqNo );
      T1R_WF_USER_ROLE.updateDataSource();

      /*
       * If source page is Worklist page, then make sure 'Select Worklist'
       * dropdown combobox on the Worklist page is re-populated with R_WF_USER_ROLE records
       * with the new display order as derived from Display Sequence Number.
       */
      reQuerySrcPage();
      return true;

   }//end of method

   /**
    * Sets Display Sequence Number of current row to highest Display Sequence Number
    * i.e., (maximum Display Sequence Number of R_WF_USER_ROLE records for current User + 1).
    * @return boolean
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet();
      VSRow       loCurrRow   = loResultSet.current();

      //Do nothing when current row is null
      if( loCurrRow == null )
      {
         return false;
      }

      //Get highest(maximum) Display Sequence Number of R_WF_USER_ROLE records for current User
      int liLastDispSeqNo = getLastDispSeqNo();

      int liDispSeqNo = loCurrRow.getData("DISP_SEQ_NO").getInt();

      //Case where Display Sequence Number of current row is less than highest Display Sequence Number
      if( liDispSeqNo < liLastDispSeqNo )
      {
         //Set Display Sequence Number on current row to (maximum Display Sequence Number
         //of R_WF_USER_ROLE records for current User + 1).
         loCurrRow.getData("DISP_SEQ_NO").setInt( liLastDispSeqNo + 1 );
         T1R_WF_USER_ROLE.updateDataSource();
      }
      else if( (liDispSeqNo == 0) && (liLastDispSeqNo==0) )
      {
         //Case where Display Sequence Number of current row is null and there are no other records
         loCurrRow.getData("DISP_SEQ_NO").setInt(1);
      }

      /*
       * If source page is Worklist page, then make sure 'Select Worklist'
       * dropdown combobox on the Worklist page is re-populated with R_WF_USER_ROLE records
       * with the new display order as derived from Display Sequence Number.
       */
      reQuerySrcPage();
      return true;

   }//end of method

   /**
    * Returns highest(maximum) Display Sequence Number of R_WF_USER_ROLE records for current User.
    * @return int
    */
   private int getLastDispSeqNo()
   {
      StringBuffer lsbWhereClause = new StringBuffer(32);
      //Sort DISP_SEQ_NO in descending so that first record value will return highest value
      //of Display Sequence Number
      String lsOrderBy = "DISP_SEQ_NO DESC";
      VSQuery loQuery = null;
      VSResultSet loResultSet = null;
      VSSession loSession = getParentApp().getSession();;

      try
      {
         lsbWhereClause.append(" USID ")
               .append(AMSSQLUtil.getANSIQuotedStr( loSession.getLogin(), AMSSQLUtil.EQUALS_OPER));
         loQuery = new VSQuery(loSession, "R_WF_USER_ROLE", lsbWhereClause.toString(), lsOrderBy);
         loResultSet = loQuery.execute();
         VSRow loRow = loResultSet.first();

         //No records found
         if( loRow == null )
         {
            return 0;
         }
         else
         {
            //Return highest Display Sequence Number of R_WF_USER_ROLE records for current User.
            return loRow.getData("DISP_SEQ_NO").getInt();
         }
      }//end try
      finally
      {
         if (loQuery != null)
         {
           loQuery.close();
           loQuery = null;
         }
      }//end finally
   }//end method


   /**
    * Increment Display Sequence Number on all R_WF_USER_ROLE records for current User by 1,
    * if required to maintain order,i.e., get R_WF_USER_ROLE records for current User in
    * ascending order of Display Sequence Number, increment Display Sequence Number to 2 on record(s)
    * with Display Sequence Number = 1, increment Display Sequence Number to 3 on
    * record(s) with Display Sequence Number = 2 and so on. If the next highest Display Sequence Number
    * for records was 5, then no need to increment them by 1 or the rest because order is still
    * maintained without incrementing them.
    * 1->2(incremented), 2->3(incremented), 5->5(no need to increment), no need to increment rest.
    * The value is not actually updated to database instead a Hashtable will be populated
    * with key as Role ID and value as the new value for Display Sequence Number as explained above.
    */
   private void setNewDisplaySequenceNo()
   {
      StringBuffer lsbWhereClause = new StringBuffer(32);
      //Get R_WF_USER_ROLE records for current User in ascending order of Display Sequence Number
      String lsOrderBy = new String("DISP_SEQ_NO ASC");
      VSQuery loQuery = null;
      VSResultSet loResultSet = null;
      VSSession loSession = getParentApp().getSession();;
      //Empty Hashtable first
      mhtRecsToUpdate.clear();

      try
      {
         lsbWhereClause.append(" USID ")
               .append(AMSSQLUtil.getANSIQuotedStr( loSession.getLogin(), AMSSQLUtil.EQUALS_OPER));
         loQuery = new VSQuery(loSession, "R_WF_USER_ROLE", lsbWhereClause.toString(), lsOrderBy);

         loResultSet = loQuery.execute();

         //Start with 1
         int liDispSeqNo = 1;
         int liFetchedDispSeqNo = 0;

         VSRow loRow = loResultSet.first();

         while( loRow != null )
         {
           liFetchedDispSeqNo = loRow.getData("DISP_SEQ_NO").getInt();

            //Read method documentation to understand the logic
            if( liFetchedDispSeqNo == liDispSeqNo )
            {
               //Increment Display Sequence Number by 1
               mhtRecsToUpdate.put( loRow.getData("ROLEID").getString(), (liDispSeqNo + 1) );

               /*
                * There could be multiple records with same Display Sequence Number that needs to
                * incremented, hence do not increment liDispSeqNo until the next record has a
                * higher Display Sequence Number.
                */
               loRow = loResultSet.next();
               if( ( loRow != null ) && ( loRow.getData("DISP_SEQ_NO").getInt() > liDispSeqNo ) )
               {
                  liDispSeqNo++;
               }
               continue;
            }
            else
            if( liFetchedDispSeqNo > liDispSeqNo )
            {
               //Stop incrementing
               break;
            }
            loRow = loResultSet.next();
         }//end while
      }//end try
      finally
      {
         if(loQuery != null)
         {
            loQuery.close();
            loQuery = null;
         }
      }//end finally
   }//end of method

   /**
    * Lookup Hashtable <code>mhtRecsToUpdate</code> for the new Display Sequence Number
    * and update them on R_WF_USER_ROLE records for current User in the database.
    */
   private void updateNewDisplaySequenceNo()
   {
      /*
       * Use a different DataSource,i.e., T2R_WF_USER_ROLE to do the update because
       * the displayed DataSource,i.e.,T1R_WF_USER_ROLE cannot be used to do the update
       * because it may be a subset of R_WF_USER_ROLE records for current User when
       * User uses a Search link to look at a subset of records.
       * For update we need a DataSource that contains all R_WF_USER_ROLE records for
       * current User.
       */

      //Get latest data from database on R_WF_USER_ROLE records for current User
      mboolFetchT2R_WF_USER_ROLE = true;
      T2R_WF_USER_ROLE.executeQuery();
      VSResultSet loResultSet = T2R_WF_USER_ROLE.getResultSet();

      //Lookup Hashtable mhtRecsToUpdate for the new Display Sequence Number
      //and update them on R_WF_USER_ROLE records for current User in the database.
      Object loDispSeqNo;
      VSRow loRow = loResultSet.first();

      while( loRow != null )
      {
         loDispSeqNo = mhtRecsToUpdate.get( loRow.getData("ROLEID").getString() );
         if( loDispSeqNo!= null )
         {
            loRow.getData("DISP_SEQ_NO").setInt( ((Integer)loDispSeqNo).intValue() );
         }
         loRow = loResultSet.next();
      }//end while
      T2R_WF_USER_ROLE.updateDataSource();
   }//end of method

   /**
    * Updates Display Sequence Number for all R_WF_USER_ROLE records to 99.
    * This would allow User to set Display Sequence Number from scratch.
    */
   private void resetAll()
   {
      /*
       * Update Display Sequence Number for all R_WF_USER_ROLE records to 99.
       * Use a different DataSource,i.e., T2R_WF_USER_ROLE to do the update because
       * the displayed DataSource,i.e.,T1R_WF_USER_ROLE cannot be used to do the update
       * because it may be a subset of R_WF_USER_ROLE records for current User when
       * User uses a Search link to look at a subset of records.
       * For update we need a DataSource that contains all R_WF_USER_ROLE records for
       * current User.
       */

      //Get latest data from database on R_WF_USER_ROLE records for current User
      mboolFetchT2R_WF_USER_ROLE = true;
      T2R_WF_USER_ROLE.executeQuery();

      VSResultSet loResultSet = T2R_WF_USER_ROLE.getResultSet();
      VSRow loRow = loResultSet.first();

      while( loRow != null )
      {
         loRow.getData("DISP_SEQ_NO").setInt(99);
         loRow = loResultSet.next();
      }//end while
      T2R_WF_USER_ROLE.updateDataSource();

      /*
       * If source page is Worklist page, then make sure 'Select Worklist'
       * dropdown combobox on the Worklist page is re-populated with R_WF_USER_ROLE records
       * with the new display order as derived from Display Sequence Number.
       */
      reQuerySrcPage();

   }//end of method

   /**
    * If source page is Worklist page, then sets a property on source page that would
    * make sure 'Select Worklist' dropdown combobox on the Worklist page is re-populated with
    * R_WF_USER_ROLE records with the new display order as derived from
    * Display Sequence Number.
    */
   private void reQuerySrcPage()
   {
      VSPage loSrcPage = getSourcePage();
      if( loSrcPage instanceof advantage.Workflow.pWorklist )
      {
         ((advantage.Workflow.pWorklist)loSrcPage).setRoleDisplayOrderChanged( true );
      }
   }//end of method

}