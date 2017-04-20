/*
 * @(#)AMSReportTreeNode.java     1.0 05/08/2000
 *
 * Copyright 2000 by American Management Systems, Inc.,
 * 4050 Legato Road, Fairfax, Virginia, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of American Management Systems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with American Management Systems, Inc.
 *
 * Initial reversata.
 * @author Mukul Gupta
 */

package com.amsinc.gems.adv.vfc.html ;
import versata.vfc.html.* ;

import versata.vfc.* ;

import versata.common.* ;

import java.io.* ;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.text.*;
import javax.swing.text.html.HTML.*;
import com.amsinc.gems.adv.common.AMSLogConstants;
import com.amsinc.gems.adv.common.AMSLogger;

import org.apache.commons.logging.Log;


//import javax.swing.text.AbstractDocument.* ;
import advantage.Reports_Sys_Admin_App.* ;
import com.amsinc.gems.adv.common.* ;

/**
 * AMSReportTreeNode class is an extension of the Jade-provided TreeNode
 * class.  It has been extended to override numerous methods that are used
 * to generate the HTML for each Report node.
 *
 * @author Mukul Gupta
 * @see versata.vfc.html.TreeNode
 * @see AMSReportTreeElement
 */


public class AMSReportTreeNode extends AMSNavigationTreeNode
{
   public static final String FOLDER_CLASS          = "Folder" ;
   public static final String JADE_REPORT_CLASS     = "Report" ;
   public static final String ACTUATE_REPORT_CLASS  = "ActuateReport" ;
   public static final String SYSTEM_BATCH          = "SystemBatch";
   public static final String CHAIN_JOB             = "ChainJob";
   private static final String QUERY_NAME           = "R_BS_CATALOG" ;
   private static final String INITIAL_WHERE_CLAUSE = "PNT_ID = 0" ;
   private static final String CHILD_WHERE_CLAUSE   = "PNT_ID =" ;
   private static final String ORDER_BY             = "CTLG_ID" ;
   private static final String CHAIN_JOB_ORDER_BY = "SEQ_NO";

   public static final String FIRST_LEVEL_FOLDER         = "FirstLevelFolder" ;

   public static final String NODE_ID               = "CTLG_ID" ;
   public static final String PARENT_ID             = "PNT_ID" ;
   public static final String ITEM_TYPE             = "ITM_TYP" ;
   public static final String NODE_LABEL            = "CTLG_NM";
   public static final String NODE_CLASS            = "CLS_NM";
   public static final String NODE_PKG_NM           = "PKG_NM";

   public static final String MODE_CONFIGURE        ="configure";
   public static final String MODE_NAVIGATE         ="navigate";

   /** Indicates if the node has been modified */
   private boolean mboolChanged = true ;

   private long   mlNodeID   = -1 ;
   private long   mlParentID    = -1 ;
   private int    miItemType       = 0 ;
   private String msClassName = "none";
   private String msPackageName = null;
   private String msCatalogLabel = null;

   /** This is the logger object */
   private static Log moAMSLog = AMSLogger.getLog( AMSReportTreeNode.class,
      AMSLogConstants.FUNC_AREA_DFLT ) ;
   
   /**
    * This is the default constructor.  It calls the super-class
    * constructor.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - Inital version
    * @see versata.vfc.html.TreeNode
    */
   public AMSReportTreeNode()
   {
   } /* End of AMSReportTreeNode() */



   /**
    * This constructor is used to set all of the instance
    * variable for the node.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - Inital version
    *
    * @param fsLabel The text that will be displayed for the node
    * @param flNodeID the unique node id in the catalog table
    * @param flParentID the id of the folder element that contains this element
    * @param fsItemType the type of item (report, folder,
    * @see versata.vfc.html.TreeNode
    */
   public AMSReportTreeNode( String fsLabel, long flNodeID, long flParentID,
      int fiItemType, String fsClassName,String fsPackageName,
      boolean fbAllowChildren )
   {
      setLabel( fsLabel ) ;
      setUniqueNodeID( flNodeID ) ;
      setParentID(flParentID);
      setItemType( fiItemType ) ;
      canHaveChildren( fbAllowChildren ) ;
      if (fsClassName !=null)
      {
         setClassName(fsClassName);
      }
      setPackageName(fsPackageName);
   } /* End of AMSReportTreeNode() */


