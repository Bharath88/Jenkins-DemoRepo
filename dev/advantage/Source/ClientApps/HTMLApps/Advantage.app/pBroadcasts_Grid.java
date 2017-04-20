//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.util.* ;
import com.amsinc.gems.adv.client.dbitem.* ;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import org.apache.commons.logging.Log;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AdvSecurityAuditUtil;
import advantage.*;
/*
**  pBroadcasts_Grid*/

//{{FORM_CLASS_DECL
public class pBroadcasts_Grid extends pBroadcasts_GridBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   private static Log moLog = AMSLogger.getLog(pBroadcasts_Grid.class,
         AMSLogConstants.FUNC_AREA_COMMON);
   // BGN ADVHR00039669
   private boolean mboolFirstLoad = true ;
   // END ADVHR00039669
   
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pBroadcasts_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setMenuName( "Grid" ) ;
   }



//{{EVENT_CODE
//{{EVENT_T1IN_ALERTS_afterQuery
void T1IN_ALERTS_afterQuery( VSResultSet foRS )
{
   VSSession loSession         = getParentApp().getSession() ;
   String    lsUserID          = loSession.getLogin() ;
   Date      loReadBroadcastDt = new Date( System.currentTimeMillis() ) ;
   VSORBSession loORBSession = loSession.getORBSession();
   try
   {
      /* Update to Last Broadcast read date on IN_USER_PROFILE entry
         should bypass security checks */
      AdvSecurityAuditUtil.turnOnSecBypassOneTime( loORBSession );
      AMSUserProfileMethods.updateReadBroadcastDt( loSession, lsUserID,
            loReadBroadcastDt ) ;
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
void T1IN_ALERTS_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
{
   SearchRequest loSrchReq   = new SearchRequest() ;
   SearchRequest loOrderBy = new SearchRequest() ;
   VSSession     loSession   = getParentApp().getSession();
   VSDate        loCurrentDt = new VSDate();
   String        lsCurrentDt = getAMSDate(loCurrentDt, DATE_YEAR_MONTH_DAY, loSession );

   String lsWhereClause = foQuery.getSQLWhereClause();
   StringBuffer lsbFilter = new StringBuffer(32);
   if (lsWhereClause != null && lsWhereClause.trim().length() > 0)
   {
      lsbFilter.append(" AND ");
   }

   // Construct the Where Clause
   lsbFilter.append(" RECIP_TYP ");
   lsbFilter.append(AMSSQLUtil.getANSIQuotedStr( CVL_RECIP_TYPImpl.RECIP_TYPE_ALL,
         AMSSQLUtil.EQUALS_OPER ));
   lsbFilter.append(" AND EFBGN_DT <= " );
   lsbFilter.append( lsCurrentDt );
   lsbFilter.append(" AND EFEND_DT >= ");
   lsbFilter.append( lsCurrentDt);
   // Add where clause to the Search request
   loSrchReq.add( lsbFilter.toString());
   foQuery.addFilter( loSrchReq ) ;
   // BGN ADVHR00039669
   if(mboolFirstLoad)
   {   
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

   public void afterPageInitialize() {
      super.afterPageInitialize();
      //Write code here for initializing your own control
      //or creating new control.

   }


}