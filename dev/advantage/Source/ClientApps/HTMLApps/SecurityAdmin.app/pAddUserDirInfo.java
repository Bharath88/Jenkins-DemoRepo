//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import advantage.AMSStringUtil;

	/*
	**  pAddUserDirInfo*/

	//{{FORM_CLASS_DECL
	public class pAddUserDirInfo extends pAddUserDirInfoBase
	
	//END_FORM_CLASS_DECL}}
	{
	   // Declarations for instance variables used in the form

	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pAddUserDirInfo ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }



	//{{EVENT_CODE
	//{{EVENT_pAddUserDirInfo_beforeActionPerformed
void pAddUserDirInfo_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line

   String lsAction = ae.getAction() ;
   String lsActnNm = ae.getName() ;

   if(AMSStringUtil.strEqual(ae.getName(),"btnT2pAddUserAppInfo"))
   {
      VSRow loDirInfoData = T1R_SC_USER_DIR_INFO.getCurrentRow();
      String lsUserId =
            loDirInfoData.getData("USER_ID").getString();
      String lsFrstNmTxt  =
            loDirInfoData.getData("FRST_NM_TXT").getString();
      String lsLastNmTxt  =
            loDirInfoData.getData("LAST_NM_TXT").getString();
      String lsEmailAddTxt  =
         loDirInfoData.getData("EMAIL_AD_TXT").getString();

      
      if( !(isEntered(lsUserId,"User ID") & isEntered(lsFrstNmTxt,"First Name")
            & isEntered(lsLastNmTxt,"Last Name") & isEntered(lsEmailAddTxt,"Email Address")))
     {
         evt.setCancel( true ) ;
         evt.setNewPage( this ) ;  
      }//End if()
   }    

}
//END_EVENT_pAddUserDirInfo_beforeActionPerformed}}

	//END_EVENT_CODE}}

	   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	   }

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pAddUserDirInfo_beforeActionPerformed( ae, evt, preq );
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
  
   /**
    *  Check if user has entered any value.
    *  @param - Value
    *  @param - Field Name 
    */
    public boolean isEntered(String lsValue,String lsFieldNm)
    {
       if(AMSStringUtil.strIsEmpty(lsValue))
       {
          raiseException(lsFieldNm + " is required. "
                ,SEVERITY_LEVEL_ERROR);
          return false;
       }
       return true;
    }
}