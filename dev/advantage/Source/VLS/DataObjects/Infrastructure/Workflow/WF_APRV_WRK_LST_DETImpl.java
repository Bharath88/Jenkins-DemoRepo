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

import java.util.ArrayList;
import versata.vfc.VSData;
import versata.vfc.VSQuery;
import versata.vfc.VSResultSet;
import versata.vfc.VSRow;

/*
**  WF_APRV_WRK_LST_DET
*/

//{{COMPONENT_RULES_CLASS_DECL
public class WF_APRV_WRK_LST_DETImpl extends  WF_APRV_WRK_LST_DETBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//{{COMP_CLASS_CTOR
	public WF_APRV_WRK_LST_DETImpl (){
		super();
	}
	
	public WF_APRV_WRK_LST_DETImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
//{{COMP_EVENT_beforeUpdate

   public void beforeUpdate(DataObject obj, Response response)
   {
      SearchRequest loSearchReq = null;
      WF_APRV_WRK_LSTImpl loParent = null;

      if(AMSStringUtil.strEqual( (getSession().getProperty( "WRK_LST_DET_ACTN" )), "true" ) )
      {
         loSearchReq = new SearchRequest();
         loSearchReq.addParameter("WF_APRV_WRK_LST", "WRK_LST_ID", getWRK_LST_ID()+"");

         loParent =
            (WF_APRV_WRK_LSTImpl)WF_APRV_WRK_LSTImpl.getObjectByKey( loSearchReq,
            getSession() );

         /* Before updating anything on this DO, we need to update the same on parent
          * WF_APRV_WRK_LST first since the child WF_APRV_WRK_LST_DET has parent
          * replicate fields which will be replicated from parent with old/existing values
          * while any update operation to child, otherwise no updates will happen when updated
          * from worklist details page.
          * Note: In future if any new persistent columns which are parent replicate should
          * be updated first in parent as below if these values are updatable through
          * Worklist details page. */
         if( loParent != null )
         {
            loParent.setWRK_LST_TYP( this.getWRK_LST_TYP() );
            loParent.setASSIGNEE( this.getASSIGNEE() );
            loParent.setASSIGNEE_FL( this.getASSIGNEE_FL() );
            loParent.setAPRV_LVL( this.getAPRV_LVL() );
            loParent.setFWD_USID( this.getFWD_USID() );
            loParent.setLOCK_USID( this.getLOCK_USID() );

            loParent.save();
         }
      }
   }
