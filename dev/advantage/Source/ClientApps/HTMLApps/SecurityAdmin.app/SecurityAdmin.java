//{{APP_IMPORT_STMTS
package advantage.SecurityAdmin;
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
	public class SecurityAdmin extends SecurityAdminBase
	//END_APP_CLASS_DECL}}
	{
	    // SecurityAdmin Class Constructor
		//{{APP_CLASS_CTOR
		public SecurityAdmin ()
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
			     	tr.set(VST_SESSION_ID, getSessionIDForTracing()).set(VST_CATEGORY, VST_PLS_ACTION).set(VST_ACTION_NAME,"SecurityAdmin.doAction");
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
	    }
	
	
	    public String getStartPageName()
	    {
		    return getDefaultStartPageName();
	    }
	
	    public String getPackageName()
	    {
		    return getDefaultPackageName();
	    }
	}