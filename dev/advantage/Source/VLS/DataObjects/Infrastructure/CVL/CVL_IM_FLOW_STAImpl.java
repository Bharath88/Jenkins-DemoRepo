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
**  CVL_IM_FLOW_STA
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_IM_FLOW_STAImpl extends  CVL_IM_FLOW_STABaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	public static final int Scheduled = 1;
	public static final int ReadyToRun = 2;
	public static final int Running  = 3;
	public static final int Completed  = 4;
	public static final int Failed = 5;
	public static final int Suspended  = 4;
	public static final int Waiting = 5;
	public static final int Skipped  = 4;
	public static final int Killed = 5;

	//{{COMP_CLASS_CTOR
	public CVL_IM_FLOW_STAImpl (){
		super();
	}
	
	public CVL_IM_FLOW_STAImpl(Session session, boolean makeDefaults)
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
		public static CVL_IM_FLOW_STAImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_IM_FLOW_STAImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

