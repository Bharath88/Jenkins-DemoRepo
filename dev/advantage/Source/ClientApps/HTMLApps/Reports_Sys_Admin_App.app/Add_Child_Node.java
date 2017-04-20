// {{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
// END_IMPORT_STMTS}}

import java.util.*;
import com.amsinc.gems.adv.vfc.html.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import advantage.AMSStringUtil;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;

import org.apache.commons.logging.Log;

/*
 * * Add_Child_Node
 */

// {{FORM_CLASS_DECL
public class Add_Child_Node extends Add_Child_NodeBase

// END_FORM_CLASS_DECL}}
{
  
   /**
    * The code logging object
    */
   private static Log moAMSLog = AMSLogger.getLog(Add_Child_Node.class,
         AMSLogConstants.FUNC_AREA_DFLT);
   
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
   // {{FORM_CLASS_CTOR
public Add_Child_Node ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE
   // {{EVENT_T1R_BS_CATALOG_beforeSave
   void T1R_BS_CATALOG_beforeSave(VSRow row, VSOutParam cancel)
   {
      VSResultSet loSeqNoRS = null;
      try
      {
         VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
         String lsCatalogId = loCurrentSession.getProperty("RSAA_NODE_ID");
         // db("The current Node id " + lsCatalogId);
         row.getData("PNT_ID").setString(lsCatalogId);

         /*
          * For chain jobs, the sequence number needs to be set to the next
          * available sequence number when a new job is added to the chain
          */

         VSQuery loQuery = new VSQuery(getParentApp().getSession(),
               "R_BS_CATALOG", " PNT_ID = " + lsCatalogId,
               "SEQ_NO DESC");

         int liNextSeqNo = 1;
         VSData loCurrSeqNo;

         loSeqNoRS = loQuery.execute();
         if (loSeqNoRS.first() != null)
         {

            loCurrSeqNo = loSeqNoRS.first().getData("SEQ_NO");

            if (loCurrSeqNo != null)
            {
               liNextSeqNo = loCurrSeqNo.getInt() + 1;
            }
         } /* end if ( loSeqNoRS.first() != null ) */


         row.getData("SEQ_NO").setInt(liNextSeqNo);

      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException("Unable to get message properties",
               SEVERITY_LEVEL_SEVERE);
         return;
      } /* end catch( RemoteException foExp ) */
      finally
      {
         if (loSeqNoRS != null)
         {
            loSeqNoRS.close();
         }
      }
   }

   // END_EVENT_T1R_BS_CATALOG_beforeSave}}
   // {{EVENT_T1R_BS_CATALOG_afterSave
   void T1R_BS_CATALOG_afterSave(VSRow row)
   {
      try
      {
         VSORBSession loCurrentSession = parentApp.getSession().getORBSession();
         String lsTreePageID = loCurrentSession.getProperty("RSAA_TREE_PG_ID");
         String lsCatalogIdToSetDirty = loCurrentSession.getProperty("RSAA_NODE_ID");

         // get Report Tree Page from PLSApp list
         ReportTreePage loTreePage = (ReportTreePage)(getParentApp().getPageFromList(lsTreePageID) );


         if (loTreePage.mhNodes.containsKey(lsCatalogIdToSetDirty))
         {
         AMSReportTreeNode loNode=(AMSReportTreeNode)(loTreePage.mhNodes.get(lsCatalogIdToSetDirty) );
            loNode.setChange(true);
         }
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException("Unable to get message properties",
               SEVERITY_LEVEL_SEVERE);
         return;
      } /* end catch( RemoteException foExp ) */
   }

   // END_EVENT_T1R_BS_CATALOG_afterSave}}
   // {{EVENT_Add_Child_Node_beforeGenerate
   void Add_Child_Node_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
   {

      // Write Event Code below this line
      VSRow loCurrRow = getRootDataSource().getCurrentRow();
      VSSession loCurrSession = getParentApp().getSession();
      VSORBSession loORBSession = loCurrSession.getORBSession();
      /* Initialize to dummy value */
      long llPntCtlgID = -1;
      try
      {
         /* Get Parent Catalog ID */
         llPntCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
      }// end try
      catch (Exception loExcp)
      {
         /* Failed to obtain Parent Catalog ID from Session properties */
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Failed to obtain Job information from Session properties",
         AMSPage.SEVERITY_LEVEL_ERROR );
         return;
      }

      /* Obtain Resource ID of the parent Catalog entry */
      String lsPntRsrcID = AMSPLSUtil.getSecRsrcIDForJob(loCurrSession,
            llPntCtlgID);
      if( !AMSStringUtil.strIsEmpty( lsPntRsrcID ) )
      {
         /*
          * Infer Resource ID from the parent Catalog entry and display an
          * informational message to indicate the same.
          */
         loCurrRow.getData("RSRC_ID").setString(lsPntRsrcID);
         raiseException("Resource ID will default from immediate Parent",
               AMSPage.SEVERITY_LEVEL_INFO);
      }

   }

   // END_EVENT_Add_Child_Node_beforeGenerate}}

   // END_EVENT_CODE}}

   public void addListeners() {
      // {{EVENT_ADD_LISTENERS

      T1R_BS_CATALOG.addDBListener(this);
      addPageListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE
   public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
        Object source = obj;
        if (source == this ) {
            Add_Child_Node_beforeGenerate(docModel, cancel, output);
        }
   }

    public void afterSave( DataSource obj, VSRow row ){
        Object source = obj;
        if (source == T1R_BS_CATALOG) {
            T1R_BS_CATALOG_afterSave(row );
        }
    }

    public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
        Object source = obj;
        if (source == T1R_BS_CATALOG) {
            T1R_BS_CATALOG_beforeSave(row ,cancel );
        }
    }

   // END_EVENT_ADAPTER_CODE}}

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
