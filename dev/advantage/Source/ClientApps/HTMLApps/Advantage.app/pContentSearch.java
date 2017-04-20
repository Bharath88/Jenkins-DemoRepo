// {{IMPORT_STMTS
package advantage.Advantage;

import java.rmi.RemoteException;
import java.text.ParsePosition;
import java.util.ArrayList;
import versata.common.SearchRequest;
import java.util.Hashtable;
import versata.common.VSException;
import versata.common.VSMetaColumn;
import versata.common.VSMetaQuery;
import versata.common.VSOutParam;
import versata.common.VSSession;

import versata.common.VSORBSession;
import versata.vfc.VSData;
import versata.vfc.VSQuery;
import versata.vfc.VSResultSet;
import versata.vfc.VSRow;
import versata.vfc.html.ActionElement;
import versata.vfc.html.DataSource;
import versata.vfc.html.HTMLDocumentSpec;
import versata.vfc.html.HiddenElement;
import versata.vfc.html.PLSApp;
import versata.vfc.html.PLSRequest;
import versata.vfc.html.PageEvent;
import versata.vfc.html.Pick;
import versata.vfc.html.VSPage;
import advantage.AMSMsgUtil;
import advantage.AMSStringUtil;
import advantage.AMSUtil;
import advantage.CVL_OBJ_ATT_TYPImpl;

import java.util.Vector;

import org.apache.commons.logging.Log;

import com.amsinc.gems.adv.common.AMSCommonConstants;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.AMSDateFourYearFormatter;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import com.amsinc.gems.adv.vfc.html.AMSPage;
import com.amsinc.gems.adv.vfc.html.AdvECMSearchFields;
import com.amsinc.gems.adv.vfc.html.AdvECMSearchGrid;

// {{FORM_CLASS_DECL
public class pContentSearch extends pContentSearchBase 

