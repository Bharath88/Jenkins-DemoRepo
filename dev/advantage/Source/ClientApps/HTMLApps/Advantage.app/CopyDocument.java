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
import com.amsinc.gems.adv.common.* ;
/*
**  CopyDocument
*/

//{{FORM_CLASS_DECL
public class CopyDocument extends CopyDocumentBase

//END_FORM_CLASS_DECL}}
implements com.amsinc.gems.adv.common.AMSCommonConstants
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public CopyDocument ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_CopyDocument_beforeActionPerformed
void CopyDocument_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   /**
    * If the source page is a document, make sure that the tabs are re-gened
    */
   if ( ae.getName().equals( "CancelCopy" ) )
   {
      VSPage loSrcPage = getSourcePage() ;

      if ( ( loSrcPage != null ) && ( loSrcPage instanceof AMSDocTabbedPage ) )
      {
         ((AMSDocTabbedPage)loSrcPage).setGenTabPage( true ) ;
      }
   }
}
//END_EVENT_CopyDocument_beforeActionPerformed}}

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
			CopyDocument_beforeActionPerformed( ae, evt, preq );
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
    * Given a row with document id fields, this method returns
    * the generated copy document page prefilling it with the
    * document to copy.
    *
    * @param foDocCopyRow The row for the document to copy
    * @param foActnElem The action element that started the copy
    * @param foPLSReq The PLS request
    * @return The generated copy document page
    */
   public static String buildCopyPage( VSRow foDocCopyRow,
         AMSHyperlinkActionElement foActnElem, PLSRequest foPLSReq )
   {
      VSPage               loSrcPage   = foActnElem.getPage() ;
      String[]             lsMsgParams = new String[6] ;
      StringBuffer         lsbWhere    = new StringBuffer( 100 ) ;
      AMSDynamicTransition loDynTran ;


      /* Set the document identifier in the session properties */
      foActnElem.setDocumentIdentifier( lsMsgParams, foDocCopyRow ) ;

      lsbWhere.append( "DOC_CD='" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[2] ) ) ;
      lsbWhere.append( "' AND DOC_DEPT_CD='" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[3] ) ) ;
      lsbWhere.append( "' AND DOC_ID='" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[4] ) ) ;
      lsbWhere.append( "' AND DOC_VERS_NO=" ) ;
      lsbWhere.append( lsMsgParams[5] ) ;

      loDynTran = new AMSDynamicTransition( "CopyDocument",
            lsbWhere.toString(), "Advantage" ) ;
      loDynTran.setSourcePage( loSrcPage ) ;

      return loDynTran.generateHTMLPage( loSrcPage.getParentApp(),
            loSrcPage.getSessionId(), foPLSReq ) ;
   } /* end buildCopyPage() */

}