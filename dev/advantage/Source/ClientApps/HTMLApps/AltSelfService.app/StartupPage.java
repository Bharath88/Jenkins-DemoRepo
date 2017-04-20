//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}


import org.apache.commons.logging.Log;

import com.amsinc.gems.adv.vfc.html.AMSDynamicTransition ;
import com.amsinc.gems.adv.common.AMSParams;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;
import com.amsinc.gems.adv.common.AMSSQLUtil;
import com.amsinc.gems.adv.common.AMSSecurityObject;
import com.amsinc.gems.adv.vfc.html.AMSAppNavTree;
import com.amsinc.gems.adv.vfc.html.AMSHyperlinkActionElement;
import advantage.AMSStringUtil;
import java.util.*;

/*
**  StartupPage
*/

//{{FORM_CLASS_DECL
public class StartupPage extends StartupPageBase

//END_FORM_CLASS_DECL}}
{
    private List moFirstLevelNodes = null;
    private List moSecondLevelNodes = null;
    private int miFirstSelectedTab = -1;
    private int miSecondSelectedTab = -1;
    private int miFirstLevelOldTab = -1;
    private int miSecondLevelOldTab = -1;
    private int miTreeTrunk = -1;
    private boolean mboolSelectedFirstLevelChanged = true;
    private boolean mboolSelectedSecondLevelChanged = true;
    private boolean mboolDisplayTabs = true;

    private String msAppNavInitSelNode = null;
    private boolean mboolOpenSiteMap = false;
    /** This is the logger object for the class */
    private static Log moLog = AMSLogger.getLog( StartupPage.class, AMSLogConstants.FUNC_AREA_PLS_SERVICES ) ;

   /** Holds Help Center file name */
   private String msHelpFileName = null;
   private String msHelpDisplayPage = null;

	// This is the constructor for the generated form. This also constructs
	// all the controls on the form. Do not alter this code.
	//{{FORM_CLASS_CTOR
	public StartupPage ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
		super(parentApp);
	//END_FORM_CLASS_CTOR}}
	}


	//{{EVENT_CODE
	//{{EVENT_StartupPage_beforeActionPerformed
