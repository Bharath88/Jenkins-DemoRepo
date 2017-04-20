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
**  STMT_RULE
*/

//{{COMPONENT_RULES_CLASS_DECL
public class STMT_RULEImpl extends  STMT_RULEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public STMT_RULEImpl (){
		super();
	}
	
	public STMT_RULEImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line
	
	commonBeforeLogic();
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	//Write Event Code below this line
	
	commonBeforeLogic();
}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static STMT_RULEImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new STMT_RULEImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
	public void commonBeforeLogic()
	{		
		// If Custom SQL and Statement Condition both are present, issue Informational Message.
		SearchRequest lsrSearch = new SearchRequest();
		lsrSearch.addParameter("STMT_CONDITION", "STMT_FY", String.valueOf(getSTMT_FY()) );
		lsrSearch.addParameter("STMT_CONDITION", "STMT_CD", getSTMT_CD() );
		lsrSearch.addParameter("STMT_CONDITION", "STMT_CELL", getSTMT_CELL() );
		lsrSearch.addParameter("STMT_CONDITION", "RULE_CD", getRULE_CD() );
		if( STMT_CONDITIONImpl.getObjectCount(lsrSearch, getSession()) > 0 && getCUSTOM_SQL() != null )
		{
			raiseException("%c:Q0182%");
		}

		/*
		 * On STMT_CELL table, there is a field named CELL_EXP which stores a mathematical
		 * expression involving Cell, Rule and Amount fields. This expression is parsed and for
		 * parsing distinction between the three is needed.
		 * A Cell value is identified by a ":", while Amount by a "$" or "-".
		 * Hence, Rule Code should not have these characters, else there would be difficulty
		 * in identification.
		 */
		if( getRULE_CD() != null && !"".equals(getRULE_CD()) && 
			(getRULE_CD().contains(":") || getRULE_CD().startsWith("$") || getRULE_CD().startsWith("-")) )
		{
			raiseException("%c:Q0178%");
		}
		
		if( AMSStringUtil.strIsEmpty(getSRC_TBL()) && !AMSStringUtil.strIsEmpty(getSRC_COL()))
		{
		   setSRC_TBL("BLNK");
		}
		
		// Update the Source table on the child Condition Column from the parent record.
		Enumeration<STMT_CONDITIONImpl> lenumCond = getStmtRuleToStmtCondition();
		while(lenumCond.hasMoreElements())
		{
		   STMT_CONDITIONImpl loCond = lenumCond.nextElement();
		   loCond.setSRC_TBL(getSRC_TBL());
		   loCond.setSRC_TBL_NM(getSRC_TBL_NM());
		   loCond.save();
		}
	}
}

