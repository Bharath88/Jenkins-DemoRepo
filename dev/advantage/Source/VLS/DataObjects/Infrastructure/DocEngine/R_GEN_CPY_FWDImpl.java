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
**  R_GEN_CPY_FWD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_GEN_CPY_FWDImpl extends  R_GEN_CPY_FWDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_GEN_CPY_FWDImpl (){
		super();
	}
	
	public R_GEN_CPY_FWDImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_GEN_CPY_FWDImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_GEN_CPY_FWDImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

	public void myInitializations()
	{
		setPreventChangeOfRecordKey(true);
	}

}
