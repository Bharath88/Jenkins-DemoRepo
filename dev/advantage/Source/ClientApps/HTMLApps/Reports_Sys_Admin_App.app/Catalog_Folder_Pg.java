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
import advantage.*;

import javax.swing.text.*;
import javax.swing.text.html.*;

import org.apache.commons.logging.Log;

import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import java.rmi.RemoteException;

/*
 **  Catalog_Folder_Pg
 */

//{{FORM_CLASS_DECL
public class Catalog_Folder_Pg extends Catalog_Folder_PgBase

//END_FORM_CLASS_DECL}}
{
  
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Catalog_Folder_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   // Declarations for instance variables used in the form
   Vector mvNodesToDelete =null;
   Vector mvOpenResultSets = null;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Catalog_Folder_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
      //Write Event Code below this line

      setParentNodeDirty(row);


   }


//END_EVENT_T1R_BS_CATALOG_afterSave}}
//{{EVENT_T1R_BS_CATALOG_beforeDelete
   void T1R_BS_CATALOG_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
   {
      // deletes all the child elements of a folder

      try
      {
         if (hasChildren(row)==true)
         {
            mvNodesToDelete = new Vector();
            VSResultSet loChildren = getChildResultSet(row);
            for(VSRow child = loChildren.first(); child !=null; child =loChildren.next())
            {
               String lsNodeID = row.getData("CTLG_ID").getString();
               mvNodesToDelete.addElement(child);
               if (hasChildren(child))
               {
                  addChildNodesToDeleteSet(child);
               }
            }//for

            for(int i =0; i< mvNodesToDelete.size();i++)
            {
               VSRowBasic loElement = (VSRowBasic)mvNodesToDelete.elementAt(i);
               loElement.deleted(true);
               loElement.save();
            } //for

            setParentNodeDirty(row);
            for(int i =0; i< mvOpenResultSets.size();i++)
            {
               VSResultSet loRS = (VSResultSet)mvOpenResultSets.elementAt(i);
               try
               {
                  if (loRS!=null)
                  {
                     loRS.close();
                  }
               }//end try
               catch(Exception loExp1)
               {
                  // Add exception log to logger object
                  moAMSLog.error("Unexpected error encountered while processing. ", loExp1);

               } //catch
            } //for
         }
         else
         {
            setParentNodeDirty(row);
            return;
         }
      }
      catch(Throwable loExcp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      }












   }


//END_EVENT_T1R_BS_CATALOG_beforeDelete}}
//{{EVENT_Catalog_Folder_Pg_pageCreated
   void Catalog_Folder_Pg_pageCreated()
   {
      //Write Event Code below this line

      HyperlinkActionElement obj = (HyperlinkActionElement)getDocumentModel().getElementByName("T1R_BS_CATALOGdelete");
      //obj.getHtmlElement().removeAttribute("HTML.Attribute.HREF");
      obj.getHtmlElement().removeAttribute("href");


   }


//END_EVENT_Catalog_Folder_Pg_pageCreated}}
//{{EVENT_Catalog_Folder_Pg_beforeActionPerformed
   void Catalog_Folder_Pg_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      // triggered only if deleting from Catalog not Catalog Parm
      if (ae.getAction().equals("delete") && ae.getName().equals("T1R_BS_CATALOGdelete"))
      {
         String lsConfirmDelete = preq.getParameter("ConfirmDelete");
         if (lsConfirmDelete.equalsIgnoreCase("No"))
         {
            evt.setNewPage(this);
            evt.setCancel(true);
         }

      }


   }


//END_EVENT_Catalog_Folder_Pg_beforeActionPerformed}}

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
	public void pageCreated ( VSPage obj ){
		Object source = obj;
		if (source == this ) {
			Catalog_Folder_Pg_pageCreated();
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
			Catalog_Folder_Pg_beforeActionPerformed( ae, evt, preq );
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
    * Returns if there are child items for catalog entry
    */
   private boolean hasChildren(VSRow row)
   {
      boolean lbFlag =false;
      VSResultSet loRS =null;
      try
      {
         VSSession loCurrentSession = getParentApp().getSession();
         String lsCatalogID = row.getData("CTLG_ID").getString();
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("PNT_ID =" +lsCatalogID );
         VSQuery loQuery = new VSQuery(loCurrentSession,"R_BS_CATALOG",searchReq,null);
         loRS =loQuery.execute();
         loRS.last();
         int liRows = loRS.getRowCount();
         //loRS.close();
         if (liRows >0)
         {
            lbFlag = true;
         }
      }
      catch(Throwable loExcp)
      {
        	   lbFlag =false;
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      }
      finally
      {
         if (loRS !=null)
         {
            loRS.close();
         }
      }
      return lbFlag ;
   } // end of hasChildren

   /**
    * Creates and returns a VSResultset (cursor) that
    * includes all the child catalog entries
    */
   private VSResultSet getChildResultSet(VSRow row)
   {
      VSResultSet loChildEntries =null;
      try
      {
         VSSession loCurrentSession = getParentApp().getSession();
         String lsCatalogID = row.getData("CTLG_ID").getString();
         SearchRequest searchReq = new SearchRequest();
         searchReq.add("PNT_ID =" + lsCatalogID );
         VSQuery loQuery = new VSQuery(loCurrentSession,"R_BS_CATALOG",searchReq,null);
         loChildEntries= loQuery.execute();
      }
      catch(Exception e)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

      }
      if (mvOpenResultSets == null)
      {
         mvOpenResultSets = new Vector();
      }
      mvOpenResultSets.addElement(loChildEntries);
      return loChildEntries ;
   } // end of getChildResultSet

   /**
    * Called recursively to add all catalog nodes
    * to be deleted to a vector collection
    */
   private void addChildNodesToDeleteSet(VSRow row)
   {
      try
      {
         VSResultSet loChildren = getChildResultSet(row);
         for(VSRow child = loChildren.first(); child !=null; child =loChildren.next())
         {
            String lsNodeID = row.getData("CTLG_ID").getString();
            mvNodesToDelete.addElement(child);
            if (hasChildren(child))
            {
               addChildNodesToDeleteSet(child);
            }
         }//for
      }//try
      catch(Exception e)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

      }
   } // end of addChildNodesToDeleteSet

   /**
    * Sets the parent Node dirty in the ReportTreePage so that
    * the node and its children are generated afesh
    */
   private void setParentNodeDirty(VSRow row)
   {
     try
      {
         long llNodeID = row.getData("PNT_ID").getLong();
         String lsNodeID = String.valueOf(llNodeID);
         VSORBSession loCurrentSession = parentApp.getSession().getORBSession();
         String lsTreePageID = loCurrentSession.getProperty("RSAA_TREE_PG_ID");
         db("The Tree Page ID " + lsTreePageID);
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
   } // end of method setParentNodeDirty


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
