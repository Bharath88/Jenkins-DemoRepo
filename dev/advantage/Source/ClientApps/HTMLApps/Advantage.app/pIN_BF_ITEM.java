//{{IMPORT_STMTS
package advantage.Advantage;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.common.*;

/*
**  pIN_BF_ITEM*/

//{{FORM_CLASS_DECL
public class pIN_BF_ITEM extends pIN_BF_ITEMBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public pIN_BF_ITEM ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }



//{{EVENT_CODE
//{{EVENT_T3IN_PAGES_BeforePickQuery
/**
 * This method is called before the query is performed on the search
 * window screen. This method is used to set up the where clause
 * to retrieve only those workspaces which belong to either the
 * common application or the application of the Workspace
 *
 * Modification Log : Kiran Hiranandani   - 08/07/02
 *                                        - inital version
 *
 * @param foPick          Pick object which is doing the query
 * @param foDataSource    The data control which started the pick
 * @param foWhereClause   Where clause for the pick query
 * @param foOrderBy       The order by clause for pick query
 * @param foCancel        param for cancelling the query
 */
void T3IN_PAGES_BeforePickQuery(Pick foPick, DataSource foDataSource,
      VSOutParam foWhereClause, VSOutParam foOrderBy, VSOutParam foCancel)
{
   String            lsOldWhere = null ;
   String            lsApplWhere = null ;
   int               liApplID ;
   StringBuffer      lsbSQL = new StringBuffer( 100 ) ;
   VSResultSet       loResultSet = null ;
   VSQuery           loQuery ;
   VSRow             loAppRow ;
   VSSession         loSession = parentApp.getSession() ;
   VSRow             loRow = getRootDataSource().getCurrentRow() ;
   String            lsBFID = loRow.getData("BF_UNID").getString().trim() ;

   if ( ( lsBFID == null ) || ( lsBFID.equals("") ) )
   {
      return ;
   } /* if ( ( lsBFID == null ) || ( lsBFID.equals("") ) ) */
   else
   {
      lsbSQL.append( "WKSP_UNID IN ( SELECT WKSP_UNID FROM IN_BA, IN_BF WHERE " ) ;
      lsbSQL.append( "IN_BA.BA_UNID = IN_BF.BA_UNID AND IN_BF.BF_UNID = " ) ;
      lsbSQL.append( lsBFID ) ;
      lsbSQL.append( " ) " ) ;

      loQuery = new VSQuery( loSession, "IN_WKSP", lsbSQL.toString(), "" ) ;
      loResultSet = loQuery.execute() ;
      loAppRow = loResultSet.first() ;

      if ( loAppRow != null )
      {
         liApplID = loAppRow.getData("APPL_ID").getInt() ;

         if ( AMSSecurity.isApplicationFlagSet( liApplID, AMSSecurity.STND_ALONE_FL_ATTR, null ) )
         {
             lsApplWhere = "(APPL_ID = " + liApplID + ")" ;
         } /* end if ( AMSSecurity.isApplicationFlagSet( ... */
         else
         {
            lsApplWhere = "(APPL_ID IN (" + AMSSecurityObject.COMMON_APPL_ID +"," + liApplID + "))" ;
         } /* end else */

         /* Check if there is already an existing where clause */
         lsOldWhere = foWhereClause.stringValue() ;
         if ( lsOldWhere != null && lsOldWhere.trim().length() > 0 )
         {
            lsApplWhere = lsOldWhere + " AND " + lsApplWhere ;
         } /* end if ( lsOldWhere != null && lsOldWhere.trim().length() > 0 ) */

         foWhereClause.setValue( lsApplWhere ) ;
      } /* end if ( loAppRow != null ) */
      if ( loResultSet != null )
      {
         loResultSet.close() ;
      } /* end if ( loResultSet != null ) */
   } /* end else */
}
//END_EVENT_T3IN_PAGES_BeforePickQuery}}

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

	T3IN_PAGES.addPickListener(this);
//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

	public void BeforePickQuery(Pick obj, DataSource dataSource, VSOutParam whereClause, VSOutParam orderBy, VSOutParam cancel){
		Object source = obj;
		if (source == T3IN_PAGES) {
			T3IN_PAGES_BeforePickQuery(obj, dataSource, whereClause, orderBy, cancel);
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
