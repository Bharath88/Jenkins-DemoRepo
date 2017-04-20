//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	
	import java.util.Enumeration ;
	import java.rmi.RemoteException;
	import javax.swing.text.* ;
	import javax.swing.text.html.* ;
	import com.amsinc.gems.adv.common.*;
	import com.amsinc.gems.adv.vfc.html.* ;
	
	
	/*
	**  pWF_RECALL_QRY_Grid
	*/
	
	//{{FORM_CLASS_DECL
	public class pWF_RECALL_QRY_Grid extends pWF_RECALL_QRY_GridBase
	
	//END_FORM_CLASS_DECL}}
	{
		// Declarations for instance variables used in the form
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public pWF_RECALL_QRY_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
	
	//{{EVENT_CODE
	//{{EVENT_T1WF_RECALL_QRY_beforeQuery
void T1WF_RECALL_QRY_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
	String               lsApplWhereClause = null ;
	String               lsOldWhereClause = null ;
	VSORBSession         loORBSession = parentApp.getSession().getORBSession() ;
	AMSSecurityObject    loSecObj = null ;
	SearchRequest        loSrchReq ;

	//Get the SearchRequest
	loSrchReq = foQuery.getFilter () ;

	if ( loSrchReq == null )
	{
	   loSrchReq = new SearchRequest () ;
    }

	try
	{
	   loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
	}
	catch ( RemoteException loRemExp )
	{
	   raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
	   return ;
	}

	/* Retrieve where clause for Applications  */
	lsApplWhereClause = AMSSecurityObject.getApplicationWhere( loSecObj ) ;

	/* Check if there is already an existing where clause */
	lsOldWhereClause = foQuery.getSQLWhereClause() ;

	if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
	{
	   lsApplWhereClause = " AND " + lsApplWhereClause ;
	} /* end if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 ) */

	/* Reset the where clause in the main query */
    loSrchReq.add( lsApplWhereClause ) ;
    foQuery.addFilter( loSrchReq ) ;
}
//END_EVENT_T1WF_RECALL_QRY_beforeQuery}}

	//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	T1WF_RECALL_QRY.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1WF_RECALL_QRY) {
			T1WF_RECALL_QRY_beforeQuery(query , resultset );
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