//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSTextFieldElement;
import advantage.AMSSQLGenerator;
import advantage.AMSUtil;
import java.util.Date;

/*
**  pCOMMON_EVENT_LOG
*/

//{{FORM_CLASS_DECL
public class pCOMMON_EVENT_LOG extends pCOMMON_EVENT_LOGBase

//END_FORM_CLASS_DECL}}
{

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pCOMMON_EVENT_LOG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T1COMMON_EVENT_LOG_beforeQuery
void T1COMMON_EVENT_LOG_beforeQuery(VSQuery foQuery ,VSOutParam resultset )
{
	//Write Event Code below this line
   String lsToDate = T1COMMON_EVENT_LOG.getQBFDataForElement("txtT1TO_DT");
   String lsFromDate = T1COMMON_EVENT_LOG.getQBFDataForElement("txtT1FROM_DT");
   String lsPriority = T1COMMON_EVENT_LOG.getQBFDataForElement("txtT1PRIORITY");

   if("".equals(lsToDate) && "".equals(lsFromDate))
      return;

   SearchRequest lsr = new SearchRequest();
   String lsWhere = "";
   if(!"".equals(lsFromDate))
   {
      lsWhere += " COMMON_EVENT_LOG.EVENT_DATE >= " + AMSSQLUtil.getAMSDate(
            new VSDate(new Date(lsFromDate)), DATE_DATE_TIMESTAMP, DataConst.TIMESTAMP,
            getDatabaseType(T1COMMON_EVENT_LOG.getSession()) );
   }
   if(!"".equals(lsToDate))
   {
      if(!"".equals(lsWhere))
         lsWhere += " AND ";
      lsWhere += " COMMON_EVENT_LOG.EVENT_DATE <= " + AMSSQLUtil.getAMSDate(
            new VSDate(new Date(lsToDate)), DATE_DATE_TIMESTAMP, DataConst.TIMESTAMP,
            getDatabaseType(T1COMMON_EVENT_LOG.getSession()) );
   }
   if(!"".equals(lsPriority))
   {
      if(!"".equals(lsWhere))
         lsWhere += " AND ";
      lsWhere += " COMMON_EVENT_LOG.PRIORITY = '" + AMSSQLUtil.getANSIQuotedStr(lsPriority) + "'";
   }

   lsr.add(lsWhere);
   foQuery.addFilter(lsr);
}
//END_EVENT_T1COMMON_EVENT_LOG_beforeQuery}}
//{{EVENT_pCOMMON_EVENT_LOG_beforeActionPerformed
void pCOMMON_EVENT_LOG_beforeActionPerformed( ActionElement foActnElem, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line

   // If Refresh link is clicked, just update the page with latest data.
   if("T1COMMON_EVENT_LOG_Refresh".equals(foActnElem.getName()))
   {
      refreshDataSource(T1COMMON_EVENT_LOG);
   }

}
//END_EVENT_pCOMMON_EVENT_LOG_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1COMMON_EVENT_LOG.addDBListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pCOMMON_EVENT_LOG_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1COMMON_EVENT_LOG) {
			T1COMMON_EVENT_LOG_beforeQuery(query , resultset );
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

   public boolean useVirtualResultSet( AMSDataSource foDataSource )
   {
      return false ;
   }

}