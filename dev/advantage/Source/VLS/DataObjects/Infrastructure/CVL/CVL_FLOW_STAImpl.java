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
**  CVL_FLOW_STA
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_FLOW_STAImpl extends  CVL_FLOW_STABaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	public static final int InActive = 1;
	public static final int Active = 2;
	public static final int Pending  = 3;
	public static final int Running  = 4;
	public static final int Complete = 5;
	
	//{{COMP_CLASS_CTOR
	public CVL_FLOW_STAImpl (){
		super();
	}
	
	public CVL_FLOW_STAImpl(Session session, boolean makeDefaults)
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
		public static CVL_FLOW_STAImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_FLOW_STAImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

