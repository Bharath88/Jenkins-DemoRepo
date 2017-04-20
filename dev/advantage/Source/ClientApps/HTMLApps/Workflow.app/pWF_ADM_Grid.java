//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

	import java.util.* ;
	import com.amsinc.gems.adv.workflow.* ;
	import com.amsinc.gems.adv.common.* ;
	import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;

	/*
	**  pWF_ADM_Grid*/

	//{{FORM_CLASS_DECL
	public class pWF_ADM_Grid extends pWF_ADM_GridBase
	
	//END_FORM_CLASS_DECL}}
	implements AMSWorkflowActionHandlerPage
	{
          private static Log moAMSLog = AMSLogger.getLog( 
             pWF_ADM_Grid.class,
             AMSLogConstants.FUNC_AREA_WORKFLOW ) ;

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pWF_ADM_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
	    * Implementation method for AMSWorkflowActionHandlerPage interface.
	    * This method will never be called on this page, so just return false.
	    */
	   public boolean assignWorklistTask( byte fbReassign )
	   {
	      return false ;
	   } /* end assignWorklistTask() */

	   /**
	    * Implementation method for AMSWorkflowActionHandlerPage interface.
	    * This method will never be called on this page, so raise an error
	    * and return the current page.
	    */
	   public String trackWorkInProgress()
	   {
	      raiseException( "Track work in progress is not supported from this page",
	            SEVERITY_LEVEL_ERROR ) ;
	      return this.generate() ;
	   } /* end trackWorkInProgress() */

	   /**
	    * This method will transition the user to the pWF_USER_QRY_Grid page
	    * where a new user may be selected. Called from worklistReassign()
	    * method of AMSHyperlinkActionElement.
	    *
	    * @param fiCase WL_REASSIGN Action as an integer.
	    * @return String The generated HTML page
	    */
	   public String reassignWorklist( int fiCase ) throws AMSSecurityException
	   {
	      VSRow  loRow     = T1WF_WRK_ADM_QRY.getCurrentRow() ;
	      long   llAprvLvl = 0 ;
	      String lsDocCd   = null ;

	      if ( loRow == null )
	      {
	         raiseException( "A row must be selected to perform this action",
	               SEVERITY_LEVEL_ERROR ) ;
	         return generate() ;
	      } /* end if ( loRow == null ) */
	      llAprvLvl = loRow.getData( AMSHyperlinkActionElement.APRV_LVL ).getLong() ;
	      lsDocCd = loRow.getData( ATTR_DOC_CD ).getString() ;

	      if ( lsDocCd != null )
	      {
	         // Reassign action authorization on document code.
	         if( !AMSSecurity.actionAuthorized( loRow.getSession().getLogin(), lsDocCd,
	               DOC_ACTN_REASSIGN ) )
	         {
	            raiseException("Not authorized to perform selected action.",
	                  SEVERITY_LEVEL_ERROR );
	            return null;
	         }
	         StringBuffer lsbRoleWhere = new StringBuffer( 100 ) ;
	         Enumeration  leValidRoles = null ;

	         /*
	          * First, get the list of valid users for reassignment
	          * Must convert approval level int to long
	          */
	         try
	         {
	            leValidRoles = AMSSecurity.getApprovedRoles( lsDocCd, llAprvLvl ) ;
	         } /* end try */
	         catch ( AMSSecurityException foExp )
	         {
                // Add exception log to logger object
                moAMSLog.error("Unexpected error encountered while processing. ", foExp);

	            raiseException( foExp.getMessage(), SEVERITY_LEVEL_ERROR ) ;
	            return generate() ;
	         } /* end catch ( AMSSecurityException foExp ) */

	         /* Create where clause from returned vector of approved users */
	         if ( leValidRoles != null )
	         {
	            boolean lboolFirst = true ;

	            while ( leValidRoles.hasMoreElements() )
	            {
	               /* For the first user name, do not prefix with OR */
	               if ( !lboolFirst )
	               {
	                  lsbRoleWhere.append( " OR " ) ;
	               } /* end if ( liCtr > 0) */

	               lsbRoleWhere.append( "R_SC_USER_ROLE_LNK.SEC_ROLE_ID='" ) ;
	               lsbRoleWhere.append( AMSSQLUtil.getANSIQuotedStr ( (String)leValidRoles.nextElement() ) ) ;
	               lsbRoleWhere.append( "'" ) ;
	               lboolFirst = false ;
	            } /* end while ( leValidRoles.hasMoreElements() ) */
	         } /* end if (leValidRoles != null) */

	         try
	         {
	            AMSDynamicTransition     loDynTran  = new AMSDynamicTransition() ;
	            AMSWorkflowUserQueryPage loUserPage = null ;

	            loDynTran.setSourcePage( this ) ;
	            loDynTran.setApplName( AMSWorklistPage.WORKFLOW_APP_NM ) ;
	            loDynTran.setDestination( "pWF_USER_QRY_Grid" ) ;
	            loDynTran.setTargetFrame( "_self" ) ;
	            loDynTran.setWhereClause( lsbRoleWhere.toString() ) ;
	            loUserPage = (AMSWorkflowUserQueryPage)loDynTran.getVSPage(getParentApp(), getSessionId()) ;
	            loUserPage.setCase( fiCase ) ;
	            return loUserPage.generate() ;
	         } /* end try */
	         catch ( VSException foExp )
	         {
                // Add exception log to logger object
                moAMSLog.error("Unexpected error encountered while processing. ", foExp);

	            raiseException( "Unable to select user.  Error opening user selection page.",
	                  SEVERITY_LEVEL_ERROR ) ;
	            return generate() ;
	         } /* end try/catch */

	      } /* end if ( lsDocCd != null ) */
	      else
	      {
	         raiseException( "A row must be selected to perform this action",
	               SEVERITY_LEVEL_ERROR ) ;
	         return generate() ;
	      } /* end else */
	   } /* end reassignWorklist() */

	}