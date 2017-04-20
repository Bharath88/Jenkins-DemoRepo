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
**  STMT_CELL
*/

//{{COMPONENT_RULES_CLASS_DECL
public class STMT_CELLImpl extends  STMT_CELLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public STMT_CELLImpl (){
		super();
	}
	
	public STMT_CELLImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line
	
	setSTMT_CELL(getCell());
	
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	//Write Event Code below this line

	setSTMT_CELL(getCell());
	
}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static STMT_CELLImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new STMT_CELLImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

   /**
    * Registers 'ALL' as a valid value for STMT_ROW and STMT_COL fields.
    */
   public void myInitializations()
   {
      registerForeignKeyException("STMT_ROW", "STMT_ROW", ALL);
      registerForeignKeyException("STMT_COLUMN", "STMT_COL", ALL);
   }
	
   /**
    * Statement Cell value is a combination of Statement Column and Statement Row 
    * separated by a colon (":").
    */
   public String getCell()
   {
      return getSTMT_COL() + ":" + getSTMT_ROW();
   }

   /**
    * Checks if the given Dollar Amount is Negative or Positive.
    * 
    * @param faryChars  The Cell Expression expressed as an array of characters.
    * @param fiCnt      Position of the Amount in the array.
    * @return           true if Negative, false if Positive.
    */
   static boolean isNegativeAmt(char[] faryChars, int fiCnt)
   {
      // Previous character to Current. It is a Non-blank character.
      char lcPrev = 0;
      char lcNext = 0;

      // Start position of the Amount field. The beginning of an Amount field
      // can be either "$" or "-".
      char lcCurr = faryChars[fiCnt];
      int liCnt = fiCnt - 1;

      // Fetch the previous non-blank character.
      while (liCnt >= 0)
      {
         if (faryChars[fiCnt] != ' ')
         {
            lcPrev = faryChars[liCnt];
            break;
         }
         liCnt--;
      }

      if (faryChars.length >= fiCnt)
         lcNext = faryChars[fiCnt + 1];

      /*
       * To check if the current Amount is a Negative Amount or just a subtraction.
       * E.g. ( -$100 ) is a Negative Amount while (RuleA - $100) is a subtraction.
       * 
       * The first line of IF condition checks if a Negative Amount exists.
       * The next line confirms that it is not a subtraction.
       */
      if ( lcCurr == '-' && lcNext == '$' &&
            (lcPrev == 0 || (lcPrev == '(' || lcPrev == '/' || lcPrev == '*')) )
      {
         return true;
      }
      return false;
   }

   /**
    * This method parses the CELL_EXP field for validation. Validation is a 2
    * steps procedure:
    * 
    * 1. Mathematical Integrity 
    *    First check for mathematical integrity i.e. if
    *    the expression is correct mathematically. Any expression is a combination
    *    of Literals and Operators. So in this step, the correct usage is checked.
    *    Operators  :   +, -, *, /, (, ), ' '(blank space) 
    *    Literals   :   Cell Code, Rule Code and Dollar Amounts 
    *    E.g. We cannot have two Literals/Operators(except "(,)") together.
    * 
    * 2. Literal Validation
    *    Literals are of 3 types: Dollar Amounts, Cell Code and Rule Code.
    *    a. Dollar Amounts
    *       They are identified by a leading "$" sign. In addition to numbers, an amount may 
    *       contain special characters like: 
    *       '$' :   A compulsory Dollar sign signifying that this is an amount literal.
    *       '-' :   Signifying a negative value
    *       ',' :   Commas for separating thousand, million, billion
    *       '.' :   A decimal point for storing up to 2 decimals.
    *    b. Cell Code
    *       Cell Code is identified by having a ":" in it. Once a Cell Code is identified, it is
    *       verified against Statement Cell Table to check if it's a valid one.
    *    c. Rule Code
    *       If a literal is neither Dollar Amount nor Cell Code, then it is assumed to be Rule Code.
    *       Rule Code is verified against Statement Rule Table to check if it's a valid one.
    */
   public String populateRuleCodes()
   {
      String lsCellExpr = getCELL_EXP();
      // If Column or Row has 'ALL', then Cell Expression cannot be entered. Hence the field 
      // is disabled (on Page Side) and any existing value is cleared. 
      if (lsCellExpr == null || AMSStringUtil.strEqual(getSTMT_COL(), ALL) || 
            AMSStringUtil.strEqual(getSTMT_ROW(), ALL))
      {
         setCELL_EXP("");
         return null;
      }
      
      // Convert the displayed cell expression to character array
      char[] laChars = lsCellExpr.toCharArray();
      String[] lsaLiteral = new String[256];
      String[] lsaLiteralCodes = new String[256];
      int liCDLiteral = 0;
      int liOPLiteral = 0;
      char[] laOprChars = new char[256];
      // Counter for tracking Opening Parentheses.
      int liOpenParentheses = 0;
      // Counter for tracking Closing Parentheses.
      int liCloseParentheses = 0;
      // Character previous to current
      Character lcPrev = null;
      // Character previous to lcPrev
      Character lcPrev2 = null;
      // If previous character was a literal i.e. Rule/ Cell/ Amount
      boolean lboolPrevLiteral = false;
      // If character previous to lboolPrevLiteral was a literal i.e. Rule/Cell/Amount
      boolean lboolPrevLiteral2 = false;


      /***** Step 1:    Check for Mathematical Integrity. *****/
      
      /* 
       * Loop through each character and parse the cell expression string.
       * Parsing will be done such that all operators will be stored in one array and 
       * the Rule Code/ Cell Code/ Amount will be stored in different array.
       */
      for (int liCnt = 0; liCnt < laChars.length; liCnt++)
      {
         if ( laChars[liCnt] == '+' || laChars[liCnt] == ' ' ||
               (laChars[liCnt] == '-' && !isNegativeAmt(laChars, liCnt)) ||
               laChars[liCnt] == '*' || laChars[liCnt] == '/' ||
               laChars[liCnt] == '(' || laChars[liCnt] == ')' )
         {
            // Add it to the Operator character array
            laOprChars[liOPLiteral++] = laChars[liCnt];

            if (laChars[liCnt] == '(')
            {
               liOpenParentheses++;
               //  "...RULE(..."         "...)(..."
               if (lboolPrevLiteral || (lcPrev != null && lcPrev == ')') ||
                     // "+(..." - Beginning Expr with an operator and bracket
                     (lcPrev != null && liCnt == 1 && !lboolPrevLiteral && lcPrev != '(') ||
                     // "...RULE("
                     (lcPrev2 != null && !lboolPrevLiteral && !lboolPrevLiteral2 && lcPrev == ')'))
               {
                  raiseException("Invalid Parentheses.");
                  return lsCellExpr;
               }
            } 
            else if (laChars[liCnt] == ')')
            {
               liCloseParentheses++;
               if (liOpenParentheses == 0 || liCloseParentheses > liOpenParentheses ||
                     // ...+)...
                     (lcPrev != null && lcPrev != ')' && !lboolPrevLiteral))
               {
                  raiseException("Invalid Parentheses.");
                  return lsCellExpr;
               }
            }

            if (laChars[liCnt] != ' ')
            {
               lboolPrevLiteral2 = lboolPrevLiteral;
               lboolPrevLiteral = false;
            }
         } 
         else
         {
            // This char is forming part of the Rule/Cell Code. Add it to different array.
            if (AMSStringUtil.strIsEmpty(lsaLiteral[liCDLiteral]))
            {
               // To maintain the position of the code add a '?' character in operator array.
               laOprChars[liOPLiteral++] = '?';

               // Rule/Cell Code string start
               lsaLiteral[liCDLiteral] = String.valueOf(laChars[liCnt]);
            } 
            else
            {
               // continuation of Rule/Cell Code
               lsaLiteral[liCDLiteral] += String.valueOf(laChars[liCnt]);
            }

            lboolPrevLiteral2 = lboolPrevLiteral;
            lboolPrevLiteral = true;
         }

         // Counter to track the count of the Rule/Cell code literals.
         // If character is not an Operator, then it is a Literal.
         if (!(laChars[liCnt] == '(' || laChars[liCnt] == ')' || laChars[liCnt] == ' ') &&
               (laChars[liCnt] == '+'
                     || (laChars[liCnt] == '-' && !isNegativeAmt(laChars, liCnt))
                     || laChars[liCnt] == '*' || laChars[liCnt] == '/'))
         {
            liCDLiteral++;
         }
         if (laChars[liCnt] != ' ')
         {
            lcPrev2 = lcPrev;
            lcPrev = laChars[liCnt];
         }
      }

      if (liCloseParentheses != liOpenParentheses)
      {
         raiseException("Invalid Parentheses.");
         return lsCellExpr;
      }

      
      /***** Step 2:    Check for Literal Validation. *****/
      
      for (int liCnt = 0; liCnt <= liCDLiteral; liCnt++)
      {
         if (lsaLiteral[liCnt] == null)
         {
            raiseException("Invalid Cell Expression");
            return lsCellExpr;
         }
         int liFirstCharASCII_CD = (int) lsaLiteral[liCnt].charAt(0);

         // If ASCII Code of 1st char is 36($) or 45(-), then it means this is an amount.
         if (liFirstCharASCII_CD == 36 || liFirstCharASCII_CD == 45)
         {
            for (int liAmtCnt = 0; liAmtCnt < lsaLiteral[liCnt].length(); liAmtCnt++)
            {
               int liCharASCIIVal = lsaLiteral[liCnt].charAt(liAmtCnt);
               // An Amount field can only consist of 36($), 44(-), 45(,), 46(.), >47(0) & <58(9)  
               if (!(liCharASCIIVal == 36 || liCharASCIIVal == 44 || liCharASCIIVal == 45 || 
                     liCharASCIIVal == 46 || (liCharASCIIVal > 47 && liCharASCIIVal < 58) ))
               {
                  raiseException("Invalid literal " + lsaLiteral[liCnt] + " in Cell Expression");
                  return lsCellExpr;
               }
            }
            // If Amount is valid, add it to the array of Literals.
            lsaLiteralCodes[liCnt] = lsaLiteral[liCnt];
         }
         // If the Literal contains a ':' then it means this is a Cell Code. 
         else if (lsaLiteral[liCnt].indexOf(":") > -1)
         {
            if ( !checkCellExists(lsaLiteral[liCnt]) )
            {
               raiseException("Invalid Code " + lsaLiteral[liCnt] + " in Cell Expression");
               return lsCellExpr;
            }
            // If Cell Code is valid, add it to the array of Literals.
            lsaLiteralCodes[liCnt] = lsaLiteral[liCnt];
         } 
         else
         {
            if (! checkRuleExists(lsaLiteral[liCnt]) )
            {
               raiseException("Invalid Code " + lsaLiteral[liCnt] + " in Cell Expression");
               return lsCellExpr;
            }
            // If Rule Code is valid, add it to the array of Literals.
            lsaLiteralCodes[liCnt] = lsaLiteral[liCnt];
         }
      }

      // Re-construct the CELL_EXP
      StringBuffer lsbFinalCellExpr = new StringBuffer(256);
      for (int liCnt = 0, liCdCnt = 0; liCnt < liOPLiteral; liCnt++)
      {
         // '?' denotes presence of Rule/Cell/Amount
         if (laOprChars[liCnt] == '?')
         {
            lsbFinalCellExpr.append(lsaLiteralCodes[liCdCnt++]);
         } 
         else
         {
            lsbFinalCellExpr.append(laOprChars[liCnt]);
         }
      }

      return lsbFinalCellExpr.toString();
   }
		

   /**
    * Queries STMT_RULE table to check if the Rule Code exists.
    * @param fsRule     The Rule Code to be validated.
    * @return
    */
   public boolean checkRuleExists(String fsRule)
   {
      if(AMSStringUtil.strEqual("DEFAULT", fsRule))
         return true;
      SearchRequest lsrRuleCd = new SearchRequest();
      lsrRuleCd.addParameter("STMT_RULE", "STMT_FY", String.valueOf(getSTMT_FY()));
      lsrRuleCd.addParameter("STMT_RULE", "STMT_CD", getSTMT_CD());
      lsrRuleCd.addParameter("STMT_RULE", "STMT_ROW", getSTMT_ROW());
      lsrRuleCd.addParameter("STMT_RULE", "STMT_COL", getSTMT_COL());
      lsrRuleCd.addParameter("STMT_RULE", "RULE_CD", fsRule);

      int liRule = STMT_RULEImpl.getObjectCount(lsrRuleCd, getSession());
      return liRule > 0 ? true : false;
   }
      

   /**
    * 
    * @param fsCell
    * @return
    */
   public boolean checkCellExists(String fsCell)
   {
      String[] laryCodes = fsCell.split(":");
      boolean lboolRow = false;
      boolean lboolCol = false;

      if (AMSStringUtil.strEqual(laryCodes[1], ALL))
      {
         lboolRow = true;
      } 
      else
      {
         SearchRequest lsrRowCd = new SearchRequest();
         lsrRowCd.addParameter("STMT_ROW", "STMT_FY", String.valueOf(getSTMT_FY()));
         lsrRowCd.addParameter("STMT_ROW", "STMT_CD", getSTMT_CD());
         lsrRowCd.addParameter("STMT_ROW", "STMT_ROW", laryCodes[1]);
         lboolRow = STMT_ROWImpl.getObjectCount(lsrRowCd, getSession()) > 0 ? true:false;
      }

      if (AMSStringUtil.strEqual(laryCodes[0], ALL))
      {
         lboolCol = true;
      } 
      else
      {
         SearchRequest lsrColCd = new SearchRequest();
         lsrColCd.addParameter("STMT_COLUMN", "STMT_FY", String.valueOf(getSTMT_FY()));
         lsrColCd.addParameter("STMT_COLUMN", "STMT_CD", getSTMT_CD());
         lsrColCd.addParameter("STMT_COLUMN", "STMT_COL", laryCodes[0]);
         lboolCol = STMT_COLUMNImpl.getObjectCount(lsrColCd, getSession()) > 0 ? true:false;
      }

      return lboolRow && lboolCol;
   }

}

