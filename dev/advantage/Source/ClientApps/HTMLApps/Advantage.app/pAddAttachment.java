//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement ;
import java.rmi.RemoteException;
import javax.swing.text.html.Option;

import org.apache.commons.logging.Log;

import com.amsinc.gems.adv.vfc.html.AMSPage;
import com.amsinc.gems.adv.vfc.html.AMSDocTabbedPage;

/*
**  pAddAttachment*/

//{{FORM_CLASS_DECL
public class pAddAttachment extends pAddAttachmentBase

//END_FORM_CLASS_DECL}}
{
   private String  msPriGrpID         = null ;
   private String  msSecGrpID         = null ;
   private String  msDocCat           = null ;
   private String  msDocType          = null ;
   private String  msDocCode          = null ;
   private String  msDocDept          = null ;
   private String  msDocID            = null ;
   private String  msDocVersNo        = null ;
   private String  msDocIntLnNoNm     = null ;
   private String  msDocIntLnNo       = null ;
   private String  msDocHdrNm         = null ;
   private String  msDocCompNm        = null ;
   private String  msTitle            = null ;
   private boolean mboolIsDocComp     = false ;
   private boolean mboolUploadingFile = false ;
   private int []  miAttachmentTypeOptions = null ;
   
   private static Log moAMSLog = AMSLogger.getLog( pAddAttachment.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;

   // Header row and component row need to be set in this page so that
   // these objects can be refreshed when an attachment is deleted from
   // a document component or header.
   private VSRow   moHdrRow       = null ;
   private VSRow   moCompRow      = null ;


   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pAddAttachment ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
         setAllowHistory( false ) ;
   }



//{{EVENT_CODE
//{{EVENT_pAddAttachment_beforeActionPerformed
void pAddAttachment_beforeActionPerformed( ActionElement foActElem,
           PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if ( foActElem.getName().equals( "T1IN_OBJ_ATT_STORSaveAll" ) )
   {
      //System.err.println( "Upload file PLS Request: " + foPLSReq.toString() ) ;
      //System.err.println( "****** File: " ) ;
      //System.err.println( foPLSReq.getParameter( "txtT1OBJ_ATT_DATA" ) ) ;
      //System.err.println( "****** End of file" ) ;

      String lsFileName       = foPLSReq.getParameter( "AttchFileName" ) ;
      String lsDescription    = foPLSReq.getParameter( "AttchDscr" ) ;
      String lsAttType        = foPLSReq.getParameter( "AttchType" ) ;
      String lsMaxSizeError   = foPLSReq.getParameter( "UPLOAD_FILE_SIZE_EXCEEDED" ) ;

      if ( lsMaxSizeError != null )
      {
         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;
         raiseException( "Maximum attachment file upload size (" + lsMaxSizeError
               + "KB) exceeded", SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end if ( lsMaxSizeError != null ) */
      if ( ( lsFileName == null ) || ( lsFileName.trim().length() <= 0 ) )
      {
         foPageEvent.setCancel( true ) ;
         foPageEvent.setNewPage( this ) ;
         raiseException( "Unable to create attachment. No file selected.",
                         SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end if ( ( lsFileName == null ) || ( lsFileName.trim().length() <= 0 ) ) */
      else
      {
         if ( ( lsAttType == null ) || ( lsAttType.trim().length() <= 0 ) )
         {
            foPageEvent.setCancel( true ) ;
            foPageEvent.setNewPage( this ) ;
            raiseException( "Attachment Type is required.",
                            SEVERITY_LEVEL_ERROR ) ;
            return ;
         } /* end if ( ( lsFileName == null ) || ( lsFileName.trim().length() <= 0 ) ) */

         VSORBSession loSession = getParentApp().getSession().getORBSession() ;

         lsFileName = parseFileName( lsFileName ) ;

         /*
          * Size of Params Array is determined based on length of Description string and File name
          * as appl works only with double digit length strings in the Params Array.
          */
         String []    loParams  = new String[15 + AMSHyperlinkActionElement.arraySizeForLargeParm(lsDescription)
               + AMSHyperlinkActionElement.arraySizeForLargeParm(lsFileName)];
         int liParamsIndex = 0;
         String       lsMsgTxt  ;

         loParams[liParamsIndex++]  = OBJ_ATT_TYPE_DOC_VIEW ;
         loParams[liParamsIndex++] = msPriGrpID ;

         liParamsIndex += AMSHyperlinkActionElement.addParmInPartsToParamArray(lsFileName, loParams, liParamsIndex);

         /*
          * Call AMSHyperlinkActionElement.addParmInPartsToParamArray method to add Description String and File name
          * string to Params Array.
          * This method returns the number of indexes used by the Description /File name string.
          */
         liParamsIndex += AMSHyperlinkActionElement.addParmInPartsToParamArray(lsDescription, loParams, liParamsIndex);
         loParams[liParamsIndex++] = lsAttType ;
         loParams[liParamsIndex++] = msSecGrpID != null ? msSecGrpID : "" ;
         loParams[liParamsIndex++]  = msDocCat ;
         loParams[liParamsIndex++]  = msDocType ;
         loParams[liParamsIndex++]  = msDocCode ;
         loParams[liParamsIndex++]  = msDocDept ;
         loParams[liParamsIndex++]  = msDocID  ;
         loParams[liParamsIndex++]  = msDocVersNo  ;
         loParams[liParamsIndex++]  = msDocHdrNm  ;
         loParams[liParamsIndex++]  = mboolIsDocComp ? "Y" : "N"  ;
         loParams[liParamsIndex++]  = msDocCompNm != null ? msDocCompNm : "" ;
         loParams[liParamsIndex++] = msDocIntLnNoNm != null ? msDocIntLnNoNm : "" ;
         loParams[liParamsIndex++] = msDocIntLnNo != null ? msDocIntLnNo : "" ;

         mboolUploadingFile = true ;
         lsMsgTxt = AMSHyperlinkActionElement.createMessageText( loParams ) ;

         try
         {
            loSession.setProperty( OBJ_ATT_MSG_TXT, lsMsgTxt ) ;
            loSession.setProperty( "CLIENT_INITIATED", "T" ) ;
         } /* end try */
         catch( RemoteException foExp )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

            raiseException( "Unable to set message properties",
                  SEVERITY_LEVEL_SEVERE ) ;
            return ;
         } /* end catch( RemoteException foExp ) */
      } /* end else */
   } /* end if ( foActElem.getName().equals( "T1IN_OBJ_ATT_STORSaveAll" ) ) */
   else if ( foActElem.getName().equals( "T1IN_OBJ_ATT_STORCancel" ) )
   {
      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( getSourcePage() ) ;
   } /* end else if ( foActElem.getName().equals( "T1IN_OBJ_ATT_STORCancel" ) ) */
}
//END_EVENT_pAddAttachment_beforeActionPerformed}}

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
			pAddAttachment_beforeActionPerformed( ae, evt, preq );
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

   private String parseFileName( String fsFilePath )
   {
      int liIndex ;

      liIndex = fsFilePath.lastIndexOf( '\\' ) ;
      if ( liIndex >= 0 )
      {
         return fsFilePath.substring( liIndex + 1 ) ;
      } /* end if ( liIndex >= 0 ) */
      else
      {
         liIndex = fsFilePath.lastIndexOf( '/' ) ;
         if ( liIndex >= 0 )
         {
            return fsFilePath.substring( liIndex + 1 ) ;
         } /* end if ( liIndex >= 0 ) */
         else
         {
            return fsFilePath ;
         } /* end else */
      } /* end else */
   } /* end parseFileName() */

   public void setPrimaryGroupID( String fsPriGrpID )
   {
      msPriGrpID = fsPriGrpID ;
   } /* end setPrimaryGroupID() */

   public void setSecondaryGroupID( String fsSecGrpID )
   {
      msSecGrpID = fsSecGrpID ;
   } /* end setSecondaryGroupID() */

   public void setDocCat( String fsDocCat )
   {
      msDocCat = fsDocCat ;
   } /* end setDocCat() */

   public void setDocType( String fsDocType )
   {
      msDocType = fsDocType ;
   } /* end setDocType() */

   public void setDocCode( String fsDocCode )
   {
      msDocCode = fsDocCode ;
   } /* end setDocCode() */

   public void setDocDept( String fsDocDept )
   {
      msDocDept = fsDocDept ;
   } /* end setDocDept() */

   public void setDocID( String fsDocID )
   {
      msDocID = fsDocID ;
   } /* end setDocID() */

   public void setDocVersNo( String fsDocVersNo )
   {
      msDocVersNo = fsDocVersNo ;
   } /* end setDocVersNo() */

   public void setDocHeaderName( String fsDocHdrNm )
   {
      msDocHdrNm = fsDocHdrNm ;
   } /* end setDocHeaderName() */

   public void setDocComponentName( String fsDocCompNm )
   {
      msDocCompNm = fsDocCompNm ;
   } /* end setDocComponentName() */

   public void setIntLnNoName( String fsDocIntLnNoNm )
   {
      msDocIntLnNoNm = fsDocIntLnNoNm ;
   } /* end setIntLnNoName() */

   public void setIntLnNo( String fsDocIntLnNo )
   {
      msDocIntLnNo = fsDocIntLnNo ;
   } /* end setIntLnNo() */

   public void setTitle( String fsTitle )
   {
      msTitle = fsTitle ;
   } /* end setTitle() */

   public void setIsDocComp( boolean fboolIsDocComp )
   {
      mboolIsDocComp = fboolIsDocComp ;
   } /* end setIsDocComp() */

   public void setAttachmentTypeOptions( int [] fiAttachmentTypeOptions )
   {
      miAttachmentTypeOptions = fiAttachmentTypeOptions ;
   } /* end setAttachmentTypeOptions() */

   public String generate()
   {
      TextContentElement loTCE       = (TextContentElement)getElementByName( "PAGE_TITLE" ) ;
      ComboBoxElement    loCBElement = (ComboBoxElement)getElementByName( "AttachType" ) ;

      if ( loTCE != null )
      {
         if ( msTitle != null )
         {
            loTCE.setValue( msTitle + " - " ) ;
         } /* end if ( msTitle != null ) */
         else
         {
            loTCE.setValue( "" ) ;
         } /* end else */
      } /* end if ( loTCE != null ) */

      // Check for attachment type options
      if ( loCBElement != null && loCBElement.getSize() != 0)
      {
         if ( miAttachmentTypeOptions != null )
         {
            loCBElement.removeAllElements() ;

            VSQuery loQuery = new VSQuery( getParentApp().getSession(), "CVL_OBJ_ATT_TYP", "", "CVL_OBJ_ATT_TYP_SV" ) ;
            VSResultSet loResultSet = null;
            try
            {
               loResultSet = loQuery.execute() ;
               Option loOption = null ;
               loOption = loCBElement.addElement( "", "" );

               for (int liIndex = 0; liIndex < miAttachmentTypeOptions.length; liIndex++)
               {
                  VSRow loRow = loResultSet.getRowAt( miAttachmentTypeOptions[liIndex] ) ;

                  if (loRow != null )
                  {
                     int liStoredValue = loRow.getData( "CVL_OBJ_ATT_TYP_SV" ).getInt() ;
                     String lsDisplayValue = loRow.getData( "CVL_OBJ_ATT_TYP_DV" ).getString() ;

                     if ( miAttachmentTypeOptions[liIndex] == liStoredValue )
                     {
                        loOption = loCBElement.addElement(lsDisplayValue, new Integer( liStoredValue ).toString() );

                     }
                  }
               }//end for
            }//end try
            finally
            {
               if( loResultSet!= null )
               {
                  loResultSet.close();
               }
            }//end finally
         }
         else
         {
            if ( loCBElement.getSize() == 4 )
            {
               // Remove the "PRICING" element from the drop down
               loCBElement.removeElementAt( loCBElement.getSize() - 1 ) ;
            }
         }

         // Remove the blank element from the drop down
         if ( loCBElement.getSelectedItem().getLabel().equals("") )
         {
            loCBElement.removeElement( "" ) ;
         }
      }

      if ( mboolUploadingFile )
      {
         if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR )
         {
            /*
             * The attachment has been successfully uploaded, so reshow
             * the attachments list page.
             */
            VSPage     loSrcPage = getSourcePage() ;
            DataSource loRootDS  = loSrcPage.getRootDataSource() ;

            if ( loRootDS != null )
            {
               loRootDS.executeQuery() ;
            } /* end if ( loRootDS != null ) */

            // If an attachment is added to the document component then
            // the header and component needs to be refreshed so that the
            // counts can be updated. This is needed so that the attachments
            // image on the component line displays correctly after an
            // attachment is deleted or added

            if ( moHdrRow != null )
            {
               moHdrRow.refresh( true ) ;
            } /* end if ( moHdrRow != null ) */
            if ( moCompRow != null )
            {
               moCompRow.refresh( true ) ;
            } /* end if ( moCompRow != null ) */

            // This method needs to be added here because after adding an
            // attachment, the secondary navigator needs to be refreshed
            // since the attachment counts need to be updated.

            ((AMSPage)loSrcPage).appendOnloadString("UTILS_LoadSecondaryNavigator()");

            AMSDocTabbedPage loParentSrcPage = getDocumentPage(loSrcPage);
            if (loParentSrcPage != null )
            {
               loParentSrcPage.updateTabInformationForAttachmentCount();
            }
            return loSrcPage.generate() ;
         } /* end if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR ) */
      } /* end if ( mboolUploadingFile ) */

      return super.generate() ;
   } /* end generate() */

   public void setHeaderRow( VSRow foHdrRow )
   {
      moHdrRow = foHdrRow ;
   } /* end setHeaderRow() */

   public void setCompRow( VSRow foCompRow )
   {
      moCompRow = foCompRow ;
   } /* end setCompRow() */

}
