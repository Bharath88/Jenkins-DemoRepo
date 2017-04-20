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
	import com.amsinc.gems.adv.common.AMSParams;
	import advantage.AMSBatchUtil;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import org.apache.commons.logging.Log;

	/*
	 **  Job_Output_Files
	 */

	//{{FORM_CLASS_DECL
	public class Job_Output_Files extends Job_Output_FilesBase
	
	//END_FORM_CLASS_DECL}}
	{

	   /** This is the logger object */
	   private static Log moAMSLog = AMSLogger.getLog( Job_Output_Files.class,
	      AMSLogConstants.FUNC_AREA_DFLT ) ;
	   
	   // Declarations for instance variables used in the form
	      String msOutputDirectory =null;
	   String msFileSeparator = System.getProperty("file.separator");
	            // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Job_Output_Files ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
	    * Subclasses method from VSPage to affect log update
	    */
	   public String generate()
	   {
	      TextAreaElement loTextBox = (TextAreaElement)getElementByName("DisplayArea");
	      if (loTextBox !=null)
	      {
	         try
	         {
	            //set output directory path, if successful continue else return
	            if (!updateOutputDirPath())
	            {
	               return super.generate();
	            }
	            loTextBox.setValue(advantage.AMSBatchUtil.readFileToString(msOutputDirectory));
	         }
	         catch(Throwable loExp)
	         {
                // Add exception log to logger object
                moAMSLog.error("Unexpected error encountered while processing. ", loExp);

	         }
	      } // if (loTextBox !=null)
	            return super.generate();
	   } // end of method generate



	   /**
	    * Updates the output directory path based on the
	    *  row currently being viewed on the page
	    */
	   private boolean updateOutputDirPath()
	   {
	      // updates the file path to the directory location pointed to
	      VSRow loCurrentRow  = getRootDataSource().getCurrentRow();
	      if (loCurrentRow !=null)
	      {
	         msOutputDirectory = AMSParams.msAdvantageLogFolder + msFileSeparator +
		      AMSBatchConstants.LOG_OUTPUT_DIR+ msFileSeparator +
	            loCurrentRow.getData("AGNT_ID").getString()  + "_" +
	            loCurrentRow.getData("BASE_TMPL_PATH").getString() +
	            AMSBatchConstants.LOG_FILE_EXT;
	          return true;
	      }
	      else
	      {
	         return false;
	      }
	   } /* end ofupdateOutputDirPath() */
	}