//END_COMP_EVENT_beforeUpdate}}Bharath kuar

	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		Bharath Kumar
	addRuleEventListener(this);
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static WF_APRV_WRK_LST_DETImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new WF_APRV_WRK_LST_DETImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
	
	   public void myInitializations()
	   {
	      setSaveBehaviorMode( AMSSAVEBEHAVIOR_DATAOBJECT_SUBMIT_SAVE ) ;
	      mboolCustomDataObjectSave = true ;

	      //Set Prefix for Organizational fields that would be used for Organizational security
	      setSecurityOrgPrefixes( "DOC" );

	   }
	   

	   /**
	    * Invokes super.save() if parameter is true or this.save() if parameter is false.  
	    * This allows the real updates occurring in this class to bypass this.save() which
	    * is used for document processing.
	    *
	    * @param fboolSuper true to go to super; false to go to this
	    * @throws ServerException
	    *
	    */
	   public void save(boolean fboolSuper) throws ServerException
	   {
	      if (fboolSuper)
	      {
	         if ( AMSCommonConstants.VLS_CASE_LOGIC_ENABLED ||
	               AMSCommonConstants.VLS_TRIM_SPACE_ENABLED)
	         {
	            applyCaseAndTrimLogicToDataObject(
	                  getMixedCaseTextAttrsList(),
	                  getUpperCaseTextAttrsList() ) ;
	         }
	         mboolCustomDataObjectSave = false ;
	         super.save();
	         mboolCustomDataObjectSave = true ;
	      }
	      else
	      {
	         this.save();
	      }
	   }

	   /**
       * Inserts a new record into WorkList Details table and common fields are copied from
       * passed in WorkList record to newly inserted WorkList details record. Then based on the
       * passed in Worklist Details Configuration record, the fields data is copied from
       * the associated Document to the Worklist Details record.
       *  This method will be typically called from WorkList Details Synchronization batch process
       *  where WorkList Details Configuration record is passed in.
       *  @param foSession Session object on which the operation is performed.
       *  @param foWorkListRec WorkList Record to insert the WorkList Details record for
       *  @param foConfigRowToProcess WorklList Details Configuration record to update the Custom Fields.
       *  @see R_WF_DET_CONFImpl#isInsertConfig
       */
      public static void insertWorklistDetails( Session foSession, WF_APRV_WRK_LSTImpl foWorkListRec,
            R_WF_DET_CNFGImpl foConfigRowToProcess  ) throws Exception
      {
         // Add a new record into WorkList Details table.
         WF_APRV_WRK_LST_DETImpl loWorkLisDetRec = ( WF_APRV_WRK_LST_DETImpl )addWorklistDetailsRec
                                                         ( foSession, foWorkListRec);
         //Set the Custom fields value as per the WorkList Details Configuration record after fetching
         //the data from the associated Document
         loWorkLisDetRec.setCustomFields( foConfigRowToProcess );
         loWorkLisDetRec.save(true);
      }


      /**
       *  Inserts a new record into WorkList Details table when a corresponding record is inserted
       *  into WorkList table. Gets the Non-Dirty WorklList Details Configuration records for the
       *  Document Code of the WorkList record and sets the corresponding Custom Fields value into the
       *  new inserted WorkList Details record.
       *  This method will be typically called from online WorkFlow processing
       *  where inserting new WorkList item triggers insertion of WorkList Details entry as well.
       *  If the insertion fails due to any reason then an exception is logged.
       *
       *  @param foSession Session object on which the operation is performed.
       *  @param foWorkListRec WorkList Record to insert the WorkList Details record for
       */
      public static void insertWorklistDetails( Session foSession, WF_APRV_WRK_LSTImpl foWorkListRec )
      {
         WF_APRV_WRK_LST_DETImpl loWorkLisDetRec = null;
         try
         {
            // Add a new record into WorkList Details table.
            loWorkLisDetRec = (WF_APRV_WRK_LST_DETImpl)addWorklistDetailsRec(foSession, foWorkListRec);

            Enumeration loNonDirtyConfigRows;
            R_WF_DET_CNFGImpl loNonDirtyConfigRow;

            // Get the Non Dirty WorklList Details Configuration records for the Document Code of the WorkList record.
            loNonDirtyConfigRows = R_WF_DET_CNFGImpl.getNonDirtyConfigRecs(foSession, foWorkListRec.getDOC_CD());

            // If Non Dirty WorklList Details Configuration records are found then only
            // add the WordList Details record.
            if(loNonDirtyConfigRows.hasMoreElements())
            {
               //Loop through each WorklList Details Configuration record
               while( loNonDirtyConfigRows.hasMoreElements() )
               {
                  loNonDirtyConfigRow = ( R_WF_DET_CNFGImpl ) loNonDirtyConfigRows.nextElement();
                  // Set the custom fields value as per the Non-Dirty WorkList Details Configuration record
                  loWorkLisDetRec.setCustomFields( loNonDirtyConfigRow );
               }

               // Save the WorkList Details records when custom fields updated for all
               // Non-Dirty WorkList Details Configuration records.
               loWorkLisDetRec.save(true);
            }

         }
         catch (Exception loEx)
         {
            loEx.printStackTrace () ;
            loWorkLisDetRec.raiseException("Error inserting record into WorkList Details Table.");
         }
      }

      /**
       * Adds a new record into WorkList Details table and copies the corresponding  data for
       * its common fields (fields common with WorkList table) from the given WorkList record.
       * @param foSession Session object on which the operation is performed.
       * @param foWorkListRec WorkList Record to copy the corresponding data from
       * @return
       */
      public static AMSDataObject addWorklistDetailsRec( Session foSession, WF_APRV_WRK_LSTImpl foWorkListRec)
      {
         WF_APRV_WRK_LST_DETImpl loWorkLisDetRec = getNewObject( foSession, true );

         // Excluding 'LOCK_USID' here.
         Vector loExcludeList = new Vector(1);
         loExcludeList.add("LOCK_USID");

         loWorkLisDetRec.copyCorresponding( foWorkListRec, loExcludeList, true );
         return loWorkLisDetRec;
      }

      /**
       * Sets the Custom Fields values for the WorkList Details according to the WorkList Details
       * Configuration record passed as parameter. The Data for the Custom Fields is fetched from
       * the associated Document of this Worklist Details instance. The assumption here is that
       * the Worklist Details instance will have Document Identity fields populated with values
       * before this method is called.
       *
       * The data fetched from the Document is added to the Custom Fields.
       * If Data Type is Char or VarChar, use the column CUSTOM_STRING_X for storing data.
       * If Data Type is Date or TimeStamp, use the column CUSTOM_DATE_X for storing data.
       * If Data Type is BigInt, Integer, Decimal, Float, Double, Numeric or Real, use the column
       * CUSTOM_NUMBER_X for storing data.
       * Where X represent the RowNumber of the WorkList Details Configuration record.
       *
       * @param foConfigRowToProcess WorkList Details Configuration record
       */
      public void setCustomFields( R_WF_DET_CNFGImpl foConfigRowToProcess  ) throws Exception
      {
	     // Get the data for the WorkList/WorkList Details record as per the
         // WorkList Details Configuration passed as parameter.

         String lsDocDeptCd = getRow().getData("DOC_DEPT_CD").getString();
         String lsDocID = getRow().getData("DOC_ID").getString();
         int liDocVerNo = getRow().getData("DOC_VERS_NO").getint();


         //Get data from Document for the fields
         AMSDataObject loDocComp = getDocRecordData( getSession(), foConfigRowToProcess.getDOC_COMP(),
               foConfigRowToProcess.getDOC_CD(), lsDocDeptCd,lsDocID, liDocVerNo);

         // Get the Row Number of the WorkList Details Configuration record.
         int liRowNo = (int)foConfigRowToProcess.getROW_NO();

         // Set Custom Fields if Data is found for the WorkList/WorkList Details record.
         if( loDocComp!=null )
         {
            // Get the Data for the as per the Document Field name given
            // on WorkList Details Configuration record
            Data loData2 = loDocComp.getData( foConfigRowToProcess.getDOC_FLD() );
            
            // Switch on data type of the WorkList Details Configuration record.
            switch( foConfigRowToProcess.getDATA_TYP() )
            {
               case DataConst.CHAR:
               case DataConst.VARCHAR:
                  getData("CUSTOM_STRING_"+liRowNo).setData(loData2);
                  break;

               case DataConst.DATE:
               case DataConst.TIMESTAMP:
                  getData("CUSTOM_DATE_"+liRowNo).setData(loData2);
                  break;
                 
               case DataConst.BIGINT:
               case DataConst.INTEGER: 
                  getData("CUSTOM_NUMBER_"+liRowNo).setint( loData2.getint());
                  break;
                 
              case DataConst.DECIMAL:
              case DataConst.NUMERIC:
                  getData("CUSTOM_NUMBER_"+liRowNo).setBigDecimal( loData2.getBigDecimal() );
                  break;  
               
              case DataConst.FLOAT:
              case DataConst.DOUBLE:
              case DataConst.REAL:
                 getData("CUSTOM_NUMBER_"+liRowNo).setData( loData2 );
                 break;
                 
             case DataConst.BIT:
                  if( loData2.getValue()!=null)
                     {   
                        String lsBitValue = String.valueOf( loData2.getValue() );
                        getData("CUSTOM_NUMBER_"+liRowNo).setint( ( AMSStringUtil.strIsEmpty( lsBitValue  ) ) 
                                                               || ( lsBitValue.equals( "false"))? 0 : 1);
                     }
                  break;
               default: break;
            } //end switch

         }
      }

      /**
       * Gets Document Header/Component record based on the passed in lookup field values.
       * Returns null if the record is not found.
       * @param foSession Session object of the calling program
       * @param fsDocTableNm The Table name to fetch the data from
       * @param fsDocCd The Document Code to fetch the data for
       * @param fsDocDeptCd The Document Department Code to fetch the data for
       * @param fsDocID The specific Document ID to fetch the data for
       * @param fiDocVerNo The specific Document Version Number to fetch the data for
       * @return AMSDataObject containing the data.
       * @throws Exception
       */
      public static AMSDataObject getDocRecordData(Session foSession,String fsDocTableNm, String fsDocCode, String
            fsDocDeptCd, String fsDocID, int fiDocVerNo) throws Exception
      {
         SearchRequest lsrSearch = new SearchRequest();
         lsrSearch.addParameter(fsDocTableNm, "DOC_CD",fsDocCode);
         lsrSearch.addParameter(fsDocTableNm, "DOC_DEPT_CD", fsDocDeptCd);
         lsrSearch.addParameter(fsDocTableNm, "DOC_ID", fsDocID);
         lsrSearch.addParameter(fsDocTableNm, "DOC_VERS_NO", String.valueOf(fiDocVerNo) );

         /*
          * Fetch the Component Lines sorted of the basis on Primary key. Hence creating an
          * Order By based on it.
          * This is needed because in case there are > 1 lines, then the data of the 1st line
          * is to be used.
          */
         Vector loOrderBy = new Vector();
         VSMetaQuery loMetaQuery = AMSDataObject.getMetaQuery(fsDocTableNm, foSession);
         Enumeration lenumSort = loMetaQuery.getUniqueKeyColumns(fsDocTableNm);
         while(lenumSort.hasMoreElements())
         {
            VSQueryColumnDefinition loCol = (VSQueryColumnDefinition)lenumSort.nextElement();
            loOrderBy.add(loCol.getName());
         }

         Enumeration loRows = AMSDataObject.getObjectsSorted(fsDocTableNm, lsrSearch, foSession, loOrderBy, 0);

         // If more than 1 Component Lines are found, use the 1st one.
         if( loRows.hasMoreElements() )
         {
            return (AMSDataObject)loRows.nextElement();
         }
         else
         {
            //If record not found then return null.
            return null;
         }
     }


	   /**
	    * Performs Workflow actions by calling the parent's WF_APRV_WRK_LSTImpl method.
	    *
	    * This method is actually meant for overriding the no-arg save() method of DataObject.
	    *
	    * All Workflow actions on Worklist page are performed in the myDataObjectSave method of
	    * WF_APRV_WRK_LSTImpl.
	    *
	    * However, in case of Worklist Details page, the Workflow action performed will be directed
	    * to this DataObject Impl. This created 2 issues:
	    *    1. All logic for performing various Workflow actions resides in the parent DataObject
	    *       WF_APRV_WRK_LST. So that will have to be replicated here.
	    *    2. This DataObject is a child of the parent WF_APRV_WRK_LST. Updates should propagate
	    *       from Parent to Child and not vice versa.
	    *
	    * Hence, whenever a Worklist Action is performed the execution is redirected to the parent.
	    */
	   protected boolean myDataObjectSave()
	   //@SuppressAbstract
	   {
	      /*
	       * This adding of "temp" to the FWD_USID is an existing logic in
	       * AMSHyperlinkActionElement, used for triggering save on the DataObject
	       * whenever some Workflow action is performed.
	       *
	       * Hence checking for the string to determine if any Workflow action has happened
	       * and if it has than redirecting to the parent.
	       */
	      String lsFwd = getFWD_USID();
	      if( lsFwd != null && lsFwd.contains("temp"))
	      {
	         setFWD_USID(lsFwd.replace("temp", ""));
             if(getWfAprvWrkLstDetToWfAprvWrkLst() != null)
             {
                return getWfAprvWrkLstDetToWfAprvWrkLst().myDataObjectSave();
             }
	      }

	      return true;
	   }


	   /**
	    * Fetches the value of reason field using the reason code stored in the parent Worklist record.
	    *
	    * Reason field is stored as a CVL value on the main Worklist page.
	    * Hence if a parent replicate would have been done, only the CVL code value would be available.
	    *
	    * The Reason Description is shown as an Informational message whenever Document is opened
	    * from the Worklist page. Hence the descriptive value was needed and not the code.
	    *
	    * So this method fetches the Worklist Reason Code and then queries on the CVL table to get
	    * the description.
	    *
	    * @return String   Display value of a Reason Code.
	    */
	   public String getReasonDisplay()
	   {
	      if(getWfAprvWrkLstDetToWfAprvWrkLst()== null)
	      {
	         return "";
	      }

	      SearchRequest loSearch = new SearchRequest();
	      loSearch.addParameter("CVL_WF_REAS", "REAS_NO", String.valueOf(
	            getWfAprvWrkLstDetToWfAprvWrkLst().getREAS()));
	      CVL_WF_REASImpl loRow = (CVL_WF_REASImpl) CVL_WF_REASImpl.getObjectByKey(
	            loSearch, getSession());

	      return loRow!=null ? loRow.getREAS_DISP(): "";
	   }

      /**
       * Clears the Data present in Custom Fields for a Row Number (ROW_NO) of a delete
       * WorkList Details Configuration.
       * This Configuration is no longer needed on the WorkList Details Configuration table,
       * hence data corresponding to it should be removed from WorkList Details table.
       * This method will be typically called from WorkList Details Synchronization batch process
       * when a WorkList Details Configuration is to deleted.
       * @param fiRowNo Row Number of the WorkList Details Configuration to be deleted.
       * @see R_WF_DET_CONFImpl#isDeleteConfig
       */
      public void clearCustomFields( Integer fiRowNo )
      {
         // Clearing the values of all Custom Fields for the Row Number.
         setNull( "CUSTOM_STRING_"+ fiRowNo);
         setNull( "CUSTOM_DATE_"+ fiRowNo);
         setNull( "CUSTOM_NUMBER_"+fiRowNo);
      }

      /**
       * Return true if records exist for a given Document Code (DOC_CD) on
       * WorkList Details (WF_APRV_WRK_LST_DET) table. Uses <code>DataObject.getObjectCount</code>
       * to check the existence.
       *
       * @return true/false
       */
      public static boolean doesWorklistDetailsRecExist(Session foSession, String fsDocCode)
      {
         SearchRequest lsr = new SearchRequest();
         lsr.addParameter("WF_APRV_WRK_LST_DET", "DOC_CD",fsDocCode );
         return (( getObjectCount(lsr, foSession )>0)? true: false );
      }



}
