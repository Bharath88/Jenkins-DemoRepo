//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.util.Enumeration;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSAppNavTree;
import com.amsinc.gems.adv.vfc.html.AMSAppNavTreeNode;
import com.amsinc.gems.adv.vfc.html.AMSNavigationTreeNode;

//{{FORM_CLASS_DECL
public class AppNavPanel extends AppNavPanelBase

//END_FORM_CLASS_DECL}}
{
   private static final int INTG_TRUNK_TREE_NODE = 37;


// This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public AppNavPanel ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         buildTree() ;
         setAllowHistory( false ) ;
         setADVUIType("PageStdUI");
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

   public void buildTree()
   {
      AMSNavigationTreeNode loRootNode = null;
      AMSNavigationTreeNode loNode     = null;
      Enumeration           loChildren = null;
      VSSession             loSession  = null;

      loSession  = getParentApp().getSession();
      moNavTree  = (AMSAppNavTree) getScalarElement("NavigationTree");
      loRootNode = new AMSNavigationTreeNode();

      moNavTree.setRootNode(loRootNode);

      AMSAppNavTreeNode.addTopLevelNodes(loRootNode, INTG_TRUNK_TREE_NODE, loSession, moNavTree);

      moNavTree.addTreeListener(this);

      loChildren = moNavTree.getAllChildren();

      while (loChildren.hasMoreElements())
      {
         loNode = (AMSNavigationTreeNode) loChildren.nextElement();

         if (!loNode.isOpen())
         {
            loNode.open();
            moNavTree.nodeOpened();
            moNavTree.postNodeOpenedEvent(loNode);
         }
      }
   } // end buildTree()
} /* end class AppNavPanel */
