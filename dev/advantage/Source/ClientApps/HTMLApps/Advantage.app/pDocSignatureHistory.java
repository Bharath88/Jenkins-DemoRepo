//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.DefaultStyledDocument.ElementSpec;

import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import com.amsinc.gems.adv.vfc.html.AMSTableElement;
import versata.vfc.html.TableElement.TableColumnElement;

/*
**  pDocSignatureHistory
*/

//{{FORM_CLASS_DECL
public class pDocSignatureHistory extends pDocSignatureHistoryBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pDocSignatureHistory ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
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




   /**
    * Overridden method to return elementspec for table cell. This will show
    * display values for columns which stores integer codes. Like Action Code.
    * 
    * @see com.amsinc.gems.adv.vfc.html.AMSPage#beforeColumnGeneration(com.amsinc.gems.adv.vfc.html.AMSTableElement,
    *      versata.vfc.VSRow, versata.vfc.VSData,
    *      versata.vfc.html.TableElement.TableColumnElement, int)
    */
   public ElementSpec beforeColumnGeneration(AMSTableElement foTableElement, VSRow foRow, VSData foData,
         TableColumnElement foTC, int fiOffset)
   {
      boolean lboolCustomElemSpec = false;
      char[] laTextToShow = "".toCharArray();
      if(foData != null)
      {
         String lsColName = foData.getMetaColumn().getName();
         if("DOC_ACTN_CD".equals(lsColName))
         {
            //Document actions codes should be replaced with actual action labels
            int liActn = foData.getInt();
            if (AMSCommonConstants.DOC_ACTN_SUBMIT == liActn)
            {
               laTextToShow = "Submit".toCharArray();
            }
            else if (AMSCommonConstants.DOC_ACTN_APPROVE == liActn)
            {
               laTextToShow = "Approve".toCharArray();
            }
            else if (AMSCommonConstants.DOC_ACTN_BYPS_APRV == liActn)
            {
               laTextToShow = "Bypass Approvals".toCharArray();
            }
            
            lboolCustomElemSpec = true;
         }
         else if("DOC_APRV_LVL".equals(lsColName))
         {
            int liAprvLvl = foData.getInt();
            //Approval Level should be blank when its value is -1.
            if (liAprvLvl == 0)
            {
               lboolCustomElemSpec = true;
            }
         }
      }
      if(lboolCustomElemSpec)
      {
         SimpleAttributeSet loSAS = new SimpleAttributeSet();
         loSAS.addAttribute(StyleConstants.NameAttribute,
               HTML.Tag.CONTENT);
         DefaultStyledDocument.ElementSpec loSpec = new DefaultStyledDocument.ElementSpec(
               loSAS, (short) 3, laTextToShow, 0, laTextToShow.length);

         return loSpec;
      }
      else
      {
         /*
          * For all other columns use the default mechanism to return
          * elementSpec
          */
         return super.beforeColumnGeneration(foTableElement, foRow, foData,
               foTC, fiOffset);
      }
      
   }


}