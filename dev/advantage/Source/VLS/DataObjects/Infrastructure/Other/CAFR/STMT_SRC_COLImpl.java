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
**  STMT_SRC_COL
*/

//{{COMPONENT_RULES_CLASS_DECL
public class STMT_SRC_COLImpl extends  STMT_SRC_COLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public STMT_SRC_COLImpl (){
		super();
	}
	
	public STMT_SRC_COLImpl(Session session, boolean makeDefaults)
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
		public static STMT_SRC_COLImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new STMT_SRC_COLImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

   /**
    * Fetches the Reference Table value from STMT_REF_TBL table.
    */
   public String getRefTable()
   {
      if( AMSStringUtil.strIsEmpty(getSRC_TBL()) || AMSStringUtil.strIsEmpty(getREF_TBL_CD()) )
         return null;

      SearchRequest lsrRefTbl = new SearchRequest();
      lsrRefTbl.addParameter("STMT_REF_TBL", "SRC_TBL", getSRC_TBL());
      lsrRefTbl.addParameter("STMT_REF_TBL", "REF_TBL_CD", getREF_TBL_CD());
      STMT_REF_TBLImpl loRef = (STMT_REF_TBLImpl)STMT_REF_TBLImpl.getObjectByKey(lsrRefTbl, getSession());
      if(loRef != null)
          return loRef.getREF_TBL();
      
      return null;
   }

   /**
    * Fetches the Reference Table Name value from STMT_REF_TBL table.
    */
   public String getRefTableName()
   {
      if( AMSStringUtil.strIsEmpty(getSRC_TBL()) || AMSStringUtil.strIsEmpty(getREF_TBL_CD()) )
         return null;

      SearchRequest lsrRefTbl = new SearchRequest();
      lsrRefTbl.addParameter("STMT_REF_TBL", "SRC_TBL", getSRC_TBL());
      lsrRefTbl.addParameter("STMT_REF_TBL", "REF_TBL_CD", getREF_TBL_CD());
      STMT_REF_TBLImpl loRef = (STMT_REF_TBLImpl)STMT_REF_TBLImpl.getObjectByKey(lsrRefTbl, getSession());
      if(loRef != null)
         return loRef.getREF_TBL_NM();
      
      return null;
   }	
}

