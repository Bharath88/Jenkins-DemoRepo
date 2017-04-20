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
**  R_ASEM_REQ
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_ASEM_REQImpl extends  R_ASEM_REQBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

   private static Log moLog = AMSLogger.getLog( AMSDocHeader.class,
         AMSLogConstants.FUNC_AREA_FORMS_ASSEMBLY ) ;

	//{{COMP_CLASS_CTOR
	public R_ASEM_REQImpl (){
		super();
	}
	
	public R_ASEM_REQImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line
	if(moLog.isDebugEnabled())
	{
		moLog.debug("R_ASEM_REQImpl.beforeInsert()");
	}



   /*
    * copied from BS_AGENT
    */
   long llJobId = -1;
   Session loSession = obj.getSession();
   try
   {
      llJobId =AMSUniqNum.getUniqNum(AMSBatchParmUtil.ASSEMBLE_TABLE_NM);
   }
   catch(AMSUniqNumException foExp)
   {
      AMSDataObject.raiseException( "Could not generate unique Job Id:" + foExp.getMessage(),
                                     loSession,
                                     AMSMsgUtil.SEVERITY_LEVEL_SEVERE );
   }

   obj.getData("REQ_ID").setlong(llJobId );

}
//END_COMP_EVENT_beforeInsert}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static R_ASEM_REQImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_ASEM_REQImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}


   public static R_ASEM_REQImpl addNewRequest(String fsApplRsrcCd,
            String fsPrntJobCd, Session foSession,
            String fsDocCd, String fsDocDeptCd, String fsDocId, int fiDocVersNo,
            long flAgntId, int fiDocPhaseCd )
   {
      R_ASEM_REQImpl loRequest = null;
      try
      {
         loRequest = R_ASEM_REQImpl.getNewObject(foSession, true);
         loRequest.setAPPL_RSRC_ID(fsApplRsrcCd);
         loRequest.setPRNT_JOB_CD(fsPrntJobCd);
         loRequest.setDOC_CD(fsDocCd);
         loRequest.setDOC_DEPT_CD(fsDocDeptCd);
         loRequest.setDOC_ID(fsDocId);
         loRequest.setDOC_VERS_NO(fiDocVersNo);
         loRequest.setAGNT_ID(flAgntId);
         loRequest.setDOC_PHASE_CD(fiDocPhaseCd);
         loRequest.save();

      }
      catch(Exception e)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

         return null;
      }

      return loRequest;
   }


}

