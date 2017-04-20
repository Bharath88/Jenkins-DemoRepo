/*
 * @(#)PLSDataTable.java 1.0 Nov 8, 2006
 * 
 * Copyright (C) 2006, 2006 by CGI-AMS Inc., 4050 Legato Road, Fairfax,
 * Virginia, U.S.A. All rights reserved.
 * 
 * This software is the confidential and proprietary information of CGI-AMS Inc.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with CGI-AMS Inc.
 * 
 * Modification log:
 * 
 */
package advantage.GenMRT;

import com.amsinc.gems.adv.common.DataTable;
import com.amsinc.gems.adv.vfc.html.AMSPage;

/**
 * Adds PLS side specific behavior to the MRT Datatable class. It is created and
 * used by GenerateMRT.
 * 
 * @see GenerateMRT (for online mrt generation)
 */
public class PLSDataTable extends DataTable
{

   /**
    * The page used to generate the MRT file (used for message display). Can be
    * null
    */
   private AMSPage moPage;

   /**
    * The default constructor
    * 
    * Modification Log : Mark Shipley - 01/03/2002 - MRT Cleanup
    * 
    * Mukul Gupta - 11/08/2006 - Removed PLS side dependendy - for this class
    */
   public PLSDataTable(AMSPage foPage)
   {
      super();
      moPage = foPage;
   } /* end DataTable() */
   
   /**
    * Implements the base class template method to pass warning to container page.
    * 
    * @param fsWarningMsg The warning message
    * @return If the warning message was sent to a page
    */
   protected boolean raiseWarning(String fsWarningMsg)
   {
      if (moPage != null)
      {
         
         moPage.raiseException(fsWarningMsg,AMSPage.SEVERITY_LEVEL_WARNING);
         return true;
      } /* end if ( moPage != null ) */
      
      return false;
   } 
   
   
   /**
    * Indicates error encountered during processing. Template method signature to
    * be defined by the subclasses which may add specific behavior. A PLS side
    * class for example could pass on the warning to the containing page
    * (Generate MRT page).
    * 
    * @param fsError The error message
    * @return If the error message was sent to a page
    */
   protected boolean raiseError(String fsError)
   {
      /*
       * If the directory location is not a directory but instead a file,
       * issue an error and return.
       */
      if (moPage != null)
      {
         moPage.raiseException(fsError,
               AMSPage.SEVERITY_LEVEL_ERROR);
         return true;
      } /* end if ( moPage != null ) */
      
      return false;
   }   
   

}
