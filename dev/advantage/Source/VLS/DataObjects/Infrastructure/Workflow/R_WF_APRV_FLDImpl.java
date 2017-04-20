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

import java.lang.reflect.* ;

/*
**  R_WF_APRV_FLD
*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_APRV_FLDImpl extends  R_WF_APRV_FLDBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
      /**
       *  The AMSDataObject that validates the document component -
       *  Also needed for document field.
       */

//{{COMP_CLASS_CTOR
public R_WF_APRV_FLDImpl (){
	super();
}

public R_WF_APRV_FLDImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static R_WF_APRV_FLDImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_WF_APRV_FLDImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }


   protected void validateCombination()
   {
      String lsDocCd = getDOC_CD() ;

      /*
       * If the document code is null, the required check will catch it,
       * so do nothing here.  No validation is possible.
       */
      if ( lsDocCd != null )
      {
         Session            loSession = getSession() ;
         SearchRequest      loSrchReq ;
         R_GEN_DOC_CTRLImpl loDocCtrl ;

         loSrchReq = new SearchRequest() ;
         loSrchReq.addParameter( "R_GEN_DOC_CTRL", ATTR_DOC_CD, lsDocCd ) ;
         loDocCtrl = (R_GEN_DOC_CTRLImpl)R_GEN_DOC_CTRLImpl.getObjectByKey( loSrchReq, loSession ) ;
         /*
          * If the document control record is null, the relationship check will
          * catch it, so do nothing here.  No validation is possible.
          */
         if ( loDocCtrl != null )
         {
            String lsDocTyp = loDocCtrl.getData( ATTR_DOC_TYP ).getString() ;

            /*
             * The document type should never be null, but just check
             * to avoid any potential NullPointerException.
             */
            if ( lsDocTyp != null )
            {
               validateDocCompAndField( lsDocTyp, lsDocCd, loSession ) ;
            } /* end if ( lsDocTyp != null ) */
         } /* end if ( loDocCtrl != null ) */
      } /* end if ( lsDocCd != null ) */
   } /* end validateCombination() */

   private boolean validateDocCompAndField( String fsDocTyp, String fsDocCd, Session foSession )
   {
      String lsDocComp = getDOC_COMP() ;

      /*
       * If the document component is null, the required check will catch it,
       * so do nothing here.  No validation is possible.
       */
      if ( lsDocComp != null )
      {
         String lsCompPrefix = fsDocTyp + "_DOC_" ;

         if ( !lsDocComp.startsWith( lsCompPrefix ) )
         {
            raiseException( "The Document Component " + lsDocComp + " does not belong to the "
                  + fsDocCd + " Document" ) ;
         } /* end if ( !lsDocComp.startsWith( lsCompPrefix ) ) */
         else
         {
            String      lsClass   = foSession.getPackageName()  + "." + lsDocComp + "Impl";
            Class       loClass ;
            Method      loMethod ;
            String      lsDocFld ;

            try
            {
               loClass = Class.forName( lsClass ) ;
            } /* end try */
            catch( Exception foExp )
            {
               VSMetaQuery loMetaQry = getCompMetaQuery( lsDocComp, foSession ) ;

               if ( loMetaQry != null )
               {
                  return validateMetaQueryOnly( loMetaQry, lsDocComp, fsDocTyp, fsDocCd, foSession ) ;
               } /* end if ( loMetaQry != null ) */
               else
               {
                  raiseException( "Unable to locate Document Component " + lsDocComp ) ;
                  return false ;
               } /* end else */
            } /* end catch( Exception foExp ) */

            if ( ( !AMSDocHeader.class.isAssignableFrom( loClass ) ) &&
                 ( !AMSDocComponent.class.isAssignableFrom( loClass ) ) )
            {
               raiseException( "The Document Component must be a document header or document line" ) ;
               return false ;
            } /* end if ( ( !AMSDocHeader.class.isAssignableFrom( loClass ) ) && . . . */

            try
            {
               loMethod = loClass.getMethod( "getMetaQuery", ((java.lang.Class[])null) ) ;
            } /* end try */
            catch( Exception foExp )
            {
               raiseException( "Document Component " + lsDocComp + " class is invalid" ) ;
               return false ;
            } /* end catch( Exception foExp ) */

            lsDocFld = getDOC_FLD() ;

            /*
             * If the document field is null, the required check will catch it,
             * so do nothing here.  No validation is possible.
             */
            if ( lsDocFld != null )
            {
               VSMetaQuery loMetaQry ;

               try
               {
                  loMetaQry = (VSMetaQuery)loMethod.invoke( ((java.lang.Object[])null), ((java.lang.Object[])null)) ;
               } /* end try */
               catch( Exception foExp )
               {
                  raiseException( "Document Component " + lsDocComp + " class is invalid" ) ;
                  return false ;
               } /* end catch( Exception foExp ) */

               if ( loMetaQry.getMetaColumn( lsDocFld ) == null )
               {
                  raiseException( "Document Field " + lsDocFld + " does not exist on " + lsDocComp ) ;
                  return false ;
               } /* end if ( loMetaQry.getMetaColumn( lsDocFld ) == null ) */
               else
               {
                  return true ;
               } /* end else */
            } /* end if ( lsDocFld != null ) */
         } /* end else */

      } /* end if ( lsDocComp != null ) */
      return false ;
   } /* end validateDocCompAndField() */

   /**
    * This method attempts to retrieve or build the meta query for the specified
    * document component by reading the structure from the database.
    *
    * @param fsDocComp The document component name
    * @param foSession The user's session
    * @return The meta query if found; null otherwise
    */
   private static VSMetaQuery getCompMetaQuery( String fsDocComp, Session foSession )
   {
      VSMetaQuery loMetaQry ;

      try
      {
         loMetaQry = (VSQueryDefinition)AMSDataObject.getMetaQuery( fsDocComp, foSession ) ;
      } /* end try */
      catch( Exception foExp )
      {
         loMetaQry = null ;
         loMetaQry = getMetaQuery( fsDocComp, foSession.getPackageName() ) ;
      } /* end catch( Exception foExp ) */

      if ( loMetaQry != null )
      {
         return loMetaQry ;
      } /* end if ( loMetaQry != null ) */
      else
      {
         loMetaQry = AMSDataObject.constructMetaQueryFromMetaInfoTable( fsDocComp, foSession ) ;

         return loMetaQry ;
      } /* end else */
   } /* end getCompMetaQuery() */

   /**
    * This method will validate if the document code, type, and field
    * are valid based only on the meta query rather than using the
    * class instance.
    *
    * @param foMetaQry The component's meta query
    * @param fsDocType The document type
    * @param fsDocCd The document code
    * @param foSession The user's session
    */
   private boolean validateMetaQueryOnly( VSMetaQuery foMetaQry,
         String fsDocComp, String fsDocTyp, String fsDocCd, Session foSession )
   {
      try
      {
         String lsDocFld = getDOC_FLD() ;

         if ( foMetaQry.getMetaColumn( lsDocFld ) == null )
         {
            raiseException( "Document Field " + lsDocFld + " does not exist on " + fsDocComp ) ;
            return false ;
         } /* end if ( foMetaQry.getMetaColumn( lsDocFld ) == null ) */
         else
         {
            return true ;
         } /* end else */
      } /* end try */
      catch( Exception foExp )
      {
         moAMSLog.error( "Unable to validate meta query for "
               + foMetaQry.getName(), foExp ) ;
         return false ;
      } /* end catch( Exception foExp ) */
   } /* end validateMetaQueryOnly() */

   /**
    * This method ensures that the meta query for the component
    * has been loaded.
    *
    * @param foRowToBeAdded The data row to be added to the result set
    * @param foResponse The response object
    */
   public static void beforeResultSetFillBeforeSecurityCheck(
      DataRow foRowToBeAdded, Response foResponse )
   {
      getCompMetaQuery( foRowToBeAdded.getData( "DOC_COMP" ).getString(),
            foRowToBeAdded.getSession() ) ;
   } /* end beforeResultSetFillBeforeSecurityCheck() */

}
