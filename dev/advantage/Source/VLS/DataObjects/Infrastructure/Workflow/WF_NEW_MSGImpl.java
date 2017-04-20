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
import com.amsinc.gems.adv.common.AMSJavaMail;

/*
**  WF_COMPOSE_MSG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_NEW_MSGImpl extends  WF_NEW_MSGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   private static final String NEW_LINE = System.getProperty("line.separator");

	//{{COMP_CLASS_CTOR
	public WF_NEW_MSGImpl (){
		super();
	}
	
	public WF_NEW_MSGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static WF_NEW_MSGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new WF_NEW_MSGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

   public void myInitializations()
   {
      mboolCustomDataObjectSave = true ;
   }

	public void addHREF()
	{
	   try
	   {
	      String lsCurBody = getBODY();
	      String lsHREFInfo = getSession().getProperty("AMS_MAIL_HREF");

	      if (lsHREFInfo == null)
	      {
	         lsHREFInfo = "";
	      }
	      if ((lsCurBody == null) || lsCurBody.equals("") ||
	          lsCurBody.equals("null"))
	      {
	         lsCurBody = "";
	      }
	      else
	      {
            lsCurBody += NEW_LINE + NEW_LINE;
	      }
	      setBODY(lsCurBody + lsHREFInfo);
	   }
	   catch (Exception loEx)
	   {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loEx);

	   }
	}

   protected boolean myDataObjectSave()
   //@SuppressAbstract
   {
	   Session loSession = getSession();

	   // set in PLS on alert page; otherwise, just do normal email sending
	   String lsPropInboxCheck = "SessionEmailInboxCheck";
	   String lsPropValue = loSession.getProperty(lsPropInboxCheck);

	   // just check for existence of value
	   if ((lsPropValue != null) && !lsPropValue.equals(""))
	   {
	      boolean lboolNewMsg =
	            AMSJavaMail.hasNewMsgArrived((VSORBSessionImpl) loSession);

	      // existence of value is good enough
	      lsPropValue = (lboolNewMsg) ? "y" : "";
	      loSession.setProperty(lsPropInboxCheck, lsPropValue);
	   }
	   else
	   {
         //super.save();
         return true;
	   }
      return false;
	}
}
