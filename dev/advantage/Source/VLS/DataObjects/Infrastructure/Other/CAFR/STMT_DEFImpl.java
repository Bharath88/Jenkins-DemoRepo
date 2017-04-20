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
**  STMT_DEF
*/

//{{COMPONENT_RULES_CLASS_DECL
public class STMT_DEFImpl extends  STMT_DEFBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public STMT_DEFImpl (){
		super();
	}
	
	public STMT_DEFImpl(Session session, boolean makeDefaults)
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
		public static STMT_DEFImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new STMT_DEFImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	/**
	 * Method to check if Fiscal Year entered is valid. A valid FY is one which is present
	 * on the DIM_FY table of InfoAdvantage DB.
	 */
	public boolean isFYValid()
	{
		boolean lboolFYValid=false;
		StringBuffer lsbSQL = new StringBuffer(128);
		lsbSQL.append("SELECT 1 FROM ");
		lsbSQL.append(AMSDataObject.getSchema("STMT_DEF",getSession()));
		lsbSQL.append(".DIM_FY ");
		lsbSQL.append("where FY=");
		lsbSQL.append(getSTMT_FY());
		lsbSQL.append(" ");
		String lsDServer = getSession().getDataServerForObject("STMT_DEF");
		ResultSet loResultSet = null;
		try
		{
			loResultSet = (ResultSet)getSession().getResultSetBySQL(lsbSQL.toString(), lsDServer,1);
			if(loResultSet != null)
			{
				DataRow loSlry = loResultSet.fetch();
				if(loSlry != null)
				{
					lboolFYValid=true;
				}
			}
		}
		catch(Exception loException)
		{
			System.err.println("Error while reading column FY from table DIM_FY.");
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loException);
		}
		finally
		{
			if(loResultSet != null)
			{
			   loResultSet.close();
			}
		}
		return lboolFYValid;
	}
}

