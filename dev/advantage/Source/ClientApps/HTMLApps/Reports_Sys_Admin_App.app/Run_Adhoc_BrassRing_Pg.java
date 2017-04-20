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
import advantage.Reports_Sys_Admin_App.Run_Adhoc_BrassRing_PgBase;

import com.ams.csf.common.CSFException;
import com.ams.csf.common.CSFIToken;
import com.ams.csf.common.CSFITokenFactory;
import com.ams.csf.common.CSFManager;
import com.ams.csf.common.CSFManagerException;
import com.ams.csf.global.CSFConstants;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;


/*
**  Run_Adhoc_MG_Pg
*/

//{{FORM_CLASS_DECL
public class Run_Adhoc_BrassRing_Pg extends Run_Adhoc_BrassRing_PgBase

//END_FORM_CLASS_DECL}}
{
	private String msSecRealm = "ADVANTAGEBRASSRINGIDP";
   private String msExtAppTokenLabel = "SAMLResponse";

   private static String LINE_SEP = "\n";

   private String HTML_END = "</body></html>";
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Run_Adhoc_BrassRing_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public Run_Adhoc_BrassRing_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

      try
      {
         /*
          * load a self-submitting html page
          */

         loBf.append(getHTMLHeader()).append("\n");

         loBf.append("<form action=\"").append(getBRStartURL()).append(
               "\" method=\"post\"").append(">\n");

         String lsToken = getSAMLToken().toString();

         loBf.append("<input type=\"hidden\" name=\"").append(
               msExtAppTokenLabel).append("\" value=\'").append(lsToken
               ).append("\'>\n");
         loBf.append("</form>\n");

         loBf.append(HTML_END);

         if (AMS_PLS_DEBUG)
         {
            System.out.println("show me the page\n" + loBf.toString());
         }

         raiseException("Launching Brass Ring in a new window...",
               SEVERITY_LEVEL_INFO);
         /*Date loDate = new Date();
         FileWriter loWriter = new FileWriter("AdvToken.xml");
         loWriter.write(lsToken + "\n " + loDate.toString());
         loWriter.flush();
         loWriter.close();*/

         return loBf.toString();

      }
      catch (Exception loe)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loe);

         raiseException(
               "Could not connect to Brass Ring. Please contact your System Administrator.",
               SEVERITY_LEVEL_ERROR);

         if (AMS_PLS_DEBUG)
         {
            System.err
                  .println("Exception while trying to open Brass Ring Window:");
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loe);
         }



         return "";
         // return super.generate();
      }

   }// end of generate

   /**
    * Returns the Meridian Global Login Page URL
    */
   private String getBRStartURL()
   {
      return AMSParams.msBrassRingLoginPagePath;
   } // of method getBOStartURL()

   private String getHTMLHeader()
   {
      StringBuffer loBuf = new StringBuffer(64 * 32);

      loBuf.append("<html><head>").append(LINE_SEP);
      loBuf.append("<title>Brass Ring</title>").append(LINE_SEP);
      loBuf.append("<base href=\"" + this.getBaseURL() + "\">")
            .append(LINE_SEP);
      loBuf.append("<link rel=\"STYLESHEET\" type=\"text/css\" href=\"../AMSImages/ADVPortalStyle.css\"/>");
      loBuf.append("<link rel=\"STYLESHEET\" type=\"text/css\" href=\"../AMSImages/ADVMenuStyle.css\"/>");
      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\"")
            .append(LINE_SEP);
      loBuf.append(" src=\"../AMSJS/AMSDHTMLLib.js\"></script>").append(
            LINE_SEP);
      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\"")
            .append(LINE_SEP);
      loBuf.append(" src=\"../AMSJS/AMSUtils.js\"></script>").append(LINE_SEP);

      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\">")
            .append(LINE_SEP);
      loBuf.append("<!--").append(LINE_SEP);

      loBuf.append("function submitme() ").append(LINE_SEP);
      loBuf.append("{").append(LINE_SEP);
      loBuf.append("      var loNewWin;").append(LINE_SEP);
      loBuf.append("      var lsHref = '").append(getBRStartURL()).append("';")
            .append(LINE_SEP);
      loBuf.append("      var loDate = new Date();").append(LINE_SEP);
      loBuf.append("      var lsWindowName = 'ADV' + loDate.getTime();")
            .append(LINE_SEP);
      loBuf.append("      loNewWin = DHTMLLIB_OpenNewWin('', lsWindowName,")
            .append(LINE_SEP);
      loBuf.append("                'no' , 'no' , 'no', 'yes' , 'no',").append(
            LINE_SEP);
      loBuf.append("                'yes', 'yes', null,").append(LINE_SEP);
      loBuf.append("                  null, 0, 0, 'yes' );").append(LINE_SEP);
      loBuf.append("       document.forms[0].target = lsWindowName;").append(
            LINE_SEP);

      loBuf.append("  document.forms[0].submit(); ").append(LINE_SEP);
      loBuf.append("};").append(LINE_SEP);
      loBuf.append("   --> ").append(LINE_SEP);
      loBuf.append(" </script> ").append(LINE_SEP);

      loBuf.append(" </head>").append(LINE_SEP);
      loBuf.append(" <body class=\"adv homepage\" style=\"height:auto;\" oncontextmenu=\"MNU_ShowPopup('HomePage', event);return false\" ")
            .append("onunload=\"UTILS_CloseOpenWindows();\" onload=\"UTILS_EnableClicks();UTILS_InitPage();UTILS_HideDocNavPanel;submitme()\">")
            .append(LINE_SEP);

      return loBuf.toString();
   }

   /**
    * Returns the SAML token after associating it with
    * the session
    */
   private final String getSAMLToken()
   {
      CSFIToken loToken;
      String lsSAMLToken = null;
      CSFITokenFactory loTokenFactory;
      HashMap loUserAttributes = null;
      String lsTokenClass = null;

      // determine if we're to do a direct transition to a specific page
      PLSApp loPlsApp = getParentApp();
      AMSSecurityObject loSecObj = (AMSSecurityObject) loPlsApp.getSession()
            .getSecurityObject();
      // lsDestination = ((AMSSecurityObject)loSecObj).getDestination() ;

      loSecObj.setStandAloneLogin(true);

      try
      {
         lsTokenClass = (String) CSFManager.getInstance().getPreferences()
               .getTokenClassImpl();

         String lsTokenImplClass = CSFManager.getInstance().getPreferences()
               .getPreference(msSecRealm, CSFConstants.PREF_TOKEN_IMPL_CLASS)
               .toString();

         CSFManager.getInstance().getPreferences().setPreference(null,
               CSFConstants.PREF_TOKEN_IMPL_CLASS, lsTokenImplClass);

         loTokenFactory = CSFManager.getInstance().getTokenFactory(msSecRealm);

         loUserAttributes = new HashMap();
         loUserAttributes.put("SiteId", "0");
         loUserAttributes.put("Application", "1");

         loToken = loTokenFactory.createToken(getUserID(),
               CSFConstants.AUTH_METHOD_X509, loUserAttributes);

         lsSAMLToken = loToken.exportToken();

         //ArrayList loErrors = new ArrayList();
         //loToken.verify(loErrors);
      }
      catch (Exception e)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

      }
      finally
      {
         try
         {
            CSFManager.getInstance().getPreferences().setPreference(null,
                  CSFConstants.PREF_TOKEN_IMPL_CLASS, lsTokenClass);
         }
         catch (Exception e)
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", e);

            raiseException(
                  "Unable to set token class back. Need to bouce server. Please contact the System Administrator.",
                  SEVERITY_LEVEL_ERROR);
         }
      }

      return lsSAMLToken;

   } // of method getSAMLtoken

}