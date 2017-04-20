
var TABNAV_Name = 0;
var TABNAV_ID = 1;

var TABNAV_FirstTabLevel = new Array(new Array(), new Array());
var TABNAV_SecondTabLevel = new Array(new Array(), new Array());

var TABNAV_SelectedFirstLevel;
var TABNAV_SelectedSecondLevel;

var TABNAV_NumberOfFirstLevel = 0;
var TABNAV_NumberOfSecondLevel = 0;

function TABNAV_AddTabToFirstLevel(fsTabToAdd, fiID)
{
   TABNAV_FirstTabLevel[TABNAV_Name][TABNAV_NumberOfFirstLevel] = fsTabToAdd;
   TABNAV_FirstTabLevel[TABNAV_ID][TABNAV_NumberOfFirstLevel] = fiID;
   TABNAV_NumberOfFirstLevel++;
}   

function TABNAV_AddTabToSecondLevel(fsTabToAdd, fiID)
{
   TABNAV_SecondTabLevel[TABNAV_Name][TABNAV_NumberOfSecondLevel] = fsTabToAdd;
   TABNAV_SecondTabLevel[TABNAV_ID][TABNAV_NumberOfSecondLevel] = fiID;
   TABNAV_NumberOfSecondLevel++;
}   

function TABNAV_SetSelectedFirstLevel(fsSelectedTab)
{
   TABNAV_SelectedFirstLevel = fsSelectedTab;
}

function TABNAV_SetSelectedSecondLevel(fsSelectedTab)
{
   TABNAV_SelectedSecondLevel = fsSelectedTab;	
}

function TABNAV_DisplayTabs( )
{
   var liLoopCount;
   var lsTabLink;
   document.open();
   
   if(TABNAV_NumberOfFirstLevel > 0)
   {
      document.writeln("<table cellspacing=0 cellpadding=0 class=tableveloneframe >");
      document.writeln("<tr class=tablevelone>");
      document.writeln("<td class=tablevelonefiller></td>");
      for(liLoopCount = 0; liLoopCount < TABNAV_NumberOfFirstLevel; liLoopCount++)
      {
         lsTabLink = "<a href=\"JavaScript:submitForm(document.StartupPage,'menu_action=menu_action&ams_action=first_level_tab&ams_sub_action=" 
            + TABNAV_FirstTabLevel[TABNAV_ID][liLoopCount] 
            + "','Startup')\">" 
            + TABNAV_FirstTabLevel[TABNAV_Name][liLoopCount] 
            + "</a>";
            
         if(TABNAV_FirstTabLevel[TABNAV_ID][liLoopCount] == TABNAV_SelectedFirstLevel)
         {
            document.writeln("<td class=\"tableveloneon leftcorner\"><img src=\"Images/TabLevelOneONLeft.gif\"></td>");
            document.writeln("<td class=tableveloneon>");
            document.writeln( lsTabLink );
            document.writeln("</td>");
            document.writeln("<td class=\"tableveloneon rightcorner\"><img src=\"Images/TabLevelOneONRight.gif\"></td>");
         }
         else
         {
            document.writeln("<td class=\"tableveloneoff leftcorner\"><img src=\"Images/TabLevelOneOFFLeft.gif\"></td>");
            document.writeln("<td class=tableveloneoff>");
            document.writeln( lsTabLink );
            document.writeln("</td>");
            document.writeln("<td class=\"tableveloneoff rightcorner\"><img src=\"Images/TabLevelOneOFFRight.gif\"></td>");
         }
      }
      document.writeln("<td width=\"60%\">&nbsp;</td>");
      document.writeln("</tr>");
      document.writeln("</table>");

      if(TABNAV_NumberOfSecondLevel > 0)
      {
         document.writeln("<table cellspacing=0 cellpadding=0 class=tableveltwoframe>");
         document.writeln("<tr class=tableveltwo>");
         var lboolIsPreviousON = true;
         for(liLoopCount = 0; liLoopCount < TABNAV_NumberOfSecondLevel; liLoopCount++)
         {
            lsTabLink = "<a href=\"JavaScript:submitForm(document.StartupPage,'menu_action=menu_action&ams_action=second_level_tab&ams_sub_action=" 
               + TABNAV_SecondTabLevel[TABNAV_ID][liLoopCount] 
               + "','Startup')\">" + TABNAV_SecondTabLevel[TABNAV_Name][liLoopCount] 
               + "</a>";
               
            if(TABNAV_SecondTabLevel[TABNAV_ID][liLoopCount] == TABNAV_SelectedSecondLevel)
            {
               document.writeln("<td class=\"tableveltwoon leftcorner\"><img src=\"Images/TabLevelTwoONLeft.gif\"></td>");
               document.writeln("<td class=tableveltwoon>");
               document.writeln( lsTabLink );
               document.writeln("</td>");
               document.writeln("<td class=\"tableveltwoon rightcorner\"><img src=\"Images/TabLevelTwoONRight.gif\"></td>");
               lboolIsPreviousON = true;
            }
            else
            {
               if( lboolIsPreviousON )
               {
                  lboolIsPreviousON = false;
            }
            else
            {
               document.writeln("<td class=tableveltwofiller> | </td>");
            }
            document.writeln("<td class=tableveltwooff>");
            document.writeln( lsTabLink );
            document.writeln("</td>")
            }
         }
         
         if(TABNAV_NumberOfSecondLevel > 1)
         {
            document.writeln("<td width=\"60%\">&nbsp;</td>");
         }
         else
         {
            document.writeln("<td width=\"90%\">&nbsp;</td>");
         }
         document.writeln("</tr>");
         document.writeln("</table>");
      }
   }
   TABNAV_NumberOfFirstLevel = 0;
   TABNAV_NumberOfSecondLevel = 0;
}
