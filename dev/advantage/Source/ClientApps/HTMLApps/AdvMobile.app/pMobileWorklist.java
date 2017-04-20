//{{IMPORT_STMTS
package advantage.AdvMobile;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSDocTabbedPage;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSTableElement;
import java.util.Vector;
import java.util.Enumeration;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import java.util.HashMap;
import com.cgi.adv.doc.render.AdvIDocRenderer;
import com.cgi.adv.doc.render.AdvJsonDocRenderer;
import com.cgi.adv.doc.AdvDocViewUtil;
import versata.vls.Session;
import versata.vls.ServerEnvironment;
import com.amsinc.gems.adv.common.AMSSecurityObject;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import org.apache.commons.logging.Log;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;

   /*
   **  pWorklist*/

//{{FORM_CLASS_DECL
public class pMobileWorklist extends pMobileWorklistBase

//END_FORM_CLASS_DECL}}
   {
      // Declarations for instance variables used in the form

      /**
       * Represents the Workflow role selected on the page list when it is pressed on the mobile app.
       */
      private String msWorkflowRole = "";

      /**
       * Represents if the Workflow role selected on the page list was the first element in the list.
       */
      private boolean mboolCurrUser = false;

      /**
       * This variable will hold the Code search parameter value.
       */
      private String msDocCd = "";

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
       * Name of the source page
       */
      private String msSrcPgNm = "";

      /**
       * Name of the  view
       */
      private String msPgVw = "WorklistSummary";


      /**
       * Doc Reject Action status
       */
      private String msDocReject = "false";

      /**
       * Doc Approve Action status
       */
      private String msDocApprove = "false";

      /**
       * Doc More Action status
       */
      private String msDocMore = "false";

      /**
       * Response string for when opening a particular document to view.
       */
      private String msResponseOpenDoc = "";

      /**
       * Response has been reset and needs callback added.
       */
      private boolean mboolRespReset = false;

      private boolean mboolSummaryMode = true;

      private static Log moLog = AMSLogger.getLog( pMobileWorklist.class,
            AMSLogConstants.FUNC_AREA_MOBILITY);

      /**
       * Represents the Selected Document Code
       */
      private String msSelectedDocCd = "";

      /**
       * Represents the Selected Document Id
       */
      private String msSelectedDocId = "";


      /**
       * Represents the Selected Document Dept Code
       */
      private String msSelectedDocDeptCd = "";

      /**
       * Represents the Selected Document Version Number
       */
      private String msSelectedDocVersNo = "";

      /**
       * Attachment file response
       */
      private String  msAttchFile = null;

      /**
       * Indicates the comment transition is being executed.
       */
      private boolean mboolCommentTrans = false;

      /**
       * Indicates the global transition is being executed.
       */
      private boolean mboolGlobalTrans = false;

      /**
       * Indicates whether or not the comment page should be in insert mode at first.
       */
      private boolean mboolAddCmnt = false;

      /**
       * This is used for document signatures to determine if when the sign box comes up
       * if upon signing they should be re-directed to the comment signature page.
       */
      private boolean mboolCmntApprove = false;

      /**
       * Index value that is used to determine what document was selected to be opened
       */
      private int miIndex = 0;

      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code. To customize paint
      // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pMobileWorklist ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      }



//{{EVENT_CODE
//{{EVENT_T1WF_WRK_LST_QRY_beforeQuery
/**
 * This method is called before the query is performed on the
 * document catalog. This method is used to set up the where clause
 * to retrieve only those documents which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 08/07/02
 *                                        - Removed the super qualifier from the
 *                                        - call to modifyQuery()
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1WF_WRK_LST_QRY_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
   getWorkflowRole();
   getCurrUser();
   retrieveWorklists();

   if(msSrcPgNm.equals(AdvMobile.MOB_WL_SRCH_PG_NM) ||
      (getSourcePage() != null && getSourcePage().getName().equals(AdvMobile.MOB_WL_SRCH_PG_NM)))
   {
      setBrowseFields();
      setQryFields();
      msSrcPgNm = AdvMobile.MOB_WL_SRCH_PG_NM;
   }

   setCurrUser(mboolCurrUser);
   setCurrWorkList(msWorkflowRole);

   SearchRequest loFilters = new SearchRequest();

   modifyQuery( foQuery, foResultSet );

   boolean lboolFilter = false;

   if(!AMSStringUtil.strIsEmpty(msDocCd))
   {
      loFilters.add(" AND (" + msDocCd + ")");
      lboolFilter = true;
   }

   if(!AMSStringUtil.strIsEmpty(msAprvLvl))
   {
      loFilters.addParameter("WF_APRV_WRK_LST","APRV_LVL", msAprvLvl);
      lboolFilter = true;
   }

   if(!AMSStringUtil.strIsEmpty(msDocDeptCd))
   {
      loFilters.add(" AND " + msDocDeptCd);
      lboolFilter = true;
   }

   if(!AMSStringUtil.strIsEmpty(msDocId))
   {
      loFilters.add(" AND " + msDocId);
      lboolFilter = true;
   }

   if(!AMSStringUtil.strIsEmpty(msDocSbmtId))
   {
      loFilters.add(" AND " + msDocSbmtId);
      lboolFilter = true;
   }

   if(!AMSStringUtil.strIsEmpty(msEsclItem))
   {
      loFilters.addParameter("WF_APRV_WRK_LST","ESCL_ITEM", msEsclItem);
      lboolFilter = true;
   }

   if(lboolFilter)
   {
      foQuery.addFilter(loFilters);
   }
}
//END_EVENT_T1WF_WRK_LST_QRY_beforeQuery}}
//{{EVENT_pMobileWorklist_beforeGenerate
void pMobileWorklist_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line
   VSSession loSession = getParentApp().getSession();

   hideNavigations(T1WF_WRK_LST_QRY);

   if(getShowPDFPanelOnLoad() != 0)
   {
      HiddenElement loSignReq = (HiddenElement) getElementByName("SigReq");
      loSignReq.setValue("true");

      if(mboolCmntApprove)
      {
         HiddenElement loDocSigCmnt = (HiddenElement) getElementByName("DocSigCmnt");
         loDocSigCmnt.setValue("true");
      }
      else
      {
         HiddenElement loDocSigCmnt = (HiddenElement) getElementByName("DocSigCmnt");
         loDocSigCmnt.setValue("false");
      }
   }
   else
   {
      HiddenElement loSignReq = (HiddenElement) getElementByName("SigReq");
      loSignReq.setValue("false");
      HiddenElement loDocSigCmnt = (HiddenElement) getElementByName("DocSigCmnt");
      loDocSigCmnt.setValue("false");
   }

   HiddenElement loByPassApp = (HiddenElement) getElementByName("ByPassApp");
   loByPassApp.setValue(AMSPLSUtil.getApplParamValue("WF_BYPASS_COMMENTS_PG_ON_APPROVE", loSession));
   HiddenElement loByPassRej = (HiddenElement) getElementByName("ByPassRej");
   loByPassRej.setValue(AMSPLSUtil.getApplParamValue("WF_BYPASS_COMMENTS_PG_ON_REJECT", loSession));
   mboolCmntApprove = false;
}
//END_EVENT_pMobileWorklist_beforeGenerate}}
//{{EVENT_pMobileWorklist_beforeActionPerformed
void pMobileWorklist_beforeActionPerformed( ActionElement foAe, PageEvent foEvt, PLSRequest foPLSReq )
{
   //Write Event Code below this line
   String lsAmsAct = foPLSReq.getParameter("ams_action");
   String lsOpenDoc = foPLSReq.getParameter("openDoc");
   String lsRefresh = foPLSReq.getParameter("Refresh");
   String lsCmntApprove = foPLSReq.getParameter("CmntApprove");
   String lsCmntReject = foPLSReq.getParameter("CmntReject");

   if(AMSStringUtil.strEqual(lsCmntApprove,"Y") || AMSStringUtil.strEqual(lsCmntReject,"Y"))
   {
      setAddCmnt(true);
      mboolCmntApprove = true;
   }
   else
   {
      setAddCmnt(false);
   }

   String lsSubAction = foPLSReq.getParameter(AMSHyperlinkActionElement.SUB_ACTN_ATTRIB);
   VSSession loCurrSession = getParentApp().getSession();
   VSORBSession loCurrentSession = loCurrSession.getORBSession();
   Session loSession = null;

   try
   {
      loSession = ServerEnvironment.getServer().getExistingSession(loCurrentSession.getSessionID());
   }
   catch(Exception loEx)
   {
      raiseException("Unable to process your request.", SEVERITY_LEVEL_ERROR);
      return;
   }

   //clear errors when generating for mobile app each time.
   HiddenElement loErrors = (HiddenElement) getElementByName("Errors");
   loErrors.setValue("{}");

   if("home".equals(lsSubAction))
   {
      return;
   }  

   //refresh query
   if(!AMSStringUtil.strIsEmpty(lsRefresh))
   {
      refreshDataSource(T1WF_WRK_LST_QRY);
      return;
   }

   if (AMSStringUtil.strEqual(lsAmsAct, "ViewAttachmentFile"))
   {
      VSQuery     loQuery ;
      VSResultSet loResultSet = null;
      String lsUnid = foPLSReq.getParameter("UNID");

      if(AMSStringUtil.strIsEmpty(lsUnid))
      {
         raiseException("Unable to get the requested attachment.", SEVERITY_LEVEL_ERROR);
         return;
      }

      try
      {
         //make sure the requested attachment is actually for the selected document.
         loQuery = new VSQuery(loCurrSession, "IN_OBJ_ATT_DOC_REF", "OBJ_ATT_UNID = " +
               lsUnid, "");

         loResultSet = loQuery.execute() ;

         if (loResultSet != null)
         {
            loResultSet.last() ;

            if(loResultSet.getRowCount() == 1)
            {
               String lsDocId = loResultSet.first().getData("DOC_ID").getString();
               String lsDocCd = loResultSet.first().getData("DOC_CD").getString();
               String lsDocDeptCd = loResultSet.first().getData("DOC_DEPT_CD").getString();
               int liDocVersNo = loResultSet.first().getData("DOC_VERS_NO").getInt();
               int liVersNo = 0;
               loResultSet.close();

               if(!AMSStringUtil.strIsEmpty(msSelectedDocVersNo))
               {
                  liVersNo = Integer.parseInt(msSelectedDocVersNo);
               }

               if(!AMSStringUtil.strEqual(msSelectedDocId, lsDocId) ||
                  !AMSStringUtil.strEqual(msSelectedDocCd, lsDocCd) ||
                  !AMSStringUtil.strEqual(msSelectedDocDeptCd, lsDocDeptCd) ||
                  liDocVersNo > liVersNo)
               {
                  raiseException("Unable to get the requested attachment.", SEVERITY_LEVEL_ERROR);
                  return;
               }
            }
         }

         /*
          * If ECM attachment, set Session property "fetchFromECM"
          * This property is used by XDAForAttachmentIntegration.fetch()
          * to call Documentum Handler to retrieve record from ECM store.
          */
         if (AMSStringUtil.strInsensitiveEqual(AMSParams.msUseECMForAttachments, "true"))
         {
            try
            {
               loCurrSession.getORBSession().setProperty(ECM_MSG_TXT, "fetchFromECM");
            }
            catch (Exception loExc)
            {
               raiseException("Unable to set property for fetch",
                     SEVERITY_LEVEL_ERROR);
            }
         }

         loQuery = new VSQuery(loCurrSession, "IN_OBJ_ATT_STOR", "OBJ_ATT_UNID = " + lsUnid, "");

         // Fix for the download of binary types
         loQuery.setColumnProjectionLevel(DataConst.ALLTYPES);

         loResultSet = loQuery.execute();

         if(loResultSet != null)
         {
            loResultSet.last();

            if(loResultSet.getRowCount() == 1)
            {
               byte [] lbAttchData = loResultSet.first().getData("OBJ_ATT_DATA").getBytes();
               String lsFileNm = "";
               if (lbAttchData != null)
               {
                  try
                  {
                     loQuery = new VSQuery(loCurrSession, "IN_OBJ_ATT_CTLG", "OBJ_ATT_UNID = " + lsUnid, "");
                     loResultSet = loQuery.execute();

                     if (loResultSet != null)
                     {
                        loResultSet.last();

                        if(loResultSet.getRowCount() == 1)
                        {
                           lsFileNm = loResultSet.first().getData("OBJ_ATT_NM").getString();
                        }
                        else
                        {
                           raiseException("Attachment file not found.", SEVERITY_LEVEL_ERROR);
                           return;
                        }
                     }
                     else
                     {
                        raiseException("Attachment file not found.", SEVERITY_LEVEL_ERROR);
                        return;
                     }

                     msAttchFile = new String(lbAttchData, 0, lbAttchData.length, "ISO8859_1");
                  }
                  catch (UnsupportedEncodingException loExcep)
                  {
                     raiseException("Encountered an unsupported encoding exception : "
                        + loExcep.getMessage(), SEVERITY_LEVEL_ERROR );
                     msAttchFile = null;
                     foEvt.setCancel(true) ;
                     foEvt.setNewPage(this) ;
                     return;
                  }

                  setDownloadFileInfo( "application/download", lsFileNm, true);
               }

               if (msAttchFile == null)
               {
                  raiseException("Attachment file not found.", SEVERITY_LEVEL_ERROR);
               } /* end if (msAttchFile == null) */
            } /* end if (loResultSet.getRowCount() == 1) */
            else
            {
               raiseException("Unable to locate attachment record.", SEVERITY_LEVEL_ERROR);
            } /* end else */
            loResultSet.close();
         } /* end if ( loResultSet == null ) */
         else
         {
            raiseException( "Unable to locate attachment record.", SEVERITY_LEVEL_ERROR ) ;
         } /* end else */
      }
      catch(Exception loEx)
      {
         if(loResultSet != null)
         {
            loResultSet.close();
         }
      }

      foEvt.setCancel(true);
      foEvt.setNewPage(this);
      return;
   } /* end else if ( ae.getName().equals( "ViewAttachmentFile" ) ) */

   //if on document mode (not summary mode), revert view to summary mode on back
   if("mob_back".equals(lsSubAction) && !mboolSummaryMode)
   {
      msResponseOpenDoc = "";
      mboolSummaryMode = true;
      HiddenElement loPgVw = (HiddenElement) getElementByName("PageView");
      msPgVw = "WorklistSummary";
      loPgVw.setValue(msPgVw);
      foAe.setAction("301");
      foEvt.setCancel(true) ;
      foEvt.setNewPage(this);
      return;
   }
   else if("mob_back".equals(lsSubAction))
   {
      foAe.setAction("301");
      return;
   }
   else
   {
      if(!AMSStringUtil.strIsEmpty(lsAmsAct))
      {
         foAe.setAction(lsAmsAct);
      }
   }

   AMSTableElement loTableElement;
   VSRow loSelectedRow = null;
   loTableElement = (AMSTableElement) getElementByName("GridBody");
   VSResultSet loRs = T1WF_WRK_LST_QRY.getResultSet();

   if(loTableElement != null && loRs != null)
   {
      /*
       * logic required because the elements don't appear to be selected when done through mobile
       * app even with currency items in the request
       */
      int liBegin = T1WF_WRK_LST_QRY.blockStartIndex();
      int liEnd = T1WF_WRK_LST_QRY.blockEndIndex();
      Vector lvSelect = new Vector();
      boolean lboolSelectedRow = false;

      for(int liIndex = liBegin; liIndex <= liEnd; liIndex++)
      {
         VSRow loRow = loRs.getRowAt(liIndex, false);
         if(loRow != null)
         {
            String lsCurr = loTableElement.getCurrencyValue(loRow);
            if(!AMSStringUtil.strIsEmpty(foPLSReq.getParameter("TE1_" + lsCurr + "_chkcurrency")))
            {
               lvSelect.add(liIndex);
               lboolSelectedRow = true;
               loSelectedRow = loRs.getRowAt(liIndex);
               miIndex = liIndex;
            }
         }
      }

      if(lboolSelectedRow)
      {
         loTableElement.setSelectedRows(lvSelect);
      }
      else
      {
         //if no selected row found and we're in document mode, current row should be the selected row.
         if(!mboolSummaryMode)
         {
            loSelectedRow = T1WF_WRK_LST_QRY.getCurrentRow();
            lvSelect.add(loRs.cursorPosition());
            loTableElement.setSelectedRows(lvSelect);
         }
      }
   }

   /**
    * do not update the selected row value when going to the document page since doc may be
    * approved already and we don't want to lose the value of the previously selected document
    * which is now gone but still visible on the screen to the user.
    */
   if(loSelectedRow != null && (foAe == null || (foAe != null &&
      !AMSStringUtil.strEqual("btnT2pDOC_CMNT", foAe.getActionCommand()))))
   {
      msSelectedDocCd = loSelectedRow.getData("DOC_CD").getString();
      msSelectedDocId = loSelectedRow.getData("DOC_ID").getString();
      msSelectedDocDeptCd = loSelectedRow.getData("DOC_DEPT_CD").getString();
      msSelectedDocVersNo = loSelectedRow.getData("DOC_VERS_NO").getString();

      //make sure the current user is authorized to open the document before proceeding to.
      if(!AMSStringUtil.strIsEmpty(lsOpenDoc))
      {
         if(!isActionAuthorized(AMSCommonConstants.DOC_ACTN_OPEN))
         {
            return;
         }
      }
   }

   if(!mboolSummaryMode || (!AMSStringUtil.strIsEmpty(lsOpenDoc) && loSelectedRow != null))
   {
      try
      {
         mboolSummaryMode = false;
         HiddenElement loPgVw = (HiddenElement) getElementByName("PageView");
         msPgVw = "DocDetail";
         loPgVw.setValue(msPgVw);

         HashMap<String, String> loDocKeyMap = new HashMap<String, String>();
         loDocKeyMap.put("DOC_CD", msSelectedDocCd);
         loDocKeyMap.put("DOC_DEPT_CD", msSelectedDocDeptCd);
         loDocKeyMap.put("DOC_ID", msSelectedDocId);
         loDocKeyMap.put("DOC_VERS_NO", msSelectedDocVersNo);

         AdvJsonDocRenderer loDocRenderer = (AdvJsonDocRenderer)AdvDocViewUtil.getDocRendererFromPLS(loSession, loDocKeyMap, AdvIDocRenderer.TYPE_JSON);
         loDocRenderer.generate(loDocKeyMap, loSession);

         msResponseOpenDoc = loDocRenderer.getJSONString() + ")";
         mboolRespReset = true;
      }
      catch(Exception loEx)
      {
         loEx.printStackTrace();
         raiseException("Unable to open the selected document.", SEVERITY_LEVEL_ERROR);
         if(moLog.isErrorEnabled())
         {
            moLog.error("An exception was encountered while trying to open a document: ", loEx);
         }
         mboolSummaryMode = true;
         HiddenElement loPgVw = (HiddenElement) getElementByName("PageView");
         msPgVw = "WorklistSummary";
         loPgVw.setValue(msPgVw);
      }
   }
   
   if(AMSStringUtil.strEqual(lsAmsAct, Integer.toString(AMSHyperlinkActionElement.OPEN_PAGE)))
   {
      mboolGlobalTrans = true;
      try
      {
         VSPage loPageTrans = AdvMobile.getMobileGlobalTransition(getParentApp(), getSessionId(), foPLSReq);
         loPageTrans.setSourcePage(this);
         foEvt.setCancel(true) ;
         foEvt.setNewPage(loPageTrans);
      }
      catch(Exception loEx)
      {
         raiseException("Unable to navigate to the requested page.", SEVERITY_LEVEL_ERROR);
         foAe.setAction("");
         mboolGlobalTrans = false;
      }
   }    
}
//END_EVENT_pMobileWorklist_beforeActionPerformed}}
//{{EVENT_pMobileWorklist_afterActionPerformed
void pMobileWorklist_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line
   /*
    * need to verify the hidden elements after action to return any applicable errors encountered
    *during approve/reject actions.
    */
   if(!AMSStringUtil.strIsEmpty(msResponseOpenDoc))
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

      if(mboolRespReset)
      {
         msResponseOpenDoc = lsCallBack + "(" + lsbMetaData.toString() + ",\"Open_Document\":" + msResponseOpenDoc;
         mboolRespReset = false;
      }
   }
}
//END_EVENT_pMobileWorklist_afterActionPerformed}}
//{{EVENT_T2pDOC_CMNT_afterPageNavigation
void T2pDOC_CMNT_afterPageNavigation( PageNavigation nav )
{
   //Write Event Code below this line
   //proceed with transition if severity less than error.
   if(getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR)
   {
      mboolCommentTrans = true;
   }
}
//END_EVENT_T2pDOC_CMNT_afterPageNavigation}}

