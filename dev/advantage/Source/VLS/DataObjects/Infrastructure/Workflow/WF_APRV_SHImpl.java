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

import com.amsinc.gems.adv.common.wf.*;

/*
**  WF_APRV_SH
*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_APRV_SHImpl extends  WF_APRV_SHBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}

{
   
   /** Constant for Underscore */
   private static final String UNDER_SCORE = "_";
   
   /** String Builder to create colunm names */
   private StringBuilder moColumnName = new StringBuilder(20);
   
//{{COMP_CLASS_CTOR
public WF_APRV_SHImpl (){
	super();
}

public WF_APRV_SHImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}


   }



//{{COMPONENT_RULES
	public static WF_APRV_SHImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new WF_APRV_SHImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}


//{{EVENT_CODE

//END_EVENT_CODE}}


   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}

   }

   public void myInitializations()
   {
      setSaveBehaviorMode( AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE ) ;
      mboolCustomDataObjectSave = true ;
   }
    /*
     * Approval Action status
     */
     /* Removed the following constants DOC-ADFX1488 */
     /*
      * public static final int APRV_ACTN_STA_PEND = 0;
      * public static final int APRV_ACTN_STA_APPROVE = 1;
      * public static final int APRV_ACTN_STA_REJECT = 2;
      * public static final int APRV_ACTN_STA_UNAPPROVE = 3;
      * public static final int APRV_ACTN_STA_RECALL = 4;
      * public static final int APRV_ACTN_STA_BYPASS = 5;
      */

         /*
    * Approval level attribute names
    * (Occurrence number needs to be concatenated if used)
    */
      private static final String ORD_NO         = "ORD_NO_";
      private static final String APRV_ASGN      = "APRV_ASGN_";
      private static final String APRV_ASGN_FL   = "APRV_ASGN_FL_";
      private static final String ASGN_DT        = "APRV_DT_";
      private static final String APRV_ACTN_STA  = "APRV_ACTN_STA_";
      private static final String APRV_ACTN_USID = "APRV_ACTN_USID_";

      /**
       * Placeholder replacement variable for storing notification template
       * for approval level; value of "%lvl%"
       */
      public static final String REPLACE_NOTIF_TMPL_APRV_LVL = "%lvl%";

      /*
    * Approval Sheet object names
    */
            private static final String OBJECT_NAME = "WF_APRV_SH";
   /*
    * Approval Sheet key attribute names
    */
            private static final String ATTR_SEQ_NO      = "SEQ_NO";

   /*
    * Approval level bit masks
    */
      //Aprv Lvl Change: changed the size from 10 to MAX_APR_LVL_NO
      //public static final short NUM_APPROVAL_LEVELS = 10;
      public static final short NUM_APPROVAL_LEVELS = MAX_APR_LVL_NO;
      public static final long APRV_LVL_MASKS[] = new long[NUM_APPROVAL_LEVELS];
   /*
    * Static initializer for the approval level mask array APRV_LVL_MASKS
    */
   static
   {
      APRV_LVL_MASKS[0] = 1;
      for (int liCtr = 1; liCtr < NUM_APPROVAL_LEVELS; liCtr++)
      {
         APRV_LVL_MASKS[liCtr] = APRV_LVL_MASKS[liCtr - 1] * 2 ;
      }

      /*
       * APRV_LVL_MASKS is an Array with capacity MAX_APR_LVL_NO (20).
       * At array's 0 index is stored a bit pattern where bit at 0 position is 1, ie. 00001(binary)=1(int)
       * At array's 1 index is stored a bit pattern where bit at 1 position is 1, ie. 00010(binary)=2(int)
       * At array's 2 index is stored a bit pattern where bit at 2 position is 1, ie. 00100(binary)=4(int)
       * At array's 3 index is stored a bit pattern where bit at 3 position is 1, ie. 01000(binary)=8(int)
       * At array's 4 index is stored a bit pattern where bit at 4 position is 1, ie. 10000(binary)=16(int)
       * and so on.
       */
   }//end

   /**
   * Load the current document's approval sheet record, update attributes with
   * the status, current user ID, and the assigned approval level and call the
   * DataObject method save() to update the approval sheet record to the database.
   * @return approve : if succcessful returns boolean true.
   */

   public static boolean approve(Session foSession,
                              String fsDocCode,
                              String fsDocDeptCode,
                              String fsDocID,
                              int fiDocVer,
                              int fiApprovalLevel)
   {

      int liApprShtIndex=0;
      int liAprShtCnt;
      int liCurrShtSeqNo=0;
      WF_APRV_SHImpl loApprovalSheetList[] = new WF_APRV_SHImpl[AMSCommonConstants.MAX_APRV_SEQ_NO+1] ;
      String lsHashCode;

      WF_APRV_SHImpl    loAprvSheetObj = null;
      Parameter      loParam = null;
      SearchRequest  loSearchReq = new SearchRequest();
       String        lsAprvUsid = null;
       String        lsUserId = foSession.getUserID();

      //Search for WF_APRV_SH records for the given Document
      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_CD ;
      loParam.value = fsDocCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_DEPT_CD ;
      loParam.value = fsDocDeptCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_ID ;
      loParam.value = fsDocID ;
      loSearchReq.add(loParam);

      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_VERS_NO ;
      loParam.value = String.valueOf(fiDocVer);
      loSearchReq.add(loParam);

      Vector loSortAttrs = new Vector( 5 ) ;

      //Get the WF_APRV_SH records in ascending order of SEQ_NO
      loSortAttrs.addElement(ATTR_DOC_CD) ;
      loSortAttrs.addElement(ATTR_DOC_DEPT_CD) ;
      loSortAttrs.addElement(ATTR_DOC_ID) ;
      loSortAttrs.addElement(ATTR_DOC_VERS_NO) ;
      loSortAttrs.addElement(ATTR_SEQ_NO) ;

      Enumeration liApprovalSheetList = (WF_APRV_SHImpl.getObjectsSorted(loSearchReq ,foSession,loSortAttrs,AMSDataObject.SORT_ASC));

      //Get SEQ_NO for the given Approval Level to trace its WF_APRV_SH record
      liCurrShtSeqNo = AMSApprovalProcessor.getSeqNoForAprvLvlAndOrdNo(fiApprovalLevel);

      //For each WF_APRV_SH record for the given Document
      while(liApprovalSheetList.hasMoreElements())
      {
         loApprovalSheetList[liApprShtIndex] = (WF_APRV_SHImpl)liApprovalSheetList.nextElement();
         if(loApprovalSheetList[liApprShtIndex].getSEQ_NO() == liCurrShtSeqNo)
         {
            //Get WF_APRV_SH record for the passed in Approval Level
            loAprvSheetObj = loApprovalSheetList[liApprShtIndex];
         }
         liApprShtIndex++;
      }//end while

      //WF_APRV_SH record not found
      if ( loAprvSheetObj == null )
      {
         return false;
      }

      lsAprvUsid = loAprvSheetObj.getAPRV_ACTN_USID(fiApprovalLevel);
      //Case where Action User ID is populated(Workflow Action already taken on the Approval Level)
      if ((lsAprvUsid != null) && !lsAprvUsid.trim().equals(""))
      {
         loAprvSheetObj.raiseException("approval level " + fiApprovalLevel +
                                         " is already processed");
         return false;
      }
      else
      {
         //Check if the Action is permitted as per Self-Approval restriction on Approval Rule record
         if(isSelfAprvRestricted(DOC_ACTN_APPROVE,fsDocCode, fsDocDeptCode, fsDocID,
            fiDocVer, fiApprovalLevel, loAprvSheetObj.getSLF_APRV_RSCT(),
            loAprvSheetObj.getDOC_CREA_USID(), loAprvSheetObj.getSBMT_ID(),
            foSession.getUserID(), foSession))
         {
            //Action is restricted
            return false;
         }
         /*
          * Case where Single Approvals Enforced is true and the User has earlier already approved
          * any of the Approval Levels for the Document.
          */
         if (loAprvSheetObj.getAPRV_SNG_FL()
             && hasUserAlreadyApproved(loApprovalSheetList, foSession))
         {
            AMSDataObject.raiseException("%c:Q0125%", foSession);
            WF_APRV_PROC_LOGImpl.logAprvProcEvent(foSession, fsDocCode,
                  fsDocDeptCode, fsDocID, fiDocVer,
                  WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL, "approve"
                  + "failed for user" + loAprvSheetObj.getDOC_CREA_USID()
                  + "on approval level" + fiApprovalLevel);

            return false;
         }
         
         //ADV_ PDF Signature
         AMSDocHeader loHeader = AMSDocHeader.getDocByKey(foSession, fsDocCode, fsDocDeptCode, fsDocID, fiDocVer);
         /*
          * At this point approve rights and other edits are done. After this check actual
          * document escalation process starts.  Refer to keyword: ESIGNATURE_LOGIC in AMSDocHeader.
          */
         if(loHeader.getSignatureType() != CVL_DOC_SIG_TYPImpl.DOC_SIG_TYP_NONE)
         {
            if(loHeader.isUserSignSubmitted())
            {
               AMSDataObject.setTransactionUserObject(
                     TRANSAC_INFO_DOC_SIGN_STATUS, new Integer(
                           TRANSAC_INFO_USER_SIGNED), foSession);
            }
            else
            {
               /*
                * Document is setup for signatures and User has not yet accepted
                * to sign the Document. Hence set the Document Transaction
                * object to Request sign status then generate the Document for
                * signing and stop further Workflow processing.
                */
               AMSDataObject.setTransactionUserObject(
                     TRANSAC_INFO_DOC_SIGN_STATUS, new Integer(
                           TRANSAC_INFO_REQUEST_SIGN), foSession);
               
               loHeader.generateDocForSignature(DOC_ACTN_APPROVE);
               return false;
            }
         }
         
         //PDF Signature 
         
         //On WF_APRV_SH record set the fields(Action, Action User ID) only for the given Approval level
         loAprvSheetObj.setAPRV_ACTN_STA(fiApprovalLevel,
               ApprovalInfo.APRV_ACTN_STA_APPROVE);
         loAprvSheetObj.setAPRV_ACTN_USID(fiApprovalLevel, lsUserId);
         //On WF_APRV_SH record set the common fields(Last Action, Last Action User ID,
         //Last Action Approval Level).
         loAprvSheetObj.setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_APPROVE);
         loAprvSheetObj.setLAST_ACTN_USID(lsUserId);
         loAprvSheetObj.setLAST_ACTN_APRV_LVL((int)(fiApprovalLevel));
         // log the action - do it before loAprvSheetObj.save() in order
         // to have a valid old status value
         WF_APRV_LOGImpl.logActionEvent(loAprvSheetObj, fiApprovalLevel, false);
         loAprvSheetObj.save();

         //Update common fields(Last Action, Last Action User ID,Last Action Approval Level)
         //on other Approval Worksheets (WF_APRV_SH records) of the Document as well.
         for (liAprShtCnt =0;liAprShtCnt < liApprShtIndex;liAprShtCnt++)
         {
            //Do not process WF_APRV_SH already processed earlier as loAprvSheetObj
            if(liAprShtCnt != (liCurrShtSeqNo-1))
            {
               loApprovalSheetList[liAprShtCnt]
                                    .setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_APPROVE);
               loApprovalSheetList[liAprShtCnt].setLAST_ACTN_USID(lsUserId);
               loApprovalSheetList[liAprShtCnt].setLAST_ACTN_APRV_LVL((int)(fiApprovalLevel));
               // log the action - do it before loAprvSheetObj.save() in order
               // to have a valid old status value
               //?WF_APRV_LOGImpl.logActionEvent(loApprovalSheetList[iAprShtCnt], fiApprovalLevel, false);
               loApprovalSheetList[liAprShtCnt].save();

            }

         }
         //Pass in WF_APRV_SH record for the given Approval Level and all WF_APRV_SH records
         //for the Document
         AMSApprovalProcessor.processApproveAction(loAprvSheetObj, loApprovalSheetList);
           return true;
      }
   }//end of method


   /**
   * <br>
   * Load the current document's approval sheet record, update attributes with
   * the status, current user ID, and the assigned approval level and call the
   * DataObject method save() to update the approval sheet record to the database.
   * @return unapprove : if succcessful returns boolean true.
   */

   public static boolean unapprove(Session foSession,
                                String fsDocCode,
                                String fsDocDeptCode,
                                String fsDocID,
                             int fiDocVer,
                             int fiApprovalLevel,
                          boolean fboolRecallFlag)
   {
      int liApprShtIndex=0;
      int liAprShtCnt;
      int liCurrShtSeqNo=0;
      WF_APRV_SHImpl loApprovalSheetList[] = new WF_APRV_SHImpl[AMSCommonConstants.MAX_APRV_SEQ_NO+1] ;

      WF_APRV_SHImpl loAprvSheetObj = null;
      Parameter loParam = null;
      SearchRequest loSearchReq = new SearchRequest();
       String lsAprvUsid = null;
       String lsUserId = foSession.getUserID();

      //Search for all WF_APRV_SH records for the given Document
      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_CD ;
      loParam.value = fsDocCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_DEPT_CD ;
      loParam.value = fsDocDeptCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_ID ;
      loParam.value = fsDocID ;
      loSearchReq.add(loParam);

      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_VERS_NO;
      loParam.value = String.valueOf(fiDocVer);
      loSearchReq.add(loParam);

      Vector loSortAttrs = new Vector( 5 ) ;

      //Get Approval Sheets(WF_APRV_SH records) in ascending order of SEQ_NO
      loSortAttrs.addElement(ATTR_DOC_CD) ;
      loSortAttrs.addElement(ATTR_DOC_DEPT_CD) ;
      loSortAttrs.addElement(ATTR_DOC_ID) ;
      loSortAttrs.addElement(ATTR_DOC_VERS_NO) ;
      loSortAttrs.addElement(ATTR_SEQ_NO) ;

      Enumeration liApprovalSheetList = (WF_APRV_SHImpl.getObjectsSorted(loSearchReq ,foSession,loSortAttrs,AMSDataObject.SORT_ASC));
      //Get SEQ_NO for the given Approval Level to trace its WF_APRV_SH record
      liCurrShtSeqNo = AMSApprovalProcessor.getSeqNoForAprvLvlAndOrdNo(fiApprovalLevel);

      //For each WF_APRV_SH record for the given Document
      while(liApprovalSheetList.hasMoreElements())
      {
         //moApprovalTemplate[liApprovalTemplateIndex] = (R_WF_APRVImpl)liApprovalTemplateList.nextElement();
         loApprovalSheetList[liApprShtIndex] = (WF_APRV_SHImpl)liApprovalSheetList.nextElement();

         if(loApprovalSheetList[liApprShtIndex].getSEQ_NO() == liCurrShtSeqNo)
         {
            //Get WF_APRV_SH record for the passed in Approval Level
            loAprvSheetObj = loApprovalSheetList[liApprShtIndex];
         }
         liApprShtIndex++;
      }//end while

      //WF_APRV_SH record not found
      if ( loAprvSheetObj == null )
      {
         return false;
      }

      lsAprvUsid = loAprvSheetObj.getAPRV_ACTN_USID(fiApprovalLevel);
      //Case where Action User ID is populated(Workflow Action already taken on the Approval Level)
      if ((lsAprvUsid != null) && !lsAprvUsid.trim().equals(""))
      {
         loAprvSheetObj.raiseException("approval level " + fiApprovalLevel +
                                       " is already processed");
         return false;
      }
      else
      {
         //On WF_APRV_SH record set the fields(Action, Action User ID) only for the given Approval level
         loAprvSheetObj.setAPRV_ACTN_STA(fiApprovalLevel,
                ApprovalInfo.APRV_ACTN_STA_UNAPPROVE);
         loAprvSheetObj.setAPRV_ACTN_USID(fiApprovalLevel, lsUserId);

         //On Approval Worksheet set the common fields(Last Action, Last Action User ID,
         //Last Action Approval Level).
         loAprvSheetObj.setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_UNAPPROVE);
         loAprvSheetObj.setLAST_ACTN_USID(lsUserId);
         loAprvSheetObj.setLAST_ACTN_APRV_LVL((int)(fiApprovalLevel));
         // log the action - do it before loAprvSheetObj.save() in order
         // to have a valid old status value
         WF_APRV_LOGImpl.logActionEvent(loAprvSheetObj, fiApprovalLevel, false);
         loAprvSheetObj.save();

         //Update common fields(Last Action, Last Action User ID,Last Action Approval Level)
         //on other Approval Worksheets (WF_APRV_SH records) of the Document as well.
         for (liAprShtCnt =0;liAprShtCnt < liApprShtIndex;liAprShtCnt++)
         {
            //Do not process WF_APRV_SH already processed earlier as loAprvSheetObj
            if(liAprShtCnt != (liCurrShtSeqNo-1))
            {
               loApprovalSheetList[liAprShtCnt]
                                    .setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_UNAPPROVE);
               loApprovalSheetList[liAprShtCnt].setLAST_ACTN_USID(lsUserId);
               loApprovalSheetList[liAprShtCnt]
                                    .setLAST_ACTN_APRV_LVL(fiApprovalLevel);
               loApprovalSheetList[liAprShtCnt].save();
            }

         }

         //Pass in WF_APRV_SH record for the given Approval Level and all WF_APRV_SH records
         //for the Document
         AMSApprovalProcessor.processUnapproveAction(loAprvSheetObj,loApprovalSheetList);

         // Delete the Document Signature for this Document
         E_SIG_CTLGImpl.deleteDocSignatureForAprvLvl(foSession, fsDocCode,
               fsDocDeptCode, fsDocID, fiDocVer, fiApprovalLevel);

         return true;
      }
   }//end of method

   /**
   * <br>
   * Load the current document's approval sheet record, update attributes with
   * the status, current user ID, and the assigned approval level and call the
   * DataObject method save() to update the approval sheet record to the database.
   * @return reject : if succcessful returns boolean true.
   */

   public static boolean reject(Session foSession,
                            String fsDocCode,
                            String fsDocDeptCode,
                            String fsDocID,
                             int fiDocVer,
                             int fiApprovalLevel)
   {
      int liApprShtIndex=0;
      int liAprShtCnt;
      int liCurrShtSeqNo=0;
      WF_APRV_SHImpl loApprovalSheetList[] = new WF_APRV_SHImpl[AMSCommonConstants.MAX_APRV_SEQ_NO+1] ;


      WF_APRV_SHImpl loAprvSheetObj = null;
      Parameter loParam = null;
      SearchRequest loSearchReq = new SearchRequest();
      String lsAprvUsid = null;
      String lsUserId = foSession.getUserID();

      //Search for all WF_APRV_SH records for the given Document
      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_CD ;
      loParam.value = fsDocCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_DEPT_CD ;
      loParam.value = fsDocDeptCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_ID ;
      loParam.value = fsDocID ;
      loSearchReq.add(loParam);

      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_VERS_NO ;
      loParam.value = String.valueOf(fiDocVer);
      loSearchReq.add(loParam);

      Vector loSortAttrs = new Vector( 5 ) ;

      //Get Approval Sheets(WF_APRV_SH records) in ascending order of SEQ_NO
      loSortAttrs.addElement(ATTR_DOC_CD) ;
      loSortAttrs.addElement(ATTR_DOC_DEPT_CD) ;
      loSortAttrs.addElement(ATTR_DOC_ID) ;
      loSortAttrs.addElement(ATTR_DOC_VERS_NO) ;
      loSortAttrs.addElement(ATTR_SEQ_NO) ;

      Enumeration liApprovalSheetList = (WF_APRV_SHImpl.getObjectsSorted(loSearchReq ,foSession,loSortAttrs,AMSDataObject.SORT_ASC));
      //Get SEQ_NO for the given Approval Level to trace its WF_APRV_SH record
      liCurrShtSeqNo = AMSApprovalProcessor.getSeqNoForAprvLvlAndOrdNo(fiApprovalLevel);

      //For each WF_APRV_SH record for the given Document
      while(liApprovalSheetList.hasMoreElements())
      {
         loApprovalSheetList[liApprShtIndex] = (WF_APRV_SHImpl)liApprovalSheetList.nextElement();

         if(loApprovalSheetList[liApprShtIndex].getSEQ_NO() == liCurrShtSeqNo)
         {
            //Get WF_APRV_SH record for the passed in Approval Level
            loAprvSheetObj = loApprovalSheetList[liApprShtIndex];
         }
         liApprShtIndex++;
      }//end while

      //WF_APRV_SH record not found
      if ( loAprvSheetObj == null )
      {
         return false;
      }

      lsAprvUsid = loAprvSheetObj.getAPRV_ACTN_USID(fiApprovalLevel);
      //Case where Action User ID is populated(Workflow Action already taken on the Approval Level)
      if ((lsAprvUsid != null) && !lsAprvUsid.trim().equals(""))
      {
         loAprvSheetObj.raiseException("approval level " + fiApprovalLevel +
                                         " is already processed");
         return false;
      }
      else
      {
         //Check if the Action is permitted as per Self-Approval restriction on Approval Rule record
         if(isSelfAprvRestricted(DOC_ACTN_REJECT,fsDocCode, fsDocDeptCode, fsDocID,
            fiDocVer, fiApprovalLevel, loAprvSheetObj.getSLF_APRV_RSCT(),
            loAprvSheetObj.getDOC_CREA_USID(), loAprvSheetObj.getSBMT_ID(),
            foSession.getUserID(), foSession))
         {
            //Action is restricted
            return false;
         }
         
         //On WF_APRV_SH record set the fields(Action, Action User ID) only for the given Approval level
         loAprvSheetObj.setAPRV_ACTN_STA(fiApprovalLevel,
                ApprovalInfo.APRV_ACTN_STA_REJECT);
         loAprvSheetObj.setAPRV_ACTN_USID(fiApprovalLevel, lsUserId);

         //On Approval Worksheet set the common fields(Last Action, Last Action User ID,
         //Last Action Approval Level).
         loAprvSheetObj.setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_REJECT);
         loAprvSheetObj.setLAST_ACTN_USID(lsUserId);
         loAprvSheetObj.setLAST_ACTN_APRV_LVL((int)(fiApprovalLevel));
         // log the action - do it before loAprvSheetObj.save() in order
         // to have a valid old status value
         WF_APRV_LOGImpl.logActionEvent(loAprvSheetObj, fiApprovalLevel, false);
         loAprvSheetObj.save();

         //Update common fields(Last Action, Last Action User ID,Last Action Approval Level)
         //on other Approval Worksheets (WF_APRV_SH records) of the Document as well.
         for (liAprShtCnt =0;liAprShtCnt < liApprShtIndex;liAprShtCnt++)
         {
            //Do not process WF_APRV_SH already processed earlier as loAprvSheetObj
            if(liAprShtCnt != (liCurrShtSeqNo-1))
            {
               loApprovalSheetList[liAprShtCnt]
                                 .setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_REJECT);
               loApprovalSheetList[liAprShtCnt].setLAST_ACTN_USID(lsUserId);
               loApprovalSheetList[liAprShtCnt]
                                 .setLAST_ACTN_APRV_LVL(fiApprovalLevel);
               loApprovalSheetList[liAprShtCnt].save();
            }
         }

         //Pass in WF_APRV_SH record for the given Approval Level and all WF_APRV_SH records
         //for the Document
         AMSApprovalProcessor.processRejectAction(loAprvSheetObj,loApprovalSheetList);
         return true;
      }
   }//end of method


   /**
    * <br>
    * Load the current document's approval sheet record, update attributes with
    * the status, current user ID, and the assigned approval level and call the
    * DataObject method save() to update the approval sheet record to the database.
    * @return rejectAll : if succcessful returns boolean true.
    */

   public static boolean rejectAll(Session foSession,
                                   String fsDocCode,
                                   String fsDocDeptCode,
                                   String fsDocID,
                                   int fiDocVer,
                                   int fiApprovalLevel,
                                   boolean fboolForceReject)
   {
      int liApprShtIndex=0;
      int liAprShtCnt;
      int liCurrShtSeqNo=0;
      WF_APRV_SHImpl loApprovalSheetList[] = new WF_APRV_SHImpl[AMSCommonConstants.MAX_APRV_SEQ_NO+1] ;

      WF_APRV_SHImpl loAprvSheetObj = null;
      Parameter loParam = null;
      SearchRequest loSearchReq = new SearchRequest();
      String lsAprvUsid = null;
      String lsUserId = foSession.getUserID();

      //Search for all WF_APRV_SH records for the given Document
      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_CD ;
      loParam.value = fsDocCode ;
      loSearchReq.add(loParam);
            loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_DEPT_CD ;
      loParam.value = fsDocDeptCode ;
      loSearchReq.add(loParam);

      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_ID ;
      loParam.value = fsDocID ;
      loSearchReq.add(loParam);

      loParam = new Parameter();
      loParam.objName = OBJECT_NAME;
      loParam.fieldName = ATTR_DOC_VERS_NO ;
      loParam.value = String.valueOf(fiDocVer);
      loSearchReq.add(loParam);


      Vector loSortAttrs = new Vector( 5 ) ;

      //Get Approval Sheets(WF_APRV_SH records) in ascending order of SEQ_NO
      loSortAttrs.addElement(ATTR_DOC_CD) ;
      loSortAttrs.addElement(ATTR_DOC_DEPT_CD) ;
      loSortAttrs.addElement(ATTR_DOC_ID) ;
      loSortAttrs.addElement(ATTR_DOC_VERS_NO) ;
      loSortAttrs.addElement(ATTR_SEQ_NO) ;

     Enumeration liApprovalSheetList = (WF_APRV_SHImpl.getObjectsSorted(loSearchReq ,foSession,loSortAttrs,AMSDataObject.SORT_ASC));
     //Get SEQ_NO for the given Approval Level to trace its WF_APRV_SH record
     liCurrShtSeqNo = AMSApprovalProcessor.getSeqNoForAprvLvlAndOrdNo(fiApprovalLevel);

      //For each WF_APRV_SH record for the given Document
      while(liApprovalSheetList.hasMoreElements())
      {
         loApprovalSheetList[liApprShtIndex] = (WF_APRV_SHImpl)liApprovalSheetList.nextElement();

         if(loApprovalSheetList[liApprShtIndex].getSEQ_NO() == liCurrShtSeqNo)
         {
            //Get WF_APRV_SH record for the passed in Approval Level
            loAprvSheetObj = loApprovalSheetList[liApprShtIndex];
            if ( AMS_DEBUG )
            {
               System.err.println("APL: RejectALL current approval sheet object assigned, index no= " +liApprShtIndex);
            }

         }
         liApprShtIndex++;
      }//end while

      //WF_APRV_SH record not found
      if ( loAprvSheetObj == null )
      {
         return false;
      }

      lsAprvUsid = loAprvSheetObj.getAPRV_ACTN_USID(fiApprovalLevel);
      //Case where Action User ID is populated(Workflow Action already taken on the Approval Level)
      if ((lsAprvUsid != null) && !lsAprvUsid.trim().equals("")
          && !fboolForceReject)
      {
         loAprvSheetObj.raiseException("approval level " + fiApprovalLevel +
                                       " is already processed");
         return false;
      }
      else
      {
         //Check if the Action is permitted as per Self-Approval restriction on Approval Rule record
         if(isSelfAprvRestricted(DOC_ACTN_REJECT_ALL,fsDocCode, fsDocDeptCode, fsDocID,
            fiDocVer, fiApprovalLevel, loAprvSheetObj.getSLF_APRV_RSCT(),
            loAprvSheetObj.getDOC_CREA_USID(), loAprvSheetObj.getSBMT_ID(),
            foSession.getUserID(), foSession))
         {
            //Action is restricted
            return false;
         }
         
         //On WF_APRV_SH record set the fields(Action, Action User ID) only for the given Approval level
         loAprvSheetObj.setAPRV_ACTN_STA(fiApprovalLevel,
                ApprovalInfo.APRV_ACTN_STA_REJECT);
         loAprvSheetObj.setAPRV_ACTN_USID(fiApprovalLevel, lsUserId);

         //On Approval Worksheet set the common fields(Last Action, Last Action User ID,
         //Last Action Approval Level).
         loAprvSheetObj.setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_REJECT);
         loAprvSheetObj.setLAST_ACTN_USID(lsUserId);
         loAprvSheetObj.setLAST_ACTN_APRV_LVL((int)(fiApprovalLevel));
         // log the action - do it before loAprvSheetObj.save() in order
         // to have a valid old status value
         WF_APRV_LOGImpl.logActionEvent(loAprvSheetObj, fiApprovalLevel, false);
         loAprvSheetObj.save();

         //On Approval Worksheet set the common fields(Last Action, Last Action User ID,
         //Last Action Approval Level).
         for (liAprShtCnt =0;liAprShtCnt < liApprShtIndex;liAprShtCnt++)
         {
            //Do not process WF_APRV_SH already processed earlier as loAprvSheetObj
            if(liAprShtCnt != (liCurrShtSeqNo-1))
            {
               loApprovalSheetList[liAprShtCnt]
                                       .setLAST_ACTN_STA(ApprovalInfo.APRV_ACTN_STA_REJECT);
               loApprovalSheetList[liAprShtCnt].setLAST_ACTN_USID(lsUserId);
               loApprovalSheetList[liAprShtCnt]
                                       .setLAST_ACTN_APRV_LVL(fiApprovalLevel);
               loApprovalSheetList[liAprShtCnt].save();
            }

         }

         //Pass in WF_APRV_SH record for the given Approval Level and all WF_APRV_SH records
         //for the Document
         AMSApprovalProcessor.processRejectAllAction(loAprvSheetObj);
         
         // ADV_ PDF Signature
         /**
          * Delete all the Signature Catalog entries for the currently processed
          * document
          */
         E_SIG_CTLGImpl.deleteDocSignatures(foSession, fsDocCode, fsDocDeptCode,
               fsDocID, fiDocVer);
         // PDF Signature
         
         return true;
      }
   }//end of method


    /**
     * Returns true when the logged in User has earlier approved(APRV_ACTN_USID=<current User ID>)
     * any of the Approval Levels on the passed in Approval Sheets for the Document. In all other
     * cases returns false.
     *
     * Note that the invoking logic is responsible for any logging
     * logic and for dealing with the result of this method.
     *
     * @param foApprovalSheets All Approval Sheets(WF_APRV_SH records) for the Document
     * @param foSession - The current session.
     *
     * @return boolean
     * @author Vipin Sharma
     */
   public static boolean hasUserAlreadyApproved(
         WF_APRV_SHImpl[] foApprovalSheets, Session foSession)
   {
      String lsUserId = foSession.getUserID();
      boolean lboolUserAlreadyApproved = false;

      //Loop through all Approval Sheets for the Document
      for (int liApprShtIndex = 0; liApprShtIndex < foApprovalSheets.length; liApprShtIndex++)
      {
         if ((foApprovalSheets[liApprShtIndex] != null)
               && (lboolUserAlreadyApproved == false))
         {
            //Loop through all Approval Levels(10) on selected Approval Sheet
            for (int liAprvLvlCnt = 1; liAprvLvlCnt <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvlCnt++)
            {
               if ((foApprovalSheets[liApprShtIndex]
                     .getAPRV_ACTN_USID(liAprvLvlCnt) != null)
                     && (foApprovalSheets[liApprShtIndex]
                           .getAPRV_ACTN_STA(liAprvLvlCnt) == ApprovalInfo.APRV_ACTN_STA_APPROVE)
                     && foApprovalSheets[liApprShtIndex].getAPRV_ACTN_USID(
                           liAprvLvlCnt).equals(lsUserId))
               {
                  //Case where Action User ID on the Approval Level was found to be the logged in User ID
                  lboolUserAlreadyApproved = true;
                  break;
               }
            }
         }
      }
      return lboolUserAlreadyApproved;
   }//end of method

   /**
    * This method overrides the definition in AMSDataObject in order to replace
    * parameters during notifications. The following case-sensitive parameters
    * are supported:
    * %doc% - substitutes the document’s identification in the form of cd,dept,id,no
    * %lvl% - substitutes the current approval level
    *
    * Modification Log : Vipin Sharma - 07/21/06 - inital version
    *
    * @param fsText - The text that may contain zero, one, or more replaceable parameters.
    * @param foAddlParams- HashMap A map that contains information to resolve
    * REPLACE_NOTIF_TMPL_APRV_LVL as necessary, where the value is assumed to be a
    * String. If this key value cannot be resolved, then blank is used.
    *
    * @return String - Text with parameters replaced.
    */
    public String replaceWfNotifParameters(String fsText, HashMap foAddlParams)
    {
       String lsText = null;
       final String DOC_PARAM = "%doc%";
       if (fsText.indexOf(DOC_PARAM) >= 0)
       {
          StringBuffer loBuf = new StringBuffer(getDOC_CD());
          loBuf.append(",");
          loBuf.append(getDOC_DEPT_CD());
          loBuf.append(",");
          loBuf.append(getDOC_ID());
          loBuf.append(",");
          loBuf.append(getDOC_VERS_NO());
          lsText = AMSUtil.replacePattern(fsText, DOC_PARAM, loBuf.toString());
       }
       else
       {
          lsText = fsText;
       }

       if (lsText.indexOf(REPLACE_NOTIF_TMPL_APRV_LVL) >= 0)
       {
          String lsReplaceValue = "";
          if (foAddlParams.get(REPLACE_NOTIF_TMPL_APRV_LVL) != null)
          {
             lsReplaceValue = foAddlParams.get(REPLACE_NOTIF_TMPL_APRV_LVL).toString();
          }
          lsText = AMSUtil.replacePattern(lsText, REPLACE_NOTIF_TMPL_APRV_LVL, lsReplaceValue);
       }
       return lsText;
    }

    /**
     * This is a convenience method for setting the NOTIF_ORIG_x values.
     * It wraps the invocations of the setNOTIF_ORIG_x(int) methods that
     * are defined in the superclass.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_ORIG_x attribute to set.
     * @param fiValue - The value to set.
     * @author Vipin Sharma
     */
    public void setNOTIF_ORIG(int fiAprvLvl, int fiValue)
    {
       //Set values only >= CVL_WF_NOTIF_ORIGImpl.NOTIFY_MIN to be compatible with CVL
       //to avoid second argument of 0, which is a possibility in
       // AMSApprovalProcessor.updateAprvSheet().
       if (fiValue < CVL_WF_NOTIF_ORIGImpl.NOTIFY_MIN)
       {
          return;
       }

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             setNOTIF_ORIG_1(fiValue);
             break;
          case 2:
             setNOTIF_ORIG_2(fiValue);
             break;
          case 3:
             setNOTIF_ORIG_3(fiValue);
             break;
          case 4:
             setNOTIF_ORIG_4(fiValue);
             break;
          case 5:
             setNOTIF_ORIG_5(fiValue);
             break;
          case 6:
             setNOTIF_ORIG_6(fiValue);
             break;
          case 7:
             setNOTIF_ORIG_7(fiValue);
             break;
          case 8:
             setNOTIF_ORIG_8(fiValue);
             break;
          case 9:
             setNOTIF_ORIG_9(fiValue);
             break;
          case 10:
             setNOTIF_ORIG_10(fiValue);
             break;
          default:
       }
    }

    /**
     * This is a convenience method for retrieving the NOTIF_ORIG_x values.
     * It wraps the invocations of the getNOTIF_ORIG_x() methods that
     * are defined in the superclass and returns the appropriate values.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_ORIG_x attribute to get.
     * @return int - NOTIF_ORIG_x
     * @author Vipin Sharma
     *
     */
    public int getNOTIF_ORIG(int fiAprvLvl)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             return getNOTIF_ORIG_1();
          case 2:
             return getNOTIF_ORIG_2();
          case 3:
             return getNOTIF_ORIG_3();
          case 4:
             return getNOTIF_ORIG_4();
          case 5:
             return getNOTIF_ORIG_5();
          case 6:
             return getNOTIF_ORIG_6();
          case 7:
             return getNOTIF_ORIG_7();
          case 8:
             return getNOTIF_ORIG_8();
          case 9:
             return getNOTIF_ORIG_9();
          case 10:
             return getNOTIF_ORIG_10();
          default:
             return 0;
       }
    }

    /**
     * This is a convenience method for retrieving the NOTIF_WO_RTE_FL_x values.
     * It wraps the invocations of the getNOTIF_WO_RTE_FL_x() methods that
     * are defined in the superclass and returns the appropriate values.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_RTE_FL_x attribute to get.
     * @return boolean - NOTIF_WO_RTE_FL_x
     * @author Vipin Sharma
     *
     */
    public boolean getNOTIF_WO_RTE_FL(int fiAprvLvl)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             return getNOTIF_WO_RTE_FL_1();
          case 2:
             return getNOTIF_WO_RTE_FL_2();
          case 3:
             return getNOTIF_WO_RTE_FL_3();
          case 4:
             return getNOTIF_WO_RTE_FL_4();
          case 5:
             return getNOTIF_WO_RTE_FL_5();
          case 6:
             return getNOTIF_WO_RTE_FL_6();
          case 7:
             return getNOTIF_WO_RTE_FL_7();
          case 8:
             return getNOTIF_WO_RTE_FL_8();
          case 9:
             return getNOTIF_WO_RTE_FL_9();
          case 10:
             return getNOTIF_WO_RTE_FL_10();
          default:
             return false;
       }
    }

    /**
     * This is a convenience method for retrieving the NOTIF_WO_ID_x values.
     * It wraps the invocations of the getNOTIF_WO_ID_x() methods that
     * are defined in the superclass and returns the appropriate values.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_ID_x attribute to get.
     * @return String - NOTIF_WO_ID_x
     * @author Vipin Sharma
     *
     */
    public String getNOTIF_WO_ID(int fiAprvLvl)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             return getNOTIF_WO_ID_1();
          case 2:
             return getNOTIF_WO_ID_2();
          case 3:
             return getNOTIF_WO_ID_3();
          case 4:
             return getNOTIF_WO_ID_4();
          case 5:
             return getNOTIF_WO_ID_5();
          case 6:
             return getNOTIF_WO_ID_6();
          case 7:
             return getNOTIF_WO_ID_7();
          case 8:
             return getNOTIF_WO_ID_8();
          case 9:
             return getNOTIF_WO_ID_9();
          case 10:
             return getNOTIF_WO_ID_10();
          default:
             return null;
       }
    }

    /**
     * This is a convenience method for retrieving the NOTIF_WO_ID_TYP_x values.
     * It wraps the invocations of the getNOTIF_WO_ID_TYP_x() methods that
     * are defined in the superclass and returns the appropriate values.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_ID_TYP_x attribute to get.
     * @return int - NOTIF_WO_ID_TYP_x
     * @author Vipin Sharma
     *
     */
    public int getNOTIF_WO_ID_TYP(int fiAprvLvl)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             return getNOTIF_WO_ID_TYP_1();
          case 2:
             return getNOTIF_WO_ID_TYP_2();
          case 3:
             return getNOTIF_WO_ID_TYP_3();
          case 4:
             return getNOTIF_WO_ID_TYP_4();
          case 5:
             return getNOTIF_WO_ID_TYP_5();
          case 6:
             return getNOTIF_WO_ID_TYP_6();
          case 7:
             return getNOTIF_WO_ID_TYP_7();
          case 8:
             return getNOTIF_WO_ID_TYP_8();
          case 9:
             return getNOTIF_WO_ID_TYP_9();
          case 10:
             return getNOTIF_WO_ID_TYP_10();
          default:
             return 0;
       }
    }

    /**
     * This is a convenience method for retrieving the NOTIF_WO_CMNT_x values.
     * It wraps the invocations of the getNOTIF_WO_CMNT_x() methods that
     * are defined in the superclass and returns the appropriate values.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_CMNT_x attribute to get.
     * @return long - NOTIF_WO_CMNT_x
     * @author Vipin Sharma
     *
     */
    public long getNOTIF_WO_CMNT(int fiAprvLvl)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       String lsNotifWoCmnt = null;
       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
          case 7:
          case 8:
          case 9:
          case 10:
             lsNotifWoCmnt = getData("NOTIF_WO_CMNT_" +
                   String.valueOf(liIndex)).getString();
             return getCmntLongValue(lsNotifWoCmnt);
          default:
             return -1;
       }
    }

    /**
     * Helper function for getNOTIF_WO_CMNT_x
     * that converts fsCmnt to its long value
     * @param fsCmnt - String comment whose long value is to be obtained
     * @return long - Long value of comment string or -1, if comment is null or empty
     * @author Vipin Sharma
     */
    private static long getCmntLongValue(String fsCmnt)
    {
       try
       {
          return Long.parseLong(fsCmnt);
       }
       catch(NumberFormatException loEx)
       {
          return -1L;
       }
    }


    /**
     * Retrieves the PRIORITY_x values where x is the Approval Level.
     * If Approval Level is 0, return 0 as no valid value exists for that level on sheet.
     * 
     * @param fiIndex The approval level (x) for the PRIORITY_x value to get.
     * @return int PRIORITY_x value
     *
     */ 
    public int getPRIORITY(int fiIndex)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiIndex);

       /*
        * If Approval Level is 0, return 0 as no valid value exists for that level on sheet.
        */
       if(liIndex==0)
       {
          return 0;
       }

       return getData("PRIORITY_" + liIndex).getint();
    }    
    
    
    /**
     * This is a convenience method for setting the NOTIF_WO_RTE_FL_x values.
     * It wraps the invocations of the NOTIF_WO_RTE_FL_x(boolean) methods that
     * are defined in the superclass.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_RTE_FL_x attribute to set.
     * @param fboolValue - The value to set.
     * @author Vipin Sharma
     *
     */
    public void setNOTIF_WO_RTE_FL(int fiAprvLvl, boolean fboolValue)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             setNOTIF_WO_RTE_FL_1(fboolValue);
             break;
          case 2:
             setNOTIF_WO_RTE_FL_2(fboolValue);
             break;
          case 3:
             setNOTIF_WO_RTE_FL_3(fboolValue);
             break;
          case 4:
             setNOTIF_WO_RTE_FL_4(fboolValue);
             break;
          case 5:
             setNOTIF_WO_RTE_FL_5(fboolValue);
             break;
          case 6:
             setNOTIF_WO_RTE_FL_6(fboolValue);
             break;
          case 7:
             setNOTIF_WO_RTE_FL_7(fboolValue);
             break;
          case 8:
             setNOTIF_WO_RTE_FL_8(fboolValue);
             break;
          case 9:
             setNOTIF_WO_RTE_FL_9(fboolValue);
             break;
          case 10:
             setNOTIF_WO_RTE_FL_10(fboolValue);
             break;
          default:
       }
    }

    /**
     * This is a convenience method for setting the NOTIF_WO_ID_x values.
     * It wraps the invocations of the NOTIF_WO_ID_x(String) methods that
     * are defined in the superclass.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_ID_x attribute to set.
     * @param fsValue - The value to set.
     * @author Vipin Sharma
     *
     */
    public void setNOTIF_WO_ID(int fiAprvLvl, String fsValue)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             setNOTIF_WO_ID_1(fsValue);
             break;
          case 2:
             setNOTIF_WO_ID_2(fsValue);
             break;
          case 3:
             setNOTIF_WO_ID_3(fsValue);
             break;
          case 4:
             setNOTIF_WO_ID_4(fsValue);
             break;
          case 5:
             setNOTIF_WO_ID_5(fsValue);
             break;
          case 6:
             setNOTIF_WO_ID_6(fsValue);
             break;
          case 7:
             setNOTIF_WO_ID_7(fsValue);
             break;
          case 8:
             setNOTIF_WO_ID_8(fsValue);
             break;
          case 9:
             setNOTIF_WO_ID_9(fsValue);
             break;
          case 10:
             setNOTIF_WO_ID_10(fsValue);
             break;
          default:
       }
    }

    /**
     * This is a convenience method for setting the NOTIF_WO_ID_TYP_x values.
     * It wraps the invocations of the setNOTIF_WO_ID_TYP_x(int) methods that
     * are defined in the superclass.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_ID_TYP_x attribute to set.
     * @param fiValue - The value to set.
     * @author Vipin Sharma
     *
     */
    public void setNOTIF_WO_ID_TYP(int fiAprvLvl, int fiValue)
    {
       //Set values only >= APRV_RULE_NOTIF_WO_TYP_MIN to be compatible with CVL
       //to avoid second argument of 0, which is a possibility in
       // AMSApprovalProcessor.updateAprvSheet().
       if (fiValue < ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_MIN)
       {
          return;
       }

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             setNOTIF_WO_ID_TYP_1(fiValue);
             break;
          case 2:
             setNOTIF_WO_ID_TYP_2(fiValue);
             break;
          case 3:
             setNOTIF_WO_ID_TYP_3(fiValue);
             break;
          case 4:
             setNOTIF_WO_ID_TYP_4(fiValue);
             break;
          case 5:
             setNOTIF_WO_ID_TYP_5(fiValue);
             break;
          case 6:
             setNOTIF_WO_ID_TYP_6(fiValue);
             break;
          case 7:
             setNOTIF_WO_ID_TYP_7(fiValue);
             break;
          case 8:
             setNOTIF_WO_ID_TYP_8(fiValue);
             break;
          case 9:
             setNOTIF_WO_ID_TYP_9(fiValue);
             break;
          case 10:
             setNOTIF_WO_ID_TYP_10(fiValue);
             break;
          default:
       }
    }

    /**
     * This is a convenience method for setting the NOTIF_WO_CMNT_x values.
     * It wraps the invocations of the NOTIF_WO_CMNT_x(long) methods that
     * are defined in the superclass.
     *
     * @param fiAprvLvl - The approval level (x) for the specific NOTIF_WO_CMNT_x attribute to set.
     * @param flValue - The value to set.
     * @author Vipin Sharma
     *
     */
    public void setNOTIF_WO_CMNT(int fiAprvLvl, long flValue)
    {

       // Handle approval level > 10
       int liIndex = getIndexFromAprvLevel(fiAprvLvl);

       // switch (liIndex)
       switch (liIndex)
       {
          case 1:
             setNOTIF_WO_CMNT_1(flValue);
             break;
          case 2:
             setNOTIF_WO_CMNT_2(flValue);
             break;
          case 3:
             setNOTIF_WO_CMNT_3(flValue);
             break;
          case 4:
             setNOTIF_WO_CMNT_4(flValue);
             break;
          case 5:
             setNOTIF_WO_CMNT_5(flValue);
             break;
          case 6:
             setNOTIF_WO_CMNT_6(flValue);
             break;
          case 7:
             setNOTIF_WO_CMNT_7(flValue);
             break;
          case 8:
             setNOTIF_WO_CMNT_8(flValue);
             break;
          case 9:
             setNOTIF_WO_CMNT_9(flValue);
             break;
          case 10:
             setNOTIF_WO_CMNT_10(flValue);
             break;
          default:
       }
    }

   /**
   * <br>
   * The following methods are accessor methods for the vatious approval level
   * attributes of the WF_APRV_SH DataObject object. These meyhods are provided
   * to make it easier to enumerate through the aproval level attribute block to
      * get or set values. These methods can be used within a for loop construct.
   * @return : attribute value or "0" as default
   */
   public int getORD_NO( int fiIndex )
   {
      //Aprv Lvl Change: code added to handle approval level > 10
       int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
        {
          case 1:
               return getORD_NO_1();
          case 2:
               return getORD_NO_2();
          case 3:
               return getORD_NO_3();
          case 4:
               return getORD_NO_4();
          case 5:
               return getORD_NO_5();
          case 6:
               return getORD_NO_6();
          case 7:
               return getORD_NO_7();
          case 8:
                  return getORD_NO_8();
          case 9:
               return getORD_NO_9();
          case 10:
               return getORD_NO_10();
                default:
            return 0;
       }
   }

   public void setORD_NO( int fiIndex, int fiValue )
   {
      //Aprv Lvl Change: code added to handle approval level > 10
       int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

       {
          case 1:
               setORD_NO_1( fiValue );
            break;
          case 2:
               setORD_NO_2( fiValue );
            break;
          case 3:
               setORD_NO_3( fiValue );
            break;
          case 4:
               setORD_NO_4( fiValue );
            break;
          case 5:
               setORD_NO_5( fiValue );
            break;
          case 6:
               setORD_NO_6( fiValue );
            break;
          case 7:
               setORD_NO_7( fiValue );
            break;
          case 8:
                  setORD_NO_8( fiValue );
            break;
          case 9:
               setORD_NO_9( fiValue );
            break;
          case 10:
               setORD_NO_10( fiValue );
            break;
                default:
        }
   }

   /**
    * Gets value of Ordinal No. Map ORD_NO_x_MAP where derivation of x is based on fiIndex.
    * Example 1: When fiIndex =  3, returns value of ORD_NO_3_MAP.
    * Example 2: When fiIndex = 15, returns value of ORD_NO_5_MAP.
    * @param fiIndex Ordinal No. (practically can range from 1 to 15)
    * @return int
    */
   public long getORD_NO_MAP( int fiIndex )
   {
      int liIndex;
      /*
       * Get the Index for given Ordinal No. (CURR_ORD_NO Routing Sequence No.)
       * Example 1: When fiIndex =  3, liIndex = 3.
       * Example 2: When fiIndex = 15, liIndex = 5.
       */
      liIndex = getIndexFromOrdNo(fiIndex);
      if ( AMS_DEBUG )
      {
         System.err.println("APL getORD_NO_MAP ord no=="+liIndex);
      }

      switch ( liIndex )
      {
         case 1:
            return getORD_NO_1_MAP();
         case 2:
            return getORD_NO_2_MAP();
         case 3:
            return getORD_NO_3_MAP();
         case 4:
            return getORD_NO_4_MAP();
         case 5:
            return getORD_NO_5_MAP();
         case 6:
            return getORD_NO_6_MAP();
         case 7:
            return getORD_NO_7_MAP();
         case 8:
               return getORD_NO_8_MAP();
         case 9:
            return getORD_NO_9_MAP();
         case 10:
            return getORD_NO_10_MAP();
             default:
         return 0;
      }//end switch
   }//end of method


   public void setORD_NO_MAP( int fiIndex, long flValue )
   {
      //Aprv Lvl Change: code added to handle ord_nos > 10
      int liIndex;
         liIndex = getIndexFromOrdNo(fiIndex);
      if ( AMS_DEBUG )
      {
         System.err.println("APL setORD_NO_MAP ord no="+liIndex+ "Value="+flValue);
            }
        //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch ( fiIndex )
        switch ( liIndex )
           {
          case 1:
               setORD_NO_1_MAP( flValue );
            break;
          case 2:
               setORD_NO_2_MAP( flValue );
            break;
          case 3:
               setORD_NO_3_MAP( flValue );
            break;
          case 4:
               setORD_NO_4_MAP( flValue );
            break;
          case 5:
               setORD_NO_5_MAP( flValue );
            break;
          case 6:
               setORD_NO_6_MAP( flValue );
            break;
          case 7:
               setORD_NO_7_MAP( flValue );
            break;
          case 8:
                  setORD_NO_8_MAP( flValue );
            break;
          case 9:
               setORD_NO_9_MAP( flValue );
            break;
          case 10:
               setORD_NO_10_MAP( flValue );
            break;
                default:
        }
   }

   public String getAPRV_ASGN( int fiIndex )
   {
      //Aprv Lvl Change: code added to handle approval level > 10
       int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);

       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

          {
          case 1:
               return getAPRV_ASGN_1();
          case 2:
               return getAPRV_ASGN_2();
          case 3:
               return getAPRV_ASGN_3();
          case 4:
               return getAPRV_ASGN_4();
          case 5:
               return getAPRV_ASGN_5();
          case 6:
               return getAPRV_ASGN_6();
          case 7:
               return getAPRV_ASGN_7();
          case 8:
                  return getAPRV_ASGN_8();
          case 9:
               return getAPRV_ASGN_9();
          case 10:
               return getAPRV_ASGN_10();
                default:
            return "0";
        }
   }


   public void setAPRV_ASGN( int fiIndex, String fsValue )
   {
       //Aprv Lvl Change: code added to handle approval level > 10
         int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

          {
          case 1:
               setAPRV_ASGN_1( fsValue );
            break;
          case 2:
               setAPRV_ASGN_2( fsValue );
            break;
          case 3:
               setAPRV_ASGN_3( fsValue );
            break;
          case 4:
               setAPRV_ASGN_4( fsValue );
            break;
          case 5:
               setAPRV_ASGN_5( fsValue );
            break;
          case 6:
               setAPRV_ASGN_6( fsValue );
            break;
          case 7:
               setAPRV_ASGN_7( fsValue );
            break;
          case 8:
                  setAPRV_ASGN_8( fsValue );
            break;
          case 9:
               setAPRV_ASGN_9( fsValue );
            break;
          case 10:
               setAPRV_ASGN_10( fsValue );
            break;
                default:
        }
   }

   public boolean getAPRV_ASGN_FL( int fiIndex )
   {

       //Aprv Lvl Change: code added to handle approval level > 10
       int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

          {
          case 1:
               return getAPRV_ASGN_FL_1();
          case 2:
               return getAPRV_ASGN_FL_2();
          case 3:
               return getAPRV_ASGN_FL_3();
          case 4:
               return getAPRV_ASGN_FL_4();
          case 5:
               return getAPRV_ASGN_FL_5();
          case 6:
               return getAPRV_ASGN_FL_6();
          case 7:
               return getAPRV_ASGN_FL_7();
          case 8:
                  return getAPRV_ASGN_FL_8();
          case 9:
               return getAPRV_ASGN_FL_9();
          case 10:
               return getAPRV_ASGN_FL_10();
                default:
            return false;
        }
   }


   public void setAPRV_ASGN_FL( int fiIndex, boolean fboolValue )
   {
       //Aprv Lvl Change: code added to handle approval level > 10
       int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

        {
          case 1:
               setAPRV_ASGN_FL_1( fboolValue );
            break;
          case 2:
               setAPRV_ASGN_FL_2( fboolValue );
            break;
          case 3:
               setAPRV_ASGN_FL_3( fboolValue );
            break;
          case 4:
               setAPRV_ASGN_FL_4( fboolValue );
            break;
          case 5:
               setAPRV_ASGN_FL_5( fboolValue );
            break;
          case 6:
               setAPRV_ASGN_FL_6( fboolValue );
            break;
          case 7:
               setAPRV_ASGN_FL_7( fboolValue );
            break;
          case 8:
                  setAPRV_ASGN_FL_8( fboolValue );
            break;
          case 9:
               setAPRV_ASGN_FL_9( fboolValue );
            break;
          case 10:
               setAPRV_ASGN_FL_10( fboolValue );
            break;
                      default:
        }
   }

   public long getASGN_CMNT( int fiIndex )
   {
      //Aprv Lvl Change: code added to handle approval level > 10
         int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

        {
          case 1:
               return getASGN_CMNT_1();
          case 2:
               return getASGN_CMNT_2();
          case 3:
               return getASGN_CMNT_3();
          case 4:
               return getASGN_CMNT_4();
          case 5:
               return getASGN_CMNT_5();
          case 6:
               return getASGN_CMNT_6();
          case 7:
               return getASGN_CMNT_7();
          case 8:
                  return getASGN_CMNT_8();
          case 9:
               return getASGN_CMNT_9();
          case 10:
               return getASGN_CMNT_10();
                default:
            return 0;
        }
   }


   public void setASGN_CMNT( int fiIndex, long flValue )
   {
        //Aprv Lvl Change: code added to handle ord_nos > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

        {
          case 1:
               setASGN_CMNT_1( flValue );
            break;
          case 2:
               setASGN_CMNT_2( flValue );
            break;
          case 3:
               setASGN_CMNT_3( flValue );
            break;
          case 4:
               setASGN_CMNT_4( flValue );
            break;
          case 5:
               setASGN_CMNT_5( flValue );
            break;
          case 6:
               setASGN_CMNT_6( flValue );
            break;
          case 7:
               setASGN_CMNT_7( flValue );
            break;
          case 8:
                  setASGN_CMNT_8( flValue );
            break;
          case 9:
               setASGN_CMNT_9( flValue );
            break;
          case 10:
               setASGN_CMNT_10( flValue );
            break;
                default:
        }
   }

   public boolean getASGN_EMAIL_FL( int fiIndex )
   {
        //Aprv Lvl Change: code added to handle ord_nos > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
        {
          case 1:
               return getASGN_EMAIL_FL_1();
          case 2:
               return getASGN_EMAIL_FL_2();
          case 3:
               return getASGN_EMAIL_FL_3();
          case 4:
               return getASGN_EMAIL_FL_4();
          case 5:
               return getASGN_EMAIL_FL_5();
          case 6:
               return getASGN_EMAIL_FL_6();
          case 7:
               return getASGN_EMAIL_FL_7();
          case 8:
                  return getASGN_EMAIL_FL_8();
          case 9:
               return getASGN_EMAIL_FL_9();
          case 10:
               return getASGN_EMAIL_FL_10();
                default:
            return false;
        }
   }


   public void setASGN_EMAIL_FL( int fiIndex, boolean fboolValue )
   {
       //Aprv Lvl Change: code added to handle approval level > 10
       int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

        {
          case 1:
               setASGN_EMAIL_FL_1( fboolValue );
            break;
          case 2:
               setASGN_EMAIL_FL_2( fboolValue );
            break;
          case 3:
               setASGN_EMAIL_FL_3( fboolValue );
            break;
          case 4:
               setASGN_EMAIL_FL_4( fboolValue );
            break;
          case 5:
               setASGN_EMAIL_FL_5( fboolValue );
            break;
          case 6:
               setASGN_EMAIL_FL_6( fboolValue );
            break;
          case 7:
               setASGN_EMAIL_FL_7( fboolValue );
            break;
          case 8:
                  setASGN_EMAIL_FL_8( fboolValue );
            break;
          case 9:
               setASGN_EMAIL_FL_9( fboolValue );
            break;
          case 10:
               setASGN_EMAIL_FL_10( fboolValue );
            break;
                default:
        }
   }

   public VSDate getASGN_DT( int fiIndex )
   {

        //Aprv Lvl Change: code added to handle approval level > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

        {
          case 1:
               return getASGN_DT_1();
          case 2:
               return getASGN_DT_2();
          case 3:
               return getASGN_DT_3();
          case 4:
               return getASGN_DT_4();
          case 5:
               return getASGN_DT_5();
          case 6:
               return getASGN_DT_6();
          case 7:
               return getASGN_DT_7();
          case 8:
                  return getASGN_DT_8();
          case 9:
               return getASGN_DT_9();
          case 10:
               return getASGN_DT_10();
                default:
            return null;
        }
   }


   public void setASGN_DT( int fiIndex, VSDate foValue )
   {
        //Aprv Lvl Change: code added to handle approval level > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
        {
          case 1:
               setASGN_DT_1( foValue );
            break;
          case 2:
               setASGN_DT_2( foValue );
            break;
          case 3:
               setASGN_DT_3( foValue );
            break;
          case 4:
               setASGN_DT_4( foValue );
            break;
          case 5:
               setASGN_DT_5( foValue );
            break;
          case 6:
               setASGN_DT_6( foValue );
            break;
          case 7:
               setASGN_DT_7( foValue );
            break;
          case 8:
                  setASGN_DT_8( foValue );
            break;
          case 9:
               setASGN_DT_9( foValue );
            break;
          case 10:
               setASGN_DT_10( foValue );
            break;
                default:
        }
   }

   public int getAPRV_ACTN_STA( int fiIndex )
   {
        //Aprv Lvl Change: code added to handle approval level > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);
       //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
        {
          case 1:
               return getAPRV_ACTN_STA_1();
          case 2:
               return getAPRV_ACTN_STA_2();
          case 3:
               return getAPRV_ACTN_STA_3();
          case 4:
               return getAPRV_ACTN_STA_4();
          case 5:
               return getAPRV_ACTN_STA_5();
          case 6:
               return getAPRV_ACTN_STA_6();
          case 7:
               return getAPRV_ACTN_STA_7();
          case 8:
                  return getAPRV_ACTN_STA_8();
          case 9:
               return getAPRV_ACTN_STA_9();
          case 10:
               return getAPRV_ACTN_STA_10();
                default:
            return 0;
        }
   }


   public int getOldAPRV_ACTN_STA( int fiIndex )
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);

        //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
      {
         case 1:
            return getOldAPRV_ACTN_STA_1();
         case 2:
            return getOldAPRV_ACTN_STA_2();
         case 3:
            return getOldAPRV_ACTN_STA_3();
         case 4:
            return getOldAPRV_ACTN_STA_4();
         case 5:
            return getOldAPRV_ACTN_STA_5();
         case 6:
            return getOldAPRV_ACTN_STA_6();
         case 7:
            return getOldAPRV_ACTN_STA_7();
         case 8:
            return getOldAPRV_ACTN_STA_8();
         case 9:
            return getOldAPRV_ACTN_STA_9();
         case 10:
            return getOldAPRV_ACTN_STA_10();
         default:
            return 0;
      }
   }


   public void setAPRV_ACTN_STA( int fiIndex, int fiValue )
   {

      //Aprv Lvl Change: code added to handle approval level > 10
         int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);

        //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)

        {
          case 1:
               setAPRV_ACTN_STA_1( fiValue );
            break;
          case 2:
               setAPRV_ACTN_STA_2( fiValue );
            break;
          case 3:
               setAPRV_ACTN_STA_3( fiValue );
            break;
          case 4:
               setAPRV_ACTN_STA_4( fiValue );
            break;
          case 5:
               setAPRV_ACTN_STA_5( fiValue );
            break;
          case 6:
               setAPRV_ACTN_STA_6( fiValue );
            break;
          case 7:
               setAPRV_ACTN_STA_7( fiValue );
            break;
          case 8:
                  setAPRV_ACTN_STA_8( fiValue );
            break;
          case 9:
               setAPRV_ACTN_STA_9( fiValue );
            break;
          case 10:
               setAPRV_ACTN_STA_10( fiValue );
            break;
                default:
        }
   }



   /**
    * Sets the PRIORITY_x value where x stands for the Approval Level.
    * 
    * If Approval Level is 0, no action is performed.
    *
    * @param fiIndex The Approval Level (x) for the PRIORITY_x value to set.
    * @param fiValue The Priority value to be set.
    *
    */ 
   public void setPRIORITY( int fiIndex, int fiValue )
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex = getIndexFromAprvLevel(fiIndex);

      if(fiIndex==0)
      {
         return;
      }
      getData("PRIORITY_" + liIndex).setint(fiValue);
   }
   
   public String getAPRV_ACTN_USID( int fiIndex )
   {
        //Aprv Lvl Change: code added to handle approval level > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);

          //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
        {
          case 1:
               return getAPRV_ACTN_USID_1();
          case 2:
               return getAPRV_ACTN_USID_2();
          case 3:
               return getAPRV_ACTN_USID_3();
          case 4:
               return getAPRV_ACTN_USID_4();
          case 5:
               return getAPRV_ACTN_USID_5();
          case 6:
               return getAPRV_ACTN_USID_6();
          case 7:
               return getAPRV_ACTN_USID_7();
          case 8:
                  return getAPRV_ACTN_USID_8();
          case 9:
               return getAPRV_ACTN_USID_9();
          case 10:
               return getAPRV_ACTN_USID_10();
                default:
            return "0";
        }
   }


   public void setAPRV_ACTN_USID( int fiIndex, String fsValue )
   {
        //Aprv Lvl Change: code added to handle approval level > 10
        int liIndex;
       liIndex = getIndexFromAprvLevel(fiIndex);

          //Aprv Lvl Change: changed from fiIndex to liIndex
       //switch (fiIndex)
       switch (liIndex)
        {
          case 1:
               setAPRV_ACTN_USID_1( fsValue );
            break;
          case 2:
               setAPRV_ACTN_USID_2( fsValue );
            break;
          case 3:
               setAPRV_ACTN_USID_3( fsValue );
            break;
          case 4:
               setAPRV_ACTN_USID_4( fsValue );
            break;
          case 5:
               setAPRV_ACTN_USID_5( fsValue );
            break;
          case 6:
               setAPRV_ACTN_USID_6( fsValue );
            break;
          case 7:
               setAPRV_ACTN_USID_7( fsValue );
            break;
          case 8:
                  setAPRV_ACTN_USID_8( fsValue );
            break;
          case 9:
               setAPRV_ACTN_USID_9( fsValue );
            break;
          case 10:
               setAPRV_ACTN_USID_10( fsValue );
            break;
                default:
      }
   }

   /**
    * Returns true if the given Approval Level has the given Ordinal No.(Routing Sequence No.),
    * else returns false.
    * Functions similar to method getAllAprvLvlsRequiredForOrdNo except that this method does the
    * processing only for the given Approval Level. Refer documentation on
    * method getAllAprvLvlsRequiredForOrdNo for more details.
    * @param fiOrdNo Ordinal No.(Routing Sequence No.)
    * @param fiAprvLvl Approval Level
    * @return boolean
    */
   public boolean isAprvLvlRequiredForOrdNo( int fiOrdNo, int fiAprvLvl)
   {
      long llOrdNoMap = getORD_NO_MAP(fiOrdNo);

      if ( (llOrdNoMap & APRV_LVL_MASKS[fiAprvLvl - 1]) > 0 )
      {
         return true;
      }
      else
      {
         return false;
      }

   }//end of method


   /**
    * Gets all Approval Levels having given Ordinal No.(Routing Sequence No.).
    * Example 1: fiOrdNo = 3
    * Get value of ORD_NO_3_MAP(index=3) and find all the Approval Levels set with Routing Sequence No. 3
    * as follows: Suppose ORD_NO_3_MAP contains int 65(bits 001000001).
    * Perform AND bit operation on ORD_NO_3_MAP value and each value on Array APRV_LVL_MASKS[] as follows:
    * 001000001 & APRV_LVL_MASKS[0] = 001000001 & 000000001 = 000000001 which means the first bit
    * on right is set to 1,i.e., Approval Level 1 is set with Routing Sequence No. 3.
    * Populate liIntArray[0] = 1 to indicate Approval Level 1 is included.
    * Carrying on, 001000001 & APRV_LVL_MASKS[1] = 001000001 & 000000010 = 000000000(zero),
    * so Approval Level 2 is not set with Routing Sequence No. 3.
    * And do similar processing with rest of the elements till APRV_LVL_MASKS[5].
    * Now 001000001 & APRV_LVL_MASKS[6] = 001000001 & 001000000 = 001000000 which means seventh bit
    * from right is set to 1,i.e., Approval Level 7 is set with Routing Sequence No. 3.
    * Populate liIntArray[1] = 7 to indicate Approval Level 7 is included.
    * Do similar processing with rest of the elements.
    * At end liIntArray[0] = 1, liIntArray[1] = 7, rest liIntArray[x] = 0 which indicates Approval Levels
    * 1 and 7 have Routing Sequence of 3.
    *
    * Example 2: fiOrdNo = 14
    * Get value of ORD_NO_4_MAP(index=4) and rest of the logic is same as above.
    */
   public int[] getAllAprvLvlsRequiredForOrdNo( int fiOrdNo )
   {
      int[] liIntArray = new int[NUM_APPROVAL_LEVELS];
      int liIndex = 0;
      //Get Ordinal No. Map for given Ordinal No. (note practically fiOrdNo can range from 1 to 15)
      long llOrdNoMap = getORD_NO_MAP(fiOrdNo);

      //Loop through NUM_APPROVAL_LEVELS(20) values
      for (int liCtr = 0; liCtr < NUM_APPROVAL_LEVELS; liCtr++)
      {
         if ( (llOrdNoMap & APRV_LVL_MASKS[liCtr]) > 0 )
         {
            liIntArray[liIndex] = liCtr + 1;
            liIndex++;
         }
      }//end for

      return liIntArray;
   }//end of method


   /**
    * Sets ORD_NO_x_MAP(x=1 to 10) field with the given Approval Level associated with
    * the given Ordinal No. (ORD_NO Routing Sequence No.).
    * Example 1: fiOrdNo = 3, fiAprvLvl = 1,
    * which means Approval Level 1 set for Routing Sequence No. 3.
    * ORD_NO_3_MAP(x = fiOrdNo,i.e., Routing Sequence No.) is set as follows:
    * Do OR bit operation on APRV_LVL_MASKS[fiAprvLvl-1=0] and current value of ORD_NO_3_MAP,
    * i.e., bits 00001 | 00000 = 00001 (int 1)
    * ORD_NO_3_MAP is set to 00001 (int 1). Looking at the bit pattern for ORD_NO_3_MAP,
    * it is evident that bit 1(flag) is turned on for first position on right, implying
    * for Routing Sequence No.=3, it would be routed to Approval Level 1.
    *
    * Example 2: Continuing with Example 1, this method is invoked for second time
    * with fiOrdNo = 3, fiAprvLvl = 7,
    * which means Approval Level 7 is also set for Routing Sequence No. 1.
    * ORD_NO_7_MAP(7 = fiOrdNo,i.e., Routing Sequence No.) is set as follows:
    * Do OR operation on APRV_LVL_MASKS[fiAprvLvl-1=6] and current value of
    * ORD_NO_7_MAP(00001, set earlier),
    * i.e., bits 001000000 | 00001 = 001000001 (int 65)
    * ORD_NO_7_MAP is set to 001000001 (int 65). Looking at the bit pattern for ORD_NO_7_MAP,
    * it is evident that bit 1(flag) is turned on for first and seventh position from right, implying
    * for Routing Sequence No.=3, it would be routed to Approval Levels 1 and 7.
    *
    * @param fiOrdNo Ordinal No.(ORD_NO Routing Sequence No.)
    * @param fiAprvLvl Approval Level
    * @return boolean
    */
   public boolean setAprvLvlRequiredForOrdNo( int fiOrdNo, int fiAprvLvl)
   {
      //Get existing value first
      long llOrdNoMap = getORD_NO_MAP(fiOrdNo);
      llOrdNoMap |= APRV_LVL_MASKS[fiAprvLvl - 1];
      setORD_NO_MAP(fiOrdNo, llOrdNoMap);
      return true;
   }//end of method

      /**
       * The save() method will override data object save.
       *
       * @author Mark Farrell - initial version 07/27/2000
    */
      protected boolean myDataObjectSave()
   //@SuppressAbstract
      {
         // Determine which action was initiated on the client.
         String lsDI_ACTN_CD = getSession().getProperty(ATTR_DI_ACTN_CD);
         int liAction = 0;

         if (lsDI_ACTN_CD == null || lsDI_ACTN_CD.equals(""))
         {
            ; // drop out of if
         }
         else
         {
            try
            {
               liAction = Integer.parseInt(lsDI_ACTN_CD);
            }
            catch (Exception loEx)
            {
               liAction = -1;
            }

            switch (liAction)
            {
               case DOC_ACTN_RECALL :
                  doRecall() ;
                  break ;
                        default :
                  break ;
            } /* end switch (liAction) */
         } /* end if (lsDI_ACTN_CD == null || lsDI_ACTN_CD.equals("") */
         return true;
         //super.save();
      }

  /**
   * The doRecall() method will perform the recall action.
   * @return boolean : if succcessful returns boolean true.
   *
   * @author Mark Farrell - initial version 07/27/2000
   */
   private void doRecall()
   {
      int liLastAction = this.getLAST_ACTN_APRV_LVL() ;
      boolean lboolResult ;
      Session loSession = getSession();
      AMSDocHeader loDocHdr = null;
      SearchRequest loSearchReq = null;
      String lsHeaderTable = null;
      boolean lboolAuthorized = false;

      loSearchReq = new SearchRequest();

            lsHeaderTable = getDOC_TYP() + "_DOC_HDR";

            loSearchReq.addParameter(lsHeaderTable, ATTR_DOC_CD, getDOC_CD());
            loSearchReq.addParameter(lsHeaderTable, ATTR_DOC_ID, getDOC_ID());
            loSearchReq.addParameter(lsHeaderTable, ATTR_DOC_DEPT_CD, getDOC_DEPT_CD());
            loSearchReq.addParameter(lsHeaderTable, ATTR_DOC_VERS_NO,
                  Integer.toString(getDOC_VERS_NO()));

            try
            {
               loDocHdr = (AMSDocHeader)
                     AMSDataObject.getObjectByKey(lsHeaderTable,
                     loSearchReq,loSession);
            }
            catch (Exception loExcep)
            {
               raiseException("Exception encountered while trying to "
                     + " retrieve the document information");
               return;
            }
            finally
            {
               if(loDocHdr == null)
               {
                  raiseException("Unable to retrieve Document Header information");
                  return;
               }
            }
      try
      {
         /*
          * Ensure that the user can recall the document.
          */
         lboolAuthorized = AMSSecurity.actionAuthorized(loSession.getUserID(),
            getDOC_CD(), DOC_ACTN_RECALL);
      }
      catch(AMSSecurityException loExcep)
      {
         lboolAuthorized = false;
      }
      finally
      {
            if(!lboolAuthorized)
            {
               AMSSecurityLogControl
                     .logDocUnauthorizedAction(loDocHdr,
                           "Not authorized to perform selected action.",
                  DOC_ACTN_RECALL ) ;
            raiseException("Not authorized to perform Recall action.");
            return;
         }
      }
     
      /**
       * Call security to check if the user has access at the component level
       * for workflow actions. This will require for the entire document
       * to be loaded and traversed
       */
      if (!checkWFCompLevelSecurity(loDocHdr, AMSCommonConstants.DOC_ACTN_RECALL))
      {
         return;
      }

      // log the action - do it before save() in order
      // to have a valid old status value
      WF_APRV_LOGImpl.logActionEvent(this, liLastAction, true);

      loSession.setProperty(ATTR_DI_ACTN_CD, "");
      //Aprv Lvl Change: loSession added
      lboolResult = AMSApprovalProcessor.processRecallAction( this , loSession) ;

      // Delete the Document Signature for this Document
      E_SIG_CTLGImpl.deleteDocSignatureForAprvLvl(loSession, getDOC_CD(),
            getDOC_DEPT_CD(), getDOC_ID(), getDOC_VERS_NO(), liLastAction);
      
      loSession.setProperty("RECALLS", String.valueOf(lboolResult));
   }

   /**
    * Gets the index value based on the given Ordinal No.(ORD_NO Routing Sequence No.)
    * For example: Note 10 in these examples implies AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK.
    * When fiIndex in range of 1 to 10(both inclusive), then returns fiIndex.
    * When fiIndex in range of 11 to 20(both inclusive), then returns (fiIndex - 10).
    * @param fiIndex Ordinal Number
    * @return int
   */
   private int getIndexFromOrdNo(int fiIndex)
   {
      int liIndex = fiIndex;
      int liRemainder ;

      //When fiIndex in range of 1 to 10(both inclusive), then returns fiIndex.
      if (fiIndex <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
      {
          liIndex = fiIndex;
      }
      else //Handling approval level > 10
      {
         //Keep on reducing fiIndex with chunks of 10(AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
         //and return the remainder,e.g.,: When fiIndex = 12 then return 2.
         while (liIndex >= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
         {
            liRemainder = liIndex - AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK;
            if(liRemainder <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
            {
               liIndex = liRemainder;//Aprv Lvl Change: this value will be returned as index
               break;//break from while loop
            }
            else
               liIndex = liRemainder;
            }//end else
         }//end while

         return liIndex;
     }//end of method

   /**
    * Gets the index value based on the given Approval Level.
    * For example: Note 10 in these examples implies AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK.
    * When fiIndex in range of 1 to 10(both inclusive), then returns fiIndex.
    * When fiIndex in range of 11 to 20(both inclusive), then returns (fiIndex - 10).
    * @param fiIndex Approval Level
    * @return int
    */
   private int getIndexFromAprvLevel(int fiIndex)
   {

      int liIndex = fiIndex;
      int liRemainder ;

      if (fiIndex <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
      {
         //When fiIndex in range of 1 to 10(both inclusive), then returns fiIndex.
         liIndex = fiIndex;

      }
      else //handling approval level > 10
      {
         //Keep on reducing fiIndex with chunks of 10(AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
         //and return the remainder,e.g.,: When fiIndex = 12 then return 2.
         while (liIndex >= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
         {
            liRemainder = liIndex - AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK;
            if(liRemainder <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
            {
               liIndex = liRemainder;//Aprv Lvl Change: this value will be returned as index
               break;//break from while loop
            }
            else
               liIndex = liRemainder;
         }//end while
      }//end else

      return liIndex;
  }//end of method

   /**
    * Checks Self-Approval related restrictions. Returns true when the Action should be restricted
    * as per Self-Approval setting i.e., when any of the following conditions are satisfied.
    * <ul>
    * <li> Self-Approval is set to 'Creator restricted' or 'Creator/Submitter restricted'
    * and the User ID is the Creator of the Document
    * and Action is Take Task or Reassign(to Creator) or Approve or Reject or Reject All or Unapprove.
    * <li> Self-Approval is set to 'Submitter restricted' or 'Creator/Submitter restricted'
    * and the User ID is the Submitter of the Document
    * and Action is Take Task or Reassign(to Submitter) or Approve or Reject or Reject All or Unapprove.
    * </ul>
    * In all other cases returns false that implies Action is not restricted as per Self-Approval setting,
    * including case where Self-Approval is set to 'no restriction'..
    * @param fiAction the action code user performed
    * @param fsDocCode the document's doc code
    * @param fsDocCeptCode the document's department code
    * @param fsDocID the document's ID
    * @param fiDocVer the document's version number
    * @param fiApprovalLevel  the workflow approval level
    * @param fsSelfAprvRsct   the Self-Approval setting on the Approval Rule record
    * @param fsCreator  the document's creator ID
    * @param fsSubmitter the document's submitter ID
    * @param fsUserIdToVerify the User ID to be evaluated
    * @param foSession  session object
    * @return boolean
    */
   public static boolean isSelfAprvRestricted(int fiAction, String fsDocCode, String fsDocDeptCode,
      String fsDocID, int fiDocVer, int fiApprovalLevel, String fsSelfAprvRsct,
      String fsCreator, String fsSubmitter, String fsUserIdToVerify, Session foSession )
   {
      //Return false when User ID to be evaluated is null or Self-Approval is 'no restriction'.
      if(AMSStringUtil.strIsEmpty(fsUserIdToVerify) ||
         fsSelfAprvRsct.equals(CVL_SLF_APRV_RSTImpl.SLF_APRV_RSCT_NONE))
      {
         return false;
      }
      else
      {
         String lsAction = null;
         int liEventType = 0;
         boolean lboolReturn = false;

         /*
          * Raise error message when all of the following conditions are satisfied:
          * 1. Self-Approval is set to 'Creator restricted' or 'Creator/Submitter restricted'
          * 2. The User ID is the Creator of the Document
          * 3. Action is Take Task or Reassign(to Creator) or Approve or Reject or Reject All or Unapprove
          */
         if( (fsSelfAprvRsct.equals(CVL_SLF_APRV_RSTImpl.SLF_APRV_RSCT_CREA) ||
            fsSelfAprvRsct.equals(CVL_SLF_APRV_RSTImpl.SLF_APRV_RSCT_BOTH))
         && fsCreator.equalsIgnoreCase(fsUserIdToVerify))
         {
            switch (fiAction)
            {
               case WL_ACTN_TAKE_TASK:
               {
                  AMSDataObject.raiseException("%c:Q0100,v:perform Take Task on%", foSession);
                  lsAction="take task on";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_ROUTING;
                  break;
               }
               case DOC_ACTN_REASSIGN:
               {
                  //In this case fsUserIdToVerify passed in is the ASSIGNEE
                  AMSDataObject.raiseException("%c:Q0105%", foSession);
                  lsAction="be assigned to";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_ROUTING;
                  break;
               }
               case DOC_ACTN_APPROVE:
               {
                  AMSDataObject.raiseException("%c:Q0100,v:Approve%", foSession);
                  lsAction="approve";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL;
                  break;
               }
               case DOC_ACTN_REJECT:
               case DOC_ACTN_REJECT_ALL:
               {
                  AMSDataObject.raiseException("%c:Q0100,v:Reject%", foSession);
                  lsAction="reject";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL;
                  break;
               }
               case DOC_ACTN_UNAPPROVE:
               {
                  AMSDataObject.raiseException("%c:Q0100,v:Unapprove%", foSession);
                  lsAction="unapprove";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL;
                  break;
               }
               default:
               {
                  //This action code should not be restricted.
                  return false;
               }
           }
           //Action is restricted
           lboolReturn = true;
        }
        /*
         * Raise error message when all of the following conditions are satisfied:
         * 1. Self-Approval is set to 'Submitter restricted' or 'Creator/Submitter restricted'
         * 2. The User ID is the Submitter of the Document
         * 3. Action is Take Task or Reassign(to Submitter) or Approve or Reject or Reject All or Unapprove
         */
        else if((fsSelfAprvRsct.equals(CVL_SLF_APRV_RSTImpl.SLF_APRV_RSCT_SBMT)||
           fsSelfAprvRsct.equals(CVL_SLF_APRV_RSTImpl.SLF_APRV_RSCT_BOTH))
         && fsSubmitter.equalsIgnoreCase(fsUserIdToVerify))
        {
           switch (fiAction)
           {
              case WL_ACTN_TAKE_TASK:
              {
                  AMSDataObject.raiseException("%c:Q0101,v:perform Take Task on%", foSession);
                  lsAction="take task on";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_ROUTING;
                  break;
              }
              case DOC_ACTN_REASSIGN:
              {
                  AMSDataObject.raiseException("%c:Q0106%", foSession);
                  lsAction="be assigned to";
                  liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_ROUTING;

                  break;
              }
              case DOC_ACTN_APPROVE:
              {
                 AMSDataObject.raiseException("%c:Q0101,v:Approve%", foSession);
                 lsAction="approve";
                 liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL;
                 break;
              }
              case DOC_ACTN_REJECT:
              case DOC_ACTN_REJECT_ALL:
              {
                 AMSDataObject.raiseException("%c:Q0101,v:Reject%", foSession);
                 lsAction="reject";
                 liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL;
                 break;
              }
              case DOC_ACTN_UNAPPROVE:
              {
                 AMSDataObject.raiseException("%c:Q0101,v:Unappprove%", foSession);
                 lsAction="unapprove";
                 liEventType = WF_APRV_PROC_LOGImpl.EVNT_TYP_GENERAL;
                 break;
               }
              default:
              {
                 //The action code should not be restricted.
                 return false;
              }
           }// end of switch

            //Action is restricted
            lboolReturn = true;
        }
        else
        {
           //Action is not restricted
           return false;
        }

        if(lboolReturn)
        {
           WF_APRV_PROC_LOGImpl.logAprvProcEvent(foSession, fsDocCode, fsDocDeptCode,
               fsDocID, fiDocVer, liEventType, lsAction +"failed for user" + fsUserIdToVerify
               + "on approval level" + fiApprovalLevel);
        }

        return lboolReturn;

      }// end else
   }//end of method

   /**
    * Calls a save on the document header to check if component level
    * security needs to be checked. This method will only call the save
    * on the header only if the App Control parameter APPLY_WF_COMP_SEC is set
    * to true
    *
    * @param foDocHdr the document header to be archived
    * @param fiActnCd workflow action being performed
    * @return void
    *
    */
   private boolean checkWFCompLevelSecurity(AMSDocHeader foDocHdr, int fiActnCd)
   {
      if (AMSStringUtil.strInsensitiveEqual(IN_APP_CTRLImpl
                        .getParameterValue(AMSCommonConstants.APPLY_WF_COMP_SEC,
                              foDocHdr.getSession()), "true"))
      {
         try
         {
            // Set the action code as a session property
            foDocHdr.getSession().setProperty(
               AMSCommonConstants.SESSION_WF_ACTN, Integer.toString(fiActnCd));

            // Call the save on the document header but retain the state of the
            // document header prior to this
            return foDocHdr.processDocumentAction(AMSCommonConstants.DOC_ACTN_CHK_WF_COMP_SEC);
         } /* end try */
         finally
         {
            // reset the session property
            foDocHdr.getSession().setProperty(AMSCommonConstants.SESSION_WF_ACTN,"");
         } /* end finally */
      } /* end if (AMSStringUtil.strInsensitiveEqual(IN_APP_CTRLImpl ... */

      return true;
   }
   
   /**
    * Returns value of WARN_ESCL_TMPL_ID_x for given Approval Level x
    * 
    * @param fiApprovalLevel Approval Level for which value of 
    *                WARN_ESCL_TMPL_ID_x is required 
    */
   protected String getWARN_ESCL_TMPL_ID(int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      Data loWarnTemplId = getData(moColumnName.append("WARN_ESCL_TMPL_ID_")
                                               .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loWarnTemplId != null)
      {
         return loWarnTemplId.getString();
      }
      else
      {
         return null;
      }
   }// end of method
   
   /**
    * Sets the value of WARN_ESCL_TMPL_ID_x for given Approval Level x
    * 
    * @param fiApprovalLevel Approval Level for which value of 
    *                        WARN_ESCL_TMPL_ID_x needs to be set
    * @param fsWarnTemplId Value to be set to WARN_ESCL_TMPL_ID_x 
    */
   protected void setWARN_ESCL_TMPL_ID(int fiApprovalLevel, String fsWarnTemplId)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      getData(moColumnName.append("WARN_ESCL_TMPL_ID_").append(fiApprovalLevel)
                  .toString()).setString(fsWarnTemplId);
      clearColNameBuilder();
   }// end of method
   
   /**
    * Returns value of WARN_THLD_AGE_x for given Approval Level x
    * 
    * @param fiApprovalLevel Approval Level for which value of 
    *                WARN_THLD_AGE_x is required 
    */
   protected Integer getWARN_THLD_AGE(int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      Data loWarnThreshold = getData(moColumnName.append("WARN_THLD_AGE_")
                                 .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loWarnThreshold != null)
      {
         return loWarnThreshold.getint();
      }
      else
      {
         return 0;
      }
   }// end of method
   
   /**
    * Sets the value of WARN_THLD_AGE_x for given Approval Level x
    * @param fiApprovalLevel Approval Level x for which value of WARN_THLD_AGE_x
    *                        needs to be set
    * @param fiWarnThreshold Value to be set to WARN_THLD_AGE_x
    */
   protected void setWARN_THLD_AGE(int fiApprovalLevel, int fiWarnThreshold)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      getData(moColumnName.append("WARN_THLD_AGE_").append(fiApprovalLevel).toString())
         .setint(fiWarnThreshold);
      clearColNameBuilder();
   }// end of method
   
   /**
    * Returns value of ESCL_FL_x for given approval level
    * where x is the specified approval level
    * 
    * @param fiApprovalLevel Approval level for which value of 
    *                ESCL_FL_x is required 
    */
   protected boolean getESCL_FL(int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      Data loEsclFl = getData(moColumnName.append("ESCL_FL_")
                                          .append(fiApprovalLevel).toString()); 
      clearColNameBuilder();
      if( loEsclFl != null)
      {
         return loEsclFl.getboolean();
      }
      else
      {
         return false;
      }
   }// end of method
   
   /**
    * Sets the value of ESCL_FL_x for given Approval Level x
    * 
    * @param fiApprovalLevel Approval Level x for which value of ESCL_FL_x
    *                        needs to be set
    * @param fboolEsclFl Value to be set to ESCL_FL_x
    */
   protected void setESCL_FL(int fiApprovalLevel, boolean fboolEsclFl)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      getData(moColumnName.append("ESCL_FL_").append(fiApprovalLevel)
            .toString()).setboolean(fboolEsclFl);
      clearColNameBuilder();
   }// end of method
   
   /**
    * Returns value of ESCL_THLD_AGEx_y for given Escalation Level x 
    * and Approval Level y
    * 
    * @param fiEscalationLevel Escalation Level x for which value of 
    *                ESCL_THLD_AGEx_y is required
    * @param fiApprovalLevel Approval Level y for which value of 
    *                ESCL_THLD_AGEx_y is required 
    */
   protected int getESCL_THLD_AGE(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      Data loEsclThldAge = getData(moColumnName.append("ESCL_THLD_AGE")
                                               .append(fiEscalationLevel).append(UNDER_SCORE)
                                               .append(fiApprovalLevel).toString()); 
      clearColNameBuilder();
      if( loEsclThldAge != null )
      {
         return loEsclThldAge.getint();
      }
      else
      {
         return 0;
      }
   }// end of method
   
   /**
    * Sets the value of ESCL_THLD_AGEx_y for given Approval Level x and Escalation Level y
    * 
    * @param fiEscalationLevel Escalation Level x
    * @param fiApprovalLevel Approval Level y
    * @param fiEsclThreshAge Value of ESCL_THLD_AGEx_y
    */
   protected void setESCL_THLD_AGE(int fiEscalationLevel, int fiApprovalLevel,
         int fiEsclThreshAge)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      getData(moColumnName.append("ESCL_THLD_AGE").append(fiEscalationLevel)
            .append(UNDER_SCORE).append(fiApprovalLevel).toString()).setint(fiEsclThreshAge);
      clearColNameBuilder();
   }// end of method
   
   /**
    * Returns value of ESCL_ASGN_IDx_y for given Escalation Level x 
    * and Approval Level y
    * 
    * @param fiEscalationLevel Escalation Level x for which value of 
    *                ESCL_ASGN_IDx_y is required
    * @param fiApprovalLevel Approval Level y for which value of 
    *                ESCL_ASGN_IDx_y is required 
    */
   protected String getESCL_ASGN_ID(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      Data loEsclAssignee1 = getData(moColumnName.append("ESCL_ASGN_ID")
                                                 .append(fiEscalationLevel).append(UNDER_SCORE)
                                                 .append(fiApprovalLevel).toString());  
      clearColNameBuilder();
      if(loEsclAssignee1 != null)
      {
         return loEsclAssignee1.getString();
      }
      else
      {
         return null;
      }
   }// end of method

   
   /**
    * Sets the value of ESCL_ASGN_IDx_y for given Approval Level x and Escalation Level y
    * 
    * @param fiEscalationLevel Escalation Level x
    * @param fiApprovalLevel Approval Level y
    * @param fsEsclAssignee Value of ESCL_ASGN_IDx_y
    */
   protected void setESCL_ASGN_ID(int fiEscalationLevel, int fiApprovalLevel,
         String fsEsclAssignee)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();
      
      getData(moColumnName.append("ESCL_ASGN_ID").append(fiEscalationLevel)
            .append(UNDER_SCORE).append(fiApprovalLevel).toString()).setString(fsEsclAssignee);
      clearColNameBuilder();
   }// end of method
   
   /**
    * Returns ESCL_ASGN_ROLE_FLx_y for given Escalation Level x 
    * and Approval Level y
    * 
    * @param fiEscalationLevel Escalation Level x for which value of 
    *                ESCL_ASGN_ROLE_FLx_y is required
    * @param fiApprovalLevel Approval level y for which value of 
    *                ESCL_ASGN_ROLE_FLx_y is required 
    */
   protected boolean getESCL_ASGN_ROLE_FL(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);
      
      clearColNameBuilder();
      
      Data loEsclAssigneeRoleFl = getData(moColumnName.append("ESCL_ASGN_ROLE_FL")
                                                    .append(fiEscalationLevel).append(UNDER_SCORE)
                                                    .append(fiApprovalLevel).toString());  
      clearColNameBuilder();
      if(loEsclAssigneeRoleFl != null)
      {
         return loEsclAssigneeRoleFl.getboolean();
      }
      else
      {
         return false;
      }
   }// end of method

   /**
    * Sets the value of ESCL_ASGN_ROLE_FLx_y for given Approval Level x and Escalation Level y
    * 
    * @param fiEscalationLevel Escalation Level x
    * @param fiApprovalLevel Approval Level y
    * @param fboolEsclAsgnRoleFl Value of ESCL_ASGN_ROLE_FLx_y
    */
   protected void setESCL_ASGN_ROLE_FL(int fiEscalationLevel, int fiApprovalLevel,
         boolean fboolEsclAsgnRoleFl)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();
      
      getData(moColumnName.append("ESCL_ASGN_ROLE_FL").append(fiEscalationLevel)
            .append(UNDER_SCORE).append(fiApprovalLevel).toString()).setboolean(fboolEsclAsgnRoleFl);
      clearColNameBuilder();
   }// end of method

   /**
    * Returns the Worksheet corresponding to the given Worklist record
    * 
    * @param foWorklistRecord Worklist record
    * @param foSession Session object
    * @return WF_APRV_WRK_LSTImpl instance
    */
   public static WF_APRV_SHImpl getWorksheet(WF_APRV_WRK_LSTImpl foWorklistRecord, Session foSession)
   {
      // Sequence Number should be 2 if the Approval Level is greater than 10
      int liSequenceNo = (foWorklistRecord.getAPRV_LVL() > 10) 
                           ? 2
                           : 1;
      SearchRequest loWorksheetSearch = new SearchRequest();

      loWorksheetSearch.addParameter("WF_APRV_SH", ATTR_DOC_CD,
            foWorklistRecord.getDOC_CD());
      loWorksheetSearch.addParameter("WF_APRV_SH", ATTR_DOC_DEPT_CD,
            foWorklistRecord.getDOC_DEPT_CD());
      loWorksheetSearch.addParameter("WF_APRV_SH", ATTR_DOC_ID,
            foWorklistRecord.getDOC_ID());
      loWorksheetSearch.addParameter("WF_APRV_SH", ATTR_DOC_VERS_NO, Integer
            .toString(foWorklistRecord.getDOC_VERS_NO()));
      loWorksheetSearch.addParameter("WF_APRV_SH", "SEQ_NO", Integer
            .toString(liSequenceNo));

      WF_APRV_SHImpl loWorksheetRec = (WF_APRV_SHImpl) WF_APRV_SHImpl
            .getObjectByKey(loWorksheetSearch, foSession);
      
      return loWorksheetRec;
   }// end of method
   
   /** Clears out the Column Name Builder */
   private void clearColNameBuilder()
   {
      moColumnName.delete(0, moColumnName.length());
   }// end of method
   
}// end of class
