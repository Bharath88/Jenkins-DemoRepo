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
**  R_SC_SEC_CNFG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_SEC_CNFGImpl extends  R_SC_SEC_CNFGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_SEC_CNFGImpl (){
		super();
	}
	
	public R_SC_SEC_CNFGImpl(Session session, boolean makeDefaults)
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
		public static R_SC_SEC_CNFGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_SEC_CNFGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

 /**
  * Calculates the number of required characters
  *
  * @return int number of required characters
  */
  public int getNumReqdChars()
  {
     int liMinChars = 0 ;

     /* If the password must contain a number */
     if ( getPSWD_NUM_FL() )
     {
        liMinChars ++ ;
     } /* end if ( getPSWD_NUM_FL() ) */

     /* If the password must contain a upper case character */
     if ( getPSWD_UPPER_FL() )
     {
        liMinChars ++ ;
     } /* end if ( getPSWD_UPPER_FL() ) */

     /* If the password must contain a lower case character */
     if ( getPSWD_LOWER_FL() )
     {
        liMinChars ++ ;
     } /* end if ( getPSWD_LOWER_FL() ) */

     /* If the password must contain a special character */
     if ( getPSWD_SYMBOL_FL() )
     {
        liMinChars ++ ;
     } /* end if ( getPSWD_SYMBOL_FL() ) */

     return liMinChars ;
  } /* end getNumReqdChars() */

}

