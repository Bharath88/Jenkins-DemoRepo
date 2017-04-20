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
import com.ams.csf.base.*;
import com.ams.csf.auth.*;
import com.ams.csf.common.*;
import java.util.ArrayList;

/*
**  R_SC_USER_SEC_GP_LNK
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_USER_ROLE_LNKImpl extends  R_SC_USER_ROLE_LNKBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /**
    * Used to store existing value of Session property maintenanceString
    */
   private String msExistingMaintStr = null;
//{{COMP_CLASS_CTOR
public R_SC_USER_ROLE_LNKImpl (){
	super();
}

public R_SC_USER_ROLE_LNKImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_SC_USER_ROLE_LNKImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_SC_USER_ROLE_LNKImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   String lsRoleID = getSEC_ROLE_ID();
   //Write Event Code below this line
   if ( isRoleValid( obj ) != true )
   {
      raiseException( "Security role not valid for the user's department" ) ;
   }

   /*
    * If the user is being assigned either the Admin role or user
    * management role then we will have to store the encrypted
    * common admin password for the user
    */
   if ( lsRoleID != null &&
         (lsRoleID.equals(AMSSecurity.ADMIN_SEC_ROLE) ||
          AMSSecurity.roleEquals(lsRoleID, AMSParams.msUserMgmtRoles) ||
          lsRoleID.equals(AMSBatchConstants.JOB_CTRL_ROLE)))
   {
      checkUserAuthorization(lsRoleID);
   } /* if ( lsRoleID != null && */
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
   String lsRoleID = getSEC_ROLE_ID();
   //Write Event Code below this line
   if ( isRoleValid( obj ) != true )
   {
     raiseException( "Security role not valid for the user's department" ) ;
   }

   if ( lsRoleID != null && getData( "SEC_ROLE_ID" ).modified() &&
         (lsRoleID.equals(AMSSecurity.ADMIN_SEC_ROLE) ||
          AMSSecurity.roleEquals(lsRoleID, AMSParams.msUserMgmtRoles) ||
          lsRoleID.equals(AMSBatchConstants.JOB_CTRL_ROLE)))
   {
      checkUserAuthorization(lsRoleID);
   } /* if ( lsRoleID != null && getData( "SEC_ROLE_ID" ).modified() ...*/
}
//END_COMP_EVENT_beforeUpdate}}

//{{COMP_EVENT_afterCommit

public void afterCommit(Session session)
//@SuppressAbstract
{
   AMSSecurity.removeUserCache( getUSER_ID() ) ;
   super.afterCommit(session);
}
//END_COMP_EVENT_afterCommit}}

