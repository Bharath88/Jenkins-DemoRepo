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
**  VIEW_ENT_JOBS_QRY
*/

//{{COMPONENT_RULES_CLASS_DECL
public class VIEW_ENT_JOBS_QRYImpl extends  VIEW_ENT_JOBS_QRYBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

/**
 Used the "setUniqueKeyColumns" functionality on QueryObject to enable the
 Virtual ResultSet on Page, so that records are retrieved as and when Required.
 to prevent the page Lockup problem.
 */
 public static VSMetaQuery getMetaQuery()
 {
  VSMetaQuery Query = VIEW_ENT_JOBS_QRYBaseImpl.getMetaQuery();
  LinkedList list = new LinkedList();
  list.add(Query.getQueryColumn("AGNT_ID"));
  Query.setIdentityColumns(list);
  return Query;
 }//end of method

}

