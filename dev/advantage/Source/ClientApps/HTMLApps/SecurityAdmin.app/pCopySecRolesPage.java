//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.common.*;

import java.rmi.RemoteException;
import java.util.*;

import org.apache.commons.logging.Log;

import advantage.AMSStringUtil;
import advantage.CVL_FGN_ORG_CNFGImpl;

/*
 * * pCopySecRolesPage
 */

//{{FORM_CLASS_DECL
public class pCopySecRolesPage extends pCopySecRolesPageBase

//END_FORM_CLASS_DECL}}
{
   /*
    * This is the logger object for the class
    */
   private static Log moLog = AMSLogger.getLog(pCopySecRolesPage.class,
         AMSLogConstants.FUNC_AREA_SECURITY);

   /*
    * Constants to store the Step Nos in the wizard
    */
   private static final int STEP_ONE = 1;

   private static final int STEP_TWO = 2;

   private static final int STEP_THREE = 3;

   private static final int STEP_FOUR = 4;

   private int miCurStep = STEP_ONE;

   private boolean mboolFirstGenerate = true;

   /*
    * Variables to store the relevant HTML Elements.
    */
   private TextContentElement moStepTitle = null;

   private DivElement moBackBtn = null;

   private DivElement moNextBtn = null;

   private DivElement moCreateBtn = null;

   private DivElement moStep1Cont = null;

   private DivElement moStep2Cont = null;

   private DivElement moStep3Cont = null;

   private DivElement moStep4Cont = null;

   private TextFieldElement moSecurityRoleID = null;

   private TextFieldElement moDescription = null;


   private ComboBoxElement moOverErrCd = null;
   private ComboBoxElement moForOrgRstr = null;

   /*
    * Variables to store new Security Role Info.
    */
   private String msNewSecRoleID = null;

   private String msDescription = null;

   private int miForOrgRstr = 0;

   //Initialized to zero
   private int miOverErrCd = 0;

   private String msOrigSecRoleID = null;

   /*
    * The Vector to store all source Access Controls selected in the first page.
    */
   private Vector mvPrevSelectAccs = new Vector(32);

   /*
    * The Vector to store all source Valid Security Roles selected in the first
    * page.
    */
   private Vector mvPrevSelectValid = new Vector(32);

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pCopySecRolesPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pCopySecRolesPage_beforeGenerate
void pCopySecRolesPage_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
//Write Event Code below this line

      if (mboolFirstGenerate)
      {
         mboolFirstGenerate = false;
         moStepTitle = (TextContentElement) getElementByName("CurrStepTitle");
         moBackBtn = (DivElement) getElementByName("back");
         moNextBtn = (DivElement) getElementByName("next");
         moCreateBtn = (DivElement) getElementByName("createNewSecurityRole");

         moStep1Cont = (DivElement) getElementByName("Step1Cont");
         moStep2Cont = (DivElement) getElementByName("Step2Cont");
         moStep3Cont = (DivElement) getElementByName("Step3Cont");
         moStep4Cont = (DivElement) getElementByName("Step4Cont");

         moSecurityRoleID = (TextFieldElement) getElementByName("NewSecurityRoleID");
         moDescription = (TextFieldElement) getElementByName("Description");
         moOverErrCd = (ComboBoxElement) getElementByName("OverErrCd");
         /*
          * Populates combobox for Override Errors(moOverErrCd). Refer method description
          * for more details.
          */
         populateOverrideLevels();
      } /* end if ( mboolFirstGenerate ) */
      moForOrgRstr = (ComboBoxElement) getElementByName("ForOrgRstr");
      if(AMSStringUtil.strIsEmpty( moForOrgRstr.getValue() ))
      {
         moForOrgRstr.setValue(String.valueOf(1) );// By default setting the value as 1=None if user leave blank
      }
      switch (miCurStep)
      {
         case STEP_ONE:
            moStepTitle.setValue("Step 1: Select a Security Role to copy...");
            moBackBtn.setVisible(false);
            moNextBtn.setVisible(true);
            moCreateBtn.setVisible(false);
            moStep1Cont.setVisible(true);
            moStep2Cont.setVisible(false);
            moStep3Cont.setVisible(false);
            moStep4Cont.setVisible(false);
            break;

         case STEP_TWO:
            moStepTitle
                  .setValue("Step 2: Verify the Foreign Organization records"
                        + " linked with the source security role to copy...");
            moBackBtn.setVisible(true);
            moNextBtn.setVisible(true);
            moCreateBtn.setVisible(false);
            moStep1Cont.setVisible(false);
            moStep2Cont.setVisible(true);
            moStep3Cont.setVisible(false);
            moStep4Cont.setVisible(false);

            retrieveForeignOrgForRole();

            break;

         case STEP_THREE:
            moStepTitle
                  .setValue("Step 3: Verify the Secured Field Access and Field"
                        + " comparision records linked with the source security role to copy...");
            moBackBtn.setVisible(true);
            moNextBtn.setVisible(true);
            moCreateBtn.setVisible(false);
            moStep1Cont.setVisible(false);
            moStep2Cont.setVisible(false);
            moStep3Cont.setVisible(true);
            moStep4Cont.setVisible(false);
            break;

         case STEP_FOUR:
            moStepTitle.setValue("Step 4: Enter New Security Role Information");
            moBackBtn.setVisible(true);
            moNextBtn.setVisible(false);
            moCreateBtn.setVisible(true);
            moStep1Cont.setVisible(false);
            moStep2Cont.setVisible(false);
            moStep3Cont.setVisible(false);
            moStep4Cont.setVisible(true);
            moSecurityRoleID.setValue(msNewSecRoleID != null ? msNewSecRoleID
                  : "");
            moDescription.setValue(msDescription != null ? msDescription : "");
            moOverErrCd.setValue( String.valueOf(miOverErrCd) );
            break;

         default:
            moStepTitle.setValue("");
            moBackBtn.setVisible(false);
            moNextBtn.setVisible(false);
            moCreateBtn.setVisible(false);
            moStep1Cont.setVisible(false);
            moStep2Cont.setVisible(false);
            moStep3Cont.setVisible(false);
            moStep4Cont.setVisible(false);
            break;
      } /* end switch( miCurStep ) */

   }

