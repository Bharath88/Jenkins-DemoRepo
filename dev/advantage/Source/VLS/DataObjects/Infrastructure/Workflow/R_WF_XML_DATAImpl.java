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
**  R_WF_XML_DATA
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_XML_DATAImpl extends  R_WF_XML_DATABaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_WF_XML_DATAImpl (){
		super();
	}
	
	public R_WF_XML_DATAImpl(Session session, boolean makeDefaults)
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
		public static R_WF_XML_DATAImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_WF_XML_DATAImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

