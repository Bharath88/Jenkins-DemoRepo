//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.*;
import com.amsinc.gems.adv.vfc.html.*;

/*
**  DocNavPanel*/

//{{FORM_CLASS_DECL
public class DocNavPanel extends DocNavPanelBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public DocNavPanel ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      setDisplayErrors(false);
      setADVUIType("PageStdUI");
      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_SHOW);
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
      public String getFileLocation()
      {
/*         if (msDocNavFileLocation == null)
         {
            StringBuffer loBuf = new StringBuffer(256);
            loBuf.append("..");
            loBuf.append(File.separator);
            loBuf.append("..");
            loBuf.append(File.separator);
            loBuf.append("VLSComponents");
            loBuf.append(File.separator);
            loBuf.append("Classes");
            loBuf.append(File.separator);
            loBuf.append("advantage");
            loBuf.append(File.separator);
            loBuf.append("Advantage");
            loBuf.append(File.separator);

            msDocNavFileLocation = loBuf.toString();
         }

         return msDocNavFileLocation;
*/
         return getPageTemplatePath();
      }

   public void afterPageInitialize() {
      super.afterPageInitialize();
      //Write code here for initializing your own control
      //or creating new control.

   }

   
  
}
