//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;

/*
**  pCopyForwardDocument
*/

//{{FORM_CLASS_DECL
public class pCopyForwardDocument extends pCopyForwardDocumentBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   private boolean mboolFirstGenerate = true ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pCopyForwardDocument ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_pCopyForwardDocument_beforeActionPerformed
void pCopyForwardDocument_beforeActionPerformed( ActionElement foElem, PageEvent foEvent, PLSRequest foPLSReq )
{
   //Write Event Code below this line
   if ( foElem.getName().equals( "Cancel" ) )
   {
      VSPage loSrcPage = getSourcePage() ;

      if ( ( loSrcPage != null ) && ( loSrcPage instanceof AMSDocTabbedPage ) )
      {
         ((AMSDocTabbedPage)loSrcPage).setGenTabPage( true ) ;
      } /* end if ( loSrcPage instanceof AMSDocTabbedPage ) */
   } /* end if ( foElem.getName().equals( "Cancel" ) ) */
}
//END_EVENT_pCopyForwardDocument_beforeActionPerformed}}

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
			pCopyForwardDocument_beforeActionPerformed( ae, evt, preq );
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

   public void beforeGenerate()
   {
      if ( mboolFirstGenerate )
      {
         mboolFirstGenerate = false ;
         if ( T2R_GEN_CPY_FWD.getCurrentRow() == null )
         {
            /*
             * If no valid copy forward document codes exist, then
             * do not show the copy forward page.  Instead, just
             * display an error to the user.
             */
            raiseException( "No valid Copy Forward targets exist", SEVERITY_LEVEL_SEVERE ) ;
         } /* end if ( T2R_GEN_CPY_FWD.getCurrentRow() == null ) */
      } /* end if ( mboolFirstGenerate ) */
   } /* end beforeGenerate() */

}