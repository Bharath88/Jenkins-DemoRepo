//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement ;
import java.rmi.RemoteException;
import javax.swing.text.html.Option;
import java.util.Vector ;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;


import org.apache.commons.logging.Log;
/*
**  pRefTblAddObjAtt*/

//{{FORM_CLASS_DECL
public class pRefTblAddObjAtt extends pRefTblAddObjAttBase

//END_FORM_CLASS_DECL}}
{
   private String  msPriGrpID         = null ;
   private String  msSecGrpID         = null ;
   private String  msTableName        = null ;
   private Vector  mvKeyValues        = null ;
   private String  msTitle            = null ;
   private boolean mboolUploadingFile = false ;
   private int []  miAttachmentTypeOptions = null ;
   
   private static Log moAMSLog = AMSLogger.getLog(pRefTblAddObjAtt.class,
         AMSLogConstants.FUNC_AREA_DFLT);

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pRefTblAddObjAtt ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
            setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
         setAllowHistory( false ) ;
   }



//{{EVENT_CODE
//{{EVENT_pRefTblAddObjAtt_beforeActionPerformed
void pRefTblAddObjAtt_beforeActionPerformed( ActionElement foActElem,
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
               + "KB) exceed", SEVERITY_LEVEL_ERROR ) ;
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
         int liKeyColCount = mvKeyValues.size() ;
         /*
          * Size of Params Array is determined based on length of Description string as appl works only with
          * double digit length strings in the Params Array.
          */
         int liNumOfDescParts = AMSHyperlinkActionElement.arraySizeForLargeParm(lsDescription);
         lsFileName = parseFileName( lsFileName ) ;
         int liNumOfFileNmParts = AMSHyperlinkActionElement.arraySizeForLargeParm(lsFileName);
         String []    loParams  = new String[4 + liKeyColCount + liNumOfDescParts + liNumOfFileNmParts] ;
         int liParamsIndex = 0;
         String       lsMsgTxt  ;


         loParams[liParamsIndex++] = OBJ_ATT_TYPE_REF_TBL ;
         loParams[liParamsIndex++] = msPriGrpID != null ? msPriGrpID : "" ;

         liParamsIndex += AMSHyperlinkActionElement.addParmInPartsToParamArray(lsFileName, loParams, liParamsIndex);

         /*
          * Call AMSHyperlinkActionElement.addParmInPartsToParamArray method to add Description String to Params Array.
          * This method returns the number of indexes used by the Description string.
          */
         liParamsIndex += AMSHyperlinkActionElement.addParmInPartsToParamArray(lsDescription, loParams, liParamsIndex);
         loParams[liParamsIndex++] = lsAttType ;
         loParams[liParamsIndex++] = msTableName ;

         for(int liCount=0;liCount < liKeyColCount ; liCount ++)
         {
            loParams[4+liCount+liNumOfDescParts+liNumOfFileNmParts] = (String) mvKeyValues.get(liCount) ;
         }

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
//END_EVENT_pRefTblAddObjAtt_beforeActionPerformed}}

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
			pRefTblAddObjAtt_beforeActionPerformed( ae, evt, preq );
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

   public void setTitle( String fsTitle )
   {
      msTitle = fsTitle ;
   } /* end setTitle() */

   public void setTableName( String fsTableName )
   {
      msTableName = fsTableName ;
   } /* end setTitle() */

   public void setKeyValues( Vector fvKeyValues )
   {
      mvKeyValues = fvKeyValues ;
   } /* end setTitle() */

   public void setAttachmentTypeOptions( int [] fiAttachmentTypeOptions )
   {
      miAttachmentTypeOptions = fiAttachmentTypeOptions ;
   } /* end setAttachmentTypeOptions() */

   public String generate()
   {
      ComboBoxElement    loCBElement = (ComboBoxElement)getElementByName( "AttachType" ) ;

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
             String lsPrimGroupId = null ;
            /*
             * The attachment has been successfully uploaded, so reshow
             * the attachments list page.
             */
            pRefTblObjAttList     loSrcPage = (pRefTblObjAttList)getSourcePage() ;
            DataSource loRootDS  = loSrcPage.getRootDataSource() ;
            VSORBSession loSession = getParentApp().getSession().getORBSession() ;

            try
            {
               lsPrimGroupId = loSession.getProperty( OBJ_ATT_MSG_TXT ) ;
               loSession.setProperty( OBJ_ATT_MSG_TXT, "" ) ;
               loSrcPage.setPrimaryGroupID( lsPrimGroupId ) ;
            } /* end try */
            catch( RemoteException foExp )
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", foExp);

               raiseException( "Unable to get message properties",
                     SEVERITY_LEVEL_SEVERE ) ;
               return super.generate() ;
            } /* end catch( RemoteException foExp ) */

            if ( loRootDS != null )
            {
               StringBuffer loBuffer = new StringBuffer(256);
               loBuffer.append("OBJ_ATT_PG_UNID = '");
               loBuffer.append(AMSSQLUtil.getANSIQuotedStr(lsPrimGroupId));
               loBuffer.append("'");
               loBuffer.append(" AND OBJ_ATT_ST = ") ;
               loBuffer.append(OBJ_ATT_ST_ACTIVE) ;

               VSQueryInfo loOldQueryInfo = loRootDS.getQueryInfo() ;

               VSQueryInfo loNewQueryInfo = new VSQueryInfo(
                      loOldQueryInfo.getMetaQueryName(),
                      loBuffer.toString() ,
                      loOldQueryInfo.getOrderBy(),
                      loOldQueryInfo.getRelnWhere(),
                      loOldQueryInfo.isRelnFromParent()) ;

               loRootDS.setQueryInfo(loNewQueryInfo) ;
               loRootDS.executeQuery() ;
                         } /* end if ( loRootDS != null ) */
                    return loSrcPage.generate() ;
               } /* end if ( getHighestSeverityLevel() < SEVERITY_LEVEL_ERROR ) */
      } /* end if ( mboolUploadingFile ) */

      return super.generate() ;
   } /* end generate() */


}
