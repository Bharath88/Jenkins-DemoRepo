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


import com.amsinc.gems.adv.common.* ;
import java.lang.reflect.Method;

/*
**  V_WF_APRV_WRK_LST
*/

//{{COMPONENT_RULES_CLASS_DECL
public class V_WF_APRV_WRK_LSTImpl extends  V_WF_APRV_WRK_LSTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   private int                  miDI_ACTN_CD   = 0;
   private R_GEN_DOC_CTRLImpl   moDocCtrl      = null ;

   static
   {
      /*
       * Since this is just a read-only view, any updates on this object
       * resulting from parent-child relationship(e.g. parent replicates update)
       * should be avoided, otherwise a DB error is issued.
       */
      enableBehavior( AMSBEHAVIOR_NO_CASCADE_RECEIVING );
   }

//{{COMP_CLASS_CTOR
public V_WF_APRV_WRK_LSTImpl (){
	super();
}

public V_WF_APRV_WRK_LSTImpl(Session session, boolean makeDefaults)
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
	public static V_WF_APRV_WRK_LSTImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new V_WF_APRV_WRK_LSTImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   public void myInitializations()
   {
      setSaveBehaviorMode( AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE ) ;
      mboolCustomDataObjectSave = true ;
   }

   // data object names

   private static final String C_R_WF_ROLE_TBL = "R_WF_ROLE";
   private static final String C_WF_APRV_WRK_LST_TBL = "WF_APRV_WRK_LST";
   private static final String C_R_SC_USER_DIR_INFO_TBL = "R_SC_USER_DIR_INFO";

   // data object attribute names

   private static final String C_ROLEID_ATTR = "ROLEID";
   private static final String C_WRK_LST_TYP_ATTR = "WRK_LST_TYP";
   private static final String C_WRK_LST_ID_ATTR = "WRK_LST_ID";
   private static final String C_ASSIGNEE_ATTR = "ASSIGNEE";
   private static final String C_ASSIGNEE_FL_ATTR = "ASSIGNEE_FL";
   private static final String C_APRV_LVL_ATTR = "APRV_LVL";
   private static final String C_DOC_CD = "DOC_CD";
   private static final String C_DOC_ID = "DOC_ID";
   private static final String C_DOC_VERS_NO = "DOC_VERS_NO";
   private static final String C_DOC_DEPT_CD = "DOC_DEPT_CD";
   private static final String C_USER_ID_ATTR = "USER_ID";

   /**
    * This method returns the role's name for a given role ID.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param fsAssignee - the value for ASSIGNEE (role ID)
    * @return String    - the role's name (on exception, an
    *                     empty string is returned)
    */
   public String getRoleName(String fsAssignee)
   {
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam = new Parameter();

      loParam.objName = C_R_WF_ROLE_TBL;
      loParam.fieldName = C_ROLEID_ATTR;
      loParam.value = fsAssignee;
      loRequest.add(loParam);

      try
      {
         R_WF_ROLEImpl loRoleRecord =
               (R_WF_ROLEImpl) R_WF_ROLEImpl.getObjectByKey(loRequest, getSession() );
         return loRoleRecord.getROLE_NM();
      }
      catch (Exception loEx)
      {
         String lsTemp = (fsAssignee == null) ? "" : fsAssignee;
         this.raiseException("error getting name for role \"" +
                             lsTemp + "\"");
         return "";
      }
   }

   /**
    * Method to return the full user name based on the user id.
    * Name is read from the R_SC_USER_DIR_INFO table.
    *
    * @param fsAssignee - the user id to search on.
    * @return String - the full user name (e.g., "Jack Smith")
    * @author - Mark Farrell
    * @author - Michael Levy
    */
   public String getUserName( String fsAssignee )
   {
      if ( fsAssignee != null )
      {
         SearchRequest          loSrchReq = new SearchRequest() ;
         Parameter              loParam   = new Parameter() ;
         R_SC_USER_DIR_INFOImpl loUserRec ;

         loParam.objName   = C_R_SC_USER_DIR_INFO_TBL ;
         loParam.fieldName = C_USER_ID_ATTR ;
         loParam.value     = fsAssignee ;
         loSrchReq.add( loParam ) ;

         loUserRec = (R_SC_USER_DIR_INFOImpl)R_SC_USER_DIR_INFOImpl.getObjectByKey( loSrchReq, getSession() ) ;

         if ( loUserRec != null )
         {
            return loUserRec.getUSER_NM() ;
         } /* end if ( loUserRec != null ) */
      } /* end if ( fsAssignee != null ) */
      return null ;

   }


   /**
    * This method is the formula for the comment display when this
    * worklist entry has been forwarded to this user from another.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @return String - the comment string
    */
   public String getCommentWithFwd()
   {
      // for a given comment id, forwarding user id, and forwarding date,
      // return the comment text

      try
      {
         VSDate loDate = getFWD_DT();
         int liHours = loDate.getHours();
         int liMinutes = loDate.getMinutes();
         int liSeconds = loDate.getSeconds();
         StringBuffer lsbReturnText = new StringBuffer(128);
         lsbReturnText.append("Fwd by ");

         boolean lboolPM = liHours >= 12;
         int liDisplayHours = liHours;

         String lsDisplayMinutes = ((liMinutes < 10) ? "0" : "") + liMinutes;
         String lsDisplaySeconds = ((liSeconds < 10) ? "0" : "") + liSeconds;

         if (liHours == 0)
         {
            liDisplayHours = 12; // 12 am
         }
         else if (liHours > 12)
         {
            liDisplayHours -= 12; // 13 -> 1 pm, etc.
         }

         lsbReturnText.append(" " + getFWD_USID());

         lsbReturnText.append(" on " + String.valueOf(loDate.getMonth() + 1) + "/" +
                         String.valueOf(loDate.getDate()) + "/" +
                         loDate.getYearLongString() + " " +
                         String.valueOf(liDisplayHours) + ":" +
                         lsDisplayMinutes + ":" + lsDisplaySeconds + " " );
         lsbReturnText.append( (lboolPM ? "PM" : "AM") + ": " + getComment() );

         return lsbReturnText.toString();
      }
      catch (Exception loEx)
      {
         return getComment();
      }
   }

   /**
    * This method is the formula for the comment display when this
    * worklist entry has not been forwarded to this user from another.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @return String - the comment string
    */
   public String getComment()
   {
      // for a given comment id, simply return the resulting comment text
      try
      {
         return getCMNT_TXT();
      }
      catch (Exception loEx)
      {
         return "** comment text unavailable **";
      }
   }

   /**
    * This method inserts a new record into the worklist table.  Note that
    * the locked user ID is always defaulted to blank and not an input
    * parameter.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    * Modification Log : Mark Farrell - 06/08/2000
    *                                 - Add dept, type, and category
    *
    * @param foSession      - the current session
    * @param fcType         - the value for WRK_LST_TYP
    * @param fsAssignee     - the value for ASSIGNEE
    * @param fboolRole      - the value for ASSIGNEE_FL
    * @param fiAprvLevel    - the value for APRV_LVL
    * @param fsDisplayPage  - the value for DISP_PG
    * @param fbReason       - the value for REAS
    * @param flCommentId    - the value for CMNT
    * @param foAsgnDate     - the value for ASGN_DT
    * @param fsFwdUserId    - the value for FWD_USID
    * @param foFwdDate      - the value for FWD_DT
    * @param fsDocCode      - the value for DOC_CD
    * @param fsDocId        - the value for DOC_ID
    * @param fiDocVersionNo - the value for DOC_VERS_NO
    * @param fsDocDept      - the value for DOC_DEPT_CD
    * @param fsDocType      - the value for DOC_TYP
    * @param fsDocCat       - the value for DOC_CAT
    * @param flAprvRuleId   - the value for APRV_RULE_ID
    * @return boolean       - status on the success of the insert
    */
   public static boolean insert(Session foSession, char fcType,
         String fsAssignee, boolean fboolRole, int fiAprvLevel, String fsDisplayPage,
         byte fbReason, long flCommentId, VSDate foAsgnDate, String fsFwdUserId,
         VSDate foFwdDate, String fsDocCode, String fsDocId, int fiDocVersionNo,
            String fsDocDept, String fsDocType, String fsDocCat ,
            String fsSelfAprvRsct, String fsSbmtId,String fsCreaId,long flAprvRuleId)
   {
      V_WF_APRV_WRK_LSTImpl loWorklistRec = null;
      try
      {
         // send in false for setting the defaults

         loWorklistRec = getNewObject(foSession, true);
      }
      catch(Exception loEx)
      {
         return false;
      }

      try
      {
         loWorklistRec.setWRK_LST_TYP(String.valueOf(fcType));
         // loWorklistRec.setWRK_LST_ID(AMSUniqNumber.getUniqNumber("WF_WL_A")); // s/b done in BRD
         loWorklistRec.setASSIGNEE(fsAssignee);
         loWorklistRec.setASSIGNEE_FL(fboolRole);
         loWorklistRec.setAPRV_LVL(fiAprvLevel);
         loWorklistRec.setDISP_PG(fsDisplayPage);
         loWorklistRec.setREAS(fbReason);
         loWorklistRec.setCMNT(flCommentId);
         loWorklistRec.setASGN_DT(foAsgnDate);
         loWorklistRec.setFWD_USID(fsFwdUserId);
         loWorklistRec.setFWD_DT(foFwdDate);
         loWorklistRec.setLOCK_USID(null);
         loWorklistRec.setDOC_CD(fsDocCode);
         loWorklistRec.setDOC_ID(fsDocId);
         loWorklistRec.setDOC_VERS_NO(fiDocVersionNo);
         loWorklistRec.setDOC_DEPT_CD(fsDocDept);
         loWorklistRec.setDOC_TYP(fsDocType);
         loWorklistRec.setDOC_CAT(fsDocCat);
         loWorklistRec.setSLF_APRV_RSCT(fsSelfAprvRsct);
         loWorklistRec.setDOC_SBMT_ID(fsSbmtId);
         loWorklistRec.setDOC_CREA_USID(fsCreaId);
         loWorklistRec.setAPRV_RULE_ID(flAprvRuleId);

         loWorklistRec.setInsert();
         loWorklistRec.save(true);
      }
      catch (Exception loEx)
      {
        // JXPtemp
        // Add exception log to logger object
        moAMSLog.error("Unexpected error encountered while processing. ", loEx);

        System.err.println ( "WORKFLOW: " + loEx.getMessage() ) ;
        // JXPtemp
         loWorklistRec.raiseException("error inserting into WF_APRV_WRK_LST");
         return false;
      }

      return true;
   }

   /**
    * This method deletes a record from the worklist, based on the exact key
    * information.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fcType         - the value for WRK_LST_TYP
    * @param flId           - the value for WRK_LST_ID
    * @param fsAssignee     - the value for ASSIGNEE
    * @param fboolRole      - the value for ASSIGNEE_FL
    * @param fiAprvLevel    - the value for APRV_LVL
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, char fcType, long flId,
         String fsAssignee, boolean fboolRole, int fiAprvLevel)
   {
      // this version of delete is designed to delete only one record

      WF_APRV_WRK_LSTImpl loWorklistRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_TYP_ATTR;
      loParam.value = String.valueOf(fcType);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_ID_ATTR;
      loParam.value = String.valueOf(flId);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_ATTR;
      loParam.value = fsAssignee;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_FL_ATTR;
      loParam.value = String.valueOf(fboolRole);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_APRV_LVL_ATTR;
      loParam.value = String.valueOf(fiAprvLevel);
      loRequest.add(loParam);

      try
      {
         loWorklistRec =
               (WF_APRV_WRK_LSTImpl) getObjectByKey(loRequest, foSession);
         if (loWorklistRec != null)
         {
            loWorklistRec.setDelete();
            loWorklistRec.save(true);
            llNumDeleted++;
         } // end if (loWorklistRec != null)
      }
      catch (Exception loEx)
      {
         loWorklistRec.raiseException("error deleting from WF_APRV_WRK_LST");
         return -1L;
      }
         return llNumDeleted;
   }

   /**
    * This method deletes records from the worklist, based on type and ID.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fcType         - the value for WRK_LST_TYP
    * @param flId           - the value for WRK_LST_ID
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, char fcType, long flId)
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long flNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_TYP_ATTR;
      loParam.value = String.valueOf(fcType);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_ID_ATTR;
      loParam.value = String.valueOf(flId);
      loRequest.add(loParam);
         try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            flNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return flNumDeleted;
   }

   /**
    * This method deletes records from the worklist, based on type, ID,
    * and approval level.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fcType         - the value for WRK_LST_TYP
    * @param flId           - the value for WRK_LST_ID
    * @param fiAprvLevel    - the value for APRV_LVL
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, char fcType, long flId,
         int fiAprvLevel)
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_TYP_ATTR;
      loParam.value = String.valueOf(fcType);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_ID_ATTR;
      loParam.value = String.valueOf(flId);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_APRV_LVL_ATTR;
      loParam.value = String.valueOf(fiAprvLevel);
      loRequest.add(loParam);
         try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            llNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted up to this point

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return llNumDeleted;
   }

   /**
    * This method deletes records from the worklist, based on assignee
    * information.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fsAssignee     - the value for ASSIGNEE
    * @param fboolRole      - the value for ASSIGNEE_FL
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, String fsAssignee, boolean fboolRole)
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_ATTR;
      loParam.value = fsAssignee;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_FL_ATTR;
      loParam.value = String.valueOf(fboolRole);
      loRequest.add(loParam);
         try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            llNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return llNumDeleted;
   }

   /**
    * This method deletes records from the worklist, based on assignee
    * information and approval level.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fsAssignee     - the value for ASSIGNEE
    * @param fboolRole      - the value for ASSIGNEE_FL
    * @param fiAprvLevel    - the value for APRV_LVL
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, String fsAssignee, boolean fboolRole,
         int fiAprvLevel)
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_ATTR;
      loParam.value = fsAssignee;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_FL_ATTR;
      loParam.value = String.valueOf(fboolRole);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_APRV_LVL_ATTR;
      loParam.value = String.valueOf(fiAprvLevel);
      loRequest.add(loParam);
         try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            llNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return llNumDeleted;
   }

   /**
    * This method removes the value in LOCK_USID for the return task action.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @return boolean - the status on the success of the update.
    */
   public boolean updateForReturnTask()
   {
      // update current worklist record to remove LOCK_USID

      try
      {
         this.setLOCK_USID("");
         this.setUpdate();
         this.save(true);
      }
      catch (Exception loEx)
      {
         this.raiseException("error in updateForReturnTask");
         return false;
      }
            return true;
   }

   /**
    * This method adds the user ID to LOCK_USID.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param  fsLockUserId - the user ID that did the take task action.
    * @return boolean      - the status on the success of the update.
    */
   public boolean updateForTakeTask(String fsLockUserId)
   {
      // update current worklist record to add LOCK_USID

      try
      {
         this.setLOCK_USID(fsLockUserId);
         this.setUpdate();
         this.save(true);
      }
      catch (Exception loEx)
      {
         this.raiseException("error in updateForTakeTask");
         return false;
      }
         return true;
   }

   /**
    * This method sets the forwarding user ID and date to a worklist
    * record.
    *
    * Modification Log : Michael Levy - 05/01/00
    *                                 - inital version
    *
    * @param  fsFwdUserId - the user ID that forwarded the worklist record.
    * @param  foFwdDate   - the date on which the worklist record was
    *                       forwarded.
    * @return boolean     - the status on the success of the update.
    */
   public boolean updateForForward(String fsFwdUserId, VSDate foFwdDate)
   {
      // update current worklist record to add the forwarded by user id
      // and the date; it is assumed that fsFwdUserId and foFwdDate are
      // both not null
         try
      {
         this.setFWD_USID(fsFwdUserId);
         this.setFWD_DT(foFwdDate);
         this.setUpdate();
         this.save(true);
      }
      catch (Exception loEx)
      {
         this.raiseException("error in updateForForward");
         return false;
      }

      return true;
   }

   /**
    * This version of save will invoke super.save() or this.save()
    * depending on the passed parameter.  This allows the real updates
    * occurring in this class to bypass this.save() which is used
    * for document processing.
    *
    * @param fboolSuper - true to go to super; false to go to this
    * @throws ServerException
    *
    */
   public void save(boolean fboolSuper) throws ServerException
   {
      if (fboolSuper)
      {
         if ( AMSCommonConstants.VLS_CASE_LOGIC_ENABLED ||
              AMSCommonConstants.VLS_TRIM_SPACE_ENABLED)
         {
            applyCaseAndTrimLogicToDataObject(
               getMixedCaseTextAttrsList(),
               getUpperCaseTextAttrsList() ) ;
         }
         mboolCustomDataObjectSave = false ;
         super.save();
         mboolCustomDataObjectSave = true ;
      }
      else
      {
         this.save();
      }
   }

      /**
       * The following method will override the super save.  This method will
       * be called when the data source is updated on the worklist (this will
       * be forced when an action on a document is performed).  Within this
       * method, the document will be found by (DOC_HDRImpl.getObjectByKey ...)
       * and a save will be forced.  Once within DOC_HDRImpl save( ...), the
       * document action and other parameters will be read from the session.
       *
       * @author Mark Farrell - initial version 06/08/2000
       *                      - modifications for ATTR_DOC_ACTN_CD. 06/29/00
       *  Michael Levy - 07/14/00 - break out processing in order to deal
       *                            with approval action processing
       *  Subramanyam Surabhi - 09/22/00 - Added case for DOC_ACTN_BYPS_APRV
       *
       */

      protected boolean myDataObjectSave()
   //@SuppressAbstract
      {
         // remove "temp" from the back for FWD_USID if it's there
         String lsFwdUsid = getFWD_USID();
         VSDate loDate = getFWD_DT();

         if (loDate == null)
         {
            setNull("FWD_USID");
         }
         else if ((lsFwdUsid != null) && !lsFwdUsid.trim().equals(""))
         {
            // if there's a date and the FWD_USID is "temp", then
            // "temp" must be the actual value
            if (!lsFwdUsid.equals("temp"))
            {
               int liPos = lsFwdUsid.indexOf("temp");

               if (liPos != -1)
               {
                  setFWD_USID(lsFwdUsid.substring(0, liPos));
               }
            }
         }

         // Determine which action was initiated on the client.
         String lsDI_ACTN_CD = getSession().getProperty(ATTR_DI_ACTN_CD);

         if (lsDI_ACTN_CD == null || lsDI_ACTN_CD.equals(""))
         {
            //super.save();
            return true;
         }
         else
         {
            miDI_ACTN_CD = Integer.parseInt(lsDI_ACTN_CD);

            switch (miDI_ACTN_CD)
            {
               case DOC_ACTN_DISCARD :
               case DOC_ACTN_VALIDATE :
               case DOC_ACTN_SUBMIT :
               case DOC_ACTN_OPEN :
               case DOC_ACTN_APPROVE :
               case DOC_ACTN_UNAPPROVE :
               case DOC_ACTN_BYPS_APRV :
               case DOC_ACTN_REJECT :
               case DOC_ACTN_REJECT_ALL :
                  doDocAction( miDI_ACTN_CD ) ;
                  break ;
               case DOC_ACTN_REASSIGN:
               {
                  getSession().setProperty(ATTR_DI_ACTN_CD,"");
                  boolean lboolAuthorized = false;
                  try
                  {
                     lboolAuthorized = AMSSecurity.actionAuthorized(getSession().getUserID(),
                        getDOC_CD(), DOC_ACTN_REASSIGN);
                  }
                  catch(Exception loe)
                  {
                     lboolAuthorized = false;
                  }
                  finally
                  {
                     if(!lboolAuthorized)
                     {
                        raiseException("Not authorized to perform selected action.");
                        getSession().setProperty(ATTR_DI_RESP_TXT, "012");
                        return false;
                     }
                  }
                  if(WF_APRV_SHImpl.isSelfAprvRestricted(DOC_ACTN_REASSIGN, getDOC_CD(), getDOC_DEPT_CD(),
                        getDOC_ID(), getDOC_VERS_NO(), getAPRV_LVL(), getSLF_APRV_RSCT(),getDOC_CREA_USID(),
                        getDOC_SBMT_ID(), getASSIGNEE(), getSession()))
                  {
                     return false;
                  }
                  return true;
               }// end case DOC_ACTN_REASSIGN:
               default :
                  //super.save();
                  return true;
               //break;
            } /* end switch (miDI_ACTN_CD) */
         } /* end if (lsDI_ACTN_CD == null || lsDI_ACTN_CD.equals("") */
         return false;
      }

      /**
       * The following method will grab the document action
       * and force a save on the appropriate VLS component.
       *
       * @author Mark Farrell - initial version 07/02/2000
       *  Michael Levy        - 07/14/00 - handle approval action processing
       *                                   in another method
       *  Subramanyam Surabhi - 09/21/00 - Handle bypass approval action
       *  Kiran Hiranandani   - 02/21/03 - Validate user action for bypass approval
       *
       */
      private void doDocAction( int fiAction )
      {
         String lsDocCat = null;
         String lsDocType = null;
         String lsDocCd = null;
         String lsDocDept = null;
         String lsDocId = null;
         String lsDocVersNo = null;
         int liIndex = 1 ;
         Session loSession = getSession();
         String lsDI_MSG_TXT = loSession.getProperty(ATTR_DI_MSG_TXT);
         boolean lboolResult = false ;
            lsDI_MSG_TXT = loSession.getProperty(ATTR_DI_MSG_TXT);
         liIndex = 1; // Skip the number of parameters.
            // Get DOC_CATEGORY
         lsDocCat = getMsgValue(lsDI_MSG_TXT, liIndex);
         liIndex += lsDocCat.length() + 2;

         // Get DOC_TYPE
         lsDocType = getMsgValue(lsDI_MSG_TXT, liIndex);
         liIndex += lsDocType.length() + 2;

         // Get DOC_CD
         lsDocCd = getMsgValue(lsDI_MSG_TXT, liIndex);
         liIndex += lsDocCd.length() + 2;

         // Get DOC_DEPT
         lsDocDept = getMsgValue(lsDI_MSG_TXT, liIndex);
         liIndex += lsDocDept.length() + 2;

         // Get DOC_ID
         lsDocId = getMsgValue(lsDI_MSG_TXT, liIndex);
         liIndex += lsDocId.length() + 2;

         // Get DOC_VERS_NO
         lsDocVersNo = getMsgValue(lsDI_MSG_TXT, liIndex);

         // handle approval actions separately, now that the information
         // has been retrieved
         if ((fiAction == DOC_ACTN_APPROVE) ||
             (fiAction == DOC_ACTN_UNAPPROVE) ||
             (fiAction == DOC_ACTN_REJECT) ||
             (fiAction == DOC_ACTN_REJECT_ALL))
         {
            doAprvAction(fiAction, lsDocCat, lsDocType, lsDocCd,
                         lsDocDept, lsDocId, lsDocVersNo);
            return;
         }
         else if (fiAction == DOC_ACTN_OPEN)
         {
            SearchRequest loKey = new SearchRequest();
            DOC_HDRImpl loDocHdr = null;
               // create the search key and perform getObjectByKey
            loKey.addParameter("DOC_HDR","DOC_CD",lsDocCd);
            loKey.addParameter("DOC_HDR","DOC_DEPT_CD",lsDocDept);
            loKey.addParameter("DOC_HDR","DOC_ID",lsDocId);
            loKey.addParameter("DOC_HDR","DOC_VERS_NO",lsDocVersNo);
            loDocHdr = (DOC_HDRImpl) DOC_HDRImpl.getObjectByKey(loKey,
                                                                loSession);

            // force save
            if (loDocHdr != null)
            {
               loDocHdr.getData(ATTR_DOC_ACTN_CD).setint(DOC_ACTN_OPEN);
               loDocHdr.save();
            }
            else
            {
               raiseException("error processing action");
               loSession.setProperty(ATTR_DI_RESP_TXT, "012");
            }
            return;
         }
         else if (fiAction == DOC_ACTN_BYPS_APRV)
         {
            boolean lboolAuthorized = false ;

            /* check if the user has authority to Bypass approvals */
            try
            {
               lboolAuthorized = AMSSecurity.actionAuthorized(
                     loSession.getUserID(), lsDocCd, fiAction);
            } /* end try */
            catch (Exception loEx)
            {
               lboolAuthorized = false;
            } /* end catch */
            finally
            {
               if (!lboolAuthorized)
               {
                  raiseException("Not authorized to perform selected action.");
                  loSession.setProperty(ATTR_DI_RESP_TXT, "012");
                  return;
               } /* end if (!lboolAuthorized) */
            } /* end finally */

            try
            {
               AMSDocHeader loDocHdr = getAMSDocHeader(loSession, lsDocCat,
                  lsDocType, lsDocCd, lsDocDept, lsDocId, lsDocVersNo);
               boolean lboolAsynchronousProcess = false ;

               if (moDocCtrl == null)
               {
                  moDocCtrl = loDocHdr.getDocControlInfo( getData(ATTR_DOC_CD).getString() ) ;
               }

               if (moDocCtrl != null)
               {
                  lboolAsynchronousProcess = moDocCtrl.getWF_ASYNC_PROC_FL();
               }

               AMSInternalWFEventImpl loIntWf = new AMSInternalWFEventImpl(lboolAsynchronousProcess);
               if (!loIntWf.docBypassApproval(loDocHdr))
               {
                  raiseException("Failed to Apply Bypass Approvals");
                  loSession.setProperty(ATTR_DI_RESP_TXT, "012");
               }
               else
               {
                  if(lboolAsynchronousProcess)
                  {
                    /* saving the document information since the docBypassApproval
                     * method may have set ready and finalApproval flags
                     * for asynchronous processing.
                     */
                    loDocHdr.save() ;
                  }
               }
            }
            catch(Exception loExcep)
            {
              // Add exception log to logger object
              moAMSLog.error("Unexpected error encountered while processing. ", loExcep);

               raiseException("Failed to Apply Bypass Approvals");
               loSession.setProperty(ATTR_DI_RESP_TXT, "012");
            }
            return;
         }

         try
         {
            AMSDocHeader loDocHdr = getAMSDocHeader(loSession, lsDocCat,
                  lsDocType, lsDocCd, lsDocDept, lsDocId, lsDocVersNo);
            // force save()
            if ( loDocHdr != null)
            {
               loDocHdr.getData(ATTR_DOC_ACTN_CD).setint(fiAction);
               loDocHdr.save() ;
            }
         }
         catch (Exception loEx)
         {
            raiseException("error processing action");
            loSession.setProperty(ATTR_DI_RESP_TXT, "012");
         }
     }

   /**
    * The following method handles the approval actions.
    *
    * @param fiAction the approval action
    * @param fsDocCat the document category
    * @param fsDocTyp the document type
    * @param fsDocCd the document code
    * @param fsDocDept the document department code
    * @param fsDocId the document ID
    * @param fsDocVers the document version number (left as String to be
    *  used in lookup)
    *
    */
   private void doAprvAction(int fiAction, String fsDocCat, String fsDocTyp,
         String fsDocCd, String fsDocDept, String fsDocId, String fsDocVers)
   {
      boolean lboolAuthorized = false;
      boolean lboolAppropriateAction;
      boolean lboolAction = false;
      Session loSession = getSession();

      // no need to do any processing if the worklist entry isn't present;
      // this is possible if processing by other users has taken place
      // while this user wasn't "looking"
      if (!findWorklistEntry(loSession, getWRK_LST_TYP(), getWRK_LST_ID()))
      {
         raiseException("Worklist entry for " + fsDocCd + " " +
                        fsDocDept + " " + fsDocId + " for approval " +
                        "level " + getAPRV_LVL() + " is no longer " +
                        "available.");
         loSession.setProperty(ATTR_DI_RESP_TXT, "011");
         return;
      }

      // Check security to validate this action for current user
      try
      {
         lboolAuthorized = AMSSecurity.actionAuthorized(
               loSession.getUserID(), fsDocCd, fiAction);
      }
      catch (Exception loEx)
      {
         lboolAuthorized = false;
      }
      finally
      {
         if (!lboolAuthorized)
         {
            //Invalid action, abort action processing
            raiseException("Not authorized to perform selected action.");
            loSession.setProperty(ATTR_DI_RESP_TXT, "012");
            return;
         }
      }

      // A worklist entry with an approval level of 0 does not indicate
      // a pending approval action.  Rather, the original submitter into
      // Pending has received his work back because the assignees have not
      // given their approvals.  Therefore, no approval action can be done.
      if (getAPRV_LVL() == 0)
      {
         raiseException("You cannot do an approval action.  Please " +
                        "check the reason and comment information.");
         loSession.setProperty(ATTR_DI_RESP_TXT, "012");
         return;
      }

      // An approval action can be done only if the user is assigned to do
      // the work.  This assignment can be in the form of user or role.
      // However, if the user's role is assigned the work, the approval
      // action cannot be performed against work still in the role's
      // worklist.  The user must've already taken the work, so that the
      // worklist item appears only in his worklist.

      try
      {
         boolean lboolAssigneeFl = getASSIGNEE_FL();

         if (!lboolAssigneeFl)
         {
            String lsAssignee = getASSIGNEE().trim();

            if (!lsAssignee.equalsIgnoreCase(loSession.getUserID()))
            {
               raiseException("You are not assigned to this task.  You " +
                              "cannot do an approval action.");
               loSession.setProperty(ATTR_DI_RESP_TXT, "012");
               return;
            }
         }
         else
         {
            String lsLockUsid = getLOCK_USID();

            if (!isRoleManager(getASSIGNEE().trim()))
            {
               if (lsLockUsid != null)
               {
                  String lsLockUsidTrimmed = lsLockUsid.trim();

                  if (lsLockUsidTrimmed.equals("") ||
                        !lsLockUsidTrimmed.equalsIgnoreCase(loSession.getUserID()))
                  {
                     raiseException("You have not taken this task.  You cannot " +
                                 " do an approval action.");
                     loSession.setProperty(ATTR_DI_RESP_TXT, "012");
                     return;
                  } /* end if ... */
               } /* end if(lsLockUsid != null) */
               else
               {
                  // worklist item not even locked; approval action cannot occur
                  raiseException("You must take the task before you can work on it.");
                  loSession.setProperty(ATTR_DI_RESP_TXT, "012");
                  return;
               } /* end else */
            } /* end   if (!isRoleManager(getASSIGNEE().trim()) */
         } /* end else */
      }  /* end try */
      catch (Exception loEx)
      {
         raiseException("error accessing worklist information for " +
                        fsDocCd + " " + fsDocDept + " " + fsDocId +
                        " " + fsDocVers);
         loSession.setProperty(ATTR_DI_RESP_TXT, "012");
         return;
      }

      // permit only valid actions to be processed, based on why the worklist
      // entry is present
      lboolAppropriateAction = true;
      switch (getREAS())
      {
         case CVL_WF_REASImpl.REASON_RJCT_UNAPRV_APRVL:
            // this case should not occur since the approval level check
            // was done above
            raiseException("You cannot do an approval action.  Please " +
                           "check the reason and comment information.");
            lboolAppropriateAction = false;
            break;
         case CVL_WF_REASImpl.REASON_APPLY_APRVL:
            if (fiAction == DOC_ACTN_UNAPPROVE)
            {
               raiseException("Approval action is not allowed.  You do not " +
                              "have an approval to remove.");
               lboolAppropriateAction = false;
               break;
            }
            break;
         case CVL_WF_REASImpl.REASON_CNFRM_REMOVE_APRVL:
            if ((fiAction == DOC_ACTN_REJECT) ||
                (fiAction == DOC_ACTN_REJECT_ALL))
            {
               raiseException("A reject action is not allowed as the approval " +
                              "has already been applied. Please " +
                              "Unapprove or Reapply your approval.");
               lboolAppropriateAction = false;
               break;
            }
            break;
         default:
            raiseException("Approval action is not allowed based on the " +
                           "reason.");
            lboolAppropriateAction = false;
            break;
      }
      if (!lboolAppropriateAction)
      {
         loSession.setProperty(ATTR_DI_RESP_TXT, "012");
         return;
      }

      // get the corresponding AMSDocHeader and pass the request to
      // approval processing / workflow
      try
      {
         AMSDocHeader loDocHeader = getAMSDocHeader(loSession, fsDocCat,
               fsDocTyp, fsDocCd, fsDocDept, fsDocId, fsDocVers);
         boolean lboolAsynchronousProcess = false ;

         if (moDocCtrl == null)
         {
            moDocCtrl = loDocHeader.getDocControlInfo( getData(ATTR_DOC_CD).getString() ) ;
         }

         if (moDocCtrl != null)
         {
            lboolAsynchronousProcess = moDocCtrl.getWF_ASYNC_PROC_FL() ;
         }

         AMSInternalWFEventImpl loIntWf = new AMSInternalWFEventImpl(lboolAsynchronousProcess);

         switch (fiAction)
         {
            case DOC_ACTN_APPROVE:
               lboolAction = loIntWf.docApprove(loDocHeader);
               if(lboolAsynchronousProcess)
               {
                  /* saving the document information since the docApprove
                   * method may have set ready and finalApproval flags
                   * for asynchronous processing.
                   */
                  loDocHeader.save() ;
               }
               break;
            case DOC_ACTN_UNAPPROVE:
               lboolAction = loIntWf.docUnapprove(loDocHeader);
               break;
            case DOC_ACTN_REJECT:
               lboolAction = loIntWf.docReject(loDocHeader);
               break;
            case DOC_ACTN_REJECT_ALL:
               lboolAction = loIntWf.docRejectAll(loDocHeader, false);
               lboolAction = loDocHeader.processAutoSubmit(
                               DOC_PHASE_DRAFT, DOC_STA_CD_REJECTED);
               break;
            default:
               break;
         }
      }
      catch (Exception loEx)
      {
         raiseException("error performing approval action");
         lboolAction = false;
      }
         // lboolAction is true => no error; otherwise, send back error
      loSession.setProperty(ATTR_DI_RESP_TXT, lboolAction ? "010" : "012");
   }

   /**
    * Helper method to find a worklist entry.
    * @param Session
    * @param fsType
    * @param flId
    * @return boolean true if the entry is found
    */
   private static boolean findWorklistEntry(Session foSession, String fsType,
         long flId)
   {
      WF_APRV_WRK_LSTImpl loTestRec = null;
      Enumeration loTestRecs = null;
      Parameter loParam;
      SearchRequest loReq = new SearchRequest();
      boolean lboolTestRecFound = false;

      loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_TYP_ATTR;
      loParam.value = fsType;
      loReq.add(loParam);

      loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_WRK_LST_ID_ATTR;
      loParam.value = Long.toString( flId );
      loReq.add(loParam);

      try
      {
         loTestRecs = getObjects(loReq, foSession);
         while (loTestRecs.hasMoreElements())
         {
            loTestRec = (WF_APRV_WRK_LSTImpl) loTestRecs.nextElement();
            lboolTestRecFound = (loTestRec != null);
            break;
         }
      }
      catch (Exception loEx)
      {
         lboolTestRecFound = false;
      }

      return lboolTestRecFound;
   }

   /**
    * This method returns an instance of the corresponding AMSDocHeader
    * object for a given set of document parameters.
    * @param foSession
    * @param fsDocCat
    * @param fsDocTyp
    * @param fsDocCd
    * @param fsDocDept
    * @param fsDocId
    * @param fsDocVers
    * @return AMSDocHeader
    */
   private static AMSDocHeader getAMSDocHeader(Session foSession,
         String fsDocCat, String fsDocTyp, String fsDocCd,
         String fsDocDept, String fsDocId, String fsDocVers)
         throws Exception
   {
      String lsValue = foSession.getPackageName() + "." +
                       fsDocTyp + "_DOC_HDRImpl";
      Class loClass = Class.forName(lsValue);
      DataObject loDO = (DataObject) loClass.newInstance();
      DataObject loNewDO = null;
      String lsCompName = loDO.getComponentName();
      Class[] loParmTypes = new Class[2];
      Method loMethod = null;
      SearchRequest loSearchReq = new SearchRequest();
      Parameter loParm = null;
      Object[] loArgs = new Object[2];
      AMSDocHeader loDocHeader = null;

      loParmTypes[0] = SearchRequest.class;
      loParmTypes[1] = Session.class;
      loMethod = loClass.getMethod("getObjectByKey", loParmTypes);

      loParm = new Parameter(lsCompName, ATTR_DOC_CD, fsDocCd);
      loSearchReq.add(loParm);

      loParm = new Parameter(lsCompName, ATTR_DOC_DEPT_CD, fsDocDept);
      loSearchReq.add(loParm);

      loParm = new Parameter(lsCompName, ATTR_DOC_ID, fsDocId);
      loSearchReq.add(loParm);

      loParm = new Parameter(lsCompName, ATTR_DOC_VERS_NO, fsDocVers);
      loSearchReq.add(loParm);

      loArgs[0] = loSearchReq;
      loArgs[1] = foSession;

      loNewDO = (DataObject) loMethod.invoke(loDO, loArgs);
      loDocHeader = (AMSDocHeader) loNewDO;

      if (loDocHeader == null)
      {
         throw new Exception("invalid AMSDocHeader");
      }
      return loDocHeader;
   }

   /**
    * This method deletes records from the worklist, based on doc_cd, doc_id,
    * doc_dept_cd, and doc_versno.
    *
    * Modification Log : Mark Farrell - 06/21/2000
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fsDoc_Cd       - the value for DOC_CD
    * @param fsDoc_Id       - the value for DOC_ID
    * @param fsDoc_Dept_Cd  - the value for DOC_DEPT_CD
    * @param fiDoc_Vers_No  - the value for DOC_VERS_NO
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, String fsDoc_Cd, String fsDoc_Id,
                                String fsDoc_Dept_Cd, int fiDoc_Vers_No)
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_CD;
      loParam.value = fsDoc_Cd;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_ID;
      loParam.value = fsDoc_Id;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_DEPT_CD;
      loParam.value = fsDoc_Dept_Cd;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_VERS_NO;
      loParam.value = String.valueOf(fiDoc_Vers_No);
      loRequest.add(loParam);
            try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            llNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return llNumDeleted;
   }

   /**
    * This method deletes records from the worklist, based on doc_cd, doc_id,
    * doc_dept_cd, doc_versno, and approval level.
    *
    * Modification Log : Mark Farrell - 06/21/2000
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fsDoc_Cd       - the value for DOC_CD
    * @param fsDoc_Id       - the value for DOC_ID
    * @param fsDoc_Dept_Cd  - the value for DOC_DEPT_CD
    * @param fiDoc_Vers_No  - the value for DOC_VERS_NO
    * @param fiAprvLevel    - the value for APRV_LVL
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, String fsDoc_Cd, String fsDoc_Id,
                                String fsDoc_Dept_Cd, int fiDoc_Vers_No,
                                int fiAprvLevel )
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_CD;
      loParam.value = fsDoc_Cd;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_ID;
      loParam.value = fsDoc_Id;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_DEPT_CD;
      loParam.value = fsDoc_Dept_Cd;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_VERS_NO;
      loParam.value = String.valueOf(fiDoc_Vers_No);
      loRequest.add(loParam);

      loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_APRV_LVL_ATTR;
      loParam.value = String.valueOf(fiAprvLevel);
      loRequest.add(loParam);
            try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            llNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return llNumDeleted;
   }

   /**
    * This method deletes records from the worklist, based on doc_cd, doc_id,
    * doc_dept_cd, doc_versno, approval level, assignee, and assignee flag.
    *
    * Modification Log : Mark Farrell - 06/21/2000
    *                                 - inital version
    *
    * @param foSession      - the current session
    * @param fsDoc_Cd       - the value for DOC_CD
    * @param fsDoc_Id       - the value for DOC_ID
    * @param fsDoc_Dept_Cd  - the value for DOC_DEPT_CD
    * @param fiDoc_Vers_No  - the value for DOC_VERS_NO
    * @param fiAprvLevel    - the value for APRV_LVL
    * @param fsAssignee     - the value for ASSIGNEE
    * @param fboolRole      - the value for ASSIGNEE_FL
    * @return long          - the number of records deleted, -1 if an
    *                         error occurred
    */
   public static long delete(Session foSession, String fsDoc_Cd, String fsDoc_Id,
                                String fsDoc_Dept_Cd, int fiDoc_Vers_No,
                                int fiAprvLevel, String fsAssignee,
                                boolean fboolRole )
   {
      WF_APRV_WRK_LSTImpl loWRec = null;
      long llNumDeleted = 0L;
      SearchRequest loRequest = new SearchRequest();
      Parameter loParam;
      Enumeration loWorklistRecs = null;
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_CD;
      loParam.value = fsDoc_Cd;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_ID;
      loParam.value = fsDoc_Id;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_DEPT_CD;
      loParam.value = fsDoc_Dept_Cd;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_DOC_VERS_NO;
      loParam.value = String.valueOf(fiDoc_Vers_No);
      loRequest.add(loParam);

      loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_APRV_LVL_ATTR;
      loParam.value = String.valueOf(fiAprvLevel);
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_ATTR;
      loParam.value = fsAssignee;
      loRequest.add(loParam);
         loParam = new Parameter();
      loParam.objName = C_WF_APRV_WRK_LST_TBL;
      loParam.fieldName = C_ASSIGNEE_FL_ATTR;
      loParam.value = String.valueOf(fboolRole);
      loRequest.add(loParam);
            try
      {
         loWorklistRecs = getObjects(loRequest, foSession);
      }
      catch (Exception loEx)
      {
         return -1L;
      }
      try
      {
         while (loWorklistRecs.hasMoreElements())
         {
            loWRec = (WF_APRV_WRK_LSTImpl) loWorklistRecs.nextElement();
            loWRec.setDelete();
            loWRec.save(true);
            llNumDeleted++;
         } // end while (loWorklistRecs.hasMoreElements())
      }
      catch (Exception loEx)
      {
         // simply return how many records were deleted

         loWRec.raiseException("error deleting from WF_APRV_WRK_LST");
      }
         return llNumDeleted;
   }

   /**
    * This method returns true if the current user is a manager of the
    * specified role and false otherwise.
    *
    * Modification Log : Kiran Hiranandani - 08/21/2002
    *                                      - inital version
    *
    * @param fsRoleID  ID of the role
    */
   public boolean isRoleManager( String fsRoleID )
   {
      SearchRequest loSearchReq = new SearchRequest() ;
      Session       loSession = getSession();

      loSearchReq.addParameter( "R_WF_USER_ROLE", C_ROLEID_ATTR, fsRoleID ) ;
      loSearchReq.addParameter( "R_WF_USER_ROLE", "USID", loSession.getUserID() ) ;

      R_WF_USER_ROLEImpl loRoleRec =
            (R_WF_USER_ROLEImpl) R_WF_USER_ROLEImpl.getObjectByKey( loSearchReq, loSession ) ;
      return loRoleRec.getROLE_MGR_FL() ;
   } /* end isRoleManager() */

   protected int getApplID()
   {
      int liApplID = getAPPL_ID() ;

      if ( liApplID > 0 )
      {
         return liApplID ;
      } /* end if ( liApplID > 0 ) */
      else
      {
         SearchRequest      loSrchReq = new SearchRequest() ;
         R_GEN_DOC_CTRLImpl loDocCtrl ;

         loSrchReq.addParameter( "R_GEN_DOC_CTRL", ATTR_DOC_CD, getDOC_CD() ) ;
         loDocCtrl = (R_GEN_DOC_CTRLImpl)R_GEN_DOC_CTRLImpl.getObjectByKey( loSrchReq, getSession() ) ;

         if ( loDocCtrl != null )
         {
            return loDocCtrl.getAPPL_ID() ;
         } /* end if ( loDocCtrl != null ) */
      } /* end else */
      return -1 ;
   } /* end getApplID() */

   /**
    * Returns true when the given behavior is enabled.
    * This method overrides the parent(BaseImpl) method. This method had been
    * added so that when other classes calls this static method
    * V_WF_APRV_WRK_LSTImpl.isBehaviorEnabled(..) then the static class block
    * in this class that adds specific behavior is first executed and
    * correct results are returned from this method.
    * Without this method overridden here, when static method
    * V_WF_APRV_WRK_LSTImpl.isBehaviorEnabled(..) is called from other classes
    * then the parent(BaseImpl) isBehaviorEnabled(..) is called
    * and the static class block of this class that adds specific behavior is not
    * executed and hence correct behavior is not obtained.
    * @param int fiBehavior Behavior to search for
    * @return boolean as explained above.
    */
   public static boolean isBehaviorEnabled( int fiBehavior )
   {
      return V_WF_APRV_WRK_LSTBaseImpl.isBehaviorEnabled(fiBehavior);
   }//end of method

   /**
    * Initiates Escalated Routing on the given Worklist item- a new row is
    * created in the Worklist with values copied from the current row with
    * Escalated Item (ESCL_ITEM) set as 1 (Yes).
    *
    * @param fiEscalationLevel Escalation level
    * @param foWorksheet Worksheet object
    * @param foWorklistItem
    *           Worklist item on which the Escalated Routing has to be performed
    */
   public void performEscalatedRouting(int fiEscalationLevel, WF_APRV_SHImpl foWorksheet)
   {
      // These conditions should never occur
      // This item is being escalated to an escalation level not yet supported by system
      // hence raise an error
      if (fiEscalationLevel > AMSCommonConstants.MAX_ESCL_LVL)
      {
         raiseException("Escalation cannot be performed beyond Escalation Level "
                     + AMSCommonConstants.MAX_ESCL_LVL);
         return;
      }

      // This item is being escalated even though it has already been escalated to
      // maximum Escalation Level possible, hence raise an error
      if ( getESCL_LEVEL() == AMSCommonConstants.MAX_ESCL_LVL)
      {
         raiseException("This item cannot be escalated as it has already "
                     + "been escalated "
                     + AMSCommonConstants.MAX_ESCL_LVL
                     + " number of times.");
         return;
      }

      // Get the Approval Level
      int liApprovalLevel = getAPRV_LVL();
      // Get Escalate Assignee for given Escalation and Approval Level
      String lsEscalateAssignee = foWorksheet.getESCL_ASGN_ID(fiEscalationLevel, liApprovalLevel);
      // Get the Role flag for the Assignee
      boolean lboolIsAssigneeRole = foWorksheet.getESCL_ASGN_ROLE_FL(fiEscalationLevel, liApprovalLevel);
      // Perform Escalated Routing
      escalateItem(fiEscalationLevel, lsEscalateAssignee, lboolIsAssigneeRole);
   }// end of method

   /**
    * Performs Escalated Routing on the current Worklist item. A new
    * WF_APRV_WRK_LST object is created and is marked as Escalated Item = 1 (Yes)
    *
    * @param fiEscalationLevel Escalation level
    * @param fsAssignee User Id to whom escalation has to be performed
    * @param fboolIsAssigneeRole Specifies if the Assignee is a Role
    */
   private void escalateItem(int fiEscalationLevel, String fsAssignee, boolean fboolIsAssigneeRole)
   {
      // Create list of attributes to be excluded from copying
      Vector<String> loExcludeVector = new Vector<String>(5);
      loExcludeVector.add("ASSIGNEE");
      loExcludeVector.add("ASSIGNEE_FL");
      loExcludeVector.add("ASGN_DT");

      // Perform Escalated Routing
      V_WF_APRV_WRK_LSTImpl loEscalatedRouteItem = V_WF_APRV_WRK_LSTImpl
            .getNewObject(getSession(), true);
      loEscalatedRouteItem.copyCorresponding(this, loExcludeVector,
            true);

      // Set Escalate User info
      loEscalatedRouteItem.setASSIGNEE(fsAssignee);
      loEscalatedRouteItem.setASSIGNEE_FL(fboolIsAssigneeRole);

      // Set Assigned Date
      loEscalatedRouteItem.setASGN_DT(new VSDate());

      // Set Escalation info
      loEscalatedRouteItem.setESCL_ITEM(CVL_YES_NOImpl.YES);
      loEscalatedRouteItem.setESCL_LEVEL(fiEscalationLevel);

      // Save the escalated DO
      loEscalatedRouteItem.save();

      // update current record for escalation
      setESCL_LEVEL(fiEscalationLevel);
      this.save();

      // Log the event
      WF_APRV_PROC_LOGImpl.logAprvProcEvent(getSession(), getDOC_CD(),
            getDOC_DEPT_CD(), getDOC_ID(), getDOC_VERS_NO(),
            WF_APRV_PROC_LOGImpl.EVNT_TYP_ESCALATE, "Item escalated to " + fsAssignee);
   }// end of method
}

