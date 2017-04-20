//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	import java.util.Vector ;
	import com.amsinc.gems.adv.common.AMSSecurity ;
	import com.amsinc.gems.adv.common.*;
	import org.apache.commons.logging.*;
	
	/*
	**  pR_SC_RSRC_ACCS*/
	
	//{{FORM_CLASS_DECL
	public class pR_SC_RSRC_ACCS extends pR_SC_RSRC_ACCSBase
	
	//END_FORM_CLASS_DECL}}
	{
	
	    private static Log moAMSLog = AMSLogger.getLog(
	            pR_SC_RSRC_ACCS.class,
	            AMSLogConstants.FUNC_AREA_SECURITY );
	
	
	   private static final int    MAX_ACTN_COLS         = 3 ;
	   private static final String APPL_ACTN_ELEM_PREFIX = "txtT1APPL_ACTN" ;
	   private static final String APPL_ACTN_ATTR_PREFIX = "APPL_ACTN" ;
	   private static final String APPL_ACTN_ATTR_SUFFIX = "_CD" ;
	   private static final long   LOW_BITS_MASK         = 4294967295L ;
	   private static final long   HIGH_BITS_MASK        = LOW_BITS_MASK << 32 ;
	
	   private int                  miTotActns          = 0 ;
	   private int[]                miApplActnCodes     = null ;
	   private int[]                miApplActnBitIdxs   = null ;
	   private String[]             msApplActnAttrNames = null ;
	   private String[]             msApplActnElemNames = null ;
	   private String[]             msApplActnCaptions  = null ;
	   private TextContentElement   moCustActnTCE       = null ;
	   private HiddenElement        moAprvLvlCd         = null ;
	   private HiddenElement        moCombActn1         = null ;
	   private HiddenElement        moCombActn2         = null ;
	   private boolean              mboolFirstGenerate  = true ;
	
	
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	
	//{{FORM_CLASS_CTOR
	public pR_SC_RSRC_ACCS ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	
	   }
	
	
	
	//{{EVENT_CODE
	//{{EVENT_pR_SC_RSRC_ACCS_pageCreated
/**
 * This method will be called when the page is newly created. It executes
 * the Query on the R_SC_DOC_CUST_ACTN table and fetches the records which are then
 * used to write out the HTML to the pR_SC_RSRC_ACCS page. The fields are added
 * on the fly and and the vectors are upon save combines into a single value to
 * be stored in the corresponding Application Actions colmns of the R_SC_RSRC_ACCS
 * table.
 *                 Modification Log: Mukund Mohan - 07/26/2001
 *                                                - Initial Version
 */
