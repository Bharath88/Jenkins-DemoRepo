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
**  CVL_FLOW_PARM_TYP
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_FLOW_PARM_TYPImpl extends  CVL_FLOW_PARM_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   public static final int TEXT     = 1;
   public static final int DATE     = 2;
   public static final int NUMERIC  = 3;
   public static final int CURRENCY = 4;
   public static final int YES_NO   = 5;

//{{COMP_CLASS_CTOR
public CVL_FLOW_PARM_TYPImpl (){
	super();
}

public CVL_FLOW_PARM_TYPImpl(Session session, boolean makeDefaults)
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
	public static CVL_FLOW_PARM_TYPImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_FLOW_PARM_TYPImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

}

