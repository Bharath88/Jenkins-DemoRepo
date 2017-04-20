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
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.vfc.html.* ;

/*
**  pIM_SRVC
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC extends pIM_SRVCBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolFirstLoad = true;

   /*
    * User's last action
    */
   private boolean mboolSave = false;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_SRVC_beforeGenerate
void pIM_SRVC_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   TextFieldElement loTextFieldElement = null;

   // Set length of Service Template from 20 to 48.
   loTextFieldElement = (TextFieldElement) getElementByName("txtT1SRVC_TMPL");
   if (loTextFieldElement != null)
   {
      loTextFieldElement.setSize(48);
   }

   // Set length of Parameter Value from 20 to 48.
   loTextFieldElement = (TextFieldElement) getElementByName("txtT2PARM_VL");
   if (loTextFieldElement != null)
   {
      loTextFieldElement.setSize(48);
   }

   if (mboolSave)
   {
      refreshDataSource(T1IM_SRVC);
      mboolSave = false;
   }
}
//END_EVENT_pIM_SRVC_beforeGenerate}}
//{{EVENT_pIM_SRVC_beforeActionPerformed
void pIM_SRVC_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   /*
    * Refresh T1IM_SRVC if a T2IM_SRVC_PARM row was saved (for Parameter Name
    * 'abi_next_service_id')
    */
   Enumeration loRows              = null;
   String      lsActionElementName = ae.getName();
   String      lsParmNm            = null;
   VSRow       loRow               = null;

   if (AMSStringUtil.strEqual(lsActionElementName, "T1IM_SRVCSaveAll"))
   {
      loRows = T2IM_SRVC_PARM.getBlockOfRows();

      while (loRows.hasMoreElements())
      {
         loRow = (VSRow) loRows.nextElement();

         if (loRow.modified())
         {
            lsParmNm = loRow.getData("PARM_NM").getString();

            if (AMSStringUtil.strEqual(lsParmNm, "abi_next_service_id"))
            {
               mboolSave = true;
            }
         }
      }
   }
}
//END_EVENT_pIM_SRVC_beforeActionPerformed}}
//{{EVENT_pIM_SRVC_afterActionPerformed
void pIM_SRVC_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
}
//END_EVENT_pIM_SRVC_afterActionPerformed}}
//{{EVENT_T1IM_SRVC_beforeQuery
void T1IM_SRVC_beforeQuery(VSQuery foQuery, VSOutParam foResultset )
{
   SearchRequest loSearchRequest = null;

   // Sort by Service Name when page is opened.
   if (mboolFirstLoad)
   {
      loSearchRequest = new SearchRequest();
      loSearchRequest.add("FLOW_NM, FLOW_ID");

      foQuery.replaceSortingCriteria(loSearchRequest);

      mboolFirstLoad = false;
   }
}
//END_EVENT_T1IM_SRVC_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1IM_SRVC.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_afterActionPerformed( ae, preq );
		}
	}
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IM_SRVC) {
			T1IM_SRVC_beforeQuery(query , resultset );
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



   public String scheduleFlow(AMSHyperlinkActionElement foActnElem,
         PLSRequest foRequest)
   {
      VSPage               loSrcPage;
      pScheduleFlow        loScheduleFlow;
      AMSDynamicTransition loDynTran ;
      String               lsHTML ;

      /*
       * Any additional where clause will be added in the beforeQuery() event
       * only.  This will optimize the filter (starts with indexed columns)
       */
      loSrcPage = foActnElem.getPage();
      loDynTran = new AMSDynamicTransition("pScheduleFlow" ,null, "ABI");

      loDynTran.setSourcePage(loSrcPage);

      loScheduleFlow = (pScheduleFlow) loDynTran.getVSPage(loSrcPage.getParentApp(),
            loSrcPage.getSessionId());

      lsHTML = loScheduleFlow.generate();
      foActnElem.getPage().getParentApp().getPageExpireAlg().pageNavigatedTo(loScheduleFlow);

      return lsHTML;
   }
}