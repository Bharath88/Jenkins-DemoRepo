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
**  CVL_PEND_OV_ALW*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_PEND_OV_ALWImpl extends  CVL_PEND_OV_ALWBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
        /**
         * Static constants for the three indicators
         */
        public static int ALW_AFT_REACHING     = 1;
        public static int REQ_BEF_REACHING     = 2;
        public static int ALW_BEF_AFT_REACHING = 3;
	//{{COMP_CLASS_CTOR
	public CVL_PEND_OV_ALWImpl (){
		super();
	}
	
	public CVL_PEND_OV_ALWImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_PEND_OV_ALWImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_PEND_OV_ALWImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
}
