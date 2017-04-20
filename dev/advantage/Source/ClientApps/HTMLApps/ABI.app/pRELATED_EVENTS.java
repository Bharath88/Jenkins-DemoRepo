// {{IMPORT_STMTS
package advantage.ABI;

import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;

import java.io.*;
import javax.swing.text.html.*;

import org.apache.commons.logging.Log;

import com.versata.util.logging.*;
// END_IMPORT_STMTS}}
import java.util.HashMap;

import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.vfc.html.AMSDataSource;
import com.amsinc.gems.adv.vfc.html.AMSPage;
import advantage.AMSStringUtil;
import advantage.CVL_DIRECTIONImpl;
import advantage.CVL_PARTNERSImpl;
import advantage.Advantage.DocPrintDialog;

/*
 * * pRELATED_EVENTS
 */
// {{FORM_CLASS_DECL
public class pRELATED_EVENTS extends pRELATED_EVENTSBase
// END_FORM_CLASS_DECL}}
{
   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( pRELATED_EVENTS.class,
         AMSLogConstants.FUNC_AREA_DFLT );

   private HashMap moMap = new HashMap();

   private boolean mboolFirst = true;

   private String msEvnetId;
   private String msDirection;
   private String msCurrentEventPartner;
   private String msEventStatus;
   private String msClientId;
   private String msTransactId;
   private String msPacketId;
   private String msMessageId;
   private AMSDataSource msCurrntDataSource;
   private static String QUERY_ACTION = "query";
   private static String CLEAR_QUERY_AACTION = "clearquery";
   private static String SELECT_PARTNER_ACTION = "selectPartner";
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
   // {{FORM_CLASS_CTOR
   public pRELATED_EVENTS( PLSApp parentApp ) throws VSException,
                                             java.beans.PropertyVetoException
   {
      super( parentApp );
      // END_FORM_CLASS_CTOR}}
      moMap.put( CVL_PARTNERSImpl.ADVFIN, T1ADV_FIN_EVENT );
      moMap.put( CVL_PARTNERSImpl.ADVHRM, T2ADV_HRM_EVENT );
      moMap.put( CVL_PARTNERSImpl.BRASSRING, T3BRASSRING_EVENT );
      moMap.put( CVL_PARTNERSImpl.INFOADVANTAGE, T4INFOADVANTAGE_EVENT );
      moMap.put( CVL_PARTNERSImpl.MAXIMO, T5MAXIMO_EVENT );
      moMap.put( CVL_PARTNERSImpl.MERIDIAN, T6MERIDIAN_EVENT );
      moMap.put( CVL_PARTNERSImpl.ADVPERMITTING, T14ADV_PERMITTING_EVENT );
      moMap.put( CVL_PARTNERSImpl.NEOGOV, T13NEOGOV_EVENT );
   }










   // {{EVENT_CODE
   // {{EVENT_pRELATED_EVENTS_beforeGenerate
   void pRELATED_EVENTS_beforeGenerate( HTMLDocumentModel docModel,
         VSOutParam cancel, VSOutParam output )
   {
      /*
       * Do not process this block if the page is loaded for the first time.
       * Since the scalar fields are not bound to any DataSource, the values in
       * fields disappear once an action is performed. So, set it explicitly
       * after any action is performed.
       */
      if( !mboolFirst )
      {
         setComboElement( "txtT1EVENT_PARTNER", msCurrentEventPartner );
         setComboElement( "txtT1DIRECTION", msDirection );
         setComboElement( "txtT1EVENT_STATUS", msEventStatus );
         setTextElement( "txtT1EVENT_ID", msEvnetId );
         setTextElement( "txtT1MESSAGEID", msMessageId );
         setTextElement( "txtT1CLIENT_ID", msClientId );
         setTextElement( "txtT1TRANSACT_ID", msTransactId );
         setTextElement( "txtT1PACKET_ID", msPacketId );
      }
      mboolFirst = false;
   }

   // END_EVENT_pRELATED_EVENTS_beforeGenerate}}
   // {{EVENT_pRELATED_EVENTS_beforeActionPerformed
   void pRELATED_EVENTS_beforeActionPerformed( ActionElement ae, PageEvent evt,
         PLSRequest preq )
   {
      msCurrentEventPartner = preq.getParameter( "txtT1EVENT_PARTNER" );
      msEvnetId = preq.getParameter( "txtT1EVENT_ID" );
      msDirection = preq.getParameter( "txtT1DIRECTION" );
      msEventStatus = preq.getParameter( "txtT1EVENT_STATUS" );
      msClientId = preq.getParameter( "txtT1CLIENT_ID" );
      msTransactId = preq.getParameter( "txtT1TRANSACT_ID" );
      msPacketId = preq.getParameter( "txtT1PACKET_ID" );
      msMessageId = preq.getParameter( "txtT1MESSAGEID" );
      if( ae.getName().equals( SELECT_PARTNER_ACTION ) )
      {
         setFields( msCurrentEventPartner );
      }
      if( ae.getAction().equals( QUERY_ACTION ) )
      {
         String lsCommonWhereClause = null;
	     if (AMSStringUtil.strIsEmpty(msCurrentEventPartner))
		 {
		    /*
			 * Throw an error if the search is performed without selecting an Event Partner.
			 */
			raiseException("Please select an Event Partner",
					AMSPage.SEVERITY_LEVEL_SEVERE);

		 }
         if( AMSStringUtil.strEqual( msCurrentEventPartner,
               CVL_PARTNERSImpl.ADVFIN )
               || AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.ADVHRM )
               || AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.INFOADVANTAGE ) )
         {
            /*
             * Throw an error if the fields Event Partner,Direction and Event Id
             * are not entered for Browse, else form a common where clause if
             * all the fields are entered.
             */
            if( AMSStringUtil.strIsEmpty( msDirection )
                  || AMSStringUtil.strIsEmpty( msEvnetId )
                  || AMSStringUtil.strIsEmpty( msCurrentEventPartner ) )
            {
               raiseException(
                     "Unable to perform the search with given criteria."
                           + "The fields Event Partner,Direction and Event Id are required for search.",
                     AMSPage.SEVERITY_LEVEL_SEVERE );
            }
            else
            {
               lsCommonWhereClause = generateCommonWhereClauseForTableName(
                     msEvnetId, msEventStatus, false );
            }
         }
         if( AMSStringUtil.strEqual( msCurrentEventPartner,
               CVL_PARTNERSImpl.BRASSRING )
               || AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.MERIDIAN )
               || AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.ADVPERMITTING )
               || AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.NEOGOV ) )
         {
            /*
             * Throw an error if the fields Event Partner,Direction,Client
             * Id,transact Id and Packet Id are not entered for Browse,, else
             * form a common where clause if all the fields are entered.
             */
            if( AMSStringUtil.strIsEmpty( msDirection )
                  || AMSStringUtil.strIsEmpty( msClientId )
                  || AMSStringUtil.strIsEmpty( msTransactId )
                  || AMSStringUtil.strIsEmpty( msPacketId )
                  || AMSStringUtil.strIsEmpty( msCurrentEventPartner ) )
            {
               raiseException(
                     "Unable to perform the search with given criteria."
                           + "The fields Event Partner,Direction,Client Id,Transact Id and Packet Id are required for search.",
                     AMSPage.SEVERITY_LEVEL_SEVERE );
            }
            else
            {
               lsCommonWhereClause = generateCommonWhereClauseForTableName(
                     msTransactId, msPacketId, msClientId, msEventStatus );
            }
         }
         if( AMSStringUtil.strEqual( msCurrentEventPartner,
               CVL_PARTNERSImpl.MAXIMO ) )
         {
            /*
             * Throw an error if the fields Event Partner,Direction and Message
             * Id are not entered for Browse,else form a common where clause if
             * all the fields are entered.
             */
            if( AMSStringUtil.strIsEmpty( msDirection )
                  || AMSStringUtil.strIsEmpty( msMessageId )
                  || AMSStringUtil.strIsEmpty( msCurrentEventPartner ) )
            {
               raiseException(
                     "Unable to perform the search with given criteria."
                           + "The fields Event Partner,Direction and Message Id are required for search.",
                     AMSPage.SEVERITY_LEVEL_SEVERE );
            }
            else
            {
               lsCommonWhereClause = generateCommonWhereClauseForTableName(
                     msMessageId, msEventStatus, true );
            }
         }
         // Process the Events for Current partner, entered in Browse.
         msCurrntDataSource = processCurrentEventStore( lsCommonWhereClause,
               msDirection, msCurrentEventPartner );
         // Process the Related Events for Current partner, i.e, if the
         // Direction is Outbound for the current Event process the
         // Related Inbound Events and Vice Versa.
         processRelatedEvents( msDirection );
      }
      if( ae.getAction().equals( CLEAR_QUERY_AACTION ) )
      {
         msCurrentEventPartner = "";
         msEventStatus = "";
         msDirection = "";
         msEvnetId = "";
         msClientId = "";
         msTransactId = "";
         msPacketId = "";
      }
   }

   // END_EVENT_pRELATED_EVENTS_beforeActionPerformed}}
   // {{EVENT_T10pQRY_INFOADVANTAGE_EVENT_beforePageNavigation
   void T10pQRY_INFOADVANTAGE_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T4INFOADVANTAGE_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsEventId = loRow.getData( "EVENT_ID" ).getString();
      String lsCorrEventId = loRow.getData( "CORR_EVENT_ID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsEventId ) && AMSStringUtil
            .strIsEmpty( lsdirection ) ) )
      {
         lsbWhere.append( "INFOADVANTAGE_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection, AMSSQLUtil.EQUALS_OPER ) );
         lsbWhere.append( " AND INFOADVANTAGE_EVENT.EVENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId, AMSSQLUtil.EQUALS_OPER ) );
         lsbWhere.append( " AND INFOADVANTAGE_EVENT.CORR_EVENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr(lsCorrEventId,AMSSQLUtil.EQUALS_OPER ) );
         nav.setDevWhere( lsbWhere.toString() );
      }
   }

   // END_EVENT_T10pQRY_INFOADVANTAGE_EVENT_beforePageNavigation}}
   // {{EVENT_T11pQRY_MAXIMO_EVENT_beforePageNavigation
   void T11pQRY_MAXIMO_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T5MAXIMO_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsMessageId = loRow.getData( "MESSAGEID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsdirection ) && AMSStringUtil
            .strIsEmpty( lsMessageId ) ) )
      {
         lsbWhere.append( "MAXIMO_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection,AMSSQLUtil.EQUALS_OPER   ) );
         lsbWhere.append( " AND MAXIMO_EVENT.MESSAGEID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsMessageId, AMSSQLUtil.EQUALS_OPER  ) );
         nav.setDevWhere( lsbWhere.toString() );
      }
   }

   // END_EVENT_T11pQRY_MAXIMO_EVENT_beforePageNavigation}}
   // {{EVENT_T12pQRY_MERIDIAN_EVENT_beforePageNavigation
   void T12pQRY_MERIDIAN_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T6MERIDIAN_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsClientId = loRow.getData( "CLIENT_ID" ).getString();
      String lsPacketId = loRow.getData( "PACKET_ID" ).getString();
      String lsTransactId = loRow.getData( "TRANSACT_ID" ).getString();
      String lsCorrClientId = loRow.getData( "CORR_CLIENT_ID" ).getString();
      String lsCorrPacketId = loRow.getData( "CORR_PACKET_ID" ).getString();
      String lsCorrTransactId = loRow.getData( "CORR_TRANSACT_ID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsdirection )
            && AMSStringUtil.strIsEmpty( lsClientId )
            && AMSStringUtil.strIsEmpty( lsTransactId )
            && AMSStringUtil.strIsEmpty( lsPacketId )
            && AMSStringUtil.strIsEmpty( lsCorrClientId )
            && AMSStringUtil.strIsEmpty( lsCorrTransactId ) && AMSStringUtil
            .strIsEmpty( lsCorrPacketId ) ) )
      {
         lsbWhere.append( "MERIDIAN_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND MERIDIAN_EVENT.CLIENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsClientId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND MERIDIAN_EVENT.TRANSACT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsTransactId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND MERIDIAN_EVENT.PACKET_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsPacketId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND MERIDIAN_EVENT.CORR_CLIENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsCorrClientId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND MERIDIAN_EVENT.CORR_TRANSACT_ID " );
         lsbWhere
               .append( AMSSQLUtil.getANSIQuotedStr( lsCorrTransactId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND MERIDIAN_EVENT.CORR_PACKET_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsCorrPacketId, AMSSQLUtil.EQUALS_OPER  ) );
         nav.setDevWhere( lsbWhere.toString() );
      }
   }

   // END_EVENT_T12pQRY_MERIDIAN_EVENT_beforePageNavigation}}
   // {{EVENT_T15pQRY_ADV_PERMITTING_EVENT_beforePageNavigation
   void T15pQRY_ADV_PERMITTING_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T14ADV_PERMITTING_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsClientId = loRow.getData( "CLIENT_ID" ).getString();
      String lsPacketId = loRow.getData( "PACKET_ID" ).getString();
      String lsTransactId = loRow.getData( "TRANSACT_ID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsdirection )
            && AMSStringUtil.strIsEmpty( lsClientId )
            && AMSStringUtil.strIsEmpty( lsTransactId ) && AMSStringUtil
            .strIsEmpty( lsPacketId ) ) )
      {
         lsbWhere.append( "ADV_PERMITTING_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND ADV_PERMITTING_EVENT.CLIENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsClientId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND ADV_PERMITTING_EVENT.TRANSACT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsTransactId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND ADV_PERMITTING_EVENT.PACKET_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsPacketId, AMSSQLUtil.EQUALS_OPER  ) );
         
         nav.setDevWhere( lsbWhere.toString() );
      }
   }

   // END_EVENT_T15pQRY_ADV_PERMITTING_EVENT_beforePageNavigation}}
   // {{EVENT_T7pQRY_ADV_FIN_EVENT_beforePageNavigation
   void T7pQRY_ADV_FIN_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T1ADV_FIN_EVENT.getCurrentRow();
      if( loRow != null )
      {
         String lsdirection = loRow.getData( "DIRECTION" ).getString();
         String lsEventId = loRow.getData( "EVENT_ID" ).getString();
         String lsCorrEventId = loRow.getData( "CORR_EVENT_ID" ).getString();
         StringBuffer lsbWhere = new StringBuffer( 64 );
         if( !( AMSStringUtil.strIsEmpty( lsdirection )
               && AMSStringUtil.strIsEmpty( lsEventId ) && AMSStringUtil
               .strIsEmpty( lsdirection ) ) )
         {
            lsbWhere.append( "ADV_FIN_EVENT.DIRECTION " );
            lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection, AMSSQLUtil.EQUALS_OPER  ) );
            lsbWhere.append( " AND ADV_FIN_EVENT.EVENT_ID " );
            lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId, AMSSQLUtil.EQUALS_OPER  ) );
            lsbWhere.append( " AND ADV_FIN_EVENT.CORR_EVENT_ID " );
            lsbWhere
                  .append( AMSSQLUtil.getANSIQuotedStr( lsCorrEventId, AMSSQLUtil.EQUALS_OPER  ) );
            nav.setDevWhere( lsbWhere.toString() );
         }
      }
   }

   // END_EVENT_T7pQRY_ADV_FIN_EVENT_beforePageNavigation}}
   // {{EVENT_T8pQRY_ADV_HRM_EVENT_beforePageNavigation
   void T8pQRY_ADV_HRM_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T2ADV_HRM_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsEventId = loRow.getData( "EVENT_ID" ).getString();
      String lsCorrEventId = loRow.getData( "CORR_EVENT_ID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsdirection )
            && AMSStringUtil.strIsEmpty( lsEventId ) && AMSStringUtil
            .strIsEmpty( lsdirection ) ) )
      {
         lsbWhere.append( "ADV_HRM_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND ADV_HRM_EVENT.EVENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsEventId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND ADV_HRM_EVENT.CORR_EVENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsCorrEventId, AMSSQLUtil.EQUALS_OPER  ) );
         nav.setDevWhere( lsbWhere.toString() );
      }
   }

   // END_EVENT_T8pQRY_ADV_HRM_EVENT_beforePageNavigation}}
   // {{EVENT_T9pQRY_BRASSRING_EVENT_beforePageNavigation
   void T9pQRY_BRASSRING_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T3BRASSRING_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsClientId = loRow.getData( "CLIENT_ID" ).getString();
      String lsPacketId = loRow.getData( "PACKET_ID" ).getString();
      String lsTransactId = loRow.getData( "TRANSACT_ID" ).getString();
      String lsCorrClientId = loRow.getData( "CORR_CLIENT_ID" ).getString();
      String lsCorrPacketId = loRow.getData( "CORR_PACKET_ID" ).getString();
      String lsCorrTransactId = loRow.getData( "CORR_TRANSACT_ID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsdirection )
            && AMSStringUtil.strIsEmpty( lsClientId )
            && AMSStringUtil.strIsEmpty( lsTransactId )
            && AMSStringUtil.strIsEmpty( lsPacketId )
            && AMSStringUtil.strIsEmpty( lsCorrClientId )
            && AMSStringUtil.strIsEmpty( lsCorrTransactId ) && AMSStringUtil
            .strIsEmpty( lsCorrPacketId ) ) )
      {
         lsbWhere.append( "BRASSRING_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND BRASSRING_EVENT.CLIENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsClientId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND BRASSRING_EVENT.TRANSACT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsTransactId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND BRASSRING_EVENT.PACKET_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsPacketId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND BRASSRING_EVENT.CORR_CLIENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsCorrClientId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND BRASSRING_EVENT.CORR_TRANSACT_ID " );
         lsbWhere
               .append( AMSSQLUtil.getANSIQuotedStr( lsCorrTransactId, AMSSQLUtil.EQUALS_OPER  ) );
         lsbWhere.append( " AND BRASSRING_EVENT.CORR_PACKET_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsCorrPacketId, AMSSQLUtil.EQUALS_OPER  ) );
         nav.setDevWhere( lsbWhere.toString() );
      }
   }

   // END_EVENT_T9pQRY_BRASSRING_EVENT_beforePageNavigation}}
   // {{EVENT_T16pQRY_NEOGOV_EVENT_beforePageNavigation
   void T16pQRY_NEOGOV_EVENT_beforePageNavigation( PageNavigation nav,
         VSOutParam cancel, VSOutParam newPage )
   {
      VSRow loRow = T13NEOGOV_EVENT.getCurrentRow();
      String lsdirection = loRow.getData( "DIRECTION" ).getString();
      String lsClientId = loRow.getData( "CLIENT_ID" ).getString();
      String lsPacketId = loRow.getData( "PACKET_ID" ).getString();
      String lsTransactId = loRow.getData( "TRANSACT_ID" ).getString();
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( !( AMSStringUtil.strIsEmpty( lsdirection )
            && AMSStringUtil.strIsEmpty( lsClientId )
            && AMSStringUtil.strIsEmpty( lsTransactId ) && AMSStringUtil
            .strIsEmpty( lsPacketId ) ) )
      {
         lsbWhere.append( "NEOGOV_EVENT.DIRECTION " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsdirection,
               AMSSQLUtil.EQUALS_OPER ) );
         lsbWhere.append( " AND NEOGOV_EVENT.CLIENT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsClientId,
               AMSSQLUtil.EQUALS_OPER ) );
         lsbWhere.append( " AND NEOGOV_EVENT.TRANSACT_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsTransactId,
               AMSSQLUtil.EQUALS_OPER ) );
         lsbWhere.append( " AND NEOGOV_EVENT.PACKET_ID " );
         lsbWhere.append( AMSSQLUtil.getANSIQuotedStr( lsPacketId,
               AMSSQLUtil.EQUALS_OPER ) );
         nav.setDevWhere( lsbWhere.toString() );
      }
   }
   // END_EVENT_T16pQRY_NEOGOV_EVENT_beforePageNavigation}}
   // END_EVENT_CODE}}
   public void addListeners()
   {
      // {{EVENT_ADD_LISTENERS
      addPageListener( this );
      T1ADV_FIN_EVENT.addDBListener( this );
      T10pQRY_INFOADVANTAGE_EVENT.addPageNavigationListener( this );
      T11pQRY_MAXIMO_EVENT.addPageNavigationListener( this );
      T12pQRY_MERIDIAN_EVENT.addPageNavigationListener( this );
      T15pQRY_ADV_PERMITTING_EVENT.addPageNavigationListener( this );
      T7pQRY_ADV_FIN_EVENT.addPageNavigationListener( this );
      T8pQRY_ADV_HRM_EVENT.addPageNavigationListener( this );
      T9pQRY_BRASSRING_EVENT.addPageNavigationListener( this );
      T16pQRY_NEOGOV_EVENT.addPageNavigationListener( this );
      // END_EVENT_ADD_LISTENERS}}
   }

   // {{EVENT_ADAPTER_CODE
   public void beforeGenerate( VSPage obj, HTMLDocumentModel docModel,
         VSOutParam cancel, VSOutParam output )
   {
      Object source = obj;
      if( source == this )
      {
         pRELATED_EVENTS_beforeGenerate( docModel, cancel, output );
      }
   }

   public void beforeActionPerformed( VSPage obj, ActionElement ae,
         PageEvent evt, PLSRequest preq )
   {
      Object source = obj;
      if( source == this )
      {
         pRELATED_EVENTS_beforeActionPerformed( ae, evt, preq );
      }
   }

   public void beforePageNavigation( PageNavigation obj, VSOutParam cancel,
         VSOutParam newPage )
   {
      Object source = obj;
      if( source == T10pQRY_INFOADVANTAGE_EVENT )
      {
         T10pQRY_INFOADVANTAGE_EVENT_beforePageNavigation( obj, cancel, newPage );
      }
      if( source == T11pQRY_MAXIMO_EVENT )
      {
         T11pQRY_MAXIMO_EVENT_beforePageNavigation( obj, cancel, newPage );
      }
      if( source == T12pQRY_MERIDIAN_EVENT )
      {
         T12pQRY_MERIDIAN_EVENT_beforePageNavigation( obj, cancel, newPage );
      }
      if( source == T15pQRY_ADV_PERMITTING_EVENT )
      {
         T15pQRY_ADV_PERMITTING_EVENT_beforePageNavigation( obj, cancel,
               newPage );
      }
      if( source == T7pQRY_ADV_FIN_EVENT )
      {
         T7pQRY_ADV_FIN_EVENT_beforePageNavigation( obj, cancel, newPage );
      }
      if( source == T8pQRY_ADV_HRM_EVENT )
      {
         T8pQRY_ADV_HRM_EVENT_beforePageNavigation( obj, cancel, newPage );
      }
      if( source == T9pQRY_BRASSRING_EVENT )
      {
         T9pQRY_BRASSRING_EVENT_beforePageNavigation( obj, cancel, newPage );
      }
      if( source == T16pQRY_NEOGOV_EVENT )
      {
         T16pQRY_NEOGOV_EVENT_beforePageNavigation( obj, cancel, newPage );
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
    * This method makes the Partner specific fields visible i.e, if the Partner
    * is ADVFIN,ADVHRM or INFOADVANTAGE Event Id field is made visible. if the
    * Partner is MAXIMO, Message Id field is made visible. if the Partner is
    * BRASSRING,MERIDIAN,ADVPERMITTING or NEOGOV Client Id,Transact Id and
    * Packet id is made visible.
    *
    * 
    * @param foSession
    *           - The Current Session
    * @param fsPartner
    *           - The partner for which fields should be made visible
    */
   private void setFields( String fsPartner )
   {
      DivElement divElementEventId = (DivElement)getElementByName( "EventId" );
      DivElement divElementInputEventId = (DivElement)getElementByName( "inputEventId" );
      DivElement divElementMessageId = (DivElement)getElementByName( "MessageId" );
      DivElement divElementInputMessageId = (DivElement)getElementByName( "InputMessageId" );
      DivElement divElementClientId = (DivElement)getElementByName( "ClientId" );
      DivElement divElementInputClientId = (DivElement)getElementByName( "inputClientId" );
      DivElement divElementTransactId = (DivElement)getElementByName( "TransactId" );
      DivElement divElementInputTransactId = (DivElement)getElementByName( "inputTransactId" );
      DivElement divElementPacketId = (DivElement)getElementByName( "PacketId" );
      DivElement divElementInputPacketId = (DivElement)getElementByName( "inputPacketId" );
      // Set the visibility to false by default for all the fields.
      divElementEventId.setVisible( false );
      divElementInputEventId.setVisible( false );
      divElementMessageId.setVisible( false );
      divElementInputMessageId.setVisible( false );
      divElementClientId.setVisible( false );
      divElementInputClientId.setVisible( false );
      divElementTransactId.setVisible( false );
      divElementInputTransactId.setVisible( false );
      divElementPacketId.setVisible( false );
      divElementInputPacketId.setVisible( false );
      if( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.ADVFIN )
            || AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.ADVHRM )
            || AMSStringUtil.strEqual( fsPartner,
                  CVL_PARTNERSImpl.INFOADVANTAGE ) )
      {
         divElementEventId.setVisible( true );
         divElementInputEventId.setVisible( true );
      }
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.MAXIMO ) ) )
      {
         divElementMessageId.setVisible( true );
         divElementInputMessageId.setVisible( true );
      }
      if( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.BRASSRING )
            || AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.MERIDIAN )
            || AMSStringUtil.strEqual( fsPartner,
                  CVL_PARTNERSImpl.ADVPERMITTING )
            || AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.NEOGOV ) )
      {
         divElementClientId.setVisible( true );
         divElementInputClientId.setVisible( true );
         divElementTransactId.setVisible( true );
         divElementInputTransactId.setVisible( true );
         divElementPacketId.setVisible( true );
         divElementInputPacketId.setVisible( true );
      }
   }

   /**
    * This method returns the table name for Current partner
    * 
    * @param fsPartner
    *           - The current partner
    * @return String which is table name for Current partner
    */
   private static String getTableName( String fsPartner )
   {
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.ADVFIN ) ) )
      {
         return "ADV_FIN_EVENT";
      }
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.ADVHRM ) ) )
      {
         return "ADV_HRM_EVENT";
      }
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.BRASSRING ) ) )
      {
         return "BRASSRING_EVENT";
      }
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.INFOADVANTAGE ) ) )
      {
         return "INFOADVANTAGE_EVENT";
      }
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.MAXIMO ) ) )
      {
         return "MAXIMO_EVENT";
      }
      if( ( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.MERIDIAN ) ) )
      {
         return "MERIDIAN_EVENT";
      }
      if( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.ADVPERMITTING ) )
      {
         return "ADV_PERMITTING_EVENT ";
      }
      if( AMSStringUtil.strEqual( fsPartner, CVL_PARTNERSImpl.NEOGOV ) )
      {
         return "NEOGOV_EVENT ";
      }
      return null;
   }

   /**
    * This method creates a where clause for the Partner
    * ADVFIN,ADVHRM,INFOADVANTAGE and MAXIMO.
    * 
    * @param fsEvent
    *           - Event Id if the partner is ADVFIN,ADVHRM or INFOADVANTAGE.
    *           Message Id if the partner is MAXIMO
    * @param fboolisMaximo
    *           - true if the partner is MAXIMO and false if the partner is
    *           ADVFIN,ADVHRM or INFOADVANTAGE
    * @return String, which is a where clause.
    */
   private static String generateCommonWhereClauseForTableName( String fsEvent,
         String fsEventStatus, boolean fboolisMaximo )
   {
      if( !fboolisMaximo )
      {
         if( AMSStringUtil.strEqual( fsEventStatus, "" ) )
         {
            return "EVENT_ID = " + fsEvent + " OR SOURCE_EVENT_ID = " + fsEvent;
         }
         else
         {
            return "EVENT_ID = " + fsEvent + " AND EVENT_STATUS = '"
                  + fsEventStatus + "' OR SOURCE_EVENT_ID = " + fsEvent;
         }
      }
      else
      {
         if( AMSStringUtil.strEqual( fsEventStatus, "" ) )
         {
            return "MESSAGEID = " + fsEvent + " OR SOURCE_MESSAGEID = "
                  + fsEvent;
         }
         {
            return "MESSAGEID = " + fsEvent + " AND EVENT_STATUS = '"
                  + fsEventStatus + "' OR SOURCE_MESSAGEID = " + fsEvent;
         }
      }
   }

   /**
    * This method creates a where clause for the Partner
    * BRASSRING,MERIDIAN,ADVPERMITTING and NEOGOV.
    * 
    * @param fsTransactID
    *           - Transact id
    * @param fsPacketId
    *           - Packet Id
    * @param fsClientId
    *           - Client Id
    * @return String, which is a where clause.
    */
   private static String generateCommonWhereClauseForTableName(
         String fsTransactID, String fsPacketId, String fsClientId,
         String fsEventStatus )
   {
      StringBuffer lsbWhere = new StringBuffer( 64 );
      if( AMSStringUtil.strEqual( fsEventStatus, "" ) )
      {
         return "CLIENT_ID = " + "'" + fsClientId + "'" + " AND TRANSACT_ID = "
               + "'" + fsTransactID + "'" + " AND PACKET_ID = " + "'"
               + fsPacketId + "'" + " OR SOURCE_TRANSACT_ID = " + "'"
               + fsTransactID + "'";
      }
      else
      {
         return "CLIENT_ID = " + "'" + fsClientId + "'" + " AND TRANSACT_ID = "
               + "'" + fsTransactID + "'" + " AND PACKET_ID = " + "'"
               + fsPacketId + "'" + " AND EVENT_STATUS = " + "'"
               + fsEventStatus + "'" + " OR SOURCE_TRANSACT_ID = " + "'"
               + fsTransactID + "'";
      }
   }

   /**
    * This method creates a where clause for the Partners having Reference field
    * as "Partner,Client Id,Transact Id,Packet Id" where Partner can be
    * BRASSRING,MERIDIAN,ADVPERMITTING or NEOGOV.
    * 
    * @param fsPartner
    *           - The selected Partner
    * @param fsTransactID
    *           - Transact id
    * @param fsPacketId
    *           - Packet Id
    * @param fsClientId
    *           - Client Id
    * @return String, which is a where clause.
    */
   private static String generateCommonWhereClauseForRelatedTableName(
         String fsPartner, String fsTransactID, String fsPacketId,
         String fsClientId )
   {
      return "REFERENCE = " + "'" + fsPartner + "," + fsClientId + ","
            + fsTransactID + "," + fsPacketId + "'";
   }

   /**
    * This method creates a where clause for the Partners having Reference field
    * as "Partner,Event Id" where Partner can be ADVFIN,ADVHRM or INFOADVANTAGE
    * or "Partner,Message Id" where Partner is MAXIMOz
    *
    * @param fsPartner
    *           - The selected Partner
    * @param fsEvent
    *           - Event Id or Message Id
    * @return String, which is a where clause.
    */
   private static String generateCommonWhereClauseForRelatedTableName(
         String fsPartner, String fsEvent )
   {
      return "REFERENCE = " + "'" + fsPartner + "," + fsEvent + "'";
   }

   /**
    * Process the Events for Current DataSource
    *
    * @param fsCommonWhereClause
    *           - Common where clause to appended for the Current partner
    * @param fsDirection
    *           - Either Inbound or Outbound
    * @param lsEventPartner
    *           - The current partner to be processed
    * @return
    */
   private AMSDataSource processCurrentEventStore( String fsCommonWhereClause,
         String fsDirection, String lsEventPartner )
   {
      try
      {
         AMSDataSource lsCurrntDataSource = (AMSDataSource)moMap
               .get( lsEventPartner );
         String lsPartnerTable = getTableName( lsEventPartner );
         StringBuffer loOutWhereClause = new StringBuffer( 64 );
         loOutWhereClause.append( "DIRECTION " );
         loOutWhereClause.append( AMSSQLUtil.getANSIQuotedStr( fsDirection,
               AMSSQLUtil.EQUALS_OPER  ) );
         loOutWhereClause.append( " AND " );
         loOutWhereClause.append( "(" );
         loOutWhereClause.append( fsCommonWhereClause );
         loOutWhereClause.append( ")" );
         lsCurrntDataSource.setQueryInfo( lsPartnerTable,
               loOutWhereClause.toString(), "", "" );
         lsCurrntDataSource.clearQBFData();
         lsCurrntDataSource.executeQuery();
         return lsCurrntDataSource;
      } catch( Exception loExp )
      {
         moAMSLog
               .error( "The values entered in one or more fields are incorrect.Please enter the correct value" );
         moAMSLog.error( loExp.getMessage() );
         return null;
      }
   }

   /**
    * Process the Related Events for Current partner selected
    *
    * @param fsDirection
    *           - If the Direction is Outbound process the Related Inbound
    *           Events, If the Direction is Inbound process the Related Outbound
    *           Events.
    */
   private void processRelatedEvents( String fsDirection )
   {
      String lsCommonWhereClause = null;
      // Loop through the hashmap for the event store.
      for( int liPartner = 0; liPartner < moMap.size(); liPartner++ )
      {
         String lsPartner = (String)moMap.keySet().toArray()[liPartner];
         AMSDataSource loDataSource = (AMSDataSource)moMap.values().toArray()[liPartner];
         // Process the related events only if the Partner is not the current
         if( !( AMSStringUtil.strEqual( lsPartner, msCurrentEventPartner ) ) )
         {
            String lsPartnerTable = getTableName( lsPartner );
            /*
             * If Direction is Outbound get the Related Inbound Events and
             * process them
             */
            if( AMSStringUtil
                  .strEqual( fsDirection, CVL_DIRECTIONImpl.OUTBOUND ) )
            {
               /*
                * Call appropriate generateCommonWhereClauseForRelatedTableName
                * for the appropriate Partner event store.
                */
               if( AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.ADVFIN )
                     || AMSStringUtil.strEqual( msCurrentEventPartner,
                           CVL_PARTNERSImpl.ADVHRM )
                     || AMSStringUtil.strEqual( msCurrentEventPartner,
                           CVL_PARTNERSImpl.INFOADVANTAGE ) )
               {
                  lsCommonWhereClause = generateCommonWhereClauseForRelatedTableName(
                        msCurrentEventPartner, msEvnetId );
               }
               if( AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.MAXIMO ) )
               {
                  lsCommonWhereClause = generateCommonWhereClauseForRelatedTableName(
                        msCurrentEventPartner, msMessageId );
               }
               if( AMSStringUtil.strEqual( msCurrentEventPartner,
                     CVL_PARTNERSImpl.BRASSRING )
                     || AMSStringUtil.strEqual( msCurrentEventPartner,
                           CVL_PARTNERSImpl.MERIDIAN )
                     || AMSStringUtil.strEqual( msCurrentEventPartner,
                           CVL_PARTNERSImpl.ADVPERMITTING )
                     || AMSStringUtil.strEqual( msCurrentEventPartner,
                           CVL_PARTNERSImpl.NEOGOV ) )
               {
                  lsCommonWhereClause = generateCommonWhereClauseForRelatedTableName(
                        msCurrentEventPartner, msTransactId, msPacketId,
                        msClientId );
               }
               // Call processRelateEventStore to process the Related Inbound
               // Events.
               processRelateEventStore( lsCommonWhereClause, lsPartnerTable,
                     loDataSource );
            }
            /*
             * If Direction is Inbound get the related Outbound Events
             * andprocess them
             */
            else
            {
               // Get the reference for Current row of the Current Partner
               String lsReference = msCurrntDataSource.getCurrentRow()
                     .getData( "REFERENCE" ).getString().trim();
               // Split the refernce field and get the first value, which is the
               // partner name
               String lsRefPartner = lsReference.split( "," )[0];
               // Get the Related Outbound events only if the first value in
               // Refernce field mathces the selected partner
               if( AMSStringUtil.strEqual( lsPartner, lsRefPartner ) )
               {
                  if( ( AMSStringUtil.strEqual( lsRefPartner,
                        CVL_PARTNERSImpl.ADVFIN )
                        || AMSStringUtil.strEqual( lsRefPartner,
                              CVL_PARTNERSImpl.ADVHRM ) || AMSStringUtil
                        .strEqual( lsRefPartner, CVL_PARTNERSImpl.INFOADVANTAGE ) ) )
                  {
                     // Get the second value in the Reference field which is the
                     // Event Id.
                     String lsEventId = lsReference.split( "," )[1].toString();
                     String lsEventStatus = lsCommonWhereClause = generateCommonWhereClauseForTableName(
                           lsEventId, "", false );
                  }
                  if( ( AMSStringUtil.strEqual( lsRefPartner,
                        CVL_PARTNERSImpl.MAXIMO ) ) )
                  {
                     // Get the second value in the Reference field which is the
                     // Message Id.
                     String lsMessageId = lsReference.split( "," )[1]
                           .toString();
                     lsCommonWhereClause = generateCommonWhereClauseForTableName(
                           lsMessageId, "", true );
                  }
                  if( ( AMSStringUtil.strEqual( lsRefPartner,
                        CVL_PARTNERSImpl.BRASSRING )
                        || AMSStringUtil.strEqual( lsRefPartner,
                              CVL_PARTNERSImpl.MERIDIAN )
                        || AMSStringUtil.strEqual( lsRefPartner,
                              CVL_PARTNERSImpl.ADVPERMITTING ) || AMSStringUtil
                        .strEqual( lsRefPartner, CVL_PARTNERSImpl.NEOGOV ) ) )
                  {
                     // Get the second value in the Reference field which is the
                     // Client Id.
                     String lsClientId = lsReference.split( "," )[1].toString();
                     // Get the third value in the Reference field which is the
                     // Transact Id.
                     String lsTransactId = lsReference.split( "," )[2]
                           .toString();
                     // Get the fourth value in the Reference field which is the
                     // Packet Id.
                     String lsPacketId = lsReference.split( "," )[3].toString();
                     lsCommonWhereClause = generateCommonWhereClauseForTableName(
                           lsTransactId, lsPacketId, lsClientId, "" );
                  }
               }
               // If the first value in refernce field do not mathch the
               // selected
               // partner, get blank records
               if( !( AMSStringUtil.strEqual( lsPartner, lsRefPartner ) ) )
               {
                  if( ( AMSStringUtil.strEqual( lsPartner,
                        CVL_PARTNERSImpl.ADVFIN )
                        || AMSStringUtil.strEqual( lsPartner,
                              CVL_PARTNERSImpl.ADVHRM ) || AMSStringUtil
                        .strEqual( lsPartner, CVL_PARTNERSImpl.INFOADVANTAGE ) ) )
                  {
                     // Set null to primary key field which is the Event Id, to
                     // get blank records.
                     lsCommonWhereClause = generateCommonWhereClauseForTableName(
                           null, "", false );
                  }
                  if( ( AMSStringUtil.strEqual( lsPartner,
                        CVL_PARTNERSImpl.MAXIMO ) ) )
                  {
                     // Set null to primary key field which is the Message Id,
                     // to get blank records.
                     lsCommonWhereClause = generateCommonWhereClauseForTableName(
                           null, "", true );
                  }
                  if( ( AMSStringUtil.strEqual( lsPartner,
                        CVL_PARTNERSImpl.BRASSRING )
                        || AMSStringUtil.strEqual( lsPartner,
                              CVL_PARTNERSImpl.MERIDIAN )
                        || AMSStringUtil.strEqual( lsPartner,
                              CVL_PARTNERSImpl.ADVPERMITTING ) || AMSStringUtil
                        .strEqual( lsPartner, CVL_PARTNERSImpl.NEOGOV ) ) )
                  {
                     // Set null to primary key fields which is the Client
                     // Id,Transact Id and Packet Id to get blank records.
                     lsCommonWhereClause = generateCommonWhereClauseForTableName(
                           null, null, null, "" );
                  }
               }
               // Call processCurrentEventStore to process the Related Outbound
               // Events.
               processCurrentEventStore( lsCommonWhereClause,
                     CVL_DIRECTIONImpl.OUTBOUND, lsPartner );
            }
         }
      }
   }

   /**
    * Process the Related Events for current Partner.
    *
    * @param fsCommonWhereClause
    *           - Common Where clause
    * @param fsPartnerTable
    *           - Partner for which Related Events should be processed.
    * @param foDataSource
    */
   private static void processRelateEventStore( String fsCommonWhereClause,
         String fsPartnerTable, AMSDataSource foDataSource )
   {
      try
      {
         foDataSource.clearQBFData();
         foDataSource
               .setQueryInfo( fsPartnerTable, fsCommonWhereClause, "", "" );
         foDataSource.executeQuery();
      } catch( Exception loExp )
      {
         moAMSLog
               .error( "The values entered in one or more fields are incorrect.Please enter the correct value" );
         moAMSLog.error( loExp.getMessage() );
      }
   }

   /**
    * Utility method to set Combobox
    *
    * @param fsElementName
    *           - Selected HTML element
    * @param fsValue
    *           - Value to be set to HTML element
    */
   private void setComboElement( String fsElementName, String fsValue )
   {
      // Set the Event Status
      ComboBoxElement loComboElement = (ComboBoxElement)getElementByName( fsElementName );
      if( !AMSStringUtil.strIsEmpty( fsValue ) )
      {

         int liIndex = DocPrintDialog.getIndexOfStoredValue( loComboElement,
               fsValue );
         loComboElement
               .setSelectedItem( loComboElement.getElementAt( liIndex ) );
      }
      else
      {
         loComboElement.setSelectedText( "" );
      }
   }

   /**
    * Utility method to set TextElement
    *
    * @param fsElementName
    *           - Selected HTML element.
    * @param fsValue
    *           - Value to be set to HTML element
    */
   private void setTextElement( String fsElementName, String fsValue )
   {
      TextElement loTextElement = (TextElement)getElementByName( fsElementName );
      if( !AMSStringUtil.strIsEmpty( fsValue ) )
      {
         loTextElement.setText( fsValue );
      }
      else
      {
         loTextElement.setText( "" );
      }
   }

   /**
    * Method to process the Related Events for a Current Selection when a
    * transition happens from partner Event Detail pages to current Page.
    *
    * @param fspartner
    *           - The Partner name which indicates which indicates the Source
    *           page.
    * @param fsWhereClause
    *           - The Where clause being set.
    * @param fsDirection
    *           - The Direction
    * @param fsEventId
    *           - The Event Id
    * @param fsMessageId
    *           - The Message Id
    * @param fsClientId
    *           - The client Id
    * @param fsTransactId
    *           - The Transact Id
    * @param fsPacketId
    *           - The packet Id
    */
   public void doNavigation( String fspartner, String fsWhereClause,
         String fsDirection, String fsEventId, String fsMessageId,
         String fsClientId, String fsTransactId, String fsPacketId )
   {
      AMSDataSource lsCurrntDataSource = (AMSDataSource)moMap.get( fspartner );
      msCurrntDataSource = lsCurrntDataSource;
      msCurrentEventPartner = fspartner;
      msEvnetId = fsEventId;
      msMessageId = fsMessageId;
      msClientId = fsClientId;
      msPacketId = fsPacketId;
      msTransactId = fsTransactId;
      // Call processCurrentEventStore to process the Events for Selected Source
      // Partner
      processCurrentEventStore( fsWhereClause, fsDirection, fspartner );
      // Call processRelatedEvents to process the Related vents for Selected
      // Source Partner
      processRelatedEvents( fsDirection );
   }
}
