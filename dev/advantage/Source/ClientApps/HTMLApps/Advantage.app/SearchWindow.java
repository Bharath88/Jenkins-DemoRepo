//{{IMPORT_STMTS
package advantage.Advantage;
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

/*
**  SearchWindow
*/

//{{FORM_CLASS_DECL
public class SearchWindow extends SearchWindowBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   /*
    * Database column code for page type code
    */
   public static String PAGE_SHOW_FLAG = "PG_SHW_FL";

   private boolean mboolFirstTime = true;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public SearchWindow ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

   }




//{{EVENT_CODE
//{{EVENT_T1IN_PAGES_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only the records with PG_SHW_FL=1. Additionally the
 * where clause is set up to retrieve only those pages which belong
 * to applications to which the user has logged in.
 *
 * Modification Log : Numhom Suravee      - 05/10/01
 *                                        - inital version
 *                    Kiran Hiranandani   - 07/09/2002
 *                                        - Portal Integration Changes
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1IN_PAGES_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
    /*
		 * code in this method has been moved to setQueryCriteria which is 
		 * also called from GblNavSearchWindow	
	   */
   setQueryCriteria(foQuery,foResultSet,this);
  
} /* end T1IN_PAGES_beforeQuery() */


//END_EVENT_T1IN_PAGES_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_PAGES.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_PAGES) {
			T1IN_PAGES_beforeQuery(query , resultset );
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

   public void setSourcePage(VSPage foSrcPage)
   {
      super.setSourcePage( foSrcPage );

      /**
       * Let us pin the page only if this page is being accessed
       * from the application navigation panel
       */
      if ( mboolFirstTime )
      {
         if ( ( foSrcPage != null ) && ( foSrcPage instanceof AppNavPanel ) )
         {
            setPin(true);
            mboolFirstTime = false;
         } /* end if ( ( foSrcPage != null ) && ( foSrcPage instanceof AppNavPanel ) ) */
      } /* end if ( mboolFirstTime ) */
   }

   public String generate()
   {
      return super.generate();
   }
		
	public static void setQueryCriteria(VSQuery foQuery ,VSOutParam foResultSet,AMSPage foPage )
	{
	   SearchRequest        loSearchRequest = null ;
	   String               lsWhereClause = null ;
	   String               lsFinalClause = null ;
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
	    */
	   lsWhereClause = AMSSecurityObject.getApplicationWhere( loSecObj, true ) ;

	   /*
	    * Add PG_SHW_FL=1 into SearchRequest and the Application Where clause
	    */
	   lsFinalClause = " ( ( " + advantage.Advantage.SearchWindow.PAGE_SHOW_FLAG + " = 1 ) AND ( " + lsWhereClause + " ) ) " ;

	   loSearchRequest = new SearchRequest() ;
	   lsOldWhereClause = foQuery.getSQLWhereClause() ;
	   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
	   {
	      lsFinalClause = " AND " + lsFinalClause ;
	   }

	   loSearchRequest.add( lsFinalClause );

	   /* Add SearchRequest into the main query */
	   foQuery.addFilter (loSearchRequest) ;
	}
	
}
