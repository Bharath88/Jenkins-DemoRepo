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
**  CVL_SC_PSWD_HINT
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_SC_PSWD_HINTImpl extends  CVL_SC_PSWD_HINTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_SC_PSWD_HINTImpl (){
		super();
	}
	
	public CVL_SC_PSWD_HINTImpl(Session session, boolean makeDefaults)
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
		public static CVL_SC_PSWD_HINTImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_SC_PSWD_HINTImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

