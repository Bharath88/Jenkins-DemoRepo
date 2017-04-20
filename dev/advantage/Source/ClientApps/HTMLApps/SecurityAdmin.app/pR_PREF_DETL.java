//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import javax.swing.text.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.rmi.*;
import javax.swing.text.DefaultStyledDocument.* ;
import javax.swing.text.html.HTML.* ;

import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;

import versata.vfc.html.TableElement.ActionColumn;
import versata.vfc.html.TableElement.TableColumnElement;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import advantage.AMSBatchUtil;
import advantage.AMSStringUtil;

import org.apache.commons.logging.Log;

/*
**  pR_PREF_DEF
*/

//{{FORM_CLASS_DECL
public class pR_PREF_DETL extends pR_PREF_DETLBase

//END_FORM_CLASS_DECL}}
{
   private VSPage moDocPage = null;
   private static final char   WILD_CARD_CHAR          = '?' ;
   private static final char   INT_WILD_CARD_CHAR      = '¥' ;
   private TreeSet<String> moFgnDept = null;
   private boolean mboolIsAdmin = false;
   private Vector moDeptValues = new Vector();
   private byte[] moPrefData = null;
   /** The Resource ID value which AMSSecurity uses for performing Authorization */
   private String msRsrcId = null;
   /** Value is true when Page Type is Document, otherwise false */
   private boolean mboolPageTypIsDoc;
   /**
    * Value is set to true when save and refresh Preference Data DataSource is required
    * while navigating back to this page from other pages, e.g., selecting
    * "Create Preference" or "Edit Preference" links and then navigating back from Document page
    * after setting preferences. In all other cases value is false.
    */
   private boolean mboolSavePrefData;
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pR_PREF_DETL.class,
      AMSLogConstants.FUNC_AREA_SECURITY ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pR_PREF_DETL ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pR_PREF_DETL_beforeGenerate
void pR_PREF_DETL_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
      moDocPage = null;
      moFgnDept = null;
      mboolIsAdmin = AMSSecurity.isUserAdmin(getUserID());

      VSRow loSourceRow = getSourcePage().getRootDataSource().getCurrentRow();
      if ( loSourceRow == null )
      {
         return;
      }

      applyPageCode(loSourceRow);
      //Sets msRsrcId with the Resource ID and mboolPageTypIsDoc to indicate if the Resource is
      //a Document or Reference page
      setRsrcIdAndPageTyp(loSourceRow);
}
//END_EVENT_pR_PREF_DETL_beforeGenerate}}
//{{EVENT_pR_PREF_DETL_beforeActionPerformed
void pR_PREF_DETL_beforeActionPerformed( ActionElement foActionElement, PageEvent foPageEvent, PLSRequest foRequest )
{
   moDocPage = null;
   //Write Event Code below this line
   String lsAction = foActionElement.getName();

   //If action is "Create Preference" or "Edit Preference" links then
   //set variable mboolSavePrefData to true to Save/refresh the Preference Data DataSource .
   if(lsAction.equals("R_PREF_DETL_Create_Pref")|| lsAction.equals("btnNavToDoc"))
   {
      mboolSavePrefData = true;
   }

   VSORBSession loORBSess = getParentApp().getSession().getORBSession();

   VSRow loSourceRow = getSourcePage().getRootDataSource().getCurrentRow();
   if ( loSourceRow == null )
   {
      raiseException("In order to setup preferences, Setup record must exist.",
               AMSPage.SEVERITY_LEVEL_ERROR);
      foPageEvent.setNewPage(this);
      foPageEvent.setCancel(true);
      return;
   }

   if(lsAction!=null)
   {
      // If the Active flag is set to No on setup page then on clicking of Create/Edit preferences, an
     // error is raised describing the situation.
     if ((lsAction.equals("R_PREF_DETL_Create_Pref") || lsAction.equals("btnNavToDoc"))
            && !getSourcePage().getRootDataSource().getCurrentRow().getData("IS_ACTIVE").getBoolean())
      {
         raiseException("Setup record must have active flag checked to create/update preferences",
                 AMSPage.SEVERITY_LEVEL_ERROR);
         foPageEvent.setNewPage(this);
         foPageEvent.setCancel(true);
         return;
     }

     /*
      * Note action authorized security check is done on pls side only for events that do
      * not immediately trigger vls side events, e.g., Create Preference and Edit Preference links.
      * Rest all action authorized security checks are done on vls side on R_PREF_DETLImpl.java,
      * e.g., User actions that cause saving a new or updating or deleting a Preference.
      */
     if(lsAction.equals("R_PREF_DETL_Create_Pref"))
      {
        String lsSelectedValue = foRequest.getParameter("txtR_PREF_DETL_Dept");
         //Raise error when Department value is not provided before selecting
         //"Create Preference" link.
         if( (lsSelectedValue == null) || (lsSelectedValue.trim().equals("")) )
         {
            raiseException("Department value is required to create preferences.",
                  AMSPage.SEVERITY_LEVEL_ERROR);
            foPageEvent.setNewPage(this);
            foPageEvent.setCancel(true);
            return;
         }
         if(lsSelectedValue  != null)
         {
            //Raise Error when User Selects Department other that ALL for Reference pages.
            if ((!mboolPageTypIsDoc) && (!lsSelectedValue.equals(SITEWIDE_PREFERENCES)))
            {
               raiseException("Department should be 'ALL' for Reference pages.",
                     AMSPage.SEVERITY_LEVEL_ERROR);
               foPageEvent.setNewPage(this);
               foPageEvent.setCancel(true);
               return;
            }
            if ( mboolIsAdmin)
            {
               if ( lsSelectedValue.equals(SITEWIDE_PREFERENCES))
               {
                 if(!getSourcePage().getRootDataSource().getCurrentRow().getData("IF_SITE_SETNGS_ON").getBoolean())
                 {
                    raiseException("Sitewise Preference is not enabled.", AMSPage.SEVERITY_LEVEL_ERROR);
                      foPageEvent.setNewPage(this);
                      foPageEvent.setCancel(true);
                      return;
                 }
               }
               else
               {
                  if (!getSourcePage().getRootDataSource().getCurrentRow().getData("IF_GRP_SETNGS_ON").getBoolean())
                  {
                     raiseException("Department Level Preference is not enabled.", AMSPage.SEVERITY_LEVEL_ERROR);
                       foPageEvent.setNewPage(this);
                       foPageEvent.setCancel(true);
                       return;
                  }
               }
            }

            int liRowIndex = T2R_PREF_DATA.getResultSet().findFirst("GROUP_ID='" + AMSSQLUtil.getANSIQuotedStr(lsSelectedValue) +"'");

            if ( liRowIndex == -1)
            {
               try
               {
                  /*
                   * Fetch the entry of this page from APGS to find out page type.
                   */
                  String lsPageCd = loSourceRow.getData("PAGE_CD").getString();
                  SearchRequest lsr = new SearchRequest();
                  lsr.addParameter("IN_PAGES", "PAGE_CD", lsPageCd);
                  VSQuery loQuery = new VSQuery(getParentApp().getSession(), "IN_PAGES", lsr,
                        new SearchRequest());
                  VSRow loRow = loQuery.execute().first();
                  int liSecAction = lsSelectedValue.equals(SITEWIDE_PREFERENCES)?
                        ACTN_CONFIGURE: ACTN_DEPT_PREF;

               /*
                * Check if the User is authorized to do Preferences.
                * Action "Sitewide Preference" should be authorized on Access Control if Department = 'ALL'.
                * Action "Department Level Preference" should be authorized on Access Control if specific Department is used.
                */
                  if (AMSSecurity.actionAuthorized(getUserID(), msRsrcId, liSecAction))
                  {
                    //Raise error when Non Admin User setup preferences for resources
                    //not belonging to Home Department.
                     AMSUser loUser = AMSSecurity.getUser( getUserID() );
                     String lsHomeDeptCd = loUser.getHomeDeptCode();

                     if((!mboolIsAdmin) && (mboolPageTypIsDoc)&&
                           (!AMSStringUtil.strEqual(lsSelectedValue, SITEWIDE_PREFERENCES)) &&
                           (!AMSStringUtil.strEqual(lsSelectedValue, lsHomeDeptCd)))
                     {
                        raiseException("While setting up preferences for specific Departments (non-sitewide), " +
                              "non-Administrator User can setup preferences for resources belonging to User’s " +
                              "Home Department only.", AMSPage.SEVERITY_LEVEL_ERROR);
                        foPageEvent.setNewPage(this);
                        foPageEvent.setCancel(true);
                        return;
                     }

                     // Page type = Document.
                     if(mboolPageTypIsDoc)
                     {
                        navigateToDoc(loSourceRow, loORBSess, lsSelectedValue, true);
                     }
                     else
                     {
                        navigateToPage(loRow, loORBSess, foPageEvent, lsSelectedValue, true);
                     }
                  }
                  else
                  {
                     raiseException("Not authorized to create Preference.", AMSPage.SEVERITY_LEVEL_ERROR);
                     foPageEvent.setNewPage(this);
                     foPageEvent.setCancel(true);
                  }
               }
               catch (Exception e)
               {
                  raiseException("An error was encountered while trying to validating"
                           + " action performed. Please contact your system administrator", AMSPage.SEVERITY_LEVEL_ERROR);
                  raiseException(e.getMessage(), SEVERITY_LEVEL_ERROR);

                  // Add exception log to logger object
                  moAMSLog.error("Unexpected error encountered while processing. ", e);


                  foPageEvent.setNewPage(this);
                  foPageEvent.setCancel(true);
               }
            }
            else
            {
               raiseException("Preference already exist for selected department code: " + lsSelectedValue,
                        AMSPage.SEVERITY_LEVEL_ERROR);
               foPageEvent.setNewPage(this);
               foPageEvent.setCancel(true);
            }
         }
      }
      //Case where User selects "Edit Preference" on Preference Detail grid
      else if ( lsAction.equals("btnNavToDoc"))
      {
         try
         {
            if(T2R_PREF_DATA.getCurrentRow().insertInDb() || T2R_PREF_DATA.getCurrentRow().modified())
            {
               raiseException("Please save data before editing Preference.", AMSPage.SEVERITY_LEVEL_ERROR);
               foPageEvent.setNewPage(this);
               foPageEvent.setCancel(true);
               return;
            }

            String lsGroupId = T2R_PREF_DATA.getCurrentRow().getData("GROUP_ID").getString();
            /*
             * Fetch the entry of this page from APGS to find out page type.
             */
            String lsPageCd = loSourceRow.getData("PAGE_CD").getString();
            SearchRequest lsr = new SearchRequest();
            lsr.addParameter("IN_PAGES", "PAGE_CD", lsPageCd);
            VSQuery loQuery = new VSQuery(getParentApp().getSession(), "IN_PAGES", lsr,
                  new SearchRequest());
            VSRow loRow = loQuery.execute().first();
            int liSecAction = lsGroupId.equals(SITEWIDE_PREFERENCES)?
                  ACTN_CONFIGURE: ACTN_DEPT_PREF;


            if (AMSSecurity.actionAuthorized(getUserID(), msRsrcId, liSecAction))
            {
               //Raise error when Non Admin User setup preferences for resources
               //not belonging to Home Department.
               AMSUser loUser = AMSSecurity.getUser( getUserID() );
                String lsHomeDeptCd = loUser.getHomeDeptCode();

                if((!mboolIsAdmin) && (mboolPageTypIsDoc)&&
                      (!AMSStringUtil.strEqual(lsGroupId, SITEWIDE_PREFERENCES))&&
                      (!AMSStringUtil.strEqual(lsGroupId, lsHomeDeptCd)))
                {
                  raiseException("While setting up preferences for specific Departments (non-sitewide), " +
                        "non-Administrator User can setup preferences for resources belonging to User’s " +
                        "Home Department only.", AMSPage.SEVERITY_LEVEL_ERROR);
                  foPageEvent.setNewPage(this);
                  foPageEvent.setCancel(true);
                  return;
                }

               loORBSess.setProperty(PREF_MODE_PROP, UPDATE_PREF);
               // Page type = Document.
               if(mboolPageTypIsDoc)
               {
                  navigateToDoc(loSourceRow, loORBSess, lsGroupId, false);
               }
               else
               {
                  navigateToPage(loRow, loORBSess, foPageEvent, lsGroupId, false);
               }
            }
            else
            {
               raiseException("Not authorized to edit Preference.", AMSPage.SEVERITY_LEVEL_ERROR);
               foPageEvent.setNewPage(this);
               foPageEvent.setCancel(true);
            }
         }
         catch (Exception e)
         {
            raiseException("An error was encountered while trying to validating"
                     + " action performed. Please contact your system administrator", AMSPage.SEVERITY_LEVEL_ERROR);
            raiseException(e.getMessage(), SEVERITY_LEVEL_ERROR);

             // Add exception log to logger object
             moAMSLog.error("Unexpected error encountered while processing. ", e);

            foPageEvent.setNewPage(this);
            foPageEvent.setCancel(true);
         }
      }
      else if (lsAction.equals( "T2R_PREF_DATACopyLine" ))
      {
         VSResultSet loResultSet = null;

         VSQuery loQuery = new VSQuery(getParentApp().getSession(), "R_PREF_DATA", "PREF_DATA_ID=" +
               T2R_PREF_DATA.getCurrentRow().getData("PREF_DATA_ID").getString(), "");

         // Fix for the download of binary types
         loQuery.setColumnProjectionLevel(DataConst.ALLTYPES);

         try
         {
            loResultSet = loQuery.execute();

            if (loResultSet != null)
            {
               loResultSet.last();

               if (loResultSet.getRowCount() > 0)
               {
                  moPrefData = loResultSet.first().getData("PREF_DATA_BINARY").getBytes();
               }
            }
         }
         finally
         {
            if(loResultSet != null)
            {
               loResultSet.close();
            }
         }
      }/*End else if (lsAction.equals( "T2R_PREF_DATACopyLine" ) )*/
      else if(lsAction.equals("T1R_PREF_DEFdelete"))
      {
        // Adding check for non-admin user, since the non-admin user is not allowed
        // to delete the definition. Deleting the definition deletes all the preferences
        // data records even for those department for which non-admin user is not
        // allowed.
        if ( !mboolIsAdmin )
          {
          raiseException("You are not allowed to delete preference definition.",
                 AMSPage.SEVERITY_LEVEL_ERROR);
             foPageEvent.setNewPage(this);
             foPageEvent.setCancel(true);
             return;
          }
      }/*End else if(lsAction.equals("T1R_PREF_DEFdelete"))*/
   }/*End if(lsAction != null)*/
}
//END_EVENT_pR_PREF_DETL_beforeActionPerformed}}
//{{EVENT_T2R_PREF_DATA_beforeInsert
void T2R_PREF_DATA_beforeInsert(VSRow newRow ,VSOutParam cancel )
{
   //Write Event Code below this line
   newRow.getData("PREF_DATA_ID").setLong(0);
}
//END_EVENT_T2R_PREF_DATA_beforeInsert}}
//{{EVENT_T2R_PREF_DATA_beforeSave
void T2R_PREF_DATA_beforeSave(VSRow row ,VSOutParam cancel )
{
   //Write Event Code below this line
   if(row.insertInDb())
   {
      VSData loData = row.getData("PREF_DATA_ID");
      try
      {
         if ( loData.getLong() == 0)
         {
            loData.setLong(AMSUniqNum.getUniqNum("R_PREF_DATA"));
            row.getData("CREAT_TIMESTAMP").setLong(System.currentTimeMillis());
         }
         //TODO once we have unique index(defid+group_id) we can remove following edit
         checkDupicateDept(row.getData("GROUP_ID").getString(), row.getRowIndex());
      }
      catch (Exception e)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

         raiseException("Error occured during unique number generation. Please contact Adminstrator.",
                  AMSPage.SEVERITY_LEVEL_ERROR);
         cancel.setValue(true);
      }
   }
   else if(row.modified())
   {
      String lsDeptCD = row.getData("GROUP_ID").getString();
      //Raise Error when User Selects Department other that ALL for Reference pages.
      if ((lsDeptCD !=null) && (!mboolPageTypIsDoc) && (!lsDeptCD.equals(SITEWIDE_PREFERENCES)))
      {
         raiseException("Department should be 'ALL' for Reference pages.",
               AMSPage.SEVERITY_LEVEL_ERROR);
         cancel.setValue(true);
         return;
      }
      //TODO once we have unique index(defid+group_id) we can remove following edit
      checkDupicateDept(lsDeptCD, row.getRowIndex());
   }
}
//END_EVENT_T2R_PREF_DATA_beforeSave}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T2R_PREF_DATA.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pR_PREF_DETL_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T2R_PREF_DATA) {
			T2R_PREF_DATA_beforeSave(row ,cancel );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_PREF_DETL_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeInsert( DataSource obj, VSRow newRow ,VSOutParam cancel ){
		Object source = obj;
		if (source == T2R_PREF_DATA) {
			T2R_PREF_DATA_beforeInsert(newRow ,cancel );
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
    * Refresh the Preference Detail page
    *
    * @param foSourceRow VSRow
    * @param foORBSess VSORBSession
    * @param fsDeptCD Dept Code
    * @param fboolCreate indicator whether to update or create
    */
   @Override
   public String generate()
   {
      if ( moDocPage != null )
      {
         String lsHTML = moDocPage.generate();
         getParentApp().getPageExpireAlg().pageNavigatedTo( moDocPage ) ;
         moDocPage = null;
         return lsHTML;
      }

      /*
       * Save and refresh Preference Definition DataSource while navigating back to this page from
       * other pages, e.g., navigating back from Document page after setting preferences.
       */
      if(mboolSavePrefData)
      {

        T1R_PREF_DEF.updateDataSource();
        T1R_PREF_DEF.executeQuery();
        //Reset mboolSavePrefData to false
        mboolSavePrefData = false;
      }
      return super.generate();
   }

   /**
    * Navigate to Document
    *
    * @param foSourceRow VSRow
    * @param foORBSess VSORBSession
    * @param fsDeptCD Dept Code
    * @param fboolCreate indicator whether to update or create
    */
   private void navigateToDoc(VSRow foSourceRow, VSORBSession foORBSess, String fsDeptCD, boolean fboolCreate)
   {
      VSResultSet loDocument = null;

      try
      {
         //Search document with Doc code
         String lsWhereClause = "DOC_CD " +
               AMSSQLUtil.getANSIQuotedStr(foSourceRow.getData("PAGE_CD").getString(),
                     AMSSQLUtil.EQUALS_OPER);

         if ( (!mboolIsAdmin) && (!AMSStringUtil.strEqual(fsDeptCD, SITEWIDE_PREFERENCES)))
         {
            //Search document alongwith Doc Dept code
            lsWhereClause = lsWhereClause + " AND DOC_DEPT_CD "+
            AMSSQLUtil.getANSIQuotedStr(fsDeptCD, AMSSQLUtil.EQUALS_OPER);
         }

         /* Set Session varibles so that XDASQLConnector could use DB
            Optimization hint on selection query for this datasource. */
         PagePrefUtil.turnOnDBHintOnVRS( foORBSess, "DOC_HDR", 1);

         VSQuery loQuery = new VSQuery( getParentApp().getSession(), "DOC_HDR",
              lsWhereClause, "" ) ;
         loDocument = loQuery.execute() ;
         VSRow loDoc = null;

         if ( (loDoc = loDocument.next()) != null)
         {
          foORBSess.setProperty(DEPT_CD_PROP, fsDeptCD );
          moDocPage = AMSHyperlinkActionElement.getDocumentRef(
                     loDoc.getData("DOC_CD").getString(), loDoc.getData("DOC_DEPT_CD").getString(),
                     loDoc.getData("DOC_ID").getString(), loDoc.getData("DOC_VERS_NO").getString(),
                     this, false ) ;

            if ( moDocPage != null )
            {
               if ( fboolCreate )
               {
                  foORBSess.setProperty(PREF_MODE_PROP, CREATE_PREF );
               }
               else
               {
                  foORBSess.setProperty(PREF_MODE_PROP, UPDATE_PREF );
               }
            }
         }
         else
         {
            raiseException("Could not find document for code: " + foSourceRow.getData("PAGE_CD").getString(),
                     AMSPage.SEVERITY_LEVEL_ERROR);
         }

      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

      }
      finally
      {
         if ( loDocument != null )
         {
            loDocument.close();
         }
      }
   }

   /**
    * Navigate to Reference Page
    *
    * @param loRow VSRow
    * @param foORBSess VSORBSession
    * @param foPageEvent PageEvent
    * @param fsDeptCD Dept Code
    * @param fboolCreate indicator whether to update or create
    */
   private void navigateToPage(VSRow loRow, VSORBSession foORBSess,
         PageEvent foPageEvent, String fsDeptCD, boolean fboolCreate)
   {
      VSResultSet loDocument = null;
      try
      {
         foORBSess.setProperty(DEPT_CD_PROP, fsDeptCD );
         foORBSess.setProperty("REF_PAGE_PREF", "true" );

         AMSDynamicTransition loDynTran = new AMSDynamicTransition(
               loRow.getData("DEST_PG").getString(),
               "", loRow.getData("APPL_NM").getString());
         loDynTran.setSourcePage(this);
         foPageEvent.setNewPage(loDynTran.getVSPage(getParentApp(), getSessionId()));
         foPageEvent.setCancel(true);

         if ( fboolCreate )
         {
            foORBSess.setProperty(PREF_MODE_PROP, CREATE_PREF );
         }
         else
         {
            foORBSess.setProperty(PREF_MODE_PROP, UPDATE_PREF );
         }

      }
      catch (Exception e)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

      }
      finally
      {
         if ( loDocument != null )
         {
            loDocument.close();
         }
      }
   }



   /**
    * This method fills Department Combobox for grid.
    * @TODO codetable can be cloned?
    *
    */
   public ElementSpec beforeColumnGeneration(AMSTableElement foTableElement, VSRow foRow, VSData foData, TableColumnElement foTC, int fiOffset)
   {
      ElementSpec loEleSpec = null;

      //Format Creation Timestamp from long value to timestamp foramt before displaying the column on page
      if ( foData != null && AMSStringUtil.strEqual(foData.getName(), "CREAT_TIMESTAMP") && foRow != null)
      {
         VSData loTimeStamp = foRow.getData("CREAT_TIMESTAMP");
         if(loTimeStamp != null)
         {
            //Get the value of the TimeStamp
            long llTimeStampID = loTimeStamp.getLong();
            SimpleDateFormat loDtFormat = new SimpleDateFormat(AMSSQLUtil.DEFAULT_TIMESTAMP_FORMAT) ;
            String lsDate =
               loDtFormat.format(AMSBatchUtil.getDateFromVSDate (new VSDate(new Date(llTimeStampID))));

            char[] laryChars = lsDate.toCharArray();

            SimpleAttributeSet loSet = new SimpleAttributeSet();
            loSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
            loEleSpec = new ElementSpec(loSet, (short)3, laryChars, 0, laryChars.length);
         }
      }
      return loEleSpec;
   }

   /**
    * Return true if user has access to configure, otherwise false
    *
    * @param fsDeptCd Department Code
    */
   //Dead code to be removed in future
   public boolean isEditAllowed(String fsDeptCD)
   {
      try
      {

         //First check if its sitewide then check user's authorization
         if ( SITEWIDE_PREFERENCES.equals(fsDeptCD))
         {
            if ( AMSSecurity.actionAuthorized(getUserID(),msRsrcId,
                  AMSCommonConstants.ACTN_CONFIGURE))
            {
               return true;
            }
            else
            {
               return false;
            }
         }

         //if its department code then check if user has authorization for department
         if ( !AMSSecurity.actionAuthorized(getUserID(), msRsrcId,
                  AMSCommonConstants.ACTN_DEPT_PREF))
         {
            return false;
         }

         AMSUser loUser = AMSSecurity.getUser( getUserID() );

         // Get the user's home department.
         String lsDept = loUser.getHomeDeptCode();
         if ( lsDept.equals(fsDeptCD))
         {
            return true;
         }

      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

      }
      return false;
   }

   /**
    * overriden this method to return true so department combobox can be edited in grid.
    */
   public boolean getEditMode()
   {
      return true;
   }

   /**
    * Raise Error when User Selects Department whose Preference already exist.
    *
    * @param fsDeptCd Department Code
    * @param fiRowIndex Row Index
    */
   private void checkDupicateDept(String fsDeptCD, int fiRowIndex)
   {
      String lsWhereClause = "GROUP_ID='" + AMSSQLUtil.getANSIQuotedStr(fsDeptCD) +"'";
      int liRowIndex = T2R_PREF_DATA.getResultSet().findFirst(lsWhereClause);
      if ( liRowIndex != -1)
      {
         if ( liRowIndex == fiRowIndex )
         {
            liRowIndex = T2R_PREF_DATA.getResultSet().findFirst(lsWhereClause, liRowIndex+1);
         }

         if ( liRowIndex != -1 && liRowIndex != fiRowIndex )
         {
            raiseException("Preference already exist for selected department code: " + fsDeptCD,
                     AMSPage.SEVERITY_LEVEL_ERROR);
         }
      }
   }

   /**
    * Apply the page code to the title so as to identify on which document/page the preferences
    * are applied.
    */
   private void applyPageCode(VSRow foSourceRow)
   {
      AMSTextContentElement loPageCd = (AMSTextContentElement)getElementByName("R_PREF_DETL_Page_Cd");
      loPageCd.setText(foSourceRow.getData("PAGE_CD").getString());
   }

   public Vector getExcludeCopyPasteAttributes()
   {
      VSRow loNewRow = T2R_PREF_DATA.getCurrentRow();

      loNewRow.getData("PREF_DATA_BINARY").setBytes(moPrefData);
      loNewRow.getData("PREF_DATA_BINARY").modified(true);

      return super.getExcludeCopyPasteAttributes();
   }

   //Dead code to be removed in future
   class PrefTableListner extends TableEventAdapter
   {

      public void afterColumnGeneration(TableEvent foTableEvent, VSRow foRow, VSData foData, TableColumnElement foTC)
      {
         if ( (foRow.insertInDb() || foRow.modified()))
         {
            return;
         }

         if ( foData != null && AMSStringUtil.strEqual(foData.getName(), "GROUP_ID"))
         {
            MutableAttributeSet loMAS = (MutableAttributeSet)foTableEvent.columnSpec.getAttributes();
            if ( isEditAllowed(foData.getString()) )
            {
               loMAS.removeAttribute( "disabled" ) ;
               loMAS.addAttribute( Attribute.CLASS, "enabled" ) ;
            }
            else
            {
               loMAS.addAttribute( "disabled", "true" ) ;
               loMAS.addAttribute( Attribute.CLASS, "disabled" ) ;
            } /* end else */
         }
         else if(foTC instanceof TableElement.HyperLinkActionColumn)
         {
            if(!isEditAllowed(foRow.getData("GROUP_ID").getString()))
            {
               //loEleSpec = foTC.getElementSpec(foRow, fiOffset);
               MutableAttributeSet loMAS = (MutableAttributeSet)foTableEvent.columnSpec.getAttributes();
               if (loMAS != null)
               {
                  loMAS.addAttribute("disabled","true");
                  loMAS.removeAttribute(HTML.Attribute.HREF);
                  loMAS.removeAttribute(AMSPage.EVT_ON_CLICK);
               }
            }
         }
      }
   }

   /**
    * Gets the Page Type for the given Page Code from Application Page Registration (IN_PAGES)
    * and sets mboolPageTypIsDoc to true if Page is a Document page, else it is set to false.
    * For Document page sets msRsrcId as Page Code, for reference pages sets msRsrcId
    * to Package.HTMLFileName. msRsrcId will be used later for security checks.
    *
    * @param foSourceRow VSRow
    */
   private void setRsrcIdAndPageTyp(VSRow fsSourceRow)
   {
      String lsPageCd = fsSourceRow.getData("PAGE_CD").getString();

      //Get the Page Type Code for the associated page code.
      int liPageTypCd = AMSPLSUtil.getPageTypCd(lsPageCd, getParentApp().getSession());
      /*
       * If PageType = Document then set msRsrcId = Page Code and mboolPageTypIsDoc to true,
       * Else set msRsrcId = PackageName.HTMLFileName combination and mboolPageTypIsDoc to false
       * msRsrcId is used for checking page security, AMSSecurity uses Appl Resource table.
       */
      if(liPageTypCd == AMSPLSUtil.PAGE_TYP_DOCUMENTS)
      {
         //Page Type = Document
         msRsrcId = lsPageCd;
         mboolPageTypIsDoc = true;
      }
      else
      {
         //Reference page
         msRsrcId = fsSourceRow.getData("APPL_NM").getString() + "." +
               fsSourceRow.getData("PAGE_NM").getString();
         mboolPageTypIsDoc = false;
      }
   }
}