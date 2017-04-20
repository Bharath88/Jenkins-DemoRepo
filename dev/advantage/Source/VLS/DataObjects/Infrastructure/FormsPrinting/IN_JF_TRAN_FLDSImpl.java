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
**  IN_JF_TRAN_FLDS*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_JF_TRAN_FLDSImpl extends  IN_JF_TRAN_FLDSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public IN_JF_TRAN_FLDSImpl (){
		super();
	}
	
	public IN_JF_TRAN_FLDSImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static IN_JF_TRAN_FLDSImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new IN_JF_TRAN_FLDSImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
   
   /**
    * Returns Field Definitions for given Form Transformation ID.
    * @param foSession Session of calling program
    * @param fiFrmTranID Form Transformation ID
    * @return Enumeration
    */
   public static Enumeration getFrmTranFieldDefs( Session foSession,
         int fiFrmTranID )
   {
      SearchRequest loSrchRqst = new SearchRequest();
      loSrchRqst.addParameter("IN_JF_TRAN_FLDS", "FRM_TRAN_ID", 
            String.valueOf(fiFrmTranID) );
      return IN_JF_TRAN_FLDSImpl.getObjects( loSrchRqst, foSession );
   }//end of method
   
}
