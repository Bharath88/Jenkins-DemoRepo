//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSReorderingPage;
import com.amsinc.gems.adv.vfc.html.AMSReportTreeNode;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
/*
**  Admin_Chain_Job_List
*/
//{{FORM_CLASS_DECL
public class Admin_Chain_Job_List extends Admin_Chain_Job_ListBase

//END_FORM_CLASS_DECL}}
implements AMSReorderingPage
{

   /**
    * The code logging object
    */
   private static Log moAMSLog = AMSLogger.getLog(Admin_Chain_Job_List.class,
         AMSLogConstants.FUNC_AREA_DFLT);
   
   
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public Admin_Chain_Job_List ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }

      // Declarations for instance variables used in the form



//{{EVENT_CODE
//{{EVENT_Admin_Chain_Job_List_beforeActionPerformed
void Admin_Chain_Job_List_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line

   VSResultSet loResultSet = null;

   int liCurrSeq;
   int liLastSeq;
   int liSeqNo;
   VSRow loNextRow;
   VSRow loLastRow;

   if (ae.getName().equals("btnT3Edit_Catalog_Entry"))
   {
      VSRow loCurrentRow = getRootDataSource().getCurrentRow();
      if (loCurrentRow == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         raiseException("No row selected",SEVERITY_LEVEL_ERROR);
         return;
      }
   }
   /* Sequence numbers of chain jobs needs to be renumbered on a delete */
   else if (ae.getAction().equals("delete"))
   {

      loResultSet = getRootDataSource().getResultSet();

      int liCursor = loResultSet.cursorPosition();
      loNextRow = loResultSet.next();
      loLastRow = loResultSet.last();

      if (loNextRow == null || loLastRow == null)
      {
         return;
      }

      liCurrSeq = loNextRow.getData( "SEQ_NO" ).getInt();
      liLastSeq = loLastRow.getData( "SEQ_NO" ).getInt();

      liSeqNo = liLastSeq;

      while (liSeqNo >= liCurrSeq)
      {
         liSeqNo = liSeqNo - 1;

         loLastRow.getData("SEQ_NO").setInt(liSeqNo);

         loLastRow = loResultSet.previous();
      }

      loResultSet.getRowAt(liCursor);

   }
}

