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
 **  R_DTPL
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_DTPLImpl extends  R_DTPLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   private static String C_FILE = "File";
   private static String C_MAXEXCEEDED = "MaxExceeded";
   private static String C_MAX_DL_LINES = "MAX_DL_LINES";
   private static String C_MAX_DOWNLOAD_LINES = "MAX_DOWNLOAD_LINES";
//{{COMP_CLASS_CTOR
public R_DTPLImpl (){
	super();
}

public R_DTPLImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}
      
   }
   
//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
   public void beforeInsert(DataObject obj, Response response)
   {
      //Write Event Code below this line
      commonBeforeLogic();
   }
   
   
   
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
   public void beforeUpdate(DataObject obj, Response response)
   {
      try
      {
         Session loSession = getSession();
         String lsDocId = loSession.getProperty(AMSCommonConstants.ATTR_DOC_ID);
         if (!AMSStringUtil.strIsEmpty(lsDocId) )
         {
            Object[] loFile = new Object[2];
            
            AMSDocumentExcelDownloadGenerator loGen = new AMSDocumentExcelDownloadGenerator();
            int liSuccess = loGen.generate(loSession, obj, loFile);
            if (liSuccess == 1)
            {
               loSession.setPropertyAsObject(C_FILE, loFile);
            }
            else if (liSuccess == 2)
            {
               loSession.setProperty(C_MAXEXCEEDED, "The number of document component lines exceeds the limit set on Document Download Template (DDTPL).");
            }
         }
         else
         {
            commonBeforeLogic();
         }
      }
      catch (Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);
      }
   }
   
   
   
//END_COMP_EVENT_beforeUpdate}}

//END_EVENT_CODE}}
   
   
   
   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addRuleEventListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }
   
//{{COMPONENT_RULES
	public static R_DTPLImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_DTPLImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   
   /**
   * This method is called before insert and update logic
   */
   public void commonBeforeLogic()
   {
      String lsMaxDownloadLines = IN_APP_CTRLImpl.getParameterValue(C_MAX_DOWNLOAD_LINES, getSession());
      
      if (! AMSStringUtil.strIsEmpty(lsMaxDownloadLines))
      {
         int liMAX_DOWN_LINE = Integer.parseInt(lsMaxDownloadLines);
         
         if (!isNull(C_MAX_DL_LINES))
         {
            int liMaxDlLines = getMAX_DL_LINES();
            if(liMaxDlLines > liMAX_DOWN_LINE)
               this.raiseException("Max Download Lines is Invalid.  The Max Download Lines value exceeds the upper limit",AMSMsgUtil.SEVERITY_LEVEL_ERROR);
            return;
         }
         
         else
         {
            setMAX_DL_LINES(liMAX_DOWN_LINE);
         }
      }
   }
   
}

