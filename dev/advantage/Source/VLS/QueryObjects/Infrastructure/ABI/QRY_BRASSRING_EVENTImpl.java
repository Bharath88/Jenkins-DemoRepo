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
**  QRY_BRASSRING_EVENT
*/

//{{COMPONENT_RULES_CLASS_DECL
public class QRY_BRASSRING_EVENTImpl extends  QRY_BRASSRING_EVENTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}
   public static void beforeResultSetFillAfterSecurityCheck(
         DataRow foRowToBeAdded, Response foResponse )
   {
      BusinessObjectImpl loBrassRingQuery = foRowToBeAdded.getBusinessObject();
      Session loSession = loBrassRingQuery.getSession();
      /*
       * Do not include the Events which have Transact Id as 0 and Source Transact Id populated
       * while calculating the Reprocessing Count and Reprocessing Success Count.
       */
      if( ( foRowToBeAdded.getData( "TRANSACT_ID" ).getint() != 0 )
            && ( ( foRowToBeAdded.getData( "SOURCE_TRANSACT_ID" ).getString() == null ) ) )
      {
         int liEventId = foRowToBeAdded.getData( "TRANSACT_ID" ).getint();
         SearchRequest loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter( "BRASSRING_EVENT", "SOURCE_TRANSACT_ID", ""
               + liEventId );
         int liRprcCnt = BRASSRING_EVENTImpl.getObjectCount( loSearchRequest,
               loSession );
         foRowToBeAdded.getData( "RPRC_CNT" ).setint( liRprcCnt );

         loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter( "BRASSRING_EVENT", "SOURCE_TRANSACT_ID", ""
               + liEventId );
         loSearchRequest.addParameter( "BRASSRING_EVENT", "EVENT_STATUS", "S" );
         int liRprcSuccsCnt = BRASSRING_EVENTImpl.getObjectCount(
               loSearchRequest, loSession );
         foRowToBeAdded.getData( "RPRC_SUCCS_CNT" ).setint( liRprcSuccsCnt );

      }
   }

}

