//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	import java.util.Vector;
	import java.util.ArrayList;
	import java.util.Enumeration;
	import java.util.Hashtable;
	import java.util.Iterator;
	import com.amsinc.gems.adv.common.*;
	import com.amsinc.gems.adv.vfc.html.*;
	import advantage.AMSStringUtil;
	
	/*
	 **  pCopyWorkflowRoles
	 */
	
	//{{FORM_CLASS_DECL
	public class pCopyWorkflowRoles extends pCopyWorkflowRolesBase
	
	//END_FORM_CLASS_DECL}}
	{
	   
	   private int                miCurStep          = 1 ;
	   private boolean            mboolFirstGenerate = true ;
	   private TextContentElement moStepTitle        = null ;
	   private DivElement         moStep1Cont        = null ;
	   private DivElement         moStep2Cont        = null ;
	   private DivElement         moStep3Cont        = null ;
	   private TextFieldElement   moSourceUserID     = null ;
	   private TextFieldElement   moTargetUserID     = null ;
	   
	   /*
	   * The Vector to store all source workflow roles selected in the first page
	   * of the copy workflow  roles
	   */
	   private Vector mvPreviouslySelected           = new Vector(32);
	   
	   /*
	   * Hashtable to store source user workflow roles that needs to be copied
	   * to target user
	   */
	   private Hashtable mhSelectedRows             = new Hashtable(32);
	   
	   /* String to store the action (Append or Replace ) selected by user.
	   * msCopyAction = 0 implies 'Append to the Existing Role'
	   * msCopyAction = 1 implies 'Replace the Existing Role'
	   */
	   private String msCopyAction                   = "";
	   private boolean mboolfirst = true;
	   
	   // Variable to track previous action selected by user.
	   private String msPrevCopyAction = "";
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pCopyWorkflowRoles ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }
	   
	   
	//{{EVENT_CODE
	//{{EVENT_pCopyWorkflowRoles_beforeGenerate
   /**
   * This method is invoked before page is generated.	 
   */
   void pCopyWorkflowRoles_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
   {
      //Write Event Code below this line
      RadioButtonElement loRadioElem = null;
      RadioButtonGroup  loRadioGroup = (RadioButtonGroup) getElementByName("CopyAction");
      String lsValueProperty = "";
      
      Enumeration loEnum = loRadioGroup.getElements();
      /* 
      * Loop through the existing Radio Buttons and re-select the radio button that
      * was previously selected by the User.
      */
      while(loEnum.hasMoreElements())
      {
         loRadioElem = (AMSRadioButtonElement) loEnum.nextElement();
         lsValueProperty = loRadioElem.getValueProperty();
         
         if(AMSStringUtil.strEqual(msPrevCopyAction, lsValueProperty))
         {
            loRadioElem.setChecked(true);
         }
      }			
   }
   
   
