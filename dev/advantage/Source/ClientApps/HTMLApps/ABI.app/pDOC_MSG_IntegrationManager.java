//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSParams;

/*
**  pDOC_MSG_IntegrationManager
*/

//{{FORM_CLASS_DECL
public class pDOC_MSG_IntegrationManager extends pDOC_MSG_IntegrationManagerBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pDOC_MSG_IntegrationManager ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T1V_DOC_MSG_beforeQuery
void T1V_DOC_MSG_beforeQuery(VSQuery foQuery, VSOutParam foResultset )
{
   SearchRequest loSearchRequest = null;
   String        lsFilter        = null;

   // Filter out rows that have blank Flow ID
   lsFilter = "FLOW_ID IS NOT NULL";

   if (!AMSStringUtil.strIsEmpty(foQuery.getSQLWhereClause()))
   {
      lsFilter = " AND " + lsFilter;
   }

   loSearchRequest = new SearchRequest();
   loSearchRequest.add(lsFilter);

   foQuery.addFilter(loSearchRequest);
}
//END_EVENT_T1V_DOC_MSG_beforeQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1V_DOC_MSG.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1V_DOC_MSG) {
			T1V_DOC_MSG_beforeQuery(query , resultset );
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