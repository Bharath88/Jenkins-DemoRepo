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
**  CVL_OV_ERR_CD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_OVERRIDE_LVLImpl extends  CVL_OVERRIDE_LVLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	
	//Constants for OVERRIDE LVL Stored Value
	public static final int OVR_LVL_ZERO 	= 0;
	public static final int OVR_LVL_ONE	 	= 1;
	public static final int OVR_LVL_TWO	 	= 2;
	public static final int OVR_LVL_THREE = 3;
	public static final int OVR_LVL_FOUR 	= 4;
	public static final int OVR_LVL_FIVE 	= 5;
	public static final int OVR_LVL_SIX	 	= 6;
	public static final int OVR_LVL_SEVEN = 7;
	public static final int OVR_LVL_EIGHT = 8;
	public static final int OVR_LVL_NINE  = 9;
	public static final int OVR_LVL_TEN   = 10;
	
	public static final int OVR_LVL_LOWER_LIMIT   = 0;
	public static final int OVR_LVL_UPPER_LIMIT   = 10;
	//{{COMP_CLASS_CTOR
	public CVL_OVERRIDE_LVLImpl (){
		super();
	}
	
	public CVL_OVERRIDE_LVLImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_OVERRIDE_LVLImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_OVERRIDE_LVLImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
}
