//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.*;
import java.rmi.RemoteException;

/*
**  GlobalSearchWindow
*/

//{{FORM_CLASS_DECL
public class GblNavSearchWindow extends GblNavSearchWindowBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public GblNavSearchWindow ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

   }




//{{EVENT_CODE
//{{EVENT_T1IN_PAGES_beforeQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only the records with PG_SHW_FL=1. Additionally the
 * where clause is set up to retrieve only those pages which belong
 * to applications to which the user has logged in.
 *
 * 
 *
 * @param foQuery         Reference to the query statement
 * @param foResultSet     Result Set
 */
void T1IN_PAGES_beforeQuery(VSQuery foQuery ,VSOutParam foResultSet )
{
	advantage.Advantage.SearchWindow.setQueryCriteria(foQuery,foResultSet,this);
   
} /* end T1IN_PAGES_beforeQuery() */


//END_EVENT_T1IN_PAGES_beforeQuery}}
//{{EVENT_GblNavSearchWindow_beforeActionPerformed
void GblNavSearchWindow_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line
	 VSRow loCurrRow = null;
	 if(ae.getName().equals("btnleaf") ||ae.getName().equals("btnleafWithData") )
	 {
	    loCurrRow = getRootDataSource().getCurrentRow();
	    HiddenElement loPageCode = (HiddenElement)getElementByName("SelectedPageCode");
		 loPageCode.setValue(loCurrRow.getData("PAGE_CD").getString());
	    if(ae.getName().equals("btnleaf"))
	    {
	       appendOnloadString("leaf()");
	    }/* end if if(ae.getName().equals("btnleaf")) */
	    else
	    {
	       appendOnloadString("leafWithData()");
	    }
	    
			//appendOnloadString("UTILS_ClosePage('"+getTargetFrame()+"')");
			appendOnloadString("closePage()");
	  }/* end if(ae.getName().equals("btnleaf") */
	
}
//END_EVENT_GblNavSearchWindow_beforeActionPerformed}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_PAGES.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			GblNavSearchWindow_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_PAGES) {
			T1IN_PAGES_beforeQuery(query , resultset );
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
