//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

	import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;

import java.rmi.RemoteException;

	/*
	 **  SetupBatch*/

	//{{FORM_CLASS_DECL
	public class SetupBatch extends SetupBatchBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSBatchConstants
	{
	   // Declarations for instance variables used in the form
	      // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.

	   private AMSDynamicTransition moTreeTran = null ;
	   
	     /** This is the logger object */
      private static Log moAMSLog = AMSLogger.getLog( SetupBatch.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;

	//{{FORM_CLASS_CTOR
	public SetupBatch ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
	    * This method generates the tree page after setting the
	    * mode to configure.
	    *
	    * Modification Log : MGupta  - 05/11/2001
	    *                             - inital version
	    * @return String - generated ReportTreePage HTML Page
	    */
	   public String generate()
	   {
	      try
	      {
	         getParentApp().getSession().getORBSession().setProperty(
	               RSAA_ROLE, AMSReportTreeNode.MODE_CONFIGURE ) ;
	         if ( moTreeTran == null )
	         {
	            moTreeTran = new AMSDynamicTransition( "ReportTreePage", "",
	                  "Reports_Sys_Admin_App" ) ;
	         } /* end if ( moTreeTran == null ) */
	         return moTreeTran.generateHTMLPage( getParentApp(), getSessionId(), null ) ;
	      } /* end try */
	      catch( RemoteException foExp )
	      {
             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", foExp);

	         raiseException( "Unable to set message properties",
	               SEVERITY_LEVEL_SEVERE ) ;
	         return super.generate() ;
	      } /* end catch( RemoteException foExp ) */
	   } /* end generate() */

	 }


