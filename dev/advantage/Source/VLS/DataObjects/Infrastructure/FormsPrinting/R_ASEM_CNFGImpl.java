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
**  R_ASEM_CNFG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_ASEM_CNFGImpl extends  R_ASEM_CNFGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   private static Log moLog = null;

	//{{COMP_CLASS_CTOR
	public R_ASEM_CNFGImpl (){
		super();
	}
	
	public R_ASEM_CNFGImpl(Session session, boolean makeDefaults)
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
		public static R_ASEM_CNFGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_ASEM_CNFGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}


   public void myInitializations()
   {
      moLog = AMSLogger.getLog(R_ASEM_CNFGImpl.class,
         AMSLogConstants.FUNC_AREA_FORMS_ASSEMBLY);
   }
}

