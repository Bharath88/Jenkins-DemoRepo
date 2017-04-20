var moDocStaCdRejected;
var moDocStaCdSubmitted;

function AMSSYSMANPARM_ProcLoadPage()
{
   
   AMSSYSMANPARM_SetParmDivs() ;

   
   AMSSYSMANPARM_DisableExpandCollapse() ;

   
   DHTMLLIB_HideLayerWithSpace( DHTMLLIB_GetLayer( 'AMSDummy' ) )

   
   if ( document.forms[0].txtT1PARM_FILE.value == "" )
   {
      AMSSYSMANPARM_ProcActnCd() ;
   }
   
   
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'advExccepInfoMaxNo1' ) ) ;
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'advExccepInfoMaxNo2' ) ) ;
   
} 

function AMSSYSMANPARM_ProcParmFile()
{

   if ( document.forms[0].txtT1PARM_FILE.value == "" )
    {

      AMSSYSMANPARM_EnableField( 'txtT1ACTN_CD' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_S_ACTN_CD' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1GENERATE_STATS' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1USER_ID' ) ;
   }
   else
   {

      AMSSYSMANPARM_SetParmDivs() ;
      AMSSYSMANPARM_DisableExpandCollapse() ;

      AMSSYSMANPARM_DisableField( 'txtT1ACTN_CD' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_S_ACTN_CD' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1GENERATE_STATS' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1USER_ID' ) ;
      document.getElementById('ProgCtrSz').style.display ="none" ;
      document.getElementById('ADVApproveId').style.display ="none";
      document.getElementById('ADVApproveRole').style.display ="none";
      document.getElementById('ADVApproveLevel').style.display ="none";
      document.getElementById('ADVApplyOverride').style.display ="none";
   }


   if ( ( document.forms[0].txtT1ACTN_CD.value != "" ) &&
        ( document.forms[0].txtT1PARM_FILE.value != "" ) )
   {
      AMSSYSMANPARM_DisableField( 'txtT1ACTN_CD' ) ;
   }
} 

function AMSSYSMANPARM_ProcActnCd()
{
   var liSelectedValue = document.forms[0].txtT1ACTN_CD.value;


   switch ( liSelectedValue )
   {
   
      case "102" :
      case "103" :
      case "111" :
      case "112" :
      case "113" :
      case "131" :
      case "151" :
      case "152" :
      case "153" :
      case "161" :
      case "162" :
      case "171" :
      case "172" :
      case "186" :
      case "191" :
      case "192" :
      case "181" :
      case "200" :
         AMSSYSMANPARM_SetDocDivs(liSelectedValue) ;
         break ;
      case "201" :
      case "202" :
      case "203" :
      case "204" :
      case "205" :
      case "206" :
      case "208" :
         AMSSYSMANPARM_SetTableDivs(liSelectedValue) ;
         break ;
      default :
         AMSSYSMANPARM_SetParmDivs(liSelectedValue) ;
         AMSSYSMANPARM_DisableExpandCollapse() ;
         break ;
   }
   AMSSYSMANPARM_SetProgCtr(liSelectedValue) ;
   AMSSYSMANPARM_SetADVApproveInfo(liSelectedValue) ;
   AMSSYSMANPARM_SetAplyOver(liSelectedValue) ;
}


 /* 
  * This method is used to enable or disable the Cleanup field based on Phase code
  * Required only for Document Action Discard. 
  */
function AMSSYSMANPARM_ProcDocPhaseCd()
{
   var liSelectedActn = document.forms[0].txtT1ACTN_CD.value;
   if( liSelectedActn != '153' )
   {
      return;
   }

   var liSelectedPhase = document.forms[0].txtT1DOC_PHASE_CD.value;
   if( liSelectedPhase == '1' || liSelectedPhase == '6')
   {
      AMSSYSMANPARM_EnableField( 'txtT1CLEANUP_PERIOD' ) ;
   }
   else
   {
      AMSSYSMANPARM_DisableField( 'txtT1CLEANUP_PERIOD' ) ; 
   }
}

/*
 * This function enables the passed field.
 *
 * Modification Log : Siddhartha Das - 04/30/2001
 *                                 - initial version
 *
 * fsField - The name of the field
 */
function AMSSYSMANPARM_EnableField( fsField )
{
   document.forms[0][fsField].className = "" ;
   document.forms[0][fsField].disabled  = false ;
} /* end AMSSYSMANPARM_EnableField() */

/*
 * This function disables the passed field.
 *
 * Modification Log : Siddhartha Das - 04/30/2001
 *                                   - initial version
 *
 * fsField - The name of the field
 */
function AMSSYSMANPARM_DisableField( fsField )
{
   document.forms[0][fsField].className = "advreadonly" ;
   document.forms[0][fsField].disabled  = true ;
   document.forms[0][fsField].value = document.forms[0][fsField].defaultValue ;
   if ( fsField == "txtT1DOC_S_ACTN_CD" )
   {
      document.forms[0][fsField].value = "" ;
   }
} 

function AMSSYSMANPARM_SetParmDivs()
{
   AMSSYSMANPARM_DisableExpandCollapse() ;
   
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVDocInfo' ) ) ;
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVTableInfo' ) ) ;
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVImpExpInfo' ) ) ;
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVExcepInfo' ) ) ;
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVRestartInfo' ) ) ;
  
}

function AMSSYSMANPARM_SetActnCdDivs()
{
   AMSSYSMANPARM_EnableExpandCollapse() ;

   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVDocInfo' ) ) ;
   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVTableInfo' ) ) ;
   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVImpExpInfo' ) ) ;
   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVExcepInfo' ) ) ;
   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVRestartInfo' ) ) ;
} 

function AMSSYSMANPARM_SetProgCtr(fiSelectedValue)
{
   if (fiSelectedValue == "162" || fiSelectedValue == "181")
   {
      document.getElementById('ProgCtrSz').style.display ="";
   }
   else
   {
      document.getElementById('ProgCtrSz').style.display ="none";
   }
	
}

function AMSSYSMANPARM_SetAplyOver(fiSelectedValue)
{
   if (fiSelectedValue == "171" || fiSelectedValue == "181")
   {
      document.getElementById('ADVApplyOverride').style.display ="";
   }
   else
   {
      document.getElementById('ADVApplyOverride').style.display ="none";
   }
	
}

function AMSSYSMANPARM_SetADVApproveInfo(fiSelectedValue)
{
   if (fiSelectedValue == "181")
   {
      document.getElementById('ADVApproveId').style.display ="";
      document.getElementById('ADVApproveRole').style.display ="";
      document.getElementById('ADVApproveLevel').style.display ="";      
   }
   else if(fiSelectedValue == "171")
   {
      document.getElementById('ADVApplyOverride').style.display ="";
   }
   else
   {
      document.getElementById('ADVApproveId').style.display ="none";
      document.getElementById('ADVApproveRole').style.display ="none";
      document.getElementById('ADVApproveLevel').style.display ="none";
      document.getElementById('ADVApplyOverride').style.display ="none";
   }
	
}

function AMSSYSMANPARM_SetDocDivs(fiSelectedValue)
{
   AMSSYSMANPARM_EnableExpandCollapse() ;

   if (fiSelectedValue != "200")
   {
      AMSSYSMANPARM_DisableField( 'txtT1DOC_S_ACTN_CD' );
   }
   else
   {
      AMSSYSMANPARM_EnableField( 'txtT1DOC_S_ACTN_CD' );
   }

   if (fiSelectedValue == "181")
   {
      AMSSYSMANPARM_DisableField( 'txtT1USER_ID' );
   }
   else
   {
      AMSSYSMANPARM_EnableField( 'txtT1USER_ID' );
   }

   if ( ( fiSelectedValue == "111" ) ||
      ( fiSelectedValue == "112" ) ||
      ( fiSelectedValue == "113" ) )
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVDocInfo' ) );
   }
   else
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVDocInfo' ) );
   }

   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVTableInfo' ) ) ;

   if ( ( fiSelectedValue == "111" ) ||
      ( fiSelectedValue == "112" ) ||
      ( fiSelectedValue == "113" ) ||
      ( fiSelectedValue == "171" ) ||
        ( fiSelectedValue == "172" ) )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVImpExpInfo' ) ) ;
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVImpExpInfo' ) ) ;
   }

   if ( fiSelectedValue == "171" )
   {
      AMSSYSMANPARM_EnableField( 'txtT1BYPASS_APPROVAL' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1APPLY_OVERRIDES' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1BYPS_ADNT_FL' ) ;
			AMSSYSMANPARM_EnableField('txtT1OVERRIDE_LVL') ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_TYP' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_DEPT_CD' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_VERS_NO' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_CREA_USID' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_CD' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_ID' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_UNIT_CD' ) ;

      if ( document.forms[0].txtT1DOC_STA_CD.length == "5" )
      {
         moDocStaCdRejected = document.forms[0].txtT1DOC_STA_CD[3] ;
         moDocStaCdSubmitted = document.forms[0].txtT1DOC_STA_CD[4] ;
         document.forms[0].txtT1DOC_STA_CD.remove('4') ;
         document.forms[0].txtT1DOC_STA_CD.remove('3') ;   
      }
   }
   else if( fiSelectedValue == "181" )
   {
      AMSSYSMANPARM_EnableField( 'txtT1APPLY_OVERRIDES' ) ;
   }
   else
   {
      AMSSYSMANPARM_DisableField( 'txtT1BYPASS_APPROVAL' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1APPLY_OVERRIDES' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1BYPS_ADNT_FL' ) ;
      AMSSYSMANPARM_DisableField('txtT1OVERRIDE_LVL') ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_TYP' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_DEPT_CD' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_VERS_NO' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_CREA_USID' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_CD' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_ID' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_UNIT_CD' ) ; 
      if ( document.forms[0].txtT1DOC_STA_CD.length == "3" )
      {
         document.forms[0].txtT1DOC_STA_CD[3] = moDocStaCdRejected
         document.forms[0].txtT1DOC_STA_CD[4] = moDocStaCdSubmitted
         moDocStaCdRejected = null;
         moDocStaCdSubmitted = null;
      }
   }

   if ( fiSelectedValue == "172" )
   {
      AMSSYSMANPARM_EnableField( 'txtT1EXP_ATT_FL' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXP_ATT_TYP' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXCL_ATT' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXCL_NON_PERSIST_ATT' ) ;
   }
   else
   {
      AMSSYSMANPARM_DisableField( 'txtT1EXP_ATT_FL' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXP_ATT_TYP' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXCL_ATT' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXCL_NON_PERSIST_ATT' ) ;
   }

   if ( fiSelectedValue == "111" ||
      fiSelectedValue == "112" ||
      fiSelectedValue == "113" ||
      fiSelectedValue == "172" )
   {
      AMSSYSMANPARM_DisableField( 'txtT1MAX_ERRORS' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1ERROR_FILE_NM' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DTL_IMP_MSG_FL' ) ;
   }
   else
   {
      AMSSYSMANPARM_EnableField( 'txtT1MAX_ERRORS' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1ERROR_FILE_NM' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DTL_IMP_MSG_FL' ) ;
   }

   if ( ( fiSelectedValue == "161") ||
        ( fiSelectedValue == "162") || 
        ( fiSelectedValue == "181") )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVExcepInfo' ) ) ;
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVExcepInfo' ) ) ;
   }

   if ( ( fiSelectedValue == "111" ) ||
      ( fiSelectedValue == "112" ) ||
      ( fiSelectedValue == "113" ) )
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVRestartInfo' ) ) ;
   }
   else
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVRestartInfo' ) ) ;
   }

   if ( ( fiSelectedValue == "111" ) ||
      ( fiSelectedValue == "112" ) ||
      ( fiSelectedValue == "113" ) ||
      ( fiSelectedValue == "171" ) )
   {
      AMSTAB_ExpandSection('T1IN_SYS_MAN_PARM_Section3',
                           'T1IN_SYS_MAN_PARM_Section3_Button');
   }
   else
   {
      AMSTAB_ExpandSection('T1IN_SYS_MAN_PARM_Section1',
                           'T1IN_SYS_MAN_PARM_Section1_Button');
   }

   // disable following fields which are applicable
   // only for Table Import/Update/Overlay/Delete
   AMSSYSMANPARM_DisableField( 'txtT1EDITS_FL' ) ;

   // For Reject All show field CLEANUP_PERIOD and disable field Phase Code
   if ( fiSelectedValue == "186" )
   {
      AMSSYSMANPARM_EnableField( 'txtT1CLEANUP_PERIOD' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_PHASE_CD' ) ;
      document.forms[0].txtT1DOC_PHASE_CD.value = '2';
      
      // This code is needed to reset the field values if user selects
      // Reject All after selecting Approve Document in the Dropdown
      AMSSYSMANPARM_EnableField( 'txtT1DOC_STA_CD' ) ;
      document.forms[0].txtT1DOC_STA_CD.value = '';
   }
   else  if (fiSelectedValue == "171")     
   {
      AMSSYSMANPARM_DisableField( 'txtT1CLEANUP_PERIOD' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1DOC_PHASE_CD' ) ;
      document.forms[0].txtT1DOC_PHASE_CD.value = '';
      document.forms[0].txtT1DOC_STA_CD.value = '';   
   }
   else  if (fiSelectedValue != "153")     
   {
      AMSSYSMANPARM_DisableField( 'txtT1CLEANUP_PERIOD' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1DOC_PHASE_CD' ) ;
      //BGN ADVHR00085123
      //document.forms[0].txtT1DOC_PHASE_CD.value = '';
      //END ADVHR00085123
   }
   else if ( fiSelectedValue == "181" )
   {
      // For Approve Document disable field Phase Code and Status Code
      AMSSYSMANPARM_DisableField( 'txtT1DOC_PHASE_CD' ) ;
      document.forms[0].txtT1DOC_PHASE_CD.value = '2';
      AMSSYSMANPARM_DisableField( 'txtT1DOC_STA_CD' ) ;
      document.forms[0].txtT1DOC_STA_CD.value = '4';
   }
   else
   {
      AMSSYSMANPARM_EnableField( 'txtT1DOC_PHASE_CD' ) ;
      document.forms[0].txtT1DOC_PHASE_CD.value = '';
      AMSSYSMANPARM_EnableField( 'txtT1DOC_STA_CD' ) ;
      document.forms[0].txtT1DOC_STA_CD.value = '';
   }

} 

function AMSSYSMANPARM_SetTableDivs(fiSelectedValue)
{
   AMSSYSMANPARM_EnableExpandCollapse() ;

   if ( fiSelectedValue == "202" || fiSelectedValue == "208" )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVTableInfo' ) ) ;
      if ( fiSelectedValue == "202" )
      {
         AMSSYSMANPARM_EnableField( 'txtT1FM_KEY' ) ;
         AMSSYSMANPARM_EnableField( 'txtT1TO_KEY' ) ;
      }
      else
      {
         AMSSYSMANPARM_DisableField( 'txtT1FM_KEY' ) ;
         AMSSYSMANPARM_DisableField( 'txtT1TO_KEY' ) ;
      }
   }
   else if ( fiSelectedValue == "206" )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVTableInfo' ) ) ;
      AMSSYSMANPARM_DisableField( 'txtT1FM_KEY' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1TO_KEY' ) ;
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVTableInfo' ) ) ;
   }

   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVDocInfo' ) ) ;

   if ( fiSelectedValue == "202" )
   {
      AMSSYSMANPARM_DisableField( 'txtT1MAX_ERRORS' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1ERROR_FILE_NM' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXP_ATT_FL' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXP_ATT_TYP' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXCL_ATT' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXCL_NON_PERSIST_ATT' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1EXP_FILE_TYP' ) ;
   }
   else
   {
      AMSSYSMANPARM_EnableField( 'txtT1MAX_ERRORS' ) ;
      AMSSYSMANPARM_EnableField( 'txtT1ERROR_FILE_NM' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXP_ATT_FL' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXP_ATT_TYP' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXCL_ATT' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXCL_NON_PERSIST_ATT' ) ;
      AMSSYSMANPARM_DisableField( 'txtT1EXP_FILE_TYP' ) ;
   }

   if ( ( fiSelectedValue == "201") ||
        ( fiSelectedValue == "202") ||
        ( fiSelectedValue == "203") ||
        ( fiSelectedValue == "204") ||
        ( fiSelectedValue == "205") ||
        ( fiSelectedValue == "208") )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVImpExpInfo' ) ) ;
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVImpExpInfo' ) ) ;
   }

   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVExcepInfo' ) ) ;

   if (fiSelectedValue != "206")
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVRestartInfo' ) ) ;
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVRestartInfo' ) ) ;
   }

   if ( (fiSelectedValue == "201") ||
        (fiSelectedValue == "203") ||
        (fiSelectedValue == "204") ||
        (fiSelectedValue == "205") )
   {
      AMSSYSMANPARM_EnableField( 'txtT1EDITS_FL' ) ;
      AMSTAB_ExpandSection('T1IN_SYS_MAN_PARM_Section3',
                           'T1IN_SYS_MAN_PARM_Section3_Button');
   }
   else if ( (fiSelectedValue == "202") ||
             (fiSelectedValue == "206") )
   {
      AMSSYSMANPARM_DisableField( 'txtT1EDITS_FL' ) ;
      AMSTAB_ExpandSection('T1IN_SYS_MAN_PARM_Section2',
                           'T1IN_SYS_MAN_PARM_Section2_Button');
   }
   else if (fiSelectedValue == "208")
   {
      AMSSYSMANPARM_EnableField( 'txtT1EDITS_FL' ) ;
      AMSTAB_ExpandSection('T1IN_SYS_MAN_PARM_Section2',
                           'T1IN_SYS_MAN_PARM_Section2_Button');
      AMSTAB_ExpandSection('T1IN_SYS_MAN_PARM_Section3',
                           'T1IN_SYS_MAN_PARM_Section3_Button');
   }

   AMSSYSMANPARM_DisableField( 'txtT1BYPASS_APPROVAL' ) ;
   AMSSYSMANPARM_DisableField( 'txtT1APPLY_OVERRIDES' ) ;
   AMSSYSMANPARM_DisableField( 'txtT1DOC_S_ACTN_CD' );
   AMSSYSMANPARM_DisableField('txtT1OVERRIDE_LVL') ;
} 

function AMSSYSMANPARM_EnableExpandCollapse()
{
   DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'ADVExpandCollapse' ) ) ;
} 

function AMSSYSMANPARM_DisableExpandCollapse()
{
   DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'ADVExpandCollapse' ) ) ;
} 

 
function AMSSYSMANPARM_ShowMaxNo()
{
   if( document.forms[0].txtT1EXCEP_REP_IND.value == "5" )
   {
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'advExccepInfoMaxNo1' ) ) ;
      DHTMLLIB_ShowLayer( DHTMLLIB_GetLayer( 'advExccepInfoMaxNo2' ) ) ;
      
   }
   else
   {
      DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'advExccepInfoMaxNo1' ) ) ;
            DHTMLLIB_HideLayer( DHTMLLIB_GetLayer( 'advExccepInfoMaxNo2' ) ) ;
      
   }
}