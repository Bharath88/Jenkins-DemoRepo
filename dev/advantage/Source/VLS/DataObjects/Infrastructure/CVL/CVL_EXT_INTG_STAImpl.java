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
**  CVL_EXT_INTG_STA
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_EXT_INTG_STAImpl extends  CVL_EXT_INTG_STABaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   public static final int C_NOT_STARTED             = 0 ;
   public static final int C_EDITS_APPLIED           = 1 ;
   public static final int C_IN_PROGRESS             = 2 ;
   public static final int C_READY_FOR_COMPLETION    = 3 ;
   public static final int C_COMPLETED               = 4 ;
   public static final int C_READY_FOR_BACKOUT       = 5 ;
   public static final int C_BACKOUT_COMPLETED       = 6 ;
   public static final int C_RESOLUTION_REQUIRED     = 7 ;
   public static final int C_READY_FOR_BACKOUT_RETRY = 8 ;
   
	//{{COMP_CLASS_CTOR
	public CVL_EXT_INTG_STAImpl (){
		super();
	}
	
	public CVL_EXT_INTG_STAImpl(Session session, boolean makeDefaults)
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
		public static CVL_EXT_INTG_STAImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_EXT_INTG_STAImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

