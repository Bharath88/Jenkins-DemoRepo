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
**  CVL_JOB_STATUS_2
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_JOB_STATUS_2Impl extends  CVL_JOB_STATUS_2BaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

   public static final int READY = 4;
   public static final int RUNNING = 5;
   public static final int COMPLETE = 3;
   
//{{COMP_CLASS_CTOR
public CVL_JOB_STATUS_2Impl (){
	super();
}

public CVL_JOB_STATUS_2Impl(Session session, boolean makeDefaults)
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
	public static CVL_JOB_STATUS_2Impl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_JOB_STATUS_2Impl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   
}

