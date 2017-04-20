//{{IMPORT_STMTS
package advantage.AdvMobile;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSSecurityObject;
import java.util.Enumeration;

/*
**  pDOC_CMNT
*/

//{{FORM_CLASS_DECL
public class pDOC_CMNT extends pDOC_CMNTBase

//END_FORM_CLASS_DECL}}
{

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.

   /* Document Identifiers */
   private String msDocCd;
   private String msDocDeptCd;
   private String msDocId;
   private String msDocVersNo;

   /**
    * Indicates if the source page has been considered or not for the type of action performed.
    */
   private boolean mboolSourcePageRead = false;

   /**
    * Name of the source page
    */
   private String msSrcPgNm = "";

   /* first time generate flag */
   private boolean mboolFirstTime = true;
   private static String C_MSG_SUPPRESSED_CMNT_EXISTS ="This document has suppressed comments.";

   /**
    * Indicates a return transaction to the originating document is taking place.
    */
   private boolean mboolReturnTrans = false;

   /**
    * Response string for when returning to a particular document to view.
    */
   private String msResponseOpenDoc = "";

   /**
    * Name of the source view
    */
   private String msSrcPgVw = "WorklistSummary";

   /**
    * Approval status of source document
    */
   private String msSrcAprv = "false";

   /**
    * Reject status of source document
    */
   private String msSrcRej = "false";

   /**
    * More status of source document
    */
   private String msSrcMore = "false";

   /**
    * Need the page_id of the source page so that the mobile app will know what page it
    * went back to. Without this, the back will not be able to work beyond the first one.
    */
   private String msSrcPageId = "";

   /**
    * Comment Value when insert action performed.
    */
   private String msCmnt = "";

   /**
    * Subject Value when insert action performed.
    */
   private String msSubj = "";

   /**
    * Indicates whether or not the comment page should be in insert mode at first.
    */
   private boolean mboolAddCmnt = false;

//{{FORM_CLASS_CTOR
public pDOC_CMNT ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

   }


