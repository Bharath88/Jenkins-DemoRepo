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
 **  R_PRNT_RSRC
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_PRNT_RSRCImpl extends  R_PRNT_RSRCBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   
   public static final String INVLD_PRNT_RSRC_ERR_MSG = 
      "Invalid Print Resource Identifier. " +
      "When View Forms is set to Y, the Print Resource " +
      "Identifier must have PDF type.";
      
	//{{COMP_CLASS_CTOR
	public R_PRNT_RSRCImpl (){
		super();
	}
	
	public R_PRNT_RSRCImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}
      
   }
   
   
//{{COMPONENT_RULES
	public static R_PRNT_RSRCImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_PRNT_RSRCImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   
//{{EVENT_CODE

//END_EVENT_CODE}}
   
   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	//END_EVENT_ADD_LISTENERS}}
   }
   
   /**
    * This method will return an instance of R_PRNT_RSRCImpl based on the passed
    * parameter, if the record is not found  it will return null.
    *
    * @param fsPrntRsrc	The Print Resource ID.
    * @param foSession 	The current Session object.
    * @return 						an instance of R_PRNT_RSRCImpl
    */
   public static R_PRNT_RSRCImpl getPrntRsrcRec(String fsPrntRsrc, Session foSession)
   {
      R_PRNT_RSRCImpl loPrntRsrc = null ;
      if (!AMSStringUtil.strIsEmpty(fsPrntRsrc))
      {
         SearchRequest lsrPrntRsrc = new SearchRequest();
         lsrPrntRsrc.addParameter("R_PRNT_RSRC", "PRNT_RSRC_ID", fsPrntRsrc);
         loPrntRsrc = (R_PRNT_RSRCImpl)R_PRNT_RSRCImpl.getObjectByKey(lsrPrntRsrc,
            foSession);
      } /* End if (!AMSStringUtil.strIsEmpty(fsPrntRsrc)) */
      return loPrntRsrc;
   } /* End getPrntRsrcRec() */
   
}
