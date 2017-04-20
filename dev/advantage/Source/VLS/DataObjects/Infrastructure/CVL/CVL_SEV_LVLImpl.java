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
**  CVL_SEV_LVL*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_SEV_LVLImpl extends  CVL_SEV_LVLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	public static final int    SEVERITY_LEVEL_INFO         =  0;
  	public static final int    SEVERITY_LEVEL_WARNING      =  1;
	public static final int    SEVERITY_LEVEL_ERROR        =  2;
	public static final int    SEVERITY_LEVEL_SEVERE       =  3;
	//{{COMP_CLASS_CTOR
	public CVL_SEV_LVLImpl (){
		super();
	}
	
	public CVL_SEV_LVLImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_SEV_LVLImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_SEV_LVLImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
}
