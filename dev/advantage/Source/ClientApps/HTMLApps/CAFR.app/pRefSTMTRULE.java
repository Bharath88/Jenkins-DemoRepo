//{{IMPORT_STMTS
package advantage.CAFR;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;

/*
**  pRefSTMTRULE
*/

//{{FORM_CLASS_DECL
public class pRefSTMTRULE extends pRefSTMTRULEBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pRefSTMTRULE ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_pk04STMT_SRC_COL_BeforePickQuery
void pk04STMT_SRC_COL_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
	//Write Event Code below this line
	
	/*
	 * This pick is of Condition Column field on the Statement Condition Grid.
	 * The pick should filter out and show Source Columns based on the Statement Source Table
	 * selected in the Statement Rule scalar. 
	 * Also, the Source Column Flag should be false indicating that it is a Condition Column.
	 */
	String lsSrcTbl = getRootDataSource().getResultSet().current().getData("SRC_TBL").getString();	
	StringBuffer lsbCond = new StringBuffer();
	if( !AMSStringUtil.strIsEmpty(lsSrcTbl) )
	{   
	   lsbCond.append(" SRC_TBL ")
	         .append(AMSSQLUtil.getANSIQuotedStr(lsSrcTbl, AMSSQLUtil.EQUALS_OPER))
	         .append(" AND ");
	}	
	lsbCond.append(" SRC_COL_FL = false ");
	whereClause.setValue(lsbCond);
}
//END_EVENT_pk04STMT_SRC_COL_BeforePickQuery}}
//{{EVENT_pk01STMT_SRC_COL_BeforePickQuery
void pk01STMT_SRC_COL_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
	//Write Event Code below this line
   
	/*
	 * This pick is of Condition Column field on the Statement Condition Grid.
	 * The pick should filter out and show Source Columns based on the Statement Source Table
	 * selected in the Statement Rule scalar. 
	 * Also, the Source Column Flag should be false indicating that it is a Condition Column.
	 */
	StringBuffer lsbCond = new StringBuffer();	
	lsbCond.append(" SRC_COL_FL = true ");
	whereClause.setValue(lsbCond);
}
//END_EVENT_pk01STMT_SRC_COL_BeforePickQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	pk04STMT_SRC_COL.addPickListener(this);
	pk01STMT_SRC_COL.addPickListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == pk04STMT_SRC_COL) {
			pk04STMT_SRC_COL_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	
		if (source == pk01STMT_SRC_COL) {
			pk01STMT_SRC_COL_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
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