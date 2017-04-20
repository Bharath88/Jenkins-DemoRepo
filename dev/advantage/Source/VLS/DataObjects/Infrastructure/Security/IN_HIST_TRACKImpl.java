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
**  IN_USER_ROLE_HIST
*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_HIST_TRACKImpl extends  IN_HIST_TRACKBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public IN_HIST_TRACKImpl (){
		super();
	}
	
	public IN_HIST_TRACKImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static IN_HIST_TRACKImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new IN_HIST_TRACKImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
		
	   /**
	    * Logs the action of assignment of a Resource to Entity onto the Historical Tracking table.
	    * 
	    * E.g. In case of Security Roles assignment(insert into R_SC_USER_ROLE_LNK), this method is 
	    * called to add a new entry to Historical Tracking table(IN_HIST_TRACK) to track the 
	    * assignment of the Role to User. In this case User is the Entity and Security Role is the
	    * Resource on the new entry.
	    * 
		 * @param foRow       AMSDataObject     The DataObject for which tracking is being done.
		 * @param foEntity    ArrayList<String> Collection of Entities for which tracking has to be 
		 *                                      performed.
		 * @param fsResource  String            Resource whose assignment/ revocation is being tracked.
		 */
		public static void trackAssignment(AMSDataObject foRow, ArrayList<String> foEntity, 
            String fsResource)
	   {
	      /*
	       * There is a field named Revoke Reason on some pages involved in Historical Tracking.
	       * This is a non-persistent field and stores the reason for revocation.
	       * The value of this field is made available on the VLS side through a session variable.
	       * Hence we fetch the Revoke Reason value from the session.
	       *
	       * Note that since this field is not compulsory and also it may not be present on every page
	       * involved in Historical Tracking, a null check is performed before using the value.
	       */
	      String lsReasonCode = foRow.getSession().getProperty(SESSION_REVOKE_REASON);

	      if( foEntity == null )
	      {
	         foEntity = foRow.getTrackedEntity();
	      }
	      if( AMSStringUtil.strIsEmpty(fsResource) )
	      {
	         fsResource = foRow.getTrackedResource();
	      }

	      /*
	       * For each record modified, log onto the tracking table.
	       * Ordinarily we would have only 1 assignment/ revocation, but there could be some
	       * cases where multiple assignments/revocations happen with a single action, e.g.,
	       * when User adds a new Security Role to MSS User Type on MSS Security Roles page then
	       * that single action maps to assignment of the new Security Role to all the Users
	       * who have MSS User Type set to MSS User Type.
	       */
	      for(String lsEntity: foEntity)
	      {
	         //Add new Historical Tracking record to store the assignment details
	         IN_HIST_TRACKImpl loHist = IN_HIST_TRACKImpl.getNewObject(foRow.getSession(), true);
	         loHist.setENTITY_ID( lsEntity );
	         loHist.setRESOURCE_ID( fsResource );
	         loHist.setDEPT_CD(foRow.getTrackedDeptCD(lsEntity));
	         loHist.setASSIGN_ADMN_ID( foRow.getUser() );
	         loHist.setFROM_DT( foRow.getDate() );
	         loHist.setTABLE_NAME(foRow.getComponentName());
	         if(!AMSStringUtil.strIsEmpty(lsReasonCode))
	         {
	            loHist.setREASON(lsReasonCode);
	            foRow.getSession().removeProperty(SESSION_REVOKE_REASON);
	         }
	         loHist.save();

	      } /* End for(String lsEntity: getTrackedEntity()) */

	   }

	   /**
	    * Logs the action of revocation of a Resource to Entity onto the Historical Tracking table.
	    * E.g. In case of Security Roles revocation(delete on R_SC_USER_ROLE_LNK), this method is called
	    * to update the entry to Historical Tracking table(IN_HIST_TRACK) with revocation details.
	    * The entry that is updated is the one that tracked the assignment of the Role
	    * to User.
       * 
       * @param foRow       AMSDataObject     The DataObject for which tracking is being done.
       * @param foEntity    ArrayList<String> Collection of Entities for which tracking has to be 
       *                                      performed.
       * @param fsResource  String            Resource whose assignment/ revocation is being tracked.
	    */
	   public static void trackRevocation(AMSDataObject foRow, ArrayList<String> foEntity, 
	         String fsResource)
	   {
	      Session loSession = foRow.getSession();
	      
	      /*
	       * There is a field named Revoke Reason on some pages involved in Historical Tracking.
	       * This is a non-persistent field and stores the reason for revocation.
	       * The value of this field is made available on the VLS side through a session variable.
	       * Hence we fetch the Revoke Reason value from the session.
	       *
	       * Note that since this field is not compulsory and also it may not be present on every page
	       * involved in Historical Tracking, a null check is performed before using the value.
	       */
	      String lsRevokeReason = loSession.getProperty(SESSION_REVOKE_REASON);

	      if( foEntity == null )
	      {
	         foEntity = foRow.getTrackedEntity();
	         fsResource = foRow.getTrackedResource();
	      }
	      
	      /*
	       * For each record modified, log onto the tracking table.
	       * Ordinarily we would have only 1 assignment/ revocation, but there could be some
	       * cases where multiple assignments/revocations happen with a single action, e.g.,
	       * when User deletes an entry on MSS Security Roles page then that single action
	       * maps to revocation of the associated Security Role from all the Users
	       * who have MSS User Type set to associated MSS User Type.
	       */
	      for(String lsEntity: foEntity)
	      {

	         SearchRequest lsrSearch = new SearchRequest();
	         StringBuffer lsbSearch = new StringBuffer();
	         lsbSearch.append(" ENTITY_ID ").append(AMSSQLUtil.getANSIQuotedStr(lsEntity, AMSSQLUtil.EQUALS_OPER));
	         lsbSearch.append(" AND RESOURCE_ID ").append( AMSSQLUtil.getANSIQuotedStr(fsResource, AMSSQLUtil.EQUALS_OPER));
	         lsbSearch.append(" AND TABLE_NAME ").append(AMSSQLUtil.getANSIQuotedStr(foRow.getComponentName(), AMSSQLUtil.EQUALS_OPER));
	         lsbSearch.append(" AND TO_DT IS NULL ");
	         lsrSearch.add(lsbSearch.toString());

	         //Get Historical Tracking record that stored the initial assignment
	         IN_HIST_TRACKImpl loHist = (IN_HIST_TRACKImpl) IN_HIST_TRACKImpl
	               .getObjectByKey(lsrSearch, loSession);

	         /* 
	          * While revocation, if no record exists with blank TO_DT, then it means that
	          * 1. The Tracking was not enabled at the time of Day Zero setup or 
	          * 2. Tracking was enabled at Day Zero but was disabled for sometime.
	          * Hence in this case, the Assignment happened at a time when Tracking was turned off.
	          * So now, insert a new record with blank FROM_DT and ASSIGN_ADMN_ID.
	          */
	         if(loHist == null)
	         {
	            loHist = IN_HIST_TRACKImpl.getNewObject(loSession, true);
	            loHist.setENTITY_ID(lsEntity);
	            loHist.setRESOURCE_ID(fsResource);
	            loHist.setTABLE_NAME(foRow.getComponentName());
	         }

	         //Update Historical Tracking record with revocation details
	         loHist.setREVOKE_ADMN_ID(foRow.getUser());
	         loHist.setTO_DT(foRow.getDate());
	         if(!AMSStringUtil.strIsEmpty(lsRevokeReason))
	         {
	            loHist.setREASON(lsRevokeReason);
	            loSession.removeProperty(SESSION_REVOKE_REASON);
	         }
	         loHist.save();

	      } /* End for(String lsEntity: getTrackedEntity()) */ 


	   }		
	
}

