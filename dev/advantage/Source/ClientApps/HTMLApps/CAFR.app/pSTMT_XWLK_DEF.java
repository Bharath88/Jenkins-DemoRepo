//{{IMPORT_STMTS
package advantage.CAFR;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSGenericPickPage;
import advantage.AMSStringUtil;

/*
**  pR_STMT_XWLK_DEF
*/

//{{FORM_CLASS_DECL
public class pSTMT_XWLK_DEF extends pSTMT_XWLK_DEFBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pSTMT_XWLK_DEF ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_T1STMT_DEF_BeforePickQuery
void T1STMT_DEF_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
	//Write Event Code below this line
   
   /*
    * By default, Statement Code pick will filter based on the stored FY (primary key).
    * But since FY field is not visible on this page, there is no way, the user can see other
    * Statement Codes than those matching with the stored FY.
    * Hence clear out that condition to show all Statement Code records.
    */
	whereClause.setValue("");
}
//END_EVENT_T1STMT_DEF_BeforePickQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1STMT_DEF.addPickListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T1STMT_DEF) {
			T1STMT_DEF_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
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