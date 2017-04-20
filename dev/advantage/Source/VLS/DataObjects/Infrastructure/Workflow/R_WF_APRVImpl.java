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

import com.amsinc.gems.adv.common.wf.*;

/*
 **  R_WF_APRV
 */

//{{COMPONENT_RULES_CLASS_DECL
public class R_WF_APRVImpl extends  R_WF_APRVBaseImpl


//END_COMPONENT_RULES_CLASS_DECL}}
{
   public static final String TYPE = "Type-";

   /** Constant for Underscore */
   private static final String UNDER_SCORE = "_";

   /** String Builder to create colunm names */
   private StringBuilder moColumnName = new StringBuilder(20);

   private StringBuilder moErrorMessage = new StringBuilder(50);

//{{COMP_CLASS_CTOR
public R_WF_APRVImpl (){
	super();
}

public R_WF_APRVImpl(Session session, boolean makeDefaults)
{
	super(session, makeDefaults);




//END_COMP_CLASS_CTOR}}

   }

//{{COMPONENT_RULES
	public static R_WF_APRVImpl getNewObject(Session session, boolean makeDefaults)
	{
		return new R_WF_APRVImpl(session, makeDefaults);
	}	

//END_COMPONENT_RULES}}

//{{EVENT_CODE

//{{COMP_EVENT_beforeInsert
   public void beforeInsert(DataObject obj, Response response)
   {
      //Write Event Code below this line
      //Write Event Code below this line
      if (AMSStringUtil.strIsEmpty(obj.getData("SLF_APRV_RSCT").getString()))
      {
      obj.getData("SLF_APRV_RSCT").setString(CVL_SLF_APRV_RSTImpl.SLF_APRV_RSCT_NONE);
      }
      insertAprvRuleId(obj);

   }


//END_COMP_EVENT_beforeInsert}}

//END_EVENT_CODE}}

   public void addListeners()
   {
	//{{EVENT_ADD_LISTENERS
	
	addRuleEventListener(this);
	//END_EVENT_ADD_LISTENERS}}
   }

   /**
    * method to get the ORD_NO_x attribute for the R_WF_APRV
    * @param fiIndex as int - the number of the ORD_NO
    * @return int : the  value of the attribute ORD_NO_x as int.
    * @author - Mark Farrell
    */

   public int getORD_NO(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);
      //APL fiIndex changed to liIndex  switch (fiIndex)
      switch (liIndex)
      {
         case 1:
            return getORD_NO_1();
         case 2:
            return getORD_NO_2();
         case 3:
            return getORD_NO_3();
         case 4:
            return getORD_NO_4();
         case 5:
            return getORD_NO_5();
         case 6:
            return getORD_NO_6();
         case 7:
            return getORD_NO_7();
         case 8:
            return getORD_NO_8();
         case 9:
            return getORD_NO_9();
         case 10:
            return getORD_NO_10();
         default:
            return -1;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the ORD_COND_y_x attribute for the R_WF_APRV
    * @param fiIndex as int - the x number of the or condition
    * @param fiCondNo as int - the y number of the or condition
    * @return String : the  value of the attribute ORD_NO_x as String.
    * @author - Mark Farrell
    */

   public String getOR_COND(int fiIndex, int fiCondNo)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)
      {
         case 1:

            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_1();
               case 2:
                  return getOR_COND_2_1();
               case 3:
                  return getOR_COND_3_1();
               case 4:
                  return getOR_COND_4_1();
               case 5:
                  return getOR_COND_5_1();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 2:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_2();
               case 2:
                  return getOR_COND_2_2();
               case 3:
                  return getOR_COND_3_2();
               case 4:
                  return getOR_COND_4_2();
               case 5:
                  return getOR_COND_5_2();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 3:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_3();
               case 2:
                  return getOR_COND_2_3();
               case 3:
                  return getOR_COND_3_3();
               case 4:
                  return getOR_COND_4_3();
               case 5:
                  return getOR_COND_5_3();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 4:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_4();
               case 2:
                  return getOR_COND_2_4();
               case 3:
                  return getOR_COND_3_4();
               case 4:
                  return getOR_COND_4_4();
               case 5:
                  return getOR_COND_5_4();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 5:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_5();
               case 2:
                  return getOR_COND_2_5();
               case 3:
                  return getOR_COND_3_5();
               case 4:
                  return getOR_COND_4_5();
               case 5:
                  return getOR_COND_5_5();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 6:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_6();
               case 2:
                  return getOR_COND_2_6();
               case 3:
                  return getOR_COND_3_6();
               case 4:
                  return getOR_COND_4_6();
               case 5:
                  return getOR_COND_5_6();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 7:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_7();
               case 2:
                  return getOR_COND_2_7();
               case 3:
                  return getOR_COND_3_7();
               case 4:
                  return getOR_COND_4_7();
               case 5:
                  return getOR_COND_5_7();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 8:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_8();
               case 2:
                  return getOR_COND_2_8();
               case 3:
                  return getOR_COND_3_8();
               case 4:
                  return getOR_COND_4_8();
               case 5:
                  return getOR_COND_5_8();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 9:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_9();
               case 2:
                  return getOR_COND_2_9();
               case 3:
                  return getOR_COND_3_9();
               case 4:
                  return getOR_COND_4_9();
               case 5:
                  return getOR_COND_5_9();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         case 10:
            switch (fiCondNo)
            {
               case 1:
                  return getOR_COND_1_10();
               case 2:
                  return getOR_COND_2_10();
               case 3:
                  return getOR_COND_3_10();
               case 4:
                  return getOR_COND_4_10();
               case 5:
                  return getOR_COND_5_10();
               default:
                  return null;
            } /* end switch ( fiCondNo ) */
         default:
            return null;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the APRV_RULE_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the APRV_RULE
    * @return String : the  value of the attribute.
    * @author - Mark Farrell
    */

   public String getAPRV_RULE(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //APL changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)

      {
         case 1:
            return getAPRV_RULE_1();
         case 2:
            return getAPRV_RULE_2();
         case 3:
            return getAPRV_RULE_3();
         case 4:
            return getAPRV_RULE_4();
         case 5:
            return getAPRV_RULE_5();
         case 6:
            return getAPRV_RULE_6();
         case 7:
            return getAPRV_RULE_7();
         case 8:
            return getAPRV_RULE_8();
         case 9:
            return getAPRV_RULE_9();
         case 10:
            return getAPRV_RULE_10();
         default:
            return null;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the USID_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the USID
    * @return String : the  value of the attribute.
    * @author - Mark Farrell
    */

   public String getUSID(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)

      {
         case 1:
            return getUSID_1();
         case 2:
            return getUSID_2();
         case 3:
            return getUSID_3();
         case 4:
            return getUSID_4();
         case 5:
            return getUSID_5();
         case 6:
            return getUSID_6();
         case 7:
            return getUSID_7();
         case 8:
            return getUSID_8();
         case 9:
            return getUSID_9();
         case 10:
            return getUSID_10();
         default:
            return null;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the ROLEID_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the ROLEID
    * @return String : the  value of the attribute.
    * @author - Mark Farrell
    */

   public String getROLEID(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)
      {
         case 1:
            return getROLEID_1();
         case 2:
            return getROLEID_2();
         case 3:
            return getROLEID_3();
         case 4:
            return getROLEID_4();
         case 5:
            return getROLEID_5();
         case 6:
            return getROLEID_6();
         case 7:
            return getROLEID_7();
         case 8:
            return getROLEID_8();
         case 9:
            return getROLEID_9();
         case 10:
            return getROLEID_10();
         default:
            return null;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the ASSIGNEE_FL_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the ASSIGNEE
    * @return boolean : the  value of the attribute.
    * @author - Mark Farrell
    */

   public boolean getASSIGNEE_FL(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)

      {
         case 1:
            return getASSIGNEE_FL_1();
         case 2:
            return getASSIGNEE_FL_2();
         case 3:
            return getASSIGNEE_FL_3();
         case 4:
            return getASSIGNEE_FL_4();
         case 5:
            return getASSIGNEE_FL_5();
         case 6:
            return getASSIGNEE_FL_6();
         case 7:
            return getASSIGNEE_FL_7();
         case 8:
            return getASSIGNEE_FL_8();
         case 9:
            return getASSIGNEE_FL_9();
         case 10:
            return getASSIGNEE_FL_10();
         default:
            return false;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the ASSIGNEE_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the ASSIGNEE
    * @return String : the  value of the attribute.
    * @author - Mark Farrell
    */

   public String getASSIGNEE(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)

      {
         case 1:
            return getASSIGNEE_1();
         case 2:
            return getASSIGNEE_2();
         case 3:
            return getASSIGNEE_3();
         case 4:
            return getASSIGNEE_4();
         case 5:
            return getASSIGNEE_5();
         case 6:
            return getASSIGNEE_6();
         case 7:
            return getASSIGNEE_7();
         case 8:
            return getASSIGNEE_8();
         case 9:
            return getASSIGNEE_9();
         case 10:
            return getASSIGNEE_10();
         default:
            return null;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the WL_CMNT_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the WL_CMNT
    * @return long : the  value of the attribute.
    * @author - Mark Farrell
    */

   public long getWL_CMNT_ID(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      String lsWlCmntId = null;
      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)
      {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
                 lsWlCmntId = getData("WL_CMNT_ID_" +
                       String.valueOf(liIndex)).getString();
            return getCmntLongValue(lsWlCmntId);
         default:
            return -1;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the WL_CMNT__TXT_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the WL_CMNT_TXT
    * @return String : the  value of the attribute.
    * @author - Mark Farrell
    */

   public String getWL_CMNT_TXT(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)

      {
         case 1:
            return getWL_CMNT_TXT_1();
         case 2:
            return getWL_CMNT_TXT_2();
         case 3:
            return getWL_CMNT_TXT_3();
         case 4:
            return getWL_CMNT_TXT_4();
         case 5:
            return getWL_CMNT_TXT_5();
         case 6:
            return getWL_CMNT_TXT_6();
         case 7:
            return getWL_CMNT_TXT_7();
         case 8:
            return getWL_CMNT_TXT_8();
         case 9:
            return getWL_CMNT_TXT_9();
         case 10:
            return getWL_CMNT_TXT_10();
         default:
            return null;

      } /* end switch ( fiIndex ) */
   }

   /**
    * method to get the EMAIL_FL_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the EMAIL_FL
    * @return boolean : the  value of the attribute.
    * @author - Mark Farrell
    */

   public boolean getEMAIL_FL(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex
      //switch (fiIndex)
      switch (liIndex)

      {
         case 1:
            return getEMAIL_FL_1();
         case 2:
            return getEMAIL_FL_2();
         case 3:
            return getEMAIL_FL_3();
         case 4:
            return getEMAIL_FL_4();
         case 5:
            return getEMAIL_FL_5();
         case 6:
            return getEMAIL_FL_6();
         case 7:
            return getEMAIL_FL_7();
         case 8:
            return getEMAIL_FL_8();
         case 9:
            return getEMAIL_FL_9();
         case 10:
            return getEMAIL_FL_10();
         default:
            return false;

      } /* end switch ( fiIndex ) */
   }

   /**
    * This is a convenience method for retrieving the NOTIF_ORIG_x values. It
    * wraps the invocations of the getNOTIF_ORIG_x() methods that are defined
    * in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return int - NOTIF_ORIG_x
    * @author Vipin Sharma
    */
   public int getNOTIF_ORIG(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_ORIG_1();
         case 2:
            return getNOTIF_ORIG_2();
         case 3:
            return getNOTIF_ORIG_3();
         case 4:
            return getNOTIF_ORIG_4();
         case 5:
            return getNOTIF_ORIG_5();
         case 6:
            return getNOTIF_ORIG_6();
         case 7:
            return getNOTIF_ORIG_7();
         case 8:
            return getNOTIF_ORIG_8();
         case 9:
            return getNOTIF_ORIG_9();
         case 10:
            return getNOTIF_ORIG_10();
         default:
            return 0;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_RTE_FL_x values.
    * It wraps the invocations of the getNOTIF_WO_RTE_FL_x() methods that are
    * defined in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return boolean - NOTIF_WO_RTE_FL_x
    * @author Vipin Sharma
    */
   public boolean getNOTIF_WO_RTE_FL(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_RTE_FL_1();
         case 2:
            return getNOTIF_WO_RTE_FL_2();
         case 3:
            return getNOTIF_WO_RTE_FL_3();
         case 4:
            return getNOTIF_WO_RTE_FL_4();
         case 5:
            return getNOTIF_WO_RTE_FL_5();
         case 6:
            return getNOTIF_WO_RTE_FL_6();
         case 7:
            return getNOTIF_WO_RTE_FL_7();
         case 8:
            return getNOTIF_WO_RTE_FL_8();
         case 9:
            return getNOTIF_WO_RTE_FL_9();
         case 10:
            return getNOTIF_WO_RTE_FL_10();
         default:
            return false;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_ID_x values. It
    * wraps the invocations of the getNOTIF_WO_ID_x() methods that are defined
    * in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return String - NOTIF_WO_ID_x
    * @author Vipin Sharma
    */
   public String getNOTIF_WO_ID(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_ID_1();
         case 2:
            return getNOTIF_WO_ID_2();
         case 3:
            return getNOTIF_WO_ID_3();
         case 4:
            return getNOTIF_WO_ID_4();
         case 5:
            return getNOTIF_WO_ID_5();
         case 6:
            return getNOTIF_WO_ID_6();
         case 7:
            return getNOTIF_WO_ID_7();
         case 8:
            return getNOTIF_WO_ID_8();
         case 9:
            return getNOTIF_WO_ID_9();
         case 10:
            return getNOTIF_WO_ID_10();
         default:
            return null;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_UID_x values. It
    * wraps the invocations of the getNOTIF_WO_UID_x() methods that are defined
    * in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return String - NOTIF_WO_UID_x
    * @author Vipin Sharma
    */
   public String getNOTIF_WO_UID(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_UID_1();
         case 2:
            return getNOTIF_WO_UID_2();
         case 3:
            return getNOTIF_WO_UID_3();
         case 4:
            return getNOTIF_WO_UID_4();
         case 5:
            return getNOTIF_WO_UID_5();
         case 6:
            return getNOTIF_WO_UID_6();
         case 7:
            return getNOTIF_WO_UID_7();
         case 8:
            return getNOTIF_WO_UID_8();
         case 9:
            return getNOTIF_WO_UID_9();
         case 10:
            return getNOTIF_WO_UID_10();
         default:
            return null;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_RID_x values. It
    * wraps the invocations of the getNOTIF_WO_RID_x() methods that are defined
    * in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return String - NOTIF_WO_RID_x
    * @author Vipin Sharma
    */
   public String getNOTIF_WO_RID(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_RID_1();
         case 2:
            return getNOTIF_WO_RID_2();
         case 3:
            return getNOTIF_WO_RID_3();
         case 4:
            return getNOTIF_WO_RID_4();
         case 5:
            return getNOTIF_WO_RID_5();
         case 6:
            return getNOTIF_WO_RID_6();
         case 7:
            return getNOTIF_WO_RID_7();
         case 8:
            return getNOTIF_WO_RID_8();
         case 9:
            return getNOTIF_WO_RID_9();
         case 10:
            return getNOTIF_WO_RID_10();
         default:
            return null;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_VAR_x values. It
    * wraps the invocations of the getNOTIF_WO_VAR_x() methods that are defined
    * in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return String - NOTIF_WO_VAR_x
    * @author Vipin Sharma
    */
   public String getNOTIF_WO_VAR(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_VAR_1();
         case 2:
            return getNOTIF_WO_VAR_2();
         case 3:
            return getNOTIF_WO_VAR_3();
         case 4:
            return getNOTIF_WO_VAR_4();
         case 5:
            return getNOTIF_WO_VAR_5();
         case 6:
            return getNOTIF_WO_VAR_6();
         case 7:
            return getNOTIF_WO_VAR_7();
         case 8:
            return getNOTIF_WO_VAR_8();
         case 9:
            return getNOTIF_WO_VAR_9();
         case 10:
            return getNOTIF_WO_VAR_10();
         default:
            return null;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_ID_TYP_x values.
    * It wraps the invocations of the getNOTIF_WO_ID_TYP_x() methods that are
    * defined in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return int - NOTIF_WO_ID_TYP_x
    * @author Vipin Sharma
    */
   public int getNOTIF_WO_ID_TYP(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_ID_TYP_1();
         case 2:
            return getNOTIF_WO_ID_TYP_2();
         case 3:
            return getNOTIF_WO_ID_TYP_3();
         case 4:
            return getNOTIF_WO_ID_TYP_4();
         case 5:
            return getNOTIF_WO_ID_TYP_5();
         case 6:
            return getNOTIF_WO_ID_TYP_6();
         case 7:
            return getNOTIF_WO_ID_TYP_7();
         case 8:
            return getNOTIF_WO_ID_TYP_8();
         case 9:
            return getNOTIF_WO_ID_TYP_9();
         case 10:
            return getNOTIF_WO_ID_TYP_10();
         default:
            return 0;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_CMNT_x values. It
    * wraps the invocations of the getNOTIF_WO_CMNT_x() methods that are defined
    * in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return long - NOTIF_WO_CMNT_x
    * @author Vipin Sharma
    */
   public long getNOTIF_WO_CMNT(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      String lsNotifWoCmnt = null;
      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
               lsNotifWoCmnt = getData("NOTIF_WO_CMNT_" +
                     String.valueOf(liIndex)).getString();
            return getCmntLongValue(lsNotifWoCmnt);
         default:
            return -1;
      }
   }

   /**
    * Helper method for getNOTIF_WO_CMNT_x and getWL_CMNT_ID_x
    * that converts fsCmnt to its long value
    * @param  fsCmnt - String whose long value is to be obtained
    * @return long value of comment string or -1, if comment is null or empty
    * @author Vipin Sharma
    */
   private static long getCmntLongValue(String fsCmnt)
   {
      try
      {
         return Long.parseLong(fsCmnt);
      }
      catch (NumberFormatException loEx)
      {
         return -1L;
      }
   }

   /**
    * This is a convenience method for retrieving the NOTIF_WO_CMNTD_x values.
    * It wraps the invocations of the getNOTIF_WO_CMNTD_x() methods that are
    * defined in the superclass and returns the appropriate values.
    * @param  fiAprvLvl - Approval level
    * @return String - NOTIF_WO_CMNTD_x
    * @author Vipin Sharma
    */
   public String getNOTIF_WO_CMNTD(int fiAprvLvl)
   {

      // Handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            return getNOTIF_WO_CMNTD_1();
         case 2:
            return getNOTIF_WO_CMNTD_2();
         case 3:
            return getNOTIF_WO_CMNTD_3();
         case 4:
            return getNOTIF_WO_CMNTD_4();
         case 5:
            return getNOTIF_WO_CMNTD_5();
         case 6:
            return getNOTIF_WO_CMNTD_6();
         case 7:
            return getNOTIF_WO_CMNTD_7();
         case 8:
            return getNOTIF_WO_CMNTD_8();
         case 9:
            return getNOTIF_WO_CMNTD_9();
         case 10:
            return getNOTIF_WO_CMNTD_10();
         default:
            return null;
      }
   }

   /**
    * This is a convenience method for setting the NOTIF_ORIG_x values.
    * It wraps the invocations of the setNOTIF_ORIG_x(int) methods that
    * are defined in the superclass.
    *
    * @param fiAprvLvl - The approval level (x) for the specific NOTIF_ORIG_x attribute to set.
    * @param fiValue - The value to set.
    * @author Vipin Sharma
    */
   public void setNOTIF_ORIG(int fiAprvLvl, int fiValue)
   {
      //Set values only >= CVL_WF_NOTIF_ORIGImpl.NOTIFY_MIN to be compatible with CVL
      //to avoid second argument of 0, which is a possibility in
      // AMSApprovalProcessor.updateAprvSheet().
      if (fiValue < CVL_WF_NOTIF_ORIGImpl.NOTIFY_MIN)
      {
         return;
      }

      // Handle approval level > 10
      int liIndex = getIndexFromAprvLevel(fiAprvLvl);

      // switch (liIndex)
      switch (liIndex)
      {
         case 1:
            setNOTIF_ORIG_1(fiValue);
            break;
         case 2:
            setNOTIF_ORIG_2(fiValue);
            break;
         case 3:
            setNOTIF_ORIG_3(fiValue);
            break;
         case 4:
            setNOTIF_ORIG_4(fiValue);
            break;
         case 5:
            setNOTIF_ORIG_5(fiValue);
            break;
         case 6:
            setNOTIF_ORIG_6(fiValue);
            break;
         case 7:
            setNOTIF_ORIG_7(fiValue);
            break;
         case 8:
            setNOTIF_ORIG_8(fiValue);
            break;
         case 9:
            setNOTIF_ORIG_9(fiValue);
            break;
         case 10:
            setNOTIF_ORIG_10(fiValue);
            break;
         default:
      }
   }

      /**
       * validation on all of the or conditions, besides the first
       * If the first or condition is equal to zero, then this or
       * condition must equal zero. Also this will validate the  
       * Condition Id from table IWF07.

       * @param fsOrCond - the current or condition
       * @param fiNumber - the number of the or condition (1-5)
       * @return boolean - false if first OR is zero, and current or
       * is not zero. True if both zero, and always return true if
       * first or is not zero.
       *
       * @author - Mark Farrell
       * @Modifyed by  - Pradip Singh
       */

   public boolean checkFirstOr(String fsOrCond, int fiNumber)
   {
      String lsFirstOr = getOR_COND(fiNumber, 1).trim();
      SearchRequest loSrchReq = new SearchRequest();
      loSrchReq.addParameter("R_WF_APRV_COND", "COND_ID", fsOrCond);
      R_WF_APRV_CONDImpl loRWFAprvCond = (R_WF_APRV_CONDImpl)R_WF_APRV_CONDImpl.getObjectByKey(loSrchReq,getSession());
      if((loRWFAprvCond == null) && ( fsOrCond!= null ) && (!( fsOrCond.equals( "0" ) ) ) )
      {
         raiseException("Invalid condition ID : "+fsOrCond+" has entered.",AMSMsgUtil.SEVERITY_LEVEL_ERROR);
      }
      if (lsFirstOr.equals("0"))
      {
         return fsOrCond.trim().equals("0");
      }
      else
      {
         return true;
      }
   }

      /**
       * validation on all of the first or conditions,
       * If the first or condition is not equal to zero and the condition id dose not
       * exist on condition(IWF07) table then
       * validation error will be throwen.
       *
       * @param fsOrCond - the current or condition
       * @param fiNumber - the number of the or condition (1-5)
       * @return boolean - false if first OR is not zero, not null and condition ID
	   * not exist on IWF07 table, otherwise true.
       *
       * @author - Pradip Singh
       */

      public boolean checkFirstCond(String fsOrCond, int fiNumber)
      {
          String lsFirstOr = getOR_COND( fiNumber, 1).trim() ;
          SearchRequest loSrchReq = new SearchRequest();
          loSrchReq.addParameter("R_WF_APRV_COND", "COND_ID", fsOrCond);
          R_WF_APRV_CONDImpl loRWFAprvCond = (R_WF_APRV_CONDImpl)R_WF_APRV_CONDImpl.getObjectByKey(loSrchReq,getSession());
          if((loRWFAprvCond == null) && ( fsOrCond!= null ) && (!( fsOrCond.equals( "0" ) ) ) )
          {
             return false;
          }
          return true;
      }

      /**
    * validation on assignee flag
    * if flag true, set role id to null, else, set user id to null
    * @param fsUserId - the current user id
    * @param fiAssigneeFL - the assignee flag, true if role, false if user
    * @fiIndex - the tab index, or userid_x
    * return true.
    *
    * author - Mark Farrell
    */
      public boolean checkUser(String fsUserId, boolean fboolAssigneeFL, int fiIndex)
   {
      if (fboolAssigneeFL == true)
      {
         int liIndex;
         liIndex = getIndexFromAprvLevel(fiIndex);

         //Aprv Lvl Change: changed from fiIndex to liIndex
         //switch (fiIndex)
         switch (liIndex)
         {
            case 1:
               setUSID_1(null);
               break;
            case 2:
               setUSID_2(null);
               break;
            case 3:
               setUSID_3(null);
               break;
            case 4:
               setUSID_4(null);
               break;
            case 5:
               setUSID_5(null);
               break;
            case 6:
               setUSID_6(null);
               break;
            case 7:
               setUSID_7(null);
               break;
            case 8:
               setUSID_8(null);
               break;
            case 9:
               setUSID_9(null);
               break;
            case 10:
               setUSID_10(null);
               break;
            default:
               return true;
         }
      }
      else
      {
         if (fboolAssigneeFL == false)
         {

            int liIndex;
            liIndex = getIndexFromAprvLevel(fiIndex);

            //Aprv Lvl Change: changed from fiIndex to liIndex
            //switch (fiIndex)
            switch (liIndex)
            {
               case 1:
                  setROLEID_1(null);
                  break;
               case 2:
                  setROLEID_2(null);
                  break;
               case 3:
                  setROLEID_3(null);
                  break;
               case 4:
                  setROLEID_4(null);
                  break;
               case 5:
                  setROLEID_5(null);
                  break;
               case 6:
                  setROLEID_6(null);
                  break;
               case 7:
                  setROLEID_7(null);
                  break;
               case 8:
                  setROLEID_8(null);
                  break;
               case 9:
                  setROLEID_9(null);
                  break;
               case 10:
                  setROLEID_10(null);
                  break;
               default:
                  return true;
            }
         }
      }
      return true;
   }

   /**
    * derivation on assignee flag
    * if user id is null, return true and set flag to true
    * if role id is null, return false and set flag to false
    * @param fboolAssignee - the assignee flag
    * @param fiIndex - the number of the tab, or assignee_fl_x
    * @return true if userid is null, false if roleid is null.
    *
    * @author - Mark Farrell
    */

   public boolean trueASSIGNEE_FL(boolean fboolAssignee, int fiIndex)
   {
      if (getROLEID(fiIndex) == null)
      {
         return false;
      }
      else if (getUSID(fiIndex) == null)
      {
         return true;
      }
      else
      {
         return getASSIGNEE_FL(fiIndex);
      }
   }

   /**Gets the index value based on the approval level
    * param fiIndex - approval level
    * return value index
    */

   private int getIndexFromAprvLevel(int fiIndex)
   {

      int liIndex = fiIndex;
      int liRemainder;

      if (fiIndex <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)//Aprv Lvl Change: handlinf oa approval levels <= 10
      {
         liIndex = fiIndex;

      }
      else
      //Aprv Lvl Change:  handling approval level > 10
      {

         while (liIndex >= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
         {

            liRemainder = liIndex - AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK;
            if (liRemainder <= AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK)
            {

               liIndex = liRemainder;//Aprv Lvl Change: this value will be returned as index
               break;//break from while loop
            }
            else
            {
               liIndex = liRemainder;
            }
         }
      }

      return liIndex;

   }

   public void myInitializations()
   {
      this.enableBehavior(AMSDataObject.AMSBEHAVIOR_EFFECTIVE_DATED);
   }

   /**
    * method to set the APRV_RULE_x attribute for the R_WF_APRV
    * @param int fiIndex - the number of the APRV_RULE_x
    * @return String : the  value of the attribute.
    */
   public String setCombAprvRule(int fiIndex)
   {
      String lsCondId = null;
      String lsDocCd = null;
      String lsRule = null;
      String lsTempRule = null;
      boolean lboolBlankRule = false;

      // nothing to generate if doc cd is blank, though the rule text
      // needs to be blanked (if doing a new rule from an existing)
      try
      {
         lsDocCd = getDOC_CD();
         if ((lsDocCd == null) || lsDocCd.equals(""))
         {
            return "";
         }
      }
      catch (Exception loEx)
      {
         return "";
      }
      try
      {
         switch (fiIndex)
         {
            case 1:
               lsCondId = getOR_COND_1_1();
               break;
            case 2:
               lsCondId = getOR_COND_1_2();
               break;
            case 3:
               lsCondId = getOR_COND_1_3();
               break;
            case 4:
               lsCondId = getOR_COND_1_4();
               break;
            case 5:
               lsCondId = getOR_COND_1_5();
               break;
            case 6:
               lsCondId = getOR_COND_1_6();
               break;
            case 7:
               lsCondId = getOR_COND_1_7();
               break;
            case 8:
               lsCondId = getOR_COND_1_8();
               break;
            case 9:
               lsCondId = getOR_COND_1_9();
               break;
            case 10:
               lsCondId = getOR_COND_1_10();
               break;
            default:
               lsCondId = "";
               break;
         }
      }
      catch (Exception loEx)
      {
         raiseException("Error retrieving OR condition " + fiIndex
               + " information");
         return "";
      }
      // if the first OR condition of each approval level
      // in use is zero, set rule to true
      if ((lsCondId.trim().equals("")) || (lsCondId.equals("0")))
      {
         lsRule = "true";
      }
      else
      {
         // create the first rule
         // returned string ... PONUM = "10" AND DATE="2002/01/01"
         lsRule = AdvAbstractRulesProcessor.createRule(this, lsCondId, getSession());
         if (lsRule != null)
         {
            // create the next 4 possible AND Conditions, separated by OR
            // example of final string is ((PONUM = "10"' AND DOCDATE = "2002/10/10")
            // OR (PONUM = "20" AND DOCDATE = "2002/10/10"))
            for (int liCount2 = 2; liCount2 <= 5; liCount2++)
            {
               lsTempRule = createOR(liCount2, fiIndex);
               if (lsTempRule != null)
               {
                  lsRule += "\nOR\n" + lsTempRule;
               }
            }
         }
      }
      return lsRule;
   }

   /**
    * This method sets up the OR conditions
    * It will retrieve the current OR_COND... row, and pass this OR_COND_X_Y long
    * value to the createRule() method.  Create rule will return the AND condition.
    *
    * @param int fiX - the X in OR_COND_X_Y
    * @param int fiY - the Y in OR_COND_X_Y
    * @return - the AND Condition
    */
   public String createOR(int fiIndexX, int fiIndexY)
   {
      int liCondCount = 0;
      String lsCheckRule = null;
      String lsCond = null;

      try
      {
         switch (fiIndexX)
         {
            case 1:
               switch (fiIndexY)
               {
                  case 1:
                     lsCond = getOR_COND_1_1();
                     break;
                  case 2:
                     lsCond = getOR_COND_1_2();
                     break;
                  case 3:
                     lsCond = getOR_COND_1_3();
                     break;
                  case 4:
                     lsCond = getOR_COND_1_4();
                     break;
                  case 5:
                     lsCond = getOR_COND_1_5();
                     break;
                  case 6:
                     lsCond = getOR_COND_1_6();
                     break;
                  case 7:
                     lsCond = getOR_COND_1_7();
                     break;
                  case 8:
                     lsCond = getOR_COND_1_8();
                     break;
                  case 9:
                     lsCond = getOR_COND_1_9();
                     break;
                  case 10:
                     lsCond = getOR_COND_1_10();
                     break;
                  default:
                     lsCond = "";
                     break;
               }
               break;
            case 2:
               switch (fiIndexY)
               {
                  case 1:
                     lsCond = getOR_COND_2_1();
                     break;
                  case 2:
                     lsCond = getOR_COND_2_2();
                     break;
                  case 3:
                     lsCond = getOR_COND_2_3();
                     break;
                  case 4:
                     lsCond = getOR_COND_2_4();
                     break;
                  case 5:
                     lsCond = getOR_COND_2_5();
                     break;
                  case 6:
                     lsCond = getOR_COND_2_6();
                     break;
                  case 7:
                     lsCond = getOR_COND_2_7();
                     break;
                  case 8:
                     lsCond = getOR_COND_2_8();
                     break;
                  case 9:
                     lsCond = getOR_COND_2_9();
                     break;
                  case 10:
                     lsCond = getOR_COND_2_10();
                     break;
                  default:
                     lsCond = "";
                     break;
               }
               break;
            case 3:
               switch (fiIndexY)
               {
                  case 1:
                     lsCond = getOR_COND_3_1();
                     break;
                  case 2:
                     lsCond = getOR_COND_3_2();
                     break;
                  case 3:
                     lsCond = getOR_COND_3_3();
                     break;
                  case 4:
                     lsCond = getOR_COND_3_4();
                     break;
                  case 5:
                     lsCond = getOR_COND_3_5();
                     break;
                  case 6:
                     lsCond = getOR_COND_3_6();
                     break;
                  case 7:
                     lsCond = getOR_COND_3_7();
                     break;
                  case 8:
                     lsCond = getOR_COND_3_8();
                     break;
                  case 9:
                     lsCond = getOR_COND_3_9();
                     break;
                  case 10:
                     lsCond = getOR_COND_3_10();
                     break;
                  default:
                     lsCond = "";
                     break;
               }
               break;
            case 4:
               switch (fiIndexY)
               {
                  case 1:
                     lsCond = getOR_COND_4_1();
                     break;
                  case 2:
                     lsCond = getOR_COND_4_2();
                     break;
                  case 3:
                     lsCond = getOR_COND_4_3();
                     break;
                  case 4:
                     lsCond = getOR_COND_4_4();
                     break;
                  case 5:
                     lsCond = getOR_COND_4_5();
                     break;
                  case 6:
                     lsCond = getOR_COND_4_6();
                     break;
                  case 7:
                     lsCond = getOR_COND_4_7();
                     break;
                  case 8:
                     lsCond = getOR_COND_4_8();
                     break;
                  case 9:
                     lsCond = getOR_COND_4_9();
                     break;
                  case 10:
                     lsCond = getOR_COND_4_10();
                     break;
                  default:
                     lsCond = "";
                     break;
               }
               break;
            case 5:
               switch (fiIndexY)
               {
                  case 1:
                     lsCond = getOR_COND_5_1();
                     break;
                  case 2:
                     lsCond = getOR_COND_5_2();
                     break;
                  case 3:
                     lsCond = getOR_COND_5_3();
                     break;
                  case 4:
                     lsCond = getOR_COND_5_4();
                     break;
                  case 5:
                     lsCond = getOR_COND_5_5();
                     break;
                  case 6:
                     lsCond = getOR_COND_5_6();
                     break;
                  case 7:
                     lsCond = getOR_COND_5_7();
                     break;
                  case 8:
                     lsCond = getOR_COND_5_8();
                     break;
                  case 9:
                     lsCond = getOR_COND_5_9();
                     break;
                  case 10:
                     lsCond = getOR_COND_5_10();
                     break;
                  default:
                     lsCond = "";
                     break;
               }
               break;
            default:
               lsCond = "";
               break;
         }
      }
      catch (Exception loEx)
      {
           raiseException("Error retrieving OR condition for " + fiIndexX +
                          "and " + fiIndexY + " information");
         return "";
      }
      if ( !( ( lsCond == null ) || ( lsCond.trim().equals( "" ) || ( lsCond.equals( "0" ) ) ) ) )
      {
         lsCheckRule = AdvAbstractRulesProcessor.createRule(this, lsCond, getSession());
      }
      return lsCheckRule;
   }

   /**
    * This method retrieves the operator for the rule.  It just places
    * the correct operator sign (<,=,<>. ... ) instead of the numeral stored
    * in the condition table.
    *
    * @param String fiOperNo - the operator passed in ( 1 - 6 )
    * @return - the operator sign as a string
    */
     public static String getOperator(int fiOperNo, AMSDataObject foDO)
   {
      try
      {
         SearchRequest loSrchReq = new SearchRequest();
            loSrchReq.addParameter("CVL_WF_OPRS", "APRV_OPR_NO", String.valueOf(fiOperNo));
            CVL_WF_OPRSImpl loOper = (CVL_WF_OPRSImpl)CVL_WF_OPRSImpl.getObjectByKey(loSrchReq, foDO.getSession());
         return loOper.getData("APRV_OPR_DISP").getString();
      }
      catch (RuntimeException loEx)
      {
            foDO.raiseException("Error accessing operator symbol for value " + fiOperNo);
         return "";
      }
   }

   /**
    * Check ASSIGNEE_1 to ASSIGNEE_10 if used for approval authorization.
    * Also check for WL_CMNT_ID_1 to WL_CMNT_ID_10. Update the record only if
    * both of these fields are filled.
    * @param  none
    * @return none
    * @author Vipin Sharma
    */
   public void checkAssigneeID()
   {
      for (int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         int liRoutingSequenceNo = getORD_NO(liAprvLvl);
         if (liRoutingSequenceNo > 0)
         {
            /*
             * The worklist comment text is required, when a new comment is
             * inserted in the R_WF_CMNT. Hence, we always have a worklist
             * comment text for every worklist comment ID.
             */
            String lsAssigneeID = getASSIGNEE(liAprvLvl);
            long llWlCommentID = getWL_CMNT_ID(liAprvLvl);
            String lsAssigneeField = getAssigneeField(liAprvLvl);
            boolean lboolNotifWoRteFl = getNOTIF_WO_RTE_FL(liAprvLvl);

            int liPageAprvLvl;
            long liSeqNo = getSEQ_NO();

            /*
             * Since, there are only ten approval levels in R_WF_APRV, but
             * multiple (15 currently) approval levels on both pages of
             * Approval Rules, add a constant value (multiples of 10) to
             * get the correct approval level.
             */
            if (liSeqNo > 1)
            {
               liPageAprvLvl = liAprvLvl + (int) ((liSeqNo - 1) * 10);
            }
            else
            {
               liPageAprvLvl = liAprvLvl;
            }

            // ADVTA00004063: Change "additional notification" to
            // "Notify None" if the field is blank
            if (getNOTIF_ORIG(liAprvLvl) < CVL_WF_NOTIF_ORIGImpl.NOTIFY_MIN)
            {
               setNOTIF_ORIG(liAprvLvl, CVL_WF_NOTIF_ORIGImpl.NOTIFY_NONE);
            }

            /*
             * Vipin Sharma 8/23/2006
             * - When NOTIF_WO_RTE_FL for liAprvLvl is false, then the assignee
             * ID and comment ID for liAprvLvl have to have values—the current logic.
             * - When NOTIF_WO_RTE_FL for liAprvLvl is true, then both assignee
             * ID and comment ID for liAprvLvl have to have values, or both assignee
             * ID and comment ID for liAprvlLvl cannot have values.
             *
             */

            if (!lboolNotifWoRteFl)
            {
                 if ((AMSStringUtil.strIsEmpty(lsAssigneeID) && AMSStringUtil.strIsEmpty(lsAssigneeField))
                     || (llWlCommentID < 0))
               {
                  if (AMSStringUtil.strIsEmpty(lsAssigneeID))
                  {
                     /*raiseException("Assignee ID has to be specified "
                      + "for approval level " + liPageAprvLvl
                      + " if Routing Sequence not 0 and Non-Approval Notification flag is false"
                      + " and the Assignee Field is blank.", AMSMsgUtil.SEVERITY_LEVEL_ERROR);*/

                       raiseException("%c:Q0171%,v:" + Integer.toString(liPageAprvLvl) + "%");
                  } /* end if ( ( lsAssigneeID == null )... */

                  if (llWlCommentID < 0)
                  {
                     raiseException("Worklist Comment ID has to be specified "
                           + "for approval level " + liPageAprvLvl,
                           AMSMsgUtil.SEVERITY_LEVEL_ERROR);
                  } /* end if ( ( lsWlComment == null )... */
               }
            } /* end if ( ( !lbNotifWoRteFl )... */
            else
            {
               if ((lsAssigneeID == null || lsAssigneeID.length() <= 0))
               {
                  if (llWlCommentID >= 0)
                  {
                     raiseException("Assignee ID has to be specified "
                           + "for approval level " + liPageAprvLvl,
                           AMSMsgUtil.SEVERITY_LEVEL_ERROR);
                  } /* end if ( ( llWlCommentID >= 0 )... */

                  String lsNotifOrig = getData(
                        "NOTIF_ORIG_" + String.valueOf(liAprvLvl)).toString();
                  if ((lsNotifOrig != null) && (lsNotifOrig.length() > 0))
                  {
                     if (getNOTIF_ORIG(liAprvLvl) != CVL_WF_NOTIF_ORIGImpl.NOTIFY_NONE)
                     {
                        raiseException("%c:Q0127%,v:"
                              + Integer.toString(liPageAprvLvl) + "%",
                              AMSMsgUtil.SEVERITY_LEVEL_ERROR);
                     }
                  }
               }
               else
               {
                  if (llWlCommentID < 0)
                  {
                     raiseException("Worklist Comment ID has to be specified "
                           + "for approval level " + liPageAprvLvl,
                           AMSMsgUtil.SEVERITY_LEVEL_ERROR);
                  } /* end of if (llWlCommentID < 0) */
               } /* end of else */
            } /* end of else */
         } /* if (liRoutingSequenceNo > 0) */
      } /* end of for (...) */
      return;
   }

   /**
    * For a given approval level, the notification-only attributes
    * (NOTIF_WO_RTE_FL_x, NOTIF_WO_ID_x, NOTIF_WO_ID_TYP_x, and NOTIF_WO_CMNT_x)
    * must be set consistently with or without “data.”
    * @param  none
    * @return none
    * @author Vipin Sharma
    */
   protected void checkNotifOnlyInfo()
   {
      for (int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         int liRoutingSequenceNo = getORD_NO(liAprvLvl);
         if (liRoutingSequenceNo > 0)
         {
            boolean lboolNotifWoRteFl = getNOTIF_WO_RTE_FL(liAprvLvl);
            String lsNotifWoId = getNOTIF_WO_ID(liAprvLvl);
            int liNotifWoIdTyp = getNOTIF_WO_ID_TYP(liAprvLvl);
            long llNotifWoCmnt = getNOTIF_WO_CMNT(liAprvLvl);

            /*
             * Since, there are only ten approval levels in R_WF_APRV, but
             * multiple (15 currently) approval levels on both pages of Approval
             * Rules, add a constant value (multiples of 10) to get the correct
             * approval level.
             */
            int liPageAprvLvl;
            long llSeqNo = getSEQ_NO();
            if (llSeqNo > 1)
            {
               liPageAprvLvl = liAprvLvl + (int) ((llSeqNo - 1) * 10);
            }
            else
            {
               liPageAprvLvl = liAprvLvl;
            }

            if (lboolNotifWoRteFl)
            {
               if ((lsNotifWoId == null) || (lsNotifWoId.length() <= 0))
               {
                  raiseException("%c:Q0128%,v:"
                        + Integer.toString(liPageAprvLvl) + "%",
                        AMSMsgUtil.SEVERITY_LEVEL_ERROR);
               } /* end if ( ( lsNotifWoId == null )... */

               if (liNotifWoIdTyp <= 0)
               {
                  raiseException("%c:Q0129%,v:"
                        + Integer.toString(liPageAprvLvl) + "%",
                        AMSMsgUtil.SEVERITY_LEVEL_ERROR);
               } /* end if ( liNotifWoIdTyp <= 0 )... */

               if (llNotifWoCmnt < 0)
               {
                  raiseException("%c:Q0130%,v:"
                        + Integer.toString(liPageAprvLvl) + "%",
                        AMSMsgUtil.SEVERITY_LEVEL_ERROR);
               } /* end if ( ( llNotifWoCmnt < 0 )... */
            } /* end if ( ( lbNotifWoRteFl )... */
            else
            {
               if ((lsNotifWoId != null) && (lsNotifWoId.length() > 0))
               {
                  raiseException("%c:Q0131%,v:"
                        + Integer.toString(liPageAprvLvl) + "%",
                        AMSMsgUtil.SEVERITY_LEVEL_ERROR);
               } /* end if ( ( lsNotifWoId == null )... */

               if (liNotifWoIdTyp > 0)
               {
                  raiseException("%c:Q0132%,v:"
                        + Integer.toString(liPageAprvLvl) + "%",
                        AMSMsgUtil.SEVERITY_LEVEL_ERROR);
               } /* end if ( ( lsNotifWoId > 0 )... */

               if (llNotifWoCmnt >= 0)
               {
                  raiseException("%c:Q0133%,v:"
                        + Integer.toString(liPageAprvLvl) + "%",
                        AMSMsgUtil.SEVERITY_LEVEL_ERROR);
               } /* end if ( ( llNotifWoCmnt >= 0 )... */
            }
         } /* end if (liRoutingSequenceNo > 0)... */
      } /* end for (int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)... */
   }

   /**
    * This method is used to derive the NOTIF_WO_ID_TYP_x value. It returns
    * ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_USER if NOTIF_WO_RID_x is null and
    * NOTIF_WO_VAR_x is null; ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_ROLE if
    * NOTIF_WO_UID_x is null and NOTIF_WO_VAR_x is null; and
    * ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_VAR if NOTIF_WO_UID_x is null and
    * NOTIF_WO_RID_x is null.
    * @param fiNotify - The NOTIF_WO_ID_TYP_x value
    * @param fiIndex - The value x in NOTIF_WO_ID_TYP_x
    * @return int - Derived NOTIF_WO_ID_TYP_x value
    * @author Vipin Sharma
    */
   protected int applyNOTIF_WO_ID_TYP_FL(int fiNotify, int fiIndex)
   {
      // Set value only if the user has specifically selected one on the screen
      if (fiNotify > 0)
      {
         if ((getNOTIF_WO_RID(fiIndex) == null)
               && (getNOTIF_WO_VAR(fiIndex) == null)
               && (getNOTIF_WO_UID(fiIndex) != null))
         {
            return ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_USER;
         }
         else if ((getNOTIF_WO_UID(fiIndex) == null)
               && (getNOTIF_WO_VAR(fiIndex) == null)
               && (getNOTIF_WO_RID(fiIndex) != null))
         {
            return ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_ROLE;
         }
         else if ((getNOTIF_WO_UID(fiIndex) == null)
               && (getNOTIF_WO_RID(fiIndex) == null)
               && (getNOTIF_WO_VAR(fiIndex) != null))
         {
            return ApprovalInfo.APRV_RULE_NOTIF_WO_TYP_VAR;
         }
         else
         {
            return getNOTIF_WO_ID_TYP(fiIndex);
         }
      }
      else
      {
         return getNOTIF_WO_ID_TYP(fiIndex);
      }
   }

   /**
    * Do a search request and check if the row exists in R_WF_APRV
    * table.If the row is not found then create the unique number and
    * set it on the APRV_RULE_ID column, but if the row is found which
    * happens in case of Copy/Paste or Second row insert, then just
    * copy the APRV_RULE_ID from the previous row.
    */
   private void insertAprvRuleId(DataObject obj)
   {
      long llAprvRuleId = 0;
      Enumeration lenumObjAtts;

      SearchRequest lsrSearchReq = new SearchRequest();
      lsrSearchReq.add(new Parameter("R_WF_APRV", "DOC_CD", ""
            + obj.getData("DOC_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "GOVT_BRN_CD", obj.getData(
            "GOVT_BRN_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "CAB_CD", obj.getData(
            "CAB_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "DEPT_CD", obj.getData(
            "DEPT_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "DIV_CD", obj.getData(
            "DIV_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "GP_CD", obj.getData("GP_CD")
            .getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "SECT_CD", obj.getData(
            "SECT_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "DSTC_CD", obj.getData(
            "DSTC_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "BUR_CD", obj.getData(
            "BUR_CD").getString()));
      lsrSearchReq.add(new Parameter("R_WF_APRV", "UNIT_CD", obj.getData(
            "UNIT_CD").getString()));

      lenumObjAtts = R_WF_APRVImpl.getObjects(lsrSearchReq, getSession());
      if (lenumObjAtts.hasMoreElements())
      {
         R_WF_APRVImpl loR_WF_APRVRec = (R_WF_APRVImpl) lenumObjAtts
               .nextElement();

         if (loR_WF_APRVRec != null)
         {

            if ((loR_WF_APRVRec.getData("APRV_RULE_ID").getObject()) == null)
            {
               llAprvRuleId = createUniqNum();
               obj.getData("APRV_RULE_ID").setlong(llAprvRuleId);
            }
            else
            {
               obj.getData("APRV_RULE_ID").setlong(
                     loR_WF_APRVRec.getData("APRV_RULE_ID").getlong());
            }
         }

      }
      else
      //else if the record is not found, create the unique Aprv Rule ID.
      {
         llAprvRuleId = createUniqNum();
         obj.getData("APRV_RULE_ID").setlong(llAprvRuleId);
      }
   }

   /**
    * Create a unique number and return it back.
    */
   private long createUniqNum()
   {
      long llUniqNum = 0;
      try
      {
         llUniqNum = AMSUniqNum.getUniqNum("APRV_RULE_ID");
      }
      catch (AMSUniqNumException foExp)
      {

         if (AMS_DEBUG)
         {
		    System.err.println("Could not generate sequence : "
                  + foExp.getMessage());
            // Add exception log to logger object
            moAMSLog.error("Unexpected error encountered while processing. ", foExp);

         } /* end if ( AMS_DEBUG ) */

         raiseException("Fatal error while generate sequence : "
               + foExp.getMessage(), getSession(),
               AMSMsgUtil.SEVERITY_LEVEL_SEVERE);
      }
      return llUniqNum;
   }

   /**
    * Returns the approval level to be shown on the page
    * based on the given approval level and
    * current row's sequence number
    *
    * @param fiAprvLvl original approval level
    * @return int actual approval level (e.g. original approval level
    * 1 returns page approval level 1 if sequence number is 1 or
    * or 11 if sequence number is 2.
    */
   protected int getPageAprvLvl(int fiAprvLvl)
   {
      int liPageAprvLvl;
      long liSeqNo = getSEQ_NO();

      /*
       * Since, there are only ten approval levels in R_WF_APRV, but
       * multiple (15 currently) approval levels on both pages of
       * Approval Rules, add a constant value (multiples of 10) to
       * get the correct approval level.
       */
      if (liSeqNo > 1)
      {
         liPageAprvLvl = fiAprvLvl + (int) ((liSeqNo - 1) * 10);
      }
      else
      {
         liPageAprvLvl = fiAprvLvl;
      }
      return liPageAprvLvl;
   }

   //BGN ADVHR00037821

   /**
    * This method returns the value of Assignee field for respective approval levels
    *
    * @param fiIndex
    * @return
    */
   public String getAssigneeField(int fiIndex)
   {
      //Aprv Lvl Change: code added to handle approval level > 10
      int liIndex;
      liIndex = getIndexFromAprvLevel(fiIndex);

      //Aprv Lvl Change: changed from fiIndex to liIndex

      switch (liIndex)
      {
         case 1:
            return getASSGN_FLD_1();
         case 2:
            return getASSGN_FLD_2();
         case 3:
            return getASSGN_FLD_3();
         case 4:
            return getASSGN_FLD_4();
         case 5:
            return getASSGN_FLD_5();
         case 6:
            return getASSGN_FLD_6();
         case 7:
            return getASSGN_FLD_7();
         case 8:
            return getASSGN_FLD_8();
         case 9:
            return getASSGN_FLD_9();
         case 10:
            return getASSGN_FLD_10();
         default:
            return null;
      } /* end switch ( fiIndex ) */
   }

   /**
    * Method checks for each approval level
    * assignee id and assignee field value are populated than raise an exception
    * assignee id and assignee is role fileds are populated than raise an exception
    *
    *
    */
   public void checkAssigneeID_Field()
   {
      String lsAssigneeField = null;
      String lsAssigneeId = null;
      boolean lsAssigneeIsRole = false;
      for (int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         lsAssigneeField = getAssigneeField(liAprvLvl);
         lsAssigneeId = getASSIGNEE(liAprvLvl);
         lsAssigneeIsRole = getASSIGNEE_FL(liAprvLvl);

         /*
          * Since, there are only ten approval levels in R_WF_APRV, but
          * multiple (15 currently) approval levels on both pages of Approval
          * Rules, add a constant value (multiples of 10) to get the correct
          * approval level.
          */
         int liPageAprvLvl;
         long llSeqNo = getSEQ_NO();
         if (llSeqNo > 1)
         {
            liPageAprvLvl = liAprvLvl + (int) ((llSeqNo - 1) * 10);
         }
         else
         {
            liPageAprvLvl = liAprvLvl;
         }

         if (!AMSStringUtil.strIsEmpty(lsAssigneeField)
               && !AMSStringUtil.strIsEmpty(lsAssigneeId))
         {
            /*
             * raiseException("Either the Assignee ID or the Assignee Field can be entered."
             + "They cannot both be entered simultaneously."
             + "for approval level " + liPageAprvLvl,
             AMSMsgUtil.SEVERITY_LEVEL_ERROR);// Reserved message code Q0169
             */
               raiseException("%c:Q0169%,v:" + Integer.toString(liPageAprvLvl) + "%");

         }
         if (!AMSStringUtil.strIsEmpty(lsAssigneeField) && lsAssigneeIsRole)
         {
            /*
             * raiseException("Assignee Is Role must be unchecked if Assignee Field is populated "
             + " for approval level " + liPageAprvLvl,
             AMSMsgUtil.SEVERITY_LEVEL_ERROR);// Reserved message code Q0170
             */
               raiseException("%c:Q0170,v:" + Integer.toString(liPageAprvLvl) + "%");

         }
      }
   }// end of method

   //END ADVHR00037821

   /**
    * Sets the value of ESCL_ASGN_USER_ID1_x and ESCL_ASGN_ROLE_ID1_x
    * depending on the value of ESCL_ASGN_ROLE_FL1_x. If ESCL_ASGN_ROLE_FL1_x
    * is set to true then sets the value of ESCL_ASGN_USER_ID1_x to null
    * else sets the value of ESCL_ASGN_ROLE_ID1_x to null.
    */
   protected void setEsclAsgnUserRole1Empty()
   {
      for(int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         clearColNameBuilder();

         if(getESCL_ASGN_ROLE_FL(1, liAprvLvl))
         {
            getData(moColumnName.append("ESCL_ASGN_USER_ID1_")
                                .append(liAprvLvl).toString()).setString(null);
         }
         else
         {
            getData(moColumnName.append("ESCL_ASGN_ROLE_ID1_")
                                .append(liAprvLvl).toString()).setString(null);
         }
      }/* end for(int liAprvLvl = 1... */
   }// end of method

   /**
    * Sets the value of ESCL_ASGN_USER_ID1_x and ESCL_ASGN_ROLE_ID1_x
    * depending on the value of ESCL_ASGN_ROLE_FL1_x. If ESCL_ASGN_ROLE_FL1_x
    * is set to true then sets the value of ESCL_ASGN_USER_ID2_x to null
    * else sets the value of ESCL_ASGN_ROLE_ID1_x to null.
    */
   protected void setEsclAsgnUserRole2Empty()
   {
      for(int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         clearColNameBuilder();

         if(getESCL_ASGN_ROLE_FL(2, liAprvLvl))
         {
            getData(moColumnName.append("ESCL_ASGN_USER_ID2_")
                                .append(liAprvLvl).toString()).setString(null);
         }
         else
         {
            getData(moColumnName.append("ESCL_ASGN_ROLE_ID2_")
                                .append(liAprvLvl).toString()).setString(null);
         }
      }/* end for(int liAprvLvl = 1... */
   }// end of method

   /**
    * Returns the value to be set to ESCL_ASGN_ROLE_FLx_y for
    * Escalation Level x and Approval Level y depending on whether
    * ESCL_ASGN_ROLE_ID or ESCL_ASGN_USER_ID is populated.
    *
    * @param fiEscalationLevel Escalation Level x
    * @param fiApprovalLevel Approval Level y
    *
    * @return true if ESCL_ASGN_USER_ID is null and
    *         false if ESCL_ASGN_ROLE_ID is null
    */
   protected boolean trueESCL_ASGN_ROLE_FL(int fiEscalationLevel, int fiApprovalLevel)
   {
      if (getESCL_ASGN_ROLE_ID(fiEscalationLevel, fiApprovalLevel) == null)
      {
         return false;
      }
      else if (getESCL_ASGN_USER_ID(fiEscalationLevel, fiApprovalLevel) == null)
      {
         return true;
      }
      else
      {
         return getESCL_ASGN_ROLE_FL(fiEscalationLevel, fiApprovalLevel);
      }
   }// end of method

   /**
    * Returns value of WARN_ESCL_TMPL_ID_x for given Approval Level x
    *
    * @param fiApprovalLevel Approval Level for which value of
    *                WARN_ESCL_TMPL_ID_x is required
    */
   protected String getWARN_ESCL_TMPL_ID(int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loWarnTemplId = getData(moColumnName.append("WARN_ESCL_TMPL_ID_")
                                               .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loWarnTemplId != null)
      {
         return loWarnTemplId.getString();
      }
      else
      {
         return null;
      }
   }// end of method

   /**
    * Returns value of WARN_THLD_AGE_x for given Approval Level x
    *
    * @param fiApprovalLevel Approval Level for which value of
    *                WARN_THLD_AGE_x is required
    */
   protected Integer getWARN_THLD_AGE(int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loWarnThreshold = getData(moColumnName.append("WARN_THLD_AGE_")
                                               .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loWarnThreshold != null)
      {
         return loWarnThreshold.getint();
      }
      else
      {
         return 0;
      }
   }// end of method

   /**
    * Returns value of ESCL_FL_x for given approval level
    * where x is the specified approval level
    *
    * @param fiApprovalLevel Approval level for which value of
    *                ESCL_FL_x is required
    */
   protected boolean getESCL_FL(int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loEsclFl = getData(moColumnName.append("ESCL_FL_")
                                          .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if( loEsclFl != null)
      {
         return loEsclFl.getboolean();
      }
      else
      {
         return false;
      }
   }// end of method

   /**
    * Returns value of ESCL_THLD_AGEx_y for given Escalation Level x
    * and Approval Level y
    *
    * @param fiEscalationLevel Escalation Level x for which value of
    *                ESCL_THLD_AGEx_y is required
    * @param fiApprovalLevel Approval Level y for which value of
    *                ESCL_THLD_AGEx_y is required
    */
   protected int getESCL_THLD_AGE(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loEsclThldAge = getData(moColumnName.append("ESCL_THLD_AGE")
                                               .append(fiEscalationLevel).append(UNDER_SCORE)
                                               .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if( loEsclThldAge != null )
      {
         return loEsclThldAge.getint();
      }
      else
      {
         return 0;
      }
   }// end of method

   /**
    * Returns value of ESCL_ASGN_IDx_y for given Escalation Level x
    * and Approval Level y
    *
    * @param fiEscalationLevel Escalation Level x for which value of
    *                ESCL_ASGN_IDx_y is required
    * @param fiApprovalLevel Approval Level y for which value of
    *                ESCL_ASGN_IDx_y is required
    */
   protected String getESCL_ASGN_ID(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loEsclAssignee1 = getData(moColumnName.append("ESCL_ASGN_ID")
                                                 .append(fiEscalationLevel).append(UNDER_SCORE)
                                                 .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loEsclAssignee1 != null)
      {
         return loEsclAssignee1.getString();
      }
      else
      {
         return null;
      }
   }// end of method

   /**
    * Returns value of ESCL_ASGN_ROLE_IDx_y for given Escalation Level x
    * and Approval Level y
    *
    * @param fiEscalationLevel Escalation Level x for which value of
    *                ESCL_ASGN_ROLE_IDx_y is required
    * @param fiApprovalLevel Approval Level y for which value of
    *                ESCL_ASGN_ROLE_IDx_y is required
    */
   protected String getESCL_ASGN_ROLE_ID(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loEsclAssigneeRole = getData(moColumnName.append("ESCL_ASGN_ROLE_ID")
                                                    .append(fiEscalationLevel).append(UNDER_SCORE)
                                                    .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loEsclAssigneeRole != null)
      {
         return loEsclAssigneeRole.getString();
      }
      else
      {
         return null;
      }
   }// end of method

   /**
    * Returns value of ESCL_ASGN_USER_IDx_y for given Escalation Level x
    * and Approval Level y
    *
    * @param fiEscalationLevel Escalation Level x for which value of
    *                ESCL_ASGN_USER_IDx_y is required
    * @param fiApprovalLevel Approval Level y for which value of
    *                ESCL_ASGN_USER_IDx_y is required
    */
   protected String getESCL_ASGN_USER_ID(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loEsclAssigneeUser = getData(moColumnName.append("ESCL_ASGN_USER_ID")
                                                    .append(fiEscalationLevel).append(UNDER_SCORE)
                                                    .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loEsclAssigneeUser != null)
      {
         return loEsclAssigneeUser.getString();
      }
      else
      {
         return null;
      }
   }// end of method

   /**
    * Returns ESCL_ASGN_ROLE_FLx_y for given Escalation Level x
    * and Approval Level y
    *
    * @param fiEscalationLevel Escalation Level x for which value of
    *                ESCL_ASGN_ROLE_FLx_y is required
    * @param fiApprovalLevel Approval level y for which value of
    *                ESCL_ASGN_ROLE_FLx_y is required
    */
   protected boolean getESCL_ASGN_ROLE_FL(int fiEscalationLevel, int fiApprovalLevel)
   {
      // Handle case of Approval Level > 10
      fiApprovalLevel = getIndexFromAprvLevel(fiApprovalLevel);

      clearColNameBuilder();

      Data loEsclAssigneeRoleFl = getData(moColumnName.append("ESCL_ASGN_ROLE_FL")
                                                    .append(fiEscalationLevel).append(UNDER_SCORE)
                                                    .append(fiApprovalLevel).toString());
      clearColNameBuilder();
      if(loEsclAssigneeRoleFl != null)
      {
         return loEsclAssigneeRoleFl.getboolean();
      }
      else
      {
         return false;
      }
   }// end of method

   /**
    * Validates the value of WARN_THLD_AGE_x for Approval Level x= 1 to 10.
    * If the WARN_THLD_AGE is valid, WARN_ESCL_TMPL_ID is required.
    */
   protected void verifyWarningFieldsValid()
   {
      int liActualApprovalLevel = 0;
      int liWarningThldAge = 0;
      int liRoutingSequenceNo = 0;
      for(int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         liRoutingSequenceNo = getORD_NO(liAprvLvl);
         if (liRoutingSequenceNo > 0)
         {
            // Compute the actual Approval level depending on the SEQ_NO
            if (getSEQ_NO() > 1)
            {
               liActualApprovalLevel = liAprvLvl
                     + AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK;
            }
            else
            {
               liActualApprovalLevel = liAprvLvl;
            }

            // Get the Warning Threshold Age
            liWarningThldAge = getWARN_THLD_AGE(liAprvLvl);
            // Check Warning Threshold Age is not less than 1 if not empty
            if ( !AMSStringUtil.strIsEmpty(getData("WARN_THLD_AGE_"+liAprvLvl).getString())
                  && liWarningThldAge < 1)
            {
               clearErrorMsgBuilder();
               raiseException(moErrorMessage.append("%c:Q0188,v:[b:R_WF_APRV/WARN_THLD_AGE_")
                     .append(liAprvLvl).append("],v:").append(liActualApprovalLevel).append("%")
                     .toString());
            }
            // Check Warning Template ID is populated if Threshold Age > 0
            if (liWarningThldAge > 0 && AMSStringUtil.strIsEmpty(getWARN_ESCL_TMPL_ID(liAprvLvl)))
            {
               clearErrorMsgBuilder();
               raiseException(moErrorMessage.append("%c:Q0104,v:[b:R_WF_APRV/WARN_ESCL_TMPL_ID_")
                     .append(liAprvLvl).append("],v:").append(liActualApprovalLevel).append("%")
                     .toString());
            }
         }/* end for(int liAprvLvl = 1... */
      }
   }// end of method

   /**
    * Raises errors depending on value of ESCL_FL_x for each Approval Level.
    * If ESCL_FL_x for an Approval Level x is set to true,
    * error will be raised if either of the below fields are blank:
    * <ul>
    * <li>ESCL_THLD_AGE1_x, ESCL_THLD_AGE2_x
    * <li>ESCL_ASGN_ID1_x, ESCL_ASGN_ID2_x
    * <li>WARN_ESCL_TMPL_ID_x
    * </ul>
    *
    */
   protected void verifyEscalationFieldsReq()
   {
      int liActualApprovalLevel = 0;
      int liRoutingSequenceNo = 0;
      for(int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         liRoutingSequenceNo = getORD_NO(liAprvLvl);
         if (liRoutingSequenceNo > 0)
         {
            // Compute the actual Approval level depending on the SEQ_NO
            if (getSEQ_NO() == 2)
            {
               liActualApprovalLevel = liAprvLvl
                     + AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK;
            }
            else
            {
               liActualApprovalLevel = liAprvLvl;
            }

            // Raise exception if Escalate flag is true but related fields are
            // blank
            if (getESCL_FL(liAprvLvl))
            {
               // Check Escalate Threshold Age 1 is not less than 1
               if (getESCL_THLD_AGE(1, liAprvLvl) < 1)
               {
                  clearErrorMsgBuilder();
                  raiseException(moErrorMessage.append("%c:Q0188,v:[b:R_WF_APRV/ESCL_THLD_AGE1_")
                        .append(liAprvLvl).append("],v:").append(liActualApprovalLevel)
                        .append("%").toString());
               }

               // Check Escalate Assignee Id 1 is populated
               if (AMSStringUtil.strIsEmpty(getESCL_ASGN_ID(1, liAprvLvl)))
               {
                  clearErrorMsgBuilder();
                  raiseException(moErrorMessage.append("%c:Q0189,v:[b:R_WF_APRV/ESCL_ASGN_ID1_")
                        .append(liAprvLvl).append("],v:").append(liActualApprovalLevel)
                        .append("%").toString());
               }

               // Escalate Level 2 fields are required if Escalate Threshold Age
               // 2 is populated
               if (!AMSStringUtil.strIsEmpty(getData("ESCL_THLD_AGE2_" + liAprvLvl).getString()))
               {
                  // Check Escalate Threshold Age 2 is not less than 1
                  if (getESCL_THLD_AGE(2, liAprvLvl) < 1)
                  {
                     clearErrorMsgBuilder();
                     raiseException(moErrorMessage.append("%c:Q0188,v:[b:R_WF_APRV/ESCL_THLD_AGE2_")
                           .append(liAprvLvl).append("],v:").append(liActualApprovalLevel)
                           .append("%").toString());
                  }

                  // Check Escalate Assignee Id 2 is populated
                  if (AMSStringUtil.strIsEmpty(getESCL_ASGN_ID(2, liAprvLvl)))
                  {
                     clearErrorMsgBuilder();
                     raiseException(moErrorMessage.append("%c:Q0189,v:[b:R_WF_APRV/ESCL_ASGN_ID2_")
                           .append(liAprvLvl).append("],v:").append(liActualApprovalLevel)
                           .append("%").toString());
                  }
               }/* end if(getData("ESCL_THLD_AGE2_"... */

               // Check Warning Escalation Template Id is Populated
               if (AMSStringUtil.strIsEmpty(getWARN_ESCL_TMPL_ID(liAprvLvl)))
               {
                  clearErrorMsgBuilder();
                  raiseException(moErrorMessage.append("%c:Q0104,v:[b:R_WF_APRV/WARN_ESCL_TMPL_ID_")
                        .append(liAprvLvl).append("],v:").append(liActualApprovalLevel)
                        .append("%").toString());
               }
            }/*end if (getESCL_FL(liAprvLvl))*/
         }
      }/*end for(int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)*/
   }// end of method

   /**
    * Verifies if the Warning Threshold Age is less than Escalation Threshold
    * Age 1 which should be less than Escalation Threshold Age 2
    */
   protected void compareWarningEsclThresholdAge()
   {
      int liActualApprovalLevel = 0;
      // Initialize these to 0.
      int liWarnThldAge = 0;
      int liEsclThldAge1 = 0;
      int liEsclThldAge2 = 0;
      int liRoutingSequenceNo = 0;
      for(int liAprvLvl = 1; liAprvLvl <= MAX_APR_LVL_IN_ONE_BLOCK; liAprvLvl++)
      {
         liRoutingSequenceNo = getORD_NO(liAprvLvl);
         // Perform the validations only if Routing Seq > 1
         if (liRoutingSequenceNo > 0)
         {
            // Compute the actual Approval level depending on the SEQ_NO
            if (getSEQ_NO() == 2)
            {
               liActualApprovalLevel = liAprvLvl
                     + AMSCommonConstants.MAX_APR_LVL_IN_ONE_BLOCK;
            }
            else
            {
               liActualApprovalLevel = liAprvLvl;
            }

            liWarnThldAge = getWARN_THLD_AGE(liAprvLvl);

            // Validate the Threshold Ages are in correct order
            if (liWarnThldAge > 0 && getESCL_FL(liAprvLvl))
            {
               liEsclThldAge1 = getESCL_THLD_AGE(1, liAprvLvl);
               liEsclThldAge2 = getESCL_THLD_AGE(2, liAprvLvl);
               // Raise error if Warning Threshold Age is greater than
               // Escalation Threshold Age 1
               if (liWarnThldAge >= liEsclThldAge1)
               {
                  clearErrorMsgBuilder();
                  raiseException(moErrorMessage.append("%c:Q0191,v:[b:R_WF_APRV/ESCL_THLD_AGE1_")
                        .append(liAprvLvl).append("],v:[b:R_WF_APRV/WARN_THLD_AGE_")
                        .append(liAprvLvl).append("],v:").append(liActualApprovalLevel).append("%")
                        .toString());
               }/* end if(liWarnThldAge > liEsclThldAge1) */
               // Raise error if Escalation Threshold Age 1 is greater than
               // Escalation Threshold Age 2
               if (!AMSStringUtil.strIsEmpty(getData("ESCL_THLD_AGE2_" + liAprvLvl).getString()) &&
                     liEsclThldAge1 >= liEsclThldAge2)
               {
                  clearErrorMsgBuilder();
                  raiseException(moErrorMessage.append("%c:Q0191,v:[b:R_WF_APRV/ESCL_THLD_AGE2_")
                        .append(liAprvLvl).append("],v:[b:R_WF_APRV/ESCL_THLD_AGE1_")
                        .append(liAprvLvl).append("],v:").append(liActualApprovalLevel).append("%")
                        .toString());
               }/* end if(liEsclThldAge1 > liEsclThldAge2) */
            }/*end if(getWARN_NOTIF_FL(liAprvLvl)...*/
         }
      }/*end for(int liAprvLvl = 1...*/
   }// end of method

   /** Clears out the Column Name Builder */
   private void clearColNameBuilder()
   {
      moColumnName.delete(0, moColumnName.length());
   }// end of method

   /** Clears out the Error Message Builder */
   private void clearErrorMsgBuilder()
   {
      moErrorMessage.delete(0, moErrorMessage.length());
   }



      /**
       * Retrieves the PRIORITY_x value where x is the Approval Level.
       * It uses getData to get the Priority value for the appropriate level.
       *
       * Case of Approval Level of 0 will not occur.
       * If in case it does, then it returns the default Priority of Normal.
       *
       * @param fiAprvLvl The approval level (x) for the PRIORITY_x value to get.
       * @return int PRIORITY_x value
       *
       */
      public int getPRIORITY(int fiAprvLvl)
      {

         // Handle approval level > 10
         int liIndex = getIndexFromAprvLevel(fiAprvLvl);

         // Case of Approval Level 0 should not occur.
         // If in case it does, then just return the default Priority.
         if(liIndex==0)
         {
            return CVL_WF_PRIORITYImpl.NORMAL;
         }
         else
         {
            return getData("PRIORITY_" + liIndex).getint();
         }

      }


      /**
       * Set the PRIORITY_x value where x is the Approval Level.
       *
       * Case of Approval Level of 0 will not occur.
       * If in case it does, then it does nothing.
       *
       * @param fiIndex The Approval Level (x) for the PRIORITY_x value to set.
       * @param fiValue The Priority value to be set.
       *
       */
      public void setPRIORITY( int fiIndex, int fiValue )
      {
         //Aprv Lvl Change: code added to handle approval level > 10
         int liIndex;
         liIndex = getIndexFromAprvLevel(fiIndex);
         if(fiIndex==0)
         {
            return;
         }
         getData("PRIORITY_" + liIndex).setint(fiValue);
      }




}// end of class