//{{EVENT_CODE
//{{EVENT_T1DOC_CMNT_beforeInsert
void T1DOC_CMNT_beforeInsert(VSRow newRow ,VSOutParam cancel )
{
   //Write Event Code below this line
   String lsDocPhaseCd = getDocPhaseCd();
   newRow.getData(ATTR_DOC_CD).setString(msDocCd);
   newRow.getData(ATTR_DOC_ID).setString(msDocId);
   newRow.getData(ATTR_DOC_DEPT_CD).setString(msDocDeptCd);
   newRow.getData(ATTR_DOC_VERS_NO).setString(msDocVersNo);
   newRow.getData(ATTR_DOC_PHASE_CD).setString(lsDocPhaseCd);
   newRow.getData("CMNT_MSG").setString(msCmnt);
   newRow.getData("CMNT_SUB").setString(msSubj);

   String[] loParms = new String[5];
   loParms[0] = msDocCd;
   loParms[1] = msDocId;
   loParms[2] = msDocDeptCd;
   loParms[3] = msDocVersNo;
   loParms[4] = lsDocPhaseCd;

   try
   {
      //set session property to carry the document identifier to the data object later
      String lsMsgTxt = AMSHyperlinkActionElement.createMessageText(loParms);
      getParentApp().getSession().getORBSession().setProperty(SESSION_DOC_CMNT_MSG_TXT, lsMsgTxt);
   }
   catch (java.rmi.RemoteException loE)
   {
      raiseException( "Unable to set message properties", SEVERITY_LEVEL_SEVERE) ;
      return;
   }
}
//END_EVENT_T1DOC_CMNT_beforeInsert}}
//{{EVENT_pDOC_CMNT_beforeActionPerformed
void pDOC_CMNT_beforeActionPerformed( ActionElement foAe, PageEvent foEvt, PLSRequest foPLSReq )
{
   //Write Event Code below this line
   String lsAction = foAe.getAction();
   String lsSubAction = foPLSReq.getParameter(AMSHyperlinkActionElement.SUB_ACTN_ATTRIB);
   String lsAmsAct = foPLSReq.getParameter("ams_action");
   if(AMSStringUtil.strEqual(lsAmsAct, Integer.toString(AMSHyperlinkActionElement.OPEN_PAGE)))
   {
      VSPage loPageTrans = AdvMobile.getMobileGlobalTransition(getParentApp(), getSessionId(), foPLSReq);
      loPageTrans.setSourcePage(this);
      foEvt.setCancel(true) ;
      foEvt.setNewPage(loPageTrans);
      return;
   }

   if(AMSStringUtil.strEqual(String.valueOf(AMSHyperlinkActionElement.ACTN_CLOSE), lsAction))
   {
      resetSessionProp();
   }

   if(AMSStringUtil.strEqual(AMSHyperlinkActionElement.AE_ACTION_INSERT, lsAction))
   {
      msCmnt = foPLSReq.getParameter("CMNT_MSG");
      msSubj = foPLSReq.getParameter("CMNT_SUB");
   }

}
//END_EVENT_pDOC_CMNT_beforeActionPerformed}}
//{{EVENT_pDOC_CMNT_beforeGenerate
void pDOC_CMNT_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line
   if(getHighestSeverityLevel() >= SEVERITY_LEVEL_ERROR)
   {
      //undo insert so that it doesn't show up as a row in app
      T1DOC_CMNT.undoAll();
   }

   if (mboolFirstTime)
   {
      //if first time generate, check for any suppressed comments on current
      //and previous versions of the document.  Display info msg to user if yes.

      /*
       * This method does a nesting to obtain the parent document page,
       * since the immediate parent may not be the document page.
       */
      VSPage  loSrcPage = AMSPage.getDocumentPage(this);

      if(loSrcPage != null)
      {
         VSRow loVSRow = loSrcPage.getRootDataSource().getCurrentRow();

         SearchRequest loSR = new SearchRequest();
         StringBuffer loWhereClause = new StringBuffer();

         //Add an additional where clause to retrieve all comments for the document
         loWhereClause.append(ATTR_DOC_CD);
         loWhereClause.append(" = '");
         loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(msDocCd));
         loWhereClause.append("' AND ");
         loWhereClause.append(ATTR_DOC_DEPT_CD);
         loWhereClause.append(" = '");
         loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(msDocDeptCd));
         loWhereClause.append("' AND ");
         loWhereClause.append(ATTR_DOC_ID);
         loWhereClause.append(" = '");
         loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(msDocId));
         loWhereClause.append("' AND ");
         loWhereClause.append(ATTR_DOC_VERS_NO);
         loWhereClause.append(" <= ");
         loWhereClause.append(msDocVersNo);
         loWhereClause.append(" AND ");
         loWhereClause.append (ATTR_DOC_CMNT_SUPRS_FL);
         loWhereClause.append(" = 1");

         loSR.add( loWhereClause.toString());
         VSQuery loQuery = new VSQuery(getParentApp().getSession(),"DOC_CMNT", loSR, null);
         VSResultSet loRS = null;
         try
         {
            loRS = loQuery.execute();
            loRS.last();
            if(loRS.getRowCount() > 0)
            {
               raiseException (C_MSG_SUPPRESSED_CMNT_EXISTS, SEVERITY_LEVEL_INFO); //Document has suppressed comments
            }
         }
         finally
         {
            if (loRS!=null)
            {
               loRS.close();
            }
         }
         AMSTextContentElement loDocCdElement = (AMSTextContentElement) getElementByName("txtRSPrefixDOC_CD");
         AMSTextContentElement loDocDeptCdElement = (AMSTextContentElement) getElementByName("txtRSPrefixDOC_DEPT_CD");
         AMSTextContentElement loDocIdElement = (AMSTextContentElement) getElementByName("txtRSPrefixDOC_ID");
         if (loDocCdElement != null)
         {
            loDocCdElement.setText (loVSRow.getData(ATTR_DOC_CD).getString());
         }
         if (loDocDeptCdElement != null)
         {
            loDocDeptCdElement.setText (loVSRow.getData(ATTR_DOC_DEPT_CD).getString());
         }
         if (loDocIdElement != null)
         {
            loDocIdElement.setText (loVSRow.getData(ATTR_DOC_ID).getString());
         }
      }
   } /* end if (mboolFirstTime) */

   if(mboolAddCmnt)
   {
      HiddenElement loInsertMode = (HiddenElement) getElementByName("InsertOnload");
      loInsertMode.setValue("true");

      //only set the value true once
      mboolAddCmnt = false;
   }
   else
   {
      HiddenElement loInsertMode = (HiddenElement) getElementByName("InsertOnload");
      loInsertMode.setValue("false");
   }

  //Set first time to false
  mboolFirstTime = false;
}
//END_EVENT_pDOC_CMNT_beforeGenerate}}
//{{EVENT_pDOC_CMNT_afterPageClose
void pDOC_CMNT_afterPageClose( PageEvent evt )
{
    resetSessionProp();
   //Write Event Code below this line
}
//END_EVENT_pDOC_CMNT_afterPageClose}}
//{{EVENT_T1DOC_CMNT_beforeQuery
void T1DOC_CMNT_beforeQuery(VSQuery foQuery ,VSOutParam resultset )
{
   //Write Event Code below this line
   if(msSrcPgNm.equals(AdvMobile.MOB_WL_PG_NM) ||
      (getSourcePage() != null && getSourcePage().getName().equals(AdvMobile.MOB_WL_PG_NM)))
   {
      if(!mboolSourcePageRead)
      {
         setSourceInfo();
      }

      msSrcPgNm = AdvMobile.MOB_WL_PG_NM;
   }

   SearchRequest loOrderBy = new SearchRequest();
   SearchRequest loFilter = new SearchRequest();
   loFilter.add(" DOC_ID "+ AMSSQLUtil.getANSIQuotedStr(msDocId ,AMSSQLUtil.EQUALS_OPER));
   loFilter.add(" AND DOC_CD "+ AMSSQLUtil.getANSIQuotedStr(msDocCd ,AMSSQLUtil.EQUALS_OPER));
   loFilter.add(" AND DOC_DEPT_CD "+ AMSSQLUtil.getANSIQuotedStr(msDocDeptCd ,AMSSQLUtil.EQUALS_OPER));
   loFilter.add(" AND DOC_VERS_NO <= "+ msDocVersNo);
   loFilter.add(" AND CMNT_SUPRS_FL <> 1");
   loOrderBy.add("DOC_VERS_NO DESC, CMNT_DT DESC");
   foQuery.replaceSortingCriteria(loOrderBy);
   foQuery.addFilter(loFilter);
   mboolSourcePageRead = true;
}
//END_EVENT_T1DOC_CMNT_beforeQuery}}
//{{EVENT_pDOC_CMNT_afterActionPerformed
void pDOC_CMNT_afterActionPerformed( ActionElement foAe, PLSRequest foPLSReq )
{
   //Write Event Code below this line
   String lsSubAction = foPLSReq.getParameter(AMSHyperlinkActionElement.SUB_ACTN_ATTRIB);
   //Indicate back action
   if("mob_back".equals(lsSubAction) && getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR)
   {
      mboolReturnTrans = true;
      mboolSourcePageRead = false;
      mboolFirstTime = false;
   }
}
//END_EVENT_pDOC_CMNT_afterActionPerformed}}
//{{EVENT_T1DOC_CMNT_afterInsert
void T1DOC_CMNT_afterInsert(VSRow newRow )
{
   //Write Event Code below this line
   newRow.save();
}
//END_EVENT_T1DOC_CMNT_afterInsert}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1DOC_CMNT.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_afterActionPerformed( ae, preq );
		}
	}
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_beforeGenerate(docModel, cancel, output);
		}
	}
	public void afterInsert( DataSource obj, VSRow newRow ){
		Object source = obj;
		if (source == T1DOC_CMNT) {
			T1DOC_CMNT_afterInsert(newRow );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeInsert( DataSource obj, VSRow newRow ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1DOC_CMNT) {
			T1DOC_CMNT_beforeInsert(newRow ,cancel );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1DOC_CMNT) {
			T1DOC_CMNT_beforeQuery(query , resultset );
		}
	}
	public void afterPageClose( VSPage obj, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_afterPageClose( evt );
		}
	}