//END_EVENT_CODE}}

      public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1WF_WRK_LST_QRY.addDBListener(this);
	addPageListener(this);
	T2pDOC_CMNT.addPageNavigationListener(this);
//END_EVENT_ADD_LISTENERS}}
      }

//{{EVENT_ADAPTER_CODE

	public void afterPageNavigation( PageNavigation obj ){
		Object source = obj;
		if (source == T2pDOC_CMNT) {
			T2pDOC_CMNT_afterPageNavigation( obj );
		}
	}
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pMobileWorklist_afterActionPerformed( ae, preq );
		}
	}
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pMobileWorklist_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pMobileWorklist_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1WF_WRK_LST_QRY) {
			T1WF_WRK_LST_QRY_beforeQuery(query , resultset );
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
    * Overrode the method to return true so that on Document close all data on root DataSource
    * of this page is refreshed as opposed to only refreshing the current row. Note for this page
    * it is not possible to get handle to current row for refresh because the QueryObject
    * SELECT do not include the primay key columns of childmost DataObject.
    * @param foDocPage The document page being closed
    * @return boolean
    */
   public boolean refreshOnDocClose( AMSDocTabbedPage foDocPage )
   {
      return true;
   }//end of method

   /**
    * Get the workflow role from the source page if it's the page list page in the mobile app.
    * @return String the workflow role indicated on the page list page of the mobile app.
    */
   protected String getWorkflowRole()
   {
      if(getSourcePage() != null && (getSourcePage().getName()).equals(AdvMobile.MOB_PG_LST_PG_NM))
      {
         msWorkflowRole = ((pWFAPageList)getSourcePage()).getWorkflowRole();
      }

      return msWorkflowRole;
   }

   /**
    * Get the workflow role from the source page if it's the page list page in the mobile app.
    * @return boolean the workflow role indicated on the page list page of the mobile app.
    */
   protected boolean getCurrUser()
   {
      if(getSourcePage() != null && (getSourcePage().getName()).equals(AdvMobile.MOB_PG_LST_PG_NM))
      {
         mboolCurrUser = ((pWFAPageList)getSourcePage()).getCurrUser();
      }

      if(getSourcePage() != null && (getSourcePage().getName()).equals(AdvMobile.MOB_WL_SRCH_PG_NM))
      {
         mboolCurrUser = ((pMobileWorklistSearch)getSourcePage()).getCurrUser();
      }

      return mboolCurrUser;
   }

   /**
    * Override beforeGenerate to add logic after the AMSWorklistPage beforeGenerate is called.
    */
   public void beforeGenerate()
   {
      super.beforeGenerate();

      if(!getActionElement("WrklstApprove").isEnabled())
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFApprove");
         loActnDiv.setVisible(false);
         HiddenElement loDocApp = (HiddenElement) getElementByName("DocApprove");
         msDocApprove = "false";
         loDocApp.setValue(msDocApprove);
      }
      else
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFApprove");
         loActnDiv.setVisible(true);
         HiddenElement loDocApp = (HiddenElement) getElementByName("DocApprove");
         msDocApprove = "true";
         loDocApp.setValue(msDocApprove);
      }

      if(!getActionElement("WrklstReject").isEnabled())
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFReject");
         loActnDiv.setVisible(false);
         HiddenElement loDocRej = (HiddenElement) getElementByName("DocReject");
         msDocReject = "false";
         loDocRej.setValue(msDocReject);
      }
      else
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFReject");
         loActnDiv.setVisible(true);
         HiddenElement loDocRej = (HiddenElement) getElementByName("DocReject");
         msDocReject = "true";
         loDocRej.setValue(msDocReject);
      }

      if(!getActionElement("WrklstTakeTask").isEnabled())
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFTake");
         loActnDiv.setVisible(false);
      }
      else
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFTake");
         loActnDiv.setVisible(true);
      }

      if(!getActionElement("WrklstReturnTask").isEnabled())
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFReturn");
         loActnDiv.setVisible(false);
      }
      else
      {
         DivElement loActnDiv = (DivElement) getElementByName("WFReturn");
         loActnDiv.setVisible(true);
      }

      /*
       * disable the approve/reject actions in doc mode for documents not selected anymore since
       * means that the query has been re-executed which means that the approval/rejection was
       * successful. Don't want to leave the approve/reject actions on the document to avoid odd
       * errors trying to approve an approved document.
       */
      if(!mboolSummaryMode)
      {

         VSRow loRow = null;
         VSResultSet loRs = T1WF_WRK_LST_QRY.getResultSet();

         if(loRs != null)
         {
            loRs.getRowAt(miIndex);
         }

         if(T1WF_WRK_LST_QRY.getCurrentRow() == null)
         {
            T1WF_WRK_LST_QRY.first();
            miIndex = 0;
         }

         loRow = T1WF_WRK_LST_QRY.getCurrentRow();

         if(loRow != null)
         {
            String lsCurrRowDocCd = loRow.getData("DOC_CD").getString();
            String lsCurrRowDocId = loRow.getData("DOC_ID").getString();
            String lsCurrRowDocDeptCd = loRow.getData("DOC_DEPT_CD").getString();
            String lsCurrRowDocVersNo = loRow.getData("DOC_VERS_NO").getString();

            if(!AMSStringUtil.strEqual(lsCurrRowDocCd, msSelectedDocCd) ||
               !AMSStringUtil.strEqual(lsCurrRowDocId, msSelectedDocId) ||
               !AMSStringUtil.strEqual(lsCurrRowDocDeptCd, msSelectedDocDeptCd) ||
               !AMSStringUtil.strEqual(lsCurrRowDocVersNo, msSelectedDocVersNo))
            {
               HiddenElement loDocRej = (HiddenElement) getElementByName("DocReject");
               msDocReject = "false";
               loDocRej.setValue(msDocReject);
               HiddenElement loDocApp = (HiddenElement) getElementByName("DocApprove");
               msDocApprove = "false";
               loDocApp.setValue(msDocApprove);
               HiddenElement loDocAct = (HiddenElement) getElementByName("DocMore");
               msDocMore = "false";
               loDocAct.setValue(msDocMore);
            }
            else
            {
               HiddenElement loDocAct = (HiddenElement) getElementByName("DocMore");
               msDocMore = "true";
               loDocAct.setValue(msDocMore);
            }
         }
         else
         {
            HiddenElement loDocRej = (HiddenElement) getElementByName("DocReject");
            msDocReject = "false";
            loDocRej.setValue(msDocReject);
            HiddenElement loDocApp = (HiddenElement) getElementByName("DocApprove");
            msDocApprove = "false";
            loDocApp.setValue(msDocApprove);
            HiddenElement loDocAct = (HiddenElement) getElementByName("DocMore");
            msDocMore = "false";
            loDocAct.setValue(msDocMore);
         }
      }
   }

   /**
    * Hides the next/first/last/previous options for the particular record source
    * if the total number of rows are known and the total number of rows are less
    * than the number of rows per page. There was the thought that if virtual result
    * set is on, the total count would be negative 1. That is if there's 11 rows with
    * 10 max row count, the total wouldn't be 10. If that's found not to be the case
    * the conditional can be modified.
    * @param foDataSource Datasource passed to determine if grid navigations should be enabled.
    */
   private void hideNavigations(AMSDataSource foDataSource)
   {
      int liRowsPp = foDataSource.getNumRowsPerPage();
      int liTotalRows = foDataSource.getTotalRows();

      /*
       * hide if we know the number of total rows for the datasource and those total rows are less
       * than the Rows per page.
       */
      if(liTotalRows != -1 && liTotalRows <= liRowsPp)
      {
         DivElement loGridNav = (DivElement) getElementByName("GridActions");
         loGridNav.setVisible(false);
      }
      else
      {
         DivElement loGridNav = (DivElement) getElementByName("GridActions");
         loGridNav.setVisible(true);
      }
   }

   /**
    * setBrowseFields sets the browsing fields for the search items from the search page.
    */
   private void setBrowseFields()
   {
      pMobileWorklistSearch loMblWrkSrch = (pMobileWorklistSearch)getSourcePage();
      if(loMblWrkSrch != null)
      {
         msWorkflowRole = loMblWrkSrch.getWrkLst();
         msAprvLvl = loMblWrkSrch.getAprvLvl();
         msDocCd = loMblWrkSrch.getDocCd();
         msDocDeptCd = loMblWrkSrch.getDocDeptCd();
         msDocId = loMblWrkSrch.getDocId();
         msDocSbmtId = loMblWrkSrch.getDocSbmtId();
         msEsclItem = loMblWrkSrch.getEsclItem();
      }
   }

   /**
    * setQryFields sets the query elements on the page to the values found on the search page.
    */
   private void setQryFields()
   {
      T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1APRV_LVL", msAprvLvl);
      T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_CD", msDocCd);
      T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_DEPT_CD", msDocDeptCd);
      T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_ID", msDocId);
      T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1DOC_SBMT_ID", msDocSbmtId);
      T1WF_WRK_LST_QRY.setQBFDataForElement("txtT1ESCL_ITEM", msEsclItem);

      /*
       * this will format the SQL so that it properly considers "," to delimit multiple document
       * codes, like the picks do.
       */
      msDocCd = getScalarElement("txtT1DOC_CD").getQBFText(msDocCd);
      msDocId = getScalarElement("txtT1DOC_ID").getQBFText(msDocId);
      msDocDeptCd = getScalarElement("txtT1DOC_DEPT_CD").getQBFText(msDocDeptCd);
      msDocSbmtId = getScalarElement("txtT1DOC_SBMT_ID").getQBFText(msDocSbmtId);
   }

   public String doAction( PLSRequest foPLSReq )
   {
      String lsResponse;
      lsResponse = super.doAction( foPLSReq ) ;

      //don't blank Response String if performing comment transaction
      if(!AMSStringUtil.strIsEmpty(msResponseOpenDoc) && !mboolCommentTrans && !mboolGlobalTrans)
      {
         lsResponse = msResponseOpenDoc;
         msResponseOpenDoc = "";
      }
      else
      {
         /*
          * something wrong has happened because the document response isn't being written but the
          * page is still in document mode. This means that the mobile app isn't going to
          * understand the response. Revert the page back to the summary mode as the mobile app is
          * expected to do the same when it encounters this kind of issue.
          */
         if(!mboolCommentTrans && !mboolGlobalTrans && AMSStringUtil.strEqual("DocDetail", msPgVw))
         {
            mboolSummaryMode = true;
            HiddenElement loPgVw = (HiddenElement) getElementByName("PageView");
            msPgVw = "WorklistSummary";
            loPgVw.setValue(msPgVw);
         }
      }

      if(msAttchFile != null)
      {
         if(!AMSStringUtil.strIsEmpty(foPLSReq.getParameter("readyForDownload")))
         {
            lsResponse = msAttchFile;
         }

         msAttchFile = null ;
      } /* end if ( mboolReturnAttach ) */

      mboolCommentTrans = false;
      mboolGlobalTrans = false;
      return lsResponse ;
   } /* end doAction() */

   /**
    * Get the selected Document Code on this page.
    * @return String representing the document code selected for opening
    */
   protected String getDocCd()
   {
      return msSelectedDocCd;
   }

   /**
    * Get the selected Document ID on this page.
    * @return String representing the document ID selected for opening
    */
   protected String getDocId()
   {
      return msSelectedDocId;
   }

   /**
    * Get the selected Document Department Code on this page.
    * @return String representing the document department code selected for opening
    */
   protected String getDocDeptCd()
   {
      return msSelectedDocDeptCd;
   }

   /**
    * Get the selected Document Version Number on this page.
    * @return String representing the document version number selected for opening
    */
   protected String getDocVersNo()
   {
      return msSelectedDocVersNo;
   }

   /**
    * Get the return response on this page for the comment page to use when returning to this
    * page.
    * @return String representing the response String for this page.
    */
   protected String getReturnResponse()
   {
      return msResponseOpenDoc;
   }

   /**
    * Returns the current page view for this page
    */
   protected String getPgVw()
   {
      return msPgVw;
   }

   /**
    * Returns the current page reject status for this page
    * @return String representing the Reject Status of the document.
    */
   protected String getRejectStatus()
   {
      return msDocReject;
   }

   /**
    * Returns the current page approve status for this page
    * @return String representing the Approve Status of the document.
    */
   protected String getApproveStatus()
   {
      return msDocApprove;
   }

   /**
    * Returns the current page approve status for this page
    * @return String representing the More Status of the document.
    */
   protected String getMoreStatus()
   {
      return msDocMore;
   }

   /**
    * Returns the current page comment transition status.
    * @return boolean representing the comment transition value.
    */
   protected boolean getCmntTrans()
   {
      return mboolCommentTrans;
   }

   /**
    * Sets the current page comment transition status. This is allowed since the comment
    * transition can be executed from TA.
    * @param fboolCommentTrans represents the value to set the comment transition to.
    */
   protected void setCmntTrans(boolean fboolCommentTrans)
   {
      mboolCommentTrans = fboolCommentTrans;
   }

   /**
    * Verifies if the current user is authorized to perform the selected action
    *
    * @param fiAction action to verify the user has access to
    * IN_PAGES entry in order to determine the resource information.
    * @return true if user is authorized to perform the action
    */
   private boolean isActionAuthorized(int fiAction)
   {
      String lsUserId = "";
      boolean lboolActnAuth = false;
      String lsRsrcNm = "";
      VSSession loCurrSession = getParentApp().getSession();
      try
      {
         lsUserId = loCurrSession.getORBSession().getUserID();
         VSQuery loQuery = null;
         try
         {
            //Lookup Application Page Registration (IN_PAGES) to find out package and page name
            SearchRequest loSrchReq = new SearchRequest();
            loSrchReq.addParameter("IN_PAGES", "PAGE_CD", msSelectedDocCd);
            loQuery = new VSQuery(loCurrSession, "IN_PAGES", loSrchReq, new SearchRequest());
            VSRow loRow = loQuery.execute().first();
            if(loRow != null)
            {
               lsRsrcNm =  loRow.getData("APPL_NM").getString() + "." +
                   loRow.getData("DEST_PG").getString();
            }
         }
         finally
         {
            if(loQuery != null)
            {
               loQuery.close();
               loQuery = null;
            }
         }

         lboolActnAuth = AMSSecurity.actionAuthorized(lsUserId, lsRsrcNm, fiAction);
         if(!lboolActnAuth)
         {
            raiseException("User not Authorized to perform the selected action.", SEVERITY_LEVEL_ERROR);
         }
     }
     catch (Exception loEx)
     {
        raiseException("Exception occured while performing custom security check",
              SEVERITY_LEVEL_ERROR);
     }

     return lboolActnAuth;
  }

   /**
    * Sets the add comment indicator if either the approve or reject with comments option
    * is indicated. This should put the comment page into "popup" mode when first generated.
    * @param fboolAddCmnt indicates whether or not the add comment is in insert mode or not.
    */
   private void setAddCmnt(boolean fboolAddCmnt)
   {
      mboolAddCmnt = fboolAddCmnt;
   }

   /**
    * Gets the add comment indicator to determine if either the approve or reject with comments
    * option is indicated. This should put the comment page into "popup" mode when first generated.
    * @return boolean indicating whether or not the add comment is in insert mode or not.
    */
   public boolean getAddCmnt()
   {
      return mboolAddCmnt;
   }

 /**
   * Returns worklistTableElement.
   *
   * @return TableElement - Worklist Table Element
   */
   public TableElement getWorklistTableElement()
   {
      return getTableElement("GridBody");
   } /* end getWorklistTableElement() */
}