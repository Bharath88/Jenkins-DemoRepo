//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.rmi.RemoteException;
import javax.swing.text.* ;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.* ;
//BGN ADVHR00039334-3S
import advantage.AMSMsgUtil;
//END ADVHR00039334-3S

/*
**  DocCatalog
*/

//{{FORM_CLASS_DECL
public class DocCatalog extends DocCatalogBase

//END_FORM_CLASS_DECL}}

{
   // Declarations for instance variables used in the form

   /** Indicates if the page is currently in search or create mode */
   private boolean mboolSearchMode = true ;

   private DivElement    moSearchSctnCtrl   = null ;
   private DivElement    moCreateSctnCtrl   = null ;
   private DivElement    moSearchSctn       = null ;
   private DivElement    moCreateSctn       = null ;
   private DivElement    moSearchModeActn   = null ;
   private DivElement    moCreateModeActn   = null ;
   private DivElement    moSearchModeRslt   = null ;
   private DivElement    moCreateModeCreate = null ;
   private ScalarElement moDocCd            = null ;
   private ScalarElement moDocDeptCd        = null ;
   private ScalarElement moDocUnitCd        = null ;
   private ScalarElement moDocId            = null ;

      /**
       * String for the Hidden Value fields of the DocCatalag page
       */
      public static  final String DOC_OPEN_BF     = "OpenInBF";
      public static  final String DOC_BF_PAGE_NUM = "BFPageNum";
      private static final String DOC_CODE_HIDDEN = "BFDocCode";
      // IR-HRFX5713 BEGIN
      // added more filtering fields (document key fields)
      private static final String DOC_DEPT_CD_HIDDEN  = "BFDocDeptCd";
      private static final String DOC_UNIT_CD_HIDDEN  = "BFDocUnitCd";
      private static final String DOC_ID_HIDDEN       = "BFDocId";
      // IR-HRFX5713 END

      private static final String PAGE_NAV    = "SOURCE_PAGE";
      private static final String DOC_CD      = "DOC_CD";
      private static final String DOC_DEPT_CD = "DOC_DEPT_CD";
      private static final String DOC_UNIT_CD = "DOC_UNIT_CD";
      private static final String DOC_ID      = "DOC_ID";

      //BGN ADVHR00039334-3S
      private static final String C_ERR_MIN_SEARCH = "Doc Code and one of the following search criteria " +
            "must be entered: Doc Dept Code, Doc ID, Create User ID or Create Date.";
      public static final String ERR_WILDCARD_ALONE = "Wildcards alone are not allowed.";
      //END ADVHR00039334-3S

      private boolean mboolFirstTime = true;
      private boolean mboolFirstTimeVls = true;
      
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public DocCatalog ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}


   }


//{{EVENT_CODE
//{{EVENT_T1DOC_HDR_beforeQuery
/**
 * This method is called before the query is performed on the
 * document catalog. This method is used to set up the where clause
 * to retrieve only those documents which belong to applications to
 * which the user has logged into.
 *
 * Modification Log : Kiran Hiranandani   - 08/07/02
 *                                        - Removed the super qualifier from the
 *                                        - call to modifyQuery()
 *
 * @param foQuery         Reference to the query statement
 * @param foRsltSet     Result Set
 */
