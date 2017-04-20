//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.common.AMSSQLUtil;
/*
**  pIN_WKSP*/

//{{FORM_CLASS_DECL
public class pIN_WKSP extends pIN_WKSPBase

//END_FORM_CLASS_DECL}}
{

   private int miType = -1;

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pIN_WKSP ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



	//{{EVENT_CODE
	//{{EVENT_T1IN_WKSP_beforeInsert
void T1IN_WKSP_beforeInsert(VSRow newRow ,VSOutParam cancel )
{
   //Write Event Code below this line
   newRow.getData(2).setInt(miType);
}
//END_EVENT_T1IN_WKSP_beforeInsert}}
// DELETED_BEGIN 
//{{EVENT_T2pIN_BA_beforePageNavigation
void T2pIN_BA_beforePageNavigation( PageNavigation nav, VSOutParam cancel, VSOutParam newPage )
{
   //Write Event Code below this line

   String lsWkspType = getRootDataSource().getCurrentRow().getData("WKSP_TYPE").getString();
      String lsWhereClause = "TYPE = " + AMSSQLUtil.getANSIQuotedStr(lsWkspType,true) ;
      nav.setDevWhere(lsWhereClause);

}
//END_EVENT_T2pIN_BA_beforePageNavigation}}
 // DELETED_END 

	//END_EVENT_CODE}}

      public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1IN_WKSP.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
      }

	//{{EVENT_ADAPTER_CODE
	
	public void beforeInsert( DataSource obj, VSRow newRow ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1IN_WKSP) {
			T1IN_WKSP_beforeInsert(newRow ,cancel );
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
      public int getType()
      {
               return miType;
            }
            public void setType( int fiType )
            {
               miType = fiType;
            }


}