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
**  USER_DOC_WFROLE*/

//{{COMPONENT_RULES_CLASS_DECL
public class USER_DOC_WFROLEImpl extends  USER_DOC_WFROLEBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   //BGN ADVHR00037535
   private boolean mboolUserAdded = false;
   //END ADVHR00037535
//{{COMP_CLASS_CTOR
public USER_DOC_WFROLEImpl (){
	super();
}

public USER_DOC_WFROLEImpl(Session session, boolean makeDefaults)
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
	public static USER_DOC_WFROLEImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new USER_DOC_WFROLEImpl(session, makeDefaults);
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
          msContext = createContext( "WFROLE" ) ;
      } /* end if ( msContext == null ) */
      return msContext ;
   } /* end getContext() */

   /**
    * This method checks if any other role lines also use
    * the same role.  If so, then it raises an exception.
    */
   protected void verifyUniqueWorkflowRole()
   {
      SearchRequest loSrchReq ;
      Session       loSession = getSession() ;
      int           liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "USER_DOC_WFROLE", ATTR_DOC_CD, getDOC_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_WFROLE", ATTR_DOC_DEPT_CD, getDOC_DEPT_CD() ) ;
      loSrchReq.addParameter( "USER_DOC_WFROLE", ATTR_DOC_ID, getDOC_ID() ) ;
      loSrchReq.addParameter( "USER_DOC_WFROLE", ATTR_DOC_VERS_NO, String.valueOf( getDOC_VERS_NO() ) ) ;
      loSrchReq.addParameter( "USER_DOC_WFROLE", "WF_ROLE_ID", getWF_ROLE_ID() ) ;
      liObjCnt = USER_DOC_WFROLEImpl.getObjectCount( loSrchReq, loSession ) ;

      if ( liObjCnt > 1 )
      {
         raiseException( "%c:Q0032,p:WF_ROLE_ID%", null, "WF_ROLE_ID" ) ;
      } /* end if ( liObjCnt > 1 ) */
   } /* end verifyUniqueWorkflowRole() */

   /**
    * Based on the COMP_ACTN_CD, this method performs the actual
    * update logic for the document component.  Namely, it updates
    * the R_WF_USER_ROLE table.
    */
   protected void maintainWorkflowRole()
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
            if ( loDocHdr.getCOMP_ACTN_CD() == CVL_DOC_COMP_ACTNImpl.ADD && !mboolUserAdded )
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
   } /* end maintainWorkflowRole() */

   /**
    * This method performs the logic of adding the new user
    * role link to the system.  It adds the R_WF_USER_ROLE
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
      R_WF_USER_ROLEImpl loUserRoleLnk ;
      SearchRequest      loSrchReq ;
      Session            loSession     = getSession() ;
      int                liObjCnt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "USID", fsUserID ) ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "ROLEID", getWF_ROLE_ID() ) ;
      liObjCnt = R_WF_USER_ROLEImpl.getObjectCount( loSrchReq, loSession ) ;
      if ( liObjCnt > 0 )
      {
         raiseException( "%c:Q0016,v:" + fsUserID + ",p:WF_ROLE_ID%", null, "WF_ROLE_ID" ) ;
      } /* end if ( liObjCnt > 0 ) */
      else
      {
         loUserRoleLnk = R_WF_USER_ROLEImpl.getNewObject( loSession, true ) ;
         loUserRoleLnk.setUSID( fsUserID ) ;
         loUserRoleLnk.setROLEID( getWF_ROLE_ID() ) ;
         loUserRoleLnk.setROLE_MGR_FL( getWF_ROLE_MGR_FL() ) ;
         loUserRoleLnk.setDISP_SEQ_NO( getDISP_SEQ_NO() );
         loUserRoleLnk.save() ;
      } /* end else */
   } /* end processAdd() */

   /**
    * This method performs the logic of updating the user role
    * link in the system.  It updates the R_WF_USER_ROLE
    * record for the user/role combination and populates it
    * with the values from the document line.
    *
    * @param fsUserID The user id
    */
   private void processUpdate( String fsUserID )
   {
      R_WF_USER_ROLEImpl loUserRoleLnk ;
      SearchRequest      loSrchReq ;
      Session            loSession     = getSession() ;
      VSDate             loRoleLnkDt ;
      VSDate             loDocDt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "USID", fsUserID ) ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "ROLEID", getWF_ROLE_ID() ) ;
      loUserRoleLnk = (R_WF_USER_ROLEImpl)R_WF_USER_ROLEImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserRoleLnk == null )
      {
         raiseException( "%c:Q0026,v:" + fsUserID + ",p:WF_ROLE_ID%", null, "WF_ROLE_ID" ) ;
      } /* end if ( loUserRoleLnk == null ) */
      else
      {
         loDocDt = getDOC_CREA_DT() ;
         loRoleLnkDt = loUserRoleLnk.getLAST_UPDATE_DT() ;
         if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) )
         {
            if ( loRoleLnkDt.after( loDocDt ) )
            {
               raiseException( "%c:Q0027,v:" + fsUserID + ",p:WF_ROLE_ID%", null, "WF_ROLE_ID" ) ;
            } /* end if ( loRoleLnkDt.after( loDocDt ) ) */
         } /* end if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) ) */
         loUserRoleLnk.setROLE_MGR_FL( getWF_ROLE_MGR_FL() ) ;
         loUserRoleLnk.setDISP_SEQ_NO( getDISP_SEQ_NO() );
         loUserRoleLnk.save() ;
      } /* end else */
   } /* end processUpdate() */


   /**
    * This method performs the logic of deleting the user role
    * link from the system.  It deletes the R_WF_USER_ROLE
    * record for the user/role combination.
    *
    * @param fsUserID The user id
    */
   private void processDelete( String fsUserID )
   {
      R_WF_USER_ROLEImpl loUserRoleLnk ;
      SearchRequest      loSrchReq ;
      Session            loSession     = getSession() ;
      VSDate             loRoleLnkDt ;
      VSDate             loDocDt ;

      loSrchReq = new SearchRequest() ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "USID", fsUserID ) ;
      loSrchReq.addParameter( "R_WF_USER_ROLE", "ROLEID", getWF_ROLE_ID() ) ;
      loUserRoleLnk = (R_WF_USER_ROLEImpl)R_WF_USER_ROLEImpl.getObjectByKey( loSrchReq, loSession ) ;
      if ( loUserRoleLnk == null )
      {
         raiseException( "%c:Q0026,v:" + fsUserID + ",p:WF_ROLE_ID%", null, "WF_ROLE_ID" ) ;
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
               raiseException( "%c:Q0027,v:" + fsUserID + ",p:WF_ROLE_ID%", null, "WF_ROLE_ID" ) ;
            } /* end if ( loRoleLnkDt.after( loDocDt ) ) */
         } /* end if ( ( loRoleLnkDt != null ) && ( loDocDt != null ) ) */
         loUserRoleLnk.setDelete() ;
         loUserRoleLnk.save() ;
      } /* end else */
   } /* end processDelete() */

}

