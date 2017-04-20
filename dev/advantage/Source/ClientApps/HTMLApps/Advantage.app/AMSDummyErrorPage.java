//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.AMSPage ;

/*
**  AMSDummyErrorPage*/


//{{FORM_CLASS_DECL
public class AMSDummyErrorPage extends AMSDummyErrorPageBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

      Exception moException = null;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public AMSDummyErrorPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

   public Exception getException()
   {
      return moException;
   }

   public void setException(Exception foExcep)
   {
      moException = foExcep;
   }

   public void beforeGenerate ()
   {
      if (moException != null)
      {
         TextAreaElement loStackElem = (TextAreaElement)
                                        getElementByName("StackTrace");
         if (loStackElem != null)
         {
            PrintWriter loPrintWriter;
            StringWriter loStringWriter;

            loStringWriter  = new StringWriter();
            loPrintWriter = new PrintWriter(loStringWriter);

            moException.printStackTrace(loPrintWriter);

            loStackElem.setText( loStringWriter.toString() );
            loPrintWriter.close();
         } /* end if (loStackElem != null) */
      } /* end if (moException != null) */
   }
}