//END_EVENT_Admin_Chain_Job_List_beforeActionPerformed}}
//{{EVENT_T1R_BS_CATALOG_beforeDelete
void T1R_BS_CATALOG_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
{
   //Write Event Code below this line
   setParentNodeDirty(row);
}
//END_EVENT_T1R_BS_CATALOG_beforeDelete}}
//{{EVENT_T3Edit_Catalog_Entry_afterPageNavigation
void T3Edit_Catalog_Entry_afterPageNavigation( PageNavigation nav )
{
   //Write Event Code below this line

    /** set parentnode dirty (the flag) so that
    *  any change in the catalog label may be reflected in the
    *  report secondary navigator
    */
    VSRow loCurrRow = getRootDataSource().getCurrentRow();
    setParentNodeDirty(loCurrRow);
}
//END_EVENT_T3Edit_Catalog_Entry_afterPageNavigation}}
//{{EVENT_T3Edit_Catalog_Entry_beforePageNavigation
void T3Edit_Catalog_Entry_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line

}
//END_EVENT_T3Edit_Catalog_Entry_beforePageNavigation}}
//{{EVENT_T1R_BS_CATALOG_beforeQuery
void T1R_BS_CATALOG_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   //Write Event Code below this line
   SearchRequest loOrderBy = new SearchRequest();
   loOrderBy.add(" SEQ_NO ASC ");
   query.replaceSortingCriteria(loOrderBy);
}
//END_EVENT_T1R_BS_CATALOG_beforeQuery}}
//{{EVENT_ACJL_Page_Add_Child_Node_beforePageNavigation
void ACJL_Page_Add_Child_Node_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line

   if (getRootDataSource() != null)
   {
      getRootDataSource().getResultSet().last();
   }
}
//END_EVENT_ACJL_Page_Add_Child_Node_beforePageNavigation}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1R_BS_CATALOG.addDBListener(this);
	T3Edit_Catalog_Entry.addPageNavigationListener(this);
	ACJL_Page_Add_Child_Node.addPageNavigationListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterPageNavigation( PageNavigation obj ){
		Object source = obj;
		if (source == T3Edit_Catalog_Entry) {
			T3Edit_Catalog_Entry_afterPageNavigation( obj );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			Admin_Chain_Job_List_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T3Edit_Catalog_Entry) {
			T3Edit_Catalog_Entry_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == ACJL_Page_Add_Child_Node) {
			ACJL_Page_Add_Child_Node_beforePageNavigation( obj, cancel, newPage );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_BS_CATALOG) {
			T1R_BS_CATALOG_beforeQuery(query , resultset );
		}
	}
	public void beforeDelete( DataSource obj, VSRow row ,VSOutParam cancel ,VSOutParam response ){
		Object source = obj;
		if (source == T1R_BS_CATALOG) {
			T1R_BS_CATALOG_beforeDelete(row ,cancel ,response );
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
    * Current row's sequence no will get incremented by 1 Next rows sequence
    * no will get decremented by 1 Basically, a swap between current and
    * next rows
    *
    * @return boolean - true if the movement was successful
    */
   public boolean moveItemDown()
   {

      VSResultSet loResultSet = T1R_BS_CATALOG.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loNextRow   = loResultSet.next() ;
      int         liCurrSeq ;
      int         liNextSeq ;

      if ( ( loCurrRow == null ) || ( loNextRow == null ) )
      {
        return false ;
      }
      /*
       * end if ( ( loCurrRow == null ) || ( loNextRow == null )
       */

      liCurrSeq = loCurrRow.getData( "SEQ_NO" ).getInt() ;
      liNextSeq = loNextRow.getData( "SEQ_NO" ).getInt() ;
      loCurrRow.getData( "SEQ_NO" ).setInt( liNextSeq ) ;
      loNextRow.getData( "SEQ_NO" ).setInt( liCurrSeq ) ;

      try
      {
         T1R_BS_CATALOG.updateDataSource() ;
      } /* end try */
      catch ( VSException foExp )
      {
         T1R_BS_CATALOG.undoAll() ;
         throw foExp ;
      } /* end catch ( VSException foExp ) */
      T1R_BS_CATALOG.executeQuery();
      T1R_BS_CATALOG.getResultSet().getRowAt( liCurrRow + 1 ) ;

      return true;
   }

   /**
    * If the selected row is the last row then do nothing Else, make the
    * selected row as the last row and decrement the sequence number of all the
    * rows from the last row to the selected row by 1
    *
    * @return - true if the movement was successful
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T1R_BS_CATALOG.getResultSet();
      VSRow loCurrRow = loResultSet.current();
      VSRow loLastRow = loResultSet.last();
      VSRow loPrevRow = loResultSet.last();

      if ((loCurrRow == null)
            || (loLastRow == null)
            || (loCurrRow.getData("SEQ_NO").getInt() == loLastRow.getData(
                  "SEQ_NO").getInt()))
      {
         return false;
      }

      int liLastSeq = loLastRow.getData("SEQ_NO").getInt();
      int liCurrSeq = loCurrRow.getData("SEQ_NO").getInt();
      int liPrevSeq = liLastSeq;

      loCurrRow.getData("SEQ_NO").setInt(liLastSeq);

      // position result set at last
      loPrevRow = loResultSet.last();

      while (liPrevSeq > liCurrSeq)
      {
         liPrevSeq = liPrevSeq - 1;
         loPrevRow.getData("SEQ_NO").setInt(liPrevSeq);
         loPrevRow = loResultSet.previous();
      }

      try
      {
         T1R_BS_CATALOG.updateDataSource();
      } /* end try */
      catch (VSException foExp)
      {
         T1R_BS_CATALOG.undoAll();
         throw foExp;
      } /* end catch ( VSException foExp ) */
      T1R_BS_CATALOG.executeQuery();
      T1R_BS_CATALOG.last();
      return true;

   }

   /**
    * If the selected row is the first row then do nothing Else, make the
    * selected row as the first row and increment the sequence number of all the
    * rows from the first row to the selected row by 1
    *
    * @return - true if the movement was successful
    */
   public boolean moveItemToTop()
   {

      VSResultSet loResultSet = T1R_BS_CATALOG.getResultSet();
      VSRow loCurrRow = loResultSet.current();
      VSRow loFirstRow = loResultSet.first();
      VSRow loNextRow = null;

      if ((loCurrRow == null) || (loFirstRow == null)
            || (loCurrRow == loFirstRow))
      {
         return false;
      }
      /*
       * end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || (
       * loFirstRow == loCurrRow ) )
       */

      int liFirstSeq = loFirstRow.getData("SEQ_NO").getInt();
      int liCurrSeq = loCurrRow.getData("SEQ_NO").getInt();
      int liNextSeq = liFirstSeq;

      loCurrRow.getData("SEQ_NO").setInt(liFirstSeq);

      // position result set at first
      loNextRow = loResultSet.first();

      while (liNextSeq < liCurrSeq)
      {
         liNextSeq = liNextSeq + 1;
         loNextRow.getData("SEQ_NO").setInt(liNextSeq);

         loNextRow = loResultSet.next();
      }

      try
      {
         T1R_BS_CATALOG.updateDataSource();
      } /* end try */
      catch (VSException foExp)
      {
         T1R_BS_CATALOG.undoAll();
         throw foExp;
      } /* end catch ( VSException foExp ) */
      T1R_BS_CATALOG.executeQuery();

      return true;
   }

   /**
    * Current row's sequence no will get decremented by 1 Previous rows sequence
    * no will get incremented by 1 Basically, a swap between current and
    * previous rows
    *
    * @return boolean - true if the movement was successful
    */
   public boolean moveItemUp()
   {
      VSResultSet loResultSet = T1R_BS_CATALOG.getResultSet();
      int liCurrRow = loResultSet.cursorPosition();
      VSRow loCurrRow = loResultSet.current();
      VSRow loPrevRow = loResultSet.previous();
      int liCurrSeq;
      int liPrevSeq;

      if ((loCurrRow == null) || (loPrevRow == null))
      {
         T1R_BS_CATALOG.first();
         return false;
      }
      /*
       * end if ( ( loCurrRow == null ) || ( loPrevRow == null )
       */

      liCurrSeq = loCurrRow.getData("SEQ_NO").getInt();
      liPrevSeq = loPrevRow.getData("SEQ_NO").getInt();
      loCurrRow.getData("SEQ_NO").setInt(liPrevSeq);
      loPrevRow.getData("SEQ_NO").setInt(liCurrSeq);

      try
      {
         T1R_BS_CATALOG.updateDataSource();
      } /* end try */
      catch (VSException foExp)
      {
         T1R_BS_CATALOG.undoAll();
         throw foExp;
      } /* end catch ( VSException foExp ) */
      T1R_BS_CATALOG.executeQuery();
      T1R_BS_CATALOG.getResultSet().getRowAt(liCurrRow - 1);
      return true;
   }


   public boolean useVirtualResultSet( AMSDataSource foDataSource )
   {
         return false ;
   }  /* end useVirtualResultSet() */


    /**
     * marks Report Node Dirty so thats is regenerated the next time
    */
   private void setParentNodeDirty(VSRow row)
   {
      try
      {
         long llNodeID = row.getData("PNT_ID").getLong();
         String lsNodeID = String.valueOf(llNodeID);

         VSORBSession loCurrentSession = parentApp.getSession().getORBSession();
         String lsTreePageID = loCurrentSession.getProperty("RSAA_TREE_PG_ID");
         // get Report Tree Page from PLSApp list
         ReportTreePage loTreePage = (ReportTreePage) (getParentApp()
               .getPageFromList(lsTreePageID));
         if (loTreePage.mhNodes.containsKey(lsNodeID))
         {
            AMSReportTreeNode loNode = (AMSReportTreeNode) (loTreePage.mhNodes
                  .get(lsNodeID));
            loNode.setChange(true);
         }
      } /* end try */
      catch (RemoteException foExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         return;
      } /* end catch( RemoteException foExp ) */
   } // of setParentNodeDirty

}