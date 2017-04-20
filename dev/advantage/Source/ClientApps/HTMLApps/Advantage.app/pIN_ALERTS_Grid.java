//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.Date;
import com.amsinc.gems.adv.client.dbitem.AMSUserProfileMethods;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import org.apache.commons.logging.Log;
import com.amsinc.gems.adv.common.AdvSecurityAuditUtil;
import advantage.*;

/*
**  pIN_ALERTS_Grid
*/

//{{FORM_CLASS_DECL
public class pIN_ALERTS_Grid extends pIN_ALERTS_GridBase

//END_FORM_CLASS_DECL}}
{
    // debugging log setup
    private static Log moLog = AMSLogger.getLog(pIN_ALERTS_Grid.class, AMSLogConstants.FUNC_AREA_COMMON);
    // BGN ADVHR00039669
    private boolean mboolFirstLoad = true;
    // END ADVHR00039669

       // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pIN_ALERTS_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setMenuName( "Grid" ) ;
}



//{{EVENT_CODE
//{{EVENT_T1IN_ALERTS_afterQuery
void T1IN_ALERTS_afterQuery(VSResultSet rs )
{
   //Get Session
   VSSession loVSSession = getParentApp().getSession() ;
   //Get user id
   String lsUserID = loVSSession.getLogin();
   //Get current system timestamp
   Date loReadChDt = new Date(System.currentTimeMillis());

   VSORBSession loORBSession = loVSSession.getORBSession();
   try
   {
      /* Update to Last Alert read date on IN_USER_PROFILE entry
         should bypass security checks */
      AdvSecurityAuditUtil.turnOnSecBypassOneTime( loORBSession );
      AMSUserProfileMethods.updateReadChgDt(loVSSession,lsUserID,loReadChDt);
      AdvSecurityAuditUtil.turnOffSecBypassOneTime( loORBSession );
   }//end try
   catch( Exception loExcp )
   {
      moLog.error(loExcp);
      raiseException("Error occurred during updating User profile",
            SEVERITY_LEVEL_ERROR);
   }
   finally
   {
      if( loORBSession == null )
      {
         loORBSession = getParentApp().getSession().getORBSession();
      }
      /* Reset Session property related to security check bypass if it
         is still set on */
      if( !AdvSecurityAuditUtil.resetSecBypassOneTimeIfSet( loORBSession ) )
      {
         moLog.error("Error occurred during resetting value related to "
               + "AdvSecurityAuditUtil");
         raiseException("Error occurred during resetting values",
               SEVERITY_LEVEL_ERROR);
      }
   }//end finally
}
//END_EVENT_T1IN_ALERTS_afterQuery}}
//{{EVENT_T1IN_ALERTS_beforeQuery





     void T1IN_ALERTS_beforeQuery( VSQuery foQuery,VSOutParam foResultSet )
     {
        // debugging log
        if (moLog.isDebugEnabled())
        {
           moLog.debug("T1IN_ALERTS_beforeQuery()");
           moLog.debug("\t login id = " + getParentApp().getSession().getLogin() );
        }

        String lsWhereClause = foQuery.getSQLWhereClause();
        StringBuffer lsbFilter = new StringBuffer(32);
        if (lsWhereClause != null && lsWhereClause.trim().length() > 0)
        {
           lsbFilter.append(" AND ");
        }

        SearchRequest loSrchReq = new SearchRequest() ;
        // ADVHR00005709 BEGIN

        VSSession     loSession   = getParentApp().getSession();
        VSDate        loCurrentDt = new VSDate();
        String        lsCurrentDt = getAMSDate(loCurrentDt, DATE_YEAR_MONTH_DAY, loSession );

        // Construct the Where Clause
        lsbFilter.append(" RECIP_TYP ");
        lsbFilter.append(AMSSQLUtil.getANSIQuotedStr( CVL_RECIP_TYPImpl.RECIP_TYPE_USR,
              AMSSQLUtil.EQUALS_OPER ));
        lsbFilter.append(" AND USER_ID ");
        lsbFilter.append(AMSSQLUtil.getANSIQuotedStr( loSession.getLogin() ,
              AMSSQLUtil.EQUALS_OPER));
        lsbFilter.append(" AND EFBGN_DT <= ");
        lsbFilter.append( lsCurrentDt );
        lsbFilter.append(" AND EFEND_DT >= ");
        lsbFilter.append(lsCurrentDt);
        // Add where clause to the Search request
        loSrchReq.add( lsbFilter.toString() );

        // Add filter and replace the sort criteria
        foQuery.addFilter( loSrchReq ) ;
        // BGN ADVHR00039669
        if(mboolFirstLoad)
        {
           SearchRequest loOrderBy = new SearchRequest();
           loOrderBy.add("ALRT_PRTY ASC, ALRT_DT DESC");
            foQuery.replaceSortingCriteria( loOrderBy ) ;
           mboolFirstLoad = false;
        }
        // END ADVHR00039669
    }


//END_EVENT_T1IN_ALERTS_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_ALERTS.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_ALERTS) {
			T1IN_ALERTS_beforeQuery(query , resultset );
		}
	}
	public void afterQuery( DataSource obj, VSResultSet rs ){
		Object source = obj;
		if (source == T1IN_ALERTS) {
			T1IN_ALERTS_afterQuery(rs );
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

   public void afterPageInitialize()
   {
      //Set target frame to display
      getParentApp().setCurrentFrameName(DISPLAY_FRAME_NAME);
      super.afterPageInitialize();
   }

}