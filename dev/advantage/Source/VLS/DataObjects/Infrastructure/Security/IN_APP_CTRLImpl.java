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
**  IN_APP_CTRL*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_APP_CTRLImpl extends  IN_APP_CTRLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   // Constants for IN_APP_CTRL Parameter Names.

      public static final String BYPASS_FIRSTLOGIC      = "BYPASS_FIRSTLOGIC";
      public static final String CR_DECR_ETYP           = "CR_DECR_ETYP";
      public static final String DSBL_BEG_WILDCARD_FLAG = "Disable_Beginning_Wildcard_Search";
      public static final String FDT_Q_ID               = "FDT_Q_ID";
      public static final String VTH_MAX_REC_COUNT      = "VTH_MAX_REC_COUNT";
      public static final String ALW_FINAL_SH_REF_CR = "ALW_FINAL_SH_REF_CR";
      public static final String REQ_CONSISTENT_TIN = "REQ_CONSISTENT_TIN_TINTYP_BET_HQ_LOC";

      public static final String ALW_ACCT_ACTV_VERIFY = "ALW_ACCT_ACTV_VERIFY";
      public static final String ALW_EFT_REG = "ALW_EFT_REG";
      public static final String ALW_MUL_LOC_HEAD_ACC = "ALW_MUL_LOC_HEAD_ACC";
      public static final String ALW_VEND_BUS_TYP = "ALW_VEND_BUS_TYP";
      public static final String ALW_VEND_REG_COMM = "ALW_VEND_REG_COMM";
      public static final String ALW_VEND_SERV_AREA = "ALW_VEND_SERV_AREA";
      public static final String DISB_W9_CERT_FORM = "DISB_W9_CERT_FORM";
      public static final String DISP_EFT_REG_FORM = "DISP_EFT_REG_FORM";
      public static final String DISP_VEND_REG_APP = "DISP_VEND_REG_APP";
      public static final String THK_YOU_PDF_SEC_TXT = "THK_YOU_PDF_SEC_TXT";
      public static final String THK_YOU_PG_TXT = "THK_YOU_PG_TXT";

   /**
    * This represents the Application Control Parameter which contains the
    * default subject text to provide when a Grant Message Subject is left
    * empty.
    */
   public static final String DFLT_GRNT_MSG_SUBJECT = "DFLT_GRNT_MSG_SUBJECT";

      /*
      **  Constants to determine if client is using Inventory component.
      */
      public static final String INVENTORY_INSTALLED = "INVENTORY_INSTALLED";
   /**
    * Check Assignment in AD Processing
    * False-Check Assign Mode After Validate/Submit of AD Document
    * True-Check assign Mode through Check Assignment process of AD Chain
    */
   public static final String BATCH_CHK_ASSIGN     = "BATCH_CHK_ASSIGN";
   public static final String BATCH_CHK_ASSIGN_VAL = "true";

   /**
    * This represents the Application Control Parameter which contains the
    * maximum Grant Awarded Amount.
    */
   public static final String GRNT_AWD_AMT = "GRNT_AWD_AMT";

   /**
    * Check Assignment in AD Processing
    * False-Check Assign Mode After Validate/Submit of AD Document
    * True-Check assign Mode through Check Assignment process of AD Chain
    */
   public static final int MAX_LEN_AMND_PRFX = 4;

   // This represents the Document Code for 'CAS' document.
   public static final String CAS_DOC_CD = "CAS_DOC_CD";

   /* Constant used to store the value of Max Download Lines from APPCTRL */
   public static String C_IN_APP_MAX_DL_LINES = "MAX_DOWNLOAD_LINES";

   /* Constant used to store the value of VCM_DISCARD for VCM_TRCK_TBL */
   public static String VCM_DISCARD = "VCM_DISCARD";
   /**
    * The constant representing the Check Stub Consolidation parameter
    * on Application Control Table
    */
   public static final String CONSOLIDATE_CHECK_STUB = "CONSOLIDATE_CHECK_STUB";
   /* Variables to store Application Parameters related to Document
      Component level organizational security */

   /* This variable will set to false after invoking mboolEnableDocCompSecurityForEdit
      for the 1st time */
   private static boolean mboolInitEnableDocCompSecurityForEdit = false;

  /* This variable is set as ENABLE_DOC_COMP_ORG_SEC_EDIT on appctrl page */
   private static boolean mboolEnableDocCompSecurityForEdit;

   /* This variable will set to false after invoking mboolInitEnableDocCompSecurityForDiscard
      for the 1st time  */
   private static boolean mboolInitEnableDocCompSecurityForDiscard = false;

   /* This variable is set as ENABLE_DOC_COMP_ORG_SEC_DISCARD on appctrl page */
   private static boolean mboolEnableDocCompSecurityForDiscard;

  /**
	* The Application Control Parameter which determines the
	* default Print Output Type value to be populated on Document Print Dialog page.
	*/
  public static final String DFLT_PRNT_OUTPUT_TYPE = "DFLT_PRNT_OUTPUT_TYPE";
  /**
	* The Application Control Parameter which determines whether the View Forms check box
	* on Document Print Dialog page needs to be selected by default or not.
	*/
  public static final String DFLT_VIEW_FORMS = "DFLT_VIEW_FORMS";


//{{COMP_CLASS_CTOR
public IN_APP_CTRLImpl (){
	super();
}

