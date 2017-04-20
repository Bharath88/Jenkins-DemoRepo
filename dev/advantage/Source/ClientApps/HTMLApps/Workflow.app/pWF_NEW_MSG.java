//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
import java.util.Enumeration ;
import com.amsinc.gems.adv.workflow.AMSWorkflowUserQueryPage;
import com.amsinc.gems.adv.vfc.html.* ;
import advantage.AMSStringUtil;
	
	/*
	**  pWF_NEW_MSG
	*/
	
	//{{FORM_CLASS_DECL
	public class pWF_NEW_MSG extends pWF_NEW_MSGBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSWorkflowUserQueryPage
	
	{
	   // Declarations for instance variables used in the form
	
	
	   private boolean mboolRefresh          = false ;
	   private boolean mboolAlreadyRefreshed = false ;
	   private boolean mboolSetReplyJS       = false ;
	   private static final String JAVASCRIPT_INSERT =
	         "submitForm(document.pWF_NEW_MSG,'T1WF_NEW_MSGinsert=T1WF_NEW_MSG','Display')";
	
	   private static final String JAVASCRIPT_PREFILL1 =
	         "<script LANGUAGE=\"JavaScript\">";
	
	   private static final String JAVASCRIPT_SUBJECTVAR =
	         "pWF_NEW_MSG.txtT1SUBJECT.value = \"";
	      private static final String JAVASCRIPT_TOVAR =
	         "\"; pWF_NEW_MSG.txtT1MAIL_TO.value = \"";
	
	   private static final String JAVASCRIPT_PREFILL2 =
	         "\"</script>";
	
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pWF_NEW_MSG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	
	
	
	
	   }
	
	
	
	
	
	//{{EVENT_CODE
	//{{EVENT_pWF_NEW_MSG_afterActionPerformed
