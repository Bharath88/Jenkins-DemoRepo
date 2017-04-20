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
**  IN_DOC_CREA_ACTN*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_DOC_CREA_ACTNImpl extends  IN_DOC_CREA_ACTNBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public IN_DOC_CREA_ACTNImpl (){
		super();
	}
	
	public IN_DOC_CREA_ACTNImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}


	//{{COMPONENT_RULES
		public static IN_DOC_CREA_ACTNImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new IN_DOC_CREA_ACTNImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeUpdate
public void beforeUpdate(DataObject obj, Response response)
{
	//Write Event Code below this line

   SearchRequest        loSrchReq = new SearchRequest() ;
   Enumeration          loDocCreaEnum ;
   IN_DOC_CREA_ACTNImpl loDocCreaActn ;
   String               lsDocCd = null ;

   // Check if the default flag is enabled
   if ( !isNull( "DFLT_DOC_FL" ) && getDFLT_DOC_FL() )
   {
      lsDocCd = getDOC_CD();

      loSrchReq.addParameter( "IN_DOC_CREA_ACTN", "ACTN_CD", getData( "ACTN_CD" ).getString() ) ;
      loDocCreaEnum = IN_DOC_CREA_ACTNImpl.getObjects( loSrchReq, getSession() ) ;

      /**
       * For each document code, disable the default flag
       */
      while ( loDocCreaEnum.hasMoreElements() )
      {
         loDocCreaActn = (IN_DOC_CREA_ACTNImpl)loDocCreaEnum.nextElement() ;

         if ( loDocCreaActn != null )
         {
            /**
             * Ignore the current document and disable for all other
             * document codes for the current action.
             */
            if ( lsDocCd != null && !lsDocCd.equals( loDocCreaActn.getDOC_CD() ) )
            {
               loDocCreaActn.setDFLT_DOC_FL( false ) ;
               loDocCreaActn.save() ;
            } /* end if ( lsDocCd != null && !lsDocCd.equals( loDocCreaActn.getDOC_CD() ) ) */

         } /* end if ( loDocCreaActn != null ) */

      } /* end while ( loDocCreaEnum.hasMoreElements() ) */

   } /* if ( !isNull( "DFLT_DOC_FL" ) && getDFLT_DOC_FL() ) */

}
//END_COMP_EVENT_beforeUpdate}}

	//END_EVENT_CODE}}

	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

   /**
    * This method will check to see if an existing
    * document code has been enabled by default for
    * the current action.
    *
    * @return boolean True if one exists, else False
    */
   protected boolean checkDefaultEnabled()
   {
      SearchRequest        loSrchReq = new SearchRequest() ;
      Enumeration          loDocCreaEnum ;
      IN_DOC_CREA_ACTNImpl loDocCreaActn ;
      int                  liEnabledCount = 0 ;

      loSrchReq.addParameter( "IN_DOC_CREA_ACTN", "ACTN_CD", getData( "ACTN_CD" ).getString() ) ;
      loDocCreaEnum = IN_DOC_CREA_ACTNImpl.getObjects( loSrchReq, getSession() ) ;

      /**
       * For each document code, check if it is enabled
       */
      while ( loDocCreaEnum.hasMoreElements() )
      {
         loDocCreaActn = (IN_DOC_CREA_ACTNImpl)loDocCreaEnum.nextElement() ;

         if ( loDocCreaActn != null )
         {
            /**
             * Check the count of documents for which the default
             * is enabled for the current action.
             */
            if ( loDocCreaActn.getDFLT_DOC_FL() )
            {
               liEnabledCount++ ;
            } /* end if ( loDocCreaActn.getDFLT_DOC_FL() ) */
         } /* end if ( loDocCreaActn != null ) */

      } /* end while ( loDocCreaEnum.hasMoreElements() ) */

      if ( liEnabledCount == 1 )
      {
         return true;
      } /* end if ( liEnabledCount != 1 ) */
      else
      {
         return false;
      } /* end else */
   }

}
