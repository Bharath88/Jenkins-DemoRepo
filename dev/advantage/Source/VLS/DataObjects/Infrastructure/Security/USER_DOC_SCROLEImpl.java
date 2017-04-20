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
**  USER_DOC_SCROLE*/

//{{COMPONENT_RULES_CLASS_DECL
public class USER_DOC_SCROLEImpl extends  USER_DOC_SCROLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   //BGN ADVHR00037535
   private boolean mboolUserAdded = false;
   //END ADVHR00037535
//{{COMP_CLASS_CTOR
public USER_DOC_SCROLEImpl (){
	super();
}

public USER_DOC_SCROLEImpl(Session session, boolean makeDefaults)
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
	public static USER_DOC_SCROLEImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new USER_DOC_SCROLEImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * This component does not have an associated catalog,
    * so disable mediation.
    */
   public void myInitializations()
   {
      mboolMediateDocComponent = false ;
   } /* end myInitializations */

   /**
    * This method returns the context for the document component.
    *
    * @return The context
    */
   public String getContext()
   {
      if ( msContext == null )
      {
          msContext = createContext( "SCROLE" ) ;
      } /* end if ( msContext == null ) */
      return msContext ;
   } /* end getContext() */

   /**
    * This method checks if any other role lines also use
    * the same role.  If so, then it raises an exception.
    */
   protected void verifyUniqueSecurityRole()
   {
      SearchRequest loSrchReq ;
      Session       loSession = getSession() ;
      int           liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_CD, getDOC_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_DEPT_CD, getDOC_DEPT_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_ID, getDOC_ID() ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_VERS_NO, String.valueOf( getDOC_VERS_NO() ) ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", "SEC_ROLE_ID", getSEC_ROLE_ID() ) ;
      liObjCnt = USER_DOC_SCROLEImpl.getObjectCount( loSrchReq, loSession ) ;

      if ( liObjCnt > 1 )
      {
         raiseException( "%c:Q0031,p:SEC_ROLE_ID%", null, "SEC_ROLE_ID" ) ;
      } /* end if ( liObjCnt > 1 ) */
   } /* end verifyUniqueSecurityRole() */

   /**
    * This method checks if any other role lines also have
    * the same precedence.  If so, then it raises an exception.
    */
   protected void verifyUniquePrecedence()
   {
      SearchRequest loSrchReq ;
      Session       loSession = getSession() ;
      int           liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_CD, getDOC_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_DEPT_CD, getDOC_DEPT_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_ID, getDOC_ID() ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", ATTR_DOC_VERS_NO, String.valueOf( getDOC_VERS_NO() ) ) ;
      loSrchReq.addParameter( "USER_DOC_SCROLE", "PREC_NO", String.valueOf( getPREC_NO() ) ) ;
      liObjCnt = USER_DOC_SCROLEImpl.getObjectCount( loSrchReq, loSession ) ;

      if ( liObjCnt > 1 )
      {
         raiseException( "%c:Q0071,p:PREC_NO%", null, "PREC_NO" ) ;
      } /* end if ( liObjCnt > 1 ) */
   } /* end verifyUniquePrecedence() */

   /**
    * Based on the COMP_ACTN_CD, this method performs the actual
    * update logic for the document component.  Namely, it updates
    * the R_SC_USER_ROLE_LNK table.
    */
   protected void maintainSecurityRole()
   {
      USER_DOC_HDRImpl loDocHdr = (USER_DOC_HDRImpl)getDocumentHeader() ;
      String           lsUserID = loDocHdr.getUSER_ID() ;

      if ( lsUserID != null )
      {
         int liAction = getCOMP_ACTN_CD() ;

         //BGN ADVHR00037535
         if ( liAction == CVL_DOC_COMP_ACTNImpl.ADD && !mboolUserAdded )
         {
            processAdd( lsUserID ) ;
         } /* end if ( liAction == CVL_DOC_COMP_ACTNImpl.ADD ) */
         else if ( liAction == CVL_DOC_COMP_ACTNImpl.UPDATE )
         {
            if ( loDocHdr.getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.ADD  && !mboolUserAdded)
            {
               /*
                * If the header action is add, then treat the
                * update line action as an Add also.
                */
               processAdd( lsUserID ) ;
            } /* end if ( loDocHdr.getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.ADD ) */
            //END ADVHR00037535
            else
            {
               processUpdate( lsUserID ) ;
            } /* end else */
         } /* end else if ( liAction == CVL_DOC_COMP_ACTNImpl.UPDATE ) */
         else if ( liAction == CVL_DOC_COMP_ACTNImpl.DELETE )
         {
            /*
             * If the header action is add, then ignore the
             * delete line action.  Otherwise, process the delete.
             */
            if ( loDocHdr.getCOMP_ACTN_CD() != CVL_DOC_COMP_ACTNImpl.ADD )
            {
               processDelete( lsUserID ) ;
            } /* end if ( loDocHdr.getCOMP_ACTN_CD() != CVL_DOC_COMP_ACTNImpl.ADD ) */
         } /* end else if ( liAction == CVL_DOC_COMP_ACTNImpl.DELETE ) */
      } /* end if ( lsUserID != null ) */
   } /* end maintainSecurityRole() */

   /**
    * This method performs the logic of adding the new user
    * role link to the system.  It adds the R_SC_USER_ROLE_LNK
    * record for the user/role combination and populates it
    * with the values from the document line.
    *
    * @param fsUserID The user id
    */
   private void processAdd( String fsUserID )
   {
      //BGN ADVHR00037535
      mboolUserAdded = true;
      //END ADVHR00037535
      R_SC_USER_ROLE_LNKImpl loUserRoleLnk ;
      SearchRequest          loSrchReq ;
      Session                loSession     = getSession() ;
      int                    liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", fsUserID ) ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "SEC_ROLE_ID", getSEC_ROLE_ID() ) ;
      liObjCnt = R_SC_USER_ROLE_LNKImpl.getObjectCount( loSrchReq, loSession ) ;
      if ( liObjCnt > 0 )
      {
         raiseException( "%c:Q0015,v:" + fsUserID + ",p:SEC_ROLE_ID%", null, "SEC_ROLE_ID" ) ;
      } /* end if ( liObjCnt > 0 ) */
      else
      {
         loUserRoleLnk = R_SC_USER_ROLE_LNKImpl.getNewObject( loSession, true ) ;
         loUserRoleLnk.copyCorresponding( this, null, true ) ;
         loUserRoleLnk.setUSER_ID( fsUserID ) ;
         loUserRoleLnk.save() ;
      } /* end else */
   } /* end processAdd() */

   /**
    * This method performs the logic of updating the user role
    * link in the system.  It updates the R_SC_USER_ROLE_LNK
    * record for the user/role combination and populates it
    * with the values from the document line.
    *
    * @param fsUserID The user id
    */
   private void processUpdate( String fsUserID )
   {
      R_SC_USER_ROLE_LNKImpl loUserRoleLnk ;
      SearchRequest          loSrchReq ;
      Session                loSession     = getSession() ;
      VSDate                 loRoleLnkDt ;
      VSDate                 loDocDt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", fsUserID ) ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "SEC_ROLE_ID", getSEC_ROLE_ID() ) ;
      loUserRoleLnk = (R_SC_USER_ROLE_LNKImpl)R_SC_USER_ROLE_LNKImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserRoleLnk == null )
      {
         raiseException( "%c:Q0024,v:" + fsUserID + ",p:SEC_ROLE_ID%", null, "SEC_ROLE_ID" ) ;
      } /* end if ( loUserRoleLnk == null ) */
      else
      {
         loDocDt = getDOC_CREA_DT() ;
         loRoleLnkDt = loUserRoleLnk.getLAST_UPDATE_DT() ;
         if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) )
         {
            if ( loRoleLnkDt.after( loDocDt ) )
            {
               raiseException( "%c:Q0025,v:" + fsUserID + ",p:SEC_ROLE_ID%", null, "SEC_ROLE_ID" ) ;
            } /* end if ( loRoleLnkDt.after( loDocDt ) ) */
         } /* end if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) ) */
         loUserRoleLnk.copyCorresponding( this, null, true ) ;
         loUserRoleLnk.save() ;
      } /* end else */
   } /* end processUpdate() */


   /**
    * This method performs the logic of deleting the user role
    * link from the system.  It deletes the R_SC_USER_ROLE_LNK
    * record for the user/role combination.
    *
    * @param fsUserID The user id
    */
   private void processDelete( String fsUserID )
   {
      R_SC_USER_ROLE_LNKImpl loUserRoleLnk ;
      SearchRequest          loSrchReq ;
      Session                loSession     = getSession() ;
      VSDate                 loRoleLnkDt ;
      VSDate                 loDocDt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "USER_ID", fsUserID ) ;
      loSrchReq.addParameter( "R_SC_USER_ROLE_LNK", "SEC_ROLE_ID", getSEC_ROLE_ID() ) ;
      loUserRoleLnk = (R_SC_USER_ROLE_LNKImpl)R_SC_USER_ROLE_LNKImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserRoleLnk == null )
      {
         raiseException( "%c:Q0024,v:" + fsUserID + ",p:SEC_ROLE_ID%", null, "SEC_ROLE_ID" ) ;
      } /* end if ( loUserRoleLnk == null ) */
      else
      {
         /**
          * Whenever any revocation action takes place, the Revoke Reason value is stored
          * in the session which is later used in Historical Tracking (IN_HIST_TRACKImpl.java)
          */
         if(!AMSStringUtil.strIsEmpty(getREASON()))
         {
            loSession.setProperty(SESSION_REVOKE_REASON, getREASON());
         }

         loDocDt = getDOC_CREA_DT() ;
         loRoleLnkDt = loUserRoleLnk.getLAST_UPDATE_DT() ;
         if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) )
         {
            if ( loRoleLnkDt.after( loDocDt ) )
            {
               raiseException( "%c:Q0025,v:" + fsUserID + ",p:SEC_ROLE_ID%", null, "SEC_ROLE_ID" ) ;
            } /* end if ( loRoleLnkDt.after( loDocDt ) ) */
         } /* end if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) ) */
         loUserRoleLnk.setDelete() ;
         loUserRoleLnk.save() ;
      } /* end else */
   } /* end processDelete() */

  /**
    * Checks if User from User Document Security Role table (USER_DOC_SCROLE)is not getting
    * assigned any Security Roles where the combination of Security Role IDs are defined as restricted on
    * Restricted Security Role Pairs table. If yes, logs error.
    */

   protected void validateSecurityRoles()
   {
      // Get a collection of Security Role IDs for current User Document Security Role record
      ArrayList<String> loListRoles = getSecRoleForUserDocScrole();

      //Checks for all possible combinations of given Security Role with the Arraylist of Security Roles
      //on Restricted Role Pairs table and returns true if any match found for a combination.
      if( R_SC_SEC_ROLEPAIRSImpl.isRoleRestricted( getSession(), getSEC_ROLE_ID(), loListRoles ) )
      {
         raiseException("%c:Q0194,v:" + getSEC_ROLE_ID() + "%");
      }
   }

   /**
    * Returns a collection of Security Role IDs for a User from User Document Security Role table (USER_DOC_SCROLE)
    * @return Arraylist of Security Role IDs
    */

   public ArrayList getSecRoleForUserDocScrole( )
   {
      SearchRequest loSrchReq = new SearchRequest();
      USER_DOC_SCROLEImpl loUserDocRole = null;

      loSrchReq.addParameter("USER_DOC_SCROLE", "DOC_CD", getDOC_CD() );
      loSrchReq.addParameter("USER_DOC_SCROLE", "DOC_DEPT_CD" , getDOC_DEPT_CD() );
      loSrchReq.addParameter("USER_DOC_SCROLE", "DOC_ID" , getDOC_ID() );
      loSrchReq.addParameter("USER_DOC_SCROLE", "DOC_VERS_NO" , String.valueOf( getDOC_VERS_NO() ) );
      loSrchReq.addParameter("USER_DOC_SCROLE", "DOC_SCROLE_NO" , String.valueOf( getDOC_SCROLE_NO() ) );
      ArrayList<String> loListRoles = new ArrayList<String>();

      Enumeration lenumRoleList = getObjects( loSrchReq,getSession() );

      // Loop through the Enumeration of User records from User Document Security Role table and add
      // the Security Role Ids of that User to the ArrayList.
      while( lenumRoleList.hasMoreElements() )
      {
         loUserDocRole = ( USER_DOC_SCROLEImpl )lenumRoleList.nextElement();
         loListRoles.add( loUserDocRole.getSEC_ROLE_ID() );
      } /* whole Enumeration of User records is traversed */
      return loListRoles;
   }

}

