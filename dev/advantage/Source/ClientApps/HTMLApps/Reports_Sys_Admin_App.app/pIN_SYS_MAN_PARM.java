//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
   import java.util.*;
   import java.text.*;
   import advantage.AMSStringUtil;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
   import com.amsinc.gems.adv.common.AMSSQLUtil;
   import java.rmi.RemoteException;
   import com.amsinc.gems.adv.common.*;
   import versata.vls.*;
import org.apache.commons.logging.Log;


   /*
   **  pIN_SYS_MAN_PARM*/

//{{FORM_CLASS_DECL
public class pIN_SYS_MAN_PARM extends pIN_SYS_MAN_PARMBase

//END_FORM_CLASS_DECL}}
   {
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pIN_SYS_MAN_PARM.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   
           // Declarations for instance variables used in the form
         String msJobId ;
         boolean mboolFirstTime = true ;


           // This is the constructor for the generated form. This also constructs
           // all the controls on the form. Do not alter this code. To customize paint
           // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pIN_SYS_MAN_PARM ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
           }



	//{{EVENT_CODE
	//{{EVENT_pIN_SYS_MAN_PARM_afterActionPerformed
void pIN_SYS_MAN_PARM_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
   if(ae.getAction() == "ok")
   {
         //Get the Value of the User ID if entered.
         String lsNewUserId = preq.getParameter("txtT1USER_ID");
         //If userid entered is NOT an empty string
         if(!AMSStringUtil.strIsEmpty(lsNewUserId))
         {
               isUserIDValid(lsNewUserId);
         }

         String lsActionCd = preq.getParameter("txtT1ACTN_CD");

         String lsOvrrdLvl = preq.getParameter("txtT1OVERRIDE_LVL");

         String lsApplyOvrrd = preq.getParameter("txtT1APPLY_OVERRIDES");

         //If action code is not empty string
         if(!AMSStringUtil.strIsEmpty(lsActionCd) )
         {
               //If action code is doc import
               if(((new Integer(lsActionCd)).intValue()) == AMSCommonConstants.DOC_ACTN_IMPORT)
               {
                     if(!AMSStringUtil.strIsEmpty(lsOvrrdLvl))
                     {
                              // "0" is from the CVL_OVERRIDE_LVL.
                              if(!lsOvrrdLvl.equals("0") && AMSStringUtil.strIsEmpty(lsApplyOvrrd))
                              {
                                 //If the Apply Overrides parameter is "False" and an Override Level
                                 //parameter has been specified an error will be issued.
                           raiseException("Invalid Input on Attribute OVERRIDE_LEVEL",SEVERITY_LEVEL_ERROR);
                              }
                     }
               }//If action code is other than doc import
               else
               {
                     // if override level is provided - "0" is from the CVL_OVERRIDE_LVL.
                     if(!AMSStringUtil.strIsEmpty(lsOvrrdLvl) && !lsOvrrdLvl.equals("0"))
                     {
                     raiseException("Override Level parameter cannot be specified for any action other than DOCIMPORT. Override Level ignored.",SEVERITY_LEVEL_WARNING);
                     }
               }
         }//if(!AMSStringUtil.strIsEmpty(lsActionCd) )

   }//if(ae.getAction() == "ok")
}
//END_EVENT_pIN_SYS_MAN_PARM_afterActionPerformed}}
//{{EVENT_T2R_SC_DOC_CUST_ACTN_BeforePickQuery
void T2R_SC_DOC_CUST_ACTN_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
   //Write Event Code below this line
     whereClause.setValue(
      "( DOC_S_ACTN_CD NOT IN ("
         + AMSDocAppConstants.DOC_SUB_ACTN_ITI_CPYFWD_TO_ITA +
            "," +AMSDocAppConstants.DOC_SUB_ACTN_GAP_CPYFWD_TO_GAX +
            "," +AMSDocAppConstants.DOC_SUB_ACTN_GAP_CPYFWD_TO_GAE +
      "," +AMSDocAppConstants.DOC_SUB_ACTN_GAE_CPYFWD_TO_GAX +
      "," +AMSDocAppConstants.DOC_SUB_ACTN_TI_CPYFWD_TO_TR + "))"
          );
}
//END_EVENT_T2R_SC_DOC_CUST_ACTN_BeforePickQuery}}
//{{EVENT_pIN_SYS_MAN_PARM_beforeGenerate
void pIN_SYS_MAN_PARM_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
	//Write Event Code below this line
	TextContentElement moTitle =
	      (TextContentElement) getElementByName("CleanupPeriod");
	if(AMSStringUtil.strEqual(AMSParams.msPrimaryApplication,
              Integer.toString(FIN_APPL)))
	{
	   moTitle.setValue("Fiscal Year: ");
	}
	else
	{
	   moTitle.setValue("Lag Days: ");
	}
}
//END_EVENT_pIN_SYS_MAN_PARM_beforeGenerate}}
//{{EVENT_T3R_WF_USER_ROLE_BeforePickQuery
void T3R_WF_USER_ROLE_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
   //Write Event Code below this line
   VSRow loSource = dataSource.getCurrentRow();

   // Filter the User Roles based on the Approver Id entered
   String lsApprvId = loSource .getData("APRV_ID").getString();
   StringBuffer lsWhereClause = new StringBuffer (200);

   // Add filter only if the Approver Id is entered
   if (!AMSStringUtil.strIsEmpty(lsApprvId))
   {
      lsWhereClause.append("USID = "+AMSSQLUtil.getANSIQuotedStr(lsApprvId,true));
      whereClause.setValue(lsWhereClause.toString());
   }// end if (!AMSStringUtil.strIsEmpty(lsApprvId))
}
//END_EVENT_T3R_WF_USER_ROLE_BeforePickQuery}}

	//END_EVENT_CODE}}

           public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	T2R_SC_DOC_CUST_ACTN.addPickListener(this);
	T3R_WF_USER_ROLE.addPickListener(this);
	//END_EVENT_ADD_LISTENERS}}
           }

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pIN_SYS_MAN_PARM_beforeGenerate(docModel, cancel, output);
		}
	}
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIN_SYS_MAN_PARM_afterActionPerformed( ae, preq );
		}
	}
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T2R_SC_DOC_CUST_ACTN) {
			T2R_SC_DOC_CUST_ACTN_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	
		if (source == T3R_WF_USER_ROLE) {
			T3R_WF_USER_ROLE_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
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
     * This helper method validate UserId against R_SC_USER_INFO Table
     * @param fsUserId -- UserId which needs to be validated
     * @return none
     */
      private void isUserIDValid(String fsUserId)
      {
            if ( AMSSecurity.getUser( fsUserId ) == null )
         {
            raiseException( "Invalid Input On Attribute USER_ID",SEVERITY_LEVEL_ERROR ) ;
         }
      }

        /**
         * Overrides method from VSPage
         */
        public String generate()
        {
           try
           {
              DataSource loDataSource ;
              VSRow      loRow ;
              VSSession  loSession ;

              if (getHighestSeverityLevel() > SEVERITY_LEVEL_WARNING)
              {
                 return super.generate();
              }

              //get job id
              loSession = getParentApp().getSession() ;
              msJobId   = loSession.getORBSession().getProperty( "RSAA_JOBID" ) ;

              // job id must be available
              if ( msJobId.equals( null ) )
              {
                 raiseException( "Job Id not found", SEVERITY_LEVEL_ERROR ) ;
                 return null ;
              }

              // get root data job source the first time
              loDataSource = this.getRootDataSource();
              if ( mboolFirstTime )
              {
                 T1IN_SYS_MAN_PARM.setQueryInfo( "IN_SYS_MAN_PARM",
                                              " ( AGNT_ID = " + msJobId + " ) " ,
                                              "",
                                              "",
                                              false ) ;
                 loDataSource.executeQuery() ;
                 // add row if no match found
                 if ( loDataSource.getCurrentRow() == null )
                 {
                    loDataSource.insert();
                    loRow = loDataSource.getCurrentRow();
                    loRow.getData( "AGNT_ID" ).setString( msJobId ) ;
                 }
                 mboolFirstTime = false ;
              } /* End if ( mboolFirstTime ) */
              // return page
              return super.generate() ;
         } /* end try */
         catch( RemoteException foExp )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

            raiseException( "Unable to get message properties",
                  SEVERITY_LEVEL_SEVERE ) ;
            return super.generate() ;
         } /* end catch( RemoteException foExp ) */
      } // of generate
   }
