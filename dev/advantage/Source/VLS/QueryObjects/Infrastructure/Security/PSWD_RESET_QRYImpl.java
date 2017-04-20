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

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;


import org.apache.commons.logging.Log;

/*
 **  PSWD_RESET_QRY
 */

//{{COMPONENT_RULES_CLASS_DECL
public class PSWD_RESET_QRYImpl extends  PSWD_RESET_QRYBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}
	
	private static Log moAMSLog = AMSLogger.getLog( 
       PSWD_RESET_QRYImpl.class,
       AMSLogConstants.FUNC_AREA_SECURITY ) ;

	/**
	 * This method overrides the super class's save method
	 * to cancel the save and perform the password reset
	 * operation instead.
	 *
	 */
	public void save() throws ServerException
	{
		boolean lboolResetFl = getData("RESET_FL").getboolean() ;
		if ( lboolResetFl )
		{
			String lsUserID = getData( "USER_ID").getString() ;
			String lsPswd   = getData( "PSWD_QUERY_ANSW" ).getString() ;

			getData("RESET_FL").setboolean( false ) ;
			try
			{
				AMSSecurity.resetUserPassword( lsUserID, lsPswd, getSession() ) ;
			}
			catch( AMSSecurityException foExp )
			{
                // Add exception log to logger object
                moAMSLog.error("Unexpected error encountered while processing. ", foExp);

				//BGN ADVHR00010187
				//throw new AMSRTSecurityException( foExp.getMessage() ) ;
				AMSDataObject.raiseException(foExp.getMessage() , getSession());
				//END ADVHR00010187
			} /* end catch( AMSSecurityException foExp ) */
		} /* end if ( lboolResetFl ) */

	} /* end save() */

}

