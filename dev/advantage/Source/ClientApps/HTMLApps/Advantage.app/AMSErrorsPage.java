//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import java.util.*;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import org.apache.commons.logging.*;
import com.amsinc.gems.adv.common.*;


/*
**  AMSErrorsPage
*/

//{{FORM_CLASS_DECL
public class AMSErrorsPage extends AMSErrorsPageBase

//END_FORM_CLASS_DECL}}



{
   /** This is the logger object for the class */
   private static Log moLog = AMSLogger.getLog( AMSErrorsPage.class,
         AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public AMSErrorsPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}





           setDisplayErrors(false);
           setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
   }






//{{EVENT_CODE
//{{EVENT_AMSErrorsPage_beforeActionPerformed
void AMSErrorsPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
   String lsMsgCode = preq.getParameter("MSG_CD");
   String lsWhereClause ="";
   String lsMsgColumn ="MSG_CD";
   String lsActionName =ae.getName();
   
   if(lsActionName.equals("T3AMSEMEXErrorDetail"))
   {
      lsMsgColumn = "ERR_CODE";
   }

   //System.err.println("lsMsgCode = " + lsMsgCode);
   // Check if the message code has been set in the
   // PLSRequest
   if (lsMsgCode != null)
   {
      // Add the where clause to the navigation object
      lsWhereClause = lsMsgColumn + " = " + AMSSQLUtil.getANSIQuotedStr(lsMsgCode,true);
      //System.err.println("lsWhereClause = " + lsWhereClause);
      //System.err.println("Query Mode = " + T2AMSErrorDetail.isInitialQueryMode());
      
   }

   if(lsActionName.equals("T3AMSEMEXErrorDetail"))
   {
      T3AMSEMEXErrorDetail.setDevWhere(lsWhereClause);
   }
   else
   {
      T2AMSErrorDetail.setDevWhere(lsWhereClause);
   }
}
//END_EVENT_AMSErrorsPage_beforeActionPerformed}}

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
			AMSErrorsPage_beforeActionPerformed( ae, evt, preq );
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

