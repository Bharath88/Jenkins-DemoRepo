//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	import com.amsinc.gems.adv.vfc.html.* ;
	import com.amsinc.gems.adv.common.*;
	import org.apache.commons.logging.*;
	
	/*
	**  pR_WF_APRV_FLD*/
	
	//{{FORM_CLASS_DECL
	public class pR_WF_APRV_FLD extends pR_WF_APRV_FLDBase
	
	//END_FORM_CLASS_DECL}}
	{
	   private static Log moLog = AMSLogger.getLog( pR_WF_APRV_FLD.class,
	         AMSLogConstants.FUNC_AREA_WORKFLOW ) ;
	
	     /**
	      * The document component column name
	      */
	   public final static String DOC_COMP_COL = "DOC_COMP" ;
	
	     /**
	      * The document field column name
	      */
	   public final static String DOC_FLD_COL = "DOC_FLD" ;
	
	   private boolean mboolShowDocFieldText = false ;
	   private DivElement moDocFldComboLabel = null ;
	   private DivElement moDocFldTextLabel  = null ;
	   private DivElement moDocFldCombo      = null ;
	   private DivElement moDocFldText       = null ;
	
	
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pR_WF_APRV_FLD ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }
	
	
	
	//{{EVENT_CODE
	//{{EVENT_pR_WF_APRV_FLD_beforeActionPerformed
