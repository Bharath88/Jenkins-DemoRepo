//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.* ;

import advantage.AMSStringUtil;
import advantage.AMSUtil;
import java.util.StringTokenizer;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;

/*
**  pUserInformation*/

//{{FORM_CLASS_DECL
public class pUserInformation extends pUserInformationBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolRegenPage = false ;
   private ComboBoxElement    moUserSecRealm ;
   private boolean mboolFirstGenerate = true ;
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pUserInformation.class,
      AMSLogConstants.FUNC_AREA_SECURITY ) ;

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pUserInformation ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_pUserInformation_beforeActionPerformed
/**
 * This method is called before pending changes in the
 * result set are committed to the database.
 *
 * Modification Log : Kiran Hiranandani - 10/01/2002
 *                                      - added removeUserCache()
 *
 * @param foActElem   action element
 * @param foPageEvt   page event
 * @param foPLSReq    PLS Request
 */
void pUserInformation_beforeActionPerformed( ActionElement foActElem,
                                             PageEvent foPageEvt,
                                             PLSRequest foPLSReq )
{
   //An Info Advantage Requirement Check
   String lsStdRptUserFl = foPLSReq.getParameter("txtT1STD_RPT_USER_FL");
   String lsPwrRptUserFl = foPLSReq.getParameter("txtT1PWR_RPT_USER_FL");

   if(!AMSStringUtil.strIsEmpty(lsStdRptUserFl) || !AMSStringUtil.strIsEmpty(lsPwrRptUserFl))
   {
      String lsLastNmTxt  = foPLSReq.getParameter("txtT2LAST_NM_TXT");
      String lsFrstNmTxt  = foPLSReq.getParameter("txtT2FRST_NM_TXT");
      String lsEmailAdTxt = foPLSReq.getParameter("txtT2EMAIL_AD_TXT");

      if(AMSStringUtil.strIsEmpty(lsLastNmTxt) ||
         AMSStringUtil.strIsEmpty(lsFrstNmTxt) ||
         AMSStringUtil.strIsEmpty(lsEmailAdTxt))
      {
         raiseException("Last Name, First Name, and Email Address are required " +
         					"in order to access the infoAdvantage portal.",SEVERITY_LEVEL_ERROR);
      }
      else if(AMSStringUtil.emailIsValid(lsEmailAdTxt) != true)
      {
         raiseException("Please enter a valid email address.",SEVERITY_LEVEL_ERROR);
      }
   }//end of Info Advantage Requirement Check

   if ( foActElem.getName().equalsIgnoreCase( "T2R_SC_USER_DIR_INFOdelete" ) )
   {
      String lsErrorMssg = "";

      VSRow loRow = T1R_SC_USER_INFO.getCurrentRow();
      String lsUserID = loRow.getData( "USER_ID" ).getString() ;

      VSResultSet loVSResultSet = null;

      StringBuffer lsbWhere  = new StringBuffer( 64 ) ;
      lsbWhere.append( "USER_ID" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsUserID, AMSSQLUtil.EQUALS_OPER ) ) ;

      VSQuery loR_SC_USER_INFO_Query = new VSQuery(getParentApp().getSession(), "R_SC_USER_INFO",lsbWhere.toString(),"USER_ID");

      loR_SC_USER_INFO_Query.setPreFetchRowCount(true);
      loVSResultSet = loR_SC_USER_INFO_Query.execute();
      loVSResultSet.last();

      if (loVSResultSet.getRowCount() == 1)
      {
         VSRow loVSRow = loVSResultSet.getRowAt(1);
         loVSRow.getData("LOCK_FL").setInt(1);
         loVSRow.save();
         loVSResultSet.updateDataSource();
         lsErrorMssg = "The User <"+lsUserID+"> has been 'DISABLED'";
      }


      loVSResultSet.close();

      mboolRegenPage = true ;

      foPageEvt.setCancel( true ) ;
      foPageEvt.setNewPage( this ) ;
      raiseException( lsErrorMssg , SEVERITY_LEVEL_INFO ) ;
   }
   else if ( foActElem.getName().equals( "changePassword" ) )
   {
      String lsAppPwd     = foPLSReq.getParameter( "userPassword" ) ;
      String lsConfAppPwd = foPLSReq.getParameter( "userPasswordConfirm" ) ;
      //Retrieve parameter expireNewPassword
      String lsExpireNewPswd = foPLSReq.getParameter( "expireNewPassword" ) ;

      if ( ( lsAppPwd == null ) || ( lsAppPwd.trim().length() < 1 ) )
      {
         raiseException( "Please specify the new password.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end if ( ( lsAppPwd == null ) || ( lsAppPwd.trim().length() < 1 ) ) */
      else if ( ( lsConfAppPwd == null ) || ( lsConfAppPwd.trim().length() < 1 ) )
      {
         raiseException( "Please confirm the new password.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end else if ( ( lsConfAppPwd == null ) || ( lsConfAppPwd.trim().length() < 1 ) ) */
      else if ( !lsAppPwd.equals( lsConfAppPwd ) )
      {
         raiseException( "The password does not match with the confirmation.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end else if ( !lsAppPwd.equals( lsConfAppPwd ) ) */
      else
      {
         VSRow loRow = T1R_SC_USER_INFO.getCurrentRow() ;
         String lsUserID = loRow.getData( "USER_ID" ).getString() ;

         /* Store the Original old password for further use */
         Object loOriginalPassword = loRow.getData("PSWD_TXT").getOriginalObject();
         String lsOrigPassword = null;

         // Perform a null check as PSWD_TXT can be null when user profile is
         // created by application automatically through SCULDAP OR ADV30GuestUser.ini
         if(loOriginalPassword != null)
         {
            lsOrigPassword = loOriginalPassword.toString();
         }


         loRow.getData( "PSWD_TXT" ).setString( lsAppPwd ) ;
         //Set the value of field EXPIRE_NEW_PSWD_FL based on value input on page.
         if ( lsExpireNewPswd != null && lsExpireNewPswd.equalsIgnoreCase("on"))
         {
            loRow.getData( "EXPIRE_NEW_PSWD_FL" ).setBoolean(true);
         }
         else
         {
            loRow.getData( "EXPIRE_NEW_PSWD_FL" ).setBoolean(false);
         }
         T1R_SC_USER_INFO.updateDataSource() ;

         /*
          * When Expire New Password is selected (on User Information or Security
          * Configuration table) and password is changed, then Last Password Changed
          * Date is changed to such a value to allow expiry of the new password and to
          * prompt the User to change the password when logging into the System for the
          * first time after the password change. Last password Change Date is updated
          * using prepared statements and so, current row has to be refreshed in order
          * to show the updated "Last password Change Date"
          */
         loRow.refresh();

         /* Add old password to Password History table */
         AMSSQLConnection loConnection = null;
         try
         {
            loConnection = AMSUtil.getSQLConnection( "SC_PSWD_HIST" ) ;
            loConnection.setAutoCommit(false);
            AMSSecurity.updatePswdHistory( lsUserID, lsOrigPassword, true,
                  loRow.getData("PSWD_TXT").getString(), loConnection, false );
            loConnection.commit();
         }//end try
         catch(Exception loExcp)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

            raiseException(loExcp.toString(), SEVERITY_LEVEL_ERROR);
            try
            {
               loConnection.rollback();
            }
            catch(Exception loExcp1)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp1);

               raiseException(loExcp1.toString(), SEVERITY_LEVEL_ERROR);
            }
         }//end catch
         finally
         {
            try
            {
               loConnection.close();
            }
            catch(Exception loExcp)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

               raiseException(loExcp.toString(), SEVERITY_LEVEL_ERROR);
            }
         }//end finally

         /*
          * To ensure that the cache is cleared on both the PLS and
          * the VLS sides, in the event they have different caches
          */
         AMSSecurity.removeUserCache( lsUserID ) ;

         if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR )
         {
            raiseException( "User password changed successfully.", SEVERITY_LEVEL_INFO ) ;
         } /* end if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR ) */
      } /* end else */
   } /* end if ( foActElem.getName().equals( "changePassword" ) ) */
   else if ( foActElem.getName().equals( "changeEmailPassword" ) )
   {
      String lsEmailPwd     = foPLSReq.getParameter( "emailPassword" ) ;
      String lsConfEmailPwd = foPLSReq.getParameter( "emailPasswordConfirm" ) ;

      if ( ( lsEmailPwd == null ) || ( lsEmailPwd.trim().length() < 1 ) )
      {
         raiseException( "Please specify the new email password.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end if ( ( lsEmailPwd == null ) || ( lsEmailPwd.trim().length() < 1 ) ) */
      else if ( ( lsConfEmailPwd == null ) || ( lsConfEmailPwd.trim().length() < 1 ) )
      {
         raiseException( "Please confirm the new email password.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end else if ( ( lsConfEmailPwd == null ) || ( lsConfEmailPwd.trim().length() < 1 ) ) */
      else if ( !lsEmailPwd.equals( lsConfEmailPwd ) )
      {
         raiseException( "The email password does not match with the confirmation.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end else if ( !lsEmailPwd.equals( lsConfEmailPwd ) ) */
      else
      {
         VSRow loRow = T1R_SC_USER_INFO.getCurrentRow() ;
         String lsUserID = loRow.getData( "USER_ID" ).getString() ;

         loRow.getData( "EMAIL_PSWD_TXT" ).setString( lsEmailPwd ) ;
         T1R_SC_USER_INFO.updateDataSource() ;

         /*
          * To ensure that the cache is cleared on both the PLS and
          * the VLS sides, in the event they have different caches
          */
         AMSSecurity.removeUserCache( lsUserID ) ;

         if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR )
         {
            raiseException( "Email password changed successfully.", SEVERITY_LEVEL_INFO ) ;
         } /* end if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR ) */
      } /* end else */
   } /* end else if ( foActElem.getName().equals( "changeEmailPassword" ) ) */
   else if ( foActElem.getName().equals( "changeBOPassword" ) )
   {
      String lsBOPwd     = foPLSReq.getParameter( "BOPassword" ) ;
      String lsConfBOPwd = foPLSReq.getParameter( "BOPasswordConfirm" ) ;

      if ( ( lsBOPwd == null ) || ( lsBOPwd.trim().length() < 1 ) )
      {
         raiseException( "Please specify the new infoADVANTAGE password.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end if ( ( lsBOPwd == null ) || ( lsBOPwd.trim().length() < 1 ) ) */
      else if ( ( lsConfBOPwd == null ) || ( lsConfBOPwd.trim().length() < 1 ) )
      {
         raiseException( "Please confirm the new infoADVANTAGE password.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end else if ( ( lsConfBOPwd == null ) || ( lsConfBOPwd.trim().length() < 1 ) ) */
      else if ( !lsBOPwd.equals( lsConfBOPwd ) )
      {
         raiseException( "The infoADVANTAGE password does not match with the confirmation.", SEVERITY_LEVEL_ERROR ) ;
         foPageEvt.setCancel( true ) ;
         foPageEvt.setNewPage( this ) ;
      } /* end else if ( !lsBOPwd.equals( lsConfBOPwd ) ) */
      else
      {
         VSRow loRow = T1R_SC_USER_INFO.getCurrentRow() ;
         String lsUserID = loRow.getData( "USER_ID" ).getString() ;

         loRow.getData( "BO_PSWD_TXT" ).setString( lsBOPwd ) ;
         T1R_SC_USER_INFO.updateDataSource() ;

         /*
          * To ensure that the cache is cleared on both the PLS and
          * the VLS sides, in the event they have different caches
          */
         AMSSecurity.removeUserCache( lsUserID ) ;

         if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR )
         {
            raiseException( "infoADVANTAGE password changed successfully.", SEVERITY_LEVEL_INFO ) ;
         } /* end if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR ) */
      } /* end else */
   } /* end else if ( foActElem.getName().equals( "changeBOPassword" ) ) */
}
//END_EVENT_pUserInformation_beforeActionPerformed}}
//{{EVENT_pUserInformation_afterActionPerformed
void pUserInformation_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line
}
//END_EVENT_pUserInformation_afterActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pUserInformation_afterActionPerformed( ae, preq );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pUserInformation_beforeActionPerformed( ae, evt, preq );
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
    * This method handles re-generating the page correctly after a delete.
    * This is needed because the parent data source of the page is actually
    * the child data object. This requires an explicit execute query on the
    * root record source when a delete action fires.
    */
   public String generate()
   {
      int liSevLevel = getHighestSeverityLevel();

      if ( mboolRegenPage )
      {
         mboolRegenPage = false ;

         if ( liSevLevel < AMSPage.SEVERITY_LEVEL_ERROR )
         {
             getRootDataSource().executeQuery() ;
         }
      }

      if ( mboolFirstGenerate )
      {
         mboolFirstGenerate = false ;
         moUserSecRealm = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM" ) ;
      }

      // If the application authentication type is LDAP, then the
      // combobox should be enabled else disabled
      if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
      {
         fetchUserSecRealm(); //populate the combobox
      }
      else
      {
         moUserSecRealm.setEnabled( false );
      }

      /**
       * If there was an error then undo the change to all
       * passwords
       */
      if ( liSevLevel >= AMSPage.SEVERITY_LEVEL_ERROR )
      {
         VSRow loRow = T1R_SC_USER_INFO.getCurrentRow();
         if (loRow != null)
         {
            VSData loData = loRow.getData( "PSWD_TXT" );
            if (loData.modified())
            {
               loData.setObject(loData.getOriginalObject());
               loData.modified(false);
            }

               loData = loRow.getData( "EMAIL_PSWD_TXT" );
            if (loData.modified())
            {
               loData.setObject(loData.getOriginalObject());
               loData.modified(false);
            }

            loData = loRow.getData( "BO_PSWD_TXT" );
            if (loData.modified())
            {
               loData.setObject(loData.getOriginalObject());
               loData.modified(false);
            }
         } /* end if (loRow != null) */
      } /* end if ( liSevLevel >= AMSPage.SEVERITY_LEVEL_ERROR ) */
      return super.generate() ;
   }

   /**
    * Fetches the values for user security realm combo box
    * available as specified in the CSF.Properties file
    */
   private void fetchUserSecRealm()
   {
      String lsUserSecRealms = "";
      String lsUserSecRealm  = "";
      StringTokenizer lstSecurityRealms = null;

      try
      {
         if ( moUserSecRealm.getSize() == 0 )
         {
            lsUserSecRealms = AMSSecurity.getSecurityRealms();
            if ( ( lsUserSecRealms != null ) && ( lsUserSecRealms.trim().length() > 0 ) )
            {
               lstSecurityRealms = new StringTokenizer( lsUserSecRealms, "," );
               while ( lstSecurityRealms.hasMoreTokens() )
               {
                  lsUserSecRealm = lstSecurityRealms.nextToken();
                  moUserSecRealm.addElement(lsUserSecRealm,lsUserSecRealm);
               } //end while
            } //end if ( ( lsUserSecRealms != null )...
            else
            {
               /*
                * when there is no property set for security realm we need to
                * set the IgnoreSetValue flag to true so that it does not set empty string in
                * the comboBox's option tags.
                */
               ((AMSComboBoxElement)moUserSecRealm).setIgnoreSetValue(true);
            }
         } //end if ( moUserSecRealm.getSize() == 0 )
      } //end try
      catch (AMSSecurityException loExp)
      {
         raiseException( "Unable to get the user security realm list", SEVERITY_LEVEL_ERROR ) ;
      } //end catch
   } //end of fetchUserSecRealm()
}
