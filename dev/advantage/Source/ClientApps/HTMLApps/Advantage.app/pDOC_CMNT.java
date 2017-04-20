//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.AMSSQLUtil;
/*
**  pDOC_CMNT
*/

//{{FORM_CLASS_DECL
public class pDOC_CMNT extends pDOC_CMNTBase

//END_FORM_CLASS_DECL}}
{

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.

   /* Document Identifiers */
   private String msDocCd;
   private String msDocDeptCd;
   private String msDocId;
   private String msDocVersNo;
   private String msDocPhaseCd;

   /* first time generate flag */
   private boolean mboolFirstTime = true;
   private static String C_MSG_SUPPRESSED_CMNT_EXISTS ="This document has suppressed comments.";

//{{FORM_CLASS_CTOR
public pDOC_CMNT ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}

   }


//{{EVENT_CODE
//{{EVENT_T1DOC_CMNT_beforeInsert
void T1DOC_CMNT_beforeInsert(VSRow newRow ,VSOutParam cancel )
{
   //Write Event Code below this line
   newRow.getData(ATTR_DOC_CD).setString(msDocCd);
   newRow.getData(ATTR_DOC_ID).setString(msDocId);
   newRow.getData(ATTR_DOC_DEPT_CD).setString(msDocDeptCd);
   newRow.getData(ATTR_DOC_VERS_NO).setString(msDocVersNo);
   newRow.getData(ATTR_DOC_PHASE_CD).setString(msDocPhaseCd);

   String[] loParms = new String[5];
   loParms[0] = msDocCd;
   loParms[1] = msDocId;
   loParms[2] = msDocDeptCd;
   loParms[3] = msDocVersNo;
   loParms[4] = msDocPhaseCd;

   try
   {
      //set session property to carry the document identifier to the data object later
      String lsMsgTxt = AMSHyperlinkActionElement.createMessageText(loParms);
      getParentApp().getSession().getORBSession().setProperty(SESSION_DOC_CMNT_MSG_TXT, lsMsgTxt);
   }
   catch (java.rmi.RemoteException loE)
   {
      raiseException( "Unable to set message properties", SEVERITY_LEVEL_SEVERE) ;
      return;
   }
}
//END_EVENT_T1DOC_CMNT_beforeInsert}}
//{{EVENT_pDOC_CMNT_beforeActionPerformed
void pDOC_CMNT_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
   String lsAction = ae.getAction();
   if (lsAction.equals(String.valueOf(AMSHyperlinkActionElement.ACTN_CLOSE)))
   {
      /*
       * This method does a nesting to obtain the parent document page,
       * since the immediate parent may not be the document page.
       */
      VSPage  loSrcPage = AMSPage.getDocumentPage(this);

      // prior to close, set source page to refresh
      if (loSrcPage != null && loSrcPage instanceof AMSDocTabbedPage)
      {
         ((AMSDocTabbedPage)loSrcPage).setGenTabPage(true);
         loSrcPage.getRootDataSource().getCurrentRow().refresh(true);
      }
      resetSessionProp();
   }
}
//END_EVENT_pDOC_CMNT_beforeActionPerformed}}
//{{EVENT_pDOC_CMNT_beforeGenerate
void pDOC_CMNT_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
   //Write Event Code below this line
   if (mboolFirstTime)
   {
      //if first time generate, check for any suppressed comments on current
      //and previous versions of the document.  Display info msg to user if yes.

      /*
       * This method does a nesting to obtain the parent document page,
       * since the immediate parent may not be the document page.
       */
      VSPage  loSrcPage = AMSPage.getDocumentPage(this);

      //BGN ADVHR00081106
      if(loSrcPage != null)
      {	  
	      VSRow loVSRow = loSrcPage.getRootDataSource().getCurrentRow();
	
	      SearchRequest loSR = new SearchRequest();
	      StringBuffer loWhereClause = new StringBuffer();
	
	      //Add an additional where clause to retrieve all comments for the document
	      loWhereClause.append(ATTR_DOC_CD);
	      loWhereClause.append(" = '");
	      loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(loVSRow.getData(ATTR_DOC_CD).getString()));
	      loWhereClause.append("' AND ");
	      loWhereClause.append(ATTR_DOC_DEPT_CD);
	      loWhereClause.append(" = '");
	      loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(loVSRow.getData(ATTR_DOC_DEPT_CD).getString()));
	      loWhereClause.append("' AND ");
	      loWhereClause.append(ATTR_DOC_ID);
	      loWhereClause.append(" = '");
	      loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(loVSRow.getData(ATTR_DOC_ID).getString()));
	      loWhereClause.append("' AND ");
	      loWhereClause.append(ATTR_DOC_VERS_NO);
	      loWhereClause.append(" <= ");
	      loWhereClause.append(loVSRow.getData(ATTR_DOC_VERS_NO).getInt());
	      loWhereClause.append(" AND ");
	      loWhereClause.append (ATTR_DOC_CMNT_SUPRS_FL);
	      loWhereClause.append(" = 1");
	
	      loSR.add( loWhereClause.toString());
	      VSQuery loQuery = new VSQuery(getParentApp().getSession(),"DOC_CMNT", loSR, null);
	      VSResultSet loRS = null;
	      try
	      {
	         loRS = loQuery.execute();
	         loRS.last();
	         if(loRS.getRowCount() > 0)
	         {
	            raiseException (C_MSG_SUPPRESSED_CMNT_EXISTS, SEVERITY_LEVEL_INFO); //Document has suppressed comments
	         }
	      }
	      finally
	      {
	         if (loRS!=null)
	         {
	            loRS.close();
	         }
	      }
	      AMSTextContentElement loDocCdElement = (AMSTextContentElement) getElementByName("txtRSPrefixDOC_CD");
	      AMSTextContentElement loDocDeptCdElement = (AMSTextContentElement) getElementByName("txtRSPrefixDOC_DEPT_CD");
	      AMSTextContentElement loDocIdElement = (AMSTextContentElement) getElementByName("txtRSPrefixDOC_ID");
	      if (loDocCdElement != null)
	      {
	         loDocCdElement.setText (loVSRow.getData(ATTR_DOC_CD).getString());
	      }
	      if (loDocDeptCdElement != null)
	      {
	         loDocDeptCdElement.setText (loVSRow.getData(ATTR_DOC_DEPT_CD).getString());
	      }
	      if (loDocIdElement != null)
	      {
	         loDocIdElement.setText (loVSRow.getData(ATTR_DOC_ID).getString());
	      }
      }
      //END ADVHR00081106
      //Set first time to false
      mboolFirstTime = false;
   } /* end if (mboolFirstTime) */
}
//END_EVENT_pDOC_CMNT_beforeGenerate}}
//{{EVENT_pDOC_CMNT_afterPageClose
void pDOC_CMNT_afterPageClose( PageEvent evt )
{
	 resetSessionProp();
	//Write Event Code below this line
}
//END_EVENT_pDOC_CMNT_afterPageClose}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1DOC_CMNT.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeInsert( DataSource obj, VSRow newRow ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1DOC_CMNT) {
			T1DOC_CMNT_beforeInsert(newRow ,cancel );
		}
	}
	public void afterPageClose( VSPage obj, PageEvent evt ){
		Object source = obj;
		if (source == this ) {
			pDOC_CMNT_afterPageClose( evt );
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
   * Sets document identiers into the page to be used during insert.
   *
   * @param fsDocCd Document code
   * @param fsDocId Document id
   * @param fsDocDeptCd Document department code
   * @param fsDocVersNo Document version number
   * @param fsDocPhaseCd Document phase code
   */
   public void setDocumentIdentifier(String fsDocCd, String fsDocId, String fsDocDeptCd,
                                       String fsDocVersNo, String fsDocPhaseCd)
   {
      msDocCd = fsDocCd;
      msDocId = fsDocId;
      msDocDeptCd = fsDocDeptCd;
      msDocVersNo = fsDocVersNo;
      msDocPhaseCd = fsDocPhaseCd;
   }
   private void resetSessionProp()
   {
       try
       {
           getParentApp().getSession().getORBSession().setProperty(SESSION_DOC_CMNT_MSG_TXT,"");
       }
       catch (java.rmi.RemoteException loE)
       {
           raiseException( "Unable to set message properties", SEVERITY_LEVEL_SEVERE) ;
           return;
       }
    }
}