public IN_APP_CTRLImpl(Session session, boolean makeDefaults)
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
	public static IN_APP_CTRLImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new IN_APP_CTRLImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}


   /**
     * Method to return IN_APP_CTRLImpl record based on the passed Paramter Name
     * @ String fdParamNm: Parameter Name
     * @ Session foSession
     * @return IN_APP_CTRLImpl
     */
   public static IN_APP_CTRLImpl getInAppCtrlRec(String fsParamNm, Session foSession)
   {
      SearchRequest lsr = new SearchRequest();
      lsr.addParameter("IN_APP_CTRL", "PARM_NM", fsParamNm);
      IN_APP_CTRLImpl loInAppCtrl = (IN_APP_CTRLImpl)IN_APP_CTRLImpl.getObjectByKey(lsr, foSession);

      return loInAppCtrl;
   }

   /**
    * This new method retrieves the parameter value given the parameter name from the IN_APP_CTRL table
    *
    * @param fsParmNm  - the parameter name
    * @param foSession - the session instance
    * @return is the parameter value being returned
    */
   public static String getParameterValue(String fsParmNm , Session foSession)
   {
      try
      {
         if (AMSStringUtil.strIsEmpty(fsParmNm))
         {
            return null;
         }
         SearchRequest lsrINAPPCTRL = new SearchRequest();
         lsrINAPPCTRL.addParameter("IN_APP_CTRL", "PARM_NM", fsParmNm);

         IN_APP_CTRLImpl loINAPPCTRL = (IN_APP_CTRLImpl) IN_APP_CTRLImpl.getObjectByKey(lsrINAPPCTRL, foSession );
         return loINAPPCTRL.getPARM_VL();
      }
      catch(Exception loex)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loex);

         return null;
      }
   }  // End method getParameterValue


public void myInitializations()
{
    setPreventChangeOfRecordKey(true);
}
   /**
    * Returns true when Application parameter value of ENABLE_DOC_COMP_ORG_SEC_EDIT
    * is set to true(case-insensitive) else false in all other cases. Returns false
    * when Application Parameter ENABLE_DOC_COMP_ORG_SEC_EDIT is not found on
    * Application Parameters. Returns false when current application is VSS.
    * @param foSession - Session
    * @return boolean
    */
   public static boolean isDocCompSecurityForEditEnabled( Session foSession )
   {
      /* When invoked for the first time, fetch the Application parameter
         value of ENABLE_DOC_COMP_ORG_SEC_EDIT from IN_APP_CTRL and return it.
         Any calls after it, will return the cached value. */
      if( !mboolInitEnableDocCompSecurityForEdit )
      {
         String lsEnable = AMSUtil.getApplParameterString(
               "ENABLE_DOC_COMP_ORG_SEC_EDIT", foSession );
         /*
          * If Application parameter ENABLE_DOC_COMP_ORG_SEC_EDIT is not defined
          * on Application Parameters then return false.
          * For VSS application this parameter is not relevant and should always
          * return false(else problems encountered during creation of Response
          * for Solicitations).
          */
         if( AMSStringUtil.strIsEmpty( lsEnable )
               || AMSStringUtil.strEqual( AMSParams.msPrimaryApplication,
                     String.valueOf(AMSCommonConstants.VSS_APPL) ))
         {
            mboolEnableDocCompSecurityForEdit = false;
         }
         else
         {
            if( AMSStringUtil.strInsensitiveEqual( lsEnable, "true" ) )
            {
               mboolEnableDocCompSecurityForEdit = true;
            }
            else
            {
               mboolEnableDocCompSecurityForEdit = false;
            }
         }//end if
         mboolInitEnableDocCompSecurityForEdit = true;
      }
      return mboolEnableDocCompSecurityForEdit;
   }/* end of getEnableDocCompSecurityForEdit */

   /**
    * Returns true when Application parameter value of ENABLE_DOC_COMP_ORG_SEC_DISCARD
    * is set to true(case-insensitive) else false in all other cases. Returns false
    * when Application Parameter ENABLE_DOC_COMP_ORG_SEC_DISCARD is not found on
    * Application Parameters. Returns false when current application is VSS.
    * @param foSession - Session
    * @return boolean
    */
   public static boolean isDocCompSecurityForDiscardEnabled( Session foSession )
   {
      /* When invoked for the first time, fetch the Application parameter
         value of ENABLE_DOC_COMP_ORG_SEC_DISCARD from IN_APP_CTRL and return it.
         Any calls after it, will return the cached value. */
      if( !mboolInitEnableDocCompSecurityForDiscard )
      {
         String lsEnable = AMSUtil.getApplParameterString(
               "ENABLE_DOC_COMP_ORG_SEC_DISCARD" , foSession );
         /*
          * If Application parameter ENABLE_DOC_COMP_ORG_SEC_DISCARD is not defined
          * on Application Parameters then return false.
          * For VSS application this parameter is not relevant and should always
          * return false(else problems encountered during creation of Response
          * for Solicitations).
          */
         if( AMSStringUtil.strIsEmpty( lsEnable )
               || AMSStringUtil.strEqual( AMSParams.msPrimaryApplication,
                     String.valueOf(AMSCommonConstants.VSS_APPL) ))
         {
            mboolEnableDocCompSecurityForDiscard = false;
         }
         else
         {
            if( AMSStringUtil.strInsensitiveEqual( lsEnable, "true" ) )
            {
               mboolEnableDocCompSecurityForDiscard = true;
            }
            else
            {
               mboolEnableDocCompSecurityForDiscard = false;
            }
         }//end if
         mboolInitEnableDocCompSecurityForDiscard = true;
      }
      return mboolEnableDocCompSecurityForDiscard;
   }/* end of getEnableDocCompSecurityForDiscard */

}

