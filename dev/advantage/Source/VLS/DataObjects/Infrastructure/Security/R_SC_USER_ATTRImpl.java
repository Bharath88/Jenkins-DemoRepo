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
**  R_SC_USER_ATTR
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_USER_ATTRImpl extends  R_SC_USER_ATTRBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_SC_USER_ATTRImpl (){
	super();
}

public R_SC_USER_ATTRImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   //Write Event Code below this line

   try {
      this.setREC_ID(AMSUniqNum.getUniqNum("R_SC_USER_ATTR"));
   } catch (AMSUniqNumException e) {

         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", e);

   }
}
//END_COMP_EVENT_beforeInsert}}

//END_EVENT_CODE}}



   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addRuleEventListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static R_SC_USER_ATTRImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_SC_USER_ATTRImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    *  Static method to get the User Id based on the External User Id parameter.
    */
   public static R_SC_USER_ATTRImpl getUserId(Session foSession , String fsExternalId)
   {
      SearchRequest loSearchReq = new SearchRequest();
      loSearchReq.addParameter("R_SC_USER_ATTR","ATTR_NM" , AMSCommonConstants.ATTR_NM_EXTARNAL_ID);
      loSearchReq.addParameter("R_SC_USER_ATTR","ATTR_VAL" , fsExternalId);
      R_SC_USER_ATTRImpl loUserAttr =
            (R_SC_USER_ATTRImpl)R_SC_USER_ATTRImpl.getObjectByKey(loSearchReq,foSession);
      return loUserAttr;
   }
}

