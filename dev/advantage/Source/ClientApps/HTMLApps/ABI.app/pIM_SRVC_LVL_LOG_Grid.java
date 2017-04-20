//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.Enumeration;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import advantage.AMSBatchUtil;
import advantage.AMSStringUtil;
import advantage.AMSUtil;
import advantage.CVL_FLOW_LOG_TYPImpl;
import adv.common.*;

/*
**  pIM_SRVC_LVL_LOG_Grid
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_LVL_LOG_Grid extends pIM_SRVC_LVL_LOG_GridBase

//END_FORM_CLASS_DECL}}
{
   private String msAttchFile = null;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_LVL_LOG_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_SRVC_LVL_LOG_Grid_beforeActionPerformed
void pIM_SRVC_LVL_LOG_Grid_beforeActionPerformed( ActionElement foAe, PageEvent foPageEvent, PLSRequest foPLSReq )
{
   AMSDynamicTransition loDynTran             = null;
   AdvantageJobMessage  loAdvantageJobMessage = null;
   ChildFlowMessage     loChildFlowMessage    = null;
   ErrorMessage         loErrorMessage        = null;
   File                 loFile                = null;
   FileMessage          loFileMessage         = null;
   PLSApp               loParentApp           = null;
   String               lsFileName            = null;
   String               lsParam               = null;
   String               lsSessionID           = null;
   String               lsWhereClause         = null;
   String[]             lsAttchFile           = null;
   TextMessage          loTextMessage         = null;
   VSPage               loTargetPage          = null;
   VSRow                loCurrentRow          = null;

   lsParam = foPLSReq.getParameter("MMO_REPORT");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      loParentApp   = getParentApp();
      lsSessionID   = getSessionId();
      lsWhereClause = new StringBuffer(32).append("AGNT_ID = ").append(lsParam).toString();
      loDynTran     = new AMSDynamicTransition("Reports_Display_Pg", lsWhereClause,
                      "Reports_Sys_Admin_App");
      loTargetPage  = loDynTran.getVSPage(loParentApp, lsSessionID);

      loDynTran.setSourcePage(this);
      loTargetPage.setSourcePage(this);
      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);
      return;
   }

   lsParam = foPLSReq.getParameter("MMO_BATCH");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      loParentApp   = getParentApp();
      lsSessionID   = getSessionId();
      lsWhereClause = new StringBuffer(32).append("AGNT_ID = ").append(lsParam).toString();
      loDynTran     = new AMSDynamicTransition("Job_Log_Entries", lsWhereClause,
                      "Reports_Sys_Admin_App");
      loTargetPage  = loDynTran.getVSPage(loParentApp, lsSessionID);

      loDynTran.setSourcePage(this);
      loTargetPage.setSourcePage(this);
      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);
      return;
   }

   lsParam = foPLSReq.getParameter("MMO_CHILD_FLOW");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      loParentApp   = getParentApp();
      lsSessionID   = getSessionId();
      lsWhereClause = new StringBuffer(32).append("FLOW_SEQ_NO = ").append(lsParam).toString();
      loDynTran     = new AMSDynamicTransition("pIM_SRVC_LVL_LOG_Grid", lsWhereClause, "ABI");
      loTargetPage  = loDynTran.getVSPage(loParentApp, lsSessionID);

      loDynTran.setSourcePage(this);
      loTargetPage.setSourcePage(this);
      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);
      return;
   }

   if (T1IM_SRVC_LVL_LOG == null)
   {
      return;
   }

   loCurrentRow = T1IM_SRVC_LVL_LOG.getCurrentRow();

   if (loCurrentRow == null)
   {
      return;
   }

   lsParam = foPLSReq.getParameter("MMO_RESOURCE");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      loParentApp   = getParentApp();
      lsSessionID   = getSessionId();
      lsWhereClause = new StringBuffer(32).append("SEQ_NO = ").append(lsParam).toString();
      loDynTran     = new AMSDynamicTransition("pIM_SRVC_FLOW_RSRC_Generic", lsWhereClause, "ABI");
      loTargetPage  = loDynTran.getVSPage(loParentApp, lsSessionID);

      loDynTran.setSourcePage(this);

      loTargetPage.setSourcePage(this);
      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);

      return;
   }

   lsParam = foPLSReq.getParameter("MMO_TEXT");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      lsFileName    = loCurrentRow.getData("MMO").getString();
      //loTextMessage = (TextMessage) RichMessageUtil.deserializeObject(lsFileName, LOG_TYPE.TEXT);
      //msAttchFile   = (String) loTextMessage.getValue();

      String[] lsTemp = lsFileName.split("\\|~\\|");
      if (lsTemp.length > 1 &&
          lsTemp[1] != null)
      {
         lsFileName = lsTemp[1];
      }
      msAttchFile = lsTemp[0];

      //setDownloadFileInfo("application/download", (String) loTextMessage.getDescription() + ".txt",
      //      true);
      setDownloadFileInfo("application/download", lsFileName + ".txt", true);
      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);

      return;
   }

   lsParam = foPLSReq.getParameter("MMO_FILE");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      lsFileName    = loCurrentRow.getData("MMO").getString();
      loFileMessage = (FileMessage) RichMessageUtil.deserializeObject(lsFileName, LOG_TYPE.FILE);
      lsFileName    = (String) loFileMessage.getFilePath();

      loFile = new File(lsFileName);

      if (!loFile.exists())
      {
         raiseException("File " + lsFileName + " does not exist.", SEVERITY_LEVEL_ERROR);

         return;
      }

      setDownloadFileInfo("application/download", AMSUtil.getFileNameFromPath(lsFileName), true);
      msAttchFile = getFileAsString(lsFileName);

      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);

      return;
   }

   lsParam = foPLSReq.getParameter("MMO_ERROR_MSG");
   if (!AMSStringUtil.strIsEmpty(lsParam))
   {
      lsFileName     = loCurrentRow.getData("MMO").getString();

      String[] lsTemp = lsFileName.split("\\|~\\|");

      loErrorMessage = (ErrorMessage) RichMessageUtil.deserializeObject(lsFileName,
                       LOG_TYPE.ERROR_MSG);
      lsFileName     = (String) loErrorMessage.getErrorFilePath();
      msAttchFile    = getFileAsString(lsFileName);

      if (lsTemp.length > 2 &&
          lsTemp[2] != null)
      {
         msAttchFile = lsTemp[2];
      }
      lsFileName = lsTemp[0];

      if (lsFileName.indexOf(".") == -1)
      {
         lsFileName = lsFileName + ".txt";
      }

      if (msAttchFile == null)
      {
         raiseException("File " + lsFileName + " does not exist.", SEVERITY_LEVEL_ERROR);

         foPageEvent.setNewPage(loTargetPage);
         foPageEvent.setCancel(true);

         return;
      }

      //setDownloadFileInfo("application/download", AMSUtil.getFileNameFromPath(lsFileName), true);
      setDownloadFileInfo("application/download", lsFileName, true);

      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);

      return;
   }
}
//END_EVENT_pIM_SRVC_LVL_LOG_Grid_beforeActionPerformed}}
//{{EVENT_T1IM_SRVC_LVL_LOG_beforeQuery
void T1IM_SRVC_LVL_LOG_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
   SearchRequest loSrchReq = new SearchRequest() ;

   String lsWhereClause = foQuery.getSQLWhereClause();
   StringBuffer lsbFilter = new StringBuffer(32);
   if (!AMSStringUtil.strIsEmpty(lsWhereClause))
   {
      lsbFilter.append(" AND ");
   }

   // Construct the Where Clause
   lsbFilter.append(" LOG_TYP <> ");
   lsbFilter.append(CVL_FLOW_LOG_TYPImpl.STATUS);

   // Add where clause to the Search request
   loSrchReq.add(lsbFilter.toString());
   foQuery.addFilter(loSrchReq);
}
//END_EVENT_T1IM_SRVC_LVL_LOG_beforeQuery}}
//{{EVENT_pIM_SRVC_LVL_LOG_Grid_beforeGenerate
void pIM_SRVC_LVL_LOG_Grid_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   filterComponent();
}
//END_EVENT_pIM_SRVC_LVL_LOG_Grid_beforeGenerate}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1IM_SRVC_LVL_LOG.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_LVL_LOG_Grid_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_LVL_LOG_Grid_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IM_SRVC_LVL_LOG) {
			T1IM_SRVC_LVL_LOG_beforeQuery(query , resultset );
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
    * generate method removes "Historical" status from search
    * @return string html excluding the status from cvl.
    */
   public String generate()
   {
      ChildFlowMessage    loChildFlowMessage    = null;
      Enumeration         loRows                = null;
      ErrorMessage        loErrorMessage        = null;
      FileMessage         loFileMessage         = null;
      FileResourceMessage loFileResourceMessage = null;
      String              lsDescription         = null;
      String              lsHtml                = null;
      String              lsLink                = null;
      String              lsMemo                = null;
      String              lsResourceID          = null;
      String              lsTarget              = null;
      TextMessage         loTextMessage         = null;
      VSRow               loRow                 = null;
      int                 liLogTyp              = 0;
      long                llResourceID          = 0;
      long                llSeqNo               = 0;

      String[] lsTemp = null;

      lsHtml   = super.generate();
      lsTarget = "<!-- RENDER MEMO -->";
      loRows   = T1IM_SRVC_LVL_LOG.getBlockOfRows();

      while (loRows.hasMoreElements())
      {
         loRow         = (VSRow) loRows.nextElement();
         liLogTyp      = loRow.getData("LOG_TYP").getInt();
         lsMemo        = loRow.getData("MMO").getString();
         lsDescription = "error";
         llResourceID  = 0;
         lsResourceID  = "";

         /*
          * If the log type is report and the current datafield is MMO, modify the value of
          * MMO to render the value of the field as a hyperlink.
          */
         switch(liLogTyp)
         {
            case CVL_FLOW_LOG_TYPImpl.TEXT:
               /*
               loTextMessage = (TextMessage) RichMessageUtil.deserializeObject(lsMemo,
                     LOG_TYPE.TEXT);

               if (loTextMessage != null)
               {
                  lsDescription = loTextMessage.getDescription();
               }*/

               lsDescription = lsMemo.split("!--")[0];

               lsLink = "<td><a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                     "returnValue = false};\" vsnavigation=\"T5StartupPage\" " +
                     "title=\"View Text\" name=\"T6StartupPage\" id=\"" +
                     "T5StartupPage\" vsaction=\"pagetransition\" href=\"" +
                     "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                     "&amp;MMO_TEXT=Text','_parent');UTILS_InitPage();\">" +
                     lsDescription + "</a></td>";
               break;

            case CVL_FLOW_LOG_TYPImpl.FILE:
               loFileMessage = (FileMessage) RichMessageUtil.deserializeObject(lsMemo,
                     LOG_TYPE.FILE);

               if (loFileMessage != null)
               {
                  lsDescription = loFileMessage.getDescription();
               }

               lsLink = "<td><a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                     "returnValue = false};\" vsnavigation=\"T6StartupPage\" " +
                     "title=\"View File\" name=\"T6StartupPage\" id=\"" +
                     "T6StartupPage\" vsaction=\"pagetransition\" href=\"" +
                     "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                     "&amp;MMO_FILE=File','_parent');UTILS_InitPage();\">" +
                     lsDescription + "</a></td>";
               break;

            case CVL_FLOW_LOG_TYPImpl.ERROR_MSG:
               /*
               loErrorMessage = (ErrorMessage) RichMessageUtil.deserializeObject(lsMemo,
                     LOG_TYPE.ERROR_MSG);

               if (loErrorMessage != null)
               {
                  lsDescription = loErrorMessage.getDescription();
               }*/

               lsTemp = lsMemo.split("\\|~\\|");

               if (lsTemp[0] != null)
               {
                  lsDescription = lsTemp[0];
               }

               if (lsTemp.length > 1 &&
                   lsTemp[1] != null)
               {
                   lsResourceID = lsTemp[1].trim();
               }

               if (lsTemp.length > 2 &&
                   lsTemp[2] != null)
               {
                   lsLink = lsTemp[2];
               }

               if (AMSStringUtil.strIsEmpty(lsResourceID))
               {
                  lsLink = "<td>" + lsDescription + ": " + lsLink + "</td>";
               }
               else
               {
                  lsLink = "<td>" + lsDescription + ": <a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                        "returnValue = false;return false};\" vsnavigation=\"T4StartupPage\" " +
                        "title=\"View Resource\" name=\"T4StartupPage\" id=\"" +
                        "T4StartupPage\" vsaction=\"pagetransition\" href=\"" +
                        "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                        "&amp;MMO_RESOURCE=" + lsResourceID +
                        "','Display');\">" + lsLink + "</a></td>";
               }

               break;

            case CVL_FLOW_LOG_TYPImpl.CHILD_FLOW:
               loChildFlowMessage = (ChildFlowMessage) RichMessageUtil.deserializeObject(lsMemo,
                     LOG_TYPE.CHILD_FLOW);

               if (loChildFlowMessage != null)
               {
                  lsDescription = loChildFlowMessage.getFlowName();
                  llSeqNo       = loChildFlowMessage.getSeqNo();
               }

               lsLink = "<td><a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                     "returnValue = false;return false};\" vsnavigation=\"T4StartupPage\" " +
                     "title=\"View Child Flow\" name=\"T4StartupPage\" id=\"" +
                     "T4StartupPage\" vsaction=\"pagetransition\" href=\"" +
                     "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                     "&amp;MMO_CHILD_FLOW=" + llSeqNo + "','Display');\">" +
                     lsDescription + "</a></td>";
               break;

            case CVL_FLOW_LOG_TYPImpl.RESOURCE:
               loFileResourceMessage = (FileResourceMessage) RichMessageUtil.deserializeObject(lsMemo,
                     LOG_TYPE.RESOURCE);

               if (loFileResourceMessage != null)
               {
                  lsDescription = loFileResourceMessage.getDescription();
                  llResourceID  = loFileResourceMessage.getResourceID();
               }

               lsLink = "<td><a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                     "returnValue = false;return false};\" vsnavigation=\"T4StartupPage\" " +
                     "title=\"View Resource\" name=\"T4StartupPage\" id=\"" +
                     "T4StartupPage\" vsaction=\"pagetransition\" href=\"" +
                     "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                     "&amp;MMO_RESOURCE=" + llResourceID +
                     "','Display');\">" + lsDescription + "</a></td>";
               break;

            case CVL_FLOW_LOG_TYPImpl.REPORT:
               lsLink = "<td><a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                     "returnValue = false;return false};\" vsnavigation=\"T3StartupPage\" " +
                     "title=\"View Report\" name=\"T3StartupPage\" id=\"" +
                     "T3StartupPage\" vsaction=\"pagetransition\" href=\"" +
                     "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                     "&amp;MMO_REPORT=" + lsMemo + "','Display');\">View Report</a></td>";
               break;

            case CVL_FLOW_LOG_TYPImpl.CHILD_JOB_ID:
               lsTemp = lsMemo.split("\\|~\\|");
               if (lsTemp.length > 1 &&
                   lsTemp[1] != null)
               {
                   lsDescription = lsTemp[1];
               }
               lsMemo = lsTemp[0];

               lsLink = "<td><a onclick=\"if (!UTILS_CheckForTransaction()){event." +
                     "returnValue = false;return false};\" vsnavigation=\"T4StartupPage\" " +
                     "title=\"View Batch\" name=\"T4StartupPage\" id=\"" +
                     "T4StartupPage\" vsaction=\"pagetransition\" href=\"" +
                     "javascript:submitForm(document.pIM_SRVC_LVL_LOG_Grid,'menu_action=menu_action" +
                     "&amp;MMO_BATCH=" + lsMemo + "','Display');\">" +
                     lsDescription + "</a></td>";
               break;

            case CVL_FLOW_LOG_TYPImpl.DATE:
            case CVL_FLOW_LOG_TYPImpl.DATE_TIME:
            case CVL_FLOW_LOG_TYPImpl.STATUS:
            default:
               lsLink = "<td class=\"memo\"><textarea class=\"advreadonly\" readonly" +
                     "=\"READONLY\" cols=\"20\" rows=\"2\" name=\"txtT1MMO\" vsds=\"" +
                     "T1IM_SRVC_LVL_LOG\" vsdf=\"MMO\" title=\"Memo\">" + lsMemo + "</textarea></td>";
         } // end switch(liLogTyp)

         lsHtml = lsHtml.replaceFirst(lsTarget, lsLink);
      } // end while (loRows.hasMoreElements())

      return lsHtml;
   }


   public String doAction(PLSRequest foPLSReq)
   {
      String lsResponse = null;

      //try
      //{
         lsResponse = super.doAction(foPLSReq);

         if (msAttchFile != null)
         {
            lsResponse  = msAttchFile;
            msAttchFile = null;
         }
      //}
      //catch (Exception foException)
      //{
      //   foException.printStackTrace();
      //}

      return lsResponse;
   } // end doAction()


   /**
    * Gets entire content of a file as a string.
    *
    * @param fsFileName - File name with full path
    * @return String - File contents
    */
   public String getFileAsString(String fsFileName)
   {
      BufferedReader loBufferedReader = null;
      StringBuffer   loFileContents   = null;
      String         lsLine           = null;
      String         lsNewLineChar    = null;

      if (fsFileName == null)
      {
         return null;
      }

      try
      {
         loBufferedReader = new BufferedReader(new FileReader(fsFileName));
         loFileContents   = new StringBuffer(1028);

         lsLine        = loBufferedReader.readLine();
         lsNewLineChar = "";

         while (lsLine != null)
         {
            loFileContents.append(lsLine).append(lsNewLineChar);

            lsLine        = loBufferedReader.readLine();
            lsNewLineChar = "\n";
         }
      }
      catch (FileNotFoundException foException)
      {
         return null;
      }
      catch (IOException foException)
      {
         return null;
      }

      return loFileContents.toString();
   } // end getFileAsString()


   /**
    * Filters Component drop down so that the only values shown are those that also appear in the
    * result rows.
    */
   public void filterComponent()
   {
      ComboBoxElement loComboBox          = null;
      int             liRowCount          = 0;
      long            llFlowID            = 0;
      String          lsLastComponentName = null;
      String          lsComponentName     = null;
      VSData          loData              = null;
      VSQuery         loQuery             = null;
      VSResultSet     loResultSet         = null;
      VSRow           loRow               = null;
      VSSession       loSession           = null;

      // Get Flow ID from source page
      loRow = getSourcePage().getRootDataSource().getCurrentRow();

      if (loRow != null)
      {
         loData = loRow.getData("SEQ_NO");

         if (loData == null)
         {
            // Give up here, the page was not called from Service Flows page
            return;
         }

         llFlowID = loData.getLong();
      }

      loSession  = getParentApp().getSession();
      loComboBox = (ComboBoxElement) getElementByName("txtT1COMP_NM");

      loComboBox.removeAllElements();
      loComboBox.addElement("", "");

      loQuery     = new VSQuery(loSession, "IM_SRVC_LVL_LOG",
                    "FLOW_SEQ_NO = " + llFlowID, "COMP_NM");
      loResultSet = loQuery.execute();

      loResultSet.last();

      liRowCount = loResultSet.getRowCount();

      for (int liIndex = 1; liIndex <= liRowCount; liIndex++)
      {
         loRow = loResultSet.getRowAt(liIndex);

         if (loRow == null)
         {
            continue;
         }

         lsComponentName = loRow.getData("COMP_NM").getString();

         if (!AMSStringUtil.strEqual(lsComponentName, lsLastComponentName))
         {
            loComboBox.addElement(lsComponentName, lsComponentName);
         }

         lsLastComponentName = lsComponentName;
      }

      loResultSet.close();
      loResultSet = null;
   }
}
