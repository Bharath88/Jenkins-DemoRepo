
ADVHMPG_SIZE_INCREMENT           = 25 ;
ADVHMPG_MIN_WIN_WIDTH            = 100 ;
ADVHMPG_MIN_WIN_HEIGHT           = 100 ;
ADVHMPG_TITLE_HEIGHT             = 20 ;
ADVHMPG_FOOTER_HEIGHT            = 10 ;
ADVHMPG_NAME_IDX                 = 0 ;
ADVHMPG_WIDTH_IDX                = 1 ;
ADVHMPG_HEIGHT_IDX               = 2 ;
ADVHMPG_TOP_IDX                  = 3 ;
ADVHMPG_LEFT_IDX                 = 4 ;
ADVHMPG_MAX_IDX                  = 5 ;
ADVHMPG_Z_IDX                    = 6 ;
ADVHMPG_MOVE_FLAG_IDX            = 7 ;
ADVHMPG_SIZE_FLAG_IDX            = 8 ;
ADVHMPG_OLD_WIDTH_IDX            = 9 ;
ADVHMPG_OLD_HEIGHT_IDX           = 10 ;
ADVHMPG_OLD_TOP_IDX              = 11 ;
ADVHMPG_OLD_LEFT_IDX             = 12 ;
ADVHMPG_START_TOP_IDX            = 13 ;
ADVHMPG_START_LEFT_IDX           = 14 ;
ADVHMPG_TITLE_SUFFIX             = 'TTL' ;
ADVHMPG_BODY_SUFFIX              = 'BDY' ;
ADVHMPG_FOOTER_SUFFIX            = 'FTR' ;
ADVHMPG_ICON_SUFFIX              = 'ICO' ;
ADVHMPG_IFRAME_SUFFIX            = 'IF'  ;
ADVHMPG_CACASDE_OFFSET           = 21 ;
ADVHMPG_MAXREST_IMAGE            = "Images/HmpgMaximize.gif" ;
ADVHMPG_MINIMIZE_IMAGE           = "Images/HmpgMinimize.gif" ;
ADVHMPG_CLOSE_IMAGE              = "Images/HmpgClose.gif" ;
ADVHMPG_DEST_ATTRIB_NAME         = "ams_destination" ;
ADVHMPG_FRAMESET_ATTRIB_NAME     = "ams_framesetpagename" ;
ADVHMPG_FRAME_ATTRIB_NAME        = "ams_framename" ;
ADVHMPG_APPL_ATTRIB_NAME         = "ams_applname" ;
ADVHMPG_PAGE_CD_ATTRIB_NAME      = "ams_pagecode" ;
ADVHMPG_ADDRECORD_ATTRIB_NAME    = "ams_addrecord" ;
ADVHMPG_QUERYMODE_ATTRIB_NAME    = "ams_query" ;
ADVHMPG_WHERE_CLAUSE_ATTRIB_NAME = "ams_whereclause" ;
ADVHMPG_ORDER_BY_ATTRIB_NAME     = "ams_orderbyclause" ;
ADVHMPG_ADD_MODE                 = 0 ;
ADVHMPG_BROWSE_MODE              = 1 ;
ADVHMPG_QUERY_MODE               = 2 ;

var moADVHMPG_WinArray = null ;

var miADVHMPG_TopZIdx ;

var msADVHMPG_BaseURL  = null ;

function ADVHMPG_BuildOuterFrames( foTitleDoc, foFooterDoc, fsTitle, fsBGColor, fsTextColor, fiDivNum )
{
   if (mboolIE4Up)
   {
      var loWinSettings = moADVHMPG_WinArray[fiDivNum] ;
      DHTMLLIB_MoveLayerTo( DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ),
                               loWinSettings[ADVHMPG_OLD_LEFT_IDX],
                               loWinSettings[ADVHMPG_OLD_TOP_IDX] ) ;
   }
                         
   ADVHMPG_BuildTitleFrame( foTitleDoc, fsTitle, fsBGColor, fsTextColor, fiDivNum ) ;
   ADVHMPG_BuildFooterFrame( foFooterDoc, fsTitle, fsBGColor, fsTextColor, fiDivNum ) ;
}

