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
**  Chain_Job_list
*/

//{{FORM_CLASS_DECL
public class Chain_Job_list extends Chain_Job_listBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

       private long mlCurrentJobID;   //  the job id
       private String msParentID;     //  the catalog id of the selected job

       private String msChainJobLabel =null; // the selected job's catalog label- set in before navigation event

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public Chain_Job_list ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T3User_AgentParm_Grid_beforePageNavigation
void T3User_AgentParm_Grid_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
    mlCurrentJobID = getRootDataSource().getCurrentRow().getData("AGNT_ID").getLong();
    msParentID     = getRootDataSource().getCurrentRow().getData("PNT_CTLG_ID").getString();
    msChainJobLabel = getRootDataSource().getCurrentRow().getData("JOB_LBL").getString();
}
//END_EVENT_T3User_AgentParm_Grid_beforePageNavigation}}
// DELETED_BEGIN 
//{{EVENT_T2Chain_Job_Parm_beforePageNavigation

//END_EVENT_T2Chain_Job_Parm_beforePageNavigation}}
 // DELETED_END 

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T3User_AgentParm_Grid.addPageNavigationListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T3User_AgentParm_Grid) {
			T3User_AgentParm_Grid_beforePageNavigation( obj, cancel, newPage );
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
    * Returns the current job ID
    */
   public long getJobID()
   {
      return mlCurrentJobID;
   }

   /**
    * Returns the current job's catalog id
    */
   public String getParentID()
   {
       return msParentID;
   } // of getParentID

   /**
    * Returns the current job's catalog label
    */
   public String getChainJobLabel()
   {
       return msChainJobLabel;
   } // of getParentID

}