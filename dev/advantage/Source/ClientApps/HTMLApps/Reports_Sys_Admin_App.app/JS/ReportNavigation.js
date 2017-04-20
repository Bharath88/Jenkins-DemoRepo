var gCurrPage = 1;
var gFirstPage = 1;
var gLastPage;
var gsFName;
var gsExtName = ".html";;

var gsName = "index";
var giCurrentEntry = 0;

var gsTitle = "";
var gsLink = "";
var gsTarget = "";

var gsNextImg = new Array;
var gsFfwdImg = new Array;
var gsRewImg = new Array;
var gsPrevImg = new Array;
var gsHstFwdImg = new Array;
var gsHstBwdImg = new Array;
var gsEntryImg = new Array;
var gsSepImg = new Image;

gsSepImg = "../Reports_Sys_Admin_App/Images/ADVNavSep.gif";
gsRewImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavRew0.gif";
gsRewImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavRew1.gif";
gsPrevImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavPrev0.gif";
gsPrevImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavPrev1.gif";
gsNextImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavNext0.gif";
gsNextImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavNext1.gif";
gsFfwdImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavFfwd0.gif";
gsFfwdImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavFfwd1.gif";
gsHstFwdImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavHstFwd0.gif";
gsHstFwdImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavHstFwd1.gif";
gsHstBwdImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavHstBwd0.gif";
gsHstBwdImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavHstBwd1.gif";
gsEntryImg[0] = "../Reports_Sys_Admin_App/Images/ADVNavFile0.gif";;
gsEntryImg[1] = "../Reports_Sys_Admin_App/Images/ADVNavFile1.gif";

var gnHstList = new Array;
var gnCurrHstPos;

var REPORT_NAV_FRAME = 'ReportNav';


function initializeIndex()
{
   var i;

   document.writeln('<table class="advaction recsrc" border=0>');

   for(i = 0; i < gsOutlineEntry.length; i++)
   {
      document.writeln('<tr><td nowrap>');
      document.writeln('   <img src="' + gsEntryImg[0] + '" border=0>');
      document.writeln('   <a href="javascript:setCurrentEntry(' + i + ');">');
      document.writeln('   ' + gsOutlineEntry[i] + '</a>'); //Verdana, Helvetica
      document.writeln('</td></tr>');
   }

   document.writeln('</table>\n</center>');
   setCurrentEntryIcon(0);
}


function setCurrentEntry(foEntryNum)
{
   openPage(giOutlinePage[foEntryNum], false);
   setCurrentEntryIcon(foEntryNum);
}


function setCurrentEntryIcon(foEntryNum)
{
   if (document.images[giCurrentEntry] != null)
   {
      document.images[giCurrentEntry].src = gsEntryImg[0];
   }
   if (document.images[foEntryNum] != null)
   {
      document.images[foEntryNum].src = gsEntryImg[1];
   }
   giCurrentEntry = foEntryNum;
}


function initializeNavigation(foLastPage)
{
   var nIconNum;

   gLastPage = foLastPage;

   if(gFirstPage == gLastPage)
      nIconNum = 0;
   else
      nIconNum = 1;

   gnCurrHstPos = 0;
   gnHstList[gnCurrHstPos] = gFirstPage;
   gnHstList[gnCurrHstPos + 1] = 0;

   if (top.frames[REPORT_NAV_FRAME] == null)
   {
      return;
   }

   var loNavDoc = top.frames[REPORT_NAV_FRAME].document;
   var lsBaseHREF = UTILS_getBaseHref( document );

   loNavDoc.open();
   loNavDoc.writeln('<HTML>');
   loNavDoc.writeln('<HEAD>');

   loNavDoc.writeln('<base href="' + lsBaseHREF + '">');
   loNavDoc.writeln('<link type="text/css" rel="STYLESHEET" href="../AMSImages/ADVStyle.css">');

   loNavDoc.writeln('</HEAD>');

   loNavDoc.writeln('<BODY oncontextmenu="return false;" bgcolor="#c0c0c0" link="#0000ff" alink="#0000ff" vlink="#0000ff" topmargin=2 leftmargin=0 marginwidth=0 marginheight=0>');

   loNavDoc.writeln('<table cellspacing=0 cellpadding=0 border=0 width="100%"><tr>');

   if(gsTitle.length > 0)
   {
      loNavDoc.writeln('<td>&nbsp;</td>');
      loNavDoc.write('<td bgcolor="#000000">');
   }
   else
      loNavDoc.write('<td>&nbsp;');

   loNavDoc.write('<a href="javascript:top.frames[\'Master\'].goFirstPage();"><img src="' + gsRewImg[0] + '" border=0 width=23 height=25 name="rewpg" alt="First Page"></a>');
   loNavDoc.write('<a href="javascript:top.frames[\'Master\'].goPrevPage();"><img src="' + gsPrevImg[0] + '" border=0 width=23 height=25 name="prevpg" alt="Previous Page"></a>');
   loNavDoc.write('<a href="javascript:top.frames[\'Master\'].goNextPage();"><img src="' + gsNextImg[nIconNum] + '" border=0 width=23 height=25 name="nextpg" alt="Next Page"></a>');
   loNavDoc.write('<a href="javascript:top.frames[\'Master\'].goLastPage();"><img src="' + gsFfwdImg[nIconNum] + '" border=0 width=25 height=25 name="ffwdpg" alt="Last Page"></a>');

   loNavDoc.write('<img src="' + gsSepImg + '" border=0 width=4 height=25>');

   loNavDoc.write('<a href="javascript:top.frames[\'Master\'].goHstBwd();"><img src="' + gsHstBwdImg[0] + '" border=0 width=23 height=25 name="hstbwd" alt="Go to Previous View"></a>');
   loNavDoc.write('<a href="javascript:top.frames[\'Master\'].goHstFwd();"><img src="' + gsHstFwdImg[0] + '" border=0 width=23 height=25 name="hstfwd" alt="Go to Next View"></a>');


   if(gsTitle.length > 0)
   {
      loNavDoc.writeln('<td bgcolor="#000000" align="right">');

      if(gsLink.length > 0)
      {
         loNavDoc.write('<a href="' + gsLink + '"');

         if(gsTarget.length > 0)
            loNavDoc.write(' target="' + gsTarget + '">');
         else
            loNavDoc.write(' target="_blank">');
      }

      loNavDoc.write('<b><font color="gold" size="2" face="Helvetica">' + gsTitle + '</b>');

      if(gsLink.length > 0)
         loNavDoc.write('</a>');

      loNavDoc.writeln('</td><td bgcolor="black">&nbsp;&nbsp;</td>');
   }

   loNavDoc.writeln('</tr></table>');

   loNavDoc.writeln('</BODY>');
   loNavDoc.writeln('</HTML>');
   loNavDoc.close();
}


