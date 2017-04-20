/*
 * @(#)pNewAlertsBroadcast_Grid.java 1.0 Sep 5, 2006
 *
 * Copyright (C) 2006, 2006 by CGI-AMS Inc., 4050 Legato Road, Fairfax,
 * Virginia, U.S.A. All rights reserved.
 *
 * This software is the confidential and proprietary information of CGI-AMS Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with CGI-AMS Inc.
 *
 * Modification log:
 *
 */
// {{IMPORT_STMTS
package advantage.Advantage;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
// END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.client.dbitem.AMSUserProfileMethods;
import java.util.Date;

/*
 * * pNewAlertsBroadcast_Grid
 */

// {{FORM_CLASS_DECL
public class pNewAlertsBroadcast_Grid extends pNewAlertsBroadcast_GridBase

// END_FORM_CLASS_DECL}}
{
   // Get current system timestamp

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
   // {{FORM_CLASS_CTOR
   public pNewAlertsBroadcast_Grid(PLSApp parentApp)
                                                    throws VSException,
                                                    java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE

   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE


   // END_EVENT_ADAPTER_CODE}}

   public HTMLDocumentSpec getDocumentSpecification()
   {
      return getDefaultDocumentSpecification();
   }

   public String getFileName()
   {
      return getDefaultFileName();
   }

   public String getFileLocation()
   {
      return getPageTemplatePath();
   }

   public void afterPageInitialize()
   {
      super.afterPageInitialize();
      // Write code here for initializing your own control
      // or creating new control.

   }

}
