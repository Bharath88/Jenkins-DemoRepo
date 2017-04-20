//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.*;
/*
 **  pShwCodeDesc
 */

//{{FORM_CLASS_DECL
public class pShwCodeDesc extends pShwCodeDescBase

//END_FORM_CLASS_DECL}}
{

   /** The detail page */
   private AMSPage moDetailPage = null;

   /** The standard error message */
   private static final String ERROR_STRING = "Show Description is available only for attribute which has " +
            "value entered manually (when editable) or automatically populated by application (when non-editable).";
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pShwCodeDesc ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pShwCodeDesc_beforeActionPerformed
void pShwCodeDesc_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line


}
//END_EVENT_pShwCodeDesc_beforeActionPerformed}}

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
			pShwCodeDesc_beforeActionPerformed( ae, evt, preq );
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



   public void showError()
   {
      DivElement loErrorDiv = (DivElement)getElementByName("showLink");

      loErrorDiv.setVisible(false);

      AMSTextContentElement loElement =
         (AMSTextContentElement)getElementByName("txtDescription");

     loElement.setCustomValue(ERROR_STRING);

   }

   /**
    * This method will display the Column Name and value for which the description
    * will be shown
    * @param fsColumnName The name of the column
    * @param fsColumnValue The column value
    */
   public void showHeader(String fsColumnName, String fsColumnValue)
   {
      AMSTextContentElement loElement =
         (AMSTextContentElement)getElementByName("txtColumnName");
      loElement.setCustomValue(fsColumnName);
      loElement = (AMSTextContentElement)getElementByName("txtColumnValue");
      loElement.setCustomValue(fsColumnValue);
   } /* end showHeader */

   /**
    * This method will retrieve the description from moDetailPage root datasource
    * If there are no errors the description will be shown.
    * @param fsDescColumnName The column name on target page
    * @return boolean indicating if this action Succeeded
    *
    */
   public boolean showDescription(String fsDescColumnName)
   {
      boolean lboolOkToProceed = false;
      String lsDesc = "";

      /*
       * Try to get the required description out from the target page.
       */
      DataSource loDataSource = moDetailPage.getRootDataSource();
      if (loDataSource != null)
      {
         VSRow loRow = loDataSource.getCurrentRow();
         if (loRow != null)
         {
            try
            {
               lsDesc = loRow.getData(fsDescColumnName).getString();
               lboolOkToProceed = true;
            }
            catch (Exception ex)
            {
               lboolOkToProceed = false;
               ((AMSPage)getSourcePage()).raiseException("Error retrieving description from target page " +
                                              ex.getMessage(),
                                              SEVERITY_LEVEL_ERROR);

            } /* end catch */
         } /* end if (loRow != null) */
      } /* end if (loDataSource != null) */

      if (!lboolOkToProceed)
      {
         return lboolOkToProceed;
      }

      /*
       * The elements here should never be null since they are the only elements on the page.
       * Hide all divs except DescDiv and ViewMoreInfo divs
       */
      AMSTextContentElement loElement =
         (AMSTextContentElement)getElementByName("txtDescription");

     loElement.setCustomValue(lsDesc);


      return lboolOkToProceed;
   }


   /**
    * This sets the refrence of the detail page on the popup for future use
    * @param foPage the detail page
    */
   public void setLeafDetailPage(AMSPage foPage)
   {
      moDetailPage = foPage;
   }


   /**
    * @return the detail page
    */
   public AMSPage getLeafDetailPage()
   {
      return moDetailPage;
   }

   public boolean useVirtualResultSet( AMSDataSource foDataSource )
   {
      return false ;
   } /* end useVirtualResultSet() */


}
