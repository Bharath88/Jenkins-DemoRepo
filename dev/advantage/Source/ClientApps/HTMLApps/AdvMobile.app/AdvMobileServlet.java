//{{SERVLET_IMPORT_STMTS
package advantage.AdvMobile;
import versata.vfc.html.*;
import versata.vfc.html.servlet.*;
import versata.vfc.html.common.*;
import versata.common.*;
import versata.vls.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.vfc.html.servlet.*;
import com.versata.util.logging.*;

//END_SERVLET_IMPORT_STMTS}}


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import adv.logging.LogManager;
import adv.logging.UserActivity;
import adv.logging.UserActivityLogTask;
import advantage.AMSStringUtil;
import advantage.AMSUtil;

import com.amsinc.gems.adv.common.AMSBase64Coder;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.common.AMSSQLConnection;

import com.ams.csf.common.CSFManager;

import org.apache.commons.logging.Log;


//{{SERVLET_CLASS_DECL
public class AdvMobileServlet extends AdvMobileBaseServlet
//END_SERVLET_CLASS_DECL}}
{
    // AdvMobileServlet Class Constructor
	//{{SERVLET_CLASS_CTOR
	public AdvMobileServlet ()
	//END_SERVLET_CLASS_CTOR}}
    {
	super();
    }

   
   private static ConcurrentLinkedQueue<UserActivity> moUserActivityQueue = null;
   
   private static LogManager moLogProducer = null;
   
   private static JSONArray moLogMobileRequestsObj = null;

   private static Log moLog =
         AMSLogger.getLog( AdvMobileServlet.class, 
         AMSLogConstants.FUNC_AREA_MOBILITY ) ;

   /**
    * Defines the mobile app id currently being accessed by the servlet.
    */
   public String msMobileAppID = "";

   static
   {
	   if(AMSParams.mboolLogMobileRequests)
	   {
	      moUserActivityQueue = new ConcurrentLinkedQueue<UserActivity>();

	      ExecutorService moThreadPool = Executors.newFixedThreadPool(3);
	      moThreadPool.submit(new UserActivityLogTask(moUserActivityQueue, 1));
	      moThreadPool.submit(new UserActivityLogTask(moUserActivityQueue, 2));
	      moThreadPool.submit(new UserActivityLogTask(moUserActivityQueue, 3));

	      moLogProducer = new LogManager(moUserActivityQueue);
	   }
   }
    public void loadLoginPage(boolean failed, HttpServletResponse res, 
          HttpServletRequest req, String msg)
		throws IOException 
    {
		//{{SERVLET_LOADLOGINPAGE
		// Load login page. If previous attempt failed, load page with failed message.
		// This method will be generated in gen'd servlet and user can
		// add code for custom login there, or it can be gen'd to supress login
		//super.loadLoginPage(failed, res, req,msg);
		//escape html-specific character first
		if( msg != null )
		{
		   msg = versata.vfc.html.HTMLWriterEx.toHTMLCharacter( msg );
		}
		
		// Do AMS specific login 
		loadLoginPage( failed, res, req, msg, defaultLogonUserID,
		               pkgName, applicationName, appServerDSN ) ;
		PrintWriter toClient = res.getWriter();
		String params = req.getQueryString();
		
		//END_SERVLET_LOADLOGINPAGE}}
		
		if (!AMSStringUtil.strIsEmpty(msg))
      {
         JSONObject loResp = new JSONObject();
         JSONObject loMetaData = new JSONObject();
         JSONObject loErrorJSON = new JSONObject();
         
         JSONObject loFirstError = new JSONObject();
         
         loFirstError.element("severity", "error");
         loFirstError.element("description", msg);
         
         loErrorJSON.element("first_error", loFirstError);
         loMetaData.element("Errors", loErrorJSON);
         loResp.element("MetaData", loMetaData);
         
         res.reset();
         
         res.getWriter().print(
               req.getParameter("callback") + "(" + loResp.toString()
                     + ")");
         return;
      }
    }

    public String getFileLocation()
    {
	    return getDefaultFileLocation();
	
    }

    public String getBaseURL(HttpServletRequest req, String packageName, String clientAppName)
    {
          return super.getBaseURL(req, packageName, clientAppName);
    }
    public String getServletURL(HttpServletRequest req)
    {
        return super.getServletURL(req);			
    }

	public PLSORBSession doLogin(HttpServletRequest req)
		throws TierSessionLimitException, RemoteException
	{
		return doDefaultLogin(req);
	}

	public void loadStartPage()
	{
		// Right now, this is done by calling session.start, which calls app.start.
		// We may want this here for the case when developer wants to customize.
	}

   /* (non-Javadoc)
    * This method is overridden to convert the JSON Data coming in request to a 
    * translatable ORBRequest. 
    * @see versata.vfc.html.servlet.PLSServlet#createORBRequest(javax.servlet.http.HttpServletRequest)
    */
   @Override
   public ORBRequest createORBRequest(HttpServletRequest req)
   {
      ORBRequest loORBReq = super.createORBRequest(req);

      //Get JSON Data
      String lsReqObj = req.getParameter("JSONData");
      try
      {
         if (lsReqObj != null)
         {
            //Transform the raw JSON to JSONObject
            JSONObject loReqObj = (JSONObject) JSONSerializer.toJSON(lsReqObj);
            if (loReqObj != null)
            {
               Vector loParamNamesColl = new Vector();
               String lsParamName = "";
               Enumeration loPNames = req.getParameterNames();
               
               Iterator loKeys = loReqObj.keys();
               String lsKey = null;
               //Iterate though the JSON and add each key as param name
               while (loKeys.hasNext())
               {
                  lsKey = loKeys.next().toString();
                  loParamNamesColl.add(lsKey);
               }

               //Check if the JSON has query_string attribute
               Properties loProps = null;
               String lsQS = loReqObj
                     .getString(HTMLDocumentModel.QUERY_STRING_ATTRIBUTE);
               if (lsQS != null)
               {
                  loProps = new Properties();
                  StringTokenizer loST = new StringTokenizer(lsQS, "&", false);
                  while (loST.hasMoreTokens())
                  {
                     String lsNVPair = loST.nextToken();
                     int liPos = lsNVPair.indexOf('=');
                     if (liPos < 1)
                        continue;
                     
                     //Add the query_string parameters to the param name vector and properties object.
                     if (liPos == lsNVPair.length() - 1)
                     {
                        String lsPropName = lsNVPair.substring(0, liPos);
                        loProps.put(lsPropName, "");
                        loParamNamesColl.addElement(lsPropName);
                     }
                     else
                     {
                        String lsPropName = lsNVPair.substring(0, liPos);
                        String lsPropVal = lsNVPair.substring(liPos + 1);
                        loProps.put(lsPropName, lsPropVal);
                        loParamNamesColl.addElement(lsPropName);
                     }
                  }
               }

               int liSize = loParamNamesColl.size();
               HttpParam loParamArray[] = new HttpParam[loORBReq.params.length + liSize];

               //Add the original request parameters to the array first
               for (int liCnt = 0; liCnt < loORBReq.params.length; liCnt++)
               {
                  loParamArray[liCnt] = loORBReq.params[liCnt];
               }
               
               //Add the JSON parameters to the array
               for (int liCnt = 0; liCnt < liSize; liCnt++)
               {
                  lsParamName = (String) loParamNamesColl.elementAt(liCnt);
                  HttpParam loParams = new HttpParam();
                  loParams.name = lsParamName;
                  if (loReqObj.containsKey(lsParamName))
                  {
                     loParams.value = loReqObj.getString(lsParamName);
                  }
                  else
                  {
                     if (loProps != null)
                        loParams.value = (String) loProps.get(lsParamName);
                     if (loParams.value == null)
                        loParams.value = "";
                  }

                  loParamArray[loORBReq.params.length + liCnt] = loParams;
               }

               if (lsQS != null)
               {
                  loORBReq.queryString = lsQS;
               }
               else
               {
                  loORBReq.queryString = "";
               }

               if (req.getServletPath() != null)
               {
                  loORBReq.servletLocation = req.getServletPath();
               }
               else
               {
                  loORBReq.servletLocation = "";
               }

               if (loReqObj.containsKey("session_id"))
               {
                  loORBReq.sessionId = loReqObj.getString("session_id");
               }
               else
               {
                  loORBReq.sessionId = req.getParameter("session_id");
               }

               loORBReq.params = loParamArray;
            }
         }
      }
      catch (Exception foExcep)
      {
         Util.logError(logger, "Exception occured while processing JSON request: "
               + foExcep);
      }

      return loORBReq;

   }

   /* (non-Javadoc)
    * @see com.amsinc.gems.adv.vfc.html.servlet.AMSPLSServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   public void doGet(HttpServletRequest foHttpReq, HttpServletResponse foHttpRes)
         throws ServletException, IOException
   {
      PrintWriter loResWriter = null;
      msMobileAppID = foHttpReq.getParameter("AppId");

      String lsMobileAppVersion = foHttpReq.getParameter("AppVersion");
      String lsValidateAppVersion = foHttpReq.getParameter("ValidateAppVersion");

      msMobileAppID = AMSBase64Coder.decodeString(msMobileAppID);
      lsMobileAppVersion = AMSBase64Coder.decodeString(lsMobileAppVersion);

      //Validate the mobile application version
      if (!AMSStringUtil.strIsEmpty(lsValidateAppVersion)
            && !AMSStringUtil.strIsEmpty(msMobileAppID)
            && !AMSStringUtil.strIsEmpty(lsMobileAppVersion))
      {
         if (AMSStringUtil.strEqual(msMobileAppID, AMSPLSServlet.MSS_MOBILE_APP) &&
             Arrays.asList(AMSParams.msMSSMobileSupportedVersions).contains(lsMobileAppVersion))
         {
            loResWriter = foHttpRes.getWriter();
            loResWriter.write( foHttpReq.getParameter("callback") + "(" + true + ")" );
         }
         else if(AMSStringUtil.strEqual(msMobileAppID, AMSPLSServlet.WFA_MOBILE_APP) &&
                 Arrays.asList(AMSParams.msWFAMobileSupportedVersions).contains(lsMobileAppVersion))
         {
            loResWriter = foHttpRes.getWriter();
            loResWriter.write( foHttpReq.getParameter("callback") + "(" + true + ")" );
         }
         else
         {
            loResWriter.write( foHttpReq.getParameter("callback") + "(" + false + ")" );
         }
         return;
      }

      if (foHttpReq.getParameter("InitLoginPage") != null)
      {
         JSONObject loResp = new JSONObject();
         foHttpRes.getWriter().print(
               foHttpReq.getParameter("callback") + "(" + loResp.toString()
                     + ")");
         return;
      }
      String lsSessionId = null;
      String lsUserId = null;
      
      boolean lboolIsLoginReq = !(AMSStringUtil.strIsEmpty(foHttpReq.getParameter("ADV_APP_USER")) &&
            AMSStringUtil.strIsEmpty(foHttpReq.getParameter("ADV_APP_PASSWORD")));
      
      boolean lboolIsLogoffReq = !(AMSStringUtil.strIsEmpty(foHttpReq.getParameter("vslogoff")) );
      lsSessionId = foHttpReq.getParameter("session_id");
      
      //Log the request if configured
      if(!lboolIsLoginReq && !lboolIsLogoffReq)
      {
         boolean[] lboolLogging = isAppLoggingEnabled(msMobileAppID);
         if(AMSParams.mboolLogMobileRequests && lboolLogging[0])
         {
            PLSORBSession loSession = null;
            if (lsSessionId != null)
            {
               loSession = getExistingSession(lsSessionId, foHttpReq);
               lsUserId = loSession.getVSORBSession().getUserID();
            }
            UserActivity loActivity = new UserActivity(lsUserId,
                  foHttpReq.getParameterMap(),
                  getRequestHeadersAsString(foHttpReq), System.currentTimeMillis(), lboolLogging[1]);
            moLogProducer.queueEvent(loActivity);
         }
      }
      
      
      //do Log off activity
      
      if (foHttpReq.getParameter("vslogoff") != null)
      {
         doLogoff(foHttpReq, foHttpRes, lsSessionId);
         JSONObject loResp = new JSONObject();
         loResp.put("timeout", "true");
         loResp.put("logout", "true");
         foHttpRes.getWriter().print(
               foHttpReq.getParameter("callback") + "(" + loResp.toString()
                     + ")");
         return;
      }
      
      super.doGet(foHttpReq, foHttpRes);      
   }
   
   public boolean doLogAccessRequests(HttpServletRequest foHttpReq)
   {
      boolean lboolLogReq = false;
      
      String lsMobileAppID = foHttpReq.getParameter("AppId");
      String lsMobileAppVersion = foHttpReq.getParameter("AppVersion");
      
      //Validate the mobile application version
      if(!AMSStringUtil.strIsEmpty(lsMobileAppID) && !AMSStringUtil.strIsEmpty(lsMobileAppVersion))
      {
         lsMobileAppID = AMSBase64Coder.decodeString(lsMobileAppID);
         lsMobileAppVersion = AMSBase64Coder.decodeString(lsMobileAppVersion);
      }

      
      return lboolLogReq;
   }
   /**
    * Returns the request header as a String
    * 
    * @param foHttpReq HTTP Request object
    * @return
    */
   public String getRequestHeadersAsString(HttpServletRequest foHttpReq)
   {
      Enumeration<String> loHeaderNames = foHttpReq.getHeaderNames();
      StringBuffer lsbHeader = new StringBuffer(256);
      while(loHeaderNames.hasMoreElements())
      {
         String lsHeader = loHeaderNames.nextElement();
         String lsHeaderVal = foHttpReq.getHeader(lsHeader);
         lsbHeader.append(lsHeader).append(":").append(lsHeaderVal).append("\n");
      }
      return lsbHeader.toString();
   }
   
   
   private static JSONArray getMobileAppLoggingParams() throws Exception
   {
      if(moLogMobileRequestsObj == null)
      {
         AMSSQLConnection loConn = null;
         try
         {
            loConn = AMSUtil.getSQLConnection("IN_APP_CTRL");
            
            /* Get Application Parameter value for APP_PARAM_VRS_DB_HINT_ENABLE */
            String lsLogMobileRequests = AMSUtil.getApplParam( loConn, 
                  "MOBILE_REQUEST_LOG_OBJ" );
            moLogMobileRequestsObj = JSONArray.fromObject(lsLogMobileRequests);
         }//end try
         catch ( Exception loExcp )
         {
            moLog.error("Error during AdvMobileServlet.getApplParameters", loExcp);
            throw new Exception("Exception while reading the Application control" +
                  " table. Please check the LogMobileRequests parameter value.");
         }//end catch
         finally
         {
            try
            {
               if( loConn != null )
               {
                  loConn.close();
               }
            }
            catch ( Exception loExcp1 )
            {
               moLog.error("Could not close Connection in getApplParameters", loExcp1);
               throw new Exception("Could not close Connection in getApplParameters");
            }
         }//end finally
      }
      
      
      return moLogMobileRequestsObj;
   }

   public boolean[] isAppLoggingEnabled(String fsAppId)
   {
      if(moLogMobileRequestsObj == null)
      {
         try
         {
            moLogMobileRequestsObj = getMobileAppLoggingParams();
         }
         catch (Exception foExcep)
         {
            moLog.error(foExcep);
         }
      }
      
      boolean [] lboolIsAppLoggingEnabled = {false, false};
      
      Iterator<JSONObject> loItr = moLogMobileRequestsObj.iterator();
      while(loItr.hasNext())
      {
         JSONObject loObj = loItr.next();
         if(fsAppId != null && fsAppId.equals(loObj.get("AppId")))
         {
            lboolIsAppLoggingEnabled[0] = loObj.getBoolean("LOG_ENABLED");
            lboolIsAppLoggingEnabled[1] = loObj.getBoolean("VERBOSE_LOGGING");
            break;
         }
      }
      
      return lboolIsAppLoggingEnabled;
   }
   
   
   /**
    * Gets the user id from servlet request parameter, if suppressLogin is
    * is on.
    *
    * @return String  User Name
    * @see #getPassword()
    */
   public String getUser( HttpServletRequest foHttpReq )
   {

      String  lsUser ;

      lsUser  = foHttpReq.getParameter ( "ADV_APP_USER" ) ;
       try
       {
          if ( lsUser == null )
          {
             if (CSFManager.getInstance().getPreferences().enableSSO())
             {
                moLog.warn ( "LOGINERROR: UserID not specified, Please check the " );
                moLog.warn ( "       Servlet parameter value for ADV_APP_USER" );
                return "" ;
             } // end if ( lsUser == null )
             return "" ;
          }else
          {
             return AMSBase64Coder.decodeString(lsUser) ;
          } // end else
       }catch(Exception loe)
       {
          moLog.error(loe);
          return "";
       }
   } /* end getUser() */

   /**
    * Gets the password from servlet request parameter, if suppressLogin is
    * is on.
    * 
    * @return String  password
    * @see #getUser()
    */
   public String getPassword( HttpServletRequest foHttpReq )
   {
      String        lsPassword ;

      lsPassword  = foHttpReq.getParameter( "ADV_APP_PASSWORD" ) ;
       try
       {
          if ( lsPassword == null )
          {
             if (CSFManager.getInstance().getPreferences().enableSSO())
             {
                moLog.warn ( "LOGINERROR: Password not specified, Please check the " );
                moLog.warn ( "       Servlet parameter value for ADV_APP_PASSWORD" );
             }
             return "" ;
          }else
          {
             return AMSBase64Coder.decodeString(lsPassword) ;
          }
       }catch(Exception loe)
       {
          moLog.error(loe);
          return "";
       }
   } /* end getPassword () */

   /**
    * Returns the list of Applications that the Servlet can connect to.
    * Overrides the getApplicationList in AMSPLSServlet.java.
    * The application list is defined in R_SC_APPL.
    *
    * @return the string of applications
    */
   public String getApplicationList()
   {
      if (AMSStringUtil.strEqual(msMobileAppID, AMSPLSServlet.MSS_MOBILE_APP))
      {
         return "11";
      }
      else if (AMSStringUtil.strEqual(msMobileAppID, AMSPLSServlet.WFA_MOBILE_APP))
      {
         return "1";
      }
      else
      {
         return "11";
      }
   }
}