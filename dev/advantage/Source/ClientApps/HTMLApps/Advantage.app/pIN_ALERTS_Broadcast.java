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
**  pIN_ALERTS_Broadcast*/

//{{FORM_CLASS_DECL
public class pIN_ALERTS_Broadcast extends pIN_ALERTS_BroadcastBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pIN_ALERTS_Broadcast ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T1IN_ALERTS_beforeQuery
void T1IN_ALERTS_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   //Write Event Code below this line
   SearchRequest loSrchReq = new SearchRequest() ;
   String lsWhereClause = query.getSQLWhereClause();
   StringBuffer lsbFilter = new StringBuffer(32);
   if (lsWhereClause != null && lsWhereClause.trim().length() > 0)
   {
      lsbFilter.append(" AND ");
   }

   lsbFilter.append( " USER_ID " +
         AMSSQLUtil.getANSIQuotedStr(null, AMSSQLUtil.EQUALS_OPER) );
   loSrchReq.add(lsbFilter.toString());
   query.addFilter( loSrchReq ) ;
}

//END_EVENT_T1IN_ALERTS_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_ALERTS.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_ALERTS) {
			T1IN_ALERTS_beforeQuery(query , resultset );
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