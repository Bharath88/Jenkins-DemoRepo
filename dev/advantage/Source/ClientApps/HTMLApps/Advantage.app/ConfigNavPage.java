//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import javax.swing.text.html.*;
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.client.dbitem.AMSHomePageMethods ;

/*
**  ConfigNavPage
*/

//{{FORM_CLASS_DECL
public class ConfigNavPage extends ConfigNavPageBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   AMSNavigationTree moNavTree = null ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigNavPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_ConfigNavPage_pageCreated
void ConfigNavPage_pageCreated()
{
   AMSHomePageMethods.ensureUserWindows( getParentApp().getSession() ) ;
   buildTree() ;
   setADVUIType("PageStdUI");
}
//END_EVENT_ConfigNavPage_pageCreated}}

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
			ConfigNavPage_pageCreated();
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
   * Builds the configurator navigation tree top-level nodes.
   *
   * Modification Log : Mark Shipley - 05/15/00
   *                                 - inital version
   *                    Mark Shipley - 06/22/00
   *                                 - added the generate() call
   *                                   so that the top-level hyperlinks
   *                                   appear when the page is built
   *
   * @param flFavID The favorite id
   * @see #getID()
   */
   public void buildTree()
   {
      if ( moNavTree == null )
      {
         AMSNavigationTreeNode loRootNode ;
         AMSNavigationTreeNode loChildNode ;

         moNavTree = (AMSNavigationTree)getScalarElement( "NavigationTree" ) ;

         loRootNode = new AMSNavigationTreeNode() ;

         loChildNode = new AMSConfigWkspsTreeNode() ;
         loRootNode.addChildNode( loChildNode ) ;

         loChildNode = new AMSConfigHmpgTreeNode( getParentApp().getSession().getLogin() ) ;
         loRootNode.addChildNode( loChildNode ) ;

         loChildNode = new AMSConfigFavTreeNode( getParentApp().getSession().getLogin() ) ;
         loRootNode.addChildNode( loChildNode ) ;

         moNavTree.setRootNode( loRootNode ) ;

         moNavTree.addTreeListener( this ) ;
         moNavTree.generate() ;
      } /* end if ( moNavTree == null ) */
   } /* end buildTree() */

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