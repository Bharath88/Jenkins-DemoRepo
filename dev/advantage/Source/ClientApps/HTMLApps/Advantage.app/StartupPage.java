//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.client.dbitem.*;
import com.amsinc.gems.adv.common.*;
import org.apache.commons.logging.*;
import java.util.*;
import java.rmi.RemoteException;

import advantage.AMSStringUtil;


/*
**  StartupPage
*/

//{{FORM_CLASS_DECL
public class StartupPage extends StartupPageBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public StartupPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }




//{{EVENT_CODE
//{{EVENT_StartupPage_beforeActionPerformed
void StartupPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line
   if( ae.getName().equals("T1HomePage") )
   {
      VSSession loVSSession = getParentApp().getSession() ;
      String    lsUserID = AMSHomePageMethods.getUserID( loVSSession ) ;
      int liNumWins = 0;
   
      if ( AMSHomePageMethods.useDfltWindows( loVSSession, lsUserID ) == true )
      {
         lsUserID = null ;
      } /* if ( AMSHomePageMethods.useDfltWindows( loVSSession, lsUserID ) == true ) */
   
      VSResultSet loVSResultSet = AMSHomePageMethods.getResultSet( loVSSession, lsUserID ) ;
      
      loVSResultSet.last() ;
      liNumWins = loVSResultSet.getRowCount() ;
      
      /* Here we loop through all the homepages until we find an internal page.
       * If we find one, we set the action to a dynamic transition that 
       * navigates to that page.  While new development prevents any other
       * homepage to be set if an internal homepage is set, this loop allows
       * for user accounts set with multiple homepages before the new
       * development was imlemented to still function under the new rules.
       */
      for ( int liWinCtr = 1 ; liWinCtr <= liNumWins ; liWinCtr++ )
      {
         VSRow loVSRow = loVSResultSet.getRowAt( liWinCtr ) ;
         
         String lsDestination = AMSHomePageMethods.getURL( loVSRow ) ;
         // lsDestination = Webi/R3901 Financial Reports/Budget Vs Actual/FIN-BA-1001-Revenues vs Budget
         
         if(loVSRow.getData("WIN_NM").getString().equals(AMSPage.INFO_REPORT_PG_TITLE))
         {              
            AMSDynamicTransition loHomePageDynTrans = showInfoReport(lsDestination);
            if (loHomePageDynTrans==null)
            {
               return;
            }
            ((AMSHyperlinkActionElement)ae).setDynamicTransition(loHomePageDynTrans);
            
            return;            
         }
         else if ( ( lsDestination == null ) || ( lsDestination.length() == 0 ) )
         {
            String lsPageCode = AMSHomePageMethods.getPageCode( loVSRow ) ;
            
            AMSDynamicTransition loHomePageDynTrans = new AMSDynamicTransition();
            loHomePageDynTrans.setPageCode( lsPageCode );
            ((AMSHyperlinkActionElement)ae).setDynamicTransition(loHomePageDynTrans);
            
            return;
         }
      }
      
      // reset the action element to default
      ((AMSHyperlinkActionElement)ae).setDynamicTransition(null);
   }
}
//END_EVENT_StartupPage_beforeActionPerformed}}

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
			StartupPage_beforeActionPerformed( ae, evt, preq );
		}
	}
//END_EVENT_ADAPTER_CODE}}

	public HTMLDocumentSpec getDocumentSpecification() 
	{
	      return getDefaultDocumentSpecification();
	}

	public String getFileName() 
	{
	    return getDefaultFileName();
	}

	public String getFileLocation() 
	{
	    return getPageTemplatePath();
	}

	public void afterPageInitialize() 
	{
	   super.afterPageInitialize();
	   //Write code here for initializing your own control
	   //or creating new control.
	   setDefaultActionName("OpenPageWithData");
	}
   
	/**
	 * This methods dynamically navigates to infoAdvantage Report.
	 * @param fsDestination : infoAdvantage Report Path 
	 * @return
	 */
	private AMSDynamicTransition showInfoReport(String fsDestination)
	{
	   String lsInfoReportName = fsDestination.substring( fsDestination.lastIndexOf( "/" ) + 1);
	   // lsInfoReportName = FIN-BA-1001-Revenues vs Budget    
   
	   String lsInfoReportType = fsDestination.substring( 0, fsDestination.indexOf( "/" ) );
	   // lsInfoReportType = Webi
   
	   String lsTempInfoReportPath = fsDestination.substring( fsDestination.indexOf( "/" ) + 1 , fsDestination.lastIndexOf( "/" ));
	   // lsTempInfoReportPath = R3901 Financial Reports/Budget Vs Actual
   
	   String lsInfoReportPath =  "[" + lsTempInfoReportPath.replace( "/", "],[" ) + "]";
	   // lsInfoReportPath = [R3901 Financial Reports],[Budget Vs Actual]
   
	   AMSDynamicTransition loHomePageDynTrans =new AMSDynamicTransition(); 
   
	   if ( lsInfoReportName == null || AMSStringUtil.strIsEmpty( lsInfoReportName ))
	   {
	      raiseException("infoAdvantage Report Name is Empty",SEVERITY_LEVEL_ERROR);
	      return loHomePageDynTrans;
	   }
   
	   if ( lsInfoReportPath == null || AMSStringUtil.strIsEmpty( lsInfoReportPath ))
	   {
	      raiseException("infoAdvantage Report Path is Empty",SEVERITY_LEVEL_ERROR);               
	      return loHomePageDynTrans;
	   }
   
	   if ( lsInfoReportType == null || AMSStringUtil.strIsEmpty( lsInfoReportType ))
	   {
	      raiseException("infoAdvantage Report Type is Empty",SEVERITY_LEVEL_ERROR);
	      return loHomePageDynTrans;
	   }           
   
	   // set infoAdvantage Report Name, Report Type , Report Path 
	   setInfoReportName( lsInfoReportName );
	   setInfoReportType( lsInfoReportType );            
	   setInfoReportPath( lsInfoReportPath );

	   // Dynamically redirecting to the infoAdvantage Report Page
              
	   loHomePageDynTrans.setSourcePage(this);
	   loHomePageDynTrans.setApplName("Reports_Sys_Admin_App");
	   loHomePageDynTrans.setDestination("pEmbeddedBOReport");
	   loHomePageDynTrans.setTargetFrame("Display");
	   return loHomePageDynTrans;
	}

} /* end class StartupPage */
