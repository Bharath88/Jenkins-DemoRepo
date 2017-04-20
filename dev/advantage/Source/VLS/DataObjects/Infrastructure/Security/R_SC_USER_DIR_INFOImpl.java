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
**  R_SC_USER_DIR
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_USER_DIR_INFOImpl extends  R_SC_USER_DIR_INFOBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	private R_SC_USER_INFOImpl moCachedUserInfo = null ;

	//{{COMP_CLASS_CTOR
	public R_SC_USER_DIR_INFOImpl (){
		super();
	}
	
	public R_SC_USER_DIR_INFOImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static R_SC_USER_DIR_INFOImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_USER_DIR_INFOImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
//{{COMP_EVENT_afterCommit
public void afterCommit(Session session)
{
   AMSSecurity.removeUserCache( getUSER_ID() ) ;
}
//END_COMP_EVENT_afterCommit}}

//{{COMP_EVENT_afterInsert
public void afterInsert(DataObject obj)
{
	//Write Event Code below this line

	SearchRequest loSearchRequest = new SearchRequest() ;
      loSearchRequest.addParameter( "IN_USER_PROFILE",
                                    "USER_ID",
                                    this.getUSER_ID() ) ;
      IN_USER_PROFILEImpl loUserProf =
	   (IN_USER_PROFILEImpl)
	   IN_USER_PROFILEImpl.getObjectByKey( loSearchRequest, this.getSession() );

	if( loUserProf == null )
	{
         loUserProf = IN_USER_PROFILEImpl.getNewObject( this.getSession(),
	                                                  true );

         loUserProf.setUSER_ID( this.getUSER_ID() );
         loUserProf.setInsert();
         loUserProf.save();

      }
     //update INTG_SC_USER_INFO table to integrate with infoAdvantage or other security
     INTG_SC_USER_INFOImpl.updateSecurityUserDirectoryInformation( getSession(), this, INTG_SC_USER_INFOImpl.INSERT_ACTION ) ;

}
//END_COMP_EVENT_afterInsert}}

//{{COMP_EVENT_afterUpdate
public void afterUpdate(DataObject obj)
{
   //update INTG_SC_USER_INFO table to integrate with infoAdvantage or other security
   INTG_SC_USER_INFOImpl.updateSecurityUserDirectoryInformation( getSession(), this, INTG_SC_USER_INFOImpl.UPDATE_ACTION ) ;

}
//END_COMP_EVENT_afterUpdate}}

//{{COMP_EVENT_afterDelete
public void afterDelete(DataObject obj)
{
   //update INTG_SC_USER_INFO table to integrate with infoAdvantage or other security
   INTG_SC_USER_INFOImpl.updateSecurityUserDirectoryInformation( getSession(), this, INTG_SC_USER_INFOImpl.DELETE_ACTION ) ;

}
//END_COMP_EVENT_afterDelete}}

//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line
	validateEmailID();
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	//Write Event Code below this line
	validateEmailID();
}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addTransactionEventListener(this);
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}


	private R_SC_USER_INFOImpl getCurrentUserInfoObject()
	{
		if ( moCachedUserInfo == null )
		{
			Enumeration loEnum = getUserInformation() ;
						if ( loEnum.hasMoreElements() )
			{
				R_SC_USER_INFOImpl loUser =
					(R_SC_USER_INFOImpl)loEnum.nextElement() ;
				moCachedUserInfo = loUser ;
				return loUser ;
			}
			else
			{
				return null ;
			}
		}
		else
		{
			return moCachedUserInfo ;
		}
	}


	public String getOrgCdFromUserInfo( int fiOrgIndex )
	{
		R_SC_USER_INFOImpl loUser = getCurrentUserInfoObject() ;

		if ( loUser != null )
		{
			String lsOrgCd = null ;

			switch ( fiOrgIndex )
			{
				// Branch Code
				case 0:
				   lsOrgCd = loUser.getHOME_GOVT_BRN_CD() ;
				   break ;
				// Cabinet Code
				case 1:
				   lsOrgCd = loUser.getHOME_CAB_CD() ;
				   break ;
				// Department Code
				case 2:
				   lsOrgCd = loUser.getHOME_DEPT_CD() ;
				   break ;
				// Division Code
				case 3:
				   lsOrgCd = loUser.getHOME_DIV_CD() ;
				   break ;
				// Group Code
				case 4:
				   lsOrgCd = loUser.getHOME_GP_CD() ;
				   break ;
				// Section Code
				case 5:
				   lsOrgCd = loUser.getHOME_SECT_CD() ;
				   break ;
				// District Code
				case 6:
				   lsOrgCd = loUser.getHOME_DSTC_CD() ;
				   break ;
				// Bureau Code
				case 7:
				   lsOrgCd = loUser.getHOME_BUR_CD() ;
				   break ;
				// Unit Code
				case 8:
				   lsOrgCd = loUser.getHOME_UNIT_CD() ;
				   break ;
				// Unknown Code
				default:
				   lsOrgCd = null ;
				   break ;
			}

			return lsOrgCd ;
		}
		else
		{
			return null ;
		}
	}

	public String getUnitCdFromUserInfo()
	{
		R_SC_USER_INFOImpl loUser = getCurrentUserInfoObject() ;

		if ( loUser != null )
		{
			return loUser.getHOME_UNIT_CD() ;
		}
		else
		{
         return null ;
		}
	}

	public static void beforeResultSetFillBeforeSecurityCheck(
									DataRow rowToBeAdded,
									Response response )
	{
		R_SC_USER_DIR_INFOImpl loUserDirInfo =
			(R_SC_USER_DIR_INFOImpl)rowToBeAdded.getComponent() ;

		/**
		 * Make sure that the virtual fields get calculated
		 */
		loUserDirInfo.initVirtualOrgFields() ;
	}

	private void initVirtualOrgFields()
	{
		getGOVT_BRN_CD() ;
		getCAB_CD() ;
		getDEPT_CD() ;
		getDIV_CD() ;
		getGP_CD() ;
		getSECT_CD() ;
		getDSTC_CD() ;
		getBUR_CD() ;
		getUNIT_CD() ;
	}

   
   private void validateEmailID()
   {
      if (!AMSStringUtil.strIsEmpty( getEMAIL_AD_TXT() ))
      {
         if (!AMSStringUtil.emailIsValid( getEMAIL_AD_TXT()))
         {
            raiseException("%c:Q0163,v: %", AMSMsgUtil.SEVERITY_LEVEL_ERROR);
         }
      }
   }
}
