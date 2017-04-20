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
**  INTG_SC_USER_ROLE
*/

//{{COMPONENT_RULES_CLASS_DECL
public class INTG_SC_USER_ROLEImpl extends  INTG_SC_USER_ROLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
// Actions
   protected static final int INSERT_ACTION = 0;
   protected static final int UPDATE_ACTION = 1;
   protected static final int DELETE_ACTION = 2;
//{{COMP_CLASS_CTOR
public INTG_SC_USER_ROLEImpl (){
	super();
}

public INTG_SC_USER_ROLEImpl(Session session, boolean makeDefaults)
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
	public static INTG_SC_USER_ROLEImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new INTG_SC_USER_ROLEImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * Called by User Role Link table to offer the User Role update to integration.
    * @param foSession - The current session.
    * @param foUserRoleLink - The User Role data object being maintained
    * @param fiAction - The action performed (0=insert, 1=update or 2=delete)
    */
   protected static void updateSecurityUserRole(
         Session foSession,
         R_SC_USER_ROLE_LNKImpl foUserRoleLink,
         int fiAction  )
   {
      // nothing happens until integration is enabled
      if ( !isIntegrationEnabled( foSession ) )
      {
         return;
      }

      // get the user
         R_SC_USER_INFOImpl loUserInfo
            =  R_SC_USER_INFOImpl.getUserInfoRec(foSession, foUserRoleLink.getUSER_ID() ) ;

      // nothing happens if the user is not active
      if ( !( loUserInfo.getLOCK_FL() == CVL_USER_LOCK_STAImpl.ACTIVE ) )
      {
         return ;
      }

      // get the role
         R_SC_SEC_ROLEImpl loSecRole
            = R_SC_SEC_ROLEImpl.getSecRoleRec(foSession, foUserRoleLink.getSEC_ROLE_ID()) ;

      // maintain the integration table
      updateIntgSecurityUserRoleTable(
            foSession,
            foUserRoleLink,
            loUserInfo,
            loSecRole,
            fiAction ) ;
   }

   /**
    * Query the Audit Log Control table to see if external notification is turned on for the
    * Integration Security User Role table.
    * @return boolean true if external notification is turned on, false otherwise.
    */
   private static boolean isIntegrationEnabled( Session foSession )
   {
      SearchRequest loSearchReq = new SearchRequest() ;

      loSearchReq.addParameter( "IN_AUD_LOG_CTL", "PROC_RSRC_NM", "INTG_SC_USER_ROLE" ) ;
      loSearchReq.addParameter( "IN_AUD_LOG_CTL", "EXTRNL_NOTIFCN_FL", "true" ) ;
      int liAudLogCtl
         = IN_AUD_LOG_CTLImpl.getObjectCount( loSearchReq, foSession ) ;

      if ( liAudLogCtl != 0 )
      {
         moAMSLog.debug("INTG_SC_USER_INFO.isIntegrationEnabled() returns true");
         return true;
      }

      moAMSLog.debug("INTG_SC_USER_INFO.isIntegrationEnabled() returns false");
      return false;
   }

    /**
     * Update the Integration Security User Table based to reflect a change in User Role Link table.
     * @param foSession - The current session
     * @param foUserRoleLink - The User Role Link record
     * @param foUserInfo - The User Information record
     * @param foSecRole - The Security Role record
     * @param fiAction - The action performed on the user (0=insert, 1=update or 2=delete)
     */
   private static void updateIntgSecurityUserRoleTable(
         Session foSession,
         R_SC_USER_ROLE_LNKImpl foUserRoleLink,
         R_SC_USER_INFOImpl foUserInfo,
         R_SC_SEC_ROLEImpl foSecRole,
         int fiAction )
   {
      SearchRequest loSearchReq = new SearchRequest() ;

      INTG_SC_USER_ROLEImpl loIntgUserRole = null;

      if ( foUserRoleLink == null )
      {
         return ;
      }

      if ( moAMSLog.isDebugEnabled() )
      {
         moAMSLog.debug( "INTG_SC_USER_ROLEImpl.updateIntgSecurityUserRoleTable(), action: "
               + fiAction
               + " for USER_ID: " + foUserRoleLink.getUSER_ID()
               + " and SEC_ROLE_ID: " + foUserRoleLink.getSEC_ROLE_ID() ) ;
      }
      loIntgUserRole
            = getIntegrationSecurityUserRole(
              foSession,
              foUserRoleLink.getUSER_ID(),
              foUserRoleLink.getSEC_ROLE_ID() ) ;

      if ( loIntgUserRole == null )
      {
         moAMSLog.debug("INTG_SC_USER_ROLEImpl.updateIntgSecurityUserRoleTable(), USER_ID/SEC_ROLE_ID not found" ) ;
         switch ( fiAction )
         {
            case DELETE_ACTION:
            {
               if( moAMSLog.isDebugEnabled() )
               {
                  moAMSLog.debug("INTG_SC_USER_ROLEImpl.updateIntgSecurityUserRoleTable(), action is Delete, so do nothing");
               }
               return ;
            }
            case INSERT_ACTION:
            case UPDATE_ACTION:
            {
               if ( !foSecRole.getINTG_SEC_ROLE_FL() )
               {
                  moAMSLog.debug("INTG_SC_USER_ROLEImpl.updateIntgSecurityUserRoleTable(), action is insert/update for a non-intergrated role, so do nothing");
                  return ;
               }

               // if the User is not in integration, place the User in integration.
               INTG_SC_USER_INFOImpl loIntgUserInfo
                     = INTG_SC_USER_INFOImpl.getIntegrationSecurityUserInfo( foSession, foUserInfo.getUSER_ID()  ) ;

                if ( loIntgUserInfo == null )
                {
                   //update Integration Security User Information table to integrate with infoAdvantage or other security
                   INTG_SC_USER_INFOImpl.updateSecurityUserInformation( foSession, foUserInfo, INTG_SC_USER_INFOImpl.UPDATE_ACTION ) ;
                }
                else
                {
                   // place the User Role in integration
                   loIntgUserRole
                         = INTG_SC_USER_ROLEImpl.getNewObject( foSession, true );
                   loIntgUserRole.setUSER_ID( foUserInfo.getUSER_ID() ) ;
                   loIntgUserRole.setSEC_ROLE_ID( foSecRole.getSEC_ROLE_ID() ) ;
                   loIntgUserRole.save() ;
                }
            }
         }
      } // end if ( loIntgUserRole == null )
      else //loIntgUserRole is not null
      {
         switch ( fiAction )
         {
            case DELETE_ACTION:
            {
               moAMSLog.debug("INTG_SC_USER_INFOImpl.updateIntgSecurityUserRoleTable(), action is Delete, remove from integration");

               loIntgUserRole.setDelete() ;
               loIntgUserRole.save() ;
               return ;
            }
            case INSERT_ACTION:
            case UPDATE_ACTION:
            {
               return ;
            }
            default: //undefined action
            {
               return;
            }
         }
      }
   }

   /**
    * Returns the Integration User Role record with specified User ID and Security Role ID.
    * @param foSession - The current session
    * @param fsUserID - The User ID
    * @param fsSecurityRoleID - the Security Role ID
    * @return Integration User Role record with specified User ID and Security Role ID. Null if no matching one found.
    */
   protected static INTG_SC_USER_ROLEImpl getIntegrationSecurityUserRole(
         Session foSession,
         String fsUserID,
         String fsSecurityRoleID )
   {
      if ( AMSStringUtil.strIsEmpty(fsUserID)
                || AMSStringUtil.strIsEmpty(fsSecurityRoleID))
      {
         return null;
      }
      SearchRequest loSearchReq = new SearchRequest() ;
      loSearchReq.addParameter( "INTG_SC_USER_ROLE", "USER_ID", fsUserID ) ;
      loSearchReq.addParameter( "INTG_SC_USER_ROLE", "SEC_ROLE_ID", fsSecurityRoleID ) ;
      return (INTG_SC_USER_ROLEImpl) INTG_SC_USER_ROLEImpl.getObjectByKey( loSearchReq, foSession ) ;
   }
}

