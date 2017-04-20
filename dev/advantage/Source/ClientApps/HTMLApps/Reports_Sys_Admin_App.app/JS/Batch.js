function CFP_Confirm_Delete()
{
   if (confirm(
      "Are you sure you want to delete the folder and all its contents"))
   {
      document.Catalog_Folder_Pg.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}


function disable_PickField()
{
   window.event.srcElement.disabled=true;
}

function clear_Value(obj)
{
   obj.value="";
}


function CLP_Confirm_Delete()
{
   if (confirm("Are you sure you want to delete the item"))
   {
      document.Catalog_LeafNode_Pg.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}

function CCP_Confirm_Delete()
{
   if (confirm("Are you sure you want to delete the chain job"))
   {
      document.Catalog_ChainJob_Pg.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}

function Chain_Job_Schedule_Confirm_Delete()
{
   if (confirm("Are you sure you want to delete the job"))
   {
      document.Chain_Job_Schedule.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}

function Add_New_Job_Confirm_Delete()
{
   if (confirm("Are you sure you want to delete the job"))
   {
      document.Add_New_Job.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}


function Chain_Jobs_Pending_User_Confirm_Delete()
{
   if (confirm("Are you sure you want to delete the job"))
   {
      document.View_Pending_Chain_Jobs.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}

function Jobs_Pending_User_Confirm_Delete()
{
   if (confirm("Are you sure you want to delete the job"))
   {
      document.Jobs_Pending_User.ConfirmDelete.value="Yes";
   }
   else
   {
      return
   }
}


function Jobs_Pending_User_Confirm_Kill()
{
   if (confirm("Are you sure you want to kill the job"))
   {
      document.Jobs_Pending_User.ConfirmKill.value="Yes";
   }
   else
   {
      return
   }
}

function Chain_Jobs_Pending_User_Confirm_Kill()
{
   if (confirm("Are you sure you want to kill the job"))
   {
      document.View_Pending_Chain_Jobs_List.ConfirmKill.value="Yes";
   }
   else
   {
      return
   }
}

function Add_Child_Node_View_Ctrl()
{
   var Folder  = 'Folder';
   var Report  = 'Report';
   var Batch   = 'System Batch';
   var BO      = 'Business Object';
   var Actuate = 'Actuate Report';
   var Chain   = 'Chain Job';

   var liSelIndex = document.Add_Child_Node.txtT1ITM_TYP.selectedIndex;

   var selectedText =
              document.Add_Child_Node.txtT1ITM_TYP.options[liSelIndex].text;

   if ( selectedText == Folder)
   {
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Application_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Application_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Job_Q_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Job_Q_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Label_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Label_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Class_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Class_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Package_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Package_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Rqrs_Aprv_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Rqrs_Aprv_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('App_Server_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('App_Server_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Single_Run_Proc_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Single_Run_Proc_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_App_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_App_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_Pg_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_Pg_Name_FLD'));

      document.Add_Child_Node.Mode.value="Folder";
   }

   if ( selectedText == Chain )
   {
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Application_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Application_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Job_Q_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Job_Q_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Label_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Label_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Class_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Class_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Package_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Package_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Rqrs_Aprv_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Rqrs_Aprv_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('App_Server_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('App_Server_Name_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Single_Run_Proc_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Single_Run_Proc_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_App_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_App_Name_FLD'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_Pg_Name_LBL'));
      DHTMLLIB_HideLayer(DHTMLLIB_GetLayer('Cust_Pg_Name_FLD'));

      document.Add_Child_Node.Mode.value="Chain";
   }

   if ( (selectedText == Batch) || (selectedText == Report) )
   {
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Application_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Application_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Job_Q_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Job_Q_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Label_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Label_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Class_Name_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Class_Name_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Package_Name_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Package_Name_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Rqrs_Aprv_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Rqrs_Aprv_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('App_Server_Name_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('App_Server_Name_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Single_Run_Proc_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Single_Run_Proc_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Cust_App_Name_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Cust_App_Name_FLD'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Cust_Pg_Name_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Cust_Pg_Name_FLD'));

      document.Add_Child_Node.Mode.value="Batch";
   }
   //Display Resource ID for any Item type except blank value
   if( (selectedText != null) && (selectedText.length != 0) )
   {
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Resource_ID_LBL'));
      DHTMLLIB_ShowLayer(DHTMLLIB_GetLayer('Resource_ID_FLD'));
   }
}




function Add_Chain_Job_Schedule_View_Ctrl()
{
   var CustomRun= 'Custom';

   var liSelIndex = document.Chain_Job_Schedule.txtT1RUN_PLCY.selectedIndex;
   var lsSelText = document.Chain_Job_Schedule.txtT1RUN_PLCY.options[liSelIndex].text;

   if ( (lsSelText == CustomRun) )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer('CustomLabel'));
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer('CustomField'));
      document.Chain_Job_Schedule.CustomRun.value='Yes';
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer('CustomLabel'));
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer('CustomField'));
      document.Chain_Job_Schedule.CustomRun.value='No';
   }
} 



function Add_New_Job_View_Ctrl()
{
   var CustomRun= 'Custom';
   var liSelIndex = document.Add_New_Job.txtT1RUN_PLCY.selectedIndex;
   var lsSelText = document.Add_New_Job.txtT1RUN_PLCY.options[liSelIndex].text;

   if ( lsSelText == CustomRun )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer('CustomLabel'));
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer('CustomField'));
      document.Add_New_Job.CustomRun.value='Yes';
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer('CustomLabel'));
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer('CustomField'));
      document.Add_New_Job.CustomRun.value='No';
   }
} 
