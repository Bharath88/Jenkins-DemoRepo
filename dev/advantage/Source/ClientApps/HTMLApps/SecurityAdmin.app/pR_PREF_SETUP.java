//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.*;
import java.rmi.RemoteException;
import advantage.IN_PAGE_TYPImpl;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;


import org.apache.commons.logging.Log;

/*
**  pR_PREF_SETUP
*/

//{{FORM_CLASS_DECL
public class pR_PREF_SETUP extends pR_PREF_SETUPBase

//END_FORM_CLASS_DECL}}
{
	// Boolean to check if the current user is admin.
	private boolean mboolIsAdmin = false;
	  /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pR_PREF_SETUP.class,
      AMSLogConstants.FUNC_AREA_SECURITY ) ;

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pR_PREF_SETUP ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T1R_PREF_SETUP_beforeQuery
void T1R_PREF_SETUP_beforeQuery(VSQuery query ,VSOutParam resultset )
{
	//Write Event Code below this line
	setQueryCriteria(query, resultset, this );
}
//END_EVENT_T1R_PREF_SETUP_beforeQuery}}
//{{EVENT_T3pR_PREF_DETL_afterPageNavigation
void T3pR_PREF_DETL_afterPageNavigation( PageNavigation nav )
{
	//Write Event Code below this line


}
//END_EVENT_T3pR_PREF_DETL_afterPageNavigation}}
//{{EVENT_T3pR_PREF_DETL_beforePageNavigation
void T3pR_PREF_DETL_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
	//Write Event Code below this line


}
//END_EVENT_T3pR_PREF_DETL_beforePageNavigation}}
//{{EVENT_pR_PREF_SETUP_beforeGenerate
void pR_PREF_SETUP_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   mboolIsAdmin = AMSSecurity.isUserAdmin(getUserID());

   try
   {
      if(!mboolIsAdmin)
      {
         // Check if the non-admin user is authorized for site-wide preferences.
    	 boolean mboolIsUserAuthorized = AMSSecurity.actionAuthorized(getUserID(),
                           getRootDataSource().getCurrentRow().getData("PAGE_CD").getString(),
                                        AMSCommonConstants.ACTN_CONFIGURE);

    	 AMSCheckboxElement loActiveTag = (AMSCheckboxElement)getElementByName("txtT1IS_ACTIVE");
         // if the non-admin is not authorized then disable the active flag.
    	 if(!mboolIsUserAuthorized)
	     {
            if(loActiveTag != null && loActiveTag.isEnabled())
            {
               loActiveTag.setEnabled(false);
        	}
	     }
    	 else
    	 {
    		 if(loActiveTag != null && !loActiveTag.isEnabled())
             {
                loActiveTag.setEnabled(true);
         	 }
    	 }
      }
   }
   catch(Exception e)
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", e);

   }
}
//END_EVENT_pR_PREF_SETUP_beforeGenerate}}
//{{EVENT_T2IN_PAGES_BeforePickQuery
void T2IN_PAGES_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
	//Write Event Code below this line

   whereClause.setValue(  "PAGE_TYP_CD = "+ IN_PAGE_TYPImpl.PAGE_TYP_DOCUMENTS);


}
//END_EVENT_T2IN_PAGES_BeforePickQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1R_PREF_SETUP.addDBListener(this);
	T3pR_PREF_DETL.addPageNavigationListener(this);
	addPageListener(this);
	T2IN_PAGES.addPickListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pR_PREF_SETUP_beforeGenerate(docModel, cancel, output);
		}
	}
	public void afterPageNavigation( PageNavigation obj ){
		Object source = obj;
		if (source == T3pR_PREF_DETL) {
			T3pR_PREF_DETL_afterPageNavigation( obj );
		}
	}
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T2IN_PAGES) {
			T2IN_PAGES_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T3pR_PREF_DETL) {
			T3pR_PREF_DETL_beforePageNavigation( obj, cancel, newPage );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_PREF_SETUP) {
			T1R_PREF_SETUP_beforeQuery(query , resultset );
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


	public static void setQueryCriteria(VSQuery foQuery ,VSOutParam foResultSet,AMSPage foPage )
	{
	   SearchRequest        loSearchRequest = null ;
	   String               lsWhereClause = null ;
	   String               lsOldWhereClause = null ;
	   VSORBSession         loORBSession = foPage.getParentApp().getSession().getORBSession() ;
	   AMSSecurityObject    loSecObj = null ;

	   try
	   {
	      loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
	   }
	   catch ( RemoteException loRemExp )
	   {
	      foPage.raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
	      foResultSet.setValue( foQuery.getNewResultSet() ) ;
	      return ;
	   }

	   /*
	    * Retrieve where clause for Applications to which the user is currently
	    * logged in. The second parameter is set to true to retrieve all the
	    * common pages. Common pages will be retrieved only if the application
	    * to which the user has logged into is not a stand-alone application.
       * The third parameter is set to true so that Secondary applications
       * like MSS,ESS are visible through PREFSTUP
       */
      lsWhereClause = AMSSecurityObject.getApplicationWhere( loSecObj,true,true) ;

	   loSearchRequest = new SearchRequest() ;
	   lsOldWhereClause = foQuery.getSQLWhereClause() ;
	   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
	   {
	      lsWhereClause = " AND " + lsWhereClause ;
	   }

	   loSearchRequest.add( lsWhereClause );

	   /* Add SearchRequest into the main query */
	   foQuery.addFilter (loSearchRequest) ;
	}

}