//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSUtil;
import advantage.AMSStringUtil;
import advantage.AMSBatchUtil;
import com.amsinc.gems.adv.common.AMSDocAppConstants;
import com.amsinc.gems.adv.common.AMSSecurity;
import com.amsinc.gems.adv.common.AMSSecurityException;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import java.rmi.RemoteException;

/*
**  pIM_SRVC_FLOW_RSRC_Generic
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_FLOW_RSRC_Generic extends pIM_SRVC_FLOW_RSRC_GenericBase

//END_FORM_CLASS_DECL}}
{
   private String msPageTitle = null;
   private String msFlowName  = null;
   private String msAttchFile = null;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_FLOW_RSRC_Generic ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pIM_SRVC_FLOW_RSRC_Generic_beforeGenerate
void pIM_SRVC_FLOW_RSRC_Generic_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   TextFieldElement loTextFieldElement = null;

   /*
    * Prevent update here because comboboxes turn to textboxes when update is not set to true in
    * the xml.  Comboboxes are needed for search.
    */
   T1IM_SRVC_FLOW_RSRC.setAllowUpdate(false);

   // Set length of File Name from 20 to 60.
   loTextFieldElement = (TextFieldElement) getElementByName("txtT1FILE_NM");
   if (loTextFieldElement != null)
   {
      loTextFieldElement.setSize(60);
   }

   // Set length of File Path from 20 to 60.
   loTextFieldElement = (TextFieldElement) getElementByName("txtT1FILE_PATH");
   if (loTextFieldElement != null)
   {
      loTextFieldElement.setSize(60);
   }
}
//END_EVENT_pIM_SRVC_FLOW_RSRC_Generic_beforeGenerate}}
//{{EVENT_pIM_SRVC_FLOW_RSRC_Generic_beforeActionPerformed
void pIM_SRVC_FLOW_RSRC_Generic_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   boolean      lboolAuth       = false;
   int          liAction        = 0;
   String       lsAction        = null;
   String       lsFileName      = null;
   String       lsFilePath      = null;
   String       lsFileSeparator = null;
   String       lsName          = null;
   String       lsResourceID    = null;
   String       lsUserID        = null;
   VSORBSession loSession       = null;
   VSRow        loCurrentRow    = null;

   // Security check for "View File" link.
   lsName = ae.getName();

   if (AMSStringUtil.strEqual(lsName, "T1IM_SRVC_FLOW_RSRCViewFile"))
   {
      liAction = AMSDocAppConstants.DOC_SUB_ACTN_IM_VIEW_FILE;
      lsAction = "View File";
   }
   else
   {
      liAction = 0;
   }

   if (liAction > 0)
   {
      // Authorization check for Schedule action on Resource ID (Service ID)
      loSession    = getParentApp().getSession().getORBSession();
      loCurrentRow = getRootDataSource().getCurrentRow();

      if (loCurrentRow == null)
      {
         raiseException("Please select a row before selecting this action.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Resource ID - for Integration Manager, use Service ID.
      lsResourceID = loCurrentRow.getData("SRVC_ID").getString();

      if (lsResourceID == null)
      {
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      // Get User ID.
      try
      {
         lsUserID = loSession.getUserID();
      }
      catch (RemoteException foException)
      {
         raiseException("Unable to get User ID from session.  " +
               "Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      try
      {
         lboolAuth = AMSSecurity.actionAuthorized(lsUserID, lsResourceID, liAction);
      }
      catch (AMSSecurityException foException)
      {
         raiseException("Cannot access resource for this service (Resource ID " + lsResourceID +
               ").  Please contact your security administrator.", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      if (!lboolAuth)
      {
         raiseException("User is not authorized to perform the " + lsAction + " action for this " +
               "service (Resource ID " + lsResourceID +").", SEVERITY_LEVEL_ERROR);
         evt.setNewPage(this);
         evt.setCancel(true);
         return;
      }

      /*
       * "View File" is a dummy action.  Intercept and open the file for the row selected.
       */
      lsFileName      = loCurrentRow.getData("FILE_NM").getString();
      lsFilePath      = loCurrentRow.getData("FILE_PATH").getString();
      lsFileSeparator = File.separator;

      if (!lsFilePath.endsWith(lsFileSeparator))
      {
         lsFilePath = lsFilePath + File.separator;
      }

      msAttchFile = getFileData(lsFilePath, lsFileName);

      if (msAttchFile != null)
      {
         setDownloadFileInfo("application/download", AMSUtil.getFileNameFromPath(lsFileName), true);
      }
      else
      {
         raiseException("File " + lsFilePath + lsFileName + " does not exist.", SEVERITY_LEVEL_ERROR);
      }

      evt.setNewPage(this);
      evt.setCancel(true);
      return;
   } // end if (liAction > 0)
}
//END_EVENT_pIM_SRVC_FLOW_RSRC_Generic_beforeActionPerformed}}

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
			pIM_SRVC_FLOW_RSRC_Generic_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIM_SRVC_FLOW_RSRC_Generic_beforeActionPerformed( ae, evt, preq );
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


   public String doAction(PLSRequest foPLSReq)
   {
      String lsResponse = null;

      lsResponse = super.doAction(foPLSReq);

      if (msAttchFile != null)
      {
         lsResponse  = msAttchFile;
         msAttchFile = null;
      }

      return lsResponse;
   } // end doAction()


   /**
    * Gets entire content of a file as a string.
    *
    * @param fsFileName - File name with full path
    * @return String - File contents
    */
   public String getFileAsString(String fsFileName)
   {
      BufferedReader loBufferedReader = null;
      StringBuffer   loFileContents   = null;
      String         lsLine           = null;
      String         lsNewLineChar    = null;

      if (fsFileName == null)
      {
         return null;
      }

      try
      {
         loBufferedReader = new BufferedReader(new FileReader(fsFileName));
         loFileContents   = new StringBuffer(1028);

         lsLine        = loBufferedReader.readLine();
         lsNewLineChar = "";

         while (lsLine != null)
         {
            loFileContents.append(lsLine).append(lsNewLineChar);

            lsLine        = loBufferedReader.readLine();
            lsNewLineChar = "\n";
         }
      }
      catch (FileNotFoundException foException)
      {
         return null;
      }
      catch (IOException foException)
      {
         return null;
      }

      return loFileContents.toString();
   } // end getFileAsString()


   /**
    * Opens and reads the file, returns the data as a String for use in a download action.
    *
    * @param fsFileLocation - Full directory.
    * @param fsFileName - File name.
    * @return String - File data.
    */
   private String getFileData(String fsFileLocation, String fsFileName)
   {
      byte[]                lbAttData      = null;
      ByteArrayOutputStream loOutputStream = null;
      FileInputStream       loInputStream  = null;
      int                   liLength       = 0;
      String                lsAttchFile    = null;

      loOutputStream = new ByteArrayOutputStream();
      lbAttData      = new byte[1024];

      // Open File
      try
      {
         loInputStream = new FileInputStream(fsFileLocation + fsFileName);
      }
      catch (FileNotFoundException foFileNotFoundException)
      {
         raiseException("File " + fsFileName + " not found in directory " + fsFileLocation + ".  " +
               "Please contact your system administrator.", SEVERITY_LEVEL_ERROR);
      }

      if (loInputStream == null)
      {
         return lsAttchFile;
      }

      // Read file into byte array
      try
      {
         while ((liLength = loInputStream.read(lbAttData, 0, 1024)) != -1)
         {
            loOutputStream.write(lbAttData, 0, liLength);
         }

         lbAttData = loOutputStream.toByteArray();
      }
      catch (IOException foIOException)
      {
         raiseException("Error encountered while reading file.  " +
               "Please contact your system administrator.", SEVERITY_LEVEL_ERROR);
         lbAttData = null;
      }

      if (lbAttData == null)
      {
         return lsAttchFile;
      }

      // Convert byte array into string with character encoding
      try
      {
         lsAttchFile = new String(lbAttData, 0, lbAttData.length, "ISO8859_1");
      }
      catch (UnsupportedEncodingException foUnsupportedEncodingException)
      {
         raiseException("Encountered unsupported encoding error.  " +
               "Please contact your system administrator.", SEVERITY_LEVEL_ERROR);
         lsAttchFile = null;
      }

      return lsAttchFile;
   } // end getFileData()


   /**
    * Custom implementation of ancestor sets title.
    */
   public String generate()
   {
      // Add Flow ID to page title
      setTitle(getCustomTitle());

      return super.generate();
   }


   /**
    * Appends Flow Name to page title
    *
    * @return String - new title
    */
   private String getCustomTitle()
   {
      DataSource loDataSource = null;
      String     lsTitle      = null;
      VSData     loData       = null;
      VSPage     loSourcePage = null;
      VSRow      loSourceRow  = null;

      if (msPageTitle == null)
      {
         msPageTitle = getTitle();
      }

      lsTitle = msPageTitle;

      loSourcePage = getSourcePage();

      if (loSourcePage != null)
      {
         loDataSource = loSourcePage.getRootDataSource();

         if (loDataSource != null)
         {
            loSourceRow = loDataSource.getCurrentRow();

            if (loSourceRow != null)
            {
               loData = loSourceRow.getData("RUN_ID");

               if (loData != null)
               {
                  msFlowName = " for Flow ID " + loData.getString();
               }
            }
         }
      }

      if (msFlowName == null)
      {
         msFlowName = "";
      }

      return msPageTitle + msFlowName;
   }
}