   /**
    * This method will be called whenever the node has
    * been opened.  It should be implemented by each
    * sub-class since the database interaction will
    * vary for each type.  If a sub-class does not
    * override this method, no nodes will be added
    * to the node when it is opened.
    *
    * Modification Log : Mukul Gupta - 05/08/2000
    *                                 - inital version
    *
    * @param foReportTree The navigation tree instance
    */
   public void nodeOpened( AMSReportTreeElement foReportTree )
   {
      if (mboolChanged)
      {
         mboolChanged = false;
         clearChildren();
         addChildNodes(foReportTree);
      }
   } /* end nodeOpened() */

   /**
    * This is the getter for the node ID instance
    * variable.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @return long The node ID
    */
   public long getUniqueNodeID()
   {
      return mlNodeID ;
   } /* end getUniqueNodeID() */

   /**
    * This is the setter for the node ID instance
    * variable.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @param flNodeID The node ID
    */
   public void setUniqueNodeID( long flNodeID )
   {
      mlNodeID = flNodeID ;
   } /* end setUniqueNodeID() */

   /**
    * This is the getter for the parent ID instance
    * variable.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @return long The parent ID
    */
   public long getParentID()
   {
      return mlParentID ;
   } /* end getUniqueNodeID() */

   /**
    * This is the setter for the parent ID instance
    * variable.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @param flParentID The parent ID
    */
   public void setParentID( long flParentID )
   {
      mlParentID = flParentID ;
   } /* end setUniqueNodeID() */

   /**
    * A getter for the item type string.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @return miItemType The item type
    */
   public int getItemType()
   {
      return miItemType ;
   } /* end getItenType() */

   /**
    * A setter for the item type string.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @param fiItemType The item type string
    */
   public void setItemType( int fiItemType )
   {
      miItemType = fiItemType ;
   } /* end setItemType() */

   /**
    * A setter for the class name string. applies to report
    * item in catalog table
    *
    * Modification Log : Mukul Gupta - 06/20/2000
    *                                 - inital version
    *
    * @param fsClassName The class name string
    */
   public void setClassName( String fsClassName )
   {
      msClassName = fsClassName ;
   } /* End of setClassName() */


   /**
    * A getter for the Class name string.
    *
    * Modification Log : Mukul Gupta - 06/20/2000
    *                                 - inital version
    *
    * @return msClassname The class name string
    */
   public String getClassName()
   {
      return msClassName ;
   } /* End of getClassName() */

   /**
    * A setter for the Package Name string. applies to report items in
    * catalog table
    *
    * Modification Log : Mukul Gupta - 06/20/2000
    *                                 - inital version
    *
    * @param fsPackageName The package name string
    */
   public void setPackageName( String fsPackageName )
   {
      msPackageName = fsPackageName ;
   } /* End of setPackageName() */

   /**
    * A getter for the Package Name string.
    *
    * Modification Log : Mukul Gupta - 06/20/2000
    *                                 - inital version
    *
    * @return msPackageName The Package Name string
    */
   public String getPackageName()
   {
      return msPackageName ;
   } /* End of getPackageName() */

   /**
    * Returns the no folder image for the tree.
    *
    * Modification Log : Mukul Gupta - 05/08/2000
    *                                 - inital version
    *
    * @param foReportTree The navigation tree
    */
   public String getNoFolderImage( AMSReportTreeElement foReportTree )
   {
      return foReportTree.getNoFolderImage();
   } /* End of getNoFolderImage */

