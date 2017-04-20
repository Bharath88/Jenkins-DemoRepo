//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

	import java.rmi.RemoteException;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.vfc.html.* ;

import org.apache.commons.logging.Log;

	/*
	**  pWF_INBOX
	*/

	//{{FORM_CLASS_DECL
	public class pWF_INBOX extends pWF_INBOXBase
	
	//END_FORM_CLASS_DECL}}
	{
	   
	   
	   
      private static Log moAMSLog = AMSLogger.getLog( 
            pWF_INBOX.class,
            AMSLogConstants.FUNC_AREA_WORKFLOW ) ;
	   
	   // Declarations for instance variables used in the form

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pWF_INBOX ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_pWF_INBOX_afterActionPerformed
void pWF_INBOX_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   //Write Event Code below this line
}
//END_EVENT_pWF_INBOX_afterActionPerformed}}
//{{EVENT_pWF_INBOX_afterHTMLDefinitionLoaded
void pWF_INBOX_afterHTMLDefinitionLoaded(HTMLDocumentModel docModel)
{
   //Write Event Code below this line
}
//END_EVENT_pWF_INBOX_afterHTMLDefinitionLoaded}}
//{{EVENT_pWF_INBOX_pageCreated
void pWF_INBOX_pageCreated()
{
   //Write Event Code below this line
}
//END_EVENT_pWF_INBOX_pageCreated}}
//{{EVENT_pWF_INBOX_beforeActionPerformed
void pWF_INBOX_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
}
//END_EVENT_pWF_INBOX_beforeActionPerformed}}
//{{EVENT_pWF_INBOX_afterPageClose
void pWF_INBOX_afterPageClose( PageEvent evt )
{
   //Write Event Code below this line
}
//END_EVENT_pWF_INBOX_afterPageClose}}

	//END_EVENT_CODE}}

	   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	   }

	//{{EVENT_ADAPTER_CODE
	
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_INBOX_afterActionPerformed( ae, preq );
		}
	}
	public void afterHTMLDefinitionLoaded ( VSPage obj, HTMLDocumentModel docModel ){
		Object source = obj;
		if (source == this ) {
			pWF_INBOX_afterHTMLDefinitionLoaded(docModel);
		}
	}
	public void pageCreated ( VSPage obj ){
		Object source = obj;
		if (source == this ) {
			pWF_INBOX_pageCreated();
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pWF_INBOX_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void afterPageClose( VSPage obj, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pWF_INBOX_afterPageClose( evt );
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
	      TextContentElement     loTextElement ;
	      VSSession              loVSSession  = getParentApp().getSession() ;
	      VSORBSession           loORBSession = loVSSession.getORBSession() ;
	      String                 lsNumMsgs    = null ;
	      HyperlinkActionElement loDeleteElem ;
	      DataSource             loDataSource ;
	      VSResultSet            loResultSet ;

	      loTextElement = (TextContentElement)getDocumentModel().getElementByName( "txtNUM_MSGS" ) ;

	      loDeleteElem = (HyperlinkActionElement)getDocumentModel().getElementByName( "T1WF_INBOX26" ) ;
	      loDataSource = loDeleteElem.getDataSource() ;
	      loResultSet  = loDataSource.getResultSet() ;

	      if ( loResultSet.getBufferedRowCount() <= 0 )
	      {
	         loDeleteElem.setEnabled( false ) ;
	      } /* end if ( loResultSet.getBufferedRowCount() <= 0 ) */
	      else
	      {
	         loDeleteElem.setEnabled( true ) ;
	      } /* end else */

	      /* This property is set in AMSJavaMailXDA */
	      try
	      {
	         lsNumMsgs = loORBSession.getProperty( "SessionEmailNumMsgs" ) ;
	      } /* end try */
	      catch( RemoteException foExp )
	      {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

	         raiseException( "Unable to determine number of e-mail messages",
	               SEVERITY_LEVEL_WARNING ) ;
	         lsNumMsgs = "(unknown)" ;
	      } /* end catch( RemoteException foExp ) */
	      if ( (lsNumMsgs == null ) || ( lsNumMsgs.equals( "" ) ) )
	      {
	         loTextElement.setValue( "(empty)" ) ;
	      } /* end if ( (lsNumMsgs == null ) || ( lsNumMsgs.equals( "" ) ) ) */
	      try
	      {
	         int liNumMsgs = Integer.parseInt( lsNumMsgs ) ;

	         if ( liNumMsgs != 1 )
	         {
	            loTextElement.setValue( "(" + liNumMsgs + " messages)" ) ;
	         } /* end if ( liNumMsgs != 1 ) */
	         else
	         {
	            loTextElement.setValue( "(1 message)" ) ;
	         } /* end else */
	      } /* end try */
	      catch( NumberFormatException foNumExp )
	      {
	         raiseException( "Unable to retrieve inbox message count", SEVERITY_LEVEL_WARNING ) ;
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", foNumExp);

	         loTextElement.setValue( "? messages" ) ;
	      } /* end catch( NumberFormatException foNumExp ) */

	      return super.generate() ;
	   } /* end generate() */

	   /**
	    * Turn off VRS since this page uses a custom XDA connector
	    */
	   public boolean useVirtualResultSet( AMSDataSource foDataSource )
	   {
		return false ;
	   }
	}