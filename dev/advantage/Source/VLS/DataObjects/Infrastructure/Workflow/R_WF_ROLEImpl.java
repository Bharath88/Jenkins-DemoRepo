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
**  R_WF_ROLE
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_ROLEImpl extends  R_WF_ROLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_WF_ROLEImpl (){
	super();
}

public R_WF_ROLEImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_WF_ROLEImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_WF_ROLEImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
   //Write Event Code below this line
   if(isChanged("ROLEID"))
   {
      wklstRecrodExist();
   }
}
//END_COMP_EVENT_beforeUpdate}}

//{{COMP_EVENT_beforeDelete
public void beforeDelete(DataObject obj, Response response)
{
   //Write Event Code below this line
   wklstRecrodExist();
}
//END_COMP_EVENT_beforeDelete}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

   public void myInitializations()
   {
      setSecurityOrgPrefixes( "HOME" ) ;
   }

   public void wklstRecrodExist()
   {
      StringBuffer loSrchRqst = new StringBuffer();
      String lsViewNm = "V_WF_APRV_WRK_LST";
      String lsTableName = "WF_APRV_WRK_LST";
      ResultSet loResultSet = null;
      Session loSession = getSession();
      DataRow loRow = null;

      loSrchRqst.append("SELECT COUNT(*) FROM "
            + AMSDataObject.getSchema(lsTableName, loSession) + "." + lsViewNm
            + " WHERE "
            + lsViewNm + ".ASSIGNEE "
            + AMSSQLUtil.getANSIQuotedStr(getOldROLEID(),
                  AMSSQLUtil.EQUALS_OPER)
            + " AND "
            + lsViewNm + ".ASSIGNEE_FL = 1");

      try
      {
         loResultSet = getSession().getResultSetBySQL(loSrchRqst.toString(),
               loSession.getDataServerForObject(lsTableName), 1);

         if(((loRow = loResultSet.fetch())!= null)
               && (loRow.getData(1).getint() > 0))
         {
            raiseException("%c:Q0164%");
         }
      }
      catch (Exception foException)
      {
         raiseException("Exception Ocurred while generating the Resultset: "+foException);
      }
      finally
      {
         if(loResultSet != null)
         {
            loResultSet.close();
            loResultSet = null;
         }
      }
   }
}
