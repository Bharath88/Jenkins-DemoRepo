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
import versata.vls.*;

import javax.swing.text.*;
import javax.swing.text.html.HTML.*;
import javax.swing.text.html.*;
import javax.swing.text.AbstractDocument.* ;

import com.amsinc.gems.adv.common.AMSBatchConstants;

import org.apache.commons.logging.Log;

import com.amsinc.gems.adv.common.*;

import advantage.AMSDataObject;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import advantage.AMSBatchUtil;

/*
 **  ReportTreePage
 */

//{{FORM_CLASS_DECL
public class ReportTreePage extends ReportTreePageBase

//END_FORM_CLASS_DECL}}
implements AMSBatchConstants
{
   // Declarations for instance variables used in the form


   public static final String FOLDER_CLASS         = "Folder" ;
   public static final String JADE_REPORT_CLASS       = "Report" ;
   public static final String ACTUATE_REPORT_CLASS    = "ActuateReport" ;
   public static final String SYSTEM_BATCH_CLASS   = "SystemBatch";
   public static final String CHAIN_JOB_CLASS         = "ChainJob";
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( ReportTreePage.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;


   protected AMSReportTreeElement treeElement =null;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ReportTreePage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

   }




//{{EVENT_CODE
//{{EVENT_ReportTreePage_pageCreated
   void ReportTreePage_pageCreated()
   {
      buildTree();
      setADVUIType("PageStdUI");
   }

//END_EVENT_ReportTreePage_pageCreated}}
//{{EVENT_ReportTreePage_beforeActionPerformed
   void ReportTreePage_beforeActionPerformed( ActionElement foActnElem, PageEvent foPgEvt, PLSRequest foPLSReq )
   {
      String lsActnNm = foActnElem.getName() ;

      if ( mhNodes.containsKey( lsActnNm ) )
      {
         AMSReportTreeNode loNode       = (AMSReportTreeNode)mhNodes.get( lsActnNm ) ;
         long              llNodeID     = loNode.getUniqueNodeID() ;
         String            lsNodeClass  = loNode.getClassName() ;
         String            lsNodePkg    = loNode.getPackageName() ;
         int               liNodeType   = loNode.getItemType() ;
         String            lsNodeLabel  = loNode.getLabel() ;
         VSORBSession      loORBSession = getParentApp().getSession().getORBSession() ;

         try
         {
            loORBSession.setProperty( RSAA_NODE_ID, String.valueOf( llNodeID ) ) ;
            loORBSession.setProperty( RSAA_AGNT_CLS_NM,
                  ( lsNodeClass != null ) ? lsNodeClass : "" ) ;
            loORBSession.setProperty( RSAA_PKG_NM,
                  ( lsNodePkg != null ) ? lsNodePkg : "" ) ;
            loORBSession.setProperty( RSAA_CTLG_LBL,
                  ( lsNodeLabel != null ) ? lsNodeLabel : "" ) ;

            switch ( liNodeType )
            {
               case AMSBatchConstants.FOLDER:
                  loORBSession.setProperty( RSAA_ITM_TYP, FOLDER_CLASS ) ;
                  break ;

               case AMSBatchConstants.REPORT:
                  loORBSession.setProperty( RSAA_ITM_TYP, JADE_REPORT_CLASS ) ;
                  break ;

               case AMSBatchConstants.ACTUATE_REPORT:
                  loORBSession.setProperty( RSAA_ITM_TYP, ACTUATE_REPORT_CLASS ) ;
                  break ;

               case AMSBatchConstants.SYSTEM_BATCH:
                  loORBSession.setProperty( RSAA_ITM_TYP, SYSTEM_BATCH_CLASS ) ;
                  break ;

               case AMSBatchConstants.CHAIN_JOB:
                  loORBSession.setProperty( RSAA_ITM_TYP, CHAIN_JOB_CLASS ) ;
                  break ;
            } /* end switch ( liNodeType ) */

         } /* end try */
         catch( RemoteException foExp )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

            raiseException( "Unable to set message properties",
                  SEVERITY_LEVEL_SEVERE ) ;
            return ;
         } /* end catch( RemoteException foExp ) */
      } /* end if ( mhNodes.containsKey( lsActnNm ) ) */
   } /* end ReportTreePage_beforeActionPerformed() */

//END_EVENT_ReportTreePage_beforeActionPerformed}}

//END_EVENT_CODE}}


   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}

   }

//{{EVENT_ADAPTER_CODE

	public void pageCreated ( VSPage obj ){
		Object source = obj;
		if (source == this ) {
			ReportTreePage_pageCreated();
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			ReportTreePage_beforeActionPerformed( ae, evt, preq );
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

   /** method used the versata TreeNode and TreeElemnt classes
   *  (their AMS customized subclasses) to dynamically generate
   *  a tree
   */
   public void buildTree()
   {
      try
      {
         AMSReportTreeElement loReportTree = (AMSReportTreeElement)getScalarElement("ReportTree");
         AMSReportTreeNode    loRootNode ;


         /* Set the tree page id param as a session property */
         VSORBSession loCurrentSession = parentApp.getSession().getORBSession() ;
         loCurrentSession.setProperty( RSAA_TREE_PG_ID, getPageId() ) ;

         loRootNode = new AMSReportTreeNode( "Batch Catalog", 0, 0,
               AMSBatchConstants.FOLDER, null, null, true ) ;

         loRootNode.setNodeDestination( loRootNode, this ) ;
         loRootNode.setNodeCSSClass( loRootNode, this ) ;

         loReportTree.addChildNode( loRootNode ) ;
         mhNodes.put( "0", loRootNode ) ;

         loRootNode.addTopLevelNodes( loRootNode, getParentApp().getSession(),
               loReportTree ) ;
         loRootNode.open() ;

         loReportTree.addTreeListener( this ) ;
         loReportTree.generate() ;
      } /* end try */
      catch( RemoteException foExp )
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to set message properties",
               SEVERITY_LEVEL_SEVERE ) ;
      } /* end catch( RemoteException foExp ) */
   } /* end buildTree() */


   /**
   * Returns true if the current user has Batch Administrator role
   * else false.
   * @return boolean
   * @deprecated
   */
   public boolean isBatchAdmin()
   {
      try
      {
         return AMSBatchUtil.isBatchAdmin(this);
      }
      catch(Exception loExcp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

          return false;
      }
   }  /* end isBatchAdmin() */


   public boolean isUseSwing()
   {
      return true ;
   } /* end isUseSwing() */

   public String doAction(PLSRequest foPLSReq)
   {
      String lsPanelState = null;
      // For full form submit save the current PanelState.
      lsPanelState = foPLSReq.getParameter("PanelState");
      HiddenElement loPanelState = (HiddenElement)this.getElementByName("PanelState");

      if(loPanelState != null && lsPanelState != null)
      {
         loPanelState.setValue(lsPanelState);
      }
      return super.doAction(foPLSReq);
   }

}
