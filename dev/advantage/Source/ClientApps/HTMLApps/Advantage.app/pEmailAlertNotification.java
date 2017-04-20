//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.common.AMSUser;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import org.apache.commons.logging.Log;

/*
**  pEmailAlertNotification
*/

//{{FORM_CLASS_DECL
public class pEmailAlertNotification extends pEmailAlertNotificationBase

//END_FORM_CLASS_DECL}}
{
    // debugging log setup
    private static Log moLog = AMSLogger.getLog(pEmailAlertNotification.class, AMSLogConstants.FUNC_AREA_COMMON);
    
   //Initialized to true
   private boolean mboolIsFirstQueryEvent = true;

   
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pEmailAlertNotification ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }

   
//{{EVENT_CODE
//{{EVENT_pEmailAlertNotification_beforeActionPerformed
void pEmailAlertNotification_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      // Write Event Code below this line

      if (moLog.isDebugEnabled())
      {
         moLog.debug("Before ActionPerformed :" + ae.getName());
      }
      if (ae.getName().equalsIgnoreCase("changeUserAlerts"))
      {
         try
         {
             T1R_SC_USER_INFO.updateDataSource();
             T2R_SC_USER_DIR_INFO.updateDataSource();
         }
         catch (VSException loException)
         {
            String lsMessage = loException.getMessage();
            if (AMSStringUtil.strIsEmpty(lsMessage))
            {
               lsMessage = "Error while saving Email Alert settings.";
            }
            
            moLog.error(lsMessage);
            if (moLog.isDebugEnabled())
            {
               moLog.debug(loException);
            }

         } /* end catch( VSException loException ) */

      } /* end if ( ae.getName().equalsIgnoreCase( "changeUserAlerts" ) ) */
      else if (ae.getName().equalsIgnoreCase("cancel"))
      {
         AMSDynamicTransition loDynTran = null;
         VSPage loHomePage;

         loDynTran = new AMSDynamicTransition("HomePage", "", "Advantage");

         loHomePage = loDynTran.getVSPage(getParentApp(), getSessionId());

         evt.setNewPage(loHomePage);
         evt.setCancel(true);
      } /* end else if ( ae.getName().equalsIgnoreCase( "cancel" ) ) */
}
//END_EVENT_pEmailAlertNotification_beforeActionPerformed}}
//{{EVENT_T1R_SC_USER_INFO_beforeQuery
void T1R_SC_USER_INFO_beforeQuery(VSQuery query ,VSOutParam resultset )
   {
      // Write Event Code below this line
      // Write Event Code below this line
      if (mboolIsFirstQueryEvent)
      {
         VSMapSecurityInfo loSecInfo = null;
         String lsErrMsg = null;

         try
         {
            loSecInfo = (VSMapSecurityInfo) parentApp.getSession()
                  .getORBSession().getServerSecurityObject();
         }
         catch (RemoteException loRemExp)
         {
            raiseException("Unable to get Security Object",
                  AMSPage.SEVERITY_LEVEL_SEVERE);
            return;
         }
         SearchRequest loSrch = new SearchRequest();
         String lsWhereClause = "USER_ID "
               + AMSSQLUtil.getANSIQuotedStr(loSecInfo.getLogin(),
                     AMSSQLUtil.EQUALS_OPER);
         loSrch.add(lsWhereClause);
         query.addFilter(loSrch);
         /*
          * Note that first time mboolIsFirstQueryEvent is true and above block
          * will take care of modifying the query. Call setQueryInfo to modify
          * the query of the datasource permanently so that for non-first times(
          * attempts after first time) the modified query is used instead of
          * framing it on every beforeQuery. Side Note: For the first time
          * setQueryInfo is too late to modify the query and hence
          * query.addFilter method is used for first time.
          */
         T1R_SC_USER_INFO.setQueryInfo("R_SC_USER_INFO", lsWhereClause, "", "",
               false);

         /* Reset so that it this block is not executed next time */
         mboolIsFirstQueryEvent = false;
      } // end if (mboolIsFirstQueryEvent )

   }
//END_EVENT_T1R_SC_USER_INFO_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1R_SC_USER_INFO.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pEmailAlertNotification_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_SC_USER_INFO) {
			T1R_SC_USER_INFO_beforeQuery(query , resultset );
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