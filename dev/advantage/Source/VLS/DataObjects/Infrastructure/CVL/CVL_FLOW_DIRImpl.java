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
**  CVL_FLOW_DIR
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_FLOW_DIRImpl extends  CVL_FLOW_DIRBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_FLOW_DIRImpl (){
		super();
	}
	
	public CVL_FLOW_DIRImpl(Session session, boolean makeDefaults)
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
		public static CVL_FLOW_DIRImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_FLOW_DIRImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

