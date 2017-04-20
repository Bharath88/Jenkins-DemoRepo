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
**  WF_TWIP_QRY*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_TWIP_QRYImpl extends  WF_TWIP_QRYBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

   private static final String  ATTR_APRV_ASGN_NM      = "APRV_ASGN_NM_" ;
   private static final String  ATTR_APRV_ASGN_FL      = "APRV_ASGN_FL_" ;
   private static final String  ATTR_APRV_ACTN_NM      = "APRV_ACTN_NM_" ;
   private static final String  ATTR_APRV_ASGN         = "APRV_ASGN_" ;
   private static final String  ATTR_APRV_ACTN_USID    = "APRV_ACTN_USID_" ;

   /**
    * Method to populate the custom fields
    *
    * Modification Log : Kiran Hiranandani - 09/21/02
    *                                      - initial version
    *
    * @param foRowToBeAdded row to modify
    * @return foResponse
    */
   public static void beforeResultSetFillBeforeSecurityCheck(
         DataRow foRowToBeAdded, Response foResponse )
   {
      BusinessObjectImpl   loTWIPQuery = foRowToBeAdded.getBusinessObject() ;
      Session              loSession = loTWIPQuery.getSession() ;
      Data                 loDataSbmtID ;
      Data                 loAprvAsgn ;
      Data                 loAprvActnUsid ;
      Data                 loAprvAsgnFl ;
      String               lsAprvAsgn ;
      String               lsAprvActnUsid ;
      String               lsSubmitNm ;
      String               lsAprvAsgnNmFld ;
      String               lsAprvAsgnFlFld ;
      String               lsAprvActnNmFld ;
      String               lsAprvAsgnFld ;
      String               lsAprvActnUsidFld ;
      String               lsAprvAsgnNm ;
      String               lsAprvActnNm ;
      boolean              lboolAprvAsgnFl ;

      loDataSbmtID = foRowToBeAdded.getData( "SBMT_ID" ) ;
      if ( loDataSbmtID != null )
      {
         lsSubmitNm = getUserName( loDataSbmtID.getString(),  loSession ) ;
         foRowToBeAdded.setValue( "SBMT_NM", lsSubmitNm ) ;
      } /* end if ( loDataSbmtID == null ) */

      for ( int liCtr = 1 ; liCtr <= 10 ; liCtr ++ )
      {
         lsAprvAsgnNmFld    = ATTR_APRV_ASGN_NM + liCtr ;
         lsAprvAsgnFlFld    = ATTR_APRV_ASGN_FL + liCtr ;
         lsAprvActnNmFld    = ATTR_APRV_ACTN_NM + liCtr ;
         lsAprvAsgnFld      = ATTR_APRV_ASGN + liCtr ;
         lsAprvActnUsidFld  = ATTR_APRV_ACTN_USID + liCtr ;

         loAprvAsgn   = foRowToBeAdded.getData( lsAprvAsgnFld ) ;
         lsAprvAsgn   = loAprvAsgn  != null ? (String)loAprvAsgn.getString() : "" ;

         loAprvAsgnFl    = foRowToBeAdded.getData( lsAprvAsgnFlFld ) ;
         lboolAprvAsgnFl = loAprvAsgnFl  != null ? (boolean)loAprvAsgnFl.getboolean() : false ;

         loAprvActnUsid = foRowToBeAdded.getData( lsAprvActnUsidFld ) ;
         lsAprvActnUsid = loAprvActnUsid  != null ? (String)loAprvActnUsid.getString() : "" ;


         if ( lboolAprvAsgnFl )
         {
            lsAprvAsgnNm = getRoleName( lsAprvAsgn, loSession ) ;
         } /* end if ( lboolAprvAsgnFl ) */
         else
         {
            lsAprvAsgnNm = getUserName( lsAprvAsgn, loSession ) ;
         } /* end else */

         lsAprvActnNm = getUserName( lsAprvActnUsid, loSession ) ;

         foRowToBeAdded.setValue( lsAprvAsgnNmFld, lsAprvAsgnNm ) ;
         foRowToBeAdded.setValue( lsAprvActnNmFld, lsAprvActnNm ) ;
      } /* end for */

   } /* end populateCustomFields() */

   /**
    * Method to return the full user name based on the user id.
    * Name is read from the R_SC_USER_DIR_INFO table.
    *
    * Modification Log : Kiran Hiranandani - 09/16/02
    *                                      - initial version
    *
    * @param fsAssignee - the user id to search on.
    * @return String - full user name - Last Name + First Name - "John Doe"
    */
   public static String getUserName( String fsAssignee, Session foSession )
   {
      SearchRequest loSrchReq = new SearchRequest() ;
      String        lsAsgnVal ;

      lsAsgnVal = ( fsAssignee == null ) ? "" : fsAssignee.trim() ;
      if ( lsAsgnVal.length() == 0 )
      {
         return "" ;
      } /* end if ( lsAsgnVal == "" )  */

      loSrchReq.addParameter( "R_SC_USER_DIR_INFO", "USER_ID", lsAsgnVal ) ;

      R_SC_USER_DIR_INFOImpl loUserRec = ( R_SC_USER_DIR_INFOImpl )
            R_SC_USER_DIR_INFOImpl.getObjectByKey( loSrchReq, foSession ) ;

      if ( loUserRec != null )
      {
         return loUserRec.getUSER_NM() ;
      } /* if ( loUserRec != null ) */

      return "" ;
   } /* end getUserName() */

   /**
    * This method returns the role's name for a given role ID.
    *
    * Modification Log : Kiran Hiranandani - 09/16/02
    *                                      - initial version
    *
    * @param fsAssignee - the value for ASSIGNEE (role ID)
    * @return String    - the role's name (on exception, an
    *                     empty string is returned)
    */
   public static String getRoleName( String fsAssignee, Session foSession )
   {
      SearchRequest loSrchReq = new SearchRequest() ;
      String        lsAsgnVal ;
      String        lsRoleNm ;

      lsAsgnVal = ( fsAssignee == null ) ? "" : fsAssignee.trim() ;
      if ( lsAsgnVal.length() == 0 )
      {
         return "" ;
      } /* end if ( lsAsgnVal == "" )  */

      loSrchReq.addParameter( "R_WF_ROLE", "ROLEID", lsAsgnVal ) ;

      R_WF_ROLEImpl loWFRoleRec = ( R_WF_ROLEImpl )
            R_WF_ROLEImpl.getObjectByKey( loSrchReq, foSession ) ;

      if ( loWFRoleRec != null )
      {
         return loWFRoleRec.getROLE_NM() ;
      } /* if ( loWFRoleRec != null ) */

      return "" ;
   } /* end getRoleName() */

}

