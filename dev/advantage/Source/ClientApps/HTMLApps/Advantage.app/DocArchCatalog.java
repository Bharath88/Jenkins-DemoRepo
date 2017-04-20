//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
//BGN ADVHR00039333-3S
import advantage.AMSMsgUtil;
import com.amsinc.gems.adv.common.AMSParams;
//END ADVHR00039333-3S

/*
**  DocArchCatalog*/

//{{FORM_CLASS_DECL
public class DocArchCatalog extends DocArchCatalogBase

//END_FORM_CLASS_DECL}}
{
	// Declarations for instance variables used in the form

	//BGN ADVHR00039333-3S
	private static final String C_ERR_MIN_SEARCH = "Doc Code and one of the following search criteria " +
	"must be entered: Doc Dept Code or Doc ID.";
	public static final String ERR_WILDCARD_ALONE = "Wildcards alone are not allowed.";
	//END ADVHR00039333-3S
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public DocArchCatalog ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T2DOC_HDR_beforeQuery
	void T2DOC_HDR_beforeQuery(VSQuery query ,VSOutParam resultset )
	{
		//Write Event Code below this line
   
		//No selection queries should be fired on DOC_HDR datasource
		//due to performance reasons.
		resultset.setValue(true);
	}
	
//END_EVENT_T2DOC_HDR_beforeQuery}}
//{{EVENT_DocArchCatalog_beforeActionPerformed
void DocArchCatalog_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
    //Write Event Code below this line
    //BGN ADVHR00039333-3S
    //Check to verfiy if Current Application in HR
    if(AMSParams.msPrimaryApplication.equals("2"))
    {
        String lsActnName = ae.getName() ;
        if (lsActnName.equals( "AMSBrowse" ))
        {
            validateSearchCriteria();
        }
    }
    //END ADVHR00039333-3S
}
//END_EVENT_DocArchCatalog_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T2DOC_HDR.addDBListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocArchCatalog_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T2DOC_HDR) {
			T2DOC_HDR_beforeQuery(query , resultset );
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

	//BGN ADVHR00039333-3S
	/**
	 * This method validates the Minimum Search Criteria.
	 */
	private void validateSearchCriteria()
	{
		   String lsDocCode = T1IN_DOC_ARCH_CTLG.getQBFDataForElement("txtT1DOC_CD").trim();
		   StringBuilder sbSearchKeyFieldsValue = new StringBuilder();
		   sbSearchKeyFieldsValue.append ( T1IN_DOC_ARCH_CTLG.getQBFDataForElement("txtT1DOC_DEPT_CD").trim() );
		   sbSearchKeyFieldsValue.append ( T1IN_DOC_ARCH_CTLG.getQBFDataForElement("txtT1DOC_ID").trim() );

		   //Check for string buffer is empty (to check all the search fields are empty or not)
		   if (lsDocCode.trim().length() == 0 || sbSearchKeyFieldsValue.toString().trim().length() == 0)
		   {
			   raiseException( C_ERR_MIN_SEARCH, AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
		   }

		   //If string buffer has value, then check if the value contains only wildcards by
		   // replacing all the occurances of '*' and '%' with empty string.
		   else if ((lsDocCode.trim().replaceAll("(%|\\*)", "").length() == 0) ||
				   (sbSearchKeyFieldsValue.toString().trim().replaceAll("(%|\\*)", "").length() == 0))
		   {
			   raiseException( C_ERR_MIN_SEARCH + " " + ERR_WILDCARD_ALONE, 
					   AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
		   }
	}
	//END ADVHR00039333-3S

}