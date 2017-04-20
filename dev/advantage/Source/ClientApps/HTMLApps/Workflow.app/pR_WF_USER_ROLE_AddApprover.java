//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.*;
import advantage.AdvIHistTracking;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
	/*
	**  pR_WF_USER_ROLE_AddApprover*/

	//{{FORM_CLASS_DECL
	public class pR_WF_USER_ROLE_AddApprover extends pR_WF_USER_ROLE_AddApproverBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSAvailableSelectionPage, AMSReorderingPage
	{
	   // Declarations for instance variables used in the form

	      public static final String ATTR_ROLEID       = "ROLEID";
	      public static final String ATTR_ROLE_MGR_FL  = "ROLE_MGR_FL" ;

          private static Log moAMSLog = AMSLogger.getLog( pR_WF_USER_ROLE_AddApprover.class,
               AMSLogConstants.FUNC_AREA_WORKFLOW ) ;
	      
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pR_WF_USER_ROLE_AddApprover ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_pR_WF_USER_ROLE_AddApprover_beforeActionPerformed
/**
 * This method called to process beforeActionPerformed event
 *
 * Modification Log : Kiran Hiranandani - 08/19/2001
 *                                      - inital version
 *
 * @param foActnElem The invoked action element
 * @param foPgEvnt The Page Event object
 * @param foPLSReq The PLS Request
 */
void pR_WF_USER_ROLE_AddApprover_beforeActionPerformed(
      ActionElement foActnElem, PageEvent foPgEvnt, PLSRequest foPLSReq )
{
   String lsAction = foActnElem.getAction();
   if ( lsAction.equals( "manager") )
   {
      VSRow loRow = getRootDataSource().getCurrentRow() ;
      if ( loRow != null )
      {
         boolean lboolRoleMgr = loRow.getData( ATTR_ROLE_MGR_FL ).getBoolean() ;

         if ( lboolRoleMgr )
         {
            loRow.getData( ATTR_ROLE_MGR_FL ).setBoolean( false ) ;
         } /* end if ( loRow != null ) */
         else
         {
            loRow.getData( ATTR_ROLE_MGR_FL ).setBoolean( true ) ;
         } /* end else */

         T1R_WF_USER_ROLE.updateDataSource() ;
      } /* if ( loRow != null ) */
   } /* end if ( foActnElem.getAction().equals( "manager") ) */


   /**
    * There is a field named Revoke Reason on this page which is non-persistent. The value of
    * this field needs to be passed onto the VLS side on assignment or revocation.
    * Whenever any assignment or revocation action takes place, the Revoke Reason value is stored
    * in the session.
    */
   if( String.valueOf(AMSHyperlinkActionElement.CONFIG_SELECT_ITEM).equals(lsAction) ||
         AMSHyperlinkActionElement.AE_ACTION_DELETE.equals(lsAction) )
   {
      try
      {
         getParentApp().getSession().getORBSession().setProperty(AdvIHistTracking.SESSION_REVOKE_REASON,
               foPLSReq.getParameter(AdvIHistTracking.SESSION_REVOKE_REASON));
      }
      catch (RemoteException leExcp)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", leExcp);

      }
   }
}
//END_EVENT_pR_WF_USER_ROLE_AddApprover_beforeActionPerformed}}

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
			pR_WF_USER_ROLE_AddApprover_beforeActionPerformed( ae, evt, preq );
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
	    * Generate method
	    */
	   public String generate()
	   {
	      // Set the title
	      VSRow loRow = getSourcePage().getRootDataSource().getCurrentRow();
	      if (loRow != null)
	      {
	         String lsRoleID = loRow.getData(ATTR_ROLEID).getString();
	         if (lsRoleID != null)
	         {
	            setTitle( "Add Approvers to " + lsRoleID );
	         }
	      }

	      return super.generate();
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
	      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet() ;
	      VSRow       loCurrRow   = loResultSet.current() ;
	      VSRow       loFirstRow  = loResultSet.first() ;

	      if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) )
	      {
	         return false ;
	      } /* end if ( ( loCurrRow == null ) || ( loFirstRow == null ) || ( loFirstRow == loCurrRow ) ) */

	      loCurrRow.getData( "AMS_ROW_VERS_NO" ).setInt( loFirstRow.getData( "AMS_ROW_VERS_NO" ).getInt() - 1 ) ;

	      T1R_WF_USER_ROLE.updateDataSource() ;
	      T1R_WF_USER_ROLE.executeQuery() ;

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
	      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet() ;
	      VSRow       loCurrRow   = loResultSet.current() ;
	      VSRow       loPrevRow   = loResultSet.previous() ;
	      VSRow       loLastRow   = loResultSet.last() ;
	      int         liCurrSeq ;
	      int         liPrevSeq ;
	      int         liLastSeq ;

	      if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) )
	      {
	         T1R_WF_USER_ROLE.first() ;
	         return false ;
	      } /* end if ( ( loCurrRow == null ) || ( loPrevRow == null ) || ( loLastRow == null ) ) */

	      liCurrSeq = loCurrRow.getData( "AMS_ROW_VERS_NO" ).getInt() ;
	      liPrevSeq = loPrevRow.getData( "AMS_ROW_VERS_NO" ).getInt() ;
	      liLastSeq = loLastRow.getData( "AMS_ROW_VERS_NO" ).getInt() ;

	      loCurrRow.getData( "AMS_ROW_VERS_NO" ).setInt( liLastSeq + 1 ) ;
	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loPrevRow.getData( "AMS_ROW_VERS_NO" ).setInt( liCurrSeq ) ;
	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loCurrRow.getData( "AMS_ROW_VERS_NO" ).setInt( liPrevSeq ) ;
	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */

	      T1R_WF_USER_ROLE.executeQuery() ;

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
	      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet() ;
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

	      liCurrSeq = loCurrRow.getData( "AMS_ROW_VERS_NO" ).getInt() ;
	      liNextSeq = loNextRow.getData( "AMS_ROW_VERS_NO" ).getInt() ;
	      liLastSeq = loLastRow.getData( "AMS_ROW_VERS_NO" ).getInt() ;

	      loCurrRow.getData( "AMS_ROW_VERS_NO" ).setInt( liLastSeq + 1 ) ;
	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loNextRow.getData( "AMS_ROW_VERS_NO" ).setInt( liCurrSeq ) ;
	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      loCurrRow.getData( "AMS_ROW_VERS_NO" ).setInt( liNextSeq ) ;
	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */

	      T1R_WF_USER_ROLE.executeQuery() ;

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
	      VSResultSet loResultSet = T1R_WF_USER_ROLE.getResultSet() ;
	      VSRow       loCurrRow   = loResultSet.current() ;
	      VSRow       loLastRow   = loResultSet.last() ;

	      if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) )
	      {
	         return false ;
	      } /* end if ( ( loCurrRow == null ) || ( loLastRow == null ) || ( loLastRow == loCurrRow ) ) */

	      loCurrRow.getData( "AMS_ROW_VERS_NO" ).setInt( loLastRow.getData( "AMS_ROW_VERS_NO" ).getInt() + 1 ) ;

	      try
	      {
	         T1R_WF_USER_ROLE.updateDataSource() ;
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */
	      T1R_WF_USER_ROLE.executeQuery() ;

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
	      VSResultSet loWFURResultSet = T1R_WF_USER_ROLE.getResultSet() ;
	      VSRow       loUDICurrRow    = T2R_SC_USER_DIR_INFO.getCurrentRow() ;
	      VSRow       loWFURNewRow ;
	      String      lsUserID ;
	      String      lsRoleID ;

	      if ( loUDICurrRow == null )
	      {
	         return false ;
	      } /* end if ( loUDICurrRow == null ) */

	      try
	      {
	         lsUserID = loUDICurrRow.getData( "USER_ID" ).getString() ;
	         lsRoleID = getSourcePage().getRootDataSource().
	                    getCurrentRow().getData("ROLEID").getString();

	         if ( ( lsUserID != null ) && ( lsUserID != null ) )
	         {
	            loWFURNewRow = loWFURResultSet.insert() ;
	            loWFURNewRow.getData( "USID" ).setString( lsUserID ) ;
	            loWFURNewRow.getData( "ROLEID" ).setString( lsRoleID ) ;

	            T1R_WF_USER_ROLE.updateDataSource() ;
	         }
	         else
	      {
	            return false ;
	         } /* end if ( ( lsUserID != null ) && ( lsUserID != null ) ) */
	      } /* end try */
	      catch ( VSException foExp )
	      {
	         T1R_WF_USER_ROLE.undoAll() ;
	         throw foExp ;
	      } /* end catch ( VSException foExp ) */

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