function ADVHMPG_BuildTitleFrame( foDocument, fsTitle, fsBGColor, fsTextColor, fiDivNum )
{
   foDocument.clear() ;
   foDocument.open() ;
   foDocument.writeln( '<HTML><HEAD><TITLE>' + fsTitle + '</TITLE>' ) ;
   foDocument.writeln( '<base href="' + msADVHMPG_BaseURL + '">' ) ;
   foDocument.writeln( '</head>' ) ;

   foDocument.writeln( '<body oncontextmenu="return false;" bgcolor="' + fsBGColor + '" text="' + fsTextColor + '" ' ) ;
   foDocument.writeln( 'onclick="parent.ADVHMPG_ActivateWindow(' + fiDivNum + ');" ' ) ;
   foDocument.write( 'ondblclick="parent.ADVHMPG_MaxRest(' + fiDivNum + ');" ' ) ;

   foDocument.writeln( 'onmousedown="parent.ADVHMPG_ActivateWindow(' + fiDivNum + ');parent.ADVHMPG_StartMove(' + fiDivNum + ',event);" ' ) ;
   foDocument.writeln( 'onmouseup="parent.ADVHMPG_StopMove(' + fiDivNum + ');" ' ) ;
   foDocument.writeln( 'onmousemove="parent.ADVHMPG_Move(' + fiDivNum + ',event);" ' ) ;
   foDocument.writeln( 'onmouseout="parent.ADVHMPG_StopMove(' + fiDivNum + ');">' ) ;

   foDocument.writeln( '<div style="position:absolute;top:2;left:5;font-weight:bold;font-family:sans-serif;font-size:8pt;">&nbsp;' + fsTitle + '</div>') ;
   foDocument.writeln( '<div style="position:absolute; top:0; right:5;">') ;
   foDocument.writeln( '<img src="' + ADVHMPG_MINIMIZE_IMAGE + '" valign="top" border=0 hspace=1 vspace=3 onclick="parent.ADVHMPG_Minimize(' + fiDivNum + ', event);">' ) ;
   foDocument.writeln( '<img name="maxrest" src="' + ADVHMPG_MAXREST_IMAGE + '" valign="top" border=0 hspace=1 vspace=3 onclick="parent.ADVHMPG_MaxRest(' + fiDivNum + ');">' ) ;
   foDocument.writeln( '<img src="' + ADVHMPG_CLOSE_IMAGE + '" valign="top" border=0 hspace=1 vspace=3 onclick="parent.ADVHMPG_CloseWindow(' + fiDivNum + ', event);">' ) ;
   foDocument.writeln( '</div>' ) ;

   foDocument.writeln( '</body></HTML>' ) ;

   foDocument.close() ;
}

function ADVHMPG_BuildFooterFrame( foDocument, fsTitle, fsBGColor, fsTextColor, fiDivNum )
{
   foDocument.clear() ;
   foDocument.open() ;
   foDocument.writeln( '<HTML><HEAD><TITLE>' + fsTitle + '</TITLE>' ) ;
   foDocument.writeln( '<base href="' + msADVHMPG_BaseURL + '">' ) ;
   foDocument.writeln( '</head>' ) ;
   foDocument.writeln( '<body oncontextmenu="return false;" bgcolor="' + fsBGColor + '" text="' + fsTextColor + '" ' ) ;
   foDocument.writeln( 'onclick="parent.ADVHMPG_ActivateWindow(' + fiDivNum + ');" ' ) ;
   foDocument.writeln( 'onmousedown="parent.ADVHMPG_ActivateWindow(' + fiDivNum + '); parent.ADVHMPG_StartResize(' + fiDivNum + ',event);" ' ) ;
   foDocument.writeln( 'onmouseup="parent.ADVHMPG_StopResize(' + fiDivNum + ');" ' ) ;
   foDocument.writeln( 'onmousemove="parent.ADVHMPG_Resize(' + fiDivNum + ',event);" ' ) ;
   foDocument.writeln( 'onmouseout ="parent.ADVHMPG_StopResize(' + fiDivNum + ');" style="cursor:se-resize;">' ) ;
   foDocument.writeln( '</body></html>' ) ;

   foDocument.close() ;
}

