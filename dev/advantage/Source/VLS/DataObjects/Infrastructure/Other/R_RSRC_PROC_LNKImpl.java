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
**  R_RSRC_PROC_LNK*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_RSRC_PROC_LNKImpl extends  R_RSRC_PROC_LNKBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_RSRC_PROC_LNKImpl (){
		super();
	}
	
	public R_RSRC_PROC_LNKImpl(Session session, boolean makeDefaults)
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
		public static R_RSRC_PROC_LNKImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_RSRC_PROC_LNKImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

