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
**  CVL_WF_PRIORITY
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_WF_PRIORITYImpl extends  CVL_WF_PRIORITYBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	

	// Constants for CVL_WF_PRIORITY Stored Value.
	
	/** Constant representing Priority value of Low. */
	public static final int VERY_LOW = 1;
	
	/** Constant representing Priority value of Very Low. */
	public static final int LOW = 2;
	
	/** Constant representing Priority value of Normal. */
	public static final int NORMAL = 3;
	
	/** Constant representing Priority value of High. */
	public static final int HIGH = 4;
	
	/** Constant representing Priority value of Very High. */
	public static final int VERY_HIGH = 5;
	
	/** Constant representing Priority value of Urgent. */
	public static final int URGENT = 6;
	
	
	//{{COMP_CLASS_CTOR
	public CVL_WF_PRIORITYImpl (){
		super();
	}
	
	public CVL_WF_PRIORITYImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	/**
    * Returns the Display value from the CVL_WF_PRIORITY table for given Stored value.
    * @param fiPrioritySV Stored value of Priority
    * @return Display value
    */
   public static String getPriorityLabel( Session foSession, int fiPrioritySV )
   {
      SearchRequest lsr = new SearchRequest();
      lsr.addParameter("CVL_WF_PRIORITY", "CVL_WF_PRIORITY_SV", String.valueOf(fiPrioritySV) );
      CVL_WF_PRIORITYImpl loCVL = (CVL_WF_PRIORITYImpl)CVL_WF_PRIORITYImpl.getObjectByKey( lsr, foSession );
      return loCVL.getCVL_WF_PRIORITY_DV();
   }

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static CVL_WF_PRIORITYImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_WF_PRIORITYImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
}