function ADVHMPG_TileWindows()
{
   var liWinCtr ;
   var liBrowserWidth  = DHTMLLIB_GetEvenNumber( DHTMLLIB_GetWindowWidth() ) ;
   var liBrowserHeight = DHTMLLIB_GetEvenNumber( DHTMLLIB_GetWindowHeight() ) ;
   var liWinWidth      = liBrowserWidth ;
   var liWinHeight     = liBrowserHeight ;
   var liLeft          = 0 ;
   var liTop           = 0 ;

   if ( moADVHMPG_WinArray.length > 1 )
   {
      liWinWidth = liBrowserWidth / 2 ;
      if ( moADVHMPG_WinArray.length > 2 )
      {
         liWinHeight = liBrowserHeight / 2 ;
      }
   }

   for ( liWinCtr = 0 ; liWinCtr < moADVHMPG_WinArray.length ; liWinCtr++ )
   {
      var loWinSettings = moADVHMPG_WinArray[liWinCtr] ;

      ADVHMPG_SetWindowSize( loWinSettings, liWinWidth, liWinHeight ) ;
      DHTMLLIB_MoveLayerTo( DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ), liLeft, liTop ) ;
      loWinSettings[ADVHMPG_LEFT_IDX]     = liLeft ;
      loWinSettings[ADVHMPG_TOP_IDX]      = liTop ;
      loWinSettings[ADVHMPG_OLD_LEFT_IDX] = liLeft ;
      loWinSettings[ADVHMPG_OLD_TOP_IDX]  = liTop ;
      ADVHMPG_Unminimize( liWinCtr ) ;
      ADVHMPG_ActivateWindow( liWinCtr ) ;

      if ( DHTMLLIB_GetEvenNumber( liWinCtr ) == liWinCtr )
      {
         liLeft = liWinWidth ;
      }
      else
      {
         liTop  += liWinHeight ;
         liLeft = 0 ;
      }
   }

   return true ;
}

function ADVHMPG_CascadeWindows()
{
   var liWinCtr ;
   var liXPos ;
   var liYPos ;

   for ( liWinCtr = 0, liXPos = 5, liYPos = 5; liWinCtr < moADVHMPG_WinArray.length ; liWinCtr++, liXPos += ADVHMPG_CACASDE_OFFSET, liYPos += ADVHMPG_CACASDE_OFFSET )
   {
      var loWinSettings = moADVHMPG_WinArray[liWinCtr] ;

      loWinSettings[ADVHMPG_OLD_LEFT_IDX] = loWinSettings[ADVHMPG_LEFT_IDX] ;
      loWinSettings[ADVHMPG_OLD_TOP_IDX]  = loWinSettings[ADVHMPG_TOP_IDX] ;
      ADVHMPG_Unminimize( liWinCtr ) ;
      ADVHMPG_ActivateWindow( liWinCtr ) ;
      DHTMLLIB_MoveLayerTo( DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ), liXPos, liYPos ) ;
      loWinSettings[ADVHMPG_LEFT_IDX]     = liXPos ;
      loWinSettings[ADVHMPG_TOP_IDX]      = liYPos ;
   }

   return true ;
}

function ADVHMPG_Minimize( fiWinNum , foEvent)
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   ADVHMPG_CloseWindow( fiWinNum , foEvent) ;
   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] + ADVHMPG_ICON_SUFFIX ) ) ;

   return true ;
}

function ADVHMPG_Unminimize( fiWinNum )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   ADVHMPG_OpenWindow( fiWinNum ) ;
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer(
           loWinSettings[ADVHMPG_NAME_IDX] + ADVHMPG_ICON_SUFFIX ) ) ;

   return true ;
}

function ADVHMPG_CloseWindow( fiWinNum, foEvent )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;
   var loHmpgWin = DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] );

   if (loHmpgWin != null)
   {
      if ( mboolIE4Up )
      {
         DHTMLLIB_HideLayer( loHmpgWin ) ;
      }
      else if ( mboolNS5Up )
      {
         ADVHMPG_CloseWindowNS(loWinSettings)
      }
   }

   return true ;
}

function ADVHMPG_OpenWindow( fiWinNum )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   if (mboolIE4Up)
   {
      DHTMLLIB_ShowLayer(
          DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ) ) ;
   }
   else if (mboolNS5Up)
   {
      ADVHMPG_RestoreNS(loWinSettings);
   }

   return true ;
}

