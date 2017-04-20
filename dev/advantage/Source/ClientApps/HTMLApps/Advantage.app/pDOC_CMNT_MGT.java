//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.common.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.vfc.html.* ;
/*
**  pDOC_CMNT_MGT
*/

//{{FORM_CLASS_DECL
public class pDOC_CMNT_MGT extends pDOC_CMNT_MGTBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolFirstTime = true;
   
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pDOC_CMNT_MGT ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }

   
//{{EVENT_CODE
//{{EVENT_T3R_GEN_DOC_CTRL_BeforePickQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those records which belong to either the
 * common application or the application to which user logged in.
 *
 * @param foPick          Pick object which is doing the query
 * @param foDataSource    The data control which started the pick
 * @param foWhereClause   Where clause for the pick query
 * @param foOrderBy       The order by clause for pick query
 * @param foCancel        param for cancelling the query
 */
void T3R_GEN_DOC_CTRL_BeforePickQuery(Pick obj, DataSource dataSource, 
      VSOutParam foWhereClause, VSOutParam orderBy, VSOutParam cancel)
{
   //Write Event Code below this line
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
   lsApplWhereClause = AMSSecurityObject.getApplicationWhere( loSecObj, true ) ;

   /* Check if there is already an existing where clause */
   lsOldWhereClause = foWhereClause.stringValue() ;

   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsApplWhereClause = lsOldWhereClause + " AND " + lsApplWhereClause ;
   } /* end if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 ) */

   /* Reset the where clause in the main query */
   foWhereClause.setValue( lsApplWhereClause ) ;   
}
//END_EVENT_T3R_GEN_DOC_CTRL_BeforePickQuery}}
//{{EVENT_T1DOC_HDR_beforeQuery
void T1DOC_HDR_beforeQuery(VSQuery foQuery, VSOutParam foRsltSet )
{
   //Write Event Code below this line
   SearchRequest loOrderBy = new SearchRequest() ;

   /**
    * If the user is navigating to this page in the browse
    * mode and if no developer where clause has been specified
    * then let's add a where clause so that no data is retrieved
    * from the database. This is a temporary fix to improve
    * performance for opening the document catalog.
    */
   if (mboolFirstTime)
   {
      String lsDevWhere = T1DOC_HDR.getQueryInfo().getDevWhere();
      String lsQueryText = T1DOC_HDR.getOnScreenQueryText();

      if ( (lsDevWhere == null || lsDevWhere.trim().length() == 0 ) &&
           (lsQueryText == null || lsQueryText.trim().length() == 0 ))
      {
         SearchRequest loAddlFilter = new SearchRequest() ;
         loAddlFilter.add(" DOC_CD = '        ' AND DOC_ID = '     ' AND DOC_DEPT_CD = '       ' AND DOC_VERS_NO = -1 AND ");
         foQuery.addFilter(loAddlFilter);
      }
   } /* end if (lsDevWhere == null || lsDevWhere.trim().length() == 0) */

   modifyQuery(foQuery, foRsltSet);
}
//END_EVENT_T1DOC_HDR_beforeQuery}}
//{{EVENT_T2QRY_DOC_CMNT_beforeDelete
void T2QRY_DOC_CMNT_beforeDelete(VSRow row ,VSOutParam cancel ,VSOutParam response )
{
   //Write Event Code below this line
   try
   {
      //Set session property to indicate the delete event is from the page
      getParentApp().getSession().getORBSession().setProperty(SESSION_DOC_CMNT_MSG_TXT, 
                                                               SESSION_DOC_CMNT_ONLINE_DELETE);
   }
   catch (java.rmi.RemoteException loE)
   {
      raiseException( "Unable to set message properties", SEVERITY_LEVEL_SEVERE) ;
      return;   
   }
}
//END_EVENT_T2QRY_DOC_CMNT_beforeDelete}}
//{{EVENT_pDOC_CMNT_MGT_beforeGenerate
void pDOC_CMNT_MGT_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line
   mboolFirstTime = false;
   setDefaultActionName("AMSBrowse");
   DivElement loElem = (DivElement)getElementByName("doc_identifier");
   if("Management".equals(getCurrentTabName()) && loElem != null )
   {
      if(getRootDataSource().getCurrentRow() == null)
      {
         loElem.setVisible(false);
      }
      else
      {
         loElem.setVisible(true);
      }
   }
}
//END_EVENT_pDOC_CMNT_MGT_beforeGenerate}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T3R_GEN_DOC_CTRL.addPickListener(this);
	T1DOC_HDR.addDBListener(this);
	T2QRY_DOC_CMNT.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_MGT_beforeGenerate(docModel, cancel, output);
		}
	}
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T3R_GEN_DOC_CTRL) {
			T3R_GEN_DOC_CTRL_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1DOC_HDR) {
			T1DOC_HDR_beforeQuery(query , resultset );
		}
	}
	public void beforeDelete( DataSource obj, VSRow row ,VSOutParam cancel ,VSOutParam response ){
		Object source = obj;
		if (source == T2QRY_DOC_CMNT) {
			T2QRY_DOC_CMNT_beforeDelete(row ,cancel ,response );
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
      // For succesful Retrieving of records on first Enter
      setDefaultActionName("AMSBrowse");
      //Write code here for initializing your own control
      //or creating new control.

   }
   
   /**
    * Method for modifying query for retrieving document catalog entries.
    * This method will append the criteria for filtering based on
    * the applications to which the user is currently logged in. This
    * method will be called from before query event of catalog pages.
    *
    * @param  foQuery Query object for retrieving the document catalog entries.
    * @param  foResultSet Result Set
    */
   private void modifyQuery( VSQuery foQuery, VSOutParam foResultSet )
   {
      SearchRequest        loSearchRequest = null ;
      VSORBSession         loORBSession = parentApp.getSession().getORBSession() ;
      AMSSecurityObject    loSecObj = null ;
      String               lsOldWhereClause = null ;
      String               lsFinalClause = null ;

      /* Retrieve the application where clause */
      try
      {
         loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
         lsFinalClause = AMSSecurityObject.getApplicationWhere( loSecObj ) ;
      }
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return ;
      }

      /* Check if there is already an existing where clause */
      lsOldWhereClause = foQuery.getSQLWhereClause() ;
      if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
      {
         lsFinalClause = " AND " + lsFinalClause ;
      } /* if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 ) */

      /* Add the search request */
      loSearchRequest = new SearchRequest () ;
      loSearchRequest.add( lsFinalClause );

      /* Add SearchRequest into the main query */
      foQuery.addFilter (loSearchRequest) ;

   } /* end modifyQuery() */
}
