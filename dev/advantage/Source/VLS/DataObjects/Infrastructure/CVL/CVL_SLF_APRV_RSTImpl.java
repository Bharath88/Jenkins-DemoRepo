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
**  CVL_SLF_APRV_RST
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_SLF_APRV_RSTImpl extends  CVL_SLF_APRV_RSTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   // default value for the flag in other tables.
   public static final String SLF_APRV_RSCT_NONE = "1";
   public static final String SLF_APRV_RSCT_CREA = "2";
   public static final String SLF_APRV_RSCT_SBMT = "3";
   public static final String SLF_APRV_RSCT_BOTH = "4";

//{{COMP_CLASS_CTOR
public CVL_SLF_APRV_RSTImpl (){
	super();
}

public CVL_SLF_APRV_RSTImpl(Session session, boolean makeDefaults)
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
	public static CVL_SLF_APRV_RSTImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_SLF_APRV_RSTImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}


   public static boolean isValidSlfAprvRSCT(String fsValue, Session foSession)
   {
      if(AMSStringUtil.strIsEmpty(fsValue))
      {
         return false;
      }
      DataObject loCode = null;
      SearchRequest loSearch = new SearchRequest();
      Parameter   loParm   = new Parameter();
      loParm.objName = "CVL_SLF_APRV_RST";
      loParm.fieldName = "SLF_APRV_RST_SV";
      loParm.value = fsValue;
      loSearch.add(loParm);
      loCode = CVL_SLF_APRV_RSTImpl.getObjectByKey(loSearch, foSession);
      return loCode==null?false:true;
   }
}

