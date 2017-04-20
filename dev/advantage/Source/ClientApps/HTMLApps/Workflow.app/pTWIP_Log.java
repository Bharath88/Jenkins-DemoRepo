//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	/*
	**  pTWIP_Log*/
	
	//{{FORM_CLASS_DECL
	public class pTWIP_Log extends pTWIP_LogBase
	
	//END_FORM_CLASS_DECL}}
	{
	
		// This is the constructor for the generated form. This also constructs
		// all the controls on the form. Do not alter this code.
		//{{FORM_CLASS_CTOR
		public pTWIP_Log ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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
	    * This method overrides the beforeGenerate method to set the page title
	    * so that the name of the workflow role is displayed as part of the
	    * page title.
	    *
	    * Modification Log : Kiran Hiranandani   - 09/17 /02
	    *                                        - inital version
	    *                                        - 03/06/2003
	    *                                        - Check to ensure current record not null
	    *
	    */
	   public void beforeGenerate()
	   {
	      TextContentElement loTCE   = (TextContentElement)getElementByName( "PageTitle" ) ;
	
	      if ( loTCE != null )
	      {
	         DataSource     loDataSource     = null ;
	         VSRow          loCurRow         = null ;
	         StringBuffer   lsbPageTitle = new StringBuffer(100) ;
	
	         lsbPageTitle.append( "Track Work in Progress Log " ) ;
	
	         loDataSource = getRootDataSource() ;
	         if ( loDataSource != null )
	         {
	            loCurRow = loDataSource.getCurrentRow() ;
	            if ( loCurRow != null )
	            {
	               String lsDocId          = null ;
	               String lsDocCode        = null ;
	               String lsDocDeptCode    = null ;
	               String lsDocVersNo      = null ;
	
	               lsDocId       = loCurRow.getData("DOC_ID").getString() ;
	               lsDocCode     = loCurRow.getData("DOC_CD").getString() ;
	               lsDocDeptCode = loCurRow.getData("DOC_DEPT_CD").getString() ;
	               lsDocVersNo   = loCurRow.getData("DOC_VERS_NO").getString() ;
	               if ( lsDocId != null )
	               {
	                  lsbPageTitle.append( lsDocCode ).append( " - " ) ;
	                  lsbPageTitle.append( lsDocDeptCode ).append( "-" ) ;
	                  lsbPageTitle.append( lsDocId ).append( " - " ) ;
	                  lsbPageTitle.append( lsDocVersNo ) ;
	               } /* if ( lsWorkflowRole != null ) */
	            } /* end if ( loCurRow != null ) */
	         } /* end if ( loDataSource != null ) */
	
	         loTCE.setValue( lsbPageTitle.toString() ) ;
	      } /* end if ( loTCE != null ) */
	
	      super.beforeGenerate() ;
	   } /* end beforeGenerate() */
	
	}