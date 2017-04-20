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

/*
 **  R_DOC_COMP_RQMTS
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_DOC_COMP_RQMTSImpl extends  R_DOC_COMP_RQMTSBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
	//Constant added for REQUIRE_COMP property
	public static final String PROP_REQUIRE_COMP = "REQUIRE_COMP";

	//Constant added for Maximum Line Limit Logic.
	public static final String PROP_MAX_LINE_LIMIT = "MAX_LINE_LIMIT";

	//{{COMP_CLASS_CTOR
	public R_DOC_COMP_RQMTSImpl (){
		super();
	}
	
	public R_DOC_COMP_RQMTSImpl(Session session, boolean makeDefaults)
	{
		super(session, makeDefaults);
	
	
	
	
	//END_COMP_CLASS_CTOR}}

	}

	//{{EVENT_CODE
	
	//END_EVENT_CODE}}



	public void addListeners() {
		//{{EVENT_ADD_LISTENERS
		
		//END_EVENT_ADD_LISTENERS}}
	}

	//{{COMPONENT_RULES
		public static R_DOC_COMP_RQMTSImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new R_DOC_COMP_RQMTSImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}

	/**
	 * This method will validate DOC_TYP and DOC_COMP.
	 * It looks upon R_WF_XML_DATA in Admin Application for validation.
	 * R_WF_XML_DATA contains metadata for all document components.
	 * It also validates Maximum line limit property.
	 */
	public  void validateDocTypComp()
	{

		/*
		 *Edit to check if the Component Name entered is a valid Component.
		 */
		String lsDocComp  = this.getDOC_COMP();
		SearchRequest lsrWfXmlData = new SearchRequest();
		lsrWfXmlData.addParameter("R_WF_XML_DATA","DOC_COMP",lsDocComp);


		if ( R_WF_XML_DATAImpl.getObjectCount(lsrWfXmlData ,
			this.getSession() )== 0 )
		{
			raiseException("%c:Q0096,v:[b:R_DOC_COMP_RQMTS/DOC_COMP]%");
			return;
		}

		/*
		 * Edit to check if Document Type entered is a valid Document Type.
		 */
		if ( !AMSStringUtil.strEqual(
			lsDocComp.substring(0, lsDocComp.indexOf("_") ), this.getDOC_TYP()))
		{

			raiseException("%c:Q0094,v:[b:R_DOC_COMP_RQMTS/DOC_COMP]%");
			return;
		}

		/*
		 * Edit to check if the Property Value entered for
		 * MAX_LINE_LIMIT is a positive Integer.
		 */
		if ( AMSStringUtil.strEqual( this.getPROPERTY_NM(), PROP_MAX_LINE_LIMIT ) )
		{
			if( !AMSStringUtil.isNumeric(this.getPROPERTY_VL()) )
			{
				raiseException("%c:Q0095,v:[b:R_DOC_COMP_RQMTS/PROPERTY_VL]%");
				return;
			}
		}
	}



   /**
    * Method to get Property Value for a given Document Component and
    * given Property Name. This method should never be called to get
    * MAX_LINE_LIMIT property value from this table, instead methods
    * of MaxLineLimitSingleton should be called.
    * @param foSession - Session of Calling program
    * @param fsDocTyp - Document Type
    * @param fsPropNm - Property Name
    * @param fsDocComp - Document Component
    */
	public static String getDocCompProperty(Session foSession ,
		String fsDocTyp, String fsPropNm, String fsDocComp)
	{

		R_DOC_COMP_RQMTSImpl loDocCompRqmts = null;
		String lsPropertyVal = null;
		SearchRequest lsrDocCompRqmt = new SearchRequest();
		lsrDocCompRqmt.addParameter( "R_DOC_COMP_RQMTS" ,"DOC_TYP", fsDocTyp );
		lsrDocCompRqmt.addParameter("R_DOC_COMP_RQMTS" ,"PROPERTY_NM", fsPropNm );
		lsrDocCompRqmt.addParameter( "R_DOC_COMP_RQMTS" ,"DOC_COMP",fsDocComp );
		loDocCompRqmts = (R_DOC_COMP_RQMTSImpl)
			R_DOC_COMP_RQMTSImpl.getObjectByKey(lsrDocCompRqmt,foSession);

		if ( loDocCompRqmts!=null)
		{
			lsPropertyVal = loDocCompRqmts.getPROPERTY_VL();

		}

		return lsPropertyVal;
	}

   /**
    * Method to get Property values for all Document Components for a
    * given Document Type, given Property Name. All the property values are
    * are return in a HashTable. This method should never be called to get
    * MAX_LINE_LIMIT property value from this table, instead methods
    * of MaxLineLimitSingleton should be called.
    * @param foSession - Session of Calling program
    * @param fsDocTyp - Document Type
    * @param fsPropNm - Property Name
    * @return HashTable Key is Document Component Name and Value is
    * Property value for that Document Component and given Property Name
    */
	public static Hashtable getDocCompPropertiesForDocTyp(Session foSession ,
		String fsDocTyp , String fsPropNm  )
	{

		R_DOC_COMP_RQMTSImpl loDocCompRqmts = null;
		Hashtable lhtPropertyVal = new Hashtable();
		SearchRequest lsrDocCompRqmt = new SearchRequest();
		lsrDocCompRqmt.addParameter("R_DOC_COMP_RQMTS" ,"DOC_TYP" ,fsDocTyp );
		lsrDocCompRqmt.addParameter("R_DOC_COMP_RQMTS","PROPERTY_NM", fsPropNm );

		Enumeration lenumDocCompRecs = R_DOC_COMP_RQMTSImpl.getObjects(
			lsrDocCompRqmt ,foSession ) ;

		while ( lenumDocCompRecs.hasMoreElements() )
		{
			loDocCompRqmts = (R_DOC_COMP_RQMTSImpl)lenumDocCompRecs.nextElement();

			lhtPropertyVal.put( loDocCompRqmts.getDOC_COMP() ,
				loDocCompRqmts.getPROPERTY_VL() );
		}

		return lhtPropertyVal;
	}
}



