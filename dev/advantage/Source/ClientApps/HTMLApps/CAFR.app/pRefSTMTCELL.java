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
**  pRefSTMTCELL
*/

//{{FORM_CLASS_DECL
public class pRefSTMTCELL extends pRefSTMTCELLBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pRefSTMTCELL ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_pk04STMT_RULE_BeforePickFill
void pk04STMT_RULE_BeforePickFill(Pick obj, DataSource receiverDataSource, VSRow supplierRow,VSOutParam cancel)
{
	//Write Event Code below this line
   
   /*
    * Normally, when a a value is selected from the Pick page, the field is updated with it by
    * replacing any prior value if present on it.
    * However, in case of Cell Expression, the value selected using Pick should not replace
    * existing value in the field, but should append to it.
    * This is needed because it is an expression field.
    */
   String lsStmtRuleCD = supplierRow.getData("RULE_CD").getString();	
   if(receiverDataSource.getCurrentRow() != null)
   {
      String lsCellExpDV = receiverDataSource.getCurrentRow().getData("CELL_EXP").getString();		
      lsCellExpDV = lsCellExpDV + lsStmtRuleCD ;
      receiverDataSource.getCurrentRow().getData("CELL_EXP").setString(lsCellExpDV );
   }
}
//END_EVENT_pk04STMT_RULE_BeforePickFill}}
//{{EVENT_pk05STMT_CELL_BeforePickFill
void pk05STMT_CELL_BeforePickFill(Pick obj, DataSource receiverDataSource, VSRow supplierRow,VSOutParam cancel)
{
	//Write Event Code below this line
   /*
    * Normally, when a a value is selected from the Pick page, the field is updated with it by
    * replacing any prior value if present on it.
    * However, in case of Cell Expression, the value selected using Pick should not replace
    * existing value in the field, but should append to it.
    * This is needed because it is an expression field.
    */   
   String lsCellCD = supplierRow.getData("STMT_CELL").getString();	
   if(receiverDataSource.getCurrentRow() != null)
   {
      String lsCellExpDV = receiverDataSource.getCurrentRow().getData("CELL_EXP").getString();
      lsCellExpDV = lsCellExpDV + lsCellCD ;
      receiverDataSource.getCurrentRow().getData("CELL_EXP").setString(lsCellExpDV );
   }
}
//END_EVENT_pk05STMT_CELL_BeforePickFill}}
//{{EVENT_pRefSTMTCELL_beforeActionPerformed
void pRefSTMTCELL_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line
		
	// When Validate is clicked, call Save to do the validation.
	if("rs01STMT_CELL_Validate".equals(ae.getName()))
	{
		String lsCellExp = preq.getParameter("txtrs01CELL_EXP");
		if(lsCellExp != null && !"".equals(lsCellExp ))
		{
			rs01STMT_CELL.getResultSet().current().getData("CELL_EXP").setString(lsCellExp );
			rs01STMT_CELL.getResultSet().current().save();
		}		
	}	
}
//END_EVENT_pRefSTMTCELL_beforeActionPerformed}}
//{{EVENT_pRefSTMTCELL_beforeGenerate
void pRefSTMTCELL_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
	//Write Event Code below this line
	
	/*
	 * If either Row or Column has value as 'ALL', the Cell Expression field should be disabled.
	 * Any existing value in the field is cleared (This is done in DataObject Impl.)
	 */
	if(rs01STMT_CELL == null || rs01STMT_CELL.getCurrentRow() == null)
	{
	   return;
	}
	String lsCol = rs01STMT_CELL.getCurrentRow().getData("STMT_COL").getString();
	String lsRow = rs01STMT_CELL.getCurrentRow().getData("STMT_ROW").getString();	
	if(AMSStringUtil.strEqual(lsCol, ALL) || AMSStringUtil.strEqual(lsRow, ALL))
	{
		((ScalarElement)getElementByName("txtrs01CELL_EXP")).setEnabled(false);
	}
	else
	{
		((ScalarElement)getElementByName("txtrs01CELL_EXP")).setEnabled(true);
	}	
	
}
//END_EVENT_pRefSTMTCELL_beforeGenerate}}
//{{EVENT_pRefSTMTCELL_requestReceived
void pRefSTMTCELL_requestReceived( PLSRequest req, PageEvent evt )
{
	//Write Event Code below this line
   
	// If Cell Expression is blank, then issue warning about the same.
	if(rs01STMT_CELL != null && rs01STMT_CELL.getCurrentRow() != null)
	{
		VSRow loRow = rs01STMT_CELL.getCurrentRow();
		
		String lsFY      = loRow.getData("STMT_FY").getString();
		String lsCode    = loRow.getData("STMT_CD").getString();		
		String lsRow     = loRow.getData("STMT_ROW").getString();
		String lsCol     = loRow.getData("STMT_COL").getString();
		String lsCell    = loRow.getData("STMT_CELL").getString();
		String lsCellExp = loRow.getData("CELL_EXP").getString();
		
		if( AMSStringUtil.strIsEmpty(lsFY) || AMSStringUtil.strIsEmpty(lsCode) || 
			AMSStringUtil.strIsEmpty(lsRow) || AMSStringUtil.strIsEmpty(lsCol) )
		{
			return;
		}
		
		SearchRequest loRuleSearch = new SearchRequest();
		loRuleSearch.addParameter("STMT_RULE", "STMT_FY", lsFY);
		loRuleSearch.addParameter("STMT_RULE", "STMT_CD", lsCode);
		loRuleSearch.addParameter("STMT_RULE", "STMT_CELL", lsCell);
		VSQuery loVSQuery = new VSQuery(getParentApp().getSession(), "STMT_RULE", loRuleSearch,
			new SearchRequest());
		VSResultSet loResultSet = loVSQuery.execute();
		boolean lboolRule = loResultSet != null && loResultSet.first() != null;

		
		// If Row or Column is 'ALL', then error need not be issued.
		if( AMSStringUtil.strIsEmpty(lsCellExp) && lboolRule && 
			!(AMSStringUtil.strEqual(lsRow, ALL) || AMSStringUtil.strEqual(lsCol, ALL)) )
		{
			raiseException("Cell Expression for Cell " +lsCell+ "is blank.", SEVERITY_LEVEL_WARNING);
		}
	}
}
//END_EVENT_pRefSTMTCELL_requestReceived}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	pk04STMT_RULE.addPickListener(this);
	pk05STMT_CELL.addPickListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pRefSTMTCELL_beforeGenerate(docModel, cancel, output);
		}
	}
	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pRefSTMTCELL_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pRefSTMTCELL_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void BeforePickFill(Pick obj, DataSource receiverDataSource, VSRow supplierRow,VSOutParam cancel){
		Object source = obj;
		if (source == pk04STMT_RULE) {
			pk04STMT_RULE_BeforePickFill(obj, receiverDataSource, supplierRow, cancel);
		}
	
		if (source == pk05STMT_CELL) {
			pk05STMT_CELL_BeforePickFill(obj, receiverDataSource, supplierRow, cancel);
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