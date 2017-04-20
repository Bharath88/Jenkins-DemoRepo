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
**  CVL_USER_LOCK_STA
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_USER_LOCK_STAImpl extends  CVL_USER_LOCK_STABaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   // Constants for CVL_USER_LOCK_STA Stored Value
   public static final int ACTIVE   = 0;
   public static final int DISABLED = 1;
   public static final int LOCKED   = 2;

//{{COMP_CLASS_CTOR
public CVL_USER_LOCK_STAImpl (){
	super();
}

public CVL_USER_LOCK_STAImpl(Session session, boolean makeDefaults)
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
	public static CVL_USER_LOCK_STAImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_USER_LOCK_STAImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

}

