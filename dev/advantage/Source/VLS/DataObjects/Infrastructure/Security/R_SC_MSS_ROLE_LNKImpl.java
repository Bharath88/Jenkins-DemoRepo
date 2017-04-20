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
import java.util.ArrayList;

/*
**  R_SC_MSS_ROLE_LNK
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_MSS_ROLE_LNKImpl extends  R_SC_MSS_ROLE_LNKBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_MSS_ROLE_LNKImpl (){
		super();
	}
	
	public R_SC_MSS_ROLE_LNKImpl(Session session, boolean makeDefaults)
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
		public static R_SC_MSS_ROLE_LNKImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_MSS_ROLE_LNKImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

   /**
    * An MSS User Type record is mapped to a Security Role on MSS Security Roles page.
    * Also MSS User Type is mapped to many Users on User Information page.
    *
    * This method is called when the Security Role is changed on this page. It tracks both
    * the revocation and assignment information.
    *
    * When a MSS User Type associated with a particular Security Role is changed, then the Users
    * are assigned the new Security Role that has now become associated with the MSS User Type.
    * Also the old Security Role now stands revoked.
    *
    */
   protected void updateHistTrackerForChangedSecRole()
   {
      if( !isHistTrackingEnabled() )
      {
         return;
      }

      IN_HIST_TRACKImpl.trackRevocation(this, null, getOldSEC_ROLE_ID());
      IN_HIST_TRACKImpl.trackAssignment(this, null, getSEC_ROLE_ID());

   }

   /**
    * Checks if a MSS User Type has any Security Roles that is defined
    * as restricted on Restricted Security Role Pair table. If yes, logs error.
    */
   protected void validateSecurityRoleForMSSUserTyp( )
   //@SupressAbstract
   {
      Session loSession = getSession();
      int liMssUserTyp = getMSS_USER_TYP();
      String lsRoleId = getSEC_ROLE_ID();

      // Get the list of Security Roles assigned to the MSS User Type
      ArrayList<String> loListRoles = getSecRoleForMSSUserTyp( loSession, liMssUserTyp );

      //Checks for all possible combinations of given Security Role with the Arraylist of Security Roles
      //on Restricted Role Pairs table and returns true if any match found for a combination.
      if( R_SC_SEC_ROLEPAIRSImpl.isRoleRestricted( loSession , lsRoleId, loListRoles ) )
      {
         raiseException("%c:Q0200,v:" + lsRoleId + "%" );
      }
   }


   /*****************************************************************************
    * Set of methods that provide implementation for AdvIHistTracking methods.
    * These methods are only relevant to DataObject which is involved in Historical Tracking.
    * A DataObject participates in Historical Tracking when Extended property HIST_TRACKING
    * is set to Y(case insensitive).
    ******************************************************************************/


   /**
    * Since this DataObject is involved in Historical Tracking, this method returns all the
    * Entities for which tracking has to be performed.
    * Here, the Entity Code will be Collection of User ID's whose MSS User Type is being
    * changed. MSS User Type is associated with Security Roles (i.e. the MSS User Type is
    * being assigned or removed from a Security Role).
    */
   public ArrayList<String> getTrackedEntity()
   {
      // First, fetch all records which have the given MSS User Type.
      SearchRequest lsrSearch = new SearchRequest();
      lsrSearch.addParameter("R_SC_USER_INFO", "MSS_USER_TYP", String.valueOf(getMSS_USER_TYP()));
      Enumeration<R_SC_USER_INFOImpl> lenumUser = R_SC_USER_INFOImpl.getObjects(lsrSearch, getSession());

      // Next iterate through each record to include them in the list of User IDs to be returned.
      ArrayList<String> loUser = new ArrayList<String>();
      while(lenumUser.hasMoreElements())
      {
         loUser.add(lenumUser.nextElement().getUSER_ID());
      }
      return loUser;
   }

   /**
    * If the DataObject is involved in Historical Tracking, then this method returns the
    * Resource whose assignment/ revocation has to be tracked.
    * For example, if R_SC_USER_ROLE_LNK is involved in Historical Tracking then
    * Resource will be Security Role ID which is assigned/ revoked to a user.
    * Current implementation returns null, however sub-classes can override this method if a
    * different implementation is required.
    */
   public String getTrackedResource()
   {
      return getSEC_ROLE_ID();
   }



   /*****************************************************************************
    * END of methods that provide implementation for AdvIHistTracking methods.
    ******************************************************************************/





   /**
    * Returns a collection of records for a particular MSS User Type.
    *
    * @param fiMSSUserType int   The MSS USer Type
    * @param foSession
    * @return
    */
   public static Enumeration<R_SC_MSS_ROLE_LNKImpl> getMSSRolesForMSSUserType(
         int fiMSSUserType, Session foSession)
   {
      SearchRequest lsrSearch = new SearchRequest();
      lsrSearch.addParameter( "R_SC_MSS_ROLE_LNK", "MSS_USER_TYP",
            String.valueOf(fiMSSUserType) );
      return R_SC_MSS_ROLE_LNKImpl.getObjects(lsrSearch, foSession);
   }

   /**
    * Returns a collection of User records belonging to a particular MSS User Type.
    *
    * @param fiMSSUserType int   The MSS USer Type
    * @param foSession
    * @return
    */
   public static Enumeration<R_SC_USER_INFOImpl> getUsersForMSSUserType( int fiMSSUserType, Session foSession)
   {
      SearchRequest lsrSearch = new SearchRequest();
      lsrSearch.addParameter( "R_SC_USER_INFOImpl", "MSS_USER_TYP",
            String.valueOf(fiMSSUserType) );
      return R_SC_USER_INFOImpl.getObjects(lsrSearch, foSession);
   }


   /**
    * Returns a collection of Security Role IDs for given MSS User Type.
    * If no such Security Roles are found then returns empty ArrayList.
    * @param foSession Current Session object
    * @param fiMssUserTyp MSS User Type
    * @return ArrayList of Security Role IDs
    */

   public static ArrayList getSecRoleForMSSUserTyp( Session foSession, int fiMssUserTyp)
   {
      // Gets a collection of MSS Security Roles records belonging to given MSS User Type
      Enumeration lenumRoleList = getMSSRolesForMSSUserType( fiMssUserTyp, foSession );

      R_SC_MSS_ROLE_LNKImpl loMssRoleLnk = null ;
      ArrayList<String> loListRoles = new ArrayList<String>();

      // Loop through the Enumeration of MSS Security Roles
       while( lenumRoleList.hasMoreElements() )
       {
          loMssRoleLnk = ( R_SC_MSS_ROLE_LNKImpl )lenumRoleList.nextElement();
          loListRoles.add( loMssRoleLnk.getSEC_ROLE_ID() );
       }/* whole Enumeration of User records is traversed */
       return loListRoles;
   }

   /**
    * Checks if the given MSS User Type has Security Roles assigned that are defined as restricted on
    * Restricted Security Rolepairs table. If restricted ones found then return false, else true.
    * @param foSession Current Session object
    * @param fiMssUserTyp MSS User Type
    * @return boolean
    */

    public static boolean checkResRolePairsForMSSUserTyp(Session foSession,int fiMssUserTyp)
    {
       // Get the list of Security Roles assigned to the MSS User Type
       ArrayList<String> loListRoles = getSecRoleForMSSUserTyp( foSession, fiMssUserTyp );
       int liSize = loListRoles.size();
       String lsSecRoleId = null;

       // Loop through ArrayList of Security Roles to validate each Role against the list of Security Roles(loListRoles)
       // and check if any pair has been defined as restricted on Restricted Security Role Pairs table
       for( int liIdx = 0; liIdx < liSize; liIdx++ )
       {
          lsSecRoleId=loListRoles.get( liIdx );

          //Checks for all possible combinations of given Security Role with the Arraylist of Security Roles
          //on Restricted Role Pairs table and returns true if any match found for a combination.
          if( R_SC_SEC_ROLEPAIRSImpl.isRoleRestricted( foSession, lsSecRoleId, loListRoles ) )
          {
             return false;
          }
       }/* whole arraylist of security roles is traversed */
       return true;
   }

}

