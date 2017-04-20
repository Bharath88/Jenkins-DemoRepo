/*
 * @(#)pAllAttachments_Grid.java     1.0 Nov 3, 2006
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
package advantage.Advantage;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;

import java.io.*;

// END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSParams;


// {{FORM_CLASS_DECL
public class pAllAttachments_Grid extends pAllAttachments_GridBase

// END_FORM_CLASS_DECL}}
{

   private String msAttchFile = null;

   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
   // {{FORM_CLASS_CTOR
   public pAllAttachments_Grid(PLSApp parentApp) throws VSException,
      java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_IGNORE);
      setAllowHistory(false);
   }

   // {{EVENT_CODE
   // {{EVENT_pAllAttachments_Grid_beforeActionPerformed
   void pAllAttachments_Grid_beforeActionPerformed(ActionElement foActnElem,
         PageEvent foPageEvent, PLSRequest foPLSReq)
   {
      // Write Event Code below this line
      if (foActnElem.getName().equals("ReturnToDoc"))
      {
         VSPage loSrcPage = getSourcePage();
         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(loSrcPage);
      } /* end else if ( foActnElem.getName().equals( "ReturnToDoc" ) ) */
      else if (foActnElem.getName().equals("DownloadAttachment"))
      {
         VSRow loCurrRow;
         VSQuery loQuery;
         VSResultSet loResultSet;
         VSORBSession loSession = getParentApp().getSession().getORBSession() ;
         try
         {
            loSession.setProperty(ECM_MSG_TXT, "fetchFromECM");
         }
         catch(Exception ae)
         {
            raiseException("Unable to set property for fetch",SEVERITY_LEVEL_ERROR);
         }

         acceptData(foPLSReq);
         loCurrRow = T1IN_OBJ_ATT_CTLG.getCurrentRow();
         if (loCurrRow != null)
         {
            loQuery = new VSQuery(getParentApp().getSession(),
                  "IN_OBJ_ATT_STOR", "OBJ_ATT_UNID="
                   + loCurrRow.getData("OBJ_ATT_UNID").getLong(), "");

            // Fix for the download of binary types
            loQuery.setColumnProjectionLevel(DataConst.ALLTYPES);

            loResultSet = loQuery.execute();

            if (loResultSet != null)
            {
               loResultSet.last();

               if (loResultSet.getRowCount() == 1)
               {
                  byte[] lbAttchData = loResultSet.first().getData(
                        "OBJ_ATT_DATA").getBytes();

                  // Verify that the attachment isn't null and its under the
                  // maximum attachment Size
                  if (lbAttchData != null
                        && lbAttchData.length > AMSParams.mlMaxDownloadSize
                        && AMSParams.mlMaxDownloadSize != -1)
                  {
                     raiseException(
                           "Attachment file exceeded maximum download size.",
                           SEVERITY_LEVEL_ERROR);
                     foPageEvent.setCancel(true);
                     foPageEvent.setNewPage(this);
                     return;
                  }
                  else if (lbAttchData != null
                        && (AMSParams.mlMaxAttachmentSize == -1
                        || lbAttchData.length <= AMSParams.mlMaxAttachmentSize))
                  {
                     try
                     {
                        msAttchFile = new String(lbAttchData, 0,
                              lbAttchData.length, "ISO8859_1");
                     }
                     catch (UnsupportedEncodingException loExcep)
                     {
                        raiseException(
                              "Encountered an unsupported encoding exception : "
                                    + loExcep.getMessage(),
                              SEVERITY_LEVEL_ERROR);
                        msAttchFile = null;
                        foPageEvent.setCancel(true);
                        foPageEvent.setNewPage(this);
                        return;
                     }
                     setDownloadFileInfo("application/download", loCurrRow
                           .getData("OBJ_ATT_NM").getString(), true);
                  }

                  if (msAttchFile == null)
                  {
                     /*
                      * This may be because of one of the following reasons:
                      * 1. Attachment file is not found
                      * 2. A zero byte empty .txt file was uploaded and null is stored
                      * on OBJ_ATT_DATA on IN_OBJ_ATT_STOR entry for DB2 and SQL Server.
                      * Note: For Oracle the data was not null and for most other file
                      * extension files the data was not null because of associated metadata.
                      */
                     raiseException( "Attachment file not found " +
                           "or Attachment file is empty with zero size.",
                           SEVERITY_LEVEL_ERROR ) ;
                  } /* end if ( msAttchFile == null ) */
               } /* end if ( loResultSet.getRowCount() == 1 ) */
               else
               {
                  raiseException("Unable to locate attachment record.",
                        SEVERITY_LEVEL_ERROR);
               } /* end else */
               loResultSet.close();
            } /* end if ( loResultSet == null ) */
            else
            {
               raiseException("Unable to locate attachment record.",
                     SEVERITY_LEVEL_ERROR);
            } /* end else */
         } /* end if ( loCurrRow != null ) */
         else
         {
            raiseException("No attachment record selected.",
                  SEVERITY_LEVEL_ERROR);
         } /* end else */

         foPageEvent.setCancel(true);
         foPageEvent.setNewPage(this);
      }
   }

   // END_EVENT_pAllAttachments_Grid_beforeActionPerformed}}

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
         pAllAttachments_Grid_beforeActionPerformed(ae, evt, preq);
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
      // Write code here for initializing your own control
      // or creating new control.

   }

   public String doAction(PLSRequest foPLSReq)
   {
      String lsResponse;

      lsResponse = super.doAction(foPLSReq);

      if (msAttchFile != null)
      {
         lsResponse = msAttchFile;
         msAttchFile = null;
      } 
      return lsResponse;
   } /* end doAction() */
}
