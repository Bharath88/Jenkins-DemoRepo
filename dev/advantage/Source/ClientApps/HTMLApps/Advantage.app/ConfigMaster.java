//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

/*
**  ConfigMaster
*/

//{{FORM_CLASS_DECL
public class ConfigMaster extends ConfigMasterBase

//END_FORM_CLASS_DECL}}
{
   public static final int    NO_TYPE                = -1 ;
   public static final int    WORKSPACE_TYPE         = 0 ;
   public static final int    BUS_AREA_TYPE          = 1 ;
   public static final int    BUS_FUNC_TYPE          = 2 ;
   public static final int    BUS_FUNC_PG_TYPE       = 3 ;
   public static final int    FAVORITE_TYPE          = 4 ;
   public static final int    HMPG_WIN_TYPE          = 5 ;
   public static final int    FIRST_VALID_TYPE       = 0 ;
   public static final int    LAST_VALID_TYPE        = 5 ;
   public static final String CONFIG_PAGE_ID_PROP = "AMSConfigPageID" ;

   private int miCopiedDataType = NO_TYPE ;
   private Object moCopiedData  = null ;

	// Declarations for instance variables used in the form

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code. To customize paint
	// behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public ConfigMaster ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}



	//{{EVENT_CODE
	//{{EVENT_ConfigMaster_pageCreated
void ConfigMaster_pageCreated()
{
   getParentApp().getSession().setORBProperty( CONFIG_PAGE_ID_PROP, getPageId() ) ;
}
//END_EVENT_ConfigMaster_pageCreated}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void pageCreated ( VSPage obj ){
		Object source = obj;
		if (source == this ) {
			ConfigMaster_pageCreated();
		}
	}
	//END_EVENT_ADAPTER_CODE}}

    	public HTMLDocumentSpec getDocumentSpecification() {
            return getDefaultDocumentSpecification();
    	}

        public String getFileName() {
		    return getDefaultFileName();
	    }
    	public String getFileLocation() {
        	return getPageTemplatePath();
   	    }

	public void afterPageInitialize() {
		super.afterPageInitialize();
		//Write code here for initializing your own control
		//or creating new control.

	}

   public int getCopiedDataType()
   {
      return miCopiedDataType ;
   } /* end getCopiedDataType() */

   public Object getCopiedData()
   {
      return moCopiedData ;
   } /* end getCopiedData() */

   private void setCopiedDataType( int fiCopiedDataType )
   {
      miCopiedDataType = fiCopiedDataType ;
   } /* end setCopiedDataType() */

   private void setCopiedData( Object foCopiedData )
   {
      moCopiedData = foCopiedData ;
   } /* end setCopiedData() */

   public boolean setCopiedData( int fiCopiedDataType, Object foCopiedData )
   {
      if (     ( fiCopiedDataType >= FIRST_VALID_TYPE )
            && ( fiCopiedDataType <= LAST_VALID_TYPE )
            && ( foCopiedData != null ) )
      {
         setCopiedDataType( fiCopiedDataType ) ;
         setCopiedData( foCopiedData ) ;
//System.err.println( "*** copied data is set ***" ) ;
         return true ;
      }

      return false ;
   } /* end setCopiedData() */

   public void clearCopiedData()
   {
      setCopiedDataType( NO_TYPE ) ;
      setCopiedData( null ) ;
   }

   public String generate()
   {
      appendOnloadString( "submitForm(document.ConfigMaster,\'T2ConfigNavPage=T2ConfigNavPage&vsnavigation=T2ConfigNavPage\',\'NavTree\');" ) ;
      return super.generate() ;
   } /* end generate() */
}