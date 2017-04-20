//{{IMPORT_STMTS
package advantage.PasswordReset;
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
/*
**  StartupPage
*/

//{{FORM_CLASS_DECL
public class StartupPage extends StartupPageBase

//END_FORM_CLASS_DECL}}
{
   private boolean              mbFirstGenerate = true ;

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public StartupPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
   setAllowHistory( false ) ;
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
		//or creating new contro

	}
   public String generate()
   {
      if ( mbFirstGenerate )
      {
         appendOnloadString( "STARTPG_LoadResetPage()" ) ;
      } /* end if ( mbFirstGenerate ) */
      return super.generate() ;
   }
}
