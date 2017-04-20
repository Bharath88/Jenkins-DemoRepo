//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
/*
**  AltSSThankyou
*/

//{{FORM_CLASS_DECL
public class AltSSThankyou extends AltSSThankyouBase

//END_FORM_CLASS_DECL}}
{

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public AltSSThankyou ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_AltSSThankyou_beforeActionPerformed
void AltSSThankyou_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
   String lsActionName = ae.getName();
   if(AMSStringUtil.strIsEmpty(lsActionName))
   {
      return;
   }
   if(AMSStringUtil.strEqual(lsActionName, "ContReg" ) )
   {
      AMSDynamicTransition loDynTran = new AMSDynamicTransition("fVCCN", "", "VSSExtBase") ;
      loDynTran.setSourcePage( this );
      VSPage loTransPage = loDynTran.getVSPage(
                                getParentApp(), getSessionId() );
      evt.setCancel( true ) ;
      evt.setNewPage( loTransPage ) ;
   }

}
//END_EVENT_AltSSThankyou_beforeActionPerformed}}

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
         AltSSThankyou_beforeActionPerformed( ae, evt, preq );
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


}