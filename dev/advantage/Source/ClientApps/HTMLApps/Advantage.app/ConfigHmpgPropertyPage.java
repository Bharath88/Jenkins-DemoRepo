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
import com.amsinc.gems.adv.client.dbitem.AMSHomePageMethods ;
import com.amsinc.gems.adv.common.* ;
import java.util.* ;
import java.rmi.RemoteException;
import versata.vls.ServerEnvironment;
import versata.vls.Session;

/*
**  ConfigHmpgPropertyPage
*/

//{{FORM_CLASS_DECL
public class ConfigHmpgPropertyPage extends ConfigHmpgPropertyPageBase

//END_FORM_CLASS_DECL}}
implements AMSReorderingPage, AMSConfigParentPage
{
   private boolean mboolPickCancel = false ;


   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigHmpgPropertyPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T2IN_PAGES_AfterPickFill
void T2IN_PAGES_AfterPickFill( Pick foPick, DataSource foDataSource, VSRow foSupplierRow )
{
   VSRow loRow = T1IN_HMPG_WIN.getCurrentRow() ;
   mboolPickCancel = false ;
   if ( ( loRow != null ) && ( foSupplierRow != null ) )
   {
      VSData loSupplierData = foSupplierRow.getData( "PAGE_CD" ) ;

      if ( loSupplierData != null )
      {
         AMSHomePageMethods.setName( loRow, loSupplierData.getString() ) ;
      } /* end if ( loSupplierData != null ) */
   } /* end if ( ( loRow != null ) && ( foSupplierRow != null ) ) */
   T1IN_HMPG_WIN.updateDataSource() ;
}
//END_EVENT_T2IN_PAGES_AfterPickFill}}
//{{EVENT_T1IN_HMPG_WIN_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those homepages which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 07/23/02
 *                                        - inital version
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1IN_HMPG_WIN_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
{
   //Write Event Code below this line
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
//END_EVENT_T1IN_HMPG_WIN_beforeQuery}}
//{{EVENT_ConfigHmpgPropertyPage_beforeActionPerformed
void ConfigHmpgPropertyPage_beforeActionPerformed( ActionElement foActElem,
           PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if ( foActElem.getName().equals( "T1IN_HMPG_WINAddExtPage" ) )
   {
      String lsServletURL = this.getServletURL() ;
      String lsURL = foPLSReq.getParameter( "txtT1HTTPField" ) ;

      VSResultSet loResultSet = T1IN_HMPG_WIN.getResultSet() ;
      
      /* If the only existing homepage is an internal page, we don't allow
       * any additional homepages to be set.
       */
      if( loResultSet.getRowCount() == 1 )
      {
         VSRow loRow = loResultSet.first();
         String loWindowName = AMSHomePageMethods.getName( loRow );  
         String loURL = AMSHomePageMethods.getURL( loRow );
         if( loWindowName.equals( AMSPage.INFO_REPORT_PG_TITLE ))
         {
            foPageEvent.setCancel( true ) ;
            foPageEvent.setNewPage( this ) ;
            raiseException( "Additional Homepages cannot be added if an " +
                  "ADVANTAGE Page / infoAdvantage Report is already set as a Homepage.",
                    SEVERITY_LEVEL_ERROR ) ;    
            return;
         }       
         else if( loURL == null || loURL.length() == 0 )
         {
            foPageEvent.setCancel( true ) ;
            foPageEvent.setNewPage( this ) ;
            
            raiseException( "Additional Homepages cannot be added if an " +
                            "ADVANTAGE Page / infoAdvantage Report Page is already set as a Homepage.",
                              SEVERITY_LEVEL_ERROR ) ;
            return ;
         }
      }
          
      if ( ( lsURL.trim().equals( lsServletURL ) ) )
      {
         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;

         raiseException( "CGI ADVANTAGE Login URL cannot be added as a Homepage.",
                         SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end if if ( ( lsURL.trim().equals( lsServletURL ) ) ) */

   } /* end if ( foActElem.getName().equals( "T1IN_HMPG_WINAddExtPage" ) ) */
   
   else if( foActElem.getName().equals( "T1IN_HMPG_WINAddAdvPage" ) 
         || foActElem.getName().equals( "displayReportData" ) )
   {
      VSResultSet loResultSet = T1IN_HMPG_WIN.getResultSet() ;
      
      /* We don't allow an internal homepage to be set unless there are no
       * homepages currently set.
       */
      if( loResultSet.getRowCount() > 0 )
      {
         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;
         
         raiseException( "An ADVANTAGE Page / infoAdvantage Report Page can only be added as a Homepage " +
                         "if there are no other Homepages set.",
                           SEVERITY_LEVEL_ERROR ) ;
         return ;
      }
   }
   
   // for opening the BO Report Lising page
   if(foActElem.getName().equals( "displayReportData" ))
   {      
      acceptData( foPLSReq ) ;
      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( showBOReportListingPage( foPLSReq ) ) ;
      
   }/* end if ( foActnElem.getName().equals( "displayReportData" ) ) */
   
}
//END_EVENT_ConfigHmpgPropertyPage_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T2IN_PAGES.addPickListener(this);
	T1IN_HMPG_WIN.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			ConfigHmpgPropertyPage_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_HMPG_WIN) {
			T1IN_HMPG_WIN_beforeQuery(query , resultset );
		}
	}
	public void AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow){
		Object source = obj;
		if (source == T2IN_PAGES) {
			T2IN_PAGES_AfterPickFill(obj, dataSource, supplierRow);
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
         VSRow loCurrentRow ;

         mboolPickCancel = false ;
         loCurrentRow = T1IN_HMPG_WIN.getCurrentRow() ;
         if ( loCurrentRow != null )
         {
            T1IN_HMPG_WIN.undo() ;
         } /* end if ( loCurrentRow != null ) */
      } /* end if ( mboolPickCancel ) */
      return super.generate() ;
   } /* end generate() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the top of the list.
    *
    * Modification Log : Mark Shipley      - 12/14/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T1IN_HMPG_WIN.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loFirstRow  = loResultSet.first() ;

      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

      AMSHomePageMethods.setSeqNo( loCurrRow,
            AMSHomePageMethods.getSequenceNumber( getParentApp().getSession(), false ) - 1 ) ;

      T1IN_HMPG_WIN.updateDataSource() ;
      T1IN_HMPG_WIN.executeQuery() ;

      return true ;
   } /* end moveItemToTop() */

   /**
    * This method will be called whenever the user wants
    * to move an item up one spot in the list.
    *
    * Modification Log : Mark Shipley      - 12/14/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemUp()
   {
      VSResultSet loResultSet = T1IN_HMPG_WIN.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loPrevRow   = loResultSet.previous() ;
      VSRow       loLastRow   = loResultSet.last() ;
      int         liCurrSeq ;
      int         liPrevSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) )
      {
         T1IN_HMPG_WIN.first() ;
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSHomePageMethods.getSeqNo( loCurrRow ) ;
      liPrevSeq = AMSHomePageMethods.getSeqNo( loPrevRow ) ;
      liLastSeq = AMSHomePageMethods.getSequenceNumber( getParentApp().getSession(), true ) ;

      AMSHomePageMethods.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T1IN_HMPG_WIN.updateDataSource() ;
      AMSHomePageMethods.setSeqNo( loPrevRow, liCurrSeq ) ;
      T1IN_HMPG_WIN.updateDataSource() ;
      AMSHomePageMethods.setSeqNo( loCurrRow, liPrevSeq ) ;
      T1IN_HMPG_WIN.updateDataSource() ;

      T1IN_HMPG_WIN.executeQuery() ;
      T1IN_HMPG_WIN.getResultSet().getRowAt( liCurrRow - 1 ) ;

      return true ;
   } /* end moveItemUp() */

   /**
    * This method will be called whenever the user wants
    * to move an item down one spot in the list.
    *
    * Modification Log : Mark Shipley      - 12/14/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T1IN_HMPG_WIN.getResultSet() ;
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

      liCurrSeq = AMSHomePageMethods.getSeqNo( loCurrRow ) ;
      liNextSeq = AMSHomePageMethods.getSeqNo( loNextRow ) ;
      liLastSeq = AMSHomePageMethods.getSequenceNumber( getParentApp().getSession(), true ) ;

      AMSHomePageMethods.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T1IN_HMPG_WIN.updateDataSource() ;
      AMSHomePageMethods.setSeqNo( loNextRow, liCurrSeq ) ;
      T1IN_HMPG_WIN.updateDataSource() ;
      AMSHomePageMethods.setSeqNo( loCurrRow, liNextSeq ) ;
      T1IN_HMPG_WIN.updateDataSource() ;

      T1IN_HMPG_WIN.executeQuery() ;
      T1IN_HMPG_WIN.getResultSet().getRowAt( liCurrRow + 1 ) ;

      return true ;
   } /* end moveItemDown() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the bottom of the list.
    *
    * Modification Log : Mark Shipley      - 06/19/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T1IN_HMPG_WIN.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loLastRow   = loResultSet.last() ;

      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

      AMSHomePageMethods.setSeqNo( loCurrRow,
            AMSHomePageMethods.getSequenceNumber( getParentApp().getSession(), true ) + 1 ) ;

      T1IN_HMPG_WIN.updateDataSource() ;
      T1IN_HMPG_WIN.executeQuery() ;
      T1IN_HMPG_WIN.getResultSet().last() ;

      return true ;
   } /* end moveItemToBottom() */

   /**
    * This method is called whenever the user chooses
    * to copy an item in the page.
    *
    * Modification Log : Mark Shipley - 12/14/2000
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return Object The copied item
    */
   public Object copyItem( PLSRequest foPLSReq )
   {
      return T1IN_HMPG_WIN.getResultSet().current() ;
   } /* end copyItem() */

   /**
    * This method is called to retrieve the type of the object
    * retrieved during the last call to copyItem().
    *
    * Modification Log : Mark Shipley - 12/14/2000
    *                                 - inital version
    *
    * @return int The last copied item type
    */
   public int getLastCopiedType()
   {
      return ConfigMaster.HMPG_WIN_TYPE ;
   } /* end getLastCopiedType() */

   /**
    * This method is called whenever the user executes
    * a paste action.  Ths object stored in the configurator
    * master page is passed along with its type.
    *
    * Modification Log : Mark Shipley      - 12/14/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
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
      switch ( fiObjectType )
      {
         case ConfigMaster.HMPG_WIN_TYPE :
         {
            VSRow loCopyRow = (VSRow)foPasteObject ;

            if ( loCopyRow != null )
            {
               VSSession   loSession   = getParentApp().getSession() ;
               String      lsUserID    = loSession.getLogin() ;
               int         liSeqNo     = 0 ;
               VSResultSet loResultSet ;
               VSRow       loLastRow ;
               VSRow       loNewRow ;

               loResultSet = T1IN_HMPG_WIN.getResultSet() ;
               loLastRow   = loResultSet.last() ;
               if ( loLastRow != null )
               {
                  liSeqNo = AMSHomePageMethods.getSequenceNumber( loSession, true ) + 1 ;
               } /* end if ( loLastRow != null ) */
               loNewRow = loResultSet.insert() ;

               AMSHomePageMethods.setUserID(     loNewRow, lsUserID ) ;
               AMSHomePageMethods.setSeqNo(      loNewRow, liSeqNo ) ;
               AMSHomePageMethods.setName(       loNewRow,
                     AMSHomePageMethods.getName( loCopyRow ) ) ;
               AMSHomePageMethods.setColor(      loNewRow,
                     AMSHomePageMethods.getColor( loCopyRow ) ) ;
               AMSHomePageMethods.setFontColor(  loNewRow,
                     AMSHomePageMethods.getFontColor( loCopyRow ) ) ;
               AMSHomePageMethods.setX(          loNewRow,
                     AMSHomePageMethods.getX( loCopyRow ) ) ;
               AMSHomePageMethods.setY(          loNewRow,
                     AMSHomePageMethods.getY( loCopyRow ) ) ;
               AMSHomePageMethods.setWidth(      loNewRow,
                     AMSHomePageMethods.getWidth( loCopyRow ) ) ;
               AMSHomePageMethods.setHeight(     loNewRow,
                     AMSHomePageMethods.getHeight( loCopyRow ) ) ;
               AMSHomePageMethods.setURL(        loNewRow,
                     AMSHomePageMethods.getURL( loCopyRow ) ) ;
               AMSHomePageMethods.setPageCode( loNewRow,
                     AMSHomePageMethods.getPageCode( loCopyRow ) ) ;

               T1IN_HMPG_WIN.updateDataSource() ;
               T1IN_HMPG_WIN.last() ;
               return true ;
            } /* end if ( loCopyRow != null ) */
         }
         default :
            break ;
      } /* end switch ( fiObjectType ) */

      return false ;
   } /* end pasteItem() */

   /**
    * This method is called whenever the user clicks the
    * "New" action button on the page.
    *
    * Modification Log : Mark Shipley      - 12/14/2000
    *                                      - inital version
    *                    Kiran Hiranandani - 07/23/02
    *                                      - Portal Integration Changes
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the new item action
    */
   public VSPage newItem( PLSRequest foPLSReq )
   {
      VSSession         loSession = getParentApp().getSession() ;
      VSORBSession      loORBSession = loSession.getORBSession() ;
      AMSSecurityObject loSecObj  = null ;

      try
      {
         loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
      }
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return null ;
      }

      if ( foPLSReq.getParameter( "T1IN_HMPG_WINAddExtPage" ) != null 
            || foPLSReq.getParameter( "ReportPickValue" ) != null )
      {
         String            lsURL = null;         
         VSResultSet       loResultSet = T1IN_HMPG_WIN.getResultSet() ;
         String            lsUserID    = loSession.getLogin() ;
         int               liSeqNo     = 0 ;
         VSRow             loLastRow ;
         VSRow             loNewRow ;
         int               liRowCount;
         
         if (foPLSReq.getParameter( "T1IN_HMPG_WINAddExtPage" ) != null )
         {
            lsURL = foPLSReq.getParameter("txtT1HTTPField") ;
         }
         else if (foPLSReq.getParameter( "ReportPickValue" ) != null )
         {            
            lsURL = foPLSReq.getParameter( "ReportPickValue" );
         }

         loLastRow = loResultSet.last() ;
         if ( loLastRow != null )
         {
            liSeqNo = AMSHomePageMethods.getSequenceNumber( loSession, true ) + 1 ;
         } /* end if ( loLastRow != null ) */
         loNewRow = loResultSet.insert() ;

         AMSHomePageMethods.setUserID(     loNewRow, lsUserID ) ;
         AMSHomePageMethods.setSeqNo(      loNewRow, liSeqNo ) ;
         
         if (foPLSReq.getParameter( "T1IN_HMPG_WINAddExtPage" ) != null )
         {
            AMSHomePageMethods.setName( loNewRow, lsURL ) ;
         }
         else if (foPLSReq.getParameter( "ReportPickValue" ) != null)
         {
            AMSHomePageMethods.setName( loNewRow, AMSPage.INFO_REPORT_PG_TITLE ) ;         
         }
                  
         AMSHomePageMethods.setURL( loNewRow, lsURL ) ;
         
         /*Cascade the windows so none are hidden by others when first loaded */
         liRowCount = loResultSet.getRowCount();
         AMSHomePageMethods.setX(          loNewRow, liRowCount * 20 );
         AMSHomePageMethods.setY(          loNewRow, liRowCount * 20 );
         
         if ( loSecObj.isStandAloneLogin() )
         {
            AMSHomePageMethods.setApplication(   loNewRow,
                  ( loSecObj.getApplicationListArray() )[0].intValue() ) ;
         } /* end if ( loSecObj.isStandAloneLogin() ) */
         else
         {
            AMSHomePageMethods.setApplication( loNewRow, AMSSecurityObject.COMMON_APPL_ID ) ;
         }

         T1IN_HMPG_WIN.updateDataSource() ;
         return this ;
      } /* end if ( foPLSReq.getParameter( "T1IN_HMPG_WINAddExtPage" ) != null ) */
      else if ( foPLSReq.getParameter( "T1IN_HMPG_WINAddAdvPage" ) != null )
      {
         VSResultSet       loResultSet = T1IN_HMPG_WIN.getResultSet() ;
         String            lsUserID    = loSession.getLogin() ;
         int               liSeqNo     = 0 ;
         VSRow             loNewRow ;
         VSRow             loLastRow ;
         VSRow             loBfRow ;
         
         loLastRow = loResultSet.last() ;
         loNewRow  = loResultSet.insert() ;

         if ( loLastRow != null )
         {
            liSeqNo = AMSHomePageMethods.getSequenceNumber( loSession, true ) + 1 ;
         } /* end if ( loLastRow != null ) */

         AMSHomePageMethods.setUserID(     loNewRow, lsUserID ) ;
         AMSHomePageMethods.setSeqNo(      loNewRow, liSeqNo ) ;
         AMSHomePageMethods.setName(       loNewRow, "New Window" ) ;

         mboolPickCancel = true ;

         if ( loSecObj.isStandAloneLogin() )
         {
            AMSHomePageMethods.setApplication(   loNewRow,
                  ( loSecObj.getApplicationListArray() )[0].intValue() ) ;
         } /* end if ( loSecObj.isStandAloneLogin() ) */

         return ( T2IN_PAGES.showPickList( null ) ) ;
      } /* end else if ( foPLSReq.getParameter( "T1IN_HMPG_WINAddAdvPage" ) != null ) */

      return this ;
   } /* end newItem() */

   /**
    * This method is called whenever the user clicks the
    * "Delete" action button on the page.
    *
    * Modification Log : Mark Shipley - 12/14/2000
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the delete action
    */
   public boolean deleteItem( PLSRequest foPLSReq )
   {
      VSRow                   loDeleteRow ;

      loDeleteRow = T1IN_HMPG_WIN.getCurrentRow() ;

      if ( loDeleteRow == null )
      {
         return false ;
      } /* end if ( loDeleteRow == null ) */

      T1IN_HMPG_WIN.delete() ;

      return true ;
   } /* end deleteItem() */
   
   
   /**
    * This method displays the BO Report Listing Page
    *   
    */
   private VSPage showBOReportListingPage( PLSRequest foRequest )
   {
      String               lsSessionID   = getSessionId() ;
      PLSApp               loParentApp   = getParentApp() ;
      VSPage               loTransPage ;
      AMSDynamicTransition loDynTran ;      

      loDynTran = new AMSDynamicTransition( "pBOReportListing_pick", "", "Reports_Sys_Admin_App" ) ;
      loDynTran.setSourcePage( this ) ;
      loTransPage = loDynTran.getVSPage( loParentApp, lsSessionID ) ;    
      loParentApp.getPageExpireAlg().pageNavigatedTo( loTransPage ) ;

      return loTransPage ;
   }
   
   
}