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
**  R_SC_SEC_ROLEPAIRS
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_SEC_ROLEPAIRSImpl extends  R_SC_SEC_ROLEPAIRSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_SEC_ROLEPAIRSImpl (){
		super();
	}
	
	public R_SC_SEC_ROLEPAIRSImpl(Session session, boolean makeDefaults)
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
		public static R_SC_SEC_ROLEPAIRSImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_SEC_ROLEPAIRSImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	/**
	 * Checks if the combination of Security Role Ids being entered already exists on the
	 * Restricted Role Pairs table. If yes, logs error.
	 * For example: SEC_ROLEID_1 = A ,SEC_ROLEID_2 = B
	 * If a new pair is inserted where SEC_ROLEID_1=B ,SEC_ROLEID_2 = A,
	 * then logs error because they both mean the same thing.
	 */
	protected void isSecRoleCombExists( )
	{
	   String lsSecRoleID1 = getSEC_ROLEID_1();
	   String lsSecRoleID2 = getSEC_ROLEID_2();

	   // This check is added to make sure that error is not logged when both the
	   // Security Role Ids being inserted is same.
	   // For example SEC_ROLEID_1 = A, SEC_ROLEID_2 = A
	   if( !AMSStringUtil.strEqual( lsSecRoleID1, lsSecRoleID2 ) )
	   {
	      StringBuilder lsbSelect = new StringBuilder( 64 );
	      SearchRequest lsrQry = new SearchRequest();
	      int liInvalidRolePairHits = 0;

	      lsbSelect.append(" SEC_ROLEID_1 = ");
	      lsbSelect.append( AMSSQLUtil.getANSIQuotedStr( lsSecRoleID2, true) );
	      lsbSelect.append(" AND ");
	      lsbSelect.append(" SEC_ROLEID_2 = ");
	      lsbSelect.append( AMSSQLUtil.getANSIQuotedStr( lsSecRoleID1, true) );

	      lsrQry.add( lsbSelect.toString() );

	      try
	      {
	         liInvalidRolePairHits = getObjectCount( "R_SC_SEC_ROLEPAIRS", lsrQry, getSession() );
	         if( liInvalidRolePairHits > 0 )

	         {
	            raiseException("%c:Q0204,v:" + lsSecRoleID1 + ",v:" + lsSecRoleID2 + "%" );
	         }
	      }
	      catch (Exception foException)
	      {
	         raiseException( "Exception occurred while checking for similar Restricted Role combination" );
	         moAMSLog.error( "Exception occurred while checking for similar Restricted Role combination", foException );
	      }
	   }
	}


	/**
	 * Checks for all possible combinations of given Security Role with the Arraylist of Security Roles
	 * on Restricted Role Pairs table and returns true if any match found for a combination.
	 * Returns false when any of the following conditions are true:
	 * <ul>
	 * <li> Given Security Role ID is null or emtpy String
	 * <li> Passed in ArrayList has no entries
	 * <li> All entries on ArrayList is null or empty String
	 * </ul>
	 *
	 * For example: fsRoleId = B, foRoles contains [B,C,D]
	 * On Restricted Role Pairs table, here are the combinations of Restricted Role Pairs setup in format
	 * [Security Role ID 1, Security Role ID 2]:
	 * [B, D]
	 * [C, D]
	 * [B, A]
	 * Method tries to find matches for B with any of these [B,C,D] Roles on Restricted Role Pairs table.
	 * Method will return true because these matches were found on Restricted Role Pairs table:
	 * [B, D]
	 * @param foSession The current session object
	 * @param fsRoleId  The Security role Id
	 * @param foRoles List of Security Role Ids
	 * @return True if combination of Restricted Security Role Pairs is encountered.
	 */
	public static boolean isRoleRestricted( Session foSession, String fsRoleId, ArrayList<String> foRoles )
	{
	   StringBuilder lsbSelect = new StringBuilder( 64 );
	   SearchRequest lsrQry = new SearchRequest();
	   int liInvalidRolePairHits=0;

	   // Gets comma seperated list of security roles.
	   String lsRoles = AMSStringUtil.getCommaSepStr(foRoles);

	   if( ( AMSStringUtil.strIsEmpty( lsRoles ) ) || ( foRoles==null ) )
	   {
	      return false;
	   }

	   lsbSelect.append( " ( ( SEC_ROLEID_1 = " );
	   lsbSelect.append( AMSSQLUtil.getANSIQuotedStr(fsRoleId, true) );
	   lsbSelect.append( "AND (" );
	   lsbSelect.append( AMSBatchUtil.getWhereClause( "SEC_ROLEID_2", lsRoles) );
	   lsbSelect.append(") ) " );
	   lsbSelect.append( "OR ");
	   lsbSelect.append( "( SEC_ROLEID_2 = " );
	   lsbSelect.append( AMSSQLUtil.getANSIQuotedStr(fsRoleId, true) );
	   lsbSelect.append( "AND (" );
	   lsbSelect.append( AMSBatchUtil.getWhereClause( "SEC_ROLEID_1", lsRoles) );
	   lsbSelect.append( ") ) )" );

	   lsrQry.add( lsbSelect.toString() );

	   try
	   {
	      liInvalidRolePairHits = getObjectCount( "R_SC_SEC_ROLEPAIRS", lsrQry, foSession );
	      if( liInvalidRolePairHits > 0 )
	      {
	         return true;
	      }
	   }
	   catch (Exception foException)
	   {
	      raiseException("Exception ocurred while getting the Restricted Security Roles " + foException, foSession);
	   }
	   return false;
	}

	/**
	 * Displays an informational message to run the "Separation of Duties
	 * Audit Report" before saving data to the database.
	 */
	public void myBeforeCommit( Session foSession, Response foResponse )
	//@SuppressAbstract
	{
	   Object loOrigSecRoleId1 = getData("SEC_ROLEID_1").getOriginalObject();
	   Object loOrigSecRoleId2 = getData("SEC_ROLEID_2").getOriginalObject();
	   String lsOrigSecRoleId1 = null;
	   String lsOrigSecRoleId2 = null;
	   if( ( loOrigSecRoleId1 != null ) && ( loOrigSecRoleId2 != null ) )
	   {
	      lsOrigSecRoleId1 = loOrigSecRoleId1.toString();
	      lsOrigSecRoleId2 = loOrigSecRoleId2.toString();
	   }

	   //Case where Save is going to be successfully committed
	   if( AMSMsgUtil.getHighestSeverityLevel( getSession() ) < AMSMsgUtil.SEVERITY_LEVEL_ERROR )
	   {
	      //Case where Security Role ID 1 or Security Role ID 2 was changed. Note isChanged() does not give correct
	      //results since this is during commit.
	      if( ( !AMSStringUtil.strEqual(lsOrigSecRoleId1, getSEC_ROLEID_1() ) ) ||
	            ( !AMSStringUtil.strEqual(lsOrigSecRoleId2, getSEC_ROLEID_2() ) ) )
	      {
	         //Display informational message
	         raiseException("%c:Q0197%");
	      }
	   }
	}

}

