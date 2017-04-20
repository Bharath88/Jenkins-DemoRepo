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
 * CVL_FLOW_LOG_TYP
 */

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_FLOW_LOG_TYPImpl extends  CVL_FLOW_LOG_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /**
    * Constant for the "Text" Stored Value.
    */
   public static final int TEXT = 1;

   /**
    * Constant for the "File" Stored Value.
    */
   public static final int FILE = 2;

   /**
    * Constant for the "Date" Stored Value.
    */
   public static final int DATE = 3;

   /**
    * Constant for the "Status" Stored Value.
    */
   public static final int STATUS = 4;

   /**
    * Constant for the "Report" Stored Value.
    */
   public static final int REPORT = 5;

   /**
    * Constant for the "Date/Time" Stored Value.
    */
   public static final int DATE_TIME = 6;

   /**
    * Constant for the "Batch Process" Stored Value.
    */
   public static final int CHILD_JOB_ID = 7;

   /**
    * Constant for the "Performance Trace" Stored Value.
    */
   public static final int PERF_TRACE = 8;

   /**
    * Constant for the "Child Flow" Stored Value.
    */
   public static final int CHILD_FLOW = 9;

   /**
    * Constant for the "Input Resource" Stored Value.
    */
   public static final int RESOURCE = 10;

   /**
    * Constant for the "Error" Stored Value.
    */
   public static final int ERROR_MSG = 11;


//{{COMP_CLASS_CTOR
public CVL_FLOW_LOG_TYPImpl (){
	super();
}

public CVL_FLOW_LOG_TYPImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static CVL_FLOW_LOG_TYPImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_FLOW_LOG_TYPImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

}
