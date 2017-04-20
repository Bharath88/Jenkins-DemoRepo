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
    **  R_PRNT_JOB
    */

//{{COMPONENT_RULES_CLASS_DECL
public class R_PRNT_JOBImpl extends  R_PRNT_JOBBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
   {

      public static final String INVLD_COMBINATION =
       "The combination of Application Resource " +
       "Id and Print Job Name is not defined " +
       "in the Print Job table.";

         public static final String ALL ="*ALL";

//{{COMP_CLASS_CTOR
public R_PRNT_JOBImpl (){
	super();
}

public R_PRNT_JOBImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

      }


//{{COMPONENT_RULES
	public static R_PRNT_JOBImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_PRNT_JOBImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

      public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	//END_EVENT_ADD_LISTENERS}}
      }

   /**
   * This method will return an instance of R_PRNT_JOBImpl based on the passed
   * parameter, if the record is not found  it will return null.
   *
   * @param fsApplRsrcId      The Application Resource ID
   * @param fsPrintJobName   The Print Job Name
   * @param foSession          The current Session object
   * @return                         an instance of R_PRNT_JOBImpl
   */
   public static R_PRNT_JOBImpl getPrntJobRec(String fsApplRsrcId, String fsPrintJobName,
    Session foSession)
   {
    R_PRNT_JOBImpl loPrnJob = null;
    if (!AMSStringUtil.strIsEmpty(fsApplRsrcId) &&
      !AMSStringUtil.strIsEmpty(fsPrintJobName))
    {
      SearchRequest lsrPrntJob = new SearchRequest() ;
      lsrPrntJob.addParameter("R_PRNT_JOB", "APPL_RSRC_ID", fsApplRsrcId);
      lsrPrntJob.addParameter("R_PRNT_JOB", "PRNT_JOB_CD", fsPrintJobName);
      loPrnJob = (R_PRNT_JOBImpl)R_PRNT_JOBImpl.getObjectByKey(lsrPrntJob, foSession);
    } /* End if (!AMSStringUtil.strIsEmpty(fsApplRsrcId) && */

    return loPrnJob;
   } /* End getPrntJobRec() */



    /**
     *   This method is called to set a comma separated 'stored value'
     *   list based on the display values of the document Phase code.
     *   Before setting this will also validate each comma separated
     *   Document Phase values entered by user with valid values of document
     *   Phase codes.If user enters invalid Document Phase code error will be
     *   thrown."*ALL" is treated as a wildcard for all document phase codes
     *   and it is bypassed from validation.
     *   DOC_PHASES_SUPRTED is a value entered by user in String format like (Draft,Final)
     *   and DOC_PHASES is a string value which is comma separated. e.g "1,3"
     *
     *   @return void
     */
    protected void setDocPhases()
    {
      String lsDocPhasesDV = getDOC_PHASES_SUPRTED();
      StringBuffer lsbDocPhaseSuprtDV = new StringBuffer(1024);
      CVL_DOC_PHASE_CDImpl loDocPhaseRec = null;
      int liDocPhaseSv = 0;
      String lsDocPhaseDv = null;
      ArrayList loDocPhaseCodeNms = null;
      ArrayList loDocPhaseCodes = null;
      SearchRequest loSearchReq = null;

      Enumeration loEnumCVLDocPhase = null;

      String lsDispValue = null;
      StringBuffer lsbStoreValue = new StringBuffer(32);
      String[] loDocPhases = null;
      String lsDelimeter = ",";
      boolean lboolIsValidPhase = false;

      if(!AMSStringUtil.strIsEmpty(lsDocPhasesDV))
      {
      loDocPhaseCodeNms = new ArrayList();
      loDocPhaseCodes = new ArrayList();
      loSearchReq = new SearchRequest();

      //get all records from CVL_DOC_PHASE_CD
      loEnumCVLDocPhase = CVL_DOC_PHASE_CDImpl.getObjects (loSearchReq,getSession());

      while (loEnumCVLDocPhase.hasMoreElements())
      {
        loDocPhaseRec = (CVL_DOC_PHASE_CDImpl)loEnumCVLDocPhase.nextElement();
        loDocPhaseCodeNms.add(loDocPhaseRec.getDOC_PHASE_NM());
        loDocPhaseCodes.add(new Integer(loDocPhaseRec.getDOC_PHASE_CD()));
      }

      //splitting the user entered document phases in tokens
      loDocPhases =  lsDocPhasesDV.split(lsDelimeter);

      for (int liDocPhsCnt = 0;liDocPhsCnt<loDocPhases.length; liDocPhsCnt++)
      {
        lboolIsValidPhase = false;
        lsDispValue = loDocPhases[liDocPhsCnt];
        lsDispValue = AMSStringUtil.strTrim(lsDispValue);

        //skip *ALL from validation
        if (AMSStringUtil.strInsensitiveEqual(lsDispValue,ALL))
        {
          lsbStoreValue.append(CVL_DOC_PHASE_CDImpl.ALL_DOC_PHASES);
          lsbStoreValue.append(",");
          lsbDocPhaseSuprtDV.append(ALL);
          lsbDocPhaseSuprtDV.append(",");
        }
        /*
        ArrayList loDocPhaseCodes is used to check if
        the DOC_PHASES_SUPRTED field (which is in CAPITAL letters)
        is matching with any of the value of DOC_PHASE_NM in
        CVL_DOC_PHASE_CD  (which is in mix case).
        Following logic will take matching value of document phase
        from array and will use that value to get record of
        CVL_DOC_PHASE_CD
        */
        else
        {
          for (int liCnt = 0; liCnt < loDocPhaseCodes.size(); liCnt++)
          {
            lsDocPhaseDv = AMSStringUtil.strTrim(
            (String)loDocPhaseCodeNms.get(liCnt));

            if(AMSStringUtil.strInsensitiveEqual(lsDispValue,lsDocPhaseDv ))
            {
             lsbStoreValue.append((Integer)loDocPhaseCodes.get(liCnt));
             lsbStoreValue.append(",");
             lsbDocPhaseSuprtDV.append(lsDocPhaseDv);
             lsbDocPhaseSuprtDV.append(",");
             lboolIsValidPhase = true;
             break;
            }
          }

          /* lboolIsValidPhase is false means Doc phase is not valid
            and error will be thrown
           */
          if (!lboolIsValidPhase)
          {
            raiseException("%c:A4232,v:" + lsDispValue + "%");
          }
        }

      }

      //to delete the extra comma at the end from DOC_PHASES
      if(lsbStoreValue.length() > 0)
      {
        lsbStoreValue.deleteCharAt(lsbStoreValue.length() - 1);
      }

      //to delete the extra comma at the end from DOC_PHASES_SUPRTED
      if(lsbDocPhaseSuprtDV.length() > 0)
      {
         lsbDocPhaseSuprtDV.deleteCharAt(lsbDocPhaseSuprtDV.length() - 1);
      }


      }// end if(!AMSStringUtil.strIsEmpty(lsDocPhasesDV))

      setDOC_PHASES(lsbStoreValue.toString());
      setDOC_PHASES_SUPRTED(lsbDocPhaseSuprtDV.toString());

    }//end setDocPhases()


}
