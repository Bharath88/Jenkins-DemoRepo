//{{IMPORT_STMTS
package advantage.GenMRT;
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
import com.amsinc.gems.adv.common.* ;

/*
**  pR_FS_MRT_BUILD
*/

//{{FORM_CLASS_DECL
public class pR_FS_MRT_BUILD extends pR_FS_MRT_BUILDBase

//END_FORM_CLASS_DECL}}
{
   private static final String GEN_MRT_AUTH_FAIL = "Not authorized to generate MRT file" ;
   private static final String GEN_MRT_NOT_ADMIN
         = "Administrator role not used for MRT file generation. Ensure proper security setup." ;
   private String msMRTFileName = null ;
   private String msMRTFileLoc  = null ;
   private AMSTextFieldElement moMRTFileLoc  = null ;
   private AMSTextFieldElement moMRTFileName = null ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pR_FS_MRT_BUILD ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_pR_FS_MRT_BUILD_beforeActionPerformed
   void pR_FS_MRT_BUILD_beforeActionPerformed( ActionElement foElem, PageEvent foEvent, PLSRequest foPLSReq )
   {
      if ( foElem.getAction().equals( "genmrtprod" ) )
      {
         generateMRT( false ) ;
      } /* end if ( foElem.getAction().equals( "genmrtprod" ) ) */
      else if ( foElem.getAction().equals( "genmrtdebug" ) )
      {
         generateMRT( true ) ;
      } /* end else if ( foElem.getAction().equals( "genmrtdebug" ) ) */
   }
//END_EVENT_pR_FS_MRT_BUILD_beforeActionPerformed}}
//{{EVENT_pR_FS_MRT_BUILD_requestReceived
   void pR_FS_MRT_BUILD_requestReceived( PLSRequest foPLSReq, PageEvent foEvent )
   {
      msMRTFileName = foPLSReq.getParameter( "txtGenMRTFileName" ) ;
      if ( msMRTFileName != null )
      {
         msMRTFileName = msMRTFileName.trim() ;
      } /* end if ( msMRTFileName != null ) */
      else
      {
         msMRTFileName = "" ;
      } /* end else */
      msMRTFileLoc = foPLSReq.getParameter( "txtGenMRTFileLoc" ) ;
      if ( msMRTFileLoc != null )
      {
         msMRTFileLoc = msMRTFileLoc.trim() ;
      } /* end if ( msMRTFileName != null ) */
      else
      {
         msMRTFileLoc = "" ;
      } /* end else */
   }
//END_EVENT_pR_FS_MRT_BUILD_requestReceived}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pR_FS_MRT_BUILD_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pR_FS_MRT_BUILD_beforeActionPerformed( ae, evt, preq );
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
    * Implemented to set the values of the unbound MRT file
    * location text boxes.
    *
    * Modification Log : Mark Shipley - 01/03/2002
    *                                 - Initial version
    */
   public void beforeGenerate()
   {
      if ( moMRTFileName == null )
      {
         moMRTFileName = (AMSTextFieldElement)getElementByName( "txtGenMRTFileName" ) ;
         msMRTFileName = AMSParams.msMRTFileName ;
         moMRTFileLoc  = (AMSTextFieldElement)getElementByName( "txtGenMRTFileLoc" ) ;
         msMRTFileLoc  = AMSParams.msMRTFileLocation ;

         ((AMSTextFieldElement)getElementByName( "txtMRTFileLoc" )).setValue(
               AMSParams.msMRTFileLocation ) ;
         ((AMSTextFieldElement)getElementByName( "txtMRTFileName" )).setValue(
               AMSParams.msMRTFileName ) ;
      } /* end if ( moMRTFileName == null ) */

      moMRTFileName.setValue( msMRTFileName ) ;
      moMRTFileLoc.setValue( msMRTFileLoc ) ;

      return ;
   } /* end beforeGenerate() */

   /**
    * Creates a new GenerateMRT object to build a new
    * MRT file.
    *
    * Modification Log : Mark Shipley - 01/03/2002
    *                                 - Initial version
    *
    * @param fboolForDebug Indicates if the MRT file will be generated in debug mode
    */
   private void generateMRT( boolean fboolForDebug )
   {
      roleCheck() ;
      if ( ( msMRTFileName == null ) || ( msMRTFileName.length() <= 0 ) )
      {
         raiseException( "MRT Generation File Name not specified.",
               SEVERITY_LEVEL_ERROR ) ;
      } /* end if ( ( msMRTFileName == null ) || ( msMRTFileName.length() <= 0 ) ) */
      else if ( ( msMRTFileLoc == null ) || ( msMRTFileLoc.length() <= 0 ) )
      {
         raiseException( "MRT Generation File Location not specified.",
               SEVERITY_LEVEL_ERROR ) ;
      } /* end if ( ( msMRTFileLoc == null ) || ( msMRTFileLoc.length() <= 0 ) ) */
      else
      {
         GenerateMRT loGenMRT = new GenerateMRT( getParentApp().getSession(), this ) ;

         loGenMRT.generate( msMRTFileLoc, msMRTFileName, fboolForDebug ) ;
      } /* end else */
   } /* end generateMRT() */

   /**
    * This method checks if the user has admin rights or belongs
    * to the security role specified in the .ini file that allows
    * MRT file generation. Only such users are allowed to generate
    * the MRT file.  If the user is an administrator, then generation
    * will proceed with no warnings or errors.  If the user is not
    * an administrator but belongs to the MRT generation security
    * role, then generation will proceed, but a warning will be issued
    * that the user does not have administrator rights.  Otherwise,
    * a severe exception is raised, and generation is cancelled.
    */
   private void roleCheck()
   {
      VSORBSession      loORBSession = getParentApp().getSession().getORBSession() ;
      VSMapSecurityInfo loSecInfo = null ;
      AMSUser           loAMSUser ;

      try
      {
         loSecInfo = (VSMapSecurityInfo) loORBSession.getServerSecurityObject() ;
      }
      catch ( RemoteException loRemExp )
      {
         raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         return ;
      }

      loAMSUser = AMSSecurity.getUser( loSecInfo.getLogin() ) ;
      if ( loAMSUser != null )
      {
         Vector lvSecGroups ;

         lvSecGroups = loAMSUser.getSecurityGroups() ;
         if ( lvSecGroups != null )
         {
            if ( lvSecGroups.contains( AMSSecurity.ADMIN_SEC_ROLE ) )
            {
               /* The user is an administrator, so return with no errors */
               return ;
            } /* end if ( lvSecGroups.contains( AMSSecurity.ADMIN_SEC_ROLE ) ) */
            else
            {
               if ( ( AMSParams.msMRTGenerationRole != null ) &&
                    ( AMSParams.msMRTGenerationRole.length() > 0 ) )
               {
                  if ( lvSecGroups.contains( AMSParams.msMRTGenerationRole ) )
                  {
                     raiseException( GEN_MRT_NOT_ADMIN, SEVERITY_LEVEL_WARNING ) ;
                     return ;
                  } /* end if ( lvSecGroups.contains( AMSParams.msMRTGenerationRole ) ) */
               } /* end if ( ( AMSParams.msMRTGenerationRole != null ) && . . . */
            } /* end else */
         } /* end if ( lvSecGroups != null ) */
      } /* end if ( loAMSUser == null ) */

      raiseException( GEN_MRT_AUTH_FAIL, SEVERITY_LEVEL_SEVERE ) ;
   } /* end roleCheck() */

}