function ADVHMPG_StartResize( fiWinNum, foEvent )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   loWinSettings[ADVHMPG_SIZE_FLAG_IDX]  = true ;
   if (mboolIE4Up)
   {
      loWinSettings[ADVHMPG_START_LEFT_IDX] = foEvent.x ;
      loWinSettings[ADVHMPG_START_TOP_IDX]  = foEvent.y ;
   }
   else if (mboolNS5Up)
   {
      loWinSettings[ADVHMPG_START_LEFT_IDX] = foEvent.pageX ;
      loWinSettings[ADVHMPG_START_TOP_IDX]  = foEvent.pageY ;
   }

   return true ;
}

function ADVHMPG_StopResize( fiWinNum )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   loWinSettings[ADVHMPG_SIZE_FLAG_IDX] = false ;

   return true ;
}

function ADVHMPG_Resize( fiWinNum, foEvent )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;
   var liEventX;
   var liEventY;

   if (mboolNS5Up)
   {
      liEventX = foEvent.pageX;
      liEventY = foEvent.pageY;
   }
   else if (mboolIE4Up)
   {
      liEventX = foEvent.x;
      liEventY = foEvent.y;
   }

   if ( loWinSettings[ADVHMPG_SIZE_FLAG_IDX] == true )
   {
      var liChangeX   = liEventX - loWinSettings[ADVHMPG_START_LEFT_IDX] ;
      var liChangeY   = liEventY - loWinSettings[ADVHMPG_START_TOP_IDX] ;
      var loWindow    = DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ) ;
      var liNewWidth  = DHTMLLIB_GetWidth( loWindow ) + liChangeX ;
      var liNewHeight = DHTMLLIB_GetHeight( loWindow ) + liChangeY ;

      ADVHMPG_SetWindowSize( loWinSettings, liNewWidth, liNewHeight ) ;
      loWinSettings[ADVHMPG_START_LEFT_IDX] = liEventX ;
      loWinSettings[ADVHMPG_START_TOP_IDX]  = liEventY ;
   }

   return true ;
}

function ADVHMPG_ActivateWindow( fiWinNum )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   DHTMLLIB_SetzIndex( DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ),
                                          ++miADVHMPG_TopZIdx ) ;

   return true ;
}

function ADVHMPG_RestoreNS( foWinSettings )
{
   var lsWinName   = foWinSettings[ADVHMPG_NAME_IDX] ;
   var loWindow    = DHTMLLIB_GetLayer( lsWinName ) ;

   DHTMLLIB_MoveLayerTo(loWindow, foWinSettings[ADVHMPG_OLD_LEFT_IDX],
                                  foWinSettings[ADVHMPG_OLD_TOP_IDX]) ;
}


function ADVHMPG_CloseWindowNS( foWinSettings )
{
   var lsWinName   = foWinSettings[ADVHMPG_NAME_IDX] ;
   var loWindow    = DHTMLLIB_GetLayer( lsWinName ) ;
   var loTitleBar  = DHTMLLIB_GetLayer( lsWinName + ADVHMPG_TITLE_SUFFIX ) ;
   var loBody      = DHTMLLIB_GetLayer( lsWinName + ADVHMPG_BODY_SUFFIX ) ;
   var loFooter    = DHTMLLIB_GetLayer( lsWinName + ADVHMPG_FOOTER_SUFFIX ) ;

   foWinSettings[ADVHMPG_LEFT_IDX]     = -10 ;
   foWinSettings[ADVHMPG_TOP_IDX]      = -10 ;
   foWinSettings[ADVHMPG_OLD_LEFT_IDX] = DHTMLLIB_GetLeft(loWindow) ;
   foWinSettings[ADVHMPG_OLD_TOP_IDX]  = DHTMLLIB_GetTop(loWindow) ;

   DHTMLLIB_MoveLayerTo(loWindow, -1000, -1000) ;
}

