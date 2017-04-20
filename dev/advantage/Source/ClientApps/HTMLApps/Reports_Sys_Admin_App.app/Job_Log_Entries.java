//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  Job_Log_Entries
*/

//{{FORM_CLASS_DECL
public class Job_Log_Entries extends Job_Log_EntriesBase

//END_FORM_CLASS_DECL}}
{
    // Declarations for instance variables used in the form

    // This is the constructor for the generated form. This also constructs
    // all the controls on the form. Do not alter this code. To customize paint
    // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Job_Log_Entries ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
    }

		//{{EVENT_CODE
		//{{EVENT_Job_Log_Entries_requestReceived
void Job_Log_Entries_requestReceived( PLSRequest req, PageEvent evt )
{
   //Write Event Code below this line
   if ( req.getParameter("refresh_page") != null )
   {
      getRootDataSource().executeQuery();
      getRootDataSource().lastPage();
      evt.setCancel( true ) ;
      evt.setNewPage( this ) ;
      /* If the current frame is not equal to target frame then set Current
         frame to target frame */
      if( ! (getParentApp().getCurrentFrameName().equals(getTargetFrame()) ) )
      {
         getParentApp().setCurrentFrameName(getTargetFrame());
      } // end if( ! (getParentApp().getCurrentFrameName() .....
   }
}

//END_EVENT_Job_Log_Entries_requestReceived}}

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
			Job_Log_Entries_requestReceived( req, evt );
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


    }