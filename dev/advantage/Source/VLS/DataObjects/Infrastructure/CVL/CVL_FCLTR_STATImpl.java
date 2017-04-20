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
 **  CVL_FCLTR_STAT
 */

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_FCLTR_STATImpl extends  CVL_FCLTR_STATBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
    
    public static final int DOC_READY_ARCH = 0;
    public static final int DOC_ARCH = 1;
    public static final int DOC_ARCH_HIST = 2;
    public static final int DOC_UNARCH = 8;
    public static final int DOC_ARCH_COMP = 9;
    public static final int TBL_READY_ARCH = 10;
    public static final int TBL_ARCH = 11;
    public static final int TBL_REST = 18;
    public static final int TBL_ARCH_COMP = 19;
    public static final int DOC_READY_PRINT = 20;
    public static final int DOC_PRINTING = 21;
    public static final int DOC_PRINT_COMP = 22;
    
    
	//{{COMP_CLASS_CTOR
	public CVL_FCLTR_STATImpl (){
		super();
	}
	
	public CVL_FCLTR_STATImpl(Session session, boolean makeDefaults)
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
		public static CVL_FCLTR_STATImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_FCLTR_STATImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
    
}

