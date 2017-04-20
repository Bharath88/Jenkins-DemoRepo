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
**  WF_APRV_LOG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_APRV_LOGImpl extends  WF_APRV_LOGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public WF_APRV_LOGImpl (){
		super();
	}
	
	public WF_APRV_LOGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static WF_APRV_LOGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new WF_APRV_LOGImpl(session, makeDefaults);
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

	/**
	 * <br>
	 * Add a new entry to the WF_APRV_LOG data object
	 * @param foAprvSheet
	 * @param fiApprovalLevel
	 * @param fboolRecall
	 * @return boolean
	 */
	public static boolean logActionEvent( WF_APRV_SHImpl foAprvSheet,
	                                      int fiApprovalLevel,
	                                      boolean fboolRecall )
	{
	   Session loSession = foAprvSheet.getSession();
	   WF_APRV_LOGImpl loAprvLogObj =
	         WF_APRV_LOGImpl.getNewObject(loSession, true);

		 loAprvLogObj.setAPRV_RULE_ID(foAprvSheet.getAPRV_RULE_ID());

	   loAprvLogObj.setDOC_CD((String) foAprvSheet.getDOC_CD());
	   loAprvLogObj.setDOC_DEPT_CD((String) foAprvSheet.getDOC_DEPT_CD());
	   loAprvLogObj.setDOC_ID((String) foAprvSheet.getDOC_ID());
	   loAprvLogObj.setDOC_VERS_NO(foAprvSheet.getDOC_VERS_NO());

	   loAprvLogObj.setEVNT_DT(new VSDate());
	   loAprvLogObj.setUSID((String) foAprvSheet.getLAST_ACTN_USID());
	   loAprvLogObj.setAPRV_LVL((int) fiApprovalLevel);

	   loAprvLogObj.setAPRV_ACTN_STA_BFR(
	         foAprvSheet.getOldAPRV_ACTN_STA(fiApprovalLevel));

	   if (fboolRecall)
	   {
	      loAprvLogObj.setAPRV_ACTN_STA_AFT(
	            foAprvSheet.getLAST_ACTN_STA());
	   }
	   else
	   {
	      loAprvLogObj.setAPRV_ACTN_STA_AFT(
	            foAprvSheet.getAPRV_ACTN_STA(fiApprovalLevel));
	   }

	   // Set OFF_APRV_FL to true if log entry is being made
	   // in a batch session
	   if(isBatchSession(loSession))
	   {
	      loAprvLogObj.setAPRV_OFLN_FL(true);
	   }
	   else
	   {
	      loAprvLogObj.setAPRV_OFLN_FL(false);
	   }

	   loAprvLogObj.save();

	   return true;
	}
}
