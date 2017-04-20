/*
 * @(#)AMSReportTreeElement.java     1.0 05/08/2000
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
 *
 */

package com.amsinc.gems.adv.vfc.html;

import com.amsinc.gems.adv.common.AMSBatchConstants ;
import java.util.Enumeration ;
import javax.swing.text.* ;
import javax.swing.text.html.HTML ;
import versata.vfc.html.* ;

/**
 * AMSReportTreeElement class is an extension of the Jade-provided TreeElement
 * class.  It has been extended to add the no folder image variable as well
 * as to set default values for the open, closed, and no folder images.
 *
 * @author Mukul Gupta
 * @see versata.vfc.html.TreeElement
 * @see AMSReportTreeNode
 */

public class AMSReportTreeElement extends AMSNavigationTree
{
   /** The image for Actuate reports */
   public static final String ACTUATE_IMAGE = "../Reports_Sys_Admin_App/Images/ADVActuate.gif" ;

   /** The image for chain jobs */
   public static final String CHAINJOB_IMAGE = "../Reports_Sys_Admin_App/Images/ADVChainJob.gif" ;

   /** The image for the chain job open folder */
   public static final String CHAINJOB_OPEN_IMAGE = "../Reports_Sys_Admin_App/Images/ADVChainJobOpen.gif" ;

   /** The image for the chain jobs closed folder */
   public static final String CHAINJOB_CLOSED_IMAGE = "../Reports_Sys_Admin_App/Images/ADVChainJobClosed.gif" ;

   /** The image for batch jobs */
   public static final String SYS_BATCH_IMAGE = "../Reports_Sys_Admin_App/Images/ADVSysBatch.gif" ;

   /** The image for report jobs */
   public static final String REPORT_IMAGE = "../Reports_Sys_Admin_App/Images/ADVReport.gif" ;

   /**
    * This constructor calls the super-class constructor
    * and then sets the folder images for the tree.
    *
    * Modification Log : Mukul Gupta    - 06/13/2000
    *                                    - Initial version.
    *
    * @param foAbsElem The HTML abstract element for the tree
    */
   public AMSReportTreeElement( HTMLElement foAbsElem )
   {
      super( foAbsElem ) ;
      setOpenFolderImage( AMSNavigationTree.PRIMARY_OPEN_FOLDER_IMAGE ) ;
      setCloseFolderImage( AMSNavigationTree.PRIMARY_CLOSED_FOLDER_IMAGE ) ;
   } /* End AMSReportTreeElement() */

   /**
    * This method returns the image for non-folder
    * nodes.  Typically, this is just an invisible
    * .gif that is used to correctly space the items
    * in the tree.
    *
    * Modification Log : Mukul Gupta    - 06/13/2000
    *                                        *
    * @return String The no folder image (including path)
    */
   public String getNoFolderImage()
   {
      return AMSNavigationTree.PRIMARY_NO_FOLDER_IMAGE ;
   } /* end getNoFolderImage() */

   /**
    * This method returns the image for non-folder
    * nodes.  Typically, this is just an invisible
    * .gif that is used to correctly space the items
    * in the tree.
    *
    * @param fiNestLevel The nest level of the node in the tree
    * @return String The no folder image (including path)
    */
   public String getNoFolderImage( int fiNestLevel )
   {
      return AMSNavigationTree.PRIMARY_NO_FOLDER_IMAGE ;
   } /* end getNoFolderImage() */

   /**
    * This method returns the image for non-folder
    * nodes.  Typically, this is just an invisible
    * .gif that is used to correctly space the items
    * in the tree.
    *
    * @param fiNestLevel The nest level of the node in the tree
    * @param foTreeNode The node in the tree
    * @return String The no folder image (including path)
    */
   public String getNoFolderImage( int fiNestLevel, TreeNode foTreeNode )
   {
      AMSReportTreeNode loTreeNode = (AMSReportTreeNode)foTreeNode ;
      int               liNodeType = loTreeNode.getItemType() ;

      switch ( loTreeNode.getItemType() )
      {
         case AMSBatchConstants.REPORT :
            return REPORT_IMAGE ;
         case AMSBatchConstants.ACTUATE_REPORT :
            return ACTUATE_IMAGE ;
         case AMSBatchConstants.SYSTEM_BATCH :
            return SYS_BATCH_IMAGE ;
         case AMSBatchConstants.CHAIN_JOB :
            return CHAINJOB_IMAGE ;
         default :
            break ;
      } /* end switch ( loTreeNode.getItemType() ) */
      return super.getNoFolderImage( fiNestLevel, foTreeNode ) ;
   } /* end getNoFolderImage() */

   /**
    * This method returns the image for closed nodes
    * Based upon the nest level of the node, it returns
    * the proper image.  This is done so that different
    * levels can use different nodes if desired.
    *
    * @param fiNestLevel The nest level of the node in the tree
    * @param foTreeNode The node in the tree
    * @return String The closed folder image (including path)
    */
   public String getCloseFolderImage( int fiNestLevel, TreeNode foTreeNode )
   {
      AMSReportTreeNode loTreeNode = (AMSReportTreeNode)foTreeNode ;
      int               liNodeType = loTreeNode.getItemType() ;

      switch ( loTreeNode.getItemType() )
      {
         case AMSBatchConstants.CHAIN_JOB :
            return CHAINJOB_CLOSED_IMAGE ;
         default :
            break ;
      } /* end switch ( loTreeNode.getItemType() ) */
      return super.getCloseFolderImage( fiNestLevel, foTreeNode ) ;
   } /* end getCloseFolderImage() */

   /**
    * This method returns the image for opened nodes
    * Based upon the nest level of the node, it returns
    * the proper image.  This is done so that different
    * levels can use different nodes if desired.
    *
    * @param fiNestLevel The nest level of the node in the tree
    * @param foTreeNode The node in the tree
    * @return String The closed folder image (including path)
    */
   public String getOpenFolderImage( int fiNestLevel, TreeNode foTreeNode )
   {
      AMSReportTreeNode loTreeNode = (AMSReportTreeNode)foTreeNode ;
      int               liNodeType = loTreeNode.getItemType() ;

      switch ( loTreeNode.getItemType() )
      {
         case AMSBatchConstants.CHAIN_JOB :
            return CHAINJOB_OPEN_IMAGE ;
         default :
            break ;
      } /* end switch ( loTreeNode.getItemType() ) */
      return super.getOpenFolderImage( fiNestLevel, foTreeNode ) ;
   } /* end getOpenFolderImage() */

} /* end class AMSReportTreeElement */
