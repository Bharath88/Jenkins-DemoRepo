//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
//BGN ADVHR00039301 - 3S
import advantage.AMSStringUtil;
import advantage.AMSMsgUtil;
//END ADVHR00039301 - 3S
	/*
	**  pWF_APLQ*/
	
	//{{FORM_CLASS_DECL
	public class pWF_ALQ extends pWF_ALQBase
	
	//END_FORM_CLASS_DECL}}
	{
		// Declarations for instance variables used in the form
		//BGN ADVHR00039301 - 3S
		private boolean mboolFirstTime = true;
		private static final String ERR_SEARCH_REQUIRED = "At least one of the search criteria must be entered.";
		private static final String ERR_WILDCARD_ALONE = "Wildcards alone are not allowed.";
		//END ADVHR00039301 - 3S

		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public pWF_ALQ ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
		//{{EVENT_CODE
		//{{EVENT_pWF_ALQ_beforeActionPerformed
        void pWF_ALQ_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
        {
            //Write Event Code below this line
            //BGN ADVHR00039301 - 3S
            String lsActionName = ae.getName();
            if(lsActionName.equals("AMSBrowse"))
            {
                StringBuffer lsbSearchFieldsValue = new StringBuffer();
                lsbSearchFieldsValue.append ( getRootDataSource().getQBFDataForElement("txtT1APRV_RULE_ID").trim() );
                lsbSearchFieldsValue.append ( getRootDataSource().getQBFDataForElement("txtT1DOC_CD").trim() );
                lsbSearchFieldsValue.append ( getRootDataSource().getQBFDataForElement("txtT1DOC_DEPT_CD").trim() );
                lsbSearchFieldsValue.append ( getRootDataSource().getQBFDataForElement("txtT1DOC_ID").trim() );
                lsbSearchFieldsValue.append ( getRootDataSource().getQBFDataForElement("txtT1USID_UP").trim() );

                // Check for string buffer is empty (to check all the search fields are empty or not)
                if ( lsbSearchFieldsValue.toString().length() == 0 )
                {
                   raiseException( ERR_SEARCH_REQUIRED, AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
                }
                // If string buffer has value, then check if the value contains only wildcards by 
                // replacing all the occurances of '*' and '%' with empty string.
                else if ( lsbSearchFieldsValue.toString().replaceAll("(%|\\*)", "").length() == 0 )
                {
                   raiseException( ERR_WILDCARD_ALONE, AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
                }
            }
            //END ADVHR00039301 - 3S
        }
        
		
//END_EVENT_pWF_ALQ_beforeActionPerformed}}
//{{EVENT_pWF_ALQ_beforeGenerate
        void pWF_ALQ_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
        {
            //Write Event Code below this line
            mboolFirstTime = false; // ADVHR00039301 - 3S
        }
        
		
//END_EVENT_pWF_ALQ_beforeGenerate}}
//{{EVENT_T1WF_APRV_LOG_beforeQuery
        void T1WF_APRV_LOG_beforeQuery(VSQuery query ,VSOutParam resultset )
        {
            //Write Event Code below this line
            //BGN ADVHR00039301 - 3S
            if (mboolFirstTime && (T1WF_APRV_LOG.getQueryMode() == DataSource.MODE_NORMAL))
            {
                String lsDevWhere = T1WF_APRV_LOG.getQueryInfo().getDevWhere();
                String lsQueryText = T1WF_APRV_LOG.getOnScreenQueryText();

                if ( AMSStringUtil.strIsEmpty(lsDevWhere) && 
                        AMSStringUtil.strIsEmpty(lsQueryText) )
                {
                    resultset.setValue(true);
                }
            }
            //END ADVHR00039301 - 3S
        }
        
		
//END_EVENT_T1WF_APRV_LOG_beforeQuery}}

		//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addPageListener(this);
	T1WF_APRV_LOG.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pWF_ALQ_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_ALQ_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1WF_APRV_LOG) {
			T1WF_APRV_LOG_beforeQuery(query , resultset );
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