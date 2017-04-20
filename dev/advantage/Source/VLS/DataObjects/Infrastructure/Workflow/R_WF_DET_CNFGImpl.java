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

/**
 * This class represent an entry of Configure Worklist Details page. It stores the Custom Field
 * Details for a particular Document.
 * This configuration is then used to display the data on Worklist Details page.
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_DET_CNFGImpl extends  R_WF_DET_CNFGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_WF_DET_CNFGImpl (){
		super();
	}
	
	public R_WF_DET_CNFGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line

   /*
    * liTotCnt has the Total number occupied rows.
    * Maximum 5 configurable fields are allowed per Document. So if liTotCnt exceeds issue an error
    * message.
    */

   SearchRequest lsrQry = new SearchRequest();
   lsrQry.addParameter("R_WF_DET_CNFG", "DOC_CD", getDOC_CD());
   int liTotCnt = getObjectCount(lsrQry, getSession());
   // Maximum 5 Custom Fields can be configured for each Document Code.
   if(liTotCnt >= 5)
   {
      raiseException("%c:Q0195","R_WF_DET_CNFG"," ");
      return;
   }

   // Check for the available Row numbers(ROW_NO) between 1 and 5 and assign to the new row.

   int liRowNo = 1;
   Vector lvSortCols = new Vector(2);
   lvSortCols.add( "DOC_CD");
   lvSortCols.add( "ROW_NO");

   Enumeration<R_WF_DET_CNFGImpl> loConfgRecrds = getObjectsSorted( lsrQry, getSession(), lvSortCols, SORT_ASC);
   R_WF_DET_CNFGImpl loConfigRow;
   ArrayList<Integer> loRowNos = new  ArrayList<Integer>();

   while(loConfgRecrds.hasMoreElements())
   {
      loConfigRow = loConfgRecrds.nextElement();
      if(liRowNo!=loConfigRow.getROW_NO())
      {
       break;
      }
      liRowNo++;
   }


   setROW_NO( (byte)liRowNo);
}
//END_COMP_EVENT_beforeInsert}}

	//END_EVENT_CODE}}

/**
 * Return true if the Worklist Details Configuration record is a delete Configuration.
 * The record is a delete Configuration when it satisfies all the following conditions:
 * Document Component and Document Field have no values and Old Document Component and
 * Old Document Field have values.
 * @return Returns true/false
 */

public boolean isDeleteConfig()
{
  if( ( AMSStringUtil.strIsEmpty( getDOC_COMP() ) ) &&
      ( AMSStringUtil.strIsEmpty( getDOC_FLD() ) ) &&
     !( AMSStringUtil.strIsEmpty( getOLD_DOC_COMP() ) ) &&
     !( AMSStringUtil.strIsEmpty( getOLD_DOC_FLD() ) ) )
  {
     return true;
  }
  return false;
}

/**
 * Return true if the Worklist Details Configuration record is an Insert/New Configuration.
 * The record is an Insert Configuration when it satisfies all the following conditions:
 * Document Component and Document Field have values and Old Document Component and
 * Old Document Field have no values.
 * @return Returns true/false
 */
public boolean isInsertConfig()
{
   if( !( AMSStringUtil.strIsEmpty( getDOC_COMP() )) &&
       !( AMSStringUtil.strIsEmpty( getDOC_FLD() ) ) &&
        ( AMSStringUtil.strIsEmpty( getOLD_DOC_COMP())) &&
        ( AMSStringUtil.strIsEmpty( getOLD_DOC_FLD())) )
   {
      return true;
   }
   return false;

}

/**
 * Return true if the Worklist Details Configuration record is an Update Configuration.
 * The record is an Update Configuration when it satisfies all the following conditions:
 * <ul>
 * <li> Document Component and Old Document Component are not null and both have different values.
 * <li> Document Field and Old Document Field are not null and both have different values.
 * </ul>
 * @return Returns true/false
 */
public boolean isUpdateConfig()
{
   if( !( AMSStringUtil.strIsEmpty( getDOC_COMP() )) &&
       !( AMSStringUtil.strIsEmpty( getOLD_DOC_COMP() ) ) &&
       !( AMSStringUtil.strIsEmpty( getDOC_FLD() ) ) &&
       !( AMSStringUtil.strIsEmpty( getOLD_DOC_FLD() ) ) )
   {
      if (!( AMSStringUtil.strInsensitiveEqual( getDOC_COMP(),  getOLD_DOC_COMP() ) ) ||
		  !( AMSStringUtil.strInsensitiveEqual( getDOC_FLD(), getOLD_DOC_FLD() ) ) )
      {
         return true;
      }
   }
   return false;

}

