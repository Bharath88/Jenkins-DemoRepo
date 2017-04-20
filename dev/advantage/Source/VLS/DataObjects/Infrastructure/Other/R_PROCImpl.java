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
**  R_PROC*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_PROCImpl extends  R_PROCBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_PROCImpl (){
	super();
}

public R_PROCImpl(Session session, boolean makeDefaults)
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
	public static R_PROCImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_PROCImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * This method sets the in progress flag for the specified progress
    *
    * @param fsProcID The process id
    * @param fboolInProgress Indicates if the process is in progress
    * @param foSession The current session
    * @return Flag indicating if the in progress flag was successfully set
    */
   public static boolean setProcessInProgress( String fsProcID,
         boolean fboolInProgress, Session foSession )
   {
      if ( ( fsProcID != null ) && ( foSession != null ) )
      {
         SearchRequest loSrchReq = new SearchRequest() ;
         R_PROCImpl    loProc ;

         loSrchReq.addParameter( "R_PROC", "PROC_ID", fsProcID ) ;
         loProc = (R_PROCImpl)getObjectByKey( loSrchReq, foSession ) ;
         loProc.setIN_PROG_FL( fboolInProgress ) ;
         loProc.save() ;
         return true ;
      } /* end if ( ( fsProcID != null ) && ( foSession != null ) ) */
      return false ;
   } /* end setProcessInProgress() */
}

