//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  pDocAssembleRequestGrid
*/

//{{FORM_CLASS_DECL
public class pDocAssembleRequestGrid extends pDocAssembleRequestGridBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pDocAssembleRequestGrid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
   setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
	}


	//{{EVENT_CODE
	//{{EVENT_pDocAssembleRequestGrid_afterActionPerformed
void pDocAssembleRequestGrid_afterActionPerformed( ActionElement ae, PLSRequest preq )
{
	//Write Event Code below this line
}
//END_EVENT_pDocAssembleRequestGrid_afterActionPerformed}}
//{{EVENT_pDocAssembleRequestGrid_requestReceived
void pDocAssembleRequestGrid_requestReceived( PLSRequest req, PageEvent evt )
{
   if ( req.getParameter( "refresh_page" ) != null )
   {
	getRootDataSource().executeQuery();
      evt.setCancel( true ) ;
      evt.setNewPage( this ) ;
   } /* end if ( req.getParameter( "refresh_page" ) != null ) */
}
//END_EVENT_pDocAssembleRequestGrid_requestReceived}}
//{{EVENT_T1R_ASEM_REQ_beforeQuery
void T1R_ASEM_REQ_beforeQuery(VSQuery query ,VSOutParam resultset )
{
	//Write Event Code below this line
	// sort result by REQ_ID desc
   SearchRequest loOrderBy = new SearchRequest() ;
   loOrderBy.add( "REQ_ID DESC" ) ;
   query.replaceSortingCriteria( loOrderBy ) ;

}
//END_EVENT_T1R_ASEM_REQ_beforeQuery}}
//{{EVENT_pDocAssembleRequestGrid_beforeActionPerformed
void pDocAssembleRequestGrid_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line
   // click back, refresh header, otherwise other user modified row excpetion will pop up
   if ("84".equals(ae.getAction()))
   {

      VSPage loSourcePage = getSourcePage();
      if(loSourcePage!=null)
      {
         loSourcePage.getRootDataSource().getResultSet().refreshCurrentRow();
      }
   }

}
//END_EVENT_pDocAssembleRequestGrid_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	T1R_ASEM_REQ.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void afterActionPerformed ( VSPage obj, ActionElement ae, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pDocAssembleRequestGrid_afterActionPerformed( ae, preq );
		}
	}
	public void requestReceived ( VSPage obj, PLSRequest req, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pDocAssembleRequestGrid_requestReceived( req, evt );
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pDocAssembleRequestGrid_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_ASEM_REQ) {
			T1R_ASEM_REQ_beforeQuery(query , resultset );
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