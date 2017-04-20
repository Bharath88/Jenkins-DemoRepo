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
**  pAltViewObjAttHist*/

//{{FORM_CLASS_DECL
public class pAltViewObjAttHist extends pAltViewObjAttHistBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   private String  msAltGroupId   = null ;


   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pAltViewObjAttHist ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
         setAllowHistory( false ) ;
   }



//{{EVENT_CODE
//{{EVENT_pAltViewObjAttHist_beforeActionPerformed
void pAltViewObjAttHist_beforeActionPerformed( ActionElement foActnElem,
           PageEvent foPageEvent, PLSRequest foPLSReq )
{
   if ( foActnElem.getName().equals( "ReturnToDoc" ) )
   {
      foPageEvent.setCancel( true ) ;
      foPageEvent.setNewPage( getSourcePage() ) ;
   } /* end else if ( foActnElem.getName().equals( "ReturnToDoc" ) ) */

}
//END_EVENT_pAltViewObjAttHist_beforeActionPerformed}}
//{{EVENT_T1OBJ_ATT_ALT_QRY_beforeQuery
void T1OBJ_ATT_ALT_QRY_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   String lsWhereClause = query.getSQLWhereClause() ;

   if ( lsWhereClause == null || lsWhereClause.trim().length() == 0 )
   {
      VSResultSet loResultSet = query.getNewResultSet() ;
      resultset.setValue( loResultSet ) ;
   }

}
//END_EVENT_T1OBJ_ATT_ALT_QRY_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addPageListener(this);
	T1OBJ_ATT_ALT_QRY.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pAltViewObjAttHist_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1OBJ_ATT_ALT_QRY) {
			T1OBJ_ATT_ALT_QRY_beforeQuery(query , resultset );
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

   public void setAltGrpId( String fsAltGrpId )
   {
      msAltGroupId = fsAltGrpId ;
   } /* end setAltGrpId() */


}