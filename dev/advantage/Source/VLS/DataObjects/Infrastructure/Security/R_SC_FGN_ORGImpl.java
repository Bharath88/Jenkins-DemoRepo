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
**  R_SC_FGN_ORG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_FGN_ORGImpl extends  R_SC_FGN_ORGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_FGN_ORGImpl (){
		super();
	}
	
	public R_SC_FGN_ORGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

    }


	//{{COMPONENT_RULES
		public static R_SC_FGN_ORGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_FGN_ORGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
    String lsFgnOrgCode = null;
    lsFgnOrgCode = com.amsinc.gems.adv.common.AMSSecurity.formCombOrgCode(
                           getFGN_GOVT_BRN_CD(), getFGN_CAB_CD(), getFGN_DEPT_CD(),
                           getFGN_DIV_CD(), getFGN_GP_CD(), getFGN_SECT_CD(),
                           getFGN_DSTC_CD(), getFGN_BUR_CD(), getFGN_UNIT_CD() ) ;
    setCOMB_ORG_CD( lsFgnOrgCode ) ;
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
    String lsFgnOrgCode = null;
    lsFgnOrgCode = com.amsinc.gems.adv.common.AMSSecurity.formCombOrgCode(
                           getFGN_GOVT_BRN_CD(), getFGN_CAB_CD(), getFGN_DEPT_CD(),
                           getFGN_DIV_CD(), getFGN_GP_CD(), getFGN_SECT_CD(),
                           getFGN_DSTC_CD(), getFGN_BUR_CD(), getFGN_UNIT_CD() ) ;
   setCOMB_ORG_CD( lsFgnOrgCode ) ;

}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}

    public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
    }

   /**
    * This method registers 'ALL' as an exception while validating RSRC_GP_ID
    * and USER_ID against the R_SC_RSRC_GP and R_SC_USER_DIR_INFO tables resp.
    *
    * @return void.
    */
   public void myInitializations()
   {
      /*
       * Register "ALL" as an exception value for "RSRC_GP_ID" against the
       * parent - "R_SC_RSRC_GP"
       */
      registerForeignKeyException("R_SC_RSRC_GP", "RSRC_GP_ID",
            AMSCommonConstants.ALL);

      /*
       * Register "ALL" as an exception value for "USER_ID" against the
       * parent - "R_SC_USER_ROLE_LNK"
       */
      registerForeignKeyException("R_SC_USER_ROLE_LNK", "USER_ID",
            AMSCommonConstants.ALL);

      mboolCustomDataObjectSave = true ;
   }//End myInitializations

   /**
    * This method is used to set the USER_ID and HOME_DEPT_CD fields.
    * If the ‘Foreign Organization Restriction’ field is set to ‘User ID
    * Required’ or ‘None’ or 'User ID or Home Department Required' on Security Role table and
    * the Home Department field is left blank while performing a save action
    * on the same record, set the field to ALL. If the ‘Foreign Organization
    * Restriction’ field is set to ‘Home Department’ or ‘None’ or
    * 'User ID or Home Department Required' on Security Role table and the User ID
    * field is left blank while performing a save
    * action on the same record, set the field to ALL
    */
   protected void setDeptOrUserIDToAll()
   {
      int liFgnOrgRstr = getSecurityRole().getFGN_ORG_RSTR();
      if((getHOME_DEPT_CD() == null) &&
         ((liFgnOrgRstr ==  CVL_FGN_ORG_CNFGImpl.USER_ID_REQUIRED) ||
             (liFgnOrgRstr == CVL_FGN_ORG_CNFGImpl.NONE) ||
             (liFgnOrgRstr == CVL_FGN_ORG_CNFGImpl.USERID_OR_HOMEDEPT_REQUIRED)))
      {
         setHOME_DEPT_CD(AMSCommonConstants.ALL);
      }
      if((getUSER_ID() == null) &&
         ((liFgnOrgRstr ==    CVL_FGN_ORG_CNFGImpl.HOME_DEPT_REQUIRED) ||
             (liFgnOrgRstr == CVL_FGN_ORG_CNFGImpl.NONE)||
             (liFgnOrgRstr == CVL_FGN_ORG_CNFGImpl.USERID_OR_HOMEDEPT_REQUIRED)))
      {
         setUSER_ID(AMSCommonConstants.ALL);
      }

   }

   /**
    * Customized save behavior to set User ID and/or Home Department to ALL
    * depending upon Foreign Organization Restriction setup for the Security Role ID on Security Roles.
    * @return boolean Returns true on successful method execution, else false.
    */
   protected boolean myDataObjectSave()
   //@SuppressAbstract
   {
      /* Sets User ID and/or Home Department to ALL depending upon
       * Foreign Organization Restriction setup for the Security Role ID on Security Roles.
       */
      setDeptOrUserIDToAll();
      return true;
   }
}
