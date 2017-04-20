//{{IMPORT_STMTS
package advantage.AdvMobile;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import com.amsinc.gems.adv.vfc.html.servlet.AMSPLSServlet;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import com.amsinc.gems.adv.workflow.aprv.AMSAprvWorkflowToolKit;
import com.amsinc.gems.adv.vfc.html.AMSComboBoxElement;

//{{FORM_CLASS_DECL
public class pMobileWorklistSearch extends pMobileWorklistSearchBase

//END_FORM_CLASS_DECL}}
{
   /**
    * This variable will hold the Code search parameter value.
    */
   private String msDocCd = "";

   /**
    * This variable will hold the Worklist parameter value.
    */
   private String msWrkLst = "";

   /**
    * This variable will hold the Level search parameter value.
    */
   private String msAprvLvl = "";

   /**
    * This variable will hold the Dept search parameter value.
    */
   private String msDocDeptCd = "";

   /**
    * This variable will hold the ID search parameter value.
    */
   private String msDocId = "";

   /**
    * This variable will hold the Submitter ID search parameter value.
    */
   private String msDocSbmtId = "";

   /**
    * This variable will hold the Escalted Item search parameter value.
    */
   private String msEsclItem = "";

   /**
    * Represents if the Workflow role selected on the page list was the first element in the list.
    */
   private boolean mboolCurrUser = false;

   /**
    * Represents if this is the first time the page is being generated.
    */
   private boolean mboolFirstGen = true;

//{{FORM_CLASS_CTOR
public pMobileWorklistSearch ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
    }



//{{EVENT_CODE
//{{EVENT_pMobileWorklistSearch_beforeActionPerformed
void pMobileWorklistSearch_beforeActionPerformed( ActionElement foActionElem, PageEvent foEvt, PLSRequest foPLSReq)
{
   //Write Event Code below this line
   String lsActionName = foActionElem.getName();
   String lsAmsAct = foPLSReq.getParameter("ams_action");

   if (AMSStringUtil.strEqual("AMSBrowse", lsActionName))
   {
      //set browse fields for pMobileWorklist to filter the query
      setBrowseFields(foPLSReq);

      AMSDynamicTransition loDynTran = new AMSDynamicTransition( "pMobileWorklist", "", "AdvMobile");
      loDynTran.setSourcePage( this) ;
      foEvt.setCancel( true ) ;
      foEvt.setNewPage(loDynTran.getVSPage( getParentApp(), getSessionId()));
   }

   if(AMSStringUtil.strEqual(lsAmsAct, Integer.toString(AMSHyperlinkActionElement.OPEN_PAGE)))
   {
      VSPage loPageTrans = AdvMobile.getMobileGlobalTransition(getParentApp(), getSessionId(), foPLSReq);
      loPageTrans.setSourcePage(this);
      foEvt.setCancel(true) ;
      foEvt.setNewPage(loPageTrans);
      return;
   }
}
//END_EVENT_pMobileWorklistSearch_beforeActionPerformed}}
//{{EVENT_pMobileWorklistSearch_beforeGenerate
void pMobileWorklistSearch_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line
   if(mboolFirstGen)
   {
      mboolFirstGen = false;
      VSSession loSession = getParentApp().getSession() ;
      String    lsUserID  = loSession.getLogin() ;

      String[] lsWorklistNames = AMSAprvWorkflowToolKit.createTabNames(loSession, lsUserID);
      String[] lsWorklistIDs   = AMSAprvWorkflowToolKit.createTabIds(loSession, lsUserID);

      AMSComboBoxElement loWrkLstCombo = (AMSComboBoxElement)getElementByName("txtT1WorkLists");
      if(loWrkLstCombo != null)
      {
         loWrkLstCombo.removeAllElements();
         for ( int liCtr = 0; liCtr < lsWorklistNames.length; liCtr++)
         {
            loWrkLstCombo.addElement( lsWorklistNames[liCtr], lsWorklistIDs[liCtr] ) ;
         } /* end for ( int liCtr = 0 ; liCtr < msWorklistNames.length ; liCtr++ ) */
      } /* end if ( moWrkLstCombo != null ) */
   }
}
//END_EVENT_pMobileWorklistSearch_beforeGenerate}}

//END_EVENT_CODE}}

    public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
    }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pMobileWorklistSearch_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pMobileWorklistSearch_beforeActionPerformed( ae, evt, preq );
		}
	}
//END_EVENT_ADAPTER_CODE}}

        public String getFileName() {
          return getDefaultFileName();
       }

      public String getFileLocation() {
         return getPageTemplatePath();
          }

      public HTMLDocumentSpec getDocumentSpecification() {
            return getDefaultDocumentSpecification();
      }

   /**
    * Set the browse fields so that they may be used by the pages that reference this one.
    * @param PLSRequest represents the request that contains the parameters received for the query.
    */
   private void setBrowseFields(PLSRequest foPLSReq)
   {
      msWrkLst = foPLSReq.getParameter("txtT1WorkLists");
      msAprvLvl = T1WF_WRK_LST_QRY.getQBFDataForElement("txtT1APRV_LVL");
      msDocCd = T1WF_WRK_LST_QRY.getQBFDataForElement("txtT1DOC_CD");
      msDocDeptCd = T1WF_WRK_LST_QRY.getQBFDataForElement("txtT1DOC_DEPT_CD");
      msDocId = T1WF_WRK_LST_QRY.getQBFDataForElement("txtT1DOC_ID");
      msDocSbmtId = T1WF_WRK_LST_QRY.getQBFDataForElement("txtT1DOC_SBMT_ID");
      msEsclItem = T1WF_WRK_LST_QRY.getQBFDataForElement("txtT1ESCL_ITEM");

      if(!AMSStringUtil.strIsEmpty(msDocCd))
      {
         msDocCd = msDocCd.toUpperCase();
         T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_CD", msDocCd);
      }

      if(!AMSStringUtil.strIsEmpty(msDocId))
      {
         msDocId = msDocId.toUpperCase();
         T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_ID", msDocId);
      }

      if(!AMSStringUtil.strIsEmpty(msDocDeptCd))
      {
         msDocDeptCd = msDocDeptCd.toUpperCase();
         T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_DEPT_CD", msDocDeptCd);
      }

      mboolCurrUser = Boolean.parseBoolean(foPLSReq.getParameter("hidCurrUser"));
      setCurrUser(mboolCurrUser);
      setCurrWorkList(msWrkLst);
   }

   /**
    * Get the Worklist for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getWrkLst()
   {
      return msWrkLst;
   }

   /**
    * Get the Level for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getAprvLvl()
   {
      return msAprvLvl;
   }

   /**
    * Get the Code for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getDocCd()
   {
      return msDocCd;
   }

   /**
    * Get the Dept for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getDocDeptCd()
   {
      return msDocDeptCd;
   }

   /**
    * Get the ID for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getDocId()
   {
      return msDocId;
   }

   /**
    * Get the Submitter ID for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getDocSbmtId()
   {
      return msDocSbmtId;
   }

   /**
    * Get the Escalted Item for the search parameter set
    * @return String representing the Worklist search parameter found.
    */
   protected String getEsclItem()
   {
      return msEsclItem;
   }

   /**
    * Returns whether or not the current user role was indicated on the page list page.
    * @return boolean representing true if the the workflow role that was selected was current user.
    */
   public boolean getCurrUser()
   {
      return mboolCurrUser;
   }
}
