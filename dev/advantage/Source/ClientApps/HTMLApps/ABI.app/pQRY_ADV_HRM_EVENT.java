//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import java.rmi.RemoteException;

import advantage.AMSStringUtil;
import advantage.CVL_DIRECTIONImpl;
import advantage.CVL_EVENT_STATUSImpl;
import advantage.CVL_PARTNERSImpl;
import advantage.CVL_PARTNER_IDImpl;

import org.apache.commons.logging.Log;

/*
**  pQRY_ADV_HRM_EVENT
*/

//{{FORM_CLASS_DECL
public class pQRY_ADV_HRM_EVENT extends pQRY_ADV_HRM_EVENTBase

//END_FORM_CLASS_DECL}}
{
   
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pQRY_ADV_HRM_EVENT.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   /**
    * Flag used to keep track of Update/ Insert transaction.
    */
   private boolean mboolUpdating;

   private static String RELATED_EVENTS_ACTION = "relatedEvents";

   private static String SERVICE_FLOWS_ACTION = "serviceFlows";
	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public pQRY_ADV_HRM_EVENT ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_T1QRY_ADV_HRM_EVENT_beforeQuery
void T1QRY_ADV_HRM_EVENT_beforeQuery(VSQuery foQuery ,VSOutParam foResultset )
{
	//Write Event Code below this line
	try
	{
	   if(getParentApp().getSession().getORBSession().getProperty("ABI_REFERENCE").trim().equals(""))
	      return;
	   /*
	    * Create new where clause based on the reference filter from the source page.
	    * "ABI_REFERENCE" contains the corresponding EVENT_ID field value as reference
	    * from BrassRing or Meridian source page.
	    * Hence use that value for fetching the required row.
	    */
	   SearchRequest lsr = new SearchRequest();
	   String lsWhere = " ADV_HRM_EVENT.EVENT_ID = " +
	         getParentApp().getSession().getORBSession().getProperty("ABI_REFERENCE").split(",")[1];
		if(foQuery.getSQLWhereClause().trim().equals(""))
		   lsr.add(lsWhere);
		else
		   lsr.add(" AND " + lsWhere);
		foQuery.addFilter(lsr);
		getParentApp().getSession().getORBSession().removeProperty("ABI_REFERENCE");
	}
	catch (RemoteException e)
	{
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", e);
	}
}
//END_EVENT_T1QRY_ADV_HRM_EVENT_beforeQuery}}
//{{EVENT_pQRY_ADV_HRM_EVENT_beforeGenerate
void pQRY_ADV_HRM_EVENT_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{
	//Write Event Code below this line

   VSRow loRow = T1QRY_ADV_HRM_EVENT.getCurrentRow();
   /*
    * When DIRECTION == OutBound, it means this is outgoing event originating in the application
    * itself; hence disable the link.
    * When DIRECTION == InBound, it means this is an incoming event originating from some
    * partner application; hence enable the link.
    */
   if( loRow == null ||
         AMSStringUtil.strEqual(CVL_DIRECTIONImpl.OUTBOUND, loRow.getData("DIRECTION").getString()))
   {
      ((AMSHyperlinkActionElement)getElementByName("T1QRY_ADV_HRM_EVENT_ViewSource")).setEnabled(false);
   }
   else
   {
      ((AMSHyperlinkActionElement)getElementByName("T1QRY_ADV_HRM_EVENT_ViewSource")).setEnabled(true);
   }


   /*
    * If Event has already completed successfully or has been scheduled for processing,
    * then we should not allow to change the Status.
    */
   if(   loRow != null &&  ( AMSStringUtil.strEqual(CVL_EVENT_STATUSImpl.SUCCESS,
         loRow.getData("EVENT_STATUS").getString()) ||
         AMSStringUtil.strEqual(CVL_EVENT_STATUSImpl.READY_FOR_PROCESSING,
               loRow.getData("EVENT_STATUS").getString()) )   )
   {
      getElementByName("txtT1EVENT_STATUS").getHtmlElement()
            .addAttribute("ams_readonly", "ams_readonly");
   }
   else
   {
      getElementByName("txtT1EVENT_STATUS").getHtmlElement()
            .removeAttribute("ams_readonly");
   }


   /*
    * If updates have happened, then we need to refresh the Data Source before generating
    * the page. This needs to be done explicitly since we insert the new row from
    * DataObject part and the DataSource is not aware about it.
    */
   if(mboolUpdating)
   {
      refreshDataSource(T1QRY_ADV_HRM_EVENT);
      mboolUpdating = false;
   }
}
//END_EVENT_pQRY_ADV_HRM_EVENT_beforeGenerate}}
//{{EVENT_pQRY_ADV_HRM_EVENT_beforeActionPerformed
void pQRY_ADV_HRM_EVENT_beforeActionPerformed( ActionElement foActnElem, PageEvent foEvent, PLSRequest preq )
{
	//Write Event Code below this line
   try
   {
      // If Refresh link is clicked, just update the page with latest data.
      if("T1QRY_ADV_HRM_EVENT_Refresh".equals(foActnElem.getName()))
      {
         refreshDataSource(T1QRY_ADV_HRM_EVENT);
      }
         /*
          * If the action is "relatedEvents" navigate to the Related Events
          * page.
          */
         if( RELATED_EVENTS_ACTION.equals( foActnElem.getName() ) )
         {
            VSRow loRow = T1QRY_ADV_HRM_EVENT.getCurrentRow();
            if( loRow != null )
            {
               String lsdirection = loRow.getData( "DIRECTION" ).getString();
               String lsEventId = loRow.getData( "EVENT_ID" ).getString();
               String lsCorrEventId = loRow.getData( "CORR_EVENT_ID" )
                     .getString();
               StringBuffer lsbWhere = new StringBuffer( 64 );
               lsbWhere.append( "DIRECTION " );
               lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection,
                     AMSSQLUtil.EQUALS_OPER ) );
               lsbWhere.append( " AND EVENT_ID " );
               lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId,
                     AMSSQLUtil.EQUALS_OPER ) );
               lsbWhere.append( " AND CORR_EVENT_ID " );
               lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsCorrEventId,
                     AMSSQLUtil.EQUALS_OPER ) );
               lsbWhere.append( " OR SOURCE_EVENT_ID " );
               lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId,
                     AMSSQLUtil.EQUALS_OPER ) );
               // Navigate to Related Events Page
               AMSDynamicTransition loDynTran = new AMSDynamicTransition(
                     "pRELATED_EVENTS", "", "ABI" );
               loDynTran.setSourcePage( this );
               pRELATED_EVENTS loTargetPage = (pRELATED_EVENTS)loDynTran
                     .getVSPage( getParentApp(), getSessionId() );
               loTargetPage.doNavigation( CVL_PARTNERSImpl.ADVHRM,
                     lsbWhere.toString(), lsdirection, lsEventId, "", "", "",
                     "" );
               foEvent.setNewPage( loTargetPage );
               foEvent.setCancel( true );
            }
         }
         /*
          * If the action is "serviceFlowss" navigate to the Flow Logs page.
          */
         if( SERVICE_FLOWS_ACTION.equals( foActnElem.getName() ) )
         {
            VSRow loRow = T1QRY_ADV_HRM_EVENT.getCurrentRow();
            if( loRow != null )
            {
               String lsdirection = loRow.getData( "DIRECTION" ).getString();
               String lsEventId = loRow.getData( "EVENT_ID" ).getString();
               StringBuffer lsbWhere = new StringBuffer( 64 );
               if( AMSStringUtil.strEqual( lsdirection,
                     CVL_DIRECTIONImpl.OUTBOUND ) )
               {
                  lsbWhere.append( "IN_EVENT_ID " );
                  lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId,
                        AMSSQLUtil.EQUALS_OPER ) );
                  lsbWhere.append( " AND IN_EVENT_PRTNR_ID" );
                  lsbWhere.append( AMSSQLUtil.getANSIQuotedStr(
                        CVL_PARTNER_IDImpl.ADVHRM, AMSSQLUtil.EQUALS_OPER ) );
               }
               else
               {
                  lsbWhere.append( "OUT_EVENT_ID " );
                  lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId,
                        AMSSQLUtil.EQUALS_OPER ) );
                  lsbWhere.append( " AND OUT_EVENT_PRTNR_ID" );
                  lsbWhere.append( AMSSQLUtil.getANSIQuotedStr(
                        CVL_PARTNER_IDImpl.ADVHRM, AMSSQLUtil.EQUALS_OPER ) );
               }
               AMSDynamicTransition loDynTran = new AMSDynamicTransition(
                     "pIM_SRVC_FLOW_All", lsbWhere.toString(), "ABI" );
               loDynTran.setSourcePage( this );
               pIM_SRVC_FLOW_All loTargetPage = (pIM_SRVC_FLOW_All)loDynTran
                     .getVSPage( getParentApp(), getSessionId() );
               foEvent.setNewPage( loTargetPage );
               foEvent.setCancel( true );
            }
         }
         /*
       * If action clicked is View Source, then it means that this is an incoming event
       * from partner application.
       *
       * Hence fetch the REFERENCE field and store in session.
       * REFERENCE field stores related source partner details in a CSV format.
       *
       * Based on the first value in the CSV, we can determine the source application.
       * And use this to transition to that partner's event page to view it's details.
       * beforeQuery() method of source page will use the session value stored.
       */
      if("T1QRY_ADV_HRM_EVENT_ViewSource".equals(foActnElem.getName()))
      {
         String lsReference = T1QRY_ADV_HRM_EVENT.getCurrentRow().getData("REFERENCE").getString();

         String lsPage = "";
         if(lsReference.split(",")[0].equals("BRASSRING"))
         {
            lsPage = "pQRY_BRASSRING_EVENT";
            getParentApp().getSession().getORBSession().setProperty("ABI_REFERENCE", lsReference);
         }
         else if(lsReference.split(",")[0].equals("MERIDIAN"))
         {
            lsPage = "pQRY_MERIDIAN_EVENT";
            getParentApp().getSession().getORBSession().setProperty("ABI_REFERENCE", lsReference);
         }
         else if(lsReference.split(",")[0].equals("NEOGOV"))
         {
            lsPage = "pQRY_NEOGOV_EVENT";
            getParentApp().getSession().getORBSession().setProperty("ABI_REFERENCE", lsReference);
         }

         AMSDynamicTransition loDynTran = new AMSDynamicTransition(lsPage, "", "ABI");
         loDynTran.setSourcePage(this);
         foEvent.setNewPage(loDynTran.getVSPage(getParentApp(), getSessionId()));
         foEvent.setCancel(true);
      }

      /*
       * If the Save action was clicked, it means, the update transaction has started.
       * Hence set the flag to true;
       */
      if("T1QRY_ADV_HRM_EVENTSaveAll".equals(foActnElem.getName()))
      {
         mboolUpdating = true;
      }
   }
   catch (VSException e)
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", e);

   }
   catch (RemoteException e)
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", e);

   }
}
//END_EVENT_pQRY_ADV_HRM_EVENT_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	T1QRY_ADV_HRM_EVENT.addDBListener(this);
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pQRY_ADV_HRM_EVENT_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			pQRY_ADV_HRM_EVENT_beforeActionPerformed( ae, evt, preq );
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1QRY_ADV_HRM_EVENT) {
			T1QRY_ADV_HRM_EVENT_beforeQuery(query , resultset );
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


}