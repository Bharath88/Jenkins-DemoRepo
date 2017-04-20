ABOUTUTILS_SECON_WIN_WIDTH        = 300;
ABOUTUTILS_SECON_WIN_HEIGHT       = 200;
ABOUTUTILS_SECON_WIN_LEFT         = 300;
ABOUTUTILS_SECON_WIN_TOP          = 300;


function ABOUTUTILS_OpenAboutPage( fsPageName )
{
   var lsHref = window.location.href ;
   var liPos = lsHref.indexOf( "ABI" ) ;

   if ( liPos != -1 )
   {
    var lsBaseHref = lsHref.substring(0, liPos );
    var fsPageName = lsBaseHref + fsPageName;
   }

   var loNewWin = DHTMLLIB_OpenNewWin( 
    fsPageName, null,'no', 'no', 'no', 'no', 'no', 'no', 'auto',  
    ABOUTUTILS_SECON_WIN_WIDTH, ABOUTUTILS_SECON_WIN_HEIGHT, 
      ABOUTUTILS_SECON_WIN_LEFT, ABOUTUTILS_SECON_WIN_TOP, 'no' );
}



function ABOUTUTILS_PopulateAppName( fsAppName )
{
   var lsHTML = "";
   
   lsHTML += "<table>";
   lsHTML += "<tr>";
   lsHTML += "<td><b>";
   lsHTML += fsAppName;
   lsHTML += "</b></td>";
   lsHTML += "</tr>";  
   lsHTML += "</table>";
   
   document.write( lsHTML );
}


function ABOUTUTILS_PopulateAboutData( faAppData )
{
   var lsHTML = "";
   var liMaxLimit = null;
   
   lsHTML += "<table>";
   
   liMaxLimit = faAppData.length;
 
   for(var liCntr = 0; liCntr < faAppData.length 
      && liCntr < liMaxLimit; liCntr++ )
   {
    lsHTML += ( liCntr % 2 == 0 ) 
       ? "<tr class=\"OddRow\">"
       : "<tr class=\"EvenRow\">";

    lsHTML += "<td align=\"right\"><b>";
    lsHTML += faAppData[liCntr][0];
    lsHTML += "</b></td>";
    lsHTML += "<td></td>";
    lsHTML += "<td class=\"Announcement\" align=\"left\">";
    lsHTML += faAppData[liCntr][1];
    lsHTML += "</td>";
    lsHTML += "</tr>";
   }      

   lsHTML += "</table>";
   
   document.write( lsHTML );
}

