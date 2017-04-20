//{{COMPONENT_IMPORT_STMTS
package advantage;
import java.util.Enumeration;
import java.util.Vector;
import versata.common.*;
import versata.common.vstrace.*;
import versata.vls.cache.*;
import versata.vls.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.amsinc.gems.adv.common.*;
import com.versata.util.logging.*;
import org.apache.commons.logging.*;

//END_COMPONENT_IMPORT_STMTS}}
import java.io.File;

/*
 **  VW_FORMS_MGMT
 */

//{{COMPONENT_RULES_CLASS_DECL
public class VW_FORMS_MGMTImpl extends  VW_FORMS_MGMTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   
//{{COMP_CLASS_CTOR
public VW_FORMS_MGMTImpl (){
	super();
}

public VW_FORMS_MGMTImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}
      
   }
   
//{{EVENT_CODE

//{{COMP_EVENT_beforeUpdate
   public void beforeUpdate(DataObject obj, Response response)
   {
      //Write Event Code below this line
      // Replace invalid windows file name character with underscore "_"
      this.setFILE_NM(AMSUtil.replaceInvalidFileNmCharacters(getFILE_NM()));
      try
      {
         String lsPendingTransfer = getSession().getProperty("Pend_Xfer");
         if ( lsPendingTransfer != null && lsPendingTransfer.equals("TRUE") )
         {
            AMSTransferForms.getLocation(getSession(), getAGNT_ID(), getFILE_NM());
         }
      }
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      }
   }
   
   
   
   
   
   
//END_COMP_EVENT_beforeUpdate}}

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   //Write Event Code below this line

  // Replace invalid windows file name character with underscore "_"
  this.setFILE_NM(AMSUtil.replaceInvalidFileNmCharacters(getFILE_NM()));
}
//END_COMP_EVENT_beforeInsert}}

//END_EVENT_CODE}}
   
   
   
   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addRuleEventListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }
   
//{{COMPONENT_RULES
	public static VW_FORMS_MGMTImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new VW_FORMS_MGMTImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   
   
   /**
    * Returns filename of generated PDF file for this job when appropriate
    * session properties are set. Returns null when PDF file is not found.
    * @return String PDF File name
    */
   public String getPDFReportData()
   {
      Session loSession		= getSession();
      String lsReportData	= null;
      StringBuffer lsbReportFileName = new StringBuffer(144);
      lsbReportFileName.append(getFILE_LOC())
            .append(File.separator)
            .append(getFILE_NM())
            .append(AMSBatchConstants.PDF_EXT);
      
      // Get the session properties
      String lsShowPDF = loSession.getProperty("show_pdf");
      if ( lsShowPDF != null && lsShowPDF.equals("TRUE") )
      {
         File loPDFFile = new File(lsbReportFileName.toString());
         if( !loPDFFile.exists() )
         {
            AMSUtil.logErrorMsg( moAMSLog, "File not found", 
                  lsbReportFileName.toString());
            return null;
         }
      }//end if
      return lsbReportFileName.toString();
   }//end of method
   
}

