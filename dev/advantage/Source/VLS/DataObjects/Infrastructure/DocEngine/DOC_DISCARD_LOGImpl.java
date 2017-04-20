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
**  DOC_DISCARD_LOG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class DOC_DISCARD_LOGImpl extends  DOC_DISCARD_LOGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public DOC_DISCARD_LOGImpl (){
	super();
}

public DOC_DISCARD_LOGImpl(Session session, boolean makeDefaults)
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
	public static DOC_DISCARD_LOGImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new DOC_DISCARD_LOGImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

  /**
   * This method inserts a new record into the Doc Discard log table.
   * parameter.
   *
   * @param foSession      - the current session
   * @param fsDocCode      - the value for DOC_CD
   * @param fsDocId        - the value for DOC_ID
   * @param fiDocVersionNo - the value for DOC_VERS_NO
   * @param fsDocDept      - the value for DOC_DEPT_CD
   * @param fsUserId       - the value for USER_ID
   * @param fiApplId       - the value for APPL_ID
   * @return boolean       - status on the success of the insert
   */
   public static boolean insert(Session foSession, String fsDocCode,
            String fsDocId, int fiDocVersionNo, String fsDocDept,
            String fsUserId, int fiApplId)
   {
      DOC_DISCARD_LOGImpl loDocDiscLogRec = null;
      try
      {
         loDocDiscLogRec = getNewObject(foSession, true);

         loDocDiscLogRec.setDOC_CD(fsDocCode);
         loDocDiscLogRec.setDOC_ID(fsDocId);
         loDocDiscLogRec.setDOC_VERS_NO(fiDocVersionNo);
         loDocDiscLogRec.setDOC_DEPT_CD(fsDocDept);
         loDocDiscLogRec.setEVNT_DT(AMSUtil.getSystemDateAndTime());
         loDocDiscLogRec.setUSER_ID(fsUserId);
         loDocDiscLogRec.setAPPL_ID(fiApplId);

         loDocDiscLogRec.save();
      }
      catch (Exception loExcep)
      {
         loDocDiscLogRec.raiseException("Error while inserting into DOC_DISCARD_LOG.");
         return false;
      }
      return true;
   }
}

