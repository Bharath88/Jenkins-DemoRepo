//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSTextContentElement;
import com.cgi.adv.html.pref.utils.AdvPrefUtils;

import advantage.AMSStringUtil;


/*
**  pIN_APP_ACCS_LOG
*/

//{{FORM_CLASS_DECL
public class pIN_APP_ACCS_LOG extends pIN_APP_ACCS_LOGBase

//END_FORM_CLASS_DECL}}
{
	
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pIN_APP_ACCS_LOG ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}

	
	//{{EVENT_CODE
	//{{EVENT_T1IN_APP_ACCS_LOG_beforeQuery
void T1IN_APP_ACCS_LOG_beforeQuery(VSQuery foQuery ,VSOutParam foResultset )
{
   //Write Event Code below this line
   String lsFromDate = T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1FROM_DT");
   String lsToDate = T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1TO_DT");
   String lsWhere = "";
   
   if (!(AMSStringUtil.strIsEmpty(lsToDate)
            && AMSStringUtil.strIsEmpty(lsFromDate)))
   {
      if(!AMSStringUtil.strIsEmpty(lsFromDate))
      {
         long llFromTS = (new Date(lsFromDate)).getTime();
         lsWhere += " IN_APP_ACCS_LOG.LOG_TS >= " + llFromTS;
      }
      if(!AMSStringUtil.strIsEmpty(lsToDate))
      {
         if(!"".equals(lsWhere))
            lsWhere += " AND ";
         long llFromTS = (new Date(lsToDate)).getTime();
         lsWhere += " IN_APP_ACCS_LOG.LOG_TS <= " + llFromTS;
      }
   }

   if(!AMSStringUtil.strIsEmpty(T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1USER_ID")))
   {
      if (!"".equals(lsWhere))
         lsWhere += " AND ";
      lsWhere += " IN_APP_ACCS_LOG.USER_ID = "
            + AMSSQLUtil.getANSIQuotedStr(
                  T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1USER_ID"),
                  true);
   }
   
   if(!AMSStringUtil.strIsEmpty(T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1APP_NM")))
   {
      if (!"".equals(lsWhere))
         lsWhere += " AND ";
      lsWhere += " IN_APP_ACCS_LOG.APP_NM <= "
            + AMSSQLUtil.getANSIQuotedStr(
                  T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1APP_NM"),
                  true);
   }
   
   if(!AMSStringUtil.strIsEmpty(T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1ACCS_LOG_ID")))
   {
      if (!"".equals(lsWhere))
         lsWhere += " AND ";
      lsWhere += " IN_APP_ACCS_LOG.ACCS_LOG_ID = "
            + T1IN_APP_ACCS_LOG.getQBFDataForElement("txtT1ACCS_LOG_ID");
   }
   VSQuery loQuery = new VSQuery(getParentApp().getSession(), "IN_APP_ACCS_LOG", lsWhere, null);
   foResultset.setValue(loQuery.execute());

}
//END_EVENT_T1IN_APP_ACCS_LOG_beforeQuery}}
//{{EVENT_pIN_APP_ACCS_LOG_beforeGenerate
   void pIN_APP_ACCS_LOG_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
   {
      DivElement loScalarView = (DivElement) getElementByName("ScalarView");

      if (loScalarView.isVisible())
      {
         AMSTextContentElement loReq = (AMSTextContentElement) getElementByName("txtT2ACCSS_REQ");
         VSRow loCurrentRow = T1IN_APP_ACCS_LOG.getCurrentRow();
         if (loCurrentRow != null)
         {
            StringBuffer lsbWhere = new StringBuffer(128);
            lsbWhere.append("ACCS_LOG_ID").append("=")
                  .append(loCurrentRow.getData("ACCS_LOG_ID").getLong());
            T2IN_APP_ACCS_LOG.getQuery().setColumnProjectionLevel(DataConst.ALLTYPES);
            T2IN_APP_ACCS_LOG.executeQuery(lsbWhere.toString(), null);
            VSResultSet loResultSet = null;
            try
            {
               VSQuery loQuery = new VSQuery(getParentApp().getSession(),
                     "IN_APP_ACCS_LOG", lsbWhere.toString(), "");

               // Fix for the download of binary types
               loQuery.setColumnProjectionLevel(DataConst.ALLTYPES);

               loResultSet = loQuery.execute();

               if (loResultSet != null)
               {
                  loResultSet.last();

                  byte[] lbAttchData = loResultSet.first().getData("ACCS_REQ")
                        .getBytes();

                  if (lbAttchData != null)
                  {

                     String lsReq = new String(
                           AdvPrefUtils.UNZIPContent(lbAttchData));
                     loReq.setCustomValue(StringEscapeUtils.unescapeHtml(lsReq));

                  }
               }
            }
            catch (IOException foEx)
            {
               //TODO
               foEx.printStackTrace();
            }
            finally
            {
               if(loResultSet != null)
               {
                  loResultSet.close();
               }
            }
         }
      }

   }
//END_EVENT_pIN_APP_ACCS_LOG_beforeGenerate}}
//{{EVENT_pIN_APP_ACCS_LOG_beforeActionPerformed
void pIN_APP_ACCS_LOG_beforeActionPerformed( ActionElement foAE, PageEvent foEvt, PLSRequest foPReq )
{
   String lsAction = foAE.getAction();
   DivElement loGridView = (DivElement)getElementByName("GridView");
   DivElement loScalarView = (DivElement)getElementByName("ScalarView");
   
   if("view_details".equals(lsAction))
   {
      loGridView.setVisible(false);
      loScalarView.setVisible(true);
   }
   else
   {
      loGridView.setVisible(true);
      loScalarView.setVisible(false);
   }
}
//END_EVENT_pIN_APP_ACCS_LOG_beforeActionPerformed}}
//{{EVENT_T2IN_APP_ACCS_LOG_beforeQuery
   void T2IN_APP_ACCS_LOG_beforeQuery(VSQuery foQuery, VSOutParam foResultSet)
   {
   }
//END_EVENT_T2IN_APP_ACCS_LOG_beforeQuery}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1IN_APP_ACCS_LOG.addDBListener(this);
	addPageListener(this);
	T2IN_APP_ACCS_LOG.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pIN_APP_ACCS_LOG_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pIN_APP_ACCS_LOG_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_APP_ACCS_LOG) {
			T1IN_APP_ACCS_LOG_beforeQuery(query , resultset );
		}
	
		if (source == T2IN_APP_ACCS_LOG) {
			T2IN_APP_ACCS_LOG_beforeQuery(query , resultset );
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