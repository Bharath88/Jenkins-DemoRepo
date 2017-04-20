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
**  DOC_MSG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class DOC_MSGImpl extends  DOC_MSGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

   /**
    * The length of the Message Text attribute. It can be obtained from the metaquery but is
    * hardcoded here to minimize overhead when needed for frequent use.
    */
   public static final int MSG_TXT_SIZE = 1500;

   public static final int MSG_CD_SIZE = 5;
//{{COMP_CLASS_CTOR
public DOC_MSGImpl (){
	super();
}

public DOC_MSGImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
   //Write Event Code below this line
}
//END_COMP_EVENT_afterInsert}}

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   commonBeforeLogic();
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
   commonBeforeLogic();
}
//END_COMP_EVENT_beforeUpdate}}

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static DOC_MSGImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new DOC_MSGImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}


   /**
    * Overrides BaseImpl's parent check to the Document Code table to prevent
    * Error A761 "Document Code does not exist in the Document Code table".
    */
   protected void parentCheckFor_DocMsgToDocCd()
   //@SuppressAbstract
   {
      return;
   }

   /**
    * Overrides BaseImpl's parent check to the Message table to prevent
    * Error A761 "Message Code does not exist in the Message table".
    */
   protected void parentCheckFor_DocMsgToMsg()
   //@SuppressAbstract
   {
      return;
   }

   /**
    * Overrides BaseImpl's parent check to the Message table to prevent
    * Error A761 "IM Service Flow Sequence does not exist in the IM Service Flow table".
    */
   protected void parentCheckFor_DocMsgToIMSrvcFlow()
   //@SuppressAbstract
   {
      return;
   }


   /**
    * Actions both before insert and update
    */
   public void commonBeforeLogic()
   {
      int liApplID = 0;

      // Default Application ID
      if (getAPPL_ID() == 0)
      {
         try
         {
            liApplID = AMSStringUtil.convertStringToInt(AMSParams.msPrimaryApplication);

            // Let value stay null if not valid
            if (liApplID > 0)
            {
               setAPPL_ID(liApplID);
            }
         }
         catch (Exception foException)
         {
            liApplID = 0;
         }
      } // end if (getAPPL_ID() == 0)
   } // end commonBeforeLogic


   /**
    * Sets Component Name, Doc Code, Doc Dept Code, Doc ID, Doc Version, Message ID,
    * Severity Level, and Message Text from input value.
    *
    * @param foErrorMessage - an instance of AdvantageBusinessErrorMessage.
    */
   public void copyFrom(AdvantageBusinessErrorMessage foErrorMessage)
   {
      if (foErrorMessage == null)
      {
         return;
      }

      copyFrom(foErrorMessage.getAttributeID(), foErrorMessage.getComponentName(),
            foErrorMessage.getMsgID(), foErrorMessage.getErrorMsg(),
            foErrorMessage.getOverride(), foErrorMessage.getErrorContext(),
            foErrorMessage.getSeverityLevel());
   } // end copyFrom()


   /**
    * Sets Component Name, Doc Code, Doc Dept Code, Doc ID, Doc Version, Message ID,
    * Severity Level, and Message Text from input value.
    *
    * @param foErrorMessage - an instance of AMSErrorMsg.
    */
   public void copyFrom(AMSErrorMsg foErrorMessage)
   {
      copyFrom(foErrorMessage.getAttributeID(), foErrorMessage.getComponentName(),
            foErrorMessage.getMsgID(), foErrorMessage.getErrorMsg(),
            foErrorMessage.getOverride(), foErrorMessage.getErrorContext(),
            foErrorMessage.getSeverityLevel());
   } // end copyFrom()


   /**
    * Sets Component Name, Doc Code, Doc Dept Code, Doc ID, Doc Version, Message ID,
    * Severity Level, and Message Text from input values.
    *
    * @param fsAttributeID - Attribute ID
    * @param fsComponentName - Component Name
    * @param fsMsgID - Message ID
    * @param fsErrorMsg - Error Message
    * @param fsOverride - Override Level
    * @param fsErrorContext - Error Context:  Doc Code, Doc Dept Code, Doc ID, Doc Version
    * @param fsSeverityLevel - Severity Level
    */
   public void copyFrom(String fsAttributeID, String fsComponentName, String fsMsgID,
         String fsErrorMsg, String fsOverride, String fsErrorContext, String fsSeverityLevel)
   {
      int      liLength = 0;
      int      liValue  = 0;
      String   lsValue  = null;
      String[] loList   = null;

      // Attribute Name
      setATTR_NM(fsAttributeID);

      // Component Name
      setCOMP_NM(fsComponentName);

      // Message Code
      if (fsMsgID          != null &&
          fsMsgID.length() <= MSG_CD_SIZE)
      {
         setMSG_CD(fsMsgID);
      }

      // Error Message
      if (fsErrorMsg          != null &&
          fsErrorMsg.length() <= MSG_TXT_SIZE)
      {
         setMSG_TXT(fsErrorMsg);
      }

      // Override
      if (!AMSStringUtil.strIsEmpty(fsOverride))
      {
         try
         {
            liValue = Integer.parseInt(fsOverride);
            setOV_LVL(liValue);
         }
         catch(NumberFormatException foNumberFormatException)
         {
            // Allow Override to remain null to distinguish zero from blank value.
         }
      }

      /*
       * Split Error Context into Doc Code, Doc Dept Code, Doc ID, Doc Version.
       *
       * Example of value expected for fsErrorContext:
       * "DOC_CD = XXX AND DOC_DEPT_CD = XXX AND DOC_ID = XXX001 AND DOC_VERS_NO = 1"
       */
      lsValue = fsErrorContext;

      if (!AMSStringUtil.strIsEmpty(fsErrorContext))
      {
         // First split input by "AND" into attribute/value pairs.
         loList   = fsErrorContext.toUpperCase().split(" AND ");
         liLength = loList.length;

         for (int liIndex = 0; liIndex < liLength; liIndex++)
         {
            // Ignore any input that is not "(attribute) = (value)"
            if (loList[liIndex].indexOf("=") == -1)
            {
               continue;
            }

            // For each attribute/value pair, split by "=".
            lsValue = loList[liIndex].split("=")[1].trim();

            if (loList[liIndex].indexOf("DOC_CD") == 0)
            {
               setDOC_CD(lsValue);
            }

            if (loList[liIndex].indexOf("DOC_DEPT_CD") == 0)
            {
               setDOC_DEPT_CD(lsValue);
            }

            if (loList[liIndex].indexOf("DOC_ID") == 0)
            {
               setDOC_ID(lsValue);
            }

            if (loList[liIndex].indexOf("DOC_VERS_NO") == 0)
            {
               try
               {
                  liValue = Integer.parseInt(lsValue);
                  setDOC_VERS_NO(liValue);
               }
               catch(NumberFormatException foNumberFormatException)
               {
                  // Allow Doc Version Number to remain null to distinguish zero from blank value.
               }
            }
         } // end for (int liIndex = 0; liIndex < liLength; liIndex++)
      } // end if (!AMSStringUtil.strIsEmpty(fsErrorContext))

      if (!AMSStringUtil.strIsEmpty(fsSeverityLevel))
      {
         try
         {
            liValue = Integer.parseInt(fsSeverityLevel);
            setMSG_SEV(liValue);
         }
         catch(NumberFormatException foNumberFormatException)
         {
            // Allow Severity Level to remain null to distinguish zero from blank value.
         }
      }
   } // end copyFrom()


  /**
   * Overridde of java.lang.Object.toString() to return relevant information
   *
   * @return - Message context and details
   */
   public String toString()
   {
      StringBuffer loMessage = null;

      loMessage = new StringBuffer().append("\n");

      // Message Code
      if (getMSG_CD() != null)
      {
         loMessage.append("Message Code: " + getMSG_CD() + "\n");
      }

      // Severity Level
      if (getMSG_SEV() >= 0)
      {
          switch (getMSG_SEV())
          {
            case AMSMsgUtil.SEVERITY_LEVEL_INFO:
               loMessage.append("Severity Level: INFO\n");
            case AMSMsgUtil.SEVERITY_LEVEL_WARNING:
               loMessage.append("Severity Level: WARNING\n");
            case AMSMsgUtil.SEVERITY_LEVEL_ERROR:
               loMessage.append("Severity Level: ERROR\n");
            case AMSMsgUtil.SEVERITY_LEVEL_SEVERE:
               loMessage.append("Severity Level: SEVERE\n");
         } // end switch (liIndex)
      }

      // Override
      if (getOV_LVL() >= 0)
      {
         loMessage.append("Override: " + getOV_LVL() + "\n");
      }

      // Message
      if (getMSG_TXT() != null)
      {
         loMessage.append("Message: " + getMSG_TXT() + "\n");
      }

      // Component Name
      if (getCOMP_NM() != null)
      {
         loMessage.append("Component Name: " + getCOMP_NM() + "\n");
      }

      // Attribute ID
      if (getATTR_NM() != null)
      {
         loMessage.append("AttributeID: " + getATTR_NM() + "\n");
      }

      // Error Context
      if (getDOC_ID() != null)
      {
         loMessage.append("Error Context: ");
         loMessage.append("DOC_CD = "      + getDOC_CD()      + " AND ");
         loMessage.append("DOC_DEPT_CD = " + getDOC_DEPT_CD() + " AND ");
         loMessage.append("DOC_ID = "      + getDOC_ID()      + " AND ");
         loMessage.append("DOC_VERS_NO = " + getDOC_VERS_NO() + "\n");
      }

      return loMessage.toString();
   } // end method toString()
   
      /**
       * Look up Message Text in Message (MESG) table by Message Code.  There is a message exactly
       * like this on R_MSG, but this method returns empty string instead of null.
       *
       * @param fsMsgCd - Message Code
       * @param foSession - Handle to caller's session
       * @return Message Explanation if record is found, otherwise empty string.
       */
      public static String getMsgExpl(String fsMsgCd, Session foSession)
      {
         R_MSGImpl     loMsg           = null; 
         SearchRequest loSearchRequest = null;
         String        lsMsgExpl       = null;
   
         loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter("R_MSG", "MSG_CD", fsMsgCd);
   
         loMsg = (R_MSGImpl) R_MSGImpl.getObjectByKey(loSearchRequest, foSession);
   
         if (loMsg != null)
         {
            lsMsgExpl = loMsg.getMSG_EXPL();
   
            if (lsMsgExpl != null)
            {
               return lsMsgExpl;
            }
         }
   
         return "";
   } // end getMsgExpl()
}