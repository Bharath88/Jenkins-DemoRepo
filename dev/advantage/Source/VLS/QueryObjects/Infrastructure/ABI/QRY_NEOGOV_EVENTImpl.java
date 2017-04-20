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
**  QRY_NEOGOV_EVENT
*/

//{{COMPONENT_RULES_CLASS_DECL
public class QRY_NEOGOV_EVENTImpl extends  QRY_NEOGOV_EVENTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}
   public static void beforeResultSetFillAfterSecurityCheck(
         DataRow foRowToBeAdded, Response foResponse )
   {
      Session loSession = foRowToBeAdded.getBusinessObject().getSession();
      /*
       * Do not include the Events which have Transact Id as 0 and Source Transact Event Id populated
       * while calculating the Reprocessing Count and Reprocessing Success Count.
       */
	Data loSrcTransID = foRowToBeAdded.getData("SOURCE_TRANSACT_ID");
      if(( foRowToBeAdded.getData( "TRANSACT_ID" ).getint() != 0 ))
      {

	   if (loSrcTransID == null || loSrcTransID.getString() == null)
	   {
		

            int liEventId = foRowToBeAdded.getData( "TRANSACT_ID" ).getint();
		String lsEventID = Integer.toString(liEventId );
            SearchRequest loSearchRequest = new SearchRequest();
            loSearchRequest.addParameter( "NEOGOV_EVENT", "SOURCE_TRANSACT_ID", lsEventID);
            int liRprcCnt = NEOGOV_EVENTImpl.getObjectCount( loSearchRequest,
               loSession );
            foRowToBeAdded.getData( "RPRC_CNT" ).setint( liRprcCnt );

            loSearchRequest = new SearchRequest();
            loSearchRequest.addParameter( "NEOGOV_EVENT", "SOURCE_TRANSACT_ID", lsEventID );
            loSearchRequest.addParameter( "NEOGOV_EVENT", "EVENT_STATUS", "S" );
            int liRprcSuccsCnt = NEOGOV_EVENTImpl.getObjectCount(
               loSearchRequest, loSession );
            foRowToBeAdded.getData( "RPRC_SUCCS_CNT" ).setint( liRprcSuccsCnt );
	   }

      }
   }
}

