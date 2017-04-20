//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSPage;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition ;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement ;
import com.amsinc.gems.adv.common.* ;

import org.apache.commons.logging.Log;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.Vector ;
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.common.AMSCommonConstants;
/*
**  pRefTblObjAttList*/

//{{FORM_CLASS_DECL
public class pRefTblObjAttList extends pRefTblObjAttListBase

//END_FORM_CLASS_DECL}}
{
   private String  msPriGrpID     = null ;
   private String  msTitle        = null ;
   private String  msTableName    = null ;
   private Vector  mvKeyValues    = null ;
   private boolean mboolNoParent  = false ;
   private VSRow   moRefTableRow  = null ;
   private boolean mboolRfrsh     = false ;
   private String  msAttchFile    = null ;
   private int []  miAttachmentTypes       = null ;
   private int []  miAttachmentTypeOptions = null ;
   private boolean mboolDisplayAttachActions = true ;
   private TextContentElement moPageTitle  = null ;
   private static Log moLog = AMSLogger.getLog( pRefTblObjAttList.class,
       AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;

   /**
    * The constant to define the Page Title.
    */
   private static final String msPageTitle = "Reference Table Attachments";

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pRefTblObjAttList ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
         setAllowHistory( false ) ;
   }



//{{EVENT_CODE
//{{EVENT_pRefTblObjAttList_beforeActionPerformed
void pRefTblObjAttList_beforeActionPerformed( ActionElement foActnElem,
   PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if ( foActnElem.getName().equals( "AddAttachment" ) )
   {

      setTblAttachInfo();

      AMSDynamicTransition loDynTran ;
      pRefTblAddObjAtt     loAttchPage ;

      loDynTran = new AMSDynamicTransition( "pRefTblAddObjAtt", "", "", "Advantage" ) ;
      loDynTran.setAddRecord( true ) ;
      loDynTran.setSourcePage( this ) ;

      loAttchPage = (pRefTblAddObjAtt)loDynTran.getVSPage( getParentApp(), getSessionId() ) ;
      loAttchPage.setPrimaryGroupID( msPriGrpID ) ;
      loAttchPage.setTitle( msTitle ) ;
      loAttchPage.setTableName( msTableName ) ;
      loAttchPage.setKeyValues( mvKeyValues ) ;

      // Check for attachment type options
      if (miAttachmentTypeOptions != null)
      {
         loAttchPage.setAttachmentTypeOptions( miAttachmentTypeOptions ) ;
      }

      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( loAttchPage ) ;

      mboolRfrsh = true ;
   } /* end if ( foActnElem.getName().equals( "AddAttachment" ) ) */
   else if ( foActnElem.getName().equals( "T1IN_OBJ_ATT_CTLGdelete" ) )
   {
      VSORBSession loSession = getParentApp().getSession().getORBSession() ;

      int liKeyColCount = mvKeyValues.size() ;
      String []    loParams  = new String[2 + liKeyColCount] ;
      String       lsMsgTxt  ;

      loParams[0] = OBJ_ATT_TYPE_REF_TBL ;
      loParams[1] = msTableName ;

      for(int liCount=0;liCount < liKeyColCount ; liCount ++)
      {
         loParams[2+liCount] = (String)mvKeyValues.get(liCount) ;
      }

      lsMsgTxt = AMSHyperlinkActionElement.createMessageText( loParams ) ;
      try
      {
         loSession.setProperty( OBJ_ATT_MSG_TXT, lsMsgTxt ) ;
         loSession.setProperty( "CLIENT_INITIATED", "T" ) ;
      } /* end try */
      catch( RemoteException foExp )
      {
         foExp.printStackTrace() ;
         raiseException( "Unable to set message properties",
               SEVERITY_LEVEL_SEVERE ) ;
         return ;
      } /* end catch( RemoteException foExp ) */
      mboolRfrsh = true ;
   } /* end else if ( foActnElem.getName().equals( "T1IN_OBJ_ATT_CTLGdelete" ) ) */
   else if ( foActnElem.getName().equals( "DownloadAttachment" ) )
   {
      VSRow       loCurrRow ;
      VSQuery     loQuery ;
      VSResultSet loResultSet ;

      VSORBSession loSession = getParentApp().getSession().getORBSession() ;
      
      if (AMSStringUtil.strInsensitiveEqual(
            AMSParams.msUseECMForAttachments, "true"))
      {
         try
         {
            loSession.setProperty(ECM_MSG_TXT, "fetchFromECM");
         }
         catch(Exception ae)
         {
            raiseException("Unable to set property for fetch",SEVERITY_LEVEL_ERROR);
         }
      }
      acceptData( foPLSReq ) ;
      loCurrRow = T1IN_OBJ_ATT_CTLG.getCurrentRow() ;
      if ( loCurrRow != null )
      {
         loQuery = new VSQuery( getParentApp().getSession(), "IN_OBJ_ATT_STOR",
               "OBJ_ATT_UNID=" + loCurrRow.getData( "OBJ_ATT_UNID" ).getLong(), "" ) ;

         // Fix for the download of binary types
         loQuery.setColumnProjectionLevel( DataConst.ALLTYPES ) ;

         loResultSet = loQuery.execute() ;

         if ( loResultSet != null )
         {
            loResultSet.last() ;

            if ( loResultSet.getRowCount() == 1 )
            {
               byte [] lbAttchData =
                  loResultSet.first().getData( "OBJ_ATT_DATA" ).getBytes();

               if (lbAttchData != null)
               {
                  try
                  {
                     msAttchFile = new String(lbAttchData, 0, lbAttchData.length, "ISO8859_1");
                  }
                  catch (UnsupportedEncodingException loExcep)
                  {
                     raiseException("Encountered an unsupported encoding exception : "
                        + loExcep.getMessage(), SEVERITY_LEVEL_ERROR );
                     msAttchFile = null;
                     foPageEvent.setCancel( true ) ;
                     foPageEvent.setNewPage( this ) ;
                     return;
                  }

                  setDownloadFileInfo( "application/download",
                        loCurrRow.getData( "OBJ_ATT_NM" ).getString(), true ) ;
               }

               if ( msAttchFile == null )
               {
                  /*
                   * This may be because of one of the following reasons:
                   * 1. Attachment file is not found
                   * 2. A zero byte empty .txt file was uploaded and null is stored
                   * on OBJ_ATT_DATA on IN_OBJ_ATT_STOR entry for DB2 and SQL Server.
                   * Note: For Oracle the data was not null and for most other file
                   * extension files the data was not null because of associated metadata.
                   */
                  raiseException( "Attachment file not found " +
                        "or Attachment file is empty with zero size.",
                        SEVERITY_LEVEL_ERROR ) ;
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
      } /* end if ( loCurrRow != null ) */
      else
      {
         raiseException( "No attachment record selected.", SEVERITY_LEVEL_ERROR ) ;
      } /* end else */

      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( this ) ;
   } /* end else if ( foActnElem.getName().equals( "DownloadAttachment" ) ) */
   else if ( foActnElem.getName().equals( "ViewAttHistory" ) )
   {
      AMSDynamicTransition loDynTran ;
      pObjAttHist          loAttchHistPage ;

      if ( (msPriGrpID == null) || (msPriGrpID.trim().length() == 0) )
      {
         loDynTran = new AMSDynamicTransition( "pObjAttHist", "", "", "Advantage" ) ;
      }
      else
      {
         StringBuffer loBuffer = new StringBuffer(100) ;
         loBuffer.append("OBJ_ATT_PG_UNID = '");
         loBuffer.append(AMSSQLUtil.getANSIQuotedStr(msPriGrpID) );
         loBuffer.append("'");

         loDynTran = new AMSDynamicTransition( "pObjAttHist", loBuffer.toString(), "", "Advantage" ) ;
      }

      loDynTran.setSourcePage( this ) ;
      loDynTran.setOrderByClause( "OBJ_ATT_DT DESC" ) ;

      loAttchHistPage = (pObjAttHist)loDynTran.getVSPage( getParentApp(), getSessionId() ) ;
      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( loAttchHistPage ) ;

   } /* end if ( foActnElem.getName().equals( "ViewAttHistory" ) ) */
   else if ( foActnElem.getName().equals( "ReturnToRefTableView" ) )
   {
      if ( mboolRfrsh == true )
      {
         if(moRefTableRow != null)
      {
         moRefTableRow.refresh() ;
      }
   } /* end if ( mboolRfrsh == true ) */

      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( getSourcePage() ) ;
   } /* end else if ( foActnElem.getName().equals( "ReturnToDoc" ) ) */
   else if ( foActnElem.getName().equals( "SearchAttachment" ) )
   {
	  setTblAttachInfo();
      AMSDynamicTransition loDynTran ;
      pContentSearch       loContentSearch ;

      loDynTran = new AMSDynamicTransition( "pContentSearch", "", "", "Advantage" ) ;
      loDynTran.setSourcePage( this ) ;
      loDynTran.setDependent(true);

      loContentSearch = (pContentSearch)loDynTran.getVSPage( getParentApp(), getSessionId() ) ;

      loContentSearch.setPrimaryGroupID( msPriGrpID ) ;
      loContentSearch.setObjAttType( OBJ_ATT_TYPE_REF_TBL) ;
      loContentSearch.setTableName( msTableName ) ;
      loContentSearch.setKeyValues( mvKeyValues ) ;
      if (miAttachmentTypeOptions != null)
      {
    	  loContentSearch.setAttachmentTypeOptions( miAttachmentTypeOptions ) ;
      }
      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( loContentSearch ) ;

      mboolRfrsh = true ;
   }

}
//END_EVENT_pRefTblObjAttList_beforeActionPerformed}}

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
			pRefTblObjAttList_beforeActionPerformed( ae, evt, preq );
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

   public void setPrimaryGroupID( String fsPriGrpID )
   {
      if ( ( fsPriGrpID == null ) || ( fsPriGrpID.trim().length() < 1 ) )
      {
         msPriGrpID = null ;
      } /* end if ( ( fsPriGrpID == null ) || ( fsPriGrpID.trim().length() < 1 ) ) */
      else
      {
         msPriGrpID = fsPriGrpID ;
      } /* end else */
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

   public void setAttachmentTypes( int [] fiAttachmentTypes )
   {
      miAttachmentTypes = fiAttachmentTypes ;
   } /* end setAttachmentTypes() */

   public void setAttachmentTypeOptions( int [] fiAttachmentTypeOptions )
   {
      miAttachmentTypeOptions = fiAttachmentTypeOptions ;
   } /* end setAttachmentTypeOptions() */

   public String doAction( PLSRequest foPLSReq )
   {
      String lsResponse ;

      lsResponse = super.doAction( foPLSReq ) ;

      if ( msAttchFile != null )
      {
         lsResponse = msAttchFile ;
         msAttchFile = null ;
      } /* end if ( mboolReturnAttach ) */
      return lsResponse ;
   } /* end doAction() */

   public void setCurrentRow( VSRow loRefTableRow )
   {
      moRefTableRow = loRefTableRow ;
   } /* end setHeaderRow() */

   /**
    * Setter method for the customization flag, mboolDisplayAttachActions
    */
   public void displayAttachmentActions( boolean fboolDisplayAttachActions )
   {
      mboolDisplayAttachActions = fboolDisplayAttachActions ;
   } /* end displayAttachmentActions() */

   /**
    * Getter method for the customization flag, mboolDisplayAttachActions
    */
   public boolean attachmentActionsDisplayed()
   {
      return mboolDisplayAttachActions ;
   } /* end displayAttachmentActions() */

   public String generate()
   {
      ActionElement      loAddAction    = (ActionElement)getElementByName( "AddAttachment" ) ;
      ActionElement      loSearchAction = (ActionElement) getElementByName("SearchAttachment");
      ActionElement      loDeleteAction = (ActionElement)getElementByName( "T1IN_OBJ_ATT_CTLGdelete" ) ;
      TextContentElement loTCE          = (TextContentElement)getElementByName( "PageTitle" ) ;
      TextContentElement loTCEReturn    = (TextContentElement)getElementByName( "ReturnLinkTitle" ) ;

      if ( loTCE != null )
      {
         if ( msTitle != null )
         {
            loTCE.setValue( msTitle + " Attachments" ) ;
         }
         else
         {
            loTCE.setValue( msPageTitle ) ;
         }
      } /* end if ( loTCE != null ) */

      if ( loTCEReturn != null )
      {
         if ( msTitle != null )
         {
               loTCEReturn.setValue( "Return to " + msTitle ) ;
         }
         else
         {
            loTCEReturn.setValue( "Return" ) ;
         }
      } /* end if ( loTCE != null ) */


      if ( mboolNoParent )
      {
         raiseException( "No parent found for attachments.",
                         SEVERITY_LEVEL_ERROR ) ;
         appendOnloadString( "alert(\'No parent found for attachments\');" ) ;
      } /* end if ( mboolNoParent ) */

      if ( ((AMSPage)getSourcePage()).getEditMode() )
      {
         loDeleteAction.setEnabled( true ) ;
         if ( getHighestSeverityLevel() > SEVERITY_LEVEL_WARNING )
         {
            loAddAction.setEnabled( false ) ;
            loSearchAction.setEnabled(false);
         } /* end if ( getHighestSeverityLevel() > SEVERITY_LEVEL_WARNING ) */
         else
         {
            loAddAction.setEnabled( true ) ;
            if (AMSStringUtil.strInsensitiveEqual(
                  AMSParams.msUseECMForAttachments, "true"))
            {
               loSearchAction.setEnabled(true);
            }
            else
            {
               loSearchAction.setEnabled(false);
            }
         } /* end else */
      } /* end else */

      return super.generate() ;
   } /* end generate() */

   /**
    * Overwriting the beforeGenerate method
    *
    * Modification Log : Srinivas Naikoti - 01/08/2003
    *                                     - Added code to show or hide DIVs
    *                                     - based on the customization flag
    */
   public void beforeGenerate()
   {
      ActionElement loAddAction    = (ActionElement)getElementByName( "AddAttachment" ) ;
      ActionElement loSearchAction = (ActionElement) getElementByName("SearchAttachment");
      ActionElement loDeleteAction = (ActionElement)getElementByName( "T1IN_OBJ_ATT_CTLGdelete" ) ;
      DivElement    loUploadDiv    = (DivElement)getElementByName( "UploadDiv" ) ;
      DivElement    loDeleteDiv    = (DivElement)getElementByName( "DeleteDiv" ) ;


      if ( loAddAction != null && loDeleteAction != null &&
           loUploadDiv != null && loDeleteDiv != null && loSearchAction != null)
      {
         if ( mboolDisplayAttachActions )
         {
            // Enable the attachment actions if the page is in edit mode
            if ( ((AMSPage)getSourcePage()).getEditMode() )
            {
               loAddAction.setEnabled( true ) ;

               if (AMSStringUtil.strInsensitiveEqual(
                     AMSParams.msUseECMForAttachments, "true"))
               {
                  loSearchAction.setEnabled(true);
               }
               else
               {
                  loSearchAction.setEnabled(false);
               }

               loDeleteAction.setEnabled( true ) ;
            }
            else
            {
               loAddAction.setEnabled( false ) ;
               loSearchAction.setEnabled(false);
               loDeleteAction.setEnabled( false ) ;
            }

            // Show the DIVs holding the attachment actions
            loUploadDiv.setVisible( true ) ;
            loDeleteDiv.setVisible( true ) ;
         }
         else
         {
            // Disable the attachment actions
            loAddAction.setEnabled( false ) ;
            loSearchAction.setEnabled(false);
            loDeleteAction.setEnabled( false ) ;

            // Hide the DIVs holding the attachment actions
            loUploadDiv.setVisible( false ) ;
            loDeleteDiv.setVisible( false ) ;
         }
      }
      
      if(getRootDataSource().getCurrentRow() != null)
	  {
    	 // Set the value of hidden variable on the page if we are d/l an HTML attachment 
	     String lsFileNm = getRootDataSource().getCurrentRow().getData( "OBJ_ATT_NM" ).getString();
	     if((lsFileNm.indexOf(".htm") != -1) || (lsFileNm.indexOf(".HTM") != -1) ) 
	     {
	        ((HiddenElement)getElementByName("DownloadType")).setValue("HTM");
	     }
	     else
	     {
	    	 ((HiddenElement)getElementByName("DownloadType")).setValue("");
	     }
      }
      
      super.beforeGenerate() ;
   } /* end beforeGenerate() */

  /**
   *  Sets Page code and User Home Department code to session
   *  info for the purpose of uploading Table Attachment to
   *  External Storage - Documentum.
   *  This will be retrieved in AdvDocumentumHandler class
   */
   public void setTblAttachInfo()
   {
      try
      {
         VSSession loSession = getParentApp().getSession();
         String lsPageCode = ((AMSPage)getSourcePage()).getPageCode();
         AMSUser loUser = AMSSecurity.getUser( loSession.getLogin() );
         String lsHomeDeptCode     = AMSStringUtil.strTrim( loUser.getHomeOrgCodeLevels()[2] );

         if(moLog.isDebugEnabled())
         {
            moLog.debug("Page code: " + lsPageCode);
            moLog.debug("User Home Dept code: " + lsHomeDeptCode);
         }

         loSession.getORBSession().setProperty(AMSCommonConstants.PG_CD_FOR_TABLES, ((AMSPage)getSourcePage()).getPageCode());
         loSession.getORBSession().setProperty(AMSCommonConstants.DEPT_CD_FOR_TABLES, lsHomeDeptCode);
      }
      catch (RemoteException loRemoteEx)
      {
         if(moLog.isDebugEnabled())
         {
            moLog.debug("Page Code and User Home Department Code could not be set in session." + loRemoteEx);
         }
      }
   }
}