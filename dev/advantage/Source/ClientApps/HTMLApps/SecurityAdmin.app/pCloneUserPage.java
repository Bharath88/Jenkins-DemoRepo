//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.common.AMSSecurity;
import org.apache.commons.logging.*;
import java.util.*;
import advantage.AMSStringUtil;

   /*
   **  pCloneUserPage*/

//{{FORM_CLASS_DECL
public class pCloneUserPage extends pCloneUserPageBase

//END_FORM_CLASS_DECL}}
   {
      /** This is the logger object for the class */
      private static Log moLog = AMSLogger.getLog( pCloneUserPage.class,
            AMSLogConstants.FUNC_AREA_SECURITY ) ;

      private int                miCurStep          = 1 ;
      private boolean            mboolFirstGenerate = true ;
      private TextContentElement moStepTitle        = null ;
      private ActionElement      moBackBtn          = null ;
      private ActionElement      moNextBtn          = null ;
      private ActionElement      moCreateBtn        = null ;
      private DivElement         moStep1Cont        = null ;
      private DivElement         moStep2Cont        = null ;
      private DivElement         moStep3Cont        = null ;
      private TextFieldElement   moNewUserID        = null ;
      private TextFieldElement   moNewLastNm        = null ;
      private TextFieldElement   moNewFirstNm       = null ;
      private TextFieldElement   moNewPswd          = null ;
      private TextFieldElement   moNewPswdConf      = null ;
      private CheckboxElement    moExpireNewPswd    = null ;
      private TextFieldElement   moEmailAddress     = null ;
      private TextFieldElement   moConfNewUserID    = null ;
      private TextFieldElement   moConfNewLastNm    = null ;
      private TextFieldElement   moConfNewFirstNm   = null ;
      private TextFieldElement   moConfNewPswd      = null ;
      private TextFieldElement   moConfNewPswdConf  = null ;
      private CheckboxElement    moConfExpireNewPswd= null ;
      private TextFieldElement   moConfEmailAddress = null ;
      private String             msNewUserID        = null ;
      private String             msNewLastNm        = null ;
      private String             msNewFirstNm       = null ;
      private String             msNewPswd          = null ;
      private String             msConfNewPswd      = null ;
      private String             msExpireNewPswd    = null ;
      private String             msEmailAddress     = null ;
      //Added these four fields to handle user security realm and expiring password
      //hgiang, 1/22/06
      private String             msUserSecRealm    = null ;
      private ComboBoxElement    moUserSecRealm ;

      private boolean mboolExistLDAPUser = false ;

      // Declarations for instance variables used in the form

      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code. To customize paint
      // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pCloneUserPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      }



//{{EVENT_CODE
//{{EVENT_pCloneUserPage_beforeActionPerformed
void pCloneUserPage_beforeActionPerformed( ActionElement foActnElem, PageEvent foPgEvt, PLSRequest foPLSReq )
{
   String lsActnNm = foActnElem.getName() ;

   if ( miCurStep == 2 )
   {
      msNewUserID   = foPLSReq.getParameter( "NewUserID" ) ;
      msNewLastNm   = foPLSReq.getParameter( "NewLastName" ) ;
      msNewFirstNm  = foPLSReq.getParameter( "NewFirstName" ) ;
      msNewPswd     = foPLSReq.getParameter( "NewPassword" ) ;
      msConfNewPswd = foPLSReq.getParameter( "NewPasswordConfirm" ) ;
      msExpireNewPswd= foPLSReq.getParameter( "expireNewPassword" ) ;
      msEmailAddress= foPLSReq.getParameter( "emailAddress" ) ;
      //Get the system user's chosen expire password and user security realm options
      //hgiang, 1/22/06
      msUserSecRealm   = foPLSReq.getParameter("txtT1USER_SEC_REALM");
      String lsExistLDAPUser  = foPLSReq.getParameter( "EXIST_LDAP_USER_FL" ) ;
      if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP &&
           lsExistLDAPUser != null && lsExistLDAPUser.length() > 0 )
      {
         mboolExistLDAPUser = true ;
      }
      else
      {
         mboolExistLDAPUser = false ;
      }

   } /* end if ( miCurStep == 2 ) */

   if ( lsActnNm.equalsIgnoreCase( "next" ) )
   {
      if ( miCurStep < 3 )
      {
         if ( validateNext( foPLSReq ) )
         {
            miCurStep++ ;
         } /* end if ( validateNext( foPLSReq ) ) */
      } /* end if ( miCurStep < 3 ) */
      foPgEvt.setCancel( true ) ;
      foPgEvt.setNewPage( this ) ;
   } /* end if ( lsActnNm.equalsIgnoreCase( "next" ) ) */
   else if ( lsActnNm.equalsIgnoreCase( "back" ) )
   {
      if ( miCurStep > 1 )
      {
         miCurStep-- ;
      } /* end if ( miCurStep > 1 ) */
      foPgEvt.setCancel( true ) ;
      foPgEvt.setNewPage( this ) ;
   } /* end else if ( lsActnNm.equalsIgnoreCase( "back" ) ) */
   else if ( lsActnNm.equalsIgnoreCase( "createNewUser" ) )
   {
      cloneUserInformation() ;
   } /* end else if ( lsActnNm.equalsIgnoreCase( "createNewUser" ) ) */
}
//END_EVENT_pCloneUserPage_beforeActionPerformed}}

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
			pCloneUserPage_beforeActionPerformed( ae, evt, preq );
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

      public void cloneUserInformation()
      {
         DataSource  loRootDS             = null ;
         VSRow       loSrcRow             = null ;
         VSResultSet loNewUserDirInfoRS   = null ;
         VSResultSet loNewUserInfoRS      = null ;
         VSResultSet loNewUserRoleLnkRS   = null ;
         String      lsOrigUserID         = null ;
      String lsMessage="";
         loRootDS = getRootDataSource() ;
         loSrcRow = loRootDS.getCurrentRow() ;
      VSSession loSession       = getParentApp().getSession() ;
      boolean   lboolBeganTrans = false ;

         if ( loSrcRow == null )
         {
            raiseException( "An existing user must first be selected.",
                AMSPage.SEVERITY_LEVEL_ERROR ) ;
            miCurStep = 1 ;
            return ;
         }

         lsOrigUserID = loSrcRow.getData( "USER_ID" ).getString() ;

      if ( mboolExistLDAPUser )
      {
         if ( AMSParams.miAuthenticationType != AMSParams.TYPE_LDAP )
         {
            raiseException( "Existing LDAP user flag cannot be checked.", SEVERITY_LEVEL_ERROR ) ;
            miCurStep = 2 ;
            return ;
         }

         if ( ( msNewPswd == null && msConfNewPswd != null ) ||
              ( msNewPswd != null && msConfNewPswd == null ) )
         {
            raiseException( "Invalid user password entered. Please re-enter user password.",
                AMSPage.SEVERITY_LEVEL_ERROR ) ;
            miCurStep = 2 ;
            return ;
         } /* end if ( ( ( msNewPswd == null ) || ( ( msNewPswd.trim() ).length() < 1 ) ) || . . . */
         else if ( msNewPswd != null && msConfNewPswd != null &&
                   !msNewPswd.equals( msConfNewPswd ) )
         {
            raiseException( "Invalid user password entered. Please re-enter user password.",
                AMSPage.SEVERITY_LEVEL_ERROR ) ;
            miCurStep = 2 ;
            return ;
         } /* end if ( msNewPswd != null && msConfNewPswd != null && . . . */
      }
      else if ( !mboolExistLDAPUser )
      {
         if ( ( ( msNewPswd == null ) || ( ( msNewPswd.trim() ).length() < 1 ) ) ||
              ( ( msConfNewPswd == null ) || ( ( msConfNewPswd.trim()).length() < 1 ) ) ||
              ( !msNewPswd.equals( msConfNewPswd ) ) )
         {
            raiseException( "Invalid user password entered. Please re-enter user password.",
                AMSPage.SEVERITY_LEVEL_ERROR ) ;
            miCurStep = 2 ;
            return ;
         } /* end if ( ( ( msNewPswd == null ) || ( ( msNewPswd.trim() ).length() < 1 ) ) || . . . */
      }

         /*
          * Now clone the original user's security information to
          * a new user entry
          */
         loNewUserDirInfoRS  = getNewResultSet( "R_SC_USER_DIR_INFO" ) ;
         cloneOrigUserDirInfo( lsOrigUserID, loNewUserDirInfoRS ) ;

         loNewUserInfoRS  = getNewResultSet( "R_SC_USER_INFO" ) ;
         cloneOrigUserInfo( lsOrigUserID, loNewUserInfoRS ) ;

         loNewUserRoleLnkRS  = getNewResultSet( "R_SC_USER_ROLE_LNK" ) ;
         cloneOrigUserRoleLnk( lsOrigUserID, loNewUserRoleLnkRS ) ;

         /*
          * Finally save the new user's information
          */
         try
         {
         if ( !loSession.isTransactionInProgress() )
         {
            lboolBeganTrans = true ;
            loSession.beginTrans() ;
         } /* end if ( !loSession.isTransactionInProgress() ) */
         lsMessage="Could not save new user information.";
         loNewUserDirInfoRS.updateDataSource() ;

         lsMessage="Could not create new user, unable to save security information and role assignments.";
         loNewUserInfoRS.updateDataSource() ;

         lsMessage="Could not create new user, unable to save security role assignments.";
         loNewUserRoleLnkRS.updateDataSource() ;

         if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) )
         {
            loSession.commit() ;
         }
      } /* end try */
         catch( VSException loException )
         {
         raiseException( lsMessage, AMSPage.SEVERITY_LEVEL_ERROR);
         moLog.error( lsMessage, loException ) ;
            loNewUserDirInfoRS.close() ;
            loNewUserInfoRS.close() ;
            loNewUserRoleLnkRS.close() ;

            return ;
         } /* end catch( VSException loException ) */
      finally
      {
         if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) )
         {
            loSession.rollback() ;
         } /* end if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) ) */
      }

         loNewUserDirInfoRS.close() ;
         loNewUserInfoRS.close() ;
         loNewUserRoleLnkRS.close() ;

         raiseException( "New user \"" + msNewUserID + "\" was created successfully.",
                AMSPage.SEVERITY_LEVEL_INFO ) ;

         miCurStep     = 1 ;
         msNewUserID   = null ;
         msNewLastNm   = null ;
         msNewFirstNm  = null ;
         msNewPswd     = null ;
         msConfNewPswd = null ;
         msExpireNewPswd = "off";
         msEmailAddress = null;
   } /* end cloneUserInformation() */


      public void cloneRow( VSRow foOrigRow, VSRow foNewRow )
      {
         for ( int i = 1 ; i <= foNewRow.getColumnCount() ; i++ )
         {
            foNewRow.getData( i ).setData( foOrigRow.getData( i ) ) ;
         } /* end for ( int i = 1 ; i <= foNewRow.getColumnCount() ; i++ ) */
      } /* end cloneRow() */


      public void cloneOrigUserDirInfo( String fsOrigUserID, VSResultSet foNewUserDirInfoRS )
      {
         VSResultSet loVSResultSet  = null ;
         VSRow       loOrigRow      = null ;
         VSRow       loNewRow       = null ;
         int         liRowCount     = 0 ;
         String      lsWhereClause  = "USER_ID=" + AMSSQLUtil.getANSIQuotedStr(fsOrigUserID,true) ;

         loVSResultSet = getResultSet( "R_SC_USER_DIR_INFO", lsWhereClause ) ;

         if ( loVSResultSet == null )
         {
           return ;
         } /* end  if ( loResultSet == null ) */
                loOrigRow    = loVSResultSet.last() ;
         liRowCount   = loVSResultSet.getRowCount() ;

         if ( ( liRowCount != 1 ) || ( loOrigRow == null ) )
         {
            loVSResultSet.close() ;
            return ;
         } /* end if ( ( liRowCount != 1 ) || ( loOrigRow == null ) ) */

         if ( foNewUserDirInfoRS == null )
         {
             return ;
         } /* end if ( foNewUserDirInfoRS == null ) */

         foNewUserDirInfoRS.last() ;

         loNewRow = foNewUserDirInfoRS.insert() ;

         if ( loNewRow == null )
         {
            return ;
         } /* end if ( loNewRow == null ) */

         cloneRow( loOrigRow, loNewRow ) ;

         loNewRow.getData( "USER_ID" ).setString( msNewUserID ) ;
         loNewRow.getData( "LAST_NM_TXT" ).setString( msNewLastNm ) ;
         loNewRow.getData( "FRST_NM_TXT" ).setString( msNewFirstNm ) ;

      //Set the email address field.
      loNewRow.getData( "EMAIL_AD_TXT" ).setString( msEmailAddress ) ;
         loVSResultSet.close() ;

         return ;
      } /* end cloneOrigUserDirInfo() */

      public void cloneOrigUserInfo( String fsOrigUserID, VSResultSet foNewUserInfoRS )
      {
         VSResultSet loVSResultSet  = null ;
         VSRow       loOrigRow      = null ;
         VSRow       loNewRow       = null ;
         int         liRowCount     = 0 ;
         String      lsWhereClause  = "USER_ID=" + AMSSQLUtil.getANSIQuotedStr(fsOrigUserID,true) ;
         java.util.Date loDate = new java.util.Date();

         loVSResultSet = getResultSet( "R_SC_USER_INFO", lsWhereClause ) ;

         if ( loVSResultSet == null )
         {
           return ;
         } /* end  if ( loResultSet == null ) */
         loOrigRow    = loVSResultSet.last() ;
         liRowCount   = loVSResultSet.getRowCount() ;

         if ( ( liRowCount != 1 ) || ( loOrigRow== null ) )
         {
            loVSResultSet.close() ;
            return ;
         } /* end if ( ( liRowCount != 1 ) || ( loOrigRow == null ) ) */

         if ( foNewUserInfoRS == null )
         {
             return ;
         } /* end if ( foNewUserInfoRS == null ) */

         foNewUserInfoRS.last() ;

         loNewRow = foNewUserInfoRS.insert() ;

         if ( loNewRow == null )
         {
            return ;
         } /* end if ( loNewRow == null ) */

         cloneRow( loOrigRow, loNewRow ) ;

         loNewRow.getData( "USER_ID" ).setString( msNewUserID ) ;
         loNewRow.getData( "PSWD_TXT" ).setString( msNewPswd ) ;
         loNewRow.getData( "PSWD_CHG_DT" ).setVSDate( new VSDate() ) ;
         loNewRow.getData( "EMAIL_PSWD_TXT" ).setString( "" ) ;
         loNewRow.getData( "CA_PSWD_TXT" ).setString( "" ) ;
         loNewRow.getData( "BO_PSWD_TXT" ).setString( "" ) ;
         loNewRow.getData( "SECRET_KEY_TXT" ).setString( "" ) ;
         loNewRow.getData( "STD_RPT_USER_FL" ).setBoolean(false);
         loNewRow.getData( "PWR_RPT_USER_FL" ).setBoolean(false);
         //Set the Expire New Password flag.

         loNewRow.getData( "USER_SEC_REALM" ).setString(msUserSecRealm);
         //set value of expire password field
         //hgiang, 1/22/06


         if ( msExpireNewPswd != null && msExpireNewPswd.equalsIgnoreCase("on"))
         {
            loDate.setTime(0);
            loNewRow.getData( "EXPIRE_NEW_PSWD_FL" ).setBoolean(true);
         }
         else
         {
            msExpireNewPswd = "off";
            loNewRow.getData( "EXPIRE_NEW_PSWD_FL" ).setBoolean(false);
         }
         loNewRow.getData("PSWD_CHG_DT").setDate(loDate);
         loVSResultSet.close() ;

         return ;
      } /* end cloneOrigUserInfo() */

      public void cloneOrigUserRoleLnk( String fsOrigUserID, VSResultSet foNewUserRoleLnkRS )
      {
         VSResultSet loVSResultSet  = null ;
         VSRow       loOrigRow      = null ;
         VSRow       loNewRow       = null ;
         int         liRowCount     = 0 ;
         String      lsWhereClause  = "USER_ID=" + AMSSQLUtil.getANSIQuotedStr(fsOrigUserID,true) ;

         loVSResultSet = getResultSet( "R_SC_USER_ROLE_LNK", lsWhereClause ) ;

         if ( loVSResultSet == null )
         {
           return ;
         } /* end  if ( loResultSet == null ) */
                loOrigRow    = loVSResultSet.last() ;
         liRowCount   = loVSResultSet.getRowCount() ;

         if ( ( liRowCount < 1 ) || ( loOrigRow == null ) )
         {
            loVSResultSet.close() ;
            return ;
         } /* end if ( ( liRowCount != 1 ) || ( loOrigRow == null ) ) */

         if ( foNewUserRoleLnkRS == null )
         {
             return ;
         } /* end if ( foNewUserRoleLnkRS == null ) */

         loOrigRow = loVSResultSet.first() ;

         while ( loOrigRow != null )
         {
            loNewRow = foNewUserRoleLnkRS.insert() ;

            if ( loNewRow == null )
            {
               return ;
            } /* end if ( loNewRow == null ) */

            cloneRow( loOrigRow, loNewRow ) ;

            loNewRow.getData( "USER_ID" ).setString( msNewUserID ) ;

            loOrigRow = loVSResultSet.next() ;
         }

         loVSResultSet.close() ;

         return ;
      }

      public VSResultSet getResultSet( String fsQueryName,
                                       String fsWhereClause )
      {
          VSQuery loQuery;

          loQuery = new VSQuery( getParentApp().getSession(), fsQueryName,
                                 fsWhereClause, "" ) ;

          return loQuery.execute() ;
      } /* end getResultSet() */


      public VSResultSet getNewResultSet( String fsQueryName )
      {
          VSQuery loQuery;

          loQuery = new VSQuery( getParentApp().getSession(), fsQueryName, "", "" ) ;

          return loQuery.getNewResultSet() ;
      } /* end getNewResultSet() */

      public void beforeGenerate()
      {
          VSRow loCurrRow = T1R_SC_USER_DIR_INFO.getCurrentRow() ;
          CheckboxElement loCBEExistLDAPUser ;

         if ( mboolFirstGenerate )
         {
            mboolFirstGenerate = false ;
            moStepTitle       = (TextContentElement)getElementByName( "CurrStepTitle" ) ;
            moBackBtn         = (ActionElement)getElementByName( "back" ) ;
            moNextBtn         = (ActionElement)getElementByName( "next" ) ;
            moCreateBtn       = (ActionElement)getElementByName( "createNewUser" ) ;
            moStep1Cont       = (DivElement)getElementByName( "Step1Cont" ) ;
            moStep2Cont       = (DivElement)getElementByName( "Step2Cont" ) ;
            moStep3Cont       = (DivElement)getElementByName( "Step3Cont" ) ;
            moNewUserID       = (TextFieldElement)getElementByName( "NewUserID" ) ;
            moNewLastNm       = (TextFieldElement)getElementByName( "NewLastName" ) ;
            moNewFirstNm      = (TextFieldElement)getElementByName( "NewFirstName" ) ;
            moNewPswd         = (TextFieldElement)getElementByName( "NewPassword" ) ;
            moNewPswdConf     = (TextFieldElement)getElementByName( "NewPasswordConfirm" ) ;
            moExpireNewPswd   = (CheckboxElement)getElementByName( "expireNewPassword" ) ;
            moEmailAddress    = (TextFieldElement)getElementByName( "emailAddress" ) ;
            moConfNewUserID   = (TextFieldElement)getElementByName( "confirmNewUserID" ) ;
            moConfNewLastNm   = (TextFieldElement)getElementByName( "confirmNewLastName" ) ;
            moConfNewFirstNm  = (TextFieldElement)getElementByName( "confirmNewFirstName" ) ;
            moConfNewPswd     = (TextFieldElement)getElementByName( "confirmNewPassword" ) ;
            moConfNewPswdConf = (TextFieldElement)getElementByName( "confirmNewPasswordConfirm" ) ;
            //--two new fields added to handle user security realm and expire password
            //--hgiang, 1/22/06
            moUserSecRealm    = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM2" ) ;

            moConfExpireNewPswd   = (CheckboxElement)getElementByName( "confirmExpireNewPassword" ) ;
            moConfEmailAddress    = (TextFieldElement)getElementByName( "confirmEmailAddress" ) ;
         } /* end if ( mboolFirstGenerate ) */

         switch( miCurStep )
         {
            case 1 :
               moStepTitle.setValue( "Step 1: Select a user to copy..." ) ;
               moBackBtn.setEnabled( false ) ;
               moNextBtn.setEnabled( true ) ;
               moCreateBtn.setEnabled( false ) ;
               moStep1Cont.setVisible( true ) ;
               moStep2Cont.setVisible( false ) ;
               moStep3Cont.setVisible( false ) ;
               break ;
            case 2 :
               moStepTitle.setValue( "Step 2: Enter New User Information" ) ;
               moBackBtn.setEnabled( true ) ;
               moNextBtn.setEnabled( true ) ;
               moCreateBtn.setEnabled( false ) ;
               moStep1Cont.setVisible( false ) ;
               moStep2Cont.setVisible( true ) ;
               moStep3Cont.setVisible( false ) ;
               moNewUserID.setValue( msNewUserID != null ? msNewUserID : "" ) ;
               moNewLastNm.setValue( msNewLastNm != null ? msNewLastNm : "" ) ;
               moNewFirstNm.setValue( msNewFirstNm != null ? msNewFirstNm : "" ) ;
               moNewPswd.setValue( msNewPswd != null ? msNewPswd : "" ) ;
               moNewPswdConf.setValue( msConfNewPswd != null ? msConfNewPswd : "" ) ;
               moUserSecRealm    = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM" ) ;
               // If the application authentication type is LDAP, then the
              // combobox should be enabled else disabled
               if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
               {
                    fetchUserSecRealm("txtT1USER_SEC_REALM"); //populate the combobox
               }
               else
               {
                  moUserSecRealm.setEnabled( false );
               }
               loCBEExistLDAPUser = (CheckboxElement) getElementByName( "EXIST_LDAP_USER_FL" ) ;
               // Check or uncheck the EXIST_LDAP_USER_FL checkbox based on
               // the value of the mboolExistLDAPUser variable
               if ( mboolExistLDAPUser )
               {
                  loCBEExistLDAPUser.setChecked( true );
               }
               else
               {
                  loCBEExistLDAPUser.setChecked( false );
               }

               // If the application authentication type is LDAP, then the
               // checkbox should be enabled else disabled
               if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
               {
                  loCBEExistLDAPUser.setEnabled( true );
               }
               else
               {
                  loCBEExistLDAPUser.setEnabled( false );
               }

               //Set the expire new password flag.
               if ( msExpireNewPswd != null)
               {
                  if (msExpireNewPswd.equalsIgnoreCase("on"))
                     moExpireNewPswd.setValue( "true");
                  else
                     moExpireNewPswd.setValue( "false");
               }
               else
               {
                  moExpireNewPswd.setValue( "false");
               }
               moEmailAddress.setValue( msEmailAddress != null ? msEmailAddress : "" ) ;
               break ;
            case 3 :
               moStepTitle.setValue( "Step 3: Confirm New User Creation" ) ;
               moBackBtn.setEnabled( true ) ;
               moNextBtn.setEnabled( false ) ;
               moCreateBtn.setEnabled( true ) ;
               moStep1Cont.setVisible( false ) ;
               moStep2Cont.setVisible( false ) ;
               moStep3Cont.setVisible( true ) ;
               moConfNewUserID.setValue( msNewUserID != null ? msNewUserID : "" ) ;
               moConfNewLastNm.setValue( msNewLastNm != null ? msNewLastNm : "" ) ;
               moConfNewFirstNm.setValue( msNewFirstNm != null ? msNewFirstNm : "" ) ;
               moConfNewPswd.setValue( msNewPswd != null ? msNewPswd : "" ) ;
               moConfNewPswdConf.setValue( msConfNewPswd != null ? msConfNewPswd : "" ) ;

               moUserSecRealm = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM2" ) ;
               // If the application authentication type is LDAP, then the
               // combobox should be enabled else disabled
               if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
               {
                  //shows chosen security realm
                  fetchUserSecRealm("txtT1USER_SEC_REALM2");
               }
               else
               {
                  moUserSecRealm.setEnabled( false );
               }
               //moUserSecRealm = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM2" ) ;
               moUserSecRealm.setSelectedText(msUserSecRealm);
               loCBEExistLDAPUser = (CheckboxElement) getElementByName( "EXIST_LDAP_USER_FL2" ) ;
               // Check or uncheck the EXIST_LDAP_USER_FL checkbox based on
               // the value of the mboolExistLDAPUser variable
               if ( mboolExistLDAPUser )
               {
                  loCBEExistLDAPUser.setChecked( true );
               }
               else
               {
                  loCBEExistLDAPUser.setChecked( false );
               }

               loCBEExistLDAPUser.setEnabled( false );

               if ( msExpireNewPswd != null)
               {
                  if (msExpireNewPswd.equalsIgnoreCase("on"))
                  {
                     moConfExpireNewPswd.setValue( "true");
                  }
                  else
                  {
                     moConfExpireNewPswd.setValue( "false");
                  }
               }
               else
               {
                  moConfExpireNewPswd.setValue( "false");
               }
               moConfEmailAddress.setValue( msEmailAddress != null ? msEmailAddress : "" ) ;
               break ;
            default :
               moStepTitle.setValue( "" ) ;
               moBackBtn.setEnabled( false ) ;
               moNextBtn.setEnabled( false ) ;
               moCreateBtn.setEnabled( false ) ;
               moStep1Cont.setVisible( false ) ;
               moStep2Cont.setVisible( false ) ;
               moStep3Cont.setVisible( false ) ;
               break ;
         } /* end switch( miCurStep ) */
      } /* end beforeGenerate() */

      private boolean validateNext( PLSRequest foPLSReq )
      {
         VSRow   loCurrRow ;
         boolean lboolRC = true ;

         switch( miCurStep )
         {
            case 1 :
               loCurrRow = T1R_SC_USER_DIR_INFO.getCurrentRow() ;
               if ( loCurrRow == null )
               {
                  raiseException( "An existing user must first be selected.",
                      SEVERITY_LEVEL_ERROR ) ;
                  lboolRC = false ;
               } /* end if ( loCurrRow == null ) */
               break ;
            case 2 :
               if ( ( msNewUserID == null ) || ( msNewUserID.trim().length() == 0 ) )
               {
                  raiseException( "User ID is required.",
                      SEVERITY_LEVEL_ERROR ) ;
                  lboolRC = false ;
               } /* end if ( ( msNewUserID == null ) || ( msNewUserID.trim().length() == 0 ) ) */
               if ( mboolExistLDAPUser )
               {
                  if ( AMSParams.miAuthenticationType != AMSParams.TYPE_LDAP )
                  {
                     raiseException( "Existing LDAP user flag cannot be checked.",
                         SEVERITY_LEVEL_ERROR ) ;
                     lboolRC = false ;
                  }

                  if ( ( msNewPswd == null && msConfNewPswd != null ) ||
                      ( msNewPswd != null && msConfNewPswd == null ) )
                  {
                     raiseException( "The password does not match with the confirmation.",
                         SEVERITY_LEVEL_ERROR ) ;
                     lboolRC = false ;
                  }
                  else if ( msNewPswd != null && msConfNewPswd != null &&
                        !msNewPswd.equals( msConfNewPswd ) )
                  {
                     raiseException( "The password does not match with the confirmation.",
                         SEVERITY_LEVEL_ERROR ) ;
                     lboolRC = false ;
                  }
               }
               else if ( !mboolExistLDAPUser )
               {
                  if ( ( msNewPswd == null ) || ( msNewPswd.trim().length() == 0 ) )
                  {
                     raiseException( "New Password is required.",
                         SEVERITY_LEVEL_ERROR ) ;
                     lboolRC = false ;
                  } /* end if ( ( msNewPswd == null ) || ( msNewPswd.trim().length() == 0 ) ) */
                  if ( ( msConfNewPswd == null ) || ( msConfNewPswd.trim().length() == 0 ) )
                  {
                     raiseException( "Confirm New Password is required.",
                         SEVERITY_LEVEL_ERROR ) ;
                     lboolRC = false ;
                  } /* end if ( ( msConfNewPswd == null ) || ( msConfNewPswd.trim().length() == 0 ) ) */
                  if ( lboolRC )
                  {
                     if ( !msNewPswd.equals( msConfNewPswd ) )
                     {
                        raiseException( "The password does not match with the confirmation.",
                            SEVERITY_LEVEL_ERROR ) ;
                        lboolRC = false ;
                     } /* end if ( !msNewPswd.equals( msConfNewPswd ) ) */
                  } /* end if ( lboolRC ) */
               }
               if(AMSStringUtil.strIsEmpty(msNewLastNm))
               {
                  raiseException("Last Name is required."
                        ,SEVERITY_LEVEL_ERROR);
                  lboolRC = false ;
               }
               if(AMSStringUtil.strIsEmpty(msNewFirstNm))
               {
                  raiseException("First Name is required."
                        ,SEVERITY_LEVEL_ERROR);
                  lboolRC = false ;
               }
               break ;
            default :
               raiseException( "Next action is not permitted from this step",
                     SEVERITY_LEVEL_ERROR ) ;
               return false ;
         } /* end switch( miCurStep ) */
         return lboolRC ;
      } /* end validateNext() */
   /**
    * This method fetches the user security realm combo box with values
    * available as specified in the CSF.Properties file
    * @author hgiang
    *
    * TODO To change the template for this generated type comment go to
    * Window - Preferences - Java - Code Style - Code Templates
    */
   private void fetchUserSecRealm(String fsRealm)
   {
      String lsUserSecRealms = "";
      String lsUserSecRealm  = "";
      StringTokenizer lstSecurityRealms = null;

      try
      {
         ComboBoxElement loCBE = (ComboBoxElement)getElementByName(fsRealm);
         if ( loCBE.getSize() == 0 )
         {
            lsUserSecRealms = AMSSecurity.getSecurityRealms();
            if ( ( lsUserSecRealms != null ) && ( lsUserSecRealms.trim().length() > 0 ) )
            {
               lstSecurityRealms = new StringTokenizer( lsUserSecRealms, "," );
               while ( lstSecurityRealms.hasMoreTokens() )
               {
                  lsUserSecRealm = lstSecurityRealms.nextToken();
                  loCBE.addElement(lsUserSecRealm,lsUserSecRealm);
               } //end while
            } //end if ( ( lsUserSecRealms != null )...
         } //end if ( loCBE.getSize() == 0 )
      } //end try
      catch (AMSSecurityException loExp)
      {
         raiseException( "Unable to get the user security realm list", SEVERITY_LEVEL_ERROR ) ;
      } //end catch
   } //end of fetchUserSecRealm()
}
