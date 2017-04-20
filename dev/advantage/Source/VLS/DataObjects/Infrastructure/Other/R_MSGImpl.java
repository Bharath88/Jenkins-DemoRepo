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
**  MESSAGE
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_MSGImpl extends  R_MSGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_MSGImpl (){
		super();
	}
	
	public R_MSGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_MSGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_MSGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	
   public static int getOverrideLevel( Session foSession, String fsMessageCode )
   {
       SearchRequest lsr = new SearchRequest();
       lsr.addParameter("R_MSG", "MSG_CD", fsMessageCode);

       R_MSGImpl loMessage = (R_MSGImpl) R_MSGImpl.getObjectByKey(lsr, foSession);

       if (loMessage == null)
       {
           return -1;
       }
       else
       {
           return loMessage.getOV_LVL();
       }
   }

   /**
   * This method will return detailed explanation for the error code passed.
   * @param foSession - Session.
   * @param fsErrorCode - Error code for which explanation is required.
   * @return Message explanation.
   */
  public static String getMsgExpl( Session foSession ,String fsErrorCode )
  {

	  SearchRequest lsr = new SearchRequest();
	  lsr.addParameter("R_MSG", "MSG_CD", fsErrorCode);

	  R_MSGImpl loMessage = (R_MSGImpl) R_MSGImpl.getObjectByKey(lsr,foSession);

	  return loMessage.getMSG_EXPL();
  }
}
