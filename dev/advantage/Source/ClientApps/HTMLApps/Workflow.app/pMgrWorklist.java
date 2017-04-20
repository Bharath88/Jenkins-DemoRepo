//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	import com.amsinc.gems.adv.common.*;
	import com.amsinc.gems.adv.workflow.AMSWorklistPage;
	import com.amsinc.gems.adv.vfc.html.*;
	import java.rmi.RemoteException;
	
	/*
	**  pMgrWorklist*/
	
	//{{FORM_CLASS_DECL
	public class pMgrWorklist extends pMgrWorklistBase
	
	//END_FORM_CLASS_DECL}}
	{
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code.
		//{{FORM_CLASS_CTOR
		public pMgrWorklist ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
		//{{EVENT_CODE
		//{{EVENT_T1WF_WRK_LST_QRY_beforeQuery
/**
 * This method is called before the query is performed on the
 * manager worklist. This method is used to set up the where clause
 * to retrieve only those records which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Srinivas Naikoti   - 01/05/04
 *                                       - Initial version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1WF_WRK_LST_QRY_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
   modifyQuery( foQuery, foResultSet ) ;
}
//END_EVENT_T1WF_WRK_LST_QRY_beforeQuery}}

		//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	T1WF_WRK_LST_QRY.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
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
	    * This method overrides the AMSWorklistPage method to always return true.
	    * This would allow all role worklist specific actions ( e.g take task )
	    * to be performed.
	    *
	    * Modification Log : Kiran Hiranandani   - 08/29/02
	    *                                        - inital version
	    *
	    * @return true if current worklist exists and is not of user
	    */
	   public boolean isRoleWorklist()
	   {
	      return true ;
	   } /* end isRoleWorklist() */
	
	   /**
	    * This method overrides the AMSWorklistPage method to always return true.
	    * This would allow all user worklist specific actions ( e.g return task )
	    * to be performed.
	    *
	    * Modification Log : Kiran Hiranandani   - 08/29/02
	    *                                        - inital version
	    *
	    * @return true if current worklist exists and is not of user
	    */
	   public boolean isUserWorklist()
	   {
	      return true ;
	   } /* end isRoleWorklist() */
	
	 /**
	   * This method modifies query information and called from beforeQuery
	   * of page.
	   *
	   * Modification Log : Kiran Hiranandani - 08/07/2002
	   *                                      - used AMSSecurityObject.getApplicationWhere
	   *
	   * @param foQuery - VSQuery object for query.
	   * @param foResultSet - VSOutParam object
	   */
	   public void modifyQuery( VSQuery foQuery, VSOutParam foResultSet )
	   {
	      SearchRequest        loSrchReq ;
	      StringBuffer         lsSQLAppend  = new StringBuffer(200) ;
	      String               lsApplWhere  = null ;
	      VSORBSession         loORBSession = getParentApp().getSession().getORBSession() ;
	      AMSSecurityObject    loSecObj     = null ;
	
	      //Get the SearchRequest
	      loSrchReq = foQuery.getFilter () ;
	
	      if ( loSrchReq == null )
	      {
	         loSrchReq = new SearchRequest () ;
	      }
	
	      /* This portion of the code adds a new filter for showing only those records related
	       * to the applications that the user is currently logged into successfully.
	       */
	      try
	      {
	         loSecObj    = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
	         lsApplWhere = AMSSecurityObject.getApplicationWhere( loSecObj, "WF_APRV_WRK_LST" ) ;
	      }
	      catch ( RemoteException loRemExp )
	      {
	         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
	         return ;
	      }
	
	      if ( lsApplWhere != null )
	      {
	         /* append AND to the beginning of the SQL if SQLWhereClause exist */
	         if ( foQuery.getSQLWhereClause() != null && foQuery.getSQLWhereClause().trim().length() > 0 )
	         {
	            lsSQLAppend.append( " AND " ) ;
	         }
	         lsSQLAppend.append( lsApplWhere ) ;
	      } /* if ( lsApplWhere != null ) */
	
	      loSrchReq.add( lsSQLAppend.toString() ) ;
	      foQuery.addFilter( loSrchReq ) ;
	   } /* end modifyQuery() */
	
	   /**
	    * This method overrides the beforeGenerate method to set the page title
	    * so that the name of the workflow role is displayed as part of the
	    * page title.
	    *
	    * Modification Log : Kiran Hiranandani   - 08/29/02
	    *                                        - inital version
	    *
	    */
	   public void beforeGenerate()
	   {
	      TextContentElement loTCE   = (TextContentElement)getElementByName( "PageTitle" ) ;
	      String lsWorkflowRole      = null ;
	
	      if ( loTCE != null )
	      {
	         lsWorkflowRole = ((AMSWorklistPage)getSourcePage()).getCurrentWorklistDisplayName();
	
	         if ( lsWorkflowRole != null )
	         {
	            loTCE.setValue( lsWorkflowRole + " Manager Worklist" ) ;
	         } /* if ( lsWorkflowRole != null ) */
	         else
	         {
	            loTCE.setValue( "Manager Worklist" ) ;
	         } /* end else */
	      } /* end if ( loTCE != null ) */
	
	      super.beforeGenerate() ;
	   } /* end beforeGenerate() */
	
	}
	