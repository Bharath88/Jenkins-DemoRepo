package com.amsinc.gems.adv.vfc.html;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import java.util.*;


public abstract class AMSReportTreePage extends AMSNavigationTreePage implements TreeListener
{
   /** The report tree nodes */
   public Hashtable mhNodes = new Hashtable() ;

   public AMSReportTreePage()
   {
      super() ;
   } /* end AMSReportTreePage() */

   public AMSReportTreePage( PLSApp foPlsApp )
   {
      super( foPlsApp ) ;
   } /* end AMSReportTreePage() */

   public abstract void buildTree() ;

   public void nodeOpened( TreeElement foTree, TreeNode foTreeNode )
   {
      ((AMSReportTreeNode)foTreeNode).nodeOpened( (AMSReportTreeElement)foTree ) ;
   }

   public void nodeClosed( TreeElement foTree, TreeNode foTreeNode )
   {
      ((AMSReportTreeNode)foTreeNode).nodeClosed( (AMSReportTreeElement)foTree ) ;
   }

   public void nodeSelected( TreeElement foTree, TreeNode foSelectedNode,
                             TreeNode foPreviousNode )
   {
      ((AMSReportTreeNode)foSelectedNode).nodeSelected(
         (AMSReportTreeElement)foTree, (AMSReportTreeNode)foPreviousNode ) ;
   }



}
