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
**  IN_JF_FRM_TRAN*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_JF_FRM_TRANImpl extends  IN_JF_FRM_TRANBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public IN_JF_FRM_TRANImpl (){
		super();
	}
	
	public IN_JF_FRM_TRANImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static IN_JF_FRM_TRANImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new IN_JF_FRM_TRANImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeDelete
public void beforeDelete(DataObject obj, Response response)
{
   //Write Event Code below this line
   int liFrmTranID = getFRM_TRAN_ID();
   Session loSession = getSession();
   /*
    * On Form Transformation Definitions it is possible to have multiple records
    * sharing the same Transformation ID. If the current record is the only record 
    * using Transformation ID (no other Form Transformation Definitions records were 
    * found using the Transformation ID) then along with deleting the current record
    * delete all Field Definitions, Group Order Definitions and 
    * Group Relationship Definitions for the given Form Transformation ID.
    */
   if( IN_JF_FRM_TRANImpl.getTranIDUsageCount( loSession, liFrmTranID ) == 1 )
   {
      AMSDataObject loToDelete = null;
      
      //Delete all related Field Definitions
      Enumeration loEnum = IN_JF_TRAN_FLDSImpl.getFrmTranFieldDefs( loSession, 
            liFrmTranID );
      while( loEnum.hasMoreElements() )
      {
         loToDelete = (IN_JF_TRAN_FLDSImpl)loEnum.nextElement();
         loToDelete.setDelete();
         loToDelete.save();
      }//end while
      
      //Delete all related Group Order Definitions
      loEnum = IN_JF_TRAN_ORDERImpl.getFrmTranGrpOrderDefs( loSession, 
            liFrmTranID );
      while( loEnum.hasMoreElements() )
      {
         loToDelete = (IN_JF_TRAN_ORDERImpl)loEnum.nextElement();
         loToDelete.setDelete();
         loToDelete.save();
      }//end while
      
      //Delete all related Group Relationship Definitions
      loEnum = IN_JF_TRAN_RELImpl.getFrmTranGrpRelDefs( loSession, 
            liFrmTranID );
      while( loEnum.hasMoreElements() )
      {
         loToDelete = (IN_JF_TRAN_RELImpl)loEnum.nextElement();
         loToDelete.setDelete();
         loToDelete.save();
      }//end while
   }//end if	
   
   /*
    * NOTE: Relationship cannot be enforced between IN_JF_FRM_TRAN and 
    * IN_JF_TRAN_FLDS, IN_JF_TRAN_ORDER and IN_JF_TRAN_REL because
    * the relationship is based on Transformation ID field and Transformation ID
    * is not unique on IN_JF_FRM_TRAN(it is possible for multiple forms to use
    * the Transformation ID and clients use such setup).
    */
}
//END_COMP_EVENT_beforeDelete}}

	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}
   
   /**
    * Returns number of Form Transformation Definitions records for the
    * given Form Transformation ID.
    * @param foSession Session of calling program
    * @param fiFrmTranID Form Transformation ID
    * @return int
    */    
   public static int getTranIDUsageCount( Session foSession,
         int fiFrmTranID )
   {
      SearchRequest loSrchRqst = new SearchRequest();
      loSrchRqst.addParameter("IN_JF_FRM_TRAN", "FRM_TRAN_ID", 
            String.valueOf(fiFrmTranID) );
      return IN_JF_FRM_TRANImpl.getObjectCount( loSrchRqst, foSession );          
   }//end of method
   
}