//END_EVENT_pCopySecRolesPage_beforeGenerate}}
//{{EVENT_pCopySecRolesPage_beforeActionPerformed
void pCopySecRolesPage_beforeActionPerformed( ActionElement foActnElem, PageEvent foPgEvt, PLSRequest foPLSReq )
{
   //Write Event Code below this line
      Enumeration loEnum = null;
      AMSTableElement loElement = null;
      TableElement loTbl = null;
      Integer loEnumIndex = null;
      String lsActnNm = foActnElem.getName();

      /*
       * When we are in Step 4, get the new role details.
       */
      if (miCurStep == STEP_FOUR)
      {
         msNewSecRoleID = foPLSReq.getParameter("NewSecurityRoleID");
         msDescription = foPLSReq.getParameter("Description");

         int liForOrgRstr = 0;
         String lsForOrgRstr = foPLSReq.getParameter("ForOrgRstr");
         try
         {
            if(!AMSStringUtil.strIsEmpty( lsForOrgRstr ))
            {
               liForOrgRstr = Integer.parseInt(lsForOrgRstr);
			    moForOrgRstr.setValue(String.valueOf(liForOrgRstr) );//Setting the values remain same if in page getting any errors
            }
         }
         catch (NumberFormatException loExcp)
         {
            if (moLog.isErrorEnabled())
            {
               moLog.error("NumberFormatException occured", loExcp);
            }
         }
         if (liForOrgRstr != 0)
         {
            miForOrgRstr = liForOrgRstr;
         }
         if (foPLSReq.getParameter("OverErrCd") != null)
         {
            try
            {
               miOverErrCd = Integer.parseInt(foPLSReq
                     .getParameter("OverErrCd"));
            }
            catch (NumberFormatException loExcp)
            {
               if (moLog.isErrorEnabled())
               {
                  moLog.error("NumberFormatException occured", loExcp);
               }
            }
         }

      } /* end if ( miCurStep == STEP_FOUR ) */

      if (lsActnNm.equalsIgnoreCase("next"))
      {
         if (miCurStep == STEP_ONE)
         {
            VSRow loSourceRow = T1R_SC_SEC_ROLE.getCurrentRow();
            if (loSourceRow == null)
            {
               raiseException(
                     "An existing source security role must be selected.",
                     SEVERITY_LEVEL_ERROR);
               return;
            } /* end if ( loSourceRow == null ) */

            msOrigSecRoleID = loSourceRow.getData("SEC_ROLE_ID").getString();

            /*
             * This part used to store the selected Access Controls.
             */
            loTbl = (TableElement) getElementByName("tblT2R_SC_RSRC_ACCS");
            if (loTbl != null)
            {
               loEnum = loTbl.getSelectedRows();
               while (loEnum.hasMoreElements())
               {
                  loEnumIndex = (Integer) loEnum.nextElement();
                  if (!mvPrevSelectAccs.contains(loEnumIndex))
                  {
                     mvPrevSelectAccs.add(loEnumIndex);
                  }
               } /* end while(loEnum.hasMoreElements()) */
            }

            if (!mvPrevSelectValid.isEmpty())
            {
               loElement = (AMSTableElement) getElementByName("tblT4R_SC_VALID_ROLES");
               loElement.setSelectedRows(mvPrevSelectValid);
            }

         } /* End if ( miCurStep == STEP_ONE ) */

         else if (miCurStep == STEP_TWO)
         {
            /*
             * This part used to store the selected Valid Departments.
             */
            loTbl = (TableElement) getElementByName("tblT4R_SC_VALID_ROLES");
            if (loTbl != null)
            {
               loEnum = loTbl.getSelectedRows();
               while (loEnum.hasMoreElements())
               {
                  loEnumIndex = (Integer) loEnum.nextElement();
                  if (!mvPrevSelectValid.contains(loEnumIndex))
                  {
                     mvPrevSelectValid.add(loEnumIndex);
                  }
               } /* end while(loEnum.hasMoreElements()) */
            }
         }

         if (miCurStep < STEP_FOUR)
         {
            {
               miCurStep++;
            } /* end if ( validateNext( foPLSReq ) ) */
         } /* end if ( miCurStep < STEP_FOUR ) */
         foPgEvt.setCancel(true);
         foPgEvt.setNewPage(this);
      } /* end if ( lsActnNm.equalsIgnoreCase( "next" ) ) */

      else if (lsActnNm.equalsIgnoreCase("back"))
      {
         if(miCurStep == STEP_TWO)
         {
            /*
             * This part used to store the selected Valid Departments.
             */
            loTbl = (TableElement) getElementByName("tblT4R_SC_VALID_ROLES");
            if (loTbl != null)
            {
               loEnum = loTbl.getSelectedRows();
               while (loEnum.hasMoreElements())
               {
                  loEnumIndex = (Integer) loEnum.nextElement();
                  if (!mvPrevSelectValid.contains(loEnumIndex))
                  {
                     mvPrevSelectValid.add(loEnumIndex);
                  }
               } /* end while(loEnum.hasMoreElements()) */
            }
         }

         /*
          * This part used to restore the previously selected Access Controls.
          */
         if (miCurStep == STEP_TWO && !mvPrevSelectAccs.isEmpty())
         {
            loElement = (AMSTableElement) getElementByName("tblT2R_SC_RSRC_ACCS");
            loElement.setSelectedRows(mvPrevSelectAccs);
         }
         /*
          * This part used to restore the previously selected Valid Roles.
          */
         else if (miCurStep == STEP_THREE && !mvPrevSelectValid.isEmpty())
         {
            loElement = (AMSTableElement) getElementByName("tblT4R_SC_VALID_ROLES");
            loElement.setSelectedRows(mvPrevSelectValid);
         }

         if (miCurStep > STEP_ONE)
         {
            miCurStep--;
         } /* end if ( miCurStep > STEP_ONE ) */
         foPgEvt.setCancel(true);
         foPgEvt.setNewPage(this);
      } /* end else if ( lsActnNm.equalsIgnoreCase( "back" ) ) */

      else if (lsActnNm.equalsIgnoreCase("createNewSecurityRole"))
      {
         if (AMSStringUtil.strIsEmpty(msNewSecRoleID))
         {
            raiseException("New Security role must be entered.",
                  SEVERITY_LEVEL_ERROR);
            return;
         }
         if (AMSStringUtil.strIsEmpty(msDescription))
         {
            raiseException("Description must be entered.", SEVERITY_LEVEL_ERROR);
            return;
         }

         copyRoleRecords();

         /*
          * Refreshes the page after copying the source security role to Target
          * security role. If we do not refresh the target security page it will
          * not show the copied security role in current page.
          */
         refreshDataSource(T2R_SC_RSRC_ACCS);
      } /* end else if ( lsActnNm.equalsIgnoreCase( "createNewSecurityRole" ) ) */
   }

