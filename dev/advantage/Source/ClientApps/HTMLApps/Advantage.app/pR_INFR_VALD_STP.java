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
import com.amsinc.gems.adv.common.AMSSQLUtil;


/*
**  pR_INFR_VALD_STP
*/

//{{FORM_CLASS_DECL
public class pR_INFR_VALD_STP extends pR_INFR_VALD_STPBase

//END_FORM_CLASS_DECL}}
{

     String msTBLNm;
     String msPageTyp;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pR_INFR_VALD_STP ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_T2IN_PAGES_BeforePickQuery
void T2IN_PAGES_BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel)
{
	//Write Event Code below this line


	VSRow loSource = dataSource.getCurrentRow();
	msTBLNm = loSource.getData("TBL_NM").getString();
	msPageTyp=loSource.getData("INFR_VALD_TYP").getString();
	String lsTBL_NM = loSource.getData("TBL_NM").getString();
	StringBuffer lsWhereClause = new StringBuffer (200);

	lsWhereClause.append("DEST_PG LIKE '%");
	lsWhereClause.append(lsTBL_NM);
	lsWhereClause.append("%'");


	whereClause.setValue(lsWhereClause.toString());








}
//END_EVENT_T2IN_PAGES_BeforePickQuery}}
//{{EVENT_T2IN_PAGES_AfterPickFill
void T2IN_PAGES_AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow)
{
	//Write Event Code below this line

      //dataSource.getCurrentRow().getData("INFR_VALD_TYP").setData(supplierRow.getData("PAGE_TYP_CD"));

      dataSource.getCurrentRow().getData("PAGE_CD").setData(supplierRow.getData("PAGE_CD"));
      dataSource.getCurrentRow().getData("INFR_VALD_DSCR").setData(supplierRow.getData("PAGE_DESC"));
}
//END_EVENT_T2IN_PAGES_AfterPickFill}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T2IN_PAGES.addPickListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T2IN_PAGES) {
			T2IN_PAGES_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow){
		Object source = obj;
		if (source == T2IN_PAGES) {
			T2IN_PAGES_AfterPickFill(obj, dataSource, supplierRow);
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