void T1DOC_HDR_beforeQuery( VSQuery foQuery, VSOutParam foRsltSet )
{
   /**
    * If the user is navigating to this page in the browse
    * mode and if no developer where clause has been specified
    * then let's disable the query fired to the database to 
    * improve performance for opening the document catalog.
    */
   if (mboolFirstTime &&
       T1DOC_HDR.getQueryMode() == DataSource.MODE_NORMAL)
   {
      String lsDevWhere = T1DOC_HDR.getQueryInfo().getDevWhere();
      String lsQueryText = T1DOC_HDR.getOnScreenQueryText();

      if ( (lsDevWhere == null || lsDevWhere.trim().length() == 0 ) &&
           (lsQueryText == null || lsQueryText.trim().length() == 0 ))
      {
         //BGN ADVHR00039334-3S
         foRsltSet.setValue(true);
         return;
         //END ADVHR00039334-3S
      }/* end if (lsDevWhere == null || lsDevWhere.trim().length() == 0) */
   }
   else if (!mboolFirstTime && T1DOC_HDR.getQueryMode() == DataSource.MODE_NORMAL)
   {
       mboolFirstTime = true;
   }


   modifyQuery(foQuery, foRsltSet) ;
}
//END_EVENT_T1DOC_HDR_beforeQuery}}
//{{EVENT_DocCatalog_beforeActionPerformed
void DocCatalog_beforeActionPerformed( ActionElement foActnElem, PageEvent foEvent, PLSRequest foPLSReq )
{
   String lsActnName = foActnElem.getName() ;

   if ( lsActnName != null )
   {
      if ( lsActnName.equals( "DocSearchSearchMode" ) )
      {
         mboolSearchMode = true ;
         foEvent.setCancel( true ) ;
         foEvent.setNewPage( this ) ;
      } /* end if ( lsActnName.equals( "DocSearchSearchMode" ) ) */
      else if ( lsActnName.equals( "DocSearchCreateMode" ) )
      {
         mboolSearchMode = false ;
         foEvent.setCancel( true ) ;
         foEvent.setNewPage( this ) ;
      } /* end else if ( lsActnName.equals( "DocSearchCreateMode" ) ) */
      else if (lsActnName.equals( "AMSBrowse" ))
      {
         //BGN ADVHR00039334-3S
         //Check to verfiy if Current Application in HR
         if(AMSParams.msPrimaryApplication.equals("2"))
         {
            validateSearchCriteria();
         }
         //END ADVHR00039334-3S
         mboolFirstTime = false;
      }
   } /* end if ( lsActnName != null ) */
}
//END_EVENT_DocCatalog_beforeActionPerformed}}
//{{EVENT_T2R_GEN_DOC_CTRL_BeforePickQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those records which belong to either the
 * common application or the application to which user logged in.
 *
 * Modification Log : Srinivas Naikoti   - 12/18/03
 *                                       - inital version
 *
 * @param foPick          Pick object which is doing the query
 * @param foDataSource    The data control which started the pick
 * @param foWhereClause   Where clause for the pick query
 * @param foOrderBy       The order by clause for pick query
 * @param foCancel        param for cancelling the query
 */
void T2R_GEN_DOC_CTRL_BeforePickQuery(Pick foPick, DataSource foDataSource,
      VSOutParam foWhereClause, VSOutParam foOrderBy, VSOutParam foCancel)
{
   String               lsApplWhereClause = null ;
   String               lsOldWhereClause = null ;
   VSORBSession         loORBSession = parentApp.getSession().getORBSession() ;
   AMSSecurityObject    loSecObj = null ;

   try
   {
      loSecObj = (AMSSecurityObject) loORBSession.getServerSecurityObject() ;
   }
   catch ( RemoteException loRemExp )
   {
      raiseException( "Unable to get Security Object", AMSPage.SEVERITY_LEVEL_SEVERE ) ;
      return ;
   }

   /* Retrieve where clause for Applications  */
   lsApplWhereClause = AMSSecurityObject.getApplicationWhere( loSecObj, true ) ;

   /* Check if there is already an existing where clause */
   lsOldWhereClause = foWhereClause.stringValue() ;

   if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 )
   {
      //BGN ADVHR00039334-3S
      lsApplWhereClause = new StringBuilder(lsOldWhereClause).append(" AND ").append(lsApplWhereClause).toString();
      //END ADVHR00039334-3S
   } /* end if ( lsOldWhereClause != null && lsOldWhereClause.trim().length() > 0 ) */

   /* Reset the where clause in the main query */
   foWhereClause.setValue( lsApplWhereClause ) ;
}
//END_EVENT_T2R_GEN_DOC_CTRL_BeforePickQuery}}
//{{EVENT_DocCatalog_afterGenerate
void DocCatalog_afterGenerate(StringBuffer s)
{
    //Write Event Code below this line
    //BGN ADVHR00039334-3S
    T1DOC_HDR.clearQBFData();
    //END ADVHR00039334-3S
}
//END_EVENT_DocCatalog_afterGenerate}}

