//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import java.util.* ;
import com.amsinc.gems.adv.client.dbitem.* ;
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.*;
import java.rmi.*;
import org.apache.commons.logging.*;


/*
**  BFNavPanel*/

//{{FORM_CLASS_DECL
public class BFNavPanel extends BFNavPanelBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form
   private static final String REPLACE_MARKER_NAME = "BF_NAV_TREE" ;

   private String                  msCurrWkspID      = null ;
   private String                  msCurrWkspNm      = null ;
   private Vector                  mvWkspBAs         = null ;
   private AMSBusinessArea         moCurrBA          = null ;
   private AMSBusinessFunction     moCurrBF          = null ;
   private boolean                 mboolLoadBFPage   = true ;
   private boolean                 mboolShowNavPanel = true ;
   private boolean                 mboolShowSaveAll  = false ;
   private boolean                 mboolShowRestart  = false ;
   private boolean                 mboolShowSave     = false ;

   /** This is the logger object for the class */
   private static Log moLog = AMSLogger.getLog( BFNavPanel.class,
         AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;



   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public BFNavPanel ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
         setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
         setADVUIType("PageStdUI");
   }



//{{EVENT_CODE
//{{EVENT_BFNavPanel_beforeActionPerformed
void BFNavPanel_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
}
//END_EVENT_BFNavPanel_beforeActionPerformed}}

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
			BFNavPanel_beforeActionPerformed( ae, evt, preq );
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

   public AMSBusinessArea getCurrBA()
   {
      return moCurrBA ;
   } /* end getCurrBA() */

   public Vector getWkspBAs()
   {
      return mvWkspBAs ;
   } /* end getCurrBA() */

   private void setCurrBA( String fsBAID )
   {
      moCurrBA = null ;
      if ( ( fsBAID == null ) || ( fsBAID.length() == 0 ) || ( mvWkspBAs == null ) )
      {
         return ;
      } /* end if ( ( fsBAID == null ) || ( fsBAID.length() == 0 ) || ( mvWkspBAs == null ) ) */
      else
      {
         int liNumBAs = mvWkspBAs.size() ;

         for ( int liBACtr = 0 ; liBACtr < liNumBAs ; liBACtr++ )
         {
            AMSBusinessArea loBA ;

            loBA = (AMSBusinessArea)mvWkspBAs.elementAt( liBACtr ) ;
            if ( loBA != null )
            {
               if ( fsBAID.equals( String.valueOf( loBA.getID() ) ) )
               {
                  moCurrBA = loBA ;
                  return ;
               } /* end if ( fsBAID.equals( String.valueOf( loBA.getID() ) ) ) */
            } /* end if ( loBA != null ) */
         } /* end for ( int liBACtr = 0 ; liBACtr < liNumBAs ; liBACtr++ ) */
      } /* end else */
   } /* end setCurrBA() */

   public AMSBusinessFunction getCurrBF()
   {
      return moCurrBF ;
   } /* end getCurrBF() */

   private void setCurrBF( String fsBFID )
   {
      moCurrBF = null ;
      if ( ( fsBFID == null ) || ( fsBFID.length() == 0 ) || ( moCurrBA == null ) )
      {
         return ;
      } /* end if ( ( fsBFID == null ) || ( fsBFID.length() == 0 ) || ( moCurrBA == null ) ) */
      else
      {
         Vector loBFs = moCurrBA.getBFs() ;
         int    liNumBFs ;

         if ( loBFs == null )
         {
            return ;
         } /* end if ( loBFs == null ) */

         liNumBFs = loBFs.size() ;

         for ( int liBFCtr = 0 ; liBFCtr < liNumBFs ; liBFCtr++ )
         {
            AMSBusinessFunction loBF ;

            loBF = (AMSBusinessFunction)loBFs.elementAt( liBFCtr ) ;
            if ( loBF != null )
            {
               if ( fsBFID.equals( String.valueOf( loBF.getID() ) ) )
               {
                  moCurrBF = loBF ;
                  return ;
               } /* end if ( fsBFID.equals( String.valueOf( loBF.getID() ) ) ) */
            } /* end if ( loBF != null ) */
         } /* end for ( int liBFCtr = 0 ; liBFCtr < liNumBFs ; liBFCtr++ ) */
      } /* end else */
   } /* end setCurrBF() */

   private void setCurrBFItem( String lsSeqNo )
   {
      int                     liSeqNo = 0 ;
      AMSBusinessFunctionItem loBFItem ;

      try
      {
         liSeqNo = Integer.parseInt( lsSeqNo ) ;
      } /* end try */
      catch( Exception foExp )
      {
         liSeqNo = 0 ;
      } /* end catch( Exception foExp ) */
      loBFItem = AMSBusinessFunction.getCurrBFItem( moCurrBF ) ;
      if ( loBFItem != null )
      {
         loBFItem.setSaveState( 1 ) ; // mark it as visited
      } /* end if ( loBFItem != null ) */
      if ( moCurrBF != null )
      {
         loBFItem = moCurrBF.getBFItem( liSeqNo ) ;
         if ( loBFItem != null )
         {
            loBFItem.setSaveState( 2 ) ; // mark it as current
         } /* end if ( loBFItem != null ) */
      } /* end if ( moCurrBF != null ) */
   } /* end setCurrBFItem() */

   public void doNotLoadBFPage()
   {
      mboolLoadBFPage = false ;
   } /* end doNotLoadBFPage() */

   public String doAction( PLSRequest foPLSRequest )
   {
      String lsBAID = foPLSRequest.getParameter( "CurrBAID" ) ;

      mboolShowSaveAll = foPLSRequest.getParameter( "ShowSaveAll" ).equals( "true" ) ;
      mboolShowRestart = foPLSRequest.getParameter( "ShowRestart" ).equals( "true" ) ;
      mboolShowSave    = foPLSRequest.getParameter( "ShowSave" ).equals( "true" ) ;

      try
      {
         resetProcessingSessionProperties( getParentApp().getSession() ) ;
      }
      catch (RemoteException loExcep)
      {
         moLog.error( "An error was encountered while resetting Session Properties", loExcep) ;
      } /* end catch (RemoteException loExcep) */

      if ( lsBAID != null )
      {
         String lsBFID        = foPLSRequest.getParameter( "CurrBFID" ) ;
         String lsBFItemSeqNo = foPLSRequest.getParameter( "BFItemSeqNo" ) ;

         setCurrBA( lsBAID ) ;
         setCurrBF( lsBFID ) ;
         setCurrBFItem( lsBFItemSeqNo ) ;
         return generate() ;
      } /* end if ( lsBAID != null ) */
      else
      {
         String lsClose = foPLSRequest.getParameter( "CloseBFNavPanel" ) ;

         if ( ( lsClose != null ) && ( lsClose.length() > 0 ) )
         {
            mboolShowNavPanel = false ;
            msCurrWkspID      = null ;
            msCurrWkspNm      = null ;
            mvWkspBAs         = null ;
            moCurrBA          = null ;
            moCurrBF          = null ;
            super.setWorkspace( null, null ) ;
            return generate() ;
         } /* end if ( ( lsClose != null ) && ( lsClose.length() > 0 ) ) */
         else
         {
            String lsRefreshAppNav =
                        foPLSRequest.getParameter( "RefreshAppNav" ) ;

            if ( lsRefreshAppNav != null && lsRefreshAppNav.length() > 0 )
            {
               PLSORBSessionImpl loSession =
                  (PLSORBSessionImpl) getParentApp().getSession();
               SecondaryNavigator loSeconNav =
                  (SecondaryNavigator) getParentApp().getPageFromList(
                  loSession.getAppProperty( SecondaryNavigator.PAGE_NAME ) );

               if (loSeconNav != null)
               {
                  return loSeconNav.generate();
               } /* end if (loSeconNav != null) */
            } /* end if ( lsRefreshAppNav != null && ... */
         } /* end else ( ( lsClose != null ) &&  ... */
      } /* end else */

      return super.doAction( foPLSRequest ) ;
   } /* End of doAction() */

   public String generate()
   {
      /* JADE bug workaround */
      targetFrame = "_self" ;
      /* end workaround */

      try
      {
         AMSStartupPage loStartup    = AMSStartupPage.getStartupPage( getParentApp() ) ;
         String         lsCurrWkspID ;

         lsCurrWkspID = loStartup.getCurrentWorkspaceID() ;
         if ( ( lsCurrWkspID != null ) && ( !lsCurrWkspID.equals( msCurrWkspID ) ) )
         {
            msCurrWkspID = lsCurrWkspID ;
            msCurrWkspNm = loStartup.getCurrentWorkspaceName() ;
            mvWkspBAs    = null ;
            moCurrBA     = null ;
            moCurrBF     = null ;
            super.setWorkspace( msCurrWkspID, msCurrWkspNm ) ;
         } /* end if ( ( lsCurrWkspID != null ) && ( !lsCurrWkspID.equals( msCurrWkspID ) ) ) */
      } /* end try */
      catch( Exception foExp )
      {}

      if ( mboolShowNavPanel )
      {
         appendOnloadString( "UTILS_ShowBFNavPanel();" ) ;
         buildNavTree() ;
      } /* end if ( mboolShowNavPanel ) */
      else
      {
         mboolShowNavPanel = true ;
      } /* end else */
      if ( mboolShowSaveAll )
      {
         appendOnloadString( "BFNAVPANEL_ShowSaveAll();" ) ;
      } /* end if ( mboolShowSaveAll ) */
      if ( mboolShowRestart )
      {
         appendOnloadString( "BFNAVPANEL_ShowRestart();" ) ;
      } /* end if ( mboolShowRestart ) */
      if ( mboolShowSave )
      {
         appendOnloadString( "BFNAVPANEL_ShowSave();" ) ;
      } /* end if ( mboolShowSave ) */

      setPin( true ) ;
      return super.generate() ;
   } /* end generate() */

   private void buildNavTree()
   {
      TextContentElement loTCE   = (TextContentElement)getElementByName( REPLACE_MARKER_NAME ) ;

      try
      {
         int liNumBAs ;

         if ( mvWkspBAs == null )
         {
            mvWkspBAs = AMSBusinessArea.getWkspBATree( getParentApp().getSession(),
                           Long.parseLong( msCurrWkspID ) ) ;
         } /* end if ( mvWkspBAs == null ) */
         writeNavTree( loTCE ) ;
      } /* end try */
      catch( Exception foExp )
      {
         if ( loTCE != null )
         {
            loTCE.setValue( "" ) ;
         } /* end if ( loTCE != null ) */
      } /* end catch( Exception foExp ) */
   } /* end buildNavTree() */

   private void writeNavTree( TextContentElement foTCE )
   {
      if ( mvWkspBAs == null )
      {
         foTCE.setValue( "" ) ;
         appendOnloadString( "setTimeout(\"submitForm(document.BFNavPanel,\'BADtlsTrans=BADtlsTrans&\',\'Display\')\",0);" ) ;
         return ;
      } /* end if ( mvWkspBAs != null ) */
      else
      {
         StringBuffer lsbNavTree = new StringBuffer( 500 ) ;
         int          liNumBAs   = mvWkspBAs.size() ;

         lsbNavTree.append( "<script type=\"text/javascript\" language=\"JavaScript\">\n" ) ;
         lsbNavTree.append( "<!--\n" ) ;

         if ( msCurrWkspNm != null )
         {
            lsbNavTree.append( "msBFTREE_WkspNm=\"" ) ;
            lsbNavTree.append( msCurrWkspNm ) ;
            lsbNavTree.append( "\"\n" ) ;
         } /* end if ( msCurrWkspNm != null ) */
         else
         {
            lsbNavTree.append( "msBFTREE_WkspNm=\"Workspace\"\n" ) ;
         } /* end else */

         for ( int liBACtr = 0 ; liBACtr < liNumBAs ; liBACtr++ )
         {
            AMSBusinessArea loBA ;

            loBA = (AMSBusinessArea)mvWkspBAs.elementAt( liBACtr ) ;
            if ( loBA != null )
            {
               lsbNavTree.append( "msBFTREE_BAs[" + liBACtr + "]= new Array(" ) ;
               lsbNavTree.append( loBA.getID() + "," ) ;
               lsbNavTree.append( "\'" + handleQuotedString( loBA.getName() ) + "\'," ) ;
               lsbNavTree.append( "\'" + handleQuotedString( loBA.getDesc() ) + "\'," ) ;
               lsbNavTree.append( "null);\n" ) ;

               if ( ( moCurrBA == null ) || ( moCurrBA == loBA ) )
               {
                  if ( moCurrBF == null )
                  {
                     appendOnloadString( "setTimeout(\"submitForm(document.BFNavPanel,\'BADtlsTrans=BADtlsTrans&\',\'Display\')\",0);" ) ;
                  } /* end if ( moCurrBA == null ) */
                  moCurrBA = loBA ;
                  writeBFTree( loBA, lsbNavTree, liBACtr ) ;
               } /* end if ( ( moCurrBA == null ) || ( moCurrBA == loBA ) ) */
            } /* end if ( loBA != null ) */
         }/* end for ( int liBACtr = 0 ; liBACtr < liNumBAs ; liBACtr++ ) */

         lsbNavTree.append( "-->\n" ) ;
         lsbNavTree.append( "</script>\n" ) ;

         foTCE.setValue( lsbNavTree.toString() ) ;
      } /* end else */

   } /* end writeNavTree() */

   private void writeBFTree( AMSBusinessArea foBA, StringBuffer fsbNavTree,
                             int fiBACtr )
   {
      int liNumBFs = foBA.getNumBFs() ;

      fsbNavTree.append( "msBFTREE_BAs[" + fiBACtr + "][3]= new Array();\n" ) ;
      for ( int liBFCtr = 0 ; liBFCtr < liNumBFs ; liBFCtr++ )
      {
         AMSBusinessFunction loBF ;

         loBF = foBA.getBF( liBFCtr ) ;
         if ( loBF != null )
         {
            fsbNavTree.append( "msBFTREE_BAs[" + fiBACtr + "][3][" + liBFCtr + "]= new Array(" ) ;
            fsbNavTree.append( loBF.getID() + "," ) ;
            fsbNavTree.append( "\'" + handleQuotedString( loBF.getName() ) + "\'," ) ;
            fsbNavTree.append( "\'" + handleQuotedString( loBF.getDesc() ) + "\'," ) ;
            if ( moCurrBF == loBF )
            {
               fsbNavTree.append( "true," ) ;
            } /* end if ( moCurrBF == loBF ) */
            else
            {
               fsbNavTree.append( "false," ) ;
            } /* end else */
            fsbNavTree.append( "null);\n" ) ;
            writeBFItemTree( loBF, fsbNavTree, fiBACtr, liBFCtr ) ;
         } /* end if ( loBF != null ) */
      }/* end for ( int liBFCtr = 0 ; liBFCtr < liNumBFs ; liBFCtr++ ) */
   } /* end writeBFTree() */

   private void writeBFItemTree( AMSBusinessFunction foBF, StringBuffer fsbNavTree,
                                 int fiBACtr, int fiBFCtr )
   {
      int     liNumBFItems   = foBF.getNumBFItems() ;
      boolean lboolFoundCurr = false ;

      fsbNavTree.append( "msBFTREE_BAs[" + fiBACtr + "][3][" + fiBFCtr + "][4]= new Array();\n" ) ;
      for ( int liBFItemCtr = 0 ; liBFItemCtr < liNumBFItems ; liBFItemCtr++ )
      {
         AMSBusinessFunctionItem loBFItem ;

         loBFItem = foBF.getBFItem( liBFItemCtr ) ;
         if ( loBFItem != null )
         {
            fsbNavTree.append( "msBFTREE_BAs[" + fiBACtr + "][3][" + fiBFCtr
                               + "][4][" + liBFItemCtr + "]= new Array(" ) ;
            fsbNavTree.append( loBFItem.getID() + "," ) ;
            fsbNavTree.append( "\'" + handleQuotedString( loBFItem.getName() ) + "\'," ) ;
            fsbNavTree.append( loBFItem.getSaveState() + "," ) ;
            fsbNavTree.append( "\'" + handleQuotedString( loBFItem.getTargetFrame() ) + "\');\n" ) ;
            if ( ( foBF == moCurrBF ) && ( loBFItem.getSaveState() == 2 ) )
            {
               lboolFoundCurr = true ;
               if ( mboolLoadBFPage )
               {
                  appendOnloadString( "BFNAVPANEL_OpenBFPage(" +  liBFItemCtr
                        + ",'" + loBFItem.getTargetFrame() + "');" ) ;
               } /* end if ( mboolLoadBFPage ) */
               else
               {
                  mboolLoadBFPage = true ;
               } /* end else */
            } /* end if ( ( foBF == moCurrBF ) && ( loBFItem.getSaveState() == 2 ) ) */
         } /* end if ( loBFItem != null ) */
      }/* end for ( int liBFItemCtr = 0 ; liBFItemCtr < liNumBFItems ; liBFItemCtr++ ) */
      if ( ( foBF == moCurrBF ) && ( !lboolFoundCurr ) )
      {
         if ( mboolLoadBFPage )
         {
            appendOnloadString( "BFNAVPANEL_OpenBFPage(0,null);" ) ;
         } /* end if ( mboolLoadBFPage ) */
         else
         {
            mboolLoadBFPage = true ;
         } /* end else */
      } /* end if ( ( foBF == moCurrBF ) && ( !lboolFoundCurr ) ) */
   } /* end writeBFItemTree() */

   /**
    * Overrides this method to do nothing as it is always the initial
    * setter of the workspace.
    *
    * @param fsWorkspaceID The workspace id
    * @param fsWorkspaceID The workspace Nm
    */
   protected void setWorkspace( String fsWorkspaceID, String fsWorkspaceNm )
   {
   } /* end setWorkspace() */

} /* end class BFNavPanel */