void StartupPage_beforeActionPerformed( ActionElement ae, PageEvent evt, PLSRequest preq )
{
	//Write Event Code below this line

   AMSDynamicTransition loDynTran = null ;
   VSPage loTransPage;

   String lsAction = preq.getParameter( "ams_action" );
   String lsSubAction = preq.getParameter( "ams_sub_action" );
   if(lsAction != null)
   {
      if(lsAction.equalsIgnoreCase("first_level_tab"))
      {
         setSelectedFirstLevel(Integer.parseInt( lsSubAction ));
      }
      else if(lsAction.equalsIgnoreCase("second_level_tab"))
      {
         setSelectedSecondLevel( Integer.parseInt( lsSubAction ) );
      }
      else if( AMSStringUtil.strEqual( lsAction, "SiteMapNav" ) )
         {
            String lsNodeTree;
            String[] lsaTreeNodes;

            lsNodeTree = preq.getParameter( "node_tree" );
            lsaTreeNodes = lsNodeTree.split( "," );
            setSelectedFirstLevel( Integer.parseInt( lsaTreeNodes[ 0 ] ) );

            if( lsaTreeNodes.length > 1 )
            {
               setSelectedSecondLevel( Integer.parseInt( lsaTreeNodes[ 1 ] ) );
               setAppNavInitSelNode( lsSubAction );
            }
         }
      else if( lsAction.equalsIgnoreCase("HideTab") )
      {
         mboolDisplayTabs = false;
         if( AMSStringUtil.strEqual( lsSubAction, "OpenSiteMap" ) )
         {
            miFirstLevelOldTab  = miFirstSelectedTab;
            miSecondLevelOldTab = miSecondSelectedTab;
            miFirstSelectedTab  = -1;
            miSecondSelectedTab = -1;
            mboolOpenSiteMap = true;
         }
      }
      else if( lsAction.equalsIgnoreCase("ShowTab") )
      {
         mboolDisplayTabs = true;
         if( AMSStringUtil.strEqual( lsSubAction, "CloseSiteMap" ) )
         {
            setSelectedFirstLevel( miFirstLevelOldTab );
            setSelectedSecondLevel( miSecondLevelOldTab );
            setAppNavInitSelNode( Integer.toString( miSecondLevelOldTab ) );
         }
      }
      else if( lsAction.equalsIgnoreCase("OpenSiteMap") )
      {
         loDynTran = new AMSDynamicTransition(
         "AltSSSiteMap", "", "AltSelfService" );
         loDynTran.setSourcePage( this );

         // Create and Navigate to Target page
         loTransPage = loDynTran.getVSPage(
         getParentApp(), getSessionId() );
         evt.setCancel( true ) ;
         evt.setNewPage( loTransPage ) ;
      }
      else
      {
         if( lsAction.equalsIgnoreCase("OpenHelp") )
         {
            setHelpFileName( AMSParams.msMainHelpFilesLocation );
         }
         else if( lsAction.equalsIgnoreCase("OpenAccessibleHelp") )
         {
            setHelpFileName( AMSParams.msAccessHelpFilesLocation );
         }

         loDynTran = new AMSDynamicTransition(
         "AltSSHelpMaster", "", "AltSelfService" );
         loDynTran.setFramesetPageName( "AltSSHelpFrameset" ) ;
         loDynTran.setFrameName( "HelpMaster" ) ;
         loDynTran.setSourcePage( this );

         // Create and Navigate to Target page
         loTransPage = loDynTran.getVSPage(
         getParentApp(), getSessionId() );
         evt.setCancel( true ) ;
         evt.setNewPage( loTransPage ) ;
      }
   }

}
//END_EVENT_StartupPage_beforeActionPerformed}}

	//END_EVENT_CODE}}

	public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	addPageListener(this);
	//END_EVENT_ADD_LISTENERS}}
	}

	//{{EVENT_ADAPTER_CODE
	
	public void beforeActionPerformed ( VSPage obj, ActionElement ae, PageEvent evt, PLSRequest preq){
		Object source = obj;
		if (source == this ) {
			StartupPage_beforeActionPerformed( ae, evt, preq );
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
    * This method overrides the getLayoutType method provided in the super
    * class AMSPage.java to return the AltSelfService layout.
    *
    * @return int layout type
    */
    public int getLayoutType()
    {
        return LAYOUT_TYPE_ALT_SELF_SERVICE;
    }

    /**
     * This method overrides the generate method provided in the super
     * class to fill the arrays that are used by the JavaScript to fill
     * the tabs
     *
     * @return the result of super.generate()
     */
    public String generate()
    {
     	if(mboolDisplayTabs)
    	{
	    	buildTree();
	    	fillTabArrays();
	    	if(mboolSelectedSecondLevelChanged)
	    	{
	    		// Reload secondary navigator
	    		appendOnloadString( "STARTPG_LoadSecondaryNavigator()" );
	    	}
    	}

      if( mboolOpenSiteMap )
      {
         //First hide the tabs
         TextContentElement loTce = ( TextContentElement )
            getElementByName("TABNAV");
         loTce.setValue( "" );

         //Re-Submit the startup page to actually open SiteMapPage
         appendOnloadString( "STARTPG_LoadSiteMapPage()" );
         appendOnloadString( "STARTPG_LoadSecondaryNavigator()" );
         //Reset flags
         mboolOpenSiteMap = false;
         mboolDisplayTabs = true;
      }
    	return super.generate();
    }

    /**
     * This method sets the value of the TABNAV TCE with the code to
     * fill the JavaScript arrays
     */
    protected void fillTabArrays()
    {
    	TextContentElement loTce ;
    	StringBuffer       loBuf        = new StringBuffer(1024) ;
    	Iterator           loIterator;

    	loTce = (TextContentElement)getElementByName("TABNAV");
    	if (loTce == null)
    	{
    		moLog.error("Failed to get element TABNAV");
    		return;
    	}

    	loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\">");

    	if(moFirstLevelNodes != null)
    	{
	    	// for each node, call TABNAV_AddTabToFirstLevel
	    	loIterator = moFirstLevelNodes.iterator();
	    	Map loRowToAdd;
	    	while(loIterator.hasNext())
	    	{
	    		loRowToAdd = (Map)loIterator.next();
	    		loBuf.append("TABNAV_AddTabToFirstLevel(\"" + loRowToAdd.get("NODE_NAME").toString() + "\"," + loRowToAdd.get("NODE_UNID").toString() + ");");
	    		if(Integer.parseInt((String)loRowToAdd.get("NODE_UNID")) == miFirstSelectedTab)
	    		{
		    		String lsPageCode = (String)loRowToAdd.get("PAGE_CD");
				    if(lsPageCode != null && !lsPageCode.equals(""))
				    {
				    	displayPage(lsPageCode);
				    }
	    		}
	    	}
    	}

    	if(moSecondLevelNodes != null)
    	{
    		// for each node, call TABNAV_AddTabToSecondLevel
	    	loIterator = moSecondLevelNodes.iterator();
	    	Map loRowToAdd;
	    	while(loIterator.hasNext())
	    	{
	    		loRowToAdd = (Map)loIterator.next();
	    		loBuf.append("TABNAV_AddTabToSecondLevel(\"" + loRowToAdd.get("NODE_NAME").toString() + "\"," + loRowToAdd.get("NODE_UNID").toString() + ");");
	    		if(Integer.parseInt((String)loRowToAdd.get("NODE_UNID")) == miSecondSelectedTab)
	    		{
		    		String lsPageCode = (String)loRowToAdd.get("PAGE_CD");
				    if(lsPageCode != null && !lsPageCode.equals(""))
				    {
				    	displayPage(lsPageCode);
				    }
	    		}
	    	}
    	}

    	loBuf.append("TABNAV_SetSelectedFirstLevel(" + miFirstSelectedTab + ");");
    	loBuf.append("TABNAV_SetSelectedSecondLevel(" + miSecondSelectedTab + ");");

    	loBuf.append("</script>");

    	loTce.setValue(loBuf.toString());

    }

   private void displayPage(String lsPageCode)
   {
      VSQuery loQuery;
      VSResultSet loResult = null;
      VSRow loRow = null;
      int liRowCount;
      StringBuffer lsbQryModeAttr = new StringBuffer();

      try
      {
         loQuery = new VSQuery(getParentApp().getSession(), "IN_PAGES", "PAGE_CD = '" + AMSSQLUtil.getANSIQuotedStr(lsPageCode) + "'", "TBL_LAST_DT");
         loResult = loQuery.execute();
         loResult.last();
         liRowCount = loResult.getRowCount();
         if(liRowCount > 0)
         {
            loRow = loResult.getRowAt(liRowCount);
            int liModeInd = loRow.getData("MODE_IND").getInt();
            if( liModeInd == AMSDynamicTransition.ADD_MODE )
            {
               lsbQryModeAttr.append( AMSHyperlinkActionElement.ADDRECORD_ATTRIBUTE );
               lsbQryModeAttr.append( "=" );
               lsbQryModeAttr.append( AMSHyperlinkActionElement.ADDRECORD_ATTRIBUTE ) ;
               lsbQryModeAttr.append( "\"" );
            }
            else if( liModeInd == AMSDynamicTransition.QUERY_MODE )
            {
               lsbQryModeAttr.append( AMSHyperlinkActionElement.QUERYMODE_ATTRIBUTE );
               lsbQryModeAttr.append( "=\"" );
               lsbQryModeAttr.append( AMSHyperlinkActionElement.QUERYMODE_ATTRIBUTE ) ;
               lsbQryModeAttr.append( "\"" );
            }

            appendOnloadString("submitForm(document.forms[0],'menu_action=menu_action&ams_action=" + AMSHyperlinkActionElement.OPEN_PAGE
               + "&" + AMSHyperlinkActionElement.DESTINATION_ATTRIBUTE + "=\"" + loRow.getData("DEST_PG").getString() + "\""
               + "&" + AMSHyperlinkActionElement.WHERECLAUSE_ATTRIBUTE + "=\"" + loRow.getData("WHERE_CLAUSE").getString() + "\""
               + "&" + AMSHyperlinkActionElement.FRAMESETPAGENAME_ATTRIBUTE + "=\"" + loRow.getData("DEST_FRAMESET").getString() + "\""
               + "&" + AMSHyperlinkActionElement.FRAMENAME_ATTRIBUTE + "=\"" + loRow.getData("TARGET_FRAME").getString() + "\""
               + "&" + AMSHyperlinkActionElement.APPLICATION_ATTRIBUTE + "=\"" + loRow.getData("APPL_NM").getString() + "\""
               + "&" + lsbQryModeAttr.toString()
               + "&" + AMSHyperlinkActionElement.ORDERBYCLAUSE_ATTRIBUTE + "=\"" + loRow.getData("ORDER_BY").getString() + "\""
               + "&" + AMSHyperlinkActionElement.PAGE_CODE_ATTRIBUTE + "=\"" + loRow.getData("PAGE_CD").getString() + "\""
               + "\"','" + "Display" + "')");
         }
      }
      catch (Exception ex)
      {
         moLog.error(ex.getMessage());
      }
      finally
      {
         if(loResult != null)
         {
            loResult.close();
         }
      }
   }


	/**
     * This method extracts the tree nodes from the database and build
     * the lists of first and second level nodes.
     */
    protected void buildTree()
    {
    	int liTreeTrunk = getTreeTrunk();
    	if(liTreeTrunk != -1)
    	{
    		if(moFirstLevelNodes == null)
    		{
    			moFirstLevelNodes = AMSAppNavTree.buildFirstLevel(liTreeTrunk, getParentApp().getSession());
    			setSelectedFirstLevel(getInitiallySelectedNode(moFirstLevelNodes));
    		}
    		if(mboolSelectedFirstLevelChanged)
    		{
    			mboolSelectedFirstLevelChanged = false;
    			moSecondLevelNodes = AMSAppNavTree.buildNextLevel(miFirstSelectedTab, getParentApp().getSession());

    			if( getAppNavInitSelNode() == null )
    			{
    				setSelectedSecondLevel(getInitiallySelectedNode(moSecondLevelNodes));
    			}
    		}
    	}
    	else
    	{
    		moLog.error("No tree trunk specified");
    	}
    }

    /**
     * This method extracts the tree trunk ID according to the user
     *
     * @return The tree trunk ID
     */
    public int getTreeTrunk()
    {
    	VSQuery loQuery;
    	VSResultSet loResult = null;
    	int liRowCount;
    	String lsUser = getParentApp().getSession().getLogin();
    	String lsSecurityRole = null;
    	int liTreeTrunk = -1;

    	try
    	{
	    	// Query to extract the security role of the user
	    	loQuery = new VSQuery(getParentApp().getSession(), "R_SC_USER_ROLE_LNK", "USER_ID = '" + AMSSQLUtil.getANSIQuotedStr(lsUser) + "'", "PREC_NO");
	    	loResult = loQuery.execute();
	    	loResult.last();
	    	liRowCount = loResult.getRowCount();
	    	if(liRowCount > 0)
	    	{
	    		lsSecurityRole = loResult.getRowAt(1).getData("SEC_ROLE_ID").getString();
	    		loResult.close();

	    		// Query to extract the tree trunk associated with the security role
	            loQuery = new VSQuery(getParentApp().getSession(), "R_TREE_SEC_MAP", "SEC_ROLE_ID = '" + AMSSQLUtil.getANSIQuotedStr(lsSecurityRole) + "'", "TREE_TRUNK_UNID");
	    		loResult = loQuery.execute();
	        	loResult.last();
	        	liRowCount = loResult.getRowCount();
	    		if(liRowCount > 0)
	        	{
	        		liTreeTrunk = loResult.getRowAt(1).getData("TREE_TRUNK_UNID").getInt();
	        	}
	    	}
    	}
    	catch (Exception ex)
    	{
			moLog.error(ex.getMessage());
    	}
    	finally
    	{
			try
			{
	    		if(loResult != null)
	    		{
	    			loResult.close();
	    		}
			}
			catch (Exception ex)
			{
				moLog.error(ex.getMessage());
			}
    	}
    	return liTreeTrunk;
    }

    /**
     * This method extracts the initially selected second level node from the list
     *
     * @param foNodeList the list of node
     * @return The initially selected node
     */
    protected int getInitiallySelectedNode(List foNodeList)
    {
       	int liSelected = -1;

    	// if there are nodes on that level, select the first one
    	if(foNodeList != null)
    	{
    		if(!foNodeList.isEmpty())
    		{
    				liSelected = Integer.parseInt(((Map)foNodeList.get(0)).get("NODE_UNID").toString());
     		}

    		// if one is selected, select it
    		Iterator liIterator = foNodeList.iterator();
    		Map loNode;
    		while(liIterator.hasNext())
    		{
    			loNode = (Map)liIterator.next();
    			if("true".equalsIgnoreCase(loNode.get("INIT_SEL_FL").toString()))
    			{
    				liSelected = Integer.parseInt(loNode.get("NODE_UNID").toString());
    			}
    		}
    	}
    	return liSelected;
    }

    /**
     * This method sets the selected first level node
     *
     * @param fiNewSelected the new value of the selected node
     */
    public void setSelectedFirstLevel(int fiNewSelected)
    {
    	if(fiNewSelected != miFirstSelectedTab)
    	{
    		mboolSelectedFirstLevelChanged = true;
    		miFirstSelectedTab = fiNewSelected;
    	}
    }

    /**
     * This method sets the selected second level node
     *
     * @param fiNewSelected the newly selected node
     */
    public void setSelectedSecondLevel(int fiNewSelected)
    {
    	if(fiNewSelected != miSecondSelectedTab)
    	{
    		mboolSelectedSecondLevelChanged = true;
    		miSecondSelectedTab = fiNewSelected;
    	}

      //Always null the InitSelNode when SelectedSecondLevel is clicked.
      setAppNavInitSelNode( null );
    }

    public int getSelectedSecondLevel()
    {
    	return miSecondSelectedTab;
    }

    public int getSelectedFirstLevel()
    {
    	return miFirstSelectedTab;
    }

    public void displayTabs(boolean fboolDisplayTabs)
    {
    	mboolDisplayTabs = fboolDisplayTabs;
    }

    public boolean areTabsDisplayed()
    {
    	return mboolDisplayTabs;
    }

   public void setHelpFileName( String fsHelpFileName )
   {
      msHelpFileName = fsHelpFileName;
   }
   public String getHelpFileName()
   {
      return msHelpFileName;
   }

   /**
   * This method is used to override init sel node
   * specified in IN_NODEs tables.
   */
   public void setAppNavInitSelNode( String fsNodeID )
   {
      msAppNavInitSelNode = fsNodeID;
   }
   /**
   * Returns Overriding node unique ID for initial selection
   */
   public String getAppNavInitSelNode( )
   {
      return msAppNavInitSelNode;
   }


}
