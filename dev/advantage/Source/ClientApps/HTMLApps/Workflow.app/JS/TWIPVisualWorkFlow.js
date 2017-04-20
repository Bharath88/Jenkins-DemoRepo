function setPageSize()
{
   var MAX_SEQUENCE_PER_LAYER= 5;
   var liMinWidth=285;
   var liMinHeight=40;
   

   if(mboolIsIE)
   {
      if(document.getElementById("visualWorkflow")!=null)
      {
         var liTotalCount=$('.gwt-HTML').length;
         var loRaphaelLinkElement=document.getElementById("raphaelLinks");
         if(loRaphaelLinkElement)
         {
            loRaphaelLinkElement.style.height=(liTotalCount*liMinHeight)+"px";
            loRaphaelLinkElement.style.width=(MAX_SEQUENCE_PER_LAYER * liMinWidth) +"px";
         }
      }
   }
   else
   {
      if(document.getElementById("visualWorkflow")!=null)
       {
          var liTotalCount=$('.gwt-HTML').length;
          var laSVGElement=document.getElementsByTagName('svg');
          
          if(laSVGElement && laSVGElement[0])
           {
              var loSVGElement=laSVGElement[0];
              loSVGElement.style.height=(liTotalCount*liMinHeight)+"px";
              loSVGElement.style.width=(MAX_SEQUENCE_PER_LAYER * liMinWidth) +"px";
           }  
        }

    }
}


function TWIPVisualWorkFlow_toggleView(view)
{
    var loshowGridLink=$('#showGrid');
    var loshowVisualLink=$('#showVisual');
    var loAprvSheetWrapper=$('#AprvSheetWrapper');
    var lovisualWorkflowLegend=$('#visualWorkflowLegend');
    var lovisualWorkflow=$('#visualWorkflow');
    var lovDivCurrAssignee=$('#DivCurrAssignee');
    
    var loDispFrameForm = top.frames['Display'].document.forms[0];
    
    if(view =="Visual")
    {
        loDispFrameForm.PageType.value = "DocStdUI";
        UTILS_setupSecondaryNavPanel();
        $(document.body).css('background-image', 'none');
        lovDivCurrAssignee.css('display','none');
        loshowGridLink.css('display','block');
        loshowVisualLink.css('display','none');
        loAprvSheetWrapper.css('display','none');
        lovisualWorkflowLegend.css('display','block');
        lovisualWorkflow.css('display','block');
    }
    else
    {
        loDispFrameForm.PageType.value = "";
        UTILS_setupSecondaryNavPanel();
        $(document.body).css('background-image','url("../AMSImages/BodyBg.gif")');
        loshowGridLink.css('display','none');
        lovDivCurrAssignee.css('display','block');
        loshowVisualLink.css('display','block');
        loAprvSheetWrapper.css('display','block');
        lovisualWorkflowLegend.css('display','none');
        lovisualWorkflow.css('display','none');
    }

}





   
