//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import java.util.*;
import advantage.AMSStringUtil;
import advantage.CVL_DOC_PHASE_CDImpl;
import advantage.IN_APP_CTRLImpl;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;

import org.apache.commons.logging.Log;


/*
 **  DocPrintDialog
 */

//{{FORM_CLASS_DECL
public class DocPrintDialog extends DocPrintDialogBase

//END_FORM_CLASS_DECL}}
{
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

   /**
    *  Member variable to hold the document code for the current instance.
    */
   private static String msDocCd = null;

   /**
    *  Member variable to hold the text content element for custom print parameters.
    */
   private TextContentElement   moCustActnTCE       = null ;

   /**
    * Constant to define the name of the text area used to display custom
    * print actions.
    */
   private static final String PRNT_ACTNS_DISPLAY_AREA = "PrintActions";

   /**
    * These delimiter characters are required for storinf and reparsing
    * print resource Id list
    */
   public static final String UNPRNTBL_DELIM_1 = "\001";
   public static final String UNPRNTBL_DELIM_2 = "\002";

   /**
    * The preferred printer identification prefix
    */
   public static final char PREFERRED_PRNTR_IDEN = '*';

   /**
    *  Member variable to hold associated value for print caption hide inactive
    *  procurement lines in the print action cvl.
    */
   public static final int HIDE_INACTV_SV = 1;

   /**
    * String constant to hold the displayed value column name for print action cvl.
    */
   public static final String PRN_ACTN_DV_COL = "CVL_PRNT_ACTN_DV";

   /**
    * String constant to hold the stored value column name for the print action cvl.
    */
   public static final String PRN_ACTN_SV_COL = "CVL_PRNT_ACTN_SV";


   private static final String PRINTER_TYPE   = "1";
   private static final String EMAIL_TYPE     = "2";
   private static final String FAX_TYPE       = "4";
   private static final String PDF_TYPE       = "3";
   private String msFieldNameForHideInactiveProcurementLines = "HIDE_INACTV";
   
   private static Log moAMSLog = AMSLogger.getLog( DocPrintDialog.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;


   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public DocPrintDialog ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_DocPrintDialog_beforeActionPerformed
   void DocPrintDialog_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
   {
      /**
       * If the source page is a document, make sure that the tabs are re-gened
       */
      if ( ae.getName().equals( "CancelPrint" ) )
      {
         VSPage loSrcPage = getSourcePage() ;

         if ( ( loSrcPage != null ) && ( loSrcPage instanceof AMSDocTabbedPage ) )
         {
            ((AMSDocTabbedPage)loSrcPage).setGenTabPage( true ) ;
         }
      }
      else if(ae.getName().equals("selectPrintOutputType"))
      {
         //Clear the values.
         retainPrintValues(false, null);

         VSSession loSession = getParentApp().getSession() ;
         String lsValue      = preq.getParameter("printOutputType");

         ComboBoxElement loPrntOutputComboBox = (ComboBoxElement)getElementByName( "printOutputType" ) ;
         int liSelectedIndex                  = getIndexOfStoredValue(loPrntOutputComboBox, lsValue);

         if (liSelectedIndex != -1)
         {
            //Set the Selected Print Output Type back, since page is loaded again.
            javax.swing.text.html.Option  loSelectedOption = loPrntOutputComboBox.getElementAt(liSelectedIndex);
            loPrntOutputComboBox.setSelectedItem(loSelectedOption);
         }

         //Set the corresponding Print Job Values
         initPrntJobComboBoxValues();

         //Set the corresponding Print Resource Types.
         ComboBoxElement loPrntJobComboBox           = (ComboBoxElement)getElementByName( "printJobCombo" ) ;
         ComboBoxElement loPrntRsrcComboBox          = (ComboBoxElement)getElementByName( "printRsrcCombo" ) ;
         AMSPage loPage                              = (AMSPage)loPrntJobComboBox.getPage();
         javax.swing.text.html.Option  loSelectedJob = loPrntJobComboBox.getSelectedItem();
         if(loSelectedJob == null)
         {
            loPage.raiseException( "No Print Jobs exist for the selected Print Output Type",
                  SEVERITY_LEVEL_ERROR ) ;
            loPrntJobComboBox.removeAllElements();
            loPrntRsrcComboBox.removeAllElements();
            setFields(getParentApp().getSession());
         }
         else
         {
            String lsPrntJobId = loSelectedJob.getValue();
            //Fill in Print Resource combobox with values
            fillPrntRsrcComboBox( loSession , loPrntRsrcComboBox ,
                  msApplRsrcId , lsPrntJobId ,
                  loSession.getLogin() , moPrintJobMap );

            //Show or hide fields that are specific to each Print Output Type.
            setFields(getParentApp().getSession());

            //Check and enable the Fax Notification Address
            DivElement divElementFax     = (DivElement) getElementByName("fax");
            if(divElementFax.isEnabled())
            {
               setFaxNotifyFields();
            }
         }
      }
      else if ( ae.getName().equals( "selectPrintJob" ) )
      {
         VSSession       loSession = getParentApp().getSession() ;
         ComboBoxElement loPrntJobComboBox ;
         ComboBoxElement loPrntRsrcComboBox ;

         //Retain previous values.
         retainPrintValues(true, preq);

         //String lsValue = loSelectedOption.getValue();
         String lsValue = preq.getParameter("printJobCombo");

         loPrntRsrcComboBox = (ComboBoxElement)getElementByName( "printRsrcCombo" ) ;

         if (lsValue !=null && lsValue.trim().length()>0 )
         {
            loPrntJobComboBox = (ComboBoxElement)getElementByName( "printJobCombo" ) ;
            /*
             * Found a selected item from the request.  This corresponds to an
             * item in the print Job combo that we should set as selected since
             * the corresponding comboboxelement is not bound
             */
            int liSelectedIndex = getIndexOfStoredValue(loPrntJobComboBox, lsValue);

            if (liSelectedIndex != -1)
            {
               //set the selected item
               javax.swing.text.html.Option  loSelectedOption =
                  loPrntJobComboBox.getElementAt(liSelectedIndex);
               loPrntJobComboBox.setSelectedItem(loSelectedOption);

               // fill in the print resource based on the select print job cd
               fillPrntRsrcComboBox( loSession,
                  loPrntRsrcComboBox,
                  msApplRsrcId,
                  lsValue,
                  loSession.getLogin(),
                  moPrintJobMap);

               //Show or hide fields those are specific to each Print Output Type.
               setFields(getParentApp().getSession());

               //Check and enable the Fax Notification address
               DivElement divElementFax     = (DivElement) getElementByName("fax");
               if(divElementFax.isEnabled())
               {
                  setFaxNotifyFields();
               }
            } // if (liSelectedIndex != -1)

         }
         else
         {
            loPrntRsrcComboBox.removeAllElements();
         }

      } //if ( ae.getName().equals( "selectPrintJob" ) )


      /**
       * Check if authorized
       */
      else if ( ae.getName().equals( "PrintDoc" ) )
      {

         //Retain previous values.
         retainPrintValues(true, preq);

         // Get the View Forms parameter value and pass it to Session
         String lsViewForm = preq.getParameter("isViewForms");
         if ( lsViewForm != null && lsViewForm.equals("true"))
         {
            // Get the Forms Description parameter value and pass it to Session
            String lsFormsDscr = preq.getParameter("formsDscr");
            VSORBSession loORBSession = getParentApp().getSession().getORBSession() ;

            /*
             * Set the session properties for VLS to identify whether it allows
             * Online Viewing of Forms.
             */
            try
            {
               loORBSession.setProperty("VIEW_FORM", "TRUE") ;
               loORBSession.setProperty("FORM_DSCR", lsFormsDscr) ;
            }
            catch (Exception loExcp)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

            } /* End Try*/
         } /* End if ( lsViewForm != null && lsViewForm.equals("true")) */
         VSSession loSession = getParentApp().getSession();

         if (! isUserAuthorizedToPrint(loSession,
            this,
               msApplRsrcId,
               preq.getParameter("printJobCombo"),
               preq.getParameter("printRsrcCombo"),
               loSession.getLogin()
               ))

         {
            raiseException("Not authorized to print",  SEVERITY_LEVEL_ERROR);
         }

      } //if ( ae.getName().equals( "PrintDoc" ) )
      else if ( ae.getName().equals( "selectprintRsrc" ) )
      {

         //Retain previous values.
         retainPrintValues(true, preq);
         String lsValue                    = preq.getParameter("printRsrcCombo");
         ComboBoxElement loPrntResComboBox = (ComboBoxElement)getElementByName( "printRsrcCombo" ) ;
         int liSelectedIndex               = getIndexOfStoredValue(loPrntResComboBox, lsValue);

         if (liSelectedIndex != -1)
         {
            //Set the selected Print Resource back, since Page is loaded again.
            javax.swing.text.html.Option  loSelectedOption = loPrntResComboBox.getElementAt(liSelectedIndex);
            loPrntResComboBox.setSelectedItem(loSelectedOption);
         }
         //Check and enable the Fax Notification address
         DivElement divElementFax     = (DivElement) getElementByName("fax");
         if(divElementFax.isEnabled())
         {
            setFaxNotifyFields();
         }
      }
   }


//END_EVENT_DocPrintDialog_beforeActionPerformed}}
//{{EVENT_DocPrintDialog_afterActionPerformed
   void DocPrintDialog_afterActionPerformed( ActionElement ae, PLSRequest preq )
   {
      //Write Event Code below this line
      resetSessionProp();

      /*
       * After Print is done, refresh the data on Source Page. The assumption here is that
       * Source page contains an instance of Document. During Print action some of the Document
       * fields would have been changed on Document Header and its components and hence it is
       * necessary to retrieve the latest data on the Source page.
       */
      if ( ae.getName().equals( "PrintDoc" ) )
      {
         refreshSrcPage();
      }
   }

//END_EVENT_DocPrintDialog_afterActionPerformed}}

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
			DocPrintDialog_afterActionPerformed( ae, preq );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocPrintDialog_beforeActionPerformed( ae, evt, preq );
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
         initPrintOutputType();
         initPrntJobComboBoxValues();

         /**
          * Initialize the print resource combo box - empty
          */
         loPrntRsrcComboBox = (ComboBoxElement)getElementByName( "printRsrcCombo" ) ;
         if ( loPrntRsrcComboBox != null )
         {
            loPrntRsrcComboBox.removeAllElements() ;
         } /* if (loComboBox != null) */


         /**
          * Get the selected value from the print job element
          */
         loPrntJobComboBox = (ComboBoxElement)getElementByName( "printJobCombo" ) ;
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
            fillPrntRsrcComboBox( loSession,
               loPrntRsrcComboBox,
               msApplRsrcId,
               lsValue,
               loSession.getLogin(),
               moPrintJobMap);

            //Show or hide fields those are specific to each Print Output Type.
            setFields(getParentApp().getSession());

            //Check and enable the Fax Notification Address
            DivElement divElementFax     = (DivElement) getElementByName("fax");
            if(divElementFax.isEnabled())
            {
               setFaxNotifyFields();
            }
         }
         else
         {
            loPrntRsrcComboBox.removeAllElements();
         }


         mboolInitDone = true;

      } //if (!mboolInitDone )
      moCustActnTCE =
            (TextContentElement)getElementByName( PRNT_ACTNS_DISPLAY_AREA );
      generateCustomPrintActions();

   } // before generate



   /**
    * This method fills the print job combo box.
    */
   private void initPrntJobComboBoxValues()
   {
      VSRow       loRootRow ;
      DataSource  loRootDS ;
      VSSession   loSession = getParentApp().getSession() ;
      ComboBoxElement loComboBox ;

      try
      {
         /**
          * Initialize the print job combo box
          */
         loComboBox = (ComboBoxElement)getElementByName( "printJobCombo" ) ;
         if ( loComboBox != null )
         {
            loComboBox.removeAllElements() ;

         } /* if (loComboBox != null) */


         /**
          * Get the reference to the data source and the current row
          * selected by the user
          *
          * also initailize the member variable to store the application
          * resource - msApplRsrcId
          */
         loRootDS   = getRootDataSource() ;
         loRootRow  = loRootDS.getCurrentRow() ;
         msApplRsrcId    = loRootRow.getData( ATTR_DOC_CD ).getString() ;

         ComboBoxElement loPrntOutputComboBox = (ComboBoxElement)getElementByName( "printOutputType" ) ;
         javax.swing.text.html.Option  loSelectedOption = loPrntOutputComboBox.getSelectedItem();
         String lsValue = loSelectedOption.getValue();
         initPrntJobComboBox( loSession,
            loComboBox,
            msApplRsrcId,
               loSession.getLogin(),loRootRow.getData( ATTR_DOC_PHASE_CD ).getString(),lsValue);
      }
      catch ( Exception loEx )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loEx);

         raiseException( "error creating list box information",
            SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end try - catch */
   }

   /**
    * Given a row with document id fields, this method returns
    * the generated Print Dialog
    *
    * @param foDocPrintRow The row for the document to print
    * @param foActnElem The action element that started the copy
    * @param foRequest The PLS request
    * @return The generated print page
    */
   public static String buildPrintDialog( VSRow foDocPrintRow,
      AMSHyperlinkActionElement foActnElem, PLSRequest foRequest )
   {
      VSPage               loSrcPage   = foActnElem.getPage() ;
      String               lsDestination = "DocPrintDialog" ;
      String               lsFrameName   = "Detail" ;
      String[]             lsMsgParams = new String[6] ;
      StringBuffer         lsbWhere    = new StringBuffer( 100 ) ;
      AMSDynamicTransition loDynTran ;

      if ( foDocPrintRow == null )
      {
         ( (AMSPage) loSrcPage).raiseException(
            "A row has to be selected to perform this action",
            AMSPage.SEVERITY_LEVEL_ERROR ) ;
         return ( (AMSPage) loSrcPage).generate() ;
      } /* end if ( foDocPrintRow == null ) */

      /* Set the document identifier */
      foActnElem.setDocumentIdentifier( lsMsgParams, foDocPrintRow ) ;
      msDocCd = lsMsgParams[2];
      /* Generate the where clause */
      lsbWhere.append( "DOC_CD=" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[2],true ) ) ;
      lsbWhere.append( " AND DOC_DEPT_CD=" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[3],true ) ) ;
      lsbWhere.append( " AND DOC_ID=" ) ;
      lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMsgParams[4],true ) ) ;
      lsbWhere.append( " AND DOC_VERS_NO=" ) ;
      lsbWhere.append( lsMsgParams[5] ) ;

      /* Create an instance of the dynamic transition */
      loDynTran = new AMSDynamicTransition( lsDestination,
         lsbWhere.toString(), "Advantage" ) ;

      /* Set the frame name */
      loDynTran.setFrameName( lsFrameName ) ;

      /* Set the reference to the source page */
      loDynTran.setSourcePage( loSrcPage ) ;

      /*
       * Return the HTML string after calling the generate method on the
       * transition page
       */
      return loDynTran.generateHTMLPage( loSrcPage.getParentApp(),
         loSrcPage.getSessionId(),
         foRequest ) ;
   } /* end buildPrintDialog() */

   /**
    * Method fills the print job choice box with value(s) that match the application
    * resource
    *
    * @param foSession
    * @param foComboBox
    * @param fsApplRscId the application resource Id for which the corresponding print jobs
    * are queried
    * @param fsUserId the user Id for which the print respource is to be mapped
    */
   protected final static void initPrntJobComboBox( VSSession foSession,
      ComboBoxElement foComboBox,
      String fsApplRscId,
      String fsUserId)
   {
      initPrntJobComboBox(foSession,foComboBox,fsApplRscId,fsUserId,null,null);

   } //initPrntJobComboBox

   /**
    * This is overloaded method with additional parameter document phase code
    * This Method fills the print job choice box with value(s) that match the
    * application resource
    *
    * @param foSession
    * @param foComboBox
    * @param fsApplRscId the application resource Id for which the corresponding print jobs
    * are queried
    * @param fsUserId the user Id for which the print respource is to be mapped
    * @param fsDocPhaseCd Document phase code
    * @param fsResourceTyp The Print Resource Type based on which the Print Job Combox
    *        is to be populated.
    */
   protected final static void initPrntJobComboBox( VSSession foSession,
         ComboBoxElement foComboBox,
         String fsApplRscId,
         String fsUserId,
         String fsDocPhaseCd,String fsResourceTyp)
   {

      VSQuery       loQuery = null;
      VSResultSet   loResultSet = null;
      VSRow         loRow = null;
      StringBuffer  lsbWhereClause ;
      String        lsJobCD = null;
      String        lsJobNM = null;

      VSQuery       loPrintQuery = null;
      StringBuffer  lsbPrintWhereClause ;
      VSResultSet   loPrintResultSet = null;
      VSRow         loPrintRow = null;

      AMSPage loPage = (AMSPage)foComboBox.getPage();

      try
      {

         lsbWhereClause = new StringBuffer(128);

         lsbWhereClause.append("APPL_RSRC_ID = ") ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsApplRscId, true) );
         lsbWhereClause.append(" AND (USER_ID='*ALL' OR USER_ID = " ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsUserId, true) ) ;
         lsbWhereClause.append(") ");
         if(! AMSStringUtil.strIsEmpty(fsResourceTyp))
         {
            //Set the Print Jobs based on the Resource type
            String lsDbSchema = getSchema(loPage , "R_PRNT_RSRC");
            lsbWhereClause.append(" AND PRNT_RSRC_ID IN");
            lsbWhereClause.append(" ( SELECT PRNT_RSRC_ID FROM ").append(lsDbSchema);
            lsbWhereClause.append(".R_PRNT_RSRC ");
            lsbWhereClause.append("  WHERE TYP = ");
            lsbWhereClause.append( fsResourceTyp ) ;
            lsbWhereClause.append(" )");
         }

         /* Form VSQuery object to get the print resources from R_PRNT_JOB */
         loQuery = new VSQuery( foSession,
            "R_PRNT_ACCS",
            lsbWhereClause.toString(),
            "PRNT_JOB_CD ASC" ) ;

         loResultSet = loQuery.execute() ;
         loRow = loResultSet.first() ;

         // empty all combobox elemnts
         if ( foComboBox !=null)
         {
            // remove all elements first
            foComboBox.removeAllElements();
         }

         /*
          * We will only populate distinct entries for the print Job code from the
          * result set.  Using a map for this purpose
          */

         HashMap loPrnJobMap = new HashMap(7);
         String lsDocPhases = null;

         while ( loRow != null )
         {
            lsJobCD  = loRow.getData( "PRNT_JOB_CD" ).getString() ;
            lsJobNM  = loRow.getData( "PRNT_JOB_NM" ).getString() ;

            if(!AMSStringUtil.strIsEmpty(fsDocPhaseCd))
            {
               lsbPrintWhereClause = new StringBuffer(128);
               lsbPrintWhereClause.append("APPL_RSRC_ID = ") ;
               lsbPrintWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsApplRscId, true) );
               lsbPrintWhereClause.append(" AND PRNT_JOB_CD = ");
               lsbPrintWhereClause.append( AMSSQLUtil.getANSIQuotedStr(lsJobCD, true) );

               loPrintQuery = new VSQuery( foSession,
                 "R_PRNT_JOB",
                 lsbPrintWhereClause.toString(),
                 null);

               loPrintResultSet = loPrintQuery.execute() ;
               loPrintRow = loPrintResultSet.first() ;

               if (loPrintRow != null)
               {
                  lsDocPhases = loPrintRow.getData("DOC_PHASES").getString();

                  if(!AMSStringUtil.strIsEmpty(lsDocPhases))
                  {
                     if (lsDocPhases.indexOf(Integer.toString(CVL_DOC_PHASE_CDImpl.ALL_DOC_PHASES)) != -1 ||
                         lsDocPhases.indexOf(fsDocPhaseCd) != -1)
                     {
                        if ( loPrnJobMap.get(lsJobCD) ==null)
                        {
                           //this entry doesnt exist - cache and populate
                           loPrnJobMap.put(lsJobCD, lsJobNM );
                           foComboBox.addElement( lsJobNM, lsJobCD ) ;
                        }
                     }
                  }
               }
          }
          else
          {
             if ( loPrnJobMap.get(lsJobCD) ==null)
             {
               //this entry doesnt exist - cache and populate
               loPrnJobMap.put(lsJobCD, lsJobNM );
               foComboBox.addElement( lsJobNM, lsJobCD ) ;
             }
          }
            loRow = loResultSet.next() ;
         } /* end while( loRow != null ) */

         if( foComboBox.getSize()==0 )
         {
            /*
             * If no print jobs exist, then do not show the print
             * dialog page.  Instead, just display an error to the user.
             */
            loPage.raiseException( "No print jobs exist", SEVERITY_LEVEL_ERROR ) ;
         }

         if (loResultSet != null)
         {
            loResultSet.close();
         }

         if (loPrintResultSet != null)
         {
            loPrintResultSet.close();
         }

      }
      catch ( Exception loEx )
      {
         if (loResultSet != null)
         {
            loResultSet.close();
         }

         if (loPrintResultSet != null)
         {
            loPrintResultSet.close();
         }


           // Add exception log to logger object
           moAMSLog.error("Unexpected error encountered while processing. ", loEx);

         loPage.raiseException( "error creating list box information",
            SEVERITY_LEVEL_ERROR ) ;
         return ;
      } /* end try - catch */
      finally
      {
         if (loResultSet != null)
         {
            loResultSet.close();
         }

         if (loPrintResultSet != null)
         {
            loPrintResultSet.close();
         }
      } /* final */

   } //initPrntJobComboBox


   /**
    * Method fills the print resource choice box with value(s) that match the print Job
    * code and the application resource
    *
    * @param foSession the update session
    * @param foComboBox the combo box to be filled in
    * @param fsApplRscId the application resource Id for which the corresponding print jobs
    * are queried
    * @param fsPrintJobCd the print Job Code
    * @param fsUserId the user Id for which the print respource is to be mapped
    * @param foPrntJobMap the map that saves print resource Id list for different print job
    * codes
    */
   protected static final void fillPrntRsrcComboBox(VSSession foSession,
      ComboBoxElement foComboBox,
      String fsApplRscId,
      String fsPrintJobCd,
      String fsUserId,
      HashMap foMap)
   {
      if (fsPrintJobCd ==null || fsPrintJobCd.trim().length() == 0 ||
         fsUserId == null    || fsUserId.trim().length() == 0 ||
            fsApplRscId ==null  || fsApplRscId.trim().length() ==0 )
      {
         return ;
      }

      if ( foComboBox !=null)
      {
         // remove all elements first
         foComboBox.removeAllElements();
      }

      /*
       * The map, foMap, contains keys that are print job codes while its values
       * are print resource Ids for that print Jobs cd (and the application resource)
       * If the print job code's corresponding list has already been queried for and
       * added to this instance, then that value will be used to populate the print resource
       * combobox
       *
       * A preferred flag is also associated with printResource and the value has the
       * following form - [preferred Id iden ]+<print resource identified><print resource delim>
       * Thus an individual print resource value may or may not be prefixed by a preferred resource
       * Id identifer
       */

      if (foMap !=null  )
      {
         Object loVal = foMap.get( fsPrintJobCd );

         if (loVal !=null)
         {
            // This value was retrieved last time and we need not query for
            // resource list
            populateComboBoxFromRsrcLst(foComboBox, (String) loVal);
            return;
         }
      }

      AMSPage loPage = (AMSPage)foComboBox.getPage();

      /*
       * At this stage the print resource list for the print job (and the app resouce
       * and the associated user ) has to be retrived and stored in the map passed (if
       * not null)
       */
      VSQuery       loQuery =null;
      VSResultSet   loResultSet =null;
      VSRow         loRow =null;
      StringBuffer  lsbWhereClause ;
      String        lsPrintRsrcId = null;
      String        lsPrintRsrcNm = null;
      boolean       lboolPrefPrintRsrc = false;
      String        lsOrderBy = null;

      try
      {
         //order by is based on database type
         if (foSession.isOracle() )
         {
            lsOrderBy = "PREF DESC,DECODE(USER_ID,'*ALL','ZZZZ')";
         }
         else // DB2 or SQL Server
         {
            lsOrderBy = "PREF DESC, CASE WHEN USER_ID='*ALL' THEN 'ZZZZ' ELSE USER_ID END";
         }


         lsbWhereClause = new StringBuffer(128);

         lsbWhereClause.append( "APPL_RSRC_ID = " ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsApplRscId,true) ) ;
         lsbWhereClause.append( " AND (USER_ID='*ALL' OR USER_ID=" ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsUserId,true) ) ;
         lsbWhereClause.append( ")");
         lsbWhereClause.append( " AND PRNT_JOB_CD=" ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsPrintJobCd,true) );


         /* Form VSQuery object to get the print resources from R_PRNT_JOB */
         loQuery = new VSQuery( foSession,
            "R_PRNT_ACCS",
            lsbWhereClause.toString(),
            lsOrderBy) ;

         loResultSet = loQuery.execute() ;
         loRow = loResultSet.first() ;

         if ( loRow == null )
         {
            /*
             * If no print jobs exist, then do not show the print
             * dialog page.  Instead, just display an error to the user.
             */
            loPage.raiseException(
               "No print resources defined  for the print job exist",
               SEVERITY_LEVEL_ERROR ) ;
         }

         /*
          * form the print resource list
          */
         StringBuffer lsbPrintRsrcLst = new StringBuffer(128);
         boolean lboolFirst = true;
         StringBuffer lsbPrintRsrcNameStr= null;

         while ( loRow != null )
         {
            if (! lboolFirst )
            {
               // delimter for resource Id - name pairs
               lsbPrintRsrcLst.append(UNPRNTBL_DELIM_2);
            }


            lsPrintRsrcId  = loRow.getData( "PRNT_RSRC_ID" ).getString() ;
            lsPrintRsrcNm  = loRow.getData( "PRNT_RSRC_NM" ).getString() ;
            lboolPrefPrintRsrc = loRow.getData( "PREF" ).getBoolean();

            // if preferred printer - prefix name with the preferred identifier
            if (lboolPrefPrintRsrc)
            {
               lsbPrintRsrcNameStr = new StringBuffer(32);
               lsbPrintRsrcNameStr.append(PREFERRED_PRNTR_IDEN);
               lsbPrintRsrcNameStr.append(lsPrintRsrcNm);
            }
            else
            {
               lsbPrintRsrcNameStr = new StringBuffer(32);
               lsbPrintRsrcNameStr.append(lsPrintRsrcNm);
            }
            foComboBox.addElement( lsbPrintRsrcNameStr.toString(),
               lsPrintRsrcId ) ;

            /*
             * append the print resourec Id and value to list
             * separated by delim 1
             */
            lsbPrintRsrcLst.append(lsPrintRsrcId);
            lsbPrintRsrcLst.append(UNPRNTBL_DELIM_1);
            lsbPrintRsrcLst.append(lsbPrintRsrcNameStr.toString());

            lboolFirst= false; // first row already processed
            loRow = loResultSet.next() ;
         } /* end while( loRow != null ) */

         if (foMap !=null)
         {
            //put the resource list for print job cd
            foMap.put(fsPrintJobCd,lsbPrintRsrcLst.toString() );
         }

      }//try
      catch ( Exception loEx )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loEx);

         loPage.raiseException( "error creating list box information",
            SEVERITY_LEVEL_ERROR ) ;
         return ;
      } //catch
      finally
      {
         if (loResultSet!=null)
         {
            loResultSet.close();
         }
      } //finally

   } //fillPrntRsrcComboBox

   /**
    * This method parses the print resource list and populates the combo box element
    * The print resource list has the values as
    * <print resource identified><print resource delim2>[preferred Identifier ]+<print resource
    * name><print resource delim3>
    *
    * where  the preferred print resource identifier is the unprintable char UNPRNTBL_DELIM_1
    * and the print resource list delimiter is UNPRNTBL_DELIM_2
    */
   private static final void populateComboBoxFromRsrcLst(ComboBoxElement foComboBox,
      String          fsValLst)
   {
      if (foComboBox==null || fsValLst ==null || fsValLst.trim().length() ==0)
      {
         return ; // nothing to populated
      }

      /*
       * This will give us tokens that contain only the print resource identifier
       * followed by delim1 and then the print resource names.  The print resource
       * name may have the prefereed identifyig character '*' prefixed
       */
      StringTokenizer loTokenizer = new StringTokenizer(fsValLst, UNPRNTBL_DELIM_2);
      String lsToken = null;
      int liIndex = -1;
      StringBuffer lsbBuffer  = null;

      // go through the list tokens
      while (loTokenizer.hasMoreElements() )
      {
         lsToken = loTokenizer.nextToken();

         liIndex = lsToken.indexOf(UNPRNTBL_DELIM_1);

         if (liIndex !=-1 )
         {
            // found print Id and name
            foComboBox.addElement(lsToken.substring(liIndex + 1), lsToken.substring(0,liIndex) );
         }
         else
         {
            // the Id is entered as the display name too
            foComboBox.addElement(lsToken, lsToken );
         }
      } // of while

   } // of populateComboBoxFromRsrcLst(ComboBoxElement, String)


   /**
    * Returns the index of an item for a given stored value. If the item is not
    * found in the list, it returns -1. The index might be required for adding or removing an
    * item at a given position in the list.
    *
    * @param foComboBox the combobox element for which the search done
    * @param fsStoredValue the stored value searched for
    * @return the
    */
   public static final int getIndexOfStoredValue(ComboBoxElement foComboBox,
      String fsStoredVal)
   {
      try
      {
         int liSize = foComboBox.getSize();

         for (int liIndex = 0 ; liIndex < liSize; liIndex++)
         {
            Option loOption = (Option)foComboBox.getElementAt(liIndex);
            String lsValue = loOption.getValue();

            if (lsValue != null && lsValue.equals(fsStoredVal))
            {
               return liIndex;
            }

         }//for
         return -1;
      } //try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);

        	 return -1;
      }

   } // getIndexOfStoredValue


   /**
    * Verifies if the combination of application resource, print Job,
    * print resource and the user Id is valid
    *
    * @param foSession the update session
    * @param foPage the page instance associated with the request
    * @param fsApplRscId the application resource Id
    * @param fsPrintJobCd the print Job Code
    * @param fsPrintRsrcId the print resource Id
    * @param fsUserId the user Id for which the print respource is to be mapped
    * @return if the values supplied are valid (authorized to print)
    */
   protected static final boolean isUserAuthorizedToPrint(VSSession foSession,
      AMSPage   foPage,
      String fsApplRscId,
      String fsPrintJobCd,
      String fsPrintRsrcId,
      String fsUserId)
   {
      VSQuery       loQuery =null;
      VSResultSet   loResultSet =null;
      VSRow         loRow =null;
      StringBuffer  lsbWhereClause ;

      // check for null or empty values
      if (fsApplRscId==null || fsApplRscId.trim().length()==0 )
      {
         foPage.raiseException("Cannot print: Invalid application resource",
            SEVERITY_LEVEL_ERROR ) ;
      }

      if (fsPrintJobCd==null || fsPrintJobCd.trim().length()==0 )
      {
         foPage.raiseException("Cannot print: Invalid print job code",
            SEVERITY_LEVEL_ERROR ) ;
      }

      if (fsPrintRsrcId==null || fsPrintRsrcId.trim().length()==0 )
      {
         foPage.raiseException("Cannot print: Invalid print resource Id",
            SEVERITY_LEVEL_ERROR ) ;
      }

      try
      {

         lsbWhereClause = new StringBuffer(128);

         lsbWhereClause.append("APPL_RSRC_ID = " ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsApplRscId,true) ) ;
         lsbWhereClause.append(" AND (USER_ID='*ALL' OR USER_ID=" ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsUserId, true) ) ;
         lsbWhereClause.append( ") ");
         lsbWhereClause.append(" AND PRNT_JOB_CD = " ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsPrintJobCd,true) );
         lsbWhereClause.append(" AND PRNT_RSRC_ID = " ) ;
         lsbWhereClause.append( AMSSQLUtil.getANSIQuotedStr(fsPrintRsrcId,true) );

         /* Form VSQuery object to get the print resources from R_PRNT_JOB */
         loQuery = new VSQuery( foSession,
            "R_PRNT_ACCS",
            lsbWhereClause.toString(),
            "PRNT_JOB_CD ASC" ) ;

         loResultSet = loQuery.execute() ;
         loRow = loResultSet.first() ;

         if ( loRow == null )
         {
            /*
             * No row with the parameters exist -not authorized
             */
            return false;
         }

         // row exists - print job authorized
         return true;

      }
      catch ( Exception loEx )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loEx);

      } /* end try - catch */
      finally
      {
         if (loResultSet!=null)
         {
            loResultSet.close();
         }
      } /* final */

      return false;
   } // of isUserAuthorizedToPrint(VSSession, String, String,String,String)


   /**
    * This method resets the session property of 'VIEW_FORM' and 'FORM_DSCR'.
    *
    * @return void.
    */
   private void resetSessionProp()
   {
      try
      {
         getParentApp().getSession().getORBSession().setProperty("VIEW_FORM","");
         getParentApp().getSession().getORBSession().setProperty("FORM_DSCR", "");
      }
      catch (Exception loExcp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      } /* End Try*/

   } /* End resetSessionProp()*/

   /**
    *
    * This method generates the custom print actions to be displayed on this page
    * based on the print action table.
    *
    */
   private void generateCustomPrintActions()
   {
      int liNumCustomPrintActions = 0;

      SearchRequest loSearchRequest = new SearchRequest();

      loSearchRequest.addParameter("R_PRNT_ACTN", "DOC_CD", msDocCd);

      SearchRequest loOrderBy = new SearchRequest();
      loOrderBy.add(" PRNT_ACTN ");

      VSQuery loQuery = new VSQuery( getParentApp().getSession(),
         "R_PRNT_ACTN",loSearchRequest , loOrderBy) ;

      VSResultSet loRsltSet = null;

      try
      {

         loRsltSet = loQuery.execute() ;
         loRsltSet.last();
         liNumCustomPrintActions = loRsltSet.getRowCount() ;

         if ( liNumCustomPrintActions > 0 )
         {
            String[][] loPrintCaptions = getPrintCaptions(loRsltSet, liNumCustomPrintActions);
            StringBuffer lsbHTML   = new StringBuffer( 1000 ) ;
            for(int liIndex = 1; liIndex <= loPrintCaptions.length; liIndex++)
            {

               lsbHTML.append( "<tr>" ) ;

               /*
               * For all actions, create a new scalar check box for the user
               * to edit the action authority for the resource/role combination.
               */
               lsbHTML.append( "<td class=\"label\"><label for=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex - 1][0] ) ;
               lsbHTML.append( "\" title=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex - 1][0] ) ;
               lsbHTML.append( "\">" ) ;
               lsbHTML.append( loPrintCaptions[liIndex - 1][0] ) ;
               lsbHTML.append( " :</label></td><td class=\"val\"><input type=\"checkbox\" vsds=\"\" title=\"") ;
               lsbHTML.append( loPrintCaptions[liIndex - 1][0] ) ;
               lsbHTML.append( "\" " ) ;
               lsbHTML.append( "class=\"adveditable\" ") ;
               if(loPrintCaptions[liIndex-1][1].equalsIgnoreCase("true"))
               {
                  lsbHTML.append( "checked=\"checked\" " ) ;
               }

               lsbHTML.append( "name=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex - 1][0]  ) ;
               lsbHTML.append( "\" id=\"" ) ;
               lsbHTML.append( loPrintCaptions[liIndex - 1][0]  ) ;
               lsbHTML.append( "\" vsdf=\"" ) ;
               //lsbHTML.append( (loPrintCaptions[liIndex - 1][0]).substring(5)) ;
               lsbHTML.append( msFieldNameForHideInactiveProcurementLines);
               lsbHTML.append( "\"></td>" ) ;



               lsbHTML.append( "</tr>" ) ;


               moCustActnTCE.setPreserveHTMLTags( true ) ;
               moCustActnTCE.setValue( lsbHTML.toString() ) ;

            }

         } /* end if ( liNumCustomPrintActions > 0 ) */


      }
      catch(Exception loExcep)
      {

         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcep);

      }
      finally
      {
         if( loRsltSet!= null )
         {
            loRsltSet.close();
         }
      }//end finally

   }

   /**
    *
    * This method gets the print captions from the print action cvl based on what
    * is required from the print action table.
    *
    * @param foResultSet The print action table rows for which we need captions.
    * @param fiRowCount The number of captions we have to retrieve.
    * @return String[][] A String array containing the caption and its associated
    *                    default value.
    */
   private String[][] getPrintCaptions(VSResultSet foResultSet, int fiRowCount)
   {

      String[][] loTempPrintCaptions = new String[fiRowCount][2];

      StringBuffer lsbInList = new StringBuffer(64);
      lsbInList.append(" CVL_PRNT_ACTN_SV ");
      VSRow loCurrentRow = null;

      /*
       * If there is only 1 print action, use equals instead of IN
       *
       */
      if(fiRowCount == 1)
      {
         lsbInList.append(" = ");
         lsbInList.append(foResultSet.getRowAt(1).getData("PRNT_ACTN").getString());

         loTempPrintCaptions[0][1] =
               foResultSet.getRowAt(1).getData("DFLT_VAL").getString();
      }
      else
      {
         /*
         * build in list for cvl query
         *
         */
         lsbInList.append(" IN ( ");
         for(int liIndex = 1; liIndex <= fiRowCount; liIndex++)
         {
            if ( liIndex > 1)
            {
               lsbInList.append( ", ");
            }

            loCurrentRow = foResultSet.getRowAt(liIndex);

            lsbInList.append(loCurrentRow.getData("PRNT_ACTN").getString());

            loTempPrintCaptions[liIndex-1][1] =
                  loCurrentRow.getData("DFLT_VAL").getString();

         }


         lsbInList.append(" ) ");
      }


      VSQuery loQuery =
            new VSQuery( getParentApp().getSession(), "CVL_PRNT_ACTN",
                 lsbInList.toString(), "");

      VSResultSet loRslts = null;
      try
      {
         loRslts = loQuery.execute();
         VSRow loCurrentCVLRow = null;
         for(int liIndex = 1; liIndex <= fiRowCount; liIndex++)
         {
            loCurrentCVLRow = loRslts.getRowAt(liIndex);
            loTempPrintCaptions[liIndex-1][0] =
                  loCurrentCVLRow.getData("CVL_PRNT_ACTN_DV").getString();

         }

      }
      catch(Exception loException)
      {

         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loException);


      }
      finally
      {
         if( loRslts!=null )
         {
            loRslts.close();
         }
      }//end finally

      return loTempPrintCaptions;

   }
   /**
    *
    *  This method adds custom print parameters to the Session Message to be
    *  used by the VLS when processing the print action.
    *
    * @param fsExistingParams The parameter array built before this method is called.
    * @param foPLSRequest The PLSRequest to get the custom parameters from.
    */
   public String[] addCustomPrintParameters(
         String[] fsExistingParams, PLSRequest foPLSRequest)
   {

      String[] lsNewParams = new String[fsExistingParams.length + 1];
      for(int liIndex = 0; liIndex < fsExistingParams.length; liIndex++)
      {
         lsNewParams[liIndex] = fsExistingParams[liIndex];
      }

      /*
       * Get parameter name from DB, in case they changed it.
       */

      SearchRequest loSearchRequest = new SearchRequest();

      loSearchRequest.addParameter("CVL_PRNT_ACTN", PRN_ACTN_SV_COL,
            Integer.toString(HIDE_INACTV_SV));

      SearchRequest loOrderBy = new SearchRequest();
      loOrderBy.add(PRN_ACTN_SV_COL);

      VSQuery loQuery = new VSQuery( getParentApp().getSession(),
         "CVL_PRNT_ACTN",loSearchRequest , loOrderBy) ;

      VSResultSet loRsltSet = null;
      String lsParameterCaption = null;
      try
      {

         loRsltSet = loQuery.execute() ;
         VSRow loCurrentRow = loRsltSet.getRowAt(1);
         lsParameterCaption = loCurrentRow.getData(PRN_ACTN_DV_COL).getString();


      }
      catch(Exception loException)
      {
          // Add exception log to logger object
          moAMSLog.error("Unexpected error encountered while processing. ", loException);

      }
      finally
      {
         if( loRsltSet!=null )
         {
            loRsltSet.close();
         }
      }

      if(lsParameterCaption != null)
      {
         String lsHideInactive =
            foPLSRequest.getParameter(lsParameterCaption);


         if( lsHideInactive == null )
         {
            lsNewParams[fsExistingParams.length] = "false";
         }
         else
         {
            lsNewParams[fsExistingParams.length] = "true";
         }
         return lsNewParams;
      }
      else
      {
         return fsExistingParams;
      }
   }

   /**
    * This method enables/disables the Email, PDF and the Fax Fields
    * depending on the Print Output Type selected.
    * @param foSession
    */
   private void setFields(VSSession foSession)
   {
      DivElement divElementEmail    = (DivElement) getElementByName("email");
      DivElement divElementFax      = (DivElement) getElementByName("fax");
      DivElement divElementView     = (DivElement) getElementByName("viewForms");
      DivElement divElementPrinter  = (DivElement) getElementByName("printer");

      //Set the visibility to false by default for all the fields.
      divElementEmail.setVisible(false);
      divElementFax.setVisible(false);
      divElementView.setVisible(false);
      divElementPrinter.setVisible(false);

      ComboBoxElement loPrntOutputComboBox = (ComboBoxElement)getElementByName( "printOutputType" ) ;
      javax.swing.text.html.Option  loSelectedOutput = loPrntOutputComboBox.getSelectedItem();

      if(loSelectedOutput == null)
      {
         AMSPage loPage = (AMSPage)loPrntOutputComboBox.getPage();
         loPage.raiseException( "No Print Output Type Selected", SEVERITY_LEVEL_ERROR ) ;
      }
      else
      {
         String lsPrntOutput = loSelectedOutput.getValue();
         if(lsPrntOutput.equals(EMAIL_TYPE))
         {
            divElementEmail.setVisible(true);
         }
         else if(lsPrntOutput.equals(FAX_TYPE))
         {
            divElementFax.setVisible(true);

         }
         else if(lsPrntOutput.equals(PDF_TYPE))
         {
            divElementView.setVisible(true);
            String lsViewForms = AMSPLSUtil.getApplParamValue(IN_APP_CTRLImpl.DFLT_VIEW_FORMS,
                                                              getParentApp().getSession());
            /**
             * Checks if the DFLT_VIEW_FORMS parameter value returned from the Application Parameters
             * table is 'true' and if yes set the 'View Forms' check box selected.
             */
             if("true".equalsIgnoreCase(lsViewForms))
             {
                CheckboxElement loViewFormChkBox = (CheckboxElement)getElementByName("isViewForms");
                loViewFormChkBox.setChecked( true );
             } // end if
         }
         else if(lsPrntOutput.equals(PRINTER_TYPE))
         {
            divElementPrinter.setVisible(true);
         }
      }
   }

   /**
    * This method initializes the Print Output Type combo box from
    * The Print Resource Type Coded Value List.
    */
   private void initPrintOutputType()
   {
      VSSession   loSession      = getParentApp().getSession() ;
      ComboBoxElement loComboBox = null ;

      //Initialize the Print Output Type
      loComboBox = (ComboBoxElement)getElementByName( "printOutputType" ) ;
      javax.swing.text.html.Option  loSelectedOption = loComboBox.getSelectedItem();
      if ( loComboBox != null )
      {
         loComboBox.removeAllElements() ;
      }

      //Add all the values from the Coded Value List to the Print Output Type Combo Box
      VSQuery       loQuery         = null;
      VSResultSet   loResultSet     = null;
      VSRow         loRow           = null;
      String        lsWhere         = null;
      String        lsOrder         = null;
      try
      {
         AMSPage loPage                = (AMSPage)loComboBox.getPage();

         //Form VSQuery object to get the Print Output Types from CVL_PRNT_RSRC_TYP
         loQuery = new VSQuery( loSession,"CVL_PRNT_RSRC_TYP" , lsWhere , lsOrder ) ;
         loResultSet = loQuery.execute() ;

         loRow = loResultSet.first() ;
         if ( loRow == null )
         {
            loPage.raiseException( "No Print Output Types are defined on the Print Resource Type.",
                  SEVERITY_LEVEL_ERROR ) ;
         }
         // Empty out the combo box elements
         if ( loComboBox !=null)
         {
            loComboBox.removeAllElements();
         }

         String lsResTypCd   = null;
         String lsResTypNm   = null;
         HashMap loPrnJobMap = new HashMap(7);
         while ( loRow != null )
         {
            lsResTypCd  = loRow.getData( "PRNT_RSRC_TYP_CD" ).getString() ;
            lsResTypNm  = loRow.getData( "PRNT_RSRC_TYP_NM" ).getString() ;
            if ( loPrnJobMap.get(lsResTypCd) == null)
            {
               //Entry does not exist, populate the HashMap with the Print Resource Types.
               loPrnJobMap.put(lsResTypCd,lsResTypNm );
               loComboBox.addElement( lsResTypNm, lsResTypCd ) ;
            }
            loRow = loResultSet.next() ;
         }

         //Gets the Default Print Output Type(DFLT_PRNT_OUTPUT_TYPE) value from
         //the Application Control Parameters table
         String lsPrntOutputType = AMSPLSUtil.getApplParamValue(IN_APP_CTRLImpl.DFLT_PRNT_OUTPUT_TYPE,
        		                                                loSession);

         /*
          * Check if the value returned for parameter DFLT_PRNT_OUTPUT_TYPE on Application Parameters
          * has valid values, if yes sets that value on Print Output Type Drop Down.
          * If it has invalid values then it throws Warning and sets value to 'PDF_TYPE'
          */
         if (AMSStringUtil.strEqual(lsPrntOutputType,PRINTER_TYPE) ||
               AMSStringUtil.strEqual(lsPrntOutputType,EMAIL_TYPE) ||
                  AMSStringUtil.strEqual(lsPrntOutputType,PDF_TYPE) ||
                     AMSStringUtil.strEqual(lsPrntOutputType,FAX_TYPE))
         {
            //Set Print Output Type Drop Down to value on parameter DFLT_PRNT_OUTPUT_TYPE
            loComboBox.setValue(lsPrntOutputType);
         }
         else
         {
             loComboBox.setValue(PDF_TYPE);
             raiseException("Value setup for Parameter DFLT_PRNT_OUTPUT_TYPE on Application Parameters has invalid value.",
                   SEVERITY_LEVEL_WARNING);
         }

         if(loSelectedOption != null)
         {
            String lsSelectedValue = loSelectedOption.getValue();
            if(lsSelectedValue != null && lsSelectedValue.trim().length() > 0)
            {
               int liSelectedIndex = getIndexOfStoredValue(loComboBox, lsSelectedValue);
               if (liSelectedIndex != -1)
               {
                  //Set the Selected Print Output Type back, since page is loaded again.
                  javax.swing.text.html.Option  loSetOption = loComboBox.getElementAt(liSelectedIndex);
                  loComboBox.setSelectedItem(loSetOption);
               }
            }
         }
      }
      catch(Exception ae)
      {
         raiseException("Error while initializing the Print Output Types." , SEVERITY_LEVEL_ERROR);
      }
      finally
      {
         loResultSet.close();
      }
   }

   /**
    * Gets the value of Notify Send Status from Print Resource Parameters for
    * the selected Print Resource and sets it on Notify Send Status field of
    * current page.
    */
   private void setFaxNotifyFields()
   {
      String lsParmValue           = null;
      VSResultSet loPrintResultSet = null;
      String lsValue               = null;
      try
      {
         //Get the currently selected Print Resource.
         ComboBoxElement loPrntOutputComboBox           = (ComboBoxElement)getElementByName( "printRsrcCombo" ) ;
         javax.swing.text.html.Option  loSelectedOption = loPrntOutputComboBox.getSelectedItem();
         if(loSelectedOption != null)
         {
            lsValue = loSelectedOption.getValue();
         }
         if(! AMSStringUtil.strIsEmpty(lsValue))
         {
            //Get the Notify Send Status Field from the Print Resource Parameters
            StringBuffer lsbWhereClause = new StringBuffer(100);
            lsbWhereClause.append("PRNT_RSRC_ID = ").append(AMSSQLUtil.getANSIQuotedStr(lsValue , true));
            lsbWhereClause.append(" AND PARM_NM = ").append(AMSSQLUtil.getANSIQuotedStr(ATTR_FAX_NOTIFY_ADD , true));

            VSQuery loPrintQuery = new VSQuery( getParentApp().getSession() , "R_PRNT_RSRC_PARM",
                  lsbWhereClause.toString() , null);

            TextFieldElement loNotify = (TextFieldElement) getElementByName("FaxNotify");
            loPrintResultSet = loPrintQuery.execute() ;
            VSRow loPrintRow = loPrintResultSet.first() ;
            if(loPrintRow != null)
            {
               lsParmValue = loPrintRow.getData("PARM_VL").getString();
               if(lsParmValue != null && lsParmValue.trim().length() > 0)
               {
                  loNotify.setText(lsParmValue);
               }
            }
            else
            {
               //Set the Notify Send Status to default as defined on the Fax Server.
               loNotify.setText("Use Default");
            }
         }
      }
      catch(Exception ae)
      {
         raiseException("Error while setting the value for Notify Send Status field." , SEVERITY_LEVEL_ERROR);
      }
      finally
      {
         if(loPrintResultSet != null)
         {
            loPrintResultSet.close();
         }
      }
   }

   /**
    * Overrides the pageCreated method of AMSPage so that we can display different options
    * when the user right-clicks on the Document Print Dialog Page.
    * The following are the options currently displayed for the Document Print Dialog
    * Print, Page Help and Field Help.
    */
   public void pageCreated(VSPage foPage)
   {
      //Sets the Menu Name so that AMSMenuArray is able to perform the actions.
      setMenuName ( "MIPageHelp_MIFieldHelp" );

      //Continue with normal processing.
      super.pageCreated(foPage);
   }

   /**
    * When argument fboolRetainValues is true then sets the values of the Elements
    * on the page back to what they were before the action took place. When argument
    * fboolRetainValues is false then clears the values of the Elements
    * on the page. Only Elements of the type TextField and TextArea are taken into consideration.
    * Every time the page is rebuilt these elements lose their values and this method
    * can be called with argument fboolRetainValues as true for those cases in order to
    * restore them to their original values.
    * @param fboolRetainValues this flag instructs the method whether to clear or retain the values.
    * @param foRequest         this is the current Request parameter
    */
   public void retainPrintValues(boolean fboolRetainValues, PLSRequest foRequest)
   {
      BoundElement loElement     = null;
      String lsElementValue      = null;
      String lsElementName       = null;
      Enumeration loPageElements = getAllElements();

      //Return if values have to be restored and PLSRequest is null
      if( fboolRetainValues && (foRequest==null) )
      {
         return;
      }
      final String BLANK = "";
      //Loop through all Elements present on the Page.
      while(loPageElements.hasMoreElements())
      {
         loElement = (BoundElement) loPageElements.nextElement();
         if(loElement instanceof ScalarElement)
         {
            ScalarElement loScalarElement = (ScalarElement) loElement;
            if(loScalarElement.isShowing() && (loScalarElement instanceof TextFieldElement ||
                  loScalarElement instanceof TextAreaElement))
            {
               if( fboolRetainValues == true)
               {
                  //Get the name of the Element.
                  lsElementName  = loScalarElement.getName();

                  //Set the value of the Element from the Request to the values
                  //before the Action was performed.
                  lsElementValue = foRequest.getParameter(lsElementName);
                  if(lsElementValue != null)
                  {
                     loScalarElement.setValue(lsElementValue);
                  }
               }
               else
               if(fboolRetainValues == false)
               {
                  //Clear the value for the Element.
                  loScalarElement.setValue(BLANK);
               }
            }
         }
      }//end WHILE
   }//end of method

   /**
    * Refresh the data on Source Page. This method should be called after Document Print.
    * The assumption here is that Source page contains an instance of Document.
    * During Print action some of the Document fields would have been changed on
    * Document Header and its components and hence it is necessary to retrieve the
    * latest data on the Source page.
    */
   private void refreshSrcPage()
   {
      AMSPage loSrcPage    = (AMSPage)getSourcePage();
      if( loSrcPage != null )
      {
         DataSource loDataSource = loSrcPage.getRootDataSource();

         if( loDataSource!= null )
         {
            /*
             * If data on Document(Source) page is not refreshed then error encountered in
             * following scenario: User opens a Document, does not Edit, selects Print to
             * go to Print Dialog page, selects Print, after Print is successful System
             * returns back to Document page, User selects Edit and
             * "Another user has changed the same row..." error is received.
             * Hence for Document(Source) page this would retrieve latest data on
             * Document Header and also on its components(dependent DataSources).
             */

             /* If Document(Source) page is an instance of AMSDocTabbedPage we are not refreshing
             * data source as the refreshing action has been performed once on AMSHyperlinkActionElement class
             * after peforming the print action successfully.
             * So performing data refresh in case Document(Source) page is an instance of AMSDocCatalogPage.
             */

            if( loSrcPage instanceof com.amsinc.gems.adv.vfc.html.AMSDocCatalogPage )
            {
               /*
                * If current row on Catalog(Source) page is not refreshed then error encountered
                * in following scenario: User goes to Document Catalog page, selects a
                * Draft Document, selects Menu to Print(goes to Print Dialog page and
                * then prints the Document) without opening the Document. Once the
                * Print is over, System returns back to Document Catalog page.
                * The Draft Document remains selected (checkbox remains selected)
                * User then does Validate and the "Another user ..." error is issued.
                * Hence the current row on Catalog(Source) page is refreshed here. Note
                * loDataSource.executeQuery() should not be called here because it is
                * redundant to refresh the whole DataSource and secondly calling it
                * results in other errors(Versata error VSErrors.VSMSG_PageDataLost)
                * on Validate in above scenario because it is necessary to refresh the
                * "selected(checkbox true)" current row as it has changed,
                * before sending that row for any other action.
                */
               VSRow loCurrRow = loDataSource.getCurrentRow();
               if( loCurrRow!= null )
               {
                  loCurrRow.refresh(true);
               }
            }
         }//end if Root DataSource not null
      }//end if Sourc Page not null

   }//end of method

}
