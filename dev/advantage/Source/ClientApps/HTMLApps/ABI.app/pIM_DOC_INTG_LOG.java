//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import java.rmi.RemoteException;

/*
**  pIM_DOC_INTG_LOG
*/

//{{FORM_CLASS_DECL
public class pIM_DOC_INTG_LOG extends pIM_DOC_INTG_LOGBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolFirstLoad = true;

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pIM_DOC_INTG_LOG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_pIM_DOC_INTG_LOG_beforeActionPerformed
void pIM_DOC_INTG_LOG_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   boolean      lboolAuth    = false;
   int          liAction     = 0;
   String       lsAction     = null;
   String       lsName       = null;
   String       lsResourceID = null;
   String       lsUserID     = null;
   VSORBSession loSession    = null;
   VSRow        loCurrentRow = null;

   // Security check for "Integration Resources" link.
   lsName = ae.getName();

   if (AMSStringUtil.strEqual(lsName, "btnT3pIM_SRVC_FLOW_RSRC_Generic"))
   {
      liAction = AMSDocAppConstants.DOC_SUB_ACTN_IM_VIEW_INTG_RSRC;
      lsAction = "View Integration Resources";
   }
   else
   {
      liAction = 0;
   }

   if (liAction > 0)
   {
      // Authorization check for Schedule action on Resource ID (Service ID)
      loSession    = getParentApp().getSession().getORBSession();
      loCurrentRow = getRootDataSource().getCurrentRow();

      if (loCurrentRow == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Resource ID - for Integration Manager, use Service ID.
      lsResourceID = loCurrentRow.getData("FLOW_ID").getString();

      if (lsResourceID == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Get User ID.
      try
      {
         lsUserID = loSession.getUserID();
      }
      catch (RemoteException foException)
      {
         raiseException("Unable to get User ID from session.  " +
               "Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      try
      {
         lboolAuth = AMSSecurity.actionAuthorized(lsUserID, lsResourceID, liAction);
      }
      catch (AMSSecurityException foException)
      {
         raiseException("Cannot access resource for this service (Resource ID " + lsResourceID +
               ").  Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      if (!lboolAuth)
      {
         raiseException("User is not authorized to perform the " + lsAction + " action for this " +
               "service (Resource ID " + lsResourceID +").", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }
   } // end if (liAction > 0)
}
//END_EVENT_pIM_DOC_INTG_LOG_beforeActionPerformed}}
//{{EVENT_T1V_IM_DOC_INTG_LOG_beforeQuery
void T1V_IM_DOC_INTG_LOG_beforeQuery(VSQuery foQuery ,VSOutParam foResultset )
{
   SearchRequest loSearchRequest = null;

   // Sort by most recent Last Modification Date in descending order when page is opened.
   if (mboolFirstLoad)
   {
      loSearchRequest = new SearchRequest();
      loSearchRequest.add("LAST_MOD_DT DESC");

      foQuery.replaceSortingCriteria(loSearchRequest);

      mboolFirstLoad = false;
   }
}
//END_EVENT_T1V_IM_DOC_INTG_LOG_beforeQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	T1V_IM_DOC_INTG_LOG.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_DOC_INTG_LOG_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1V_IM_DOC_INTG_LOG) {
			T1V_IM_DOC_INTG_LOG_beforeQuery(query , resultset );
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


}