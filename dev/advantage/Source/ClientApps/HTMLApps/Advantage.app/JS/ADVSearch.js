function QSRCH_AppendStar( foTextBox )
      {
         var loTextBoxValue = foTextBox.value;

         if (loTextBoxValue == null)
         {
            loTextBoxValue="*";
         }
         else if (loTextBoxValue.charAt(loTextBoxValue.length-1) != "*")
         {
            loTextBoxValue+="*";
         }

         foTextBox.value = loTextBoxValue.toUpperCase();

         return true;
      }

      function QSRCH_CheckKeyPress( foTextBox, foEvent )
      {
         var liKeyPressed = 0;

         if (mboolIE4Up)
         {
            liKeyPressed = foEvent.keyCode;
         }
         else if (mboolNS5Up)
         {
            liKeyPressed = foEvent.which;
         }

         /**
          * In Netscape6, the default behaviour is not
          * blur for textfields when you press "ENTER"
          * key, hence this change was required.
          */
         if (liKeyPressed == 13) // Enter Key
         {
            //foTextBox.blur();
            QSRCH_AppendStar( foTextBox );
         }

         return true;
      }

      /*
       * Method to reset the target frame if the selected row and the
       * OpenWithData have different target frames. The method retrieves
       * the target frame of the selected row and resets the value of
       * the HREF attribute for the Open With Data link.
       *
       * foLink - The Open With Data Link
       */
      function QSRCH_ResetTargetFrame( foLink )
      {
         var lsHREF = DHTMLLIB_GetAttributeValue( foLink, 'href' );
         var loTable = document.getElementById("tblT1IN_PAGES");
         var loNodeList = loTable.getElementsByTagName('TR');
         var loTR;
         var loSelTR;
         var loAnchorElems;
         var liOffset;
         var lsClassName;
         var lsRowHREF;
         var lsTempHREF;
         var liSbmtOffset;
         var liCtr;
         var liSelCtr;
         var loArray;
         var loRowArray;
         var lsModHREF;

         /* Loop through the rows to find the selected row.*/
         for ( var liIndex = 0; liIndex < loNodeList.length; liIndex++ )
         {
            loTR = loNodeList.item( liIndex );
            lsClassName = DHTMLLIB_GetClassName( loTR );
            if ( lsClassName != null )
            {
               /* Check the class name to see if it selected */
               liOffset = lsClassName.indexOf( UTILS_GRID_SEL_TR_CLASS_NAME );
               if ( liOffset > 0 )
               {
                  /* Retrieve the anchor elements from the row */
                  loAnchorElems = loTR.getElementsByTagName('A');
                  /* Loop through the anchors to find the one which submits the form */
                  for ( liCtr = 0; liCtr < loAnchorElems.length; liCtr ++ )
                  {
                     lsTempHREF = DHTMLLIB_GetAttributeValue(loAnchorElems[liCtr], 'href');
                     liSbmtOffset = lsTempHREF.indexOf( "submitForm" );
                     if ( liSbmtOffset > 0 )
                     {
                        lsRowHREF = lsTempHREF;
                        liSelCtr = liCtr ;
                        break;
                     } /* end if ( liSbmtOffset > 0 ) */
                  } /* end for */
                  break;
               } /* end if ( liOffset > 0 ) */
            } /* end if (lsClassName != null) */
         } /* end for (var liIndex = 0; liIndex ... */

        /* Array to hold the Parsed Tokens from HREF strings */
        loArray  = lsHREF.split(",");
        loRowArray = lsRowHREF.split(",");
        if ( loArray[2] != loRowArray[2] )
        {
           lsModHREF = loArray[0] + ',' + loArray[1] + ',' + loRowArray[2] ;
           DHTMLLIB_SetAttributeValue( foLink, 'href', lsModHREF );
        } /* end if ( loArray[2] != loRowArray[2] ) */
      } /* end QSRCH_ResetTargetFrame() */