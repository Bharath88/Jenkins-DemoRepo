/*
 * @(#)GenerateMRT.java
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

import java.rmi.RemoteException;
import java.util.Arrays;

import versata.common.VSMetaColumn;
import versata.common.VSMetaQuery;
import versata.common.VSORBSession;
import versata.common.VSSession;
import versata.vfc.VSQuery;
import versata.vfc.VSResultSet;
import versata.vfc.VSRow;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.DataTable;
import com.amsinc.gems.adv.common.Header;
import com.amsinc.gems.adv.common.OneDataRow;
import com.amsinc.gems.adv.common.TabRow;
import com.amsinc.gems.adv.vfc.html.AMSPage;

import org.apache.commons.logging.Log;

/**
 * This class is the main driver for generating the MRT file.  Called from the
 * Generate MRT page, it is responsible (through helper classes) for reading
 * the MRT data from the database tables, formatting the MRT data, and writing
 * that data to the MRT file.
 *
 * @see DataTable
 * @see TabRow
 * @see Header
 * @see OneDataRow
 * @see pR_FS_MRT_BUILD
 */
public class GenerateMRT
{
   /** The user's session to be used for data access */
   private VSSession moSession ;
   /** The page used to generate the MRT file (used for message display).  Can be null */
   private AMSPage moPage ;
   
   /**
    * The code logging object
    */
   private static Log moAMSLog = AMSLogger.getLog(GenerateMRT.class,
         AMSLogConstants.FUNC_AREA_MRT);

   /**
    * Builds a GenerateMRT object so that a new MRT data file
    * can be built using the generate method.
    *
    * Modification Log : Mark Shipley - 01/03/2002
    *                                 - MRT Cleanup
    *
    * @param foSession The user's session to be used when querying MRT data
    * @param foPage The page building the MRT file (used for message display).  Can be null.
    */
   protected GenerateMRT( VSSession foSession, AMSPage foPage )
   {
      moSession   = foSession ;
      moPage      = foPage ;
   } /* end GenerateMRT() */

   /**
    * Generates a new MRT data file
    *
    * Modification Log : Mark Shipley - 01/03/2002
    *                                 - MRT Cleanup
    *
    * @param fsFileLoc The directory location where the MRT file will be generated
    * @param fsFileName The name of MRT File to be generated
    * @param fboolForDebug Flag indicating if the MRT file will be generated for debugging purposes
    * @return boolean Indicates success (true) or failure (false) of the MRT data file generation
    */
   protected boolean generate( String fsFileLoc, String fsFileName, boolean fboolForDebug )
   {
      boolean      lboolSuccess = true ;
      VSORBSession loORBSess    = moSession.getORBSession() ;

      try
      {
         DataTable  loDataTbl ;
         TabRow[]   loMRTTbls ;

         loORBSess.setProperty( "maintenanceString", "G" ) ;

         loDataTbl = new PLSDataTable( moPage ) ;

         loMRTTbls = TabRow.getTabRows() ;
         lboolSuccess = getTableData( loMRTTbls, loDataTbl ) ;

         if ( lboolSuccess )
         {
            /*
             * Based on the debug flag, generate the file in a format either
             * easily readable for debugging purposes or in a production
             * format.  MRT files generated in debug format can only be
             * used for debugging.  They cannot be used at runtime.
             */
            lboolSuccess = loDataTbl.generate( fsFileLoc, fsFileName, fboolForDebug ) ;
         } /* end if ( lboolSuccess ) */
      } /* end try */
      catch( Exception foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         if ( moPage != null )
         {
            moPage.raiseException( "A processing error occurred. " + foExp.getMessage(),
                  AMSPage.SEVERITY_LEVEL_ERROR ) ;
         } /* end if ( moPage != null ) */
         lboolSuccess = false ;
      } /* end catch( Exception foExp ) */
      finally
      {
         try
         {
            loORBSess.setProperty( "maintenanceString", "" ) ;
         } /* end try */
         catch( RemoteException foExp )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         } /* end catch( RemoteException foExp ) */
      } /* end finally */

