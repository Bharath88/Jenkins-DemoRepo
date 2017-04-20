//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.util.* ;
import java.rmi.RemoteException;
import com.amsinc.gems.adv.vfc.html.* ;
import versata.vls.*;

import javax.swing.text.*;
import javax.swing.text.html.HTML.*;
import javax.swing.text.html.*;
import javax.swing.text.AbstractDocument.* ;
import com.amsinc.gems.adv.common.AMSBatchConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSecurityObject ;
import advantage.AMSBatchUtil;
import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSCommonConstants;


import org.apache.commons.logging.Log;
/*
**  ReportSearchPg*/

//{{FORM_CLASS_DECL
public class ReportSearchPg extends ReportSearchPgBase

//END_FORM_CLASS_DECL}}
implements AMSBatchConstants

{
   // Declarations for instance variables used in the form

     public static final String ATTR_CTLG_ID = "CTLG_ID";
     public static final String ATTR_FOLDER_NM = "FOLDER_NAME";
     public static final String ATTR_REPORT_NM = "REPORT_NAME";
     
     /** This is the logger object */
     private static Log moAMSLog = AMSLogger.getLog( ReportSearchPg.class,
        AMSLogConstants.FUNC_AREA_DFLT ) ;

     // distinct folder names are fetched and stored in a String Array when the page is
     // first created.  A drop down list is created for the searchable folder names
     public boolean mbFolderNamesFetched =false;

   //Initialized to true
   private boolean mboolIsFirstQueryEventForTblQry = true;
   private boolean mboolIsFirstQueryEventForFldrQry = true;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public ReportSearchPg ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T2Report_Completed_User_beforePageNavigation
void T2Report_Completed_User_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   try
   {
      String lsWhereClause =null;
      VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();
      VSRow loCurrRow = getRootDataSource().getCurrentRow();
      String lsNodeId = loCurrRow.getData(ATTR_CTLG_ID).getString();
      String lsNodeLabel = loCurrRow.getData(ATTR_REPORT_NM).getString();


      // cancel navigation if node id not retrieved
      if (lsNodeId ==null)
      {
         //cancel navigation
         cancel.setValue(true);
         return;
      }

      // Report Label on Session
      if (lsNodeLabel !=null)
      {
         loCurrentSession.setProperty(RSAA_CTLG_LBL,lsNodeLabel);
      }

      // set the node id property
      loCurrentSession.setProperty(RSAA_NODE_ID,lsNodeId);

      //set other required session properties
      // cancel navigation if false returned
      if (setSessionProperties(lsNodeId)== false)
      {
         //cancel navigation
         cancel.setValue(true);
         return;
      }

      StringBuffer lsbWhereClause = new StringBuffer(160);
      lsbWhereClause.append("(PNT_CTLG_ID=")
         .append(lsNodeId)
         .append(") AND (ITM_TYP=")
         .append(AMSBatchConstants.REPORT)
         .append(") AND (RUN_STA=")
         .append(AMSBatchConstants.STATUS_COMPLETE)
         .append(")");
      nav.setDevWhere(lsbWhereClause.toString());
   } /* end try */
   catch( Exception loExcp )
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Error occurred during processing:" + loExcp.getMessage(),
            SEVERITY_LEVEL_ERROR ) ;
      //Cancel navigation
      cancel.setValue(true);
      return ;
   }//end catch
}
//END_EVENT_T2Report_Completed_User_beforePageNavigation}}
//{{EVENT_T1RPT_SRCH_TBL_QRY_beforeQuery
void T1RPT_SRCH_TBL_QRY_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
{
   try
   {
      /* Only execute for the first time */
      if (mboolIsFirstQueryEventForTblQry )
      {
         String lsUserID = AMSPLSUtil.getCurrentUserID(this);

         /* A User that has ADMN role or a role that is listed as BatchAdminRoles
            on ADV30Params.ini, would be able to see Report Catalog entries from
            all applications on this page. For rest of the Users, filter by
            Application ID will be applied which means they would only be able
            to see Report Catalog entries for the applications they are authorized to. */
         if( !AMSPLSUtil.isJobAdminRole(lsUserID) )
         {
            SearchRequest     loSrch         = new SearchRequest();
            String            lsAppWhere     = null ;
            StringBuffer      lsbWhereClause = new StringBuffer( 100 ) ;
            VSORBSession      loORBSession   = getParentApp().getSession().getORBSession() ;
            AMSSecurityObject loSecObj       = null ;
            String            lsPrefix       = null ;
            String            lsQueryText    = T1RPT_SRCH_TBL_QRY.getOnScreenQueryText();

            if (lsQueryText != null && lsQueryText.trim().length() > 0 )
            {
               lsPrefix = " AND " ;
            }
            else
            {
               lsPrefix = " " ;
            }


            /* Retrieve the application where clause */
            try
            {
               loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
               //Pass in alias as table name
               lsAppWhere = AMSSecurityObject.getApplicationAuthWhere( loSecObj,
                  true, "a" ) ;
            }
            catch ( RemoteException loRemExp )
            {
               raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
               foResultSet.setValue( foQuery.getNewResultSet() ) ;
               return ;
            }
            lsbWhereClause.append(lsPrefix)
               .append("(")
               .append(lsAppWhere)
               .append(")");

            loSrch.add( lsbWhereClause.toString() ) ;
            foQuery.addFilter( loSrch ) ;

            /* Note that first time mboolIsFirstQueryEventForTblQry is true and
               above block will take care of modifying the query.
               Call setQueryInfo to modify the query of the datasource permanently
               so that for non-first times( attempts after first time)
               the modified query is used instead of framing it on every beforeQuery.
               Side Note: For the first time setQueryInfo is too late to modify the
               query and hence query.addFilter method is used for first time. */
            T1RPT_SRCH_TBL_QRY.setQueryInfo( T1RPT_SRCH_TBL_QRY.getMetaQueryName(),
               lsbWhereClause.toString(), AMSCommonConstants.EMPTY_STR,
               AMSCommonConstants.EMPTY_STR ) ;
         }//not isJobAdminRole

         /* Reset so that it this block is not executed next time */
         mboolIsFirstQueryEventForTblQry = false;
      }//end if mboolIsFirstQueryEventForTblQry
   }//end try
   catch( Exception loExcp )
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Error occurred during processing:" + loExcp.getMessage(),
         AMSPage.SEVERITY_LEVEL_ERROR ) ;
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }//end catch
}
//END_EVENT_T1RPT_SRCH_TBL_QRY_beforeQuery}}
//{{EVENT_T3RPT_SRCH_FLDR_QRY_beforeQuery
void T3RPT_SRCH_FLDR_QRY_beforeQuery( VSQuery foQuery ,VSOutParam foResultSet )
{
   try
   {
      /* Only execute for the first time */
      if (mboolIsFirstQueryEventForFldrQry )
      {
         String lsUserID = AMSPLSUtil.getCurrentUserID(this);

         /* A User that has ADMN role or a role that is listed as BatchAdminRoles
            on ADV30Params.ini, would be able to see Report Catalog entries from
            all applications on the Folder dropdown box. For rest of the Users,
            filter by Application ID will be applied which means they would only
            be able to see Report Catalog entries for the applications they are
            authorized to. */
         if( !AMSPLSUtil.isJobAdminRole(lsUserID) )
         {
            SearchRequest     loSrch         = new SearchRequest();
            String            lsAppWhere     = null ;
            StringBuffer      lsbWhereClause = new StringBuffer( 100 ) ;
            VSORBSession      loORBSession   = getParentApp().getSession().getORBSession() ;
            AMSSecurityObject loSecObj       = null ;
            String            lsPrefix       = null;
            String            lsQueryText    = T3RPT_SRCH_FLDR_QRY.getOnScreenQueryText();

            if (lsQueryText != null && lsQueryText.trim().length() > 0 )
            {
               lsPrefix = " AND " ;
            }
            else
            {
               lsPrefix = " " ;
            }
            /* Retrieve the application where clause */
            try
            {
               loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
               //Pass in alias as table name
               lsAppWhere = AMSSecurityObject.getApplicationAuthWhere( loSecObj,
                  true, "a" ) ;
            }
            catch ( RemoteException loRemExp )
            {
               raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
               foResultSet.setValue( foQuery.getNewResultSet() ) ;
               return ;
            }
            lsbWhereClause.append(lsPrefix)
               .append("(")
               .append(lsAppWhere)
               .append(")");

            loSrch.add( lsbWhereClause.toString() ) ;
            foQuery.addFilter( loSrch ) ;
            /* Note that first time mboolIsFirstQueryEventForFldrQry is true and
               above block will take care of modifying the query.
               Call setQueryInfo to modify the query of the datasource permanently
               so that for non-first times( attempts after first time)
               the modified query is used instead of framing it on every beforeQuery.
               Side Note: For the first time setQueryInfo is too late to modify the
               query and hence query.addFilter method is used for first time. */
            T3RPT_SRCH_FLDR_QRY.setQueryInfo( T3RPT_SRCH_FLDR_QRY.getMetaQueryName(),
               lsbWhereClause.toString(), AMSCommonConstants.EMPTY_STR,
               AMSCommonConstants.EMPTY_STR ) ;
         }//not isJobAdminRole

         /* Reset so that it this block is not executed next time */
         mboolIsFirstQueryEventForFldrQry = false;
      }//end if mboolIsFirstQueryEventForFldrQry
   }//end try
   catch( Exception loExcp )
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      raiseException( "Error occurred during processing:" + loExcp.getMessage(),
         AMSPage.SEVERITY_LEVEL_ERROR ) ;
      foResultSet.setValue( foQuery.getNewResultSet() ) ;
      return ;
   }//end catch
}
//END_EVENT_T3RPT_SRCH_FLDR_QRY_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T2Report_Completed_User.addPageNavigationListener(this);
	T1RPT_SRCH_TBL_QRY.addDBListener(this);
	T3RPT_SRCH_FLDR_QRY.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T2Report_Completed_User) {
			T2Report_Completed_User_beforePageNavigation( obj, cancel, newPage );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1RPT_SRCH_TBL_QRY) {
			T1RPT_SRCH_TBL_QRY_beforeQuery(query , resultset );
		}
	
		if (source == T3RPT_SRCH_FLDR_QRY) {
			T3RPT_SRCH_FLDR_QRY_beforeQuery(query , resultset );
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

    /*
     * For debugging
     */
    private void db(String fsMsg)
    {
       if( AMS_PLS_DEBUG )
       {
         System.err.println(fsMsg);
       }
    }

     /** method sets session level properties before navigating to the
      *  report summary page
      *  @param fsNodeId  The catalog node ID
      *  @return if the Session peroperties were set
      */
     public boolean setSessionProperties(String fsNodeId)
     {
         VSQuery     loQuery =null;
         VSResultSet loResultSet =null;
         VSRow       loRow =null;
         SearchRequest loCtlgSr = new SearchRequest();

         try
         {
            loCtlgSr.add(ATTR_CTLG_ID +"=" + fsNodeId);
            loQuery = new VSQuery( getParentApp().getSession(),
                                   "R_BS_CATALOG", loCtlgSr, null) ;
            loResultSet = loQuery.execute() ;
            loRow = loResultSet.first() ;

            while ( loRow != null )
            {
               getParentApp().getSession().getORBSession().setProperty(
               RSAA_AGNT_CLS_NM,
               loRow.getData("CLS_NM").getString());

               getParentApp().getSession().getORBSession().setProperty(
               RSAA_PKG_NM,
               loRow.getData("PKG_NM").getString());


               // This property always indicates a report (may be extended later to specify type
               // of report
               getParentApp().getSession().getORBSession().setProperty(
               RSAA_ITM_TYP,REPORT_CLASS);

               loRow = loResultSet.next() ;
            } /* end while( loRow != null ) */

            loResultSet.close() ;
         }
         catch ( Exception loEx )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loEx);

            if (loResultSet !=null)
            {
               loResultSet.close() ;
            }
         return false;
         } /* end try - catch */
      return true;
     }

    /** method used to insert values for the folder list names
     *  the first time the current page is generated
     */
    public String generate()
    {
       // setup the folder name drop down list initially
       if (mbFolderNamesFetched ==false)
       {
          VSResultSet loResultSet =null ;
          VSRow       loRow =null;

           // initialize combobox element
          ComboBoxElement loComboBox = (ComboBoxElement)getElementByName("showFolderCombo");
          if ( loComboBox != null )
          {
            loComboBox.removeAllElements() ;
          }

          try
          {
             T3RPT_SRCH_FLDR_QRY.executeQuery();
             loResultSet = T3RPT_SRCH_FLDR_QRY.getResultSet() ;
             Option  loSelectedOption = loComboBox.addElement( "", "" );
             loComboBox.setSelectedItem(loSelectedOption);
             loRow = loResultSet.first() ;

             while ( loRow != null )
             {
                String  lsFolderName  = loRow.getData(ATTR_FOLDER_NM).getString() ;
                loComboBox.addElement( lsFolderName, lsFolderName ) ;
                loRow = loResultSet.next() ;
             } /* end while( loRow != null ) */
             // set the member variable value to indicate the initial creation
             // of the folder name search drop down list
             mbFolderNamesFetched =true ;
           } // end try
           catch ( Exception loEx )
           {
              // Add exception log to logger object
              moAMSLog.error("Unexpected error encountered while processing. ", loEx);

           } /* end try - catch */
       } //if (mbFolderNamesFetched ==false)
       return super.generate();
    } /* end method generate() */

}
