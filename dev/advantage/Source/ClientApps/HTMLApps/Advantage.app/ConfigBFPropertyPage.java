//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.client.dbitem.AMSBusinessFunctionItem ;
import com.amsinc.gems.adv.client.dbitem.AMSBusinessFunction ;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.*;
import java.rmi.RemoteException;

/*
**  ConfigBFPropertyPage
*/

//{{FORM_CLASS_DECL
public class ConfigBFPropertyPage extends ConfigBFPropertyPageBase

//END_FORM_CLASS_DECL}}
implements AMSReorderingPage, AMSConfigParentPage
{
   private boolean mboolPickCancel = false ;
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigBFPropertyPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T3IN_PAGES_AfterPickFill
void T3IN_PAGES_AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow)
{
   mboolPickCancel = false ;
   T2IN_BF_ITEM.updateDataSource() ;
}
//END_EVENT_T3IN_PAGES_AfterPickFill}}
//{{EVENT_T2IN_BF_ITEM_beforeQuery
/**
 * This method is called before the query is performed on the
 * document catalog. This method is used to set up the where clause
 * to retrieve only those documents which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 08/12/02
 *                                          initial version
 *
 * @param foQuery       Reference to the query statement
 * @param foResultSet   Result Set
 */
void T2IN_BF_ITEM_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
{
   SearchRequest        loSearchRequest = null ;
   String               lsWhereClause = null ;
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
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }

   /* Retrieve where clause for Applications  */
   lsWhereClause = AMSSecurityObject.getApplicationWhere( loSecObj, true ) ;

   loSearchRequest = new SearchRequest() ;
   lsOldWhereClause = foQuery.getSQLWhereClause() ;
   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      lsWhereClause = " AND " + lsWhereClause ;
   }

   loSearchRequest.add( lsWhereClause );

   /* Add SearchRequest into the main query */
   foQuery.addFilter( loSearchRequest ) ;
}
//END_EVENT_T2IN_BF_ITEM_beforeQuery}}
//{{EVENT_T3IN_PAGES_BeforePickQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those workspaces which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 08/07/02
 *                                        - inital version
 *
 * @param foPick          Pick object which is doing the query
 * @param foDataSource    The data control which started the pick
 * @param foWhereClause   Where clause for the pick query
 * @param foOrderBy       The order by clause for pick query
 * @param foCancel        param for cancelling the query
 */
