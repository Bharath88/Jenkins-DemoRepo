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
import com.amsinc.gems.adv.client.dbitem.AMSFavorite ;
import com.amsinc.gems.adv.common.* ;
import java.rmi.RemoteException;

/*
**  ConfigFavCommonPropertyPage
*/

//{{FORM_CLASS_DECL
public class ConfigFavCommonPropertyPage extends ConfigFavCommonPropertyPageBase

//END_FORM_CLASS_DECL}}
implements AMSConfigParentPage, AMSReorderingPage
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigFavCommonPropertyPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_ConfigFavCommonPropertyPage_beforeActionPerformed
void ConfigFavCommonPropertyPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   syncRecordCurrency( T1IN_FAV, preq ) ;
}
//END_EVENT_ConfigFavCommonPropertyPage_beforeActionPerformed}}
//{{EVENT_T1IN_FAV_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only the records with PG_SHW_FL=1. Additionally the
 * where clause is set up to retrieve only those pages which belong
 * to applications to which the user has logged in.
 *
 * Modification Log : Numhom Suravee      - 05/10/01
 *                                        - inital version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1IN_FAV_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
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
      lsFinalClause = AMSSecurityObject.getApplicationWhere( loSecObj, true ) ;
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
//END_EVENT_T1IN_FAV_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1IN_FAV.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			ConfigFavCommonPropertyPage_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_FAV) {
			T1IN_FAV_beforeQuery(query , resultset );
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
    * This method is called whenever the user chooses
    * to copy a favorite in the page.
    *
    * Modification Log : Mark Shipley - 06/19/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return Object The copied item
    */
   public Object copyItem( PLSRequest foPLSReq )
   {
      VSRow       loCopyRow   = T1IN_FAV.getCurrentRow() ;

      if ( loCopyRow == null )
      {
         return null ;
      } /* end if ( loCopyRow == null ) */

      return AMSFavorite.deepCopy( getParentApp().getSession(), loCopyRow ) ;
   } /* end copyItem() */

   /**
    * This method is called to retrieve the type of the object
    * retrieved during the last call to copyItem().
    *
    * Modification Log : Mark Shipley - 06/19/00
    *                                 - inital version
    *
    * @return int The last copied item type
    */
   public int getLastCopiedType()
   {
      return ConfigMaster.FAVORITE_TYPE ;
   } /* end getLastCopiedType() */

   /**
    * This method is called whenever the user executes
    * a paste action.
    *
    * Modification Log : Mark Shipley      - 06/16/00
    *                                      - inital version
    *                    Kiran Hiranandani - 07/22/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @param foPasteObject The object to paste
    * @param fiObjectType The type of the object to paste
    * @return boolean The success or failure of the paste action
    */
   public boolean pasteItem( PLSRequest foPLSReq, Object foPasteObject, int fiObjectType )
   {
      try
      {
         switch ( fiObjectType )
         {
            case ConfigMaster.FAVORITE_TYPE :
            {
               AMSFavorite loFav = (AMSFavorite)foPasteObject ;
               VSResultSet loResultSet = T1IN_FAV.getResultSet() ;
               VSRow       loLastRow   = loResultSet.last() ;
               VSRow       loNewRow ;
               int         liSeqNo     = -1 ;

               if ( loLastRow != null )
               {
                 liSeqNo = AMSFavorite.getSequenceNumber( parentApp.getSession(), true ) ;
               } /* end if ( loLastRow != null ) */

               loFav.setSeqNo( liSeqNo + 1 ) ;

               loNewRow = loResultSet.insert() ;
               AMSFavorite.fillNewRow( loNewRow, loFav ) ;

               T1IN_FAV.updateDataSource() ;

               AMSFavorite.deepPaste( loFav, getParentApp().getSession() ) ;

               return true ;
            }
            default :
               break ;
         } /* end switch ( fiObjectType ) */
      } /* end try */
      catch ( Throwable foExp )
      {}

      return false ;
   } /* end pasteItem() */

   /**
    * This method is called whenever the user clicks the
    * "New" action button on the page.  It creates a new
    * folder as a child of the folder for which the property
    * page is shown. For stand alone logins the Application ID
    * of the new Item is set to the ID of the stand alone
    * application. For non stand alone applications, this value
    * is left null.
    *
    * Modification Log : Mark Shipley      - 06/16/00
    *                                      - inital version
    *                    Kiran Hiranandani - 07/22/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the new item action
    */
   public VSPage newItem( PLSRequest foPLSReq )
   {
      VSResultSet          loResultSet = T1IN_FAV.getResultSet() ;
      VSRow                loLastRow   = loResultSet.last() ;
      VSRow                loNewRow    = loResultSet.insert() ;
      int                  liSeqNo     = 0 ;
      VSORBSession         loORBSession = parentApp.getSession().getORBSession() ;
      AMSSecurityObject    loSecObj = null ;

      try
      {
         loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
      }
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return null ;
      }

      if ( loLastRow != null )
      {
         liSeqNo = AMSFavorite.getSequenceNumber( parentApp.getSession(), true ) + 1;
      } /* end if ( loLastRow != null ) */

      AMSFavorite.setUserID(   loNewRow, getParentApp().getSession().getLogin() ) ;
      AMSFavorite.setSeqNo(    loNewRow, liSeqNo ) ;
      AMSFavorite.setName(     loNewRow, "New Folder" ) ;
      AMSFavorite.setIsFolder( loNewRow, true ) ;

      /*
       * If application is stand alone then Application ID belongs to the currently
       * logged in application - else it is the common application ID
       */
      if ( loSecObj.isStandAloneLogin() )
      {
         AMSFavorite.setApplication(   loNewRow,
               ( loSecObj.getApplicationListArray() )[0].intValue() ) ;
      } /* end if ( loSecObj.isStandAloneLogin() ) */
      else
      {
         AMSFavorite.setApplication( loNewRow, AMSSecurityObject.COMMON_APPL_ID ) ;
      }

      T1IN_FAV.updateDataSource() ;

      return this ;
   } /* end newItem() */

   /**
    * This method is called whenever the user clicks the
    * "Delete" action button on the page.
    *
    * Modification Log : Mark Shipley - 06/16/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the delete action
    */
   public boolean deleteItem( PLSRequest foPLSReq )
   {
      VSRow                   loDeleteRow ;

      loDeleteRow = T1IN_FAV.getCurrentRow() ;

      if ( loDeleteRow == null )
      {
         return false ;
      } /* end if ( loDeleteRow == null ) */

      T1IN_FAV.delete() ;

      return true ;
   } /* end deleteItem() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the top of the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T1IN_FAV.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loFirstRow  = loResultSet.first() ;

      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

      AMSFavorite.setSeqNo( loCurrRow,
            AMSFavorite.getSequenceNumber( parentApp.getSession(), false ) - 1 ) ;

      T1IN_FAV.updateDataSource() ;
      T1IN_FAV.executeQuery() ;

      return true ;
   } /* end moveItemToTop() */

   /**
    * This method will be called whenever the user wants
    * to move an item up one spot in the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemUp()
   {
      VSResultSet loResultSet = T1IN_FAV.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loPrevRow   = loResultSet.previous() ;
      VSRow       loLastRow   = loResultSet.last() ;
      int         liCurrSeq ;
      int         liPrevSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) )
      {
         T1IN_FAV.first() ;
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSFavorite.getSeqNo( loCurrRow ) ;
      liPrevSeq = AMSFavorite.getSeqNo( loPrevRow ) ;
      liLastSeq = AMSFavorite.getSequenceNumber( parentApp.getSession(), true ) ;

      AMSFavorite.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T1IN_FAV.updateDataSource() ;
      AMSFavorite.setSeqNo( loPrevRow, liCurrSeq ) ;
      T1IN_FAV.updateDataSource() ;
      AMSFavorite.setSeqNo( loCurrRow, liPrevSeq ) ;
      T1IN_FAV.updateDataSource() ;

      T1IN_FAV.executeQuery() ;

      return true ;
   } /* end moveItemUp() */

   /**
    * This method will be called whenever the user wants
    * to move an item down one spot in the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T1IN_FAV.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loNextRow   = loResultSet.next() ;
      VSRow       loLastRow   = loResultSet.last() ;
      int         liCurrSeq ;
      int         liNextSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loNextRow == null ) || ( loLastRow == null ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loNextRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSFavorite.getSeqNo( loCurrRow ) ;
      liNextSeq = AMSFavorite.getSeqNo( loNextRow ) ;
      liLastSeq = AMSFavorite.getSequenceNumber( parentApp.getSession(), true ) ;

      AMSFavorite.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T1IN_FAV.updateDataSource() ;
      AMSFavorite.setSeqNo( loNextRow, liCurrSeq ) ;
      T1IN_FAV.updateDataSource() ;
      AMSFavorite.setSeqNo( loCurrRow, liNextSeq ) ;
      T1IN_FAV.updateDataSource() ;

      T1IN_FAV.executeQuery() ;

      return true ;
   } /* end moveItemDown() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the bottom of the list.
    *
    * Modification Log : Mark Shipley      - 06/19/00
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T1IN_FAV.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loLastRow   = loResultSet.last() ;

      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

      AMSFavorite.setSeqNo( loCurrRow,
            AMSFavorite.getSequenceNumber( parentApp.getSession(), true ) + 1 ) ;

      T1IN_FAV.updateDataSource() ;
      T1IN_FAV.executeQuery() ;

      return true ;
   } /* end moveItemToBottom() */

}