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
**  CVL_IM_COMP_NM
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_IM_COMP_NMImpl extends  CVL_IM_COMP_NMBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

//{{COMP_CLASS_CTOR
public CVL_IM_COMP_NMImpl (){
	super();
}

public CVL_IM_COMP_NMImpl(Session session, boolean makeDefaults)
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
	public static CVL_IM_COMP_NMImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_IM_COMP_NMImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

}

