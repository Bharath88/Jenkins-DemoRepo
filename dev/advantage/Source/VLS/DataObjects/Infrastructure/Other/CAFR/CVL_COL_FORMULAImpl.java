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
**  CVL_COL_FORMULA
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_COL_FORMULAImpl extends  CVL_COL_FORMULABaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_COL_FORMULAImpl (){
		super();
	}
	
	public CVL_COL_FORMULAImpl(Session session, boolean makeDefaults)
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
		public static CVL_COL_FORMULAImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_COL_FORMULAImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