//END_EVENT_pCopyWorkflowRoles_beforeGenerate}}
//{{EVENT_pCopyWorkflowRoles_beforeActionPerformed
   void pCopyWorkflowRoles_beforeActionPerformed( ActionElement foActnElem, PageEvent foPgEvt, PLSRequest foPLSReq )
   {
      //Write Event Code below this line
      String lsActnNm     = foActnElem.getName() ;
      String lsCopyAction = null ;
      TableElement loTbl  = null;
      
      
      if ( lsActnNm.equalsIgnoreCase( "next" ) )
      {
         loTbl = (TableElement)getElementByName("tblT2R_USER_WF_ROLES_QRY") ;
         if(loTbl != null)
         {
            Enumeration loEnum = loTbl.getSelectedRows() ;
            while ( loEnum.hasMoreElements() )
            {
               Integer loEnumIndex = ( Integer ) loEnum.nextElement() ;
               if(!mvPreviouslySelected.contains(loEnumIndex))
               {
                  mvPreviouslySelected.add(loEnumIndex) ;
               }
            } /* end while(loEnum.hasMoreElements()) */
            
         } /* end if(loTbl!= null) */
         
         foPgEvt.setCancel( true ) ;
         foPgEvt.setNewPage( this ) ;
         /*
         * For the 1st page,  miCurStep = 1 ( Selection of the Source User)
         * For the 2nd page,  miCurStep = 2 ( Selection of the Target User)
         * For the last page, miCurStep = 3.( Selection of the copy mode)
         * validateNext() has to be called only in first 2 pages,
         * validateNext() should not be called in the last page of Copy Workflow
         * Roles
         */
         if ( miCurStep < 3 )
         {
            if ( validateNext( foPLSReq ) )
            {
               
               miCurStep++ ;
            }
         } /* end if ( miCurStep < 3 ) */
      } /* end if ( lsActnNm.equalsIgnoreCase( "next" ) ) */
      
      else if ( lsActnNm.equalsIgnoreCase( "back" ) )
      {
         
         AMSTableElement loElement =
            ( AMSTableElement )getElementByName("tblT2R_USER_WF_ROLES_QRY") ;
         loElement.setSelectedRows(mvPreviouslySelected) ;
         
         if ( miCurStep > 1 )
         {
            
            miCurStep-- ;
         }
         // Store the Copy action selected by User
         msPrevCopyAction = foPLSReq.getParameter( "hdnCopyMode" );
         
         foPgEvt.setCancel( true ) ;
         foPgEvt.setNewPage( this ) ;
      } /* end else if ( lsActnNm.equalsIgnoreCase( "back" ) ) */
      
      /*
      * hdnCopyMode is hidden value set in the Copy Workflow Roles page.
      * hdnCopyMode = 0 when Radio button 'Append to the Existing Role'
      * is selected.
      * hdnCopyMode = 1 when Radio button 'Replace  to the Existing Role'
      * is selected.
      */
      
      else if ( lsActnNm.equalsIgnoreCase( "copyWFRoles" ) )
      {
         msCopyAction = foPLSReq.getParameter( "hdnCopyMode" ) ;
         
         try
         {
            copyRoles();
            refreshDataSource(T4R_USER_WF_ROLES_QRY);
         }
         catch(Exception loExcep)
         {
            raiseException(loExcep.getMessage(),AMSPage.SEVERITY_LEVEL_ERROR);
         }
         
         
         
      } /* end else if ( lsActnNm.equalsIgnoreCase( "copyWFRoles" ) ) */
   }
   
   
//END_EVENT_pCopyWorkflowRoles_beforeActionPerformed}}
//{{EVENT_T3R_SC_USER_INFO_beforeQuery
   void T3R_SC_USER_INFO_beforeQuery(VSQuery query ,VSOutParam resultset )
   {
      //Write Event Code below this line
      //Write Event Code below this line
      /** If this record source query is getting fired the first time
      i.e the next link is clicked the first time then
      append an additional where clause to display a blank user
      there by allowing the user to select it.**/
      if(mboolfirst)
      {
         mboolfirst = false;
         SearchRequest lsr = new SearchRequest();
         
         lsr.add("USER_ID IS NULL");
         query.addFilter(lsr);
      }
      
      
   }
   
   
