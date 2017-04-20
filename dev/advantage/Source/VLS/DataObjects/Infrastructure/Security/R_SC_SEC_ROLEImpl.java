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
**  R_SC_SEC_GP
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_SEC_ROLEImpl extends  R_SC_SEC_ROLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_SEC_ROLEImpl (){
		super();
	}
	
	public R_SC_SEC_ROLEImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_SC_SEC_ROLEImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_SEC_ROLEImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}



   /**
    * This method will return an instance of Security role
    * record(R_SC_SEC_ROLEImpl) for the given Security Role ID.
    * It will return null when passed Security role ID is invalid and
    * could not be found on Security roles (R_SC_SEC_ROLE).
    * @param foSession - Session of calling program
    * @param fsSecRoleID - Security Role ID for lookup
    * @return R_SC_SEC_ROLEImpl
    */
   public static R_SC_SEC_ROLEImpl getSecRoleRec( Session foSession,
      String fsSecRoleID )
   {
      if( fsSecRoleID == null )
      {
         return null;
      }
      SearchRequest loSearchRequest = new SearchRequest();
      loSearchRequest.addParameter("R_SC_SEC_ROLE","SEC_ROLE_ID",fsSecRoleID);
      return( (R_SC_SEC_ROLEImpl)R_SC_SEC_ROLEImpl.getObjectByKey(
            loSearchRequest, foSession) );
   }/* end of getSecRoleRec(..) */


    /**
    * This method will return Override Error level for the given
    * Security Role ID. It will return 0 when (passed Security role ID
    * is invalid and could not be found on Security roles (R_SC_SEC_ROLE) )
    * or Override Error level for the given Security Role is null.
    * @param foSession - Session of calling program
    * @param fsSecRoleID - Security Role ID for lookup
    * @return int
    */
    public static int getOverrideLvlForSecRole( Session foSession,
       String fsSecRoleID )
    {
       /* Get Security Role record for given Role ID */
       R_SC_SEC_ROLEImpl loSecRoleRec = R_SC_SEC_ROLEImpl.getSecRoleRec(foSession,
             fsSecRoleID);

       if( loSecRoleRec != null )
       {
          return loSecRoleRec.getOV_ERR_CD();
       }
       return 0;
    }/* end of getOverrideLvlForSecRole(..) */

    /**
    * When the field 'Foreign Organization Restriction' is changed
    * on Security Roles then this method checks to see if there are invalid
    * records found on the Foreign Organization table for Security Role ID
    * record being modified.
    * Error is raised when any of the following condition is satisfied.
    * <ul>
    * <li> Foreign Organization Restriction is being changed to None and
    *      Foreign Organization table has records for the given Security Role ID
    *      with (User ID set to any value other than ALL
    *      Or Home Department set to any value other than ALL).
    * <li>
    * <li> Foreign Organization Restriction is being changed to UserID Required and
    *      Foreign Organization table has records for the given Security Role ID
    *      with (User ID set to any value other than ALL
    *      Or Home Department set to any value other than ALL).
    * <li>
    * <li> Foreign Organization Restriction is being changed to Home Dept Required and
    *      Foreign Organization table has records for the given Security Role ID
    *      with (User ID set to any value other than ALL
    *      Or Home Department set to  ALL).
    * <li>
    * <li> Foreign Organization Restriction is being changed to UserID or
    *      Home Dept Required and Foreign Organization table has records for
    *      the given Security Role ID with (User ID set to ALL
    *      And Home Department set to ALL).
    * <li>
    *
    */
    protected void chkForInvalidForeignOrgRecords()
    {
       int liFgnOrg = getFGN_ORG_RSTR();
       StringBuffer loWhereClause = new StringBuffer(64);
       SearchRequest loRScFgnOrgSR = new SearchRequest();

       loRScFgnOrgSR.addParameter("R_SC_FGN_ORG","SEC_ROLE_ID",getSEC_ROLE_ID());

       if(liFgnOrg== CVL_FGN_ORG_CNFGImpl.NONE)
       {
          loWhereClause.append("(USER_ID <> '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("' OR HOME_DEPT_CD <> '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("')");
          loRScFgnOrgSR.add(loWhereClause.toString());
          if(getForeignOrgRecs(loRScFgnOrgSR))
          {
             raiseException("%c:Q0122%");
          }
       }
       else if(liFgnOrg== CVL_FGN_ORG_CNFGImpl.USER_ID_REQUIRED)
       {
          loWhereClause.append("(USER_ID = '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("' OR HOME_DEPT_CD <> '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("')");
          loRScFgnOrgSR.add(loWhereClause.toString());
          if(getForeignOrgRecs(loRScFgnOrgSR))
          {
             raiseException("%c:Q0123%");
          }
       }
       else if(liFgnOrg== CVL_FGN_ORG_CNFGImpl.HOME_DEPT_REQUIRED)
       {
          loWhereClause.append("(USER_ID <> '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("' OR HOME_DEPT_CD = '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("')");
          loRScFgnOrgSR.add(loWhereClause.toString());
          if(getForeignOrgRecs(loRScFgnOrgSR))
          {
             raiseException("%c:Q0158%");
          }
       }
       else if(liFgnOrg== CVL_FGN_ORG_CNFGImpl.USERID_OR_HOMEDEPT_REQUIRED)
       {
          loWhereClause.append("USER_ID = '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("' AND HOME_DEPT_CD = '");
          loWhereClause.append( AMSCommonConstants.ALL );
          loWhereClause.append("'");
          loRScFgnOrgSR.add(loWhereClause.toString());
          if(getForeignOrgRecs(loRScFgnOrgSR))
          {
             raiseException("%c:Q0159%");
          }
       }
    }

   protected void updateIntegrationInfo()
   {
      Session loSession = getSession();
      String lsSecRole = getSEC_ROLE_ID();
      String lsUserId = null;
      Iterator loUsersForRole = R_SC_USER_INFOImpl.getUsersForRole( loSession, lsSecRole, true); ;
      R_SC_USER_ROLE_LNKImpl loUserRoleLink;
      if( getINTG_SEC_ROLE_FL() == false )
      {
         while(loUsersForRole.hasNext())
         {
            lsUserId = ((R_SC_USER_INFOImpl)loUsersForRole.next()).getUSER_ID();
            loUserRoleLink = R_SC_USER_ROLE_LNKImpl.getUserRoleLinkRec( loSession, lsUserId, lsSecRole );
            INTG_SC_USER_ROLEImpl.updateSecurityUserRole( loSession, loUserRoleLink, INTG_SC_USER_ROLEImpl.DELETE_ACTION );
         }
      }
      else if( getINTG_SEC_ROLE_FL() == true )
      {
         while(loUsersForRole.hasNext())
         {
            lsUserId = ((R_SC_USER_INFOImpl)loUsersForRole.next()).getUSER_ID();
            R_SC_USER_INFOImpl loUserInformation = R_SC_USER_INFOImpl.getUserInfoRec(loSession, lsUserId);
            loUserRoleLink = R_SC_USER_ROLE_LNKImpl.getUserRoleLinkRec( loSession, lsUserId, lsSecRole );
            INTG_SC_USER_INFOImpl.updateSecurityUserInformation( loSession, loUserInformation, INTG_SC_USER_INFOImpl.INSERT_ACTION );
            INTG_SC_USER_ROLEImpl.updateSecurityUserRole( loSession, loUserRoleLink, INTG_SC_USER_ROLEImpl.INSERT_ACTION );
         }
      }
   }
   /**
   * This method checks to see if there are  records from R_SC_FGN_ORG  table
   * based on the search request passed to it.
   * @param loRScFgnOrgSR Search Request
   * @return boolean
   */
   private boolean getForeignOrgRecs(SearchRequest loRScFgnOrgSR)
   {
      int liInvalidRecCount =
            R_SC_FGN_ORGImpl.getObjectCount(loRScFgnOrgSR,this.getSession());
      return ((liInvalidRecCount > 0) ? true : false);
   }
   /**
    * This method returns Security Roles granted to the specificed User. The Security Roles can be optionally
    * limited to only those Users who have been marked for security integration.
    * @param foSession - The Current session
    * @param fsUserID - Users assigned with specified Security Roles
    * @param fboolLimitToSecurityIntegration - Indicates whether selection should be limited to Security Roles
    * marked for security integration
    * @return Iterator of Security Roles granted to the specificed User
    */
   protected static Iterator getRolesForUser( Session foSession, String fsUserID, boolean fboolLimitToSecurityIntegration  )
   {
      int liRecCount = 0;
      Vector lvRolesForUser = new Vector() ;
      R_SC_USER_ROLE_LNKImpl loUserRoleLink = null ;
      R_SC_SEC_ROLEImpl loSecRole = null ;
      SearchRequest loSearchReq = new SearchRequest();
      loSearchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", fsUserID ) ;
      Enumeration loRolesForUser = loUserRoleLink.getObjects( loSearchReq, foSession ) ;

      while ( loRolesForUser.hasMoreElements() )
      {
         loUserRoleLink
               = ( R_SC_USER_ROLE_LNKImpl ) loRolesForUser.nextElement() ;

         loSecRole
               = getSecRoleRec(
                       foSession,
                       loUserRoleLink.getSEC_ROLE_ID() ) ;

         if ( loSecRole != null
               && ( loSecRole.getINTG_SEC_ROLE_FL()
                         || !fboolLimitToSecurityIntegration
                  )
            )
         {
            lvRolesForUser.add( loSecRole ) ;
         }
      }
      return lvRolesForUser.iterator() ;
   }
}
