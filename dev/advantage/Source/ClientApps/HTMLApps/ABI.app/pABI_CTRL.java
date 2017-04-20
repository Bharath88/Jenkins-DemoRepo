//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  pABI_CTRL
*/

//{{FORM_CLASS_DECL
public class pABI_CTRL extends pABI_CTRLBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pABI_CTRL ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_pABI_CTRL_beforeActionPerformed
void pABI_CTRL_beforeActionPerformed( ActionElement foActnElem, PageEvent evt, PLSRequest preq )
{
	VSResultSet loResultSet = T1ABI_CTRL.getResultSet();
	boolean lboolPollingFlag = false;
	if("T1ABI_CTRLStartPoll".equals(foActnElem.getName()))
	{
		lboolPollingFlag = true;
		if(preq.getParameter("chkboxSelectAll") == null && T1ABI_CTRL.getCurrentRow() != null)
		{
			T1ABI_CTRL.getCurrentRow().getData("POLLING_STATUS").setBoolean(lboolPollingFlag);
		}
	}
	
	if("T1ABI_CTRLStopPoll".equals(foActnElem.getName()))
	{
		lboolPollingFlag = false;
		if(preq.getParameter("chkboxSelectAll") == null && T1ABI_CTRL.getCurrentRow() != null)
		{
			T1ABI_CTRL.getCurrentRow().getData("POLLING_STATUS").setBoolean(lboolPollingFlag);
		}
	}
	if(preq.getParameter("chkboxSelectAll") != null && loResultSet != null)
	{
		int liRowCnt = loResultSet.getRowCount();
		VSRow loTempRow = null;
		for(int liCnt = 1; liCnt <= liRowCnt; liCnt++)
		{
			loTempRow = loResultSet.getRowAt(liCnt, false);
			if(loTempRow != null)
			{
				loTempRow.getData("POLLING_STATUS").setBoolean(lboolPollingFlag);
			}
		}
	}
	
	T1ABI_CTRL.updateDataSource();
	
}
//END_EVENT_pABI_CTRL_beforeActionPerformed}}

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
			pABI_CTRL_beforeActionPerformed( ae, evt, preq );
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