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
**  CVL_ARCH_TYP
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_ARCH_TYPImpl extends  CVL_ARCH_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
    public static final int CHECK = 1;
    public static final int LEAVE = 2;
    public static final int DOCUMENT = 3;
    public static final int JOB_MANAGER = 4;
    public static final int MATCH_STA_TBL = 5;
    public static final int JRNL_LDGR     = 6;
    public static final int CHECK_WRITER = 7;
    public static final int EFT_ACH_ARCHIVE = 8;
    public static final int BUDGET = 9;
    public static final int PAID_CHECK = 10;
    public static final int EMPLOYEE = 11;


	//{{COMP_CLASS_CTOR
	public CVL_ARCH_TYPImpl (){
		super();
	}
	
	public CVL_ARCH_TYPImpl(Session session, boolean makeDefaults)
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
		public static CVL_ARCH_TYPImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_ARCH_TYPImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

}

