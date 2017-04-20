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
   **  pRoleUserLink
   */

//{{FORM_CLASS_DECL
public class pRoleUserLink extends pRoleUserLinkBase

//END_FORM_CLASS_DECL}}
   implements AMSAvailableSelectionPage
   {
      
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pRoleUserLink.class,
      AMSLogConstants.FUNC_AREA_SECURITY ) ;
   
   // Declarations for instance variables used in the form

      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code. To customize paint
      // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pRoleUserLink ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      }



//{{EVENT_CODE
//{{EVENT_pRoleUserLink_beforeActionPerformed
void pRoleUserLink_beforeActionPerformed( ActionElement foAction, PageEvent evt, PLSRequest foPLSReq )
{
	//Write Event Code below this line

   /**
    * There is a field named Revoke Reason on this page which is non-persistent. The value of
    * this field needs to be passed onto the VLS side on assignment or revocation.
    * Whenever any assignment or revocation action takes place, the Revoke Reason value is stored
    * in the session.
    */
   if( String.valueOf(AMSHyperlinkActionElement.CONFIG_SELECT_ITEM).equals(foAction.getAction()) ||
         String.valueOf(AMSHyperlinkActionElement.CONFIG_REMOVE_ITEM).equals(foAction.getAction()) )
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
//END_EVENT_pRoleUserLink_beforeActionPerformed}}

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
			pRoleUserLink_beforeActionPerformed( ae, evt, preq );
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
       * to move an item from the list of available items to
       * the list of selected items.
       *
       * Modification Log : Mark Shipley - 04/14/2001
       *                                 - inital version
       *
       * @param foPLSReq The PLS Request
       * @return boolean The success or failure of the action
       */
      public boolean addToSelected( PLSRequest foPLSReq )
      {
       VSResultSet loResultSet = T2R_SC_USER_ROLE_LNK.getResultSet() ;
       VSRow       loCurrRow   = T3R_SC_USER_INFO.getCurrentRow() ;
       VSRow       loRoleRow   = T1R_SC_SEC_ROLE.getCurrentRow() ;
       VSRow       loNewRow ;
       VSQuery     loQuery ;
       VSResultSet loPrecNoRS ;
       String      lsUserID ;
       int         liPrecNo    = 1 ;

       if ( ( loCurrRow == null ) || ( loRoleRow == null ) )
       {
          return false ;
       } /* end if ( ( loCurrRow == null ) || ( loRoleRow == null ) ) */

       lsUserID = loCurrRow.getData( "USER_ID" ).getString() ;

       loQuery = new VSQuery( getParentApp().getSession(), "R_SC_USER_ROLE_LNK",
                              "USER_ID=" + AMSSQLUtil.getANSIQuotedStr(lsUserID,true), "PREC_NO DESC" ) ;

       loPrecNoRS = loQuery.execute() ;
       if ( loPrecNoRS.first() != null )
       {
          liPrecNo = loPrecNoRS.first().getData( "PREC_NO" ).getInt() + 1 ;
       } /* end if ( loPrecNoRS.first() != null ) */
       loPrecNoRS.close() ;

       loNewRow = loResultSet.insert() ;
       loNewRow.getData( "USER_ID" ).setString( lsUserID ) ;
       loNewRow.getData( "SEC_ROLE_ID" ).setString( loRoleRow.getData( "SEC_ROLE_ID" ).getString() ) ;
       loNewRow.getData( "PREC_NO" ).setInt( liPrecNo ) ;

       try
       {
          T2R_SC_USER_ROLE_LNK.updateDataSource() ;
       } /* end try */
       catch ( VSException foExp )
       {
          T2R_SC_USER_ROLE_LNK.undoAll() ;
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
       * Modification Log : Mark Shipley - 04/14/2001
       *                                 - inital version
       *
       * @param foPLSReq The PLS Request
       * @return boolean The success or failure of the action
       */
      public boolean removeFromSelected( PLSRequest foPLSReq )
      {
         VSRow loCurrRow = T2R_SC_USER_ROLE_LNK.getCurrentRow();
         if( loCurrRow == null )
         {
            return false ;
         }
         try
         {
            //Delete the User-Role Link record
            T2R_SC_USER_ROLE_LNK.delete();
            T2R_SC_USER_ROLE_LNK.updateDataSource();
         } /* end try */
         catch ( VSException loException )
         {
            /*
            When Administrator tries to un-assign a Role from User, then it
            a delete on R_SC_USER_ROLE_LNK.
            If delete on R_SC_USER_ROLE_LNK failed then an Exception would be
            caught here. It is very important to undo the delete when any such
            Exception occurs. Note that this page has multiple datasources and
            VSPage during generating this page make efforts to synchronize the
            datasources. Suppose the first attempt delete was unsuccesful due to
            data setup(say Foreign organization records existed for this entry) and
            we did not undo the delete here, and then in the same Session User
            delete associated Foreign organization records and came back to this page
            (note that navigation between Foreign Organization page and Security Role
            User Grants page should occur via Role Maintenance->Manage Role->Edit Role
            link on Left hand side navigation panel) then during page generation,
            updateDataSource is again fired and the earlier pending delete go through
            successfully, and the end-User on navigating to this page finds the
            User Role link record already deleted. Hence it is very important to
            undo the delete here in case the delete failed for some reason.
            */
            T2R_SC_USER_ROLE_LNK.undoAll() ;
            throw loException ;
         }//end catch

         /*
            To ensure that the user's roles have been updated in
            the cache - the cache is cleared on both the PLS and
            the VLS sides, in the event they have different caches
         */
         AMSSecurity.removeUserCache( loCurrRow.getData( "USER_ID" ).getString() ) ;
         return true;
      }//end of method

   }
