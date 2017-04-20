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
**  CVL_JOB_Q_DEF
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_JOB_Q_DEFImpl extends  CVL_JOB_Q_DEFBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_JOB_Q_DEFImpl (){
		super();
	}
	
	public CVL_JOB_Q_DEFImpl(Session session, boolean makeDefaults)
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
		public static CVL_JOB_Q_DEFImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_JOB_Q_DEFImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

