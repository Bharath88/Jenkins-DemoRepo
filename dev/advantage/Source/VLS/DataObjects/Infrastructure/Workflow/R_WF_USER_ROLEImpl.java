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


import com.amsinc.gems.adv.common.AMSSQLUtil;
/*
**  R_WF_USER_ROLE
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_USER_ROLEImpl extends  R_WF_USER_ROLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
/**
 *  The USERID Column
 */
public static final String USERID_COL = "USID" ;

/**
 *  The ROLEID Column
 */
public static final String ROLEID_COL = "ROLEID" ;

//{{COMP_CLASS_CTOR
public R_WF_USER_ROLEImpl (){
	super();
}

public R_WF_USER_ROLEImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_WF_USER_ROLEImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_WF_USER_ROLEImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

     /**
      * method to set up the filter to acquire a vector of roles that a user belongs to.
      * @param fsUserId as String : the id of the user.
      * @param foSSession as Session : object to be associated with the objects.
      * @return Vector of Roles that the user belongs to (fsUserId).
      * @author - Mark Farrell
      */
      public static Vector getUserRoles( String fsUserId, Session foSession )
      {
         Vector loRoleVector = null ;
         Enumeration loEnum = null ;
         SearchRequest loSearchReq = new SearchRequest();

         loSearchReq.add( USERID_COL + "=" +
                          AMSSQLUtil.getANSIQuotedStr(fsUserId,true) ) ;

         /* perform the search on the user id which returns enumeration */
         loEnum = R_WF_USER_ROLEImpl.getObjects( loSearchReq, foSession ) ;

         /* Using the enumeration returned, create the vector of role ids */
         return createVector( loEnum, true ) ;
      }

     /**
      * method to set up the filter to acquire a vector of users that belong to a role.
      * @param fsRoleId as String : the name of the role.
      * @param foSSession as Session : object to be associated with the objects.
      * @return Vector of Users that belong to the role (fsRoleId).
      * @author - Mark Farrell
      */
      public static Vector getRoleUsers( String fsRoleId, Session foSession )
      {
         Vector loUserVector = null ;
         Enumeration loEnum = null ;
         SearchRequest loSearchReq = new SearchRequest();

         loSearchReq.add( ROLEID_COL + "=" +
                          AMSSQLUtil.getANSIQuotedStr(fsRoleId,true) ) ;

         /* perform the search on the role id which returns enumeration */
         loEnum = R_WF_USER_ROLEImpl.getObjects( loSearchReq, foSession );

         /* Using the enumeration returned, create the vector of users */
         return createVector( loEnum, false ) ;
      }

     /**
      * method to create a vector of the specific column (either role or user)
      * from the Enumeration passed in.
      *
      * @param foEnum as Enumeration - the Enumeration of R_WF_USER_ROLE objects
      * @param fboolUserRole as boolean - true if creating vector of role ids.
      *                                   false if creating vector of user ids.
      * @return Vector of values in user id column or role id column.
      * @author - Mark Farrell
      */
      private static Vector createVector( Enumeration foEnum,
                                          boolean fboolUserRole )
      {
         Vector loVector = new Vector() ;

         while ( foEnum.hasMoreElements() )
         {
            R_WF_USER_ROLEImpl loUserRole =
               (R_WF_USER_ROLEImpl)foEnum.nextElement();

            /* cycle through the enumeration.  If boolean passed in is false */
            /* place the user id column values in the vector.  If the boolean*/
            /* is true, place the role id column values in the vector        */
            if (loUserRole != null)
            {
               if (fboolUserRole == true)
               {
                  loVector.addElement( loUserRole.getROLEID() );
               }
               else
               {
                  loVector.addElement( loUserRole.getUSID() );
               } /* end if (fboolUserRole == true) */
            } /* end if (loUserRole != null) */
         }  /* end while (foEnum.hasMoreElements() ) */

         return loVector ;
      }

      /**
       * This method checks if the Approval Role is assigned to the User
       *
       * @param fsUserId User Id
       * @param fsUserRole Approval Role
       * @param foSession Session object
       *
       * @return <code>true</code> if the role is assigned to the user;
       *         <code>false</code> otherwise
       */
      public static boolean isRoleAssignedToUser(String fsUserId, String fsUserRole,Session foSession)
      {
         SearchRequest loRoleSearch = new SearchRequest();

         // return false if either paramter is null
         if(AMSStringUtil.strIsEmpty(fsUserRole) ||
               AMSStringUtil.strIsEmpty(fsUserId) )
         {
            return false;
         }
         // Search on R_WF_SER_ROLE if there is a matching entry for specified
         // combination of User Id and Role
         loRoleSearch.addParameter("R_WF_USER_ROLE", "USID", fsUserId);
         loRoleSearch.addParameter("R_WF_USER_ROLE", "ROLEID", fsUserRole);

         // Return true if 1 or more matching records are found
         return R_WF_USER_ROLEImpl.getObjectCount(loRoleSearch, foSession) > 0;
      }// end of the method


   /*****************************************************************************
    * Set of methods that provide implementation for AdvIHistTracking methods.
    * These methods are only relevant to DataObject which is involved in Historical Tracking.
    * A DataObject participates in Historical Tracking when Extended property HIST_TRACKING
    * is set to Y(case insensitive).
    ******************************************************************************/

   /**
    * Since this DataObject is involved in Historical Tracking, this method returns all the
    * Entities for which tracking has to be performed.
    * Here, the Entity Code will be User ID to whom a Workflow Role is being assigned or revoked.
    */
   public ArrayList<String> getTrackedEntity()
   {
      ArrayList<String> loUser = new ArrayList<String>();
      loUser.add(getUSID());
      return loUser;
   }

   /**
    * Since this DataObject is involved in Historical Tracking, this method returns the
    * Resource whose assignment/ revocation has to be tracked.
    * Here, Resource will be Workflow Role ID which is assigned/ revoked to a User.
    */
   public String getTrackedResource()
   {
      return getROLEID();
   }



   /*****************************************************************************
    * END of methods that provide implementation for AdvIHistTracking methods.
    ******************************************************************************/

}
