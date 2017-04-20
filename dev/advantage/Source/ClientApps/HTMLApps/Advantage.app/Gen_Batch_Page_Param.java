//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.*;
import java.math.*;
import java.text.*;
import java.io.* ;
import com.amsinc.gems.adv.client.dbitem.*;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.*;
import advantage.AMSStringUtil;
import advantage.*;
import org.apache.commons.logging.Log;


/*
 **  Gen_Batch_Page_Param*/


/**********
Information for Customization:

1) Find for the first instance of <table class="ADVAction RecSrc"> &
Delete the following code including it.

<table class="ADVAction RecSrc">
<tr>
<td>

<ams_map title="Update Actions" name="Update Actions">
<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="saveall" name="T1R_PRNT_JOBSaveAll" id="T1R_PRNT_JOBSaveAll" title="Save All" accesskey="5">Save</a>
<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="undoall" name="T1R_PRNT_JOBUndoAll" id="T1R_PRNT_JOBUndoAll" title="Undo All" accesskey="6">Undo</a>
</ams_map>
<ams_map title="Copy/Paste" name="Copy/Paste">
<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="11" name="T1R_PRNT_JOBCopyRecord" id="T1R_PRNT_JOBCopyRecord" title="Copy Current Record" accesskey="C">Copy</a>

</ams_map>
</td>

<td><vstext vsds=" " name="T1R_PRNT_JOBSearchColList" vspreservetag="true"></td>

<td><ams_map title="Record Navigation" name="Record Navigation">
<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="first" name="T1R_PRNT_JOBfirst" id="T1R_PRNT_JOBfirst" title="First Record" accesskey="0">First</a>

<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="prev" name="T1R_PRNT_JOBprev" id="T1R_PRNT_JOBprev" title="Previous Record" accesskey="1">Prev</a>

<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="next" name="T1R_PRNT_JOBnext" id="T1R_PRNT_JOBnext" title="Next Record" accesskey="2">Next</a>

<a href="Advantage" vsds="T1R_PRNT_JOB" vsaction="last" name="T1R_PRNT_JOBlast" id="T1R_PRNT_JOBlast" title="Last Record" accesskey="3">Last</a>
</ams_map></td>


</tr>
</table>


2) Find for the second instance of <table class="ADVAction RecSrc"> &
Delete the following code including it.

<table class="ADVAction RecSrc">
<tr>
<td>

<ams_map title="Update Actions" name="Update Actions">
</ams_map>
<ams_map title="Copy/Paste" name="Copy/Paste">
<a href="Advantage" vsds="T2R_PRNT_RSRC" vsaction="11" name="T2R_PRNT_RSRCCopyRecord" id="T2R_PRNT_RSRCCopyRecord" title="Copy Current Record" accesskey="C">Copy</a>

</ams_map>
</td>

<td><vstext vsds=" " name="T2R_PRNT_RSRCSearchColList" vspreservetag="true"></td>

<td><ams_map title="Record Navigation" name="Record Navigation">
<a href="Advantage" vsds="T2R_PRNT_RSRC" vsaction="first" name="T2R_PRNT_RSRCfirst" id="T2R_PRNT_RSRCfirst" title="First Record" accesskey="0">First</a>

<a href="Advantage" vsds="T2R_PRNT_RSRC" vsaction="prev" name="T2R_PRNT_RSRCprev" id="T2R_PRNT_RSRCprev" title="Previous Record" accesskey="1">Prev</a>

<a href="Advantage" vsds="T2R_PRNT_RSRC" vsaction="next" name="T2R_PRNT_RSRCnext" id="T2R_PRNT_RSRCnext" title="Next Record" accesskey="2">Next</a>

<a href="Advantage" vsds="T2R_PRNT_RSRC" vsaction="last" name="T2R_PRNT_RSRClast" id="T2R_PRNT_RSRClast" title="Last Record" accesskey="3">Last</a>
</ams_map></td>


</tr>
</table>


3) Cut the following code

<tr>
<td class="Label"><label for="txtT2PRNT_RSRC_ID" title="Print Resource ID">Print Resource ID :</label></td>
<td class="Val">
<select name="txtT2PRNT_RSRC_ID" id="txtT2PRNT_RSRC_ID" vsds="T2R_PRNT_RSRC" vsdf="PRNT_RSRC_ID" size="1" vscodetable=" " ShowCodeInfo="" title="Print Resource ID">
<option value="JADEStored">JADEDisplayed</option>
</select>
</td>
</tr>

& paste it after

<tr>
<td class="Label"><label for="txtT1PRNT_JOB_CD" title="Print Job Code">Print Job Code :</label></td>
<td class="Val">
<select name="txtT1PRNT_JOB_CD" id="txtT1PRNT_JOB_CD" vsds="T1R_PRNT_JOB" vsdf="PRNT_JOB_CD" size="1" vscodetable=" " ShowCodeInfo="" title="Print Job Code">
<option value="JADEStored">JADEDisplayed</option>
</select>
</td>
</tr>


4) Find for the first instance of
<table id="FormTrans" name="FormTrans" title="Page Navigations" class="ADVTrans Form"> &
copy the following code after its <ams_map title="Page Navigations" name="Page Navigations"> tag.

<!-- BEGIN CUSTOMIZATION -->
<input type="submit" class="ADVButton" vsaction="print" value="OK" name="T1Gen_Batch_Pageprint1" id="T1Gen_Batch_Pageprint1" title="Print">
<input type="submit" class="ADVButton" vsaction="cancel" value="Cancel" name="Cancel" id="Cancel" title="Cancel">
<!-- END CUSTOMIZATION -->


5) Add Javascript

<!-- BEGIN CUSTOMIZATION -->
<script type="text/javascript" language="JavaScript" >
<!--
function NTFY_CheckForNotifications()
{
document.Gen_Batch_Page_Param.flag.value = "1";
document.Gen_Batch_Page_Param.T1Gen_Batch_Pageprint1.click();
}
-->
</script>
<!-- END CUSTOMIZATION -->


6) Find: <form action="Accounts_Receivable" method="post" vsform="true" name="Gen_Batch_Page_Param">
and add the following

<!-- BEGIN CUSTOMIZATION -->
<input type="hidden" name="flag" value="0">
<a style="visibility:hidden" href="Advantage" vsds=" " vsaction=""
name="selectPrintJob" id="selectPrintJob" title="Select Print Job">Back</a>
<!-- END CUSTOMIZATION -->


***********/


