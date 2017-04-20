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
**  QRY_MAXIMO_EVENT
*/

//{{COMPONENT_RULES_CLASS_DECL
public class QRY_MAXIMO_EVENTImpl extends  QRY_MAXIMO_EVENTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}
   public static void beforeResultSetFillAfterSecurityCheck(
         DataRow foRowToBeAdded, Response foResponse )
   {
      BusinessObjectImpl loMaxiomoQuery = foRowToBeAdded.getBusinessObject();
      Session loSession = loMaxiomoQuery.getSession();
      /*
       * Do not include the Events which have Message Id as 0 and Source Message Id populated
       * while calculating the Reprocessing Count and Reprocessing Success Count.
       */
      if( ( foRowToBeAdded.getData( "MESSAGEID" ).getint() != 0 )
            && ( ( foRowToBeAdded.getData( "SOURCE_MESSAGEID" ).getString() == null ) ) )
      {
         int liEventId = foRowToBeAdded.getData( "MESSAGEID" ).getint();
         SearchRequest loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter( "MAXIMO_EVENT", "SOURCE_MESSAGEID", ""
               + liEventId );
         int liRprcCnt = MAXIMO_EVENTImpl.getObjectCount( loSearchRequest,
               loSession );
         foRowToBeAdded.getData( "RPRC_CNT" ).setint( liRprcCnt );

         loSearchRequest = new SearchRequest();
         loSearchRequest.addParameter( "MAXIMO_EVENT", "SOURCE_MESSAGEID", ""
               + liEventId );
         loSearchRequest.addParameter( "MAXIMO_EVENT", "EVENT_STATUS", "S" );
         int liRprcSuccsCnt = MAXIMO_EVENTImpl.getObjectCount(
               loSearchRequest, loSession );
         foRowToBeAdded.getData( "RPRC_SUCCS_CNT" ).setint( liRprcSuccsCnt );

      }
   }

}

