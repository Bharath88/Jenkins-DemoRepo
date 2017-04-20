//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

 import java.util.* ;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amsinc.gems.adv.vfc.html.* ;

   /*
   **  pWF_DISP_MSG
   */

//{{FORM_CLASS_DECL
public class pWF_DISP_MSG extends pWF_DISP_MSGBase

//END_FORM_CLASS_DECL}}
   {
      // Declarations for instance variables used in the form

      private static final String WL_DOC_CD_ATTRIBUTE = "ams_doc_cd";
      private static final String WL_DOC_DEPT_ATTRIBUTE = "ams_doc_dept";
      private static final String WL_DOC_ID_ATTRIBUTE = "ams_doc_id";
      private static final String WL_DOC_VERS_NO_ATTRIBUTE = "ams_doc_vers_no";

      private static final String APPROVAL_LEVEL_ATTRIBUTE = "ams_aprv_lvl";
      private static final String ASSIGNEE_ATTRIBUTE = "ams_assignee";
      private static final String ASSIGNEE_TYPE_ATTRIBUTE = "ams_assignee_fl";

      private static final String APRV_DOCUMENT = "Assigned Document: ";
      private static final String APRV_LEVEL = "Assigned Approval Level: ";
      private static final String APRV_USER = "Assigned User: ";
      private static final String APRV_ROLE = "Assigned Role: ";
      private static final String DOCUMENT = "Document: ";

      private static final String NEW_LINE = System.getProperty("line.separator");
      private static final String HTML_BREAK_TAG = "<br>";
      private static final String SPACE = " ";

      private static final String HREF1 = "<a " +
            "href=\"javascript:submitForm(document.pWF_DISP_MSG," +
            "'T1WF_DISP_MSG43=T1WF_DISP_MSG43&amp;vsnavigation=pagetransition&amp;" +
            "openDoc=openDoc&amp;"; 

      private static final String HREF2 = "','Display');\">";
      private static final String HREF3 = "</a>";
      private static final String HREF4 = "<a href";

      private String msHyperlinkText = null ;

      /*
       * Patten to parse a documents components from space limited aggregate
       * of the form '<Doc Code> <Doc Dept Code> <Doc Id> <Doc Vers No>.
       *
       * Note that document Id could have spaces and hence the 'reluctant
       * quantifier' ([\\w\\s]*?) used as opposed to the greedy quantifier
       * ([\\w\\s]*)
       */
      private static final Pattern moDocPattern =
         Pattern.compile("([\\w]*)\\s([\\w]*)\\s([\\w\\s]*?)\\s([\\w]*)$");

      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code. To customize paint
      // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pWF_DISP_MSG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
       * Overridden the super class method to re-set the value of message
       * body with the link to the document generated.
       *
       * @return String Returns the generated page
       */
      public String generate()
      {
         String lsMsgValue = null ;
         String lsMsgContent = null ;
         String lsHyperlinkInfo = null ;
         VSRow  loRow = null ;

         loRow = getRootDataSource().getCurrentRow() ;

         if ( loRow != null )
         {
            lsMsgValue = loRow.getData( "BODY" ).getString() ;

            if ( lsMsgValue != null && lsMsgValue.length() > 0 )
            {
               lsHyperlinkInfo = parseMessageText( lsMsgValue ) ;

               if ( lsHyperlinkInfo != null && lsHyperlinkInfo.length() > 0 )
               {
                  lsMsgValue = checkAndRemoveAnchor(lsMsgValue);
                  lsMsgValue = AMSPLSDocUtil.replacePattern( lsMsgValue, NEW_LINE, HTML_BREAK_TAG ) ;

                  lsMsgContent = HREF1 + lsHyperlinkInfo + HREF2 + msHyperlinkText + HREF3 ;

                  lsMsgValue = AMSPLSDocUtil.replacePattern( lsMsgValue, msHyperlinkText, lsMsgContent ) ;

                  loRow.getData( "BODY" ).setString( lsMsgValue ) ;
               } /* end if ( lsHyperlinkInfo != null && lsHyperlinkInfo.length() > 0 ) */

            } /* end if ( lsMsgValue != null && lsMsgValue.length() > 0 ) */

         } /* end if ( loRow != null ) */

         return super.generate() ;
      } /* end generate() */

      /**
       * This method parses the message text and returns the hyperlink
       * information.
       *
       * @param fsMsgTxt Message Text
       * @return String Text containing information for document hyperlink
       */
      private String parseMessageText( String fsMsgTxt )
      {
         StringTokenizer loTokenizer = new StringTokenizer( fsMsgTxt, NEW_LINE ) ;
         StringBuffer lsHyperlinkInfoBuf = new StringBuffer() ;
         String lsDocIdent = null ;
         String lsAprvLevel = null ;
         String lsAprvUser = null ;
         String lsAprvRole = null ;

         while ( loTokenizer.hasMoreTokens() )
         {
            String lsToken = loTokenizer.nextToken() ;

            if ( lsToken.startsWith( APRV_DOCUMENT ) )
            {
               lsDocIdent = lsToken.substring( APRV_DOCUMENT.length() ) ;
               lsDocIdent = removeAnchor( lsDocIdent );
               msHyperlinkText = lsDocIdent ;

               lsHyperlinkInfoBuf.append( parseDocIdent( lsDocIdent ) ) ;
            } /* end if ( lsToken.startsWith( APRV_DOCUMENT ) ) */
            else if ( lsToken.startsWith( APRV_LEVEL ) )
            {
               lsAprvLevel = lsToken.substring( APRV_LEVEL.length() ) ;

               lsHyperlinkInfoBuf.append( "&amp;" + APPROVAL_LEVEL_ATTRIBUTE + "=" + lsAprvLevel ) ;
            } /* end else if ( lsToken.startsWith( APRV_LEVEL ) ) */
            else if ( lsToken.startsWith( APRV_USER ) )
            {
               lsAprvUser = lsToken.substring( APRV_USER.length() ) ;

               lsHyperlinkInfoBuf.append( "&amp;" + ASSIGNEE_ATTRIBUTE + "=" + lsAprvUser +
                                          "&amp;" + ASSIGNEE_TYPE_ATTRIBUTE + "=false" ) ;
            } /* end else if ( lsToken.startsWith( APRV_USER ) ) */
            else if ( lsToken.startsWith( APRV_ROLE ) )
            {
               lsAprvRole = lsToken.substring( APRV_ROLE.length() ) ;

               lsHyperlinkInfoBuf.append( "&amp;" + ASSIGNEE_ATTRIBUTE + "=" + lsAprvRole +
                                          "&amp;" + ASSIGNEE_TYPE_ATTRIBUTE + "=true" ) ;
            } /* end else if ( lsToken.startsWith( APRV_ROLE ) ) */
            else if ( lsToken.startsWith( DOCUMENT ) )
            {
               lsDocIdent = lsToken.substring( DOCUMENT.length() ) ;
               lsDocIdent = removeAnchor( lsDocIdent );
               msHyperlinkText = lsDocIdent ;

               lsHyperlinkInfoBuf.append( parseDocIdent( lsDocIdent ) ) ;
            } /* end if ( lsToken.startsWith( APRV_DOCUMENT ) ) */

         } /* end while ( loTokenizer.hasMoreTokens() ) */

         return lsHyperlinkInfoBuf.toString();
      } /* end parseMessageText() */

      /**
       * This method parses the document identifier and builds the
       * document parameter information for the hyperlink.
       *
       * @param fsDocIdent Document Identifier
       * @return String Text containing document info for hyperlink
       */
      private String parseDocIdent( String fsDocIdent )
      {
         String lsDocCd = null;
         String lsDocDept = null ;
         String lsDocId = null ;
         String lsDocVersNo = null ;

         /*
          * Match the compiled pattern to the document id string
          */
         Matcher loMatcher = moDocPattern.matcher(fsDocIdent.trim());
         loMatcher.find();

         lsDocCd = loMatcher.group(1) ;
         lsDocDept = loMatcher.group(2) ;
         lsDocId = loMatcher.group(3) ;
         lsDocVersNo =loMatcher.group(4) ;

         String lsHyperlinkInfo = WL_DOC_CD_ATTRIBUTE + "=" + lsDocCd +
               "&amp;" + WL_DOC_DEPT_ATTRIBUTE + "=" + lsDocDept +
               "&amp;" + WL_DOC_ID_ATTRIBUTE + "=" + lsDocId +
               "&amp;" + WL_DOC_VERS_NO_ATTRIBUTE + "=" + lsDocVersNo;

         return lsHyperlinkInfo;
      } /* end parseDocIdent() */

      /**
       * Turn off VRS since this page uses a custom XDA connector
       */
      public boolean useVirtualResultSet( AMSDataSource foDataSource )
      {
         return false ;
      }
	  
	  /**
       * Check and removes the anchor type "href" from document identifier 
       * and returns the formated text
       * @param fsMsgTxt - The original text to be passed
       * @return -  formated text
       */
      private String checkAndRemoveAnchor(String fsMsgTxt)
      {
         StringBuffer lsbFormatedMsg =  new StringBuffer();
         StringTokenizer loTokenizer = new StringTokenizer( fsMsgTxt, NEW_LINE ) ;
         while(loTokenizer.hasMoreTokens())
         {
            String lsToken = loTokenizer.nextToken() ;
            if( lsToken.startsWith( APRV_DOCUMENT ) && lsToken.contains( HREF4 ))
            {
               lsToken = removeAnchor( lsToken );
            }else if ( lsToken.startsWith( DOCUMENT ) && lsToken.contains( HREF4 ))
            {
               lsToken = removeAnchor( lsToken);
            }
            lsbFormatedMsg.append( lsToken ).append( NEW_LINE ).append( NEW_LINE );
         }
         return lsbFormatedMsg.toString();
      }
	  
	   /**
       * Removes the anchor "href" if present in the text
       * @param fsToken -  The text to be passed
       * @param fsTokenPrefix - The Doc Identifier prefix to be passed
       * @return
       */
      private String removeAnchor( String fsToken ) 
      {
         String lsToken = fsToken;
         String lsHrefStart = null;
         try
         {
            if( lsToken.contains( HREF4 ) )
            {
                lsHrefStart = lsToken.substring( lsToken.indexOf( HREF4 ), lsToken.indexOf(">" )+1 );
                lsToken = AMSPLSDocUtil.replacePattern( lsToken, lsHrefStart, "" );
                lsToken = AMSPLSDocUtil.replacePattern( lsToken, HREF3, "" );
            }
         } catch( Exception loEx )
         {
            return lsToken;
         }
         return lsToken;
      }
   }