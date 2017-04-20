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
	
	/*
	**  CVL_DOC_PHASE_CD
	*/
	
	//{{COMPONENT_RULES_CLASS_DECL
	public class CVL_DOC_PHASE_CDImpl extends  CVL_DOC_PHASE_CDBaseImpl
	
	
	//END_COMPONENT_RULES_CLASS_DECL}}
	{
	   // Constants for CVL_DOC_PHASE_CD Stored Value
	   public static final int NOPHASE = 0;
	   public static final int DRAFT = 1;
	   public static final int PENDING = 2;
	   public static final int FINAL = 3;
	   public static final int HISTORICALPENDING = 4;
	   public static final int HISTORICALFINAL = 5;
	   public static final int CONFLICTDRAFT = 6;
	   public static final int TEMPLATE = 7;
	   public static final int ALL_DOC_PHASES = 99;
	
		//{{COMP_CLASS_CTOR
		public CVL_DOC_PHASE_CDImpl (){
			super();
		}
		
		public CVL_DOC_PHASE_CDImpl(Session session, boolean makeDefaults)
		{
			super(session, makeDefaults);
		
		
		
		
		//END_COMP_CLASS_CTOR}}
	
		}
	
	
		//{{COMPONENT_RULES
			public static CVL_DOC_PHASE_CDImpl getNewObject(Session session, boolean makeDefaults)
			{
				return new CVL_DOC_PHASE_CDImpl(session, makeDefaults);
			}	
		
		//END_COMPONENT_RULES}}
	
		//{{EVENT_CODE
		
		//END_EVENT_CODE}}
	
		public void addListeners() {
			//{{EVENT_ADD_LISTENERS
			
			//END_EVENT_ADD_LISTENERS}}
		}
        
        
//      Workers Comp
        
        
        public static String getDocPhaseString(int fiDocPhCd)
        {
            String lsDocPhStr = " ";
            
            if(fiDocPhCd==0)
            {
                lsDocPhStr="NOPHASE"; 
            }
            
            else if(fiDocPhCd==1)
            {
                lsDocPhStr="DRAFT";
            }
            else if(fiDocPhCd==2)
            {
                lsDocPhStr="PENDING";
                
            }
            else if(fiDocPhCd==3)
            {
                lsDocPhStr="FINAL";
            }
            else if(fiDocPhCd==4)
            {
                lsDocPhStr="HISTORICALPENDING";
            }
            else if(fiDocPhCd==5)
            {
                lsDocPhStr="HISTORICALFINAL";
            }
            else if(fiDocPhCd==6)
            {
                lsDocPhStr="CONFLICTDRAFT";
            }
            else if(fiDocPhCd==7)
            {
                lsDocPhStr="TEMPLATE";
            }
            else if(fiDocPhCd==99)
            {
                lsDocPhStr="ALL_DOC_PHASES";
            }
            
            return lsDocPhStr;
        }
        
        //Workers Comp
        
        
        
	}
	