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
**  UPLOAD_FILES
*/

//{{COMPONENT_RULES_CLASS_DECL
public class UPLOAD_FILESImpl extends  UPLOAD_FILESBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
        private static final int C_INT_ZERO = 0;
        private static final int C_REPEAT_NO = -1;
        private static final long C_CLTG_ID = 2015;
		//{{COMP_CLASS_CTOR
		public UPLOAD_FILESImpl (){
			super();
		}
		
		public UPLOAD_FILESImpl(Session session, boolean makeDefaults)
		{
			super(session, makeDefaults);
		
		
		
		
		//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
	//Write Event Code below this line
    Session loSession = getSession();
    String lsEmailAddr = getEMAIL_AD_TXT();
    if (AMSStringUtil.strIsEmpty(lsEmailAddr))
    {
        lsEmailAddr = getEmailAddr();
        getRow().getData("EMAIL_AD_TXT").setString(lsEmailAddr);
    }

    Date loSchedDt  =null;
    Date loExprDt  =null;
    BS_AGENTImpl loBsAgent = null;
    // Begin ADVHR00019967
    /*  If schedule time is not entered by user , run option is RUN_IMMEDIATE
     *  If schedule time is entered by user, run option will be RUN_ONCE and
     *  "don’t schedule after time" will be more than schedule time by 1 minute.
     */
    if (getSCHED_DT() != null)
    {
        loSchedDt = AMSBatchUtil.getDateFromVSDate(getSCHED_DT());
        VSDate loVSDExprDt = getSCHED_DT();
        loVSDExprDt.setMinutes(loVSDExprDt.getMinutes()+1);
        loExprDt = AMSBatchUtil.getDateFromVSDate(loVSDExprDt);
        loBsAgent = BS_AGENTImpl.addNewJob(loSession,
                        C_CLTG_ID,
                        loSession.getUserID(),
                        AMSBatchConstants.RUNPLCY_ONCE,
                        loSchedDt,loExprDt,
                        C_INT_ZERO,
                        C_REPEAT_NO,
                        AMSBatchConstants.JOB_PRTY_1,
                        lsEmailAddr,
                        C_INT_ZERO,
                        C_INT_ZERO,
                        AMSBatchConstants.EXPR_AFTER_6MONTH);
    }
    else
    {
        loSchedDt = AMSBatchUtil.getDateFromVSDate(new VSDate());
        loBsAgent = BS_AGENTImpl.addNewJob(loSession,
        C_CLTG_ID,
        loSession.getUserID(),
        AMSBatchConstants.RUNPLCY_IMMEDIATE,
        loSchedDt,loSchedDt,
        C_INT_ZERO,
        C_REPEAT_NO,
        AMSBatchConstants.JOB_PRTY_1,
        lsEmailAddr,
        C_INT_ZERO,
        C_INT_ZERO,
        AMSBatchConstants.EXPR_AFTER_6MONTH);
    }
    // End ADVHR00019967
    long llJobID = loBsAgent.getAGNT_ID();
    String lsActnCode = String.valueOf(SysManUtil.CSV_OVERLAY);
    String lsTblNm = (String)loSession.getProperty("IMPORT_TABLE_NAME");
	String lsJobNm = lsTblNm + (new VSDate()).getTime();

    SearchRequest lsrBSAgnt = new SearchRequest();
    lsrBSAgnt.addParameter("BS_AGENT","AGNT_ID",String.valueOf(llJobID));

    BS_AGENTImpl loBSAgnt = (BS_AGENTImpl)BS_AGENTImpl.getObjectByKey(
           lsrBSAgnt,loSession);

    loBSAgnt.setJOB_NM(lsJobNm);
    /**
     * add parameters for SMU job.
     */
    BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_FILE_NM,
        getUPLOAD_FILE_NM());
        BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_ERR_FILE_NM,
        getUPLOAD_FILE_NM()+"error");

    BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_EDITS_FL,
        AMSCommonConstants.TRUE_VAL);

    BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_TBL_NM,
        lsTblNm);

    BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_ACTN_CD,
        lsActnCode);

    BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_DETAIL_IMP_ERR_RPT,
        "TRUE");
         
BS_AGENT_PARMImpl.addNewParameter(loSession,
        llJobID,
        SysManUtil.ATTR_GENSTATS_FL,
        AMSCommonConstants.TRUE_VAL);


    BS_AGENTImpl.setJobEnabled(loSession, llJobID);

    raiseException("%c:Q0141,v:" + lsJobNm + ",v:" + llJobID + ",v:"
        + loSchedDt.toString() + "%");


}
//END_COMP_EVENT_afterInsert}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static UPLOAD_FILESImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new UPLOAD_FILESImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}


    /**
     * This method is used to retrieve the email address of the user
     *
     * @return String - returns the email-address of the user
     */
    private String getEmailAddr()
    {
        Session loSession = getSession();
        SearchRequest loSrchReq = new SearchRequest();
        loSrchReq.addParameter("R_SC_USER_DIR_INFO", "USER_ID",
            loSession.getUserID());
        R_SC_USER_DIR_INFOImpl loUserRec =
            (R_SC_USER_DIR_INFOImpl)R_SC_USER_DIR_INFOImpl.getObjectByKey(
                loSrchReq, loSession);
        if ( loUserRec != null )
        {
            return loUserRec.getEMAIL_AD_TXT() ;
        } /* if ( loUserRec != null ) */
        return "";
    } // end getEmailAddr method

}

