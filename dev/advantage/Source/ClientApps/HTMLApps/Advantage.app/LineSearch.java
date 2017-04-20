//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import com.amsinc.gems.adv.common.AMSSQLUtil;

/*
**  LineSearch
*/

//{{FORM_CLASS_DECL
public class LineSearch extends LineSearchBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public LineSearch ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
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

   public String doAction(PLSRequest foPLSReq)
   {
      String lsCtlgName = foPLSReq.getParameter("ctlg_name");
      //System.err.println("ctlg_name = " + lsCtlgName);
      if (lsCtlgName != null)
      {
         VSQuery     loQuery ;
         VSResultSet loResultSet ;
         PLSApp      loPLSApp = getParentApp();
         VSSession   loSession = loPLSApp.getSession();

         loQuery = new VSQuery(loSession, "IN_LN_SRCH",
               "LN_TYP_CTLG_NM=" + AMSSQLUtil.getANSIQuotedStr(lsCtlgName,true), "");

         loResultSet = loQuery.execute() ;

         loResultSet.last() ;

         if ( loResultSet.getRowCount() == 1 )
         {
            VSRow loRow = loResultSet.getRowAt(1);
            if ( loRow != null )
            {
               String lsApplNm = loRow.getData("APPL_NM").getString();
               String lsPageNm = loRow.getData("LN_SRCH_PG_NM").getString();
               //System.err.println("lsApplNm = " + lsApplNm);
               //System.err.println("lsPageNm = " + lsPageNm);

               AMSDynamicTransition loDynTran = new AMSDynamicTransition(
                                                     lsPageNm, "",lsApplNm);
               loDynTran.setSourcePage(this);
                              loResultSet.close();
               return loDynTran.generateHTMLPage( loPLSApp, getSessionId(), foPLSReq ) ;
            } /* end if ( loRow != null ) */
            else
            {
               loResultSet.close() ;
               return "Invalid Catalog Name";
            }
         } /* end if ( loResultSet.getRowCount() == 1 ) */
         else
         {
            loResultSet.close() ;
            return "Invalid Catalog Name";
         }
            }
      else
      {
         return super.doAction(foPLSReq);
      }
   }
}