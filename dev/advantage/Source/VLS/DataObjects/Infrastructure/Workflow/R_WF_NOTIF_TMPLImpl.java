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
**  R_WF_NOTIF_TMPL
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_NOTIF_TMPLImpl extends  R_WF_NOTIF_TMPLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /**
    * The SEQ_ID value for the system template when the 
    * document enters Pending phase
    */
   public static final long SEQ_ID_NOTIF_ORIG_DOC = 0L;

   /**
    * The SEQ_ID value for the system template when the 
    * document reaches a particular approval level
    */
   public static final long SEQ_ID_NOTIF_ORIG_APRV = 1L;

	//{{COMP_CLASS_CTOR
	public R_WF_NOTIF_TMPLImpl (){
		super();
	}
	
	public R_WF_NOTIF_TMPLImpl(Session session, boolean makeDefaults)
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
		public static R_WF_NOTIF_TMPLImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_WF_NOTIF_TMPLImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
   /**
    * This method returns the template’s description and extended description, 
    * which correspond to an e-mail subject and message body, respectively.  
    * If the template is not found, then null is returned.  Otherwise, a 
    * two-column array that contains the template information is returned.
    * 
    * @param foSession - The current session
    * @param flId - The ID of the template to retrieve.
    * @param foObj - The instance of the business object (AMSDataObject 
    *  or AMSQueryObject) that contains the necessary logic and data 
    *  for performing the parameter substitutions present in the 
    *  extended description (that is, the second column of the 
    *  returned array).  An AMSDocHeader or AMSDocComponent instance 
    *  could be present instead of an AMSDataObject instance.  Thus, 
    *  a specific Impl class is responsible for overriding the 
    *  replaceWfNotifParameters() method in AMSDataObject or AMSQueryObject 
    *  to do the substitutions.  This argument can be null.
    * @param foAddlParams - A map of additional parameters to be used during 
    *  replacement such that this information cannot be derived from foObj.  
    *  While the format is dependent on the overriding logic in 
    *  replaceWfNotifParameters(), the suggested format is to use the parameter 
    *  to be replaced as the key and of course the parameter’s value as the map’s 
    *  value.
    * @return - If the template is not found, then null is returned.
    *  Otherwise, a two-column array that contains the template information is returned. 
    *  @author vsharma 8/23/2006
    */
   public static String[] getTemplate(Session foSession, long flId,
         versata.vls.BusinessObjectImpl foObj, HashMap foAddlParams)
   {
      String[] lsTemplate = new String[2];
      SearchRequest loSrchReq = new SearchRequest();
      loSrchReq.addParameter("R_WF_NOTIF_TMPL", "SEQ_ID", Long.toString(flId));

      R_WF_NOTIF_TMPLImpl loTemplate = (R_WF_NOTIF_TMPLImpl) 
            R_WF_NOTIF_TMPLImpl.getObjectByKey(loSrchReq, foSession);

      if (loTemplate == null)
      {
         return null;
      }
      else
      {
         lsTemplate[0] = loTemplate.getDSCR();
         lsTemplate[1] = loTemplate.getEXT_DSCR();
         if (foObj != null)
         {
            if (foObj instanceof AMSDataObject)
            {
               AMSDataObject loDataObject = (AMSDataObject) foObj;
               lsTemplate[0] = loDataObject.replaceWfNotifParameters(lsTemplate[0],
                     foAddlParams);
               lsTemplate[1] = loDataObject.replaceWfNotifParameters(lsTemplate[1],
                     foAddlParams);
            }
            else if (foObj instanceof AMSQueryObject)
            {
               AMSQueryObject loDataObject = (AMSQueryObject) foObj;
               lsTemplate[0] = loDataObject.replaceWfNotifParameters(lsTemplate[0],
                     foAddlParams);
               lsTemplate[1] = loDataObject.replaceWfNotifParameters(lsTemplate[1],
                     foAddlParams);
            }
         }
         return lsTemplate;
      }
   }
   
   /**
    * This method returns the template’s description and extended description, 
    * which correspond to an e-mail subject and message body, respectively.  
    * If the template is not found, then null is returned.  Otherwise, a 
    * two-column array that contains the template information is returned.
    * Use this method when the template is to be retrieved using the Template Id
    * instead of Sequence Id.
    * 
    * @param foSession - The current session
    * @param fsNotifId - The Notification ID of the template to retrieve.
    * @param foObj - The instance of the business object (AMSDataObject 
    *  or AMSQueryObject) that contains the necessary logic and data 
    *  for performing the parameter substitutions present in the 
    *  extended description (that is, the second column of the 
    *  returned array).  An AMSDocHeader or AMSDocComponent instance 
    *  could be present instead of an AMSDataObject instance.  Thus, 
    *  a specific Impl class is responsible for overriding the 
    *  replaceWfNotifParameters() method in AMSDataObject or AMSQueryObject 
    *  to do the substitutions.  This argument can be null.
    * @param foAddlParams - A map of additional parameters to be used during 
    *  replacement such that this information cannot be derived from foObj.  
    *  While the format is dependent on the overriding logic in 
    *  replaceWfNotifParameters(), the suggested format is to use the parameter 
    *  to be replaced as the key and of course the parameter’s value as the map’s 
    *  value.
    * @return - If the template is not found, then null is returned.
    *  Otherwise, a two-column array that contains the template information is returned. 
    *  @author vsharma 8/23/2006
    */
   public static String[] getTemplate(Session foSession, String fsNotifId,
         versata.vls.BusinessObjectImpl foObj, HashMap foAddlParams)
   {
      String[] lsTemplate = new String[2];
      SearchRequest loSrchReq = new SearchRequest();
      loSrchReq.addParameter("R_WF_NOTIF_TMPL", "ALT_ID", fsNotifId);

      R_WF_NOTIF_TMPLImpl loTemplate = (R_WF_NOTIF_TMPLImpl) 
            R_WF_NOTIF_TMPLImpl.getObjectByKey(loSrchReq, foSession);

      if (loTemplate == null)
      {
         return null;
      }
      else
      {
         lsTemplate[0] = loTemplate.getDSCR();
         lsTemplate[1] = loTemplate.getEXT_DSCR();
         if (foObj != null)
         {
            if (foObj instanceof AMSDataObject)
            {
               AMSDataObject loDataObject = (AMSDataObject) foObj;
               lsTemplate[0] = loDataObject.replaceWfNotifParameters(lsTemplate[0],
                     foAddlParams);
               lsTemplate[1] = loDataObject.replaceWfNotifParameters(lsTemplate[1],
                     foAddlParams);
            }
            else if (foObj instanceof AMSQueryObject)
            {
               AMSQueryObject loDataObject = (AMSQueryObject) foObj;
               lsTemplate[0] = loDataObject.replaceWfNotifParameters(lsTemplate[0],
                     foAddlParams);
               lsTemplate[1] = loDataObject.replaceWfNotifParameters(lsTemplate[1],
                     foAddlParams);
            }
         }
         return lsTemplate;
      }
   }// end of method
}// end of class

