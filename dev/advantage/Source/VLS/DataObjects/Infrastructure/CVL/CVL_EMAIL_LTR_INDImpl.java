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
**  CVL_EMAIL_LTR_IND
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_EMAIL_LTR_INDImpl extends  CVL_EMAIL_LTR_INDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public CVL_EMAIL_LTR_INDImpl (){
	super();
}

public CVL_EMAIL_LTR_INDImpl(Session session, boolean makeDefaults)
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
	public static CVL_EMAIL_LTR_INDImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_EMAIL_LTR_INDImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   
   public static final int EMAIL = 1;
   public static final int LETTER = 2;
}

