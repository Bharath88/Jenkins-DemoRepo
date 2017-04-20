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
**  CVL_Y_N_NC
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_Y_N_NCImpl extends  CVL_Y_N_NCBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	public static final int NO = 0;
	public static final int YES= 1;
	public static final int NO_CHANGE = 2;

	//{{COMP_CLASS_CTOR
	public CVL_Y_N_NCImpl (){
		super();
	}
	
	public CVL_Y_N_NCImpl(Session session, boolean makeDefaults)
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
		public static CVL_Y_N_NCImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_Y_N_NCImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

}

