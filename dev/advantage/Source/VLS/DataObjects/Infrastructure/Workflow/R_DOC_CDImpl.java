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
**  R_DOC_CD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_DOC_CDImpl extends  R_DOC_CDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_DOC_CDImpl (){
		super();
	}
	
	public R_DOC_CDImpl(Session session, boolean makeDefaults)
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
		public static R_DOC_CDImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_DOC_CDImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}


   /**
    * Gets a record by Document Code (by primary key).
    *
    * @param fsDocCd - Document Code
    * @param foSession - handle to caller's session
    * @return Document Code record or null if not found
    */
   public static R_DOC_CDImpl getRecord(String fsDocCd, Session foSession)
   {
      SearchRequest loSearchRequest = null;

      if (fsDocCd   == null ||
          foSession == null)
      {
         return null;
      }

      loSearchRequest = new SearchRequest();

      loSearchRequest.addParameter("R_DOC_CD", "DOC_CD", fsDocCd);

      return (R_DOC_CDImpl) R_DOC_CDImpl.getObjectByKey(loSearchRequest, foSession);
   } // end getRecord()
}