//{{FORM_CLASS_DECL
public class Gen_Batch_Page_Param extends Gen_Batch_Page_ParamBase

//END_FORM_CLASS_DECL}}
{
   protected Hashtable moHtParamVal;
   protected String msProcessName;
   private long mlAgentId = 0;
   int miPrintStatus = 0;
   int miOldPrintStatus = 0;
   int miStatusFlag = 0;
   /** Flag to indicate whether move to the previous page or not */
   boolean mboolGoToPrevPage = false;

   /**
    * This map keeps a list of print resource Ids as values
    * where the print job Cd is the key.  The resource Ids
    * are prefixed by a character, '*' in our case, to indicate
    * the the corresponding resource has been set up as a
    * preferred destination for the job
    */
   private HashMap moPrintJobMap = new HashMap(7);

   /*
    * the print job codes are queried and fetched
    * initiallly
    */
   private boolean mboolInitDone = false;

   /*
    *  the Application resource value
    */
   private String msApplRsrcId = null;

   /** Variable hold the previous page reference */
   private VSPage moSource = null;

   
   private static Log moAMSLog = AMSLogger.getLog( Gen_Batch_Page_Param.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;



   // Declarations for instance variables used in the form
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Gen_Batch_Page_Param ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }
//{{EVENT_CODE
//{{EVENT_Gen_Batch_Page_Param_beforeActionPerformed
   void Gen_Batch_Page_Param_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {

      if (ae.getAction().equals("cancel"))
      {
         removeTargetPage(this);
      }
      else if ( ae.getName().equals( "selectPrintJob" ) )
      {
         VSSession       loSession = getParentApp().getSession() ;
         ComboBoxElement loPrntJobComboBox ;
         ComboBoxElement loPrntRsrcComboBox ;

         String lsSelectedValue = preq.getParameter("txtT1PRNT_JOB_CD");

         loPrntRsrcComboBox = (ComboBoxElement)getElementByName( "txtT2PRNT_RSRC_ID" ) ;

         if (lsSelectedValue !=null && lsSelectedValue.trim().length()>0 )
         {
            loPrntJobComboBox = (ComboBoxElement)getElementByName( "txtT1PRNT_JOB_CD" ) ;
            /*
             * Found a selected item from the request.  This corresponds to an
             * item in the print Job combo that we should set as selected since
             * the corresponding comboboxelement is not bound
             */
            int liSelectedIndex = DocPrintDialog.getIndexOfStoredValue(loPrntJobComboBox,
               lsSelectedValue);

            if (liSelectedIndex != -1)
            {
               //set the selected item in print job combo box
               javax.swing.text.html.Option  loSelectedOption =
                  loPrntJobComboBox.getElementAt(liSelectedIndex);
               loPrntJobComboBox.setSelectedItem(loSelectedOption);

               // fill in the print resource based on the selected print job cd
               DocPrintDialog.fillPrntRsrcComboBox( loSession,
                  loPrntRsrcComboBox,
                  msApplRsrcId,
                  lsSelectedValue,
                  loSession.getLogin(),
                  moPrintJobMap);

            } // if (liSelectedIndex != -1)

         }
         else
         {
            loPrntRsrcComboBox.removeAllElements();
         }

      } //if ( ae.getName().equals( "selectPrintJob" ) )


      /**
       * Check if authorized before printing
       */
      else if ( ae.getName().equalsIgnoreCase( "T1Gen_Batch_Pageprint1" ) )
      {

         boolean lboolError = false;
         VSSession loSession = getParentApp().getSession();
         if (! DocPrintDialog.isUserAuthorizedToPrint(loSession,
            this,
               msApplRsrcId,
               preq.getParameter("txtT1PRNT_JOB_CD"),
               preq.getParameter("txtT2PRNT_RSRC_ID"),
               loSession.getLogin() )
               )
         {
            raiseException("Not authorized to print",  SEVERITY_LEVEL_ERROR);
            lboolError = true;
         }
         else
         {
            VSResultSet   loResultSet = null;
            try
            {
               String lsViewForms = preq.getParameter("txtT1VIEW_FORMS");

               /*
                * if the View Forms flag is selected then checked that user
                * specified print resource id is PDF type of not.
                */
               if ( lsViewForms != null && lsViewForms.equals("true"))
               {
                  VSQuery       loQuery = null;
                  VSRow         loRow 	= null;
                  StringBuffer lsbWhereClause = new StringBuffer(128);
                  String lsPrntRsrcID = preq.getParameter("txtT2PRNT_RSRC_ID");

                  lsbWhereClause.append("PRNT_RSRC_ID " ) ;
                  lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsPrntRsrcID, AMSSQLUtil.EQUALS_OPER) ) ;

                  /* Form VSQuery object to get the print resources from R_PRNT_RSRC */
                  loQuery = new VSQuery( loSession, "R_PRNT_RSRC",
                     lsbWhereClause.toString(), null ) ;

                  loResultSet = loQuery.execute() ;
                  loRow = loResultSet.first() ;
                  if ( loRow != null )
                  {
                     int liPrntRsrcTyp = loRow.getData("TYP").getInt();
                     if (liPrntRsrcTyp != 3)
                     {
                        raiseException(R_PRNT_RSRCImpl.INVLD_PRNT_RSRC_ERR_MSG ,
                           SEVERITY_LEVEL_SEVERE);
                        lboolError =  true;
                     }
                  }
               }

               if (!lboolError)
               {
                  submitAndPrint(ae, evt, preq);
               }

            }
            catch (Exception loExp)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExp);

            }
            finally
            {
               if (loResultSet != null)
               {
                  loResultSet.close();
               }
            }
         }

         if(mboolGoToPrevPage)
         {
            mboolGoToPrevPage = false;
            appendOnloadString( "NTFY_SetNoNotifications();" ) ;
            evt.setNewPage(moSource);

            if(moSource.getRootDataSource() != null)
            {
               if(moSource.getRootDataSource().getResultSet() != null)
               {
                  moSource.getRootDataSource().getResultSet().refreshCurrentRow();
               }
            }

            miStatusFlag = 0;
            miPrintStatus = 0;
            miOldPrintStatus = 0;
            mlAgentId = 0;
            evt.setCancel(true);
         }
      } //else if ( ae.getName().equals( "T1Gen_Batch_Pageprint1" ) )
   }


