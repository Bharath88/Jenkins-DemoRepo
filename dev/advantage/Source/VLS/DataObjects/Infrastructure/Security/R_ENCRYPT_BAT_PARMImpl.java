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
 **  R_ENCRYPT_BAT_PARM
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_ENCRYPT_BAT_PARMImpl extends  R_ENCRYPT_BAT_PARMBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_ENCRYPT_BAT_PARMImpl (){
	super();
}

public R_ENCRYPT_BAT_PARMImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}
      
   }
   
//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
   public void beforeInsert(DataObject obj, Response response)
   {
      //Write Event Code below this line
  
      /**
       * Call method commonBeforeLogic()
       * to do some common processing 
       * while inserting
       */  
      commonBeforeLogic();
   }
   
   
   
   
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
   public void beforeUpdate(DataObject obj, Response response)
   {
      //Write Event Code below this line
      
      /**
       * Call method commonBeforeLogic()
       * to do some common processing 
       * while updating
       */
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
	public static R_ENCRYPT_BAT_PARMImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_ENCRYPT_BAT_PARMImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   /**
   * This method gets the associated FTP password based on the PARM ID from
   * Encrypted batch parameter table. This method is invoked 
   * mainly from processes like DPC Export, ADVToVSS 
   * which need FTP password for FTPing files.
   * @param fsParmId - Parm ID 
   * @param foSession - Handle to session.
   * @return String - Associated FTP Password for passed Parm Id.
   */
   public static String getPassword(String fsParmId, Session foSession)
   {
      String lsPassword = null;
      if(!AMSStringUtil.strIsEmpty(fsParmId))
      {
         SearchRequest loSearchReq = new SearchRequest();
         
         loSearchReq.addParameter ("R_ENCRYPT_BAT_PARM","PARM_ID",fsParmId);
         R_ENCRYPT_BAT_PARMImpl loRec =  (R_ENCRYPT_BAT_PARMImpl) 
            R_ENCRYPT_BAT_PARMImpl.getObjectByKey(loSearchReq, foSession);
         
         if(loRec != null)
         {
            lsPassword = loRec.getPARM_VL_VIEW();
         }
      }
      return lsPassword;
   }
   
   /**
    * This method does some common processing before inserting or updating.
    *
    * This method is called within methods beforeUpdate() and beforeInsert()
    * 
    * The method checks the Parameter Value is null or not.
    * If the Parameter Value is null then error is raised.
    * 
    * The mathod checks for valid combination of Unit and Department.
    * If the combination of Department and Unit is invalid then error is raised.
    * 
    * @return void.
    */
   
   public void commonBeforeLogic()
   {
      // Get the Parameter Value.
      String lsParamValueView = getPARM_VL_VIEW();
      
      /* Check if the paramter value is null. */
      if (AMSStringUtil.strIsEmpty(lsParamValueView))
      {
         raiseException("%c:Q0058,v:[b:R_ENCRYPT_BAT_PARM/PARM_VL]%");
      }// End  if (AMSStringUtil.strIsEmpty(lsParamValueView))
      
      // Get the value of Department.
      String lsDept = getDEPT_CD();
      
      // Get the value of Unit.
      String lsUnit = getUNIT_CD();
      
      /* Check whether the entered Unit and Department are of valid combination*/ 
			if ((!AMSStringUtil.strIsEmpty(lsDept) || 
            !AMSStringUtil.strIsEmpty(lsUnit)) && 
            !(R_UNITImpl.isValidUnit(this.getSession(), lsDept, lsUnit)))
      {
         raiseException("%c:Q0111%");
      }// End if
			
   } // End commonBeforeLogic
		
   
   
   
}

