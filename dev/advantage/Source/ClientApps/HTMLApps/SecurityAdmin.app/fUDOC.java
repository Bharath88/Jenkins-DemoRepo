//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.*;
import java.util.*;
import advantage.AMSStringUtil;
   /*
   **  fUSER*/

//{{FORM_CLASS_DECL
public class fUDOC extends fUDOCBase

//END_FORM_CLASS_DECL}}
   {
      private boolean mboolExistLDAPUser = false ;
      private boolean mboolFirstGenerate = true ;
      private ComboBoxElement    moUserSecRealm ;
   //Action performed on page
   private String msAction = null;
   //True when Display Sequence Number has to be sorted in ascending order.
   private boolean mboolSortDispSeqNoAsc = false;



      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public fUDOC ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      }


		//{{EVENT_CODE
		//{{EVENT_fUDOC_beforeActionPerformed
void fUDOC_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
   msAction = ae.getName();
   /*
    * When action is to sort Display Sequence Number then indicate if sort is
    * ascending or descending. First click on Display Sequence Number sort link
    * means ascending order, second click means descending order, and so on.
    */
   if( AMSStringUtil.strEqual( msAction, "T5DISP_SEQ_NOGridActionSort" ) )
   {
      //Flip value
      mboolSortDispSeqNoAsc = !mboolSortDispSeqNoAsc;
   }
   String lsExistLDAPUser = preq.getParameter( "EXIST_LDAP_USER_FL" ) ;

   if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP &&
         lsExistLDAPUser != null && lsExistLDAPUser.length() > 0 )
   {
      mboolExistLDAPUser = true ;
   }
   else
   {
      mboolExistLDAPUser = false ;
    }
}
//END_EVENT_fUDOC_beforeActionPerformed}}
//{{EVENT_T5USER_DOC_WFROLE_beforeQuery
void T5USER_DOC_WFROLE_beforeQuery(VSQuery query ,VSOutParam resultset )
{
	//Write Event Code below this line
   //Case where User selects sort link on Display Sequence Number
   if( AMSStringUtil.strEqual( msAction, "T5DISP_SEQ_NOGridActionSort") )
   {
      SearchRequest loOrderBy = new SearchRequest();

      /*
       * When User selects sort link on Display Sequence Number the sort on
       * Display Sequence Number and Role Name.
       * Records will be displayed as it would appear on 'Select Worklist' dropdown
       * combobox on Worklist page.
       */
      if( mboolSortDispSeqNoAsc )
      {
         //Case where User clicks on Display Sequence Number sort link
         //for ascending sort
         loOrderBy.add("DISP_SEQ_NO,WF_ROLE_NM");
      }
      else
      {
         //Case where User clicks on Display Sequence Number sort link for descending sort
         loOrderBy.add("DISP_SEQ_NO DESC,WF_ROLE_NM DESC");
      }
      query.replaceSortingCriteria(loOrderBy);
   }//end if
}
//END_EVENT_T5USER_DOC_WFROLE_beforeQuery}}

		//END_EVENT_CODE}}

		public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addPageListener(this);
	T5USER_DOC_WFROLE.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
		}

		//{{EVENT_ADAPTER_CODE
		
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			fUDOC_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T5USER_DOC_WFROLE) {
			T5USER_DOC_WFROLE_beforeQuery(query , resultset );
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
      * This method will enable/disable User Security Realm cvl depending on
      * AuthenticationType set on ADV30Parms then generate the html page
     */
     public String generate()
     {
        CheckboxElement loCBEExistLDAPUser;

        if ( mboolFirstGenerate )
        {
           mboolFirstGenerate = false ;
           moUserSecRealm = (ComboBoxElement) getElementByName( "txtT1USER_SEC_REALM" ) ;
        }

        // If the application authentication type is LDAP, then the
        // combobox should be enabled else disabled
        if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
        {
           fetchUserSecRealm(); //populate the combobox
        }
        else
        {
           moUserSecRealm.setEnabled( false );
        }

        loCBEExistLDAPUser = (CheckboxElement) getElementByName( "txtT1EXIST_LDAP_USER_FL" );

        // Check or uncheck the EXIST_LDAP_USER_FL checkbox based on
        // the value of the mboolExistLDAPUser variable
        if ( mboolExistLDAPUser )
        {
           loCBEExistLDAPUser.setChecked( true );
        }
        else
        {
           loCBEExistLDAPUser.setChecked( false );
        }

        // If the application authentication type is LDAP, then the
        // checkbox should be enabled else disabled
        if ( AMSParams.miAuthenticationType == AMSParams.TYPE_LDAP  )
        {
           loCBEExistLDAPUser.setEnabled( true );
        }
        else
        {
           loCBEExistLDAPUser.setEnabled( false );
        }

        return super.generate();
     }

    /**
     * This method fetches the user security realm combo box with values
     * available as specified in the CSF.Properties file
     */
    private void fetchUserSecRealm()
    {
       String lsUserSecRealms = "";
       String lsUserSecRealm  = "";
       StringTokenizer lstSecurityRealms = null;

       try
       {
          if ( moUserSecRealm.getSize() == 0 )
          {
             lsUserSecRealms = AMSSecurity.getSecurityRealms();
             if ( ( lsUserSecRealms != null ) && ( lsUserSecRealms.trim().length() > 0 ) )
             {
                lstSecurityRealms = new StringTokenizer( lsUserSecRealms, "," );
                while ( lstSecurityRealms.hasMoreTokens() )
                {
                   lsUserSecRealm = lstSecurityRealms.nextToken();
                   moUserSecRealm.addElement(lsUserSecRealm,lsUserSecRealm);
                } //end while
             } //end if ( ( lsUserSecRealms != null )...
             else
             {
                /*
                 * when there is no property set for security realm we need to
                 * set the IgnoreSetValue flag to true so that it does not set empty string in
                 * the comboBox's option tags.
                 */
                ((AMSComboBoxElement)moUserSecRealm).setIgnoreSetValue(true);
             }
          } //end if ( moUserSecRealm.getSize() == 0 )
       } //end try
       catch (AMSSecurityException loExp)
       {
          raiseException( "Unable to get the user security realm list", SEVERITY_LEVEL_ERROR ) ;
       } //end catch
    } //end of fetchUserSecRealm()
  }