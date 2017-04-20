//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.* ;
import com.amsinc.gems.adv.vfc.html.* ;
import javax.swing.text.*;
import javax.swing.text.html.*;
import versata.vls.*;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import java.rmi.RemoteException;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;


import org.apache.commons.logging.Log;

           /*
**  Catalog_LeafNode_Pg
*/

//{{FORM_CLASS_DECL
public class Catalog_LeafNode_Pg extends Catalog_LeafNode_PgBase

//END_FORM_CLASS_DECL}}
{
        // Declarations for instance variables used in the form
     // public static final String JADE_REPORT_CLASS            = "Report" ;
        //public static final String SYSTEM_BATCH_CLASS                 = "SystemBatch";
                                       // This is the constructor for the generated form. This also constructs
        // all the controls on the form. Do not alter this code. To customize paint
        // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Catalog_LeafNode_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
        }

   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Catalog_LeafNode_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

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
   //Sets the parent node dirty
   setParentNodeDirty(row);
}
//END_EVENT_T1R_BS_CATALOG_afterSave}}
//{{EVENT_Catalog_LeafNode_Pg_beforeActionPerformed
void Catalog_LeafNode_Pg_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{

   // trigger only if Catalog Entry deleted and not Catalog Parameter entry
   if (ae.getAction().equals("delete") && ae.getName().equals("T1R_BS_CATALOGdelete") )
   {

      String lsConfirmDelete = preq.getParameter("ConfirmDelete"); //  done in Batch.js
      if (lsConfirmDelete.equals("No"))
      {
           evt.setNewPage(this);
          evt.setCancel(true);
      }

  }




}
//END_EVENT_Catalog_LeafNode_Pg_beforeActionPerformed}}
//{{EVENT_T1R_BS_CATALOG_beforeDelete
void T1R_BS_CATALOG_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
{
   setParentNodeDirty(row);
}
//END_EVENT_T1R_BS_CATALOG_beforeDelete}}
// DELETED_BEGIN 
//{{EVENT_T3Jobs_Completed_Admin_beforePageNavigation
void T3Jobs_Completed_Admin_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
    try
    {
       VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
       String lsNodeID = loCurrentSession.getProperty("RSAA_NODE_ID");
       String lsWhereClause ="PNT_CTLG_ID = " + lsNodeID;
       String lsAddWhereClause = "RUN_STA ="+ AMSBatchConstants.STATUS_COMPLETE;
       String lsFinalWhereClause = "( "+ lsWhereClause + ") AND (" + lsAddWhereClause + ")";
       nav.setDevWhere(lsFinalWhereClause);
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
//END_EVENT_T3Jobs_Completed_Admin_beforePageNavigation}}
//{{EVENT_T4Jobs_Pending_Admin_beforePageNavigation
void T4Jobs_Pending_Admin_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   try
   {
      VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
      String lsNodeID = loCurrentSession.getProperty("RSAA_NODE_ID");
      String lsWhereClause ="PNT_CTLG_ID =" + lsNodeID ;
      String lsAddWhereClause = "RUN_STA <>" + AMSBatchConstants.STATUS_COMPLETE;
      String lsFinalWhereClause = "( "+ lsWhereClause + ") AND (" + lsAddWhereClause + ")";
      nav.setDevWhere(lsFinalWhereClause);

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
//END_EVENT_T4Jobs_Pending_Admin_beforePageNavigation}}
//{{EVENT_T5Report_Completed_Admin_beforePageNavigation
void T5Report_Completed_Admin_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   try
   {
       VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
       String lsNodeID = loCurrentSession.getProperty("RSAA_NODE_ID");
       String lsWhereClause ="PNT_CTLG_ID =" + lsNodeID ;
       String lsAddWhereClause = "RUN_STA =" + AMSBatchConstants.STATUS_COMPLETE;
       String lsFinalWhereClause = "( "+ lsWhereClause + ") AND (" + lsAddWhereClause + ")";
       nav.setDevWhere(lsFinalWhereClause);
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
//END_EVENT_T5Report_Completed_Admin_beforePageNavigation}}
 // DELETED_END 

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
			Catalog_LeafNode_Pg_beforeActionPerformed( ae, evt, preq );
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
     * Makes the reportTreePage load the parent node again
     *  all changes are reflected in the reloaded node objects
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
    } // setParentNodeDirty


    /**
     * For debugging
     */
    private void db(String fsMsg)
    {
       if( AMS_PLS_DEBUG )
       {
         System.err.println(fsMsg);
       }
    }


    private String getSystemBatchNavigationName()
    {
       return new String("T3Jobs_Completed_Admin");
    }


    private String getReportNavigationName()
    {
       return new String("T5Report_Completed_Admin");
    }

}