//END_EVENT_T3R_SC_USER_INFO_beforeQuery}}

	//END_EVENT_CODE}}
	   
	   public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
	addPageListener(this);
	T3R_SC_USER_INFO.addDBListener(this);
		//END_EVENT_ADD_LISTENERS}}
	   }
	   
	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pCopyWorkflowRoles_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pCopyWorkflowRoles_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T3R_SC_USER_INFO) {
			T3R_SC_USER_INFO_beforeQuery(query , resultset );
		}
	}
	//END_EVENT_ADAPTER_CODE}}
	   
	   public HTMLDocumentSpec getDocumentSpecification() {
	      return getDefaultDocumentSpecification();
	   }
	   
	   public String getFileName() {
	      return getDefaultFileName();
	   }
	   public String getFileLocation() {
	      return getPageTemplatePath();
	   }
	   
	   public void afterPageInitialize() {
	      super.afterPageInitialize();
	      //Write code here for initializing your own control
	      //or creating new control.
	      
	   }
	   
	   
	   /*
	   * This method copies the work flow roles of source user to target user
	   * @return none.
	   */
	   public void copyRoles()
	   {
	      VSRow lvrSrcRow            = null;
	      VSRow lvrTgtRow            = null;
	      VSResultSet  loTgtWFRoleRS = null ;
	      String lsSrcUserID         = "" ;
	      String lsTgtUserID         = "" ;
	      boolean  lboolBeganTrans   = false ;
	      VSSession  loSession       = getParentApp().getSession() ;
	      lvrSrcRow                  = T1R_SC_USER_INFO.getCurrentRow() ;
	      
	      if ( lvrSrcRow == null )
	      {
	         raiseException( "An existing source user must be selected.",
	            AMSPage.SEVERITY_LEVEL_ERROR ) ;
	         return ;
	      }
	      lvrTgtRow = T3R_SC_USER_INFO.getCurrentRow() ;
	      if ( lvrTgtRow == null )
	      {
	         raiseException( "An existing target user must be selected.",
	            AMSPage.SEVERITY_LEVEL_ERROR ) ;
	         return ;
	      }
	      lsSrcUserID = lvrSrcRow.getData( "USER_ID" ).getString();
	      lsTgtUserID = lvrTgtRow .getData( "USER_ID" ).getString();
	      if ( lsSrcUserID == null || lsTgtUserID == null )
	      {
	         raiseException( "A source or target user must be selected.",
	            AMSPage.SEVERITY_LEVEL_ERROR ) ;
	         return ;
	      }
	      else if( lsSrcUserID.equals( lsTgtUserID ) )
	      {
	         raiseException( "Source and target cannot be the same user.",
	            AMSPage.SEVERITY_LEVEL_ERROR ) ;
	         return ;
	      }
	      try
	      {
	         if ( !loSession.isTransactionInProgress() )
	         {
	            lboolBeganTrans = true ;
	            loSession.beginTrans() ;
	         } /* end if ( !loSession.isTransactionInProgress() ) */
	         
	         /* msCopyAction = 1 implies 'Replace to the Existing Role'. */
	         if(AMSStringUtil.strEqual(msCopyAction,"1"))
	         {
	            /*
	            * First remove exisintg roles on the target if specified
	            */
	            removeExistingRoles( lsTgtUserID ) ;
	            loTgtWFRoleRS = getNewResultSet( "R_WF_USER_ROLE") ;
	            
	         }
	         else
	         {
	            
	            loTgtWFRoleRS=getResultSet( "R_WF_USER_ROLE", "USID="+AMSSQLUtil.getANSIQuotedStr(lsTgtUserID,true) ) ;
	         }
	         
	         /*
	         * Now copy the source user's workflow role information to
	         * the target user
	         */
	         copyWorkflowRoles( lsSrcUserID, lsTgtUserID, loTgtWFRoleRS  ) ;
	         /*
	         * Finally save the target user's workflow role information
	         */
	         loTgtWFRoleRS.updateDataSource() ;
	         
	         
	         miCurStep = 1 ;
	         if ( ( lboolBeganTrans ) &&
	            ( loSession.isTransactionInProgress() ) )
	         {
	            
	            loSession.commit() ;
	            
	         }
	         
	         raiseException( " Workflow roles successfully copied from \"" +
	            lsSrcUserID + "\" to \"" + lsTgtUserID + "\"." ,
	            AMSPage.SEVERITY_LEVEL_INFO ) ;
	         
	         
	      }//end of Outer try
	      catch(Exception  loExcep)
	      {
	         
	         
	         loSession.rollback() ;
	         raiseException( "Unable to copy roles from \"" +
	            lsSrcUserID + "\" to \"" + lsTgtUserID + "\"." ,
	            AMSPage.SEVERITY_LEVEL_INFO ) ;
	         
	      }
	      finally
	      {
	         if (loTgtWFRoleRS != null)
	         {
	            loTgtWFRoleRS.close();
	         }
	      }//end of Finally
	   } /* end of copyRoles() */
	   
	   /*
	   * Clones the Original Row to New Row.
	   * @param foOrigRow Original Row.
	   * @param foNewRow  New Row, which is the clone of Original Row.
	   * @return none.
	   */
	   private void cloneRow( VSRow foOrigRow, VSRow foNewRow )
	   {
	      int liColumnCount = foNewRow.getColumnCount() ;
	      for ( int liCount = 1 ; liCount <= liColumnCount ; liCount++ )
	      {
	         
	         /* To avoid the copying the Manager Flag , if the source
	         * user's Workflow contains the Manager Flag = 1.
	         *
	         */
	         
	         if(foOrigRow.getMetaColumn(liCount).getCaption().equals("Manager"))
	         {
	            
	            continue;
	            
	         }
	         if(foOrigRow.getData( liCount  ) != null)
	         {
	            foNewRow.getData( liCount ).setData( foOrigRow.getData( liCount ) ) ;
	         }
	      }
	   } /* end cloneRow() */
	   
	   /* Removes the Existing Role of the targent user.
	   * @param fsTgtUserID  The target user Id.
	   * @return none.
	   */
	   private void removeExistingRoles( String fsTgtUserID )
	   {
	      VSResultSet loVSResultSet  = null ;
	      VSRow       loOrigRow      = null ;
	      int         liRowCount     = 0 ;
	      
	      loVSResultSet = getResultSet( "R_WF_USER_ROLE", "USID=" +AMSSQLUtil.getANSIQuotedStr(fsTgtUserID,true) ) ;
	      
	      if ( loVSResultSet == null )
	      {
	         return ;
	      }
	      
	      loOrigRow    = loVSResultSet.last() ;
	      liRowCount   = loVSResultSet.getRowCount() ;
	      if ( ( liRowCount < 1 ) || ( loOrigRow == null ) )
	      {
	         loVSResultSet.close() ;
	         return ;
	      } /* end if ( ( liRowCount != 1 ) || ( loOrigRow == null ) ) */
	      
	      loOrigRow = loVSResultSet.first() ;
	      
	      while ( loOrigRow != null )
	      {
	         loVSResultSet.delete() ;
	         loOrigRow = loVSResultSet.current() ;
	      }
	      
	      /*
	      * Delete the target user's security role information
	      */
	      try
	      {
	         loVSResultSet.updateDataSource() ;
	      } /* end try */
	      catch( VSException loException )
	      {
	         raiseException( "Could not remove existing security roles from "+
	            "target user.", AMSPage.SEVERITY_LEVEL_ERROR ) ;
	         
	         loVSResultSet.close() ;
	         throw loException ;
	      } /* end catch( VSException loException ) */
	      
	      loVSResultSet.close() ;
	      
	      return ;
	   } /* end removeExistingRoles() */
	   
	   /*
	   * This method assigns the workflow roles of source user to the target user.
	   * @param fsSrcUserID  Source User Id
	   * @param fsTgtUserID  Targent User Id
	   * @param foTgtWFRoleRS	VSResultSet of the Target User.
	   * @return none.
	   */
	   private void copyWorkflowRoles(String fsSrcUserID,
	      String fsTgtUserID, VSResultSet foTgtWFRoleRS )
	   {
	      VSResultSet loVSResultSet  = null ;
	      VSRow       loOrigRow      = null ;
	      VSRow       loNewRow       = null ;
	      int         liRowCount     = 0 ;
	      int liIndex                = 0;
	      Object loKey               = null;
	      VSRow loRow                = null;
	      int        liSize				= 0;
	      
	      if ( foTgtWFRoleRS == null )
	      {
	         return ;
	      } /* end if ( foTgtWFRoleRS == null ) */
	      
	      
	      
	      loVSResultSet = getResultSet( "R_WF_USER_ROLE" , "USID=" + AMSSQLUtil.getANSIQuotedStr(fsSrcUserID,true) ) ;
	      if ( loVSResultSet == null )
	      {
	         return ;
	      } /* end  if ( loResultSet == null ) */
	      loOrigRow    = loVSResultSet.last() ;
	      liRowCount   = loVSResultSet.getRowCount() ;
	      if ( ( liRowCount < 1 ) || ( loOrigRow == null ) )
	      {
	         loVSResultSet.close() ;
	         return ;
	      } /* end if ( ( liRowCount != 1 ) || ( loOrigRow == null ) ) */
	      
	      
	      /*
	      * The logic here is to populate the hashtable with the workflow roles
	      * of the source user that needs to be copied to target user.
	      * Initially using the for loop all selected source user workflow roles
	      * are stored in the Hashtable.
	      * If the Copy action = Append , then we check if any source user workflow
	      * role already exists in target result set foTgtWFRoleRS
	      * using findFirst() method. If the workflow role exists , then it is removed
	      * from the hashtable. So the Hashtable finally contains only those roles
	      * which needs to be copied to target user.
	      */
	      
	      
	      liSize = mvPreviouslySelected.size();
	      for(int liCount = 0 ; liCount < liSize ; liCount++)
	      {
	         Integer loEnumIndex = (Integer) mvPreviouslySelected.get(liCount);
	         mhSelectedRows.put( loEnumIndex ,
	            loVSResultSet.getRowAt(loEnumIndex.intValue())) ;
	         
	      }
	      /* msCopyAction = 0 implies Append to the Existing Role. */
	      if(AMSStringUtil.strEqual(msCopyAction,"0"))
	      {
	         Enumeration loKeysEnum = mhSelectedRows.keys();
	         while (loKeysEnum.hasMoreElements())
	         {
	            loKey = loKeysEnum.nextElement() ;
	            loRow = (VSRow)mhSelectedRows.get(loKey );
	            liIndex = foTgtWFRoleRS .findFirst("ROLEID LIKE "+AMSSQLUtil.getANSIQuotedStr(loRow.getData("ROLEID").getString(), true));
	            if(liIndex > 0)
	            {
	               mhSelectedRows.remove(loKey);
	            }
	         } /* end  while (loKeysEnum.hasMoreElements()) */
	      } /* end if(AMSStringUtil.strEqual(msCopyAction,"0")) */
	      Iterator loIterator =  mhSelectedRows.values().iterator();
	      while (loIterator.hasNext())
	      {
	         loOrigRow = (VSRow)loIterator.next() ;
	         loNewRow = foTgtWFRoleRS.insert() ;
	         if ( loNewRow == null )
	         {
	            return ;
	         } /* end if ( loNewRow == null ) */
	         
	         cloneRow( loOrigRow, loNewRow ) ;
	         VSData loData = loNewRow.getData("USID");
	         if (loData!= null )
	         {
	            loData.setString( fsTgtUserID ) ;
	         }
	         
	         
	      }//end while (loIterator.hasNext())
	      
	      
	      return ;
	   } /* end copyWorkflowRoles */
	   
	   
	   /* Returns the ResultSet.
	   * @param fsQueryName DataObject Name
	   * @param fsWhereClause Condition clause
	   * @return VSResultSet
	   */
	   private VSResultSet getResultSet( String fsQueryName,
	      String fsWhereClause )
	   {
	      VSQuery loQuery;
	      loQuery = new VSQuery( getParentApp().getSession(), fsQueryName,
	         fsWhereClause, "" ) ;
	      
	      return loQuery.execute() ;
	   } /* end getResultSet() */
	   
	   
	   /*
	   * Returns the ResultSet.
	   * @param fsQueryName DataObject Name
	   * @return VSResultSet
	   */
	   private VSResultSet getNewResultSet( String fsQueryName )
	   {
	      VSQuery loQuery;
	      loQuery = new VSQuery( getParentApp().getSession(), fsQueryName, "", "" );
	      return loQuery.getNewResultSet() ;
	   } /* end getNewResultSet() */
	   
	   
	   
	   /*
	   *	This method is invoked before generation of a page.
	   *	It enables/disables the back,next,copyWorkflowRoles buttons
	   *	depending upon the page being viewed.
	   @ return none.
	   */
	   public void beforeGenerate()
	   {
	      VSRow   loSourceRow = null ;
	      VSRow   loTargetRow = null ;
	      if ( mboolFirstGenerate )
	      {
	         mboolFirstGenerate = false ;
	         moStepTitle = (TextContentElement)getElementByName( "CurrStepTitle" ) ;
	         moStep1Cont = (DivElement)getElementByName( "Step1Cont" ) ;
	         moStep2Cont = (DivElement)getElementByName( "Step2Cont" ) ;
	         moStep3Cont = (DivElement)getElementByName( "Step3Cont" ) ;
	         moSourceUserID  = (TextFieldElement)getElementByName( "fromUserID" ) ;
	         moTargetUserID  = (TextFieldElement)getElementByName( "toUserID" ) ;
	      } /* end if ( mboolFirstGenerate ) */
	      switch( miCurStep )
	      {
	      case 1 :
	         
	         moStepTitle.setValue( "Step 1: Select a user to copy selected roles"+
	            " from..." ) ;
	         moStep1Cont.setVisible( true ) ;
	         moStep2Cont.setVisible( false ) ;
	         moStep3Cont.setVisible( false ) ;
	         break ;
	      case 2 :
	         
	         moStepTitle.setValue( "Step 2: Select a user to copy "+
	            " selected roles into" ) ;
	         moStep1Cont.setVisible( false ) ;
	         moStep2Cont.setVisible( true ) ;
	         moStep3Cont.setVisible( false ) ;
	         break ;
	      case 3 :
	         
	         loSourceRow = T1R_SC_USER_INFO.getCurrentRow() ;
	         loTargetRow = T3R_SC_USER_INFO.getCurrentRow() ;
	         moStepTitle.setValue( "Step 3: Confirm Copy User Roles" ) ;
	         moStep1Cont.setVisible( false ) ;
	         moStep2Cont.setVisible( false ) ;
	         moStep3Cont.setVisible( true ) ;
	         moSourceUserID.setValue(loSourceRow.getData("USER_ID").getString());
	         moTargetUserID.setValue(loTargetRow.getData("USER_ID").getString());
	         break ;
	      default :
	         
	         moStepTitle.setValue( "" ) ;
	         moStep1Cont.setVisible( false ) ;
	         moStep2Cont.setVisible( false ) ;
	         moStep3Cont.setVisible( false ) ;
	         break ;
	      } /* end switch( miCurStep ) */
	   } /* end beforeGenerate() */
	   
	   /*
	   * This method checks whether the NEXT action invoked is
	   * valid one or not.
	   * Returns True if the NEXT action is valid else False is returned.
	   * @param  foPLSReq The PLS Request object
	   * @return boolean
	   */
	   private boolean validateNext( PLSRequest foPLSReq )
	   {
	      VSRow   loSourceRow     ;
	      VSRow   loSourceRoleRow ;
	      VSRow   loTargetRow     ;
	      String  lsSourceUserID  ;
	      String  lsTargetUserID  ;
	      boolean lboolRC = true  ;
	      
	      switch( miCurStep )
	      {
	      case 1 :
	         loSourceRow = T1R_SC_USER_INFO.getCurrentRow() ;
	         if ( loSourceRow == null )
	         {
	            raiseException( "An existing source user must be selected.",
	               SEVERITY_LEVEL_ERROR ) ;
	            lboolRC = false ;
	         } /* end if ( loSourceRow == null ) */
	         if ( lboolRC  )
	         {
	            loSourceRoleRow = T2R_USER_WF_ROLES_QRY.getCurrentRow() ;
	            if ( loSourceRoleRow == null )
	            {
	               raiseException( "Selected source user does not have any "+
	                  "Workflow role.",
	                  SEVERITY_LEVEL_ERROR ) ;
	               lboolRC = false ;
	               
	            }
	         }
	         if ( lboolRC )
	         {
	            if(mvPreviouslySelected.size() == 0)
	            {
	               raiseException("Select Workflow roles of source user "+
	                  "to be copied.",
	                  AMSPage.SEVERITY_LEVEL_ERROR );
	               lboolRC = false ;
	               
	            }
	         }
	         break ;
	      case 2 :
	         loSourceRow = T1R_SC_USER_INFO.getCurrentRow() ;
	         loTargetRow = T3R_SC_USER_INFO.getCurrentRow() ;
	         if ( loTargetRow == null )
	         {
	            raiseException( "An existing target user must be selected.",
	               SEVERITY_LEVEL_ERROR ) ;
	            lboolRC = false ;
	         } /* end if ( loTargetRow == null ) */
	         else
	         {
	            lsSourceUserID = loSourceRow.getData( "USER_ID" ).getString() ;
	            lsTargetUserID = loTargetRow.getData( "USER_ID" ).getString() ;
	            
	            if ( lsSourceUserID.equals( lsTargetUserID ) )
	            {
	               raiseException( "Source and target cannot be the same user.",
	                  AMSPage.SEVERITY_LEVEL_ERROR ) ;
	               lboolRC = false ;
	            } /* end if ( lsSourceUserID.equals( lsTargetUserID ) ) */
	         } /* end else */
	         break ;
	      default :
	         raiseException( "Next action is not permitted from this step",
	            SEVERITY_LEVEL_ERROR ) ;
	         return false ;
	      } /* end switch( miCurStep ) */
	      return lboolRC ;
	   } /* end validateNext() */
	   
	   
	   
	}
	