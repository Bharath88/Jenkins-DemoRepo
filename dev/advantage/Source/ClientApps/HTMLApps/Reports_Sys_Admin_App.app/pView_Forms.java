//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSParams ;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.*;
import java.util.Vector;

import com.amsinc.gems.adv.vfc.html.AMSDocTabbedPage;

import org.apache.commons.logging.Log;
/*
 **  pView_Forms
 */

//{{FORM_CLASS_DECL
public class pView_Forms extends pView_FormsBase

//END_FORM_CLASS_DECL}}
{
   private String msFormFile   = null ;
   private boolean mboolFirstTimeGenerate = true;
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pView_Forms.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pView_Forms ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_T1VW_FORMS_MGMT_beforeQuery
   void T1VW_FORMS_MGMT_beforeQuery(VSQuery query ,VSOutParam resultset )
   {

      // This will get called when the page is opened or when Browse action is performed.
      try
      {
         VSPage loSourcePage = getSourcePage();
         if( loSourcePage != null )
         {
            String lsSourcePage = loSourcePage.getName();
            /**
             * If we are transitioning to current page either from 'View All Jobs'
             *  or 'Report Completed User' or 'Job Completed User' page, the
             * AMSBatchJobListTableElement class will take care of Where Clause
             * (i.e. it will perform filter based on Agnt ID).
             * Otherwise below filter criteria will be used.
             **/
            if ( !(lsSourcePage.equals("advantage.Reports_Sys_Admin_App.View_All_Jobs")
               || lsSourcePage.equals("advantage.Reports_Sys_Admin_App.Report_Completed_User")
                  || lsSourcePage.equals("advantage.Reports_Sys_Admin_App.Job_Completed_User")) )
            {
               String lsLoginUser   = getParentApp().getSession().getLogin();

               Vector lvUserSecRole = new Vector();
               lvUserSecRole.addElement(AMSSecurity.ADMIN_SEC_ROLE);

               SearchRequest loSrchReq = new SearchRequest() ;
               SearchRequest loOrderBy = new SearchRequest() ;
               StringBuffer lsbWhere = new StringBuffer(160);

               String lsOldWhereClause = query.getSQLWhereClause() ;
               if ( lsOldWhereClause != null && lsOldWhereClause.length() > 0)
               {
                  // Check If the existing Query is not null, if it is not null
                  // append " AND " before the custom Query begins.
                  lsbWhere.append(" AND ");
               } /* End if ( lsOldWhereClause != null . . .*/

               if (mboolFirstTimeGenerate && loSourcePage instanceof AMSDocTabbedPage)
               {

                  DataSource loDS = loSourcePage.getRootDataSource();
                  if (loDS != null)
                  {
                     VSRow loVSRow = loDS.getCurrentRow();
                     if (loVSRow != null)
                     {
						//get document information from the source document page
                        String lsDOC_CD      = loVSRow.getData( "DOC_CD" ).getString();
                        String lsDOC_DEPT_CD = loVSRow.getData( "DOC_DEPT_CD" ).getString();
                        String lsDOC_ID      = loVSRow.getData( "DOC_ID" ).getString();

                        //set to search fields
                        T1VW_FORMS_MGMT.setQBFDataForElement( "txtT1DOC_CD", lsDOC_CD );
                        T1VW_FORMS_MGMT.setQBFDataForElement( "txtT1DOC_DEPT_CD", lsDOC_DEPT_CD );
                        T1VW_FORMS_MGMT.setQBFDataForElement( "txtT1DOC_ID", lsDOC_ID );
                     } //END if (loVSRow != null)
                  } //END if (loDS != null)

                  String lsQueryText = T1VW_FORMS_MGMT.getOnScreenQueryText();
                  if (!AMSStringUtil.strIsEmpty(lsQueryText))
                  {
                     lsbWhere.append (lsQueryText);
                     lsbWhere.append(" AND ");
                  }

                  mboolFirstTimeGenerate = false;
               } // end if (mboolFirstTimeGenerate)
               /*
                * Users in the administrator role to be able to see all entries
                * otherwise filters the records to display based on the user ID.
                */
               if (! AMSSecurity.IsValidUserRoles(lsLoginUser,lvUserSecRole,false))
               {
                  lsbWhere.append(" USER_ID ");
                  lsbWhere.append(AMSSQLUtil.getANSIQuotedStr(lsLoginUser,
                     AMSSQLUtil.EQUALS_OPER));
                  lsbWhere.append(" AND ");
               } /* if (! AMSSecurity.checkRoles(lsLoginUser,lvUserSecRole,false)) */

               lsbWhere.append(" ONLN_FL = 1");
               lsbWhere.append(" AND APPL_ID = ");
               lsbWhere.append(AMSParams.msPrimaryApplication);

               loSrchReq.add(lsbWhere.toString());
               loOrderBy.add( " EXPR_DT DESC " ) ;

               query.addFilter( loSrchReq );
               query.replaceSortingCriteria( loOrderBy ) ;
               setSessionProperties("Searchable", "TRUE");
            }
            else
            {
               setSessionProperties("Searchable", "FALSE");
               hideSearchColumns();
            } /* Edn if ( !(lsSourcePage.equals("advantage.Reports_Sys_ . . .*/
         } /* Emd if( getSourcePage() != null )*/
      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } /* End Try */

   }










//END_EVENT_T1VW_FORMS_MGMT_beforeQuery}}
//{{EVENT_pView_Forms_beforeActionPerformed
   void pView_Forms_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      /*
       * This will get called when any sort of action is performed on the page.
       * Our main intention is to trap 'View PDF' action.
       */
      if (ae.getName() != null && ae.getName().equals("show_pdf"))
      {
         VSRow loCurrentRow = getRootDataSource().getCurrentRow();
         int liPrntStatus = loCurrentRow.getData("PRNT_STA").getInt();

         if ( liPrntStatus == 1 )
         {
            // Set the session properties for VLS
            setSessionProperties("Pend_Xfer", "TRUE");

            /**
             * This save will send control for a particular Row to VLS side,
             * where FTP action will be invoked to transfer generated pdf
             * file from JetForm server to Application server.
             **/
            loCurrentRow.save();

            // Reset the session properties
            setSessionProperties("Pend_Xfer", "");
         } /* End if ( liPrntStatus == 1 ) */

         // Set the session properties for VLS
         setSessionProperties("show_pdf", "TRUE");

         // Get the report file from VLS
         getFormFile("RPT_PDF", ".pdf");

         // Reset the session properties
         setSessionProperties("show_pdf", "");

         evt.setCancel( true ) ;
         evt.setNewPage( getSourcePage() ) ;
      } /* End if (ae.getName() != null && ae.getName().equals("show_pdf")) */
   }

   /**
    * This method will call hideSearchColumns method which will
    * hide the searchable fields if user goes to View_Form page
    * thorugh 'View_All_Jobs', 'Report_Completed_User' or
    * 'Job_Completed_User' pages
    *
    * @return void
    */
   public void beforeGenerate()
   {
      try
      {
         String lsSearchable =
            getParentApp().getSession().getORBSession().getProperty("Searchable");
         if ( AMSStringUtil.strEqual(lsSearchable, "FALSE"))
         {
            hideSearchColumns();
         } /* End if ( AMSStringUtil.strEqual(lsSearchable, "FALSE")) */
      }
      catch(Exception loExp)
      {
         // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } /* End Try */

   } /* End beforeGenerate() */

   /**
    * Set the session properties for later use in VLS
    *
    * @param fsShowPDF  Session attribure name
    * @param fsValue Session attribure value
    * @return void
    */
   private void setSessionProperties(String fsShowPDF,	String fsValue)
   {
      VSORBSession loORBSession = getParentApp().getSession().getORBSession();

      try
      {
         loORBSession.setProperty(fsShowPDF, fsValue);
      }
      catch (Exception loExp)
      {
              // Add exception log to logger object
              moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      } /* End Try */

   } /* End setSessionProperties() */


   /**
    * Run the query and get the form data from VLS
    *
    * @return void
    */
   private void getFormFile(String foDataType, String foFormExt)
   {
      VSQuery     loQuery ;
      VSResultSet loResultSet = null;
      try
      {
         loQuery = new VSQuery( getParentApp().getSession(), "VW_FORMS_MGMT",
            ("FILE_NM " + AMSSQLUtil.getANSIQuotedStr(getFormName(),
            AMSSQLUtil.EQUALS_OPER)), "" ) ;

         // Fix for the download of binary types
         loQuery.setColumnProjectionLevel( DataConst.ALLTYPES ) ;
         loResultSet = loQuery.execute() ;

         if ( loResultSet != null )
         {
            loResultSet.last() ;

            if ( loResultSet.getRowCount() == 1 )
            {
               VSData loData;
               msFormFile  = loResultSet.first().getData( foDataType ).getString() ;
               if ( ( msFormFile == null ) ||
                  ( msFormFile.trim().length() == 0 ) )
               {
                  msFormFile  = getErrorText();
               }
               else
               {
                  String lsAbsFilePath = getFormPath() + File.separator +
                     getFormName() + foFormExt;

                  setStreamFileInfo( "application/pdf",
                     lsAbsFilePath , false ) ;
               } /* End if ( ( msFormFile == null ) ||. . .  */

               /**
                * An offline process (Generate Invoice) was run with incorrect
                * parameters such that "View Forms" link when selected displayed
                * "No PDF data was found for this form!" and this Form was not
                * purged when Online Forms Cleanup job was run. Reason was
                * ACCS_FL is set only if there is PDF data. The code to set
                * ACCS_FL is brought out of the "if  ...else.." block so that
                * no matter the form is empty or not, the moment the User
                * selects "View PDF" to view the form, the ACCS_FL is marked
                * as true and this record will be eligible for Online Forms Cleanup.
                */
               loData = getRootDataSource().getCurrentRow().getData("ACCS_FL");
               if ( !loData.getBoolean())
               {
                  loData.setBoolean(true);
                  getRootDataSource().getCurrentRow().save();
               }/* End if ( !loData.getBoolean()) */

            }
            else
            {
               raiseException( "Unable to locate agent record.",
                  SEVERITY_LEVEL_ERROR ) ;
            } /* End  if ( loResultSet.getRowCount() == 1 ) */
         }
         else
         {
            raiseException( "Unable to locate agent record.",
               SEVERITY_LEVEL_ERROR ) ;
         } /* End if ( loResultSet != null ) */
      }
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      }
      finally
      {
         if (loResultSet != null)
         {
            loResultSet.close();
         }
      } /* End Try */

   } /* End getFormFile() */

   /**
    * Thie methods returns the File Name associated with the current row
    *
    * @return The Form File Name
    */
   private String getFormName()
   {
      try
      {
         return getRootDataSource().getCurrentRow().getData("FILE_NM").getString();
      } //end try
      catch(Throwable loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

		 return "-1";
      } /* End Try*/
   } /* end method getFormName() */



   /**
    * This methods returns the File Location associated with the current row
    *
    * @return The Form File Path
    */
   private String getFormPath()
   {
      try
      {
         return getRootDataSource().getCurrentRow().getData("FILE_LOC").getString();
      } //end try
      catch(Throwable loExp)
      {
         // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loExp);

          return "-1";
      } /* End Try */
   } /* end method getFormPath() */

   /**
    * This methods hide the searchable fields
    *
    * @return void
    */
   private void hideSearchColumns()
   {
      DivElement loSearchCols ;
      loSearchCols = (DivElement)getElementByName("Browse");
      if ( loSearchCols != null )
      {
         loSearchCols.setVisible(false);
      }/* End if ( loSearchCols != null )*/

      loSearchCols = (DivElement)getElementByName("SearchCols");
      if ( loSearchCols != null )
      {
         loSearchCols.setVisible(false);
      } /* End if ( loSearchCols != null ) */

   } /* End hideSearchColumns() */


   /**
    * Method to print the error message and
    * close window if data is not found
    *
    * @return String Error message
    */
   private String getErrorText()
   {
      StringBuffer loTextBuffer = new StringBuffer();

      loTextBuffer.append("<HTML>");
      loTextBuffer.append("<HEAD>");
      loTextBuffer.append("<script>");
      loTextBuffer.append("function closeDataWindow() {");
      loTextBuffer.append("   var mbMinIE4 = (document.all) ? 1 : 0;");
      loTextBuffer.append("   alert('No PDF data was found for this form!');");
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
   } /* End getErrorText()*/



   public String doAction( PLSRequest foPLSReq )
   {
      String lsResponse ;
      lsResponse = super.doAction( foPLSReq ) ;

      if ( msFormFile != null )
      {
         lsResponse = msFormFile ;
         msFormFile = null ;
      } /* if ( msFormFile != null ) */

      return lsResponse ;

   } /* end doAction() */











