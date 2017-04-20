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
import com.amsinc.gems.adv.common.AMSSQLUtil;
/*
**  R_WF_ALT_APRV_ID
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_ALT_APRV_IDImpl extends  R_WF_ALT_APRV_IDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{


   /*
    * The role id column name
    */

   public static final String ROLEID_COLUMN = "ROLEID" ;

   /*
    * The alt role id column name
    */

   public static final String ALT_ROLEID_COLUMN = "ALT_ROLEID" ;

   /*
    * The approval level column name
    */

   public static final String APRV_LVL_COLUMN = "APRV_LVL" ;

   /*
    * The user id column name
    */

   public static final String USID_COLUMN = "USID" ;

   /*
    * The alt user id column name
    */

   public static final String ALT_USID_COLUMN = "ALT_USID" ;


//{{COMP_CLASS_CTOR
public R_WF_ALT_APRV_IDImpl (){
	super();
}

public R_WF_ALT_APRV_IDImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_WF_ALT_APRV_IDImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_WF_ALT_APRV_IDImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
   beforeSaveValidate(obj, response);
}
//END_COMP_EVENT_beforeUpdate}}

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   beforeSaveValidate(obj, response);
}
//END_COMP_EVENT_beforeInsert}}

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addRuleEventListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

      /**
    * for a given id, this method determines if this ID should be alternately
       * assigned to another ID, based on the effective date and the approval level.
       * @param fsid - user id or workflow role id to be checked.
       * @param fboolRole - true if fsid is a role, or false if fsid is user.
       * @param fiApprovalLevel - specifies what approval level must be checked for the
       *                          alternate assignment.
       * @return 2 element array : 1st element is String that specifies alternate ID,
       *                           2nd element is Boolean that is true if alternate ID
       *                           is role ID or false if it is user id.
       *                           return null if no alternate assignment is to occur.
       * @author Mark Farrell - initial version 05/18/2000
       *
       */
         public Object [] getAlternativeAssignment(String fsId,
            boolean fboolRole, int fiApprovalLevel)
      {
      Object [] loResults = null;

         /* First, get the results with the approval level received as parameter */
                  Enumeration loEnum = getResults( fsId, fiApprovalLevel, fboolRole ) ;

         if (loEnum == null)
         {
            return null;
         }

         loResults = checkApproval(loEnum);

         // if the array is null, recheck with approval level of zero
         if (loResults != null)
         {
            return loResults;
         }
         else
         {
            if (fiApprovalLevel != 0)
            {
               loEnum = getResults(fsId, 0, fboolRole);
               if (loEnum == null)
               {
                  return null;
               }
               loResults = checkApproval(loEnum);
               return loResults;
            }
            else
            {
               return null;
            }
         }
      }

     /**
      * for a given id and approval level, this method returns an enumeration
      * of objects found.  If the boolean is true, search on the role id and
      * approval level, and if the boolean is false, search on the user id and
      * approval level.
      * @param fsid - user id or workflow role id to be checked.
      * @param fboolRole - true if fsid is a role, or false if fsid is user.
      * @param fiApprovalLevel - specifies what approval level must be checked for the
      *                          alternate assignment.
      * @return enumeration - objects found.
      *
      * @author Mark Farrell - initial version 05/18/2000
      *
      */
      private Enumeration getResults ( String fsId, int fiApprovalLevel,
                                       boolean fboolRole )
      {
         SearchRequest loSearchReq = null ;

         if ( AMSStringUtil.strIsEmpty( fsId ) )
         {
            return null ;
         }

         loSearchReq = new SearchRequest();

         // if the boolean is true, search on role id, else search on user id
         loSearchReq.add((fboolRole ? ROLEID_COLUMN : USID_COLUMN) +
                         "='" +
                         AMSSQLUtil.getANSIQuotedStr(fsId) +
                         "' AND " +
                         APRV_LVL_COLUMN +
                         "=" +
                         fiApprovalLevel );

         return getObjects( loSearchReq, this.getSession() ) ;
      }

     /**
      * for a enumeration of R_WF_ALT_APRV_ID objects, if the Alternate assignment
      * flag is set to true, get the alternate role id.  If the flag is false, get
      * the alternate user id.  Populate the array to be returned with the alternate
      * assignment and the boolean flag.
      *
      * @param foEnum - enumeration of objects found
      * @param fboolRole - true if fsid is a role, or false if fsid is user.
      * @param fiApprovalLevel - specifies what approval level must be checked for the
      *                          alternate assignment.
      * @return 2 element array : 1st element is String that specifies alternate ID,
      *                           2nd element is Boolean that is true if alternate ID
      *                           is role ID or false if it is user id.
      *                           return null if no alternate assignment is to occur.
      *
      * @author Mark Farrell - initial version 05/18/2000
      *
      */

      private Object [] checkApproval ( Enumeration foEnum )
      {
//ml - to do:  deal with effective dating
         if (foEnum.hasMoreElements() )
         {
            Object [] loReturnArray = null;
            R_WF_ALT_APRV_IDImpl loAltId =
                  (R_WF_ALT_APRV_IDImpl) foEnum.nextElement();
            boolean lboolAlt = false;

            try
            {
               lboolAlt = loAltId.getALT_ASSIGNEE_FL() ;
            }
            catch (Exception loEx)
            {
               return null;
            }

            /* populate the array to be returned with the alternate id and the */
            /* boolean - true if role id and false if user id                  */

            loReturnArray = new Object[2];
            try
            {
               loReturnArray[0] = (lboolAlt ? loAltId.getALT_ROLEID().trim()
                                            : loAltId.getALT_USID().trim());
               loReturnArray[1] = new Boolean(lboolAlt) ;
            }
            catch (Exception loEx)
            {
               return null;
            }

            return loReturnArray;
         }
         else
         {
             return null ;
         } /* end if (foEnum.hasMoreElements() ) */
   }


     /**
   * Derivation on ALT_ASSIGNEE_FL.
      * If both the alt user id and alt role id are null, set flag to false.
      * If the alt user id is null, set flag to true.
      * If the alt role id is null, set flag to false.
      *
      * @param fboolAltAssignee - the Alternate Assignee flag
      * @return boolean - true or false
      *
      * @author Mark Farrell - initial version 05/18/2000
      *
      */

      public boolean setALTASSIGNEE_FL(boolean fboolAltAssignee)
      {
          if (getALT_USID() == null)
          {
             if (getALT_ROLEID() == null)
             {
                return false;
             }
             else
             {
                return true;
             }
          }
          else
          {
             return false;
          }
      }

      public boolean set_ASSIGNEE_FL(boolean fboolAssignee)
      {
          if (getUSID() == null)
          {
             if (getROLEID() == null)
             {
                return false;
             }
             else
             {
                return true;
             }
          }
          else
          {
             return false;
          }
      }

      public boolean checkROLEID(String fsUser, String fsRole)
      {
         if (fsRole == null && fsUser == null)
         {
            return false ;
         }
         return true ;
      }

      public boolean checkALT_ROLEID(String fsUser, String fsRole)
      {
         if (fsRole == null && fsUser == null)
         {
            return false ;
         }
         return true ;
      }

     /**
   * Validation on ASSIGNEE_FL.
      * If the Assignee flag is set to true, the USERID will be set to zero
      * If the Assignee flag is set to false, the ROLEID will be set to zero
      *
      * @param fboolAssignee - the Assignee flag, true if role, false if user
      * @return boolean - always return true.
      *
      * @author Mark Farrell - initial version 05/18/2000
      */

      public boolean checkFlag(boolean fboolAssignee)
      {
         if ( fboolAssignee == true)
         {
            setUSID("0");
         }
         else
         {
            setROLEID("0");
         }
         return true ;
      }

     /**
   * Validation on ALT_ASSIGNEE_FL.
      * If the Alt Assignee flag is set to true, the ALT_USERID will be set to null
      * If the Alt Assignee flag is set to false, the ALT_ROLEID will be set to null
      *
      * @param fboolAssignee - the Alt Assignee flag, true if role, false if user
      * @return boolean - always return true.
      *
      * @author Mark Farrell - initial version 05/18/2000
      */

      public boolean checkAltFlag(boolean fboolAssignee)
      {
         if ( fboolAssignee == true)
         {
            setALT_USID(null);
         }
         else
         {
            setALT_ROLEID(null);
         }

         return true ;
      }

      /**
       * validation on alternate assignee flag
       * if flag true, set role id to null, else, set user id to null
       * @param fsUserId - the current user id
       * @param fiAssigneeFL - the assignee flag, true if role, false if user
       * @return true.
       *
       * author - Mark Farrell
       */
      public boolean checkAltUser(String fsUserId, boolean fboolAssigneeFL)
      {
         if ( fboolAssigneeFL == true )
         {
             setALT_USID(null);
         }
         else if ( fboolAssigneeFL == false )
         {
             setALT_ROLEID(null);
         }
         return true ;
      }

      /**
       * derivation on alt assignee flag
       * if user id is null, return true and set flag to true
       * if role id is null, return false and set flag to false
       * @param fboolAssignee - the assignee flag
       * @return true if userid is null, false if roleid is null.
       *
       * @author - Mark Farrell
       */

      public boolean trueALT_ASSIGNEE_FL(boolean fboolAssignee )
      {
          if ( getALT_ROLEID() == null )
          {
             return false ;
          }
          else if ( getALT_USID() == null )
          {
             return true ;
          }
          else
          {
             return getALT_ASSIGNEE_FL() ;
          }
      }

      /**
       * validation on assignee flag
       * if flag true, set role id to null, else, set user id to null
       * @param fsUserId - the current user id
       * @param fiAssigneeFL - the assignee flag, true if role, false if user
       * @return true.
       *
       * author - Mark Farrell
       */
      public boolean checkUser(String fsUserId, boolean fboolAssigneeFL)
      {
         if ( fboolAssigneeFL == true )
         {
             setUSID("0");
         }
         else if ( fboolAssigneeFL == false )
         {
             setROLEID("0");
         }
         return true ;
      }

      /**
       * derivation on assignee flag
       * if user id is null, return true and set flag to true
       * if role id is null, return false and set flag to false
       * @param fboolAssignee - the assignee flag
       * @return true if userid is null, false if roleid is null.
       *
       * @author - Mark Farrell
       */

      public boolean trueASSIGNEE_FL(boolean fboolAssignee)
      {
      String lsUserId = null;
      String lsRoleId = getROLEID();

         if ((lsRoleId == null) || lsRoleId.trim().equals("0"))
         {
            return false ;
         }

         lsUserId = getUSID();
         if ((lsUserId == null) || lsUserId.trim().equals("0"))
         {
            return true ;
         }

         return getASSIGNEE_FL() ;
      }


      public void myInitializations()
      {
               // Make "0" an exception for "ROLEID" against R_WF_ROLE
         // Make "0" an exception for "USID" against R_SC_USER_DIR_INFO

         registerForeignKeyException( "R_WF_ROLE", "ROLEID", "0");
         registerForeignKeyException( "R_SC_USER_DIR_INFO", "USID", "0");

     //    registerForeignKeyException( "R_WF_ROLE", "ALT_ROLEID", null);
     //    registerForeignKeyException( "R_SC_USER_DIR_INFO", "ALT_USID", null);

         this.enableBehavior( AMSDataObject.AMSBEHAVIOR_EFFECTIVE_DATED);
      }

      /**
       * validate assignee/alt assigne/effective dates before save.
       * This is being done instead of enforcing a relationship as in the TPL
       * we return after the first succesful relation enforcement instead of
       * checking all relationships
       */
      public void beforeSaveValidate(DataObject obj, Response response)
      {
         DataRow loRow = obj.getRow();
         String lsUserId ;
         String lsRoleId ;
         String lsAltUserId ;
         String lsAltRoleId ;
         VSDate loEfBgnDt ;
         VSDate loEfEndDt ;

         // validate userid/role - ignore userid/role set to 0 or null
         lsUserId = loRow.getData( "USID" ).getString() ;
         lsRoleId = loRow.getData( "ROLEID" ).getString() ;
         if ( ( ! ( ( lsUserId == null ) || ( lsUserId.trim().length() == 0 ) ) ) &&
              ( ! ( lsUserId.equals( "0" ) ) ) )
         {
            validateUser( lsUserId, 0 ) ;
         }
         if ( ( ! ( ( lsRoleId == null ) || ( lsRoleId.trim().length() == 0 ) ) ) &&
              ( ! ( lsRoleId.equals( "0" ) ) ) )
         {
            validateRole( lsRoleId, 0 ) ;
         }

         // validate alt userid/alt role - ignore userid/role set to null
         lsAltUserId = loRow.getData( "ALT_USID" ).getString() ;
         lsAltRoleId = loRow.getData( "ALT_ROLEID" ).getString() ;
         if ( ! ( ( lsAltUserId == null ) || ( lsAltUserId.trim().length() == 0 ) ) )
         {
            validateUser( lsAltUserId, 1 ) ;
         }
         if ( ! ( ( lsAltRoleId == null ) || ( lsAltRoleId.trim().length() == 0 ) ) )
         {
            validateRole( lsAltRoleId, 1 ) ;
         }

         // From date should not be later than To date
         loEfBgnDt = loRow.getData( "EFBGN_DT" ).getVSDate();
         loEfEndDt = loRow.getData( "EFEND_DT" ).getVSDate();
         if ( ( loEfBgnDt != null ) && ( loEfEndDt != null ) &&
              ( loEfBgnDt.after( loEfEndDt ) ) )
         {
            raiseException( "From Date should not be later than To Date.",
                            "R_WF_ALT_APRV_ID","EFBGN_DT" ) ;
         }
      }

      /**
       * validate assignee/alt assignee
       * @param fsValue - userid
       * @param fiType - whether user or alt user
       */
      public void validateUser(String fsValue, int fiType)
      {
         DataObject    loR_SC_USER_DIR_INFO = null ;
         Parameter     loParam              = new Parameter() ;
         SearchRequest loSearchReq          = new SearchRequest() ;

         loParam.objName   = "R_SC_USER_DIR_INFO" ;
         loParam.fieldName = "USER_ID" ;
         loParam.value     = fsValue ;
         loSearchReq.add( loParam ) ;
         loR_SC_USER_DIR_INFO = R_SC_USER_DIR_INFOImpl.getObjectByKey( loSearchReq, getSession() ) ;
         if ( loR_SC_USER_DIR_INFO == null )
         {
            if ( fiType == 0 )
            {
               raiseException("Userid specified is invalid.","R_WF_ALT_APRV_ID","USID") ;
            }
            else
            {
               raiseException("Alternate userid specified is invalid.","R_WF_ALT_APRV_ID","ALT_USID") ;
            }
         }
      }

      /**
       * validate role/alt role before save
       * @param fsValue - role
       * @param fiType - whether role or alt role
       */
      public void validateRole(String fsValue, int fiType)
      {
         DataObject    loR_WF_ROLE = null ;
         Parameter     loParam     = new Parameter() ;
         SearchRequest loSearchReq = new SearchRequest() ;

         loParam.objName   = "R_WF_ROLE" ;
         loParam.fieldName = "ROLEID" ;
         loParam.value     = fsValue ;
         loSearchReq.add( loParam ) ;
         loR_WF_ROLE = R_WF_ROLEImpl.getObjectByKey( loSearchReq, getSession() ) ;
         if ( loR_WF_ROLE == null )
         {
            if ( fiType == 0 )
            {
               raiseException("Role specified is invalid.","R_WF_ALT_APRV_ID","ROLEID");
            }
            else
            {
               raiseException("Alternate Role specified is invalid.","R_WF_ALT_APRV_ID","ALT_ROLEID");
            }
         }
      }
}
