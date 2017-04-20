//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.*;
import java.util.*;
import org.apache.commons.logging.*;
import advantage.AMSStringUtil;
import advantage.CVL_DOC_PHASE_CDImpl;
import com.amsinc.gems.adv.common.AMSSQLUtil;


/*
 **  DocAssembleDialog
 */

//{{FORM_CLASS_DECL
public class DocAssembleDialog extends DocAssembleDialogBase

//END_FORM_CLASS_DECL}}
{

   private static Log moLog = AMSLogger.getLog( DocAssembleDialog.class,
      AMSLogConstants.FUNC_AREA_FORMS_ASSEMBLY ) ;

   /**
   *  Member variable to hold the document code for the current instance.
   */
   private String msDocCd = null;

   /**
   *  Member variable to hold the text content element for custom print parameters.
   */
   private TextContentElement moCustActnTCE = null;

   /**
   * Constant to define the name of the text area used to display custom
   * print actions.
   */
   private static final String PRNT_ACTNS_DISPLAY_AREA = "PrintActions";

   private String msFieldNameForHideInactiveProcurementLines = "HIDE_INACTV";

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public DocAssembleDialog ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);

   }


//{{EVENT_CODE
//{{EVENT_DocAssembleDialog_beforeActionPerformed
   void DocAssembleDialog_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      //Write Event Code below this line
      if(moLog.isDebugEnabled())
      {
         moLog.debug("ae.getname=" + ae.getName());
      }
      /**
      * If the source page is a document, make sure that the tabs are re-gened
      */
      if ( ae.getName().equals( "CancelPrint" ) )
      {
         VSPage loSrcPage = getSourcePage() ;

         if ( ( loSrcPage != null ) && ( loSrcPage instanceof AMSDocTabbedPage ) )
         {
            ((AMSDocTabbedPage)loSrcPage).setGenTabPage( true ) ;
         }
      }

      else if ( ae.getName().equals( "PrintDoc" ) )
      {
         VSSession loSession = getParentApp().getSession();


         VSPage loTarget = createAssemblyReq (preq);
         evt.setCancel(true);
         evt.setNewPage(loTarget);
      }
   }

//END_EVENT_DocAssembleDialog_beforeActionPerformed}}
//{{EVENT_T2PRNT_ASEM_JOB_QRY_beforeQuery
   void T2PRNT_ASEM_JOB_QRY_beforeQuery(VSQuery query ,VSOutParam resultset )
   {
      //Write Event Code below this line

      SearchRequest loSrch = new SearchRequest();
      VSRow loHdr = T1DOC_HDR.getCurrentRow();
      StringBuffer lsbWhere = new StringBuffer(32);

      if(loHdr != null)
      {
         /*This will build a runtime SQL as shown below and append it to VSQuery
         (R_PRNT_JOB.APPL_RSRC_ID='DOC_CD'  AND
         (R_PRNT_JOB.DOC_PHASES LIKE '%99%' OR
                     R_PRNT_JOB.DOC_PHASES LIKE '%DOC_PHASE_CD%'))*/

         lsbWhere.append(" R_PRNT_JOB.APPL_RSRC_ID='"+
               AMSSQLUtil.getANSIQuotedStr(loHdr.getData("DOC_CD").getString())+ "' ")
         .append(" AND (R_PRNT_JOB.DOC_PHASES LIKE '%"+ CVL_DOC_PHASE_CDImpl.ALL_DOC_PHASES +"%' ")
         .append(" OR R_PRNT_JOB.DOC_PHASES LIKE '%"+
               AMSSQLUtil.getANSIQuotedStr(loHdr.getData("DOC_PHASE_CD").getString())+ "%') ");

         loSrch.add(lsbWhere.toString());
         query.addFilter(loSrch);
      }
   }

