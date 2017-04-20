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
**  R_SC_USER_DOC_COMP*/

//{{COMPONENT_RULES_CLASS_DECL
public class R_SC_USER_DOC_COMPImpl extends  R_SC_USER_DOC_COMPBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
//{{COMP_CLASS_CTOR
public R_SC_USER_DOC_COMPImpl (){
	super();
}

public R_SC_USER_DOC_COMPImpl(Session session, boolean makeDefaults)
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
	public static R_SC_USER_DOC_COMPImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_SC_USER_DOC_COMPImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

   /**
    * This method verifies that the document component exists and that it is a
    * document component data object belonging to the user maintenance document.
    */
   protected boolean checkDocComp()
   {
      String lsDocComp = getDOC_COMP() ;

      /*
       * If the document component is null, the required check will catch it,
       * so do nothing here.  No validation is possible.
       */
      if ( lsDocComp != null )
      {
         String lsCompPrefix = "USER_DOC_" ;

         if ( !lsDocComp.startsWith( lsCompPrefix ) )
         {
            raiseException( "%c:Q0064,v:" + lsDocComp + "%" ) ;
         } /* end if ( !lsDocComp.startsWith( lsCompPrefix ) ) */
         else
         {
            String      lsClass   = getSession().getPackageName()  + "." + lsDocComp + "Impl";
            Class       loClass ;
            Method      loMethod ;
            String      lsDocFld ;

            try
            {
               loClass = Class.forName( lsClass ) ;
            } /* end try */
            catch( Exception foExp )
            {
               raiseException( "%c:Q0065,v:" + lsDocComp + "%" ) ;
               return false ;
            } /* end catch( Exception foExp ) */

            if ( !AMSDocComponent.class.isAssignableFrom( loClass ) )
            {
               raiseException( "%c:Q0066%" ) ;
            } /* end if ( !AMSDocComponent.class.isAssignableFrom( loClass ) ) */
            else
            {
               return true ;
            } /* end else */
         } /* end else */
      } /* end if ( lsDocComp != null ) */
      return false ;
   } /* end checkDocComp() */
}

