//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import com.amsinc.gems.adv.client.dbitem.*;
import com.amsinc.gems.adv.vfc.html.*;
import java.util.*;
import versata.vls.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML.*;
import javax.swing.text.AbstractDocument;

/*
**  HomePage
*/

//{{FORM_CLASS_DECL
public class HomePage extends HomePageBase

//END_FORM_CLASS_DECL}}


{
   // Declarations for instance variables used in the form


   private static final String REPLACE_MARKER_NAME  = "HOME_PAGE_WINDOWS" ;
   private static final String DIV_NAME_PREFIX      = "ADVHMPGWIN" ;
   private static final String TITLE_DIV_SUFFIX     = "TTL" ;
   private static final String TITLE_IFRAME_SUFFIX  = "TTLIF" ;
   private static final String BODY_DIV_SUFFIX      = "BDY" ;
   private static final String BODY_IFRAME_SUFFIX   = "BDYIF" ;
   private static final String FOOTER_DIV_SUFFIX    = "FTR" ;
   private static final String FOOTER_IFRAME_SUFFIX = "FTRIF" ;
   private static final String ICON_DIV_SUFFIX      = "ICO" ;
   private static final String ARRAY_VAR_NAME       = "moADVHMPG_WinArray" ;
   private static final String ZINDEX_VAR_NAME      = "miADVHMPG_TopZIdx" ;
   private static final String BASE_URL_VAR_NAME    = "msADVHMPG_BaseURL" ;
   private static final String ONLOAD_FUNC_NAME     = "ADVHMPG_BuildOuterFrames" ;
   private static final String OPENPAGE_FUNC_NAME   = "ADVHMPG_OpenPage" ;
   private static final String MIN_ICON_FUNC_NAME   = "ADVHMPG_Unminimize" ;
   private static final String MINIMIZED_ICON_NAME  = "Images/HmpgMinimizedWin.gif" ;
   private static final int    TITLE_BAR_HEIGHT     = 20 ;
   private static final int    FOOTER_HEIGHT        = 10 ;
   private static final int    ICON_WIDTH           = 35 ;
   private static final int    ICON_HEIGHT          = 25 ;
   private static final int    ICON_LEFT_OFFSET     = 5 ;
   private static final int    ICON_TOP_OFFSET      = 5 ;
   private static final int    WIN_CODE_LENGTH      = 900 ;
   private static final int    ONLOAD_LENGTH        = 200 ;
   private static final int    SCRIPT_LENGTH        = 100 ;
   private static final int    WIN_SCRIPT_LENGTH    = 100 ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public HomePage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}


         setMenuName( "HomePage" ) ;

   }





//{{EVENT_CODE

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}


   }

//{{EVENT_ADAPTER_CODE

