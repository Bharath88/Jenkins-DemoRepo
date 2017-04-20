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
**  CVL_WF_OPRS
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_WF_OPRSImpl extends  CVL_WF_OPRSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public CVL_WF_OPRSImpl (){
		super();
	}
	
	public CVL_WF_OPRSImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_WF_OPRSImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_WF_OPRSImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

   /** Rule Operators */
	public static final String RULE_OPERATOR []  =
                   {"", "<", ">", "=", "<>", "<=", ">=", "IL", "NI", "INULL", "NNULL"} ;

   /** The less than operator */
   public static final String LESS_THAN = RULE_OPERATOR[1] ;

   /** The greater than operator */
   public static final String GREATER_THAN = RULE_OPERATOR[2] ;

   /** The equals operator */
   public static final String EQUAL = RULE_OPERATOR[3] ;

   /** The not equals operator */
   public static final String NOT_EQUAL = RULE_OPERATOR[4] ;

   /** The less than or equal to operator */
   public static final String LESS_THAN_EQUAL = RULE_OPERATOR[5] ;

   /** The greater than or equal to operator */
   public static final String GREATER_THAN_EQUAL = RULE_OPERATOR[6] ;

   /** The in list operator */
   public static final String IN_LIST = RULE_OPERATOR[7] ;

   /** The not in list operator */
   public static final String NOT_IN_LIST = RULE_OPERATOR[8] ;
   
   /** The IS NULL Operator */
   public static final String IS_NULL = RULE_OPERATOR[9];
   
   /** The IS NOT NULL Operator */
   public static final String IS_NOT_NULL = RULE_OPERATOR[10];
}
