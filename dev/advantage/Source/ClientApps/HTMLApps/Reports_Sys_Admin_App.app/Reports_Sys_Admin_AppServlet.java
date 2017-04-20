//{{SERVLET_IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
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
	
	//{{SERVLET_CLASS_DECL
	public class Reports_Sys_Admin_AppServlet extends Reports_Sys_Admin_AppBaseServlet
	//END_SERVLET_CLASS_DECL}}
	{
	    // Reports_Sys_Admin_AppServlet Class Constructor
		//{{SERVLET_CLASS_CTOR
		public Reports_Sys_Admin_AppServlet ()
		//END_SERVLET_CLASS_CTOR}}
	    {
			super();
		}
		
		public void loadLoginPage(boolean failed, HttpServletResponse res, HttpServletRequest req, String msg)
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
		    return HttpUtils.getRequestURL(req).toString();
		
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
	
	}