void pWF_NEW_MSG_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line
}
//END_EVENT_pWF_NEW_MSG_afterActionPerformed}}
//{{EVENT_pWF_NEW_MSG_beforeActionPerformed
void pWF_NEW_MSG_beforeActionPerformed( ActionElement foActnElem, PageEvent foEvent, PLSRequest foPLSReq )
{
   //Write Event Code below this line
   // "save off" the information that's currently in the fields so that
   // the information'll be around when we return from the address book
   if ( foActnElem.getName().equals( "displayaddr" ) )
   {
      acceptData( foPLSReq ) ;
      foEvent.setCancel( true ) ;
      foEvent.setNewPage( showAddrBookPage( foPLSReq ) ) ;
   } /* end if ( foActnElem.getName().equals( "displayaddr" ) ) */
}
//END_EVENT_pWF_NEW_MSG_beforeActionPerformed}}
//{{EVENT_pWF_NEW_MSG_beforeGenerate
void pWF_NEW_MSG_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line

   /*
    * To prevent possible XSS attacks convert BODY textarea
    * to special characters
    */
   VSRow loRow = T1WF_NEW_MSG.getCurrentRow();
   if ( loRow != null )
   {
      String lsData = loRow.getData( "BODY" ).getString();
      try
      {
         if(!AMSStringUtil.strIsEmpty(lsData))
         {
            loRow.getData( "BODY" ).
            setString(AMSPLSDocUtil.convertSpecialChars(lsData));
         }
      }
      catch( IOException loExp )
      {
         raiseException( loExp.getMessage(), SEVERITY_LEVEL_ERROR );
      }
   }
}
//END_EVENT_pWF_NEW_MSG_beforeGenerate}}

	//END_EVENT_CODE}}
	
	
	   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	
	
	   }
	
	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pWF_NEW_MSG_beforeGenerate(docModel, cancel, output);
		}
	}
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_NEW_MSG_afterActionPerformed( ae, preq );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_NEW_MSG_beforeActionPerformed( ae, evt, preq );
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
	
	   public String generate()
	   {
	      String            lsJS            = null ;
	      String            lsPrevAttribute = null ;
	      String            lsHTML          = null ;
	      HTMLDocumentModel loDocModel      = getDocumentModel() ;
	      ActionElement     loRtrnElem = null ;
	      PageNavigation    loNav           = getSourceNavigation() ;
	      String            lsNavName       = loNav.getName() ;
	      boolean           lboolReply      = lsNavName.equalsIgnoreCase( "T2pWF_NEW_MSG" ) ;
	
	      if ( !mboolRefresh )
	      {
	         /*
	          * All transitions to this page should now be in insert
	          * mode, so this is no longer needed.  It was originally
	          * included because previously, transitions from the navigation
	          * panel could not be in any other mode but browse.  They
	          * now can be in insert mode, so this code has been commented
	          * out.
	          *
	          * appendOnloadString( JAVASCRIPT_INSERT ) ;
	          */
	         mboolRefresh = true ;
	      } /* end if ( !mboolRefresh ) */
	      else
	      {
	         mboolAlreadyRefreshed = true ;
	      } /* end else */
	
	      lsHTML = super.generate() ;
	
	      /*
	       * If we're coming from a displayed message, pre-populate the to address
	       * and subject fields; however, once the user has done his typing, don't
	       * change the information (for display purposes)
	       */
	      if ( ( !mboolAlreadyRefreshed ) || ( !mboolSetReplyJS ) )
	      {
	         if ( lboolReply )
	         {
	            VSPage loFromPage = getSourcePage() ;
	
	            if ( loFromPage != null )
	            {
	               Enumeration loDSEnum     = loFromPage.getDataSourceList() ;
	
	               if ( ( loDSEnum != null ) && ( loDSEnum.hasMoreElements() ) )
	               {
	                  DataSource loDataSource = (DataSource)loDSEnum.nextElement() ;
	                  VSRow      loRow        = loDataSource.getCurrentRow() ;
	
	                  if ( loRow != null )
	                  {
	                     VSData loFrom = loRow.getData( "MAIL_FROM" ) ;
	                     VSData loSubj = loRow.getData( "SUBJECT" ) ;
	
	                     if ( ( loFrom != null ) && ( loSubj != null ) )
	                     {
	                        String lsToAddr  = loFrom.getString() ;
	                        String lsSubject = loSubj.getString() ;
	
	                        if ( lsSubject == null )
	                        {
	                           lsSubject = "Re: " ;
	                        } /* end if ( lsSubject == null ) */
	                        else if ( !lsSubject.startsWith( "Re: " ) )
	                        {
	                           lsSubject = "Re: " + lsSubject ;
	                        } /* end else if ( !lsSubject.startsWith( "Re: " ) ) */
	
	                        lsJS = JAVASCRIPT_PREFILL1 + JAVASCRIPT_SUBJECTVAR + lsSubject
	                               + JAVASCRIPT_TOVAR + lsToAddr + JAVASCRIPT_PREFILL2 ;
	                        if ( mboolAlreadyRefreshed )
	                        {
	                           /* Don't change this prematurely */
	                           mboolSetReplyJS = true ;
	                        } /* end if ( mboolAlreadyRefreshed ) */
	                     } /* end if ( ( loFrom != null ) && ( loSubj != null ) ) */
	                  } /* end if ( loRow != null ) */
	               } /* end if ( ( loDSEnum != null ) && ( loDSEnum.hasMoreElements() ) ) */
	            } /* end if ( loFromPage != null ) */
	         } /* end if ( lboolReply ) */
	      } /* end if ( ( !mboolAlreadyRefreshed ) || ( !mboolSetReplyJS ) ) */
	
	      if ( lsJS != null )
	      {
	         int liBodyLocation = lsHTML.indexOf("</body>") ;
	         return ( lsHTML.substring( 0, liBodyLocation ) + lsJS
	                  + lsHTML.substring( liBodyLocation ) ) ;
	      } /* end if ( lsJS != null ) */
	      else
	      {
	         return lsHTML ;
	      } /* end else */
	   } /* end generate() */
	
	   /**
	    * This method gets called from the address book.  The
	    * selected addresses are incorporated into the specified field.
	    * @param fsField the field to receive the address(es)
	    * @param fsValue the additional address(es)
	    */
	   public void setEmailInfo( String fsField, String fsValue )
	   {
	      VSData loData   = T1WF_NEW_MSG.getCurrentRow().getData( fsField ) ;
	      String lsCurVal = loData.getString() ;
	
	      if ( ( fsValue == null ) || ( fsValue.equals( "" ) ) )
	      {
	         /* Nothing to add */
	         return ;
	      } /* end if ( ( fsValue == null ) || ( fsValue.equals( "" ) ) ) */
	      if ( ( lsCurVal != null ) && ( !lsCurVal.equals( "" ) ) )
	      {
	         lsCurVal += ", ";
	      } /* end if ( ( lsCurVal != null ) && ( !lsCurVal.equals( "" ) ) ) */
	      loData.setString( lsCurVal + fsValue ) ;
	   } /* end setEmailInfo() */
	
	
	   /**
	     * The following methods are not to be implemented here.
	     * These methods are coded because of implementation of
	     * of interface AMSWorkflowUserQueryPage and actual
	     * implementation is in pWF_USER_QRY_Grid.
	     */
	   public String selectUser( PLSRequest foPLSReq )
	   {
	   return null;
	   }
	
	   public void setCase( int fiCase )
	   {
	
	   }
	
	   public int getCase()
	   {
	      return 0;
	   }
	
	   /**
	    * Turn off VRS since this page uses a custom XDA connector
	    */
	   public boolean useVirtualResultSet( AMSDataSource foDataSource )
	   {
	      return false ;
	   }
	
	   private VSPage showAddrBookPage( PLSRequest foRequest )
	   {
	      String               lsSessionID   = getSessionId() ;
	      PLSApp               loParentApp   = getParentApp() ;
	      VSPage               loTransPage ;
	      AMSDynamicTransition loDynTran ;
	      BoundElement         loAddrColElem ;
	
	      loDynTran = new AMSDynamicTransition( "pWF_EMAIL_ID_QRY_Grid", "", "Workflow" ) ;
	      loDynTran.setSourcePage( this ) ;
	      loTransPage = loDynTran.getVSPage( loParentApp, lsSessionID ) ;
	      loAddrColElem = loTransPage.getElementByName( AMSHyperlinkActionElement.EMAIL_ADDR_NAME ) ;
	      loAddrColElem.addAttribute( AMSHyperlinkActionElement.EMAIL_COL_INFO, "MAIL_TO" );
	      loParentApp.getPageExpireAlg().pageNavigatedTo( loTransPage ) ;
	
	      return loTransPage ;
	   }
	}
	