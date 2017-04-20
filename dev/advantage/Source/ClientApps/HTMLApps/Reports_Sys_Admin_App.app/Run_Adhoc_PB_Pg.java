//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import advantage.*;
import com.ams.csf.common.CSFIToken;
import com.ams.csf.common.CSFITokenFactory;
import com.ams.csf.common.CSFManager;
import com.ams.csf.global.CSFConstants;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;
import java.util.HashMap;

import org.apache.commons.logging.Log;

/*
**  Run_Adhoc_PB_Pg
*/

//{{FORM_CLASS_DECL
public class Run_Adhoc_PB_Pg extends Run_Adhoc_PB_PgBase

//END_FORM_CLASS_DECL}}
{
   private static final  String EXT_APP_TOKEN_LABEL = "token";
   private String HTML_END = "</body></html>";
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Run_Adhoc_PB_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public Run_Adhoc_PB_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

   /**
   * This method is used to generate the String representation for the HTML page
   * that opens in a new window to sign in into the Performance Budgeting
   * application.
   * @return The String representation of the HTML page
   */
   public String generate()
   {
      try
      {
         StringBuffer loBf        = new StringBuffer();
         StringBuffer lsbUrlValue = new StringBuffer();
         String lsOriginalTarget  = AMSParams.msPBOriginalTarget;
         String lsAuthService     = AMSParams.msPBAuthServiceFlag;
         String lsExtAppUrlValue  = AMSParams.msPBURLforSSO;

         if(AMSStringUtil.strIsEmpty(lsExtAppUrlValue))
         {
            raiseException("The PB application URL cannot be left blank.",SEVERITY_LEVEL_ERROR);
         }

         lsbUrlValue.append(lsExtAppUrlValue);

         //Add the Original Target to the URL.
         if(!AMSStringUtil.strIsEmpty(lsOriginalTarget))
         {
            lsbUrlValue.append("?originalTarget=").append(lsOriginalTarget);
         }

         //Add the Auth Service Flag to the URL.
         if(!AMSStringUtil.strIsEmpty(lsAuthService))
         {
            lsbUrlValue.append("&authServiceFlag=").append(lsAuthService);
         }

         //Get the SAML token to be passed to PB
         String lsExtAppTokenValue = getSAMLToken();

          if(AMS_PLS_DEBUG)
          {
               System.out.println("External Application Token Value :\n" + lsExtAppTokenValue);
          }

          //Create the HTML.
          loBf.append(getHTMLContent()).append("\n");
          loBf.append("<form action=\"").append(lsbUrlValue.toString())
                 .append("\" method=\"post\"").append(" >\n");
          loBf.append("<input type=\"hidden\" name=\"")
                 .append(EXT_APP_TOKEN_LABEL).append("\" value=\"")
                 .append(lsExtAppTokenValue).append("\">\n").append("</form>\n");
          loBf.append(HTML_END);

          if(AMS_PLS_DEBUG)
          {
               System.out.println("the Complete HTML is \n"+loBf.toString());
          }
          return loBf.toString();
       }
       catch(Exception loe)
       {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loe);

          raiseException( "Errors encountered while launching the Performance Budgeting Application",
                                SEVERITY_LEVEL_INFO ) ;
          return super.generate();
       }
   }

   /**
    * This method returns the SAML token for logging into the PB Application.
    * @return String representation of the generated Token.
    */
   private final String getSAMLToken()
   {
      String lsSAMLToken       = null;
      HashMap loUserAttributes = new HashMap();
      String lsTokenClass      = null;
      String lsUserName        = null;
      CSFITokenFactory loTokenFactory;
      CSFIToken loToken;
      PLSApp loPlsApp;
      AMSSecurityObject loSecObj;
      try
      {
         loPlsApp   = getParentApp();
         loSecObj = (AMSSecurityObject) loPlsApp.getSession().getSecurityObject();
         loSecObj.setStandAloneLogin(true);

         //Create the SAML Token, and set the user id as an attribute.
         lsUserName = getParentApp().getSession().getLogin();
         loUserAttributes.put("user_id",lsUserName);
         lsTokenClass = (String) CSFManager.getInstance().getPreferences().getTokenClassImpl();
         CSFManager.getInstance().getPreferences().setPreference(null,
               CSFConstants.PREF_TOKEN_IMPL_CLASS, "com.ams.csf.auth.CSFSAMLToken");
         loTokenFactory = CSFManager.getInstance().getTokenFactory(
               "ADVANTAGEPB");
         loToken = loTokenFactory.createToken(lsUserName, CSFConstants.AUTH_METHOD_DBMS,
               loUserAttributes );
         lsSAMLToken = loToken.exportToken();

         if(AMS_PLS_DEBUG)
         {
              System.out.println("Unencoded Token -         > " + lsSAMLToken);
         }

         //Encode the token
         lsSAMLToken = CSFManager.getInstance().getEncoder().encode(lsSAMLToken);
      }
      catch (Exception e)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", e);

         raiseException("Could not generate the Security Token.",SEVERITY_LEVEL_ERROR);
      }
      finally
      {
         try
         {
            //Set the Token Class back.
            CSFManager.getInstance().getPreferences().setPreference(null,
                  CSFConstants.PREF_TOKEN_IMPL_CLASS, lsTokenClass);
         }
         catch (Exception e)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", e);

            raiseException(
                  "Unable to revert back the Token class, please contact your System Administrator. ",
                  SEVERITY_LEVEL_ERROR);
         }
      }
      return lsSAMLToken;
   } // of method getSAMLtoken

  /**
  * Creates the HTML content for the page
  */
  public String getHTMLContent()
   {
	   StringBuffer lsbHtmlContent = new StringBuffer(150);
	   lsbHtmlContent.append("<html><head>\n");
	   lsbHtmlContent.append("<title>ADVANTAGE Single Sign-on  to PB</title>\n");
	   lsbHtmlContent.append("<base href=\"" + this.getBaseURL() + "\">\n");
	   lsbHtmlContent.append("<script language=\"JavaScript\"> \n");
	   lsbHtmlContent.append("<!--\n");
	   lsbHtmlContent.append("function submitme() \n");
	   lsbHtmlContent.append("{\n");
	   lsbHtmlContent.append("      var loNewWin;\n");
	   lsbHtmlContent.append("      var lsHref = UTILS_getBaseHref( document ) + '../AMSImages/Empty.htm';\n");
	   lsbHtmlContent.append("      var loDate = new Date();\n");
	   lsbHtmlContent.append("      var lsWindowName = 'ADV' + loDate.getTime();\n");
	   lsbHtmlContent.append("     loNewWin = DHTMLLIB_OpenNewWin(lsHref, lsWindowName,\n");
	   lsbHtmlContent.append("               'no' , 'no' , 'no', 'yes' , 'no',\n");
	   lsbHtmlContent.append("                'yes', 'yes', null,\n");
	   lsbHtmlContent.append("                  null, 0, 0, 'yes' );\n");
	   lsbHtmlContent.append("       document.forms[0].target = lsWindowName;\n");
	   lsbHtmlContent.append("  document.forms[0].submit(); \n");
	   lsbHtmlContent.append("}\n");
	   lsbHtmlContent.append("   --> \n");
	   lsbHtmlContent.append(" </script> \n");
	   lsbHtmlContent.append("<script type=\"text/javascript\" language=\"JavaScript\"");
	   lsbHtmlContent.append(" src=\"../AMSJS/AMSDHTMLLib.js\"></script>\n");
	   lsbHtmlContent.append("<script type=\"text/javascript\" language=\"JavaScript\"");
	   lsbHtmlContent.append(" src=\"../AMSJS/AMSUtils.js\"></script>\n");
	   lsbHtmlContent.append("</head>\n");
       lsbHtmlContent.append(" <body onload=\"UTILS_EnableClicks();UTILS_InitPage();UTILS_HideDocNavPanel;submitme()\">");
       return lsbHtmlContent.toString();

   }

}