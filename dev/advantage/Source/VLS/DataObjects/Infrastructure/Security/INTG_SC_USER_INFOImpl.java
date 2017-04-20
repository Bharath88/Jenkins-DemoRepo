//{{COMPONENT_IMPORT_STMTS
package advantage;
import java.util.Enumeration;
import java.util.Vector;
import versata.common.*;
import versata.common.vstrace.*;
import versata.vls.cache.*;
import versata.vls.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.amsinc.gems.adv.common.*;
import com.versata.util.logging.*;
import org.apache.commons.logging.*;

//END_COMPONENT_IMPORT_STMTS}}

/*
**  INTG_SC_USER_INFO
*/

//{{COMPONENT_RULES_CLASS_DECL
public class INTG_SC_USER_INFOImpl extends  INTG_SC_USER_INFOBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
// Actions
   protected static final int INSERT_ACTION = 0;
   protected static final int UPDATE_ACTION = 1;
   protected static final int DELETE_ACTION = 2;
//{{COMP_CLASS_CTOR
public INTG_SC_USER_INFOImpl (){
	super();
}

public INTG_SC_USER_INFOImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}
   }

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static INTG_SC_USER_INFOImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new INTG_SC_USER_INFOImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * Called by User Information table to offer the user information update to integration.
    * @param foSession - The current session.
    * @param foUserInfo -  The user information data object being maintained.
    * @param fiAction - The action performed (0=insert, 1=update or 2=delete)
    */
   protected static void updateSecurityUserInformation(
         Session foSession,
         R_SC_USER_INFOImpl foUserInfo,
         int fiAction )
   {
      // nothing happens until integration is enabled
      if ( !isIntegrationEnabled( foSession ) )
      {
         return;
      }

      SearchRequest loSearchReq = new SearchRequest() ;
      loSearchReq.addParameter(
            "R_SC_USER_DIR_INFO",
            "USER_ID",
            foUserInfo.getUSER_ID() ) ;
      R_SC_USER_DIR_INFOImpl loUserDirInfo
            = (R_SC_USER_DIR_INFOImpl) R_SC_USER_DIR_INFOImpl.getObjectByKey(
               loSearchReq, foSession ) ;
      updateIntgSecurityUserTable(
            foSession,
            foUserInfo,
            loUserDirInfo,
            fiAction ) ;
   }

   /**
    * Called by User Directory Information table to offer the User Directory information update to integration.
    * @param foSession - The current session.
    * @param foUserDirInfo - The user directory information data object being maintained.
    * @param fiAction - The action performed (0=insert, 1=update or 2=delete)
    */
   protected static void updateSecurityUserDirectoryInformation(
         Session foSession,
         R_SC_USER_DIR_INFOImpl foUserDirInfo,
         int fiAction )
   {
      // nothing happens until integration is enabled
      if ( !isIntegrationEnabled( foSession ) )
      {
         return;
      }

      R_SC_USER_INFOImpl loUserInfo
            = R_SC_USER_INFOImpl.getUserInfoRec( foSession, foUserDirInfo.getUSER_ID() );
      updateIntgSecurityUserTable(
            foSession,
            loUserInfo,
            foUserDirInfo,
            fiAction ) ;
   }

   /**
    * Query the Audit Log Control table to see if external notification is turned on for the
    * Integration Security User table.
    * @return boolean true if external notification is turned on, false otherwise.
    */
   private static boolean isIntegrationEnabled( Session foSession )
   {
      SearchRequest loSearchReq = new SearchRequest() ;

      loSearchReq.addParameter( "IN_AUD_LOG_CTL", "PROC_RSRC_NM", "INTG_SC_USER_INFO" ) ;
      loSearchReq.addParameter( "IN_AUD_LOG_CTL", "EXTRNL_NOTIFCN_FL", "true" ) ;

      int liAudLogCtl = IN_AUD_LOG_CTLImpl.getObjectCount( loSearchReq, foSession ) ;

      if ( liAudLogCtl != 0 )
      {
         moAMSLog.debug("INTG_SC_USER_INFO.isIntegrationEnabled() returns true");
         return true;
      }

      moAMSLog.debug("INTG_SC_USER_INFO.isIntegrationEnabled() returns false");
      return false;
   }

   /**
    * Returns the Integration User Information record with specified User ID.
    * @param foSession - The current session
    * @param fsUserID - The User ID
    * @return INTG_SC_USER_INFOImpl Integration User Information reccord with specified USER_ID.Null if no matching one found.
    */
   protected static INTG_SC_USER_INFOImpl getIntegrationSecurityUserInfo( Session foSession, String fsUserID )
   {
      if (AMSStringUtil.strIsEmpty(fsUserID))
      {
         return null;
      }
      SearchRequest loSearchReq = new SearchRequest() ;
      loSearchReq.addParameter( "INTG_SC_USER_INFO", "USER_ID", fsUserID ) ;
      return (INTG_SC_USER_INFOImpl) INTG_SC_USER_INFOImpl.getObjectByKey( loSearchReq, foSession ) ;
   }

   /**
    * Update the Integration Security User Table based to reflect a change in User Information, User Directory Information.
    * @param foSession - The current session
    * @param foUserInfo - The security User Information record
    * @param foUserDirInfo - The security User Directory Information record
    * @param fiAction - The action performed on the user (0=insert, 1=update or 2=delete)
    */
   private static void updateIntgSecurityUserTable(
         Session foSession,
         R_SC_USER_INFOImpl foUserInfo,
         R_SC_USER_DIR_INFOImpl foUserDirInfo,
         int fiAction )
   {
      SearchRequest loSearchReq = new SearchRequest() ;
      if ( foUserInfo == null )
      {
         return ;
      }

      if ( moAMSLog.isDebugEnabled() )
      {
         moAMSLog.debug("INTG_SC_USER_INFOImpl.updateIntgSecurityUserTable(), action: "
               + fiAction
               + " for USER_ID: " + foUserInfo.getUSER_ID() ) ;
      }

      INTG_SC_USER_INFOImpl loIntgUserInfo
            = getIntegrationSecurityUserInfo( foSession, foUserInfo.getUSER_ID() ) ;

      if ( loIntgUserInfo == null )
      {
         moAMSLog.debug("INTG_SC_USER_INFOImpl.updateIntgSecurityUserTable(), USER_ID not found" ) ;
         switch ( fiAction )
         {
            case DELETE_ACTION:
            {
               moAMSLog.debug("INTG_SC_USER_INFOImpl.updateIntgSecurityUserTable(), action is Delete, so do nothing");

               //already not in integration, no action required
               return ;
            }
            case INSERT_ACTION:
            case UPDATE_ACTION:
            {
               // Do not place into integration if User is not active
               if ( !( foUserInfo.getLOCK_FL() == CVL_USER_LOCK_STAImpl.ACTIVE ) )
               {
                  return ;
               }
               // get an iterator of the User's security roles that are flagged for integration
               Iterator loRolesForUser
                     = R_SC_SEC_ROLEImpl.getRolesForUser(
                             foSession,
                             foUserInfo.getUSER_ID(),
                             true ) ;
               // Do not place User into integration if User is not associated
               // with at least one Security Role that is flagged for integration
               if ( !loRolesForUser.hasNext() )
               {
                  return ;
               }
               // place the User into integration
               loIntgUserInfo = INTG_SC_USER_INFOImpl.getNewObject( foSession, true );
               loIntgUserInfo.setUSER_ID( foUserInfo.getUSER_ID() ) ;
               loIntgUserInfo.setLOCK_FL( foUserInfo.getLOCK_FL() ) ;
               if ( foUserDirInfo != null )
               {
                  loIntgUserInfo.setUSER_NM( foUserDirInfo.getUSER_NM() ) ;
                  loIntgUserInfo.setEMAIL_AD_TXT( foUserDirInfo.getEMAIL_AD_TXT() ) ;
               }
                loIntgUserInfo.save() ;
                // place the integrated Roles into integration
                R_SC_SEC_ROLEImpl loSecRole = null ;
                INTG_SC_USER_ROLEImpl loIntgUserSecRole = null ;
                while ( loRolesForUser.hasNext() )
                {
                   loSecRole = ( R_SC_SEC_ROLEImpl ) loRolesForUser.next() ;
                   loIntgUserSecRole = INTG_SC_USER_ROLEImpl.getNewObject( foSession, true );
                   loIntgUserSecRole.setUSER_ID( foUserInfo.getUSER_ID() ) ;
                   loIntgUserSecRole.setSEC_ROLE_ID( loSecRole.getSEC_ROLE_ID() ) ;
                   loIntgUserSecRole.save() ;
                }
            }
         }
      } // end if ( loIntgUserInfo == null )
      else //loIntgUserInfo is not null
      {
         switch ( fiAction )
         {
            case DELETE_ACTION:
            {
               moAMSLog.debug("INTG_SC_USER_INFOImpl.updateIntgSecurityUserTable(), action is Delete, remove from integration");

               loIntgUserInfo.setDelete() ;
               loIntgUserInfo.save() ;
               INTG_SC_USER_ROLEImpl loIntgUserSecRole = null ;
               loSearchReq = new SearchRequest();
               loSearchReq.addParameter( "INTG_SC_USER_ROLE", "USER_ID", foUserInfo.getUSER_ID() ) ;
               Enumeration loRolesForUser = loIntgUserSecRole.getObjects( loSearchReq, foSession ) ;
               while ( loRolesForUser.hasMoreElements() )
               {
                  loIntgUserSecRole = ( INTG_SC_USER_ROLEImpl ) loRolesForUser.nextElement() ;
                  loIntgUserSecRole.setDelete() ;
                   loIntgUserSecRole.save() ;
               }
               return ;
            }
            case INSERT_ACTION:
            case UPDATE_ACTION:
            {
               // Remove the User from integration if the User is not active
               if ( !( foUserInfo.getLOCK_FL() == CVL_USER_LOCK_STAImpl.ACTIVE ) )
               {
                  loIntgUserInfo.setDelete() ;
                  loIntgUserInfo.save() ;
                  loSearchReq = new SearchRequest();
                  INTG_SC_USER_ROLEImpl loIntgUserSecRole = null ;
                  loSearchReq.addParameter( "INTG_SC_USER_ROLE", "USER_ID", foUserInfo.getUSER_ID() ) ;
                  Enumeration loRolesForUser = loIntgUserSecRole.getObjects( loSearchReq, foSession ) ;

                  while ( loRolesForUser.hasMoreElements() )
                  {
                     loIntgUserSecRole = ( INTG_SC_USER_ROLEImpl ) loRolesForUser.nextElement() ;
                     loIntgUserSecRole.setDelete() ;
                     loIntgUserSecRole.save() ;
                  }
                  return ;
                }
                //update integration User record
                loIntgUserInfo.setLOCK_FL( foUserInfo.getLOCK_FL() ) ;
                if ( foUserDirInfo != null )
                {
                   loIntgUserInfo.setUSER_NM( foUserDirInfo.getUSER_NM() ) ;
                   loIntgUserInfo.setEMAIL_AD_TXT( foUserDirInfo.getEMAIL_AD_TXT() ) ;
                }
                loIntgUserInfo.save() ;
                break;
            }
            default: //undefined action
            {
               return;
            }
         }
      }
   }
}