function goFirstPage()
{
   openPage(gFirstPage, true);
}


function goLastPage()
{
   openPage(gLastPage, true);
}


function goPrevPage()
{
   var loPageNum = gCurrPage - 1;

   if(loPageNum < gFirstPage)
      return;

   openPage(loPageNum, true);
}


function goNextPage()
{
   var loPageNum = gCurrPage + 1;

   if(loPageNum > gLastPage)
      return;

   openPage(loPageNum, true);
}


function goHstBwd()
{
   var loPageNum;

   if(gnCurrHstPos > 0)
   {
      gnCurrHstPos = gnCurrHstPos - 1;
      loPageNum = gnHstList[gnCurrHstPos];

      updatePage(loPageNum);
      updateContents(loPageNum);
      updateBar(loPageNum);
      gCurrPage = loPageNum;
   }
}


function goHstFwd()
{
   var loPageNum;

   if(gnHstList[gnCurrHstPos + 1] > 0)
   {
      gnCurrHstPos = gnCurrHstPos + 1;
      loPageNum = gnHstList[gnCurrHstPos];

      updatePage(loPageNum);
      updateContents(loPageNum);
      updateBar(loPageNum);
      gCurrPage = loPageNum;
   }
}


function openPage(foPageNum, doContents)
{
   if(foPageNum >= gFirstPage && foPageNum <= gLastPage)
   {
      gnCurrHstPos = gnCurrHstPos + 1;
      gnHstList[gnCurrHstPos] = foPageNum;
      gnHstList[gnCurrHstPos + 1] = 0;

      updatePage(foPageNum);
      if (doContents) updateContents(foPageNum);
      updateBar(foPageNum);
      gCurrPage = foPageNum;
   }
}


function updatePage(foPageNum)
{
   submitCurrentPage(foPageNum);
}


function submitCurrentPage(foPageNum)
{
   var loQueryStr = 'show_html=show_html&report_num=' + foPageNum;
   submitForm(document.Report_Driver_Pg, loQueryStr, 'ReportDetail');

   UTILS_InitPage();
}


function updateContents(foPageNum)
{
   setCurrentPageIcon(foPageNum);
}


function setCurrentPageIcon(foPageNum)
{
   for (loEntryNum=0; loEntryNum<giOutlinePage.length; loEntryNum++)
   {
      if (giOutlinePage[loEntryNum] == foPageNum)
      {
         setCurrentEntryIcon(loEntryNum);
         break;
      }
   }
}


function updateBar(foPageNum)
{
   var rewPrevIcon = 0;
   var ffwdNextIcon = 0;
   var hstFwdIcon = 0;
   var hstBwdIcon = 0;

   if(gFirstPage != gLastPage)
   {
      if(foPageNum == gFirstPage)
         ffwdNextIcon = 1;
      else if(foPageNum == gLastPage)
         rewPrevIcon = 1;
      else if(foPageNum > gFirstPage || foPageNum < gLastPage)
      {
         rewPrevIcon = 1;
         ffwdNextIcon = 1;
      }
   }

   var navFrame = top.frames[REPORT_NAV_FRAME];

   navFrame.document.nextpg.src = gsNextImg[ffwdNextIcon];
   navFrame.document.ffwdpg.src = gsFfwdImg[ffwdNextIcon];
   navFrame.document.rewpg.src = gsRewImg[rewPrevIcon];
   navFrame.document.prevpg.src = gsPrevImg[rewPrevIcon];

   if(gnCurrHstPos > 0)
      hstBwdIcon = 1;
   if(gnHstList[gnCurrHstPos + 1] > 0)
      hstFwdIcon = 1;

   navFrame.document.hstfwd.src = gsHstFwdImg[hstFwdIcon];
   navFrame.document.hstbwd.src = gsHstBwdImg[hstBwdIcon];
}
