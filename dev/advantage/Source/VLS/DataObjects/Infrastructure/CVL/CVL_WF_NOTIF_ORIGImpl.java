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
**  CVL_WF_NOTIF_ORIG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_WF_NOTIF_ORIGImpl extends  CVL_WF_NOTIF_ORIGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

   /** Vipin Sharma 8/23/2006 */    
   /** Choices for sending an e-mail notification */

   /** The minimum value that can be assigned to notifying options */
   public static final int NOTIFY_MIN = 1;

   /** The "notify none" option */
   public static final int NOTIFY_NONE = 1;

   /** The "notify submitter" option */
   public static final int NOTIFY_SUBMITTER = 2;

   /** The "notify creator" option */
   public static final int NOTIFY_CREATOR = 3;

   /** The "notify both" option */
   public static final int NOTIFY_BOTH = 4;

//{{COMP_CLASS_CTOR
public CVL_WF_NOTIF_ORIGImpl (){
	super();
}

public CVL_WF_NOTIF_ORIGImpl(Session session, boolean makeDefaults)
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
		public static CVL_WF_NOTIF_ORIGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_WF_NOTIF_ORIGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
}

