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
**  CVL_DOC_STA_CD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_DOC_STA_CDImpl extends  CVL_DOC_STA_CDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	// Constants for CVL_DOC_STA_CD Stored Value

   	public static final int HELD = 1;
   	public static final int READY = 2;
   	public static final int REJECTED = 3;
	public static final int SUBMITTED = 4;

	//{{COMP_CLASS_CTOR
	public CVL_DOC_STA_CDImpl (){
		super();
	}
	
	public CVL_DOC_STA_CDImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static CVL_DOC_STA_CDImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_DOC_STA_CDImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
    
    //Workers Comp
    
    
    public static String getDocStatusString(int fiDocStaCd)
    {
        String lsDocStatStr = " ";
        
        if(fiDocStaCd==1)
        {
            lsDocStatStr="HELD";
        }
        else if(fiDocStaCd==2)
        {
            lsDocStatStr="READY";
            
        }
        else if(fiDocStaCd==3)
        {
            lsDocStatStr="REJECTED";
        }
        else if(fiDocStaCd==4)
        {
            lsDocStatStr="SUBMITTED";
        }
        
        return lsDocStatStr;
    }
    
    //Workers Comp
    
    
	
}