//END_EVENT_pView_Forms_beforeActionPerformed}}
//{{EVENT_pView_Forms_afterGenerate
void pView_Forms_afterGenerate(StringBuffer s)
{
	//Write Event Code below this line
   VSPage loSourcePage = getSourcePage();
   if( loSourcePage != null )
   {
      String lsSourcePage = loSourcePage.getName();

      /**
       * Offline forms generation set the print status as NO FORMS if
       * there is no data to print. In this case user may or may not
       * click on Link View PDF. If view PDF link will not clicked
       * then access flag will not set for this record.
       * Since there is no data and folders should be purged
       * by next online forms cleanup process.
       *
       * Following code will set the access flag to true if current record
       * is having print status as NO FORMS. Since this case is
       * specific to offline processes if user open the page from
       * 'View All Jobs'  or 'View Fin Jobs' or 'Report Completed User'
       * or 'Job Completed User' page then only this code will be executed,
       **/
      if ( (lsSourcePage.equals("advantage.Reports_Sys_Admin_App.View_All_Jobs")
            || lsSourcePage.equals("advantage.Reports_Sys_Admin_App.Report_Completed_User")
            || lsSourcePage.equals("advantage.Reports_Sys_Admin_App.Job_Completed_User")) )
      {
         VSRow loCurrentRow = getRootDataSource().getCurrentRow();
         if(loCurrentRow != null)
         {
            int liPrntStatus = loCurrentRow.getData("PRNT_STA").getInt();
            if(liPrntStatus == 3)
            {
               VSData   loData = getRootDataSource().getCurrentRow().getData("ACCS_FL");
               if ( !loData.getBoolean())
               {
                  loData.setBoolean(true);
                  getRootDataSource().getCurrentRow().save();
               }
            }
         }
      }
   }
}
//END_EVENT_pView_Forms_afterGenerate}}

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1VW_FORMS_MGMT.addDBListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void afterGenerate(VSPage obj, StringBuffer s){
		Object source = obj;
		if (source == this ) {
			pView_Forms_afterGenerate(s);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pView_Forms_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1VW_FORMS_MGMT) {
			T1VW_FORMS_MGMT_beforeQuery(query , resultset );
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




}
