//{{IMPORT_STMTS
package advantage.CAFR;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import advantage.AMSStringUtil;

/*
**  pRefSTMT_COLUMN
*/

//{{FORM_CLASS_DECL
public class pRefSTMTCOL extends pRefSTMTCOLBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pRefSTMTCOL ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_rs01STMT_COLUMN_beforeQuery
void rs01STMT_COLUMN_beforeQuery(VSQuery foQuery,VSOutParam resultset )
{
	//Write Event Code below this line
   /* Statement Column Grid should be ordered by SEQ_NO unless current action is sort. */
   if( getCurrentAction() == null || !AMSStringUtil.strEqual(getCurrentAction().getAction(), "sort") )
   {
      SearchRequest loOrderBy = new SearchRequest();
      loOrderBy.add(" SEQ_NO ");
      foQuery.replaceSortingCriteria(loOrderBy);
   }
}
//END_EVENT_rs01STMT_COLUMN_beforeQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	rs01STMT_COLUMN.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == rs01STMT_COLUMN) {
			rs01STMT_COLUMN_beforeQuery(query , resultset );
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