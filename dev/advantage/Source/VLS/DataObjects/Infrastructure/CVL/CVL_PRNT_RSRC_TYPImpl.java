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
 **  CVL_PRNT_RSRC_TYP
 */

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_PRNT_RSRC_TYPImpl extends  CVL_PRNT_RSRC_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /** Constant to indicate Print Resource Type of printer */
   public static final int PRINTER = 1;

   /** Constant to indicate Print Resource Type of email */
   public static final int EMAIL = 2;

   /** Constant to indicate Print Resource Type of pdf */
   public static final int PDF = 3;

   /** Constant to indicate Print Resource Type of Fax */
   public static final int FAX = 4;

//{{COMP_CLASS_CTOR
public CVL_PRNT_RSRC_TYPImpl (){
	super();
}

public CVL_PRNT_RSRC_TYPImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static CVL_PRNT_RSRC_TYPImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new CVL_PRNT_RSRC_TYPImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }
   
   /**
    * Returns the display value for the type of resource passed as integer. 
    * @param fiRsrcTyp Integer representing the type of resource.
    * @return String representing the display value for the type of resource.
    */
   public static String getCVLRsrcDisplayVal(int fiRsrcTyp)
   {
      //return the respective string representation of the resource type.
      switch(fiRsrcTyp)
      {
         case 1: 
            return "Printer";
         case 2:
            return "Email";
         case 3:
            return "PDF";
         case 4:
            return "Fax";
         default :
            return null;
      }
   }

}
