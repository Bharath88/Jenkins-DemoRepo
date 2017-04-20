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
import java.rmi.RemoteException;

/*
**  pIN_HMPG_WIN_Grid*/

//{{FORM_CLASS_DECL
public class pIN_HMPG_WIN_Grid extends pIN_HMPG_WIN_GridBase

//END_FORM_CLASS_DECL}}
implements AMSConfigParentPage
{
   private boolean mboolPickCancel = false ;

   // Declarations for instance variables used in the form

   public static final String ATTR_USER_ID = "USER_ID";

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pIN_HMPG_WIN_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T1IN_HMPG_WIN_beforeQuery
void T1IN_HMPG_WIN_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   // Get only records with USER_ID as "NULL"
   SearchRequest loSrchReq = new SearchRequest();
   loSrchReq.addParameter("IN_HMPG_WIN", ATTR_USER_ID, null );

   // Add the search request
   query.addFilter(loSrchReq);
}
//END_EVENT_T1IN_HMPG_WIN_beforeQuery}}
//{{EVENT_T2IN_PAGES_AfterPickFill
void T2IN_PAGES_AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow)
{
   // Update the datasource
   mboolPickCancel = false ;
   T1IN_HMPG_WIN.updateDataSource() ;
}
//END_EVENT_T2IN_PAGES_AfterPickFill}}
//{{EVENT_pIN_HMPG_WIN_Grid_beforeActionPerformed
void pIN_HMPG_WIN_Grid_beforeActionPerformed( ActionElement foActElem,
           PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if ( foActElem.getName().equals( "T1IN_HMPG_WINAddExtPage" ) )
   {
      String lsServletURL = this.getServletURL() ;
      String lsURL = foPLSReq.getParameter( "txtT1HTTPField" ) ;

      if ( ( lsURL.trim().equals( lsServletURL ) ) )
      {
         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;

         raiseException( "CGI ADVANTAGE Login URL cannot be added as a Homepage.",
                         SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end if ( ( lsURL.trim().equals( lsServletURL ) ) ) */

   } /* end if ( foActElem.getName().equals( "T1IN_HMPG_WINAddExtPage" ) ) */
}
//END_EVENT_pIN_HMPG_WIN_Grid_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_HMPG_WIN.addDBListener(this);
	T2IN_PAGES.addPickListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIN_HMPG_WIN_Grid_beforeActionPerformed( ae, evt, preq );
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
               int         liSeqNo     = 0 ;
               VSResultSet loResultSet ;
               VSRow       loLastRow ;
               VSRow       loNewRow ;

               loResultSet = T1IN_HMPG_WIN.getResultSet() ;
               loLastRow   = loResultSet.last() ;
               if ( loLastRow != null )
               {
                  liSeqNo = AMSHomePageMethods.getDefaultSequenceNumber( parentApp.getSession(), true ) + 1 ;
               } /* end if ( loLastRow != null ) */
               loNewRow = loResultSet.insert() ;

               AMSHomePageMethods.setUserID(     loNewRow, null ) ;
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
               AMSHomePageMethods.setPageCode(   loNewRow,
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
    * Modification Log : Mark Shipley - 12/14/2000
    *                                 - inital version
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

      if ( foPLSReq.getParameter( "T1IN_HMPG_WINAddExtPage" ) != null )
      {
         String            lsURL       = foPLSReq.getParameter("txtT1HTTPField") ;
         VSResultSet       loResultSet = T1IN_HMPG_WIN.getResultSet() ;
         int               liSeqNo     = 0 ;
         VSRow             loLastRow ;
         VSRow             loNewRow ;

         loLastRow = loResultSet.last() ;
         if ( loLastRow != null )
         {
            liSeqNo = AMSHomePageMethods.getDefaultSequenceNumber( loSession, true ) + 1 ;
         } /* end if ( loLastRow != null ) */
         loNewRow = loResultSet.insert() ;

         AMSHomePageMethods.setUserID(     loNewRow, null ) ;
         AMSHomePageMethods.setSeqNo(      loNewRow, liSeqNo ) ;
         AMSHomePageMethods.setName(       loNewRow, lsURL ) ;
         AMSHomePageMethods.setURL(        loNewRow, lsURL ) ;

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
         VSResultSet    loResultSet = T1IN_HMPG_WIN.getResultSet() ;
         int            liSeqNo     = 0 ;
         VSRow          loNewRow ;
         VSRow          loLastRow ;
         VSRow          loBfRow ;

         loLastRow = loResultSet.last() ;
         loNewRow  = loResultSet.insert() ;

         if ( loLastRow != null )
         {
         liSeqNo = AMSHomePageMethods.getDefaultSequenceNumber( loSession, true ) + 1 ;
         } /* end if ( loLastRow != null ) */

         AMSHomePageMethods.setUserID(     loNewRow, null ) ;
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

}