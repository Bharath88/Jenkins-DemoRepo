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
**  R_SC_FLD_ACCS
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_FLD_ACCSImpl extends  R_SC_FLD_ACCSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_FLD_ACCSImpl (){
		super();
	}
	
	public R_SC_FLD_ACCSImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_SC_FLD_ACCSImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_FLD_ACCSImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
}
