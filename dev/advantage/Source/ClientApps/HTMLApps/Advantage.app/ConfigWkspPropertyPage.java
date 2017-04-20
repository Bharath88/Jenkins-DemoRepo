//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.client.dbitem.AMSWorkspace ;
import com.amsinc.gems.adv.client.dbitem.AMSBusinessArea ;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;


import org.apache.commons.logging.*;

/*
**  ConfigWkspPropertyPage
*/

//{{FORM_CLASS_DECL
public class ConfigWkspPropertyPage extends ConfigWkspPropertyPageBase

//END_FORM_CLASS_DECL}}
implements AMSReorderingPage, AMSConfigParentPage
{
   private static Log moAMSLog = AMSLogger.getLog(ConfigWkspPropertyPage.class,
         AMSLogConstants.FUNC_AREA_DFLT);

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ConfigWkspPropertyPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T2IN_BA_afterCommit
void T2IN_BA_afterCommit(VSResultSet rs)
{
   //Write Event Code below this line
}
//END_EVENT_T2IN_BA_afterCommit}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T2IN_BA.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterCommit( DataSource obj ,VSResultSet rs){
		Object source = obj;
		if (source == T2IN_BA) {
			T2IN_BA_afterCommit(rs);
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
    * This method will be called whenever the user wants
    * to move an item to the top of the list.
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToTop()
   {
      VSResultSet loResultSet = T2IN_BA.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loFirstRow  = loResultSet.first() ;

      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

      AMSBusinessArea.setSeqNo( loCurrRow, AMSBusinessArea.getSeqNo( loFirstRow ) - 1 ) ;

      T2IN_BA.updateDataSource() ;
      T2IN_BA.executeQuery() ;

      return true ;
   } /* end moveItemToTop() */

   /**
    * This method will be called whenever the user wants
    * to move an item to the bottom of the list.
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemToBottom()
   {
      VSResultSet loResultSet = T2IN_BA.getResultSet() ;
      VSRow       loCurrRow   = loResultSet.current() ;
      VSRow       loLastRow   = loResultSet.last() ;

      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
      {
         return false ;
      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

      AMSBusinessArea.setSeqNo( loCurrRow, AMSBusinessArea.getSeqNo( loLastRow ) + 1 ) ;

      T2IN_BA.updateDataSource() ;
      T2IN_BA.executeQuery() ;
      T2IN_BA.getResultSet().last() ;

      return true ;
   } /* end moveItemToBottom() */
      /**
    * This method will be called whenever the user wants
    * to move an item up one spot in the list.
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemUp()
   {
   VSResultSet loResultSet = T2IN_BA.getResultSet() ;
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

      liCurrSeq = AMSBusinessArea.getSeqNo( loCurrRow ) ;
      liPrevSeq = AMSBusinessArea.getSeqNo( loPrevRow ) ;
      liLastSeq = AMSBusinessArea.getSeqNo( loLastRow ) ;

      AMSBusinessArea.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T2IN_BA.updateDataSource() ;
      AMSBusinessArea.setSeqNo( loPrevRow, liCurrSeq ) ;
      T2IN_BA.updateDataSource() ;
      AMSBusinessArea.setSeqNo( loCurrRow, liPrevSeq ) ;
      T2IN_BA.updateDataSource() ;

      T2IN_BA.executeQuery() ;
      T2IN_BA.getResultSet().getRowAt( liCurrRow - 1 ) ;

      return true ;
   } /* end moveItemUp() */

  /**
    * This method will be called whenever the user wants
    * to move an item down one spot in the list.
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @return boolean The success or failure of the action
    */
   public boolean moveItemDown()
   {
      VSResultSet loResultSet = T2IN_BA.getResultSet() ;
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

      liCurrSeq = AMSBusinessArea.getSeqNo( loCurrRow ) ;
      liNextSeq = AMSBusinessArea.getSeqNo( loNextRow ) ;
      liLastSeq = AMSBusinessArea.getSeqNo( loLastRow ) ;

      AMSBusinessArea.setSeqNo( loCurrRow, liLastSeq + 1 ) ;
      T2IN_BA.updateDataSource() ;
      AMSBusinessArea.setSeqNo( loNextRow, liCurrSeq ) ;
      T2IN_BA.updateDataSource() ;
      AMSBusinessArea.setSeqNo( loCurrRow, liNextSeq ) ;
      T2IN_BA.updateDataSource() ;

      T2IN_BA.executeQuery() ;
      T2IN_BA.getResultSet().getRowAt( liCurrRow + 1 ) ;

      return true ;
   } /* End moveItemDown() */

  /**
    * This method is called whenever the user chooses
    * to copy an item in the page.
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return Object The copied item
    */
   public Object copyItem( PLSRequest foPLSReq )
   {
      VSRow           loCopyRow ;
      AMSBusinessArea loBA  = null ;

      loCopyRow = T2IN_BA.getCurrentRow() ;

      if ( loCopyRow == null )
      {
         return null ;
      } /* end if ( loCopyRow == null ) */

      loBA = new AMSBusinessArea( loCopyRow ) ;

      return loBA ;
   } /* end copyItem() */

   /**
    * This method is called to retrieve the type of the object
    * retrieved during the last call to copyItem().
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @return int The last copied item type
    */
   public int getLastCopiedType()
   {
      return ConfigMaster.BUS_AREA_TYPE ;
   } /* end getLastCopiedType() */


  /**
    * This method is called whenever the user executes
    * a paste action.  Ths object stored in the configurator
    * master page is passed along with its type.
    *
    * Modification Log : Mark Shipley - 12/12/2000
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
            case ConfigMaster.BUS_AREA_TYPE :
            {
               AMSBusinessArea loBA        = (AMSBusinessArea)foPasteObject ;
               VSResultSet     loResultSet = T2IN_BA.getResultSet() ;
               VSRow           loLastRow   = loResultSet.last() ;
               VSRow           loNewRow ;
               int             liSeqNo     = -1 ;

               if ( loLastRow != null )
               {
                 liSeqNo = AMSBusinessArea.getSeqNo( loLastRow ) ;
               } /* end if ( loLastRow != null ) */

               loNewRow = loResultSet.insert() ;
               AMSBusinessArea.setWkspID( loNewRow, loBA.getWkspID() ) ;
               AMSBusinessArea.setName(   loNewRow, loBA.getName() ) ;
               AMSBusinessArea.setDesc(   loNewRow, loBA.getDesc() ) ;
               AMSBusinessArea.setSeqNo(  loNewRow, liSeqNo + 1 ) ;
               AMSBusinessArea.setTmplID( loNewRow, loBA.getID() ) ;
               T2IN_BA.updateDataSource() ;
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
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the new item action
    */
   public VSPage newItem( PLSRequest foPLSReq )
   {
      long         llWkspID ;
      VSResultSet  loResultSet ;
      VSRow        loWkspRow ;
      VSRow        loLastRow ;
      VSRow        loNewRow ;
      int          liSeqNo = 0 ;

      loResultSet = T1IN_WKSP.getResultSet() ;
      loWkspRow   = loResultSet.last() ;
      llWkspID    = AMSWorkspace.getID( loWkspRow ) ;

      loResultSet = T2IN_BA.getResultSet() ;
      loLastRow = loResultSet.last() ;
      loNewRow  = loResultSet.insert() ;
      if ( loLastRow != null )
      {
         liSeqNo = AMSBusinessArea.getSeqNo( loLastRow ) + 1;
      } /* end if ( loLastRow != null ) */

      AMSBusinessArea.setWkspID( loNewRow, llWkspID ) ;
      AMSBusinessArea.setName(   loNewRow, "New Business Area" ) ;
      AMSBusinessArea.setSeqNo(  loNewRow, liSeqNo ) ;
         T2IN_BA.updateDataSource() ;
      return this ;
   } /* end newItem() */

   /**
    * This method is called whenever the user clicks the
    * "Delete" action button on the page.
    *
    * Modification Log : Mark Shipley - 12/12/2000
    *                                 - inital version
    *
    * @param foPLSReq The PLS Request
    * @return boolean The success or failure of the delete action
    */
   public boolean deleteItem( PLSRequest foPLSReq )
   {
      VSRow loDeleteRow ;

      loDeleteRow = T2IN_BA.getCurrentRow() ;

      if ( loDeleteRow == null )
      {
         return false ;
      } /* end if ( loDeleteRow == null ) */

      T2IN_BA.delete() ;

      return true ;
   } /* end deleteItem() */

   public String generate()
   {
//      ActionElement      loAddAction    = (ActionElement)getElementByName( "AddAttachment" ) ;
//      TextContentElement loTCEReturn    = (TextContentElement)getElementByName( "ReturnLinkTitle" ) ;
      ScalarElement  loApplIDElem   = getScalarElement( "txtT1APPL_ID" ) ;

      if ( loApplIDElem != null )
      {
         loApplIDElem.setEnabled( false ) ;
      }

      return super.generate() ;
   } /* end generate() */

   /**
    * This method is called before the generate method. It
    * calls fillApplComboBox() to set the combo box values.
    *
    * Modification Log : Kiran Hiranandani - 08/12/02
    *                                      - initial version
    *
    * @param foQuery         Reference to the query statement
    * @param foResultSet     Result Set
    */
   public void beforeGenerate()
   {
      VSQuery            loBAQuery = null ;
      VSResultSet        loBAResultSet = null ;
      int                liBALen = 0 ;
      VSSession          loSession = parentApp.getSession() ;
      DataSource         loCondDataSource = getRootDataSource() ;
      VSRow              loCondRow = loCondDataSource.getCurrentRow() ;
      long               llWkspID = loCondRow.getData("WKSP_UNID").getLong();
      AMSComboBoxElement loComboBox =
            (AMSComboBoxElement) getElementByName( "txtT1APPL_ID" ) ;

      if ( llWkspID == 0 )
      {
         return ;
      } /* end if ( llWkspID == 0 ) */
      else
      {
         loBAQuery = new VSQuery(loSession, "IN_BA", "WKSP_UNID = " +
                                 llWkspID, "") ;
         loBAResultSet = loBAQuery.execute();
         if ( loBAResultSet != null )
         {
            loBAResultSet.last();
            liBALen = loBAResultSet.getRowCount();
            loBAResultSet.close();
         } /* end if ( loBAResultSet != null ) */

         if ( liBALen > 0 )
         {
            loComboBox.getHtmlElement().addAttribute("ams_readonly", "true") ;
            loComboBox.setEnabled( false ) ;
         } /* if ( loComboBox != null ) */
         else
         {
            loComboBox.setEnabled( true ) ;
            if ( loComboBox != null )
            {
               loComboBox.setIgnoreSetValue( true ) ;
               loComboBox.removeAllElements() ;

               fillApplComboBox( loSession, loComboBox ) ;
               loComboBox.setIgnoreSetValue( false ) ;
            } /* if (loComboBox != null) */
         } /* end else */
      } /* end outer else */
   } /* end beforeGenerate() */

   /**
    * This method is called to fill the Application combo
    * box with Application Names of applications to which
    * the user has access.
    *
    * Modification Log : Kiran Hiranandani - 08/12/02
    *                                      - initial version
    *
    * @param foQuery         Reference to the query statement
    * @param foResultSet     Result Set
    */
   private void fillApplComboBox( VSSession foSession,
         ComboBoxElement foComboBox )
   {
      VSQuery           loQuery ;
      VSResultSet       loResultSet ;
      VSRow             loRow ;
      VSORBSession      loORBSession = getParentApp().getSession().getORBSession() ;
      AMSSecurityObject loSecObj = null ;
      String            lsApplWhere = null ;

      try
      {
         loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
         lsApplWhere = AMSSecurityObject.getApplicationWhere( loSecObj ) ;

         /* Form VSQuery object to get the Application Names from R_SC_APPL */
         loQuery = new VSQuery( getParentApp().getSession(),
               "R_SC_APPL", lsApplWhere, " APPL_NM " ) ;

         loResultSet = loQuery.execute() ;
         loRow = loResultSet.first() ;

         while ( loRow != null )
         {
            String  lsApplID  = loRow.getData( "APPL_ID" ).getString() ;
            String  lsApplNM  = loRow.getData( "APPL_NM" ).getString() ;

            foComboBox.addElement( lsApplNM, lsApplID ) ;
            loRow = loResultSet.next() ;
         } /* end while( loRow != null ) */

         loResultSet.close() ;
      } /* end try */
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return ;
      }
      catch ( Exception loEx )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loEx);

         raiseException( "error creating list box information",
               SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end try - catch */
   } /* end fillApplComboBox() */

}
