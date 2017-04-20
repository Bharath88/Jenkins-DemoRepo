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
**  DOC_ERR_OFLOW
*/

//{{COMPONENT_RULES_CLASS_DECL
public class DOC_ERR_OFLOWImpl extends  DOC_ERR_OFLOWBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public DOC_ERR_OFLOWImpl (){
	super();
}

public DOC_ERR_OFLOWImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static DOC_ERR_OFLOWImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new DOC_ERR_OFLOWImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	//END_EVENT_ADD_LISTENERS}}
   }

   public void myBeforeCommit(Session foSession, Response foResponse)
   //@SuppressAbstract
   {
      if(!isDocActnUnarchivePreview())
      {
         saveRowOnCommit( true ) ;
         ignoreRowErrorsOnCommit( true ) ; 
      }
   }

      public void myInitializations()
      {
         /**
          * Turn off table level security
          */
         mboolCheckInsertSecurity  = false ;
         mboolCheckUpdateSecurity  = false ;
         mboolCheckDeleteSecurity  = false ;
      }
}