void T3IN_PAGES_BeforePickQuery( Pick foPick, DataSource foDataSource,
      VSOutParam foWhereClause, VSOutParam foOrderBy, VSOutParam foCancel )
{
   String            lsOldWhere = null ;
   String            lsApplWhere = null ;
   int               liApplID ;
   StringBuffer      lsbSQL = new StringBuffer( 100 ) ;
   VSResultSet       loResultSet = null ;
   VSQuery           loQuery ;
   VSRow             loAppRow ;
   VSSession         loSession = parentApp.getSession() ;
   VSRow             loRow = getRootDataSource().getCurrentRow() ;
   String            lsBFID = loRow.getData("BF_UNID").getString().trim() ;

   if ( ( lsBFID == null ) || ( lsBFID.equals("") ) )
   {
      return ;
   } /* if ( ( lsBFID == null ) || ( lsBFID.equals("") ) ) */
   else
   {
      lsbSQL.append( "WKSP_UNID IN ( SELECT WKSP_UNID FROM IN_BA, IN_BF WHERE " ) ;
      lsbSQL.append( "IN_BA.BA_UNID = IN_BF.BA_UNID AND IN_BF.BF_UNID = " ) ;
      lsbSQL.append( lsBFID ) ;
      lsbSQL.append( " ) " ) ;

      loQuery = new VSQuery( loSession, "IN_WKSP", lsbSQL.toString(), "" ) ;
      loResultSet = loQuery.execute() ;
      loAppRow = loResultSet.first() ;

      if ( loAppRow != null )
      {
         liApplID = loAppRow.getData("APPL_ID").getInt() ;

         if ( AMSSecurity.isApplicationFlagSet( liApplID, AMSSecurity.STND_ALONE_FL_ATTR, null ) )
         {
            lsApplWhere = "(APPL_ID = " + liApplID + ")" ;
         } /* end if ( loSecObj.isStandAloneLogin() ) */
         else
         {
            lsApplWhere = "(APPL_ID IN (" + AMSSecurityObject.COMMON_APPL_ID +"," + liApplID + "))" ;
         } /* end else */

         /* Check if there is already an existing where clause */
         lsOldWhere = foWhereClause.stringValue() ;
         if ( lsOldWhere != null && lsOldWhere.trim().length() > 0 )
         {
            lsApplWhere = lsOldWhere + " AND " + lsApplWhere ;
         } /* end if ( lsOldWhere != null && lsOldWhere.trim().length() > 0 ) */

         foWhereClause.setValue( lsApplWhere ) ;
      } /* end if ( loAppRow != null ) */

      if ( loResultSet != null )
      {
         loResultSet.close() ;
      } /* end if ( loResultSet != null ) */
   } /* end else */
}
//END_EVENT_T3IN_PAGES_BeforePickQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T3IN_PAGES.addPickListener(this);
	T2IN_BF_ITEM.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T3IN_PAGES) {
			T3IN_PAGES_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T2IN_BF_ITEM) {
			T2IN_BF_ITEM_beforeQuery(query , resultset );
		}
	}
	public void AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow){
		Object source = obj;
		if (source == T3IN_PAGES) {
			T3IN_PAGES_AfterPickFill(obj, dataSource, supplierRow);
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


   public String generate()
   {
      if ( mboolPickCancel )
      {
         VSRow loDeleteRow ;

         mboolPickCancel = false ;
         loDeleteRow = T2IN_BF_ITEM.getCurrentRow() ;
         if ( loDeleteRow != null )
         {
            T2IN_BF_ITEM.delete() ;
         } /* end if ( loDeleteRow != null ) */
      } /* end if ( mboolPickCancel ) */
      return super.generate() ;
   } /* end generate() */

   /**
    * This method is called whenever the user chooses
    * to copy an item in the page.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return Object The copied item
    */
   public Object copyItem( PLSRequest foPLSReq )
   {
      VSRow                   loCopyRow ;
      AMSBusinessFunctionItem loBFItem  = null ;

      loCopyRow = T2IN_BF_ITEM.getCurrentRow() ;

      if ( loCopyRow == null )
      {
         return null ;
      } /* end if ( loCopyRow == null ) */

      loBFItem = new AMSBusinessFunctionItem( loCopyRow ) ;

      return loBFItem.clone() ;
   } /* end copyItem() */

   /**
    * This method is called to retrieve the type of the object
    * retrieved during the last call to copyItem().
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *
    * @return int The last copied item type
    */
   public int getLastCopiedType()
   {
      return ConfigMaster.BUS_FUNC_PG_TYPE ;
   } /* end getLastCopiedType() */

   /**
    * This method is called whenever the user executes
    * a paste action.  Ths object stored in the configurator
    * master page is passed along with its type.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *                    Mark Shipley - 12/12/00
    *                                 - Fixed defects and updated for data model changes
    *
    * @param foPLSReq The PLS Request
    * @param foPasteObject The object to paste
    * @param fiObjectType The type of the object to paste
    * @return boolean The success or failure of the paste action
    */
   public boolean pasteItem( PLSRequest foPLSReq, Object foPasteObject,
                             int fiObjectType )
   {
      try
      {
         switch ( fiObjectType )
         {
            case ConfigMaster.BUS_FUNC_PG_TYPE :
            {
               AMSBusinessFunctionItem loBFItem    = (AMSBusinessFunctionItem)foPasteObject ;
               VSResultSet             loResultSet = T2IN_BF_ITEM.getResultSet() ;
               VSRow                   loLastRow   = loResultSet.last() ;
               VSRow                   loNewRow ;
               int                     liSeqNo     = -1 ;

               if ( loLastRow != null )
               {
                 liSeqNo = AMSBusinessFunctionItem.getSeqNo( loLastRow ) ;
               } /* end if ( loLastRow != null ) */

               loNewRow = loResultSet.insert() ;

               AMSBusinessFunctionItem.setBFID(   loNewRow, loBFItem.getBFID() ) ;
               AMSBusinessFunctionItem.setName(   loNewRow, loBFItem.getName() ) ;
               AMSBusinessFunctionItem.setPageCode(   loNewRow, loBFItem.getPageCode() ) ;
               AMSBusinessFunctionItem.setTmplID( loNewRow, loBFItem.getTmplID() ) ;
               AMSBusinessFunctionItem.setSeqNo(  loNewRow, liSeqNo + 1 );

               T2IN_BF_ITEM.updateDataSource() ;
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
    * "New" action button on the page.  What is actually
    * created is up to the implementing page.
    *
    * Modification Log : Prasanna Ramakrishnan   - 07/26/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the new item action
    */
   public VSPage newItem( PLSRequest foPLSReq )
   {
      VSRow          loNewRow ;
      VSRow          loLastRow ;
      VSRow          loBfRow ;
      VSResultSet    loResultSet = T2IN_BF_ITEM.getResultSet() ;
      int            liSeqNo = 0 ;

      loLastRow = loResultSet.last() ;
      loNewRow  = loResultSet.insert() ;
      loBfRow   = T1IN_BF.getCurrentRow() ;

      if ( loLastRow != null )
      {
         liSeqNo = AMSBusinessFunctionItem.getSeqNo( loLastRow ) + 1 ;
      } /* end if ( loLastRow != null ) */

      AMSBusinessFunctionItem.setName(  loNewRow, "New Page" ) ;
      AMSBusinessFunctionItem.setBFID(  loNewRow, AMSBusinessFunction.getID( loBfRow ) ) ;
      AMSBusinessFunctionItem.setSeqNo( loNewRow, liSeqNo ) ;
      mboolPickCancel = true ;

      return ( T3IN_PAGES.showPickList( null ) ) ;
   } /* end newItem() */

   /**
    * This method is called whenever the user clicks the
    * "Delete" action button on the page.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the delete action
    */
   public boolean deleteItem( PLSRequest foPLSReq )
   {
      VSRow                   loDeleteRow ;

      loDeleteRow = T2IN_BF_ITEM.getCurrentRow() ;

      if ( loDeleteRow == null )
      {
         return false ;
      } /* end if ( loDeleteRow == null ) */

      T2IN_BF_ITEM.delete() ;

      return true ;
   } /* end deleteItem() */


   /**
    * This method will be called whenever the user wants
    * to move an item to the top of the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T2IN_BF_ITEM.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loFirstRow  = loResultSet.first() ;

      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
      {
        return false ;
      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

      AMSBusinessFunctionItem.setSeqNo( loCurrRow, AMSBusinessFunctionItem.getSeqNo( loFirstRow ) - 1 ) ;

      T2IN_BF_ITEM.updateDataSource() ;
      T2IN_BF_ITEM.executeQuery() ;

      return true ;
   } /* end moveItemToTop() */


   /**
    * This method will be called whenever the user wants
    * to move an item to the bottom of the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *                    Mark Shipley - 12/12/00
    *                                 - Fixed defects and updated for data model changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T2IN_BF_ITEM.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loLastRow   = loResultSet.last() ;

      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

      AMSBusinessFunctionItem.setSeqNo( loCurrRow, AMSBusinessFunctionItem.getSeqNo( loLastRow ) + 1 ) ;

      T2IN_BF_ITEM.updateDataSource() ;
      T2IN_BF_ITEM.executeQuery() ;
      T2IN_BF_ITEM.getResultSet().last() ;

      return true ;
   } /* end moveItemToBottom() */


   /**
    * This method will be called whenever the user wants
    * to move an item up one spot in the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemUp()
   {
      VSResultSet loResultSet = T2IN_BF_ITEM.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loPrevRow   = loResultSet.previous() ;
      VSRow       loLastRow   = loResultSet.last() ;
      int         liCurrSeq ;
      int         liPrevSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) )
      {
         T2IN_BF_ITEM.first() ;
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSBusinessFunctionItem.getSeqNo( loCurrRow ) ;
      liPrevSeq = AMSBusinessFunctionItem.getSeqNo( loPrevRow ) ;
      liLastSeq = AMSBusinessFunctionItem.getSeqNo( loLastRow ) ;

      AMSBusinessFunctionItem.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T2IN_BF_ITEM.updateDataSource() ;
      AMSBusinessFunctionItem.setSeqNo( loPrevRow, liCurrSeq ) ;
      T2IN_BF_ITEM.updateDataSource() ;
      AMSBusinessFunctionItem.setSeqNo( loCurrRow, liPrevSeq ) ;
      T2IN_BF_ITEM.updateDataSource() ;

      T2IN_BF_ITEM.executeQuery() ;
      T2IN_BF_ITEM.getResultSet().getRowAt( liCurrRow - 1 ) ;

      return true ;
   } /* End moveItemUp() */

   /**
    * This method will be called whenever the user wants
    * to move an item down one spot in the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/04/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T2IN_BF_ITEM.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
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

      liCurrSeq = AMSBusinessFunctionItem.getSeqNo( loCurrRow ) ;
      liNextSeq = AMSBusinessFunctionItem.getSeqNo( loNextRow ) ;
      liLastSeq = AMSBusinessFunctionItem.getSeqNo( loLastRow ) ;

      AMSBusinessFunctionItem.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T2IN_BF_ITEM.updateDataSource() ;
      AMSBusinessFunctionItem.setSeqNo( loNextRow, liCurrSeq ) ;
      T2IN_BF_ITEM.updateDataSource() ;
      AMSBusinessFunctionItem.setSeqNo( loCurrRow, liNextSeq ) ;
      T2IN_BF_ITEM.updateDataSource() ;

      T2IN_BF_ITEM.executeQuery() ;
      T2IN_BF_ITEM.getResultSet().getRowAt( liCurrRow + 1 ) ;

      return true ;
   } /* End moveItemDown() */

}