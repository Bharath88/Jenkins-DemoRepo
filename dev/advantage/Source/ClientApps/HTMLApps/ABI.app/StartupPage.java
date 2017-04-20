//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.client.dbitem.*;
import com.amsinc.gems.adv.common.*;
import org.apache.commons.logging.*;
import java.util.*;
import java.rmi.RemoteException;

/*
**  StartupPage
*/

//{{FORM_CLASS_DECL
public class StartupPage extends StartupPageBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public StartupPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }




//{{EVENT_CODE
//{{EVENT_StartupPage_beforeActionPerformed
void StartupPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   if( ae.getName().equals("T1HomePage") )
   {
      VSSession loVSSession = getParentApp().getSession() ;
      String    lsUserID = AMSHomePageMethods.getUserID( loVSSession ) ;
      int liNumWins = 0;

      if ( AMSHomePageMethods.useDfltWindows( loVSSession, lsUserID ) == true )
      {
         lsUserID = null ;
      } /* if ( AMSHomePageMethods.useDfltWindows( loVSSession, lsUserID ) == true ) */

      VSResultSet loVSResultSet = AMSHomePageMethods.getResultSet( loVSSession, lsUserID ) ;

      loVSResultSet.last() ;
      liNumWins = loVSResultSet.getRowCount() ;

      /* Here we loop through all the homepages until we find an internal page.
       * If we find one, we set the action to a dynamic transition that
       * navigates to that page.  While new development prevents any other
       * homepage to be set if an internal homepage is set, this loop allows
       * for user accounts set with multiple homepages before the new
       * development was imlemented to still function under the new rules.
       */
      for ( int liWinCtr = 1 ; liWinCtr <= liNumWins ; liWinCtr++ )
      {
         VSRow loVSRow = loVSResultSet.getRowAt( liWinCtr ) ;

         String lsDestination = AMSHomePageMethods.getURL( loVSRow ) ;
         if ( ( lsDestination == null ) || ( lsDestination.length() == 0 ) )
         {
            String lsPageCode = AMSHomePageMethods.getPageCode( loVSRow ) ;

            AMSDynamicTransition loHomePageDynTrans = new AMSDynamicTransition();
            loHomePageDynTrans.setPageCode( lsPageCode );
            ((AMSHyperlinkActionElement)ae).setDynamicTransition(loHomePageDynTrans);

            return;
         }
      }

      // reset the action element to default
      ((AMSHyperlinkActionElement)ae).setDynamicTransition(null);
   }
}
//END_EVENT_StartupPage_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			StartupPage_beforeActionPerformed( ae, evt, preq );
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
         setDefaultActionName("OpenPageWithData");

   }

} /* end class StartupPage */
