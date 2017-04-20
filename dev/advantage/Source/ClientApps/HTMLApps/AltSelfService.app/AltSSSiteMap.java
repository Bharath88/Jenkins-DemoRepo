//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import com.amsinc.gems.adv.vfc.html.*;
import advantage.AMSStringUtil;
import java.util.*;

/*
**  AltSSSiteMap
*/

//{{FORM_CLASS_DECL
public class AltSSSiteMap extends AltSSSiteMapBase

//END_FORM_CLASS_DECL}}
{
   StringBuffer msbJSArrayTxt = new StringBuffer( );
   StringBuffer msbParentTree;
   int miLvlKey;
   int miMasterCount;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code.
//{{FORM_CLASS_CTOR
public AltSSSiteMap ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
   }


//{{EVENT_CODE

//END_EVENT_CODE}}

   public void addListeners() {
//{{EVENT_ADD_LISTENERS

//END_EVENT_ADD_LISTENERS}}
   }

//{{EVENT_ADAPTER_CODE

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
   * Overriden this method here to set default values on the page
   *
   * @return String  The generated HTML
   */
   public String generate()
   {
      List moFirstLevelNodes;
      VSPage loSourcePage = getSourcePage();
      TextContentElement loSiteMapJSArray;
      StringBuffer lsbScript = new StringBuffer();

      if ( loSourcePage instanceof advantage.AltSelfService.StartupPage )
      {
         int liTreeTrunk = ((advantage.AltSelfService.StartupPage)
            loSourcePage).getTreeTrunk();

         moFirstLevelNodes = AMSAppNavTree.buildFirstLevel(
            liTreeTrunk, getParentApp().getSession());

         //Create Site Map JS Array Text
         msbJSArrayTxt  = new StringBuffer("\n" );
         miMasterCount  = -1;
         miLvlKey       = 0;
         msbParentTree  = new StringBuffer();
         populateSiteMapTree( moFirstLevelNodes );

         // Write JavaScript Text in to HTML Doc.
         lsbScript.append("\n<script type=\"text/javascript\" language=\"JavaScript\">\n");
         lsbScript.append( "<!-- \n var msaNodeArray = new Array(); \n" );
         lsbScript.append( msbJSArrayTxt );
         lsbScript.append("\n-->\n</script>\n");
         loSiteMapJSArray = (TextContentElement)
            getElementByName( "SiteMapJSArray" );
         loSiteMapJSArray.setValue( lsbScript.toString() );
      }

      return super.generate();
   }

   /**
   * gets called recarsively to get all the tree nodes and
   * create JavaScript array text to display site map.
   *
   * @param foNodes First Level Node list.
   */
   private void populateSiteMapTree( List foNodes )
   {
      Map loNode;
      List loNextNode;
      String lsNodeID = null;

      for( int liCntr = 0; liCntr < foNodes.size(); liCntr ++ )
      {
         miMasterCount ++;

         loNode      = (Map)foNodes.get( liCntr );
         lsNodeID    = loNode.get("NODE_UNID").toString();

         msbParentTree.append( "," );
         msbParentTree.append( lsNodeID );
         miLvlKey += 1;

         msbJSArrayTxt.append( "msaNodeArray[" );
         msbJSArrayTxt.append( miMasterCount );
         msbJSArrayTxt.append( "] = new Array(); \n" );

         // Node Level
         msbJSArrayTxt.append( "msaNodeArray["  );
         msbJSArrayTxt.append( miMasterCount );
         msbJSArrayTxt.append( "][0] = "  );
         msbJSArrayTxt.append( miLvlKey );
         msbJSArrayTxt.append( "; \n" );

         // NODE_UNID
         msbJSArrayTxt.append( "msaNodeArray["  );
         msbJSArrayTxt.append( miMasterCount );
         msbJSArrayTxt.append( "][1] = \""  );
         msbJSArrayTxt.append( lsNodeID );
         msbJSArrayTxt.append( "\"; \n" );

         // Node Name
         msbJSArrayTxt.append( "msaNodeArray[" );
         msbJSArrayTxt.append( miMasterCount );
         msbJSArrayTxt.append( "][2] = \""  );
         msbJSArrayTxt.append( loNode.get("NODE_NAME").toString() );
         msbJSArrayTxt.append( "\"; \n" );

         // Parent Node IDs
         msbJSArrayTxt.append( "msaNodeArray["  );
         msbJSArrayTxt.append( miMasterCount );
         msbJSArrayTxt.append( "][3] = \""  );
         msbJSArrayTxt.append( msbParentTree.substring( 1 ) );
         msbJSArrayTxt.append( "\"; \n" );

         loNextNode = AMSAppNavTree.buildNextLevel(
            Integer.parseInt( lsNodeID ), getParentApp().getSession() );
         populateSiteMapTree( loNextNode );
      }

      // Make Level Key one level back.
      if( miLvlKey > 0 )
      {
         miLvlKey -= 1;
         msbParentTree.delete( msbParentTree.lastIndexOf( "," ),
            msbParentTree.length() );
      }

   }
}