//END_EVENT_pCopySecRolesPage_beforeActionPerformed}}

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
			pCopySecRolesPage_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pCopySecRolesPage_beforeActionPerformed( ae, evt, preq );
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
    * This is the main method for copying Security Role. It calls helper methods
    * to copy all relevant tables.
    */
   private void copyRoleRecords()
   {
      VSResultSet loNewSecurityRoleRS = null;
      VSResultSet loTgtAccessRS = null;
      VSResultSet loTgtValidRS = null;
      VSResultSet loTgtForeignRS = null;
      VSResultSet loTgtFieldRS = null;
      VSResultSet loTgtFieldCmprRS = null;

      VSSession loCurrSession = getParentApp().getSession();
      if (!loCurrSession.isTransactionInProgress())
      {
         loCurrSession.beginTrans();
      }
      VSORBSession loORBSess = loCurrSession.getORBSession();

      try
      {
         /*
          * This is the most important property for the copying process. Four of
          * the tables that we are updating below are MRT Tables. MRT Tables
          * cannot be directly updated. So we have to set this property before
          * updating MRT tables.
          */
         loORBSess.setProperty("maintenanceString", "Y");

         loNewSecurityRoleRS = getNewResultSet("R_SC_SEC_ROLE");
         loTgtAccessRS = getNewResultSet("R_SC_RSRC_ACCS");
         loTgtValidRS = getNewResultSet("R_SC_VALID_ROLES");
         loTgtForeignRS = getNewResultSet("R_SC_FGN_ORG");
         loTgtFieldRS = getNewResultSet("R_SC_FLD_ACCS");
         loTgtFieldCmprRS = getNewResultSet("R_SC_FLD_CMPR");

         createSecRole(loNewSecurityRoleRS);
         loNewSecurityRoleRS.updateDataSource();

         copyAccessControls(loTgtAccessRS);
         loTgtAccessRS.updateDataSource();

         copyValidRoles(loTgtValidRS);
         loTgtValidRS.updateDataSource();

         /*
          * We are copying only those Foreign Organizations which have USER_ID =
          * 'ALL'. But if the 'Foreign Organization Restriction' Value is
          * User ID Required for the new Security Role, then wildcard 'ALL' is
          * not allowed. Hence don't copy anything.
          */

         if (miForOrgRstr == CVL_FGN_ORG_CNFGImpl.USER_ID_REQUIRED)
         {
            raiseException(
                  "Foreign Organization records were not be copied since  "
                        + "'Foreign Organization Restriction' field is set to ‘User ID Required’",
                  AMSPage.SEVERITY_LEVEL_INFO);
         }
         else
         {
            copyForeignOrg(loTgtForeignRS);
            loTgtForeignRS.updateDataSource();
         }

         copyFieldAccesses(loTgtFieldRS);
         loTgtFieldRS.updateDataSource();

         copyFieldComparisons(loTgtFieldCmprRS);
         loTgtFieldCmprRS.updateDataSource();

         /* All inserts should happen as single unit of work. */

         loCurrSession.commit();
      } /* end try */
      catch (Exception loExcp)
      {
         /*
          * If insert on any table failed or the inserts could not be committed
          * successfully then the exception would be caught here and the
          * transaction should be rolled back to ensure that none of the inserts
          * are saved.
          */

         loCurrSession.rollback();
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExcp);

         raiseException("Security Role could not be created successfully",
               AMSPage.SEVERITY_LEVEL_ERROR);
         raiseException(loExcp.getMessage(), AMSPage.SEVERITY_LEVEL_ERROR);
         return;
      }
      finally
      {
         loNewSecurityRoleRS.close();
         loTgtAccessRS.close();
         loTgtValidRS.close();
         loTgtForeignRS.close();
         loTgtFieldRS.close();
         loTgtFieldCmprRS.close();
         try
         {
            loORBSess.setProperty("maintenanceString", "");
         }
         catch (RemoteException loExcp)
         {
            if (moLog.isErrorEnabled())
            {
               moLog.error("maintenanceString could not be reset.", loExcp);
            }
         // Add exception log to logger object
         moLog.error("Unexpected error encountered while processing. ", loExcp);

         }
      }

      raiseException("New security role \"" + msNewSecRoleID
            + "\" was created " + "successfully.", AMSPage.SEVERITY_LEVEL_INFO);
      raiseException("Regenerate MRT file & Restart the server for changes to be" +
            " reflected", AMSPage.SEVERITY_LEVEL_INFO);

      miCurStep = STEP_ONE;
      msNewSecRoleID = null;
      msDescription = null;
      miForOrgRstr = 0;
      miOverErrCd = 0;

   } /* end copySecurityRole() */

   /**
    * This method creates a new Security role based on the info entered by user.
    *
    * @param foNewSecInfoRS Resultset of target role
    * @return none.
    */
   private void createSecRole(VSResultSet foNewSecInfoRS) throws Exception
   {
      VSRow loNewRow = null;

      if (foNewSecInfoRS == null)
      {
         throw new Exception("Security Role could not be created successfully");
      }

      foNewSecInfoRS.last();
      loNewRow = foNewSecInfoRS.insert();

      if (loNewRow == null)
      {
         throw new Exception("Security Role could not be created successfully");
      } /* end if ( loNewRow == null ) */

      loNewRow.getData("SEC_ROLE_ID").setString(msNewSecRoleID);
      loNewRow.getData("SEC_ROLE_DSCR").setString(msDescription);
      loNewRow.getData("FGN_ORG_RSTR").setInt(miForOrgRstr);
      if (miOverErrCd != 0)
      {
         loNewRow.getData("OV_ERR_CD").setInt(miOverErrCd);
      }
   } /* end createSecRole() */

   /**
    * Clones the Original Row to New Row.
    *
    * @param foOrigRow Original Row.
    * @param foNewRow  New Row, which is the clone of Original Row.
    * @return none.
    */
   private void cloneRow(VSRow foOrigRow, VSRow foNewRow)
   {
      int liColumnCount = foNewRow.getColumnCount();
      for (int i = 1; i <= liColumnCount; i++)
      {
         foNewRow.getData(i).setData(foOrigRow.getData(i));
      }
   }// end cloneRow()

   /**
    * This method copies the selected access control records of source security
    * role to the new security role.
    *
    * @param loTgtUserRoleLnkRS  An empty resultset used for inserting new Access
    *                            Control records.
    * @return void.
    */
   private void copyAccessControls(VSResultSet foTgtAccessRS) throws Exception
   {
      VSRow loOrigRow = null;
      VSRow loNewRow = null;

      /*
       * If Size is zero it means no records are checked. Hence there is nothing
       * to do.
       */
      if (mvPrevSelectAccs.size() == 0)
      {
         return;
      }

      Iterator loIterator = getCheckedRows(mvPrevSelectAccs, T2R_SC_RSRC_ACCS.getResultSet());

      while (loIterator.hasNext())
      {
         loOrigRow = (VSRow) loIterator.next();
         loNewRow = foTgtAccessRS.insert();
         if (loNewRow == null)
         {
            throw new Exception(
                  "Access Control records could not be copied successfully.");
         } /* end if ( loNewRow == null ) */

         cloneRow(loOrigRow, loNewRow);
         loNewRow.getData("SEC_ROLE_ID").setString(msNewSecRoleID);
      }
   } // end copySecurityRoleLinks

   /**
    * This method copies the foreign organization of source security role to the
    * new security role.
    *
    * @param loTgtUserRoleLnkRS    An empty resultset used for inserting new
    *                              Foreign Organization records.
    * @return void.
    */
   private void copyForeignOrg(VSResultSet foTgtForeignRS) throws Exception
   {
      VSResultSet loSrcVSResultSet = null;
      VSRow loOrigRow = null;
      VSRow loNewRow = null;

      loSrcVSResultSet = T3R_SC_FGN_ORG.getResultSet();

      loOrigRow = loSrcVSResultSet.first();
      while (loOrigRow != null)
      {
         loNewRow = foTgtForeignRS.insert();
         if (loNewRow == null)
         {
            throw new Exception(
                  "Foreign Organization records could not be copied successfully.");
         } /* end if ( loNewRow == null ) */

         cloneRow(loOrigRow, loNewRow);
         loNewRow.getData("SEC_ROLE_ID").setString(msNewSecRoleID);
         loOrigRow = loSrcVSResultSet.next();
      }

   } // end copySecurityRoleLinks

   /**
    * This method copies the selected Valid security roles of source security
    * role to the new security role.
    *
    * @param loTgtUserRoleLnkRS  An empty resultset used for inserting new Valid
    *                            Role records.
    * @return void.
    */
   private void copyValidRoles(VSResultSet foTgtAccessRS) throws Exception
   {
      VSRow loOrigRow = null;
      VSRow loNewRow = null;

      /*
       * If Size is zero it means no records are checked. Hence there is nothing
       * to do.
       */
      if (mvPrevSelectValid.size() == 0)
      {
         return;
      }

      Iterator loIterator = getCheckedRows(mvPrevSelectValid,
            T4R_SC_VALID_ROLES.getResultSet());

      while (loIterator.hasNext())
      {
         loOrigRow = (VSRow) loIterator.next();
         loNewRow = foTgtAccessRS.insert();
         if (loNewRow == null)
         {
            throw new Exception(
                  "Valid Sec Roles records could not be copied successfully.");
         } /* end if ( loNewRow == null ) */
         cloneRow(loOrigRow, loNewRow);
         loNewRow.getData("SEC_ROLE_ID").setString(msNewSecRoleID);
      }

   } // end copySecurityRoleLinks

   /**
    * This method copies the Field accesses of source security role to the
    * new security role.
    *
    * @param loTgtUserRoleLnkRS   An empty resultset used for inserting new Field
    *                             Access records.
    * @return void.
    */
   private void copyFieldAccesses(VSResultSet foTgtFieldRS) throws Exception
   {
      VSResultSet loSrcVSResultSet = null;
      VSRow loOrigRow = null;
      VSRow loNewRow = null;

      loSrcVSResultSet = getResultSet("R_SC_FLD_ACCS", "SEC_ROLE_ID="
            + AMSSQLUtil.getANSIQuotedStr(msOrigSecRoleID, true));

      loOrigRow = loSrcVSResultSet.first();
      while (loOrigRow != null)
      {
         loNewRow = foTgtFieldRS.insert();
         if (loNewRow == null)
         {
            throw new Exception(
                  "Field Access records could not be copied successfully.");
         } /* end if ( loNewRow == null ) */

         cloneRow(loOrigRow, loNewRow);
         loNewRow.getData("SEC_ROLE_ID").setString(msNewSecRoleID);
         loOrigRow = loSrcVSResultSet.next();
      }
      loSrcVSResultSet.close();

   } // end copyField()

   /**
    * This method copies the Field compares of source security role to the
    * new security role.
    *
    * @param loTgtUserRoleLnkRS    An empty resultset used for inserting new
    *                              Field Compare records.
    * @return void.
    */
   private void copyFieldComparisons(VSResultSet foTgtFieldRS) throws Exception
   {
      VSResultSet loSrcVSResultSet = null;
      VSRow loOrigRow = null;
      VSRow loNewRow = null;

      loSrcVSResultSet = getResultSet("R_SC_FLD_CMPR", "SEC_ROLE_ID="
            + AMSSQLUtil.getANSIQuotedStr(msOrigSecRoleID, true));

      loOrigRow = loSrcVSResultSet.first();
      while (loOrigRow != null)
      {
         loNewRow = foTgtFieldRS.insert();
         if (loNewRow == null)
         {
            throw new Exception(
                  "Field Compare records could not be copied successfully.");
         } /* end if ( loNewRow == null ) */

         cloneRow(loOrigRow, loNewRow);
         loNewRow.getData("SEC_ROLE_ID").setString(msNewSecRoleID);
         loOrigRow = loSrcVSResultSet.next();
      }
      loSrcVSResultSet.close();

   } // end copyField()

   /**
    * This is used to modify the query for Foreign Organization. Only those
    * Foreign Organization records are retrieved for whom Access Control records
    * were selected in Step 1 and for which USER_ID = 'ALL'.
    *
    */
   private void retrieveForeignOrgForRole()
   {
      VSRow loOrigRow = null;
      StringBuffer lsbWhere = new StringBuffer();

      Iterator loIterator = getCheckedRows(mvPrevSelectAccs,
            T2R_SC_RSRC_ACCS.getResultSet());

         lsbWhere.append(" USER_ID = 'ALL' AND RSRC_GP_ID IN ('ALL'");
         if (loIterator.hasNext())
         {
            while (loIterator.hasNext())
            {
               loOrigRow = (VSRow) loIterator.next();
               lsbWhere.append(", '" + AMSSQLUtil.getANSIQuotedStr ( loOrigRow.getData("RSRC_GP_ID").getString() )
                     + "'");
            }
         }
         lsbWhere.append(")");

      T3R_SC_FGN_ORG.setQueryInfo("R_SC_FGN_ORG", lsbWhere.toString(),
            "[SEC_ROLE_ID] = [?SEC_ROLE_ID]", "", true);
      T3R_SC_FGN_ORG.executeQuery();
   }

   /**
    * Returns the ResultSet by runnning a query on the Table Name passed.
    * The where clause is also passed as parameter.
    *
    * @param fsQueryName DataObject Name.
    * @param fsWhereClause Condition Clause
    * @return VSResultSet.
    */
   public VSResultSet getResultSet(String fsQueryName, String fsWhereClause)
   {
      VSQuery loQuery;

      loQuery = new VSQuery(getParentApp().getSession(), fsQueryName,
            fsWhereClause, "SEC_ROLE_ID");

      return loQuery.execute();
   } /* end getResultSet() */

   /**
    * Creates a new (empty) VSResultSet for the given DataSource name and
    * returns it. The where clause " 1=2 " will ensure that we get an empty one.
    *
    * @param fsQueryName DataObject Name.
    * @return VSResultSet
    */
   public VSResultSet getNewResultSet(String fsQueryName)
   {
      VSQuery loQuery;
      loQuery = new VSQuery(getParentApp().getSession(), fsQueryName, " 1=2 ", "");
      return loQuery.getNewResultSet();
   } /* end getNewResultSet() */


   /**
    * This method returns a collection of records selected on a checkbox.
    *
    * @param fvChecked
    * @param foVSResultSet
    * @return
    */
   private Iterator getCheckedRows(Vector fvChecked, VSResultSet foVSResultSet)
   {
      Hashtable lhSelectedAccs = new Hashtable(32);
      Integer loEnumIndex = null;
      /*
       * The logic here is to populate the hashtable with the roles of the
       * source user that needs to be copied to target user. Initially using the
       * for loop all selected source user roles are stored in the Hashtable.
       */

      for (int liCtr = 0; liCtr < fvChecked.size(); liCtr++)
      {
         loEnumIndex = (Integer) fvChecked.get(liCtr);
         lhSelectedAccs.put(loEnumIndex, foVSResultSet.getRowAt(loEnumIndex
               .intValue()));
      }

      return lhSelectedAccs.values().iterator();
   }


   /**
    * Populates combobox for Override Errors(moOverErrCd). Since vsds is set to empty String for this
    * element on html page, values(1 to 10) displayed in this combobox is sorted as String
    * and hence combobox displayes 1,10,2,3....so on. This type of sort is numerically incorrect
    * because 10 should not appear after 1. Hence this method queries CVL_OV_ERR_CD sorted by
    * OV_ERR_CD and manually populates the combobox so that values are sorted numerically and
    * displayed correctly( 1,2,3...,10).
    */
   private void populateOverrideLevels()
   {
      //Empty the CVL_OV_ERR_CD combobox
      moOverErrCd.removeAllElements();
      //Query CVL_OV_ERR_CD sorted by OV_ERR_CD
      VSQuery loQuery = new VSQuery( getParentApp().getSession(), "CVL_OV_ERR_CD",
            AMSCommonConstants.EMPTY_STR, "OV_ERR_CD" );
      VSResultSet loOverrideLvlRS = null;
      try
      {
         loOverrideLvlRS = loQuery.execute();
         VSRow loOverrideLvlRow = loOverrideLvlRS.first();
         if( loOverrideLvlRow!= null )
         {
            //Add the first blank value element
            moOverErrCd.addElement( AMSCommonConstants.EMPTY_STR, "0" );
            //Add display value and stored value
            moOverErrCd.addElement( String.valueOf(loOverrideLvlRow.getData("OV_ERR_DESC").getInt()),
                  String.valueOf(loOverrideLvlRow.getData("OV_ERR_CD").getInt()) );
            loOverrideLvlRow = loOverrideLvlRS.next();
            while( loOverrideLvlRow!=null )
            {
               moOverErrCd.addElement( String.valueOf(loOverrideLvlRow.getData("OV_ERR_DESC").getInt()),
                     String.valueOf(loOverrideLvlRow.getData("OV_ERR_CD").getInt()) );
               loOverrideLvlRow = loOverrideLvlRS.next();
            }//end while
         }//end if Row is not null
         loOverrideLvlRS.close();
         loOverrideLvlRS = null;
      }//end try
      finally
      {
         if( loOverrideLvlRS!= null )
         {
            loOverrideLvlRS.close();
         }
      }//end finally
   }//end of method


}
