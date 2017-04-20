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
**  CVL_RSRC_TYP_IND
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_RSRC_TYP_INDImpl extends  CVL_RSRC_TYP_INDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	public static final String DOCUMENT = "D";
   public static final String PAGE = "P";
	public static final String QUERY = "Q";
	public static final String REFERENCE_TABLE = "T";
	public static final String TAB = "A";	
   public static final String JOB = "J";
	
	//{{COMP_CLASS_CTOR
	public CVL_RSRC_TYP_INDImpl (){
		super();
	}
	
	public CVL_RSRC_TYP_INDImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_RSRC_TYP_INDImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_RSRC_TYP_INDImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
}
