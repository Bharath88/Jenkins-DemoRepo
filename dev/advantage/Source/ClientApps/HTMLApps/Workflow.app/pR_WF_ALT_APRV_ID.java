//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
	
	/*
	**  pR_WF_ALT_APRV_ID
	*/
	
	//{{FORM_CLASS_DECL
	public class pR_WF_ALT_APRV_ID extends pR_WF_ALT_APRV_IDBase
	
	//END_FORM_CLASS_DECL}}
	{
	   /** The hidden (by a div) input element for the assignee user id attribute */
	   private ScalarElement moUserIDInput = null ;
	   /** The hidden (by a div) input element for the assignee role id attribute */
	   private ScalarElement moRoleIDInput = null ;
	   /** The input element for the assignee attribute */
	   private ScalarElement moAssigneeInput = null ;
	   /** The input element for the assignee flag attribute */
	   private ScalarElement moAssigneeCheckbox = null ;
	
	
	   // Declarations for instance variables used in the form
	
	   // This is the constructor for the generated form. This also constructs
	   // all the controls on the form. Do not alter this code. To customize paint
	   // behavior, modify/augment the paint and the handleEvent methods.
	//{{FORM_CLASS_CTOR
	public pR_WF_ALT_APRV_ID ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	   }
	
	
	
	//{{EVENT_CODE
	//{{EVENT_T1R_WF_ALT_APRV_ID_beforeSave