function ADVHMPG_SetWindowSize( foWinSettings, fiWidth, fiHeight )
{
   var liNewWidth  = fiWidth  < ADVHMPG_MIN_WIN_WIDTH  ? ADVHMPG_MIN_WIN_WIDTH  : fiWidth ;
   var liNewHeight = fiHeight < ADVHMPG_MIN_WIN_HEIGHT ? ADVHMPG_MIN_WIN_HEIGHT : fiHeight ;
   var lsWinName   = foWinSettings[ADVHMPG_NAME_IDX] ;
   var loWindow    = DHTMLLIB_GetLayer( lsWinName ) ;
   var loTitleBar  = DHTMLLIB_GetLayer( lsWinName + ADVHMPG_TITLE_SUFFIX + ADVHMPG_IFRAME_SUFFIX) ;
   var loBody      = DHTMLLIB_GetLayer( lsWinName + ADVHMPG_BODY_SUFFIX + ADVHMPG_IFRAME_SUFFIX) ;
   var loFooter    = DHTMLLIB_GetLayer( lsWinName + ADVHMPG_FOOTER_SUFFIX + ADVHMPG_IFRAME_SUFFIX) ;
   var liHeightDif = liNewHeight - DHTMLLIB_GetHeight( loWindow ) ;

   foWinSettings[ADVHMPG_WIDTH_IDX]  = liNewWidth ;
   foWinSettings[ADVHMPG_HEIGHT_IDX] = liNewHeight ;
   loWindow.style.width    = liNewWidth ;
   loTitleBar.style.width  = liNewWidth ;
   loBody.style.width      = liNewWidth ;
   loFooter.style.width    = liNewWidth ;

   loWindow.style.height = liNewHeight ;
   loBody.style.height   = liNewHeight - ( ADVHMPG_TITLE_HEIGHT + ADVHMPG_FOOTER_HEIGHT ) ;
   loFooter.style.top    = ADVHMPG_GetNumber( loFooter.style.top ) + liHeightDif ;
}

function ADVHMPG_StartMove( fiWinNum, foEvent )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   loWinSettings[ADVHMPG_MOVE_FLAG_IDX]  = true ;
   if (mboolIE4Up)
   {
      loWinSettings[ADVHMPG_START_LEFT_IDX] = foEvent.x ;
      loWinSettings[ADVHMPG_START_TOP_IDX]  = foEvent.y ;
   }
   else if (mboolNS5Up)
   {
      loWinSettings[ADVHMPG_START_LEFT_IDX] = foEvent.pageX ;
      loWinSettings[ADVHMPG_START_TOP_IDX]  = foEvent.pageY ;
   }

   return true ;
}

function ADVHMPG_StopMove( fiWinNum )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   loWinSettings[ADVHMPG_MOVE_FLAG_IDX] = false ;

   return true ;
}

function ADVHMPG_Move( fiWinNum, foEvent )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;
   var liEventX;
   var liEventY;

   if (mboolNS5Up)
   {
      liEventX = foEvent.pageX;
      liEventY = foEvent.pageY;
   }
   else if (mboolIE4Up)
   {
      liEventX = foEvent.x;
      liEventY = foEvent.y;
   }

   if ( loWinSettings[ADVHMPG_MOVE_FLAG_IDX] == true )
   {
      var liChangeX = liEventX - loWinSettings[ADVHMPG_START_LEFT_IDX] ;
      var liChangeY = liEventY - loWinSettings[ADVHMPG_START_TOP_IDX] ;

      DHTMLLIB_MoveLayerBy( DHTMLLIB_GetLayer( loWinSettings[ADVHMPG_NAME_IDX] ), liChangeX, liChangeY ) ;

      loWinSettings[ADVHMPG_LEFT_IDX]     += liChangeX ;
      loWinSettings[ADVHMPG_TOP_IDX]      += liChangeY ;
      loWinSettings[ADVHMPG_OLD_LEFT_IDX] += liChangeX ;
      loWinSettings[ADVHMPG_OLD_TOP_IDX]  += liChangeY ;
   }

   return true ;
}

function ADVHMPG_MaxRest( fiWinNum )
{
   var loWinSettings = moADVHMPG_WinArray[fiWinNum] ;

   if ( loWinSettings[ADVHMPG_MAX_IDX] == false )
   {
      ADVHMPG_Maximize( loWinSettings ) ;
   }
   else
   {
      ADVHMPG_Restore( loWinSettings ) ;
   }

   return true ;
}