//END_EVENT_ADAPTER_CODE}}



      public HTMLDocumentSpec getDocumentSpecification()
   {
            return getDefaultDocumentSpecification();
      }

      public String getFileName()
   {
          return getDefaultFileName();
   }

      public String getFileLocation()
   {
         return getPageTemplatePath();
      }

   public void afterPageInitialize()
   {
      super.afterPageInitialize();
      //Write code here for initializing your own control
      //or creating new control.

   }

   /**
    * This method overrides the VSPage generate method.
    * The HomePage.htm file is essentially a blank HTML
    * file.  All of the windows in the home page are
    * dynamically added in this function after the
    * super.generate() method is called.  For each window
    * in the home page, a new <div> is added to the <body>
    * and extra javascript code is created.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @return String The HTML page source to be displayed
    */
   public String generate()
   {
      TextContentElement loTxtContElem = (TextContentElement)getElementByName( REPLACE_MARKER_NAME ) ;

      /* JADE bug workaround */
      targetFrame = "_self" ;
      /* end workaround */

      appendOnloadString( "UTILS_ShowAppNavPanel();" ) ;
      VSResultSet loVSResultSet = null;
      try
      {
         StringBuffer       lsbWindowCode ;
         StringBuffer       lsbOnLoadCode ;
         StringBuffer       lsbScriptCode ;
         loVSResultSet = getWinResultSet() ;
         int                liNumWins ;

         /* get the set of windows to be shown in the page */
         loVSResultSet.last() ;
         liNumWins = loVSResultSet.getRowCount() ;

         /* allocate the StringBuffers needed to build the page */
         if ( liNumWins > 0 )
         {
            lsbWindowCode = new StringBuffer( WIN_CODE_LENGTH * liNumWins ) ;
            lsbOnLoadCode = new StringBuffer( ONLOAD_LENGTH * liNumWins ) ;
            lsbScriptCode = new StringBuffer( WIN_SCRIPT_LENGTH * liNumWins + SCRIPT_LENGTH ) ;
         } /* end if ( liNumWins > 0 ) */
         else
         {
            lsbWindowCode = new StringBuffer() ;
            lsbOnLoadCode = new StringBuffer() ;
            lsbScriptCode = new StringBuffer( SCRIPT_LENGTH ) ;
         } /* end else */

         /* create the common script header elements */
         buildScriptHeader( lsbScriptCode, liNumWins ) ;

         if ( liNumWins > 0 )
         {
            /* add the "javascript:" keyword to the onload code */
            lsbOnLoadCode.append( "javascript:" ) ;
         }

         for ( int liWinCtr = 1 ; liWinCtr <= liNumWins ; liWinCtr++ )
         {
            /* For each window in the home page, build it */
            VSRow loVSRow = loVSResultSet.getRowAt( liWinCtr ) ;

            buildWindow( loVSRow, liWinCtr - 1, lsbWindowCode,
                         lsbOnLoadCode, lsbScriptCode ) ;
         } /* end for ( int liWinCtr = 1 ; liWinCtr <= liNumWins ; liWinCtr++ ) */

         /* add the footer to the script element */
         buildScriptFooter( lsbScriptCode ) ;

         /* add the onload code to the <body> tag */
         appendOnloadString(lsbOnLoadCode.toString()) ;

         /* replace the marker tag with the home page code */
         loTxtContElem.setValue( lsbScriptCode.toString() +
                                 lsbWindowCode.toString() ) ;
      } /* end try */
      catch ( Throwable exp )
      {
         return super.generate() ;
      } /* end catch ( Throwable exp ) */
      finally
      {
         if( loVSResultSet!= null )
         {
            loVSResultSet.close();
         }
      }//end finally

      /* call the super-class generate() method to build the page */
      return super.generate() ;
   } /* end generate() */

   /**
    * This method returns the result set of windows
    * to be shown in the user's home page.  It first
    * checks if the user is using default windows
    * or if he/she has custom windows.  It then sets
    * the user id appropriately and retrieves the
    * result set.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @return VSResultSet The result set containing the home page windows to display
    */
   private VSResultSet getWinResultSet()
   {
      VSSession loVSSession = getParentApp().getSession() ;
      String    lsUserID = AMSHomePageMethods.getUserID( loVSSession ) ;

      if ( AMSHomePageMethods.useDfltWindows( loVSSession, lsUserID ) == true )
      {
         lsUserID = null ;
      } /* if ( AMSHomePageMethods.useDfltWindows( loVSSession, lsUserID ) == true ) */

      return AMSHomePageMethods.getResultSet( loVSSession, lsUserID ) ;

   } /* end getWinResultSet() */

   /**
    * This method is used to create the HTML and javascript
    * code needed for each window to be shown in the user's
    * home page.  It first retrieves all of the information
    * from the result set row passed in.  It then calls
    * to build the window's HTML code as well as the necessary
    * javascript and onload code.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @param foVSRow The row containing the window settings
    * @param fiWinNum The number of the window in the home page
    * @param fsbWindowCode The HTML code to which the new code will be appended
    * @param fsbOnLoadCode The OnLoad javascript code to which the new code will be appended
    * @param fsbScriptCode The javascript code to which the new code will be appended
    */
   private void buildWindow( VSRow foVSRow, int fiWinNum,
                             StringBuffer fsbWindowCode,
                             StringBuffer fsbOnLoadCode,
                             StringBuffer fsbScriptCode )
   {
      boolean lbExternal    = false ;
      String  lsPageCode    = null ;
      String  lsWhereClause = null ;
      String  lsOrderBy     = null ;
      String  lsDestination = null ;
      String  lsFrameset    = null ;
      String  lsFrame       = null ;
      String  lsApplName    = null ;
      String  lsName        = handleQuotedString( AMSHomePageMethods.getName( foVSRow ) ) ;
      String  lsColor       = AMSHomePageMethods.getColor( foVSRow ) ;
      String  lsFontColor   = AMSHomePageMethods.getFontColor( foVSRow ) ;
      int     liModeInd     = AMSDynamicTransition.BROWSE_MODE ;
      int     liX           = AMSHomePageMethods.getX( foVSRow ) ;
      int     liY           = AMSHomePageMethods.getY( foVSRow ) ;
      int     liWidth       = AMSHomePageMethods.getWidth( foVSRow ) ;
      int     liHeight      = AMSHomePageMethods.getHeight( foVSRow ) ;

      lsDestination = AMSHomePageMethods.getURL( foVSRow ) ;
      if ( ( lsDestination != null ) && ( lsDestination.length() > 0 ) )
      {
         lbExternal = true ;
      } /* end if ( ( lsDestination != null ) && ( lsDestination.length() > 0 ) ) */
      else
      {
         /* Support for using Advantage, or internal, pages as homepages to be
          * shown in an iframe has been removed in development done for 3.7.
          * This code, and all other code that depends on the lbExternal flag
          * being false is deprecated, but left until it can be determined that 
          * it can be safely removed.
          */
         lsPageCode    = AMSHomePageMethods.getPageCode( foVSRow ) ;
         lsWhereClause = AMSHomePageMethods.getWhereClause( foVSRow ) ;
         lsOrderBy     = AMSHomePageMethods.getOrderBy( foVSRow ) ;
         lsDestination = AMSHomePageMethods.getDestination( foVSRow ) ;
         lsFrameset    = AMSHomePageMethods.getFramesetPageName( foVSRow ) ;
         lsFrame       = AMSHomePageMethods.getFrameName( foVSRow ) ;
         lsApplName    = AMSHomePageMethods.getApplName( foVSRow ) ;
         liModeInd     = AMSHomePageMethods.getModeInd( foVSRow ) ;
      } /* end else */


      buildWindowCode( fsbWindowCode, fiWinNum, lsName, liX, liY, liWidth, liHeight, lsDestination, lbExternal) ;

      buildScriptCode( fsbScriptCode, fiWinNum, liX, liY, liWidth, liHeight ) ;

      buildOnLoadCode( fsbOnLoadCode, fiWinNum, lbExternal, lsPageCode,
            lsWhereClause, lsOrderBy, lsDestination, lsFrameset, lsFrame,
            lsApplName, liModeInd, lsName, lsColor, lsFontColor ) ;
   } /* end buildWindow() */

   /**
    * This method builds the first part of the script element.
    * It creates the <script> tag and adds the code to
    * create the window array set the z-index variable and to
    * set the base URL variable.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @param fsbScriptCode The javascript code to which the new code will be appended
    * @param fiNumWins The total number of windows to be displayed in the home page
    */
   private void buildScriptHeader( StringBuffer fsbScriptCode, int fiNumWins )
   {
      fsbScriptCode.append( "<SCRIPT language=\"JavaScript\">\n<!--\n" ) ;
      fsbScriptCode.append( "\t" ) ;
      fsbScriptCode.append( ARRAY_VAR_NAME ) ;
      fsbScriptCode.append( "=Array(" ) ;
      fsbScriptCode.append( fiNumWins ) ;
      fsbScriptCode.append( ");\n" ) ;
      fsbScriptCode.append( "\t" ) ;
      fsbScriptCode.append( ZINDEX_VAR_NAME ) ;
      fsbScriptCode.append( "=" ) ;
      fsbScriptCode.append( fiNumWins ) ;
      fsbScriptCode.append( ";\n" ) ;
      fsbScriptCode.append( "\t" ) ;
      fsbScriptCode.append( BASE_URL_VAR_NAME ) ;
      fsbScriptCode.append( "=" ) ;
      fsbScriptCode.append( "\"" ) ;
      fsbScriptCode.append( getBaseURL() ) ;
      fsbScriptCode.append( "\";\n" ) ;
   } /* end buildScriptHeader() */

   /**
    * This method builds the bottom part of the script element
    * by closing the </script> tag.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @param fsbScriptCode The javascript code to which the new code will be appended
    */
   private void buildScriptFooter( StringBuffer fsbScriptCode )
   {
      fsbScriptCode.append( "-->\n</SCRIPT>\n" ) ;
   } /* end buildScriptFooter() */

   /**
    * This method is the main workhorse of the home page
    * For each window in the home page, this method is called.
    * It builds outer-most <DIV> that defines the window as well
    * as the three inner <DIV>s and their corresponding <IFRAME>
    * tags.  Finally, it creates the minimized icon and for the
    * window.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @param fsbWindowCode The HTML code to which the new window will be appended
    * @param fiNumWin The number of the window being built
    * @param fsWinTitle The title for the window
    * @param fiXPos The number of pixels from the left of page for the window
    * @param fiYPos The number of pixels from the top of page for the window
    * @param fiWidth The width of the window in pixels
    * @param fiHeight The height of the window in pixels
    * @param fsDestination The destination location/URL
    * @param fboolExternal Flag to indicate whether the destination is external or not
    */
   private void buildWindowCode( StringBuffer fsbWindowCode, int fiWinNum, String fsWinTitle,
                                 int fiXPos, int fiYPos, int fiWidth, int fiHeight, 
                                 String fsDestination, boolean fboolExternal )
   {
      /* Build the outer-most <DIV> */
      fsbWindowCode.append( "<DIV ID=\"" + DIV_NAME_PREFIX + fiWinNum + "\" STYLE=\"position:absolute;" ) ;
      fsbWindowCode.append( "left:" + fiXPos + ";top:" + fiYPos + ";width:" + fiWidth + ";height:" + fiHeight + ";" ) ;
      fsbWindowCode.append( "z-index:" + fiWinNum + ";\">\n" ) ;

      /* Build the title bar <DIV> */
      fsbWindowCode.append ( "\t<DIV ID=\"" + DIV_NAME_PREFIX + fiWinNum + TITLE_DIV_SUFFIX + "\">\n");
      /* Build the title bar <IFRAME> */
      fsbWindowCode.append( "\t\t<IFRAME name=\"" + DIV_NAME_PREFIX + fiWinNum + TITLE_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( "title=\"" + DIV_NAME_PREFIX + fiWinNum + TITLE_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( "id=\"" + DIV_NAME_PREFIX + fiWinNum + TITLE_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( " width=\""+fiWidth 
                              + "\" height=\"" + TITLE_BAR_HEIGHT 
                              + "\" frameborder=0 margin=0 scrolling=\"no\""
                              + ">\n" ) ;
      /* Close the title bar <IFRAME> and <DIV> tags */
      fsbWindowCode.append( "\t\t</IFRAME>\n\t</DIV>\n" ) ;

      /* Build the body <DIV> */
      fsbWindowCode.append( "\t<DIV ID=\"" + DIV_NAME_PREFIX + fiWinNum + BODY_DIV_SUFFIX + "\">\n" ) ;
      /* Build the body <IFRAME> */
      fsbWindowCode.append( "\t\t<IFRAME name=\"" + DIV_NAME_PREFIX + fiWinNum + BODY_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( "title=\"" + DIV_NAME_PREFIX + fiWinNum + BODY_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( "id=\"" + DIV_NAME_PREFIX + fiWinNum + BODY_IFRAME_SUFFIX + "\" " ) ;
      if (fboolExternal)
      {
         fsbWindowCode.append( " width=\""+fiWidth 
                              + "\" height=\"" + ( fiHeight - TITLE_BAR_HEIGHT - FOOTER_HEIGHT ) 
                              + "\" frameborder=1 margin=1 scrolling=\"auto\""
                              + " src=\"" + fsDestination 
                              + "\" locatin.href=\"" + fsDestination + "\">\n" ) ;
      }
      else
      {
         /* Support for using Advantage, or internal, pages as homepages to be
          * shown in an iframe has been removed in development done for 3.7.
          * This code, and all other code that depends on the fbExternal flag
          * being false is deprecated, but left until it can be determined that 
          * it can be safely removed.
          */
         fsbWindowCode.append( "left=\""+ fiXPos +"\" top=\""+ (fiYPos+TITLE_BAR_HEIGHT) +"\"  width=\""+fiWidth 
                              + "\" height=\"" + ( fiHeight - TITLE_BAR_HEIGHT - FOOTER_HEIGHT ) 
                              + "\" frameborder=1 margin=1 scrolling=\"auto\">\n" ) ;      
      }
      /* Close the body <IFRAME> and <DIV> tags */
      fsbWindowCode.append( "\t\t</IFRAME>\n\t</DIV>\n" ) ;

      /* Build the footer <DIV> */
      fsbWindowCode.append( "\t<DIV ID=\"" + DIV_NAME_PREFIX + fiWinNum + FOOTER_DIV_SUFFIX + "\">\n");
      /* Build the footer <IFRAME> */
      fsbWindowCode.append( "\t\t<IFRAME name=\"" + DIV_NAME_PREFIX + fiWinNum + FOOTER_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( "title=\"" + DIV_NAME_PREFIX + fiWinNum + FOOTER_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( "id=\"" + DIV_NAME_PREFIX + fiWinNum + FOOTER_IFRAME_SUFFIX + "\" " ) ;
      fsbWindowCode.append( " width=\""+fiWidth 
                              + "\" height=\"" + FOOTER_HEIGHT 
                              + "\" frameborder=0 margin=0 scrolling=\"no\""
                              + ">\n" ) ;
      /* Close the footer <IFRAME> and <DIV> tags */
      fsbWindowCode.append( "\t\t</IFRAME>\n\t</DIV>\n" ) ;

      /* Close the outer-most <DIV> */
      fsbWindowCode.append( "</DIV>\n" ) ;


      /* Build the minimized <DIV> */
      fsbWindowCode.append( "<DIV ID=\"" + DIV_NAME_PREFIX + fiWinNum + ICON_DIV_SUFFIX +  "\" STYLE=\"position:absolute;" ) ;
      fsbWindowCode.append( "left:" + ICON_LEFT_OFFSET + ";top:" + ( fiWinNum * ( ICON_HEIGHT + ICON_TOP_OFFSET ) + ICON_TOP_OFFSET ) + ";" ) ;
      fsbWindowCode.append( "width:" + ICON_WIDTH + ";height:" + ICON_HEIGHT + ";" ) ;
      fsbWindowCode.append( "z-index:0;visibility:hidden;\" " ) ;
      fsbWindowCode.append( "ondblclick=\"" + MIN_ICON_FUNC_NAME + "(" + fiWinNum + ");\">\n" ) ;

      /* Build the minimized <IMG> */
      fsbWindowCode.append( "\t<IMG SRC=\"" + MINIMIZED_ICON_NAME + "\" title=\"" + fsWinTitle + "\" alt=\"" + fsWinTitle + "\" " ) ;
      fsbWindowCode.append( "hspace=0 vspace=0 border=0>\n" ) ;

      /* Close the minimized <DIV> tag */
      fsbWindowCode.append( "</DIV>\n" ) ;

   } /* end buildExternalWindowCode() */

   /**
    * This method builds the javascript array for the
    * window.  This array holds all of the window's settings
    * so that they can be accessed by the javascript methods.
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @param fsbWindowCode The HTML code to which the new window will be appended
    * @param fiNumWin The number of the window being built
    * @param fiXPos The number of pixels from the left of page for the window
    * @param fiYPos The number of pixels from the top of page for the window
    * @param fiWidth The width of the window in pixels
    * @param fiHeight The height of the window in pixels
    */
   private void buildScriptCode( StringBuffer fsbScriptCode, int fiWinNum,
                                 int fiXPos, int fiYPos, int fiWidth, int fiHeight )
   {
      fsbScriptCode.append( "\t" + ARRAY_VAR_NAME + "[" + fiWinNum + "]=new Array(\"" + DIV_NAME_PREFIX + fiWinNum + "\"," ) ;
      fsbScriptCode.append( fiWidth + "," + fiHeight + "," + fiYPos + "," + fiXPos + ",false," + fiWinNum + "," ) ;
      fsbScriptCode.append( "false,false," + fiWidth + "," + fiHeight + "," + fiYPos + "," + fiXPos + ",0,0);\n" ) ;
   } /* end buildWinArray() */

   /**
    * This method builds the OnLoad javascript code for each
    * window.  If the window contains an external page, then
    * it just sets the href of the body frame to point to the
    * destination.  Otherwise, the page in an ADVANTAGE page,
    * so it builds the javascript call necessary to perform a
    * dynamic transition to the page.  Finally, for both types
    * of windows, it creates the code to build the header and
    * footer frames.
    *
    *
    * Modification Log : Mark Shipley - 05/09/2000
    *                                 - inital version
    *
    * @param fsbOnLoadCode The OnLoad javascript code to which the new window will be appended
    * @param fiNumWin The number of the window being built
    * @param fbExternal A flag indicating if the window contains an external web site or an ADVANTAGE page
    * @param fsDestination The page to be displayed in the window (a URL or a page template name).
    * @param fsFrameset The name of the frameset page (if any) if it is an ADVANTAGE page
    * @param fsFrame The name of the frame inside of the frameset (if any) if it is an ADVANTAGE page
    * @param fsWinTitle The title bar text for the window.
    * @param fsTBarColor The background color for the window's title bar.
    * @param fsTBarFontColor The text color for the window's title bar.
    */
   private void buildOnLoadCode( StringBuffer fsbOnLoadCode, int fiWinNum, boolean fbExternal,
         String fsPageCode, String fsWhereClause, String fsOrderBy, String fsDestination,
         String fsFrameset, String fsFrame, String fsApplName, int fiModeInd,
         String fsWinTitle, String fsTBarColor, String fsTBarFontColor )
   {
      if (!fbExternal)
      {
         /* Support for using Advantage, or internal, pages as homepages to be
          * shown in an iframe has been removed in development done for 3.7.
          * This code, and all other code that depends on the fbExternal flag
          * being false is deprecated, but left until it can be determined that 
          * it can be safely removed.
          */
         fsbOnLoadCode.append( OPENPAGE_FUNC_NAME ) ;
         fsbOnLoadCode.append( "('" ) ;
         fsbOnLoadCode.append( DIV_NAME_PREFIX ) ;
         fsbOnLoadCode.append( fiWinNum ) ;
         fsbOnLoadCode.append( BODY_IFRAME_SUFFIX ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( fsPageCode ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( handleQuotedString( fsWhereClause ) ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( fsOrderBy ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( fsDestination ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( fsFrameset ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( fsFrame ) ;
         fsbOnLoadCode.append( "','" ) ;
         fsbOnLoadCode.append( fsApplName ) ;
         fsbOnLoadCode.append( "'," ) ;
         fsbOnLoadCode.append( fiModeInd ) ;
         fsbOnLoadCode.append( ");\n" ) ;
      } /* end else */

      fsbOnLoadCode.append( ONLOAD_FUNC_NAME ) ;
      fsbOnLoadCode.append( "(" ) ;
      fsbOnLoadCode.append( "window.frames['" ) ;
      fsbOnLoadCode.append( DIV_NAME_PREFIX ) ;
      fsbOnLoadCode.append( fiWinNum ) ;
      fsbOnLoadCode.append( TITLE_IFRAME_SUFFIX ) ;
      fsbOnLoadCode.append( "'].document," ) ;
      fsbOnLoadCode.append( "window.frames['" ) ;
      fsbOnLoadCode.append( DIV_NAME_PREFIX ) ;
      fsbOnLoadCode.append( fiWinNum ) ;
      fsbOnLoadCode.append( FOOTER_IFRAME_SUFFIX ) ;
      fsbOnLoadCode.append( "'].document," ) ;
      fsbOnLoadCode.append( "'" ) ;
      fsbOnLoadCode.append( fsWinTitle ) ;
      fsbOnLoadCode.append( "','" ) ;
      fsbOnLoadCode.append( fsTBarColor ) ;
      fsbOnLoadCode.append( "','" ) ;
      fsbOnLoadCode.append( fsTBarFontColor ) ;
      fsbOnLoadCode.append( "'," ) ;
      fsbOnLoadCode.append( fiWinNum ) ;
      fsbOnLoadCode.append( ");\n" ) ;
   } /* end buildOnLoadCode() */

}
