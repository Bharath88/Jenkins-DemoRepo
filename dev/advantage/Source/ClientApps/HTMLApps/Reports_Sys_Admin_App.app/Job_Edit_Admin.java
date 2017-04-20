//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import com.amsinc.gems.adv.client.batch.AMSPLSBatchUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;

import java.rmi.RemoteException;






/*
**  Job_Edit_Admin*/

//{{FORM_CLASS_DECL
public class Job_Edit_Admin extends Job_Edit_AdminBase

//END_FORM_CLASS_DECL}}
implements AMSBatchConstants

{
        // Declarations for instance variables used in the form
      private long mlCurrentJobID;

        // This is the constructor for the generated form. This also constructs
        // all the controls on the form. Do not alter this code. To customize paint
        // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Job_Edit_Admin ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
        }



	//{{EVENT_CODE
	//{{EVENT_T3User_AgentParm_Grid_beforePageNavigation
void T3User_AgentParm_Grid_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   mlCurrentJobID = getRootDataSource().getCurrentRow().getData("AGNT_ID").getLong();
}
//END_EVENT_T3User_AgentParm_Grid_beforePageNavigation}}
//{{EVENT_T5Chain_Job_list_beforePageNavigation
void T5Chain_Job_list_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
	//Write Event Code below this line

	VSRow loVSRow = T1BS_AGENT.getCurrentRow();
	long llAgentId = loVSRow.getData("AGNT_ID").getLong();
	String lsAgentId = Long.toString(llAgentId);
	if(!AMSStringUtil.strIsEmpty(lsAgentId))
	{
		nav.setDevWhere(" ID " + AMSSQLUtil.getANSIQuotedStr
			(lsAgentId , AMSSQLUtil.EQUALS_OPER));
	}

}
//END_EVENT_T5Chain_Job_list_beforePageNavigation}}

	//END_EVENT_CODE}}

        public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T3User_AgentParm_Grid.addPageNavigationListener(this);
	T5Chain_Job_list.addPageNavigationListener(this);
	//END_EVENT_ADD_LISTENERS}}
        }

	//{{EVENT_ADAPTER_CODE
	
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T3User_AgentParm_Grid) {
			T3User_AgentParm_Grid_beforePageNavigation( obj, cancel, newPage );
		}
	
		if (source == T5Chain_Job_list) {
			T5Chain_Job_list_beforePageNavigation( obj, cancel, newPage );
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
    * Returns the current job ID
    */
   public long getJobID()
   {
      return mlCurrentJobID;
   }

   /**
    * Override method from VSPage
    */
   public String generate()
   {

      VSORBSession loCurrentSession = getParentApp().getSession()
            .getORBSession();


/* Only Report Item Type will be able to see the public viawable flag
       */
      DivElement loPublicFlField =(DivElement)getElementByName("PUBLIC_FL_FIELD");
      DivElement loPublicFlLabel =(DivElement)getElementByName("PUBLIC_FL_LABEL");
      DivElement loEditParamsNonChain = (DivElement) getElementByName("EditParamsNonChain");
      DivElement loEditParamsForChain = (DivElement) getElementByName("EditParamsForChain");
      if (AMSPLSBatchUtil.isReportItemType(loCurrentSession))
	{
         if (loPublicFlField !=null)










         {
            loPublicFlField.setVisible(true);
         }
         if (loPublicFlLabel !=null)
         {
            loPublicFlLabel.setVisible(true);
         }
      }
      else
      {
         if (loPublicFlField !=null)
         {
            loPublicFlField.setVisible(false);
         }
         if (loPublicFlLabel!=null)
         {
            loPublicFlLabel.setVisible(false);
         }
      }

      if (AMSPLSBatchUtil.isChainItemType(loCurrentSession))
      {
         if (loEditParamsForChain != null)
         {
            loEditParamsForChain.setVisible(true);
         }
         if (loEditParamsNonChain != null)
         {
            loEditParamsNonChain.setVisible(false);
         }
      }
      else
      {
         if (loEditParamsForChain != null)
         {
            loEditParamsForChain.setVisible(false);
         }
         if (loEditParamsNonChain != null)
         {
            loEditParamsNonChain.setVisible(true);
         }
      }

      return super.generate();






















   }/* end of generate */




}