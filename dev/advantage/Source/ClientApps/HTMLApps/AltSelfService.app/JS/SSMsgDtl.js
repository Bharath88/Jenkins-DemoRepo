function MSGDTL_ShowDetail(fsMsgCd)
{
   var lsQueryStr;
   var lsTargetFrame = 'ErrorDetail';

   lsQueryStr = 'T2AltSSErrorDetail=T2AltSSErrorDetail'
              + '&vsnavigation=T2AltSSErrorDetail'
              + '&MSG_CD=' + fsMsgCd;

   UTILS_StopRegisteringTransactions();
   submitForm(document.AltSSErrorsPage,lsQueryStr, lsTargetFrame);
}


