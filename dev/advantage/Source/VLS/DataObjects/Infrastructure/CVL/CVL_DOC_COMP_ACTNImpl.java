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
**  CVL_COMPONENT_ACTION*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_DOC_COMP_ACTNImpl extends  CVL_DOC_COMP_ACTNBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /** This is the add action stored value */
   public static final int ADD    = 0 ;
   /** This is the update action stored value */
   public static final int UPDATE = 1 ;
   /** This is the delete action stored value */
   public static final int DELETE = 2 ;

//{{COMP_CLASS_CTOR
public CVL_DOC_COMP_ACTNImpl (){
	super();
}

public CVL_DOC_COMP_ACTNImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static CVL_DOC_COMP_ACTNImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_DOC_COMP_ACTNImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

}
