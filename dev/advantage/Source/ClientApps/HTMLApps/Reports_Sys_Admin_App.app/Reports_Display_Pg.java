//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	import java.net.*;
	import java.rmi.RemoteException;
	import advantage.AMSBatchUtil;
	import com.amsinc.gems.adv.common.*;
	import com.amsinc.gems.adv.vfc.html.*;
import org.apache.commons.logging.Log;

	/*
	 **  Reports_Display_Pg*/

	//{{FORM_CLASS_DECL
	public class Reports_Display_Pg extends Reports_Display_PgBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSBusinessObjectsIntegrationConstants
	{
	   private String msBOReportName = null;
	   private String msBOReportType = null;
	   private String msBOReportId   = null;
	   private String msBOReportRepositoryType =null;

	   public static final String ATTR_BO_RPT_NM_AND_ID_COL = "JOB_OTPT_LOC";
      /** Variable to indicate if Error message has to be passed back to page */
      private boolean mboolSendErrorText;

	   private String msReportIdentifier = "";
	   
	   /** This is the logger object */
	   private static Log moAMSLog = AMSLogger.getLog( Reports_Display_Pg.class,
	      AMSLogConstants.FUNC_AREA_DFLT ) ;




	   //    Declarations for instance variables used in the form

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Reports_Display_Pg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_Reports_Display_Pg_beforeActionPerformed
void Reports_Display_Pg_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line

   // Get the report name
   String lsReportName = preq.getParameter("report_name");

   // Check to see if the report type is PDF or HTML
   if (ae.getName() != null && ae.getName().equals("show_pdf"))
   {
      // Set the session properties for VLS
      setSessionProperties("", "show_pdf", lsReportName, ".pdf");

      // Get the report file from VLS
      getReportFile("RPT_PDF", ".pdf");

      // Reset the session properties
      setSessionProperties("", "", "", "");

      evt.setCancel( true ) ;
      evt.setNewPage( getSourcePage() ) ;
   } /* end if (ae.getName() != null && ae.getName().equals("show_pdf")) */
   else if (ae.getName() != null && ae.getName().equals("show_html"))
   {
      // Set the session properties for VLS
      setSessionProperties("show_html", "", lsReportName, "_index.html");

      // Get the next page which is Report Driver Frameset page
      Report_Driver_Frameset loTransPage = getNextPage();
      loTransPage.setAgentID( getJobID() );

      evt.setCancel( true ) ;
      evt.setNewPage( loTransPage ) ;
   } /* end else */
}
//END_EVENT_Reports_Display_Pg_beforeActionPerformed}}

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
			Reports_Display_Pg_beforeActionPerformed( ae, evt, preq );
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
       * Sets stream with information of the PDF file that needs to be displayed.
       * If PDF file is not found then sets mboolSendErrorText to true to
       * display message to indicate that PDF file was not found.
       */
      private void getReportFile(String foDataType, String foReportExt)
      {
         VSQuery     loQuery ;
         VSResultSet loResultSet ;
         String lsReportFileName;

         loQuery = new VSQuery( getParentApp().getSession(), "BS_AGENT",
                                "AGNT_ID=" + getJobID(), "" ) ;

         // Fix for the download of binary types
         loQuery.setColumnProjectionLevel( DataConst.ALLTYPES ) ;

         loResultSet = loQuery.execute() ;

         if ( loResultSet != null )
         {
            loResultSet.last() ;

            if ( loResultSet.getRowCount() == 1 )
            {
               //Get File Name
               lsReportFileName  = loResultSet.first().getData( foDataType ).getString() ;
               if ( ( lsReportFileName == null ) ||
                    ( lsReportFileName.trim().length() == 0 ) )
               {
                  //If File is not found then send Error message back to page
                  mboolSendErrorText = true;
               } /* end if ( msReportFile == null ) */
               else
               {
                  mboolSendErrorText = false;
                  //Display the file contents on the page(inline) with
                  //efficient streaming
                  setStreamFileInfo( "application/pdf", lsReportFileName, false ) ;
               }
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
      }

	   /**
	    * Build the transition and get the next page (frameset)
	    */
	   private Report_Driver_Frameset getNextPage()
	   {
	      String lsDestination      = "Report_Driver_Pg";
	      String lsFramesetPageName = "Report_Driver_Frameset";
	      String lsFrameName        = "Master";

	      Report_Driver_Frameset loTransPage;
	      AMSDynamicTransition   loDynTran ;

	      // Create an instance of the dynamic transition
	      loDynTran = new AMSDynamicTransition( lsDestination, "",
	                                           "Reports_Sys_Admin_App" ) ;

	      // Set the frameset page name and frame name
	      if ( ( lsFramesetPageName != null ) && ( lsFrameName != null ) )
	      {
	         loDynTran.setFramesetPageName( lsFramesetPageName ) ;
	         loDynTran.setFrameName( lsFrameName ) ;
	      } /* end if ((lsFramesetPageName!=null) && (lsFrameName != null) ) */

	      // Set the reference to the source page
	      loDynTran.setSourcePage( this );

	      // create the transition page
	      loTransPage = (Report_Driver_Frameset) loDynTran.getVSPage( getParentApp(), getSessionId() );

	      return loTransPage;
	   }

	   public String doAction( PLSRequest foPLSReq )
	   {
	      String lsResponse ;

	      lsResponse = super.doAction( foPLSReq ) ;
                   //Display error message for case where File to display was not found
                   if ( mboolSendErrorText )
                  {
                     lsResponse = getErrorText();
                     mboolSendErrorText = false;
	     }
	      return lsResponse ;
	   } /* end doAction() */


	  /**
	   * This method overrides the method in  VSPage. It gets a list of generated output report files
	   * and forms links to those files
	   * @return String  The generated HTML
	   * @author Mukul Gupta
	   */
	   public String generate()
	   {
	      TextContentElement loTce = null;
	      String loText            = null;
	      try
	      {
	         // Check to see if the report type is a Business Object Report
	         if ( getJobClassName().equals("BusObjReport") )
	         {
	            StringBuffer loTextBuffer  = new StringBuffer();
	            StringBuffer lsbBOHtmlLink = new StringBuffer(256);

	            boolean lboolValidReportDataSaved = setBOReportIdentifier();

	            VSORBSession loORBSession = parentApp.getSession().getORBSession();
	            String  lsUserID = loORBSession.getUserName();
	            AMSUser loUser = AMSSecurity.getUser( lsUserID ) ;
	            String  lsPassword = loUser.getBOPassword();

	            if ( !AMSParams.mboolIsBOIntegrationEnabled ) /* Is BO integration enabled?*/
	            {
	               loTextBuffer.append("<TR class=\""+"advgridoddrow\"" + ">").append("\n\t");
	               loTextBuffer.append("<TD>");
	               loTextBuffer.append("infoADVANTAGE integration is not enabled.");
	               loTextBuffer.append(" Please contact your System Administrator.");
	               loTextBuffer.append("</TD>");
	               loTextBuffer.append("\n\t");
	               loTextBuffer.append("</TR>");
	            }
	            else if ( !loUser.getStdRptUserFl() ) /* Is user authorized? */
	            {
	               loTextBuffer.append("<TR class=\""+"advgridoddrow\"" + ">").append("\n\t");
	               loTextBuffer.append("<TD>");
	               loTextBuffer.append("You are not authorized to view infoADVANTAGE reports.");
	               loTextBuffer.append("</TD>");
	               loTextBuffer.append("\n\t");
	               loTextBuffer.append("</TR>");
	            }
	            else
	            {

	               if (lboolValidReportDataSaved)
	               {

				      /*
				       * The HTML page has the second FORM element for which the
				       * action field will be filled now.  Also the following
				       * will be populated
				       * the security token
				       * the report identifier
				       */

	                  BoundElement loFormE =
	                    getDocumentModel().getElementByName("infoADVANTAGE_displayreport");
	                  if (loFormE !=null)
	                  {
	                     UnknownScalarElement loFormElem = (UnknownScalarElement)loFormE;
	                     loFormElem.addAttribute("action",getBOStartURL());
	                  }


	                  BoundElement loTokenE =
	                            getDocumentModel().getElementByName("token");
	                  if (loTokenE !=null)
	                  {
	                     UnknownScalarElement loTokenElem = (UnknownScalarElement)loTokenE;
	                     loTokenElem.setValue(getSAMLToken());
	                  }


	                  BoundElement loReportIdE =
	                      getDocumentModel().getElementByName("report_identifier");
	                  if (loReportIdE !=null)
	                  {

	                     UnknownScalarElement loReportIdElem = (UnknownScalarElement)loReportIdE;
	                     loReportIdElem.setValue(msReportIdentifier);
	                  }



	                  loTextBuffer.append("<TR class=\"advgridoddrow\">").append("\n\t");

	                  loTextBuffer.append("<TD></TD>");
	                  loTextBuffer.append("\n\t");

	                  /*
	                   * New HTML Archetypes after Accessibilty changes have
	                   * a TD less
	                   */

	                  //Business Object Report Name
	                  loTextBuffer.append("<TD>");
	                  loTextBuffer.append(getJobLabel());
	                  loTextBuffer.append("</TD>");
	                  loTextBuffer.append("\n\t");


	                  /*
	                   * New HTML Archetypes after Accessibilty changes have
	                   * a TD less
	                   */
	                  // Add the Html Link
	                  loTextBuffer.append("<TD class=\"" + "advnavlink\"" +">");
	                  loTextBuffer.append("<a title=\"View HTML Report \"");
	                  loTextBuffer.append(" vsaction = \"(None)\"");
	                  loTextBuffer.append(" vsds= \"(None)\"");
	                  loTextBuffer.append(" name=\"BOReportTrigger\" " );




	                  loTextBuffer.append("href=\"javascript:void(document.infoADVANTAGE_displayreport.submit()); \">" );
	                  loTextBuffer.append("View HTML  </a>");
	                  loTextBuffer.append("</TD>");
	                  loTextBuffer.append("\n\t");


	                  /*
	                   * New HTML Archetypes after Accessibilty changes have
	                   * a TD less
	                   */

	                  // Add the Pdf Link
	                  loTextBuffer.append("<TD class=\"" + "advnavlink\"" +">");
	                  loTextBuffer.append("No PDF");
	                  loTextBuffer.append("</TD>");
	                  loTextBuffer.append("\n\t");

	                  /*
	                   * New HTML Archetypes after Accessibilty changes have
	                   * a TD less
	                   */

	                  loTextBuffer.append("<TD></TD>");
	                  loTextBuffer.append("\n\t");
	                  loTextBuffer.append("</TR>").append("\n");
	               } //if (lboolValidReportDataSaved)
	               else
	               {
	                  loTextBuffer.append("<TR class=\""+"advgridoddrow\"" + ">").append("\n\t");
	                  loTextBuffer.append("<TD></TD>");
	                  loTextBuffer.append("\n\t");
	                  loTextBuffer.append("<TD></TD>");
	                  loTextBuffer.append("\n\t");
	                  loTextBuffer.append("<TD>");
	                  loTextBuffer.append("No infoADVANTAGE Report Data Retrieved");
	                  loTextBuffer.append("</TD>");
	                  loTextBuffer.append("\n\t");
	                  loTextBuffer.append("<TD></TD>");
	                  loTextBuffer.append("\n\t");
	                  loTextBuffer.append("</TR>");
	               }
	            }

	            loText = loTextBuffer.toString();
	         }
	         else  // else get report text from VLS
	         {
	            loText = getReportLinks();
	         }
	      }
	      catch(Exception loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	      }

	      loTce = (TextContentElement)getElementByName("AMSReportOutputRows");

	      loTce.setValue(loText);

	      return super.generate();
	   } /* end method generate() */

	  /** Method returns the "index file" with all the links to the report pages
	   *  from VLS
	   *  @return The Report index file
	   */
	   private String getReportLinks()
	   {
	      String loText = null;

	      VSQuery     loQuery ;
	      VSResultSet loResultSet ;

	      try
	      {
	         getParentApp().getSession().getORBSession().setProperty("get_reports", "get_reports");
	      } /* end try */
	      catch( RemoteException foExp )
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", foExp);

	         raiseException( "Unable to set message properties",
	               SEVERITY_LEVEL_SEVERE ) ;
	         return "" ;
	      } /* end catch( RemoteException foExp ) */

	      loQuery = new VSQuery( getParentApp().getSession(), "BS_AGENT",
	            "AGNT_ID=" + getJobID(), "" ) ;

	      // Fix for the download of binary types
	      loQuery.setColumnProjectionLevel( DataConst.ALLTYPES ) ;

	      loResultSet = loQuery.execute() ;

	      if ( loResultSet != null )
	      {
	         loResultSet.last() ;

	         if ( loResultSet.getRowCount() == 1 )
	         {
	            loText = loResultSet.first().getData( "RPT_DATA" ).getString() ;

	            if ( loText == null )
	            {
	               raiseException( "Attachment file not found.", SEVERITY_LEVEL_ERROR ) ;
	            } /* end if ( msAttchFile == null ) */
	         } /* end if ( loResultSet.getRowCount() == 1 ) */
	         else
	         {
	            raiseException( "Unable to locate attachment record.", SEVERITY_LEVEL_ERROR ) ;
	         } /* end else */

	         loResultSet.close() ;
	      } /* end if ( loResultSet == null ) */
	      else
	      {
	         raiseException( "Unable to locate attachment record.", SEVERITY_LEVEL_ERROR ) ;
	      } /* end else */

	      return loText;
	   }

	   /** Methods returns the Job ID aoosiated with the current row
	   *  @return The Job Id
	   */
	   public String getJobID()
	   {
	      try
	      {
	         return getRootDataSource().getCurrentRow().getData("AGNT_ID").getString();
	      } //end try
	      catch(Throwable loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         return "-1";
	      }
	   } /* end method getJobID() */


	   /**
	    * Methods returns the Job Class Name associated with the current row
	    *  @return The Job Class Name
	    */
	   private String getJobClassName()
	   {
	      try
	      {
	         return getRootDataSource().getCurrentRow().getData("AGNT_CLS_NM").getString();
	      } //end try
	      catch(Throwable loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         return "-1";
	      }
	   } /* end method getJobClassName() */


	   /**
	    * Methods returns the Job Label associated with the current row
	    *  @return The Job Class Name
	    */
	   private String getJobLabel()
	   {
	      try
	      {
	         return getRootDataSource().getCurrentRow().getData("JOB_LBL").getString();
	      } //end try
	      catch(Throwable loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         return "-1";
	      }
	   } /* end method getJobLabel() */



	   /** Methods returns the output time stamp  aoosiated with the current job row
	   *  @return The Job output time stamp
	   */
	   public String getJobTimeStamp()
	   {
	      try
	      {
	         return getRootDataSource().getCurrentRow().getData("BASE_TMPL_PATH").getString();
	      } //end try
	      catch(Throwable loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         return "-1";
	      }
	   } /* end method getJobTimeStamp() */


	   /**
	    * Method to print the error msg and close window if no data
	    */
	   private String getErrorText()
	   {
	      StringBuffer loTextBuffer = new StringBuffer();

	      loTextBuffer.append("<HTML>");
	      loTextBuffer.append("<HEAD>");
	      loTextBuffer.append("<script>");
	      loTextBuffer.append("function closeDataWindow() {");
	      loTextBuffer.append("   var mbMinIE4 = (document.all) ? 1 : 0;");
	      loTextBuffer.append("   alert('No PDF data was found for this report!');");
	      loTextBuffer.append("   if ( mbMinIE4 ) {");
	      loTextBuffer.append("      window.close();");
	      loTextBuffer.append("   } else {");
	      loTextBuffer.append("      history.back();");
	      loTextBuffer.append("   }");
	      loTextBuffer.append("}");
	      loTextBuffer.append("</script>");
	      loTextBuffer.append("</HEAD>");
	      loTextBuffer.append("<BODY onload=\"closeDataWindow();\">");
	      loTextBuffer.append("</BODY>");
	      loTextBuffer.append("</HTML>");

	      return loTextBuffer.toString();
	   }

	   public boolean isUseSwing()
	   {
	      return true;
	   }


	   /**
	    * Returns the Business Objects portal start URL
	    */
	   private String getBOStartURL()
	   {

	      StringBuffer loBuf = new StringBuffer(64*2);
	      String lsBaseBOURL = AMSParams.msBOServerName;



	      if (lsBaseBOURL  !=null &&
	          lsBaseBOURL.trim().endsWith("/") )
	      {
	         loBuf.append(lsBaseBOURL);
	         loBuf.append("scripts/login/webiStart.jsp");
	      }
	      else
	      {
	         loBuf.append(lsBaseBOURL);
	         loBuf.append("/scripts/login/webiStart.jsp");
	      }


	      return loBuf.toString();

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

	         raiseException( "Unable to get Security Object",
	                          AMSPage.SEVERITY_LEVEL_SEVERE ) ;
	      }


	      return null;

	   } // of method getSAML token

	   /**
	    * This method is used to determine the value of the report identifeir
	    */
	   private boolean setBOReportIdentifier()
	   {

	      try
	      {
	         VSRow loCurrRow = null;
	         boolean lboolIsPublic = false;
	         loCurrRow = getRootDataSource().getCurrentRow();

	         msReportIdentifier = loCurrRow.getData(ATTR_BO_RPT_NM_AND_ID_COL).getString();

	         if (msReportIdentifier !=null && msReportIdentifier.trim().length()>0 )
	         {
	            return true;
	         }
	         else
	         {
	            return false;
	         }

	      } //end try
	      catch(Throwable loExp)
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         return false;
	      }
	   } /* end method setBOReportIdentifier() */

	}