//END_EVENT_Gen_Batch_Page_Param_beforeActionPerformed}}

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
			Gen_Batch_Page_Param_beforeActionPerformed( ae, evt, preq );
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
    * Sets the job parameter map, the job class name and application
    * resource Id for the print job
    */
   public final void setParam(Hashtable fohst,
      String fsProcessName,
      String fsApplRsrcId)
   {
      moHtParamVal = fohst;
      msProcessName = fsProcessName;
      msApplRsrcId = fsApplRsrcId;
   }

   /**
    * Check the Job status and print message accordingly
    */
   private final int checkPrintingStatus(ActionElement ae, PLSRequest preq )
   {

      if(mlAgentId > 0)
      {
         VSResultSet loRS =null;
         SearchRequest losearchReq = new SearchRequest();
         losearchReq.add(" AGNT_ID = " + mlAgentId );
         VSQuery loQuery = new VSQuery(getParentApp().getSession(),"BS_AGENT",losearchReq, null);
         loRS =loQuery.execute();
         VSRow loRow = loRS.first();

         if (loRow != null)
         {
            int liJobstatus, liJobReturnCd;
            liJobstatus = loRow.getData("RUN_STA").getInt();
            liJobReturnCd = loRow.getData("RET_CD").getInt();
            String lsErrorMsg = "";
            switch(liJobstatus)
            {
               case AMSBatchConstants.STATUS_SUBMITTED:

                  if(miStatusFlag < 3)
                  {
                     lsErrorMsg += "Printing process submitted. Please wait.... ";
                  }
                  else
                  {
                     lsErrorMsg += "Printing process submitted.";
                     mboolGoToPrevPage = true;
                  }
                  setStatusMessage(lsErrorMsg);
                  break;
               case AMSBatchConstants.STATUS_RUNNING:
                  lsErrorMsg += "Printing process running. Please wait.... ";
                  miStatusFlag = 0;
                  setStatusMessage(lsErrorMsg);
                  break;
               case AMSBatchConstants.STATUS_COMPLETE:
                  miStatusFlag = 0;

                  if(liJobReturnCd == AMSBatchConstants.RET_CODE_SUCCESSFUL)
                  {
                     lsErrorMsg += "Printing process successful.";
                     setStatusMessage(lsErrorMsg);
                     mboolGoToPrevPage = true;
                  }
                  else if(liJobReturnCd == AMSBatchConstants.RET_CODE_FAILED)
                  {
                     lsErrorMsg += "Printing process failed. ";
                     setStatusMessage(lsErrorMsg);
                     mboolGoToPrevPage = true;
                  }
                  break;
               default :
                  lsErrorMsg += "Please wait.... ";
                  setStatusMessage(lsErrorMsg);
            }//End Of switch
            return liJobReturnCd;
         }
      }
      return -1;
   }



   private void setStatusMessage(String fsMsg)
   {
      try
      {
         raiseException(fsMsg,SEVERITY_LEVEL_ERROR);
      }
      catch(Exception loExp)
      {
        // Add exception log to logger object
        moAMSLog.error("Unexpected error encountered while processing. ", loExp);

      }
   } // of setStatusMessage



   public String generate()
   {
      beforeGenerate() ;
      return super.generate() ;
   }


   /**
    * This method gets the current row,
    * and retrieves doc code and doc component.
    */
   public void beforeGenerate()
   {
      VSSession       loSession = getParentApp().getSession() ;
      ComboBoxElement loPrntJobComboBox ;
      ComboBoxElement loPrntRsrcComboBox ;

      /**
       * Query and populate the print job code
       * combo box once. also remove every thing from
       * print resource combo box
       */
      if (!mboolInitDone )
      {
         /**
          * Initialize the print job combo box
          */
         loPrntJobComboBox = (ComboBoxElement)getElementByName( "txtT1PRNT_JOB_CD" ) ;

         DocPrintDialog.initPrntJobComboBox( loSession,
            loPrntJobComboBox,
            msApplRsrcId,
            loSession.getLogin());
         /**
          * Initialize the print resource combo box - empty
          */
         loPrntRsrcComboBox = (ComboBoxElement)getElementByName( "txtT2PRNT_RSRC_ID" ) ;
         if ( loPrntRsrcComboBox != null )
         {
            loPrntRsrcComboBox.removeAllElements() ;
         } /* if (loComboBox != null) */


         /**
          * Get the selected value from the print job element
          */
         loPrntRsrcComboBox = (ComboBoxElement)getElementByName( "txtT2PRNT_RSRC_ID" ) ;
         javax.swing.text.html.Option  loSelectedOption = loPrntJobComboBox.getSelectedItem();

         /**
          * Return if no print jobs found after 'emptying' the print resource
          * box
          */
         if (loSelectedOption ==null )
         {
            loPrntRsrcComboBox.removeAllElements();
            return;
         }

         String lsValue = loSelectedOption.getValue();

         if (lsValue !=null && lsValue.trim().length()>0 )
         {
            DocPrintDialog.fillPrntRsrcComboBox( loSession,
               loPrntRsrcComboBox,
               msApplRsrcId,
               lsValue,
               loSession.getLogin(),
               moPrintJobMap);
         }
         else
         {
            loPrntRsrcComboBox.removeAllElements();
         }

         moSource = getSourcePage();
         mboolInitDone = true;

      } //if (!mboolInitDone )



   } // before generate

   /**
    * This method will wait for 10 sec, then check the job status and print the
    * message on Page using raiseException. method will increment miStatusFlag if old
    * status of job and new status are same.if misStatusFlag is more thatn 3 then
    * checkPrintingStatus will return back to the parent page instead of polling
    * repeatedly. NTFY_CheckForNotifications() will be added to htm of page if refresh
    * is needed
    * @boolean fboolViewForms       True if View Forms are selected
    * @ActionElement ae             Action Element
    * @PLSRequest preq              PLS Request
    */
   private void pollStatus(boolean fboolViewForms, ActionElement ae, PLSRequest preq)
   {
      /*
       * if the View Forms flag is not selected then by the by pass to check
       * the status of  scheduled job & displaying messages on the screen
       * and move the previous page immediately
       */
      if (!fboolViewForms)
      {
         if((miPrintStatus != -1) && (miPrintStatus != 12) && (miPrintStatus != 1))
         {
            try
            {
               Thread.sleep(10000);
            }
            catch(InterruptedException ie)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", ie);

            }/* End Try*/
            miOldPrintStatus = miPrintStatus;
            miPrintStatus = checkPrintingStatus(ae, preq);
            if(miOldPrintStatus == miPrintStatus)
            {
               miStatusFlag++;
            }
            else
            {
               miStatusFlag = 0;
            }/* End if(miOldPrintStatus == miPrintStatus)*/
            if(miStatusFlag <= 4)
            {
               appendOnloadString( "NTFY_CheckForNotifications();" ) ;
            }
            else
            {
               miPrintStatus = checkPrintingStatus(ae, preq);
               mboolGoToPrevPage = true;
            }/* End if(miStatusFlag <= 4) */

         }
         else
         {
            miPrintStatus = checkPrintingStatus(ae, preq);
            mboolGoToPrevPage = true;
         } /* End if((miPrintStatus != -1) && (miPrintStatus != 12). . .*/
      }
      else
      {
         mboolGoToPrevPage = true;
      } /* End if (!fboolViewForms)*/

   }

   /**
    * This method will submit a new job if Flag on HTM page is 0.
    * If page is refreshed automatically (i.e Flag = 1) then the method will call
    * pollStatus method.
    * @ActionElement ae       Action Element
    * @PageEvent evt          Page Event
    * @PLSRequest preq        PLS Request
    */
   private void submitAndPrint(ActionElement ae, PageEvent evt, PLSRequest preq)
   {
        String flag = preq.getParameter("flag");
        String lsViewForms = preq.getParameter("txtT1VIEW_FORMS");

        boolean lboolViewForms;

         /* Flag indicate whether user want to see Form PDF file to view */
         lboolViewForms =
            AMSStringUtil.strEqual(lsViewForms,"true") ? true : false;


         if (AMSStringUtil.strEqual(flag,"0"))
         {
            long llJobId;
            String lsParamName;
            String lsParamVal;
            String lstrPrintJobCode;
            String lstrPrintResID;
            String lsPackage;
            String lsUser;

            Enumeration loe;

            // Get the current package name
            lsPackage = getParentApp().getPackageName();
            lsPackage = lsPackage.substring( 0, lsPackage.indexOf( "."  ) );

            // Get the current ID of the user logged
            lsUser = getParentApp().getSession().getLogin();

            llJobId = AMSPLSUtil.addNewJob(msProcessName, lsPackage, lsUser, this);

            mlAgentId = llJobId;

            loe = moHtParamVal.keys();

            while(loe.hasMoreElements())
            {
               lsParamName = (String)loe.nextElement();
               lsParamVal = (String)moHtParamVal.get(lsParamName);
               AMSPLSUtil.addNewParameter(llJobId,lsParamName,lsParamVal,this);
            }

            lstrPrintJobCode = preq.getParameter("txtT1PRNT_JOB_CD");
            lstrPrintResID = preq.getParameter("txtT2PRNT_RSRC_ID");

            /*
             * setParam would have supplied the application resource
             */
            AMSPLSUtil.addNewParameter(llJobId,"APPL_RSRC_ID",msApplRsrcId,this);
            AMSPLSUtil.addNewParameter(llJobId,"PRINT_JOB_NAME",lstrPrintJobCode,this);
            AMSPLSUtil.addNewParameter(llJobId,"PRINT_RSRC",lstrPrintResID,this);

            /*
             * Adding 'VIEW_FORMS' parameter. If user selected view form
             * as checked (ticked) then set its value as 'Y' else set as 'N'.
             */
            if (lboolViewForms)
            {
               AMSPLSUtil.addNewParameter(llJobId, "VIEW_FORMS", "Y", this);
            }
            else
            {
               AMSPLSUtil.addNewParameter(llJobId, "VIEW_FORMS", "N", this);
            }/* End if if (lboolViewForms) */


            /* Set the FORMS_DSCR field value */
            String lsFormsDscr = preq.getParameter("txtT1FORMS_DSCR");
            if ( lsFormsDscr != null && lsFormsDscr.length() > 0 )
            {
               AMSPLSUtil.addNewParameter(llJobId, "FORMS_DSCR", lsFormsDscr, this);
            }
            else
            {
               /*
                * If user prints the 'Print Deposit Ticket' then set
                * bank account code as prefix with the form description.
                */
               if (AMSStringUtil.strEqual(msProcessName,"PrintDepositTicket"))
               {
                  lsFormsDscr = (String)moHtParamVal.get("BANK_ACCT_CD");
                  lsFormsDscr = "Bank: " + lsFormsDscr ;
                  AMSPLSUtil.addNewParameter(llJobId, "FORMS_DSCR", lsFormsDscr, this);
               } /* End if (AMSStringUtil.strEqual(msProcessName,"PrintDepositTicket")) */

            } /* End if ( lsFormsDscr != null && lsFormsDscr.length() > 0 )*/

            AMSPLSUtil.setRunStatus(llJobId,6,this);
            /*
             * if the View Forms flag is not selected then by the by pass to check
             * the status of  scheduled job & displaying messages on the screen
             */
            if(!lboolViewForms)
            {
               miPrintStatus = checkPrintingStatus(ae, preq);
               appendOnloadString( "NTFY_CheckForNotifications();" ) ;
            }
            else
            {
               evt.setNewPage(moSource);
               miStatusFlag = 0;
               miPrintStatus = 0;
               mlAgentId = 0;
               evt.setCancel(true);
            }

         }
         else
         {
            pollStatus(lboolViewForms, ae, preq);

         } /* End if (AMSStringUtil.strEqual(flag,"0")) */

   } // End submitAndPrint


}
