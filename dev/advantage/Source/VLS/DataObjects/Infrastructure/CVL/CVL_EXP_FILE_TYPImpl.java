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
**  CVL_EXP_FILE_TYP
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_EXP_FILE_TYPImpl extends  CVL_EXP_FILE_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   public static final int XML = 1;
   public static final int CSV = 2;
//{{COMP_CLASS_CTOR
public CVL_EXP_FILE_TYPImpl (){
	super();
}

public CVL_EXP_FILE_TYPImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static CVL_EXP_FILE_TYPImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_EXP_FILE_TYPImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	//END_EVENT_ADD_LISTENERS}}
   }
   }
