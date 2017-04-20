//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSStringUtil;
import advantage.R_MSGImpl;
import java.rmi.RemoteException;
import java.util.Enumeration;
import versata.vls.ServerEnvironment;
import versata.vls.Session;
import com.amsinc.gems.adv.common.AMSParams;

/*
**  pDOC_MSG_Generic
*/

//{{FORM_CLASS_DECL
public class pDOC_MSG_Generic extends pDOC_MSG_GenericBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pDOC_MSG_Generic ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_T1DOC_MSG_beforeQuery
void T1DOC_MSG_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   String        lsApplID        = null;
   String        lsWhereClause   = null;
   SearchRequest loSearchRequest = null;
   
   lsApplID = AMSParams.msPrimaryApplication;

   if (!AMSStringUtil.strIsEmpty(lsApplID))
   {
      loSearchRequest = new SearchRequest();
      lsWhereClause   = query.getSQLWhereClause();

      if (lsWhereClause != null && 
          lsWhereClause.trim().length() > 0)
      {
         lsApplID = " AND APPL_ID = " + lsApplID;
      }
      else
      {
         lsApplID = " APPL_ID = " + lsApplID;
      }

      loSearchRequest.add(lsApplID);

      query.addFilter(loSearchRequest);
   } // end if (!AMSStringUtil.strIsEmpty(lsApplID))
}
//END_EVENT_T1DOC_MSG_beforeQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1DOC_MSG.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1DOC_MSG) {
			T1DOC_MSG_beforeQuery(query , resultset );
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