//{{COMPONENT_IMPORT_STMTS
package advantage;
import java.util.Enumeration;
import java.util.Vector;
import versata.common.*;
import versata.common.vstrace.*;
import versata.vls.cache.*;
import versata.vls.*;
import java.util.*;
import java.text.*;
import java.math.*;
import com.amsinc.gems.adv.common.*;
import com.versata.util.logging.*;
import org.apache.commons.logging.*;

//END_COMPONENT_IMPORT_STMTS}}
import java.util.Calendar;
import java.util.HashMap;
import com.cgi.adv.doc.AdvDocViewUtil;
import java.io.OutputStream;
/*
**  E_SIG_CTLG
*/

//{{COMPONENT_RULES_CLASS_DECL
public class E_SIG_CTLGImpl extends  E_SIG_CTLGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public E_SIG_CTLGImpl (){
		super();
	}
	
	public E_SIG_CTLGImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeDelete
public void beforeDelete(DataObject foObj, Response foResponse)
{
   if(getLINK_FL())
   {
      setNull(AMSCommonConstants.ATTR_OBJ_ATT_PG_UNID );
   }
}
//END_COMP_EVENT_beforeDelete}}

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static E_SIG_CTLGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new E_SIG_CTLGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
		
   private static Log moLog = AMSLogger.getLog(E_SIG_CTLGImpl.class,
         AMSLogConstants.FUNC_AREA_DOC_SIGNATURES);

   public static final String E_SIG_CTLG = "E_SIG_CTLG";
   
   /**
    * Removes the Signature Catalog entry for the Approval Level passed as parameter
    * 
    * @param foSession Session Object
    * @param fsDocCode Document Code
    * @param fsDocDeptCode Document Department Code
    * @param fsDocID Document Id
    * @param fiDocVer Document Version Number
    * @param fiApprovalLevel Document Approval Level
    */
   public static void deleteDocSignatureForAprvLvl(Session foSession,
         String fsDocCode, String fsDocDeptCode, String fsDocID, int fiDocVer,
         int fiApprovalLevel)
   {
      SearchRequest loRequest = new SearchRequest();

      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_CD,
            fsDocCode);
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_DEPT_CD,
            fsDocDeptCode);
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_ID,
            fsDocID);
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_VERS_NO,
            String.valueOf(fiDocVer));
      if(fiApprovalLevel > -1)
      {
         loRequest.addParameter(E_SIG_CTLG, "DOC_APRV_LVL",
               String.valueOf(fiApprovalLevel));
      }

      Enumeration loEnum = E_SIG_CTLGImpl.getObjects(loRequest, foSession);

      if (moLog.isDebugEnabled())
      {
         moLog.debug("Deleting Signature entries for Document: "
               + fsDocCode
               + " "
               + fsDocDeptCode
               + " "
               + fsDocID
               + " "
               + String.valueOf(fiDocVer)
               + ((fiApprovalLevel > -1) ? " "
                     + String.valueOf(fiApprovalLevel) : ""));
      }
      while (loEnum.hasMoreElements())
      {
         E_SIG_CTLGImpl loNewSignCtlgObj = (E_SIG_CTLGImpl) loEnum
               .nextElement();
         loNewSignCtlgObj.setDelete();
         loNewSignCtlgObj.save(AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE);
      }
   }
   
   /**
    * Deletes Signature Catalog entry for Document which 
    * was signed earlier for Action passed in as parameter.
    * 
    * @param foSession Application Session
    * @param fsDocCode Document Code
    * @param fsDocDeptCode Document Department Code 
    * @param fsDocID Document ID
    * @param fiDocVer Document Version
    * @param fiAction Action Code
    */
   public static void deleteDocSignatureForAction(Session foSession,
         String fsDocCode, String fsDocDeptCode, String fsDocID, int fiDocVer,
         int fiAction)
   {
      SearchRequest loRequest = new SearchRequest();

      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_CD,
            fsDocCode);
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_DEPT_CD,
            fsDocDeptCode);
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_ID,
            fsDocID);
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_VERS_NO,
            String.valueOf(fiDocVer));
      loRequest.addParameter(E_SIG_CTLG, "DOC_ACTN_CD",
               String.valueOf(fiAction));

      Enumeration loEnum = E_SIG_CTLGImpl.getObjects(loRequest, foSession);

      if (moLog.isDebugEnabled())
      {
         moLog.debug("Deleting Signature entries for Document: "
               + fsDocCode
               + " "
               + fsDocDeptCode
               + " "
               + fsDocID
               + " "
               + String.valueOf(fiDocVer) + " "
               + fiAction);
      }
      while (loEnum.hasMoreElements())
      {
         E_SIG_CTLGImpl loNewSignCtlgObj = (E_SIG_CTLGImpl) loEnum
               .nextElement();
         loNewSignCtlgObj.setDelete();
         loNewSignCtlgObj.save(AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE);
      }
   }
   
   /**
    * Deletes all the Signature Catalog history for the Document. This is
    * generally called for actions like Reject All and Document Discard.
    * 
    * @param foSession Session object
    * @param fsDocCode Document Code
    * @param fsDocDeptCode Document Department Code
    * @param fsDocID Document Id
    */
   public static void deleteDocSignatures(Session foSession,
         String fsDocCode, String fsDocDeptCode, String fsDocID, int fiDocVer)
   {
      deleteDocSignatureForAprvLvl(foSession, fsDocCode, fsDocDeptCode,
            fsDocID, fiDocVer, -1);
   }


   /**
    * Stores the Signed Document to the Signature Catalog table.
    * 
    * @param foTarget Document Header
    * @param fiAction User initiated signing action
    * @param fsSignedDocLocation Signed location
    * @param fsCopiedDoc Incase if same Document is signed as compared to previously signed Document the the Attachment Catalog reference.
    * @param fiAprvLvl Approval level
    * @param foCal Calendar instance when the Document was signed.
    * @throws Exception
    */
   public static void saveDocSignature(AMSDataObject foTarget, int fiAction,
         String fsSignedDocLocation, String fsCopiedDoc, int fiAprvLvl, Calendar foCal)
         throws Exception
   {
      Session loSession = foTarget.getSession();
      String lsUser = loSession.getUserID();
      VSDate loDate = new VSDate(foCal.getTime());
      String lsDocCd = foTarget.getData(ATTR_DOC_CD).getString();
      String lsDocDeptCd = foTarget.getData(ATTR_DOC_DEPT_CD).getString();
      String lsDocId = foTarget.getData(ATTR_DOC_ID).getString();
      int liDocVersNo = foTarget.getData(ATTR_DOC_VERS_NO).getint();
      
      
      if( (fiAction == DOC_ACTN_BYPS_APRV) || (fiAction == DOC_ACTN_SUBMIT) )
      {
         /*
          * There can be only one catalog entry for these actions. Although
          * Document Engine handles Signatures this is an additional check.
          */
         E_SIG_CTLGImpl.deleteDocSignatureForAction(loSession, lsDocCd,
               lsDocDeptCd, lsDocId, liDocVersNo, fiAction);
      }
      else
      {
         /*
          * In case of scenario wherein user at approval level 2 rejects
          * Document then it goes to Approval level 1. In this case signature
          * catalog entry for approval level is still present in the table. Now
          * if user approves the Document then the existing signature catalog
          * entry must be updated. So instead of updating, delete the existing
          * entry and re-insert new one.
          */
         E_SIG_CTLGImpl.deleteDocSignatureForAprvLvl(loSession,
                  lsDocCd, lsDocDeptCd, lsDocId, liDocVersNo, fiAprvLvl);
      }
      
      if(fiAprvLvl == -1)
      {
         fiAprvLvl = 0;
      }
      
      String lsREASON = getSigningReason(fiAction);
      
      E_SIG_CTLGImpl loNewSignCtlgObj = (E_SIG_CTLGImpl) E_SIG_CTLGImpl
            .getNewObject(loSession, true);
      loNewSignCtlgObj.getData("USER_ID").setString(lsUser);
      loNewSignCtlgObj.getData(ATTR_DOC_CD).setString(lsDocCd);
      loNewSignCtlgObj.getData(ATTR_DOC_DEPT_CD).setString(lsDocDeptCd);
      loNewSignCtlgObj.getData(ATTR_DOC_ID).setString(lsDocId);
      loNewSignCtlgObj.getData(ATTR_DOC_VERS_NO).setString(
            String.valueOf(liDocVersNo));
      loNewSignCtlgObj.getData(ATTR_DOC_ACTN_CD).setint(fiAction);
      loNewSignCtlgObj.getData("SIG_TS").setVSDate(loDate);
      loNewSignCtlgObj.getData("SIG_TYP").setint(
            ((AMSDocHeader) foTarget).getSignatureType());
      loNewSignCtlgObj.getData("DOC_APRV_LVL").setint(fiAprvLvl);
      loNewSignCtlgObj.getData("SIG_MEMO").setString(lsREASON);
      if (fsCopiedDoc != null)
      {
         loNewSignCtlgObj.getData("LINK_FL").setboolean(true);
         loNewSignCtlgObj.getData("OBJ_ATT_PG_UNID").setString(fsCopiedDoc);
      }
      else
      {

         loNewSignCtlgObj.getData("LINK_FL").setboolean(false);
         /*
          * When document goes to pending by default other table doesn't get
          * saved but we will need to save this data so adding save behavior.
          */
         IN_OBJ_ATT_CTLGImpl loAttCtlg = loNewSignCtlgObj.addObjectAttachment(
               fsSignedDocLocation, "Signed Document", 1, loDate, lsUser, true,
               AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE);
         loAttCtlg.getData("OBJ_ATT_COMP_NM").setString(E_SIG_CTLG);
      }

      loNewSignCtlgObj.save(AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE);
   }
   
   /**
    * Returns the Signature Catalog Entry for the corresponding Document.
    * @param foTarget Document header dataobject
    * @throws Exception
    */
   public static E_SIG_CTLGImpl getLastSignedDocument(AMSDataObject foDocHeader) throws Exception
   {
      Enumeration loEnum = getAllSignaturesForDocAprvLevel(foDocHeader, -1);
      if (loEnum.hasMoreElements())
      {
         return (E_SIG_CTLGImpl) loEnum.nextElement();
      }

      return null;
   }
   
   /**
    * Returns the last Signature Catalog entry for the this Document at
    * provided Workflow Approval Level.
    * 
    * @param foTarget Document Header
    * @param fiApprvLvl Approval Level
    * @return Signature Catalog entry
    * @throws Exception
    */
   public static E_SIG_CTLGImpl getLastSignedDocumentForApprvLvl(
         AMSDataObject foDocHeader, int fiAprvLvl) throws Exception
   {
      Enumeration loEnum = getAllSignaturesForDocAprvLevel(foDocHeader, fiAprvLvl);
      if (loEnum.hasMoreElements())
      {
         return (E_SIG_CTLGImpl) loEnum.nextElement();
      }

      return null;

   }
   
   /**
    * Returns an Enumeration of the Signature Catalog entries for this Document
    * sorted with last Signed Document.
    * 
    * @param foDocHeader Document Header
    * @return Enumeration
    */
   public static Enumeration getAllSignaturesForDocument(AMSDataObject foDocHeader)
   {
      return getAllSignaturesForDocAprvLevel(foDocHeader, -1);
   }

   /**
    * Returns an Enumeration of the Signature Catalog entries for this Document
    * at provided Approval Level sorted with last signed Document.
    * 
    * @param foDocHeader Document Header
    * @param fiAprvLvl Document Approval Level
    */
   public static Enumeration getAllSignaturesForDocAprvLevel(AMSDataObject foDocHeader, int fiAprvLvl)
   {

      Session loSession = foDocHeader.getSession();
      Vector loSortCols = new Vector(1);
      loSortCols.add("ID");

      SearchRequest loRequest = new SearchRequest();

      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_ID,
            foDocHeader.getData(ATTR_DOC_ID).getString());
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_CD,
            foDocHeader.getData(ATTR_DOC_CD).getString());
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_DEPT_CD,
            foDocHeader.getData(ATTR_DOC_DEPT_CD).getString());
      loRequest.addParameter(E_SIG_CTLG, ATTR_DOC_VERS_NO,
            String.valueOf(foDocHeader.getData(ATTR_DOC_VERS_NO).getint()));
      
      if(fiAprvLvl > -1)
      {
         loRequest.addParameter(E_SIG_CTLG, "DOC_APRV_LVL",
               String.valueOf(fiAprvLvl));
      }
      
      if(moLog.isDebugEnabled())
      {
         moLog.debug("Querying signature entries for Document: "
               + foDocHeader.getData(ATTR_DOC_ID).getString() + " "
               + foDocHeader.getData(ATTR_DOC_CD).getString() + " "
               + foDocHeader.getData(ATTR_DOC_DEPT_CD).getString() + " "
               + String.valueOf(foDocHeader.getData(ATTR_DOC_VERS_NO).getint()) + " " 
               + String.valueOf(fiAprvLvl));
      }
      return E_SIG_CTLGImpl.getObjectsSorted(loRequest,
            loSession, loSortCols, AMSDataObject.SORT_DSC);

   }
   
   /**
    * Returns the Signature Document from the Attachment Store
    * @param foE_SIG_CTLGImpl Signature catalog entry
    */
   public static byte[] getSignedDocFromCatalog(E_SIG_CTLGImpl foE_SIG_CTLGImpl)
   {
      Session loSession = foE_SIG_CTLGImpl.getSession();
      Enumeration loEnum = IN_OBJ_ATT_CTLGImpl.getObjectAttachments(foE_SIG_CTLGImpl, loSession);
      IN_OBJ_ATT_CTLGImpl loAttCtlg = (IN_OBJ_ATT_CTLGImpl) loEnum
            .nextElement();

      IN_OBJ_ATT_STORImpl loAttStor;
      SearchRequest loSrchReq = new SearchRequest();
      String lsAttID;

      lsAttID = loAttCtlg.getData(AMSCommonConstants.ATTR_OBJ_ATT_UNID)
            .getString();
      loSrchReq.addParameter("IN_OBJ_ATT_STOR",
            AMSCommonConstants.ATTR_OBJ_ATT_UNID, lsAttID);
      loAttStor = (IN_OBJ_ATT_STORImpl) IN_OBJ_ATT_STORImpl.getObjectByKey(
            loSrchReq, loSession);

      if (loAttStor != null)
      {
         return loAttStor.getData("OBJ_ATT_DATA").getbytes();
      }
      return null;

   }
   
   /**
    * Returns signing reason for corresponding action.
    * 
    * @param fiAction User action.
    */
   public static String getSigningReason(int fiAction)
   {
      String lsReason = null;
      switch(fiAction)
      {
         case DOC_ACTN_APPROVE: 
            lsReason = SIGN_REASON_APPROVAL;
            break;
         case DOC_ACTN_SUBMIT: 
            lsReason = SIGN_REASON_SUBMIT;
            break;
         case DOC_ACTN_BYPS_APRV: 
            lsReason = SIGN_REASON_BYPASS_APPROVAL;
            break;
         default: 
            lsReason = "";
            break;
      }
      return lsReason;
   }
   
   /**
    * Archives the associated signature catalog entries for this Document.
    * Signature catalog entries are exported to the Document xml similar to
    * Document comments. Signed Document resources like the Document PDF are
    * exported to the corresponding Document attachment archive.
    * 
    * @param foHeader Document Header
    * @param flArchiveID Document Archive Catalog ID for this Document
    * @return true if successful
    * @throws AMSXMLImportExportException
    */
   public static boolean archiveDocSignatures(AMSDocHeader foHeader, long flArchiveID)
         throws AMSXMLImportExportException
   {
      moLog.debug(">>> Inside archiveDocSignatures");
      E_SIG_CTLGImpl loSignHist = null;
      
      //Get all the signature catalog entries for this document.
      Enumeration loSigHistEnum = E_SIG_CTLGImpl.getAllSignaturesForDocument(foHeader);

      if (loSigHistEnum != null)
      {
         moLog.debug("Exporting Document signature catalog entries to Document xml.");
         while (loSigHistEnum.hasMoreElements())
         {
            //Export each signature catalog entry to the Document xml.
            loSignHist = (E_SIG_CTLGImpl) loSigHistEnum.nextElement();
            foHeader.moXMLDocExporter.toXML (loSignHist, null);
            
            /*
             * Store the signed Document resources attached to this catalog
             * entry to the attachment archive storage
             */
            try
            {
               loSignHist.archiveDocSignatureAtt(foHeader, flArchiveID);
            }
            catch ( AMSArchiveStorageException loArchiveException )
            {
               foHeader.raiseException( "Error while storing document attachment archive: " +
                  loArchiveException.getMessage() ) ;
               return false ;
            }
            catch ( Exception loException )
            {
               foHeader.raiseException( "Error while archiving document attachment: " +
                  loException.getMessage() ) ;
               foHeader.moXMLDocExporter = null ;
               return false ;
            }            
            //Delete the signature catalog entry.
            loSignHist.setDelete();
            loSignHist.save();
         }
      }
      return true;
   }
   /**
    * Archives the associated attachments for the this Signature catalog entry 
    * 
    * @param foHeader Document Header
    * @param foAttArchStorage Archive Storage instance
    * @param flArchiveID Archive ID
    * @throws Exception
    */
   public void archiveDocSignatureAtt(AMSDocHeader foHeader,
         long flArchiveID) throws Exception
   {
      Enumeration loAttCtlgEnum = null;
      IN_OBJ_ATT_CTLGImpl loAttCtlg = null;
      IN_DOC_ARCH_ATTImpl loAttArchCtlg = null;

      // Get attachment catalog entries
      loAttCtlgEnum = IN_OBJ_ATT_CTLGImpl.getAttachmentInfoByPrimGrp(getData("OBJ_ATT_PG_UNID").getString(), getSession());

      moLog.debug("Exporting signature attachment catalog entries to Document xml.");
      /*
       * For each attachment, create an attachment archive catalog entry and
       * archive off the attachment data
       */
      while (loAttCtlgEnum.hasMoreElements())
      {
         // Make corresponding attachment catalog entry in Attachment archive
         // catalog table
         loAttCtlg = (IN_OBJ_ATT_CTLGImpl) loAttCtlgEnum.nextElement();

         loAttArchCtlg = (IN_DOC_ARCH_ATTImpl) IN_DOC_ARCH_ATTImpl
               .getNewObject(getSession(), true);

         loAttArchCtlg.copyCorresponding(loAttCtlg, new Vector(1), true);

         loAttArchCtlg.setDOC_ARCH_ID(flArchiveID);
         loAttArchCtlg.save();


         Enumeration loEnum = loAttCtlg.getAttachmentData();

         if (loEnum.hasMoreElements())
         {
            //In case if the storage archive is null create a new and initialize the member variable
            if (foHeader.moAttArchStorage == null)
            {
               foHeader.moAttArchStorage = AMSArchiveStorage.getInstance(getSession());

               foHeader.moAttArchStorage.setArchFileLocation(foHeader.getArchFileLocation());

               foHeader.moAttArchStorage.createNewArchive(flArchiveID,
                     "ADVANTAGE Document Attachment",
                     AMSArchiveStorage.ARCHIVE_TYPE_OBJ_ATT);
            }
            /*
             * There is only one attachment per catalog entry; now add it to the
             * archive file
             */
            IN_OBJ_ATT_STORImpl loAttData = (IN_OBJ_ATT_STORImpl) loEnum
                  .nextElement();

            OutputStream loOS = foHeader.moAttArchStorage.addArchiveData(
                  loAttCtlg.getOBJ_ATT_UNID(), loAttCtlg.getOBJ_ATT_NM());

            /*
             * This BLOB data should really be "chunked" but Versata does not
             * support this yet. Need to change this when they do support BLOB
             * "chunking"
             */
            byte[] loData = loAttData.getData("OBJ_ATT_DATA").getbytes();

            moLog.debug("Writing signed PDF to the archive zip.");
            loOS.write(loData, 0, loData.length);

            foHeader.moAttArchStorage.saveArchiveData();
         }

         /*
          * Check if OBJ_ATT_ALT_UNID exist, if yes, do not delete unless
          * OBJ_ATT_ALT_ST is set to 'Deleted'. Also set OBJ_ATT_ST to
          * 'ARCHIVED' for these records. Delete all others.
          */
         if (loAttCtlg.getData(ATTR_OBJ_ATT_ALT_UNID).getObject() != null
               && loAttCtlg.getData(ATTR_OBJ_ATT_ALT_ST).getint() != OBJ_ATT_ST_DELETED)
         {
            loAttCtlg.getData(ATTR_OBJ_ATT_ST).setint(OBJ_ATT_ST_ARCHIVED);
            loAttCtlg.save();
         }
         else
         {
            /*
             * Remove the attachment catalog entry from the system
             */
            loAttCtlg.setDelete();
            loAttCtlg.save();
         }
      }
   }
}

