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
**  STMT_CONDITION
*/

//{{COMPONENT_RULES_CLASS_DECL
public class STMT_CONDITIONImpl extends  STMT_CONDITIONBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public STMT_CONDITIONImpl (){
		super();
	}
	
	public STMT_CONDITIONImpl(Session session, boolean makeDefaults)
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
		public static STMT_CONDITIONImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new STMT_CONDITIONImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	public String getConditionExpression()
	{
		return getCOND_OPRTR() + " " + getCOND_VAL();
	}
	
	public void commonBeforeLogic()
	{
	   // Fetch the values of Source Table from the parent.
	   setSRC_TBL(getStmtConditionToStmtRule().getSRC_TBL());
	   setSRC_TBL_NM(getStmtConditionToStmtRule().getSRC_TBL_NM());
	   
	   // If the Condition Column on STMT_SRC_COL table has flag as true then it cannot be used.
	   if(getStmtConditionToStmtSrcCol() != null && getStmtConditionToStmtSrcCol().getSRC_COL_FL() )
	   {
	      raiseException("%c:A761,v:Condition Column,v:Condition Column Table%");
	   }
	}
}

