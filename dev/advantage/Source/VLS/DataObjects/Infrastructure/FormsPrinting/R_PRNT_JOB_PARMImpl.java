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
**  R_PRNT_JOB_PARM
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_PRNT_JOB_PARMImpl extends  R_PRNT_JOB_PARMBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
  /**
   * Name of the Parameter that contains the xsl file Name.
   */
   public static final String PARM_XSL_FILE_NM = "XSL_FILE_NM";
	//{{COMP_CLASS_CTOR
	public R_PRNT_JOB_PARMImpl (){
		super();
	}
	
	public R_PRNT_JOB_PARMImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_PRNT_JOB_PARMImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_PRNT_JOB_PARMImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}
	

   /**
    * Returns the value of the given parameter for the Print Job from Print Job Parameters table.
    * Returns null when lookup field values are null or the retrieved parameter value is null.
    * @param foSession Session of calling program
    * @param fsApplRsrcID Application Resource ID
    * @param fsPrntJobCd Print Job Code
    * @param fsParmNm Parameter Name
    * @return Parameter Value
    */
   public static String getPrntJobParmValue( Session foSession, String fsApplRsrcID,
         String fsPrntJobCd, String fsParmNm )
   {
      //If any lookup field value is null then return null (avoid db query)
      if( AMSStringUtil.strIsEmpty( fsApplRsrcID ) || AMSStringUtil.strIsEmpty( fsPrntJobCd )
            || AMSStringUtil.strIsEmpty( fsParmNm ) )
      {
         return null;
      }

      SearchRequest loSrchRqst = new SearchRequest();
      loSrchRqst.addParameter("R_PRNT_JOB_PARM", "APPL_RSRC_ID", fsApplRsrcID);
      loSrchRqst.addParameter("R_PRNT_JOB_PARM", "PRNT_JOB_CD", fsPrntJobCd);
      loSrchRqst.addParameter("R_PRNT_JOB_PARM", "PARM_NM", fsParmNm);
      R_PRNT_JOB_PARMImpl loPrntJobParm = (R_PRNT_JOB_PARMImpl)R_PRNT_JOB_PARMImpl.getObjectByKey(
            loSrchRqst, foSession );

      if( loPrntJobParm == null )
      {
         return null;
      }
      else
      {
         //Return Parameter Value for the given Parameter Name of the Print Job
         return loPrntJobParm.getPARM_VL();
      }
   }//end of method


   /**
    * Returns <code>true</code> when Parameter indicated by <code>PARM_XSL_FILE_NM</code>
    * constant exists for the Print Job on Print Job Parameters table and it has some value.
    * In all other cases returns <code>false</code>.
    * @param foSession Session of calling program
    * @param fsApplRsrcID Application Resource ID
    * @param fsPrntJobCd Print Job Code
    * @return <code>boolean</code>
    */
   public static boolean useXsl( Session foSession, String fsApplRsrcID,
            String fsPrntJobCd )
   {
      String lsXslFileNm = getPrntJobParmValue( foSession, fsApplRsrcID,
         fsPrntJobCd, PARM_XSL_FILE_NM );
      return !AMSStringUtil.strIsEmpty( lsXslFileNm );
   }//end of method


}
