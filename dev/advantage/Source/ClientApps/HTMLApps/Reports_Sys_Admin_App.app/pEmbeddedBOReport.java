//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import org.apache.commons.logging.Log;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSLogger;
/*
**  pEmbeddedBOReport*/

//{{FORM_CLASS_DECL
public class pEmbeddedBOReport extends pEmbeddedBOReportBase

//END_FORM_CLASS_DECL}}
{

   /* variable name used in the app login page */

   private String msExtAppUrlValue = "";

   /** Get the logger instance */
   private static Log moLog = AMSLogger.getLog(pEmbeddedBOReport.class,
         AMSLogConstants.FUNC_AREA_INFO_REPORTS);


 // Declarations for instance variables used in the form

 // This is the constructor for the generated form. This also constructs
 // all the controls on the form. Do not alter this code. To customize paint
 // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pEmbeddedBOReport ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

public HTMLDocumentSpec getDocumentSpecification()
{
   return getDefaultDocumentSpecification();
}

public String getFileName()
{
   return getDefaultFileName();
}

public String getFileLocation()
{
   return getPageTemplatePath();
}

public void afterPageInitialize()
{
   super.afterPageInitialize();
   //Write code here for initializing your own control
   //or creating new control.
}

   /**
    * Returns the Business Objects portal start URL
    */
   private String getBOStartURL()
   {
      if (msExtAppUrlValue == null || msExtAppUrlValue.trim().length() == 0)
      {
         StringBuffer loBuf = new StringBuffer(64 * 2);
         String lsBaseBOURL = AMSParams.msBOServerName + ":"
               + AMSParams.msBOServerPort;
         String lsLoginPgName = AMSParams.msBOReportPgName;
         loBuf.append(lsBaseBOURL);
         loBuf.append(lsLoginPgName);
         msExtAppUrlValue = loBuf.toString();

      }// if(msExtAppUrlValue==null
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
   catch ( Exception foExeption )
   {
      moLog.error("Unexpected error encountered while processing. ", foExeption);
      raiseException( "Security validations failed.",
            AMSPage.SEVERITY_LEVEL_SEVERE ) ;
   }
   return null;

} // of method getSAML token

@Override
public void beforeGenerate()
{
   if(getFirstTimeGenFlag())
   {
      if(getSourcePage() instanceof com.amsinc.gems.adv.vfc.html.AMSStartupPage)
      {
         setAddBackLink(false);
      }
      else
      {
         setAddBackLink(true);
      }
   }

   AMSStartupPage loStartupPage = AMSStartupPage.getStartupPage(getParentApp());
   loStartupPage.setInfoReportPage(this);

   super.beforeGenerate();
   try
   {
      String lsReportName = null;
      String lsReportPath = null;
      String lsReportType = null;

      if( AMSParams.mboolIsBOIntegrationEnabled )
      {
         AMSStartupPage loStartPage = AMSStartupPage.getStartupPage( getParentApp());

         // Retrieving the infoAdvantage report name , report type , report path
         lsReportName = loStartPage.getInfoReportName();
         lsReportPath = loStartPage.getInfoReportPath();
         lsReportType = loStartPage.getInfoReportType();

         //Storing infoAdvantage Report Name, Report Path and Report Type
         setInfoReportName(lsReportName);
         setInfoReportPath(lsReportPath);
         setInfoReportType(lsReportType);

         //create infoAdvantage Report URL
         String lsBoStarURL = getBOStartURL() + "?REPORT_TYPE="
               + lsReportType + "&REPORT_NAME=" + lsReportName
               + "&REPORT_PATH=" + lsReportPath;

         //Set the csf token value
         HiddenElement loTokenElem = (HiddenElement)getElementByName( "token" );
         if(loTokenElem != null)
         {
            loTokenElem.setValue( getSAMLToken().toString() );
         }
         
         HiddenElement loReloadFrame = (HiddenElement)getElementByName( "ReloadFrame" );
         if(loReloadFrame != null)
         {
            loReloadFrame.setValue( String.valueOf(doRefreshInfoFrame() ));
            //Reset the flag to reload the frame always
            setRefreshInfoFrame(true);
         }

         //set infoAdvantage Report URL
         HiddenElement loInfoAction = (HiddenElement)getElementByName( "InfoAction" );
         if(loInfoAction != null)
         {
            loInfoAction.setValue( lsBoStarURL );
         }
      }
      else
      {
         raiseException(
               "infoADVANTAGE integration is not enabled. Please contact your System Administrator.",
               SEVERITY_LEVEL_ERROR );
      }
   }
   catch( Exception foException )
   {
      moLog.error("Unexpected error encountered while processing. ", foException);
      raiseException(
            "Could not connect to infoADVANTAGE. Please contact your System Administrator.",
            SEVERITY_LEVEL_ERROR );
   }
}

}
