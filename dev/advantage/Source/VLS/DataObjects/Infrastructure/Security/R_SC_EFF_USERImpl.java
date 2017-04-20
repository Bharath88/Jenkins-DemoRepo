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

import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSUser;

/*
 **  R_SC_EFF_USER*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_EFF_USERImpl extends  R_SC_EFF_USERBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	private static final byte DEPT_CODE_POSITION = 2;       // 3rd element
	//{{COMP_CLASS_CTOR
	public R_SC_EFF_USERImpl (){
		super();
	}
	
	public R_SC_EFF_USERImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}
		
	}
	
	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
	public void beforeInsert(DataObject obj, Response response)
	{
		
		String lsUserID = obj.getData("USER_ID").getString();
		String lsUserRole = obj.getData("ROLE_ID").getString();
		
		/*
		 * Call methods for validation of data - 
		 * Only a user role or user id should have been entered
		 */
		
		
		if (! validUserOrRole(lsUserID,lsUserRole ) )
		{
			raiseException("A valid role OR user id. required","R_SC_EFF_USER","");
			return;
		}
		
		if ( !validEffectiveUser(obj) )
		{
			raiseException("An effective user id is required","R_SC_EFF_USER","");
			return;
		}
		
		/*
		 * If user role has been provided, check correspnding flag
		 * also update value of user role
		 *
		 */
		if (lsUserID ==null || lsUserID .trim().length()==0 )
		{
			// user id is not provided
			obj.getData("USER_ROLE_FL").setboolean(true);
			obj.getData("USER_ROLE").setString(lsUserRole.trim() );   
		}
		else
		{
			// user id is provided
			obj.getData("USER_ROLE_FL").setboolean(false);
			obj.getData("USER_ROLE").setString(lsUserID.trim() );	
		}
		commonBeforeLogic();
		
		/*
		 * update the department code
		 */
		updateDepartmentCode(obj);
	}
	
	
	
	
	
	
	
	
	
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
	public void beforeUpdate(DataObject obj, Response response)
	{
		
		String lsUserID = obj.getData("USER_ID").getString();
		String lsUserRole = obj.getData("ROLE_ID").getString();
		
		/*
		 * Call methods for validation of data - 
		 * Only a user role or user id should have been entered
		 */
		
		
		if (! validUserOrRole(lsUserID,lsUserRole ) )
		{
			raiseException("A valid role OR user id. required","R_SC_EFF_USER","");
			return;
		}
		
		
		if ( !validEffectiveUser(obj) )
		{
			raiseException("An effective user id is required","R_SC_EFF_USER","");
			return;
		}
		
		/*
		 * If user role has been provided, check correspnding flag
		 * also update value of user role
		 *
		 */
		if (lsUserID ==null || lsUserID .trim().length()==0 )
		{
			// user id is not provided
			obj.getData("USER_ROLE_FL").setboolean(true);
			obj.getData("USER_ROLE").setString(lsUserRole.trim() );   
		}
		else
		{
			// user id is provided
			obj.getData("USER_ROLE_FL").setboolean(false);
			obj.getData("USER_ROLE").setString(lsUserID.trim() );	
		}
		commonBeforeLogic();
		/*
		 * At this stage we have the user id or role Id, we must update the 
		 * department code
		 */
		updateDepartmentCode(obj);
		
	}
	
	
	
	
	
	
	
	
	
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}
	
	
	
	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}
	
	//{{COMPONENT_RULES
		public static R_SC_EFF_USERImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_EFF_USERImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
	/**
	 * This methods validates that only one of the two, user id or user
	 * role is specified
	 */
	private boolean validUserOrRole(String fsUserID, String fsUserRole )	
	{
		boolean lboolUserNullOrEmpty = (fsUserID==null || 
			fsUserID.trim().length()==0);
		
		boolean lboolUserRoleNullOrEmpty = (fsUserRole ==null ||
			fsUserRole .trim().length()==0);
		
		// return false only if both null or both have value
		if (lboolUserNullOrEmpty && lboolUserRoleNullOrEmpty )
		{
			return false;
		}
		else if (!lboolUserNullOrEmpty && !lboolUserRoleNullOrEmpty )
		{
			return false;
		}
		return true;
	} // validUserOrRole
	
	/**
	 * This method returns if an efective user is needed.  An effective
	 * user id. is not required only when the allow delegation value is 
	 * is true
	 */
	private static boolean validEffectiveUser(DataObject foObject )
	{
		boolean lboolAlwDlgtnEmpty = foObject.isNull("ALW_DLGTN")   ||
			( foObject.getData("ALW_DLGTN").getboolean()==false) ; 
		
		String lsEffUser = foObject.getData("EFF_USER_ID").getString();
		boolean lboolEffUserEmpty = (lsEffUser ==null ||
			lsEffUser.trim().length()==0); 
		
		// no effective user id ok if allow delegation true -   
		if (lboolEffUserEmpty && lboolAlwDlgtnEmpty )
		{
			return false;
		}
		
		return true;
	} //validEffectiveUser
	
	/**
	 * This method updates the department code for the row.  If the user id
	 * has been entered, the users home department code is entered.  If the 
	 * role is entered, the department code of the user logged in is taken
	 */
	private void updateDepartmentCode(DataObject obj)
	{
		boolean lboolRoleIdProvided = obj.getData("USER_ROLE_FL").getboolean();
		String  lsUserId = null;
		
		if (lboolRoleIdProvided )
		{
			//role id provided - use department code of current user
			lsUserId  = getSession().getUserID();                         
		}
		else
		{
			//user Id provided
			lsUserId = obj.getData("USER_ID").getString();	         
		}
		
		AMSUser loAMSUser = AMSSecurity.getUser( lsUserId ) ;
		
		String  msDeptCode = loAMSUser.getHomeOrgCodeLevels()[DEPT_CODE_POSITION] ;
		obj.getData("DEPT_CD").setString(msDeptCode );	
	} //updateDepartmentCode
	
	/**
	 * Method for performing common beforeUpdate/beforeInsert logic.
	 *
	 * @return void.
	 */
	private void commonBeforeLogic()
	{
		/**
		 * Set defalut REALM_ID infer from R_SC_REALM table if
		 * REALM_ID is not enterd.
		 */
		if(AMSStringUtil.strIsEmpty(getREALM_ID()))
		{
			setREALM_ID(R_SC_REALMImpl.findDefaultRealmId(getSession()));
		}
		//Validate the Realm ID
		if(!isValidRealmId())
		{
			raiseException("%c:A4020%");
		}
	}
	
	/**
	 * This method validates the enterd Realm ID is exist on the R_SC_REALM table.
	 * If it is exist method returns true otherwisw return false. 
	 *
	 * @return boolean - Returns the true is if Realm ID is exist on the R_SC_REALM table.
	 */
	private boolean isValidRealmId()
	{
		if(!AMSStringUtil.strIsEmpty(getREALM_ID()))
		{
			StringBuffer loWhereClause = new StringBuffer(64);
			loWhereClause.append(" REALM_ID ")
				.append(AMSSQLUtil.getANSIQuotedStr(getREALM_ID(), AMSSQLUtil.EQUALS_OPER));
			if (R_SC_REALMImpl.getRealmRec(loWhereClause, getSession()) != null)
			{
				return true;
			}
		}
		return false;
	}//End of isValidRealmId
}

