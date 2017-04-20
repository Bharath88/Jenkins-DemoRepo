//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.rmi.RemoteException;
import advantage.AdvIHistTracking;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.*;
import org.apache.commons.logging.Log;


	/*
	**  pUserRoleLink
	*/

	//{{FORM_CLASS_DECL
	public class pUserRoleLink extends pUserRoleLinkBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSAvailableSelectionPage, AMSReorderingPage
	{
	    private static Log moLog = AMSLogger.getLog( pUserRoleLink.class, AMSLogConstants.FUNC_AREA_SECURITY );

	   // Declarations for instance variables used in the form

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pUserRoleLink ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_pUserRoleLink_beforeActionPerformed
void pUserRoleLink_beforeActionPerformed( ActionElement foAction, PageEvent evt, PLSRequest foPLSReq )
{
	//Write Event Code below this line

   /**
    * There is a field named Revoke Reason on this page which is non-persistent. The value of
    * this field needs to be passed onto the VLS side on assignment or revocation.
    * Whenever any assignment or revocation action takes place, the Revoke Reason value is stored
    * in the session.
    */
   if( String.valueOf(AMSHyperlinkActionElement.CONFIG_SELECT_ITEM).equals(foAction.getAction()) ||
         AMSHyperlinkActionElement.AE_ACTION_DELETE.equals(foAction.getAction()) )
   {
      try
      {
         getParentApp().getSession().getORBSession().setProperty(AdvIHistTracking.SESSION_REVOKE_REASON,
               foPLSReq.getParameter(AdvIHistTracking.SESSION_REVOKE_REASON));
      }
      catch (RemoteException leExcp)
      {
          // Add exception log to logger object
          moLog.error("Unexpected error encountered while processing. ", leExcp);

      }
   }
}
//END_EVENT_pUserRoleLink_beforeActionPerformed}}

	//END_EVENT_CODE}}

	   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	   }

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pUserRoleLink_beforeActionPerformed( ae, evt, preq );
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
	    * Modification Log : Mark Shipley - 04/11/2001
	    *                                 - inital version
	    *
	    * @return boolean The success or failure of the action
	    */
	   public boolean moveItemToTop()
	   {
	      VSResultSet loResultSet = T3R_SC_USER_ROLE_LNK.getResultSet() ;
	      VSRow       loCurrRow   = loResultSet.current() ;
	      VSRow       loFirstRow  = loResultSet.first() ;

	      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
	      {
	         return false ;
	      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

	      loCurrRow.getData( "PREC_NO" ).setInt( loFirstRow.getData( "PREC_NO" ).getInt() - 1 ) ;

	      T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      T3R_SC_USER_ROLE_LNK.executeQuery() ;

	      return true ;
	   } /* end moveItemToTop() */

	   /**
	    * This method will be called whenever the user wants
	    * to move an item up one spot in the list.
	    *
	    * Modification Log : Mark Shipley - 04/11/2001
	    *                                 - inital version
	    *
	    * @return boolean The success or failure of the action
	    */
	   public boolean moveItemUp()
	   {
	      VSResultSet loResultSet = T3R_SC_USER_ROLE_LNK.getResultSet() ;
	      VSRow       loCurrRow   = loResultSet.current() ;
	      VSRow       loPrevRow   = loResultSet.previous() ;
	      VSRow       loLastRow   = loResultSet.last() ;
	      int         liCurrSeq ;
	      int         liPrevSeq ;
	      int         liLastSeq ;

	      if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) )
	      {
	         T3R_SC_USER_ROLE_LNK.first() ;
	         return false ;
	      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

	      liCurrSeq = loCurrRow.getData( "PREC_NO" ).getInt() ;
	      liPrevSeq = loPrevRow.getData( "PREC_NO" ).getInt() ;
	      liLastSeq = loLastRow.getData( "PREC_NO" ).getInt() ;

	      loCurrRow.getData( "PREC_NO" ).setInt( liLastSeq + 1 ) ;
	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loPrevRow.getData( "PREC_NO" ).setInt( liCurrSeq ) ;
	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loCurrRow.getData( "PREC_NO" ).setInt( liPrevSeq ) ;
	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */

	      T3R_SC_USER_ROLE_LNK.executeQuery() ;

	      return true ;
	   } /* end moveItemUp() */

	   /**
	    * This method will be called whenever the user wants
	    * to move an item down one spot in the list.
	    *
	    * Modification Log : Mark Shipley - 04/11/2001
	    *                                 - inital version
	    *
	    * @return boolean The success or failure of the action
	    */
	   public boolean moveItemDown()
	   {
	      VSResultSet loResultSet = T3R_SC_USER_ROLE_LNK.getResultSet() ;
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

	      liCurrSeq = loCurrRow.getData( "PREC_NO" ).getInt() ;
	      liNextSeq = loNextRow.getData( "PREC_NO" ).getInt() ;
	      liLastSeq = loLastRow.getData( "PREC_NO" ).getInt() ;

	      loCurrRow.getData( "PREC_NO" ).setInt( liLastSeq + 1 ) ;
	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loNextRow.getData( "PREC_NO" ).setInt( liCurrSeq ) ;
	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loCurrRow.getData( "PREC_NO" ).setInt( liNextSeq ) ;
	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */

	      T3R_SC_USER_ROLE_LNK.executeQuery() ;

	      return true ;
	   } /* end moveItemDown() */

	   /**
	    * This method will be called whenever the user wants
	    * to move an item to the bottom of the list.
	    *
	    * Modification Log : Mark Shipley - 04/11/2001
	    *                                 - inital version
	    *
	    * @return boolean The success or failure of the action
	    */
	   public boolean moveItemToBottom()
	   {
	      VSResultSet loResultSet = T3R_SC_USER_ROLE_LNK.getResultSet() ;
	      VSRow       loCurrRow   = loResultSet.current() ;
	      VSRow       loLastRow   = loResultSet.last() ;

	      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
	      {
	         return false ;
	      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

	      loCurrRow.getData( "PREC_NO" ).setInt( loLastRow.getData( "PREC_NO" ).getInt() + 1 ) ;

	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      T3R_SC_USER_ROLE_LNK.executeQuery() ;

	      return true ;
	   } /* end moveItemToBottom() */

	   /**
	    * This method will be called whenever the user wants
	    * to move an item from the list of available items to
	    * the list of selected items.
	    *
	    * Modification Log : Mark Shipley - 04/11/2001
	    *                                 - inital version
	    *
	    * @param foPLSReq The PLS Request
	    * @return boolean The success or failure of the action
	    */
	   public boolean addToSelected( PLSRequest foPLSReq )
	   {
	       //debugging log
	       if (moLog.isDebugEnabled())
	       {
	           moLog.debug("addToSelected()");
	       }

	      VSResultSet loResultSet = T3R_SC_USER_ROLE_LNK.getResultSet() ;
	      VSRow       loCurrRow   = T4R_SC_SEC_ROLE.getCurrentRow() ;
	      VSRow       loUserRow   = T1R_SC_USER_INFO.getCurrentRow() ;
	      VSRow       loLastRow ;
	      VSRow       loNewRow ;
	      VSQuery     loQuery ;
	      VSResultSet loPrecNoRS ;
	      String      lsUserID ;
	      int         liPrecNo    = 1 ;

	      if ( ( loUserRow == null ) || ( loCurrRow == null ) )
	      {
	         return false ;
	      } /* end if ( ( loUserRow == null ) || ( loCurrRow == null ) ) */

	      lsUserID = loUserRow.getData( "USER_ID" ).getString() ;

	      //debugging log
	      if (moLog.isDebugEnabled())
	      {
	          moLog.debug("\t lsUserID (R_SC_USER_INFO.USER_ID) = " + lsUserID );
	      }

	      // ADVHR00005709 BEGIN
	      // escape apostrophe
	      //loQuery = new VSQuery( getParentApp().getSession(), "R_SC_USER_ROLE_LNK",
	      //                       "USER_ID='" + AMSSQLUtil.getANSIQuotedStr(lsUserID) + "'", "PREC_NO DESC" ) ;
	      loQuery = new VSQuery( getParentApp().getSession(),
	              "R_SC_USER_ROLE_LNK",
	              "USER_ID = " + AMSSQLUtil.getANSIQuotedStr(lsUserID, true),
	              "PREC_NO DESC" );
	      // ADVHR00005709 END


	      loPrecNoRS = loQuery.execute() ;
	      if ( loPrecNoRS.first() != null )
	      {
	         liPrecNo = loPrecNoRS.first().getData( "PREC_NO" ).getInt() + 1 ;
	      } /* end if ( loPrecNoRS.first() != null ) */
	      loPrecNoRS.close() ;

	      loResultSet.last() ;
	      loNewRow = loResultSet.insert() ;
	      loNewRow.getData( "USER_ID" ).setString( loUserRow.getData( "USER_ID" ).getString() ) ;
	      loNewRow.getData( "SEC_ROLE_ID" ).setString( loCurrRow.getData( "SEC_ROLE_ID" ).getString() ) ;
	      loNewRow.getData( "PREC_NO" ).setInt( liPrecNo ) ;

	      try
	      {
	         T3R_SC_USER_ROLE_LNK.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T3R_SC_USER_ROLE_LNK.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */

	      /*
	       * To ensure that the user's roles have been upated in
	       * the cache - the cache is cleared on both the PLS and
	       * the VLS sides, in the event they have different caches
	       */
	      AMSSecurity.removeUserCache( lsUserID ) ;

	      return true ;
	   } /* end addToSelected() */

	   /**
	    * This method will be called whenever the user wants
	    * to move an item from the list of selected items back
	    * to the list of available items.
	    *
	    * This method should never be called because we will
	    * just use the delete action to perform this action.
	    *
	    * Modification Log : Mark Shipley - 04/11/2001
	    *                                 - inital version
	    *
	    * @param foPLSReq The PLS Request
	    * @return boolean The success or failure of the action
	    */
	   public boolean removeFromSelected( PLSRequest foPLSReq )
	   {
	      return false ;
	   } /* end removeFromSelected() */

	}