void T1R_WF_ALT_APRV_ID_beforeSave(VSRow row ,VSOutParam cancel )
{
   VSORBSession loCurrentSession = getParentApp().getSession().getORBSession();

   // reset userid/roleid to 0 as it is part of primary key
   if ( row.getData("ASSIGNEE_FL").getBoolean() )
   {
      row.getData( "USID" ).setString( "0" );
   }
   else
   {
      row.getData( "ROLEID" ).setString( "0" );
   }
   // reset alternate userid/alternate roleid
   if ( row.getData("ALT_ASSIGNEE_FL").getBoolean() )
   {
      row.getData( "ALT_USID" ).setString( null );
   }
   else
   {
      row.getData( "ALT_ROLEID" ).setString( null );
   }
}
//END_EVENT_T1R_WF_ALT_APRV_ID_beforeSave}}
//{{EVENT_T1R_WF_ALT_APRV_ID_beforeQuery
void T1R_WF_ALT_APRV_ID_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   // User/alt user and role/alt role filter criteria are being added if
   // specified by the user. This is being done as we do not know whether
   // the user is searching for user or role (or alternate user or roles)
   String lsOrigWhereClause = null ;
   String lsNewWhereClause  = null ;
   ActionElement loActionElement = getCurrentAction();
   if(loActionElement != null)
   {
    	if(!loActionElement.getAction().equals(ActionElement.db_query))
		{
			return;
		}
   }
   // build new alternate where clause if the user specified a search
   lsOrigWhereClause = query.getSQLWhereClause() ;
   if ( ( lsOrigWhereClause != null ) || ( lsOrigWhereClause.trim().length() > 0 ) )
   {
      lsNewWhereClause = buildNewWhereClause( lsOrigWhereClause ) ;
      // get rows matching newly created alternate filter criteria
      if ( lsNewWhereClause.trim().length() > 0 )
      {
         DataSource loDataSource = getRootDataSource() ;
         VSQuery loQuery = new VSQuery( getParentApp().getSession(),
                                        loDataSource.getMetaQueryName(),
                                        lsNewWhereClause,
                                        loDataSource.getQueryInfo().getOrderBy() ) ;
         VSResultSet loRows = loQuery.execute() ;
         resultset.setValue( loRows );
      }
   }
}
//END_EVENT_T1R_WF_ALT_APRV_ID_beforeQuery}}

	//END_EVENT_CODE}}
	
	   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1R_WF_ALT_APRV_ID.addDBListener(this);
	//END_EVENT_ADD_LISTENERS}}
	   }
	
	//{{EVENT_ADAPTER_CODE
	
	public void beforeSave( DataSource obj, VSRow row ,VSOutParam cancel ){
		Object source = obj;
		if (source == T1R_WF_ALT_APRV_ID) {
			T1R_WF_ALT_APRV_ID_beforeSave(row ,cancel );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1R_WF_ALT_APRV_ID) {
			T1R_WF_ALT_APRV_ID_beforeQuery(query , resultset );
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
	
	     /**
	      * This method is required as the fact that a pick has been
	      * simulated is lost by Versata. Appropriate simulated pick
	      * request parameters are added to the pls request based on
	      * the parameters already available in the pls request
	      *
	      * @param foPLSReq The PLS Request
	      * @return PLS request
	      */
	   public String doAction(PLSRequest foRequest)
	   {
	      String lsParm1Val = foRequest.getParameter("pckT1ASSIGNEE.x") ;
	      String lsParm2Val = foRequest.getParameter("pckT1ALT_ASSIGNEE.x") ;
	
	      if (lsParm1Val != null)
	      {
	         if (foRequest.getParameter("txtT1ASSIGNEE_FL") != null)
	         {
	            foRequest.addParameter("pckT1ROLEID","Submit");
	         }
	         else
	         {
	            foRequest.addParameter("pckT1USID","Submit");
	         }
	      }
	      if (lsParm2Val != null)
	      {
	         if (foRequest.getParameter("txtT1ALT_ASSIGNEE_FL") != null)
	         {
	            foRequest.addParameter("pckT1ALT_ROLEID","Submit");
	         }
	         else
	         {
	            foRequest.addParameter("pckT1ALT_USID","Submit");
	         }
	      }
	      return super.doAction(foRequest);
	   }
	
	   /**
	    * Build new where clause based on the search criteria specified by the user.
	    * Eg: ((USID_UP = 'SA')) will be converted to
	    *     ((((USID_UP = 'SA')) OR ((ROLEID_UP = 'SA'))))
	    *
	    * @param fsOrigWhereClause The original where clause generated by Versata
	    * @return new where clause
	    */
	   public String buildNewWhereClause( String fsOrigWhereClause )
	   {
	      int               liUsid ;
	      int               liAltUsid ;
	      String            lsFirstPart       = null ;
	      String            lsSecondPart      = null ;
	      StringBuffer      lsbNewWhereClause = new StringBuffer() ;
	      VSStringTokenizer loStrTok ;
	
	      // Parse the compound where clause into individual components
	      loStrTok = new VSStringTokenizer( fsOrigWhereClause, "AND", true );
	      while ( loStrTok.hasMoreTokens() )
	      {
	         if ( lsFirstPart == null )
	         {
	            lsFirstPart = loStrTok.nextToken() ;
	         }
	         else if ( lsSecondPart == null )
	         {
	            lsSecondPart = loStrTok.nextToken() ;
	         }
	         else
	         {
	            raiseException( "More than two tokens not expected in WHERE clause, " +
	                            "please contact your System Administrator",
	                            SEVERITY_LEVEL_ERROR);
	            break ;
	         }
	      }
	      // Build matching clauses for search criteria
	      if ( lsSecondPart != null )
	      {
	         // both USID_UP and ALT_USID_UP have been specified
	         lsbNewWhereClause.append( "((" ) ;
	         lsbNewWhereClause.append( buildMatchingClause( lsFirstPart, 0 ) ) ;
	         lsbNewWhereClause.append( " AND " + buildMatchingClause( lsSecondPart, 1 ) ) ;
	         lsbNewWhereClause.append( "))" ) ;
	      }
	      else if ( lsFirstPart != null )
	      {
	         // only one of USID_UP or ALT_USID_UP has been specified
	         liUsid    = lsFirstPart.indexOf( "((USID_UP " ) ;
	         liAltUsid = lsFirstPart.indexOf( "((ALT_USID_UP " ) ;
	         if ( liUsid != -1 )
	         {
	            lsbNewWhereClause.append( "((" ) ;
	            lsbNewWhereClause.append( buildMatchingClause( fsOrigWhereClause, 0 ) ) ;
	            lsbNewWhereClause.append( "))" ) ;
	         }
	         else if ( liAltUsid != -1 )
	         {
	            lsbNewWhereClause.append( "((" ) ;
	            lsbNewWhereClause.append( buildMatchingClause( fsOrigWhereClause, 1 ) ) ;
	            lsbNewWhereClause.append( "))" ) ;
	         }
	      }
	      return lsbNewWhereClause.toString() ;
	   }
	
	   /**
	    * Build where clause matching the first field specified. Thus, for usid an
	    * additional roleid clause will be added, a similarl clause will also be
	    * added for alternate user/role fields.
	    *
	    * @param fsWhere The original where clause
	    * @param fsType The field type
	    * @return matching where clause
	    */
	   public String buildMatchingClause( String fsWhere, int fiType )
	   {
	      String lsRemainder ;
	      String lsNewWhere ;
	      if ( fiType == 0 )
	      {
	        lsRemainder = fsWhere.substring( 10 ) ;
	        lsNewWhere  = "((" + fsWhere + " OR ((ROLEID_UP " + lsRemainder + "))";
	      }
	      else
	      {
	        lsRemainder = fsWhere.substring( 14 ) ;
	        lsNewWhere  = "((" + fsWhere + " OR ((ALT_ROLEID_UP " + lsRemainder + "))";
	      }
	      return lsNewWhere ;
	   }
	
	   /**
	    * This method is used to disable the scalar assignee field if
	    * either the user id or role id hidden fields are disabled.
	    * This is needed because although the assignee id is not part
	    * of the primary key, user id and role id are, so when primary
	    * key updates are not allowed, the assignee field should be
	    * disabled.
	    */
	   public void beforeGenerate()
	   {
	      if ( moUserIDInput == null )
	      {
	         moUserIDInput = (ScalarElement)getElementByName( "txtT1USID" ) ;
	      } /* end if ( moUserIDInput == null ) */
	      if ( moRoleIDInput == null )
	      {
	         moRoleIDInput = (ScalarElement)getElementByName( "txtT1ROLEID" ) ;
	      } /* end if ( moRoleIDInput == null ) */
	      if ( moAssigneeInput == null )
	      {
	         moAssigneeInput = (ScalarElement)getElementByName( "txtT1ASSIGNEE" ) ;
	      } /* end if ( moAssigneeInput == null ) */
	      if ( moAssigneeCheckbox == null )
	      {
	         moAssigneeCheckbox = (ScalarElement)getElementByName( "txtT1ASSIGNEE_FL" ) ;
	      } /* end if ( moAssigneeCheckbox == null ) */
	
	      if ( ( moAssigneeInput != null ) && ( moAssigneeCheckbox != null ) )
	      {
	         if ( ( ( moUserIDInput != null ) && ( moUserIDInput.needsLocking() ) ) ||
	              ( ( moRoleIDInput != null ) && ( moRoleIDInput.needsLocking() ) ) )
	         {
	            moAssigneeInput.setEnabled( false ) ;
	            moAssigneeCheckbox.setEnabled( false ) ;
	         } /* end if ( ( ( moUserIDInput != null ) && ( moUserIDInput.needsLocking() ) ) || . . . */
	         else
	         {
	            moAssigneeInput.setEnabled( true ) ;
	            moAssigneeCheckbox.setEnabled( true ) ;
	         } /* end else */
	      } /* end if ( ( moAssigneeInput != null ) && ( moAssigneeCheckbox != null ) ) */
	   } /* end beforeGenerate() */
	
	  /**
	   * This method overrides the super class generate method.
	   *
	   * Modification Log : Siddhartha Das  - 03/21/02
	   *                                    - inital version
	   */
	   public String generate()
	   {
	      /**
	       * Assignee picks trigger the hidden userid/roleid picks
	       * which cause problems when setting focus after the pick
	       * window is closed. The equivalent assignee fields are
	       * instead being substituted as the actual pick field on
	       * the startup page to prevent this problem.
	       */
	       String lsLastAction = getLastAction();
	       if ( ( lsLastAction != null ) &&
	            ( lsLastAction.equals( ActionElement.db_pick ) ) )
	       {
	          appendOnloadString("WFALTAPRV_AdjustPickField();");
	       }
	      return super.generate() ;
	   } /* End of generate () */
	
	}
	