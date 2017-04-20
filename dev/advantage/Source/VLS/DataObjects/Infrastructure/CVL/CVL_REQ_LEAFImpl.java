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
**  CVL_REQ_LEAF
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_REQ_LEAFImpl extends  CVL_REQ_LEAFBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	public static final int NOT_USED = 0;
	public static final int REQUIRED = 1;
	public static final int OPTIONAL = 2;
	//{{COMP_CLASS_CTOR
	public CVL_REQ_LEAFImpl (){
		super();
	}
	
	public CVL_REQ_LEAFImpl(Session session, boolean makeDefaults)
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
		public static CVL_REQ_LEAFImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_REQ_LEAFImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

