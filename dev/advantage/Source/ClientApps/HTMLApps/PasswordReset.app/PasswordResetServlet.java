//{{SERVLET_IMPORT_STMTS
package advantage.PasswordReset;
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
import org.omg.CORBA.*;
import java.rmi.NoSuchObjectException;
import com.amsinc.gems.adv.common.* ;

//{{SERVLET_CLASS_DECL
public class PasswordResetServlet extends PasswordResetBaseServlet
//END_SERVLET_CLASS_DECL}}
{
    // PasswordResetServlet Class Constructor
	//{{SERVLET_CLASS_CTOR
	public PasswordResetServlet ()
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
        return super.getServletURL(req);
    }

	public PLSORBSession doLogin(HttpServletRequest req)
		throws TierSessionLimitException, RemoteException
	{
		return doDefaultLogin(req);
	}

   public void doLogoff( HttpServletRequest foReq, HttpServletResponse foRes,
                         String fsSessionId )
      throws ServletException, IOException
   {
      super.doLogoff( foReq, foRes, fsSessionId ) ;

   } /* end doLogoff() */

	public void loadStartPage()
	{
		// Right now, this is done by calling session.start, which calls app.start.
		// We may want this here for the case when developer wants to customize.
	}

   /**
    * This method will set the password reset flag on the security object
    *
    * Modification Log : Kiran Hiranandani  - 04/03/2003
    *                                      - initial version
    *
    */
   protected void beforeLogin( AMSSecurityObject foSecObj )
   {
      foSecObj.setPasswordReset( true ) ;
   } /* end beforeLogin() */

   /**
    * Returns the list of Applications that the Servlet can connect to.
    * Overrides the getApplicationList in AMSPLSServlet.java.
    * Assumption - 7 is the Password Reset application defined in
    * R_SC_APPL.
    *
    * Modification Log : Kiran Hiranandani  - 04/03/2003
    *                                      - initial version
    *
    * @return the string of applications
    */
   public String getApplicationList()
   {
      return "8" ;
   } /* end getApplicationList() */

}