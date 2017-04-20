//{{IMPORT_STMTS
package advantage.SecurityAdmin;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
   import com.amsinc.gems.adv.vfc.html.*;
   import com.amsinc.gems.adv.common.*;
   import java.util.*;
   import advantage.AMSStringUtil;
   /*
   **  pCopyUserRolesPage*/

//{{FORM_CLASS_DECL
public class pCopyUserRolesPage extends pCopyUserRolesPageBase

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
       * The Vector to store all source user roles selected in the first page
       * of the copy user roles
       */
      private Vector mvPreviouslySelected           = new Vector(32);

      /* String to store the action (Append or Replace ) selected by user.
       * msCopyAction = 0 implies 'Append to the Existing Role'
       * msCopyAction = 1 implies 'Replace the Existing Role'
       */

      private String msCopyAction                   = ""; 

      /*
       * Hashtable to store source user roles that needs to be copied
       * to target user
       */
      private Hashtable mhSelectedRows             = new Hashtable(32);

      // Declarations for instance variables used in the form

      // This is the constructor for the generated form. This also constructs
      // all the controls on the form. Do not alter this code. To customize paint
      // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pCopyUserRolesPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      }



//{{EVENT_CODE
//{{EVENT_pCopyUserRolesPage_beforeActionPerformed
 /**
   * This method is invoked before any action invoked on the page.
   * @parm   foActnElem The ActionElement
   * @parm   foPgEvt    The PageEvent
   * @parm    foPLSReq The PLS Request Object.
   * @return none.
   */



   void pCopyUserRolesPage_beforeActionPerformed ( ActionElement foActnElem,
         PageEvent foPgEvt, PLSRequest foPLSReq ) {
   String lsActnNm = foActnElem.getName() ;

   if ( lsActnNm.equalsIgnoreCase( "next" ) )
   {
         TableElement loTbl =
         ( TableElement )getElementByName( "tblT2qR_SC_USER_ROLES_QRY" );
           if ( loTbl!= null )
         {
              Enumeration loEnum = loTbl.getSelectedRows();
            while ( loEnum.hasMoreElements() )
            {
               Integer loEnumIndex = (Integer) loEnum.nextElement();
               if(!mvPreviouslySelected.contains(loEnumIndex))
               {
                  mvPreviouslySelected.add(loEnumIndex);
               }
            } /* end while(loEnum.hasMoreElements()) */

         }

      if ( miCurStep < 3 )
      {
         if ( validateNext( foPLSReq ) )
         {
            miCurStep++ ;
         } /* end if ( validateNext( foPLSReq ) ) */
      } /* end if ( miCurStep < 3 ) */
      foPgEvt.setCancel( true ) ;
      foPgEvt.setNewPage( this ) ;
   } /* end if ( lsActnNm.equalsIgnoreCase( "next" ) ) */
   else if ( lsActnNm.equalsIgnoreCase( "back" ) )
   {
         AMSTableElement loElement = (AMSTableElement)
               getElementByName("tblT2qR_SC_USER_ROLES_QRY");
         loElement.setSelectedRows(mvPreviouslySelected);
      if ( miCurStep > 1 )
      {
         miCurStep-- ;
      } /* end if ( miCurStep > 1 ) */
      foPgEvt.setCancel( true ) ;
      foPgEvt.setNewPage( this ) ;
   } /* end else if ( lsActnNm.equalsIgnoreCase( "back" ) ) */
      /*
       * hdnCopyMode is hidden value set in the Copy User Roles page.
       * hdnCopyMode = 0 when Radio button 'Append to the Existing Role'
       * is selected.
       * hdnCopyMode = 1 when Radio button 'Replace  to the Existing Role'
       * is selected.
       */

   else if ( lsActnNm.equalsIgnoreCase( "copyUserRoles" ) )
   {
         msCopyAction = foPLSReq.getParameter("hdnCopyMode");
         try
      {
               copyUserRoles() ;
            }
            catch(Exception loException)
         {
         raiseException(loException.getMessage(),AMSPage.SEVERITY_LEVEL_INFO );


      }

         /* Refreshes the page after copying the source user role to
          * Target user role.
          * If we not refresh the target user page
          * it will not show the copied user role in current page.
          */
         refreshDataSource(T4qR_SC_USER_ROLES_QRY);
   } /* end else if ( lsActnNm.equalsIgnoreCase( "copyUserRoles" ) ) */
}
//END_EVENT_pCopyUserRolesPage_beforeActionPerformed}}
//{{EVENT_T3R_SC_USER_INFO_beforeQuery
void T3R_SC_USER_INFO_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   //Write Event Code below this line
   if(miCurStep == 1)
   {
      String lsDummyWhrClause = " AMS_ROW_VERS_NO = -1 ";
      /* Check if there is already an existing where clause */
      String lsOldWhrClause = query.getSQLWhereClause() ;
      if ( lsOldWhrClause != null && lsOldWhrClause.trim().length() > 0 )
      {
         lsDummyWhrClause = lsOldWhrClause + " AND " + lsDummyWhrClause ;
      }

      SearchRequest loSearchReq = new SearchRequest();
      loSearchReq.add(lsDummyWhrClause);
      query.addFilter(loSearchReq);
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

	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pCopyUserRolesPage_beforeActionPerformed( ae, evt, preq );
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
       * This method copies the user roles of source user to target user
       * @return none.
       */

      private void copyUserRoles()
      {
         VSRow       loSrcRow             = null ;
         VSRow       loTgtRow             = null ;
         VSResultSet loTgtUserRoleLnkRS   = null ;
         String      lsSrcUserID          = null ;
         String      lsTgtUserID          = null ;
         boolean     lboolBeganTrans      = false ;
         VSSession   loSession            = getParentApp().getSession() ;

         loSrcRow = T1R_SC_USER_INFO.getCurrentRow() ;

         if ( loSrcRow == null )
         {
            raiseException( "An existing source user must be selected.",
                            AMSPage.SEVERITY_LEVEL_ERROR ) ;
            return ;
         }

         loTgtRow = T3R_SC_USER_INFO.getCurrentRow() ;

         if ( loTgtRow == null )
         {
            raiseException( "An existing target user must be selected.",
                            AMSPage.SEVERITY_LEVEL_ERROR ) ;
            return ;
         }

         lsSrcUserID = loSrcRow.getData( "USER_ID" ).getString() ;


         lsTgtUserID = loTgtRow.getData( "USER_ID" ).getString() ;


         if ( lsSrcUserID == null || lsTgtUserID == null )
         {
            raiseException( "A source or target user must be selected.",
                            AMSPage.SEVERITY_LEVEL_ERROR ) ;
            return ;
         }
         else if ( lsSrcUserID.equals( lsTgtUserID ) )
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
            /* msCopyAction = 1 implies 'Replace the Existing Role'*/
            if(AMSStringUtil.strEqual(msCopyAction,"1"))
            {
            /*
             * First remove exisintg roles on the target if specified
             */
            removeExistingRoles( lsTgtUserID ) ;
               loTgtUserRoleLnkRS = getNewResultSet( "R_SC_USER_ROLE_LNK") ;
              }
              else
            {

               loTgtUserRoleLnkRS  = getResultSet( "R_SC_USER_ROLE_LNK",
                "USER_ID=" + AMSSQLUtil.getANSIQuotedStr(lsTgtUserID,true)) ;
            }
            /*
             * Now copy the source user's security role information to
             * the target user
             */
            copySecurityRoleLinks( lsSrcUserID, lsTgtUserID, loTgtUserRoleLnkRS ) ;

            /*
             * Finally save the target user's security role information
             */
            try
            {
               loTgtUserRoleLnkRS.updateDataSource() ;
            } /* end try */
            catch( VSException loException )
            {
               raiseException( "Could not copy security roles to target user.",
                               AMSPage.SEVERITY_LEVEL_ERROR ) ;

               loTgtUserRoleLnkRS.close() ;
               throw loException ;
            } /* end catch( VSException loException ) */

            loTgtUserRoleLnkRS.close() ;
            miCurStep = 1 ;
            if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) )
            {
               loSession.commit() ;
            } /* end if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) ) */
            raiseException( "Security roles sucessfully copied from \"" +
                            lsSrcUserID + "\" to \"" + lsTgtUserID + "\"." ,
                            AMSPage.SEVERITY_LEVEL_INFO ) ;
         } /* end try */
         catch( Exception loExcep )
         {
            raiseException( "Unable to copy roles from \"" +
                            lsSrcUserID + "\" to \"" + lsTgtUserID + "\"." ,
                            AMSPage.SEVERITY_LEVEL_INFO ) ;
         } /* end catch( Exception foExp ) */
         finally
         {
            if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) )
            {
               loSession.rollback() ;
            } /* end if ( ( lboolBeganTrans ) && ( loSession.isTransactionInProgress() ) ) */
      } /* end finally */

      }//end of copyUserRoles()

       /*
       * Clones the Original Row to New Row.
       * @param foOrigRow Original Row.
       * @param foNewRow  New Row, which is the clone of Original Row.
       * @return none.
       */
      private void cloneRow( VSRow foOrigRow, VSRow foNewRow )
      {
         int liColumnCount = foNewRow.getColumnCount() ;
         for ( int i = 1 ; i <= liColumnCount ; i++ )
         {
            foNewRow.getData( i ).setData( foOrigRow.getData( i ) ) ;
         }
      }//end cloneRow()

      /* Removes the Existing Role of the targent user.
       * @param fsTgtUserID  The target user Id.
       * @return none.
       */
      private void removeExistingRoles( String fsTgtUserID )
      {
         VSRow       loOrigRow      = null ;
         int         liRowCount     = 0 ;
         VSResultSet  loVSResultSet = getResultSet( "R_SC_USER_ROLE_LNK","USER_ID=" +AMSSQLUtil.getANSIQuotedStr(fsTgtUserID,true)) ;

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
            raiseException( "Could not remove existing security roles from target user.",
                            AMSPage.SEVERITY_LEVEL_ERROR ) ;

            loVSResultSet.close() ;
            throw loException ;
         } /* end catch( VSException loException ) */

         loVSResultSet.close() ;

         return ;
      }
      /*
       * This method assigns the roles of source user to the target user
       * @param fsSrcUserID Source User Id
       * @param fsTgtUserID Target User Id
       * @param loTgtUserRoleLnkRS  Resultset of target user
       * @return none.
       */

      private void copySecurityRoleLinks( String fsSrcUserID,
                                         String fsTgtUserID,
                                         VSResultSet foTgtUserRoleLnkRS )
      {
         VSResultSet loVSResultSet  = null ;
         VSRow       loOrigRow      = null ;
         VSRow       loNewRow       = null ;
         VSRow loRow                = null;
         int         liRowCount     = 0 ;
         int         liPrevSelCount = 0;
         int         liIndex        =  0;
         Object loKey               = null;
      int liLastPrecNo = 0;
      VSRow loLastRow = null;


         if ( foTgtUserRoleLnkRS == null )
         {
            return ;
         } /* end if ( foNewUserRoleLnkRS == null ) */


         loVSResultSet = getResultSet( "R_SC_USER_ROLE_LNK","USER_ID=" + AMSSQLUtil.getANSIQuotedStr(fsSrcUserID,true)  ) ;

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


         liPrevSelCount = mvPreviouslySelected.size();

         /*
          * The logic here is to populate the hashtable with the roles of
          * the source user that needs to be copied to target user.
          * Initially using the for loop all selected source user roles
          * are stored in the Hashtable.
          * If the Copy action = Append , then we check if any source user role
          * already exists in target result set foTgtUserRoleLnkRS
          * using findFirst() method. If the role exists , then it is removed
          * from the hashtable. So the Hashtable finally contains only those roles
          * which needs to be copied to target user.
          */

         for ( int liCtr = 0 ; liCtr < liPrevSelCount ; liCtr++)
         {
            Integer loEnumIndex = (Integer) mvPreviouslySelected.get(liCtr);
              mhSelectedRows.put( loEnumIndex ,
                    loVSResultSet.getRowAt(loEnumIndex.intValue()));
         }
         /* msCopyAction = 0 means 'Append to the Existing Role'*/
         if(AMSStringUtil.strEqual(msCopyAction,"0"))
         {
            Enumeration loKeysEnum = mhSelectedRows.keys();
              while (loKeysEnum.hasMoreElements())
            {
               loKey   = loKeysEnum.nextElement() ;
               loRow   = (VSRow)mhSelectedRows.get(loKey );
               liIndex = foTgtUserRoleLnkRS.findFirst(
                   "SEC_ROLE_ID LIKE "+ AMSSQLUtil.getANSIQuotedStr(
                    loRow.getData("SEC_ROLE_ID").getString(),true));


                 if(liIndex > 0)
               {
                  mhSelectedRows.remove(loKey);
               }

            } /* end while (loKeysEnum.hasMoreElements()) */

         } /* end if(AMSStringUtil.strEqual(msCopyAction,"0")) */

      loLastRow = foTgtUserRoleLnkRS.last();
      if(loLastRow != null)
      {
         liLastPrecNo = loLastRow.getData("PREC_NO").getInt();
      }
      Iterator loIterator =  mhSelectedRows.values().iterator();
         while (loIterator.hasNext())
         {
            loOrigRow = (VSRow)loIterator.next() ;
            loNewRow = foTgtUserRoleLnkRS.insert() ;
            if ( loNewRow == null )
            {
               return ;
            } /* end if ( loNewRow == null ) */

            cloneRow( loOrigRow, loNewRow ) ;
            VSData loData = loNewRow.getData( "USER_ID" ) ;
            if ( loData != null )
            {
              loData.setString( fsTgtUserID ) ;
            }
         loData = loNewRow.getData( "PREC_NO" ) ;
            if ( loData != null )
            {
              loData.setInt( ++liLastPrecNo) ;

            }

      }
         loVSResultSet.close() ;

         return ;
      } //end copySecurityRoleLinks

      /* Returns the ResultSet.
       * @param fsQueryName DataObject Name.
       * @param fsWhereClause Condition Clause
       * @return VSResultSet.
       */
      public VSResultSet getResultSet( String fsQueryName,
                                       String fsWhereClause )
      {
          VSQuery loQuery;

          loQuery = new VSQuery( getParentApp().getSession(), fsQueryName,
                                 fsWhereClause, "PREC_NO" ) ;

          return loQuery.execute() ;
      } /* end getResultSet() */

      /* Returns the ResultSet
       * @param fsQueryName DataObject Name.
       * @return VSResultSet
       */
      public VSResultSet getNewResultSet( String fsQueryName )
      {
          VSQuery loQuery;

          loQuery = new VSQuery( getParentApp().getSession(), fsQueryName, "", "" ) ;

          return loQuery.getNewResultSet() ;
      } /* end getNewResultSet() */

      /*
       *   This method is invoked before generation of a page.
       *   It enables/disables the back,next,copyWorkflowRoles buttons
       *   depending upon the page being viewed.
       @ return none.
      */
      public void beforeGenerate()
      {
         VSRow   loSourceRow = null ;
         VSRow   loTargetRow = null ;

         if ( mboolFirstGenerate )
         {
            mboolFirstGenerate = false ;
            moStepTitle       = (TextContentElement)getElementByName( "CurrStepTitle" ) ;
            moStep1Cont       = (DivElement)getElementByName( "Step1Cont" ) ;
            moStep2Cont       = (DivElement)getElementByName( "Step2Cont" ) ;
            moStep3Cont       = (DivElement)getElementByName( "Step3Cont" ) ;
            moSourceUserID    = (TextFieldElement)getElementByName( "fromUserID" ) ;
            moTargetUserID    = (TextFieldElement)getElementByName( "toUserID" ) ;
         } /* end if ( mboolFirstGenerate ) */

         switch( miCurStep )
         {
            case 1 :
               moStepTitle.setValue( "Step 1: Select a user to copy "+
               "selected roles from..." ) ;
               moStep1Cont.setVisible( true ) ;
               moStep2Cont.setVisible( false ) ;
               moStep3Cont.setVisible( false ) ;
               break ;
            case 2 :
               moStepTitle.setValue( "Step 2: Select a user to copy "+
               "selected roles into" ) ;
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
               moSourceUserID.setValue( loSourceRow.getData( "USER_ID" ).getString() ) ;
               moTargetUserID.setValue( loTargetRow.getData( "USER_ID" ).getString() ) ;
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
         VSRow   loSourceRow ;
         VSRow   loSourceRoleRow ;
         VSRow   loTargetRow ;
         String  lsSourceUserID ;
         String  lsTargetUserID ;
         boolean lboolRC = true ;

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
               if ( lboolRC )
               {
                    loSourceRoleRow = T2qR_SC_USER_ROLES_QRY.getCurrentRow() ;
                   if ( loSourceRoleRow == null )
                   {
                      raiseException( "Selected source user does not have any User role.",
                            SEVERITY_LEVEL_ERROR ) ;
                      lboolRC = false ;

                   }
               }
               if ( lboolRC )
               {
                   if(mvPreviouslySelected.size() == 0)
                   {
                      raiseException("Select User roles of source user to be copied.",
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
