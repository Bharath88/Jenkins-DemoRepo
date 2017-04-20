//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;
import java.util.*;
import java.text.MessageFormat;

/*
**  AltSSSubmitErrorQPage
*/

//{{FORM_CLASS_DECL
public class AltSSSubmitErrorQPage extends AltSSSubmitErrorQPageBase

//END_FORM_CLASS_DECL}}
{
   boolean mboolSubmitClicked = false;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public AltSSSubmitErrorQPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
   }


//{{EVENT_CODE
//{{EVENT_AltSSSubmitErrorQPage_beforeActionPerformed
void AltSSSubmitErrorQPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line

   if( ae.getName() != null && ae.getName().equals("btnSubmitErrorQ") )
   {
      String lsQuestion = preq.getParameter( "txtQuestion" ) ;

      // Only if user enter some question text.
      if( lsQuestion != null && lsQuestion.trim().length() > 0 )
      {
         // Append Page_data value with vendor question text.
         StringBuffer lsbPageData = new StringBuffer( T1R_EMAIL_LTR_GEN
            .getCurrentRow().getData( "PAGE_DATA" ).getString() );
         lsbPageData.append( "Question: \n" );
         lsbPageData.append( lsQuestion );
         T1R_EMAIL_LTR_GEN.getCurrentRow().getData( "PAGE_DATA" )
            .setString( lsbPageData.toString() );
         mboolSubmitClicked = true;
      }
      else
      {
         T1R_EMAIL_LTR_GEN.undoAll();
         raiseException( "You must enter the question before submitting",
               SEVERITY_LEVEL_ERROR ) ;
         evt.setNewPage(this);
         evt.setCancel(true);
      }
   }

}
//END_EVENT_AltSSSubmitErrorQPage_beforeActionPerformed}}

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
			AltSSSubmitErrorQPage_beforeActionPerformed( ae, evt, preq );
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
   * Overriden this method here to construct the email
   * from predefined configuration in R_EXT_EMAIL_LTRCNF
   *
   * @return String  The generated HTML
   */
   public String generate()
   {
      // After clicking the submit button on this page,
      // in beforeActionPerformed mboolSubmitClicked will be set to true.
      if( mboolSubmitClicked )
      {
         if( getHighestSeverityLevel() > SEVERITY_LEVEL_INFO )
         {
            appendOnloadString( "alert(\'Your email cannot be sent at this time, \\n please select the Contact Us link for a number to call.\')" ) ;
            appendOnloadString( "top.window.close()" ) ;
         }
         else
         {
            appendOnloadString( "alert(\'Email sent successfully\')" );
            appendOnloadString( "top.window.close()" ) ;
         }

      }
      else
      {
         populateEmail( );
      }

      return super.generate();
   }


   /**
   * retrieves all the email config data from R_EXT_EMAIL_LTRCNF
   * table for the email sent from this page.
   */
   public void populateEmail( )
   {
      String lsWhereClause       = null;
      VSQuery loQuery            = null;
      VSResultSet loRSConfig     = null;
      VSRow loConfigRow          = null;
      VSRow loInsertRow      = null;
      String lsUserID        = null;
      try
      {
         //Get the User ID value from Session
         lsUserID = getParentApp().getSession().getLogin();

         //Get inserted blank row for the new email.
         T1R_EMAIL_LTR_GEN.insert();
         loInsertRow = T1R_EMAIL_LTR_GEN.getCurrentRow();

         // Get the Email Configuration data
         lsWhereClause = "EMAIL_LTR_TYP='SUBERQ'";
         loQuery = new VSQuery( getParentApp().getSession(),
         "R_EXT_EMAIL_LTRCNF", lsWhereClause, "" ) ;
         loRSConfig = loQuery.execute() ;

         loConfigRow = loRSConfig.first();
         // If record not found or found more than one record, display error
         if ( loRSConfig == null || loRSConfig.getRowCount() != 1 )
         {
            raiseException( "Error retrieving email configuration row",
               SEVERITY_LEVEL_ERROR ) ;
            return;
         }

         loConfigRow = loRSConfig.first();

         if( loConfigRow == null )
         {
            raiseException( "Error retrieving email configuration row",
               SEVERITY_LEVEL_ERROR ) ;
            return;
         }/* end - if( loConfigRow == null || loUserInfoRow == null ) */

         // Set Sync_email_fl to true so that, it is sent immediately
         // also not re-sent and deleted from the table in batch process.
         loInsertRow.getData( "SYNC_EMAIL_FL" ).setInt( 1 );

         // This is an email and not a latter.
         loInsertRow.getData( "LTR_EMAIL_IND" ).setInt( 1 );

         // Set Default send_to from config row
         loInsertRow.getData( "SEND_TO" ).setString(
            loConfigRow.getData( "DFLT_SEND_TO" ).getString() );

         // Set subject from the config row to predefined subject.
         loInsertRow.getData( "SUBJECT" ).setString(
            loConfigRow.getData( "SUBJECT" ).getString() );

         // Here PAGE_DATA column will be populated with only config header,
         // body text and Errors information. when user clicks on submit,
         // it's appended with vendor question text.
         StringBuffer lsbPageData = new StringBuffer( 50 );
         String[] lsaUserInfo = getUserInfo( loInsertRow );

         // Get Header, Body and Footer config data
         // and Substitute the user info values.
         String lsHdrTxt  = loConfigRow.getData( "HDR_TXT" ).getString();
         String lsBodyTxt = loConfigRow.getData( "BODY_TXT" ).getString();
         lsBodyTxt = MessageFormat.format( lsBodyTxt, lsaUserInfo );
         lsbPageData.append( lsHdrTxt );
         lsbPageData.append( lsBodyTxt );

         // Then get the error messages information also.
         lsbPageData.append( getErrorInfo() );

         // Set this combined email message in PAGE_DATA column.
         loInsertRow.getData( "PAGE_DATA" )
            .setString( lsbPageData.toString() );

         // Set AUTO_SEND_EMAIL_AD from User Info Array
         if( lsaUserInfo != null && lsaUserInfo.length >=2 )
         {
            loInsertRow.getData( "AUTO_SEND_EMAIL_AD" ).setString(
               lsaUserInfo[ 2 ] );
         }
         else
         {
            loInsertRow.getData( "AUTO_SEND_EMAIL_AD" ).setString(
               loConfigRow.getData( "AUTO_SEND_EMAIL_AD" ).getString() );
         }

      }/*end - try */
      catch ( Exception loEx )
      {
         raiseException( loEx.getMessage(), SEVERITY_LEVEL_ERROR ) ;

         return ;

      }  /*end - catch */
      finally
      {
         if (loRSConfig != null)
         {
            loRSConfig.close();
         }

      }  /*end - finally */

   }/* end  populateEmail( ) */


   /**
   * Returns User Information substitution value array.
   * required to construct the email message text.
   * Note - ADMIN build version of this file will have only
   * one substitution value which is User ID, but the VSS
   * version of this file can overwrite this method to retun
   * multiple values as required for message body.
   *
   * return String[] User Information substitution value array
   */
   public String[] getUserInfo( VSRow foInsertRow )
   {
      String lsWhereClause          = null;
      VSQuery loQuery               = null;
      VSResultSet loRSUserInfo      = null;
      VSRow loUserInfoRow           = null;

      String[] lsaSubstitute        = new String[1];
      String lsUserID = getParentApp().getSession().getLogin();
      lsaSubstitute[ 0 ] = lsUserID; // User ID

      return lsaSubstitute;

   }/*end - setUserInfo */


   /**
   * Returns Email Page Data text
   *
   * return String Email Page Data text
   */
   public String getErrorInfo( )
   {
      // String buffer for error info to append in email message
      StringBuffer lsbReturn = new StringBuffer( 100 );
      // String buffer constructing grid to display on the page
      StringBuffer lsbGridData = new StringBuffer( 100 );


      lsbReturn.append( "\n" );
      lsbReturn.append( "Code   " );
      lsbReturn.append( "Reference           " );
      lsbReturn.append( "Type           " );
      lsbReturn.append( "Message Description" );
      lsbReturn.append( "\n" );
      lsbReturn.append( "-----  ------------------  -------------  --------------------\n" );
      // create Display Grid headers
      createDisplayGridHeaders();


      // Get messages from source page error msg table, and process one by one
      Hashtable loErrorsHash = ((AMSPage) getSourcePage()
         .getSourcePage()).getErrorMsgTable();
      Enumeration loErrorsEnum = loErrorsHash.keys();
      int liCounter              = 1;
      String lsMessage           = null;
      AMSErrorMessage loErrMsg   = null;
      StringBuffer lsbTmpData;
      String lsTmpData;
      while( loErrorsEnum.hasMoreElements() )
      {
         // Get each message
         lsMessage = (String) loErrorsEnum.nextElement();
         loErrMsg = new AMSErrorMessage(lsMessage, true);

         // Set alternet row style class
         if (liCounter % 2 == 0)
         {
            lsbGridData.append("<tr class=\"ADVGridEvenRow\">");
         }/*end - if (liCounter % 2 == 0) */
         else
         {
            lsbGridData.append("<tr class=\"ADVGridOddRow\">");
         }/*end - else */

         // MsgCode max length is 5, so create string buffer of 5+2 spaces
         lsTmpData = loErrMsg.getMsgCode();
         lsbTmpData = new StringBuffer( "       " );
         lsbTmpData.replace( 0, lsTmpData.length(), lsTmpData );
         lsbGridData.append("<td>");
         lsbGridData.append( lsTmpData );
         lsbGridData.append("</td>");
         lsbReturn.append( lsbTmpData.toString() );

         // Msg Component max length is 18, so create string buffer of 18+2 spaces
         lsTmpData = loErrMsg.getComponent();
         lsbTmpData = new StringBuffer( "                    " );
         lsbTmpData.replace( 0, lsTmpData.length(), lsTmpData );
         lsbGridData.append("<td>");
         lsbGridData.append( lsTmpData );
         lsbGridData.append("</td>");
         lsbReturn.append( lsbTmpData.toString() );

         // Msg type max length is 13, so create string buffer of 13+2 spaces
         lsTmpData = AltSSErrorsPage.MESSAGE_SEVERITY[
            loErrMsg.getMsgSeverity() ];
         lsbTmpData = new StringBuffer( "               " );
         lsbTmpData.replace( 0, lsTmpData.length(), lsTmpData );
         lsbGridData.append("<td>");
         lsbGridData.append( lsTmpData );
         lsbGridData.append("</td>");
         lsbReturn.append( lsbTmpData.toString() );

         lsTmpData = loErrMsg.getMsgDescription();
         lsbGridData.append("<td>");
         lsbGridData.append( lsTmpData );
         lsbGridData.append("</td>");
         lsbReturn.append( lsTmpData );

         lsbGridData.append("</tr>");
         lsbReturn.append( "\n" );

         liCounter ++;

      } /* end while(loErrorsEnum.hasMoreElements()) */

      lsbReturn.append( "\n" );

      //Set constructed message detail data into TextContentElement
      TextContentElement loTce = (TextContentElement)
         getElementByName( "ErrorMsgGridData" );
      loTce.setPreserveHTMLTags( true );
      loTce.setValue( lsbGridData.toString() );

      return lsbReturn.toString();

   }/*end - getErrorInfo*/


   /**
   * Creates Message Grid Header, which is shown on this page
   */
   private void createDisplayGridHeaders()
   {
      StringBuffer lsbHeader = new StringBuffer( 100 );
      lsbHeader.append("<th><span id=\"Code\" name=\"Code\" title=\"Code\">Code</span></th>");
      lsbHeader.append("<th><span id=\"Reference\" name=\"Reference\" title=\"Reference\">Reference</span></th>");
      lsbHeader.append("<th><span id=\"Type\" name=\"Type\" title=\"Type\">Type</span></th>");
      lsbHeader.append("<th><span id=\"Message\" name=\"Message\" title=\"Message\">Message</span></th>");

      TextContentElement loTce = (TextContentElement)
         getElementByName( "ErrorMsgGridHeader" );
      loTce.setPreserveHTMLTags( true );
      loTce.setValue( lsbHeader.toString() );

   }/*end - createDisplayGridHeaders */
}
