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
import java.lang.reflect.*;

/*
**  DOC_CMNT
*/

//{{COMPONENT_RULES_CLASS_DECL
public class DOC_CMNTImpl extends  DOC_CMNTBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   /** Constant for Document comment number sequence name */
   public static final String C_DOC_CMNT_NO_SN = "DOC_CMNT";
//{{COMP_CLASS_CTOR
public DOC_CMNTImpl (){
	super();
}

public DOC_CMNTImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
/**
* Will be called prior to insert to perform necessary actions. 
*
* @param obj DataObject being updated.
* @param response The response object.
*/
public void beforeInsert(DataObject obj, Response response)
{
   String lsDocHeaderInfo = getSession().getProperty(SESSION_DOC_CMNT_MSG_TXT);
   
   //session property set, insert is triggered from Document Comment page
   //set fields accordingly & set cmnt_fl to true on doc_hdr (current + later versions).
   if (!AMSStringUtil.strIsEmpty(lsDocHeaderInfo)) 
   {
      String lsDocCd;
      String lsDocId;
      String lsDocDeptCd;
      String lsDocVersNo;
      String lsDocPhaseCd;
      int liMsgIndex = 2;
      

      lsDocCd = getMsgValue( lsDocHeaderInfo, liMsgIndex ) ;
      liMsgIndex += lsDocCd.length() + 2 ;
      
      lsDocId = getMsgValue( lsDocHeaderInfo, liMsgIndex ) ;
      liMsgIndex += lsDocId.length() + 2 ;
      
      lsDocDeptCd = getMsgValue( lsDocHeaderInfo, liMsgIndex ) ;
      liMsgIndex += lsDocDeptCd.length() + 2 ;
            
      lsDocVersNo = getMsgValue( lsDocHeaderInfo, liMsgIndex ) ;
      liMsgIndex += lsDocVersNo.length() + 2 ;
      
      lsDocPhaseCd = getMsgValue( lsDocHeaderInfo, liMsgIndex ) ;
      liMsgIndex += lsDocPhaseCd.length() + 2 ;
            
      setDOC_CD(lsDocCd);
      setDOC_ID(lsDocId);
      setDOC_DEPT_CD(lsDocDeptCd);
      setDOC_VERS_NO(Integer.parseInt(lsDocVersNo));
      setDOC_PHASE_CD(Integer.parseInt(lsDocPhaseCd));
      
      //Get all XXX_DOC_HDR for current + later versions & set CMNT_FL to true
      //mediation will update associated DOC_HDRs accordingly.
      //Enum is sorted by DOC_VERS_NO
      Enumeration loEnum = getDocHdrInclLaterVers(getDOC_CD(), getDOC_DEPT_CD(), 
                                             getDOC_ID(), getDOC_VERS_NO());     
      if (loEnum == null)
      {
         //reset session property

         raiseException ( "Error retrieving current and/or later versions of this document.", 
                              AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
         return;
      }
      
      AMSDocHeader loDocHdr = null;
      while (loEnum.hasMoreElements())
      {
         loDocHdr = (AMSDocHeader) loEnum.nextElement();
         
         if (loDocHdr!=null && loDocHdr.getData(ATTR_DOC_CMNT_FL)!=null 
               && !loDocHdr.getData(ATTR_DOC_CMNT_FL).getboolean()) 
         {
            //if CMNT_FL is not already set to true, set to true and save
            loDocHdr.getData(ATTR_DOC_CMNT_FL).setboolean(true);
            loDocHdr.save();
         }
         else
         {
            //if any future version has cmnt_fl=true, subsequent versions should already be set
            break;
         }
      }      
   }
}
//END_COMP_EVENT_beforeInsert}}

//{{COMP_EVENT_beforeDelete
/**
* Event handler to be called prior to delete of the data object to perform
* necessary actions.
*
* @param obj The DataObject being deleted.
* @param response The response object.
*/
public void beforeDelete(DataObject obj, Response response)
{
   //Write Event Code below this line
   String lsDocCd = getDOC_CD();
   String lsDocDeptCd = getDOC_DEPT_CD();
   String lsDocId = getDOC_ID();
   int liDocVersNo = getDOC_VERS_NO();
   
   String lsOnlineDel = getSession().getProperty(SESSION_DOC_CMNT_MSG_TXT);
   
   //if session property is set, then perform delete cleanup to reset cmnt_fl on
   //associated xxx_doc_hdr bojects (mediation will then update doc_hdr accordingly).
   //if not set, updates will not be performed (e.g. archive and import).
   if (lsOnlineDel!=null && lsOnlineDel.equals(SESSION_DOC_CMNT_ONLINE_DELETE)) // from online
   {
      //reset session property
      getSession().setProperty(SESSION_DOC_CMNT_MSG_TXT, "");

      int liCount = getCmntCntInclPrevVers(lsDocCd, lsDocDeptCd, lsDocId, liDocVersNo);
      if (liCount<=1) 
      {
         //if other than this comment there is no more comments for the same doc, 
         //thus set cmt_fl to false on XXX_DOC_HDRs (mediation will then update DOC_HDRs).
         //perform same check for all later versions to ensure cmnt_fl synchorization
         Enumeration loEnum = getDocHdrInclLaterVers(lsDocCd, lsDocDeptCd, 
                                                      lsDocId, liDocVersNo);                                                      
         if (loEnum == null)
         {
            raiseException ( "Error retrieving this document and/or its prior versions.", 
                                 AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
            return;
         }
                                                      
         AMSDocHeader loDocHdr = null;
         while (loEnum.hasMoreElements())
         {
            loDocHdr = (AMSDocHeader)loEnum.nextElement();
            if (loDocHdr!=null)
            {
               int liVersCmntCnt = liCount; 
               
               //for later versions, check the count of comments
               //no need to check count for current version since checked already
               if (loDocHdr.getData(ATTR_DOC_VERS_NO)!=null 
                  && loDocHdr.getData(ATTR_DOC_VERS_NO).getint()>liDocVersNo) 
               {
                  liVersCmntCnt = getCmntCntInclPrevVers(lsDocCd, lsDocDeptCd, 
                                                         lsDocId, 
                                                         loDocHdr.getData(ATTR_DOC_VERS_NO).getint());
               }
               //no more comments exist for this version and prior, reset flag
               if (liVersCmntCnt<=1) 
               {
                  if (loDocHdr.getData(ATTR_DOC_CMNT_FL)!=null 
                        && loDocHdr.getData(ATTR_DOC_CMNT_FL).getboolean())
                  {
                     loDocHdr.getData("DOC_CMNT_FL").setboolean(false);
                     loDocHdr.save();
                  }
               }
               else 
               {
                  //if comment count for a particular version is higher than 1
                  //then do not flip cmnt_fl for this particular and later version
                  //Enumeration of header objects in order of version number.
                  break; 
               }
            }
            else //cannot get doc_hdr
            {
               raiseException( "Error while trying to get reference to document", AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
               break;
            }
         } /* end while has more doc headers to update */
      } /* end if (liCount<=1) */
   } /* end online event */
}
//END_COMP_EVENT_beforeDelete}}

//END_EVENT_CODE}}



   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	addRuleEventListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{COMPONENT_RULES
	public static DOC_CMNTImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new DOC_CMNTImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}
   
   /**
   * Checks to see if user is authorized to add comment to the document
   * 
   * @return boolean true if user is authorized to add comment to this document, 
   * false otherwise
   */
   public boolean isInsertAuthorized()
   {
      AMSDocHeader loHdr = getDocHeader(getDOC_CD(), getDOC_DEPT_CD(), 
                                          getDOC_ID(), getDOC_VERS_NO());
      return loHdr.isAddCommentAuthorized();
   } /* end isInsertAuthorized() */
      
   /**
   * Returns the doc header (XXX_DOC_HDR) identified by the given parameters.
   *
   * @param fsDocCd Document Code of the document header to get.
   * @param fsDocDeptCd Department Code of the document header to get.
   * @param fsDocId Document Id of the document header to get.
   * @param fiDocVersNo Document version number of the document header to get.
   *
   * @return AMSDocHeader XXX_DOC_HDR object associated with this document comment.
   */
   private AMSDocHeader getDocHeader(String fsDocCd, String fsDocDeptCd, 
                                             String fsDocId, int fiDocVersNo)
   {
      Session loSession = getSession();
      try
      {
         String lsDocHdrClassName = getDocHdrClassName(fsDocCd);
         Class lClass = Class.forName(lsDocHdrClassName);
         AMSDocHeader loDocHeader = (AMSDocHeader) lClass.newInstance() ;
         
         Class [] loParmTypes = new Class[2] ;
         loParmTypes [0] = SearchRequest.class ;
         loParmTypes [1] = Session.class ;
      
         Method lMethod = lClass.getMethod( "getObjectByKey", loParmTypes ) ;
      
         SearchRequest loSrchReq = new SearchRequest();
         Parameter loParm = null ;
      
         loParm = new Parameter( loDocHeader.getComponentName(),
            ATTR_DOC_CD, fsDocCd);
         loSrchReq.add( loParm ) ;
      
         loParm = new Parameter( loDocHeader.getComponentName(),
            ATTR_DOC_DEPT_CD, fsDocDeptCd);
         loSrchReq.add( loParm ) ;
      
         loParm = new Parameter( loDocHeader.getComponentName(),
            ATTR_DOC_ID, fsDocId);
         loSrchReq.add( loParm ) ;
      
         loParm = new Parameter( loDocHeader.getComponentName(),
            ATTR_DOC_VERS_NO, String.valueOf(fiDocVersNo));
         loSrchReq.add( loParm ) ;
      
         Object [] args          = new Object[2] ;
         args [0]      = loSrchReq ;
         args [1]      = loSession;
      
         return (AMSDocHeader) lMethod.invoke( loDocHeader, args ) ;
      }
      catch ( Exception ex )
      {
         raiseException( "Fatal error while trying to get reference to document: "
            + ex.getMessage() ) ;
         return null;
      }
   } /* end getDocHeader() */

   /**
   * Returns an enumeration of XXX_DOC_HDRs associated with the given document 
   * identifiers and its later versions.  The retrieved enumeration will be sorted
   * by DOC_VERS_NO in ascending order.
   *
   * @param fsDocCd Document Code of the document header to get.
   * @param fsDocDeptCd Department Code of the document header to get.
   * @param fsDocId Document Id of the document header to get.
   * @param fiCurrentDocVersNo Starting Document version number of the 
   * document header to get (will return doc headers of this version 
   * and later versions if exists).
   *
   * @return AMSDocHeader XXX_DOC_HDR object associated with this document comment.
   */
   private Enumeration getDocHdrInclLaterVers (String fsDocCd, String fsDocDeptCd, 
                                                String fsDocId, int fiCurrentDocVersNo)
   {
      Session loSession = getSession();
      try
      {
         String lsDocHdrClassName = getDocHdrClassName(fsDocCd);
         Class lClass = Class.forName(lsDocHdrClassName);
      
         AMSDocHeader loDocHeader = (AMSDocHeader) lClass.newInstance() ;
      
         Class [] loParmTypes = new Class[2] ;
         loParmTypes [0] = SearchRequest.class ;
         loParmTypes [1] = Session.class ;
      
         Method lMethod = lClass.getMethod( "getObjects", loParmTypes ) ;
      
         SearchRequest loSrchReq = new SearchRequest();
         
         StringBuffer         loWhereClause = new StringBuffer();
         //Add an additional where clause to retrieve all doc hdr with version prior
         loWhereClause.append(ATTR_DOC_CD);
         loWhereClause.append(" = '");
         loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsDocCd));
         loWhereClause.append("' AND ");
         loWhereClause.append(ATTR_DOC_DEPT_CD);
         loWhereClause.append(" = '");
         loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsDocDeptCd));
         loWhereClause.append("' AND ");
         loWhereClause.append(ATTR_DOC_ID);
         loWhereClause.append(" = '");
         loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsDocId));
         loWhereClause.append("' AND ");
         loWhereClause.append(ATTR_DOC_VERS_NO);
         loWhereClause.append(" >= ");
         loWhereClause.append(fiCurrentDocVersNo);
         
         loSrchReq.add(loWhereClause.toString());
         
         Object [] args          = new Object[2] ;
         args [0]      = loSrchReq ;
         args [1]      = loSession;

         Enumeration loEnum = (Enumeration) lMethod.invoke( loDocHeader, args );
         
         //sort the XXX_DOC_HDRs by DOC_VERS_NO ascending
         Enumeration loSortedEnum = null ;
         Vector loSortCols = new Vector(1);
         loSortCols.add(ATTR_DOC_VERS_NO);
         loSortedEnum = AMSDataObject.sort( loEnum, loSortCols, AMSDataObject.SORT_ASC);
         
         //return sorted enumeration of XXX_DOC_HDR objects
         return loSortedEnum;
      }
      catch ( Exception ex )
      {
         raiseException( "Error while trying to get document and later versions: "
            + ex.getMessage() ) ;
         return null;
      }
   } /* end getDocHdrInclLaterVers() */
   
   /**
   * Returns the XXX_DOC_HDR class name for the given document ("XXX_DOC_HDRImpl").
   *
   * @param fsDocCd The Document Code of the XXX_DOC_HDR class name to be returned. 
   * @return String XXX_DOC_HDR class name
   */
   private String getDocHdrClassName(String fsDocCd)
   {
      Session loSession = getSession();
      //get DOC_TYPE from doc control table.
      SearchRequest lsrRequest = new SearchRequest();
      lsrRequest.add(new Parameter ( "R_GEN_DOC_CTRL" , ATTR_DOC_CD, fsDocCd ));

      R_GEN_DOC_CTRLImpl loGenDocCtrl = (R_GEN_DOC_CTRLImpl)R_GEN_DOC_CTRLImpl.getObjectByKey(
      lsrRequest, loSession);
   
      return loSession.getPackageName() + "." + loGenDocCtrl.getData(
               ATTR_DOC_TYP).getString() + "_DOC_HDRImpl";   
   } /* end getDocHdrClassName() */
   
   /**
   * Returns the total number of comments for the document up until the version
   * specified (count of comments for current version and all its previous versions). 
   *
   * @param fsDocCd Document code
   * @param fsDocDeptCd Document department code
   * @param fsDocId Document ID
   * @param fiCurrentDocVersNo Current document version number. Comments for this 
   * document until this specified version will be included in the count to be returned.
   *
   * @return int Number of comments for this document until the specified version.
   */
   private int getCmntCntInclPrevVers( String fsDocCd, String fsDocDeptCd, 
                                          String fsDocId, int fiCurrentDocVersNo)
   {
      //Check for number of comments for this document prior deletion
      SearchRequest lsrRequest = new SearchRequest();
      
      StringBuffer         loWhereClause = new StringBuffer();
      
      //Add an additional where clause to retrieve all doc hdr with version prior
      loWhereClause.append(ATTR_DOC_CD);
      loWhereClause.append(" = '");
      loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsDocCd));
      loWhereClause.append("' AND ");
      loWhereClause.append(ATTR_DOC_DEPT_CD);
      loWhereClause.append(" = '");
      loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsDocDeptCd));
      loWhereClause.append("' AND ");
      loWhereClause.append(ATTR_DOC_ID);
      loWhereClause.append(" = '");
      loWhereClause.append(AMSSQLUtil.getANSIQuotedStr(fsDocId));
      loWhereClause.append("' AND ");
      loWhereClause.append(ATTR_DOC_VERS_NO);
      loWhereClause.append(" <= ");
      loWhereClause.append(fiCurrentDocVersNo);
      
      lsrRequest.add(loWhereClause.toString());
        
      return getObjectCount(lsrRequest, getSession());
   } /* end getCmntCntInclPrevVers() */
}