//{{COMP_EVENT_beforeDelete
public void beforeDelete(DataObject obj, Response response)
{
   /*
    * If the user does not belong to either the Admin role or
    * user management role blank out the CA password
    */
   String lsDelRoleID = getSEC_ROLE_ID();
   String lsOtherRoleID = null;
   boolean lboolBlankCAPassword = false;

   if ( lsDelRoleID.equals(AMSSecurity.ADMIN_SEC_ROLE) )
   {
      lsOtherRoleID = "SEC_ROLE_ID IN (" +
         getANSIQuotedRoles(AMSParams.msUserMgmtRoles) +
         ", " +
         AMSSQLUtil.getANSIQuotedStr(AMSBatchConstants.JOB_CTRL_ROLE, true) +
         ")";
      lboolBlankCAPassword = true;
   } /* end if (lsDelRoleID.equals(AMSSecurity.ADMIN_SEC_ROLE)) */
   else if ( AMSSecurity.roleEquals(lsDelRoleID, AMSParams.msUserMgmtRoles) )
   {
      lsOtherRoleID = "SEC_ROLE_ID IN (" +
         AMSSQLUtil.getANSIQuotedStr(AMSSecurity.ADMIN_SEC_ROLE, true) +
         ", " +
         AMSSQLUtil.getANSIQuotedStr(AMSBatchConstants.JOB_CTRL_ROLE, true) +
         ")";
      lboolBlankCAPassword = true;
   } /* end else if ( AMSSecurity.roleEquals(lsDelRoleID, AMSParams.msUserMgmtRoles) ) */
   else if ( lsDelRoleID.equals(AMSBatchConstants.JOB_CTRL_ROLE) )
   {
      lsOtherRoleID = "SEC_ROLE_ID IN (" +
         AMSSQLUtil.getANSIQuotedStr(AMSSecurity.ADMIN_SEC_ROLE, true) +
         ", " +
         getANSIQuotedRoles(AMSParams.msUserMgmtRoles) +
         ")";
      lboolBlankCAPassword = true;
   } /* end else if ( lsDelRoleID.equals(AMSBatchConstants.JOB_CTRL_ROLE) ) */

   if ( lsOtherRoleID != null )
   {
      SearchRequest loSR = new SearchRequest() ;
      int liRoleNum;

      loSR.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID",
                         getData( "USER_ID" ).getString() ) ;

      loSR.add( lsOtherRoleID ) ;

      liRoleNum = R_SC_USER_ROLE_LNKImpl.getObjectCount( loSR,
                                                     getSession() ) ;
      if (liRoleNum == 1)
      {
         lboolBlankCAPassword = false;
      } /* end if (liRoleNum == 1) */
   } /* end if (lsOtherRoleID != null) */

   if (lboolBlankCAPassword)
   {
      R_SC_USER_INFOImpl loUserInfo = getUserInfo();

      // Lookup R_SC_USER_INFO and blank out the CA password
      if ( loUserInfo != null )
      {
         loUserInfo.setCA_PSWD_TXT(null);
         loUserInfo.save();
      } /* end if ( loUserInfo != null ) */
   } /* end if (lboolBlankCAPassword) */
}
//END_COMP_EVENT_beforeDelete}}

//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
   //Write Event Code below this line

   //update INTG_SC_USER_ROLE table to integrate with infoAdvantage or other security
   INTG_SC_USER_ROLEImpl.updateSecurityUserRole( getSession(), this, INTG_SC_USER_ROLEImpl.INSERT_ACTION ) ;

}
//END_COMP_EVENT_afterInsert}}

//{{COMP_EVENT_afterUpdate
public void afterUpdate(DataObject obj)
{
   //Write Event Code below this line

   //update INTG_SC_USER_ROLE table to integrate with infoAdvantage or other security
   INTG_SC_USER_ROLEImpl.updateSecurityUserRole( getSession(), this, INTG_SC_USER_ROLEImpl.UPDATE_ACTION ) ;

}
//END_COMP_EVENT_afterUpdate}}

