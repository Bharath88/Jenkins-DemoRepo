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
**  IN_DOC_ARCH_CTLG*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_DOC_ARCH_CTLGImpl extends  IN_DOC_ARCH_CTLGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /**
    * Indicates if this catalog entry performed a document restore;
    * we use flag to know when to clean up the archive files/data
    */
   public boolean mboolPerformedDocumentRestore = false ;

//{{COMP_CLASS_CTOR
public IN_DOC_ARCH_CTLGImpl (){
	super();
}

public IN_DOC_ARCH_CTLGImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }


//{{COMPONENT_RULES
	public static IN_DOC_ARCH_CTLGImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new IN_DOC_ARCH_CTLGImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_afterCommit
public void afterCommit( Session session )
//@SuppressAbstract
{
   /**
    * If this catalog entry just retored a document
    * try to clean up the archive data from storage
    */
   if ( ( mboolPerformedDocumentRestore ) &&
        ( AMSMsgUtil.getHighestSeverityLevel( session )
       <= AMSMsgUtil.SEVERITY_LEVEL_WARNING ) )
   {
      try
      {
         AMSArchiveStorage loArchStore =
            AMSArchiveStorage.getInstance( session ) ;

         loArchStore.setArchFileLocation( getFILE_LOCATION() ) ;

         loArchStore.deleteArchive( getDOC_ARCH_ID(),
                                    AMSArchiveStorage.ARCHIVE_TYPE_DOC ) ;

         loArchStore.deleteArchive( getDOC_ARCH_ID(),
                                    AMSArchiveStorage.ARCHIVE_TYPE_OBJ_ATT ) ;

         mboolPerformedDocumentRestore = false ;
      }
      catch ( Exception loException )
      {
         if ( AMS_DEBUG )
         {
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loException);
         }

         raiseException( "Warning: archive data could not be cleaned up.",
                         AMSMsgUtil.SEVERITY_LEVEL_WARNING ) ;
      }
   }

//   super.afterCommit( session ) ;
}
//END_COMP_EVENT_afterCommit}}

//END_EVENT_CODE}}

   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addTransactionEventListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

}
