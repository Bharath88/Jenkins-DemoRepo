//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.common.*;
import versata.vls.*;
import com.amsinc.gems.adv.vfc.html.*;
import com.ams.csf.base.*;
import com.ams.csf.auth.*;
import com.ams.csf.common.*;

import org.apache.commons.logging.Log;


import advantage.AMSStringUtil;


/*
**  AMSAdvAppPage
*/

//{{FORM_CLASS_DECL
public class AMSSSOAppPage extends AMSSSOAppPageBase

//END_FORM_CLASS_DECL}}
{
   /* variable name used in the app login page */
   private String msExtAppUsernameLabel = "ADV_APP_USER";
   private String msExtAppPasswordLabel = "ADV_APP_PASSWORD";
   private String msExtAppServernameLabel = "ADV_APP_SERVERNAME";
   private String msExtAppTokenLabel = "ADV_APP_TOKEN";

   private String msExtAppUsernameValue = null;
   private String msExtAppPasswordValue = null;
   private String msExtAppServernameValue = "<localBlsServer>";
   private String msExtAppTokenValue = null;
   private String msExtAppUrlValue = null;
   private String msExtAppFrameValue = null;

   private static String LINE_SEP = System.getProperty("line.seperator");
   
   private static Log moLog = AMSLogger.getLog( AMSSSOAppPage.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;


   /* static content of html page */
   private final String HTML_CONTENT = "<html><head>\n"
         + "<title>ADVANTAGE Single Sign-on </title>\n"
         + "<base href=\"" + this.getBaseURL() + "\">\n"
         +"<script language=\"JavaScript\"> \n"
         + "<!--\n"
         + "function submitme() \n"
         + "{\n"
         + "      var loNewWin;\n"
         + "      var lsHref = UTILS_getBaseHref( document ) + '../AMSImages/Empty.htm';\n"
         + "      var loDate = new Date();\n"
         + "      loNewWin = DHTMLLIB_OpenNewWin(lsHref, 'SSO',\n"
         + "                'no' , 'no' , 'no', 'yes' , 'no',\n"
         + "                'yes', 'yes', null,\n"
         + "                  null, 0, 0, 'yes' );\n"
         + "       document.forms[0].target = 'SSO';\n"

         + "  document.forms[0].submit(); \n"
         + "}\n"
         + "   --> \n"
         + " </script> \n"
         + "<script type=\"text/javascript\" language=\"JavaScript\""
         + " src=\"../AMSJS/AMSDHTMLLib.js\"></script>\n"
         + "<script type=\"text/javascript\" language=\"JavaScript\""
         + " src=\"../AMSJS/AMSUtils.js\"></script>\n"
         + " </head>\n"
         + " <body onload=\"UTILS_EnableClicks();UTILS_InitPage();UTILS_HideDocNavPanel;UTILS_GetMainStartupPage().UTILS_InitPage();submitme()\">";
   private String HTML_END = "</body></html>";



   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public AMSSSOAppPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

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

   public String generate()
   {
      StringBuffer loBf = new StringBuffer();

      AMSStartupPage loStartup = null;
      AMSSSOAppNode loNode = null;

      /* for the moment, the lsToken will be keystring+encoded(password)*/
      String lsToken = null;
      String lsEncryptedPassword = null;
      String lsKey = null;
      byte[] loCipherText = null;
      byte[] loKeyData = null;

      try
      {
         if(msExtAppUsernameValue==null&&msExtAppPasswordValue==null)
         {
            // get username/password of this session
            VSORBSession loORBSession = parentApp.getSession().getORBSession();
            Object loSecObj = loORBSession.getServerSecurityObject() ;
            if( loSecObj instanceof VSMapSecurityInfo )
            {
               msExtAppUsernameValue = ((VSMapSecurityInfo)loSecObj).getLogin() ;
               msExtAppPasswordValue = ((VSMapSecurityInfo)loSecObj).getPassword() ;

            } // end if ( loSecObj instanceof VSMapSecurityInfo )
         }

         if(msExtAppUrlValue==null)
         {
            // from the cached node to get the APP URL
            // node is on the top of the history list
            loStartup = AMSStartupPage.getStartupPage( getParentApp() ) ;
            if(!AMSStringUtil.strIsEmpty(loStartup.msSSOURL))
            {
               msExtAppUrlValue = loStartup.msSSOURL;
            }
            else
            {
               loNode = (AMSSSOAppNode) loStartup.getHistoryNode(1);
               msExtAppUrlValue = loNode.getURL();
            }
         }

         if(msExtAppFrameValue == null)
         {
            // from the cached node to get the APP URL FRAME
            if(loNode == null)
            {
               loStartup = AMSStartupPage.getStartupPage( getParentApp() ) ;
               loNode = (AMSSSOAppNode) loStartup.getHistoryNode(1);
            }
            if(!AMSStringUtil.strIsEmpty(loStartup.msSSOURL))
            {
               msExtAppFrameValue = loStartup.msSSOFrame;
            }
            else
            {
               msExtAppFrameValue = loNode.getURLTarget();
            }
            
         }

         if(AMS_PLS_DEBUG)
         {
            System.out.println("show me username:\n" + msExtAppUsernameValue);
            System.out.println("show me password:\n" + msExtAppPasswordValue);
            System.out.println("show me URL:\n" + msExtAppUrlValue);
            System.out.println("show me Frame:\n" + msExtAppFrameValue);
         }
         
         if(AMSStringUtil.strIsEmpty(msExtAppPasswordValue))
         {
            AMSSecurityObject loAMSSecurityObject = (AMSSecurityObject) getParentApp()
                  .getSession().getSecurityObject();
            lsToken = CSFManager.getInstance().getEncoder()
                  .encode(loAMSSecurityObject.getCSFToken());
         }
         else if(msExtAppTokenValue == null)
         {
            // CSF to encrypt the password by using USER_ID
            msExtAppTokenValue =
               AMSCSFKeyUtil.encryptPasswordByUserID(msExtAppUsernameValue,
                                 msExtAppPasswordValue);
         }


         if(AMS_PLS_DEBUG)
         {
            System.out.println("msExtAppTokenValue:\n" + msExtAppTokenValue);
         }


         /*
          * load a self-submitting html page
          */

         loBf.append(HTML_CONTENT).append("\n");

         loBf.append("<form action=\"").append(msExtAppUrlValue)
            .append("\" method=\"post\" target=\"").
               append(msExtAppFrameValue+System.currentTimeMillis()).append("\">\n");

         if(!AMSStringUtil.strIsEmpty(msExtAppPasswordValue))
         {
            loBf.append("<input type=\"hidden\" name=\"")
                  .append(msExtAppUsernameLabel).append("\" value=\"")
                  .append(msExtAppUsernameValue).append("\">\n");

            loBf.append("<input type=\"hidden\" name=\"")
                  .append(msExtAppPasswordLabel).append("\" value=\"")
                  // .append(msExtAppPasswordValue).append("\">\n");
                  .append("\">\n");
            
            loBf.append("<input type=\"hidden\" name=\"")
                  .append(msExtAppTokenLabel).append("\" value=\"")
                  .append(msExtAppTokenValue).append("\">\n");

            loBf.append("<input type=\"hidden\" name=\"")
                  .append(msExtAppServernameLabel).append("\" value=\"")
                  .append(msExtAppServernameValue).append("\"></form>\n");
         }
         else
         {
            loBf.append("<input type=\"hidden\" name=\"").append("token")
                  .append("\" value=\"").append(lsToken).append("\">\n");
         }
         
         loBf.append(HTML_END);
         if(AMS_PLS_DEBUG)
         {
            System.out.println("show me the page\n" + loBf.toString());
         }
         return loBf.toString();
      }
      catch(Exception loe)
      {
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loe);
         raiseException( "Launching external ADVANTAGE app encountered errors.",
                               SEVERITY_LEVEL_INFO ) ;
         return super.generate();
      }
   }// end of generate

   /**
    * Getter method for msExtAppUserNameLabel
    */
   public String getExtAppUsernameLabel ()
   {
      return msExtAppUsernameLabel;
   }
   /**
    * Setter method for msExtAppUsernameLabel
    */
   public void setExtAppUsernameLabel(String fsLabel)
   {
      msExtAppUsernameLabel = fsLabel;
   }
   /**
    * Getter method for msExtAppPasswordLabel
    */
   public String getExtAppPasswordLabel ()
   {
      return msExtAppPasswordLabel;
   }
   /**
    * Setter method for msExtAppPasswordLabel
    */
   public void setExtAppPasswordLabel(String fsLabel)
   {
      msExtAppPasswordLabel = fsLabel;
   }

   /**
    * Getter method for msExtAppServernameLabel
    */
   public String getExtAppServernameLabel ()
   {
      return msExtAppServernameLabel;
   }
   /**
    * Setter method for msExtAppPasswordLabel
    */
   public void setExtAppServernameLabel(String fsLabel)
   {
      msExtAppServernameLabel = fsLabel;
   }

   /**
    * Getter method for msExtAppTokenLabel
    */
   public String getExtAppTokenLabel ()
   {
      return msExtAppTokenLabel;
   }
   /**
    * Setter method for msExtAppTokenLabel
    */

   public void setExtAppTokenLabel(String fsLabel)
   {
      msExtAppTokenLabel = fsLabel;
   }

   /**
    * Getter method for msExtAppUsernameValue
    */
   public String getExtAppUsernameValue ()
   {
      return msExtAppUsernameValue;
   }

   /**
    * Setter method for msExtAppUsernameValue
    */

   public void setExtAppUsernameValue(String fsValue)
   {
      msExtAppUsernameValue = fsValue;
   }

   /**
    * Getter method for msExtAppPasswordValue
    */
   public String getExtAppPasswordValue ()
   {
      return msExtAppPasswordValue;
   }

   /**
    * Setter method for msExtAppPasswordValue
    */

   public void setExtAppPasswordValue(String fsValue)
   {
      msExtAppPasswordValue = fsValue;
   }

   /**
    * Getter method for msExtAppServernameValue
    */
   public String getExtAppServernameValue ()
   {
      return msExtAppServernameValue;
   }

   /**
    * Setter method for msExtAppServernameValue
    */

   public void setExtAppServernameValue(String fsValue)
   {
      msExtAppServernameValue = fsValue;
   }

   /**
    * Getter method for msExtAppTokenValue
    */
   public String getExtAppTokenValue ()
   {
      return msExtAppTokenValue;
   }

   /**
    * Setter method for msExtAppTokenValue
    */

   public void setExtAppTokenValue(String fsValue)
   {
      msExtAppTokenValue = fsValue;
   }

   /**
    * Getter method for msExtAppUrlValue
    */
   public String getExtAppUrlValue ()
   {
      return msExtAppUrlValue;
   }

   /**
    * Setter method for msExtAppUrlValue
    */

   public void setExtAppUrlValue(String fsValue)
   {
      msExtAppUrlValue = fsValue;
   }
   /**
    * Getter method for msExtAppFrameValue
    */
   public String getExtAppFrameValue ()
   {
      return msExtAppFrameValue;
   }

   /**
    * Setter method for msExtAppFrameValue
    */

   public void setExtAppFrameValue(String fsValue)
   {
      msExtAppFrameValue = fsValue;
   }


}