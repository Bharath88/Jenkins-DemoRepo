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
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.* ;
import java.rmi.RemoteException;
import java.util.Vector ;

import org.apache.commons.logging.Log;

/*
**  pAltViewObjAttList*/

//{{FORM_CLASS_DECL
public class pAltViewObjAttList extends pAltViewObjAttListBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
      private String  msTitle        = null ;
      private String  msRsrcName     = null ;
      private Vector  mvKeyValues    = null ;
      private boolean mboolRfrshDoc  = false ;
      private String  msAttchFile    = null ;
      private String  msAltGroupId   = null ;
      private boolean mboolDisplayAttachActions = false ;
      private static Log moAMSLog = AMSLogger.getLog( pAltViewObjAttList.class,
            AMSLogConstants.FUNC_AREA_DFLT ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pAltViewObjAttList ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
         setAllowHistory( false ) ;
   }



//{{EVENT_CODE
//{{EVENT_pAltViewObjAttList_beforeActionPerformed
void pAltViewObjAttList_beforeActionPerformed( ActionElement foActnElem,
   PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if ( foActnElem.getName().equals( "DownloadAttachment" ) )
   {
      VSRow       loCurrRow ;
      VSQuery     loQuery ;
      VSResultSet loResultSet ;

      acceptData( foPLSReq ) ;
      loCurrRow = T1OBJ_ATT_ALT_QRY.getCurrentRow() ;
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
   else if ( foActnElem.getName().equals( "T1OBJ_ATT_ALT_QRYdelete" ) )
   {
      VSORBSession loSession     = getParentApp().getSession().getORBSession() ;
      VSRow        loCurrRow ;
      int          liKeyColCount = mvKeyValues.size() ;
      String []    loParams      = new String[7 + liKeyColCount] ;
      String       lsMsgTxt  ;


      loCurrRow = T1OBJ_ATT_ALT_QRY.getCurrentRow() ;

      if ( loCurrRow != null )
      {
         loParams[0]  = OBJ_ATT_TYPE_ALT_VIEW ;
         loParams[1]  = msRsrcName ;
         loParams[2]  = loCurrRow.getData( "DOC_CAT" ).getString() ;
         loParams[3]  = loCurrRow.getData( "DOC_TYP" ).getString() ;
         loParams[4]  = loCurrRow.getData( "DOC_CD" ).getString() ;
         loParams[5]  = loCurrRow.getData( "DOC_DEPT_CD" ).getString() ;
         loParams[6]  = loCurrRow.getData( "DOC_ID" ).getString()  ;
         //BGN ADVHR00017649

         /* Check if the document to which the attachment is associated with is in the
         ** FINAL state. Users should not be able to delete attachments from documents
         ** in the FINAL state.
         */

         String lsVersion = loCurrRow.getData("DOC_VERS_NO").getString();
         String lsHdrNm=loParams[4]+"_DOC_HDR";
         String lsWhereClause = "DOC_CD = "+"'"+ AMSSQLUtil.getANSIQuotedStr ( loParams[4] ) +"'"+ " AND " + "DOC_ID = " +
                                "'"+ AMSSQLUtil.getANSIQuotedStr ( loParams[6] )+"'"+" AND " + "DOC_DEPT_CD = " +"'"+ AMSSQLUtil.getANSIQuotedStr ( loParams[5] ) +"'"+
                                " AND " + "DOC_VERS_NO = "+lsVersion;


         VSQuery loQuery=new VSQuery(getParentApp().getSession(),lsHdrNm,lsWhereClause," ");
         VSResultSet loresult=loQuery.execute();
         if(loresult != null)
         {
             int lidocphase=loresult.first().getData("DOC_PHASE_CD").getInt();
             if(lidocphase== DOC_PHASE_FINAL )
             {
                 raiseException("Cannot delete attachment when the document "+
                                "is  in final state ",SEVERITY_LEVEL_ERROR);
                 return;
             }
         }
         //END ADVHR00017649
         for(int liCount=0;liCount < liKeyColCount ; liCount ++)
         {
            loParams[7+liCount] = (String)mvKeyValues.get(liCount) ;
         }

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

            raiseException( "Unable to set message properties", SEVERITY_LEVEL_SEVERE ) ;
            return ;
         } /* end catch( RemoteException foExp ) */
      } /* end if ( loCurrRow != null ) */
      else
      {
         raiseException( "No attachment record selected", SEVERITY_LEVEL_ERROR ) ;
      } /* end else */

   } /* end else if ( foActnElem.getName().equals( "T1OBJ_ATT_ALT_QRYdelete" ) ) */
   else if ( foActnElem.getName().equals( "ViewAttHistory" ) )
   {
      AMSDynamicTransition loDynTran ;
      pAltViewObjAttHist   loAttchHistPage ;

      if ( (msAltGroupId == null) || (msAltGroupId.trim().length() == 0) )
      {
         loDynTran = new AMSDynamicTransition( "pAltViewObjAttHist", "", "", "Advantage" ) ;
      }
      else
      {
         StringBuffer loBuffer = new StringBuffer(80) ;
         loBuffer.append("OBJ_ATT_ALT_UNID = ") ;
         loBuffer.append(msAltGroupId) ;

         loDynTran = new AMSDynamicTransition( "pAltViewObjAttHist", loBuffer.toString(), "", "Advantage" ) ;
      }

      loDynTran.setAddRecord( false ) ;
      loDynTran.setSourcePage( this ) ;
      loDynTran.setOrderByClause( "OBJ_ATT_DT DESC" ) ;

      loAttchHistPage = (pAltViewObjAttHist)loDynTran.getVSPage( getParentApp(), getSessionId() ) ;
      loAttchHistPage.setAltGrpId( msAltGroupId ) ;

      PLSApp loParentApp = getParentApp() ;
      loParentApp.getPageExpireAlg().pageNavigatedTo( loAttchHistPage ) ;

      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( loAttchHistPage ) ;

   } /* end if ( foActnElem.getName().equals( "ViewAttHistory" ) ) */
   else if ( foActnElem.getName().equals( "ReturnToAltView" ) )
   {
      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( getSourcePage() ) ;
   } /* end else if ( foActnElem.getName().equals( "ReturnToAltView" ) ) */

}
//END_EVENT_pAltViewObjAttList_beforeActionPerformed}}
//{{EVENT_T1OBJ_ATT_ALT_QRY_beforeQuery
void T1OBJ_ATT_ALT_QRY_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   String lsWhereClause = query.getSQLWhereClause() ;

   if ( lsWhereClause == null || lsWhereClause.trim().length() == 0 )
   {
      VSResultSet loResultSet = query.getNewResultSet() ;
      resultset.setValue( loResultSet ) ;
   }
}
//END_EVENT_T1OBJ_ATT_ALT_QRY_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1OBJ_ATT_ALT_QRY.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pAltViewObjAttList_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1OBJ_ATT_ALT_QRY) {
			T1OBJ_ATT_ALT_QRY_beforeQuery(query , resultset );
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
    * Setter method for the primary key vector
    */
   public void setKeyValues( Vector fvKeyValues )
   {
      mvKeyValues = fvKeyValues ;
   } /* end setKeyValues() */

   /**
    * Getter method for the primary key vector
    */
   public Vector getKeyValues()
   {
      return mvKeyValues ;
   } /* end getKeyValues() */

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

   public void setTitle( String fsTitle )
   {
      msTitle = fsTitle ;
   } /* end setTitle() */

   public void setAltGrpId( String fsAltGrpId )
   {
      msAltGroupId = fsAltGrpId ;
   } /* end setAltGrpId() */

   /**
    * Setter method for the resource name
    */
   public void setResourceName( String fsRsrcName )
   {
      msRsrcName = fsRsrcName ;
   } /* end setResourceName() */

   /**
    * Getter method for the resource name
    */
   public String getResourceName()
   {
      return msRsrcName ;
   } /* end getResourceName() */

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
            loTCE.setValue( "Attachments" ) ;
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

      return super.generate() ;
   } /* end generate() */

   /**
    * Overwriting the beforeGenerate method
    *
    * Modification Log : Srinivas Naikoti - 01/08/2003
    *                                     - Added code to show or hide DIV
    *                                     - based on the customization flag
    */
   public void beforeGenerate()
   {
      ActionElement loDeleteAction = (ActionElement)getElementByName( "T1OBJ_ATT_ALT_QRYdelete" ) ;
      DivElement    loDeleteDiv    = (DivElement)getElementByName( "DeleteDiv" ) ;

      if ( loDeleteAction != null && loDeleteDiv != null )
      {
         if ( mboolDisplayAttachActions )
         {
            // Enable the attachment action
            loDeleteAction.setEnabled( true ) ;

            // Show the DIV holding the attachment action
            loDeleteDiv.setVisible( true ) ;
         }
         else
         {
            // Disable the attachment action
            loDeleteAction.setEnabled( false ) ;

            // Hide the DIV holding the attachment action
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

}
