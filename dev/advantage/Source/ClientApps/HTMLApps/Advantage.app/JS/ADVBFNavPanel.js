/*
 * @(#)ADVBFNavPanel.js     1.0 12/11/2000
 *
 * Copyright 2000 by American Management Systems, Inc.,
 * 4050 Legato Road, Fairfax, Virginia, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of American Management Systems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with American Management Systems, Inc.
 *
 * JavaScript Dependencies : Jade Script, AMSDHTMLLib
 *
 * PVCS Modification Log:
 *
 * $Log:   N:/ASG/Projects/AMS_ADVANTAGE_Technical_Architecture/archives/dev/advantage/Source/ClientApps/HTMLApps/Advantage.app/JS/ADVBFNavPanel.js-arc  $

   Rev 1.2   09 Dec 2003 18:32:40   mshipley
IR-HRFX4976: Set the target frame for business function pages

   Rev 1.1   19 Oct 2003 11:53:50   mshipley
Accessibility changes

   Rev 1.0   20 Aug 2002 15:12:44   SCOLONEY
Initial revision.

   Rev 1.1   27 Jun 2002 17:23:00   SNaikoti
IR-NFFX006116: Added CONSTANTS for hard-coded values where needed.

   Rev 1.0   Apr 13 2002 14:20:46   SColoney
Initial revision.

   Rev 1.3   Nov 01 2001 16:32:18   SSurabhi
Changes for Netscape61

   Rev 1.0   Jun 24 2001 09:06:04   SColoney
Initial revision.

   Rev 1.3   Jun 21 2001 19:42:50   SDas
Surabhi changes

   Rev 1.2   Jun 20 2001 10:52:14   JXP
Removed the logic for confirmation. Requested by system test.

   Rev 1.1   Feb 26 2001 17:38:08   MShipley
Improved document closing logic

   Rev 1.0   Dec 13 2000 15:27:46   rapparaj
Initial revision.
 *
 */

var msBFTREE_BAs    = new Array() ;
var moCurrBFItemImg = null ;

var BFNAVPANEL_DISPLAY_FRAME = 'Display';
var BFNAVPANEL_BFNAV_FRAME   = 'BFNav';

function BFNAVPANEL_OpenBA( fiBAID )
{
   UTILS_HideDocNavPanel() ;
   UTILS_ShowBFNavPanel() ;
   BFNAVPANEL_HideRestart() ;
   BFNAVPANEL_HideSave() ;
   submitForm( document.BFNavPanel, 'CurrBAID=' +  fiBAID, BFNAVPANEL_BFNAV_FRAME ) ;
} 

function BFNAVPANEL_OpenBF( fiBAID, fiBFID, fiBFItemSeqNo )
{
   UTILS_HideDocNavPanel() ;
   UTILS_ShowBFNavPanel() ;
   BFNAVPANEL_HideRestart() ;
   BFNAVPANEL_HideSave() ;
   submitForm( document.BFNavPanel, 'CurrBAID=' + fiBAID + '&CurrBFID=' + fiBFID + '&BFItemSeqNo=' + fiBFItemSeqNo, BFNAVPANEL_BFNAV_FRAME ) ;
} 

function BFNAVPANEL_OpenBFPage( fiSeqNo, fsTargetFrame )
{
   var loBFItemImg = eval( 'document.BFItem_' + fiSeqNo ) ;

   if ( ( fsTargetFrame == null ) || ( fsTargetFrame.length == 0 ) )
   {
      fsTargetFrame = BFNAVPANEL_DISPLAY_FRAME ;
   }

   BFNAVPANEL_ShowRestart() ;
   BFNAVPANEL_ShowSave() ;
   BFNAVPANEL_ShowSaveAll() ;
   if ( moCurrBFItemImg != null )
   {
      DHTMLLIB_SetAttributeValue( moCurrBFItemImg, "src", "../AMSImages/ADVNavVisited.gif" ) ;
      DHTMLLIB_SetAttributeValue( moCurrBFItemImg, "alt", "Visited" ) ;
      DHTMLLIB_SetAttributeValue( moCurrBFItemImg, "title", "Visited" ) ;
   } 
   if ( loBFItemImg != null )
   {
      DHTMLLIB_SetAttributeValue( loBFItemImg, "src", "../AMSImages/ADVNavCurr.gif" ) ;
      DHTMLLIB_SetAttributeValue( loBFItemImg, "alt", "Current" ) ;
      DHTMLLIB_SetAttributeValue( loBFItemImg, "title", "Current" ) ;
      moCurrBFItemImg = loBFItemImg ;
   }
   submitForm( document.BFNavPanel, 'OpenBFPage=OpenBFPage&ams_bfcurrpage=' + fiSeqNo, fsTargetFrame ) ;
} 

