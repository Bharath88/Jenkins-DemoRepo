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
**  CVL_DOC_SIG_TYP
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_DOC_SIG_TYPImpl extends  CVL_DOC_SIG_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   
   public static final int DOC_SIG_TYP_NONE        = 0;
   public static final int DOC_SIG_TYP_DIGITAL     = 1;
   public static final int DOC_SIG_TYP_NON_DIGITAL = 2;
   
	//{{COMP_CLASS_CTOR
	public CVL_DOC_SIG_TYPImpl (){
		super();
	}
	
	public CVL_DOC_SIG_TYPImpl(Session session, boolean makeDefaults)
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
		public static CVL_DOC_SIG_TYPImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_DOC_SIG_TYPImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

