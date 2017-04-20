/*
 * @(#)Formatter.java
 *
 * Copyright 1999-2002 by American Management Systems, Inc.,
 * 4050 Legato Road, Fairfax, Virginia, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of American Management Systems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with American Management Systems, Inc.
 */
package advantage.GenMRT;

import versata.common.VSDate;
import versata.common.VSMetaColumn;
import versata.vfc.VSData;
import versata.vfc.VSRow;

import com.amsinc.gems.adv.common.CommonFormatter;
import com.amsinc.gems.adv.vfc.html.AMSPage;

import java.math.BigDecimal;

/**
 * This class of static utility methods called by GenerateMRT is responsible for
 * correctly formatting the MRT data to be written to the MRT file. It formats
 * both the column meta data and column values. An instance of this class is
 * never needed. It may be necessary to revisit this class if future releases of
 * Versata add new data types.
 * 
 * @see GenerateMRT
 */
public class PLSMRTFormatter extends CommonFormatter
{
   /**
    * Formats an MRT column value so that it can be written to the MRT data
    * file.
    * 
    * Modification Log : Mark Shipley - 01/03/2002 - MRT Cleanup
    * 
    * @param fiColNum
    *           The column index withing the row
    * @param foRow
    *           The row of data
    * @param foMetaCol
    *           The column meta data
    * @param foPage
    *           The page building the MRT file (used for message display). Can
    *           be null.
    * @return String The formatted data value
    */
   protected static String formatData(int fiColNum, VSRow foRow,
         VSMetaColumn foMetaCol, AMSPage foPage)
   {
      VSData loData = foRow.getData(fiColNum);
      String lsData = null;
      VSDate loDate;
      BigDecimal loNum;
      switch (foMetaCol.getColumnType())
      {
         case VSData.INTEGER:
         case VSData.SMALLINT:
            lsData = fixLength(String.valueOf(loData.getInt()),
                  INTEGER_MAX_LENGTH, true);
            break;
         case VSData.BIGINT:
            lsData = fixLength(String.valueOf(loData.getLong()),
                  BIGINT_MAX_LENGTH, true);
            break;
         case VSData.BIT:
            lsData = loData.getBoolean() ? BOOLEAN_TRUE : BOOLEAN_FALSE;
            break;
         case VSData.NUMERIC:
            loNum = getBDAftChkForNull(loData.getBigDecimal());
            lsData = formatDecimal(loNum, CURRENCY_MAX_LENGTH,
                  foMetaCol.getScale());
            break;
         case VSData.DECIMAL:
            loNum = getBDAftChkForNull(loData.getBigDecimal());
            lsData = formatDecimal(loNum, DECIMAL_MAX_LENGTH,
                  foMetaCol.getScale());
            break;
         case VSData.REAL:
            loNum = getBDAftChkForNull(loData.getBigDecimal());
            lsData = formatDecimal(loNum, REAL_MAX_LENGTH,
                  foMetaCol.getScale());
            break;
         case VSData.DOUBLE:
            loNum = getBDAftChkForNull(loData.getBigDecimal());
            lsData = formatDecimal(loNum, DOUBLE_MAX_LENGTH,
                  foMetaCol.getScale());
            break;
         case VSData.DATE:
            loDate = loData.getVSDate();
            if (loDate == null)
            {
               lsData = fixLength("", DATE_MAX_LENGTH, false);
            } /* end if ( loDate == null ) */
            else
            {
               lsData = formatDate(loData.getDate(), VSDate.DATE_FORMAT);
            } /* end else */
            break;
         case VSData.TIMESTAMP:
            loDate = loData.getVSDate();
            if (loDate == null)
            {
               lsData = fixLength(lsData, DATETIME_MAX_LENGTH, false);
            } /* end if ( loDate == null ) */
            else
            {
               lsData = formatDate(loData.getDate(), VSDate.DATETIME_FORMAT);
            } /* end else */
            break;
         case VSData.TIME:
            loDate = loData.getVSDate();
            if (loDate == null)
            {
               lsData = fixLength(lsData, TIME_MAX_LENGTH, false);
            } /* end if ( loDate == null ) */
            else
            {
               lsData = loDate.getTime();
            } /* end else */
            break;
         case VSData.CHAR:
         case VSData.VARCHAR:
            lsData = fixLength(loData.getString(), foMetaCol.getSize(), false);
            break;
         case VSData.LONGVARCHAR:
            lsData = fixLength(loData.getString(), LONGVARCHAR_MAX_LENGTH,
                  false);
            break;
         default:
            if (foPage != null)
            {
               foPage.raiseException("Data type, " + foMetaCol.getColumnType()
                     + ", not supported in MRT.  Occurred on "
                     + foMetaCol.getTable().getName() + "."
                     + foMetaCol.getName(), AMSPage.SEVERITY_LEVEL_ERROR);
            } /* end if ( foPage != null ) */
            break;
      } /* end switch ( foMetaCol.getColumnType() ) */

      return lsData;
   } /* end formatData() */
   
   
   /**
    * Template method from <code>CommonFormatter<code> that is implemented in thsi class.
    * @param foObject The tartget for the raised error.  AMSPage type here
    * @param fsErrorMsg The error messages
    */
   public static void raiseError(Object foObject, String fsErrorMsg)
   {
      AMSPage loPage = null;
      
      if (foObject !=null)
      {
         loPage = (AMSPage)foObject;
         
      }
      
      loPage.raiseException(fsErrorMsg, AMSPage.SEVERITY_LEVEL_ERROR);
   }

   /**
    * Returns 0 For null BigDecimal Amounts, otherwise returns the BigDecimal Passed.
    * @param foAmount BigDecimal amount to be checked for null.
    */
   private static BigDecimal getBDAftChkForNull(BigDecimal foAmount)
   {
      if(foAmount == null)
      {
         return new BigDecimal("0");
      }

      return foAmount;
   }
} /* end class Formatter */