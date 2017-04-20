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
**  CVL_OBJ_ATT_TYP*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_OBJ_ATT_TYPImpl extends  CVL_OBJ_ATT_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   public static int STANDARD = 1;
   public static int PROPRIETARY = 2;
   public static int PRICING= 3;
   public static int DOCUMENT_XML= 4;
   public static int IMAGE_ZIPPED = 5;
   public static int IMAGE = 6;
   public static int ASSEMBLED_FORM = 7;
   public static int DOCUMENT_XML_SUMM= 8;
   public static int CATALOG_PICTURE= 9;
   public static int EMPL_PHOTO             = 10; // ADVHR00020276
   public static int ECM_ATTACHMENT = 11;

	//{{COMP_CLASS_CTOR
	public CVL_OBJ_ATT_TYPImpl (){
		super();
	}
	
	public CVL_OBJ_ATT_TYPImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_OBJ_ATT_TYPImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_OBJ_ATT_TYPImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line
	//if the line limit is one the document limit must be set as well
	if(getSG_ATT_LTD_FL())
	{
		setPG_ATT_LTD_FL(true);
	}

}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	//Write Event Code below this line
	//if the line limit is one the document limit must be set as well
	if(getSG_ATT_LTD_FL())
	{
		setPG_ATT_LTD_FL(true);
	}
}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

}
