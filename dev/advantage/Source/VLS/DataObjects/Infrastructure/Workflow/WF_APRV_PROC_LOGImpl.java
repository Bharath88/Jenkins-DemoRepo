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
import com.amsinc.gems.adv.common.* ;


/*
**  WF_APRV_PROC_LOG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_APRV_PROC_LOGImpl extends  WF_APRV_PROC_LOGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public WF_APRV_PROC_LOGImpl (){
		super();
	}
	
	public WF_APRV_PROC_LOGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static WF_APRV_PROC_LOGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new WF_APRV_PROC_LOGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

	public void myInitializations()
	{
		setSaveBehaviorMode( AMSSAVEBEHAVIOR_DATAOBJECT_ALWAYS_SAVE ) ;
	}
	
	/*
       * Approval Proc Log Constants
	 */
	public static final int EVNT_TYP_GENERAL = 0;
	public static final int EVNT_TYP_ROUTING = 1;
	public static final int EVNT_TYP_RULES = 2;
	public static final int EVNT_TYP_SECURITY = 3;
   /** Event type indicating Warning */
   public static final int EVNT_TYP_WARNING = 4;
   /** Event type indicating Escalation */
   public static final int EVNT_TYP_ESCALATE = 5;
   
	private static final int DESC_COL_SIZE = 80;
	
	/**	  
	 * Add a new entry to the WF_APRV_PROC_LOG data object
	 * @param foSession
	 * @param fsDocCode
	 * @param fsDocDeptCD
	 * @param fsDocID
	 * @param fiDocVer
	 * @param fiEventType
	 * @param fsEventDescription
	 * @return boolean
	 */
	public static boolean logAprvProcEvent(Session foSession,
	                                       String fsDocCode,
	                                       String fsDocDeptCD,
	                                       String fsDocID,
	                                       int fiDocVer,
	                                       int fiEventType,
	                                       String fsEventDescription)
	{
	   WF_APRV_PROC_LOGImpl loAprvProcLogObj =
	         WF_APRV_PROC_LOGImpl.getNewObject(foSession, true);

	   // LOG_NO handled automatically by BRD logic

	   loAprvProcLogObj.setDOC_CD(fsDocCode);
	   loAprvProcLogObj.setDOC_DEPT_CD(fsDocDeptCD);
	   loAprvProcLogObj.setDOC_ID(fsDocID);
	   loAprvProcLogObj.setDOC_VERS_NO(fiDocVer);
	   loAprvProcLogObj.setEVNT_DT(new VSDate());
	   loAprvProcLogObj.setEVNT_TYP(fiEventType);

	   try
	   {
	      String lsDesc = null;

	      if (fsEventDescription.length() <= DESC_COL_SIZE)
	      {
	         lsDesc = fsEventDescription;
	      }
	      else
	      {
	         lsDesc = fsEventDescription.substring(0, DESC_COL_SIZE);
	      }
	      loAprvProcLogObj.setEVNT_DSCR(lsDesc);
	   }
	   catch (Exception loEx)
	   {
	      loAprvProcLogObj.setEVNT_DSCR("");
	   }

	   loAprvProcLogObj.save();  

	   return true; 
	}
}
