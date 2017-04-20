//{{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
//END_IMPORT_STMTS}}


/*
**  Add_LeafNode
*/

//{{FORM_CLASS_DECL
public class Add_LeafNode extends VSPage
//END_FORM_CLASS_DECL}}

{
	// Declarations for instance variables used in the form
	//{{FORM_VAR_DECL
	public static HTMLDocumentSpec docSpec = null;
		 DataSource T1R_BS_CATALOG = new DataSource();
	
	//END_FORM_VAR_DECL}}


	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public Add_LeafNode ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super();
	    setParentApp(parentApp);
	//END_FORM_CLASS_CTOR}}

		
		//{{FORM_OBJ_CTOR
		beforePageInitialize();
		
		
			T1R_BS_CATALOG.setName("T1R_BS_CATALOG");
			T1R_BS_CATALOG.setSession( parentApp.getSession() );
			T1R_BS_CATALOG.setQueryInfo("R_BS_CATALOG", "", "", "", false);
		       setRootDataSource(T1R_BS_CATALOG);
			T1R_BS_CATALOG.setPage(this);
			T1R_BS_CATALOG.setDataDependency(false, false);
		    T1R_BS_CATALOG.setAllowInsert(true);
		    T1R_BS_CATALOG.setAllowDelete(true);
		    T1R_BS_CATALOG.setAllowUpdate(true);
		    T1R_BS_CATALOG.setNumRowsPerPage(10);
		    T1R_BS_CATALOG.setPreFetchRowCount(false);
		    
		    T1R_BS_CATALOG.setMaxRowsPerFetch(16);
			T1R_BS_CATALOG.setSaveMode(T1R_BS_CATALOG.SAVE_IMMEDIATE);
		
			add(T1R_BS_CATALOG);
		
		
		
		addListeners();
		afterPageInitialize();
		
		//END_FORM_OBJ_CTOR}}

	}

	//{{FORM_GETFILENAME
	    public String getDefaultFileName() {
			return "Add_LeafNode.htm";
		}
	    public HTMLDocumentSpec getDefaultDocumentSpecification() {    
			//At one point of time only one thread should
			//read document specification. 
			synchronized(this.getClass()) {
		        	if (docSpec == null) {
	        	    		docSpec = VSPage.createSpecification(getFileLocation()  + getFileName());
	        		}
			}
	        	return docSpec;
	    }
	
	//END_FORM_GETFILENAME}}



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


}