//END_EVENT_T2PRNT_ASEM_JOB_QRY_beforeQuery}}
//{{EVENT_DocAssembleDialog_beforeGenerate
   void DocAssembleDialog_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
   {

      //Write Event Code below this line
      VSRow loPrntRow = getDataSource("T2PRNT_ASEM_JOB_QRY").getCurrentRow();
      msDocCd = T1DOC_HDR.getCurrentRow().getData("DOC_CD").getString();
      moLog.debug("Generating Assembly Page for Doc Cd = " + msDocCd);
      if (loPrntRow == null)
      {
         raiseException(
            "No assembly job is available for this document code.",
            AMSPage.SEVERITY_LEVEL_ERROR);
      }
      moCustActnTCE =
         (TextContentElement)getElementByName( PRNT_ACTNS_DISPLAY_AREA );
      generateCustomPrintActions();

   }

//END_EVENT_DocAssembleDialog_beforeGenerate}}

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	T2PRNT_ASEM_JOB_QRY.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			DocAssembleDialog_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocAssembleDialog_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T2PRNT_ASEM_JOB_QRY) {
			T2PRNT_ASEM_JOB_QRY_beforeQuery(query , resultset );
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
   * This method will trigger assembe action on documen header.
   * It will return the page to which to be transitioned.
   * @param foPLSReq  the http request object
   * @return the VSPage object
   */
   private VSPage createAssemblyReq(PLSRequest foPLSReq)
   {
      if(moLog.isDebugEnabled())
      {
         moLog.debug("DocAssembleDialog.createAssemblyReq");
      }

      VSRow      loVSRow ;
      DataSource loRootDS ;
      String     lsDI_ACTN_CD ;
      String     lsDI_SUB_ACTN_CD;
      String     lsDI_MSG_TXT ;
      String     [] lsMsgParams = new String[2] ;
      String     [] lsRespParams ;
      int        liRetStatus ;
      VSSession  loSession = getParentApp().getSession() ;

      VSPage loSourcePage = getSourcePage() ;
      if ( loSourcePage instanceof AMSDocTabbedPage )
      {
         // Regenerate the entire document page
         ((AMSDocTabbedPage)loSourcePage).setGenTabPage( true ) ;

      }

      loRootDS = getRootDataSource() ;
      if(moLog.isDebugEnabled())
      {
         moLog.debug("loRootDS.name=" + loRootDS.getName());
      }
      loVSRow  = loRootDS.getCurrentRow() ;
      // If no row is available then we can return
      if (loVSRow == null)
      {
         raiseException(
            "A document must be selected to perform this action",
            AMSPage.SEVERITY_LEVEL_ERROR);
         return loSourcePage;
      }

      // Get the user's print options selections and
      // put them in the session property so the VLS can use them
      VSRow loPrntRow = getDataSource("T2PRNT_ASEM_JOB_QRY").getCurrentRow();
      if( loPrntRow == null)
      {
         raiseException(
            "An assembly job must be selected.",
            AMSPage.SEVERITY_LEVEL_ERROR);
         return loSourcePage;
      }

      lsMsgParams[0] = loPrntRow.getData("PRNT_JOB_CD").getString();
      if(moLog.isDebugEnabled())
      {
         moLog.debug( "PrintRsrc=" + lsMsgParams[0] ) ;
      }
      if (lsMsgParams[0] ==null || lsMsgParams[0].trim().length()==0)
      {
         raiseException("Invalid print job selected",
            AMSPage.SEVERITY_LEVEL_ERROR);
         return loSourcePage;
      }

      //  get Inactive Procurement Lines checkbox and set the session message here
      String lsHideInactive = foPLSReq.getParameter("Hide Inactive Procurement Lines");

      if( lsHideInactive == null )
      {
         lsMsgParams[1] = "false";
      }
      else
      {
         lsMsgParams[1] = "true";
      }
      // Set the message text
      lsDI_MSG_TXT = AMSHyperlinkActionElement.createMessageText( lsMsgParams ) ;

      // Set up the messages to be sent to the server
      // Set the action code
      lsDI_ACTN_CD = Integer.toString( DOC_ACTN_OTHER ) ;
      lsDI_SUB_ACTN_CD = Integer.toString(AMSDocAppConstants.DOC_SUB_ACTN_ASSEMBLE);

      // Set the message in the session object
      AMSHyperlinkActionElement.setSessionMessage( lsDI_ACTN_CD, lsDI_SUB_ACTN_CD, lsDI_MSG_TXT, loSession, this ) ;

      // Set the action to be performed in the data row
      loVSRow.getData( ATTR_DOC_ACTN_CD ).setInt( DOC_ACTN_OTHER ) ;
      loVSRow.getData("DOC_S_ACTN_CD").setInt(  AMSDocAppConstants.DOC_SUB_ACTN_ASSEMBLE);
      loVSRow.getData( ATTR_DOC_S_FUNC_CD ).setInt( DOC_S_FUNC_ONLINE ) ;

      // Update the data source to perform the action on the VLS
      loRootDS.updateDataSource() ;

      // Retrieve the response parameters from the session object
      lsRespParams = AMSHyperlinkActionElement.parseResponseText(
         AMSHyperlinkActionElement.getSessionResponse( loSession, this ) ) ;

      if(moLog.isDebugEnabled())
      {
         moLog.debug("DocAssembleDialog.assembleDocumen.lsRespParams=" +lsRespParams[0]);
      }
      if (lsRespParams != null)
      {
         // Get the return status
         liRetStatus = Integer.parseInt(lsRespParams[0]);
         if (liRetStatus != 0)
         {
            raiseException( "Failed to submit the assemble request",
               AMSPage.SEVERITY_LEVEL_ERROR);
         }
      }
      else
      {
         raiseException(
            "There was no response from the server",
            AMSPage.SEVERITY_LEVEL_ERROR);
      }

      // refresh header, otherwise other user modified row excpetion will pop up
      loSourcePage.getRootDataSource().getResultSet().refreshCurrentRow();

      //return loSourcePage;
      // transition to the reqest grid page
      StringBuffer loBuf = new StringBuffer();


      loBuf.append(ATTR_DOC_CD);
      loBuf.append(AMSSQLUtil.getANSIQuotedStr(
         loVSRow.getData(ATTR_DOC_CD).getString(),
         AMSSQLUtil.EQUALS_OPER));

      loBuf.append(" AND ").append(ATTR_DOC_DEPT_CD);
      loBuf.append(AMSSQLUtil.getANSIQuotedStr(
         loVSRow.getData(ATTR_DOC_DEPT_CD).getString(),
         AMSSQLUtil.EQUALS_OPER));

      loBuf.append(" AND " ).append(ATTR_DOC_ID);
      loBuf.append(AMSSQLUtil.getANSIQuotedStr(
         loVSRow.getData(ATTR_DOC_ID).getString(),
         AMSSQLUtil.EQUALS_OPER));

      loBuf.append(" AND ").append(ATTR_DOC_VERS_NO).append("=");
      loBuf.append(loVSRow.getData(ATTR_DOC_VERS_NO).getString());

      AMSDynamicTransition loDynTran =
         new AMSDynamicTransition( "pDocAssembleRequestGrid",
         loBuf.toString(),"REQ_ID DESC",
         "Advantage" ) ;


      /**
      * Set the reference to the source page
      */
      loDynTran.setSourcePage( this.getSourcePage() ) ;

      /**
      * Return the HTML string after calling the generate method on the
      * transition page
      */
      return loDynTran.getVSPage( getParentApp(), getSessionId());

   }


   /**
   * Given a row with document id fields, this method returns
   * the generated Print Dialog
   *
   * @param foDocPrintRow The row for the document to print
   * @param foActnElem The action element that started the copy
   * @param foRequest The PLS request
   * @return The generated print page
   */
   public static String buildAssembleDialog( VSRow foDocPrintRow,
      AMSHyperlinkActionElement foActnElem, PLSRequest foRequest )
   {
      if(moLog.isDebugEnabled())
      {
         moLog.debug("buildAssembleDialog");
      }
      VSPage               loSrcPage   = foActnElem.getPage() ;
      String               lsDestination = "DocAssembleDialog" ;
      String               lsFrameName   = "Detail" ;
      String[]             lsMsgParams = new String[6] ;
      StringBuffer         lsbWhere    = new StringBuffer( 100 ) ;
      AMSDynamicTransition loDynTran ;

      if ( foDocPrintRow == null )
      {
         ( (AMSPage) loSrcPage).raiseException(
            "A row has to be selected to perform this action",
            AMSPage.SEVERITY_LEVEL_ERROR ) ;
         return ( (AMSPage) loSrcPage).generate() ;
      } /* end if ( foDocPrintRow == null ) */

      /* Set the document identifier */
      foActnElem.setDocumentIdentifier( lsMsgParams, foDocPrintRow ) ;

      /* Generate the where clause */
      lsbWhere.append( "DOC_CD=" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[2],true ) ) ;
      lsbWhere.append( " AND DOC_DEPT_CD=" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[3],true ) ) ;
      lsbWhere.append( " AND DOC_ID=" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[4],true ) ) ;
      lsbWhere.append( " AND DOC_VERS_NO=" ) ;
      lsbWhere.append( lsMsgParams[5] ) ;

      /* Create an instance of the dynamic transition */
      loDynTran = new AMSDynamicTransition( lsDestination,
         lsbWhere.toString(), "Advantage" ) ;

      /* Set the frame name */
      loDynTran.setFrameName( lsFrameName ) ;

      /* Set the reference to the source page */
      loDynTran.setSourcePage( loSrcPage ) ;

      /*
      * Return the HTML string after calling the generate method on the
      * transition page
      */
      return loDynTran.generateHTMLPage( loSrcPage.getParentApp(),
         loSrcPage.getSessionId(),
         foRequest ) ;
   } /* end buildPrintDialog() */

   /**
   *
   * This method generates the custom print actions to be displayed on this page
   * based on the print action table.
   *
   */
   private void generateCustomPrintActions()
   {
      int liNumCustomPrintActions = 0;

      SearchRequest loSearchRequest = new SearchRequest();

      loSearchRequest.addParameter("R_PRNT_ACTN", "DOC_CD", msDocCd);

      SearchRequest loOrderBy = new SearchRequest();
      loOrderBy.add(" PRNT_ACTN ");

      VSQuery loQuery = new VSQuery( getParentApp().getSession(),
         "R_PRNT_ACTN",loSearchRequest , loOrderBy) ;

      VSResultSet loRsltSet = null;
      try
      {

         loRsltSet = loQuery.execute() ;
         loRsltSet.last();
         liNumCustomPrintActions = loRsltSet.getRowCount() ;

         if ( liNumCustomPrintActions > 0 )
         {
            String[][] loPrintCaptions = getPrintCaptions(loRsltSet,
               liNumCustomPrintActions);
            StringBuffer lsbHTML   = new StringBuffer( 1000 ) ;
            for(int liIndex= 1; liIndex<= loPrintCaptions.length; liIndex++)
            {

               lsbHTML.append( "<tr>" ) ;

               /*
               * For all actions, create a new scalar check box for the user
               * to edit the action authority for the resource/role combination.
               */
               lsbHTML.append( "<td class=\"label\"><label for=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex- 1][0] ) ;
               lsbHTML.append( "\" title=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex- 1][0] ) ;
               lsbHTML.append( "\">" ) ;
               lsbHTML.append( loPrintCaptions[liIndex- 1][0] ) ;
               lsbHTML.append( " :</label></td><td class=\"val\"><input type=\"checkbox\" vsds=\"\" title=\"") ;
               lsbHTML.append( loPrintCaptions[liIndex- 1][0] ) ;
               lsbHTML.append( "\" " ) ;
               lsbHTML.append( "class=\"adveditable\" ") ;

               //if the default is true from print actions, set the
               //check box to be checked.
               if(loPrintCaptions[liIndex-1][1].equalsIgnoreCase(
                  AMSCommonConstants.TRUE_VAL))
               {
                  lsbHTML.append( "checked=\"checked\" " ) ;
               }

               lsbHTML.append( "name=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex- 1][0]  ) ;
               lsbHTML.append( "\" id=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex- 1][0]  ) ;
               lsbHTML.append( "\" vsdf=\"" ) ;
           //  lsbHTML.append( (loPrintCaptions[liIndex- 1][0]).substring(5)) ;
               lsbHTML.append( msFieldNameForHideInactiveProcurementLines);
               lsbHTML.append( "\"></td>" ) ;
               lsbHTML.append( "</tr>" ) ;
               moLog.debug("\nHTML generated for custom print actions is: ");
               moLog.debug(lsbHTML.toString() );

               moCustActnTCE.setPreserveHTMLTags( true ) ;
               moCustActnTCE.setValue( lsbHTML.toString() ) ;

            }

         } /* end if ( liNumCustomPrintActions > 0 ) */


      }
      catch(Exception loExcep)
      {

         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExcep);

      }


   }//end generateCustomPrintActions

   /**
   *
   * This method gets the print captions from the print action cvl based on
   * what is required from the print action table.
   *
   * @param foResultSet The print action table rows for which we need captions.
   * @param fiRowCount The number of captions we have to retrieve.
   * @return String[][] A String array containing the caption and its
   *                    associated default value.
   */
   private String[][] getPrintCaptions(VSResultSet foResultSet, int fiRowCount)
   {

      String[][] lsTempPrintCaptions = new String[fiRowCount][2];

      StringBuffer lsbInList = new StringBuffer(64);
      lsbInList.append(" CVL_PRNT_ACTN_SV ");
      VSRow loCurrentRow = null;

      if(fiRowCount == 1)
      {
         lsbInList.append (" = ");
         lsbInList.append(
            foResultSet.getRowAt(1).getData("PRNT_ACTN").getString());
         lsTempPrintCaptions[0][1] =
            foResultSet.getRowAt(1).getData("DFLT_VAL").getString();
      }
      else
      {
         /*
         * build in list for cvl query
         *
         */
         lsbInList.append("IN ( ");
         for(int liIndex = 1; liIndex <= fiRowCount; liIndex++)
         {
            if ( liIndex > 1)
            {
               lsbInList.append( ", ");
            }

            loCurrentRow = foResultSet.getRowAt(liIndex);

            lsbInList.append(loCurrentRow.getData("PRNT_ACTN").getString());

            lsTempPrintCaptions[liIndex-1][1] =
               loCurrentRow.getData("DFLT_VAL").getString();

         }

         lsbInList.append(" ) ");
      }

      VSQuery loQuery = new VSQuery( getParentApp().getSession(),
         "CVL_PRNT_ACTN", lsbInList.toString(), "");

      try
      {
         VSResultSet loRslts = null;
         loRslts = loQuery.execute();
         VSRow loCurrentCVLRow = null;
         for(int liIndex = 1; liIndex <= fiRowCount; liIndex++)
         {
            loCurrentCVLRow = loRslts.getRowAt(liIndex);
            lsTempPrintCaptions[liIndex-1][0] =
               loCurrentCVLRow.getData("CVL_PRNT_ACTN_DV").getString();

         }

      }
      catch(Exception loException)
      {

         // Add exception log to logger object
          moLog.error("Unexpected error encountered while processing. ", loException);


      }

      return lsTempPrintCaptions;

   }
}