//END_EVENT_ADAPTER_CODE}}

      public HTMLDocumentSpec getDocumentSpecification() {
            return getDefaultDocumentSpecification();
      }

        public String getFileName() {
         return getDefaultFileName();
       }
      public String getFileLocation() {
         return getPageTemplatePath();
          }

   public void afterPageInitialize() {
      super.afterPageInitialize();
      //Write code here for initializing your own control
      //or creating new control.

   }

   /**
   * Sets document identiers into the page to be used during insert. And other information
   *
   */
   public void setSourceInfo()
   {
      msDocCd = ((pMobileWorklist)getSourcePage()).getDocCd();
      msDocId = ((pMobileWorklist)getSourcePage()).getDocId();
      msDocDeptCd = ((pMobileWorklist)getSourcePage()).getDocDeptCd();
      msDocVersNo = ((pMobileWorklist)getSourcePage()).getDocVersNo();
      msSrcPgVw = ((pMobileWorklist)getSourcePage()).getPgVw();
      msSrcRej = ((pMobileWorklist)getSourcePage()).getRejectStatus();
      msSrcAprv = ((pMobileWorklist)getSourcePage()).getApproveStatus();
      msSrcMore = ((pMobileWorklist)getSourcePage()).getMoreStatus();
      msSrcPageId = ((pMobileWorklist)getSourcePage()).getPageId();
      msResponseOpenDoc = ((pMobileWorklist)getSourcePage()).getReturnResponse();
      mboolAddCmnt = ((pMobileWorklist)getSourcePage()).getAddCmnt();

      HiddenElement loDocElem = (HiddenElement) getElementByName("Code");
      loDocElem.setValue(msDocCd);
      loDocElem = (HiddenElement) getElementByName("Dept");
      loDocElem.setValue(msDocDeptCd);
      loDocElem = (HiddenElement) getElementByName("Id");
      loDocElem.setValue(msDocId);
      loDocElem = (HiddenElement) getElementByName("Version");
      loDocElem.setValue(msDocVersNo);

      /*
       * This page can be accessed only from the mobile worklist transition, however there is the
       * possibility it may be coming to this page through the AMSHyperlink action element. If
       * the source page value for comment transition is anything other than true, set the value
       * to true so that the do action will not send the generated string of the document again,
       * and will send this page as intended.
       */
      boolean lboolTransCmnt = ((pMobileWorklist)getSourcePage()).getCmntTrans();
      if(!lboolTransCmnt)
      {
         ((pMobileWorklist)getSourcePage()).setCmntTrans(true);
      }
   }
   private void resetSessionProp()
   {
       try
       {
           getParentApp().getSession().getORBSession().setProperty(SESSION_DOC_CMNT_MSG_TXT, "");
       }
       catch (java.rmi.RemoteException loE)
       {
           raiseException( "Unable to set message properties", SEVERITY_LEVEL_SEVERE) ;
           return;
       }
   }

   public String doAction(PLSRequest foPLSReq)
   {
      String lsResponse;
      lsResponse = super.doAction( foPLSReq ) ;

      //returning to work list page, set the open document as the response.
      if(!AMSStringUtil.strIsEmpty(msResponseOpenDoc) && mboolReturnTrans)
      {
         AMSSecurityObject loSecObj = (AMSSecurityObject) (getParentApp().getSession().getSecurityObject());

         String lsCallBack = getCallBack();
         if (lsCallBack == null && loSecObj.getMobileCallBackFunction() != null)
         {
            lsCallBack = loSecObj.getMobileCallBackFunction();
         }

         StringBuffer lsbMetaData = new StringBuffer("{\"MetaData\":{");

         //append MetaData Info to response
         for (Enumeration loHiddenElemEnum = getDocumentModel().getHiddenElements();
              loHiddenElemEnum.hasMoreElements();)
         {

            HiddenElement loHElement = (HiddenElement) loHiddenElemEnum
                  .nextElement();
            String lsName = loHElement.getName();
            lsbMetaData.append("\"").append(lsName).append("\":");

            //don't quote the errors hidden attribute
            if(AMSStringUtil.strEqual(lsName, "Errors"))
            {
               lsbMetaData.append(loHElement.getValue());
            }
            else if(AMSStringUtil.strEqual(lsName, "PageView"))
            {
               lsbMetaData.append("\"").append(msSrcPgVw).append("\"");
            }
            else if(AMSStringUtil.strEqual(lsName, "DocReject"))
            {
               lsbMetaData.append("\"").append(msSrcRej).append("\"");
            }
            else if(AMSStringUtil.strEqual(lsName, "DocApprove"))
            {
               lsbMetaData.append("\"").append(msSrcAprv).append("\"");
            }
            else if(AMSStringUtil.strEqual(lsName, "DocMore"))
            {
               lsbMetaData.append("\"").append(msSrcMore).append("\"");
            }
            else if(AMSStringUtil.strEqual(lsName, "page_id"))
            {
               lsbMetaData.append("\"").append(msSrcPageId).append("\"");
            }
            else
            {
               lsbMetaData.append("\"").append(loHElement.getValue()).append("\"");
            }
            if(loHiddenElemEnum.hasMoreElements())
            {
               lsbMetaData.append(",");
            }
            else
            {
               lsbMetaData.append("}");
            }
         }

         lsResponse = lsCallBack + "(" + lsbMetaData.toString() + ",\"Open_Document\":" + msResponseOpenDoc;
      }

      mboolReturnTrans = false;
      return lsResponse ;
   } /* end doAction() */

   /**
    * Get the current phase code of the document since it may have been in progress of being approved
    * on load.
    * @return String representing the current document phase code from the document header record.
    */
   private String getDocPhaseCd()
   {
      SearchRequest loSrchRq = new SearchRequest();
      VSResultSet loDocPhaseRs = null;
      String lsDocPhaseCd = "";

      try
      {
         loSrchRq.addParameter("DOC_HDR", "DOC_ID", msDocId);
         loSrchRq.addParameter("DOC_HDR", "DOC_CD", msDocCd);
         loSrchRq.addParameter("DOC_HDR", "DOC_DEPT_CD", msDocDeptCd);
         loSrchRq.addParameter("DOC_HDR", "DOC_VERS_NO", msDocVersNo);

         VSQuery loDocHdrQry = new VSQuery(getParentApp().getSession(),"DOC_HDR", loSrchRq, null);

         // execute query into resultset
         loDocPhaseRs = loDocHdrQry.execute();
         VSRow loCurrStatusCodeRow = loDocPhaseRs.last();
         lsDocPhaseCd = loCurrStatusCodeRow.getData("DOC_PHASE_CD").getString();
      }
      catch(Exception loEx)
      {
         raiseException("Unable to determine document phase code.", SEVERITY_LEVEL_SEVERE);
      }
      finally
      {
         if(loDocPhaseRs != null)
         {
            loDocPhaseRs.close();
         }
      }

      return lsDocPhaseCd;
   }
}
