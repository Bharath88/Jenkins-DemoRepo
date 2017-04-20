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
**  R_SC_RSRC_ACCS
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_RSRC_ACCSImpl extends  R_SC_RSRC_ACCSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}


{
			//{{COMP_CLASS_CTOR
			public R_SC_RSRC_ACCSImpl (){
				super();
			}
			
			public R_SC_RSRC_ACCSImpl(Session session, boolean makeDefaults)
			{
				super(session, makeDefaults);
			
			
			
			
			//END_COMP_CLASS_CTOR}}



	}




	//{{COMPONENT_RULES
		public static R_SC_RSRC_ACCSImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_RSRC_ACCSImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}



	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
   long llDefaultValue = 0;
	
   for( int liCount=1; liCount < 13; liCount++ )
   {
      String lsAppActnCdColName = "APPL_ACTN" + liCount +"_CD";

      if( isNull(lsAppActnCdColName) )
      {
         getData(lsAppActnCdColName).setlong(llDefaultValue);
      }	       
   }
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}


	}
	
}
