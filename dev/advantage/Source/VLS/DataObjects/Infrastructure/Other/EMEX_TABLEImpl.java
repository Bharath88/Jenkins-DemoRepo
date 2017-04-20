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
**  EMEX_TABLE*/

//{{COMPONENT_RULES_CLASS_DECL
public class EMEX_TABLEImpl extends  EMEX_TABLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public EMEX_TABLEImpl (){
		super();
	}
	
	public EMEX_TABLEImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
	//Write Event Code below this line
}
//END_COMP_EVENT_afterInsert}}

//{{COMP_EVENT_afterUpdate
public void afterUpdate(DataObject obj)
{
	//Write Event Code below this line
}
//END_COMP_EVENT_afterUpdate}}

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	

}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static EMEX_TABLEImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new EMEX_TABLEImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