//{{COMP_EVENT_afterDelete
public void afterDelete(DataObject obj)
{
   //Write Event Code below this line
   /* Restore the original value of Session property maintenanceString.
      Refer documentation on method checkForeignOrgInDB for more details.
   */
   Session loSession = getSession();
   loSession.setProperty( MAINTENANCE_STR, msExistingMaintStr) ;
   //update INTG_SC_USER_ROLE table to integrate with infoAdvantage or other security
   INTG_SC_USER_ROLEImpl.updateSecurityUserRole( getSession(), this, INTG_SC_USER_ROLEImpl.DELETE_ACTION ) ;
}
//END_COMP_EVENT_afterDelete}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
	addTransactionEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

   /**
    * Checks if a User has any Security Roles that is defined
    * as restricted on Restricted Security Role Pair table. If yes, logs error.
    */

   protected void validateSecurityRolesForUser()
   {
      // Get a collection of Security Role IDs belonging to a given User.
      ArrayList<String> loListRoles = getSecRoleForUserRoleLnk();

      //Checks for all possible combinations of given Security Role with the Arraylist of Security Roles
      //on Restricted Role Pairs table and returns true if any match found for a combination.
      if( R_SC_SEC_ROLEPAIRSImpl.isRoleRestricted( getSession(), getSEC_ROLE_ID(), loListRoles ) )
      {
         raiseException( "%c:Q0194,v:" + getSEC_ROLE_ID() + "%" );
      }

   }

   /**
    * Returns a collection of Security Role IDs for given User.
    * If no such Security Roles are found then returns empty ArrayList.
    * @return ArrayList of Security Role IDs
    */

   public ArrayList getSecRoleForUserRoleLnk( )
   {
      SearchRequest loSrchReq = new SearchRequest();

      // Search for the records present in User Role Link for a given User ID
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", getUSER_ID() );
      R_SC_USER_ROLE_LNKImpl loUserRoleLnk = null;
      ArrayList<String> loListRoles = new ArrayList<String>();

      Enumeration lenumRoleList = getObjects( loSrchReq, getSession() );

      // Loop through the Enumeration.
      while( lenumRoleList.hasMoreElements() )
      {
         loUserRoleLnk = ( R_SC_USER_ROLE_LNKImpl )lenumRoleList.nextElement();
         loListRoles.add( loUserRoleLnk.getSEC_ROLE_ID() );
      }/* whole Enumeration of User records is traversed */
      return loListRoles;
   }

   /**
    * constraint to confirm that there are valid sec-roles for
    * the user's department
    *
    * @return boolean - true if validation OK, else false
    *
    * @author - Satish Mokkapati
    */
    public boolean isRoleValid( Object obj )
    {
            // get the userid and security role from the current row
      String lsUser = ((R_SC_USER_ROLE_LNKImpl)obj).getUSER_ID() ;
      String lsRole = ((R_SC_USER_ROLE_LNKImpl)obj).getSEC_ROLE_ID() ;
      String lsDeptCode = null ;
            int liValidCnt = 0 ;
      boolean lbFound = false ;
      String lsValidDeptCode ;

      // get the user's department
      R_SC_USER_INFOImpl parent = null;

      SearchRequest searchReq = new SearchRequest();
      Parameter param = new Parameter();
      param.objName = "R_SC_USER_INFO";
      param.fieldName = "USER_ID";
      param.value   = lsUser ;
      searchReq.add(param);

      parent = (R_SC_USER_INFOImpl)(R_SC_USER_INFOImpl.getObjectByKey(searchReq ,getSession()));
      if ( parent != null )
      {
         lsDeptCode = parent.getHOME_DEPT_CD() ;
         if ( lsDeptCode != null )
         {
            lsDeptCode = lsDeptCode.trim() ;
         }
         else
         {
            lsDeptCode = "" ;
         }

         // verify if a record exists on the valid roles table
         // with the security role and dept. code
         searchReq = new SearchRequest();
         param = new Parameter();
         param.objName = "R_SC_VALID_ROLES";
         param.fieldName = "SEC_ROLE_ID";
         param.value   = lsRole ;
         searchReq.add(param);

         Enumeration eValidRows = R_SC_VALID_ROLESImpl.getObjects(searchReq ,getSession());
         R_SC_VALID_ROLESImpl loValidRole = null ;
         while ( eValidRows.hasMoreElements() )
         {
            loValidRole = (R_SC_VALID_ROLESImpl)(eValidRows.nextElement()) ;
            if ( loValidRole != null )
            {
               liValidCnt++ ;
               lsValidDeptCode = loValidRole.getDEPT_CD() ;
               lsValidDeptCode = (lsValidDeptCode == null ? "" : lsValidDeptCode.trim());
               if ( lsValidDeptCode.equals( lsDeptCode ) )
               {
                   lbFound = true ;
                   break ;
               }
            }
         } // end while enumeration

         // if no record, return true
         if ( ( liValidCnt == 0 ) || ( lbFound == true ) )
         {
            return true ;
         }

      } // end if parent != null
      return false ;
 }

   public static void beforeResultSetFillBeforeSecurityCheck(
                           DataRow rowToBeAdded,
                           Response response )
   {
      /**
       * Make sure that the virtual fields get calculated
       */
      ((R_SC_USER_ROLE_LNKImpl)rowToBeAdded.getComponent()).initVirtualOrgFields() ;
   }


   private void initVirtualOrgFields()
   {
      getGOVT_BRN_CD() ;
      getCAB_CD() ;
      getDEPT_CD() ;
      getDIV_CD() ;
      getGP_CD() ;
      getSECT_CD() ;
      getDSTC_CD() ;
      getBUR_CD() ;
      getUNIT_CD() ;
   }

   /**
    * This method is used to set the common admin password
    * for the user in the user information table
    *
    * @param current role that is being assigned
    */
   private void checkUserAuthorization(String fsRoleID)
   {
      AMSUser loADMNUser = AMSSecurity.getUser(getSession().getUserID());
      Enumeration leSecGrps;
      String lsTempRole;
      boolean lboolAuthorized = false;

      // Check if the user doing the assignment belongs to the role
      // that is being assigned or higher
      leSecGrps = loADMNUser.getSecurityGroups().elements();

      while ( leSecGrps.hasMoreElements() )
      {
         lsTempRole = (String)leSecGrps.nextElement() ;

         if (lsTempRole.equals(fsRoleID) ||
             lsTempRole.equals(AMSSecurity.ADMIN_SEC_ROLE))
         {
            lboolAuthorized = true;
            break;
         } /* if (lsTempRole.equals(lsRoleID) || ... */
      } /* end while ( leSecGrps.hasMoreElements() ) */

      if (!lboolAuthorized)
      {
         raiseException("Not authorized to assign the " + fsRoleID + " role");
         return;
      } /* end if (!lboolAuthorized) */
   } /* end checkUserAuthorization */

   /**
    * This method returns the user information record for the
    * current user
    *
    * @return R_SC_USER_INFOImpl
    */
   private R_SC_USER_INFOImpl getUserInfo()
   {
      SearchRequest loSR = new SearchRequest() ;
      loSR.addParameter( "R_SC_USER_INFO", "USER_ID", getUSER_ID() ) ;

      return (R_SC_USER_INFOImpl)
                     R_SC_USER_INFOImpl.getObjectByKey( loSR,
                                                     getSession() ) ;
   } /* end getUserInfo */

   /**
    * Should be only called during delete of this dataobject(and just before
    * Versata relationship check method childCascadeFor_R_SC_FGN_ORG).
    * Sets member variable msExistingMaintStr with the existing value of
    * Session property maintenanceString. Then sets Session property maintenanceString
    * to Y so that check for existing Foreign Organization records(children by
    * Versata relationship) for the User and the Role should go against database
    * and not against MRT. Method afterDelete will take care of restoring the value
    * to the original value.
    */
    /* The reason for the modification introduced in this method is as follows:
    The default behaviour of this dataobject is that when it is being deleted
    it will check if any Foreign Organization records exist for the User and
    the Role because of enforced Versata relationship. (3.7)When Administrator tries
    to un-assign a Role from an User for which Foreign Organization records exists,
    then he/she gets error saying that Foreign Org records exist(if they exist).
    The Administrator then manually deletes the Foreign Organization records for
    the User and the Role and and tries the un-assignment again. This time again
    he/she gets the same error. The problem is because Foreign Organization records
    are maintained on MRT and the records deleted still stays on MRT. The relationship
    check was checking for the existence of Foreign org records on MRT. However, few
    clients want the system to allow un-assignment on the second attempt(after Foreign
    Org records were deleted, for Foreign org deletion they would regenerate the MRT
    and bounce the vls at later part of the day). Hence in order to do so, this method
    sets Session property maintenanceString to Y so that relationship check for existence
    of Foreign Org records happens against database and not against MRT. The timeframe
    for which this Session property is set to Y and then reset back, should be short
    and narrow enough for just relationship check for Foreign Org to happen(hence
    conditional action is used to set the Session property because they trigger just
    before relationship check).
    Technical Note: childCascadeFor_R_SC_FGN_ORG on R_SC_USER_ROLE_LNKBaseImpl does
    the check, it calls R_SC_FGN_ORGBaseImpl.getObjects that uses
    createXDAConnector(<Session>) which uses MRT depending on certain conditions.
   */
   protected void checkForeignOrgInDB()
   {
      Session loSession = getSession();
      //Store existing value of Session property maintenanceString
      msExistingMaintStr = loSession.getProperty(MAINTENANCE_STR);
      //Set Session property maintenanceString to Y
      loSession.setProperty( MAINTENANCE_STR, SESSION_DOC_YES );
   }//end of method
   /**
    * This method returns the list of ANSI Quoted user management roles
    *
    * @param fsUserMgmtRoles List of user management roles
    * @return String ANSI Quoted user management roles
    */
   private String getANSIQuotedRoles( String[] fsUserMgmtRoles )
   {
      String lsANSIQuotedRoles = null;
      String lsANSIQuotedRole  = null;

      for ( int liArrayIndex = 0; liArrayIndex < fsUserMgmtRoles.length; liArrayIndex++ )
      {
         lsANSIQuotedRole = AMSSQLUtil.getANSIQuotedStr(fsUserMgmtRoles[liArrayIndex], true);
         if ( lsANSIQuotedRoles == null )
         {
            lsANSIQuotedRoles = lsANSIQuotedRole;
         }
         else
         {
            lsANSIQuotedRoles = lsANSIQuotedRoles + ", " + lsANSIQuotedRole;
         }
      } /* end for */

      return lsANSIQuotedRoles;
   } /* end getANSIQuotedRoles() */


   /*****************************************************************************
    * Set of methods that provide implementation for AdvIHistTracking methods.
    * These methods are only relevant to DataObject which is involved in Historical Tracking.
    * A DataObject participates in Historical Tracking when Extended property HIST_TRACKING
    * is set to Y(case insensitive).
    ******************************************************************************/

   /**
    * Since this DataObject is involved in Historical Tracking, this method returns the
    * Entity for which tracking has to be performed.
    * Here, the Entity Code will be User ID to whom a Security Role is being assigned or revoked.
    */
   public ArrayList<String> getTrackedEntity()
   {
      ArrayList<String> loUser = new ArrayList<String>();
      loUser.add(getUSER_ID());
      return loUser;
   }

   /**
    * Since this DataObject is involved in Historical Tracking, this method returns the
    * Resource whose assignment/ revocation has to be tracked.
    * Here, Resource will be Security Role ID which is assigned/ revoked to a User.
    */
   public String getTrackedResource()
   {
      return getSEC_ROLE_ID();
   }

   public String getTrackedDeptCD(String fsParam)
   {
      return getDEPT_CD();
   }
   /*****************************************************************************
    * END of methods that provide implementation for AdvIHistTracking methods.
    ******************************************************************************/

   /**
    * This method will return an instance of User Role Link
    * record for the given User ID and Security Role ID.
    * It will return null when passed User ID and Security Role ID
    * is an invalid combination that could not be found on User
    * Role Link.
    * @param foSession - The current session
    * @param fsUserID - User ID for lookup
    * @param fsSecurityRoleID - Security Role ID for lookup
    * @return R_SC_USER_ROLE_LNKImpl
    */
   public static R_SC_USER_ROLE_LNKImpl getUserRoleLinkRec(
               Session foSession, String fsUserID, String fsSecurityRoleID )
   {
      if ( fsUserID == null || fsSecurityRoleID == null )
      {
         return null ;
      }
      SearchRequest loSearchRequest = new SearchRequest() ;
      loSearchRequest.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", fsUserID ) ;
      loSearchRequest.addParameter( "R_SC_USER_ROLE_LNK", "SEC_ROLE_ID", fsSecurityRoleID ) ;
      return( ( R_SC_USER_ROLE_LNKImpl )R_SC_USER_ROLE_LNKImpl.getObjectByKey( loSearchRequest, foSession ) );
   }
}