//END_EVENT_CODE}}


   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1DOC_HDR.addDBListener(this);
	addPageListener(this);
	T2R_GEN_DOC_CTRL.addPickListener(this);
//END_EVENT_ADD_LISTENERS}}

   }

//{{EVENT_ADAPTER_CODE

	public void afterGenerate(VSPage obj, StringBuffer s){
		Object source = obj;
		if (source == this ) {
			DocCatalog_afterGenerate(s);
		}
	}
	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T2R_GEN_DOC_CTRL) {
			T2R_GEN_DOC_CTRL_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			DocCatalog_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1DOC_HDR) {
			T1DOC_HDR_beforeQuery(query , resultset );
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
    * Overwriting the setSourcePage method
    * to pin the page first time
    *
    * Modification Log : Srinivas Naikoti    - 04/10/2002
    *                                        - Initial version.
    *
    * @param foSrcPage The source page which performed the transition
    */
   public void setSourcePage(VSPage foSrcPage)
   {
      super.setSourcePage( foSrcPage );

      if ( ( foSrcPage != null ) && ( foSrcPage instanceof AMSDocCatalogPage ) )
      {
         /*
          * If the source page is another document catalog page, then
          * we assume that it is a auxiliary document catalog page that
          * is transitioning to this page because the user clicked on
          * the "Create" hyperlink.  Therefore, we'll initially show
          * the create <div>
          */
         mboolSearchMode = false ;
      } /* end if ( ( foSrcPage != null ) && ( foSrcPage instanceof AMSDocCatalogPage ) ) */

      /**
       * Let us pin the page only if this page is being accessed
       * from the application navigation panel
       */
      if ( mboolFirstTime )
      {
         if ( ( foSrcPage != null ) && ( foSrcPage instanceof AppNavPanel ) )
         {
            setPin(true);
         } /* end if ( ( foSrcPage != null ) && ( foSrcPage instanceof AppNavPanel ) ) */
      } /* end if ( mboolFirstTime ) */
   }

   /**
    * This method sets the filter doc code on the
    * page to the document code specified.
    *
    * @param fsDocCode The document code by which to filter
    */
   public void setFilterDocCode( String fsDocCode )
   {
      HiddenElement loHiddenCodeElem = (HiddenElement)getElementByName( DOC_CODE_HIDDEN ) ;

      // IR-HRFX5713 BEGIN
      // hidden element setValue() method cannot handle null parameter
      //if ( loHiddenCodeElem != null )
      if ( loHiddenCodeElem != null && fsDocCode != null && fsDocCode.length() > 0 )
      // IR-HRFX5713 END
      {
         loHiddenCodeElem.setValue( fsDocCode ) ;
         T1DOC_HDR.setQBFDataForElement( ATTR_DOC_CD, fsDocCode ) ;
      } /* end if ( loHiddenCodeElem != null ) */
   } /* end setFilterDocCode() */


    // IR-HRFX5713 BEGIN

    /**
     * This method sets the filter Document Department code on the
     * page to the Document Department code specified.
     *
     * @param fsDocDeptCd - The Document Department code by which to filter
     */
    public void setFilterDocDeptCd( String fsDocDeptCd )
    {
        HiddenElement loHiddenCodeElem = (HiddenElement)getElementByName( DOC_DEPT_CD_HIDDEN );

        if ( loHiddenCodeElem != null && fsDocDeptCd != null && fsDocDeptCd.length() > 0 )
        {
            loHiddenCodeElem.setValue( fsDocDeptCd );
            T1DOC_HDR.setQBFDataForElement( ATTR_DOC_DEPT_CD, fsDocDeptCd );
        }
    }



    /**
     * This method sets the filter Document Unit code on the
     * page to the Document Unit code specified.
     *
     * @param fsDocUnitCd - The Document Unit code by which to filter
     */
    public void setFilterDocUnitCd( String fsDocUnitCd )
    {
        HiddenElement loHiddenCodeElem = (HiddenElement)getElementByName( DOC_UNIT_CD_HIDDEN );

        if ( loHiddenCodeElem != null && fsDocUnitCd != null && fsDocUnitCd.length() > 0 )
        {
            loHiddenCodeElem.setValue( fsDocUnitCd );
            T1DOC_HDR.setQBFDataForElement( ATTR_DOC_UNIT_CD, fsDocUnitCd );
        }
    }



    /**
     * This method sets the filter Document ID on the
     * page to the Document ID specified.
     *
     * @param fsDocId - The Document ID by which to filter
     */
    public void setFilterDocId( String fsDocId )
    {
        HiddenElement loHiddenCodeElem = (HiddenElement)getElementByName( DOC_ID_HIDDEN );

        if ( loHiddenCodeElem != null && fsDocId != null && fsDocId.length() > 0 )
        {
            loHiddenCodeElem.setValue( fsDocId );
            T1DOC_HDR.setQBFDataForElement( ATTR_DOC_ID, fsDocId );
        }
    }

    // IR-HRFX5713 END


   /**
    * Calling this method will enable/disable the DOC_CD
    * choice box based on the flag passed.  It then calls
    * the regular generate method.
    *
    * Modification Log : Mark Shipley    - 07/14/2000
    *                                    - Initial version.
    *
    * @param fbDisableDocCode The doc code field enable flag
    */
   public String generate( boolean fbDisableDocCode )
   {
      if ( moDocCd != null )
      {
         if ( fbDisableDocCode )
         {
            PickElement loDocCdPick = (PickElement)getElementByName( "pckT1DOC_CD" ) ;

            moDocCd.addAttribute( "ams_readonly", "ams_readonly" ) ;
            if ( loDocCdPick != null )
            {
               loDocCdPick.setEnabled( false ) ;
            } /* end if ( loDocCdPick != null ) */
         } /* end if ( fbDisableDocCode ) */
         else
         {
            moDocCd.getHtmlElement().removeAttribute( "ams_readonly" ) ;
         } /* end else ) */
      } /* end if ( moDocCd != null ) */
      return generate() ;
   } /* end generate( String fsDocCode ) */

   /**
    * Overwriting the generate method
    *
    * Modification Log : Mukund Mohan - 08/02/2001
    *                                 - Added code to show the tab from
    *                                 - which the pick was performed after
    *                                 - the pick has been performed.
    */
   public void beforeGenerate()
   {
      VSORBSession loSession = parentApp.getSession().getORBSession();

      try
      {
         if(loSession.getProperty(PAGE_NAV).equals("CNTCATLG"))
         {
            T1DOC_HDR.setQBFDataForElement(ATTR_DOC_CD, loSession.getProperty(DOC_CD));
            T1DOC_HDR.setQBFDataForElement(ATTR_DOC_DEPT_CD, loSession.getProperty(DOC_DEPT_CD));
            T1DOC_HDR.setQBFDataForElement(ATTR_DOC_UNIT_CD, loSession.getProperty(DOC_UNIT_CD));
            T1DOC_HDR.setQBFDataForElement(ATTR_DOC_ID, loSession.getProperty(DOC_ID));
            resetSessionProp();
         }
      } /* end try */
      catch( RemoteException foExp )
      {
         raiseException( "Unable to get Session Property",SEVERITY_LEVEL_SEVERE);
      } /* end catch( RemoteException foExp ) */

       if ( mboolFirstTimeVls )
       {
         mboolFirstTimeVls = false ;

         moSearchSctnCtrl   = (DivElement)getElementByName( "SearchSctnCtrl" ) ;
         moCreateSctnCtrl   = (DivElement)getElementByName( "CreateSctnCtrl" ) ;
         moSearchSctn       = (DivElement)getElementByName( "SearchSctn" ) ;
         moCreateSctn       = (DivElement)getElementByName( "CreateSctn" ) ;
         moSearchModeActn   = (DivElement)getElementByName( "SearchModeActn" ) ;
         moCreateModeActn   = (DivElement)getElementByName( "CreateModeActn" ) ;
         moSearchModeRslt   = (DivElement)getElementByName( "SearchModeRslt" ) ;
         moCreateModeCreate = (DivElement)getElementByName( "CreateModeCreate" ) ;
         moDocCd            = (ScalarElement)getElementByName( ATTR_DOC_CD ) ;
         moDocDeptCd        = (ScalarElement)getElementByName( ATTR_DOC_DEPT_CD ) ;
         moDocUnitCd        = (ScalarElement)getElementByName( ATTR_DOC_UNIT_CD ) ;
         moDocId            = (ScalarElement)getElementByName( ATTR_DOC_ID ) ;
      } /* end if ( mboolFirstTimeVls ) */

      if ( moSearchSctnCtrl != null )
      {
         moSearchSctnCtrl.setVisible( mboolSearchMode ) ;
      } /* end if ( moSearchSctnCtrl != null ) */
      if ( moCreateSctnCtrl != null )
      {
         moCreateSctnCtrl.setVisible( !mboolSearchMode ) ;
      } /* end if ( moCreateSctnCtrl != null ) */
      if ( moSearchSctn != null )
      {
         moSearchSctn.setVisible( mboolSearchMode ) ;
      } /* end if ( moSearchSctn != null ) */
      if ( moCreateSctn != null )
      {
         moCreateSctn.setVisible( !mboolSearchMode ) ;
      } /* end if ( moCreateSctn != null ) */
      if ( moSearchModeActn != null )
      {
         moSearchModeActn.setVisible( !mboolSearchMode ) ;
      } /* end if ( moSearchModeActn != null ) */
      if ( moCreateModeActn != null )
      {
         moCreateModeActn.setVisible( mboolSearchMode ) ;
      } /* end if ( moCreateModeActn != null ) */
      if ( moSearchModeRslt != null )
      {
         moSearchModeRslt.setVisible( mboolSearchMode ) ;
      } /* end if ( moSearchModeRslt != null ) */
      if ( moCreateModeCreate != null )
      {
         moCreateModeCreate.setVisible( !mboolSearchMode ) ;
      } /* end if ( moCreateModeCreate != null ) */
      if ( moDocCd != null )
      {
         if ( mboolSearchMode )
         {
            ((MutableAttributeSet)moDocCd.getHtmlElement()).removeAttribute( HTML.Attribute.MAXLENGTH ) ;
         } /* end if ( mboolSearchMode ) */
         else
         {
           ((MutableAttributeSet)moDocCd.getHtmlElement()).addAttribute( HTML.Attribute.MAXLENGTH,
                  Integer.toString( moDocCd.getMetaColumn().getSize() ) ) ;
         } /* end else */
      } /* end if ( moDocCd != null ) */
      if ( moDocDeptCd != null )
      {
         if ( mboolSearchMode )
         {
            ((MutableAttributeSet)moDocDeptCd.getHtmlElement()).removeAttribute( HTML.Attribute.MAXLENGTH ) ;
         } /* end if ( mboolSearchMode ) */
         else
         {
           ((MutableAttributeSet)moDocDeptCd.getHtmlElement()).addAttribute( HTML.Attribute.MAXLENGTH,
                  Integer.toString( moDocDeptCd.getMetaColumn().getSize() ) ) ;
         } /* end else */
      } /* end if ( moDocDeptCd != null ) */
      if ( moDocUnitCd != null )
      {
         if ( mboolSearchMode )
         {
            ((MutableAttributeSet)moDocUnitCd.getHtmlElement()).removeAttribute( HTML.Attribute.MAXLENGTH ) ;
         } /* end if ( mboolSearchMode ) */
         else
         {
           ((MutableAttributeSet)moDocUnitCd.getHtmlElement()).addAttribute( HTML.Attribute.MAXLENGTH,
                  Integer.toString( moDocUnitCd.getMetaColumn().getSize() ) ) ;
         } /* end else */
      } /* end if ( moDocUnitCd != null ) */
      if ( moDocId != null )
      {
         if ( mboolSearchMode )
         {
            ((MutableAttributeSet)moDocId.getHtmlElement()).removeAttribute( HTML.Attribute.MAXLENGTH ) ;
         } /* end if ( mboolSearchMode ) */
         else
         {
           ((MutableAttributeSet)moDocId.getHtmlElement()).addAttribute( HTML.Attribute.MAXLENGTH,
                  Integer.toString( moDocId.getMetaColumn().getSize() ) ) ;
         } /* end else */
      } /* end if ( moDocId != null ) */
      if ( !mboolSearchMode )
      {
         appendOnloadString( "AMSTAB_ExpandSection('T1DOC_HDR_Section1','T1DOC_HDR_Section1_Button');"
               + "AMSTAB_ExpandSection('T1DOC_HDR_Section4','T1DOC_HDR_Section4_Button')" ) ;
      } /* end if ( !mboolSearchMode ) */
      super.beforeGenerate() ;
   } /* end beforeGenerate() */

   /**
    * This method returns the context information that can be displayed
    * to the user. In most cases the context in the document catalog
    * doesn't make sense so we always turn of context sensitive navigation
    *
    * Modification Log : Subramanyam Surabhi - 07/24/00
    *                                        - inital version
    * @param String - Context information in the form of a where clause
    * @return String - User friendly context information
    */
   public String getUserContext(String fsContext)
   {
      return "";
   }


   //IR-NFOC000487 Fix Begin
   /**
    *  Turning on Virtual ResultSet for Document Catalog.  It is not necessary, but
    *  it is done to keep the code in-sync with OC
    */

   public boolean useVirtualResultSet( AMSDataSource foDataSource )
   {
      return true ;
   }
   //IR-NFOC000487 Fix End

   /**
     * This method resets the session property.
     * @return void.
     */
   private void resetSessionProp()
   {
      VSORBSession loSession = parentApp.getSession().getORBSession();

      try
      {
         loSession.setProperty(PAGE_NAV,"");
         loSession.setProperty(DOC_CD,"");
         loSession.setProperty(DOC_DEPT_CD,"");
         loSession.setProperty(DOC_UNIT_CD,"");
         loSession.setProperty(DOC_ID,"");
      } /* end try */
      catch( RemoteException foExp )
      {
         raiseException( "Unable to get Session Property",SEVERITY_LEVEL_SEVERE);
      } /* end catch( RemoteException foExp ) */
   }
   
   //BGN ADVHR00039334-3S
   /**
    * This method validates the Minimum Search Criteria.
    */
   private void validateSearchCriteria()
   {
	   //Declare the minimum search criteria for this page.
	   String lsDocCode = T1DOC_HDR.getQBFDataForElement(ATTR_DOC_CD).trim();
	   StringBuilder sbSearchKeyFieldsValue = new StringBuilder();
	   sbSearchKeyFieldsValue.append ( T1DOC_HDR.getQBFDataForElement(ATTR_DOC_DEPT_CD).trim() );
	   sbSearchKeyFieldsValue.append ( T1DOC_HDR.getQBFDataForElement(ATTR_DOC_ID).trim() );
	   sbSearchKeyFieldsValue.append ( T1DOC_HDR.getQBFDataForElement("txtT1DOC_CREA_USID_UP").trim() );
	   sbSearchKeyFieldsValue.append ( T1DOC_HDR.getQBFDataForElement("txtT1DOC_APPL_CREA_DT").trim() );

	   //Check for string buffer is empty (to check all the search fields are empty or not)
	   if (lsDocCode.trim().length() == 0 || sbSearchKeyFieldsValue.toString().trim().length() == 0)
	   {
		   raiseException( C_ERR_MIN_SEARCH, AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
	   }

	   //If string buffer has value, then check if the value contains only wildcards by
	   // replacing all the occurances of '*' and '%' with empty string.
	   else if ((lsDocCode.trim().replaceAll("(%|\\*)", "").length() == 0) ||
			   (sbSearchKeyFieldsValue.toString().trim().replaceAll("(%|\\*)", "").length() == 0))
	   {
		   raiseException( C_ERR_MIN_SEARCH + " " + ERR_WILDCARD_ALONE,
				   AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
	   }
   }
   //END ADVHR00039334-3S
}