// END_FORM_CLASS_DECL}}
{

   private ArrayList moECMSrchMetaData = null;

   private ArrayList moECMAttachMetaData = null;

   public static String ROW_SELECTION_CHKBOX_PREFIX = "ECM_SEL_CHKBOX";

   //Query object used for ECM search service
   public static String QRY_ECM_SEARCH = "QRY_ECM_SEARCH";

   private int miSelectedRowIndex = 1;

   private String msECMType = null;

   private String msPriGrpID = null;

   private String msSecGrpID = null;

   private String msDocCat = null;

   private String msDocType = null;

   private String msDocCode = null;

   private String msDocDept = null;

   private String msDocID = null;

   private String msDocVersNo = null;

   private String msDocIntLnNoNm = null;

   private String msDocIntLnNo = null;

   private String msDocHdrNm = null;

   private String msDocCompNm = null;

   private boolean mboolIsDocComp = false;

   private String msTableName = null;

   private Vector mvKeyValues = null;

   private VSRow moHdrRow = null;

   private VSRow moCompRow = null;

   private String msObjAttType = null;

   private String  msAttchFile    = null ;
   private static Log moLog = AMSLogger.getLog( pContentSearch.class,
       AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
   // {{FORM_CLASS_CTOR
   public pContentSearch(PLSApp parentApp) throws VSException,
                                          java.beans.PropertyVetoException
   {
      super(parentApp);
      // END_FORM_CLASS_CTOR}}
   }

   // {{EVENT_CODE
   // {{EVENT_pContentSearch_beforeActionPerformed
   void pContentSearch_beforeActionPerformed(ActionElement foActElem,
         PageEvent foPageEvent, PLSRequest foPLSReq)
   {
      // Write Event Code below this line

      VSORBSession loSession = getParentApp().getSession().getORBSession();

      /*
       * For any action make sure we get the selected row index and store it on
       * the page we need to make sure that selected row is shown to the user
       * when the page regenerates
       */

      String lsSelectedRecord = foPLSReq.getParameter("selectedrowindex");
      miSelectedRowIndex = 1;
      if (!AMSStringUtil.strIsEmpty(lsSelectedRecord))
      {
         miSelectedRowIndex = Integer.parseInt(lsSelectedRecord);
      }
      
      if(moLog.isDebugEnabled())
      {
         moLog.debug("Selected Row Index: " + miSelectedRowIndex);         
      }
      
      VSResultSet loResultSet     = null;
      VSResultSet loPageResultSet = null;
            
      try
      {

         if (foActElem.getName().equals("ECMDownload"))
         {
            /*
             * For the XDA to know that this save is for the download,
             * update the selected row with action code as 1 and OBJ_ATT_DATA as
             * the object id. DocumentumHandler will then use this data to do a
             * fetch from ECM.
             */
            VSRow loRow = this.getRootDataSource().getResultSet().getRowAt(
                  miSelectedRowIndex);
            String lsObjectID = loRow.getData("OBJECT_ID").getString();
            loRow.getData("OBJ_ATT_DATA").setBytes(lsObjectID.getBytes());
            loRow.getData("ACTN_CD").setInt(AMSCommonConstants.ECM_DOWNLOAD);
            loRow.save();
            try
            {
               msAttchFile = loSession.getProperty( "ECM_FILE_CONTENT" );
            } catch( RemoteException loEx )
            {
               moLog.error( "Unexpected error encountered while processing. ",
                     loEx );
               foPageEvent.setCancel( true );
               foPageEvent.setNewPage( this );
               raiseException( "Error getting session string FILE_CONTENT",
                     AMSMsgUtil.SEVERITY_LEVEL_SEVERE );
               return;
            }
            finally
            {
               try
               {
                  loSession.removeProperty( "ECM_FILE_CONTENT" );
               } catch( RemoteException loEx )
               {
                  moLog.error(
                        "Unexpected error encountered while processing. ",
                        loEx );
                  raiseException(
                        "Error removing session property ECM_FILE_CONTENT",
                        SEVERITY_LEVEL_ERROR );
               }
            }
            setDownloadFileInfo("application/download", loRow
                  .getData("OBJ_ATT_NM").getString(), true);
         }
         else if (foActElem.getName().equals("T1CONTENT_SRCHclearquery"))
         {
            // Clear the selected Pick value, Dynamic Search Scalar section and Grid
            moECMSrchMetaData = null;
            ((AdvECMSearchFields) getElementByName("txtT1ECM_SEARCH"))
            .setAdvSysType(null);
            ((AdvECMSearchGrid) getElementByName("txtT1ECM_SEARCHGrid"))
            .setAdvSysType(null);
            HiddenElement loHiddenCodeElem = (HiddenElement) getElementByName("txtT1ADV_SYS_TYPhidden");
            loHiddenCodeElem.setValue("");

         }
         else if (foActElem.getName().equals("AMSBrowse"))
         {
            String lsAdvSysTyp = foPLSReq.getParameter("txtT1ADV_SYS_TYPhidden");
            int liDataType=0;

            if (lsAdvSysTyp == null || lsAdvSysTyp.trim().length() == 0)
            {
               foPageEvent.setCancel(true);
               foPageEvent.setNewPage(this);
               raiseException(
                     "Please select Advantage System Type before proceeding.",
                     AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
               return;

            }

            /*
             * Create list of search metadata based on the selected adv sys typ from pick
             * and store it in session variable - ECM_SEARCH_REQUEST
             * Eg: ABS,adv_subtype_abs_doc::adv_appr_cd=010,adv_module=ABS
             */
            Object[] loMetaDatArray = moECMSrchMetaData.toArray();
            StringBuffer lsSessionString = new StringBuffer();

            // set the adv sys type txtT1ADV_SYS_TYP
            lsSessionString.append(lsAdvSysTyp);
            lsSessionString.append(",");
            lsSessionString.append(msECMType);
            lsSessionString.append("::");         
            /*
             * We have to go in the loop twice for the following reasons Loop 1:
             * Acts like pre-accept data and maps the entered value against the
             * moECMSrchMetaData, this is required so that even if the validation
             * fails in step2, when the page gets regenerated we can still see the
             * value that user entered.
             *
             * loop 2: do validation based on data type and generate the session
             * string
             */

            /*
             * The meta data is an array with the following column values
             * EXT_SYS_COL_NM ADV_ATTR ADV_COMP ADV_SYS_TYP EXT_SYS_TYP ADV_ATTR_NM
             * ADV_ATTR_NM_UP DATA_TYP DATA_SIZE Value from the page
             */
            for (int liIndex = 0; liIndex < moECMSrchMetaData.size(); liIndex++)
            {
               String[] lsColumnArray = (String[]) loMetaDatArray[liIndex];
               String lsColumnValue = foPLSReq.getParameter(lsColumnArray[1]);
               if (lsColumnValue == null)
               {
                  lsColumnValue = "";// cannot enter null in array as it causes
                  // errors downstream.
               }/* end if(lsColumnValue == null) */

               lsColumnArray[9] = lsColumnValue;
               moECMSrchMetaData.remove(liIndex);
               moECMSrchMetaData.add(liIndex, lsColumnArray);
            }

            boolean lbolSearchValid = true;
            for (int liIndex = 0; liIndex < moECMSrchMetaData.size(); liIndex++)
            {
               String[] lsColumnArray = (String[]) loMetaDatArray[liIndex];

               String lsColumnValue = foPLSReq.getParameter(lsColumnArray[1]);
               liDataType = Integer.parseInt(lsColumnArray[7]);
               // if column value is null skip validation
               if ((lsColumnValue != null) && (lsColumnValue.trim().length() > 0))
               {

                  //Type Validation
                  if (VSData.DATE == liDataType)// validate the entered date
                  {
                     try
                     {
                        AMSDateFourYearFormatter loDtFmt = new AMSDateFourYearFormatter(
                              this);
                        StringBuffer lsbDateBuffer = new StringBuffer(20);                     

                        lsColumnValue =(String)(loDtFmt.parseObject(lsColumnValue, new ParsePosition(0) ));
                     }
                     catch (Exception loExp)
                     {
                        /*
                         * If exception, it means parser has thrown some error
                         * raiseException has already been done
                         * inside the formatter's parse method.
                         */                
                        lbolSearchValid = false;
                     } /* end catch ( Exception loExp ) */

                  }/* end if(VSData.DATE == liDataType) */

                  // validate if its an integer column. More can be added here as
                  // required.
                  else if ((VSData.INTEGER == liDataType)
                        || (VSData.NUMERIC == liDataType))
                  {
                     try
                     {
                        Integer.parseInt(lsColumnValue);
                     }
                     catch (Exception ex)
                     {
                        raiseException("Invalid " + lsColumnArray[5] + "- "
                              + ex.getMessage(), AMSMsgUtil.SEVERITY_LEVEL_ERROR);
                        lbolSearchValid = false;
                     }
                  }

               }
               /*
                * Now create the string which is to be stored in the session.
                */

               if (liIndex > 0)
               {
                  lsSessionString.append(",");

               }
               lsSessionString.append(lsColumnArray[0]);
               lsSessionString.append("=");
               lsSessionString.append(lsColumnValue);
               lsSessionString.append("?");
               lsSessionString.append(liDataType);
            }

            if(!lbolSearchValid)
            {
               raiseException("Invalid Search criteria entered on page.", AMSMsgUtil.SEVERITY_LEVEL_SEVERE);  
            }

            try
            {
               /*
                * This property is used by AdvDocumentumHandler to query ECM
                * for attachments.
                */
               loSession.setProperty(AMSCommonConstants.ECM_SEARCH_REQUEST, lsSessionString
                     .toString());

               if(moLog.isDebugEnabled())
               {
                  moLog.debug("ECM_SEARCH_REQUEST session string: " + lsSessionString);         
               }
            }
            catch (RemoteException fex)
            {
               // TODO Auto-generated catch block
               // Add exception log to logger object
               moLog.error("Unexpected error encountered while processing. ", fex);

               foPageEvent.setCancel(true);
               foPageEvent.setNewPage(this);
               raiseException("Error setting session string ECM_SEARCH_REQUEST",
                     AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
               return;
            }
         }/* end if ( foActElem.getName().equals( "AMSBrowse" ) ) */
         else if (foActElem.getName().equals("PickOK"))
         {
            if (AMSStringUtil.strIsEmpty(lsSelectedRecord))
            {
               foPageEvent.setCancel(true);
               foPageEvent.setNewPage(this); 
               raiseException("Please select a row to perform Attach action",
                     AMSPage.SEVERITY_LEVEL_ERROR);                 
               return;
            }
            
            String lsAdvSysTyp = "";
            String lsECMType = "";
            String lsAdvComp = "";
                      
            if (AMSStringUtil.strEqual(msObjAttType, OBJ_ATT_TYPE_REF_TBL))
            {
               lsAdvComp   = msTableName;
               lsAdvSysTyp = msTableName;
            }
            else if (AMSStringUtil.strEqual(msObjAttType, OBJ_ATT_TYPE_DOC_VIEW))
            {
               lsAdvComp = msDocCompNm;
               
               //For Documents derive Advantage type from Component name
               //Eg: derive ABS from ABS_DOC_HDR
               lsAdvSysTyp = msDocCompNm.substring(0, msDocCompNm.indexOf("_DOC_"));
            }
                    
            /*
             * Check if Advantage-ECM meta data mapping exists
             * Get ECM type (ext_sys_typ), from mapping table
             */
            
            SearchRequest loSearchReq = new SearchRequest();
            VSRow loRow = null;

            loSearchReq.addParameter("R_ADV_EXT_TYP","ADV_SYS_TYP",lsAdvSysTyp );
            
            VSQuery loQuery = new VSQuery(getParentApp().getSession(), "R_ADV_EXT_TYP",
                  loSearchReq, null);
            loResultSet = loQuery.execute();
            loRow = loResultSet.first();
            
            if(loRow == null)
            {   
               foPageEvent.setCancel(true);
               foPageEvent.setNewPage(this); 
               raiseException(
                     "The Table/Document does not have an entry on the ECM Mapping Table. " +
                     "Unable to attach content from ECM.",
                     AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
                             
            }
            else
            {
               lsECMType = loRow.getData("EXT_SYS_TYP").getString();
            }   
            
            if(moLog.isDebugEnabled())
            {
               moLog.debug("Processing Advantage Type: " + lsAdvSysTyp);
               moLog.debug("Processing Component: " + lsAdvComp);  
               moLog.debug("Processing ECM Type: " + lsECMType);  
            }

            /*
             * Create list of atatch metadata based on the current component
             * and store them in the Search Data Row
             * Eg: ATT_FIELD_1:adv_appr_cd=010, ATT_FIELD_2:adv_module=ABS
             * This meta data information will be used by XDA in update service
             * to pass to ECM.
             */

            createMetaDataList(lsAdvComp);

            int liKeyColCount = 0;
            
            /*
             * For reference tables get primary key info from mvKeyValues
             * Vector set by previous page
             */

            if (AMSStringUtil.strEqual(msObjAttType, OBJ_ATT_TYPE_REF_TBL))
            {
               // Get Table Meta Data information to be updated
               liKeyColCount = mvKeyValues.size();
               loSearchReq = new SearchRequest();

               for (int liCount = 0; liCount < liKeyColCount; liCount++)
               {
                  loSearchReq.addParameter(msTableName, (String) mvKeyValues
                        .get(liCount++), (String) mvKeyValues.get(liCount));
               }
               loQuery = new VSQuery(getParentApp().getSession(), lsAdvComp,
                     loSearchReq, null);
               loResultSet = loQuery.execute();
               loResultSet.last();
               loRow = loResultSet.getRowAt(1);
            }
            else if (AMSStringUtil.strEqual(msObjAttType, OBJ_ATT_TYPE_DOC_VIEW))
            {
               /*
                * For Document atatchment, current component object is passed
                * by previous page
                */
               if (mboolIsDocComp)
               {
                  loRow = moCompRow;
               }
               else
               {
                  loRow = moHdrRow;
               }
            }

            if (loRow == null)
            {
               return;
            }

            loPageResultSet = this.getRootDataSource().getResultSet();
            VSRow loSearchRow;

            /*
             * We need to go in a loop through all records to find out what rows
             * have been checked. This is simpler instead of adding the logic to
             * store the checked row in javascript.
             */

            int liRowCount = loPageResultSet.getRowCount();

            for (int liCount = 1; liCount <= liRowCount; liCount++)
            {
               /*
                * IF the request has the column and its value is "ON" the row was
                * selected.
                */

               if (!AMSStringUtil.strIsEmpty(foPLSReq
                     .getParameter(this.ROW_SELECTION_CHKBOX_PREFIX + "_"
                           + liCount)))
               {
                  /*
                   * If ECM_SEL_CHKBOX_liIndex is found in request it means that
                   * the row was checked.
                   */
                  loSearchRow = loPageResultSet.getRowAt(liCount);

                  String lsAdvColNm;
                  String lsECMColNm;
                  String lsKeyVal;                
                  String lsValue = null;
                  Object[] loMetaDatArray = moECMAttachMetaData.toArray();
                  String[] lsColumnArray;  
                 
                                  
                  boolean lboolDept_cd = false;
                  boolean lboolAdv_module = false;
                  
                  for (int liIndex = 0; liIndex < moECMAttachMetaData.size(); liIndex++)
                  {
                     lsColumnArray = (String[]) loMetaDatArray[liIndex];

                     lsECMColNm = lsColumnArray[0];
                     
                     /* 
                      * For Document attachment, check if minimum field mappings i.e.
                      * dept_cd and adv_module has been mapped for current component.  
                      * Set flags if dept_cd/adv_module is mapped.
                      * These flags will be used later to raise errors 
                      * if entries not found on mapping table
                      */  
                     if(AMSStringUtil.strEqual(lsECMColNm, "dept_cd"))
                     {
                        lboolDept_cd = true; 
                     }
                     else if(AMSStringUtil.strEqual(lsECMColNm, "adv_module"))
                     {
                        lboolAdv_module = true;
                     }
                     lsAdvColNm = lsColumnArray[1];                                  

                     int liDataType = Integer.parseInt(lsColumnArray[7]);
                     VSData loData = loRow.getData(lsAdvColNm);

                     try
                     {
                        switch (liDataType)
                        {
                           case VSData.TINYINT:
                           case VSData.INTEGER:
                           case VSData.SMALLINT:
                           case VSData.BIGINT:
                              lsValue = String.valueOf(loData.getInt());
                              break;
                           case VSData.REAL:
                           case VSData.FLOAT:
                           case VSData.DOUBLE:
                           case VSData.NUMERIC:
                           case VSData.DECIMAL:
                              if (loData.getBigDecimal() != null)
                              {
                                 lsValue = loData.getBigDecimal().toString();
                              }
                              break;
                           case VSData.BIT:
                           case VSData.LONGVARBINARY:
                           case VSData.VARBINARY:
                           case VSData.BINARY:
                           case VSData.LONGVARCHAR:
                           case VSData.CHAR:
                           case VSData.VARCHAR:
                              lsValue = loData.getString();
                              break;
                           case VSData.DATE:
                           case VSData.TIME:
                           case VSData.TIMESTAMP:
                              if (loData.getVSDate() != null)
                              {
                                 lsValue = AMSSQLUtil.formatDate(loData
                                       .getVSDate(),
                                       AMSUtil.GEN_DATE_MONTH_DAY_YEAR_SLASH);
                              }
                              break;
                           default:
                        }
                     }
                     catch (Exception e)
                     {
                       // Add exception log to logger object
                       moLog.error("Unexpected error encountered while processing. ", e);

                        foPageEvent.setCancel(true);
                        foPageEvent.setNewPage(this);
                        raiseException(
                              "Error processing the metadata for the Advantage Type.",
                              AMSMsgUtil.SEVERITY_LEVEL_ERROR);
                     }

                     lsKeyVal = lsECMColNm + "=" + lsValue+"?"+liDataType;
                     loSearchRow.getData(AMSCommonConstants.ATT_FIELD_PREFIX + (liIndex + 1)).setString(
                           lsKeyVal);
                  }

                  StringBuffer loParam = new StringBuffer();
                  loParam.append("EXT_SYS_TYP=" + lsECMType + ",");
                  loParam.append("ADV_COMP=" + lsAdvComp);

                  loSearchRow.getData("PARAMS").setString(loParam.toString());
                  loSearchRow.getData("ADV_SYS_TYP").setString(lsAdvSysTyp);

                  String lsDescription = "ECM Attachment: Linked to Object ID - "
                     + loSearchRow.getData("OBJECT_ID").getString();

                  String lsAttType = Integer.toString(CVL_OBJ_ATT_TYPImpl.ECM_ATTACHMENT);

                  /*
                   * Size of Params Array is determined based on length of
                   * Description string and File name as appl works only with
                   * double digit length strings in the Params Array.
                   */
                  int liNumOfDescParts = AMSHyperlinkActionElement
                        .arraySizeForLargeParm(lsDescription);
                  String lsFileName = loSearchRow.getData("OBJ_ATT_NM")
                        .getString();

                  lsFileName = parseFileName(lsFileName);
                  int liNumOfFileNmParts = AMSHyperlinkActionElement
                        .arraySizeForLargeParm(lsFileName);

                  int liInitialCount = 0;

                  if (AMSStringUtil.strEqual(msObjAttType, OBJ_ATT_TYPE_REF_TBL))
                  {
                     liInitialCount = 4 + liKeyColCount;
                  }
                  else if (AMSStringUtil.strEqual(msObjAttType,
                        OBJ_ATT_TYPE_DOC_VIEW))
                  {
                     //Raise error if either Dept_cd or Adv_module is not mapped
                     if(!lboolDept_cd || !lboolAdv_module)
                     {
                        foPageEvent.setCancel(true);
                        foPageEvent.setNewPage(this); 
                        raiseException(
                              "Error processing the ECM Type: "+lsECMType+": The Document component " +
                              "does not have required entries for dept_cd/adv_module " +
                              "on the Metadata Mapping Table. Unable to attach content.",
                              AMSMsgUtil.SEVERITY_LEVEL_SEVERE);                                   
                     }
                     liInitialCount = 15;
                  }

                  String[] loParams = new String[liInitialCount + liNumOfDescParts
                                                 + liNumOfFileNmParts];

                  int liParamsIndex = 0;
                  String lsMsgTxt;

                  loParams[liParamsIndex++] = msObjAttType;
                  loParams[liParamsIndex++] = msPriGrpID != null ? msPriGrpID : "";

                  liParamsIndex += AMSHyperlinkActionElement
                        .addParmInPartsToParamArray(lsFileName, loParams,
                        liParamsIndex);

                  /*
                   * Call AMSHyperlinkActionElement.addParmInPartsToParamArray
                   * method to add Description and File name String to Params
                   * Array. This method returns the number of indexes used by the
                   * Description/File name string.
                   */
                  liParamsIndex += AMSHyperlinkActionElement
                        .addParmInPartsToParamArray(lsDescription, loParams,
                        liParamsIndex);
                  loParams[liParamsIndex++] = lsAttType;

                  if (AMSStringUtil.strEqual(msObjAttType, OBJ_ATT_TYPE_REF_TBL))
                  {
                     loParams[liParamsIndex++] = msTableName;

                     for (int liCnt = 0; liCnt < liKeyColCount; liCnt++)
                     {
                        loParams[4 + liCnt + liNumOfDescParts + liNumOfFileNmParts] = (String) mvKeyValues
                        .get(liCnt);
                     }
                  }
                  else if (AMSStringUtil.strEqual(msObjAttType,
                        OBJ_ATT_TYPE_DOC_VIEW))
                  {
                     loParams[liParamsIndex++] = msSecGrpID != null ? msSecGrpID
                           : "";
                     loParams[liParamsIndex++] = msDocCat;
                     loParams[liParamsIndex++] = msDocType;
                     loParams[liParamsIndex++] = msDocCode;
                     loParams[liParamsIndex++] = msDocDept;
                     loParams[liParamsIndex++] = msDocID;
                     loParams[liParamsIndex++] = msDocVersNo;
                     loParams[liParamsIndex++] = msDocHdrNm;
                     loParams[liParamsIndex++] = mboolIsDocComp ? "Y" : "N";
                     loParams[liParamsIndex++] = msDocCompNm != null ? msDocCompNm
                           : "";
                     loParams[liParamsIndex++] = msDocIntLnNoNm != null ? msDocIntLnNoNm
                           : "";
                     loParams[liParamsIndex++] = msDocIntLnNo != null ? msDocIntLnNo
                           : "";
                  }

                  lsMsgTxt = AMSHyperlinkActionElement.createMessageText(loParams);

                  try
                  {
                     loSession.setProperty(OBJ_ATT_MSG_TXT, lsMsgTxt);

                     if(moLog.isDebugEnabled())
                     {
                        moLog.debug("OBJ_ATT_MSG_TXT session property: " + lsMsgTxt);         
                     }

                     loSession.setProperty("CLIENT_INITIATED", "T");
                  } /* end try */
                  catch (RemoteException foExp)
                  {
                     // Add exception log to logger object
                     moLog.error("Unexpected error encountered while processing. ", foExp);

                     foPageEvent.setCancel(true);
                     foPageEvent.setNewPage(this);
                     raiseException("Unable to set message properties",
                           SEVERITY_LEVEL_SEVERE);                     
                     return;
                  } /* end catch( RemoteException foExp ) */

                  try
                  {
                     loSearchRow.getData("ACTN_CD").setString(
                           Integer.toString(AMSCommonConstants.ECM_ATTACH));
                     loSearchRow.save();
                     foPageEvent.setNewPage(getSourcePage());                     
                  }
                  catch (Exception foExc)
                  {
                     // Add exception log to logger object
                     moLog.error("Unexpected error encountered while processing. ", foExc);
                     foPageEvent.setCancel( true ) ;
                     foPageEvent.setNewPage( this ) ;
                     raiseException("Unable to attach content from ECM.",
                           SEVERITY_LEVEL_SEVERE);                        
                     break;
                  }
               }/* end if(!AMSStringUtil.strIsEmpty(foPLSReq.getParameter */

            }/* end for (int liCount =1;liCount<=liRowCount;liCount++) */
            if ( getHighestSeverityLevel() > this.SEVERITY_LEVEL_WARNING )
            {
               foPageEvent.setCancel( true ) ;
               foPageEvent.setNewPage( this ) ; 
               return;
            }
            else
            {
               // Refresh Source page and retain row index 
               DataSource loDataSource = getSourcePage().getRootDataSource();
               if(loDataSource != null)
               {                     
                  int liRowIdx = 0; 

                  if(loDataSource.getResultSet()!= null)
                  {
                     //Store previous row index
                     liRowIdx = loDataSource.getResultSet().cursorPosition();
                  }
                  loDataSource.executeQuery();
                  VSResultSet loRsltSet = loDataSource.getResultSet();
                  if(loRsltSet != null)
                  { 
                     //Retain previous row index if row position has changed
                     if(liRowIdx > 0 && liRowIdx != loRsltSet.cursorPosition())
                     {
                        loRsltSet.getRowAt(liRowIdx);
                     }
                  }                    
               }
               removeSessionProp(loSession);
            }
         }
         else if (foActElem.getName().equals("pickcancel"))
         {
            removeSessionProp(loSession);        
         }
      }
      finally
      {                  
         if( loResultSet != null)
         {
            loResultSet.close();  
         }    
      }
   }

   // END_EVENT_pContentSearch_beforeActionPerformed}}
   // {{EVENT_T2R_ADV_SYS_TYP_AfterPickFill
   void T2R_ADV_SYS_TYP_AfterPickFill(Pick obj, DataSource dataSource,
         VSRow supplierRow)
   {
      // Write Event Code below this line

      /*
       * Recreate the hashmap storing the selected document meta data using the
       * picked row's sys typ.
       */

      String lsSysType = supplierRow.getData("ADV_SYS_TYP").getString();
      /*
       * Add the value to hidden element txtT1ADV_SYS_TYPhidden so that it can
       * be retrieved in beforeActionPerformed. This is needed as disabled
       * field values are not sent in PLS Request when form is resubmitted.
       */

      HiddenElement loHiddenCodeElem = (HiddenElement) getElementByName("txtT1ADV_SYS_TYPhidden");
      loHiddenCodeElem.setValue(lsSysType);
      VSQuery loQuery = null;
      VSRow loRow = null;
      VSResultSet loResultSet = null;
      VSSession loSession = this.getParentApp().getSession();
      int liNumRows = -1;
      
      try
      {
         loQuery = new VSQuery(loSession, QRY_ECM_SEARCH,
               "R_ADV_EXT_MAPPING.ADV_SYS_TYP "
               + AMSSQLUtil.getANSIQuotedStr(lsSysType,
                     AMSSQLUtil.EQUALS_OPER), "");
         loResultSet = loQuery.execute();

         loResultSet.last();
         liNumRows = loResultSet.getRowCount();
              
        
         if (moECMSrchMetaData == null)
         {
            moECMSrchMetaData = new ArrayList();
         }/* end if(moECMSrchMetaData == null) */
         moECMSrchMetaData.clear();
         moECMSrchMetaData.trimToSize();
         msECMType = null;
         Hashtable loColumns = new Hashtable();
         for (int liCnt = 1; liCnt <= liNumRows; liCnt++)
         {
            /*
             * Restrict no of search fields to 25 as there is a 
             * limitation on Content Search page to display only 25 
             * search fields. 
             */  
            if(moECMSrchMetaData.size() == 25)
            {
               break;   
            }
            
            loRow = loResultSet.getRowAt(liCnt);
            String lsColumnName = loRow.getData("ADV_ATTR").getString();

            /*
             * If the column already exists in the array, skip it. There could be
             * cases where the same column from different tables HDR,ACTG will be
             * present.
             */
            if (loColumns.containsKey(lsColumnName))
            {
               continue;
            }
            loColumns.put(lsColumnName, lsColumnName);
            String[] lsaMetaData = new String[11];
            lsaMetaData[0] = loRow.getData("EXT_SYS_COL_NM").getString();
            lsaMetaData[1] = loRow.getData("ADV_ATTR").getString();
            lsaMetaData[2] = loRow.getData("ADV_COMP").getString();
            lsaMetaData[3] = loRow.getData("ADV_SYS_TYP").getString();
            lsaMetaData[4] = loRow.getData("EXT_SYS_TYP").getString();
            lsaMetaData[5] = loRow.getData("ADV_ATTR_NM").getString();
            lsaMetaData[6] = loRow.getData("ADV_ATTR_NM_UP").getString();
            lsaMetaData[7] = loRow.getData("DATA_TYP").getString();
            lsaMetaData[8] = loRow.getData("DATA_SIZE").getString();
            lsaMetaData[9] = "";// value

            /*
             * ADD LOGIC TO GET META DATA FROM METAQUERY
             */

            VSMetaQuery loTableMetaQuery = loSession.getMetaQuery(lsaMetaData[2]);
            VSMetaColumn loMetaColumn = loTableMetaQuery
                  .getMetaColumn(lsaMetaData[1]);

            /*
             * The size of column populated by the ECM Synch batch job is the versata
             * (dataobject) size. What that means is for some data types like
             * Integer the size is -1. We need to translate this to the actual size
             * of textbox that is shown on the page. This is done by adding the
             * same logic as added in the archetypes where the ams_max_length, size
             * is given to text boxes depending on the data type. The archetype
             * chosen is the ColumnArchetypeADVQueryTextbox.htm as this gets added
             * for scalar query fields. This is how the mapping is done for fields
             * whose size is returned as -1 in the meta data.
             *
             * Datatype Size VSData.INTEGER 10 VSData.BIT boolean (add CVL_YES_NO)
             * -5 its a BIGINT so no mapping VSData.DATE 10 date VSData.TIME 10
             * date -1 its a LONGVARCHAR so no mapping
             */

            int liColumSize = Integer.parseInt(lsaMetaData[8]);
            int liDataType = Integer.parseInt(lsaMetaData[7]);
            if (liColumSize == -1)
            {
               if (liDataType == VSData.INTEGER)
               {
                  lsaMetaData[8] = "10";
               }
               else if (liDataType == VSData.BIT)
               {
                  lsaMetaData[10] = "CVL_YES_NO";
                  lsaMetaData[8] = "1";
               }
               else if ((liDataType == VSData.DATE) || (liDataType == VSData.TIME))
               {
                  lsaMetaData[8] = "10";
               }

            }/* end if(liColumSize == -1) */

            String lsCodeTable = loMetaColumn.getCodeTable();

            if (!AMSStringUtil.strIsEmpty(lsCodeTable))
            {
               /*
                * This means the column has a drop down attached indicated in
                * the meta data.
                */
               lsaMetaData[10] = lsCodeTable;
            }
            moECMSrchMetaData.add(lsaMetaData);

            msECMType = loRow.getData("EXT_SYS_TYP").getString();

         }/* end if(moECMSrchMetaData == null) */
      }      
      finally
      {
         if( loResultSet != null)
         {
            loResultSet.close();  
         }       
      }

      // set the pick fill type for the dummy vstext elements
      ((AdvECMSearchFields) getElementByName("txtT1ECM_SEARCH"))
            .setAdvSysType(lsSysType);
      ((AdvECMSearchGrid) getElementByName("txtT1ECM_SEARCHGrid"))
            .setAdvSysType(lsSysType);

      /*
       * Since meta data has been recreated we also need to make sure existing
       * data in the resultset for the root datasource is also refreshed. this
       * should only be done if the query has been executed before
       */

      if (this.getRootDataSource().getQuery() != null)
      {
         getRootDataSource().setResultSet(
               this.getRootDataSource().getQuery().getNewResultSet());

      }/* end if(this.getRootDataSource().getQuery() != null) */

   }

   // END_EVENT_T2R_ADV_SYS_TYP_AfterPickFill}}
   // {{EVENT_T2R_ADV_SYS_TYP_BeforePickFill
   void T2R_ADV_SYS_TYP_BeforePickFill(Pick obj, DataSource receiverDataSource,
         VSRow supplierRow, VSOutParam cancel)
   {
      // Write Event Code below this line
      receiverDataSource.setQBFDataForElement("txtT1ADV_SYS_TYP", "");
      HiddenElement loHiddenCodeElem = (HiddenElement) getElementByName("txtT1ADV_SYS_TYPhidden");
      loHiddenCodeElem.setValue("");
   }

   // END_EVENT_T2R_ADV_SYS_TYP_BeforePickFill}}
   // END_EVENT_CODE}}

   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS

      addPageListener(this);
      T2R_ADV_SYS_TYP.addPickListener(this);
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE

   public void beforeActionPerformed(VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq)
   {
      Object source = obj;
      if (source == this)
      {
         pContentSearch_beforeActionPerformed(ae, evt, preq);
      }
   }

   public void AfterPickFill(Pick obj, DataSource dataSource, VSRow supplierRow)
   {
      Object source = obj;
      if (source == T2R_ADV_SYS_TYP)
      {
         T2R_ADV_SYS_TYP_AfterPickFill(obj, dataSource, supplierRow);
      }
   }
   public void BeforePickFill(Pick obj, DataSource receiverDataSource, VSRow supplierRow,VSOutParam cancel)
   {
      Object source = obj;
      if (source == T2R_ADV_SYS_TYP) 
      {
         T2R_ADV_SYS_TYP_BeforePickFill(obj, receiverDataSource, supplierRow, cancel);
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

  /**
   * Parse file name
   * @param fsFilePath
   * @return
   */
   private String parseFileName(String fsFilePath)
   {
      int liIndex;

      liIndex = fsFilePath.lastIndexOf('\\');
      if (liIndex >= 0)
      {
         return fsFilePath.substring(liIndex + 1);
      } /* end if ( liIndex >= 0 ) */
      else
      {
         liIndex = fsFilePath.lastIndexOf('/');
         if (liIndex >= 0)
         {
            return fsFilePath.substring(liIndex + 1);
         } /* end if ( liIndex >= 0 ) */
         else
         {
            return fsFilePath;
         } /* end else */
      } /* end else */
   } /* end parseFileName() */

 /**
  *
  * @param fsPriGrpID
  */
   public void setPrimaryGroupID(String fsPriGrpID)
   {
      msPriGrpID = fsPriGrpID;
   } /* end setPrimaryGroupID() */
  /**
   *
   * @param fsSecGrpID
   */
   public void setSecondaryGroupID(String fsSecGrpID)
   {
      msSecGrpID = fsSecGrpID;
   } /* end setSecondaryGroupID() */

  /**
   *
   * @param fsDocCat
   */
   public void setDocCat(String fsDocCat)
   {
      msDocCat = fsDocCat;
   } /* end setDocCat() */

  /**
   *
   * @param fsDocType
   */
   public void setDocType(String fsDocType)
   {
      msDocType = fsDocType;
   } /* end setDocType() */

  /**
   *
   * @param fsDocCode
   */
   public void setDocCode(String fsDocCode)
   {
      msDocCode = fsDocCode;
   } /* end setDocCode() */

  /**
   *
   * @param fsDocDept
   */
   public void setDocDept(String fsDocDept)
   {
      msDocDept = fsDocDept;
   } /* end setDocDept() */

  /**
   *
   * @param fsDocID
   */
   public void setDocID(String fsDocID)
   {
      msDocID = fsDocID;
   } /* end setDocID() */


   /**
    * @param fsDocVersNo
    */
   public void setDocVersNo(String fsDocVersNo)
   {
      msDocVersNo = fsDocVersNo;
   } /* end setDocVersNo() */

   /**
    * @param fsDocHdrNm
    */
   public void setDocHeaderName(String fsDocHdrNm)
   {
      msDocHdrNm = fsDocHdrNm;
   } /* end setDocHeaderName() */

   /**
    * @param fsDocCompNm
    */
   public void setDocComponentName(String fsDocCompNm)
   {
      msDocCompNm = fsDocCompNm;
   } /* end setDocComponentName() */

   /**
    * @param fsDocIntLnNoNm
    */
   public void setIntLnNoName(String fsDocIntLnNoNm)
   {
      msDocIntLnNoNm = fsDocIntLnNoNm;
   } /* end setIntLnNoName() */

   /**
    * @param fsDocIntLnNo
    */
   public void setIntLnNo(String fsDocIntLnNo)
   {
      msDocIntLnNo = fsDocIntLnNo;
   } /* end setIntLnNo() */

   /**
    * @param fboolIsDocComp
    */
   public void setIsDocComp(boolean fboolIsDocComp)
   {
      mboolIsDocComp = fboolIsDocComp;
   } /* end setIsDocComp() */

   /*
    * @see com.amsinc.gems.adv.vfc.html.AMSPage#generate()
    */
   public String generate()
   {
      return super.generate();
   } /* end generate() */

   /**
    * @param fsTableName
    */
   public void setTableName(String fsTableName)
   {
      msTableName = fsTableName;
   } /* end setTableName() */

   /**
    * @param fvKeyValues
    */
   public void setKeyValues(Vector fvKeyValues)
   {
      mvKeyValues = fvKeyValues;
   } /* end setKeyValues() */

   /**
    * @return
    */
   public ArrayList getSearchMetaData()
   {

      return this.moECMSrchMetaData;
   }

   /* (non-Javadoc)
    * @see AMSPage#useVirtualResultSet(AMSDataSource)
    */
   public boolean useVirtualResultSet(AMSDataSource foDataSource)
   {
      return false;
   } /* end useVirtualResultSet() */

   /**
    * @return
    */
   public int getSelectedRowIndex()
   {
      return miSelectedRowIndex;
   }

   /**
    * @param foHdrRow
    */
   public void setHeaderRow(VSRow foHdrRow)
   {
      moHdrRow = foHdrRow;
   } /* end setHeaderRow() */

   /**
    * @param foCompRow
    */
   public void setCompRow(VSRow foCompRow)
   {
      moCompRow = foCompRow;
   } /* end setCompRow() */

   /**
    * @param fsObjAttType
    */
   public void setObjAttType(String fsObjAttType)
   {
      msObjAttType = fsObjAttType;
   } /* end setObjAttType() */


  /**
   * Create List of meta data column names to be sent as a part of
   * ECM update service for a given Advantage component like ABS_DOC_ACTG.
   * These column names with corresponding values from data object
   * will be sent to ECM during Attach action.
   * @param fsTableNm For which meta data columns will be fetched.
   */
   public void createMetaDataList(String fsTableNm)
   {
      VSQuery loQuery = null;
      VSRow loRow = null;
      VSResultSet loResultSet = null;
      int liNumRows = -1;

      try
      {
         loQuery = new VSQuery(getParentApp().getSession(), QRY_ECM_SEARCH,
               "R_ADV_EXT_MAPPING.ADV_COMP "
               + AMSSQLUtil.getANSIQuotedStr(fsTableNm,
                     AMSSQLUtil.EQUALS_OPER), "");
         loResultSet = loQuery.execute();

         loResultSet.last();
         liNumRows = loResultSet.getRowCount();
                       
         if (moECMAttachMetaData == null)
         {
            moECMAttachMetaData = new ArrayList();
         }/* end if(moECMSrchMetaData == null) */
         moECMAttachMetaData.clear();
         moECMAttachMetaData.trimToSize();

         for (int liCnt = 1; liCnt <= liNumRows; liCnt++)
         {
            /*
             * Restrict no of update fields to 25 as there is a 
             * limitation on Content Search page to attach only 
             * 25 meta data fields. 
             */
            if(moECMAttachMetaData.size() == 25)
            {
               break;   
            }
            String[] lsaMetaData = new String[10];
            loRow = loResultSet.getRowAt(liCnt);
            lsaMetaData[0] = loRow.getData("EXT_SYS_COL_NM").getString();
            lsaMetaData[1] = loRow.getData("ADV_ATTR").getString();
            lsaMetaData[2] = loRow.getData("ADV_COMP").getString();
            lsaMetaData[3] = loRow.getData("ADV_SYS_TYP").getString();
            lsaMetaData[4] = loRow.getData("EXT_SYS_TYP").getString();
            lsaMetaData[5] = loRow.getData("ADV_ATTR_NM").getString();
            lsaMetaData[6] = loRow.getData("ADV_ATTR_NM_UP").getString();
            lsaMetaData[7] = loRow.getData("DATA_TYP").getString();
            lsaMetaData[8] = loRow.getData("DATA_SIZE").getString();
            lsaMetaData[9] = "";// value

            moECMAttachMetaData.add(lsaMetaData);
         }/* end if(moECMSrchMetaData == null) */
      }
      finally
      {
         if( loResultSet != null)
         {
            loResultSet.close();  
         }  
      }
   }
   
   /**
    * Removes Table session properties
    * @param foSession
    */
   public void removeSessionProp(VSORBSession foSession)
   {
      try
      {
         foSession.removeProperty(AMSCommonConstants.PG_CD_FOR_TABLES);
         foSession.removeProperty(AMSCommonConstants.DEPT_CD_FOR_TABLES);
      }
      catch (Exception loExc)
      {
          // Add exception log to logger object
          moLog.error("Unexpected error encountered while processing. ", loExc);

         raiseException(
               "Error removing session properties " + AMSCommonConstants.PG_CD_FOR_TABLES
               + ", " + AMSCommonConstants.DEPT_CD_FOR_TABLES, SEVERITY_LEVEL_ERROR);      
         return;
      }
   }
   public String doAction( PLSRequest foPLSReq )
   {
      String lsResponse;
      lsResponse = super.doAction( foPLSReq );
      if( msAttchFile != null )
      {
         lsResponse = msAttchFile;
         msAttchFile = null;
      } /* end if ( msAttchFile != null ) */
      return lsResponse;
   } /* end doAction() */
}
