/*
 * @(#)pChain_Job_Steps_Grid.java     1.0 Dec 15, 2006
 *
 * Copyright (C) 2006, 2006 by CGI-AMS Inc.,
 * 4050 Legato Road, Fairfax, Virginia, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of CGI-AMS Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with CGI-AMS Inc.
 *
 * Modification log:
 *
*/
// {{IMPORT_STMTS
package advantage.Reports_Sys_Admin_App;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;

import java.io.*;
import javax.swing.text.html.*;



// END_IMPORT_STMTS}}

import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.versata.util.logging.*;
import org.apache.commons.logging.Log;

/*
 * * pChain_Job_Steps_Grid
 */

// {{FORM_CLASS_DECL
public class pChain_Job_Steps_Grid extends pChain_Job_Steps_GridBase

// END_FORM_CLASS_DECL}}
{
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pChain_Job_Steps_Grid.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   
   
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
   // {{FORM_CLASS_CTOR
   public pChain_Job_Steps_Grid(PLSApp parentApp)
                                                 throws VSException,
                                                 java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE
   // {{EVENT_pChain_Job_Steps_Grid_beforeActionPerformed
   void pChain_Job_Steps_Grid_beforeActionPerformed(ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      // Write Event Code below this line]
      String lsName = ae.getName();
      if (lsName != null
            && lsName.equals("btnT2Chain_Job_Schedule"))
      {

         /* Initialize to dummy value */
         long llCtlgID = -1;
         VSSession loCurrSession = getParentApp().getSession();
         try
         {
            VSORBSession loORBSession = loCurrSession.getORBSession();
            /* Get Catalog ID */
            llCtlgID = Long.parseLong(loORBSession.getProperty("RSAA_NODE_ID"));
         }// end try
         catch (Exception loExcp)
         {
            /* Failed to obtain Catalog ID from Session properties */
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

            raiseException(
                  "Failed to obtain Job information from Session properties",
                  SEVERITY_LEVEL_ERROR);
            evt.setNewPage(this);
            evt.setCancel(true);
            return;
         }
         /*
          * Check if the User is authorized to Schedule New Job as per security
          * settings
          */
         if (!AMSPLSUtil.isJobActnAuthorizedForUser(loCurrSession, AMSPLSUtil
               .getCurrentUserID(loCurrSession), llCtlgID,
               AMSCommonConstants.JOB_ACTN_SCHEDULE, true, this))
         {
            /*
             * Case where the User is not authorized to Schedule New Job.
             * Prevent the navigation to the page that allows to schedule new
             * job.
             */
            evt.setNewPage(this);
            evt.setCancel(true);
            return;
         }
      }
   }

   // END_EVENT_pChain_Job_Steps_Grid_beforeActionPerformed}}

   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      addPageListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         pChain_Job_Steps_Grid_beforeActionPerformed(ae, evt, preq);
      }
   }

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
      //Write code here for initializing your own control
      //or creating new control.

   }

}



