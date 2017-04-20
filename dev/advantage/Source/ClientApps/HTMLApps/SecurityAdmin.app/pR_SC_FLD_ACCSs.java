//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	/*
	**  pR_SC_FLD_ACCSs*/
	
	//{{FORM_CLASS_DECL
	public class pR_SC_FLD_ACCSs extends pR_SC_FLD_ACCSsBase
	
	//END_FORM_CLASS_DECL}}
	{
		// Declarations for instance variables used in the form
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code. To customize paint
		// behavior, modify/augment the paint and the handleEvent methods.
		//{{FORM_CLASS_CTOR
		public pR_SC_FLD_ACCSs ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
			super(parentApp);
		//END_FORM_CLASS_CTOR}}
		}
	
	
	
		//{{EVENT_CODE
		//{{EVENT_T4pR_SC_FLD_CMPRs_beforePageNavigation
void T4pR_SC_FLD_CMPRs_beforePageNavigation( PageNavigation foPageNav,
      VSOutParam foCancel, VSOutParam foNewPage )
{
   VSRow loRow = T2R_SC_FLD_ACCS.getCurrentRow() ;

   if ( loRow == null )
   {
      raiseException( "No field access record selected", SEVERITY_LEVEL_ERROR ) ;
      foCancel.setValue( true ) ;
      foNewPage.setValue( this ) ;
   } /* end if ( loRow == null ) */
   else
   {
      String lsCmprType = loRow.getData( "CMPR_TYP_CD" ).getString() ;

      if ( lsCmprType == null )
      {
         return ;
      } /* end if ( lsCmprType == null ) */
      else
      {
         T2R_SC_FLD_ACCS.updateDataSource() ;

         if ( lsCmprType.equals( "E" ) )
         {
            foCancel.setValue( true ) ;
            foNewPage.setValue( T8pEqualFieldCompare.startPage() ) ;
         } /* end if ( lsCmprType.equals( "E" ) ) */
         else if ( lsCmprType.equals( "I" ) )
         {
            foCancel.setValue( true ) ;
            foNewPage.setValue( T6pInsideRangeFldCmpr.startPage() ) ;
         } /* end else if ( lsCmprType.equals( "I" ) ) */
         else if ( lsCmprType.equals( "N" ) )
         {
            foCancel.setValue( true ) ;
            foNewPage.setValue( T9pNotEqualFieldCompare.startPage() ) ;
         } /* end else if ( lsCmprType.equals( "N" ) ) */
         else if ( lsCmprType.equals( "O" ) )
         {
            foCancel.setValue( true ) ;
            foNewPage.setValue( T7pOutsideRangeFldCmpr.startPage() ) ;
         } /* end else if ( lsCmprType.equals( "O" ) ) */
      } /* end else */
   } /* end else */
}
//END_EVENT_T4pR_SC_FLD_CMPRs_beforePageNavigation}}

		//END_EVENT_CODE}}
	
		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	T4pR_SC_FLD_CMPRs.addPageNavigationListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}
	
		//{{EVENT_ADAPTER_CODE
		
	public void beforePageNavigation( PageNavigation obj, VSOutParam cancel, VSOutParam newPage ){
		Object source = obj;
		if (source == T4pR_SC_FLD_CMPRs) {
			T4pR_SC_FLD_CMPRs_beforePageNavigation( obj, cancel, newPage );
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