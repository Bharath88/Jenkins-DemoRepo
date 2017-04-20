//{{IMPORT_STMTS
package advantage.Workflow;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.client.dbitem.AMSPLSUtil;
import com.amsinc.gems.adv.common.*;
import com.amsinc.gems.adv.vfc.html.*;
import com.amsinc.gems.adv.workflow.*;
import com.amsinc.gems.adv.workflow.aprv.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Enumeration;
import advantage.AMSStringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.swing.text.MutableAttributeSet;


/*
**  pTWIP*/

//{{FORM_CLASS_DECL
public class pTWIP extends pTWIPBase

//END_FORM_CLASS_DECL}}
{

   private boolean      mboolFirstTime                 = true ;
   private StringBuffer msbActiveAprvls                = new StringBuffer(250) ;
    private static final String  ATTR_APRV_RULE_ID      = "APRV_RULE_ID" ;
   private static final String  ATTR_ORD_NO            = "ORD_NO_" ;
   private static final String  ATTR_APRV_LVL          = "APRV_LVL_" ;
   private static final String  ATTR_ASGN_DT           = "ASGN_DT_" ;
   private static final String  ATTR_APRV_ASGN_NM      = "APRV_ASGN_NM_" ;
   private static final String  ATTR_APRV_ASGN_USER_ID = "APRV_ASGN_" ;
   private static final String  ATTR_APRV_ACTN_NM      = "APRV_ACTN_NM_" ;
   private static final String  ATTR_APRV_ACTN_STA     = "APRV_ACTN_STA_" ;
   private static final String  ATTR_TEXT              = "txt" ;
   private static final String  DIV_NAME               = "DivRow" ;
   private static final String  ROW_NAME               = "APRV_ROW_" ;
   private static final String  TABLE_NAME             = "TWIPTable" ;
   private static final HashMap< Integer, String > STA_CD_DISP    = new HashMap< Integer, String >();  //Stores the Action Status Code <---> Action Status Display association

   VSResultSet moQryRs=null;    //Stores the Result Set OF the Workflow Approval Log Table


   private static final String  TABLE_HEADER = "<table name=\"AprvSheet\" id=\"AprvSheet\" class=\"advgrid\">"
      + "<tr class=\"advgridhdr\"><th class=\"advcorner left\"></th>"
         + "<th><span title=\"Approval Rule ID\">Approval Rule ID</span></th>"
      + "<th><span title=\"Seq No\">Seq No</span></th>"
      + "<th><span title=\"Approval Level\">Approval Level</span></th>"
      + "<th><span title=\"Assignment Date\">Assignment Date</span></th>"
      + "<th><span title=\"Initial Assignee Name\">Initial Assignee Name</span></th>"
      + "<th><span title=\"Approval Status\">Approval Status</span></th>"
      + "<th><span title=\"Approval User Name\">Approval User Name</span></th>"
      + "<th class=\"advcorner right\"></th></tr>" ;

   private static final String  TABLE_FOOTER = "</table>" ;


   private JSONObject moAprvHist                   = new JSONObject();  // Stores the Complete Information about the TWIP GRID. It has the deatils of the Apprvl Lvls
   private static String  msApprvLvlStatusBefore   = null ;             // Stores the "Status Before " Of an Apprvl Lvl
   private static String  msApprvLvlStatusAfter    = null ;             // Stores the "Status After  " Of an Apprvl Lvl
   private Hashtable mhtCurrentApprvLevel          = new Hashtable();   // Stores current Approval Level(s) as key and Initial Assignee(s) as Value.
   /**
    * This represents the Application Control Parameter which controls the
    * default view of the TWIP Page. Whethers it shows the Grid View or the visual
    * View.
    */
   public static final String TWIP_DFLT_VIEW = "TWIP_DFLT_VIEW";



   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pTWIP ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_T3WF_APRV_WRK_LST_beforeQuery
void T3WF_APRV_WRK_LST_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   //Write Event Code below this line
   StringBuffer lsbWhere = new StringBuffer(128);
   Enumeration loKeys = mhtCurrentApprvLevel.keys();
   boolean lboolFirst = true;
   String lsAprvLvl;
   /* mhtCurrentApprvLevel contains the current Approval Levels and their Assignee(s) from
      Approval sheet (WF_TWIP_QRY) datasource. Frame WHERE clause on WF_APRV_WRK_LST
      datasource such that Current Assignee(s) for the current Approval Levels are fetched.
      Initial Assignee is the original Assignee as per Approval Rule and is obtained from
      WF_TWIP_QRY datasource. Current Assignee may be different from Initial Assignee
      because of Alternate assignment, Manual Route, Reassign or Take Task action.
      To obtain Current Assignee(s), query Worklist (WF_APRV_WRK_LST) such that
      ((Assignee on the Worklist entry is different than the Initial Assignee) OR
      (Assignee is a Approver Role AND an Approver User has taken the task)).
   */
   while(loKeys.hasMoreElements())
   {

      /* Get each current Approval Level. Note multiple Approval Levels can exist at the
         same time when more than 1 Approval Level on Approval Rules have the same Approval
         Sequence number. */
      lsAprvLvl = loKeys.nextElement().toString();
      /* Note WF_APRV_WRK_LST datasource already has WHERE conditions to filter records
         for the current document, hence add "AND" to append new conditions */
      if( lboolFirst )
      {
         lsbWhere.append(" AND (");
         lboolFirst = false;
      }
      else
      {
         lsbWhere.append(" OR ");
      }
      lsbWhere.append("(APRV_LVL = ")
            .append(lsAprvLvl)
            .append(" AND ((ASSIGNEE ")
            .append(AMSSQLUtil.getANSIQuotedStr(mhtCurrentApprvLevel.get(lsAprvLvl).toString(),
                 AMSSQLUtil.NOT_EQUALS_OPER))
            .append(") OR (ASSIGNEE_FL=1 AND LOCK_USID IS NOT NULL) ) )");
   }//end while

   //Case where current Approval Level(s) were found(e.g. document in Pending state)
   if( !lboolFirst )
   {
      //Query Worklist with the additional WHERE clause to obtain Current Assignee(s)
      lsbWhere.append(")");
      SearchRequest loSearchRequest = new SearchRequest() ;
      loSearchRequest.add( lsbWhere.toString() );
      query.addFilter( loSearchRequest ) ;
   }
   else
   {
      /* If there are no current Approval Level(s)(for e.g. Finalized document which
         has passed all Approvals) then bypass the query to WF_APRV_WRK_LST
         (no need to fire it) */
      resultset.setValue(true);
   }//end else
}
//END_EVENT_T3WF_APRV_WRK_LST_beforeQuery}}
//{{EVENT_pTWIP_beforeGenerate
void pTWIP_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
      HiddenElement loPageTypElem = (HiddenElement)getElementByName( "PageType" );
      DivElement loTCEALTASSGNE = (DivElement)getElementByName( "DivCurrAssignee" );

      DivElement loTCEAPRVSHEET = (DivElement)getElementByName( "AprvSheetWrapper" );

      DivElement loVisualWorkflowLegend = (DivElement)getElementByName( "visualWorkflowLegend" );

      DivElement loVisualWorkflow = (DivElement)getElementByName( "visualWorkflow" );

      DivElement loShowVisualLink = (DivElement)getElementByName( "showVisual" );
      DivElement loShowGridLink = (DivElement)getElementByName( "showGrid" );

      if( "Visual".equalsIgnoreCase( AMSPLSUtil.getApplParamValue( TWIP_DFLT_VIEW,
            getParentApp().getSession() ) ) )
      {
          loPageTypElem.setValue( "DocStdUI" );

          if(loTCEALTASSGNE !=null)
          {
             loTCEALTASSGNE.addAttribute( "style", "display:none" );
          }

          if(loVisualWorkflowLegend != null)
          {
             loVisualWorkflowLegend.addAttribute( "style", "display:block" );
          }

          if(loShowVisualLink != null)
          {
             loShowVisualLink.addAttribute( "style", "display:none" );
          }

          if(loShowGridLink != null)
          {
             loShowGridLink.addAttribute( "style", "display:block" );
          }

          HTMLElement loBodyElem    = getDocumentModel().getBodyElement() ;
          MutableAttributeSet loMAS = (MutableAttributeSet)loBodyElem.getAttributes();

          if(loMAS != null)
          {
             loMAS.addAttribute( "style", "background-image:none;" );
          }

         if(loVisualWorkflow !=null)
         {
            loVisualWorkflow.addAttribute( "style", "display:block" );
         }

         if(loTCEAPRVSHEET !=null)
         {
            loTCEAPRVSHEET.addAttribute( "style", "display:none" );
         }
      }
      else
      {
         loPageTypElem.setValue( "" );

         if(loVisualWorkflowLegend != null)
         {
            loVisualWorkflowLegend.addAttribute( "style", "display:none" );
         }

         if(loVisualWorkflow !=null)
         {
            loVisualWorkflow.addAttribute( "style", "display:none" );
         }

         if(loTCEAPRVSHEET !=null)
         {
             loTCEAPRVSHEET.addAttribute( "style", "display:block" );
         }

         if(loShowVisualLink != null)
         {
            loShowVisualLink.addAttribute( "style", "display:block" );
         }

         if(loShowGridLink != null)
         {
            loShowGridLink.addAttribute( "style", "display:none" );
         }
      }
}
//END_EVENT_pTWIP_beforeGenerate}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T3WF_APRV_WRK_LST.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pTWIP_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T3WF_APRV_WRK_LST) {
			T3WF_APRV_WRK_LST_beforeQuery(query , resultset );
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
    * This method overrides the beforeGenerate method to set the page title
    * so that the name of the workflow role is displayed as part of the
    * page title.
    *
    * Modification Log : Kiran Hiranandani   - 09/18/02
    *                                        - inital version
    *
    */
   public void beforeGenerate()
   {
      if ( mboolFirstTime )
      {
         VSRow              loCurrRow               = null ;
         VSRow              loCurrStatusCodeRow     = null ;
         VSRow              loDocPhaseRow           = null ;
         int                liNumRows               = 0 ;
         int                liCurrRow               = 0 ;
         int                liActvCtr               = 0 ;
         DataSource         loRootDataSrc           = getRootDataSource() ;
         boolean            lboolNextRow            = false ;
         int[]              loSeqNoArray ;
         StringBuffer       lsbHTML                 = new StringBuffer( 1000 ) ;
         boolean            lboolOddRow             = true ;

         // Gets the Handle To the "Aprv Sheet" On The TWIP Page.
         TextContentElement loTCE                   = (TextContentElement)getElementByName( "AprvSheet" ) ;

         // Gets the Handle To the "Aprv Hist JSON" Hidden Element On The TWIP Page.
         HiddenElement loHEAPRVHIST                 = (HiddenElement)getElementByName( "AprvHistJson" ) ;

         /*
          *  Gets the Handle To the "Final Doc Phase" Hidden Element On The TWIP Page.
          */
         HiddenElement loFINALDOCPHASE              = (HiddenElement)getElementByName( "FinalDocPhase" ) ;


         HiddenElement loDOCID              = (HiddenElement)getElementByName( "DocId" ) ;


         DivElement loVisualWorkflow = (DivElement)getElementByName( "visualWorkflow" );


         SearchRequest loSearchReq                  = new SearchRequest();
         int liRowCount                             = 0;
         String       lsShowVisualTWIP              = null ;

      /*
       *  Setting the default Final Phase of a document as Pending.
       */
         loFINALDOCPHASE.setValue( "Pending" );

         VSResultSet loStatusCodeRs       = null;
         VSResultSet loDocPhaseRs         = null;
         SearchRequest loRequest          = new SearchRequest();
         SearchRequest loDocPhaseRequest  = new SearchRequest();
         VSSession loSession = getParentApp().getSession();

         /*
          * Gets the "SHOW_VISUAL_TWIP" value from the Application Control Parameters table. This decides to show Visual/Grid View on the TWIP Page.
          */
         lsShowVisualTWIP = AMSPLSUtil.getApplParamValue(TWIP_DFLT_VIEW,loSession);

         liNumRows = loRootDataSrc.getTotalRows() ;


         if ( liNumRows < 1 )
         {
            raiseException( "No approval record exists for this document",
                     SEVERITY_LEVEL_SEVERE ) ;
            return ;
         }/* end if ( liNumRows < 1 ) */

         if ( liNumRows != 2 )
         {
            raiseException( "Only two approval records should exist for this document.",
                  SEVERITY_LEVEL_SEVERE ) ;
            return ;
         }/* end if ( liNumRows != 2 ) */

         lsbHTML.append( TABLE_HEADER ) ;
         loRootDataSrc.first() ;
         loCurrRow = loRootDataSrc.getCurrentRow() ;
         setPageTitle( loCurrRow ) ;

         /*
         *  Query The WF_APRV_LOG To get the entires, this will used for Before and After Status.
         *
         */


         loDOCID.setValue( loCurrRow.getData( "DOC_ID" ).getString() );

         loRequest.addParameter("WF_APRV_LOG", "DOC_ID", loCurrRow.getData( "DOC_ID" ).getString());
         loRequest.addParameter("WF_APRV_LOG", "DOC_CD", loCurrRow.getData( "DOC_CD" ).getString());
         loRequest.addParameter("WF_APRV_LOG", "DOC_DEPT_CD", loCurrRow.getData( "DOC_DEPT_CD" ).getString());

         VSQuery loSeqNoQry = new VSQuery(loSession,"WF_APRV_LOG", loRequest,null);

         moQryRs = loSeqNoQry.execute();
         moQryRs.last();

         liRowCount=moQryRs.getRowCount();


         /*
         *  Query The CVL_WF_STA_ACTN To get the entires, this will used for "CVL STATUS CODE & DISPLAY NAME"
         *
         */

         loRequest=null;
         VSQuery loSeqNoQryCVL = new VSQuery(loSession,"CVL_WF_STA_ACTN" , loRequest , null);
         loStatusCodeRs = loSeqNoQryCVL.execute();
         loStatusCodeRs.last();

         liRowCount=loStatusCodeRs.getRowCount();

         if(loStatusCodeRs !=null)
         {
            liRowCount=loStatusCodeRs.getRowCount();
            for( int liIndex =1;liIndex <=liRowCount; liIndex++)
            {
               loCurrStatusCodeRow = loStatusCodeRs.getRowAt(liIndex);
               STA_CD_DISP.put( loCurrStatusCodeRow.getData("LOG_STA_CD").getInt(),loCurrStatusCodeRow.getData("LOG_STA_DISP").getString());
            }
         }


        /*
         *  Query The DOC_HDR To get the entry, this will used for fetching the Documments Final Phase.
         *
         */

         loDocPhaseRequest.addParameter("DOC_HDR", "DOC_ID", loCurrRow.getData( "DOC_ID" ).getString());
         loDocPhaseRequest.addParameter("DOC_HDR", "DOC_CD", loCurrRow.getData( "DOC_CD" ).getString());
         loDocPhaseRequest.addParameter("DOC_HDR", "DOC_DEPT_CD", loCurrRow.getData( "DOC_DEPT_CD" ).getString());
         loDocPhaseRequest.addParameter("DOC_HDR", "DOC_VERS_NO", loCurrRow.getData( "DOC_VERS_NO" ).getString());

         loSeqNoQry = new VSQuery(loSession,"DOC_HDR", loDocPhaseRequest,null);

         // execute query into resultset
         loDocPhaseRs = loSeqNoQry.execute();
         loDocPhaseRs.last();
         if(loDocPhaseRs !=null)
         {
            liRowCount=loDocPhaseRs.getRowCount();
            for( int liIndex =1;liIndex <=liRowCount; liIndex++)
            {
               loCurrStatusCodeRow = loDocPhaseRs.getRowAt(liIndex);

               if(loCurrStatusCodeRow.getData("DOC_PHASE_CD").getInt() == 3)
               {
                  loFINALDOCPHASE.setValue("Final");
               }
            }
         }

         loSeqNoArray = getRoutingSeqArray() ;

         /* Loop through 15 approval levels */
         for ( int liCtr = 1 ; liCtr <= AMSAprvWorkflowToolKit.NUM_APRVL_LVLS ; liCtr++ )
         {
            for ( int liAryCtr = 1 ; liAryCtr <= AMSAprvWorkflowToolKit.NUM_APRVL_LVLS ; liAryCtr++ )
            {
               if ( loSeqNoArray[liAryCtr-1] == liCtr )
               {

                  if ( liAryCtr > 10 )
                  {
                     if ( !lboolNextRow )
                     {
                        loRootDataSrc.next() ;
                        loCurrRow = loRootDataSrc.getCurrentRow() ;
                        lboolNextRow = true ;
                     } /* end if ( !lboolNextRow ) */
                     liCurrRow = liAryCtr - 10 ;
                  } /* end if ( liAryCtr > 10 ) */
                  else
                  {
                     if ( lboolNextRow )
                     {
                        loRootDataSrc.previous() ;
                        loCurrRow = loRootDataSrc.getCurrentRow() ;
                        lboolNextRow = false ;
                     } /* end if ( lboolNextRow ) */
                     liCurrRow = liAryCtr ;
                  } /* end else */

                  lsbHTML.append( generateRow( loCurrRow, liCurrRow, liCtr, liAryCtr, lboolOddRow ) ) ;

                 /*
                  *  IF "lsShowVisualTWIP is TRUE " then Calculate the Aprv Sheet History and store it in a JSON Object as Hidden Field
                  *  On The TWIP Page.
                  *
                  */

                     generateRowJson( loCurrRow, liCurrRow, liCtr, liAryCtr, lboolOddRow );

                  lboolOddRow = !lboolOddRow ;
               } /* if ( loSeqNoArray[lAryCtr] == liCtr ) */
            } /* end inner for */

         } /* end for */
         lsbHTML.append( TABLE_FOOTER ) ;

         loHEAPRVHIST.setValue( moAprvHist.toString());
         loTCE.setValue( lsbHTML.toString() ) ;



         /* Display current Assignee(s) if they are different from Initial Assignee(s)
            due to Alternate assignment, Manual Route, Reassign or Take Task action. */
         displayCurrAssignee();

         mboolFirstTime = false ;

      } /* end if ( mboolFirstTime ) */

  } /* end beforeGenerate() */




   private void setPageTitle( VSRow foCurrRow )
   {
      String lsDocId       = foCurrRow.getData( "DOC_ID" ).getString() ;
      String lsDocCode     = foCurrRow.getData( "DOC_CD" ).getString() ;
      String lsDocDeptCode = foCurrRow.getData( "DOC_DEPT_CD" ).getString() ;
      String lsDocVersNo   = foCurrRow.getData( "DOC_VERS_NO" ).getString() ;

      if ( lsDocId != null )
      {
         StringBuffer lsbTitle = new StringBuffer( 80 ) ;

         lsbTitle.append( "Track Work in Progress - " ) ;
         lsbTitle.append( lsDocCode ).append( " - " ) ;
         lsbTitle.append( lsDocDeptCode ).append( " - " ) ;
         lsbTitle.append( lsDocId ).append( " - " ) ;
         lsbTitle.append( lsDocVersNo ) ;
         setTitle( lsbTitle.toString() ) ;
      } /* if ( lsWorkflowRole != null ) */
   } /* end setPageTitle() */

   private String generateRow( VSRow loDataRow, int fiCurrRow, int fiSeqNo, int fiAprvLvl, boolean fboolOddRow )
   {

      long         llAprvRuleId      = 0 ;
      VSData       loAprvRuleIdData     = null ;

      String       lsAsgnDtFld      = null ;
      String       lsAsgneeNmFld    = null ;
      String       lsAprvlFlFld     = null ;
      String       lsAprvUserNmFld  = null ;
      String       lsAsgnDt         = null ;
      String       lsAsgneeNm       = null ;
      String       lsAprvUserNm     = null ;
      int          liAprvlFl        = 0 ;
      String       lsAprvlStatus    = null ;
      VSData       loAsgnDtData     = null ;
      VSData       loAsgneeNmData   = null ;
      VSData       loAprvlFlData    = null ;
      VSData       loAprvUserNmData = null ;
      StringBuffer lsbHTML          = new StringBuffer( 256 ) ;

      lsAsgnDtFld       = ATTR_ASGN_DT + fiCurrRow ;
      lsAsgneeNmFld     = ATTR_APRV_ASGN_NM + fiCurrRow ;
      lsAprvlFlFld      = ATTR_APRV_ACTN_STA + fiCurrRow ;
      lsAprvUserNmFld   = ATTR_APRV_ACTN_NM + fiCurrRow ;

      loAprvRuleIdData     = loDataRow.getData( ATTR_APRV_RULE_ID ) ;
      llAprvRuleId         = loAprvRuleIdData  != null ? loAprvRuleIdData.getLong() : 0 ;

      loAsgnDtData     = loDataRow.getData( lsAsgnDtFld ) ;
      lsAsgnDt         = loAsgnDtData  != null ? (String)loAsgnDtData.getString() : "" ;

      loAsgneeNmData   = loDataRow.getData( lsAsgneeNmFld ) ;
      lsAsgneeNm       = loAsgneeNmData != null ? (String)loAsgneeNmData.getString() : "" ;

      loAprvlFlData    = loDataRow.getData( lsAprvlFlFld ) ;
      liAprvlFl        = loAprvlFlData != null ? loAprvlFlData.getInt() : 0 ;

      loAprvUserNmData = loDataRow.getData( lsAprvUserNmFld ) ;
      lsAprvUserNm     = loAprvUserNmData != null ? (String)loAprvUserNmData.getString() : "" ;

      switch ( liAprvlFl )
      {
         case 0 : // Pending
            lsAprvlStatus = "Pending" ;
            break ;
         case 1 : // Approved
            lsAprvlStatus = "Approved" ;
            break ;
         case 2 : // Rejected
            lsAprvlStatus = "Rejected" ;
            break ;
         case 3 : // Unapproved
            lsAprvlStatus = "Unapproved" ;
            break ;
         case 4 : // Recalled
            lsAprvlStatus = "Recalled" ;
            break ;
         case 5:  // Bypassed
             lsAprvlStatus = "Bypassed";
             break;
         case 6:  // N/A
             lsAprvlStatus = "N/A";
             break;
         default :
            lsAprvlStatus = "" ;
            break ;
      } /* end switch ( liAprvlFl ) */

      if ( fboolOddRow )
      {
         lsbHTML.append( "<tr class=\"advgridoddrow" ) ;
      } /* end if ( fboolOddRow ) */
      else
      {
         lsbHTML.append( "<tr class=\"advgridevenrow" ) ;
      } /* end else */

      /*
       * Highlight rows for Approval Level(s) at current Routing Sequence which is signified when
       * any of the following conditions are met:
       * 1. The Worklist item is pending in an Assignee's bucket for approval in a forward Workflow
       * 2. Worklist item is routed back to previous Assignee for re-confirmation when the item
       *    was rejected earlier.
       */
      if ( ( ( liAprvlFl == 0 ) && ( lsAsgnDt != null ) && ( lsAsgnDt.trim().length() > 0 ) ) ||
           ( (liAprvlFl == 1) && ( AMSStringUtil.strIsEmpty(lsAprvUserNm) ) ) )
      {
         lsbHTML.append( " advgridselrow\"><td class=\"advgridrowselect\">" ) ;
         lsbHTML.append( "<img title=\"Current Approval Step\" alt=\"Current Approval Step\" src=\"../AMSImages/ADVRowCurr.gif\">" ) ;
         /* Add the current Approval Level(s) to the Hashtable along with Initial Assignee(s).
            The Hashtable would be used to fetch Current Assignee(s). */
         String lsInitialAssignee = loDataRow
               .getData( ATTR_APRV_ASGN_USER_ID + fiCurrRow ).getString();
         if( (lsInitialAssignee!=null)
               && (!lsInitialAssignee.equals(AMSCommonConstants.EMPTY_STR) ) )
         {
            mhtCurrentApprvLevel.put( String.valueOf(fiAprvLvl), lsInitialAssignee );
         }
      } /* end if ( ( liAprvlFl == 0 ) && ( lsAsgnDt != null ) && ( lsAsgnDt.trim().length() > 0 ) ) */
      else
      {
         lsbHTML.append( "\"><td class=\"advgridrowselect\">" ) ;
         lsbHTML.append( "<img title=\"\" alt=\"\" src=\"../AMSImages/ADVRowSel.gif\">" ) ;
      } /* end else */

         lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( Long.toString( llAprvRuleId ) ) ;
      lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( Integer.toString( fiSeqNo ) ) ;
      lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( Integer.toString( fiAprvLvl ) ) ;
      lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( lsAsgnDt ) ;
      lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( lsAsgneeNm ) ;
      lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( lsAprvlStatus ) ;
      lsbHTML.append( "</td><td>" ) ;
      lsbHTML.append( lsAprvUserNm ) ;
      lsbHTML.append( "</td><td></td></tr>" ) ;


      return lsbHTML.toString() ;
   } /* end generateRow() */

   /**
    * This method returns an array with each member of the array having the
    * routing sequence number for that approval level.
    *
    * Modification Log : Kiran Hiranandani   - 09/18/02
    *                                        - inital version
    *
    * @param fiSeqNo routing sequence no
    * @return int[] an Array of approval levels which have the given
    */
   private int[] getRoutingSeqArray()
   {
      int[]       loSeqNoArray         = new int[ AMSAprvWorkflowToolKit.NUM_APRVL_LVLS ] ;
      int         liCurRow             = 0 ;
      int         liSeqNo              = 0 ;
      DataSource  loRootDataSrc        = getRootDataSource() ;
      boolean     lboolNextRow         = false ;
      VSRow       loCurrRow             = null ;
      String      lsSeqNoFld           = null ;
      VSData      loSeqData            = null ;

      loRootDataSrc.first() ;
      loCurrRow = loRootDataSrc.getCurrentRow() ;

      /* Loop through 15 approval levels */
      for ( int liCtr = 1 ; liCtr <= AMSAprvWorkflowToolKit.NUM_APRVL_LVLS ; liCtr++ )
      {
         if ( liCtr > 10 )
         {
            if ( !lboolNextRow )
            {
               loRootDataSrc.next() ;
               loCurrRow = loRootDataSrc.getCurrentRow() ;
               lboolNextRow = true ;
            } /* if ( !lboolNextRow ) */
            liCurRow = liCtr - 10 ;
         }
         else
         {
            liCurRow = liCtr ;
         } /* end else */

         lsSeqNoFld  = ATTR_ORD_NO + liCurRow ;
         loSeqData   = loCurrRow.getData( lsSeqNoFld ) ;
         if ( loSeqData != null )
         {
            liSeqNo = loSeqData.getInt() ;
         } /* end if ( loSeqData != null ) */
         else
         {
            liSeqNo = 0 ;
         } /* end else */

         loSeqNoArray[ liCtr-1 ] = liSeqNo ;
      } /* for ( int liCtr = 0 ; liCtr <= AMSAprvWorkflowToolKit.NUM_APRVL_LVLS ; liCtr++ ) */

      /* Set the current row as it was */
      if ( lboolNextRow )
      {
         loRootDataSrc.previous() ;
      } /* if ( lboolNextRow ) */

      return loSeqNoArray ;
   } /* end getRoutingSeqArray() */

   /**
    * Displays Current Assignee(s) when they are different from Initial Assignee(s)
    * due to Alternate assignment, Manual Route, Reassign or Take Task action.
    */
   private void displayCurrAssignee()
   {
      DivElement loCurrAssigneeDiv = (DivElement)getElementByName("DivCurrAssignee");
      //Case where current Approval Level(s) were found(e.g. document in Pending state)
      if( mhtCurrentApprvLevel.size() > 0 )
      {
         /* Execute query on WF_APRV_WRK_LST to obtain Current Assignee(s). Note
            beforeQuery event will add additional WHERE clause to this datasource. */
         T3WF_APRV_WRK_LST.executeQuery();
         VSResultSet loRSCurrAssignee = T3WF_APRV_WRK_LST.getResultSet();
         VSRow loRowCurrAssignee;
         String lsLockUserId;

         loRowCurrAssignee = loRSCurrAssignee.first();

         boolean lboolRowFound = false;

         while( loRowCurrAssignee != null )
         {
            lsLockUserId = loRowCurrAssignee.getData("LOCK_USID").getString();

            /* Display the User who has taken the task as Current Assignee for cases
               where approver is an Approver Role. */
            if( (lsLockUserId!= null) && (!lsLockUserId.equals(AMSCommonConstants.EMPTY_STR)) )
            {
               loRowCurrAssignee.getData("ASSIGNEE_NM")
                     .setString(loRowCurrAssignee.getData("LOCK_USER_NM").getString());


            }
            loRowCurrAssignee = loRSCurrAssignee.next();
            lboolRowFound = true;

         }//end while
         //Focus back to first record



         loRSCurrAssignee.first();

         //Display Current Assignee(s) section only when they were found
         if( lboolRowFound )
         {
            loCurrAssigneeDiv.setVisible( true );
         }
         else
         {
            loCurrAssigneeDiv.setVisible( false );
         }
      }//end if current Approval Level(s) found
      else
      {
         /* If there are no current Approval Level(s)(for e.g. Finalized document which
            has passed all Approvals) then no need to display Current Assignee(s) */
         loCurrAssigneeDiv.setVisible( false );
      }
   }//end of method


   /*
    * This method gets the data for the visual Workflow Representation. The data is further used to construct
    * the json Object for the GWT Application.
    *
    */

   private void generateRowJson( VSRow loDataRow, int fiCurrRow, int fiSeqNo, int fiAprvLvl, boolean fboolOddRow )
   {
      long         llAprvRuleId         = 0 ;
      VSData       loAprvRuleIdData     = null ;
      String       lsRouteSequenceKey   = null ;
      String       lsAsgnDtFld          = null ;
      String       lsAsgneeNmFld        = null ;
      String       lsAsgneeIdFld        = null ;
      String       lsAprvlFlFld         = null ;
      String       lsAprvUserNmFld      = null ;
      String       lsAsgnDt             = null ;
      String       lsAsgneeNm           = null ;
      String       lsAsgneeId           = null ;
      String       lsAprvUserNm         = null ;
      String       lsCurrentAction      = "";
      String       lsCurrentLevel       = null;
      String       lsCurrentLevelList;
      int          liAprvlFl            = 0 ;
      String       lsAprvlStatus        = null ;
      VSData       loAsgnDtData         = null ;
      VSData       loAsgneeNmData       = null ;
      VSData       loAsgneeIDData       = null ;
      VSData       loAprvlFlData        = null ;
      VSData       loAprvUserNmData     = null ;
      StringBuffer lsbHTML              = new StringBuffer( 256 ) ;
      HiddenElement loCURRNTAPRVLVL     = (HiddenElement)getElementByName( "CurrentAprvRouteSequence" ) ;

      String        lsCurrentLevelIndicator="CurrentLevel_";

      lsAsgnDtFld       = ATTR_ASGN_DT + fiCurrRow ;
      lsAsgneeNmFld     = ATTR_APRV_ASGN_NM + fiCurrRow ;
      lsAprvlFlFld      = ATTR_APRV_ACTN_STA + fiCurrRow ;
      lsAprvUserNmFld   = ATTR_APRV_ACTN_NM + fiCurrRow ;
      lsAsgneeIdFld     = ATTR_APRV_ASGN_USER_ID + fiCurrRow;

      loAprvRuleIdData     = loDataRow.getData( ATTR_APRV_RULE_ID ) ;
      llAprvRuleId         = loAprvRuleIdData  != null ? loAprvRuleIdData.getLong() : 0 ;

      loAsgnDtData     = loDataRow.getData( lsAsgnDtFld ) ;
      lsAsgnDt         = loAsgnDtData  != null ? (String)loAsgnDtData.getString() : "" ;

      loAsgneeNmData   = loDataRow.getData( lsAsgneeNmFld ) ;
      lsAsgneeNm       = loAsgneeNmData != null ? (String)loAsgneeNmData.getString() : "" ;

      loAsgneeIDData   = loDataRow.getData( lsAsgneeIdFld ) ;
      lsAsgneeId       = loAsgneeNmData != null ? (String)loAsgneeIDData.getString() : "" ;

      loAprvlFlData    = loDataRow.getData( lsAprvlFlFld ) ;
      liAprvlFl        = loAprvlFlData != null ? loAprvlFlData.getInt() : 0 ;

      loAprvUserNmData = loDataRow.getData( lsAprvUserNmFld ) ;
      lsAprvUserNm     = loAprvUserNmData != null ? (String)loAprvUserNmData.getString() : "" ;



      lsCurrentLevel =  loCURRNTAPRVLVL.getValue();

      if ( ( ( liAprvlFl == 0 ) && ( lsAsgnDt != null ) && ( lsAsgnDt.trim().length() > 0 ) ) ||
            ( (liAprvlFl == 1) && ( AMSStringUtil.strIsEmpty(lsAprvUserNm) ) ) )
      {
         if(lsCurrentLevel.isEmpty())
         {
            lsCurrentLevel=lsCurrentLevelIndicator+fiSeqNo;
         }else
         {
             lsCurrentLevel=lsCurrentLevel + "," + lsCurrentLevelIndicator+fiSeqNo;
         }

         loCURRNTAPRVLVL.setValue(lsCurrentLevel);
      }

      switch ( liAprvlFl )
      {
         case 0 : // Pending
            lsAprvlStatus = "Pending" ;
            break ;
         case 1 : // Approved
            lsAprvlStatus = "Approved" ;
            break ;
         case 2 : // Rejected
            lsAprvlStatus = "Rejected" ;
            break ;
         case 3 : // Unapproved
            lsAprvlStatus = "Unapproved" ;
            break ;
         case 4 : // Recalled
            lsAprvlStatus = "Recalled" ;
            break ;
         case 5:  // Bypassed
             lsAprvlStatus = "Bypassed";
             break;
         case 6:  // N/A
             lsAprvlStatus = "N/A";
             break;
         default :
            lsAprvlStatus = "" ;
            break ;
      } /* end switch ( liAprvlFl ) */


      JSONArray loAprvLvls = moAprvHist.optJSONArray( String.valueOf( fiSeqNo ) );


      if( loAprvLvls == null )
      {
          loAprvLvls = new JSONArray();
      }

      JSONObject loAprvDetails = new JSONObject();

      lsCurrentAction=getCurrentWorkflowActionForAprvLvl(fiAprvLvl);

      if(msApprvLvlStatusBefore == null && msApprvLvlStatusAfter == null)
      {
         msApprvLvlStatusBefore =  lsAprvlStatus;
         msApprvLvlStatusAfter  =  lsAprvlStatus;
      }

      loAprvDetails.element( "RouteSequence", String.valueOf( fiSeqNo ));
      loAprvDetails.element( "Level", fiAprvLvl );
      loAprvDetails.element( "Assignee", lsAsgneeNm );
      loAprvDetails.element( "AssigneeID", lsAsgneeId );
      loAprvDetails.element( "CurrentAssignee", getCurrentAssigneesForAprvLvl(fiAprvLvl) );
      loAprvDetails.element( "AssignmentDate", lsAsgnDt );
      loAprvDetails.element( "PreviousAction", msApprvLvlStatusBefore );
      loAprvDetails.element( "CurrentAction", msApprvLvlStatusAfter );

      loAprvLvls.add( loAprvDetails );
      moAprvHist.element( String.valueOf( fiSeqNo ) , loAprvLvls );
      }

   /*
    * GETS THE ALTENATE ASSIGNEE FOR A PARTICULAR APPROVAL LEVEL
    *
    */

   public String getCurrentAssigneesForAprvLvl(int fiAprvLvl)
   {
      T3WF_APRV_WRK_LST.executeQuery();
      VSResultSet loCurAsgRS = T3WF_APRV_WRK_LST.getResultSet();

      int liRowCnt = loCurAsgRS.getBufferedRowCount();

      JSONArray loArray= new JSONArray();
      JSONObject loCurrentAssigneeDetails = new JSONObject();

      for(int liCnt = 1;liCnt <= liRowCnt; liCnt++)
      {
         VSRow loRow = loCurAsgRS.getRowAt( liCnt, false );
         if(fiAprvLvl == loRow.getData( "APRV_LVL" ).getInt())
         {
            if(loRow.getData( "LOCK_USID" ).getString() == null || loRow.getData( "LOCK_USID" ).getString() == "")
            {
               loCurrentAssigneeDetails.element( "CurrentAssignee", loRow.getData( "ASSIGNEE" ).getString());
               loArray.add( loCurrentAssigneeDetails );
            }
            else
            {
               loCurrentAssigneeDetails.element( "CurrentAssignee", loRow.getData( "LOCK_USID" ).getString());
               loArray.add( loCurrentAssigneeDetails );
            }
         }
      }

      return loArray.toString();
   }

   /*
    * GETS THE WORKFLOW BEFORE AND AFTER STATUS FOR A PARTICULAR APPROVAL LEVEL
    *
    */

   public String getCurrentWorkflowActionForAprvLvl(int fiAprvLvl){

      int liRowCount         = 0;
      int liLog_STA_CD       = 0;
      String liLog_STA_DISP  = null;
      int lsLevel            = 0;
      msApprvLvlStatusBefore = null;
      msApprvLvlStatusAfter = null;

      if(moQryRs !=null)
      {
         liRowCount=moQryRs.getRowCount();

         for( int liIndex =1;liIndex <=liRowCount; liIndex++)
         {
            VSRow loCurrRow = moQryRs.getRowAt(liIndex);
            lsLevel =loCurrRow.getData("APRV_LVL").getInt();

            if(lsLevel == fiAprvLvl)
            {
               liLog_STA_CD=loCurrRow.getData("APRV_ACTN_STA_BFR").getInt();
               msApprvLvlStatusBefore=STA_CD_DISP.get( liLog_STA_CD );

               liLog_STA_CD=loCurrRow.getData("APRV_ACTN_STA_AFT").getInt();
               msApprvLvlStatusAfter=STA_CD_DISP.get( liLog_STA_CD );
            }
         }
      }
      return liLog_STA_DISP;
   }
}