//{{IMPORT_STMTS
package advantage.AltSelfService;
import versata.vfc.html.*;
import versata.vfc.*;
import versata.common.*;
import java.io.*;
import javax.swing.text.html.*;
import com.versata.util.logging.*;
//END_IMPORT_STMTS}}
import java.util.*;
import com.amsinc.gems.adv.vfc.html.*;

/*
**  DocNavPanel*/

//{{FORM_CLASS_DECL
public class DocNavPanel extends DocNavPanelBase

//END_FORM_CLASS_DECL}}
{
   // Declarations for instance variables used in the form

   private String msCurrentTabName;
   private Hashtable mhTabInfo;
   private boolean   mboolShowFDT;
   private boolean   mboolShowDocHist;
   private boolean   mboolShowDocRef;
   /** Whether Doc Comments link is to be displayed or not */
   private boolean   mboolShowDocComm = true;
   /** Whether document has comments */
   private boolean   mboolShowCommIcon = false;
   private static String msDocNavFileLocation = null;
   private String msFormName;

   // This is the constructor for the generated form. This also constructs
   // all the controls on the form. Do not alter this code. To customize paint
   // behavior, modify/augment the paint and the handleEvent methods.
//{{FORM_CLASS_CTOR
public DocNavPanel ( PLSApp parentApp ) throws VSException, java.beans.PropertyVetoException {
	super(parentApp);
//END_FORM_CLASS_CTOR}}
      setDisplayErrors(false);
      setDocNavPanelInd(DOC_MULTI_TBL_NAV_PANEL_SHOW);
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
      public String getFileLocation()
      {
/*         if (msDocNavFileLocation == null)
         {
            StringBuffer loBuf = new StringBuffer(256);
            loBuf.append("..");
            loBuf.append(File.separator);
            loBuf.append("..");
            loBuf.append(File.separator);
            loBuf.append("VLSComponents");
            loBuf.append(File.separator);
            loBuf.append("Classes");
            loBuf.append(File.separator);
            loBuf.append("advantage");
            loBuf.append(File.separator);
            loBuf.append("Advantage");
            loBuf.append(File.separator);

            msDocNavFileLocation = loBuf.toString();
         }

         return msDocNavFileLocation;
*/
         return getPageTemplatePath();
      }

   public void afterPageInitialize() {
      super.afterPageInitialize();
      //Write code here for initializing your own control
      //or creating new control.

   }

   public String generate()
   {
      // Buffer that holds the javascript that has to be wriiten out
      StringBuffer       loBuf        = new StringBuffer(512) ;
      StringBuffer       loSectionBuf = new StringBuffer(512) ;
      String             lsTabId ;
      String[]           lsTabInfo ;
      String             lsDocTitle   = "" ;
      VSPage             loSrcPage    = getSourcePage() ;
      TextContentElement loTce ;

      if ( loSrcPage != null )
      {
         lsDocTitle = ((AMSDocTabbedPage)loSrcPage).getDocTitle() ;
         mboolShowCommIcon = ((AMSDocTabbedPage)loSrcPage).getDocCommentFlag();
      } /* end if ( loSrcPage != null ) */
      // Get the element that represents the vstext tag
      loTce = (TextContentElement)getElementByName("DOCTREE");
      if (loTce == null)
      {
         System.err.println("Failed to get element DOCTREE");
         return super.generate();
      }

      loBuf.append("<script type=\"text/javascript\" language=\"JavaScript\">");
      loBuf.append("\n");

      // Set the javascript variable to indicate if the Document Comments link
      // to be displayed or not
      if (mboolShowDocComm)
      {
         loBuf.append("mboolDOCTREE_ShowDocComm = true;\n");
         if (mboolShowCommIcon) //document comment exists, show icon
         {
            loBuf.append("mboolDOCTREE_ShowCommIcon = true;\n");
         }
         else //document does not have associated comments, do not show icon
         {
            loBuf.append("mboolDOCTREE_ShowCommIcon = false;\n");
         }
      }
      else
      {
         loBuf.append("mboolDOCTREE_ShowDocComm = false;\n");
      }
      // Set the javascript variable to indicate if the Future Triggering
      // node has to be displayed or not
      if (mboolShowFDT)
      {
         loBuf.append("mboolDOCTREE_ShowFDT = true;\n");
      }
      else
      {
         loBuf.append("mboolDOCTREE_ShowFDT = false;\n");
      }

      // Set the javascript variable to indicate if the Document History
      // node has to be displayed or not
      if (mboolShowDocHist)
      {
         loBuf.append("mboolDOCTREE_ShowDocHist = true;\n");
      }
      else
      {
         loBuf.append("mboolDOCTREE_ShowDocHist = false;\n");
      }

      // Set the javascript variable to indicate if the Document Reference
      // node has to be displayed or not
      if (mboolShowDocRef)
      {
         loBuf.append("mboolDOCTREE_ShowDocRef = true;\n");
      }
      else
      {
         loBuf.append("mboolDOCTREE_ShowDocRef = false;\n");
      }

      loBuf.append("msDOCTREE_FormName = '");
      loBuf.append( msFormName ) ;
      loBuf.append("';\n");

      // Escape the backslash "\" character
      lsDocTitle = AMSPLSDocUtil.replacePattern( lsDocTitle, "\\", "\\\\" );

      // Convert the special characters in
      // doc title before showing in the page
      try
      {
         lsDocTitle = AMSPLSDocUtil.convertSpecialChars( lsDocTitle );
      }
      catch( IOException ioExp )
      {
         raiseException( ioExp.getMessage(), SEVERITY_LEVEL_ERROR );
      }

      loBuf.append("msDOCTREE_DOC_ID = '");
      loBuf.append( handleQuotedString( lsDocTitle ) ) ;
      loBuf.append("';\n");

      for (Enumeration loKeys = mhTabInfo.keys(); loKeys.hasMoreElements() ;)
      {
         lsTabId = (String) loKeys.nextElement();

         lsTabInfo = (String []) mhTabInfo.get(lsTabId);

         // Start adding to the buffer
         loBuf.append("msDOCTREE_TABS[");
         loBuf.append(lsTabInfo[1]);  // Tab Sequence Number
         loBuf.append("] = new Array(null,'");
         loBuf.append(lsTabInfo[0]);     // Tab Name
         loBuf.append("','");
         loBuf.append(lsTabId);  // Tab Id
         loBuf.append("',");

         // If this is the current tab then set the flag that this tab
         // has been visited and put it back into the hash table
         if (lsTabId.equals(msCurrentTabName))
         {
            loBuf.append("true,");

            // Update the hash table
            lsTabInfo[2] = SESSION_DOC_YES;
            mhTabInfo.put(lsTabId, lsTabInfo);

            // If there are sections then add javascript arrays
            // for them as well
            if ((lsTabInfo[3] != null && lsTabInfo[3].length() != 0) &&
                (lsTabInfo[4] != null && lsTabInfo[4].length() != 0))
            {
               int liIndex;
               loSectionBuf.append("msDOCTREE_TABS[");
               loSectionBuf.append(lsTabInfo[1]);
               loSectionBuf.append("][0] = new Array();\n");

               loSectionBuf.append("msDOCTREE_TABS[");
               loSectionBuf.append(lsTabInfo[1]);
               loSectionBuf.append("][0][0] = new Array(");
               liIndex = lsTabInfo[3].lastIndexOf(",");
               loSectionBuf.append(lsTabInfo[3].substring(0,liIndex).trim());
               loSectionBuf.append(");\n");

               loSectionBuf.append("msDOCTREE_TABS[");
               loSectionBuf.append(lsTabInfo[1]);
               loSectionBuf.append("][0][1] = new Array(");
               liIndex = lsTabInfo[4].lastIndexOf(",");
               loSectionBuf.append(lsTabInfo[4].substring(0,liIndex).trim());
               loSectionBuf.append(");\n");
            }
         }
         else
         {
            loBuf.append("false,");
         }

         if (lsTabInfo[2] == SESSION_DOC_YES)
         {
            loBuf.append("true);\n");
         }
         else
         {
            loBuf.append("false);\n");
         }
      }
      loSectionBuf.append("\n");
      loSectionBuf.append("</script>");

      loTce.setValue(loBuf.toString() + loSectionBuf.toString());

      return super.generate();
   }

   /**
    * Setter for the Tab Information
    *
    * Modification Log : Subramanyam Surabhi - 12/23/2000
    *                                        - inital version
    *
    * @param  fhTabInfo Hashtable containing the tab information
    */
   public void setTabInformation(Hashtable fhTabInfo)
   {
      mhTabInfo = fhTabInfo;
   }

   /**
    * Setter for the Show FDT flag
    *
    * Modification Log : Subramanyam Surabhi - 02/12/2001
    *                                        - inital version
    *
    * @param  fboolShowFDT Show FDT Flag
    */
   public void setShowFDT(boolean fboolShowFDT)
   {
      mboolShowFDT = fboolShowFDT;
   }

   /**
    * Setter for the Show Document History flag
    *
    * Modification Log : Subramanyam Surabhi - 07/25/2001
    *                                        - inital version
    *
    * @param  fboolShowDocHist Show Document History Flag
    */
   public void setShowDocHist(boolean fboolShowDocHist)
   {
      mboolShowDocHist = fboolShowDocHist;
   }

   /**
    * Setter for the Show Document Reference flag
    *
    * Modification Log : Subramanyam Surabhi - 07/25/2001
    *                                        - inital version
    *
    * @param  fboolShowDocRef Show Document Reference Flag
    */
   public void setShowDocRef(boolean fboolShowDocRef)
   {
      mboolShowDocRef = fboolShowDocRef;
   }
   /**
    * Setter for the Show Document Comments flag
    *
    * @param boolean Show Document Commetns Flag
    */
   public void setShowDocComm(boolean fboolShowDocComm)
   {
      mboolShowDocComm = fboolShowDocComm;
   }

   /**
    * Setter for the current tab name
    *
    * Modification Log : Subramanyam Surabhi - 12/23/2000
    *                                        - inital version
    *
    * @param  fsTabName Name of the tab
    */
   public void setCurrentTabName(String fsTabName)
   {
      msCurrentTabName = fsTabName;
   }

   /**
    * Setter for the Form Name
    *
    * Modification Log : Subramanyam Surabhi - 04/16/2001
    *                                        - inital version
    *
    * @param  fsFormName Name of the Document Form
    */
   public void setFormName(String fsFormName)
   {
      msFormName = fsFormName;
   }

   /**
    * Method overridden to make all the pages with
    * transitions as dependent.
    *
    * @return true for dependent transitions
    */
   public boolean isDependenceOf( VSPage foPage )
   {
      VSPage loSrcPage = getSourcePage() ;

      if ( loSrcPage == null )
      {
         /*
          * This case should never happen because the source
          * page should always be the document page.
          */
         return false ;
      } /* end if ( loSrcPage == null ) */
      else
      {
         if ( foPage == loSrcPage )
         {
            if ( ((AMSPage)loSrcPage).isClosed() )
            {
               return false ;
            } /* end if ( ((AMSPage)loSrcPage).isClosed() ) */
            else
            {
               return true ;
            } /* end else */
         } /* end if ( foPage == loSrcPage ) */
         else
         {
            return loSrcPage.isDependenceOf( foPage ) ;
         } /* end else */
      } /* end else */
   } /* end isDependenceOf() */

}
