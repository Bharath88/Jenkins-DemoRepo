//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.client.dbitem.AMSWorkspace;
import com.amsinc.gems.adv.common.* ;
import java.rmi.RemoteException;

/*
**  ConfigWkspsPropertyPage*/

//{{FORM_CLASS_DECL
public class ConfigWkspsPropertyPage extends ConfigWkspsPropertyPageBase

//END_FORM_CLASS_DECL}}
implements AMSAvailableSelectionPage, AMSReorderingPage, AMSConfigParentPage
{
   private static final String QUERY_NAME          = "IN_USER_WKGP" ;
   private static final String USER_WHERE_CLAUSE   = "USER_ID=" ;
   private static final String WKGP_WHERE_CLAUSE   = "WKGP_UNID=" ;
   private static final String WKGP_INVALID_WHERE_CLAUSE  = "WKGP_UNID=-1 AND USER_ID IS NULL" ;


   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigWkspsPropertyPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T1IN_WKSP_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those workspaces which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 07/10/02
 *                                        - inital version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1IN_WKSP_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
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
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }

   /* Check if there is already an existing where clause */
   lsOldWhereClause = foQuery.getSQLWhereClause() ;
   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsFinalClause = " AND " + lsFinalClause ;
   }

   /* Add the search request */
   loSearchRequest = new SearchRequest () ;
   loSearchRequest.add( lsFinalClause );

   /* Add SearchRequest into the main query */
   foQuery.addFilter (loSearchRequest) ;
}
//END_EVENT_T1IN_WKSP_beforeQuery}}
//{{EVENT_T4IN_WKSP_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those workspaces which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 07/10/02
 *                                        - inital version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T4IN_WKSP_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
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
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }

   /* Check if there is already an existing where clause */
   lsOldWhereClause = foQuery.getSQLWhereClause() ;
   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsFinalClause = " AND " + lsFinalClause ;
   }

   /* Add the search request */
   loSearchRequest = new SearchRequest () ;
   loSearchRequest.add( lsFinalClause );

   /* Add SearchRequest into the main query */
   foQuery.addFilter (loSearchRequest) ;
}
//END_EVENT_T4IN_WKSP_beforeQuery}}
//{{EVENT_T2IN_WKSP_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those unused workspaces which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 07/10/02
 *                                        - inital version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T2IN_WKSP_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
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
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }

   /* Check if there is already an existing where clause */
   lsOldWhereClause = foQuery.getSQLWhereClause() ;
   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsFinalClause = " AND " + lsFinalClause ;
   }

   /* Add the search request */
   loSearchRequest = new SearchRequest () ;
   loSearchRequest.add( lsFinalClause );

   /* Add SearchRequest into the main query */
   foQuery.addFilter (loSearchRequest) ;
}
//END_EVENT_T2IN_WKSP_beforeQuery}}
//{{EVENT_T3IN_WKSP_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those used workspaces which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 07/10/02
 *                                        - inital version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T3IN_WKSP_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
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
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }

   /* Check if there is already an existing where clause */
   lsOldWhereClause = foQuery.getSQLWhereClause() ;
   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsFinalClause = " AND " + lsFinalClause ;
   }

   /* Add the search request */
   loSearchRequest = new SearchRequest () ;
   loSearchRequest.add( lsFinalClause );

   /* Add SearchRequest into the main query */
   foQuery.addFilter (loSearchRequest) ;
}
//END_EVENT_T3IN_WKSP_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_WKSP.addDBListener(this);
	T4IN_WKSP.addDBListener(this);
	T2IN_WKSP.addDBListener(this);
	T3IN_WKSP.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_WKSP) {
			T1IN_WKSP_beforeQuery(query , resultset );
		}
	
		if (source == T4IN_WKSP) {
			T4IN_WKSP_beforeQuery(query , resultset );
		}
	
		if (source == T2IN_WKSP) {
			T2IN_WKSP_beforeQuery(query , resultset );
		}
	
		if (source == T3IN_WKSP) {
			T3IN_WKSP_beforeQuery(query , resultset );
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

   public void afterPageInitialize()
   {
      String lsWkgpWhereClause ;

      lsWkgpWhereClause = buildWkgpWhereClause() ;
      if ( lsWkgpWhereClause != null )
      {
         VSQueryInfo loQueryInfo = T4IN_WKSP.getQueryInfo() ;
         T4IN_WKSP.setQueryInfo("IN_WKSP", loQueryInfo.getDevWhere() + " AND " + lsWkgpWhereClause, "", " WKGP_NM,  WKSP_NM", false) ;
      } /* end if ( lsWkgpWhereClause != null ) */
   else
   {
      //dont display anything  - set default where clause
      T4IN_WKSP.setQueryInfo("IN_WKSP", WKGP_INVALID_WHERE_CLAUSE , "", " ", false) ;
   } /* end else */

      super.afterPageInitialize() ;
   } /* end afterPageInitialize() */

   private String buildWkgpWhereClause()
   {
      VSResultSet  loResultSet = null;
      try
      {
         VSQuery      loQuery ;
         VSSession    loSession      = getParentApp().getSession() ;
         int          liNumRows ;
         StringBuffer lsbWhereClause = null ;
   
         loQuery = new VSQuery( loSession, QUERY_NAME, USER_WHERE_CLAUSE +
               AMSSQLUtil.getANSIQuotedStr(loSession.getLogin(), true), "" ) ;
   
         loResultSet = loQuery.execute() ;
         loResultSet.last() ;
   
         liNumRows = loResultSet.getRowCount() ;
   
         if ( liNumRows < 1 )
         {
            return null ;
         } /* end if ( liNumRows < 1 ) */
   
         lsbWhereClause = new StringBuffer( liNumRows * 17 ) ;
   
         lsbWhereClause.append ("(");
         for ( int liRowCtr=1 ; liRowCtr<=liNumRows ; liRowCtr++ )
         {
            VSRow loCurrRow = loResultSet.getRowAt( liRowCtr ) ;
   
            if ( loCurrRow != null )
            {
               lsbWhereClause.append( WKGP_WHERE_CLAUSE + loCurrRow.getData(2).getString() ) ;
               if ( liRowCtr != liNumRows )
               {
                  lsbWhereClause.append( " OR " ) ;
               } /* end if ( liRowCtr != liNumRows ) */
            } /* end if ( loCurrRow != null ) */
         } /* end for ( int liRowCtr=1 ; liRowCtr<=liNumRows ; liRowCtr++ ) */
   
         lsbWhereClause.append(")");
         return lsbWhereClause.toString() ;
      }
      finally
      {
         if( loResultSet!= null )
         {
            loResultSet.close();
         }
      }//end finally
      
   } /* end buildWkgpWhereClause() */

   /**
    * This method will be called whenever the user wants
    * to move an item from the list of available items to
    * the list of selected items.
    *
    * Modification Log : Mark Shipley      - 12/13/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the action
    */
   public boolean addToSelected( PLSRequest foPLSReq )
   {
      VSRow loCurrRow = T2IN_WKSP.getCurrentRow() ;
      int   liSeqNo   = getSequenceNumber( true ) ;

      if ( loCurrRow == null )
      {
         return false ;
      } /* end if ( loCurrRow == null ) */

      liSeqNo = liSeqNo + 1 ;

      AMSWorkspace.setSeqNo( loCurrRow, liSeqNo ) ;
      AMSWorkspace.setInUse( loCurrRow, true ) ;

      T2IN_WKSP.updateDataSource() ;

      T3IN_WKSP.executeQuery() ;
      T2IN_WKSP.executeQuery() ;

      return true ;
   } /* end addToSelected() */

   /**
    * This method will be called whenever the user wants
    * to move an item from the list of selected items back
    * to the list of available items.
    *
    * Modification Log : Mark Shipley      - 05/22/00
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the action
    */
   public boolean removeFromSelected( PLSRequest foPLSReq )
   {
      VSRow loCurrRow = T3IN_WKSP.getCurrentRow() ; ;
      int   liSeqNo   = getSequenceNumber( true ) ;

      if ( loCurrRow == null )
      {
         return false ;
      } /* end if ( loCurrRow == null ) */

      liSeqNo = liSeqNo + 1 ;

      AMSWorkspace.setSeqNo( loCurrRow, liSeqNo ) ;
      AMSWorkspace.setInUse( loCurrRow, false ) ;

      T3IN_WKSP.updateDataSource() ;

      T2IN_WKSP.executeQuery() ;
      T3IN_WKSP.executeQuery() ;

      return true ;
   } /* end removeFromSelected() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the top of the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T3IN_WKSP.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loFirstRow  = loResultSet.first() ;

      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

      AMSWorkspace.setSeqNo( loCurrRow, getSequenceNumber( false ) - 1 ) ;

      T3IN_WKSP.updateDataSource() ;
      T3IN_WKSP.executeQuery() ;

      return true ;
   } /* end moveItemToTop() */

   /**
    * This method will be called whenever the user wants
    * to move an item up one spot in the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemUp()
   {
      VSResultSet loResultSet = T3IN_WKSP.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loPrevRow   = loResultSet.previous() ;
      int         liCurrSeq ;
      int         liPrevSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loPrevRow == null ) )
      {
         T3IN_WKSP.first() ;
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSWorkspace.getSeqNo( loCurrRow ) ;
      liPrevSeq = AMSWorkspace.getSeqNo( loPrevRow ) ;
      liLastSeq = getSequenceNumber( true ) ;

      AMSWorkspace.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T3IN_WKSP.updateDataSource() ;
      AMSWorkspace.setSeqNo( loPrevRow, liCurrSeq ) ;
      T3IN_WKSP.updateDataSource() ;
      AMSWorkspace.setSeqNo( loCurrRow, liPrevSeq ) ;
      T3IN_WKSP.updateDataSource() ;

      T3IN_WKSP.executeQuery() ;
      T3IN_WKSP.getResultSet().getRowAt( liCurrRow - 1 ) ;

      return true ;
   } /* end moveItemUp() */

   /**
    * This method will be called whenever the user wants
    * to move an item down one spot in the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T3IN_WKSP.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loNextRow   = loResultSet.next() ;
      int         liCurrSeq ;
      int         liNextSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loNextRow == null ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loNextRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSWorkspace.getSeqNo( loCurrRow ) ;
      liNextSeq = AMSWorkspace.getSeqNo( loNextRow ) ;
      liLastSeq = getSequenceNumber( true ) ;

      AMSWorkspace.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T3IN_WKSP.updateDataSource() ;
      AMSWorkspace.setSeqNo( loNextRow, liCurrSeq ) ;
      T3IN_WKSP.updateDataSource() ;
      AMSWorkspace.setSeqNo( loCurrRow, liNextSeq ) ;
      T3IN_WKSP.updateDataSource() ;

      T3IN_WKSP.executeQuery() ;
      T3IN_WKSP.getResultSet().getRowAt( liCurrRow + 1 ) ;

      return true ;
   } /* end moveItemDown() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the bottom of the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T3IN_WKSP.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loLastRow   = loResultSet.last() ;

      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

      AMSWorkspace.setSeqNo( loCurrRow, getSequenceNumber( true ) + 1 ) ;

      T3IN_WKSP.updateDataSource() ;
      T3IN_WKSP.executeQuery() ;
      T3IN_WKSP.getResultSet().last() ;

      return true ;
   } /* end moveItemToBottom() */

   /**
    * This method is called whenever the user chooses
    * to copy an item in the page.
    *
    * Modification Log : Jayant Pal   - 05/30/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return Object The copied item
    */
   public Object copyItem( PLSRequest foPLSReq )
   {
      int         liWhichRS    = getWhichRS( foPLSReq, "Copy" ) ;
      DataSource  loDataSource = null ;
      VSResultSet loResultSet ;
      VSRow       loCurrRow ;

      switch ( liWhichRS )
      {
         case 1 :
            loDataSource = T1IN_WKSP ;
            break ;
         case 2 :
            loDataSource = T2IN_WKSP ;
            break ;
         case 3 :
            loDataSource = T3IN_WKSP ;
            break ;
         case 4 :
            loDataSource = T4IN_WKSP ;
            break ;
         default :
            return null ;
      } /* end switch ( liWhichRS ) */

      loResultSet = loDataSource.getResultSet() ;
      loCurrRow   = loResultSet.current() ;

      if ( loCurrRow != null )
      {
         long llWkspID = AMSWorkspace.getID( loCurrRow ) ;

         if ( llWkspID != -1 )
         {
            return new Long( llWkspID ) ;
         } /* end if ( llWkspID != -1 ) */
      } /* end if ( loCurrRow != null ) */

      return null ;
   } /* end copyItem() */

   /**
    * This method is called to retrieve the type of the object
    * retrieved during the last call to copyItem().
    *
    * Modification Log : Jayant Pal   - 05/30/00
    *                                 - inital version
    *
    * @return int The last copied item type
    */
   public int getLastCopiedType()
   {
      return ConfigMaster.WORKSPACE_TYPE ;
   } /* end getLastCopiedType() */

   /**
    * This method is called whenever the user executes
    * a paste action.  Ths object stored in the configurator
    * master page is passed along with its type.
    *
    * Modification Log : Mark Shipley      - 12/13/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @param foPasteObject The object to paste
    * @param fiObjectType The type of the object to paste
    * @return boolean The success or failure of the paste action
    */
   public boolean pasteItem( PLSRequest foPLSReq, Object foPasteObject,
                             int fiObjectType )
   {
      VSResultSet loResultSet = null;
      try
      {
         switch ( fiObjectType )
         {
            case ConfigMaster.WORKSPACE_TYPE :
            {
               DataSource  loDataSource = null ;
               int         liWhichRS    = getWhichRS( foPLSReq, "Paste" ) ;
               VSSession   loSession    = getParentApp().getSession() ;
               String      lsUserID     = loSession.getLogin() ;
               VSRow       loLastRow ;
               VSRow       loCopyRow ;
               long        llWkspID     = ((Long)foPasteObject).longValue() ;
               int         liNumRows ;
               int         liSeqNo      = 0 ;
               boolean     lboolInUse   = true ;

               switch ( liWhichRS )
               {
                  case 2 :
                     loDataSource = T2IN_WKSP ;
                     lboolInUse = false ;
                     break ;
                  case 3 :
                     loDataSource = T3IN_WKSP ;
                     lboolInUse = true ;
                     break ;
                  default :
                     return false ;
               } /* end switch ( liWhichRS ) */
               liSeqNo   = getSequenceNumber( true ) + 1 ;

               loResultSet =  AMSWorkspace.getResultSet( loSession, "WKSP_UNID="
                                                         + llWkspID, "" ) ;
               loResultSet.last() ;
               liNumRows = loResultSet.getRowCount() ;
               if ( liNumRows != 1 )
               {
                  loResultSet.close() ;
                  loResultSet = null;
                  return false ;
               } /* end if ( liNumRows != 1 ) */

               loCopyRow = loResultSet.getRowAt( 1 ) ;
               AMSWorkspace.deepCopyWksp( loSession, loCopyRow, lsUserID,
                                          liSeqNo, lboolInUse ) ;

               loResultSet.close() ;
               loResultSet = null;
               loDataSource.executeQuery() ;
               loDataSource.last() ;
               return true ;
            }
            default :
               break ;
         } /* end switch ( fiObjectType ) */

         return false ;
      }//end try
      finally
      {
         if( loResultSet!= null )
         {
            loResultSet.close();
         }
      }//end finally
      
   } /* end pasteItem() */

   /**
    * This method is called whenever the user clicks the
    * "New" action button on the page.  What is actually
    * created is up to the implementing page.
    *
    * Modification Log : Mark Shipley      - 12/13/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 7/10/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the new item action
    */
   public VSPage newItem( PLSRequest foPLSReq )
   {
      PLSApp            loPLSApp     = getParentApp() ;
      VSSession         loVSSession  = loPLSApp.getSession() ;
      int               liWhichRS    = getWhichRS( foPLSReq, "New" ) ;
      VSResultSet       loResultSet ;
      VSRow             loLastRow ;
      VSRow             loNewRow ;
      int               liSeqNo      = 0 ;
      DataSource        loDataSource = null ;
      boolean           lbInUse      = false ;
      String            lsUserID     = loVSSession.getLogin() ;
      VSORBSession      loORBSession = loVSSession.getORBSession() ;
      AMSSecurityObject loSecObj     = null ;
      int               liDefaultAppID ;

      try
      {
         loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
      }
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return null ;
      }

      /*
       * Default application ID - first application in the list.
       */
      liDefaultAppID = ( loSecObj.getApplicationListArray() )[0].intValue() ;

      switch ( liWhichRS )
      {
         case 2 :
            loDataSource = T2IN_WKSP ;
            lbInUse = false ;
            break ;
         case 3 :
            loDataSource = T3IN_WKSP ;
            lbInUse = true ;
            break ;
         default :
            return this ;
      } /* end switch ( liWhichRS ) */

      loResultSet = loDataSource.getResultSet() ;
      loLastRow = loResultSet.last() ;
      loNewRow  = loResultSet.insert() ;

      if ( loLastRow != null )
      {
         liSeqNo = AMSWorkspace.getSeqNo( loLastRow ) + 1;
      } /* end if ( loLastRow != null ) */

      AMSWorkspace.setNewID(  loNewRow ) ;
      AMSWorkspace.setType(   loNewRow, AMSWorkspace.WKSP_TYPE_USER ) ;
      AMSWorkspace.setUserID( loNewRow, lsUserID ) ;
      AMSWorkspace.setInUse(  loNewRow, lbInUse ) ;
      AMSWorkspace.setSeqNo(  loNewRow, liSeqNo ) ;
      AMSWorkspace.setName(   loNewRow, "New Workspace" ) ;
      /* Set the application ID */
      AMSWorkspace.setApplication( loNewRow, liDefaultAppID ) ;

      loDataSource.updateDataSource() ;

      return this ;
   } /* end newItem() */

   /**
    * This method is called whenever the user clicks the
    * "Delete" action button on the page.
    *
    * Modification Log : Mark Shipley - 12/13/2000
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the delete action
    */
   public boolean deleteItem( PLSRequest foPLSReq )
   {
      DataSource  loDataSource = null ;
      VSRow       loDeleteRow ;
      int         liWhichRS    = getWhichRS( foPLSReq, "Delete" ) ;

      switch ( liWhichRS )
      {
         case 2 :
            loDataSource = T2IN_WKSP ;
            break ;
         case 3 :
            loDataSource = T3IN_WKSP ;
            break ;
         default :
            return false ;
      } /* end switch ( liWhichRS ) */

      loDeleteRow = loDataSource.getCurrentRow() ;

      if ( loDeleteRow == null )
      {
         return false ;
      } /* end if ( loDeleteRow == null ) */

      loDataSource.delete() ;

      return true ;
   } /* end deleteItem() */

   private int getWhichRS( PLSRequest foPLSReq, String fsSuffix )
   {
      if ( foPLSReq.getParameter( "T1IN_WKSP" + fsSuffix ) != null )
      {
         return 1 ;
      } /* end if ( foPLSReq.getParameter( "T1IN_WKSP" + fsSuffix ) != null ) */
      else if ( foPLSReq.getParameter( "T2IN_WKSP" + fsSuffix ) != null )
      {
         return 2 ;
      } /* end else if ( foPLSReq.getParameter( "T2IN_WKSP" + fsSuffix ) != null ) */
      else if ( foPLSReq.getParameter( "T3IN_WKSP" + fsSuffix ) != null )
      {
         return 3 ;
      } /* end else if ( foPLSReq.getParameter( "T3IN_WKSP" + fsSuffix ) != null ) */
      else if ( foPLSReq.getParameter( "T4IN_WKSP" + fsSuffix ) != null )
      {
         return 4 ;
      } /* end else if ( foPLSReq.getParameter( "T4IN_WKSP" + fsSuffix ) != null ) */

      return 0 ;
   } /* end getWhichRS */

   /**
    * This method retrieves the highest or the lowest sequence number
    * being used by a user without regard to the applications that he's
    * logged into.
    *
    * Modification Log : Kiran Hiranandani - 07/10/2002
    *                                      - inital version
    *
    * @param fboolLastRow true if last sequence number is desired
    * @return int The last or the first sequence number
    */
   private int getSequenceNumber( boolean fboolLastRow )
   {
      VSResultSet  loResultSet = null ;
      try
      {
         VSSession    loSession   = getParentApp().getSession() ;
         VSQuery      loQuery     = null ;
         VSRow        loRow ;
         String       lsWhere     = null ;
         int          liSeqNo     = 0 ;
   
         lsWhere  = "WKSP_OWNER=\'" + loSession.getLogin() + "\'" ;
   
         if ( fboolLastRow )
         {
            loQuery = new VSQuery( loSession, "IN_WKSP", lsWhere , " WKSP_SEQ_NO DESC" ) ;
            loResultSet = loQuery.execute() ;
            loRow = loResultSet.first() ;
            if ( loRow != null )
            {
               liSeqNo = AMSWorkspace.getSeqNo( loRow ) ;
            } /* end if ( loRow != null ) */
         } /* end if ( fboolLastRow ) */
         else
         {
            loQuery = new VSQuery( loSession, "IN_WKSP", lsWhere , " WKSP_SEQ_NO ASC" ) ;
            loResultSet = loQuery.execute() ;
            loRow = loResultSet.first() ;
            if ( loRow != null )
            {
               liSeqNo = AMSWorkspace.getSeqNo( loRow ) ;
            } /* end if ( loRow != null ) */
         } /* end else */   
         return liSeqNo ;
      }//end try
      finally
      {
         /* Close the result set */
         if ( loResultSet != null )
         {
            loResultSet.close() ;
         }//end if
      }
   } /* end getSequenceNumber() */
}