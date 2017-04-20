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
**  R_EXT_EMAIL_LTRCNF
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_EXT_EMAIL_LTRCNFImpl extends  R_EXT_EMAIL_LTRCNFBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_EXT_EMAIL_LTRCNFImpl (){
		super();
	}
	
	public R_EXT_EMAIL_LTRCNFImpl(Session session, boolean makeDefaults)
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
		public static R_EXT_EMAIL_LTRCNFImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_EXT_EMAIL_LTRCNFImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

