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
**  CVL_WF_REAS
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_WF_REASImpl extends  CVL_WF_REASBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_WF_REASImpl (){
		super();
	}
	
	public CVL_WF_REASImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_WF_REASImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_WF_REASImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
//ssb
     /*
      * Routing Reason Codes
      */
	public static final byte REASON_RJCT_UNAPRV_APRVL  = 0;
	public static final byte REASON_APPLY_APRVL        = 1;
	public static final byte REASON_CNFRM_REMOVE_APRVL = 2;
//ssb end
	
}
