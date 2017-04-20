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
**  STMT_XWLK_DEF
*/

//{{COMPONENT_RULES_CLASS_DECL
public class STMT_XWLK_DEFImpl extends  STMT_XWLK_DEFBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public STMT_XWLK_DEFImpl (){
		super();
	}
	
	public STMT_XWLK_DEFImpl(Session session, boolean makeDefaults)
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
		public static STMT_XWLK_DEFImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new STMT_XWLK_DEFImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

