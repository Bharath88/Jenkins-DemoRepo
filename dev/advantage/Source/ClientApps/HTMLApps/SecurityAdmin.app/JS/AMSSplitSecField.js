
function AMSSPLITSECFIELD_DoSplitSecFields()
{
   AMSSPLITSECFIELD_DoSplitSecField( "txtT1APRV_LVL_CD", 15 ) ;
   AMSSPLITSECFIELD_DoSplitSecField( "txtT1COMB_ACTN1_CD", 30 ) ;
   AMSSPLITSECFIELD_DoSplitSecField( "txtT1COMB_ACTN2_CD", 22 ) ;
} 

function AMSSPLITSECFIELD_DoApplSplitSecField( combined_code_field )
{
   var remainder_value;
   var oneBitField;
   var combined_code_value;
   var number = 1;
   var field;
   var fldVal;
   var liCount = document.pR_SC_RSRC_ACCS.elements.length;

   with (document.pR_SC_RSRC_ACCS)
   {
      field = eval( combined_code_field);
      combined_code_value = DHTMLLIB_GetAttributeValue( field, "value");

      fldVal = combined_code_field + "_" ;
      for( var liIndex = 0; liIndex < liCount ; liIndex++)
      {
         if( (document.pR_SC_RSRC_ACCS.elements[liIndex].name).search(fldVal) != -1 )
         {
            var lsStr = document.pR_SC_RSRC_ACCS.elements[liIndex].name;
            var lsArray = lsStr.split("_");
            var liLength = lsArray.length;

            var liPos = parseInt(lsArray[liLength-1]);
            oneBitField = fldVal + liPos ;

            if ( oneBitField != null )
             {
               document.pR_SC_RSRC_ACCS.elements[liIndex].checked = combined_code_value & ( number << (liPos-1) );
            }
         }

      }

   }
}



function AMSSPLITSECFIELD_DoSplitSecField( combined_code_field, howmany )
{
   var remainder_value;
   var oneBitField;
   var combined_code_value;
   var number;
   var field;

   with (document.pR_SC_RSRC_ACCS)
   {
      field = eval( combined_code_field);
      combined_code_value = DHTMLLIB_GetAttributeValue( field, "value");
      number = 1;

      for ( var i = 1 ; i <= howmany ; i++ )
      {
          oneBitField = eval( combined_code_field + "_" + i );
          if ( oneBitField != null )
          {
             oneBitField.checked = combined_code_value & ( number << (i-1) );
          }
      }

   }

} 

function AMSSPLITSECFIELD_GrantAllApplActns( )
{
   var liCount = document.pR_SC_RSRC_ACCS.elements.length;
   var loElem ;

   for( var liIndex = 0; liIndex < liCount ; liIndex++)
   {
      loElem = document.pR_SC_RSRC_ACCS.elements[liIndex] ;
      if ( ( loElem.tagName == 'INPUT' ) && ( DHTMLLIB_GetAttributeValue( loElem, 'type' ) == 'checkbox' ) )
      {
         if( (document.pR_SC_RSRC_ACCS.elements[liIndex].name).search("APPL_ACTN") != -1 &&
             (document.pR_SC_RSRC_ACCS.elements[liIndex].name).search("CD_") != -1            )
         {
            document.pR_SC_RSRC_ACCS.elements[liIndex].checked = true;
         }
      }
   }
} 

function AMSSPLITSECFIELD_RemoveAllApplActns()
{
   var liCount = document.pR_SC_RSRC_ACCS.elements.length;
   var loElem ;

   for( var liIndex = 0; liIndex < liCount ; liIndex++)
   {
      loElem = document.pR_SC_RSRC_ACCS.elements[liIndex] ;
      if ( ( loElem.tagName == 'INPUT' ) && ( DHTMLLIB_GetAttributeValue( loElem, 'type' ) == 'checkbox' ) )
      {
         if ( (loElem.name).search("APPL_ACTN") != -1 && (loElem.name).search("CD_") != -1 )
         {
            loElem.checked = false;
         }
      }

   }
} 



function AMSSPLITSECFIELD_GrantAll( combined_code_field, howmany )
{
   AMSSPLITSECFIELD_ChangeAllAccess( combined_code_field, howmany, true ) ;
}

function AMSSPLITSECFIELD_RemoveAll( combined_code_field, howmany )
{
   AMSSPLITSECFIELD_ChangeAllAccess( combined_code_field, howmany, false ) ;
} 

function AMSSPLITSECFIELD_ChangeAllAccess( combined_code_field, howmany, grant_all )
{
   var checkbox;

   with ( document.pR_SC_RSRC_ACCS )
   {
      for ( var i = 1 ; i <= howmany ; i++ )
      {
          checkbox = eval( combined_code_field + "_" + i );

          if ( checkbox != null )
          {
             checkbox.checked = grant_all;
          }
      }
   }
} 
