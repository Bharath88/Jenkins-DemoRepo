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
**  WF_WRK_ADM_QRY*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_WRK_ADM_QRYImpl extends  WF_WRK_ADM_QRYBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMPOSITE_COMPONENT_METHODS
	
	//END_COMPOSITE_COMPONENT_METHODS}}
			//{{EVENT_CODE
			
			//END_EVENT_CODE}}

  /**
   * Performs initialization steps.
   */
   public void myInitializations()
   {
      /* Set Prefix for Organizational fields that would be used 
      for Organizational security */
      setSecurityOrgPrefixes( "DOC" ) ;
   }//end of method
}