void pR_WF_APRV_FLD_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
   //Write Event Code below this line
   String lsAction = ae.getAction();

   if ( lsAction != null )
   {
      if ( lsAction.equals( String.valueOf( AMSHyperlinkActionElement.APRV_FLD_COMBOBOX ) ) )
      {
         //The following line will flush the pending changes to the result set.
         acceptData(preq);
         evt.setNewPage(this);
         evt.setCancel(true);
      }
   }
}
//END_EVENT_pR_WF_APRV_FLD_beforeActionPerformed}}

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
			pR_WF_APRV_FLD_beforeActionPerformed( ae, evt, preq );
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
	
	      public String generate()
	      {
	         myBeforeGenerate() ;
	         showDocField() ;
	         return super.generate() ;
	      }
	
	    /**
	     * This method gets the current row, and retrieves doc code and doc component.
	     *
	     * @author Mark Farrell
	     */
	
	      private void myBeforeGenerate()
	      {
	          String     lsDocValue     = null ;
	          String     lsDocCompValue = null ;
	          String     lsCheckComp    = null ;
	          String     lsDocCode      = null ;
	          DataSource loDataSource   = getRootDataSource() ;
	          VSRow      loRow          = loDataSource.getCurrentRow();
	          VSData     loDocComp      = null ;
	          VSSession  loSession      = loDataSource.getSession() ;
	
	          mboolShowDocFieldText = false ;
	          try
	          {
	             if ( loRow != null )
	             {
	                loDocComp = loRow.getData( DOC_COMP_COL ) ;
	                lsDocCode = loRow.getData( AMSCommonConstants.ATTR_DOC_CD ).getString() ;
	             } /* if ( loRow != null ) */
	          }
	          catch (RuntimeException loR_WF_APRV_FLDException)
	          {
	              raiseException("Error retrieving field information",
	                             SEVERITY_LEVEL_ERROR);
	              return;
	          } /* end try - catch */
	
	          AMSComboBoxElement loComboBox =
	                (AMSComboBoxElement) getElementByName("txtT1DOC_FLD");
	
	          if (loComboBox != null)
	          {
	             loComboBox.setIgnoreSetValue(true);
	
	             loComboBox.removeAllElements() ;
	
	             if ( loDocComp != null)
	             {
	                /* If the document component is nothing (length is zero) */
	                /* then it is assumed that the user is entering new rec. */
	                lsCheckComp = loDocComp.getString() ;
	                if ((lsCheckComp == null) || (lsCheckComp.length() == 0))
	                {
	                   return ;
	                }
	                else
	                {
	                   createComboBoxValues( loSession, loComboBox,
	                                         lsCheckComp, lsDocCode ) ;
	                } /* end (lsCheckComp.length() == 0 ) */
	             } /* if ( loDocComp != null) */
	
	             loComboBox.setIgnoreSetValue(false);
	          } /* if (loComboBox != null) */
	      }
	
	   /**
	    * This method fills the combo box.  It will search the document component
	    * data object pass as fsDOC_COMP, and populate the combo box with the name
	    * of the columns in this data object, as well as the captions for each column.
	    *
	    * @param VSSession foSession - the current session
	    * @param ComboBoxElement foComboBox - the combo box from html page
	    * @param String fsDOC_COMP - the document component
	    *
	    * @author Mark Farrell
	    */
	   public void createComboBoxValues( VSSession foSession,
	         ComboBoxElement foComboBox, String fsDocComp, String fsDocCode )
	   {
	      String       lsCaption ;
	      String       lsName ;
	      Option       loOption ;
	      VSQuery      loQuery ;
	      VSResultSet  loRsltSet = null ;
	      StringBuffer lsbWhere  = new StringBuffer( 64 ) ;
	      int          liNumRows ;
	      VSRow        loRow ;
	
	      try
	      {
	         foComboBox.removeAllElements() ;
	       //Add blank value to be displayed as the first item in the dropdown box
	         foComboBox.addElement( "" );
	
	         lsbWhere.append( "DOC_CD" ) ;
	         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( fsDocCode, AMSSQLUtil.EQUALS_OPER ) ) ;
	         lsbWhere.append( " AND DOC_COMP" ) ;
	         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( fsDocComp, AMSSQLUtil.EQUALS_OPER ) ) ;
	         loQuery = new VSQuery( foSession, "R_WF_XML_DATA", lsbWhere.toString(), "DOC_FLD_NM_UP, DOC_FLD" ) ;
	         loRsltSet = loQuery.execute() ;
	         loRsltSet.last() ;
	
	         liNumRows = loRsltSet.getRowCount() ;
	
	         /*
	          * For each column, get the name and the caption
	          * to populate the combo box
	          */
	          for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ )
	          {
	             loRow = loRsltSet.getRowAt( liRowCtr ) ;
	             lsName = loRow.getData( "DOC_FLD" ).getString() ;
	             lsCaption = loRow.getData( "DOC_FLD_NM" ).getString() ;
	             loOption = foComboBox.addElement( lsName + " - " + lsCaption, lsName ) ;
	          } /* end for (liCount=1; liCount<liColumnCount; liCount++) */
	      } /* end try */
	      catch( RuntimeException foExp )
	      {
	         raiseException( "Error creating list of fields", SEVERITY_LEVEL_ERROR ) ;
	         return;
	      } /* end catch( RuntimeException foExp ) */
	      finally
	      {
	         if ( loRsltSet != null )
	         {
	            loRsltSet.close() ;
	         } /* end if ( loRsltSet != null ) */
	      } /* end finally */
	   } /* end createComboBoxValues() */
	
	   /**
	    * This method returns true so that changing the document component
	    * will cause an accept data.
	    */
	   public boolean acceptDataOnSyncCurrency( DataSource foDSSync, PLSRequest foPLSReq )
	   {
	      return true ;
	   } /* end acceptDataOnSyncCurrency() */
	
	   /**
	    * This method attempts to determine if the document component
	    * name passed in is possibly a valid document component.  It
	    * verifies this by seeing if the component name begins with the
	    * document type followed by "_DOC_".  The actual validation
	    * is then done on the server side.
	    *
	    * @param fsDocComp The user-entered document component
	    * @param foSession The user's session
	    */
	   private boolean isPossibleDocComponent( String fsDocComp, String fsDocCode,
	         VSSession foSession )
	   {
	      if ( ( fsDocCode != null ) && ( fsDocCode.trim().length() > 0 ) )
	      {
	         VSQuery     loQuery ;
	         VSResultSet loRsltSet = null ;
	
	         loQuery = new VSQuery( foSession, "R_GEN_DOC_CTRL",
	               AMSCommonConstants.ATTR_DOC_CD + "="
	               + AMSSQLUtil.getANSIQuotedStr( fsDocCode, true ), "" ) ;
	
	         try
	         {
	            VSRow  loDocRow ;
	            String lsDocType ;
	            String lsPrefix ;
	
	            loRsltSet = loQuery.execute() ;
	            loRsltSet.last() ;
	            loDocRow = loRsltSet.current() ;
	            if ( loDocRow == null )
	            {
	               raiseException( "Document control record not found for " + fsDocCode,
	                     SEVERITY_LEVEL_ERROR ) ;
	               return false ;
	            } /* end if ( loDocRow == null ) */
	            lsDocType = loDocRow.getData( AMSCommonConstants.ATTR_DOC_TYP ).getString() ;
	            lsPrefix = lsDocType + "_DOC_" ;
	            if ( ( fsDocComp.startsWith( lsPrefix ) ) && ( !fsDocComp.equals( lsPrefix ) ) )
	            {
	               return true ;
	            } /* end if ( ( fsDocComp.startsWith( lsPrefix ) && ( !fsDocComp.equals( lsPrefix ) ) ) */
	            else
	            {
	               raiseException( "Document component is invalid",
	                     SEVERITY_LEVEL_ERROR ) ;
	               return false ;
	            } /* end else */
	         } /* end try */
	         finally
	         {
	            if ( loRsltSet != null )
	            {
	               loRsltSet.close() ;
	            } /* end if ( loRsltSet != null ) */
	         } /* end finally */
	      } /* end if ( ( fsDocCode != null ) && ( fsDocCode.trim().length() > 0 ) ) */
	      else
	      {
	         raiseException( "A document code must be specifed to retrieve the component information",
	               SEVERITY_LEVEL_ERROR ) ;
	         return false ;
	      } /* end else */
	   } /* end isPossibleDocComponent() */
	
	   /**
	    * This method shows the correct document field input
	    * element and label (combo box or text input).
	    */
	   private void showDocField()
	   {
	      if ( moDocFldText == null )
	      {
	         moDocFldComboLabel = (DivElement)getElementByName( "DocFldComboLabel" ) ;
	         moDocFldTextLabel  = (DivElement)getElementByName( "DocFldTextLabel" ) ;
	         moDocFldCombo      = (DivElement)getElementByName( "DocFldCombo" ) ;
	         moDocFldText       = (DivElement)getElementByName( "DocFldText" ) ;
	      } /* end if ( moDocFldText == null ) */
	
	      moDocFldComboLabel.setVisible( !mboolShowDocFieldText ) ;
	      moDocFldTextLabel.setVisible( mboolShowDocFieldText ) ;
	      moDocFldCombo.setVisible( !mboolShowDocFieldText ) ;
	      moDocFldText.setVisible( mboolShowDocFieldText ) ;
	   } /* end showDocField() */
	
	}
	