//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.AMSStringUtil;
import com.amsinc.gems.adv.vfc.html.* ;
import com.amsinc.gems.adv.common.* ;
import java.rmi.RemoteException;
import advantage.AMSMsgUtil;
import advantage.AMSDataObject;
import versata.vls.*;

import org.apache.commons.logging.Log;

/*
**  pAutoDocCreateInfo
*/

//{{FORM_CLASS_DECL
public class pAutoDocCreateInfo extends pAutoDocCreateInfoBase

//END_FORM_CLASS_DECL}}
{

   private DataSource                moSrcDataSrc  = null ;
   private AMSHyperlinkActionElement moSrcActnElem = null ;

   // Member variables indicating the source page
   boolean mbIsSrcInvoice = false;
   boolean mbIsSrcStndAlone = false;
   private static Log moAMSLog = AMSLogger.getLog( pAutoDocCreateInfo.class,
         AMSLogConstants.FUNC_AREA_DFLT ) ;
   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pAutoDocCreateInfo ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      setAllowHistory( false ) ;
      setDocNavPanelInd( DOC_MULTI_TBL_NAV_PANEL_IGNORE ) ;
   }


//{{EVENT_CODE
//{{EVENT_T1IN_DOC_CREA_ACTN_beforeQuery
void T1IN_DOC_CREA_ACTN_beforeQuery(VSQuery query ,VSOutParam resultset )
{
   VSORBSession loVSORBSession = parentApp.getSession().getORBSession();
   SearchRequest lsr = new SearchRequest();
   String lsSourcePage = getSourcePage().getName();
   VSRow loPoDocHdr = null;
   String lsDocTyp = "IN";
   String lsDfltIN = "IN";
   String lsDfltIS = "IS";

   //This Code Should only get executed if the Source Page
   //is pInvoicingSearch_SelectLines.
   if (!AMSStringUtil.strIsEmpty(lsSourcePage)
           && (lsSourcePage.indexOf("pInvoicingSearch_SelectLines") != -1))
   {
      mbIsSrcInvoice = true;
   }
   if(!AMSStringUtil.strIsEmpty(lsSourcePage)
              && (lsSourcePage.indexOf("pInvoicingSearch_SelectPO") != -1))
   {
      mbIsSrcStndAlone = true;
   }

   try
   {
      //This Code Should only get executed if the Source Page
      //is pInvoicingSearch_SelectLines.
      if (mbIsSrcInvoice)
      {
         // Filter for all records with DOC_TYP = 'IN' DOC_SUBTYP = 'IN'
         lsr.add("AND DOC_CD IN ( SELECT DOC_CD FROM "+ getSchema(this,"R_GEN_DOC_CTRL")
            + ".R_GEN_DOC_CTRL WHERE DOC_TYP  "+ AMSSQLUtil.getANSIQuotedStr(lsDocTyp, AMSSQLUtil.EQUALS_OPER)
            + "AND DOC_SUBTYP "+ AMSSQLUtil.getANSIQuotedStr( lsDfltIN, AMSSQLUtil.EQUALS_OPER)
            + ")");
      }
      else if (mbIsSrcStndAlone)
      {
         // Filter for all records with DOC_TYP = 'IN' DOC_SUBTYP = 'IS'
         lsr.add("AND DOC_CD IN ( SELECT DOC_CD FROM "+ getSchema(this,"R_GEN_DOC_CTRL")
            + ".R_GEN_DOC_CTRL WHERE DOC_TYP  "+ AMSSQLUtil.getANSIQuotedStr(lsDocTyp, AMSSQLUtil.EQUALS_OPER)
            + "AND DOC_SUBTYP "+ AMSSQLUtil.getANSIQuotedStr( lsDfltIS, AMSSQLUtil.EQUALS_OPER)
            + ")");
      }
   }
   catch (RemoteException foException)
   {
      // Add exception log to logger object
      moAMSLog.error("Unexpected error encountered while processing. ", foException);

      AMSDataObject.raiseException(
         "Remote Exception Encountered",
         (Session)loVSORBSession,
         AMSMsgUtil.SEVERITY_LEVEL_SEVERE );
   }

   if (mbIsSrcInvoice || mbIsSrcStndAlone)
   {
      query.addFilter(lsr);
      // Auto Numbering will default to checked
      CheckboxElement loAutoNumChkBox = (CheckboxElement)getElementByName("DOC_AUTO_NUM");
      loAutoNumChkBox.setChecked( true );
   }

}
//END_EVENT_T1IN_DOC_CREA_ACTN_beforeQuery}}
//{{EVENT_pAutoDocCreateInfo_beforeGenerate
void pAutoDocCreateInfo_beforeGenerate(HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output)
{

   if(mbIsSrcInvoice || mbIsSrcStndAlone)
   {
      VSRow loPoDocHdr = null;

      String lsDocDeptCode     = "";
      String lsDocUnitCode     = "";
      //This Code Should only get executed if the Source Page
      //is pInvoicingSearch_SelectLines, pInvoicingSearch_SelectLinesPO.
      if (mbIsSrcInvoice)
      {

          //Get a Handle to the PO Header.
          loPoDocHdr = getSourcePage().getDataSource("T3PO_DOC_HDR").getCurrentRow();

          //If Handle is not null..
          if (loPoDocHdr != null)
          {
              //Get the DEPT_CD and UNIT_CD of the PO document
              lsDocDeptCode = loPoDocHdr.getData(ATTR_DOC_DEPT_CD).getString();
              lsDocUnitCode = loPoDocHdr.getData(ATTR_DOC_UNIT_CD).getString();
          }
      }
      else if (mbIsSrcStndAlone)
      {
         // For stand alone invoice use the current user's home Department Code
         // defined for the user in security as the Document Dept Code.
         //
         AMSUser loUser = AMSSecurity.getUser( getParentApp().getSession().getLogin() );
         lsDocDeptCode     = AMSStringUtil.strTrim( loUser.getHomeOrgCodeLevels()[2] );
         lsDocUnitCode     = AMSStringUtil.strTrim( loUser.getHomeOrgCodeLevels()[8] );
      }

     //Get a handle to the textbox.
     AMSTextFieldElement loDeptCD = (AMSTextFieldElement) getElementByName("DOC_DEPT_CD");
     AMSTextFieldElement loUnitCD = (AMSTextFieldElement) getElementByName("DOC_UNIT_CD");
      //Set the DEPT_CD
      if (loDeptCD != null)
      {
         if (!AMSStringUtil.strIsEmpty(lsDocDeptCode))
         {
             loDeptCD.setValue(lsDocDeptCode);
         }
         else
         {
             loDeptCD.setValue("");
         }//End of if (!AMSStringUtil.strIsEmpty(lsDOC_DEPT_CD))
      }//End of if (loDeptCD != null)
     //Set the UNIT_CD
     if (loUnitCD != null)
     {
         if (!AMSStringUtil.strIsEmpty(lsDocUnitCode))
         {
             loUnitCD.setValue(lsDocUnitCode);
         }
         else
         {
             loUnitCD.setValue("");
         }//End of if (!AMSStringUtil.strIsEmpty(lsDOC_UNIT_CD))
     }//End of if (loUnitCD != null)
   }
}
//END_EVENT_pAutoDocCreateInfo_beforeGenerate}}
//{{EVENT_T1IN_DOC_CREA_ACTN_afterQuery
void T1IN_DOC_CREA_ACTN_afterQuery(VSResultSet rs )
{
   //Write Event Code below this line
}
//END_EVENT_T1IN_DOC_CREA_ACTN_afterQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IN_DOC_CREA_ACTN.addDBListener(this);
	addPageListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeGenerate(VSPage obj, HTMLDocumentModel docModel, VSOutParam cancel , VSOutParam output){
		Object source = obj;
		if (source == this ) {
			pAutoDocCreateInfo_beforeGenerate(docModel, cancel, output);
		}
	}
	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IN_DOC_CREA_ACTN) {
			T1IN_DOC_CREA_ACTN_beforeQuery(query , resultset );
		}
	}
	public void afterQuery( DataSource obj, VSResultSet rs ){
		Object source = obj;
		if (source == T1IN_DOC_CREA_ACTN) {
			T1IN_DOC_CREA_ACTN_afterQuery(rs );
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
    * Sets the source data source and action element using which
    * the document creation action will be performed.
    *
    * @param foDataSrc The source data source
    */
   public void setSourceElements( DataSource foDataSrc,
         AMSHyperlinkActionElement foActnElem )
   {
      moSrcDataSrc  = foDataSrc ;
      moSrcActnElem = foActnElem ;
   } /* end setSourceElements() */

   /**
    * Returns the data source on which the document creation
    * action will be performed.
    *
    * @return The source data source
    */
   public DataSource getSourceDataSource()
   {
      return moSrcDataSrc ;
   } /* end getSourceDataSource() */

   /**
    * Returns the data source on which the document creation
    * action will be performed.
    *
    * @return The source data source
    */
   public AMSHyperlinkActionElement getSourceActionElement()
   {
      return moSrcActnElem ;
   } /* end getSourceDataSource() */

   /**
    * This method is called before the page is generated. It
    * checks to see if any records exist on data source before
    * showing the page.
    */
   public void beforeGenerate()
   {
      if ( T1IN_DOC_CREA_ACTN.getCurrentRow() == null )
      {
         /*
          * If no valid document creation action exist, then
          * do not show the Create Document page. Instead, just
          * display an error to the user.
          */
         raiseException( "No valid Document Creation actions exist", SEVERITY_LEVEL_SEVERE ) ;
      } /* end if ( T2R_GEN_CPY_FWD.getCurrentRow() == null ) */
   } /* end beforeGenerate() */

   /**
    * This method gets the current Fiscal Year from R_CLDT table
    * @param foSession - Session Object
    * @return - int containing current Fiscal Year
    */
   private int getCLDTInfoFY(VSSession foSession)
   {
      String lsParamValue = null ;

      lsParamValue = getApplParamValue( "APPL_SYS_DT", foSession ) ;

      VSDate loAppSysDate = new VSDate( lsParamValue );

      VSResultSet loRSCldt = null;

      SearchRequest loSearchRequest = new SearchRequest();
      loSearchRequest.addParameter( "R_CLDT", "CAL_DT",
         loAppSysDate.toShortString() );
      VSQuery loQry = new VSQuery( foSession, "R_CLDT", loSearchRequest, null);
      loRSCldt =loQry.execute();

      VSRow loCLDT = loRSCldt.first();

      int liFY = -1;
      if( loCLDT != null)
      {
         liFY = loCLDT.getData("FY").getInt();
      }
      loRSCldt.close();
      return( liFY );
   } // End method getCLDTInfoFY


    /**
    * This method returns the parameter value as a string for the parameter name that has
    * been passed. If the parameter is not defined then it raises a severe exception.
    *
    * @param fsParamName - the application parameter name
    * @param foSession - the session object used for updates
    * @return String - the parameter value
    */
   private String getApplParamValue(String fsParamName, VSSession foSession)
   {
      VSQuery        loVSQuery =null;
      VSResultSet    loVSResultSet =null;
      VSRow loRow = null;

      String lsParamValue = null ;

      try
      {
         SearchRequest loSearchReq = new SearchRequest();
         loSearchReq.addParameter("IN_APP_CTRL", "PARM_NM", fsParamName) ;
         loVSQuery = new VSQuery(foSession,"IN_APP_CTRL",
                                     loSearchReq ,null);
         loVSResultSet = loVSQuery.execute();
         loRow   = loVSResultSet.first();

         lsParamValue =  loRow.getData("PARM_VL").getString();

      } //try
      catch(Exception loExcp)
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", loExcp);

      }
      finally
      {
         if (loVSResultSet !=null)
         {
            loVSResultSet.close();
         }
      } // finally

      return lsParamValue ;
   }  //getApplParamValue
}
