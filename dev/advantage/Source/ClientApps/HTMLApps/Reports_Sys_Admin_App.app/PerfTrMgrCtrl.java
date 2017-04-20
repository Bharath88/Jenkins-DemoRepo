//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import org.apache.commons.logging.Log;
import com.amsinc.gems.adv.common.tracing.*;
	/*
	**  PerfTrMgrCtrl*/

	//{{FORM_CLASS_DECL
	public class PerfTrMgrCtrl extends PerfTrMgrCtrlBase
	
	//END_FORM_CLASS_DECL}}
	{
	   
	   /** This is the logger object */
	   private static Log moAMSLog = AMSLogger.getLog( PerfTrMgrCtrl.class,
	      AMSLogConstants.FUNC_AREA_DFLT ) ;

		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code.
		//{{FORM_CLASS_CTOR
		public PerfTrMgrCtrl ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}


		//{{EVENT_CODE
		//{{EVENT_PerfTrMgrCtrl_beforeActionPerformed
void PerfTrMgrCtrl_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{


   String lsAction = ae.getName();
   if (lsAction != null && lsAction.equals("flushTraceBuffers" ) )
   {
      try
      {
         AMSPerfTraceManager loTrMgr = AMSPerfTraceManager.getTraceManager();
         if (loTrMgr  !=null)
         {
            loTrMgr.flushTimerBuffers(); //flush timer data
         }
         else
         {
            raiseException("Could not locate trace manager: check if tracing off",
	                      SEVERITY_LEVEL_INFO);
         }
      }//try
      catch(Throwable loExp)
      {
         // we have to catch throwable here as the methd flushTimerBuffers
         // could encounter error and thus throws throwable
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

         raiseException("Could not flush trace buffer data:" +
                      loExp.getMessage(),SEVERITY_LEVEL_INFO);
      }
   }


}
//END_EVENT_PerfTrMgrCtrl_beforeActionPerformed}}

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
			PerfTrMgrCtrl_beforeActionPerformed( ae, evt, preq );
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


	}