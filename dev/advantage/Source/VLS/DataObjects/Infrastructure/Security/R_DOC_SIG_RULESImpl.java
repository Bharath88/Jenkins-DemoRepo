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
**  R_DOC_SIG_RULES
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_DOC_SIG_RULESImpl extends  R_DOC_SIG_RULESBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_DOC_SIG_RULESImpl (){
	super();
}

public R_DOC_SIG_RULESImpl(Session session, boolean makeDefaults)
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
	public static R_DOC_SIG_RULESImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_DOC_SIG_RULESImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}



   /**
    * Method to set the display only RULE attribute on Digital Signature Rule Table.
    * This method will read the OR conditions specified on the Digital Signature Rule Table
    * and look up the corresponding AND conditions to form the display String.
    *
    * @return String The combined signature rule.including all AND and OR conditions
    */
   public String setCombSigRule()
   {
      String lsCondId = null;
      String lsDocCd = null;
      String lsRule = null;
      String lsTempRule = null;

      // nothing to generate if Doc Code is blank, though the rule text needs to be blanked
      if (AMSStringUtil.strIsEmpty(getDOC_CD()))
      {
         return "";
      }

      try
      {
         lsCondId = getOR_COND_1();
      }
      catch (Exception loEx)
      {
         raiseException("Error retrieving OR condition 1 information");
         return "";
      }

      // if the first OR condition is blank or zero, set rule to true
      if ( AMSStringUtil.strIsEmpty(lsCondId) || lsCondId.equals( "0" ) )
      {
         lsRule = "true";
      }
      else
      {

         /*
          * create the first rule, example of returned
          * string ... PONUM = "10" AND DATE="2002/01/01"
          */
         lsRule = AdvAbstractRulesProcessor.createRule(this, lsCondId, getSession());
         if (lsRule != null)
         {
            /*
             * create the next 4 possible AND Conditions, separated by OR
             * example of final string is ((PONUM = "10"' AND DOCDATE = "2002/10/10")
             * OR (PONUM = "20" AND DOCDATE = "2002/10/10"))
             */
            for (int liCount = 2; liCount <= 5; liCount++)
            {
               lsTempRule = createOR(liCount) ;
               if (lsTempRule != null)
               {
                  lsRule += "\nOR\n" + lsTempRule ;
               }
            }
         }
      }
      return lsRule;
   }

   /**
    * This method sets up the OR conditions
    * It will retrieve the current OR_COND... row, and pass this OR_COND_X long
    * value to the createRule() method.  Create rule will return the AND condition.
    *
    * @param int fiIndexX the X in OR_COND_X
    * @return String the AND Condition
    */
   public String createOR(int fiIndexX)
   {
      String lsCheckRule = null;
      String lsCond      = null;

      try
      {
         lsCond = getCOND(fiIndexX);
      }
      catch (Exception loEx)
      {
         raiseException("Error retrieving OR condition for " + fiIndexX + " information");
         return "";
      }
      if ( ! ( AMSStringUtil.strIsEmpty(lsCond) || lsCond.equals( "0" ) ) )
      {
         lsCheckRule = AdvAbstractRulesProcessor.createRule(this, lsCond, getSession());
      }
      return lsCheckRule ;
   }

   /**
    * This method gets the attribute value of the condition fields as per the index passed to it.
    *
    * @param fiCondNo number of the condition attribute
    * @return String value of condition attribute
    */
   public String getCOND(int fiCondNo)
   {
      switch (fiCondNo)
      {
         case 1:
         return getOR_COND_1();
         case 2:
         return getOR_COND_2();
         case 3:
         return getOR_COND_3();
         case 4:
         return getOR_COND_4();
         case 5:
         return getOR_COND_5();
         default :
         return null ;
      }
   }
}

