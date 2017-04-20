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

import java.util.* ;
import com.amsinc.gems.adv.common.AMSSQLUtil ;
/*
**  R_WF_APRV_COND
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_APRV_CONDImpl extends  R_WF_APRV_CONDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{

   /* Constant indicating dot character*/
   private static final char DOT_CHAR = '.';
   /* Constant indicating dot character*/
   private static final char UNDER_SCORE_CHAR = '_';
   /* Constant indicating the header component suffix*/
   private static final String  HDR_SUFFIX= "_DOC_HDR";

//{{COMP_CLASS_CTOR
public R_WF_APRV_CONDImpl (){
	super();
}

public R_WF_APRV_CONDImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_WF_APRV_CONDImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_WF_APRV_CONDImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

   /**
    * method to get the AND_COND_LHS_x attribute for the R_WF_APRV_COND
    * @param int fiIndex - the number of the AND_COND_LHS_x
    * @return boolean : the  value of the attribute.
    * @author - Mark Farrell
    */

   public String getAND_COND_LHS(int fiIndex)
   {
       switch (fiIndex)
       {
          case 1 :
             return getAND_COND_LHS_1();
          case 2 :
             return getAND_COND_LHS_2();
          case 3 :
             return getAND_COND_LHS_3();
          case 4 :
             return getAND_COND_LHS_4();
          case 5 :
             return getAND_COND_LHS_5();
          default :
             return null ;

       } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the AND_COND_RHS_x attribute for the R_WF_APRV_COND
    * @param int fiIndex - the number of the AND_COND_RHS_x
    * @return boolean : the  value of the attribute.
    */

   public String getAND_COND_RHS(int fiIndex)
   {
       switch (fiIndex)
       {
          case 1 :
             return getAND_COND_RHS_1();
          case 2 :
             return getAND_COND_RHS_2();
          case 3 :
             return getAND_COND_RHS_3();
          case 4 :
             return getAND_COND_RHS_4();
          case 5 :
             return getAND_COND_RHS_5();
          default :
             return null ;

       } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the AND_COND_OPR_x attribute for the R_WF_APRV_COND
    * @param int fiIndex - the number of the AND_COND_OPR_x
    * @return byte : the  value of the attribute.
    * @author - Mark Farrell
    */

   public byte getAND_COND_OPR(int fiIndex)
   {
       switch (fiIndex)
       {
          case 1 :
             return getAND_COND_OPR_1();
          case 2 :
             return getAND_COND_OPR_2();
          case 3 :
             return getAND_COND_OPR_3();
          case 4 :
             return getAND_COND_OPR_4();
          case 5 :
             return getAND_COND_OPR_5();
          default :
             return -1 ;

       } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the AND_COND_TYP_x attribute for the R_WF_APRV_COND
    * @param int fiIndex - the number of the AND_COND_TYP_x
    * @return int : Returns Condition Type for the given Condition number(1-5).
    * Returns -1 if an invalid Condition number is given..
    */
   public int getAND_COND_TYP(int fiIndex)
   {
      switch (fiIndex)
      {
         case 1 :
            return getAND_COND_TYP_1();
         case 2 :
            return getAND_COND_TYP_2();
         case 3 :
            return getAND_COND_TYP_3();
         case 4 :
            return getAND_COND_TYP_4();
         case 5 :
            return getAND_COND_TYP_5();
         default :
            return -1 ;
      } /* end switch ( fiIndex ) */
  }// End of getAND_COND_TYP()

   /**
    * validation on Doc Code.
    * if the document code is null, the condition id must be zero.
    * if the document code is not null, this validation is not used, return true.
    * @param fsDocCode as String - the document code.
    * @param fsCondID as String - the condition id.
    *
    * @return boolean - true if doc code null and condition id zero
    */

   public boolean checkDocCode(String fsDocCode,String fsCondID)
   {
      if ( fsDocCode == null )
      {
      if (fsCondID == null)
      {
         return false;
      }
         if (fsCondID.equalsIgnoreCase("0"))
         {
            return true ;
         }
         else
         {
            return false ;
         } /* end if (fsCondID.equalsIgnoreCase("0") */
      } /* end if ( fsDocCode == null ) */
      return true ;
   }

   /**
    * This method will check to see if the document and document
    * field, which is either in the right or left hand side of a
    * condition, is valid on R_WF_APRV_FLD.
    *
    * @param foEnum - Enumeration of the R_WF_APRV_FLD objects
    * @param fsRightLeft as String - the right or left hand condition.
    * @return boolean - true if condition is in the Enumeration.
    */
   private boolean checkFields(Enumeration foEnum, String fsRightLeft )
   {
      String lsComb_Doc_At = null ;

      while (foEnum.hasMoreElements() )
      {
         R_WF_APRV_FLDImpl loFldRow = (R_WF_APRV_FLDImpl)foEnum.nextElement() ;

         if (loFldRow != null)
         {
            lsComb_Doc_At = loFldRow.getCOMB_DOC_AT() ;
            if (lsComb_Doc_At != null)
            {
               if ( lsComb_Doc_At.equalsIgnoreCase( fsRightLeft ) )
               {
                  return true ;
               } /* end if (lsComb_Doc_At.equalsIgnoreCase(fsRightLeft) */
            } /* end if (lsComb_Doc_At != null) */
         } /* end if (loFldRow != null) */
      } /* end while (loEnum.hasMoreElements() */

      /* If the document code/field is not found, return false */
      return false ;
   }

   /**
    * This method ensures that the first condition is in
    * condition 1 and that all subsequent conditions are
    * continuous. If not, then it repositions the conditions
    * in a contiguous manner. For example, if conditions
    * 2,4, and 5 are entered, this method will reposition
    * them to conditions 1,2, and 3, respectively.
    */
   private void ensureConditionContinuity( )
   {
      /* If the operators are not valid, then do not move the conditions */
      if ( checkOperators() )
      {
         /*
          * When Left Hand Side, Operator and Right Hand Side for a Condition
          * are empty then infer the default value, i.e., set Operator to
          * CVL_WF_OPRSImpl.EQUAL. Left Hand Side and Right Hand Side will continue
          * to remain empty.
          */
         defaultForEmptyConditions();

         int liCount = 0;
         int liNumber = 0 ;
         int liCurrentRow = 0;
         Data loNewData = null ;
         Data loOldData = null ;

         /* Cycle through the conditions.  Make sure the conditions in front */
         /* of current condition are not blank, if so, move current condition up */

         for (liCount = 2; liCount < 6; liCount++ )
         {
             liNumber = liCount ;
             while (liNumber > 1)
             {
                liCurrentRow = liCount - ( liNumber - 1 );
                if (getAND_COND_LHS(liCurrentRow) == null)
                {
                    /*
                     * copy old condition to new condition, or condition 2 to 1 ...
                     * first get data from new left hand side, and get data from
                     * old left hand side, and copy new to old.  Repeat for right
                     * hand side , operator and Condition Type.
                     */

                    loNewData = getData("AND_COND_LHS_" + liCount ) ;
                    loOldData = getData("AND_COND_LHS_" + liCurrentRow ) ;
                    loOldData.setString(loNewData.getString() );
                    loNewData = getData("AND_COND_RHS_" + liCount ) ;
                    loOldData = getData("AND_COND_RHS_" + liCurrentRow ) ;
                    loOldData.setString(loNewData.getString() );
                    loNewData = getData("AND_COND_RHS_FLD_" + liCount ) ;
                    loOldData = getData("AND_COND_RHS_FLD_" + liCurrentRow ) ;
                    loOldData.setString(loNewData.getString() );
                    loNewData = getData("AND_COND_OPR_" + liCount ) ;
                    loOldData = getData("AND_COND_OPR_" + liCurrentRow ) ;
                    loOldData.setbyte(loNewData.getbyte() );
                    loNewData = getData("AND_COND_TYP_" + liCount ) ;
                    loOldData = getData("AND_COND_TYP_" + liCurrentRow ) ;
                    loOldData.setint(loNewData.getint() );
                    /*
                     * reset the old condition back to null ,operator to default 3
                     * and Condition type to 1 (i.e. Actual Value)
                     */
                    loNewData = getData("AND_COND_LHS_" + liCount ) ;
                    loNewData.setString(null);
                    loNewData = getData("AND_COND_RHS_" + liCount ) ;
                    loNewData.setString(null);
                    loNewData = getData("AND_COND_RHS_FLD_" + liCount ) ;
                    loNewData.setString(null);
                    loNewData = getData("AND_COND_OPR_" + liCount ) ;
                    loNewData.setbyte((byte)3);
                    loNewData = getData("AND_COND_TYP_" + liCount ) ;
                    loNewData.setint(CVL_WF_COND_TYPImpl.ACTUAL_VAL);

                } /* end if (getAND_COND_LHS(liCurrentRow) == null) */

                liNumber-- ;

             } /* end while (liNumber > 1) */
         } /* end for (liCount = 2; liCount < 6; liCount++ ) */
      } /* end if ( checkOperators() ) */
   }

   /**
    * method to get the type of attribute field, associated
    * with the left hand side data object:attribute.  The
    * data object will be found, and then the attribute, and then
    * the type of data of the attribute (String, int, boolean, etc.)
    * A code representing this type of code will be returned.
    *
    * @param fsDocComp - the data object to find.
    * @param fsDocFld - the attribute, or column name, to find
    * @return int - the data type of attribute, as an int.
    *
    * @author - Mark Farrell - 06/26/00 initial version
    *         - Greg Hodum   - 11/26/02 simplified method by
    *                          just calling AMSDataObject.getMetaQuery()
    */
   public int getFieldType( String fsDocComp, String fsDocFld )
   {
      VSMetaQuery loMetaQry ;
      Session     loSession = getSession() ;

      try
      {
         loMetaQry =  AMSDataObject.constructMetaQueryFromMetaInfoTable( fsDocComp, loSession ) ;
      } /* end try */
      catch( Exception foExp )
      {
         loMetaQry = null ;
         loMetaQry = getMetaQuery( fsDocComp, loSession.getPackageName() ) ;
      } /* end catch( Exception foExp ) */

      if ( loMetaQry != null )
      {
         VSMetaColumn loMetaCol = loMetaQry.getMetaColumn( fsDocFld ) ;

         if ( loMetaCol != null )
         {
            return loMetaCol.getColumnType() ;
         } /* end if ( loMetaCol != null ) */
      } /* end if ( loMetaQry != null ) */

      return DataConst.NULL ;
   } /* end getFieldType() */

   /**
    * This method checks that the value is valid for the given
    * approval field data type
    *
    * @param fiFieldType the workflow approval field data type
    * @param fsValue The value of the approval field condition
    * @param fiIndex The index of the approval condition
    */

   private void checkValue(int fiFieldType, String fsValue, int fiIndex)
   {
      switch ( fiFieldType )
      {
         // text fields
         case DataConst.CHAR :
         case DataConst.VARCHAR :
         case DataConst.LONGVARCHAR :
         {
            try
            {
               // check if first and last characters on are quotes.
               String lsRHS = fsValue.trim();
               char [] lcChar = lsRHS.toCharArray();

               if ((lcChar[0] == '"') && (lcChar[lsRHS.length() - 1] == '"'))
               {
                  return ;
               } /* end if ((lcChar[0] == '"') && ... */
               else
               {
                  raiseException( "%c:Q0079%" ) ;
               } /* end else */
            } /* end try */
            catch (RuntimeException loException)
            {
               raiseException( "%c:Q0079%" ) ;
            } /* end catch */
            break ;
         } /* case DataConst.CHAR */
         case DataConst.INTEGER :
         {
            try
            {
               Integer loInt = new Integer(fsValue);
               return ;
            }
            catch (NumberFormatException loException)
            {
               raiseException( "%c:Q0080,v: " + Integer.MIN_VALUE +
                               ",v:" + Integer.MAX_VALUE + "%" ) ;
            }
            break ;
         } /* case DataConst.INTEGER */
         case DataConst.BIGINT :
         {
            try
            {
               Long loLong = new Long(fsValue);
               return ;
            }
            catch (NumberFormatException loException)
            {
               raiseException( "%c:Q0081,v: " + Long.MIN_VALUE +
                               ",v:" + Long.MAX_VALUE + "%" ) ;
            }
            break ;
         } /* case DataConst.BIGINT */
         case DataConst.SMALLINT :
         case DataConst.TINYINT :
         {
            try
            {
               Byte loByte = new Byte(fsValue);
               return ;
            }
            catch (NumberFormatException loException)
            {
               raiseException( "%c:Q0082,v: "+ Byte.MIN_VALUE +
                               ",v:" + Byte.MAX_VALUE + "%" ) ;
            }
            break ;
         } /* case DataConst.SMALLINT */
         case DataConst.REAL :
         {
            try
            {
               Float loFloat = new Float(fsValue);
               return ;
            }
            catch (NumberFormatException loException)
            {
               raiseException( "%c:Q0083,v: " + Float.MIN_VALUE +
                               ",v:" + Float.MAX_VALUE + "%" ) ;
            }
            break ;
         } /* case DataConst.REAL */
         case DataConst.DOUBLE :
         case DataConst.FLOAT :
         case DataConst.DECIMAL :
         {
            try
            {
               Double loDouble = new Double(fsValue);
               return ;
            }
            catch (NumberFormatException loException)
            {
               raiseException( "%c:Q0084,v: " + Double.MIN_VALUE +
                               ",v:" + Double.MAX_VALUE + "%" ) ;
            }
            break ;
         } /* case DataConst.DOUBLE */
         case DataConst.NUMERIC :
         {
            try
            {
               BigDecimal loBigDec = new BigDecimal(fsValue);
               return ;
            }
            catch (NumberFormatException loException)
            {
               raiseException( "%c:Q0085%" ) ;
            }
            break ;
         } /* case DataConst.NUMERIC */
         case DataConst.BIT :
         {
            String lsRHS = fsValue.trim();
            if (lsRHS.equalsIgnoreCase("\"true\"") ||
                lsRHS.equalsIgnoreCase("\"false\""))
            {
               if ( !( lsRHS.equals("\"true\"") ) || !( lsRHS.equals("\"false\"") ) )
               {

                 Data loNewData = getData("AND_COND_RHS_" + fiIndex ) ;

                 if ( lsRHS.equalsIgnoreCase("\"true\"") )
                 {
                    loNewData.setString("\"true\"") ;
                    raiseException( "Value " + lsRHS + " has been modified to \"true\"",
                          AMSMsgUtil.SEVERITY_LEVEL_WARNING ) ;
                 } /* end if ( lsRHS.equalsIgnoreCase("\"true\"") ) */
                 else
                 {
                    loNewData.setString("\"false\"") ;
                    raiseException( "Value " + lsRHS + " has been modified to \"false\"",
                          AMSMsgUtil.SEVERITY_LEVEL_WARNING ) ;
                 } /* end else */

               } /* end if ( !( lsRHS.equals("\"true\"") ) || !( lsRHS.equals("\"false\"") ) ) */
               return ;
            }
            else
            {
               raiseException( "%c:Q0086%" ) ;
            }
            break ;
         } /* case DataConst.BIT */
         case DataConst.TIMESTAMP :
         case DataConst.DATE :
         {
            // date must be in format "MM/dd/yyyy" - within quotes
            try
            {
               // check if first and last characters on are quotes.
               String lsRHS = fsValue.trim();
               char [] lcChar = lsRHS.toCharArray();

               if ( ( lcChar[0] == '"' ) &&
                    ( lcChar[lsRHS.length() - 1] == '"' ) )
               {
                  SimpleDateFormat loDateFormat = new SimpleDateFormat( "\"MM/dd/yyyy\"" ) ;

                  // create the Date object
                  Date loJavaDate = loDateFormat.parse( lsRHS, new ParsePosition( 0 ) ) ;

                  if ( loJavaDate == null )
                  {
                     raiseException( "%c:Q0087%" ) ;
                  }

                  VSDate loDate = new VSDate( loJavaDate ) ;
                  return ;
               }
               else
               {
                  raiseException( "%c:Q0087%" ) ;
               }
            }
            catch (RuntimeException loException)
            {
               raiseException( "%c:Q0087%" ) ;
            }
            break ;

         } /* case DataConst.TIMESTAMP */
         default :
         {
            raiseException( "%c:Q0088%" ) ;
            return ;
         } /* end default */

      } /* end switch ( fiFieldType ) */
   }

   /**
    * method to parse out a string and return either the month,
    * day, or year as an int.
    *
    * @param fsRight - the right hand side of the condition
    * @param fiStart - the starting offset
    * @param fiEnd - the ending offset
    * @return int - return the day, month, or year as an int.
    * @throws RuntimeException
    *
    * @author - Mark Farrell - 06/26/00 initial version
    */

   private static int parseValue(String fsRight, int fiStart, int fiEnd)
      throws NumberFormatException
   {
      int liReturn = 0 ;  // the day, month, or year to be returned.
      String lsValue = fsRight.substring(fiStart,fiEnd);

      return Integer.decode( lsValue ).intValue() ;
   }

   /**
    * This method verifies that if a left hand side value has
    * been specified that an operator has also been specified.
    * Also it verifies that if a left hand side value has been specified
    * that Condition type has also been specified.
    * And if a right hand side value has been specified
    * that Condition type has also been specified.
    * It does this for each condition 1-5.If not, then it raises an exception.
    *
    * @return Indicates if no errors were found
    */
   protected boolean checkOperators()
   {
      Object  loLeft ;
      Object  loRight ;
      Object  loOperator ;
      Object  loConditionType ;
      boolean lboolOK    = true ;

      for ( int liCtr = 1 ; liCtr <= 5 ; liCtr++ )
      {
         loLeft               = getData( "AND_COND_LHS_" + liCtr ).getObject() ;
         loConditionType      = getData( "AND_COND_TYP_" + liCtr ).getObject() ;
         loOperator           = getData( "AND_COND_OPR_" + liCtr ).getObject() ;
         loRight              = getData( "AND_COND_RHS_" + liCtr ).getObject() ;
         if ( ( loLeft != null ) && ( loOperator == null ) )
         {
            lboolOK = false ;
            raiseException( "An operator is required for Condition " + liCtr ) ;
         } /* end if ( ( loLeft != null ) && ( loOperator == null ) ) */

         if (loConditionType == null)
         {
            if(loLeft != null)
            {
               lboolOK = false ;
               raiseException( "%c:Q0150,v: Condition Type for Condition " +
                  liCtr + ",v: Left Hand Side is %" ) ;
            }
            if(loRight != null)
            {
               lboolOK = false ;
               raiseException( "%c:Q0150,v: Condition Type for Condition " +
                  liCtr + ",v: Right Hand Side is %" ) ;
            }
         } /* end if (loConditionType == null) */
      } /* end for ( int liCtr = 1 ; liCtr <= 5 ; liCtr++ ) */
      return lboolOK ;
   } /* end checkOperators() */

   /**
    * This method verifies that fields from only one non-header
    * component is used in the condition.
    */
   protected void checkComponentList()
   {
      String    lsLeft ;
      String    lsDocComp ;
      int       liDotIdx ;
      int       liNumComps ;
      Hashtable loCompHash = new Hashtable( 5 ) ;

      for ( int liCtr = 1 ; liCtr <= 5 ; liCtr++ )
      {
         lsLeft = getData( "AND_COND_LHS_" + liCtr ).getString() ;
         if ( lsLeft != null )
         {
            liDotIdx = lsLeft.indexOf( '.' ) ;
            if ( liDotIdx >= 0 )
            {
               lsDocComp = lsLeft.substring( 0, liDotIdx ) ;
            } /* end if ( liDotIdx >= 0 ) */
            else
            {
               lsDocComp = lsLeft ;
            } /* end else */
            loCompHash.put( lsDocComp, lsDocComp ) ;
         } /* end if ( lsLeft != null ) */
      } /* end for ( int liCtr = 1 ; liCtr <= 5 ; liCtr++ ) */

      liNumComps = loCompHash.size() ;
      if ( liNumComps > 2 )
      {
         raiseException( "%c:Q0089%" ) ;
      } /* end if ( liNumComps > 2 ) */
      else if ( liNumComps == 2 )
      {
         Enumeration leComps = loCompHash.elements() ;
         String      lsComp1 = (String)leComps.nextElement() ;
         String      lsComp2 = (String)leComps.nextElement() ;

         if ( ( !lsComp1.endsWith( "_DOC_HDR" ) ) &&
              ( !lsComp2.endsWith( "_DOC_HDR" ) ) )
         {
            raiseException( "%c:Q0089%" ) ;
         } /* end if ( ( !lsComp1.endsWith( "_DOC_HDR" ) ) && . . . */
      } /* end else if ( liNumComps == 2 ) */
   } /* end checkComponentList() */

   /**
    * This method performs all condition evaluations. Namely,
    * it will first make all conditions (1-5) contiguous,
    * removing any blanks in the middle. It will then check
    * that only one non-header component is used across the
    * five conditions. Finally, it will evaluate each condition.
    */
   protected void evaluateConditions()
   {
      ensureConditionContinuity() ;
      checkComponentList() ;

      for ( int liCondCtr = 1 ; liCondCtr <= 5 ; liCondCtr++ )
      {
         checkCondition( liCondCtr ) ;
      } /* end for */

   } /* end evaluateConditions() */

   /**
    * This method checks the following for each condition:
    *
    * <ul>
    * <li>If there is a value in the left hand side or the
    *     right hand side, then a value must exist for the
    *     left, for the right, and for the operator.
    * <li>If the field in the left hand side is a valid
    *     workflow approval field
    * <li>If the right hand side value/field is valid for the
    *     approval field and operator combination.
    * <li>If there is a value in the left hand side, the
    *     right hand side and Operator then a value must
    *     exist for the Condition Type.
    * <li>If Operator is 'In List' or 'Not In List' or 'Is Null'
    *     or 'Is Not Null' then Approval Condition Type
    *     must be 'Actual Value'.
    * <li>If Condition Type is Delta Change, Percent Change,
    *     Absolute Delta Change or Absolute Percent Change.
    *     then Right hand side value must be numeric data.
    * <li>If Condition Type is Delta Change, Percent Change,
    *     Absolute Delta Change or Absolute Percent Change.
    *     then field of Left hand side condition must store
    *     Dollar Amount or Quantity.
    * </ul>
    *
    * @param fiIndex index for the condition
    */
   private void checkCondition( int fiIndex )
   {
      Vector           lvVals = new Vector();
      int              liNumTokens = 0;
      String           lsDocCd = null ;
      String           lsOperator = null ;
      int              liIndex ;
      String           lsDataObject = null ;
      String           lsFieldName = null ;
      int              liFieldType ;
      String           lsLHS = getAND_COND_LHS( fiIndex ) ;
      String           lsRHS = getAND_COND_RHS( fiIndex ) ;
      String           lsRHS_FLD = getAND_COND_RHS_FLD( fiIndex );
      byte             lbOper = getAND_COND_OPR( fiIndex ) ;
      int              liCondType = getAND_COND_TYP( fiIndex ) ;
      boolean          lboolLHSExists = true ;
      boolean          lboolRHSExists = true ;
      boolean          lboolListOper  = false ;
      char []          lcRhsChar = null;
      int              liRhsLen = 0;
      boolean          lboolRHSFLDExists  = true ;
      boolean          lboolIsNullOrIsNotNullOper  = false ;
      int              liRHSIndex=-1;
      String           lsRHSDataObject = null ;
      String           lsRHSFieldName = null ;
      int              liRHSFieldType=0;

      if ( ( lsLHS == null ) || ( lsLHS.trim().length() <= 0 ) )
      {
         lboolLHSExists = false ;
      } /* end if ( ( lsLHS == null ) || ( lsLHS.trim().length() <= 0 ) ) */

      lsOperator = CVL_WF_OPRSImpl.RULE_OPERATOR[lbOper];

      lboolIsNullOrIsNotNullOper=lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NULL)||
	                                   lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NOT_NULL);

	  if (!lboolIsNullOrIsNotNullOper && (( lsRHS == null ) || ( lsRHS.trim().length() <= 0 )) )
	  {
	     lboolRHSExists = false ;
	  } /* end if ( ( lsRHS == null ) || ( lsRHS.trim().length() <= 0 ) ) */

	   // Check if RHS Field exists.
	  if (!lboolIsNullOrIsNotNullOper && AMSStringUtil.strIsEmpty(lsRHS_FLD) )
	  {
	     lboolRHSFLDExists = false ;
	  } /* end if ( AMSStringUtil.strIsEmpty(lsRHS_FLD) ) */


      /*
       * If the value for the left hand side is null, and the value
       * for the right hand side is null, then return
       */
      if ( ! ( lboolLHSExists || lboolRHSExists || lboolRHSFLDExists) )
	  {
	     return ;
	  } /* end if ( ! ( lboolLHSExists || lboolRHSExists ) ) */

	  /*
	   * If the value for the left hand side is not null, but
	   * the value for the right hand side is null, then
	   * raise an exception
	   */
	  if ( lboolLHSExists && !lboolRHSExists && !lboolRHSFLDExists)
	  {
	     raiseException( "%c:Q0074%" ) ;
	     return ;
	  } /* end if ( lboolLHSExists && !lboolRHSExists ) */

	  if(!lboolIsNullOrIsNotNullOper)
	  {
	    /*
	     * If right hand side value and right hand side field both are present, then
	     * raise an exception
	     */
	     if ( lboolRHSFLDExists && lboolRHSExists )
	     {
	        raiseException( "%c:Q0142%" ) ;
	        return ;
	     } /* end if ( lboolRHSFLDExists && lboolRHSExists ) */

	     /*If right hand side field component neither equal to the left hand side
	      *component nor the header component.
	     */
	     if(lboolLHSExists && lboolRHSFLDExists && !isRHSFldCompEqualsLHSOrHDR(lsLHS,lsRHS_FLD))
	     {
	        raiseException( "%c:Q0145%" ) ;
	        return ;
	     }
	  }

	  /*
	   * If the value for the left hand side is null, but
	   * the value for the right hand side is not null, then
	   * raise an exception
	   */
	  if ( !lboolLHSExists && (lboolRHSExists || lboolRHSFLDExists))
	  {
	     raiseException( "%c:Q0075%" ) ;
	     return ;
	  } /* end if ( ( lsLHS == null ) && ... */

	  /*
	   * If both left hand and right hand sides are not null, but the
	   * operator is null, then raise an exception
	   */
	  if ( ( lboolLHSExists ) && ( lboolRHSExists || lboolRHSFLDExists) && ( lbOper <= 0 ) )
	  {
	     raiseException( "%c:Q0076%" ) ;
	     return ;
	  } /* end if ( ( lboolLHSExists ) ... */

      /*
       * If both left hand , right hand sides are not null and operator
       * is not null but the type of condition is null, then raise an exception
       */
      if ( ( lboolLHSExists ) && ( lboolRHSExists || lboolRHSFLDExists) && ( lbOper > 0 ) && liCondType <= 0 )
      {
          raiseException( "%c:Q0150,v: Condition Type for Condition " +
             fiIndex + ",v: Left Hand Side, Operator and Right Hand Side are %" ) ;
          return ;
      } /* end if ( ( lboolLHSExists ) ... */


      /*
       * If the left hand side, right hand side, or operator value is null
       * then return
       */
      if ( ( !lboolLHSExists ) || ( !lboolRHSExists && !lboolRHSFLDExists) || ( lbOper <= 0 ) )
      {
         return ;
      } /* end if ( ( !lboolLHSExists ) || ... */

      /*
       * If both left hand and right hand sides are not null, but the
       * operator is "IS_NULL" or "IS_NOT_NULL", then raise an exception
       */
      if ((lsLHS != null ) && (lsRHS != null || !AMSStringUtil.strIsEmpty(lsRHS_FLD)) &&
           (lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NULL) ||
              lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NOT_NULL)))
      {
	     raiseException( "%c:Q0113%" ) ;
	     return;
      } /* end if ( (lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NULL)).... */

      /*
       * Verify that the left hand side is a valid workflow approval
       * field. If not, then raise an exception
       */
      lsDocCd = getDOC_CD() ;
      if ( ( lsDocCd != null ) && ( lsDocCd.trim().length() > 0 ) )
      {
         Enumeration loEnum = null ;
         Enumeration loEnumForRHSFld = null ;
         String lsWhereClause = "DOC_CD=" +
               AMSSQLUtil.getANSIQuotedStr( lsDocCd,true ) ;

         loEnum = R_WF_APRV_FLDImpl.getObjects( lsWhereClause, this.getSession() ) ;
         loEnumForRHSFld = R_WF_APRV_FLDImpl.getObjects( lsWhereClause, getSession() ) ;
         if ( !checkFields( loEnum, lsLHS ) )
         {
            raiseException( "%c:Q0077%" ) ;
            return ;
          } /* end if ( !checkFields( loEnum, lsLHS ) */
         if(!lboolIsNullOrIsNotNullOper && lboolRHSFLDExists)
             {
               if ( !checkFields( loEnumForRHSFld, lsRHS_FLD ) )
               {
                  raiseException( "%c:Q0077%" ) ;
                  return ;
               } /* end if ( !checkFields( loEnum, lsRHS_FLD ) */
          }/* end if (lboolRHSFLDExists) */
      } /* end if ( ( lsDocCd != null ) && ( lsDocCd.trim.length() > 0 ) ) */

      liIndex = lsLHS.indexOf( '.' ) ;
      lsDataObject = lsLHS.substring( 0,liIndex ) ;
      lsFieldName = lsLHS.substring( liIndex + 1, lsLHS.length() ) ;
      liFieldType = getFieldType( lsDataObject, lsFieldName ) ;

      /*Field Type for the left hand side field and the right hand side field
       *should be comparable to each other.
       */
      if(!lboolIsNullOrIsNotNullOper && lboolRHSFLDExists)
      {
         liRHSIndex = lsRHS_FLD.indexOf( DOT_CHAR ) ;
         lsRHSDataObject = lsRHS_FLD.substring( 0,liRHSIndex ) ;
         lsRHSFieldName = lsRHS_FLD.substring( liRHSIndex + 1, lsRHS_FLD.length() ) ;
         liRHSFieldType = getFieldType( lsRHSDataObject, lsRHSFieldName ) ;
         if(!isFieldTypeComparable(liFieldType,liRHSFieldType))
         {
            raiseException( "%c:Q0148%" ) ;
            return;
         }
      }

      /*
       * If the data type of the field is boolean
       * and operator used is not EQUAL(=) or NOT_EQUAL(<>)
       * then raise an exception
       */
      if( liFieldType == DataConst.BIT
         && !AMSStringUtil.strEqual(lsOperator,CVL_WF_OPRSImpl.EQUAL)
         && !AMSStringUtil.strEqual(lsOperator,CVL_WF_OPRSImpl.NOT_EQUAL))
      {
         raiseException( "%c:Q0155%" ) ;
         return ;
      }

      /*
       * Parse the value from the right hand side into individual
       * values which are comma separated.
       */
      if(!(AMSStringUtil.strIsEmpty(lsRHS)))
      {
         lvVals = getListVals( lsRHS, getSession() ) ;
         lcRhsChar = lsRHS.toCharArray();
         liRhsLen = lsRHS.trim().length();
      }

      if ( lvVals != null )
      {
         liNumTokens = lvVals.size() ;
      } /* end if ( loTokens != null )  */

      if ( ( lsOperator.trim().equals( CVL_WF_OPRSImpl.IN_LIST ) ) ||
            ( lsOperator.trim().equals( CVL_WF_OPRSImpl.NOT_IN_LIST ) ) )
      {
         lboolListOper = true ;
      } /* end if if ( ( lsOperator.trim().equals( CVL_WF_OPRSImpl.IN_LIST )...*/

      //List operator should not be used with the right hand side field.
      if(lboolListOper && lboolRHSFLDExists)
      {
         raiseException( "%c:Q0149%" ) ;
         return;
      }

      if(liCondType == CVL_WF_COND_TYPImpl.DELTA_CHANGE ||
            liCondType == CVL_WF_COND_TYPImpl.PERCENT_CHANGE ||
            liCondType == CVL_WF_COND_TYPImpl.ABS_DELTA_CHANGE ||
            liCondType == CVL_WF_COND_TYPImpl.ABS_PERCENT_CHANGE)
      {
         /*
          * Raise error if Operator is IN LIST or NOT_IN_LIST or IS_NULL or IS_NOT_NULL
          * and Approval Condition Type is other than 'Actual Value'.
          */
         lsOperator = lsOperator.trim();
         if(AMSStringUtil.strEqual(lsOperator, CVL_WF_OPRSImpl.IN_LIST) ||
            AMSStringUtil.strEqual(lsOperator, CVL_WF_OPRSImpl.NOT_IN_LIST) ||
            AMSStringUtil.strEqual(lsOperator, CVL_WF_OPRSImpl.IS_NULL) ||
            AMSStringUtil.strEqual(lsOperator, CVL_WF_OPRSImpl.IS_NOT_NULL))
         {
            raiseException( "%c:Q0152,v:" + fiIndex + "%" ) ;
            return;
         }

         if(liFieldType != DataConst.BIGINT &&
            liFieldType != DataConst.DECIMAL &&
            liFieldType != DataConst.DOUBLE &&
            liFieldType != DataConst.FLOAT &&
            liFieldType != DataConst.INTEGER &&
            liFieldType != DataConst.NUMERIC &&
            liFieldType != DataConst.SMALLINT &&
            liFieldType != DataConst.REAL &&
            liFieldType != DataConst.TINYINT)
         {
            /*
             * Raise error when Left Hand Side is not numeric and
             * Condition Type is Delta Change, Percent Change,
             * Absolute Delta Change or Absolute Percent Change.
             */
            raiseException("%c:Q0151,v: " +
               CVL_WF_COND_TYPImpl.getConditionTypeDV(liCondType, getSession()) +
               " for Condition " + fiIndex + "%");
            return;
         }

         for (int liCnt = 0; liCnt < liRhsLen; liCnt++)
         {
            if (!((lcRhsChar[liCnt] == '-') ||
                  (lcRhsChar[liCnt] == '.') ||
                   Character.isDigit(lcRhsChar[liCnt])))
            {
               /*
                * Raise error when Right Hand Side is not numeric and
                * Condition Type is Delta Change, Percent Change,
                * Absolute Delta Change or Absolute Percent Change.
                */
               raiseException("%c:Q0153,v: " + fiIndex + "%");
               return;
            }
         }  //end for loop
      }

      /* If the operator is not "In List" or "Not In List" */
      if ( !lboolListOper )
      {
         /* if more than one parsed value */
         if ( liNumTokens > 1 )
         {
            StringBuffer lsbSingleVal = new StringBuffer(256) ;
            raiseException( "%c:Q0077%" ) ;
         }
         else
         {
            /*
             * If the data type of the field is boolean
             * and operator used is not EQUAL(=) or NOT_EQUAL(<>)
             * then raise an exception
             */
            if( liFieldType == DataConst.BIT
               && !AMSStringUtil.strEqual(lsOperator,CVL_WF_OPRSImpl.EQUAL)
               && !AMSStringUtil.strEqual(lsOperator,CVL_WF_OPRSImpl.NOT_EQUAL))
            {
               raiseException( "%c:Q0155%" ) ;
            }

         } /* end else of if ( liNumTokens > 1 ) */
      } /*  end if ( !lboolListOper ) */
      else
      {
         /*
          * If the data type of the field is not numeric or text,
          * then raise an exception
          */
         if ( !( ( isFieldNumeric( lsDataObject, lsFieldName ) ) ||
               ( liFieldType == DataConst.CHAR ) ||
               ( liFieldType == DataConst.VARCHAR ) ) )
         {
            raiseException( "%c:Q0078%" ) ;
            return ;
         } /* end if ( !( isFieldNumeric... */
      } /* end else */

      if ( ( lvVals != null ) && ( liNumTokens > 0 ) )
      {
         String lsParsedVal ;
         for( int liCtr = 0 ; liCtr < liNumTokens ; liCtr ++ )
         {
            lsParsedVal = (String) lvVals.elementAt(liCtr) ;
            checkValue( liFieldType, lsParsedVal, fiIndex ) ;
         } /* end while ( loTokens.hasMoreTokens() ) */
      } /* end if ( loTokens != null ) */
      else
      {
         if(!(lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NULL)|| lsOperator.trim().equals( CVL_WF_OPRSImpl.IS_NOT_NULL))
            && !AMSStringUtil.strIsEmpty(lsRHS) )
         {
         	checkValue( liFieldType, lsRHS, fiIndex ) ;
		 }
      } /* end else */
   } /* end checkCondition() */

   /**
    * This method checks in a condition whether Right Hand Side Field Component is equal
    * to the Left Hand Side component or header component.
    *
    * @param fsLHS This the Left Hand Side field of the condition.
    * @param fsRHSFLD This the Right Hand Side field of the condition.
    * @param fsDocCd This is the document code related to the condition.
    * @return true if Right Hand Side Field Component is equal to the Left Hand Side component or header component.
    */

   private boolean isRHSFldCompEqualsLHSOrHDR(String fsLHS,String fsRHSFLD)
   {
      int liIndex=0;
      String lsRHSFLDComp=null;
      String lsLHSComp=null;
      String lsHDRComp=null;

      liIndex = fsRHSFLD.indexOf( DOT_CHAR ) ;
      lsRHSFLDComp=fsRHSFLD.substring( 0,liIndex );
      liIndex = fsLHS.indexOf( DOT_CHAR ) ;
      lsLHSComp=fsLHS.substring( 0,liIndex );
      lsHDRComp=getHDRCompFromLHSComp(fsLHS);
      if(lsLHSComp.equalsIgnoreCase(lsRHSFLDComp))
      {
        return true;
      }
      else if(lsHDRComp.equalsIgnoreCase(lsRHSFLDComp))
      {
        return true;
      }
      return false;
   }/*end  isRHSFldCompEqualsLHSOrHDR()*/

   /**
   * This method returns the header component of the document from the some other component.eg
   * CR_DOC_ACTG is the component name passed as a parameter then the header component will be CR_DOC_HDR.
   *
   *
   * @param lsCompName this is the some other component of the doc.
   * @return the header component of the document.
   */
   public static String getHDRCompFromLHSComp(String fsCompName)
   {
      String lsHDRComp=null;
      int liFirstIndex=-1;

      liFirstIndex=fsCompName.indexOf(UNDER_SCORE_CHAR);
      lsHDRComp=fsCompName.substring(0,liFirstIndex)+HDR_SUFFIX;
      return lsHDRComp;
   }
   /**
     * This method returns true if both the field type are comparable to each other.
     * @param fiLHSFldType The left hand side field type.
     * @param fiRHSFldType The right hand side field type.
     * @return true if both the field type are comparable otherwise false.
     */
   private boolean isFieldTypeComparable(int fiLHSFldType,int fiRHSFldType)
   {
      boolean lboolResult=false;
      if(fiLHSFldType==fiRHSFldType)
      {
         lboolResult=true;
      }
      switch(fiLHSFldType)
      {
         case DataConst.BIT:
         case DataConst.DECIMAL:
         case DataConst.FLOAT:
         case DataConst.NUMERIC:
         case DataConst.REAL:
         case DataConst.BIGINT:
         case DataConst.DOUBLE:
         case DataConst.INTEGER:
         case DataConst.SMALLINT:
         case DataConst.TINYINT:
         {
            if(fiRHSFldType== DataConst.BIT || fiRHSFldType==DataConst.BIGINT ||
               fiRHSFldType== DataConst.DECIMAL || fiRHSFldType==DataConst.DOUBLE ||
               fiRHSFldType== DataConst.FLOAT || fiRHSFldType==DataConst.INTEGER ||
               fiRHSFldType== DataConst.NUMERIC || fiRHSFldType==DataConst.SMALLINT ||
               fiRHSFldType== DataConst.REAL || fiRHSFldType==DataConst.TINYINT )
            {
               lboolResult=true;
            }
         }
         break;
         case DataConst.DATE:
         case DataConst.TIMESTAMP:
         {
            if(fiRHSFldType== DataConst.DATE || fiRHSFldType==DataConst.TIMESTAMP)
            {
               lboolResult=true;
            }
         }
         break;
         case DataConst.CHAR:
         case DataConst.VARCHAR:
         case DataConst.LONGVARCHAR:
         {
            if(fiRHSFldType== DataConst.CHAR || fiRHSFldType==DataConst.LONGVARCHAR ||
               fiRHSFldType== DataConst.VARCHAR )
            {
               lboolResult=true;
            }
         }
         break;
         default:
            lboolResult=false;
      }
      return lboolResult;
   }

   /**
    * Method to check whether a field is numeric.
    * Note: Since Versata's isNumberType returns
    * true if the field is Yes/No, we return false
    * if the field is Yes/No
    *
    * @param fsComponent the component
    * @param fsField the field to evaluate
    * @return true if the field is numeric
    */
   private boolean isFieldNumeric( String fsComponent, String fsField )
   {
      try
      {
         VSMetaQuery loVsmq =
               AMSDataObject.constructMetaQueryFromMetaInfoTable( fsComponent, getSession() ) ;

         VSMetaColumn loColumn = loVsmq.getMetaColumn( fsField ) ;

         /*
          * Versata returns true if the data type is Yes/No
          * In that case we return false
          */
         if ( loColumn.getColumnType() == DataConst.BIT )
         {
            return false ;
         } /* end if ( loColumn.getColumnType() == DataConst.BIT ) */
         else
         {
            return loColumn.isNumberType() ;
         } /* end else */
      } /* end try */
      catch ( Exception loEx )
      {
         return false ;
      } /* end catch */
   } /* end isFieldNumeric */

   /**
    * This method separates the comma delimited values
    * and populates a vector
    *
    * @param fsVals the right hand side string
    * @return Vector containing the individual values
    */
   public static Vector getListVals( String fsVals, Session foSession )
   {
      Vector   loVals = new Vector() ;
      int      liValLen = 0 ;
      int      liQuoteIndex = -1 ;
      int      liCommaIndex = -1 ;
      int      liEndQuoteIndex = -1 ;
      String   lsValue ;
      String   lsTempVal ;

      lsValue = fsVals ;
      liValLen = lsValue.length() ;

      liQuoteIndex = lsValue.indexOf( '\"' ) ;
      liCommaIndex = lsValue.indexOf( ',' ) ;

      /*
       * If the string has quotes, then retrieve the individual
       * values within the quotes
       */
      if ( liQuoteIndex != -1 )
      {
         while ( liValLen > 0 )
         {
            liEndQuoteIndex = lsValue.indexOf( '\"', liQuoteIndex+1 ) ;

            /*
             * Raise an exception if there's no end quote since
             * all values must be properly enclosed in quotes
             */
            if ( liEndQuoteIndex == -1 )
            {
               AMSDataObject.raiseException( "%c:Q0079%", foSession ) ;
               return null ;
            } /* end if ( liEndQuoteIndex <= 0 ) */

            /* Retrieve the value and add to the vector */
            lsTempVal = lsValue.substring( liQuoteIndex, liEndQuoteIndex+1 ) ;
            loVals.addElement( lsTempVal ) ;

            /*
             * Check that there is a comma, between two values. If
             * there is no comma, then throw an exception
             */
            if ( liValLen > liEndQuoteIndex+2 )
            {
               if ( ( lsValue.charAt( liEndQuoteIndex + 1 ) != ',' ) )
               {
                  AMSDataObject.raiseException( "%c:Q0079%", foSession ) ;
                  break ;
               } /* end if ( ( fsVals.charAt( liEndQuoteIndex + 1 ) != ',' ) ) */

               /* Create the new string after the comma and reset the length */
               lsValue = lsValue.substring( liEndQuoteIndex+2, liValLen ) ;
               liValLen = lsValue.length() ;

               /* Reset the quote index */
               liQuoteIndex = lsValue.indexOf( '\"' ) ;
            } /* end if ( liValLen > liEndQuoteIndex+2 ) */
            else
            {
               liValLen = 0 ;
            } /* end else */
         } /* end while */
      } /* end if ( liQuoteIndex != -1 ) */
      else
      {
         /*
          * If the values are numeric then retrieve the comma delimited
          * values and populate the vector
          */
          while ( liValLen > 0 )
          {
             if ( liCommaIndex != -1 )
             {
                lsTempVal = lsValue.substring( 0, liCommaIndex ) ;
                loVals.addElement( lsTempVal ) ;

                if ( liValLen > ( liCommaIndex + 1 ) )
                {
                   lsValue = lsValue.substring( liCommaIndex+1, liValLen ) ;
                   liValLen = lsValue.length() ;

                   liCommaIndex = lsValue.indexOf( ',' ) ;
                } /* end if ( liValLen > ( liCommaIndex + 1 ) ) */
             } /* end if ( liCommaIndex != -1 ) */
             else
             {
                lsTempVal = lsValue.substring( 0, liValLen ) ;
                loVals.addElement( lsTempVal ) ;
                liValLen = 0 ;
             } /* end else */
          } /* end while */
      } /* end else */
      return loVals ;
   } /* end getListVals() */


   /**
    * When Left Hand Side, Operator and Right Hand Side for a Condition
    * are empty then infer the default value, i.e., set Operator to
    * CVL_WF_OPRSImpl.EQUAL. Left Hand Side and Right Hand Side will continue
    * to remain empty.
    */
   private void defaultForEmptyConditions()
   {
      /*
       * When any of the Conditions has all Left Hand Side, Operator and
       * Right Hand Side as empty then set Operator to default value of
       * CVL_WF_OPRSImpl.EQUAL(mimic default situation).
       * Note if this is not done then the condition will store null value
       * for Left Hand Side, Operator and Right Hand Side, and the whole condition
       * record will not be displayed on "Manage Approval Conditions" page
       * because the page selection query uses an equal join with R_WF_APRV_COND
       * and CVL_WF_OPRS tables.
       */
      for ( int liCtr = 1 ; liCtr <= 5 ; liCtr++ )
      {
         if( isNull("AND_COND_LHS_" + liCtr) && isNull("AND_COND_OPR_" + liCtr)
               && isNull("AND_COND_RHS_" + liCtr) )
         {
            getData("AND_COND_OPR_" + liCtr).setbyte((byte)3);
         }
      }//end for
   }//end of method

   /**
    * Depending on the value of the parameter passed, X, this method returns the value
    * in the corresponding AND_COND_RHS_FLD_X where X is a number 1-5.
    *
    * @param int fiIndex - the number of the AND_COND_RHS_FLD_x
    * @return String : the  value of the attribute.
    */

   public String getAND_COND_RHS_FLD(int fiIndex)
   {
     switch (fiIndex)
       {
          case 1 :
             return getAND_COND_RHS_FLD_1();
          case 2 :
             return getAND_COND_RHS_FLD_2();
          case 3 :
             return getAND_COND_RHS_FLD_3();
          case 4 :
             return getAND_COND_RHS_FLD_4();
          case 5 :
             return getAND_COND_RHS_FLD_5();
          default :
             return null ;
       }//end switch(fiIndex)

   } /* end getAND_COND_RHS_FLD() */

   /**
    * This returns true if header component exists in the left hand side of the condition.
    * mask and return it.
    *
    * @param fsCondition - This is the condition.
    * @return true if header component exists in the left hand side of the condtion otherwise false;
    */
   public static boolean isHeaderCompAtLHS(String fsCondition)
   {
      //Get only the left hand side component.
      int liFirstIndex= fsCondition.indexOf( DOT_CHAR);
      String lsLHSComp=fsCondition.substring( 0,liFirstIndex );
      if(lsLHSComp.indexOf(HDR_SUFFIX)>=0)
      {
         return true;
      }
      else
      {
         return false;
      }

   }

   /**
    * This method will return either the Right Hand Side Field or the Right Hand Side Value, depending
    * on which is populated, from the passed R_WF_APRV_COND data object and corresponding to the field
    * number passed.
    *
    * @param fiIndex as int - the x number of the or condition
    * @return String : the RHS field or value.
    * @author
    */
   public String getAndCondRHS(int fiIndex)
   {
      String lsRHS = getAND_COND_RHS( fiIndex );
      String lsRHS_FLD = getAND_COND_RHS_FLD( fiIndex ) ;
      if(!AMSStringUtil.strIsEmpty(lsRHS))
      {
         return lsRHS;
      }
      else if(!AMSStringUtil.strIsEmpty(lsRHS_FLD))
      {
         return lsRHS_FLD;
      }
      return null;
   }
}
