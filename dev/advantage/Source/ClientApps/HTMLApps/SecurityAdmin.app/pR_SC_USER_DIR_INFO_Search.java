//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.vfc.html.AMSPage;

	/*
	**  pR_SC_USER_DIR_INFO_Search
	*/

	//{{FORM_CLASS_DECL
	public class pR_SC_USER_DIR_INFO_Search extends pR_SC_USER_DIR_INFO_SearchBase
	
	//END_FORM_CLASS_DECL}}
	{

		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code.
		//{{FORM_CLASS_CTOR
		public pR_SC_USER_DIR_INFO_Search ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

      public void beforeGenerate()
      {
         /*
          * When LDAP is used for storing Directory Information, it is possible that different Users
          * have different User Security Realm(different LDAP servers/different LDAP AD domains).
          * This edit has been added because currently we do not support querying across multiple
          * LDAP domains in a single query and secondly the assumption here is that LDAP servers
          * should be directly used for maintaining such data since the information
          * is sourced from there.
          */
         if( AMSParams.miDirectoryInfoType == AMSParams.TYPE_LDAP )
         {
            raiseException( "This page is not available when user information is sourced from "
               + "a directory server (for example LDAP based directories).",
               AMSPage.SEVERITY_LEVEL_SEVERE ) ;
         }
      }

		public String generate()
		{
		   beforeGenerate();
		   return super.generate();
	    }



	}