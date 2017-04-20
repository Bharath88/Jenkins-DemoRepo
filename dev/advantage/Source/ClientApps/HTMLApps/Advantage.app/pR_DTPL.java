//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.*;

import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;
import advantage.AMSStringUtil;
import advantage.AMSUtil;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSLogConstants;

import org.apache.commons.logging.Log;

/*
 **  pR_DTPL
 */

//{{FORM_CLASS_DECL
public class pR_DTPL extends pR_DTPLBase

//END_FORM_CLASS_DECL}}
{
    private String msDocCd;
    private String msDocDeptCd;
    private String msDocId;
    private int miDocVersNo;
    private String msDocTyp;

   private static Log moAMSLog = AMSLogger.getLog(pR_DTPL.class,
         AMSLogConstants.FUNC_AREA_DFLT);


    // This is the constructor for the generated form. This also constructs
    // all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pR_DTPL ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
    }


	//{{EVENT_CODE
	//{{EVENT_T1R_DTPL_beforeQuery
    void T1R_DTPL_beforeQuery(VSQuery query, VSOutParam resultset)
    {
        //Write Event Code below this line
        VSORBSession loORBSession = getParentApp().getSession().getORBSession();

        try
        {
            msDocCd = loORBSession.getProperty(ATTR_DOC_CD);
            msDocTyp = loORBSession.getProperty(ATTR_DOC_TYP);
        }
        catch (Exception loExp)
        {
           // Add exception log to logger object
           moAMSLog.error("Unexpected error encountered while processing. ", loExp);
        }

        SearchRequest loSrchReq = new SearchRequest();
        StringBuffer lsbWhere = new StringBuffer(160);
        String lsOldWhereClause = query.getSQLWhereClause();
        if (!AMSStringUtil.strIsEmpty(lsOldWhereClause))
        {
            // Check If the existing Query is not null, if it is not null append " AND " before
            // the custom Query begins.
            lsbWhere.append(" AND ");
        }
        lsbWhere.append(" DOC_CD ");
        lsbWhere.append(AMSSQLUtil.getANSIQuotedStr(msDocCd, AMSSQLUtil.EQUALS_OPER));
        lsbWhere.append(" AND DOC_TYP ");
        lsbWhere.append(AMSSQLUtil.getANSIQuotedStr(msDocTyp, AMSSQLUtil.EQUALS_OPER));
        loSrchReq.add(lsbWhere.toString());
        query.addFilter(loSrchReq);
    }

//END_EVENT_T1R_DTPL_beforeQuery}}

	//END_EVENT_CODE}}

    public void addListeners()
    {
		//{{EVENT_ADD_LISTENERS
		
	T1R_DTPL.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
    }

	//{{EVENT_ADAPTER_CODE
	
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_DTPL) {
			T1R_DTPL_beforeQuery(query , resultset );
		}
	}
	//END_EVENT_ADAPTER_CODE}}

    public HTMLDocumentSpec getDocumentSpecification()
    {
        return getDefaultDocumentSpecification();
    }

    public String getFileName()
    {
        return getDefaultFileName();
    }

    public String getFileLocation()
    {
        return getPageTemplatePath();
    }

    public void afterPageInitialize()
    {
        super.afterPageInitialize();
        //Write code here for initializing your own control
        //or creating new control.

    }

    public void setDocumentIdentifier(String fsDocDeptCd,
        String fsDocId, int fiDocVersNo)
    {
        msDocDeptCd = fsDocDeptCd;
        msDocId = fsDocId;
        miDocVersNo = fiDocVersNo;
    }

    public static String downloadDocPage(VSRow foDTPLRow,
        AMSHyperlinkActionElement foActnElem, PLSRequest foRequest)
    {
        VSPage loSrcPage = foActnElem.getPage();
        pR_DTPL loDTPL;
        AMSDynamicTransition loDynTran;
        String lsHTML = "";

        VSORBSession loORBSession = loSrcPage.getParentApp().getSession().getORBSession();

        try
        {
            loORBSession.setProperty(ATTR_DOC_CD, foDTPLRow.getData(ATTR_DOC_CD).getString());
            loORBSession.setProperty(ATTR_DOC_TYP, foDTPLRow.getData(ATTR_DOC_TYP).getString());
            /*
             * Any additional where clause will be added in the beforeQuery() event
             * only.  This will optimize the filter (starts with indexed columns)
             */
            loDynTran = new AMSDynamicTransition("pR_DTPL", null, "Advantage");
            loDynTran.setSourcePage(loSrcPage);
            loDTPL = (pR_DTPL) loDynTran.getVSPage(loSrcPage.getParentApp(), loSrcPage.getSessionId());

            /* Set the document identifier */
            loDTPL.setDocumentIdentifier(foDTPLRow.getData(ATTR_DOC_DEPT_CD).getString(),
                foDTPLRow.getData(ATTR_DOC_ID).getString(),
                foDTPLRow.getData(ATTR_DOC_VERS_NO).getInt());

            lsHTML = loDTPL.generate();
            foActnElem.getPage().getParentApp().getPageExpireAlg().pageNavigatedTo(loDTPL);
        }
        catch (Exception loExp)
        {
           // Add exception log to logger object
           moAMSLog.error("Unexpected error encountered while processing. ", loExp);

        }
        return lsHTML;
    } /* end downloadDocPage() */

    public String generate()
    {
        return super.generate();
    }

    public void exportDocToExcel()
    {
        VSORBSession loORBSession = getParentApp().getSession().getORBSession();
        VSRow loRow = T1R_DTPL.getCurrentRow();
        if (loRow != null)
        {
            // Set the session properties for VLS
            try
            {
                loORBSession.setProperty("DOC_ID", msDocId);
                loORBSession.setProperty("DOC_VERS_NO", String.valueOf(miDocVersNo));
                loORBSession.setProperty("DOC_CD",msDocCd);
                loORBSession.setProperty("DOC_DEPT_CD",msDocDeptCd);
                loRow.save();
                loORBSession.removeProperty("DOC_ID");
                loORBSession.removeProperty("DOC_VERS_NO");
                loORBSession.removeProperty("DOC_CD");
                loORBSession.removeProperty("DOC_DEPT_CD");
            }
            catch (Exception feException)
            {
               // Add exception log to logger object
               moAMSLog.error("Unexpected error encountered while processing. ", feException);

            }
        }
    }


}
