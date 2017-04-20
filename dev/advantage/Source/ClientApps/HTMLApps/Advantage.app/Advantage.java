//{{APP_IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.html.common.*;
import versata.common.*;
import versata.common.vstrace.*;
import versata.vls.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.versata.util.logging.*;
import com.amsinc.gems.adv.common.AMSSecurityObject ;
//END_APP_IMPORT_STMTS}}
import org.apache.commons.logging.*;
import versata.vfc.*;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.client.dbitem.AMSUserProfileMethods;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.tracing.*;
import com.amsinc.gems.adv.vfc.html.AMSPage;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.common.AdvSecurityAuditUtil;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSSecurityObject;

//{{APP_CLASS_DECL
public class Advantage extends AdvantageBase
//END_APP_CLASS_DECL}}

{

   /**
    * The name of the alerts broadcast time interval parameter
    * as defined in the application control page
    */
   public static final String ALERT_BROADCAST_CHECK_INTERVAL = "ACTIVE_ALERT_INTERVAL" ;

   private boolean mboolTraceInitialized = false;
   private boolean mboolTraceBrowseActn = false;

   /** variable holding date/time when new broadcast message was last checked */
   private Date moLastMsgCheckDt = null;

   /** Previous RD_CHG_DT from IN_USER_PROFILE
    * will be used by new message page to fire same where
    * clause when displaying new messages to the user
    */
   VSDate moPreviousAlertReadDate = null;

   /** Previous Alert Read Date, will be used by new message page to fire same where
    * clause when displaying new messages to the user
    */
   VSDate moPreviousBroadcastReadDate = null;

   /** Flag to indicate if new alerts are present*/
   private boolean mboolAlertExists = false;

   /** Flag to indicate if new alerts are present*/
   private boolean mboolBroadcastExists = false;

   /** Time interval for checking new alert and broadcast messages*/
   private static int miMessageInterval = -1;

    /** This is the logger object for the class */
    private static Log moLog = AMSLogger.getLog( Advantage.class,
        AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;


    // Advantage Class Constructor
//{{APP_CLASS_CTOR
public Advantage ()
//END_APP_CLASS_CTOR}}

    {
       super();
    }

   public synchronized PLSResponse doAction(PLSRequest preq)
   {
      //START PERF TRACING CODE
      AMSPerfTimer loTimer = null;
      int liEndEvent       = -1;

      if(AMSPerfTraceManager.TRACING_ON && loTimer == null)
      {
         String lsSessionId = getSessionIDForTracing();
         loTimer = AMSPerfTraceManager.getSessionTimer(lsSessionId);
         liEndEvent = -1; // fired if save or browse action being traced

         /*
          * perf tracing for browse actions begins in this
          * method
          */
         if ( !mboolTraceInitialized )
         {
            mboolTraceBrowseActn =
               AMSPerfTraceManager.isBeingTraced(AMSPerfTraceConstants.BROWSE);

            mboolTraceInitialized = true;
         }

      }

      if(loTimer != null)
      {
         loTimer.logEvent(AMSPerfTraceConstants.ACTN_BEGIN);

         /*
          * If browse being traced - determine from request if the current
          * request corresponds to AMSBrowse
          */
         if (mboolTraceBrowseActn )
         {
            String lsParamValue = preq.getParameter("AMSBrowse");
            String lsIdValue = preq.getParameter("ams_action");
            if ( (lsParamValue !=null && lsParamValue.equals("AMSBrowse") ) ||
                 (lsIdValue != null && lsIdValue.equals("72") ) )
            {
               loTimer.setActionName(AMSPerfTraceConstants.BROWSE);
               loTimer.setUserId(getSession().getLogin());
               loTimer.logEvent(AMSPerfTraceConstants.EVT_PLS_INIT_BEGIN);
               liEndEvent =AMSPerfTraceConstants.EVT_PLS_INIT_END;

            }
         }


      } //if(loTimer != null)

      //END PERF TRACING CODE

	//Check for new messages
	String lsTargetFrame = preq.getParameter("frame_name");

      if (lsTargetFrame != null && (lsTargetFrame.equals("Display") || lsTargetFrame.equals("BatDisplay")
         || lsTargetFrame.equals("ConfigDisplay")))
      {
        /*
         * Check for new Alert and Broadcast only when Restricted Single Sign on from MSS to HRM is false.
         * If user does Single Sign on from MSS to HRM the user Last message check date and broadcast\alert flags should not be updated.
         * This is done to display broadcast and alert popup in HRM.
         */
        VSORBSession loORBSession = getSession().getORBSession();
        try
        {
            AMSSecurityObject loSecurityObj = (AMSSecurityObject) loORBSession.getServerSecurityObject();
            if(!loSecurityObj.getRestrictedSSO())
            {
               checkForNewAlertsAndBroadcasts();
            }
        }
        catch(java.rmi.RemoteException foException)
        {
            moLog.error( "Error during restrictive SSO " , foException ) ;

        }

      }
      //END check for new messages code

      // this try ensures that the timer code in finally is called
      // in the finally block
      try
   {


//{{APP_DOACTION
   IVSTrace tr = null;  long begTime = 0;
   if ( VSTrace.IS_ON ){
   	tr = VSTrace.get();	
     	begTime = tr.beg(logger);
     	tr.set(VST_SESSION_ID, getSessionIDForTracing()).set(VST_CATEGORY, VST_PLS_ACTION).set(VST_ACTION_NAME,"Advantage.doAction");
}
    PLSResponse response = new PLSResponse();
	VSPage p = null;
	try
	{
		p = getPageFromList(preq.getParameter("page_id"));
		m_currentReq = preq;
		p.doAction(preq, response);
	}
	catch (Throwable ex)
	{
		m_currentReq = null;
		
		Util.logWarning(logger, ex);
		
		response.responseValue = "An unexpected error has occurred: " + ex.getMessage();
		if (p != null)
		   response.metaContentType = p.getContentType();
		else
		   response.metaContentType = "text/html; charset=utf-8";
	}
	
    if ( tr != null ) tr.end( begTime );

    return response;

//END_APP_DOACTION}}

       } //try
       finally
       {
          if (loTimer !=null)
          {
              if (liEndEvent != -1)
              {
                 // browse action is being traced
                 loTimer.logEvent(liEndEvent);
                 VSPage loCurrPage = getCurrentPage();
                 if (loCurrPage  !=null)
                 {
                    loTimer.set("APP NAME", loCurrPage.getAppName() );
                    loTimer.set("PAGE NAME", loCurrPage.getName() );
                 }
              }

             loTimer.logEvent(AMSPerfTraceConstants.ACTN_END);

          } // if (loTimer !=null)

       }// finally

    } /* doAction() */

   public PLSResponse start( String sessionID, VSSession session )
   {

	//{{APP_START
	    VSPage p = null;
	    PLSResponse response;
	    String s = "";
	    
	    boolean lboolRestrictedSSOMode = false;
	
	    try
	    {
	       if ( session.getLogin() == null )
	       {
	          Object loSecObj = session.getORBSession().getServerSecurityObject() ;
	
	          if ( loSecObj instanceof VSMapSecurityInfo )
	          {
	             session.setLogin( ((VSMapSecurityInfo)loSecObj).getLogin() ) ;
	             session.setPassword( ((VSMapSecurityInfo)loSecObj).getPassword() ) ;
	          } /* end if ( loSecObj instanceof VSMapSecurityInfo ) */
	          
	          //Checking for the Restricted SSO flag
	          if ( loSecObj instanceof AMSSecurityObject )
		  	          {
		  	             lboolRestrictedSSOMode = ((AMSSecurityObject)loSecObj).getRestrictedSSO();
		          }
	          
	          
	          
	       } /* end if ( session.getLogin() == null ) */
	    }
	    catch ( java.rmi.RemoteException loRemExp )
	    {
	       loRemExp.printStackTrace();
	       response = new PLSResponse();
	       response.responseValue = "<html>Unable to get Security Object</html>";
	
	       response.metaContentType = "text/html; charset=utf-8";
	       return response;
	    }
	
	    response = super.start( sessionID, session );
	
	    try
	    {
	
	        String startPage = null;
	        if (getPackageName() != null && getPackageName().length() != 0)
		        startPage = getPackageName() + "." + getStartPageName();
	        else
		        startPage = getStartPageName();
	        PageNavigation StartPageNav = new PageNavigation(null, startPage);
	        StartPageNav.setParentApp(this);
	        //If Restricted SSO is true, we need to switch the page to Framset_SSO
	        if(!lboolRestrictedSSOMode)
	        {
	           
	           StartPageNav.setTargetFramePageName(getPackageName() + "." + "FramesetPage");
	        }
	        else
	        {
	           
	           StartPageNav.setTargetFramePageName(getPackageName() + "." + "FramesetPage_SSO");
	        }	
	        	
	        
	        StartPageNav.setTargetName("Startup");
	        StartPageNav.setName("StartPageNav");
		    p = StartPageNav.startPage();
		    p.setSessionId(sessionID);
		    s = p.generate();
		    response.responseValue = s;
		    response.metaContentType = p.getContentType();
	
	    }
	    catch (Exception ex)
	    {
		    ex.printStackTrace();
		    response.responseValue = "<html> " + ex.toString() + "</html>";
		    response.metaContentType = p.getContentType();
		    return response;
	    }
	    if (response.responseValue.length() == 0)
	    {
		    response.responseValue = "<html> Unexpected Error </html>";
		    response.metaContentType = p.getContentType();
	    }
	    return (response);
	
	//END_APP_START}}
   } /* end start() */



    public String getStartPageName()
    {
       return getDefaultStartPageName();
    }

    //MD!!!
      public String getPackageName() {
          String pn = ((PLSORBSessionImpl)getSession()).getAppProperty("PackageName");

          if ( pn!=null )
         return pn;
          else if (m_PackageName != null && !m_PackageName.equals(""))
            return m_PackageName;
          else
               return getDefaultPackageName();
        }

            public void setPackageName(String name)
            {
             m_PackageName = name;
            }


   String m_PackageName = null;

   /*
    * This method is only included for verification of the page
    * expiration logic.  It should be removed or commented out
    * before production release.  To view this output in the
    * vlsOut.log file, set PrintVLSLOG to true in the VLS Console.
    */
   public void printPageList()
   {
      Enumeration loPages = pageList.elements() ;

      System.out.println() ;
      System.out.println( "*** Begin PLSApp Page List (" + pageList.size() + " pages) ***" ) ;
      while( loPages.hasMoreElements() )
      {
         VSPage loPage = (VSPage)loPages.nextElement() ;

         System.out.println( loPage.getPageId() + ": " + loPage.getClass().getName() ) ;
      } /* end while( loPages.hasMoreElements() ) */
      System.out.println( "*** End PLSApp Page List ***" ) ;
      System.out.println() ;
   } /* end printPageList() */

   /**
   * Checks to see if there are new alerts or broadcast messages for the user.
   */
   private void checkForNewAlertsAndBroadcasts()
   {
      if (mboolAlertExists || mboolBroadcastExists)
      {
         return;
      }
      boolean lboolNewAlerts = false;
      boolean lboolNewBroadcasts = false;
      VSResultSet loResultSet = null;
      VSRow loRow = null;
      VSSession loVSSession = getSession();
      Date loCurrentDt = new Date(System.currentTimeMillis());
      String lsUserId = AMSPLSUtil.getCurrentUserID(loVSSession);
      int liMessageInterval = getMessageInterval();

      if (moLastMsgCheckDt != null
            && ((loCurrentDt.getTime() - moLastMsgCheckDt.getTime())
            < (liMessageInterval * 60000)))
      {
         //moLastMsgCheckDt is null when new msg was never checked during this session
         //(e.g. when logging in). Thus, if moLastMsgCheckDt is not null and time elapsed is
         //less than the predefined interval on APPCTRL, then message checking is not required.
         return;
      }

      loResultSet = AMSUserProfileMethods.getResultSet(loVSSession, lsUserId);
      if (loResultSet != null)
      {
         loRow = loResultSet.getRowAt(1);
         if (loRow != null)
         {
            //store this in member variable to be used by new message page later
            moPreviousBroadcastReadDate = AMSUserProfileMethods
                  .getReadBroadcastDate(loRow);
            //store this in member variable to be used by new message page later
            moPreviousAlertReadDate = AMSUserProfileMethods.getReadChgDt(loRow);
         }
         else
	   {
            moLog.debug("Active Alert Process Failed: Cannot find user profile");
         }
         loResultSet.close();
      }
      else
	{
         moLog.debug("Active Alert Process Failed: Cannot find user profile");
      }

      lboolNewAlerts = hasNewMessage (moPreviousAlertReadDate, lsUserId);
      lboolNewBroadcasts = hasNewMessage (moPreviousBroadcastReadDate, null);
      if (lboolNewAlerts && lboolNewBroadcasts)
      {
         setAlertsAndBroadcastsExists(true);
      }
      else if (lboolNewAlerts)
      {
         setAlertsExists(true);
      }
      else if (lboolNewBroadcasts)
      {
         setBroadcastsExists(true);
      }
      else
      {
         //neither new alert nor new broadcast exist, set both to false
         setAlertsAndBroadcastsExists(false);
      }

      //update last msg check date here
      moLastMsgCheckDt = loCurrentDt;

      VSORBSession loORBSession = loVSSession.getORBSession();
      try
      {
         /* Updates to Last Alert read date and Last Broadcast read date
            on IN_USER_PROFILE entry should bypass security checks */
         AdvSecurityAuditUtil.turnOnSecBypassOneTime( loORBSession );
         AMSUserProfileMethods.updateReadBroadcastDt(loVSSession,
               lsUserId, moLastMsgCheckDt);
         AMSUserProfileMethods.updateReadChgDt(loVSSession,
               lsUserId, moLastMsgCheckDt);
         AdvSecurityAuditUtil.turnOffSecBypassOneTime( loORBSession );
      }//end try
      catch( Exception loExcp )
      {
         moLog.error(loExcp);
      }
      finally
      {
         if( loORBSession == null )
         {
            loORBSession = getSession().getORBSession();
         }
         /* Reset Session property related to security check bypass if it
            is still set on */
         if( !AdvSecurityAuditUtil.resetSecBypassOneTimeIfSet( loORBSession ) )
         {
            moLog.error("Error occurred during resetting value related to "
                  + "AdvSecurityAuditUtil");
         }
      }//end finally
   }

   private boolean hasNewMessage(VSDate foLastAlertReadDate, String fsUserID)
   {
      StringBuffer lsbWhereClause = new StringBuffer(100);
      String lsOrderBy = new String("ALRT_DT");
      VSQuery loQuery = null;
      VSResultSet loResultSet = null;
      VSSession loSession = getSession();
      VSDate loCurrentDt = new VSDate();

      //if new alert exists.
      boolean lboolHasNewMessage = false;

      try
      {
         if (fsUserID != null)
         {
            lsbWhereClause.append("USER_ID = ");
            lsbWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsUserID, true));
         }
         else
         {
            lsbWhereClause.append("USER_ID IS NULL ");
         }

         if (foLastAlertReadDate != null)
         {
            lsbWhereClause.append(" AND ALRT_DT > ");
            lsbWhereClause.append(AMSSQLUtil
                  .getAMSDate(foLastAlertReadDate, null, DataConst.TIMESTAMP,
                        AMSPage.getDatabaseType(loSession)));
            lsbWhereClause.append(" AND ");
            lsbWhereClause.append(AMSSQLUtil.getAMSDate(loCurrentDt, null, DataConst.TIMESTAMP,AMSPage.getDatabaseType(loSession)));
            lsbWhereClause.append(" >= EFBGN_DT ");
            

         }

         //retrieve all rows order by date
         loQuery = new VSQuery(loSession, "IN_ALERTS", lsbWhereClause
               .toString(), lsOrderBy);

         loResultSet = loQuery.execute();

         if (loResultSet != null)
         {
            VSRow loRow = loResultSet.getRowAt(1);
            if (loRow != null)
            {
               lboolHasNewMessage = true;
            }
         }
      }
      finally
      {
         if (loResultSet != null)
         {
            loResultSet.close();
         }
      }
      return lboolHasNewMessage;
   }

   public boolean doAlertsAndBroadcastsExists()
   {
      return (mboolAlertExists && mboolBroadcastExists);
   }

   public void setAlertsAndBroadcastsExists(
         boolean fboolAlertsAndBroadcastsExists)
   {
      mboolAlertExists = fboolAlertsAndBroadcastsExists;
      mboolBroadcastExists = fboolAlertsAndBroadcastsExists;
   }

   public boolean doAlertsExists()
   {
      return mboolAlertExists;
   }

   public void setAlertsExists(boolean fboolAlertsExists)
   {
      mboolAlertExists = fboolAlertsExists;
   }

   public boolean doBroadcastsExists()
   {
      return mboolBroadcastExists;
   }

   public void setBroadcastsExists(boolean fboolBroadcastsExists)
   {
      mboolBroadcastExists = fboolBroadcastsExists;
   }

   private int getMessageInterval()
   {

      if (miMessageInterval == -1)
      {
         //get user defined parameter value
         String lsParamValue = AMSPLSUtil.getApplParamValue(
               ALERT_BROADCAST_CHECK_INTERVAL, getSession());

         if (lsParamValue != null)
         {
            try
            {
               miMessageInterval = new Integer(lsParamValue).intValue();
            }
            catch (NumberFormatException loEx)
            {
               //Default to 1 minute
               miMessageInterval = 1;
            }
         }
         else
         {
            //Default to 1 minute
            miMessageInterval = 1;
         }
      }

      return miMessageInterval;
   }

   public VSDate getPreviousBroadcastReadDate()
   {
      return moPreviousBroadcastReadDate;
   }

   public VSDate getPreviousAlertReadDate()
   {
      return moPreviousAlertReadDate;
   }

   /**
    * This method removes any single or double quotes from the
    * Frame Name property value.
    * @param fsFrameName frame_name value
    */
   public void setCurrentFrameName( String fsFrameName )
   {

      String lsFrameName = fsFrameName ;

      if ( !AMSStringUtil.strIsEmpty(lsFrameName ) )
      {
         if ( lsFrameName.indexOf("\"")!= -1)
         {
            lsFrameName = lsFrameName.replace('"', ' ');
         }

         if ( lsFrameName.indexOf("'")!= -1)
         {
            lsFrameName = lsFrameName.replace('\'', ' ');
         }
      }
      super.setCurrentFrameName( lsFrameName ) ;
   }//end of setCurrentFrameName.

   public VSPage getPageFromList( String fsPageID )
   {
      return super.getPageFromList( fsPageID != null ? fsPageID : "" ) ;
   }


}