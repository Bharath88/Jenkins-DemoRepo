//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.*;
import java.util.* ;
import com.amsinc.gems.adv.vfc.html.* ;
import javax.swing.text.*;
import javax.swing.text.html.*;

import org.apache.commons.logging.Log;

import versata.vls.*;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import java.rmi.RemoteException;
/*
 **  Catalog_ChainJob_Pg
 */

//{{FORM_CLASS_DECL
public class Catalog_ChainJob_Pg extends Catalog_ChainJob_PgBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
      // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Catalog_ChainJob_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
//{{FORM_CLASS_CTOR
public Catalog_ChainJob_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }
//{{EVENT_CODE
//{{EVENT_T2BS_CATALOG_PARM_beforeSave
   void T2BS_CATALOG_PARM_beforeSave(VSRow row ,VSOutParam cancel )
   {
      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
         String lsCatalogId = loCurrentSession.getProperty("RSAA_NODE_ID");

         row.getData("CTLG_ID").setString(lsCatalogId);
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to get message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */

   }

//END_EVENT_T2BS_CATALOG_PARM_beforeSave}}
//{{EVENT_T1R_BS_CATALOG_afterSave
   void T1R_BS_CATALOG_afterSave(VSRow row )
   {
      try
       {
         //Write Event Code below this line

         long llNodeID = row.getData("PNT_ID").getLong();
         String lsNodeID = String.valueOf(llNodeID);

         VSORBSession loCurrentSession = parentApp.getSession().getORBSession();
         String lsTreePageID = loCurrentSession.getProperty("RSAA_TREE_PG_ID");

         //get Report Tree Page from PLSApp list
         ReportTreePage loTreePage = (ReportTreePage)(getParentApp().getPageFromList(lsTreePageID) );

         if (loTreePage.mhNodes.containsKey(lsNodeID) )
         {
            AMSReportTreeNode loNode=(AMSReportTreeNode)(loTreePage.mhNodes.get(lsNodeID) );
            loNode.setChange(true);
         }
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to get message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */
   }

//END_EVENT_T1R_BS_CATALOG_afterSave}}
//{{EVENT_Catalog_ChainJob_Pg_beforeActionPerformed
   void Catalog_ChainJob_Pg_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      //Write Event Code below this line

      if (ae.getAction().equals("delete") && ae.getName().equals("T1R_BS_CATALOGdelete") )
      {
         String lsConfirmDelete = preq.getParameter("ConfirmDelete"); // through Batch.js

         if (lsConfirmDelete.equals("No"))

         {
            evt.setNewPage(this);
            evt.setCancel(true);
         }

      }
   }

//END_EVENT_Catalog_ChainJob_Pg_beforeActionPerformed}}
//{{EVENT_T1R_BS_CATALOG_beforeDelete
   void T1R_BS_CATALOG_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
   {
      //Write Event Code below this line

      deleteChildren(row);
   }

//END_EVENT_T1R_BS_CATALOG_beforeDelete}}

//END_EVENT_CODE}}
      public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T2BS_CATALOG_PARM.addDBListener(this);
	T1R_BS_CATALOG.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }
//{{EVENT_ADAPTER_CODE

	public void afterSave( DataSource obj, VSRow row ){
		Object source = obj;
		if (source == T1R_BS_CATALOG) {
			T1R_BS_CATALOG_afterSave(row );
		}
	}
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T2BS_CATALOG_PARM) {
			T2BS_CATALOG_PARM_beforeSave(row ,cancel );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			Catalog_ChainJob_Pg_beforeActionPerformed( ae, evt, preq );
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
    * Sets Report Node dirty such that it is regenerated
    * next
    */
   private void setParentNodeDirty(VSRow row)
   {
      try
      {
         long llNodeID = row.getData("PNT_ID").getLong();
         String lsNodeID = String.valueOf(llNodeID);
         VSORBSession loCurrentSession = parentApp.getSession().getORBSession();
         String lsTreePageID = loCurrentSession.getProperty("RSAA_TREE_PG_ID");

         //get Report Tree Page from PLSApp list
         ReportTreePage loTreePage = (ReportTreePage)(getParentApp().getPageFromList(lsTreePageID) );
         if (loTreePage.mhNodes.containsKey(lsNodeID) )
         {
            AMSReportTreeNode loNode=(AMSReportTreeNode)(loTreePage.mhNodes.get(lsNodeID) );
            loNode.setChange(true);
         }
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         return ;
      } /* end catch( RemoteException foExp ) */
   }


   /**
    * Deletes all the chaild chain jobs for the parent chain job entry
    */
   private void deleteChildren(VSRow row)
   {
      VSResultSet loRS =null;
      try
      {
         VSSession loCurrentSession = getParentApp().getSession();
         String lsCatalogID = row.getData("CTLG_ID").getString();
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("PNT_ID =" + lsCatalogID );
         VSQuery loQuery = new VSQuery(loCurrentSession,"R_BS_CATALOG",searchReq,null);
         loRS =loQuery.execute();
         VSRow loRow = loRS.last();
         int cnt = loRS.getRowCount();
         for(int i=1; i<= cnt;i++)
         {
            VSRowBasic loJobRow = (VSRowBasic)loRS.getRowAt(i);
            loJobRow.deleted(true);
            loJobRow.save();
         }
      }
      catch(Throwable loExcp)
      {
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
      setParentNodeDirty(row);
   } // end of delete children


    /*
     * For debugging
     */
    private void db(String fsMsg)
    {
       if( AMS_PLS_DEBUG )
       {
         System.err.println(fsMsg);
       }
    }

}