void pR_SC_RSRC_ACCS_pageCreated()
{
   if ( moAMSLog.isDebugEnabled() ) {
       moAMSLog.debug( "pR_SC_RSRC_ACCS_pageCreated()" );
   }

   VSQuery            loQuery ;
   VSResultSet        loRsltSet = null ;
   VSRow              loRow ;

   loQuery = new VSQuery( getParentApp().getSession(),
         "R_SC_DOC_CUST_ACTN", "","DOC_S_ACTN_CD" ) ;
   try
   {
      loRsltSet = loQuery.execute() ;
      loRsltSet.last() ;
      miTotActns = loRsltSet.getRowCount() ;

      if ( miTotActns > 0 )
      {
         int    liBitIdx   = 0 ;
         int    liAttrIdx  = 0 ;

         miApplActnCodes     = new int[miTotActns] ;
         miApplActnBitIdxs   = new int[miTotActns] ;
         msApplActnAttrNames = new String[miTotActns] ;
         msApplActnElemNames = new String[miTotActns] ;
         msApplActnCaptions  = new String[miTotActns] ;

         for ( int liActnCtr = 1 ; liActnCtr <= miTotActns ; liActnCtr++ )
         {
            loRow = loRsltSet.getRowAt( liActnCtr ) ;
            setCodeInfo( liActnCtr - 1, loRow ) ;
         } /* end for ( int liActnCtr = 1 ; liActnCtr <= miTotActns ; liActnCtr++ ) */
      } /* end if ( miTotActns > 0 ) */
   } /* end try */
   finally
   {
      if ( loRsltSet != null )
      {
         loRsltSet.close() ;
      } /* end if ( loRsltSet != null ) */
   } /* end finally */

}/* end pR_SC_RSRC_ACCS_pageCreated() */
//END_EVENT_pR_SC_RSRC_ACCS_pageCreated}}

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
			pR_SC_RSRC_ACCS_pageCreated();
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
	    * We override this method so we do not expand the
	    * initial section by default
	    */
	   protected void expandInitialSection()
	   {
	      // Do nothing
	      return ;
	   }
	
	
	   /**
	    * This method will re-construct the action/approval
	    * bit vector from the on page combo boxes
	    *
	    * @param foPLSReq The PLS request
	    * @param fsWhichCode The prefix of the checkbox names to combine
	    * @param fiHowMany The number of checkboxes for the code
	    * @return String The combined value
	    */
	   private String formCombCode( PLSRequest foPLSReq,
	         String fsWhichCode, int fiHowMany )
	   {
	      String lsParmNm  = null ;
	      String lsParmVal = null ;
	      long   llCombVal = 0 ;
	
	      for ( int liCtr = 1 ; liCtr <= fiHowMany ; liCtr++ )
	      {
	         lsParmNm  = fsWhichCode + Integer.toString( liCtr ) ;
	         lsParmVal = foPLSReq.getParameter( lsParmNm ) ;
	         if ( lsParmVal != null )
	         {
	            llCombVal = llCombVal + ( 1L << ( liCtr - 1 ) ) ;
	         } /* end if ( lsParmVal != null ) */
	      } /* end for ( int liCtr=1 ; liCtr <= fiHowMany ; liCtr++ ) */
	
	      return Long.toString( llCombVal ) ;
	   } /* end formCombCode() */
	
	   /**
	    * This method will re-construct the Application Action's of R_SC_RSRC_ACCS
	    * from the Sub-Action Checkboxes on the Page.
	    *
	    * @param foPLSReq The PLS request
	    */
	   private void formApplCode( PLSRequest foPLSReq )
	   {
	      VSRow loCurrRow = T1R_SC_RSRC_ACCS.getCurrentRow() ;
	
	      if ( loCurrRow != null )
	      {
	         VSData  loData ;
	         long    llCombVal ;
	
	         for ( int liActnCtr = 0 ; liActnCtr < miApplActnCodes.length ; liActnCtr++ )
	         {
	            loData    = loCurrRow.getData( msApplActnAttrNames[liActnCtr] ) ;
	            llCombVal = loData.getLong() ;
	
	            if ( foPLSReq.getParameter( msApplActnElemNames[liActnCtr] ) != null )
	            {
	               llCombVal = llCombVal | ( 1L << ( miApplActnBitIdxs[liActnCtr] - 1 ) ) ;
	            } /* end */
	            else
	            {
	               llCombVal = llCombVal & ( ~( 1L << ( miApplActnBitIdxs[liActnCtr] - 1 ) ) ) ;
	            } /* end else */
	
	            loData.setLong( llCombVal ) ;
	         } /* end for ( int liActnCtr = 0 ; liActnCtr < miApplActnCodes.length ; liActnCtr++ ) */
	      } /* end if ( loCurrRow != null ) */
	   } /* end formApplCode() */
	
	   private void setCodeInfo( int fiCodeIdx, VSRow foRow )
	   {
	      if ( moAMSLog.isDebugEnabled() ) {
	          moAMSLog.debug( "setCodeInfo()" );
	      }
	
	      int          liActnCode    = foRow.getData( "DOC_S_ACTN_CD" ).getInt() ;
	      int          liAttrIdx     = 0 ;
	      int          liBitIdx      = 0 ;
	      StringBuffer lsbElemName   = new StringBuffer( APPL_ACTN_ELEM_PREFIX ) ;
	      StringBuffer lsbAttrName   = new StringBuffer( APPL_ACTN_ATTR_PREFIX ) ;
	      String       lsAttrCaption = foRow.getData( "DOC_S_ACTN_DESC" ).getString() ;
	
	      /*
	       * We now must determine from which application action code attribute
	       * value the action authorization is derived and within that attribute
	       * the bit that represents the authorization.
	       */
	      if ( ( liActnCode >= AMSSecurity.APPL_ACTN_01_BGN ) &&
	           ( liActnCode <= AMSSecurity.APPL_ACTN_01_END ) )
	      {
	         liAttrIdx = 1 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_01_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_01_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_02_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_02_END ) )
	      {
	         liAttrIdx = 2 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_02_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_02_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_03_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_03_END ) )
	      {
	         liAttrIdx = 3 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_03_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_03_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_04_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_04_END ) )
	      {
	         liAttrIdx = 4 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_04_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_04_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_05_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_05_END ) )
	      {
	         liAttrIdx = 5 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_05_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_05_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_06_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_06_END ) )
	      {
	         liAttrIdx = 6 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_06_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_06_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_07_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_07_END ) )
	      {
	         liAttrIdx = 7 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_07_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_07_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_08_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_08_END ) )
	      {
	         liAttrIdx = 8 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_08_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_08_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_09_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_09_END ) )
	      {
	         liAttrIdx = 9 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_09_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_09_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_10_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_10_END ) )
	      {
	         liAttrIdx = 10 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_10_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_10_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_11_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_11_END ) )
	      {
	         liAttrIdx = 11 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_11_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_11_BGN ) && . . . */
	      else if ( ( liActnCode >= AMSSecurity.APPL_ACTN_12_BGN ) &&
	                ( liActnCode <= AMSSecurity.APPL_ACTN_12_END ) )
	      {
	         liAttrIdx = 12 ;
	         liBitIdx = liActnCode - AMSSecurity.APPL_ACTN_12_BGN + 1 ;
	      } /* end if ( ( liActnCode >= AMSSecurity.APPL_ACTN_12_BGN ) && . . . */
	
	
	      lsbElemName.append( liAttrIdx ) ;
	      if ( liBitIdx <= 32 )
	      {
	         lsbElemName.append( "A_CD_" ) ;
	         lsbElemName.append( liBitIdx ) ;
	      } /* end if ( liBitIdx <= 32 ) */
	      else
	      {
	         lsbElemName.append( "B_CD_" ) ;
	         lsbElemName.append( liBitIdx - 32 ) ;
	      } /* end else */
	
	      lsbAttrName.append( liAttrIdx ) ;
	      lsbAttrName.append( APPL_ACTN_ATTR_SUFFIX ) ;
	
	      miApplActnCodes[fiCodeIdx]     = liActnCode ;
	      miApplActnBitIdxs[fiCodeIdx]   = liBitIdx ;
	      msApplActnAttrNames[fiCodeIdx] = lsbAttrName.toString() ;
	      msApplActnElemNames[fiCodeIdx] = lsbElemName.toString() ;
	      msApplActnCaptions[fiCodeIdx]  = lsAttrCaption ;
	
	      if ( moAMSLog.isDebugEnabled() ) {
	          moAMSLog.debug( "\t variables set:" );
	          moAMSLog.debug( "\t\t liAttrIdx (index for APPL_ACTN)    = " + liAttrIdx );
	          moAMSLog.debug( "\t\t liBitIdx  (bit index in APPL_ACTN) = " + liBitIdx );
	          moAMSLog.debug( "\t\t miApplActnCodes["+fiCodeIdx+"]     = " + miApplActnCodes[fiCodeIdx] );
	          moAMSLog.debug( "\t\t miApplActnBitIdxs["+fiCodeIdx+"]   = " + miApplActnBitIdxs[fiCodeIdx] );
	          moAMSLog.debug( "\t\t msApplActnAttrNames["+fiCodeIdx+"] = " + msApplActnAttrNames[fiCodeIdx] );
	          moAMSLog.debug( "\t\t msApplActnElemNames["+fiCodeIdx+"] = " + msApplActnElemNames[fiCodeIdx] );
	          moAMSLog.debug( "\t\t msApplActnCaptions["+fiCodeIdx+"]  = " + msApplActnCaptions[fiCodeIdx] );
	      }
	
	   } /* end setCodeInfo() */
	
	   public void beforeGenerate()
	   {
	      if ( moAMSLog.isDebugEnabled() ) {
	          moAMSLog.debug( "beforeGenerate()" );
	      }
	
	      VSRow loCurrRow  = T1R_SC_RSRC_ACCS.getCurrentRow() ;
	
	      if ( mboolFirstGenerate )
	      {
	         mboolFirstGenerate = false ;
	         moCustActnTCE = (TextContentElement)getElementByName( "AMSApplicationSubActions" ) ;
	         moAprvLvlCd = (HiddenElement)getElementByName( "txtT1APRV_LVL_CD" ) ;
	         moCombActn1 = (HiddenElement)getElementByName( "txtT1COMB_ACTN1_CD" ) ;
	         moCombActn2 = (HiddenElement)getElementByName( "txtT1COMB_ACTN2_CD" ) ;
	      } /* end if ( mboolFirstGenerate ) */
	      if ( loCurrRow != null )
	      {
	         moAprvLvlCd.setValue( Long.toString( loCurrRow.getData("APRV_LVL_CD").getLong() ) ) ;
	         moCombActn1.setValue( Long.toString( loCurrRow.getData("COMB_ACTN1_CD").getLong() ) ) ;
	         moCombActn2.setValue( Long.toString( loCurrRow.getData("COMB_ACTN2_CD").getLong() ) ) ;
	      } /* end if ( loCurrRow != null ) */
	      else
	      {
	         moAprvLvlCd.setValue( "0" ) ;
	         moCombActn1.setValue( "0" ) ;
	         moCombActn2.setValue( "0" ) ;
	      } /* end else */
	
	      addCustomActions( loCurrRow ) ;
	   } /* end beforeGenerate() */
	
	   public void acceptData( PLSRequest foPLSReq )
	   {
	
	      if ( moAMSLog.isDebugEnabled() ) {
	          moAMSLog.debug( "acceptData()" );
	      }
	
	      VSRow  loCurrRow = T1R_SC_RSRC_ACCS.getCurrentRow() ;
	      VSData loData ;
	
	      if ( loCurrRow != null )
	      {
	         if ( moAMSLog.isDebugEnabled() ) {
	            moAMSLog.debug( "\t T1R_SC_RSRC_ACCS current row retrieved" );
	         }
	
	         /* Form the approval level code */
	         loData = loCurrRow.getData( "APRV_LVL_CD" ) ;
	         loData.setString( formCombCode( foPLSReq, "txtT1APRV_LVL_CD_", 15 ) ) ;
	         /* Form the comb actn 1 code */
	         loData = loCurrRow.getData( "COMB_ACTN1_CD" ) ;
	         loData.setString( formCombCode( foPLSReq, "txtT1COMB_ACTN1_CD_", 30 ) ) ;
	         /* Form the comb actn 2 code */
	         loData = loCurrRow.getData( "COMB_ACTN2_CD" ) ;
	         loData.setString( formCombCode( foPLSReq, "txtT1COMB_ACTN2_CD_", 30 ) ) ;
	         /* Add the ten application codes */
	         formApplCode( foPLSReq ) ;
	      } /* end if ( loCurrRow != null ) */
	      super.acceptData( foPLSReq ) ;
	   } /* end acceptData() */
	
	
	
	   private void addCustomActions( VSRow foCurrRow )
	   {
	      if ( moAMSLog.isDebugEnabled() ) {
	          moAMSLog.debug( "addCustomActions()" );
	      }
	
	      StringBuffer lsbHTML   = new StringBuffer( 1000 ) ;
	      int          liActnCtr = 0 ;
	      long         llCombVal ;
	
	      lsbHTML.append( "<tr>" ) ;
	
	      for ( liActnCtr = 1 ; liActnCtr <= miTotActns ; liActnCtr++ )
	      {
	         if ( ( liActnCtr % MAX_ACTN_COLS == 1 ) && ( liActnCtr != 1 ) )
	         {
	            /*
	             * If this is not the first row, then start a new row
	             * if we have gone past the maximum number of action
	             * columns per row.  The previous closing </tr> tag is
	             * added at the end of the loop from the previous action.
	             * The opening <tr> tag for the first row has already
	             * been added.
	             */
	            lsbHTML.append("<tr>");
	         } /* end if ( ( liActnCtr % MAX_ACTN_COLS == 1 ) && ( liActnCtr != 1 ) ) */
	
	         /*
	          * For all actions, create a new scalar check box for the user
	          * to edit the action authority for the resource/role combination.
	          */
	         lsbHTML.append( "<td class=\"label\"><label for=\"" ) ;
	         lsbHTML.append( msApplActnElemNames[liActnCtr - 1] ) ;
	         lsbHTML.append( "\" title=\"" ) ;
	         lsbHTML.append( msApplActnCaptions[liActnCtr - 1] ) ;
	         lsbHTML.append( "\">" ) ;
	         lsbHTML.append( msApplActnCaptions[liActnCtr - 1] ) ;
	         lsbHTML.append( " :</label></td><td class=\"val\"><input type=\"checkbox\" vsds=\"\" title=\"") ;
	         lsbHTML.append( msApplActnCaptions[liActnCtr - 1] ) ;
	         lsbHTML.append( "\" " ) ;
	         if ( ( getEditMode() ) && ( foCurrRow != null ) )
	         {
	            lsbHTML.append( "class=\"adveditable\" ") ;
	         } /* end if ( ( getEditMode() ) && ( foCurrRow != null ) ) */
	         else
	         {
	            lsbHTML.append( "class=\"advreadonly\" disabled=\"disabled\"") ;
	         } /* end else */
	         if ( foCurrRow != null )
	         {
	            llCombVal = foCurrRow.getData( msApplActnAttrNames[liActnCtr - 1] ).getLong() ;
	            if ( ( llCombVal & ( 1L << ( miApplActnBitIdxs[liActnCtr - 1] - 1 ) ) ) != 0L )
	            {
	               lsbHTML.append( "checked=\"checked\" " ) ;
	            } /* end if ( ( llCombVal & ( 1L << ( miApplActnBitIdxs[liActnCtr] - 1 ) ) ) != 0L ) */
	         } /* end if ( foCurrRow != null ) */
	         lsbHTML.append( "name=\"" ) ;
	         lsbHTML.append( msApplActnElemNames[liActnCtr - 1] ) ;
	         lsbHTML.append( "\" id=\"" ) ;
	         lsbHTML.append( msApplActnElemNames[liActnCtr - 1] ) ;
					 lsbHTML.append( "\" vsdf=\"" ) ;
	         lsbHTML.append( (msApplActnElemNames[liActnCtr - 1]).substring(5)) ;
	         lsbHTML.append( "\"></td>" ) ;
	
	         if ( moAMSLog.isDebugEnabled() ) {
	            moAMSLog.debug("\t msApplActnAttrNames["+ (liActnCtr - 1) + "] = " + msApplActnAttrNames[liActnCtr - 1] );
	            moAMSLog.debug("\t miApplActnBitIdxs["+ (liActnCtr - 1) + "]   = " + miApplActnBitIdxs[liActnCtr - 1] );
	            moAMSLog.debug("\t sifted value to be ANDed                    = " + ( 1L << ( miApplActnBitIdxs[liActnCtr - 1] - 1 ) ) );
	            moAMSLog.debug("\t msApplActnElemNames["+ (liActnCtr - 1) + "] = " + msApplActnElemNames[liActnCtr - 1] );
	            moAMSLog.debug("\t msApplActnCaptions["+ (liActnCtr - 1) + "]  = " + msApplActnCaptions[liActnCtr - 1] );
	            moAMSLog.debug("\t ----------------------------------------------------------");
	         }
	
	         /*
	          * If we have reached the maximum number of action
	          * columns per row, then close the table row with a
	          * </tr> tag.  The next opening <tr> tag will be added
	          * at the beginning of this loop if a new row should be
	          * added.
	          */
	         if ( ( liActnCtr % MAX_ACTN_COLS ) == 0 )
	         {
	            lsbHTML.append( "</tr>" ) ;
	         } /* end if ( ( liActnCtr % MAX_ACTN_COLS ) == 0 ) */
	      } /* end for ( liActnCtr = 1 ; liActnCtr <= miTotActns ; liActnCtr++ ) */
	
	      /*
	       * In the case the last row is not completely filled with actions,
	       * then we must add spacers for the remaining table definitions as
	       * well as the last </tr> tag.
	       */
	      if ( ( liActnCtr - 1 ) % MAX_ACTN_COLS != 0 )
	      {
	         int liNumPad = MAX_ACTN_COLS - ( ( liActnCtr - 1 ) % MAX_ACTN_COLS ) ;
	
	         for ( int liPad = 1 ; liPad <= liNumPad ; liPad++ )
	         {
	            lsbHTML.append( "<td></td><td></td>" ) ;
	         } /* end for ( int liPad = 1 ; liPad <= liNumPad ; liPad++ ) */
	
	         lsbHTML.append("</tr>");
	      } /* end if ( ( liActnCtr - 1 ) % MAX_ACTN_COLS != 0 ) */
	
	      moCustActnTCE.setPreserveHTMLTags( true ) ;
	      moCustActnTCE.setValue( lsbHTML.toString() ) ;
	   } /* end addCustomActions() */
	}