function ADVHMPG_Maximize( foWinSettings )
{
   var liNewWidth  = DHTMLLIB_GetWindowWidth() ;
   var liNewHeight = DHTMLLIB_GetWindowHeight() ;

   foWinSettings[ADVHMPG_OLD_HEIGHT_IDX] = foWinSettings[ADVHMPG_HEIGHT_IDX] ;
   foWinSettings[ADVHMPG_OLD_WIDTH_IDX]  = foWinSettings[ADVHMPG_WIDTH_IDX] ;
   foWinSettings[ADVHMPG_OLD_TOP_IDX]    = foWinSettings[ADVHMPG_TOP_IDX] ;
   foWinSettings[ADVHMPG_OLD_LEFT_IDX]   = foWinSettings[ADVHMPG_LEFT_IDX] ;

   foWinSettings[ADVHMPG_TOP_IDX]  = 0 ;
   foWinSettings[ADVHMPG_LEFT_IDX] = 0 ;
   foWinSettings[ADVHMPG_MAX_IDX]  = true ;

   DHTMLLIB_MoveLayerTo( DHTMLLIB_GetLayer( foWinSettings[ADVHMPG_NAME_IDX] ), 0, 0 ) ;
   ADVHMPG_SetWindowSize( foWinSettings, liNewWidth, liNewHeight ) ;
}

function ADVHMPG_Restore( foWinSettings )
{
   DHTMLLIB_MoveLayerTo( DHTMLLIB_GetLayer( foWinSettings[ADVHMPG_NAME_IDX] ),
                         foWinSettings[ADVHMPG_OLD_LEFT_IDX],
                         foWinSettings[ADVHMPG_OLD_TOP_IDX] ) ;

   foWinSettings[ADVHMPG_TOP_IDX]  = foWinSettings[ADVHMPG_OLD_TOP_IDX] ;
   foWinSettings[ADVHMPG_LEFT_IDX] = foWinSettings[ADVHMPG_OLD_LEFT_IDX] ;
   foWinSettings[ADVHMPG_MAX_IDX]  = false ;

   ADVHMPG_SetWindowSize( foWinSettings,
                       foWinSettings[ADVHMPG_OLD_WIDTH_IDX],
                       foWinSettings[ADVHMPG_OLD_HEIGHT_IDX] ) ;
}

function ADVHMPG_GetNumber( fsPixel )
{
   var liIndex = fsPixel.indexOf( "px" ) ;
   var liNumber = Number ( fsPixel.substr( 0,liIndex ) ) ;
   return liNumber ;
}

function ADVHMPG_OpenPage( fsTarget, fsPageCode, fsWhereClause, fsOrderBy,
      fsDestination, fsFrameset, fsFrame, fsApplName, fiModeInd )
{
   var lsRequest = "" ;

   lsRequest += 'OpenPage=OpenPage&' ;
   lsRequest += ADVHMPG_PAGE_CD_ATTRIB_NAME      + '="' + ( fsPageCode    ? fsPageCode    : "" ) + '"&' ;
   if ( fiModeInd == ADVHMPG_ADD_MODE )
   {
      lsRequest += ADVHMPG_ADDRECORD_ATTRIB_NAME + '="' + ADVHMPG_ADDRECORD_ATTRIB_NAME + '"&' ;
   } 
   else if ( fiModeInd == ADVHMPG_QUERY_MODE )
   {
      lsRequest += ADVHMPG_QUERYMODE_ATTRIB_NAME + '="' + ADVHMPG_QUERYMODE_ATTRIB_NAME + '"&' ;
   } 
   lsRequest += ADVHMPG_WHERE_CLAUSE_ATTRIB_NAME + '="' + ( fsWhereClause ? fsWhereClause : "" ) + '"&' ;
   lsRequest += ADVHMPG_ORDER_BY_ATTRIB_NAME     + '="' + ( fsOrderBy     ? fsOrderBy     : "" ) + '"&' ;
   lsRequest += ADVHMPG_DEST_ATTRIB_NAME         + '="' + ( fsDestination ? fsDestination : "" ) + '"&' ;
   lsRequest += ADVHMPG_FRAMESET_ATTRIB_NAME     + '="' + ( fsFrameset    ? fsFrameset    : "" ) + '"&' ;
   lsRequest += ADVHMPG_FRAME_ATTRIB_NAME        + '="' + ( fsFrame       ? fsFrame       : "" ) + '"&' ;
   lsRequest += ADVHMPG_APPL_ATTRIB_NAME         + '="' + ( fsApplName    ? fsApplName    : "" ) + '"' ;

   submitForm( document.forms["HomePage"], lsRequest, fsTarget ) ;
}
