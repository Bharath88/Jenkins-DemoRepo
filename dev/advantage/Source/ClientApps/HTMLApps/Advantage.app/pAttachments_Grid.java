// {{IMPORT_STMTS
package advantage.Advantage;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;

import advantage.AMSStringUtil;

import com.versata.util.logging.*;
// END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSParams;
import java.rmi.RemoteException;
import java.sql.*;
import com.amsinc.gems.adv.common.AMSSQLUtil;

import org.apache.commons.logging.Log;

/*******************************************************************************
 * pAttachments_Grid*
 ******************************************************************************/

// {{FORM_CLASS_DECL
public class pAttachments_Grid extends pAttachments_GridBase

// END_FORM_CLASS_DECL}}
{
   private String msPriGrpID = null;

   private String msSecGrpID = null;

   private String msDocCat = null;

   private String msDocType = null;

   private String msDocCode = null;

   private String msDocDept = null;

   private String msDocID = null;

   private String msDocVersNo = null;

   private String msDocIntLnNoNm = null;

   private String msDocIntLnNo = null;

   private String msDocHdrNm = null;

   private String msDocCompNm = null;

   private String msTitle = null;

   private boolean mboolNoParent = false;

   private boolean mboolIsDocComp = false;

   private boolean mboolEditable = false;

   private boolean mboolRestore = false;

   private VSRow moHdrRow = null;

   private VSRow moCompRow = null;

   private boolean mboolRfrshDoc = false;

   private boolean mboolDelete = false;

   private boolean mboolRestoreAction = false;

   private String msAttchFile = null;

   private int[] miAttachmentTypes = null;

   private int[] miAttachmentTypeOptions = null;

   private String msCurrentTab = null;

   private VSPage moDocSrcPage = null;

   private boolean mboolSrcPage = false;
   
   private static Log moAMSLog = AMSLogger.getLog( pAttachments_Grid.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
   // {{FORM_CLASS_CTOR
   public pAttachments_Grid(PLSApp parentApp) throws VSException,
                                             java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
      setAllowHistory(false);
   }

   // {{EVENT_CODE
   // {{EVENT_pAttachments_Grid_beforeActionPerformed
   void pAttachments_Grid_beforeActionPerformed(ActionElement foActnElem,
         PageEvent foPageEvent, PLSRequest foPLSReq)
   {
      if (foActnElem.getName().equals("AddAttachment"))
      {
         AMSDynamicTransition loDynTran;
         pAddAttachment loAttchPage;

         loDynTran = new AMSDynamicTransition("pAddAttachment", "", "",
               "Advantage");
         loDynTran.setAddRecord(true);
         loDynTran.setSourcePage(this);
         loDynTran.setDependent(true);

         loAttchPage = (pAddAttachment) loDynTran.getVSPage(getParentApp(),
               getSessionId());
         loAttchPage.setPrimaryGroupID(msPriGrpID);
         loAttchPage.setSecondaryGroupID(msSecGrpID);
         loAttchPage.setDocCat(msDocCat);
         loAttchPage.setDocType(msDocType);
         loAttchPage.setDocCode(msDocCode);
         loAttchPage.setDocDept(msDocDept);
         loAttchPage.setDocID(msDocID);
         loAttchPage.setDocVersNo(msDocVersNo);
         loAttchPage.setDocHeaderName(msDocHdrNm);
         loAttchPage.setDocComponentName(msDocCompNm);
         loAttchPage.setIntLnNoName(msDocIntLnNoNm);
         loAttchPage.setIntLnNo(msDocIntLnNo);
         loAttchPage.setIsDocComp(mboolIsDocComp);
         loAttchPage.setTitle(msTitle);
         // Header row and component row need to be set in this page so that
         // these objects can be refreshed when an attachment is deleted from
         // a document component or header.

         loAttchPage.setHeaderRow(moHdrRow);
         loAttchPage.setCompRow(moCompRow);

         // Check for attachment type options
         if (miAttachmentTypeOptions != null)
         {
            loAttchPage.setAttachmentTypeOptions(miAttachmentTypeOptions);
         }

         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(loAttchPage);

         mboolRfrshDoc = true;
      } /* end if ( foActnElem.getName().equals( "AddAttachment" ) ) */
      else if (foActnElem.getName().equals("SearchAttachment"))
      {
         AMSDynamicTransition loDynTran;
         pContentSearch loContentSearch;

         loDynTran = new AMSDynamicTransition("pContentSearch", "", "",
               "Advantage");
         loDynTran.setSourcePage(this);
         loDynTran.setDependent(true);

         loContentSearch = (pContentSearch) loDynTran.getVSPage(getParentApp(),
               getSessionId());
         loContentSearch.setPrimaryGroupID(msPriGrpID);
         loContentSearch.setSecondaryGroupID(msSecGrpID);
         loContentSearch.setDocCat(msDocCat);
         loContentSearch.setDocType(msDocType);
         loContentSearch.setDocCode(msDocCode);
         loContentSearch.setDocDept(msDocDept);
         loContentSearch.setDocID(msDocID);
         loContentSearch.setDocVersNo(msDocVersNo);
         loContentSearch.setDocHeaderName(msDocHdrNm);
         loContentSearch.setDocComponentName(msDocCompNm);
         loContentSearch.setIntLnNoName(msDocIntLnNoNm);
         loContentSearch.setIntLnNo(msDocIntLnNo);
         loContentSearch.setIsDocComp(mboolIsDocComp);
         loContentSearch.setObjAttType( OBJ_ATT_TYPE_DOC_VIEW ) ;
         // Header row and component row need to be set in this page so that
         // these objects can be refreshed when an attachment is deleted from
         // a document component or header.

         loContentSearch.setHeaderRow(moHdrRow);
         loContentSearch.setCompRow(moCompRow);

         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(loContentSearch);

         mboolRfrshDoc = true;
      } /* end if ( foActnElem.getName().equals( "SearchAttachment" ) ) */
      else if (foActnElem.getName().equals("T1IN_OBJ_ATT_CTLGdelete"))
      {
         VSORBSession loSession = getParentApp().getSession().getORBSession();
         String[] loParams = new String[12];
         String lsMsgTxt;

         loParams[0] = OBJ_ATT_TYPE_DOC_VIEW;
         loParams[1] = msDocCat;
         loParams[2] = msDocType;
         loParams[3] = msDocCode;
         loParams[4] = msDocDept;
         loParams[5] = msDocID;
         loParams[6] = msDocVersNo;
         loParams[7] = msDocHdrNm;
         loParams[8] = mboolIsDocComp ? "Y" : "N";
         loParams[9] = msDocCompNm != null ? msDocCompNm : "";
         loParams[10] = msDocIntLnNoNm != null ? msDocIntLnNoNm : "";
         loParams[11] = msDocIntLnNo != null ? msDocIntLnNo : "";

         lsMsgTxt = AMSHyperlinkActionElement.createMessageText(loParams);
         try
         {
            loSession.setProperty(OBJ_ATT_MSG_TXT, lsMsgTxt);
            loSession.setProperty("CLIENT_INITIATED", "T");
         } /* end try */
         catch (RemoteException foExp)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

            raiseException("Unable to set message properties",
                  SEVERITY_LEVEL_SEVERE);
            return;
         } /* end catch( RemoteException foExp ) */

         mboolRfrshDoc = true;
         /*
          * Set mboolDelete as true if the attachments Primary State is not
          * Pending Deletion
          */
         if (T1IN_OBJ_ATT_CTLG.getCurrentRow().getData("OBJ_ATT_ST").getInt() != OBJ_ATT_ST_PEND_DEL)
         {
            mboolDelete = true;
         }
      } /* end else if ( foActnElem.getName().equals( "T1IN_OBJ_ATT_CTLGdelete" ) ) */
      else if (foActnElem.getName().equals("DownloadAttachment"))
      {
         VSRow loCurrRow;
         VSQuery loQuery;
         VSResultSet loResultSet;
         VSORBSession loSession = getParentApp().getSession().getORBSession();
         if (AMSStringUtil.strInsensitiveEqual(
               AMSParams.msUseECMForAttachments, "true"))
         {
            try
            {
               loSession.setProperty(ECM_MSG_TXT, "fetchFromECM");
            }
            catch (Exception ae)
            {
               raiseException("Unable to set property for fetch",
                     SEVERITY_LEVEL_ERROR);
            }
         }

         acceptData(foPLSReq);
         loCurrRow = T1IN_OBJ_ATT_CTLG.getCurrentRow();


         String lsAttachment = getAttachmentData(this, loCurrRow);

         if(getHighestSeverityLevel() <= SEVERITY_LEVEL_WARNING)
         {
            msAttchFile = lsAttachment;
         }


         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(this);
      } /* end else if ( foActnElem.getName().equals( "DownloadAttachment" ) ) */
      else if (foActnElem.getName().equals("ReturnToDoc"))
      {
         VSPage loSrcPage = getSourcePage();

         /*
          * Check to see if the document page has expired. If expired, get the
          * document instance and load it.
          */
         if (loSrcPage != null && !(((AMSPage) loSrcPage).isClosed()))
         {
            if (mboolRfrshDoc == true)
            {
               /*
                * Attachment addition or deletion on document may cause change
                * in data on Document header and its components. Hence re-query
                * the data on document page so that data on document is
                * refreshed on document header as well as on document components
                * and to avoid "Another User has updated the row..." error (note
                * this error was issued in absence of below executeQuery code
                * when a document is copied, and an attachment is uploaded to
                * the copied document header, and then a field value changed on
                * Vendor line, then clicking on Commodity or Accounting line.
                * Note no other action like Save or Validate is done by the User
                * in between these steps).
                */
               if (loSrcPage instanceof AMSDocTabbedPage)
               {
                  loSrcPage.getRootDataSource().executeQuery();
               }

            } /* end if ( mboolRfrshDoc == true ) */

         } /* end if ( loSrcPage != null && !( loSrcPage.isClosed() ) ) */
         else
         {
            VSPage loDocPage = null;

            // Get a reference to the document
            loDocPage = AMSHyperlinkActionElement.createDocInstance(msDocCode, // Doc
                                                                                 // Code
                  msDocDept, // Doc Dept Code
                  msDocID, // Doc Id
                  msDocVersNo, // Doc Vers No.
                  moDocSrcPage);

            if (loDocPage != null)
            {
               // set the regenerate page to the
               // document source page
               ((AMSDocTabbedPage) loDocPage).setCurrentTabName(msCurrentTab);
               ((AMSDocTabbedPage) loDocPage).setEditMode(mboolEditable);

               loSrcPage = loDocPage;
            } /* end if ( loDocPage != null ) */
            else
            {
               // Document source page is NULL, hence
               // the current page is being regenerated
               loSrcPage = this;
               raiseException("Unable to retrieve the document page.",
                     SEVERITY_LEVEL_ERROR);
            } /* end else */
         } /* end else */

         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(loSrcPage);
      } /* end else if ( foActnElem.getName().equals( "ReturnToDoc" ) ) */
      else if (foActnElem.getName().equals("ViewAttHistory"))
      {
         AMSDynamicTransition loDynTran;
         pObjAttHist loAttchHistPage;

         StringBuffer loBuffer = new StringBuffer(100);
         loBuffer.append("OBJ_ATT_PG_UNID = ");
         loBuffer.append(AMSSQLUtil.getANSIQuotedStr(msPriGrpID, true));

         if (msSecGrpID != null)
         {
            loBuffer.append(" AND OBJ_ATT_SG_UNID = ");
            loBuffer.append(msSecGrpID);
         }

         loDynTran = new AMSDynamicTransition("pObjAttHist", loBuffer
               .toString(), "", "Advantage");
         loDynTran.setSourcePage(this);
         loDynTran.setOrderByClause("OBJ_ATT_DT DESC");
         // The transition is made dependent so that the source document page
         // is not lost, since this is needed for other transitions from the
         // navigation bar such as click on document comments, history etc.
         loDynTran.setDependent(true);

         loAttchHistPage = (pObjAttHist) loDynTran.getVSPage(getParentApp(),
               getSessionId());

         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(loAttchHistPage);

      } /* end if ( foActnElem.getName().equals( "ViewAttHistory" ) ) */
      else if (foActnElem.getName().equals(ACTION_ATT_RESTORE))
      {
         VSRow loCurrRow;

         acceptData(foPLSReq);
         loCurrRow = T1IN_OBJ_ATT_CTLG.getCurrentRow();

         if (loCurrRow != null)
         {
            int liAttState = loCurrRow.getData(ATTR_OBJ_ATT_ST).getInt();
            if (liAttState == OBJ_ATT_ST_PEND_DEL)
            {
               VSORBSession loSession = getParentApp().getSession()
                     .getORBSession();
               String[] loParams = new String[13];
               String lsMsgTxt;

               loParams[0] = ACTION_ATT_RESTORE;
               loParams[1] = OBJ_ATT_TYPE_DOC_VIEW;
               loParams[2] = msDocCat;
               loParams[3] = msDocType;
               loParams[4] = msDocCode;
               loParams[5] = msDocDept;
               loParams[6] = msDocID;
               loParams[7] = msDocVersNo;
               loParams[8] = msDocHdrNm;
               loParams[9] = mboolIsDocComp ? "Y" : "N";
               loParams[10] = msDocCompNm != null ? msDocCompNm : "";
               loParams[11] = msDocIntLnNoNm != null ? msDocIntLnNoNm : "";
               loParams[12] = msDocIntLnNo != null ? msDocIntLnNo : "";

               lsMsgTxt = AMSHyperlinkActionElement.createMessageText(loParams);
               try
               {
                  loSession.setProperty(OBJ_ATT_MSG_TXT, lsMsgTxt);
                  loSession.setProperty("CLIENT_INITIATED", "T");
               } /* end try */
               catch (RemoteException foExp)
               {
                  // Add exception log to logger object
                  moAMSLog.error("Unexpected error encountered while processing. ", foExp);

                  raiseException("Unable to set message properties",
                        SEVERITY_LEVEL_SEVERE);
                  return;
               } /* end catch( RemoteException foExp ) */

               loCurrRow.getData(ATTR_OBJ_ATT_ST).setInt(OBJ_ATT_ST_ACTIVE);
               loCurrRow.getData(ATTR_OBJ_ATT_DEL_USID).setString(null);
               loCurrRow.getData(ATTR_OBJ_ATT_DEL_DT).setVSDate(null);
               loCurrRow.save();
               mboolRestoreAction = true;
            } /* if ( liAttState == OBJ_ATT_ST_PEND_DEL ) */
            else
            {
               raiseException(
                     "Only pending delete attachments can be restored.",
                     SEVERITY_LEVEL_WARNING);
            } /* end else */
         } /* end if ( loCurrRow != null ) */
         else
         {
            raiseException("No attachment record selected.",
                  SEVERITY_LEVEL_WARNING);
         } /* end else */

         mboolRfrshDoc = true;
         // foPageEvent.setCancel( true ) ;
         // foPageEvent.setNewPage( this ) ;
      }

   }

   // END_EVENT_pAttachments_Grid_beforeActionPerformed}}
   // DELETED_BEGIN
   // {{EVENT_T2pAddAttachment_beforePageNavigation

   // END_EVENT_T2pAddAttachment_beforePageNavigation}}
   // DELETED_END

   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      addPageListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         pAttachments_Grid_beforeActionPerformed(ae, evt, preq);
      }
   }

   // END_EVENT_ADAPTER_CODE}}

   public HTMLDocumentSpec getDocumentSpecification()
   {
      return getDefaultDocumentSpecification();
   }

   public String getFileName()
   {
      return getDefaultFileName();
   }

   public String getFileLocation()
   {
      return getPageTemplatePath();
   }

   public void afterPageInitialize()
   {
      super.afterPageInitialize();
      // Write code here for initializing your own control
      // or creating new control.

   }

   public void setPrimaryGroupID(String fsPriGrpID)
   {
      if ((fsPriGrpID == null) || (fsPriGrpID.trim().length() < 1))
      {
         msPriGrpID = null;
      } /* end if ( ( fsPriGrpID == null ) || ( fsPriGrpID.trim().length() < 1 ) ) */
      else
      {
         msPriGrpID = fsPriGrpID;
      } /* end else */
   } /* end setPrimaryGroupID() */

   public void setSecondaryGroupID(String fsSecGrpID)
   {
      if ((fsSecGrpID == null) || (fsSecGrpID.trim().length() < 1))
      {
         msSecGrpID = null;
      } /* end if ( ( fsSecGrpID == null ) || ( fsSecGrpID.trim().length() < 1 ) ) */
      else
      {
         msSecGrpID = fsSecGrpID;
      } /* end else */
   } /* end setSecondaryGroupID() */

   public void setDocCat(String fsDocCat)
   {
      if ((fsDocCat == null) || (fsDocCat.trim().length() < 1))
      {
         msDocCat = null;
      } /* end if ( ( fsDocCat == null ) || ( fsDocCat.trim().length() < 1 ) ) */
      else
      {
         msDocCat = fsDocCat;
      } /* end else */
   } /* end setDocCat() */

   public void setDocType(String fsDocType)
   {
      if ((fsDocType == null) || (fsDocType.trim().length() < 1))
      {
         msDocType = null;
      } /* end if ( ( fsDocType == null ) || ( fsDocType.trim().length() < 1 ) ) */
      else
      {
         msDocType = fsDocType;
      } /* end else */
   } /* end setDocType() */

   public void setDocCode(String fsDocCode)
   {
      if ((fsDocCode == null) || (fsDocCode.trim().length() < 1))
      {
         msDocCode = null;
      } /* end if ( ( fsDocCode == null ) || ( fsDocCode.trim().length() < 1 ) ) */
      else
      {
         msDocCode = fsDocCode;
      } /* end else */
   } /* end setDocCode() */

   public void setDocDept(String fsDocDept)
   {
      if ((fsDocDept == null) || (fsDocDept.trim().length() < 1))
      {
         msDocDept = null;
      } /* end if ( ( fsDocDept == null ) || ( fsDocDept.trim().length() < 1 ) ) */
      else
      {
         msDocDept = fsDocDept;
      } /* end else */
   } /* end setDocDept() */

   public void setDocID(String fsDocID)
   {
      if ((fsDocID == null) || (fsDocID.trim().length() < 1))
      {
         msDocID = null;
      } /* end if ( ( fsDocID == null ) || ( fsDocID.trim().length() < 1 ) ) */
      else
      {
         msDocID = fsDocID;
      } /* end else */
   } /* end setDocID() */

   public void setDocVersNo(String fsDocVersNo)
   {
      if ((fsDocVersNo == null) || (fsDocVersNo.trim().length() < 1))
      {
         msDocVersNo = null;
      } /*
          * end if ( ( fsDocVersNo == null ) || ( fsDocVersNo.trim().length() <
          * 1 ) )
          */
      else
      {
         msDocVersNo = fsDocVersNo;
      } /* end else */
   } /* end setDocVersNo() */

   public void setDocHeaderName(String fsDocHdrNm)
   {
      if ((fsDocHdrNm == null) || (fsDocHdrNm.trim().length() < 1))
      {
         msDocHdrNm = null;
      } /* end if ( ( fsDocHdrNm == null ) || ( fsDocHdrNm.trim().length() < 1 ) ) */
      else
      {
         msDocHdrNm = fsDocHdrNm;
      } /* end else */
   } /* end setDocHeaderName() */

   public void setDocComponentName(String fsDocCompNm)
   {
      if ((fsDocCompNm == null) || (fsDocCompNm.trim().length() < 1))
      {
         msDocCompNm = null;
      } /*
          * end if ( ( fsDocCompNm == null ) || ( fsDocCompNm.trim().length() <
          * 1 ) )
          */
      else
      {
         msDocCompNm = fsDocCompNm;
      } /* end else */
   } /* end setDocComponentName() */

   public void setIntLnNoName(String fsDocIntLnNoNm)
   {
      if ((fsDocIntLnNoNm == null) || (fsDocIntLnNoNm.trim().length() < 1))
      {
         msDocIntLnNoNm = null;
      } /*
          * end if ( ( fsDocIntLnNoNm == null ) || (
          * fsDocIntLnNoNm.trim().length() < 1 ) )
          */
      else
      {
         msDocIntLnNoNm = fsDocIntLnNoNm;
      } /* end else */
   } /* end setIntLnNoName() */

   public void setIntLnNo(String fsDocIntLnNo)
   {
      if ((fsDocIntLnNo == null) || (fsDocIntLnNo.trim().length() < 1))
      {
         msDocIntLnNo = null;
      } /*
          * end if ( ( fsDocIntLnNo == null ) || ( fsDocIntLnNo.trim().length() <
          * 1 ) )
          */
      else
      {
         msDocIntLnNo = fsDocIntLnNo;
      } /* end else */
   } /* end setIntLnNo() */

   public void setIsDocComp(boolean fboolIsDocComp)
   {
      mboolIsDocComp = fboolIsDocComp;
   } /* end setIsDocComp() */

   public void setTitle(String fsTitle)
   {
      msTitle = fsTitle;
   } /* end setTitle() */

   public void setEditable(boolean fboolEditable)
   {
      mboolEditable = fboolEditable;
   } /* end setEditable() */

   public void setRestore(boolean fboolRestore)
   {
      mboolRestore = fboolRestore;
   } /* end setRestore() */

   public void setHeaderRow(VSRow foHdrRow)
   {
      moHdrRow = foHdrRow;
   } /* end setHeaderRow() */

   public void setCompRow(VSRow foCompRow)
   {
      moCompRow = foCompRow;
   } /* end setCompRow() */

   public void setAttachmentTypes(int[] fiAttachmentTypes)
   {
      miAttachmentTypes = fiAttachmentTypes;
   } /* end setAttachmentTypes() */

   public void setAttachmentTypeOptions(int[] fiAttachmentTypeOptions)
   {
      miAttachmentTypeOptions = fiAttachmentTypeOptions;
   } /* end setAttachmentTypeOptions() */

   public String doAction(PLSRequest foPLSReq)
   {
      String lsResponse;

      lsResponse = super.doAction(foPLSReq);

      if (msAttchFile != null)
      {
         lsResponse = msAttchFile;
         msAttchFile = null;
      } /* end if ( mboolReturnAttach ) */
      return lsResponse;
   } /* end doAction() */


   /**
    * Overwriting the beforeGenerate method
    */
   public void beforeGenerate()
   {
      if(getRootDataSource().getCurrentRow() != null)
	  {
    	 // Set the value of hidden variable on the page if we are d/l an HTML attachment
	     String lsFileNm = getRootDataSource().getCurrentRow().getData( "OBJ_ATT_NM" ).getString();
	     if((lsFileNm.indexOf(".htm") != -1) || (lsFileNm.indexOf(".HTM") != -1) )
	     {
	        ((HiddenElement)getElementByName("DownloadType")).setValue("HTM");
	     }
	     else
	     {
	    	 ((HiddenElement)getElementByName("DownloadType")).setValue("");
	     }
      }
	  super.beforeGenerate() ;
   }

   public String generate()
   {
      ActionElement loAddAction = (ActionElement) getElementByName("AddAttachment");
      ActionElement loSearchAction = (ActionElement) getElementByName("SearchAttachment");
      ActionElement loDeleteAction = (ActionElement) getElementByName("T1IN_OBJ_ATT_CTLGdelete");
      ActionElement loRestoreAction = (ActionElement) getElementByName("RestoreAttachment");

      TextContentElement loTCE = (TextContentElement) getElementByName("PAGE_TITLE");
      VSPage loSrcPage = getSourcePage();

      if (loTCE != null)
      {
         if (msTitle != null)
         {
            loTCE.setValue(msTitle + " - ");
         } /* end if ( msTitle != null ) */
         else
         {
            loTCE.setValue("");
         } /* end else */
      } /* end if ( loTCE != null ) */

      if (mboolNoParent)
      {
         raiseException("No parent found for attachments.",
               SEVERITY_LEVEL_ERROR);
         appendOnloadString("alert(\'No parent found for attachments\');");
      } /* end if ( mboolNoParent ) */

      if (!mboolEditable)
      {
         loAddAction.setEnabled(false);
         loSearchAction.setEnabled(false);
         loDeleteAction.setEnabled(false);
         loRestoreAction.setEnabled(false);
      } /* end if ( !mboolEditable ) */
      else
      {
         loDeleteAction.setEnabled(true);
         loRestoreAction.setEnabled(mboolRestore);
         if (getHighestSeverityLevel() > SEVERITY_LEVEL_WARNING)
         {
            loAddAction.setEnabled(false);
            loSearchAction.setEnabled(false);

         } /* end if ( getHighestSeverityLevel() > SEVERITY_LEVEL_WARNING ) */
         else
         {
            loAddAction.setEnabled(true);
            if (AMSStringUtil.strInsensitiveEqual(
                  AMSParams.msUseECMForAttachments, "true"))
            {
               loSearchAction.setEnabled(true);
            }
            else
            {
               loSearchAction.setEnabled(false);
            }
         } /* end else */

      } /* end else */

      // Store the source page (i.e Doc Catalog Page)
      // and the current tab of the document
      if (!mboolSrcPage)
      {
         if (loSrcPage != null)
         {
            moDocSrcPage = loSrcPage.getSourcePage();
            msCurrentTab = ((AMSDocTabbedPage) loSrcPage).getCurrentTabName();
         } /* end if ( loSrcPage != null ) */

         mboolSrcPage = true;
      } /* end if ( !mboolSrcPage ) */

      if (mboolRestoreAction)
      {
         mboolRestoreAction = false;

         /*
          * This method needs to be added here because after restoring an
          * attachment, the secondary navigator needs to be refreshed since the
          * attachment counts need to be updated.
          */

         appendOnloadString("UTILS_LoadSecondaryNavigator()");
         ((AMSDocTabbedPage) loSrcPage)
               .updateTabInformationForAttachmentCount();

      }
      else if (mboolDelete)
      {
         DataSource loDataSrc = getRootDataSource();
         loDataSrc.executeQuery();
         mboolDelete = false;

         /*
          * If an attachment is deleted from the document component then the
          * header and component needs to be refreshed so that the counts can be
          * updated. This is needed so that the attachments image on the
          * component line displays correctly after an attachment is deleted or
          * added
          */

         if (loSrcPage != null && !(((AMSPage) loSrcPage).isClosed()))
         {
            if (moHdrRow != null)
            {
               moHdrRow.refresh(true);
            } /* end if ( moHdrRow != null ) */
            if (moCompRow != null)
            {
               moCompRow.refresh(true);
            } /* end if ( moCompRow != null ) */

            /*
             * This method needs to be added here because after deleting an
             * attachment, the secondary navigator needs to be refreshed since
             * the attachment counts need to be updated.
             */

            appendOnloadString("UTILS_LoadSecondaryNavigator()");
            ((AMSDocTabbedPage) loSrcPage)
                  .updateTabInformationForAttachmentCount();

         } /* end if ( loSrcPage != null && !( loSrcPage.isClosed() ) ) */
      }
      return super.generate();
   } /* end generate() */

  /**
   * Returns the attachment data as a string object for the current row
   */
   public static String getAttachmentData(AMSPage foPage, VSRow foCurrRow)
   {
      VSQuery     loQuery ;
      VSResultSet loResultSet ;
      String lsAttachFile = null;
      if ( foCurrRow != null )
      {
         loQuery = new VSQuery( foPage.getParentApp().getSession(), "IN_OBJ_ATT_STOR",
               "OBJ_ATT_UNID=" + foCurrRow.getData( "OBJ_ATT_UNID" ).getLong(), "" ) ;

         // Fix for the download of binary types
         loQuery.setColumnProjectionLevel( DataConst.ALLTYPES ) ;

         loResultSet = loQuery.execute() ;

         if ( loResultSet != null )
         {
            loResultSet.last() ;

            if ( loResultSet.getRowCount() == 1 )
            {
               byte [] lbAttchData =
                  loResultSet.first().getData( "OBJ_ATT_DATA" ).getBytes();

               //Verify that the attachment isn't null and its under the maximum attachment Size
               if(lbAttchData != null && lbAttchData.length > AMSParams.mlMaxDownloadSize
                  && AMSParams.mlMaxDownloadSize != -1)
               {
                  AMSPage.raiseExceptionOnPage( foPage, "Attachment file exceeded maximum download size.", SEVERITY_LEVEL_ERROR ) ;
               }
               else if (lbAttchData != null && (AMSParams.mlMaxAttachmentSize == -1 ||
                       lbAttchData.length <= AMSParams.mlMaxAttachmentSize) )
               {
                  try
                  {
                     lsAttachFile = new String(lbAttchData, 0, lbAttchData.length, "ISO8859_1");
                  }
                  catch (UnsupportedEncodingException loExcep)
                  {
                     AMSPage.raiseExceptionOnPage(foPage, "Encountered an unsupported encoding exception : "
                           + loExcep.getMessage(), SEVERITY_LEVEL_ERROR );
                  }
                  foPage.setDownloadFileInfo( "application/download",
                        foCurrRow.getData( "OBJ_ATT_NM" ).getString(), true ) ;
               }

               if ( lsAttachFile == null )
               {
                  AMSPage.raiseExceptionOnPage(foPage, "Attachment file not found.", SEVERITY_LEVEL_ERROR ) ;
               } /* end if ( msAttchFile == null ) */
            } /* end if ( loResultSet.getRowCount() == 1 ) */
            else
            {
               AMSPage.raiseExceptionOnPage( foPage, "Unable to locate attachment record.", SEVERITY_LEVEL_ERROR ) ;
            } /* end else */
            loResultSet.close() ;
         } /* end if ( loResultSet == null ) */
         else
         {
            AMSPage.raiseExceptionOnPage( foPage, "Unable to locate attachment record.", SEVERITY_LEVEL_ERROR ) ;
         } /* end else */
      } /* end if ( loCurrRow != null ) */
      else
      {
         AMSPage.raiseExceptionOnPage( foPage, "No attachment record selected.", SEVERITY_LEVEL_ERROR ) ;
      } /* end else */

      return lsAttachFile;
   }
}
