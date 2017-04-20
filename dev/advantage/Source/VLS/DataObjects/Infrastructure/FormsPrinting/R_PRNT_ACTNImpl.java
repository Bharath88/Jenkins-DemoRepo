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
**  R_PRNT_ACTN
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_PRNT_ACTNImpl extends  R_PRNT_ACTNBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_PRNT_ACTNImpl (){
		super();
	}
	
	public R_PRNT_ACTNImpl(Session session, boolean makeDefaults)
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
		public static R_PRNT_ACTNImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_PRNT_ACTNImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
   /**
    * This method returns the Hide Inactive Procurement Lines record for 
    * a given Document Type and Document Code.
    * 
    * @param fsDocType the document type
    * @param fsDocCode the document code
    * @param foSession the current session
    */
   public static R_PRNT_ACTNImpl getInactiveProcurmentRecord(String fsDocType,
                                                      String fsDocCode,
                                                      Session foSession)
   {
      SearchRequest loSearchRequest = new SearchRequest();
      loSearchRequest.addParameter("R_PRNT_ACTN",
            AMSCommonConstants.ATTR_DOC_TYP,
            fsDocType);
      loSearchRequest.addParameter("R_PRNT_ACTN",
            AMSCommonConstants.ATTR_DOC_CD, fsDocCode);
      loSearchRequest.addParameter("R_PRNT_ACTN",
            "PRNT_ACTN",
            Integer.toString(CVL_PRNT_ACTNImpl.HIDE_INACTIVE_LINES));
      
      return (R_PRNT_ACTNImpl)
            R_PRNT_ACTNImpl.getObjectByKey(loSearchRequest, foSession);
      
   }
}

