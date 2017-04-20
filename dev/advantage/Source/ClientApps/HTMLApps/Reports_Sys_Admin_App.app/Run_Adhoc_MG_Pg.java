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

import com.ams.csf.common.CSFException;
import com.ams.csf.common.CSFIToken;
import com.ams.csf.common.CSFITokenFactory;
import com.ams.csf.common.CSFManager;
import com.ams.csf.common.CSFManagerException;
import com.ams.csf.global.CSFConstants;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.commons.logging.Log;


/*
**  Run_Adhoc_MG_Pg
*/

//{{FORM_CLASS_DECL
public class Run_Adhoc_MG_Pg extends Run_Adhoc_MG_PgBase

//END_FORM_CLASS_DECL}}
{
   private String msExtAppTokenLabel = "SamlResponse";

   private static String LINE_SEP = "\n";

   private String HTML_END = "</body></html>";

   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Run_Adhoc_MG_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   
   
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public Run_Adhoc_MG_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

         loBf.append("<form action=\"").append(getMGStartURL())
         .append("\" method=\"post\" ")
         .append(">\n");


         loBf.append("<input type=\"hidden\" name=\"")
         .append(msExtAppTokenLabel).append("\" value=\'")
         .append(getSAMLToken().toString()).append("\'>\n");
         loBf.append("</form>\n");

         loBf.append(HTML_END);



         if(AMS_PLS_DEBUG)
         {
            System.out.println("show me the page\n" + loBf.toString());
         }

         raiseException( "Launching the Meridian Global in a new window...",
                            SEVERITY_LEVEL_INFO ) ;

         return loBf.toString();



      }
      catch(Exception loe)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loe);

         raiseException( "Could not connect to Meridian Global. Please contact your System Administrator.",
                         SEVERITY_LEVEL_ERROR ) ;

         if ( AMS_PLS_DEBUG )
         {
            System.err.println("Exception while trying to open Meridian Global Window:");
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loe);
         }




         return "";
         //return super.generate();
      }

   }// end of generate

   /**
    * Returns the Meridian Global Login Page URL
    */
   private String getMGStartURL()
   {
      return AMSParams.msMeridianGlobalLoginPagePath;
   } // of method getBOStartURL()

   private String getHTMLHeader()
   {
      StringBuffer loBuf = new StringBuffer(64*32);

      loBuf.append("<html><head>").append(LINE_SEP);

      loBuf.append("<title>Meridian Global</title>").append(LINE_SEP);
      loBuf.append("<base href=\"" + this.getBaseURL() + "\">").append(LINE_SEP);

      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\"").append(LINE_SEP);
      loBuf.append(" src=\"../AMSJS/AMSDHTMLLib.js\"></script>").append(LINE_SEP);
      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\"").append(LINE_SEP);
      loBuf.append(" src=\"../AMSJS/AMSUtils.js\"></script>").append(LINE_SEP);

      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\">").append(LINE_SEP);
      loBuf.append("<!--").append(LINE_SEP);

      String lsPopupWin = null;
      try
      {
         lsPopupWin = getParentApp().getSession().getORBSession().getProperty("POPUP_WIN");
         getParentApp().getSession().getORBSession().removeProperty("POPUP_WIN");
      }
      catch (RemoteException foException)
      {
         moAMSLog.error(foException);
      }

      loBuf.append("function submitme() ").append(LINE_SEP);
      loBuf.append("{").append(LINE_SEP);
      loBuf.append("      var loNewWin;").append(LINE_SEP);
      loBuf.append("      var lsHref = '").append(getMGStartURL()).append("';").append(LINE_SEP);
      loBuf.append("      var loDate = new Date();").append(LINE_SEP);
      if(AMSStringUtil.strIsEmpty(lsPopupWin))
      {
         loBuf.append("      var lsWindowName = 'ADV' + loDate.getTime();").append(LINE_SEP);
      }
      else
      {
         loBuf.append("      var lsWindowName = \'").append(lsPopupWin).append("\'").append(LINE_SEP);
      }
      loBuf.append("      loNewWin = DHTMLLIB_OpenNewWin('', lsWindowName,").append(LINE_SEP);
      loBuf.append("                'no' , 'no' , 'no', 'yes' , 'no',").append(LINE_SEP);
      loBuf.append("                'yes', 'yes', null,").append(LINE_SEP);
      loBuf.append("                  null, 0, 0, 'yes' );").append(LINE_SEP);
      loBuf.append("       document.forms[0].target = lsWindowName;").append(LINE_SEP);

      loBuf.append("  document.forms[0].submit(); ").append(LINE_SEP);
      loBuf.append("};").append(LINE_SEP);
      loBuf.append("   --> ").append(LINE_SEP);
      loBuf.append(" </script> ").append(LINE_SEP);

      loBuf.append(" </head>").append(LINE_SEP);
      loBuf.append(" <body onload=\"UTILS_EnableClicks();UTILS_InitPage();UTILS_HideDocNavPanel;submitme()\">").append(LINE_SEP);

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

         VSQuery loQuery = new VSQuery(getParentApp().getSession(),
               "ESS_EMPL_USER_ID", "USER_ID = "
                     + AMSSQLUtil.getANSIQuotedStr(getParentApp().getSession()
                           .getLogin(), true), "");

         VSResultSet lrsESSUserInfo = loQuery.execute();
         lrsESSUserInfo.last();
         if (lrsESSUserInfo.getRowCount() > 0)
         {
            CSFManager.getInstance().getPreferences().setPreference(null,
                  CSFConstants.PREF_TOKEN_IMPL_CLASS,
                  "com.amsinc.gems.adv.common.AdvMeridianSAMLToken");
            loTokenFactory = CSFManager.getInstance().getTokenFactory(
                  "ADVANTAGEIDP");
            loToken = loTokenFactory.createToken(lrsESSUserInfo.first()
                  .getData("INTERNAL_EMPL_ID").getString(),
                  CSFConstants.AUTH_METHOD_X509, loUserAttributes);

            lsSAMLToken = loToken.exportToken();
         }
         else
         {
            raiseException(
                  "Unable to retrieve user security information. Please contact the System Administrator.",
                  SEVERITY_LEVEL_ERROR);
         }

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