   /**
    * This method queries R_BS_CATALOG table to retrieve the
    * child nodes and then creates a new node for each
    * child.
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @param foReportTree The report Tree tree instance
    */
   public void addChildNodes( AMSReportTreeElement foReportTree )
   {
      VSQuery        loVSQuery ;
      VSResultSet    loVSResultSet ;
      int            liNumRows ;
      ReportTreePage loPage        = (ReportTreePage)foReportTree.getPage() ;
      boolean        lboolCnfgMode = isConfigureMode( loPage ) ;
      VSSession      loSession     = loPage.getParentApp().getSession() ;

      if ( !lboolCnfgMode )
      {
         StringBuffer      lsbWhereClause = new StringBuffer( 250 ) ;
         VSORBSession      loORBSession   = loSession.getORBSession() ;

         lsbWhereClause.append( "((" ) ;
         lsbWhereClause.append("(APPL_ID=");
         lsbWhereClause.append(AMSParams.msPrimaryApplication);
         lsbWhereClause.append(") ");
         lsbWhereClause.append( ") AND (" ) ;
         lsbWhereClause.append( CHILD_WHERE_CLAUSE ) ;
         lsbWhereClause.append( mlNodeID ) ;
         lsbWhereClause.append( "))" ) ;

         if (getItemType()==AMSBatchConstants.CHAIN_JOB)
         {
            loVSQuery = new VSQuery( loSession, QUERY_NAME,
                  CHILD_WHERE_CLAUSE + mlNodeID, CHAIN_JOB_ORDER_BY ) ;
         }
         else
         {
            loVSQuery = new VSQuery( loSession, QUERY_NAME,
                  lsbWhereClause.toString(), ORDER_BY ) ;
         }

      } /* end if ( !lboolCnfgMode ) */
      else
      {
         if (getItemType()==AMSBatchConstants.CHAIN_JOB)
         {
            loVSQuery = new VSQuery( loSession, QUERY_NAME,
                  CHILD_WHERE_CLAUSE + mlNodeID, CHAIN_JOB_ORDER_BY ) ;
         }
         else
         {
            loVSQuery = new VSQuery( loSession, QUERY_NAME,
                  CHILD_WHERE_CLAUSE + mlNodeID, ORDER_BY ) ;
         }

      } /* end else */

      loVSResultSet =  loVSQuery.execute() ;
      loVSResultSet.last() ;
      liNumRows = loVSResultSet.getRowCount() ;

      for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ )
      {
         VSRow              loVSRow ;
         String             lsLabel ;
         String             lsPackageName;
         String             lsClassName;
         int                liItemType;
         boolean            lbAllowChildren =false;
         long               llNodeID ;
         long               llParentID;
         AMSReportTreeNode  loNewNode ;

         loVSRow = loVSResultSet.getRowAt( liRowCtr ) ;

         lsLabel         = loVSRow.getData( NODE_LABEL ).getString() ;
         llNodeID        = loVSRow.getData( NODE_ID).getLong() ;
         llParentID      = loVSRow.getData( PARENT_ID).getLong();
         liItemType      = loVSRow.getData( ITEM_TYPE ).getInt() ;
         lsClassName     = loVSRow.getData( NODE_CLASS).getString() ;
         lsPackageName   = loVSRow.getData( NODE_PKG_NM).getString() ;

         if (liItemType != 0)
         {
            if (liItemType==AMSBatchConstants.FOLDER)
            {
               lbAllowChildren = true;
            }
            else if ( ( liItemType==AMSBatchConstants.CHAIN_JOB ) && ( lboolCnfgMode ) )
            {
               lbAllowChildren = true;
            }
            else
            {
               lbAllowChildren = false;
            }
         }

         loNewNode = new AMSReportTreeNode( lsLabel, llNodeID, llParentID,
            liItemType,lsClassName, lsPackageName, lbAllowChildren) ;

         setNodeDestination( loNewNode, loPage ) ;
         setNodeCSSClass( loNewNode, loPage ) ;
         addChildNode( loNewNode ) ;

         // put new node in Hashtable of the containing Page
         Hashtable lhNodeList = ((AMSReportTreePage)(foReportTree .getPage())).mhNodes;
         lhNodeList.put(loNewNode.getNodeID(),loNewNode);

      } /* end for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ ) */

