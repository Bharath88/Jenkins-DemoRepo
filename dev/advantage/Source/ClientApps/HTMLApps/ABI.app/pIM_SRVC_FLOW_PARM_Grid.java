//{{IMPORT_STMTS
package advantage.ABI;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import advantage.IntegrationManagerClient;
import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition;

/*
**  pIM_SRVC_FLOW_PARM_Grid
*/

//{{FORM_CLASS_DECL
public class pIM_SRVC_FLOW_PARM_Grid extends pIM_SRVC_FLOW_PARM_GridBase

//END_FORM_CLASS_DECL}}
{
   private boolean mboolFirstLoad = true;
   private long    mlFlowSeqNo    = 0;



   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public pIM_SRVC_FLOW_PARM_Grid ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE
//{{EVENT_T1IM_SRVC_FLOW_PARM_beforeQuery
void T1IM_SRVC_FLOW_PARM_beforeQuery(VSQuery foQuery, VSOutParam foResultset )
{
   SearchRequest loSearchRequest = null;

   // Sort by Component Name, Parameter Name when page is opened.
   if (mboolFirstLoad)
   {
      loSearchRequest = new SearchRequest();
      loSearchRequest.add("COMP_NM, PARM_NM");

      foQuery.replaceSortingCriteria(loSearchRequest);

      mboolFirstLoad = false;
   }
}
//END_EVENT_T1IM_SRVC_FLOW_PARM_beforeQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T1IM_SRVC_FLOW_PARM.addDBListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void beforeQuery( DataSource obj, VSQuery query ,VSOutParam resultset){
		Object source = obj;
		if (source == T1IM_SRVC_FLOW_PARM) {
			T1IM_SRVC_FLOW_PARM_beforeQuery(query , resultset );
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

   public static VSPage editParameters(
         VSPage foSrcPage , PLSRequest foRequest, VSRow foRow)
   {
        pIM_SRVC_FLOW_PARM_Grid          loEditParametersPage ;
         AMSDynamicTransition loDynTran ;
         String               lsHTML ;

         /*
          * Any additional where clause will be added in the beforeQuery() event
          * only.  This will optimize the filter (starts with indexed columns)
          */
         loDynTran = new AMSDynamicTransition( "pIM_SRVC_FLOW_PARM_Grid",null , "ABI");
         loDynTran.setSourcePage( foSrcPage ) ;

         loDynTran.setWhereClause("FLOW_SEQ_NO= "+ foRow.getData("SEQ_NO").getLong());

         loEditParametersPage = (pIM_SRVC_FLOW_PARM_Grid) loDynTran.getVSPage(
              foSrcPage.getParentApp(), foSrcPage.getSessionId() );

         loEditParametersPage.createFlowRunParmRecords(foRow);
         loEditParametersPage.generate() ;

         return loEditParametersPage ;
   }

   public void createFlowRunParmRecords(VSRow foRow)
   {
      try
      {
         if ( flowRunParmExist(foRow))
         {

            return;
         }

         long llFlowID = foRow.getData("FLOW_ID").getLong();
         String lsWhere = " FLOW_ID=" + llFlowID;
         VSQuery loQuery =
              new VSQuery( getParentApp().getSession(), "IM_SRVC_PARM", lsWhere, "UPPER(COMP_NM), UPPER(PARM_NM)");
          VSResultSet loRes = loQuery.execute();
          VSRow loRow = loRes.first();
          while( loRow!=null )
          {
            T1IM_SRVC_FLOW_PARM.insert();
            VSRow loCurrentRow = T1IM_SRVC_FLOW_PARM.getCurrentRow();
            loCurrentRow.getData("SEQ_NO").setLong(IntegrationManagerClient.getUniqNum("IM_SRVC_FLOW_PARM"));
            loCurrentRow.getData("FLOW_SEQ_NO").setLong(foRow.getData("SEQ_NO").getLong());
            loCurrentRow.getData("FLOW_ID").setLong(loRow.getData("FLOW_ID").getLong());
            loCurrentRow.getData("PARM_NM").setString(loRow.getData("PARM_NM").getString());
            loCurrentRow.getData("PARM_VL").setString(loRow.getData("PARM_VL").getString());
            loCurrentRow.getData("PARM_TYP").setInt(loRow.getData("PARM_TYP").getInt());
            loCurrentRow.getData("ENCR_FL").setBoolean(loRow.getData("ENCR_FL").getBoolean());
            loCurrentRow.getData("PARM_DESC").setString(loRow.getData("PARM_DESC").getString());
            loCurrentRow.getData("COMP_NM").setString(loRow.getData("COMP_NM").getString());
            loCurrentRow.save();
            T1IM_SRVC_FLOW_PARM.updateDataSource();
            loRow = loRes.next();
          }

       }
      catch (VSException e)
       {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public boolean flowRunParmExist(VSRow foRow)
   {
      SearchRequest lsr = new SearchRequest();
      lsr.add("FLOW_SEQ_NO="+ foRow.getData("SEQ_NO").getLong());
      VSQuery loQuery =
           new VSQuery( getParentApp().getSession(), "IM_SRVC_FLOW_PARM", lsr, null);
       VSResultSet loRes = loQuery.execute();
       VSRow loRow = loRes.first();

       boolean lboolRet = (loRow != null)? true : false;
      return lboolRet;
   }

}