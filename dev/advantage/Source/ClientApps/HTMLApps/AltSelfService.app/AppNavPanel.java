//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.*;
import com.versata.util.logging.*;
import org.apache.commons.logging.*;

/*
**  AppNavPanel*/

import java.util.* ;

import javax.swing.text.html.*;
import com.amsinc.gems.adv.vfc.html.* ;
import java.rmi.RemoteException;
import advantage.*;

//{{FORM_CLASS_DECL
public class AppNavPanel extends AppNavPanelBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolFirstTime = true;
   private AMSAppNavTree    moNavTree ;


   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public AppNavPanel ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setAllowHistory( false ) ;
         setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
   }



//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

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

   private long mlTreeRoot = -1;
   private boolean mboolTreeRootChanged = true;
   private TreeNode moNextNodeToExpandChildren = null;

   /**
    * Overrides the generate method for two purposes.
    * First, there is a JADE bug workaround to set the
    * targetFrame instance variable to "_self".  Then,
    * we must check if the startup page is to be shown as
    * well.  If it is, we add the necessary javascript code
    * to do it.
    *
    * Modification Log : Mark Shipley - 05/01/00
    *                                 - inital version
    *                    Mark Shipley - 06/28/00
    *                                 - updated for startup page
    *
    * @return String The generated page source
    */
   public String generate()
   {
      /* JADE bug workaround */
      targetFrame = "_self" ;
      /* end workaround */

      setPin( true ) ;

      if (mboolFirstTime)
      {
         mboolFirstTime = false;
         appendOnloadString( "UTILS_StartRegisteringTransactions()" );
      }

      buildTree();

      return super.generate() ;
   } /* end generate() */

   /**
    * This method is called whenever a tree must be
    * built.  If the root has notchanged, its tree is retrieved
    * from the cache and shown.  Otherwise, the tree structure is queried
    * from the database, added to the cache, and shown. Then, the
    * first child is expanded and its first child is expanded and
    * so on until a node has no more children.
    *
    * Modification Log : Mark Shipley - 05/01/00
    *                                 - updated for new page structure
    *                    Cédric Poiré - 12/06/05
    *                                 - Modified for SelfService
    *
    */
   public void buildTree()
   {
      // Retrieve the StartupPage to get the selected second level tab
      StartupPage loPage = (StartupPage)AMSStartupPage.getStartupPage(getParentApp());
      setTreeRoot(loPage.getSelectedSecondLevel());
      String lsInitSelNode = loPage.getAppNavInitSelNode( );

      // if the root has changed since the last time, build the tree form the root
      if(mboolTreeRootChanged)
      {
         AMSNavigationTreeNode loRootNode ;
         moNavTree = (AMSAppNavTree)getScalarElement( "NavigationTree" ) ;

         loRootNode = new AMSNavigationTreeNode() ;
         moNavTree.setRootNode( loRootNode ) ;

         AMSAppNavTreeNode.addTopLevelNodes( loRootNode, mlTreeRoot,
               getParentApp().getSession(), moNavTree, false ) ;


         moNavTree.addTreeListener( this ) ;
         mboolTreeRootChanged = false;
         moNextNodeToExpandChildren = moNavTree.getRootNode();
      }

      // If there is a node to expand, expand it and set its first child as the next
      // node to expand
      Enumeration loCurrentChildren;
      AMSAppNavTreeNode loChild;
      while(moNextNodeToExpandChildren != null)
      {
         loCurrentChildren = moNextNodeToExpandChildren.getChildren();
         if(loCurrentChildren.hasMoreElements())
         {
            // select the first child
            AMSAppNavTreeNode loSelectedChild = (AMSAppNavTreeNode)loCurrentChildren.nextElement();

            // go through the children
            // If the id correspond, replace the selected child with this one
            loCurrentChildren = moNextNodeToExpandChildren.getChildren();
            while(loCurrentChildren.hasMoreElements())
            {
               loChild = (AMSAppNavTreeNode)loCurrentChildren.nextElement();
               if( ( AMSStringUtil.strIsEmpty( lsInitSelNode )
                  && loChild.getInitSelFl() ) || AMSStringUtil.strEqual(
                  lsInitSelNode, Long.toString( loChild.getUniqueNodeID() ) ) )
               {
                  loSelectedChild = loChild;
               }
            }
            // if the node  is not a leaf, open it
            if(loSelectedChild.canHaveChildren())
            {
               loSelectedChild.open();
               loSelectedChild.nodeOpened(moNavTree);
            }
            // else if it is a link, display the page
            else if(loSelectedChild.getTargetFrame() != null)
            {
               String lsNodeID= loSelectedChild.getNodeID();
               appendOnloadString( "submitForm(document.AppNavPanel,'" + AMSSQLUtil.getANSIQuotedStr(lsNodeID) + "=" +
                   AMSSQLUtil.getANSIQuotedStr(lsNodeID) + "','" + AMSSQLUtil.getANSIQuotedStr(loSelectedChild.getTargetFrame()) + "')");
               loSelectedChild.nodeSelected(moNavTree, null);
            }
            moNextNodeToExpandChildren = loSelectedChild;
         }
         else
         {
            moNextNodeToExpandChildren = null;
         }


      }
   } /* end buildTree() */

   public boolean isUseSwing()
   {
      return true ;
   } /* end isUseSwing() */

   /**
    * This method sets the tree root
    *
    * @param flNewRoot the new root
    */
   public void setTreeRoot(long flNewRoot)
   {
      if(mlTreeRoot != flNewRoot)
      {
         mlTreeRoot = flNewRoot;
         mboolTreeRootChanged = true;
      }
   }


} /* end class AppNavPanel */
