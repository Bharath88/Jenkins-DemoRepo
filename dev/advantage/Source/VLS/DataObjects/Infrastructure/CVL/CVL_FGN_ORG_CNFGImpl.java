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
**  CVL_FGN_ORG_CNFG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_FGN_ORG_CNFGImpl extends  CVL_FGN_ORG_CNFGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
    public static final int NONE=1;
	public static final int USER_ID_REQUIRED=2;
	public static final int HOME_DEPT_REQUIRED=3;
    public static final int USERID_OR_HOMEDEPT_REQUIRED=4;
	//{{COMP_CLASS_CTOR
	public CVL_FGN_ORG_CNFGImpl (){
		super();
	}
	
	public CVL_FGN_ORG_CNFGImpl(Session session, boolean makeDefaults)
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
		public static CVL_FGN_ORG_CNFGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_FGN_ORG_CNFGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

}

