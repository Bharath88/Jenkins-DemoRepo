//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.CVL_FLOW_LOG_TYPImpl;
import java.util.Enumeration;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import advantage.AMSStringUtil;
import java.util.StringTokenizer;
import java.util.Hashtable;

/*
**  pIM_SRVC_LVL_LOG_Status
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_LVL_LOG_Status extends pIM_SRVC_LVL_LOG_StatusBase

//END_FORM_CLASS_DECL}}
{

   private Hashtable moCvlFlowTyp = new Hashtable(5);
   private boolean mboolInitialized = false;

   private String msCompSta = "%";
   private String msStartTm = "%";
   private String msEndTm = "%";
   private String msUserId = "%";
   private String msLastModDt = "%";

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_LVL_LOG_Status ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_SRVC_LVL_LOG_Status_beforeActionPerformed
void pIM_SRVC_LVL_LOG_Status_beforeActionPerformed( ActionElement foAe, PageEvent foPageEvent, PLSRequest foPLSReq )
{
   //Write Event Code below this line
   /*
    * MMO_BATCH comes from the page to indicate which BS Agent record to load.
    */
   String lsMMOBatch = foPLSReq.getParameter("MMO_BATCH");
   String lsActnName = "";

   if(!AMSStringUtil.strIsEmpty(lsMMOBatch))
   {
      PLSApp loParentApp    = getParentApp() ;
      String lsSessionID    = getSessionId() ;
      StringBuffer lsbWhereClauseBuf = new StringBuffer(128);

      lsbWhereClauseBuf.append("AGNT_ID = ");
      lsbWhereClauseBuf.append(lsMMOBatch);

      AMSDynamicTransition loDynTran = new AMSDynamicTransition("Job_Log_Entries",
            lsbWhereClauseBuf.toString(), "Reports_Sys_Admin_App");

      loDynTran.setSourcePage(this);
      VSPage loTargetPage = loDynTran.getVSPage(getParentApp(), getSessionId());
      loTargetPage.setSourcePage(this);
      foPageEvent.setNewPage(loTargetPage);
      foPageEvent.setCancel(true);
      return;
   }

   if(foAe != null)
   {
      lsActnName = foAe.getName();
   }

   if(AMSStringUtil.strEqual(lsActnName, "AMSBrowse"))
   {
      String lsValue = foPLSReq.getParameter("txtT1COMP_STA");

      if(AMSStringUtil.strIsEmpty(lsValue))
      {
         msCompSta = "%";
      }
      else
      {
         msCompSta = lsValue;
      }

      lsValue = foPLSReq.getParameter("txtT1STRT_TM");

      if(AMSStringUtil.strIsEmpty(lsValue))
      {
         msStartTm = "%";
      }
      else
      {
         msStartTm = lsValue;
      }

      lsValue = foPLSReq.getParameter("txtT1END_TM");

      if(AMSStringUtil.strIsEmpty(lsValue))
      {
         msEndTm = "%";
      }
      else
      {
         msEndTm = lsValue;
      }

      lsValue = foPLSReq.getParameter("txtT1USER_ID");

      if(AMSStringUtil.strIsEmpty(lsValue))
      {
         msUserId = "%";
      }
      else
      {
         msUserId = lsValue;
      }

      lsValue = foPLSReq.getParameter("txtT1LAST_MOD_DT");

      if(AMSStringUtil.strIsEmpty(lsValue))
      {
         msLastModDt = "%";
      }
      else
      {
         msLastModDt = lsValue;
      }
   }
}
//END_EVENT_pIM_SRVC_LVL_LOG_Status_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_LVL_LOG_Status_beforeActionPerformed( ae, evt, preq );
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
      super.afterPageInitialize();
   }

   /**
    * generate method removes "Historical" status from search
    * @return string html excluding the status from cvl.
    */
   public String generate()
   {
      populateCvlCompSta();
      Enumeration loEnum = T1IM_SRVC_LVL_LOG.getBlockOfRows();

      String lsHtml = super.generate();
      String lsTarget = "<!-- RENDER MEMO -->";
      while(loEnum.hasMoreElements())
      {
         VSRow loRow = (VSRow) loEnum.nextElement();

         String lsMMO = loRow.getData("MMO").getString();
         StringTokenizer loTokenizer = new StringTokenizer(lsMMO, ";");
         String lsRow = "";

         if(loTokenizer.countTokens() != 6)
         {
            //missing data, just make a blank line
            lsRow = "<td></td><td></td><td></td><td></td><td></td><td></td>";
         }
         else
         {
            String lsFlowSta = loTokenizer.nextToken();
            String lsStartTm = loTokenizer.nextToken();
            String lsEndTm = loTokenizer.nextToken();
            String lsUserId = loTokenizer.nextToken();
            String lsLastModDt = loTokenizer.nextToken();
            String lsAgntId = loTokenizer.nextToken();

            lsFlowSta = (String)moCvlFlowTyp.get(lsFlowSta);
            StringBuffer lsbRow = new StringBuffer(512);
            lsbRow.append("<td>")
                  .append(lsFlowSta)
                  .append("</td><td class=\"right\">")
                  .append(lsStartTm)
                  .append("</td><td class=\"right\">")
                  .append(lsEndTm)
                  .append("</td><td>")
                  .append(lsUserId)
                  .append("</td><td class=\"right\">")
                  .append(lsLastModDt)
                  .append("</td><td><a onclick=\"if (!UTILS_CheckForTransaction()){event.")
                  .append("returnValue = false;return false};\" vsnavigation=\"T3StartupPage\" ")
                  .append("title=\"View Report\" name=\"T3StartupPage\" id=\"")
                  .append("T3StartupPage\" vsaction=\"pagetransition\" href=\"")
                  .append("javascript:submitForm(document.pIM_SRVC_LVL_LOG_Status,'menu_action=menu_action")
                  .append("&amp;MMO_BATCH=")
                  .append(lsAgntId)
                  .append("','Display');\">Component Logs</a></td>");
            lsRow = lsbRow.toString();
         }

         lsHtml = lsHtml.replaceFirst(lsTarget, lsRow);
      }

      return lsHtml;
   }

   private void populateCvlCompSta()
   {
      VSResultSet loResultSet = null;
      if(mboolInitialized)
      {
         return;
      }

      try
      {
         //loCBElement.removeAllElements() ;
         VSQuery loQuery = new VSQuery( getParentApp().getSession(), "CVL_FLOW_STA", "", "CVL_FLOW_STA_SV");
         loResultSet = loQuery.execute();
         loResultSet.last();
         int liCBSize = loResultSet.getRowCount();
         moCvlFlowTyp.clear();
         for (int liIndex = 0; liIndex < liCBSize; liIndex++)
         {
            VSRow loRow = loResultSet.getRowAt(liIndex);
            if (loRow != null )
            {
               String lsStoredValue = loRow.getData("CVL_FLOW_STA_SV").getString() ;
               String lsDisplayValue = loRow.getData("CVL_FLOW_STA_DV").getString() ;

               moCvlFlowTyp.put(lsStoredValue, lsDisplayValue);
            }
         }

         mboolInitialized = true;
      }
      catch (RuntimeException loException )
      {
         raiseException( "Error populating the Component Status Status combo box", SEVERITY_LEVEL_ERROR);
         loException.printStackTrace();
         return;
      }
      finally
      {
         if (loResultSet != null)
         {
            loResultSet.close() ;
         }
      }
   }

   /**
    * Overrides a super class method.  Called before HTML is generated.
    */
   public void beforeGenerate()
   {
      super.beforeGenerate();
    /*if(!AMSStringUtil.strEqual(msCompSta, "%"))
      {
         ((ScalarElement)getElementByName("txtT1COMP_STA")).setValue(msCompSta);
      }
      else
      {
         ((ScalarElement)getElementByName("txtT1COMP_STA")).setValue("");
      }

      if(!AMSStringUtil.strEqual(msStartTm, "%"))
      {
         ((ScalarElement)getElementByName("txtT1STRT_TM")).setValue(msStartTm);
      }
      else
      {
         ((ScalarElement)getElementByName("txtT1STRT_TM")).setValue("");
      }

      if(!AMSStringUtil.strEqual(msEndTm, "%"))
      {
         ((ScalarElement)getElementByName("txtT1END_TM")).setValue(msEndTm);
      }
      else
      {
         ((ScalarElement)getElementByName("txtT1END_TM")).setValue("");
      }

      if(!AMSStringUtil.strEqual(msUserId, "%"))
      {
         ((ScalarElement)getElementByName("txtT1USER_ID")).setValue(msUserId);
      }
      else
      {
         ((ScalarElement)getElementByName("txtT1USER_ID")).setValue("");
      }

      if(!AMSStringUtil.strEqual(msLastModDt, "%"))
      {
         ((ScalarElement)getElementByName("txtT1LAST_MOD_DT")).setValue(msLastModDt);
      }
      else
      {
         ((ScalarElement)getElementByName("txtT1LAST_MOD_DT")).setValue("");
      }*/
   }
}