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
/*
**  AuditLogCtrl*/

//{{FORM_CLASS_DECL
public class AuditLogCtrl extends AuditLogCtrlBase

//END_FORM_CLASS_DECL}}
{
	// Declarations for instance variables used in the form

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public AuditLogCtrl ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
    * It will set the appropriate checkboxes to be checked.
    */
   public void beforeGenerate()
   {               
      VSRow loCurrRow  = T1IN_AUD_LOG_CTL.getCurrentRow() ;
            
      long liCheckboxMask = 0;      
            
      if ( loCurrRow != null )
      {         
         VSData loSubmitPhaseData = loCurrRow.getData("DOC_SBMT_PHASES");
         
         if ( loSubmitPhaseData != null )
         {                  
            liCheckboxMask = loSubmitPhaseData.getLong();
         } // end if ( loSubmitPhaseData != null ) )
      } // end if ( loCurrRow != null )                 
            
      this.setCheckboxesChecked( liCheckboxMask );
   } /* end beforeGenerate() */
      
   /**
    * Overrides the acceptData method so it can set the doc_sbmt_phases
    * value depending on which checkboxes are checked
    *
    * @param foPLSReq The request
    */
   public void acceptData( PLSRequest foPLSReq )
   {                              
      VSRow loCurrRow  = T1IN_AUD_LOG_CTL.getCurrentRow() ;
      long   llCheckboxesVal = 0;  
      
      if ( loCurrRow != null )          
      {      
         if ( foPLSReq.getParameter( "txtT1DOC_SBMT_PHASE_DRAFT" ) != null )               
         {                
            llCheckboxesVal = llCheckboxesVal + (1L << DOC_PHASE_DRAFT);
         }
   
         if ( foPLSReq.getParameter( "txtT1DOC_SBMT_PHASE_PENDING" ) != null )
         {                  
            llCheckboxesVal = llCheckboxesVal + (1L << DOC_PHASE_PENDING);
         }
   
         if ( foPLSReq.getParameter( "txtT1DOC_SBMT_PHASE_FINAL" ) != null )
         {                  
            llCheckboxesVal = llCheckboxesVal + (1L << DOC_PHASE_FINAL);
         }
      
         VSData loData = loCurrRow.getData( "DOC_SBMT_PHASES" );
         loData.setString( Long.toString(llCheckboxesVal) );     
      }// end if ( loCurrRow != null )   
      
      super.acceptData(foPLSReq);
   }

   /**
    * Reads the checkbox mask and checks the corresponding checkboxes
    *
    * @flMaskVal The mask of the checkboxes
    */       
   private void setCheckboxesChecked( long flMaskVal )
   {
      AMSCheckboxElement loSubmitPhaseDraft = 
         (AMSCheckboxElement)getElementByName("txtT1DOC_SBMT_PHASE_DRAFT");
      AMSCheckboxElement loSubmitPhasePending = 
         (AMSCheckboxElement)getElementByName("txtT1DOC_SBMT_PHASE_PENDING");
      AMSCheckboxElement loSubmitPhaseFinal = 
         (AMSCheckboxElement)getElementByName("txtT1DOC_SBMT_PHASE_FINAL");

      boolean lboolCheckedDraft = false;
      boolean lboolCheckedPending = false;
      boolean lboolCheckedFinal = false;

      if ( (flMaskVal & (1L << DOC_PHASE_DRAFT)) > 0L )
      {         
         lboolCheckedDraft = true;
      }         
      
      if ( (flMaskVal & (1L << DOC_PHASE_PENDING)) > 0L )
      {
         lboolCheckedPending = true;
      }
         
      if ( (flMaskVal & (1L << DOC_PHASE_FINAL)) > 0L )
      {
         lboolCheckedFinal = true;
      }
            
      if ( loSubmitPhaseDraft != null )
      {
         loSubmitPhaseDraft.setChecked( lboolCheckedDraft );
      }

      if ( loSubmitPhasePending != null )
      {
         loSubmitPhasePending.setChecked( lboolCheckedPending );
      }

      if ( loSubmitPhaseFinal != null )
      {
         loSubmitPhaseFinal.setChecked( lboolCheckedFinal );
      }
   } // end of setCheckboxesChecked()
}