//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.rmi.RemoteException;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;

/*
**  Report_Driver_Pg*/

//{{FORM_CLASS_DECL
public class Report_Driver_Pg extends Report_Driver_PgBase

//END_FORM_CLASS_DECL}}
{
   // Agent ID for a report
   private String msAgentID     = null ;

   // Report Data and Content Type
   private String msReportText  = null ;
   private String msContentType = null ;
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( Report_Driver_Pg.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // Report name for a particular report
   private String msReportName  = null ;

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Report_Driver_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_Report_Driver_Pg_beforeActionPerformed
void Report_Driver_Pg_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line

   // Get the file extension type and report name
   String lsFileExt = preq.getParameter("file_ext");
   String lsReportNum = preq.getParameter("report_num");

   // Construct the report file(report1, report2,....and so on)
   if ( lsReportNum != null)
   {
      lsFileExt = "_" + lsReportNum + ".html";
   }

   // Set the session properties for VLS
   setSessionProperties("show_html", "", msReportName, lsFileExt);

   // Get the report file(report1, report2,....and so on) from VLS
   msReportText = getReportFile("RPT_HTML", ".html");

   // Reset the session properties
   setSessionProperties("", "", "", "");

   evt.setCancel( true ) ;
   evt.setNewPage( this ) ;
}
//END_EVENT_Report_Driver_Pg_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			Report_Driver_Pg_beforeActionPerformed( ae, evt, preq );
		}
	}
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
    * Set the session properties for later use in VLS
    */
   private void setSessionProperties( String fsShowHTML, String fsShowPDF,
                                      String fsReportName, String fsFileExt )
   {
      VSORBSession loORBSession = getParentApp().getSession().getORBSession();

      try
      {
         loORBSession.setProperty("show_html", fsShowHTML);
         loORBSession.setProperty("show_pdf", fsShowPDF);
         loORBSession.setProperty("report_name", fsReportName);
         loORBSession.setProperty("file_ext", fsFileExt);
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to set message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */
   }

   /**
    * Run the query and get the report data from VLS
    */
   private String getReportFile(String fsDataType, String fsReportExt)
   {
      String lsText = null;

      VSQuery     loQuery ;
      VSResultSet loResultSet ;

      loQuery = new VSQuery( getParentApp().getSession(), "BS_AGENT",
                             "AGNT_ID=" + msAgentID, "" ) ;

      // Fix for the download of binary types
      loQuery.setColumnProjectionLevel( DataConst.ALLTYPES ) ;

      loResultSet = loQuery.execute() ;

      if ( loResultSet != null )
      {
         loResultSet.last() ;

         if ( loResultSet.getRowCount() == 1 )
         {
            lsText  = loResultSet.first().getData( fsDataType ).getString() ;

             msContentType = "text/html; charset=utf-8" ;

            if ( lsText == null )
            {
               raiseException( "Report file not found.", SEVERITY_LEVEL_ERROR ) ;
            } /* end if ( lsText == null ) */
         } /* end if ( loResultSet.getRowCount() == 1 ) */
         else
         {
            raiseException( "Unable to locate agent record.", SEVERITY_LEVEL_ERROR ) ;
         } /* end else */

         loResultSet.close() ;
      } /* end if ( loResultSet == null ) */
      else
      {
         raiseException( "Unable to locate agent record.", SEVERITY_LEVEL_ERROR ) ;
      } /* end else */

      // insert Base URL in HTML page if template exists and return
      if (lsText !=null)
      {
         //REPORTS_BASEURL ="<!--BASEURL-->"
         int liBaseURLStrInd = lsText.indexOf(AMSBatchConstants.REPORTS_BASEURL);
         if (liBaseURLStrInd !=-1)
         {
            return lsText.substring(0,liBaseURLStrInd) +
            "<base href=\""+ getBaseURL() +"\">" +
            lsText.substring(liBaseURLStrInd);
         }
      }
      return lsText;
   }

   public String doAction( PLSRequest foPLSReq )
   {
      String lsResponse ;

      msContentType = null ;
      lsResponse = super.doAction( foPLSReq ) ;

      if ( msReportText != null )
      {
         lsResponse = msReportText ;
         msReportText = null ;
      } /* end if ( msReportText != null ) */

      return lsResponse ;
   } /* end doAction() */

   public String getContentType()
   {
      if ( msContentType != null )
      {
         return msContentType ;
      }
      else
      {
         return super.getContentType() ;
      } /* end else */
   } /* end getContentType */

   /**
    * The generate method gets the index page for a report from VLS
    */
   public String generate()
   {
      // Store the session properties for later use in beforeActionPerformed
      VSORBSession loORBSession = getParentApp().getSession().getORBSession();
      try
      {
         String lsFileExt = null;

         //TextContentElement to set vstext
         TextContentElement loTce;

         lsFileExt    = loORBSession.getProperty("file_ext");

         // Get the index page, only once
         if ( lsFileExt != null && lsFileExt.equals("_index.html"))
         {
            // Get the current row and report name
            msReportName = loORBSession.getProperty("report_name");

            // Get the agent ID from the parent(frameset) page
            Report_Driver_Frameset loSourcePage = (Report_Driver_Frameset) getSourcePage();
            msAgentID = loSourcePage.getAgentID();

            // Get the report file from VLS
            msReportText = getReportFile("RPT_HTML", ".html");
            if ( ( msReportText == null ) ||
                 ( msReportText.trim().length() == 0 ) )
            {
               appendOnloadString( "alert(\'No HTML data was found for this report!\');self.parent.close();" ) ;
            } /* end if ( msReportFile == null ) */

            // Reset the session properties
            setSessionProperties("", "", "", "");

            // Fill the text into the text element to display
            loTce = (TextContentElement)getElementByName("ReportData");
            loTce.setValue(msReportText);
         }
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         raiseException( "Unable to get message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return super.generate() ;
      } /* end catch( RemoteException foExp ) */

      return super.generate() ;
   }

}
