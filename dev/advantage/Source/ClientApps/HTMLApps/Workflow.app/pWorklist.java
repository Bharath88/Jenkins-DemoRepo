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
	**  pWorklist*/
	
	//{{FORM_CLASS_DECL
	public class pWorklist extends pWorklistBase
	
	//END_FORM_CLASS_DECL}}
	{
		// Declarations for instance variables used in the form
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public pWorklist ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
	
		//{{EVENT_CODE
		//{{EVENT_T1WF_WRK_LST_QRY_beforeQuery
/**
 * This method is called before the query is performed on the
 * document catalog. This method is used to set up the where clause
 * to retrieve only those documents which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 08/07/02
 *                                        - Removed the super qualifier from the
 *                                        - call to modifyQuery()
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1WF_WRK_LST_QRY_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
	modifyQuery( foQuery, foResultSet ) ;
}
//END_EVENT_T1WF_WRK_LST_QRY_beforeQuery}}
//{{EVENT_pWorklist_requestReceived
void pWorklist_requestReceived( PLSRequest req, PageEvent evt )
{
	super.worklistRequestReceived( req, evt ) ;
}
//END_EVENT_pWorklist_requestReceived}}
//{{EVENT_T2R_GEN_DOC_CTRL_BeforePickQuery
void T2R_GEN_DOC_CTRL_BeforePickQuery(Pick foPick, DataSource foDataSource,
	  VSOutParam foWhereClause, VSOutParam foOrderBy, VSOutParam foCancel)
{
   String               lsApplWhereClause = null ;
   String               lsOldWhereClause = null ;
   VSORBSession         loORBSession = parentApp.getSession().getORBSession() ;
   AMSSecurityObject    loSecObj = null ;

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
   lsOldWhereClause = foWhereClause.stringValue() ;

   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsApplWhereClause = lsOldWhereClause + " AND " + lsApplWhereClause ;
   } /* end if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 ) */

   /* Reset the where clause in the main query */
   foWhereClause.setValue( lsApplWhereClause ) ;
}
//END_EVENT_T2R_GEN_DOC_CTRL_BeforePickQuery}}

		//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	T1WF_WRK_LST_QRY.addDBListener(this);
	addPageListener(this);
	T2R_GEN_DOC_CTRL.addPickListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pWorklist_requestReceived( req, evt );
		}
	}
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T2R_GEN_DOC_CTRL) {
			T2R_GEN_DOC_CTRL_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1WF_WRK_LST_QRY) {
			T1WF_WRK_LST_QRY_beforeQuery(query , resultset );
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
	
   /**
    * Overrode the method to return true so that on Document close all data on root DataSource
    * of this page is refreshed as opposed to only refreshing the current row. Note for this page
    * it is not possible to get handle to current row for refresh because the QueryObject
    * SELECT do not include the primay key columns of childmost DataObject.
    * @param foDocPage The document page being closed
    * @return boolean
    */
   public boolean refreshOnDocClose( AMSDocTabbedPage foDocPage )
   {
      return true;
   }//end of method

	}