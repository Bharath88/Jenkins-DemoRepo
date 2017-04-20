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
**  IN_PRNT_JOB_SPOOL
*/
import java.io.*;


//{{COMPONENT_RULES_CLASS_DECL
public class IN_PRNT_JOB_SPOOLImpl extends  IN_PRNT_JOB_SPOOLBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public IN_PRNT_JOB_SPOOLImpl (){
	super();
}

public IN_PRNT_JOB_SPOOLImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static IN_PRNT_JOB_SPOOLImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new IN_PRNT_JOB_SPOOLImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

   public static void spoolPrintJob( Session foSession,
                                     long    flPrntJobId,
                                     String  fsApplRsrcID,
                                     String  fsPrintRsrc,
                                     String  fsPrintJobName,
                                     byte[]  fbPrintDataBytes )
   {
      IN_PRNT_JOB_SPOOLImpl loPrintSpool ;
      loPrintSpool = IN_PRNT_JOB_SPOOLImpl.getNewObject( foSession,
                                                         true ) ;
      loPrintSpool.setPRNT_JOB_ID( flPrntJobId ) ;
      loPrintSpool.setAPPL_RSRC_ID( fsApplRsrcID ) ;
      loPrintSpool.setPRNT_RSRC_ID( fsPrintRsrc ) ;
      loPrintSpool.setPRNT_JOB_CD( fsPrintJobName ) ;
      loPrintSpool.getData( "PRNT_JOBDAT" ).setbytes( fbPrintDataBytes ) ;

      loPrintSpool.save() ;
   }


   /**
   * Will use a native connection to upload data from the specified
   * file.
   *
   * @param foSession the update Session
   * @param flPrntJobId the print job Id
   * @param fsApplRsrcID application resource Id
   * @param fsPrintRsrc print resource Id
   * @param fsPrintJobName print job name
   * @param fsFileWithBinaryDataToUpLd file to be uploaded
   */
   public static void spoolPrintJob( Session foSession,
      long    flPrntJobId,
      String  fsApplRsrcID,
      String  fsPrintRsrc,
      String  fsPrintJobName,
      String  fsFileWithBinaryDataToUpLd )
      throws Exception

   {
      IN_PRNT_JOB_SPOOLImpl loPrintSpool ;
      File loUpLdFile = new File(fsFileWithBinaryDataToUpLd);
      AMSSQLConnection loConnection = null;
      java.sql.PreparedStatement loPsmt = null;
      InputStream loBinDatStream = null;



      if (!loUpLdFile.exists() )
      {
         throw new Exception("Invalid print file specified to upload");
      }

      try
      {

         /*
         * This populates the default field values
         */

         loPrintSpool = IN_PRNT_JOB_SPOOLImpl.getNewObject( foSession,
            true ) ;


         /*
         * Use this data object to populate the PreparedStatement fields.
         *
         * The PreparedStatement is not cached
         */
         String []  lsDBPropsArray =
            AMSUtil.getJDBCConnectionProps("IN_PRNT_JOB_SPOOL");

         String  lsSchema       = lsDBPropsArray[ AMSUtil.DB_SCHEMA ] ;
         String  lsDBType       = lsDBPropsArray[ AMSUtil.DB_TYPE ] ;

         StringBuffer lsbSQL = new StringBuffer(64*16);

         lsbSQL.append("INSERT INTO ").append(lsSchema).append(".") ;
         lsbSQL.append("IN_PRNT_JOB_SPOOL (PRNT_JOB_ID,");
         lsbSQL.append("USER_ID,DT,APPL_RSRC_ID,PRNT_RSRC_ID,");
         lsbSQL.append("PRNT_JOB_CD,PRNT_JOBDT,AMS_ROW_VERS_NO");

         lsbSQL.append(" VALUES (?,?,?,?,?,?,?,?)");

         loConnection = new AMSSQLConnection((VSJdbc)foSession.getConnection(
            foSession.getDataServerForObject("IN_PRNT_JOB_SPOOL")),
            lsDBType, lsSchema);

         loPsmt = loConnection.prepareStatement(lsbSQL.toString() );

         loPsmt.setLong(1,flPrntJobId);
         loPsmt.setString(2,loPrintSpool.getUSER_ID() );
         loPsmt.setDate(3,new java.sql.Date(System.currentTimeMillis()) );
         loPsmt.setString(4,fsApplRsrcID );
         loPsmt.setString(5,fsPrintRsrc);
         loPsmt.setString(6,fsPrintJobName);
         loPsmt.setLong(8,1); //new row - AMSROW_VERS_NO


         /*
         * set the file stream reference
         */
         loBinDatStream = new FileInputStream(loUpLdFile);

         loPsmt.setBinaryStream( 7,
            loBinDatStream,
            (int)(loUpLdFile.length()));
         loPsmt.executeUpdate();
         loConnection.commit();

      }//try
      catch(Exception loExp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExp);


      }
      finally
      {
         //Close Statement
         if( loPsmt!= null )
         {
            loPsmt.close();
         }
         /*
          * Release the underlying connection back to the pool
          */
         if (loConnection !=null )
         {
            loConnection.close();
         }
      }//finally

   }  //spoolPrintJob




}
