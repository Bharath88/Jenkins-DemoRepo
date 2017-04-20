//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.client.dbitem.AMSBusinessArea ;
import com.amsinc.gems.adv.client.dbitem.AMSBusinessFunction ;
import com.amsinc.gems.adv.vfc.html.*;

/*
**  ConfigBAPropertyPage*/

//{{FORM_CLASS_DECL
public class ConfigBAPropertyPage extends ConfigBAPropertyPageBase

//END_FORM_CLASS_DECL}}
implements AMSReorderingPage, AMSConfigParentPage
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigBAPropertyPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

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
    * to copy an item in the page.
    *
    * Modification Log : Prasanna Ramakrishnan - 07/03/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return Object The copied item
    */
   public Object copyItem( PLSRequest foPLSReq )
   {
      VSRow               loCopyRow ;
      AMSBusinessFunction loBF  = null ;

      loCopyRow = T2IN_BF.getCurrentRow() ;

      if ( loCopyRow == null )
      {
         return null ;
      } /* end if ( loCopyRow == null ) */

      loBF = new AMSBusinessFunction( loCopyRow ) ;

      return loBF ;
   } /* end copyItem() */

   /**
    * This method is called to retrieve the type of the object
    * retrieved during the last call to copyItem().
    *
    * Modification Log : Prasanna Ramakrishnan - 08/03/00
    *                                 - inital version
    *
    * @return int The last copied item type
    */
   public int getLastCopiedType()
   {
      return ConfigMaster.BUS_FUNC_TYPE ;
   } /* end getLastCopiedType() */

   /**
    * This method is called whenever the user executes
    * a paste action.  Ths object stored in the configurator
    * master page is passed along with its type.
    *
    * Modification Log : Prasanna Ramakrishnan - 07/03/00
    *                                 - inital version
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
            case ConfigMaster.BUS_FUNC_TYPE :
            {
               AMSBusinessFunction     loBF        = (AMSBusinessFunction)foPasteObject ;
               VSResultSet             loResultSet = T2IN_BF.getResultSet() ;
               VSRow                   loLastRow   = loResultSet.last() ;
               VSRow                   loNewRow ;
               int                     liSeqNo     = -1 ;

               if ( loLastRow != null )
               {
                 liSeqNo = AMSBusinessFunction.getSeqNo( loLastRow ) ;
               } /* end if ( loLastRow != null ) */

               loNewRow = loResultSet.insert() ;
               AMSBusinessFunction.setBAID(   loNewRow, loBF.getBAID() ) ;
               AMSBusinessFunction.setName(   loNewRow, loBF.getName() ) ;
               AMSBusinessFunction.setDesc(   loNewRow, loBF.getDesc() ) ;
               AMSBusinessFunction.setSeqNo(  loNewRow, liSeqNo + 1 ) ;
               AMSBusinessFunction.setTmplID( loNewRow, loBF.getID() ) ;
               T2IN_BF.updateDataSource() ;
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
      long         llBAID ;
      VSResultSet  loResultSet ;
      VSRow        loBARow ;
      VSRow        loLastRow ;
      VSRow        loNewRow ;
      int          liSeqNo = 0 ;

      loResultSet = T1IN_BA.getResultSet() ;
      loBARow     = loResultSet.last() ;
      llBAID      = AMSBusinessArea.getID( loBARow ) ;

      loResultSet = T2IN_BF.getResultSet() ;
      loLastRow = loResultSet.last() ;
      loNewRow  = loResultSet.insert() ;
      if ( loLastRow != null )
      {
         liSeqNo = AMSBusinessFunction.getSeqNo( loLastRow ) + 1;
      } /* end if ( loLastRow != null ) */

      AMSBusinessFunction.setBAID(   loNewRow, llBAID ) ;
      AMSBusinessFunction.setName(   loNewRow, "New Business Function" ) ;
      AMSBusinessFunction.setSeqNo(  loNewRow, liSeqNo ) ;

      T2IN_BF.updateDataSource() ;
      return this ;
   } /* End newItem() */

   /**
    * This method is called whenever the user clicks the
    * "Delete" action button on the page.
    *
    * Modification Log : Mark Shipley - 05/25/00
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the delete action
    */
   public boolean deleteItem( PLSRequest foPLSReq )
   {
      VSRow loDeleteRow ;

      loDeleteRow = T2IN_BF.getCurrentRow() ;

      if ( loDeleteRow == null )
      {
         return false ;
      } /* end if ( loDeleteRow == null ) */

      T2IN_BF.delete() ;

      return true ;
   } /* end deleteItem() */


   /**
    * This method will be called whenever the user wants
    * to move an item to the top of the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/03/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T2IN_BF.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loFirstRow  = loResultSet.first() ;

      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

      AMSBusinessFunction.setSeqNo( loCurrRow, AMSBusinessFunction.getSeqNo( loFirstRow ) - 1 ) ;

      T2IN_BF.updateDataSource() ;
      T2IN_BF.executeQuery() ;

      return true ;
    } /* end moveItemToTop() */


   /**
    * This method will be called whenever the user wants
    * to move an item to the bottom of the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/03/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T2IN_BF.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loLastRow   = loResultSet.last() ;

      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

      AMSBusinessFunction.setSeqNo( loCurrRow, AMSBusinessFunction.getSeqNo( loLastRow ) + 1 ) ;

      T2IN_BF.updateDataSource() ;
      T2IN_BF.executeQuery() ;
      T2IN_BF.getResultSet().last() ;

      return true ;
   } /* end moveItemToBottom() */


   /**
    * This method will be called whenever the user wants
    * to move an item up one spot in the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/03/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemUp()
   {
   VSResultSet loResultSet = T2IN_BF.getResultSet() ;
      int         liCurrRow   = loResultSet.cursorPosition() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loPrevRow   = loResultSet.previous() ;
      VSRow       loLastRow   = loResultSet.last() ;
      int         liCurrSeq ;
      int         liPrevSeq ;
      int         liLastSeq ;

      if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

      liCurrSeq = AMSBusinessFunction.getSeqNo( loCurrRow ) ;
      liPrevSeq = AMSBusinessFunction.getSeqNo( loPrevRow ) ;
      liLastSeq = AMSBusinessFunction.getSeqNo( loLastRow ) ;

      AMSBusinessFunction.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T2IN_BF.updateDataSource() ;
      AMSBusinessFunction.setSeqNo( loPrevRow, liCurrSeq ) ;
      T2IN_BF.updateDataSource() ;
      AMSBusinessFunction.setSeqNo( loCurrRow, liPrevSeq ) ;
      T2IN_BF.updateDataSource() ;

      T2IN_BF.executeQuery() ;
      T2IN_BF.getResultSet().getRowAt( liCurrRow - 1 ) ;

      return true ;
   } /* End moveItemUp() */

  /**
    * This method will be called whenever the user wants
    * to move an item down one spot in the list.
    *
    * Modification Log : Prasanna Ramakrishnan - 08/03/00
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T2IN_BF.getResultSet() ;
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

      liCurrSeq = AMSBusinessFunction.getSeqNo( loCurrRow ) ;
      liNextSeq = AMSBusinessFunction.getSeqNo( loNextRow ) ;
      liLastSeq = AMSBusinessFunction.getSeqNo( loLastRow ) ;

      AMSBusinessFunction.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T2IN_BF.updateDataSource() ;
      AMSBusinessFunction.setSeqNo( loNextRow, liCurrSeq ) ;
      T2IN_BF.updateDataSource() ;
      AMSBusinessFunction.setSeqNo( loCurrRow, liNextSeq ) ;
      T2IN_BF.updateDataSource() ;

      T2IN_BF.executeQuery() ;
      T2IN_BF.getResultSet().getRowAt( liCurrRow + 1 ) ;

      return true ;
   } /* End moveItemDown() */


}