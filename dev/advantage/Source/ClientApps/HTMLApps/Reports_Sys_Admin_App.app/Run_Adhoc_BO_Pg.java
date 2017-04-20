//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	import com.amsinc.gems.adv.common.*;

	import java.rmi.RemoteException;
	import java.net.*;
	import com.ams.csf.base.*;
	import com.ams.csf.auth.*;
	import com.ams.csf.common.*;
	import java.util.*;
	import com.amsinc.gems.adv.vfc.html.*;

import org.apache.commons.logging.Log;


	/*
	**  Run_Adhoc_BO_Pg*/

	//{{FORM_CLASS_DECL
	public class Run_Adhoc_BO_Pg extends Run_Adhoc_BO_PgBase
	
	//END_FORM_CLASS_DECL}}
	{

	   /* variable name used in the app login page */

	   private String msExtAppTokenLabel = "token";
	   private String msExtAppTokenValue = "";
	   private String msExtAppUrlValue = "";
	   private String msExtAppFrameValue = "_BOWINDOW";

	   private static String LINE_SEP = "\n";

	   private String HTML_END = "</body></html>";
	   
	   /** This is the logger object */
	   private static Log moAMSLog = AMSLogger.getLog( Run_Adhoc_BO_Pg.class,
	      AMSLogConstants.FUNC_AREA_DFLT ) ;



	 // Declarations for instance variables used in the form

	 // This is the constructor for the generated form. This also constructs
	 // all the controls on the form. Do not alter this code. To customize paint
	 // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Run_Adhoc_BO_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

	      String lsToken = null;

	      try
	      {
	         if ( AMSParams.mboolIsBOIntegrationEnabled )
	         {

	            /*
	             * load a self-submitting html page
	             */

	            loBf.append(getHTMLHeader()).append("\n");

	            loBf.append("<form action=\"").append(getBOStartURL())
	            .append("\" method=\"post\" ")
	            .append(">\n");


	            loBf.append("<input type=\"hidden\" name=\"")
	            .append("token").append("\" value=\"")
	            .append(getSAMLToken().toString()).append("\">\n");
	            loBf.append("</form>\n");

	            loBf.append(HTML_END);



	            if(AMS_PLS_DEBUG)
	            {
	               System.out.println("show me the page\n" + loBf.toString());
	            }

	            raiseException( "Launching the infoADVANTAGE Portal in a new window...",
	                               SEVERITY_LEVEL_INFO ) ;

	            return loBf.toString();

	       }//if ( AMSParams.mboolIsBOIntegrationEnabled )
	       else
	          {
	         raiseException( "infoADVANTAGE integration is not enabled. Please contact your System Administrator.",
	                                   SEVERITY_LEVEL_ERROR ) ;
	         return super.generate() ;
	          }

	      }
	      catch(Exception loe)
	      {
	         raiseException( "Could not connect to infoADVANTAGE. Please contact your System Administrator.",
	                         SEVERITY_LEVEL_ERROR ) ;


	         if ( AMS_PLS_DEBUG )
	         {
	            System.err.println("Exception while trying to open infoADVANTAGE Window:");
                // Add exception log to logger object
                moAMSLog.error("Unexpected error encountered while processing. ", loe);
	         }


	         return "";
	         //return super.generate();
	      }

	   }// end of generate


	   private String getHTMLHeader()
	   {
	      StringBuffer loBuf = new StringBuffer(64*32);

	      loBuf.append("<html><head>").append(LINE_SEP);

	         loBuf.append("<title>infoADVANTAGE</title>").append(LINE_SEP);
	         loBuf.append("<base href=\"" + this.getBaseURL() + "\">").append(LINE_SEP);

	         loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\"").append(LINE_SEP);
	         loBuf.append(" src=\"../AMSJS/AMSDHTMLLib.js\"></script>").append(LINE_SEP);
	         loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\"").append(LINE_SEP);
	         loBuf.append(" src=\"../AMSJS/AMSUtils.js\"></script>").append(LINE_SEP);

	         loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\">").append(LINE_SEP);
	         loBuf.append("<!--").append(LINE_SEP);



	         loBuf.append("function submitme() ").append(LINE_SEP);
	         loBuf.append("{").append(LINE_SEP);
	         loBuf.append("      var loNewWin;").append(LINE_SEP);
	         loBuf.append("      var lsHref = '").append(getBOStartURL()).append("';").append(LINE_SEP);
	         loBuf.append("      var loDate = new Date();").append(LINE_SEP);
	         loBuf.append("      var lsWindowName = 'ADV' + loDate.getTime();").append(LINE_SEP);
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
	    * Returns the Business Objects portal start URL
	    */
	   private String getBOStartURL()
	   {
	      if(msExtAppUrlValue==null || msExtAppUrlValue.trim().length()==0)
	      {
	         StringBuffer loBuf = new StringBuffer(64*2);
	         String lsBaseBOURL = AMSParams.msBOServerName;
	             String lsLoginPgName = AMSParams.msBOLoginPgName;
	             loBuf.append(lsBaseBOURL);
	         loBuf.append(lsLoginPgName);
	         msExtAppUrlValue = loBuf.toString();


	      }//if(msExtAppUrlValue==null

	      return msExtAppUrlValue;
	   } // of method getBOStartURL()




	   /**
	    * Returns the SAML token after associating it with
	    * the session
	    */
	   private final String getSAMLToken()
	   {
	      VSORBSession         loORBSession = parentApp.getSession().getORBSession() ;
	      AMSSecurityObject    loSecObj = null ;

	      try
	      {
	         loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
	         return loSecObj.createCSFToken();
	      }
	      catch ( Exception loExp )
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         raiseException( "Security validations failed.",
	               AMSPage.SEVERITY_LEVEL_SEVERE ) ;
	      }


	      return null;

	   } // of method getSAML token




	}
