//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import org.apache.commons.logging.*;
import com.amsinc.gems.adv.common.*;

/*
**  AltSSErrorsPage
*/

//{{FORM_CLASS_DECL
public class AltSSErrorsPage extends AltSSErrorsPageBase

//END_FORM_CLASS_DECL}}
{
   /** This is the logger object for the class */
   private static Log moLog = AMSLogger.getLog( AltSSErrorsPage.class,
      AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public AltSSErrorsPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

      setDisplayErrors(false);
      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
   }


//{{EVENT_CODE
//{{EVENT_AltSSErrorsPage_beforeActionPerformed
void AltSSErrorsPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line

   // Check if the message code has been set in the PLSRequest
   String lsMsgCode = preq.getParameter("MSG_CD");
   if (lsMsgCode != null)
   {
      // Add the where clause to the navigation object
      String lsWhereClause = "MSG_CD = "
         + AMSSQLUtil.getANSIQuotedStr(lsMsgCode,true);
      T2AltSSErrorDetail.setDevWhere(lsWhereClause);
   }
}
//END_EVENT_AltSSErrorsPage_beforeActionPerformed}}

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
			AltSSErrorsPage_beforeActionPerformed( ae, evt, preq );
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
   * Overridden this method to change the default columns shown on the
   * error detail popup page. For selfService framework, CONTEXT and
   * OVERRIDE columns from errors grid are not shown.
   */
   public String generate()
   {
      // For AltSelfService framework show only SEVERITY and MESSAGE columns
      int[] liaColumnsToDisplay = new int[] { MSG_WIN_COL_SEVERITY,
         MSG_WIN_COL_MESSAGE};
      AMSPage.setErrorMsgColumns (liaColumnsToDisplay);

      return super.generate();
   }


   /**
   * Returns HTML text string for error message grid header Here
   * In this child class for AltSelfService framework, Component header
   * is shown as Reference and Severity column is shown as Type
   *
   * @return String HTML text string for error message grid header
   */
   public String getErrorMsgColumnHeaders()
   {

      StringBuffer lsbTextBuffer = new StringBuffer();

       int[] liaColumnsToDisplay = AMSPage.getErrorMsgColumns();
       for (int liLoop=0; liLoop<liaColumnsToDisplay.length; liLoop++)
       {
          int liColumn = liaColumnsToDisplay[liLoop];

          switch (liColumn)
          {
             case MSG_WIN_COL_SEVERITY:
             {
                lsbTextBuffer.append("<th><span id=\"Type\" name=\"Type\" title=\"Type\">Type</span></th>");
                break;
             }
             case MSG_WIN_COL_MESSAGE:
             {
                lsbTextBuffer.append("<th><span id=\"Message\" name=\"Message\" title=\"Message\">Message</span></th>");
                break;
             }
             default:
             {
                moLog.error( "Error message column is invalid and will be skipped.");
                break;
             }
          }
       }

      return lsbTextBuffer.toString();
   }

}