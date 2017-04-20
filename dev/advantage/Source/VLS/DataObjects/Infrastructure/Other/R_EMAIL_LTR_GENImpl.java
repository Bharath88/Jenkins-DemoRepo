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
**  R_EMAIL_LTR_GEN
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_EMAIL_LTR_GENImpl extends  R_EMAIL_LTR_GENBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_EMAIL_LTR_GENImpl (){
	super();
}

public R_EMAIL_LTR_GENImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//{{COMP_EVENT_afterCommit
public void afterCommit(Session session)
{
   //Write Event Code below this line

   // If it was in inser mode and SYNC_EMAIL_FL is true
   // Send and immediate email 
   if( mboolWasInserted && getSYNC_EMAIL_FL() == 1 )
   {
      try
      {
         AMSJavaMail loMail = new AMSJavaMail( 
            (VSORBSessionImpl)getSession() );
         AMSSendMail loSend = new AMSSendMail( 
            (VSORBSessionImpl)getSession() );
         
         
         loSend.setRecipientsTo( new String[]{ getSEND_TO() } ) ;
         loSend.setFrom( getAUTO_SEND_EMAIL_AD() ) ;
         loSend.setSubject( getSUBJECT() ) ;
         if( getPAGE_DATA() != null )
         {
            // Currently Error Question email uses this column, all other email 
            // submission uses EMAIL_LTRTXT. In future we need to fix this and 
            // this column will be dropped.
            loSend.setMsgText( getPAGE_DATA() ) ;
         }
         else
         {
            loSend.setMsgText( new String( 
               getData( "EMAIL_LTRTXT" ).getbytes() ) ) ;
         }
         
         loSend.send() ;
      }
      catch( versata.vls.SendMailException loSMExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loSMExp);
      }
   }
   
   //reset was inserted flag
   mboolWasInserted = false;
   
}
//END_COMP_EVENT_afterCommit}}

//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
   //Write Event Code below this line
   mboolWasInserted = ( isInserted() ) ? true : false;
}
//END_COMP_EVENT_afterInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
   //Write Event Code below this line
}
//END_COMP_EVENT_beforeUpdate}}

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addTransactionEventListener(this);
	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static R_EMAIL_LTR_GENImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_EMAIL_LTR_GENImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   boolean mboolWasInserted = false;
}

