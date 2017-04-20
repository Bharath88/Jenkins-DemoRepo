//{{APP_IMPORT_STMTS
package advantage.AdvMobile;
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
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import com.amsinc.gems.adv.common.AMSBase64Coder;
import com.amsinc.gems.adv.vfc.html.servlet.AMSPLSServlet;
import advantage.AMSStringUtil;

//{{APP_CLASS_DECL
public class AdvMobile extends AdvMobileBase
//END_APP_CLASS_DECL}}
{

   /**
    * Mobile Worklist Search page name
    */
   public static final String MOB_WL_SRCH_PG_NM = "advantage.AdvMobile.pMobileWorklistSearch";

   /**
    * Mobile Worklist Search page name
    */
   public static final String MOB_WL_PG_NM = "advantage.AdvMobile.pMobileWorklist";

   /**
    * Mobile Page List Page name
    */
   public static final String MOB_PG_LST_PG_NM = "advantage.AdvMobile.pWFAPageList";

    // AdvMobile Class Constructor
//{{APP_CLASS_CTOR
public AdvMobile ()
//END_APP_CLASS_CTOR}}
    {
     super();
    }

    public synchronized PLSResponse doAction(PLSRequest preq)
    {
//{{APP_DOACTION
   IVSTrace tr = null;  long begTime = 0;
   if ( VSTrace.IS_ON ){
   	tr = VSTrace.get();	
     	begTime = tr.beg(logger);
     	tr.set(VST_SESSION_ID, getSessionIDForTracing()).set(VST_CATEGORY, VST_PLS_ACTION).set(VST_ACTION_NAME,"AdvMobile.doAction");
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
    }

 public PLSResponse start(String sessionID, VSSession session)
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

        PageNavigation StartPageNav = new PageNavigation();
        StartPageNav.setParentApp( this );
        StartPageNav.setTargetPageName(getPackageName() + ".pPageList");
        StartPageNav.setInitialAddMode(false);
        StartPageNav.setInitialQueryMode(false);

	String devWhere = "";

	if (getQueryStringParams() != null) {
	String params = getQueryStringParams().getProperty("dev_where", "");
	if (devWhere.length() != 0){
		if (params.length() != 0)
			devWhere = devWhere + " AND " + params;
	}
	else
		devWhere = params;
	}

	StartPageNav.setDevWhere(devWhere);

        StartPageNav.setOrderBy("");
        StartPageNav.setTargetPageCaption("Mobile Apps");
	StartPageNav.setTargetName("");
        StartPageNav.setTargetPageName(getPackageName() + "." + getProperties().getProperty("HomePage"));

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
    }



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

   /**
    * This method handles the mobile global transitions which can be accessed from any mobile page
    * in the application.
    * @param foPrntApp Parent application for performing a transition
    * @param fsSessionId the session ID for performing a transition
    * @param foPLSReq represents the request being processed.
    * @return VSPage representing the page to transition to.
    */
   public static VSPage getMobileGlobalTransition(PLSApp foPrntApp, String fsSessionId, PLSRequest foPLSReq)
   {
      String lsDestPg = foPLSReq.getParameter(AMSHyperlinkActionElement.DESTINATION_ATTRIBUTE);
      String lsApplNm = foPLSReq.getParameter(AMSHyperlinkActionElement.APPLICATION_ATTRIBUTE);
      String lsSrchTyp = foPLSReq.getParameter("Search_Type");

      AMSDynamicTransition loDynTrans = new AMSDynamicTransition(lsDestPg, "", "", lsApplNm);

      return loDynTrans.getVSPage(foPrntApp, fsSessionId);
   }

   /**
    * Override this method to set the property which determines
   * that this is a Mobile Client Application.
   */
   @Override
   protected Properties loadAppProperties()
   {
      Properties loProperties = super.loadAppProperties();
      loProperties.setProperty("IsMobileApp", "true");

      String lsMobileAppID = "";

      //try to see if there's an AppId, if not, do the default "else" logic
      try
      {
         lsMobileAppID = AMSBase64Coder.decodeString(getQueryStringParams().getProperty("AppId"));
      }
      catch(Exception loEx)
      {
          //default else case since AppId isn't present.
      }

      if(AMSStringUtil.strEqual(lsMobileAppID, AMSPLSServlet.WFA_MOBILE_APP))
      {
         loProperties.setProperty("HomePage", "pWFAPageList");
      }
      else
      {
         loProperties.setProperty("HomePage", "pPageList");
      }



      return loProperties;
   }
}