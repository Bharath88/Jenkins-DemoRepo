/*
 * @(#)AMSReportJobParmTable.java     1.0 06/28/2000
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
 * @author Mukul Gupta
 * Initial reversata.
 *   vsclass="com.amsinc.gems.adv.vfc.html.AMSReportJobParmTable"
 */

package com.amsinc.gems.adv.vfc.html;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.DefaultStyledDocument.* ;

public class AMSReportJobParmTable extends AMSTableElement
{
   //public AMSReportJobParmTable(AbstractDocument.AbstractElement element)
   public AMSReportJobParmTable(HTMLElement element)
   {
      super(element);
   }


   /**  Extends AMSTableInputColumn Element to create a new type of
    *   column called AMSReportJobParmInputColumn
    */
   public class AMSReportJobParmInputColumn extends AMSTableElement.AMSInputColumn
   {
      public AMSReportJobParmInputColumn(ElementSpec spec, String datafield)
      {
         super(spec,datafield);
      }

      public ElementSpec getElementSpec(VSRow foVSRow ,int fiOffset)
      {
         ElementSpec loES = super.getElementSpec(foVSRow,fiOffset);
         MutableAttributeSet loMas = (MutableAttributeSet)loES.getAttributes();

         if (getDataFieldName().equals("PARM_DSCR"))
         {
            loMas =(MutableAttributeSet)loES.getAttributes();
            String lsValue = foVSRow.getData(getDataFieldName()).getString();
            loMas.addAttribute("value",lsValue);
         }

         if ( getDataFieldName().equals("PARM_VL")   &&
            (foVSRow.getData("OV_FL").getBoolean()==false))
         {
            loMas =(MutableAttributeSet)loES.getAttributes();
            String lsValue = foVSRow.getData(getDataFieldName()).getString();
            loMas.addAttribute("value",lsValue);
            loMas.addAttribute( "READONLY", "READONLY" ) ;
            loMas.addAttribute( "class", "advreadonly" ) ;

         }

         return loES;
      } /* end getElementSpec */
   } /* end AMSReportJobParmInputColumn */


      public TableColumnElement creatingColumnElement( ElementSpec foElemSpec )
      {

         AttributeSet loMAS = foElemSpec.getAttributes() ;
         /*
          * use subclassed InputColumn for Parameter description (Textarea)
          *  and parameter value cell only
          */
         if (loMAS != null)
         {
         	HTML.Tag tag = (HTML.Tag)loMAS.getAttribute(StyleConstants.NameAttribute);

         	if (loMAS.isDefined(HTMLDocumentModel.DATASOURCE_ATTRIBUTE) &&
            loMAS.isDefined(HTMLDocumentModel.DATAFIELD_ATTRIBUTE) &&
               (tag==HTML.Tag.INPUT || tag==HTML.Tag.TEXTAREA) )
         	{
            	String dataField = loMAS.getAttribute(HTMLDocumentModel.DATAFIELD_ATTRIBUTE).toString();

            	return new AMSReportJobParmInputColumn(foElemSpec,dataField);
         	}
      		else
      		{
        	 	return super.creatingColumnElement( foElemSpec ) ;
   			} /* end else */
		}
		else
		{
        	return super.creatingColumnElement( foElemSpec ) ;
		}


      } /* end creatingColumn */


}
