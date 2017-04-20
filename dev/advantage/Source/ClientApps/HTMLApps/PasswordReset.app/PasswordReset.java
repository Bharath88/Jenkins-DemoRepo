//{{APP_IMPORT_STMTS
package advantage.PasswordReset;
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
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.tracing.*;
import java.rmi.RemoteException;
import advantage.AMSStringUtil;

//{{APP_CLASS_DECL
public class PasswordReset extends PasswordResetBase
//END_APP_CLASS_DECL}}
{
    // PasswordReset Class Constructor
	//{{APP_CLASS_CTOR
	public PasswordReset ()
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
		     	tr.set(VST_SESSION_ID, getSessionIDForTracing()).set(VST_CATEGORY, VST_PLS_ACTION).set(VST_ACTION_NAME,"PasswordReset.doAction");
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