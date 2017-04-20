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
**  R_SC_APP_ROLE_LNK
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_APP_ROLE_LNKImpl extends  R_SC_APP_ROLE_LNKBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public R_SC_APP_ROLE_LNKImpl (){
		super();
	}
	
	public R_SC_APP_ROLE_LNKImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeInsert
public void beforeInsert(DataObject obj, Response response)
{
	//Write Event Code below this line
   checkESS_MSSuserTypes();
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	//Write Event Code below this line
   checkESS_MSSuserTypes();
}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static R_SC_APP_ROLE_LNKImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_SC_APP_ROLE_LNKImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	/**
	 * This method is used to check if application is MSS then MSS user type should populate else raise an error.
	 * @return false - if application name is MSS and MSS user type is null else returns true
	 */
	public boolean isAppNameIsMSS()
	{
	   int liAppName = getAPP_NM();
	   int liMssUserTyp = getMSS_USER_TYP();
	   boolean mboolReturnValue = true;

	   if(liMssUserTyp <= 0 && liAppName == CVL_APP_NMImpl.C_APPNM_MSS)
		 {
		    mboolReturnValue = false;
		 }
		else
       {
          mboolReturnValue = true;
       }

		return mboolReturnValue;
	}

	/**
	 * This method is used to check if application is ESS then ESS user type should populate else raise an error.
    * @return false - if application name is ESS and ESS user type is null else returns true
	 */
	public boolean isAppNameIsESS()
   {
      int liAppName = getAPP_NM();
      int liEssUserTyp = getESS_USER_TYP();
      boolean mboolReturnValue = true;

      if(liEssUserTyp <= 0 && liAppName == CVL_APP_NMImpl.C_APPNM_ESS)
       {
          mboolReturnValue = false;
	}
      else
       {
          mboolReturnValue = true;
       }

      return mboolReturnValue;
   }


	/**
	 * This method is used to concat the following fields and store that to CONCAT_KEYS.
	 * And we are using this as primary key attribute for R_SC_APP_ROLE_LNK
	 * fields are [APP_NM, SEC_ROLE_ID, MSS_USER_TYP, ESS_USER_TYP]. If application name is ESS then
	 * it will append ESS_USER_TYP else append MSS_USER_TYP
	 * @param foDataObject
	 * @return returns concatinated values
	 */
	public String concatFields(AMSDataObject foDataObject)
	{
	   String C_EMPTY_SPACE = " ";
	   VSMetaColumn loMetaColumn;
      String lsKeyColumn;
      Data loKeyColumnData ;
      String lsAppName = "";
      VSMetaQuery  loMetaQuery = foDataObject.getMetaQueryNotStatic();
      StringBuffer lsbConcatenatedKey = new StringBuffer(100);


      lsKeyColumn = "APP_NM";
      loMetaColumn = loMetaQuery.getMetaColumn(lsKeyColumn);
      loKeyColumnData = foDataObject.getData(lsKeyColumn) ;
      lsAppName = String.valueOf(loKeyColumnData.getint());
      if (loMetaColumn.isNumberType())
      {
         //If the column is numeric we want to pad with zeros at the front
         // and remove decimal point any zeros beyond the specified scale.

         String lsKeyColumnData = String.valueOf(loKeyColumnData.getint());

         if ((lsKeyColumnData == null) || (lsKeyColumnData.trim().length() == 0)){
            lsbConcatenatedKey.append("");
         } else {
            lsbConcatenatedKey.append(
                     formatNumberForPerformanceKey(
                              String.valueOf(loKeyColumnData.getint()),
                              String.valueOf(loKeyColumnData.getint()).length(),
                              loMetaColumn.getScale() ));
         }
      }
      lsKeyColumn = "SEC_ROLE_ID";
      loMetaColumn = loMetaQuery.getMetaColumn(lsKeyColumn);
      if(foDataObject.getData(lsKeyColumn)!= null)
      {


      loKeyColumnData = foDataObject.getData(lsKeyColumn) ;

    //If the column is non-numeric we want to pad with spaces at the end
      lsbConcatenatedKey.append(padField(C_EMPTY_SPACE,false,loKeyColumnData));
      }
      if(Integer.valueOf(lsAppName) == Integer.valueOf(CVL_APP_NMImpl.C_APPNM_ESS))
      {
         lsKeyColumn = "ESS_USER_TYP";
         loMetaColumn = loMetaQuery.getMetaColumn(lsKeyColumn);
         loKeyColumnData = foDataObject.getData(lsKeyColumn) ;

      }
      else  if(Integer.valueOf(lsAppName) == Integer.valueOf(CVL_APP_NMImpl.C_APPNM_MSS))
      {
         lsKeyColumn = "MSS_USER_TYP";
         loMetaColumn = loMetaQuery.getMetaColumn(lsKeyColumn);
         loKeyColumnData = foDataObject.getData(lsKeyColumn) ;
      }


      if (loMetaColumn.isNumberType())
      {
         //If the column is numeric we want to pad with zeros at the front
         // and remove decimal point any zeros beyond the specified scale.

         String lsKeyColumnData = String.valueOf(loKeyColumnData.getint());

         if ((lsKeyColumnData == null) || (lsKeyColumnData.trim().length() == 0)){
            lsbConcatenatedKey.append("");
         } else {
            lsbConcatenatedKey.append(
                     formatNumberForPerformanceKey(
                              String.valueOf(loKeyColumnData.getint()),
                              String.valueOf(loKeyColumnData.getint()).length(),
                              loMetaColumn.getScale() ));
         }
      }

      if ( lsbConcatenatedKey.toString().length() == 0 )
      {
         return " " ;
      }
      else
      {
         return lsbConcatenatedKey.toString();
      }

	}

	/**
	 * If both ESS user type and MSS user type are populated then raise an error
	 * saying that any one is allowed depending on the application type
	 */
	public void checkESS_MSSuserTypes()
	{
	   if(getESS_USER_TYP() > 0 && getMSS_USER_TYP() > 0)
	   {
	      raiseException("%c:Q0175%");
	   }
	}


   /**
    * Used to format numeric columns for inclusion in the Performance Key
    *
    * @param fsColumnValue
    *            The string value of the column to be formatted.
    * @param fiPerformanceKeyLength
    *            The number of characters the column is allowed within the
    *            Performance Key.
    * @param fiColumnScale
    *            Indicates the number of digits making up the decimal portion (i.e.
    *            to the right of the decimal point)
    * @return String   The formatted number
    *
    */
   private String formatNumberForPerformanceKey (
            String fsColumnValue, int fiPerformanceKeyLength, int fiColumnScale)
   {

   	final String C_ZERO_STRING          = "0";
	if (moAMSLog.isDebugEnabled())
      {
         moAMSLog.debug("formatNumberForPerformanceKey()");
         moAMSLog.debug("\t fsColumnValue            = " + fsColumnValue);
         moAMSLog.debug("\t fiPerformanceKeyLength   = " + fiPerformanceKeyLength);
         moAMSLog.debug("\t fiColumnScale            = " + fiColumnScale);
      }



      // error
      if (fiPerformanceKeyLength <= 0)
      {
         return "";
      }
      final int liTotalLength = fiPerformanceKeyLength;  //total length of String to be returned
      final int liDecimalLength;                         //length of the decimal portion
      if (fiColumnScale > 0)
      {
         liDecimalLength = fiColumnScale;
      }
      else  // scale can be -1 if not set, e.g integer data type
      {
         liDecimalLength = 0;
      }
      final int liIntegerLength = liTotalLength - liDecimalLength;   //length of the integer portion
      final int liDecimalLocation = fsColumnValue.indexOf(".");      //the location of the decimal point
      String lsIntegerPortion = "";      //value of Integer Portion
      String lsDecimalPortion = "";      //value of Decimal Portion
      StringBuffer lsbPerformanceKeyNumber = new StringBuffer(liTotalLength); //String to be returned

      // if decimal length (scale) exceeds the total length, return all 0's.
      if (fiPerformanceKeyLength > 0 && liIntegerLength < 0)
      {
         for (int i = 0; i < fiPerformanceKeyLength; i++)
         {
            lsbPerformanceKeyNumber.insert(0,C_ZERO_STRING);
         }
         return lsbPerformanceKeyNumber.toString();
      }

      //Scenario 1: The scale = 0 and the string contains a decimal point
      //(integer value with decimal point)
      //In this case, get rid of the decimal portion, pad Integer portion with zeros to match lsTotalLength
      //THERE WILL BE LOSS OF PRECISION!
      if (liDecimalLength == 0 && liDecimalLocation >= 0)
      {
         lsIntegerPortion = fsColumnValue.substring(0, liDecimalLocation);
         // cut from left if extracted integer is longer than designated length
         if (lsIntegerPortion.length() > liTotalLength)
         {
            lsIntegerPortion = lsIntegerPortion.substring(lsIntegerPortion.length() - liTotalLength);
         }

         // 0-pad on the left if integer portion up to the designated length
         int liPad = liTotalLength - lsIntegerPortion.length();
         for (int liLen = 0; liLen < liPad; liLen++)
         {
            lsbPerformanceKeyNumber.append(C_ZERO_STRING);
         }
         lsbPerformanceKeyNumber.append(lsIntegerPortion);
      }

      //Scenario 2: The scale = 0 and the string contains no decimal point
      //(integer value without decimal point)
      else if (liDecimalLength == 0 && liDecimalLocation == -1)
      {
         lsIntegerPortion = fsColumnValue;
         // cut from left if extracted integer is longer than designated length
         if (lsIntegerPortion.length() > liTotalLength)
         {
            lsIntegerPortion = lsIntegerPortion.substring(lsIntegerPortion.length() - liTotalLength);
         }

         // 0-pad on the left if integer portion up to the designated length
         int liPad = liTotalLength - lsIntegerPortion.length();
         for (int liLen = 0; liLen < liPad; liLen++)
         {
            lsbPerformanceKeyNumber.append(C_ZERO_STRING);
         }
         lsbPerformanceKeyNumber.append(lsIntegerPortion);
      }

      //Scenario 3: The scale > 0 and the string contains a decimal point
      //(decimal number value with decimal point)
      else if (liDecimalLength > 0 && liDecimalLocation >= 0)
      {
         lsIntegerPortion = fsColumnValue.substring(0, liDecimalLocation);
         lsDecimalPortion = fsColumnValue.substring(liDecimalLocation + 1);

         // process integer number portion
         // cut from left if extracted integer is longer than designated length
         if (lsIntegerPortion.length() > liIntegerLength)
         {
            lsIntegerPortion = lsIntegerPortion.substring(lsIntegerPortion.length() - liIntegerLength);
         }

         // 0-pad if length of integer portion is less than allotted space
         int liPad = liIntegerLength - lsIntegerPortion.length();
         for (int liLen = 0; liLen < liPad; liLen++)
         {
            lsbPerformanceKeyNumber.append(C_ZERO_STRING);
         }
         lsbPerformanceKeyNumber.append(lsIntegerPortion);

         // process decimal number portion
         // cut from left if extracted decimal number is longer than designated scale
         if (lsDecimalPortion.length() > liDecimalLength)
         {
            lsDecimalPortion = lsDecimalPortion.substring(0, liDecimalLength);
         }

         //while length of decimal portion is less than allotted space
         lsbPerformanceKeyNumber.append(lsDecimalPortion);
         while (lsbPerformanceKeyNumber.length() < liTotalLength)
         {
            lsbPerformanceKeyNumber.append(C_ZERO_STRING);
         }
      }

      //Scanario 4: The scale > 0 and the string contains no decimal point
      //(decimal number value without decimal point)
      //Example if parameters are 4234,5,2 the return value = 23400
      //Priority is given to scale rather than value
      else if (liDecimalLength > 0 && liDecimalLocation == -1)
      {
         lsIntegerPortion = fsColumnValue;
         // cut from left if extracted decimal number is longer than designated scale
         if (lsIntegerPortion.length() > liIntegerLength)
         {
            lsIntegerPortion = lsIntegerPortion.substring(lsIntegerPortion.length() - liIntegerLength);
         }
         //while length of integer portion is less than allotted space
         int liPad = liIntegerLength - lsIntegerPortion.length();
         for (int liLen = 0; liLen < liPad; liLen++)
         {
            lsbPerformanceKeyNumber.append(C_ZERO_STRING);
         }
         lsbPerformanceKeyNumber.append(lsIntegerPortion);

         // append 0 to fill up the space allocated for decimal portion
         while (lsbPerformanceKeyNumber.length() < liTotalLength)
         {
            lsbPerformanceKeyNumber.append(C_ZERO_STRING);
         }
      }

      return lsbPerformanceKeyNumber.toString();

   }


   /**
    * Used to pad any field with any string value.  The method should be passed a data column
    * containing the value that will be padded.  Logic uses meta data to figure out how long the
    * data column is and pads it to match the length.  A flag specifies if the padding should
    * occur at the beginning or end of the field.
    *<br><br>
    *
    * If a null value is passed as the data column then an empty String is returned.
    * If a null value is passed as the pad value then the value of the data column is returned.
    *<br><br>
    *
    * Example : A 7 byte data column is passed containing the String "Test".  The following
    *           are examples of the types of padding that may be applied.
    *<br><br>
    *
    * <table>
    *   <tr><td>Pad Value</td> <td>Pad At Start</td> <td>Result</td></tr>
    *   <tr><td>=========</td> <td>============</td> <td>=======</td></tr>
    *   <tr><td>"0"      </td> <td>true        </td> <td>"000Test"</td></tr>
    *   <tr><td>"0"      </td> <td>false       </td> <td>"Test000"</td></tr>
    *   <tr><td>" "      </td> <td>true        </td> <td>"&nbsp&nbsp&nbspTest"</td></tr>
    *   <tr><td>" "      </td> <td>false       </td> <td>"Test&nbsp&nbsp&nbsp"</td></tr>
    * </table>
    *
    * @param  String   The value that will be used to pad the field.
    * @param  boolean  Specifies if the field should be padded at the beiginning (True) or end.
    * @param  Data     Data column that will hold the padded unique number
    * @return String   The padded field
    */

   public String padField(String fsPadValue, boolean fboolPadAtStart, Data foColumnToPad)
   {
      //Return an empty string if the column that needs padding is null
      if (foColumnToPad.getObject() == null)
      {
         return "";
      }


      String lsColumnToPad = foColumnToPad.getObject().toString();

      //Return the column as a string if the pad value is null
      if (fsPadValue == null)
      {
         return lsColumnToPad;
      }

      //Check the length of the column.  If the length matches then there is nothing to pad
      VSMetaColumn loColumn = foColumnToPad.getMetaColumn();
      int liUnpaddedLength = lsColumnToPad.length();
      int liColumnLength = getFieldLength(loColumn);


      if (liColumnLength == liUnpaddedLength)
      {
         return foColumnToPad.getString();
      }


      //Figure out how many bytes need to be padded and loop thru performing the padding
      int liColsToPad = liColumnLength - liUnpaddedLength;

      StringBuffer lsbPaddedField = new StringBuffer(liColumnLength);
      lsbPaddedField.insert(0,lsColumnToPad);

      for (int liIndex=1; liIndex <= liColsToPad; liIndex++)
      {
         //Perform the padding at the beginning or end depending on the parameter
         if (fboolPadAtStart)
         {
            lsbPaddedField.insert(0,fsPadValue);
         }
         else
         {
            lsbPaddedField.insert(liUnpaddedLength,fsPadValue);
         }
      }

      return lsbPaddedField.toString();
   }

   /**
    * Used to return the length of a column.  Decimal fields will check for an extended property called "ConcatKeyFieldLength"
    * and will use the length associated with that property.  If that property doesn't exist it will use 15 bytes.
    *
    * @param  VSMetaColumn   The Data column that we are calculating the length.
    * @return int            The length of the field
    */
   private int getFieldLength (VSMetaColumn foColumn)
   {

      String lsConcatKeyFieldLength = null ;
   		final String C_CONCAT_FIELD_LENGTH_PROP = "ConcatKeyFieldLength";
   		final int    C_CONCAT_FIELD_LENGTH_BIGINT      =   18 ;
   		final int    C_CONCAT_FIELD_LENGTH_BIT         =    3 ;
   		final int    C_CONCAT_FIELD_LENGTH_DECIMAL     =   15 ;
   		final int    C_CONCAT_FIELD_LENGTH_DOUBLE      =  126 ;
   		final int    C_CONCAT_FIELD_LENGTH_INTEGER     =   10 ;
   		final int    C_CONCAT_FIELD_LENGTH_LONGVARCHAR = 1500 ;
   		final int    C_CONCAT_FIELD_LENGTH_NUMERIC     =   16 ;
   		final int    C_CONCAT_FIELD_LENGTH_REAL        =   63 ;
   		final int    C_CONCAT_FIELD_LENGTH_SMALLINT    =    3 ;

      switch(foColumn.getColumnType())
      {
         case DataConst.CHAR:
            return foColumn.getSize();
         case DataConst.DECIMAL:
            lsConcatKeyFieldLength = foColumn.getProperty(C_CONCAT_FIELD_LENGTH_PROP);
            if (lsConcatKeyFieldLength != null)
            {
               return Integer.parseInt(lsConcatKeyFieldLength);
            }
            else
            {
               return C_CONCAT_FIELD_LENGTH_DECIMAL ;
            }
         case DataConst.DOUBLE:
            return C_CONCAT_FIELD_LENGTH_DOUBLE ;
         case DataConst.REAL:
            return C_CONCAT_FIELD_LENGTH_REAL ;
         case DataConst.INTEGER:
            lsConcatKeyFieldLength = foColumn.getProperty(C_CONCAT_FIELD_LENGTH_PROP);
            if (lsConcatKeyFieldLength != null)
            {
               return Integer.parseInt(lsConcatKeyFieldLength);
            }
            else
            {
               return C_CONCAT_FIELD_LENGTH_INTEGER ;
            }
         case DataConst.NUMERIC:
            return C_CONCAT_FIELD_LENGTH_NUMERIC ;
         case DataConst.VARCHAR:
            return foColumn.getSize();
         case DataConst.SMALLINT:
            lsConcatKeyFieldLength = foColumn.getProperty(C_CONCAT_FIELD_LENGTH_PROP);
            if (lsConcatKeyFieldLength != null)
            {
               return Integer.parseInt(lsConcatKeyFieldLength);
            }
            else
            {
               // Versata datatype of Byte
               return C_CONCAT_FIELD_LENGTH_SMALLINT ;
            }
         case DataConst.BIT:
            lsConcatKeyFieldLength = foColumn.getProperty(C_CONCAT_FIELD_LENGTH_PROP);
            if (lsConcatKeyFieldLength != null)
            {
               return Integer.parseInt(lsConcatKeyFieldLength);
            }
            else
            {
               return C_CONCAT_FIELD_LENGTH_BIT ;
            }
         case DataConst.LONGVARCHAR:
            return C_CONCAT_FIELD_LENGTH_LONGVARCHAR ;
         case DataConst.BIGINT:
            lsConcatKeyFieldLength = foColumn.getProperty(C_CONCAT_FIELD_LENGTH_PROP);
            if (lsConcatKeyFieldLength != null)
            {
               return Integer.parseInt(lsConcatKeyFieldLength);
            }
            else
            {
               // Versata datatype of LongInt deploys as 19
               // but the COBOL code can handle only a max length of 18
               return C_CONCAT_FIELD_LENGTH_BIGINT ;
            }
         default:
            return -1;
      }
   }




}

