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
	**  R_INFR_VALD_STP
	*/
	
	//{{COMPONENT_RULES_CLASS_DECL
	public class R_INFR_VALD_STPImpl extends  R_INFR_VALD_STPBaseImpl
	
	
	//END_COMPONENT_RULES_CLASS_DECL}}
	{
	//{{COMP_CLASS_CTOR
	public R_INFR_VALD_STPImpl (){
		super();
	}
	
	public R_INFR_VALD_STPImpl(Session session, boolean makeDefaults)
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
		public static R_INFR_VALD_STPImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_INFR_VALD_STPImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
	
	   /**
	    * This method returns a boolean value indicating if the given Inference /Validation table is to Active.
	    * @parm fsTableName denoting the Inference or Validation
	    * @parm foSession giving a handle to the session
	    * @return boolean: denoting if Inference/ Validation is active.
	    */
	
	   public static boolean isInferenceActive(String fsTableName, Session foSession)
	   {
	       R_INFR_VALD_STPImpl loInfrValdRec ;
	       SearchRequest     loSrchReq = new SearchRequest() ;
	
	       loSrchReq.addParameter( "R_INFR_VALD_STP", "TBL_NM", fsTableName ) ;
	       loInfrValdRec = (R_INFR_VALD_STPImpl)R_INFR_VALD_STPImpl.getObjectByKey( loSrchReq, foSession ) ;
	       if ( loInfrValdRec != null )
	       {
	          return loInfrValdRec.getACTV_FL() ;
	       } /* end if ( R_INFR_VALD_STPImpl != null ) */
	
	       return true ;
	   }
	
	
	}
	
	