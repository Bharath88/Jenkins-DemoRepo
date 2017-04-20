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
**  WF_ESCL_TRACK
*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_ESCL_TRACKImpl extends  WF_ESCL_TRACKBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /** Worklist action Warning */
   public static String WRKLST_ACTN_WARN           =  "WARN";
   
   /** Worklist action Escalate Level 1/2 */
   public static String WRKLST_ACTN_ESCL           =  "ESCL";
   
   /** Worklist action Notify Original Assignee */
   public static String WRKLST_ACTN_NOTIF_ORIG     =  "NOTIF_ORIG";
   
	//{{COMP_CLASS_CTOR
	public WF_ESCL_TRACKImpl (){
		super();
	}
	
	public WF_ESCL_TRACKImpl(Session session, boolean makeDefaults)
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
		public static WF_ESCL_TRACKImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new WF_ESCL_TRACKImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
   /**
    * Inserts a new record for Warning/Escalation. This record is later
    * used by Automated Workflow Escalations job to compose the E-mails and 
    * generate report. For Warning action, if the Assignee on the Worklist Item
    * is a Role and if Take Task has not yet been performed on it, record 
    * is created for each Role owner, otherwise a record is created only for 
    * the user who has performed the Take Task. For Escalation action, if
    * the Escalate Assignee specified on the Worksheet is a Role, record 
    * is created for each Role owner otherwise for the Escalate Assignee User only.
    * 
    * @param flJobId Id of the job processing the Worklist items
    * @param fsAction Action to be taken on the Worklist Item
    * @param fiEsclLevel Escalation Level to be performed, 0 if action is Warning
    * @param fiAge Calculated Age of the Worklist Item
    * @param foWorklistItem Worklist Item on which the Action is being taken
    * @param foWorksheet Worksheet corresponding to the Worklist item
    * @param foSession Session object
    */
   /*
    * Search for Keyword : ADD_ESCL_TRACK in AutomatedWorkflowEscalations.java for 
    * details on how the Track table is populated
    */
   public static void insertWarnEsclRecord(long flJobId, String fsAction, int fiEsclLevel,
         int fiAge, WF_APRV_WRK_LSTImpl foWorklistItem,
         WF_APRV_SHImpl foWorksheet, Session foSession)
   {
      String lsAssignee = null;
      String lsEmailAssignee = null;
      boolean lboolAssigneeIsRole = false;
      // Get the Approval Level
      int liApprovalLevel = foWorklistItem.getAPRV_LVL();
      String lsNotifTemplId = foWorksheet.getWARN_ESCL_TMPL_ID(liApprovalLevel);

      // Check if the action is WARN or NOTIF_ORIG
      if (AMSStringUtil.strEqual(fsAction, WRKLST_ACTN_WARN)
            || AMSStringUtil.strEqual(fsAction, WRKLST_ACTN_NOTIF_ORIG))
      {
         lsAssignee = foWorklistItem.getASSIGNEE();
         lboolAssigneeIsRole = foWorklistItem.getASSIGNEE_FL();

         // Check if the Assignee is a Role
         if (lboolAssigneeIsRole)
         {
            lsEmailAssignee = foWorklistItem.getLOCK_USID();
            // Check if the task has been taken
            if (!AMSStringUtil.strIsEmpty(lsEmailAssignee))
            {
               // Add a new record. Note that the E-mail notification will be sent only to 
               // the user who has taken the task.
               addRecord(flJobId, fiEsclLevel, fiAge, foWorklistItem,
                     lsAssignee, lboolAssigneeIsRole, lsEmailAssignee, 
                     fsAction, lsNotifTemplId, foSession);
            }/* end if(!AMSStringUtil.strIsEmpty(foWorklistItem.getLOCK_USID())) */
            else
            {
               Vector<String> lvRoleUsers = R_WF_USER_ROLEImpl.getRoleUsers(
                     lsAssignee, foSession);
               for (String lsUserId : lvRoleUsers)
               {
                  // Add a record for each User for this Role. The e-mail
                  // notification will be sent to each User.
                  lsEmailAssignee = lsUserId;
                  addRecord(flJobId, fiEsclLevel, fiAge, foWorklistItem, lsAssignee,
                        lboolAssigneeIsRole, lsEmailAssignee, fsAction, lsNotifTemplId, foSession);
               }/* end for(String lsUserId : lvRoleUsers) */
            }/* end if else */
         }/* end if(foWorklistItem.getASSIGNEE_FL()) */
         else
         {
            // Add record for the Assignee. E-mail notification will be sent to
            // Assignee himself
            lsEmailAssignee = lsAssignee;
            addRecord(flJobId, fiEsclLevel, fiAge, foWorklistItem, lsAssignee,
                  lboolAssigneeIsRole, lsAssignee, fsAction, lsNotifTemplId,
                  foSession);
         }/* end if else */
      }/* end if (AMSStringUtil.strEqual(fsAction, WF_APRV_WRK_LSTImpl.WRKLST_ACTN_WARN)... */
      // check if the action is ESCL1 OR ESCL2
      else if(AMSStringUtil.strEqual(fsAction, WRKLST_ACTN_ESCL))
      {
         // Get the Escalate Assignee
         lsAssignee = foWorksheet.getESCL_ASGN_ID(fiEsclLevel, liApprovalLevel);
         lboolAssigneeIsRole = foWorksheet.getESCL_ASGN_ROLE_FL(fiEsclLevel, liApprovalLevel);
         
         // Check if the Escalate Assignee is a Role
         if (lboolAssigneeIsRole)
         {
            Vector<String> lvRoleUsers = R_WF_USER_ROLEImpl.getRoleUsers(
                  lsAssignee, foSession);
            for (String lsUserId : lvRoleUsers)
            {
               // Add a record for each User for this Role. The e-mail
               // notification will be sent to each User.
               lsEmailAssignee = lsUserId;
               addRecord(flJobId, fiEsclLevel, fiAge, foWorklistItem,
                     lsAssignee, lboolAssigneeIsRole, lsEmailAssignee, fsAction,
                     lsNotifTemplId, foSession);
            }/* end for(String lsUserId : lvRoleUsers) */
         }/* end if(foWorksheet.getESCL_ASGN_ROLE_FL... */
         else
         {
            // Add a record for the Escalate Assignee. E-mail notification will be sent to
            // Escalate Assignee himself
            lsEmailAssignee = lsAssignee;
            addRecord(flJobId, fiEsclLevel, fiAge, foWorklistItem, lsAssignee, lboolAssigneeIsRole,
                  lsEmailAssignee, fsAction, lsNotifTemplId, foSession);
         }/*end if else*/
      }/*end else if(AMSStringUtil.strEqual(fsAction, WF_APRV_WRK_LSTImpl.WRKLST_ACTN_ESCL))*/
   }// end of method
   
   /**
    * Adds a new record for the given values. Rest of the values are taken from Worklist.
    * 
    * @param flJobId Id of the job processing the Worklist items
    * @param fiEsclLevel Escalation Level to be performed, 0 if action is Warning
    * @param fiAge Calculated Age of the Worklist Item
    * @param foWorklistItem Worklist Item on which the Action is being taken
    * @param fsAssignee Assignee id for the new record
    * @param fboolIsAssigneeRole Signifies if fsAssigneeUser is a Role Id
    * @param fsEmailAssignee Assignee User Id to whom the e-mail notification has to be sent
    * @param fsAction Action to be taken on the Worklist Item
    * @param fsNotifTemplId Notification Template Id to be used during Email generation
    * @param foSession Session object
    */
   /*
    * Search for Keyword : ADD_ESCL_TRACK in AutomatedWorkflowEscalations.java for 
    * details on how the Track table is populated
    */
   public static void addRecord(long flJobId, int fiEsclLevel, int fiAge, 
         WF_APRV_WRK_LSTImpl foWorklistItem, String fsAssignee, boolean fboolIsAssigneeRole,
         String fsEmailAssignee, String fsAction, String fsNotifTemplId, Session foSession)
   {
      // Create a new temp object
      WF_ESCL_TRACKBaseImpl loNewEsclTrackObj = getNewObject(foSession, true);
      
      // Create a Vector of attributes to be excluded from copying to new temp object
      Vector lvExcludeAttributes = new Vector();
      lvExcludeAttributes.add("ASSIGNEE");
      lvExcludeAttributes.add("ASSIGNEE_FL");
      lvExcludeAttributes.add("ESCL_LEVEL");
      
      // Copy the matching values from Worklist to the new temp object
      loNewEsclTrackObj.copyCorresponding(foWorklistItem, lvExcludeAttributes, true);
      
      // Set specific values to the new temp object
      loNewEsclTrackObj.setJOB_ID(flJobId);
      loNewEsclTrackObj.setAGE(fiAge);
      loNewEsclTrackObj.setASSIGNEE(fsAssignee);
      loNewEsclTrackObj.setASSIGNEE_FL(fboolIsAssigneeRole);
      loNewEsclTrackObj.setEMAIL_ASSIGNEE(fsEmailAssignee);
      loNewEsclTrackObj.setESCL_LEVEL(fiEsclLevel);
      loNewEsclTrackObj.setACTION(fsAction);
      loNewEsclTrackObj.setACTION_DT(new VSDate());
      loNewEsclTrackObj.setNOTIF_TMPL(fsNotifTemplId);
      // Save the new object
      loNewEsclTrackObj.save();
   }// end of method
   
   /**
    * Returns record count for the given Job Id, Action and Escalation Level.
    * If Action is empty, Action and Escalation Level are not considered in 
    * the search criteria.
    * 
    * @param fosession Session object
    * @param flJobId Job Id for which Record Count is required
    * @param fsAction Action for which Record Count is required
    * @param fiEsclLevel Escalation Level for which Record Count is required
    * @return The record count for a given Job Id
    */
   public static int getTrackRecordsCount(Session fosession, Long flJobId, String fsAction, 
         int fiEsclLevel)
   {
      SearchRequest loWfEsclTrackSech = new SearchRequest();
      
      loWfEsclTrackSech.addParameter("WF_ESCL_TRACK", "JOB_ID", Long.toString(flJobId));
      if(!AMSStringUtil.strIsEmpty(fsAction))
      {
         loWfEsclTrackSech.addParameter("WF_ESCL_TRACK", "ACTION", fsAction);
         loWfEsclTrackSech.addParameter("WF_ESCL_TRACK", "ESCL_LEVEL", 
               Integer.toString(fiEsclLevel));
      }
      
      return WF_ESCL_TRACKImpl.getObjectCount(loWfEsclTrackSech, fosession);
   }// end of method
}// end of class

