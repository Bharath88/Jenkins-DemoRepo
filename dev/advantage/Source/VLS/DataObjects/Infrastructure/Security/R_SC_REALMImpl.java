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
 **  R_SC_REALM
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_REALMImpl extends  R_SC_REALMBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_REALMImpl (){
		super();
	}
	
	public R_SC_REALMImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}
		
	}
	public static final String R_SC_REALM = "R_SC_REALM";
	
	//{{EVENT_CODE
	
	//END_EVENT_CODE}}
	
	
	
	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
	//{{COMPONENT_RULES
		public static R_SC_REALMImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_REALMImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
	/**
	 * If a new or an updated record will now be the default record, 
	 * then existing default record needs to be made a regular record. 
	 * An informational message is also displayed.
	 *
	 * @return void
	 */
	protected void makeExistingDefaultRegular()
	{
		R_SC_REALMImpl loRScRealmRec = null;
		StringBuffer loWhereClause = new StringBuffer(64);
		if(getDFLT_FL())
		{
			loWhereClause.append(" ACTN_ID <> ")
				.append(getACTN_ID())
				.append(" AND DFLT_FL = 1");
			loRScRealmRec = getRealmRec(loWhereClause, getSession());
			if (loRScRealmRec != null)
			{
				loRScRealmRec.setDFLT_FL(false);
				loRScRealmRec.save();
			
				raiseException("%c:A4021,v:Realm =" + loRScRealmRec.getREALM_ID() + ", Action = ,v:" +loRScRealmRec.getACTN_ID() + "%");
	
			}
		}
	}//End of makeExistingDefaultRegular
	
	/**
	 * This method returns the realm ID for a given action area ID. 
	 * If the action area is not defined, then this method returns null.
	 *
	 * @param fiActnAreaId - The supplied action area ID.
	 * @param foSession - The current session.
	 * @return String - Returns the realm ID for a given action area ID
	 */
	public static String findRealmId(int fiActnAreaId, Session foSession)
	{
		R_SC_REALMImpl loRScRealmRec = null;
		StringBuffer loWhereClause = new StringBuffer(64);
		loWhereClause.append(" ACTN_ID = ").append(fiActnAreaId);
		loRScRealmRec = getRealmRec(loWhereClause, foSession);
		if (loRScRealmRec != null)
		{
			return loRScRealmRec.getREALM_ID();
		}
		return null;
	}//End of findRealmId
	
	/**
	 * This method returns the realm ID for the default R_SC_REALM record.
	 * The only way that there is no default record is if the table's 
	 * records are modified outside of Versata; then a null value is returned. 
	 * Otherwise, a realm ID value should always be returned
	 *
	 * @param foSession - The current session.
	 * @return String - Returns the realm ID for the default R_SC_REALM record
	 */
	public static String findDefaultRealmId(Session foSession)
	{
		R_SC_REALMImpl loRScRealmRec = null;
		StringBuffer loWhereClause = new StringBuffer(64);
		loWhereClause.append(" DFLT_FL = 1");
		loRScRealmRec = getRealmRec(loWhereClause, foSession);
		if(loRScRealmRec != null)
		{
			return loRScRealmRec.getREALM_ID();
		}
		return null;
	}//End of findDefaultRealmId
	
	/**
	 * This method is used to check default Realm Id is existed except the
	 * current Realm Id record. If yes then returns true otherwise return false. It
	 * used to make sure that R_SC_REALM table has at least one default record.
	 *
	 * @return boolean - Returns true if default Realm Id is exist other than
	 * 										cureent Realm Id.
	 */
	protected boolean isDefaultRealmIdExist()
	{
		StringBuffer loWhereClause = new StringBuffer(64);
		loWhereClause.append(" ACTN_ID <> ").append(getACTN_ID())
			.append(" AND DFLT_FL = 1");
		System.err.println("loWhereClause "+loWhereClause.toString());
		if(getRealmRec(loWhereClause, getSession()) != null )
		{
			return true;
		}
		return false;
	}//End of isDefaultRealmIdExist
	
	/**
	 * This method returns the R_SC_REALM record based on foWhereClause
	 * passed as paramter.
	 *
	 * @param foWhereClause - WhereClause to select record from R_SC_REALM table
	 * @param foSession - The current session.
	 * @return String - Returns the realm ID for the default R_SC_REALM record
	 */
	public static R_SC_REALMImpl getRealmRec(StringBuffer foWhereClause, Session foSession)
	{
		SearchRequest loRScRealmSR = new SearchRequest();
		loRScRealmSR.add(foWhereClause.toString());
		Enumeration loEnumRealm = R_SC_REALMImpl.getObjects(loRScRealmSR, foSession);
		if(loEnumRealm.hasMoreElements())
		{
			return (R_SC_REALMImpl)loEnumRealm.nextElement();
		}
		return null;
	}//End of getRealmRec
}

