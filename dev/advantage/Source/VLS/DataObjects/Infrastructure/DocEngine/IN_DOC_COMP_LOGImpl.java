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

import com.amsinc.gems.adv.common.*;
/*
 **  IN_DOC_COMP_LOG*/

//{{COMPONENT_RULES_CLASS_DECL
public class IN_DOC_COMP_LOGImpl extends  IN_DOC_COMP_LOGBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
   implements AMSCommonConstants, AMSDocAppConstants
{
//{{COMP_CLASS_CTOR
public IN_DOC_COMP_LOGImpl (){
	super();
}

public IN_DOC_COMP_LOGImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}
   }
	//{{COMPONENT_RULES
		public static IN_DOC_COMP_LOGImpl getNewObject(Session session, boolean makeDefaults)
		{
			return new IN_DOC_COMP_LOGImpl(session, makeDefaults);
		}	
	
	//END_COMPONENT_RULES}}
//{{EVENT_CODE

//END_EVENT_CODE}}
   public void addListeners() {
	//{{EVENT_ADD_LISTENERS
	
	//END_EVENT_ADD_LISTENERS}}
   }
   public void myInitializations()
   {
      setSaveBehaviorMode( AMSSAVEBEHAVIOR_DATAOBJECT_ALWAYS_SAVE ) ;
   }
   /**
    * This method parses the raw context filed and populates the 
    * virtual field USER_CTXT
    * COMP_CTXT sample startP@DOC_CD=PO1...
    */
   public String getUserContext()
   {
      int liIndex;
      String lsWhereClause;
      VSStringTokenizer loStrTok;
      StringBuffer loContextBuf;
      String lsToken;
      String lsAttribute;
      String lsValue;
      boolean lbFirstToken = true;
      int liStartIndex;
      String msContext = getCOMP_CTXT();
      printDebug("User Context " + msContext);
      if (msContext == null)
      {
         return null;
      }
      liStartIndex = msContext.indexOf('@');
      if (liStartIndex != -1)
      {
         msContext=msContext.substring(liStartIndex + 1);
      }
      else
      {
         return msContext;
      }
      // Parse the where clause through a string tokenizer
      loStrTok = new VSStringTokenizer(msContext, "AND", true);
      // Create a new string buffer
      loContextBuf = new StringBuffer();
      while(loStrTok.hasMoreTokens())
      {
         lsToken = loStrTok.nextToken();
         liIndex = lsToken.indexOf("=");
         if (liIndex == -1)
         {
            System.err.println("Ignoring token " +  lsToken);
            continue;
         }
         lsAttribute = lsToken.substring(0,liIndex).trim();
         lsValue     = lsToken.substring(liIndex+1);
         // If the attribute is part of the document identifier
         // then ignore it
         if (lsAttribute.equals(ATTR_DOC_CD)      ||
            lsAttribute.equals(ATTR_DOC_DEPT_CD) ||
               lsAttribute.equals(ATTR_DOC_VERS_NO) ||
               lsAttribute.equals(ATTR_DOC_CAT) ||
               lsAttribute.equals(ATTR_DOC_TYP) ||
               lsAttribute.equals(ATTR_DOC_ID))
         {
            continue;
         }
         lsAttribute = lsAttribute.substring(4,lsAttribute.indexOf("_",5));
         if (lbFirstToken)
         {
            loContextBuf.append(lsAttribute + " = " );
            lbFirstToken = false;
         }
         else
         {
            loContextBuf.append(" ," + lsAttribute + " = " );
         }
         loContextBuf.append(lsValue);
      }
      // If the buffer is empty at this stage we can assume that
      // the where clause was just made up of the document
      // identifier. Thus we can return the context as Header
      if (loContextBuf.length() == 0)
      {
         return  "Header";
      }
      else
      {
         return  loContextBuf.toString();
      }
   }
   private void printDebug(String fsMsg)
   {
      if (AMS_DEBUG)
      {
         System.err.println(fsMsg);
      }
   }
}
