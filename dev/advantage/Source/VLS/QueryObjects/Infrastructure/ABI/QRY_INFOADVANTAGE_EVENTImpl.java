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
**  QRY_INFOADVANTAGE_EVENT
*/

//{{COMPONENT_RULES_CLASS_DECL
public class QRY_INFOADVANTAGE_EVENTImpl extends  QRY_INFOADVANTAGE_EVENTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}
   public static void beforeResultSetFillAfterSecurityCheck(
         DataRow foRowToBeAdded, Response foResponse )
   {
      BusinessObjectImpl loInfoAdvQuery = foRowToBeAdded.getBusinessObject();
      Session loSession = loInfoAdvQuery.getSession();
      /*
       * Do not include the Events which have Event Id as 0 and Source Event Id populated
       * while calculating the Reprocessing Count and Reprocessing Success Count.
       */
      if( ( foRowToBeAdded.getData( "EVENT_ID" ).getint() != 0 )
            && ( ( foRowToBeAdded.getData( "SOURCE_EVENT_ID" ).getString() == null ) ) )
      {
         int liEventId = foRowToBeAdded.getData( "EVENT_ID" ).getint();
         SearchRequest loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter( "INFOADVANTAGE_EVENT", "SOURCE_EVENT_ID", ""
               + liEventId );
         int liRprcCnt = INFOADVANTAGE_EVENTImpl.getObjectCount( loSearchRequest,
               loSession );
         foRowToBeAdded.getData( "RPRC_CNT" ).setint( liRprcCnt );

         loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter( "INFOADVANTAGE_EVENT", "SOURCE_EVENT_ID", ""
               + liEventId );
         loSearchRequest.addParameter( "INFOADVANTAGE_EVENT", "EVENT_STATUS", "S" );
         int liRprcSuccsCnt = INFOADVANTAGE_EVENTImpl.getObjectCount(
               loSearchRequest, loSession );
         foRowToBeAdded.getData( "RPRC_SUCCS_CNT" ).setint( liRprcSuccsCnt );

      }
   }

}