function BFNAVPANEL_Close()
{
   UTILS_HideDocNavPanel() ;
   UTILS_ShowBFNavPanel() ;
   UTILS_ShowAppNavPanel() ;
   BFNAVPANEL_HideRestart() ;
   BFNAVPANEL_HideSave() ;
   BFNAVPANEL_HideSaveAll() ;

   UTILS_StopRegisteringTransactions();
   submitForm( document.BFNavPanel, 'HomePgTrans=HomePgTrans', BFNAVPANEL_DISPLAY_FRAME ) ;
   submitForm( document.BFNavPanel, 'CloseWksp=CloseWksp&CloseBFNavPanel=true', BFNAVPANEL_BFNAV_FRAME ) ;
   UTILS_StartRegisteringTransactions();
} 

function BFNAVPANEL_SaveBF()
{
   BFNAVPANEL_HideSave() ;
   submitForm( document.BFNavPanel, 'SaveBF=SaveBF', BFNAVPANEL_BFNAV_FRAME ) ;
} 

function BFNAVPANEL_SaveWksp()
{
   BFNAVPANEL_HideSave() ;
   submitForm( document.BFNavPanel, 'SaveWksp=SaveWksp', BFNAVPANEL_BFNAV_FRAME ) ;
} 

function BFNAVPANEL_RestartBF()
{
   BFNAVPANEL_ShowSave() ;
   submitForm( document.BFNavPanel, 'RestartBF=RestartBF', BFNAVPANEL_BFNAV_FRAME ) ;
} 

function BFNAVPANEL_ShowSaveAll()
{
   var loLayer = DHTMLLIB_GetLayer( 'BFSaveAll' ) ;

   if ( loLayer != null )
   {
      document.forms[0].ShowSaveAll.value=true ;
      loLayer.style.visibility = 'visible';
      loLayer.style.display = 'block';
   } 
}

function BFNAVPANEL_HideSaveAll()
{
   var loLayer = DHTMLLIB_GetLayer( 'BFSaveAll' ) ;

   if ( loLayer != null )
   {
      document.forms[0].ShowSaveAll.value=false ;
      loLayer.style.visibility = 'hidden';
      loLayer.style.display = 'none';
   } 
}

function BFNAVPANEL_ShowRestart()
{
   var loLayer = DHTMLLIB_GetLayer( 'BFRestart' ) ;

   if ( loLayer != null )
   {
      document.forms[0].ShowRestart.value=true ;
      loLayer.style.visibility = 'visible';
      loLayer.style.display = 'block';
   }
}

function BFNAVPANEL_HideRestart()
{
   var loLayer = DHTMLLIB_GetLayer( 'BFRestart' ) ;

   if ( loLayer != null )
   {
      document.forms[0].ShowRestart.value=false ;
      loLayer.style.visibility = 'hidden';
      loLayer.style.display = 'none';
   } 
}

function BFNAVPANEL_ShowSave()
{
   var loLayer = DHTMLLIB_GetLayer( 'BFSave' ) ;

   if ( loLayer != null )
   {
      document.forms[0].ShowSave.value=true ;
      loLayer.style.visibility = 'visible';
      loLayer.style.display = 'block';
   } 
}

function BFNAVPANEL_HideSave()
{
   var loLayer = DHTMLLIB_GetLayer( 'BFSave' ) ;

   if ( loLayer != null )
   {
      document.forms[0].ShowSave.value=false ;
      loLayer.style.visibility = 'hidden';
      loLayer.style.display = 'none';
   }
}

