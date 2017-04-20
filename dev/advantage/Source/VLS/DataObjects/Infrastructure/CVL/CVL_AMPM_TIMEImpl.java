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
**  CVL_AMPM_TIME
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_AMPM_TIMEImpl extends  CVL_AMPM_TIMEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_AMPM_TIMEImpl (){
		super();
	}
	
	public CVL_AMPM_TIMEImpl(Session session, boolean makeDefaults)
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
		public static CVL_AMPM_TIMEImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_AMPM_TIMEImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

