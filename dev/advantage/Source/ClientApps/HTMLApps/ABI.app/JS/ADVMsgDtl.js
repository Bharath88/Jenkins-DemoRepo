/*
 * @(#)ADVMsgDtl.js     1.0 10/05/2000
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
 * JavaScript Dependencies : Jade Script
 *
 * PVCS Modification Log:
 *
 * $Log:   N:/ASG/Projects/ADVANTAGE_3.1_V55_Infrastructure/archives/dev/advantage/Source/ClientApps/HTMLApps/Advantage.app/JS/ADVMsgDtl.js-arc  $

   Rev 1.1   16 Sep 2002 16:31:06   MShipley
Fixed unconverted jade_navigation

   Rev 1.0   20 Aug 2002 15:12:46   SCOLONEY
Initial revision.

   Rev 1.0   Apr 13 2002 14:20:50   SColoney
Initial revision.

   Rev 1.3   Oct 08 2001 23:45:36   RApparaj
Advantage EJB conversion (Build 5)

   Rev 1.2   Oct 08 2001 20:34:52   RApparaj
Versata EJB changes(Build 5)

   Rev 1.1   Jun 29 2001 12:38:46   SSurabhi
Changes to reset the transaction flag as soon as the browser begins painting the page

   Rev 1.0   Jun 24 2001 09:06:06   SColoney
Initial revision.

   Rev 1.1   Oct 05 2000 15:36:40   MShipley
Added dependency list

   Rev 1.0   Oct 05 2000 14:40:12   SCOLONEY
Initial revision.
 */

/**
 * This method takes a form as input and displays a new HTML page
 * in the bottom frame of the AMSErrorsPage. A figure and a message
 * corresponding to the selected message on the
 * Errors Page are displayed. An ErrorCode is also displayed.
 * @param foForm A form
 */

function MSGDTL_ShowDetail(fsMsgCd)
{
   var lsQueryStr;
   var lsTargetFrame = 'ErrorDetail';

   lsQueryStr = 'T2AMSErrorDetail=T2AMSErrorDetail'
              + '&vsnavigation=T2AMSErrorDetail'
              + '&MSG_CD=' + fsMsgCd;

   UTILS_StopRegisteringTransactions();
   submitForm(document.AMSErrorsPage,lsQueryStr, lsTargetFrame);
}

function MSGDTL_PerformContextNavigation(fsContext, fsWinName,
                                         fsComponent, fsAttribute, fsMessage)
{
   var lsQueryStr = 'ContextNavigation=ContextNavigation'
              + '&context_info=' + fsContext
              + '&attribute=' + fsAttribute
              + '&component=' + fsComponent
              + '&message=' + fsMessage;
              
   if( mboolIsIE )
   {
	   parent.opener.parent.document.frames[fsWinName].focus();
   }

   submitForm(document.AMSErrorsPage,lsQueryStr, fsWinName);
}

function MSGDTL_ShowOtherDetail(fsMsgCd)
{
   var lsQueryStr;
   var lsTargetFrame = 'ErrorDetail';

   lsQueryStr = 'T3AMSEMEXErrorDetail=T3AMSEMEXErrorDetail'
              + '&vsnavigation=T3AMSEMEXErrorDetail'
              + '&MSG_CD=' + fsMsgCd;

   UTILS_StopRegisteringTransactions();
   submitForm(document.AMSErrorsPage,lsQueryStr, lsTargetFrame);
}