      loVSResultSet.close();
   } /* end addChildNodes() */


   /**
    * This method is called by the ReportTree
    * page to add the first-level nodes to the report
    * tree.  It queries the R_BS_CATALOG table for nodes with
    * parent as the Root Node
    *
    * Modification Log : Mukul Gupta - 06/05/2000
    *                                 - inital version
    *
    * @param foRootNode The navigation tree's root node
    * @param flTreeTrunkID The tree trunk ID for the tree
    * @param foVSSession The user's session object
    * @param foNavTree The navigation tree
    */
   public  void addTopLevelNodes( AMSReportTreeNode foRootNode,
      VSSession foVSSession,
      AMSReportTreeElement foNavTree )
   {
      VSQuery        loVSQuery ;
      VSResultSet    loVSResultSet ;
      int            liNumRows ;
      ReportTreePage loPage        = (ReportTreePage)foNavTree.getPage() ;
      boolean        lboolCnfgMode = isConfigureMode( loPage ) ;
      VSSession      loSession     = loPage.getParentApp().getSession() ;

      if ( !lboolCnfgMode )
      {
         StringBuffer      lsbWhereClause = new StringBuffer( 250 ) ;
         VSORBSession      loORBSession   = loSession.getORBSession() ;

         lsbWhereClause.append( "((" ) ;
         lsbWhereClause.append("(APPL_ID=");
         lsbWhereClause.append(AMSParams.msPrimaryApplication);
         lsbWhereClause.append(") ");
         lsbWhereClause.append( ") AND (" ) ;
         lsbWhereClause.append( INITIAL_WHERE_CLAUSE ) ;
         lsbWhereClause.append( "))" ) ;

         loVSQuery = new VSQuery( foVSSession, QUERY_NAME,
            lsbWhereClause.toString(), ORDER_BY ) ;
      } /* end if ( !lboolCnfgMode ) */
      else
      {
         loVSQuery = new VSQuery( foVSSession, QUERY_NAME,
            INITIAL_WHERE_CLAUSE, ORDER_BY ) ;
      } /* end else */

      loVSResultSet =  loVSQuery.execute() ;
      loVSResultSet.last() ;
      liNumRows = loVSResultSet.getRowCount() ;

      if ( liNumRows > 0 )
      {
         /* There are nodes defined, so we will use them */

         for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ )
         {
            VSRow              loVSRow ;
            String             lsPackageName;
            String             lsLabel ;
            String             lsClassName;
            int                liItemType;
            boolean            lbAllowChildren =false;
            long               llNodeID ;
            long               llParentID;
            AMSReportTreeNode  loNewNode ;

            loVSRow = loVSResultSet.getRowAt( liRowCtr ) ;

            lsLabel         = loVSRow.getData( NODE_LABEL ).getString() ;
            llNodeID        = loVSRow.getData( NODE_ID).getLong() ;
            llParentID      = loVSRow.getData( PARENT_ID).getLong();
            liItemType      = loVSRow.getData( ITEM_TYPE ).getInt() ;
            lsClassName     = loVSRow.getData( NODE_CLASS ).getString() ;
            lsPackageName   = loVSRow.getData( NODE_PKG_NM ).getString() ;

            if (liItemType != 0)
            {
               if (liItemType==AMSBatchConstants.FOLDER)
               {
                  lbAllowChildren = true;
               }
               else if ( ( liItemType==AMSBatchConstants.CHAIN_JOB ) && ( lboolCnfgMode ) )
               {
                  lbAllowChildren = true;
               }
               else
               {
                  lbAllowChildren = false;
               }
            }

            loNewNode = new AMSReportTreeNode( lsLabel, llNodeID, llParentID,
                              liItemType, lsClassName, lsPackageName, lbAllowChildren) ;

            setNodeDestination( loNewNode, loPage ) ;
            setNodeCSSClass( loNewNode, loPage ) ;

            foRootNode.addChildNode( loNewNode ) ;

            // put new node in Hashtable of the containing Page
            Hashtable lhNodeList = ((AMSReportTreePage)(foNavTree.getPage())).mhNodes;
            lhNodeList.put(loNewNode.getNodeID(),loNewNode);

         } /* end for ( int liRowCtr = 1 ; liRowCtr <= liNumRows ; liRowCtr++ ) */
      } /* end if ( liNumRows > 0 ) */

      loVSResultSet.close();
   } /* end addTopLevelNodes() */

   /**
    * This methods sets the destination of the node based on its type.
    *
    * @param foTreeNode The report tree node
    * @param foTreePage The report tree page
    */
   public static void setNodeDestination( AMSReportTreeNode foTreeNode,
         ReportTreePage foTreePage )
   {
      boolean lboolConfigMode = foTreeNode.isConfigureMode( foTreePage ) ;
      long    llNodeID        = foTreeNode.getUniqueNodeID() ;

      switch ( foTreeNode.getItemType() )
      {
         case AMSBatchConstants.REPORT :
            if ( lboolConfigMode )
            {
               foTreeNode.setPageTemplate( "Catalog_LeafNode_Pg" ) ;
               foTreeNode.setWhereClause( "CTLG_ID=" + llNodeID ) ;
            } /* end if ( lboolConfigMode ) */
            else
            {
               StringBuffer lsbWhere = new StringBuffer( 100 ) ;

               foTreeNode.setPageTemplate( "Report_Completed_User" ) ;
               //Display latest jobs on top
               foTreeNode.setOrderByClause( "AGNT_ID DESC" ) ;
               lsbWhere.append( "(" ) ;
               /*
                * If the user is a system admin, then show all reports.
                * Otherwise, restrict the list to only those reports run
                * by the user or that are public.
                */
                  lsbWhere.append( "PNT_CTLG_ID=" ) ;
                  lsbWhere.append( llNodeID ) ;
               lsbWhere.append( ") AND (" ) ;

               lsbWhere.append( "ITM_TYP=" ) ;
               lsbWhere.append( AMSBatchConstants.REPORT ) ;
               lsbWhere.append( ") AND (RUN_STA =" ) ;
               lsbWhere.append( AMSBatchConstants.STATUS_COMPLETE ) ;
               lsbWhere.append( ")" ) ;

               foTreeNode.setWhereClause( lsbWhere.toString() ) ;
            } /* end else */
            foTreeNode.setTargetFrame( "BatDisplay" ) ;
            foTreeNode.setApplName( "Reports_Sys_Admin_App" ) ;
            break ;
         case AMSBatchConstants.ACTUATE_REPORT :
            if ( lboolConfigMode )
            {
               foTreeNode.setPageTemplate( "Root_Node_Pg" ) ;
               foTreeNode.setApplName( "Reports_Sys_Admin_App" ) ;
            } /* end if ( lboolConfigMode ) */
            else
            {
               StringBuffer lsbURL = new StringBuffer( AMSParams.msActuateServerName ) ;

               lsbURL.append( foTreeNode.getPackageName() + "/" ) ;
               lsbURL.append( foTreeNode.getClassName() ) ;
               lsbURL.append( "-ASP=" ) ;
               lsbURL.append( foTreePage.getSessionId().substring( 4 ) ) ; // removes the prefix IOR: from session id
               lsbURL.append( foTreePage.getPageId() ) ;

               foTreeNode.setURL( lsbURL.toString() ) ;
            } /* end else */
            foTreeNode.setTargetFrame( "BatDisplay" ) ;
            break ;
         case AMSBatchConstants.SYSTEM_BATCH :
            if ( lboolConfigMode )
            {
               foTreeNode.setPageTemplate( "Catalog_LeafNode_Pg" ) ;
               foTreeNode.setWhereClause( "CTLG_ID=" + llNodeID ) ;
            } /* end if ( lboolConfigMode ) */
            else
            {
               StringBuffer lsbWhere = new StringBuffer( 100 ) ;

               foTreeNode.setPageTemplate( "Job_Completed_User" ) ;
               //Display latest jobs on top
               foTreeNode.setOrderByClause( "AGNT_ID DESC" ) ;
               lsbWhere.append( "(" ) ;
               /*
                * If the user is a system admin, then show all jobs.
                * Otherwise, restrict the list to only those jobs run
                * by the user.
                */
                  lsbWhere.append( "PNT_CTLG_ID=" ) ;
                  lsbWhere.append( llNodeID ) ;
               lsbWhere.append( ") AND (" ) ;
               lsbWhere.append( "ITM_TYP=" ) ;
               lsbWhere.append( AMSBatchConstants.SYSTEM_BATCH ) ;
               lsbWhere.append( ") AND (RUN_STA=" ) ;
               lsbWhere.append( AMSBatchConstants.STATUS_COMPLETE ) ;
               lsbWhere.append( ")" ) ;

               foTreeNode.setWhereClause( lsbWhere.toString() ) ;
            } /* end else */
            foTreeNode.setTargetFrame( "BatDisplay" ) ;
            foTreeNode.setApplName( "Reports_Sys_Admin_App" ) ;
            break ;
         case AMSBatchConstants.CHAIN_JOB :
            if ( lboolConfigMode )
            {
               foTreeNode.setPageTemplate( "Catalog_ChainJob_Pg" ) ;
               foTreeNode.setWhereClause( "CTLG_ID=" + llNodeID ) ;
            } /* end if ( lboolConfigMode ) */
            else
            {


               StringBuffer lsbWhere = new StringBuffer( 100 ) ;

               foTreeNode.setPageTemplate( "Admin_User_Chain_Job_Sch" ) ;
               foTreeNode.setOrderByClause( "AGNT_ID DESC" ) ;

               lsbWhere.append( "(" ) ;
                  lsbWhere.append( "PNT_CTLG_ID =" ) ;
                  lsbWhere.append( llNodeID ) ;


               lsbWhere.append( ") AND (" ) ;
               lsbWhere.append( "ITM_TYP=" ) ;
               lsbWhere.append( AMSBatchConstants.CHAIN_JOB ) ;
               lsbWhere.append( ") AND (RUN_STA=" ) ;
               lsbWhere.append( AMSBatchConstants.STATUS_COMPLETE ) ;
               lsbWhere.append( ")" ) ;


               foTreeNode.setWhereClause( lsbWhere.toString() ) ;

               // end changes

            } /* end else */
            foTreeNode.setTargetFrame( "BatDisplay" ) ;
            foTreeNode.setApplName( "Reports_Sys_Admin_App" ) ;
            break ;
         case AMSBatchConstants.FOLDER :
            if ( lboolConfigMode )
            {
               if ( foTreeNode.getUniqueNodeID() == 0 )
               {
                  /* Root node */
                  foTreeNode.setPageTemplate( "Root_Node_Pg" ) ;
                  foTreeNode.setWhereClause( "CTLG_ID=0" ) ;
               } /* end if ( foTreeNode.getUniqueNodeID() == 0 ) */
               else
               {
                  foTreeNode.setPageTemplate( "Catalog_Folder_Pg" ) ;
               foTreeNode.setWhereClause( "CTLG_ID=" + llNodeID ) ;
               } /* end else */
               foTreeNode.setTargetFrame( "BatDisplay" ) ;
               foTreeNode.setApplName( "Reports_Sys_Admin_App" ) ;
            } /* end if ( lboolConfigMode ) */
            break ;
         default :
            break ;
      } /* end switch ( foTreeNode.getItemType() ) */
   } /* end setNodeDestination() */

   /**
    * This methods sets the style sheet class of the node based on its type.
    *
    * @param foTreeNode The report tree node
    * @param foTreePage The report tree page
    */
   public static void setNodeCSSClass( AMSReportTreeNode foTreeNode,
         ReportTreePage foTreePage )
   {
      switch ( foTreeNode.getItemType() )
      {
         case AMSBatchConstants.FOLDER :
            if ( foTreeNode.getParentID() == 0 )
            {
               foTreeNode.setCSSClass( FIRST_LEVEL_FOLDER ) ;
            } /* end if ( foTreeNode.getParentID() == 0 ) */
            else
            {
               foTreeNode.setCSSClass( FOLDER_CLASS ) ;
            } /* end else */
            break ;
         case AMSBatchConstants.REPORT :
            foTreeNode.setCSSClass( JADE_REPORT_CLASS ) ;
            break ;
         case AMSBatchConstants.ACTUATE_REPORT :
            foTreeNode.setCSSClass( ACTUATE_REPORT_CLASS ) ;
            break ;
         case AMSBatchConstants.CHAIN_JOB :
            foTreeNode.setCSSClass( CHAIN_JOB ) ;
            break ;
         case AMSBatchConstants.SYSTEM_BATCH :
            foTreeNode.setCSSClass( SYSTEM_BATCH ) ;
            break ;
         default :
            break ;
      } /* end switch ( liNodeType ) */
   } /* end setNodeCSSClass() */

   /**
    * Overrides toString from java.lang.Object to output the node parameter values
    */
   public String toString()
   {
      StringBuffer loBuffer = new StringBuffer(256);
      loBuffer.append("\n****  AMSReportTreeNode*******\n");
      loBuffer.append("Label : " + getLabel() +"\n");
      loBuffer.append("Node ID :" + getUniqueNodeID() +"\n");
      loBuffer.append("Parent ID: " + getParentID() +"\n");
      loBuffer.append("Item Type: " + getItemType() +"\n");
      loBuffer.append("Can Have Childeren: " + canHaveChildren()+"\n");
      loBuffer.append("Is Open: " + isOpen()+"\n");
      loBuffer.append("\n****  AMSReportTreeNode*******\n");

      return loBuffer.toString();
   } /* end toString() */

   /**
    * Returns the node unique Id as String
    */
   public String getNodeID()
   {
      return String.valueOf( getUniqueNodeID() ) ;
   } /* end getNodeID() */


   /**
    * @param the boolean parameter to set the corresponding node flag 'dirty'
    * indicating change in a node child items
    */
   public void setChange( boolean fboolChanged )
   {
      mboolChanged = fboolChanged;
   } /* end setChange() */


   /**
    * Returns if the child node view is to be refreshed
    */
   public boolean isChanged()
   {
      return mboolChanged ;
   } /* end isChanged() */

   /**
    * The report tree is generated differently for the configuration and for regular
    * navigation.  A session property is set in AMSNavigationTreePage (link clicked)
    * to identify if configure or navigation functions are involved
    */
   public boolean isConfigureMode( VSPage foPage )
   {
      try
      {
         String lsRole ;

         lsRole = foPage.getParentApp().getSession().getORBSession().getProperty(
               AMSBatchConstants.RSAA_ROLE ) ;

         if ( lsRole.equals( MODE_CONFIGURE ) )
         {
            return true;
         } /* end if ( lsRole.equals( MODE_CONFIGURE ) ) */
         else
         {
            /* if property null or MODE_NAVIGATE */
            return false;
         } /* end else */
      } /* end try */
      catch( RemoteException foExp )
      {
         // Add exception log to logger object
         moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         return false ;
      } /* end catch( RemoteException foExp ) */
   } /* end isConfigureMode() */

   /**
    * Returns the type of folder to be used in the alt and title
    * attributes of the folder links in the navigation tree.  If
    * the node is a chain job, then it returns "Chain Job".
    * Otherwise, it calls the super-class method.
    *
    * @return "Folder"
    */
   public String getFolderType()
   {
      if ( getItemType() == AMSBatchConstants.CHAIN_JOB )
      {
         return "Chain Job" ;
      } /* end if ( getItemType() == AMSBatchConstants.CHAIN_JOB ) */
      else
      {
         return super.getFolderType() ;
      } /* end else */
   } /* end getFolderType() */

   /**
    * Returns the type of no folder to be used in the alt and title
    * attributes of the no folder images in the navigation tree.
    *
    * @return The title based on the node type
    */
   public String getNoFolderType()
   {
      int liNodeType = getItemType() ;

      switch ( liNodeType )
      {
         case AMSBatchConstants.REPORT :
            return "Report" ;
         case AMSBatchConstants.ACTUATE_REPORT :
            return "Actuate Report" ;
         case AMSBatchConstants.CHAIN_JOB :
            return "Chain Job" ;
         case AMSBatchConstants.SYSTEM_BATCH :
            return "Job" ;
         default :
            return super.getNoFolderType() ;
      } /* end switch ( liNodeType ) */
   } /* end getNoFolderType() */

} /* end class AMSReportTreeNode */
