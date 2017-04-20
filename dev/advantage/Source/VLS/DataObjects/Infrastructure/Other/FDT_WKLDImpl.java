//{{COMPONENT_IMPORT_STMTS
package advantage;
import java.util.Enumeration;
import java.util.Vector;
import versata.common.*;
import versata.common.vstrace.*;
import versata.vls.cache.*;
import versata.vls.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.amsinc.gems.adv.common.*;
import com.versata.util.logging.*;
import org.apache.commons.logging.*;

//END_COMPONENT_IMPORT_STMTS}}

/*
**  FDT_WKLD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class FDT_WKLDImpl extends  FDT_WKLDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public FDT_WKLDImpl (){
		super();
	}
	
	public FDT_WKLDImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

    }

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}



    public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
    }

	//{{COMPONENT_RULES
		public static FDT_WKLDImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new FDT_WKLDImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

    /**
     * Check to see if Recurring Transcation Flag is set for
     * given Document Code on DCTRL table.
     */

    public boolean isCurrTranAllowed()
    {
       String lsDocCd = this.getDOC_CD();
       boolean isTranAlwd = true;
       int liDocVersNo = getDOC_VERS_NO();
       R_GEN_DOC_CTRLImpl loDctrl  = R_GEN_DOC_CTRLImpl.getDocControlInfo(lsDocCd,session);
       int liTriggerType = getData( ATTR_DOC_S_ACTN_CD ).getint();
       Session loSession = getSession();
       boolean lboolInvalidDoc  = false;

       if (loDctrl == null)
       {
          raiseException("This Document Code doesn't Exist in DCTRL table");
          isTranAlwd = false;
       }
       else if (!loDctrl.getRCUR_TRAN_ALW_FL())
       {
          raiseException("Recurring Transaction Flag is False on DCTRL for document code "+lsDocCd );
          isTranAlwd = false;
       }

       if(isTranAlwd)
       {
          SearchRequest lsrDocHdr = new SearchRequest();
          lsrDocHdr.addParameter("DOC_HDR", "DOC_CD", lsDocCd);
          lsrDocHdr.addParameter("DOC_HDR", "DOC_DEPT_CD", getDOC_DEPT_CD());
          lsrDocHdr.addParameter("DOC_HDR", "DOC_ID", getDOC_ID());
          if(liDocVersNo == 0)
          {
             liDocVersNo = 1;
          }
          if (liTriggerType == AMSDocAppConstants.DOC_SUB_ACTN_RECLASSIFICATION)
          {
             lsrDocHdr.addParameter("DOC_HDR", "DOC_FUNC_CD", Integer.toString(DOC_FUNC_CANCELED));
             int liObjCnt = DOC_HDRImpl.getObjectCount(lsrDocHdr, loSession);
             if (liObjCnt > 0)
             {
                lboolInvalidDoc = true;
             }
          }
          else
          {
             lsrDocHdr.addParameter("DOC_HDR", "DOC_VERS_NO", Integer.toString(liDocVersNo));
             DOC_HDRImpl loDocHdr = (DOC_HDRImpl)DOC_HDRImpl.getObjectByKey(lsrDocHdr, loSession);
             if (loDocHdr == null)
             {
                lboolInvalidDoc = true;
             }
          }
          if (lboolInvalidDoc)
          {
             raiseException("%c:A6250");
             isTranAlwd = false;
          }
       }
       return isTranAlwd ;
   }

}

