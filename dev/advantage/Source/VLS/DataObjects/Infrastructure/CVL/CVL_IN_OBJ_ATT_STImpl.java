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
**  CVL_IN_OBJ_ATT_ST*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_IN_OBJ_ATT_STImpl extends  CVL_IN_OBJ_ATT_STBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

//{{COMP_CLASS_CTOR
public CVL_IN_OBJ_ATT_STImpl (){
	super();
}

public CVL_IN_OBJ_ATT_STImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static CVL_IN_OBJ_ATT_STImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_IN_OBJ_ATT_STImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	//END_EVENT_ADD_LISTENERS}}
   }

}
