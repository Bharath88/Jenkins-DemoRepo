//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  SecondaryNavigator*/

//{{FORM_CLASS_DECL
public class SecondaryNavigator extends SecondaryNavigatorBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   public  static final String PAGE_NAME               = "APPNAV_PG" ;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public SecondaryNavigator ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
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

   public String generate()
   {
      appendOnloadString( "UTILS_ShowAppNavPanel()");
      appendOnloadString( "submitForm(document.SecondaryNavigator,\'ADVAppNavTrans=ADVAppNavTrans&\',\'AppNav\')" ) ;

      /**
       * There is currently a problem where once in a while the
       * target frame name and the frame name in PLSApp are out
       * of sync when the user logs into the application for the
       * first time. To avoid this we will reset the frame name
       * to _self so that the page will be generated
       */
      targetFrame = "_self";

      setPin( true ) ;
      return super.generate() ;
   } /* end generate() */


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