      if ( moPage != null )
      {
         if ( lboolSuccess )
         {
            moPage.raiseException( "MRT file generated successfully.", AMSPage.SEVERITY_LEVEL_INFO ) ;
         } /* end if ( lboolSuccess ) */
         else
         {
            moPage.raiseException( "MRT file generation failed.", AMSPage.SEVERITY_LEVEL_ERROR ) ;
         } /* end else */
      } /* end if ( moPage != null ) */
      return lboolSuccess ;
   } /* end generate() */

   /**
    * Retrieves from the database and stores into memory the
    * MRT data for each MRT table.
    *
    * Modification Log : Mark Shipley - 01/03/2002
    *                                 - MRT Cleanup
    *
    * @param foMRTTbls The MRT tables as read from R_FS_MRT_BUILD
    * @param foDataTable An empty DataTable to be populated with data for each MRT table
    * @return boolean Success (true) or failure (false) of the data retrieve
    */
   private boolean getTableData( TabRow[] foMRTTbls, DataTable foDataTable )
   {
      boolean      lboolSuccess = true ;
      TabRow       loMRTTbl ;
      VSQuery      loQuery ;
      VSMetaQuery  loMetaQry ;
      VSResultSet  loRsltSet ;
      int          liNumRows ;
      OneDataRow[] loTblRows ;
      int          liNumCols ;
      VSMetaColumn loMetaCol ;
      String       lsMetaInfo ;
      int          liKeyLen ;
      int          liResultLen ;
      String       lsKey       = "" ;
      String       lsResult    = "" ;
      VSRow        loVSRow ;
      Header       loHeader ;
      String       lsColVal ;
      String       lsColMeta ;

      for( int liTblCtr = 0 ; liTblCtr < foMRTTbls.length ; liTblCtr++ )
      {
         loMRTTbl = (TabRow)foMRTTbls[liTblCtr] ;
         lsMetaInfo = "" ;

         loQuery   = new VSQuery( moSession, loMRTTbl.getDataObjName(), "", "" ) ;
         loRsltSet = loQuery.execute() ;
         loMetaQry = loRsltSet.getMetaQuery() ;
         loRsltSet.last() ;

         liNumRows = loRsltSet.getRowCount() ;
         liNumCols = loMetaQry.getColumnCount() ;
         loTblRows = new OneDataRow[liNumRows] ;

         /* Check if the number of keys specified is valid. */
         if ( loMRTTbl.getNoKeyCols() >= liNumCols )
         {
            if ( moPage != null )
            {
               moPage.raiseException( "The number of key columns specified for "
                     + loMRTTbl.getDataObjName() + ", " + loMRTTbl.getNoKeyCols()
                     + ", is greater than its total number of columns, "
                     + liNumCols, AMSPage.SEVERITY_LEVEL_ERROR ) ;
            } /* end if ( moPage != null ) */
            lboolSuccess = false ;

            /* Continue processing to accumulate errors */
            continue ;
         } /* end if ( loMRTTbl.getNoKeyCols >= liNumCols ) */
         else if ( loMRTTbl.getNoKeyCols() <= 0 )
         {
            if ( moPage != null )
            {
               moPage.raiseException( "The number of key columns specified for "
                     + loMRTTbl.getDataObjName() + ", " + loMRTTbl.getNoKeyCols()
                     + ", must be greater than 0.", AMSPage.SEVERITY_LEVEL_ERROR ) ;
            } /* end if ( moPage != null ) */
            lboolSuccess = false ;

            /* Continue processing to accumulate errors */
            continue ;
         } /* end else if ( loMRTTbl.getNoKeyCols <= 0 ) */

         for ( int liColCtr = 1 ; liColCtr <= liNumCols ; liColCtr++ )
         {
            loMetaCol = loMetaQry.getMetaColumn( liColCtr ) ;

            lsColMeta = PLSMRTFormatter.getColumnMetaData( loMetaCol, moPage ) ;
            if ( lsColMeta == null )
            {
               if ( moPage != null )
               {
                  moPage.raiseException( "Column meta data could not be generated for "
                        + loMetaQry.getName() + "." + loMetaCol.getName(),
                        AMSPage.SEVERITY_LEVEL_ERROR ) ;
               } /* end if ( moPage != null ) */
               lboolSuccess = false ;

               /* Continue processing to accumulate errors */
               continue ;
            } /* end if ( lsColMeta == null ) */
            else
            {
               lsMetaInfo += lsColMeta ;
            } /* end else */
         } /* end for ( int liColCtr = 1 ; liColCtr <= liNumCols ; liColCtr++ ) */

         for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ )
         {
            loVSRow = loRsltSet.getRowAt( liRowCtr ) ;
            lsKey   = "" ;
            lsResult  = "" ;

            for ( int liColCtr = 1 ; liColCtr <= liNumCols ; liColCtr++ )
            {
               loMetaCol = loMetaQry.getMetaColumn( liColCtr ) ;

               lsColVal = PLSMRTFormatter.formatData( liColCtr, loVSRow, loMetaCol, moPage ) ;
               if ( lsColVal == null )
               {
                  if ( moPage != null )
                  {
                     moPage.raiseException( "Column value could not be generated for "
                           + loMetaQry.getName() + "." + loMetaCol.getName(),
                           AMSPage.SEVERITY_LEVEL_ERROR ) ;
                  } /* end if ( moPage != null ) */
                  lboolSuccess = false ;

                  /* Continue processing to accumulate errors */
                  continue ;
               } /* end if ( lsColVal == null ) */
               else
               {
                  if ( liColCtr <= loMRTTbl.getNoKeyCols())
                  {
                     lsKey += lsColVal ;
                  } /* end if ( liColCtr <= loMRTTbl.getNoKeyCols ) */
                  else
                  {
                     lsResult += lsColVal ;
                  } /* end else */
               } /* end else */
            } /* end for ( int liColCtr = 1 ; liColCtr <= liNumCols ; liColCtr++ ) */

            loTblRows[liRowCtr - 1] = new OneDataRow( lsKey, lsResult ) ;
         } /* end for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ ) */

         loRsltSet.close() ;
         Arrays.sort( loTblRows ) ;
         liKeyLen  = lsKey.length() ;
         liResultLen = lsResult.length() ;
         loHeader = new Header( loMRTTbl.getTableID(), loMRTTbl.getDataObjName(),
                                liKeyLen, liKeyLen + liResultLen, loTblRows.length,
                                loMRTTbl.getNoKeyCols(), liNumCols, lsMetaInfo ) ;

         foDataTable.addTable( loHeader, loTblRows ) ;
      } /* end for( int liTblCtr = 0 ; liTblCtr < foMRTTbls.length ; liTblCtr++ ) */

      return lboolSuccess ;
   } /* end getTableData() */

} /* end class GenerateMRT */