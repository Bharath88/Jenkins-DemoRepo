//{{APP_IMPORT_STMTS
package advantage.Admin;
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

//{{APP_CLASS_DECL
public class Admin extends AdminBase
//END_APP_CLASS_DECL}}
{
    // Admin Class Constructor
	//{{APP_CLASS_CTOR
	public Admin ()
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
		     	tr.set(VST_SESSION_ID, getSessionIDForTracing()).set(VST_CATEGORY, VST_PLS_ACTION).set(VST_ACTION_NAME,"Admin.doAction");
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
		        StartPageNav.setTargetPageName(getPackageName() + ".pNoData");
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
		        StartPageNav.setTargetPageCaption("");
			StartPageNav.setTargetName("");
		
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
}