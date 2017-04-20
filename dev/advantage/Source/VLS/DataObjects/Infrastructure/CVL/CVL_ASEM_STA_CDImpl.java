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
**  CVL_ASEM_STA_CD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_ASEM_STA_CDImpl extends  CVL_ASEM_STA_CDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}


{
   public static final int SUBMITTED = 1;
   public static final int RUNNING = 2;
   public static final int FAILED = 3;
   public static final int SUCCESSFUL = 4;

	//{{COMP_CLASS_CTOR
	public CVL_ASEM_STA_CDImpl (){
		super();
	}
	
	public CVL_ASEM_STA_CDImpl(Session session, boolean makeDefaults)
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
		public static CVL_ASEM_STA_CDImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_ASEM_STA_CDImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

}

