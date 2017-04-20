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
**  CVL_ESS_USER_TYP
*/

//{{COMPONENT_RULES_CLASS_DECL
public class CVL_ESS_USER_TYPImpl extends  CVL_ESS_USER_TYPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	// BGN	ADVHR00075542
    public static final int C_ESS_USER_1                = 1;
    public static final int C_ESS_USER_2                = 2;
    public static final int C_ESS_USER_3                = 3;
    public static final int C_ESS_USER_4                = 4;
    public static final int C_ESS_USER_5                = 5;
    public static final int C_ESS_USER_6                = 6;
    public static final int C_ESS_USER_7                = 7;
    public static final int C_ESS_USER_8                = 8;
    public static final int C_ESS_USER_9                = 9;
    public static final int C_ESS_USER_10               = 10;
    private static final String C_ESS_USER_TYP_1        = "ESS User 1- Superuser";
    private static final String C_ESS_USER_TYP_2        = "ESS User 2";
    private static final String C_ESS_USER_TYP_3        = "ESS User 3";
    private static final String C_ESS_USER_TYP_4        = "ESS User 4";
    private static final String C_ESS_USER_TYP_5        = "ESS User 5";
    private static final String C_ESS_USER_TYP_6        = "ESS User 6";
    private static final String C_ESS_USER_TYP_7        = "ESS User 7";
    private static final String C_ESS_USER_TYP_8        = "ESS User 8";
    private static final String C_ESS_USER_TYP_9        = "ESS User 9";
    private static final String C_ESS_USER_TYP_10       = "ESS User 10";
	
	// END	ADVHR00075542
	//{{COMP_CLASS_CTOR
	public CVL_ESS_USER_TYPImpl (){
		super();
	}
	
	public CVL_ESS_USER_TYPImpl(Session session, boolean makeDefaults)
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
		public static CVL_ESS_USER_TYPImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new CVL_ESS_USER_TYPImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	// BGN	ADVHR00075542
    public static int getStoredValue(String fsEssUserTyp)
    {
        int liReturnSV = 0;
        if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_1))
        {
            return C_ESS_USER_1;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_2))
        {
            return C_ESS_USER_2;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_3))
        {
            return C_ESS_USER_3;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_4))
        {
            return C_ESS_USER_4;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_5))
        {
            return C_ESS_USER_5;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_6))
        {
            return C_ESS_USER_6;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_7))
        {
            return C_ESS_USER_7;
        }
        else if (AMSStringUtil.strInsensitiveEqual(fsEssUserTyp, C_ESS_USER_TYP_8))
        {
            return C_ESS_USER_8;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_9))
        {
            return C_ESS_USER_9;
        }
        else if (AMSStringUtil.strEqual(fsEssUserTyp, C_ESS_USER_TYP_10))
        {
            return C_ESS_USER_10;
        }
        return liReturnSV;
    }//END getStoredValue()
	// END	ADVHR00075542
}

