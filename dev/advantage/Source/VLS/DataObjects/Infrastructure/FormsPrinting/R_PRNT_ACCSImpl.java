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
**  R_PRNT_ACCS*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_PRNT_ACCSImpl extends  R_PRNT_ACCSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_PRNT_ACCSImpl (){
	super();
}

public R_PRNT_ACCSImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//{{COMP_EVENT_beforeUpdate
  public void beforeUpdate(DataObject obj, Response response)
  {
       commonBeforeLogic();
  }
//END_COMP_EVENT_beforeUpdate}}

//{{COMP_EVENT_beforeInsert
  public void beforeInsert(DataObject obj, Response response)
 {
    commonBeforeLogic();
 }
//END_COMP_EVENT_beforeInsert}}

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static R_PRNT_ACCSImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_PRNT_ACCSImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}


   public void myInitializations()
   {
      //Allow '*ALL' to be used
      registerForeignKeyException( "R_SC_USER_DIR_INFO", "USER_ID", "*ALL");
   }


   /**
    * Method returns if the user, application resource, print job and
    * print resource combination is valid and the user authorized for
    * that combination.
    *
    * @param foSession the update session object instance
    * @param fsUserId user id
    * @param fsApplRsrcId application resource
    * @param fsPrntJobCd print job code
    * @param fsPrntRsrcId print resource id
    * @return if the user id is authorized to for the application resource
    * and the print resource combination
    */
   public static final boolean isUserAuthorizedToPrint(Session foSession,
                                                       String  fsUserId,
                                                       String fsApplRsrcId,
                                                       String fsPrntJobCd,
                                                       String fsPrntRsrcId)
   {

      return isAuthorizedToPrint(foSession,fsUserId,fsApplRsrcId, fsPrntJobCd,
                                 fsPrntRsrcId,true);

   } /* isUserAuthorizedToPrint(Session, String, String, String, String) */


   /**
    * Alternate method returns if the application resource, print job and
    * print resource combination is valid for a user of "*ALL"
    *
    * @param foSession the update session object instance
    * @param fsApplRsrcId application resource
    * @param fsPrntJobCd print job code
    * @param fsPrntRsrcId print resource id
    * @return if the user id is authorized to for the application resource
    * and the print resource combination
    */

   public static final boolean isAuthorizedToPrintGroupOnly(Session foSession,
                                                                   String fsApplRsrcId,
                                                                   String fsPrntJobCd,
                                                                   String fsPrntRsrcId)
   {

      return isAuthorizedToPrint(foSession,null,fsApplRsrcId, fsPrntJobCd,
                                 fsPrntRsrcId,false);

   }/* isGroupOnlyAuthorizedToPrint(Session, String, String, String) */


   /**
    * Method returns if the user, application resource, print job and
    * print resource combination is valid and the user authorized for
    * that combination.
    *
    * @param foSession the update session object instance
    * @param fsUserId user id
    * @param fsApplRsrcId application resource
    * @param fsPrntJobCd print job code
    * @param fsPrntRsrcId print resource id
    * @param fboolChkUserID Determines whether to check for
    * @return if the user id is authorized to for the application resource
    * and the print resource combination
    */

   private static final boolean isAuthorizedToPrint( Session foSession,
                                                     String  fsUserId,
                                                     String fsApplRsrcId,
                                                     String fsPrntJobCd,
                                                     String fsPrntRsrcId,
                                                     boolean fboolChkUserID)
   {
      SearchRequest loSrchReq ;

      if ( ( foSession == null ) ||
           ( AMSStringUtil.strIsEmpty( fsApplRsrcId ) ) ||
           ( AMSStringUtil.strIsEmpty( fsPrntJobCd ) ) ||
           ( AMSStringUtil.strIsEmpty( fsPrntRsrcId ) ) ||
           ( fboolChkUserID && AMSStringUtil.strIsEmpty( fsUserId ) ) )
      {
         return false ;
      } /* end if ( ( foSession == null ) || . . . */

      loSrchReq = new SearchRequest() ;

      if ( fboolChkUserID )
      {
         loSrchReq.add( "USER_ID='*ALL' OR USER_ID="
               + AMSSQLUtil.getANSIQuotedStr( fsUserId, true ) ) ;
      } /* end if ( fboolChkUserID ) */
      else
      {
         loSrchReq.addParameter( "R_PRNT_ACCS", "USER_ID", "*ALL" ) ;
      } /* end else */

      loSrchReq.addParameter( "R_PRNT_ACCS", "APPL_RSRC_ID", fsApplRsrcId ) ;
      loSrchReq.addParameter( "R_PRNT_ACCS", "PRNT_JOB_CD", fsPrntJobCd ) ;
      loSrchReq.addParameter( "R_PRNT_ACCS", "PRNT_RSRC_ID", fsPrntRsrcId ) ;

      try
      {
         Enumeration lePrntAccs = R_PRNT_ACCSImpl.getObjects( loSrchReq, foSession ) ;

         if ( lePrntAccs != null )
         {
            Response        loResponse ;
            R_PRNT_ACCSImpl loPrntAccs ;

            while ( lePrntAccs.hasMoreElements() )
            {
               loPrntAccs = (R_PRNT_ACCSImpl)lePrntAccs.nextElement() ;

               /*
                * Handle row security for the fetched data row.  All non null
                * data rows are evaluated- A single non null - not rejected
                * through row security - is enough to confirm validity of
                * print paramaters
                */
               loResponse = new Response() ;
               beforeResultsetFill( loPrntAccs.getRow(), loResponse );

               if ( !loResponse.rejected() )
               {
                  /* found a valid row */
                  return true;
               } /* end if ( !loResponse.rejected() ) */
            } /* end while ( lePrntAccs.hasMoreElements() ) */
         } /* end if ( lePrntAccs != null ) */
      } /* end try */
      catch( Exception foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

      } /* end catch( Exception foExp ) */

      /* print parameters not valid */
      return false ;
   } /* end isAuthorizedToPrint() */


   /* Method called in beforeInsert/Update to execute common code.
    *
    * @return void
    */

   public void commonBeforeLogic()
   {
     if(!(this.getDEPT_CD().equals("ALL") || this.getUNIT_CD().equals("ALL") ))
     {

      Session foSession = getSession();

      R_UNITImpl lounit = null;
      SearchRequest loSearchReq = new SearchRequest() ;

      loSearchReq.addParameter("R_UNIT", "DEPT_CD" , getDEPT_CD());
      loSearchReq.addParameter("R_UNIT", "UNIT_CD" , getUNIT_CD());


      lounit = (R_UNITImpl)R_UNITImpl.getObjectByKey(loSearchReq, foSession);

      if(lounit == null)
      {
        raiseException("The Department and Unit combination does not exist on the Unit Table.");
      }

     }
   }







} /* of class R_PRNT_ACCSImpl */

