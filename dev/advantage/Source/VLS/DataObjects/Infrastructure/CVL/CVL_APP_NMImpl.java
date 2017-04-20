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
**  CVL_APP_NM
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_APP_NMImpl extends  CVL_APP_NMBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//Constants for stored values
   public static int C_APPNM_ESS = 1;
   public static int C_APPNM_MSS = 2;
	//{{COMP_CLASS_CTOR
	public CVL_APP_NMImpl (){
		super();
	}
	
	public CVL_APP_NMImpl(Session session, boolean makeDefaults)
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
		public static CVL_APP_NMImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_APP_NMImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