/**
 * Return true if the Worklist Details Configuration record is an Applied Configuration.
 * The Applied Configuration is the Config. that has been already processed by the batch job.
 * The record is an Applied Configuration when it satisfies all the following conditions:
 * <ul>
 * <li> Document Component and Old Document Component have same values and are not null.
 * <li> Document Field and Old Document Field have same values and are not null.
 * </ul>
 * @return Returns true/false
 */

public boolean isAppliedConfig()
{
   if( ( AMSStringUtil.strInsensitiveEqual( getDOC_COMP(),  getOLD_DOC_COMP(), false ) ) &&
       ( AMSStringUtil.strInsensitiveEqual( getDOC_FLD(), getOLD_DOC_FLD() , false )  ) )
   {
      return true;
   }
   return false;

}

/**
 * Returns true if all Worklist Details Configuration records for the Document Code
 * found on the current instance are DELETE configuration.
 * @return boolean true/false
 * @throws Exception
 * @see R_WF_DET_CONFImpl#isDeleteConfig
 */

public boolean isAllDeleteConfigurations() throws Exception
{
   StringBuilder lsbSearch = new StringBuilder(100);
   SearchRequest lsrSearch = new SearchRequest();

  /**
   * If any Worklist Details Configuration records satisfying NOT (DELETE configuration),
   * which implies INSERT or UPDATE configuration was found then conclude that
   * not all Configurations records are DELETE configuration, hence return false.
   * Refer R_WF_DET_CONFImpl.isDeleteConfig to understand the conditions that dictate
   * DELETE configuration.
   */

   lsbSearch.append( "( DOC_CD ")
            .append( AMSSQLUtil.getANSIQuotedStr( getDOC_CD(), AMSSQLUtil.EQUALS_OPER ) )
            .append(" )")
            .append(" AND NOT ( DOC_COMP IS NULL AND DOC_FLD IS NULL AND OLD_DOC_COMP IS NOT NULL")
            .append(" AND OLD_DOC_FLD IS NOT NULL ) ");

   lsrSearch.add( lsbSearch.toString() );

   return ( getObjectCount("R_WF_DET_CNFG", lsrSearch, getSession()) > 0? false : true );


}//end of method

/**
 * Resets the Worklist Details Configuration record to be non-dirty.
 * This involves resetting Dirty flag to false and depending on the parameter value
 * passed copies over values from Document Component, Document Field, Field Caption fields
 * to Old Document Component,Old Document Field and Old Field Caption fields respectively.
 * @param fboolCopyNewToOld If true then it copies the values.
 */
public void resetAsNonDirty(boolean fboolCopyNewToOld )
{
   setDIRTY_FL(false);

   if(fboolCopyNewToOld )
   {
      // Copy new Configuration values into old Configuration values.
      setOLD_DATA_TYP(getDATA_TYP());
      setOLD_DOC_FLD(getDOC_FLD());
      setOLD_DOC_FLD_NM(getDOC_FLD_NM());
      setOLD_DOC_COMP(getDOC_COMP());
   }
   save();
}

/**
 * Updates DIRTY_FL if current Session is an online Session(and not a batch session).
 */
public void updateDIRTY_FL()
{
   if(!isBatchSession(getSession()))
   {
   	setDIRTY_FL(true);
      //Throw informational message to indicate that WorkfLow Details Synchronization
      //batch job should be run in order for changes here to take effect.
   	raiseException("%c:Q0196","R_WF_DET_CNFG"," ");
   }
}

/**
 *  Selects and return the set of WorklList Details Configuration Non-Dirty records (DIRTY_FL= 0)
 *  for a given Document Code(DOC_CD).
 *  Non-Dirty records are the records which have not been recently edited  and hence their data can
 *  be used to synchronize entries on Worklist Details table.
 *  @param foSession Session object on which the operation is performed.
 *  @param fsDocCode The Document Code to select the records for
 *  @return Enumeration the set of WorlkList Details Configuration Records
 */
public static Enumeration getNonDirtyConfigRecs(Session foSession, String fsDocCode)
{
   SearchRequest lsrSearch = new SearchRequest();

   // Search for Processed(Non Dirty) Records present in Worklist Details Configuration Records
   // for a Document Code.
   lsrSearch.addParameter("R_WF_DET_CNFG", "DIRTY_FL", "0");
   lsrSearch.addParameter("R_WF_DET_CNFG", "DOC_CD", fsDocCode);

   //Returning the Enumeration with Non-Dirty Worklist Details Configuration for the given Document Code.
   return getObjects(lsrSearch, foSession);
}


	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static R_WF_DET_CNFGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_WF_DET_CNFGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}



}

