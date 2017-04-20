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
**  CVL_TIMEFRAME
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_TIMEFRAMEImpl extends  CVL_TIMEFRAMEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
    // constants for stored values
    public static final String C_SV_PAST            = "P";
    public static final String C_SV_CURRENT         = "C";
    public static final String C_SV_FUTURE          = "F";
    public static final String C_SV_EXPIRE          = "E";
    public static final String C_SV_NO_TIMEFRAME    = "N";
    
	//{{COMP_CLASS_CTOR
	public CVL_TIMEFRAMEImpl (){
		super();
	}
	
	public CVL_TIMEFRAMEImpl(Session session, boolean makeDefaults)
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
		public static CVL_TIMEFRAMEImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_TIMEFRAMEImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

