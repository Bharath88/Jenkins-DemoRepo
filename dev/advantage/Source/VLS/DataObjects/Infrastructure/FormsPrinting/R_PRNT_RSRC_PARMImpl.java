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
**  R_PRNT_RSRC_PARM
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_PRNT_RSRC_PARMImpl extends  R_PRNT_RSRC_PARMBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_PRNT_RSRC_PARMImpl (){
		super();
	}
	
	public R_PRNT_RSRC_PARMImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_PRNT_RSRC_PARMImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_PRNT_RSRC_PARMImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}


   /**
    * Populates and returns the HashMap with the Print Resource Parameters
    * of the Print Resource whose Print Resource Id is passed to this method.
    * @param loSession
    * @param fsRsrcId id for which the Parameters are required.
    * @return A HashMap of the Print Resource Parameters.
    * Key on the HashMap is the Parameter name and Value is Parameter value.
    * Returns empty HashMap if parameters for given Resource ID is not found.
    */
   public static HashMap getPrntRsrcParms(Session loSession, String fsRsrcId)
   {
      SearchRequest lsrPrntRsrcRec = new SearchRequest() ;
      lsrPrntRsrcRec.addParameter( "R_PRNT_RSRC_PARM", "PRNT_RSRC_ID", fsRsrcId ) ;
      String lsParmName                   = null;
      String lsParmVal                    = null;
      R_PRNT_RSRC_PARMImpl loPrntRsrcParm = null;
      Enumeration loEnumPrntRsrcRec       = R_PRNT_RSRC_PARMImpl.getObjects( lsrPrntRsrcRec , loSession ) ;
      HashMap loPrntRsrcParams            = new HashMap();
      while ( loEnumPrntRsrcRec.hasMoreElements() )
      {
         loPrntRsrcParm = (R_PRNT_RSRC_PARMImpl)loEnumPrntRsrcRec.nextElement() ;
         lsParmName     = loPrntRsrcParm.getData( "PARM_NM" ).getString() ;
         lsParmVal      = loPrntRsrcParm.getData( "PARM_VL" ).getString() ;
         loPrntRsrcParams.put( lsParmName, lsParmVal ) ;
      }
      return loPrntRsrcParams;
   }//end of method

}
