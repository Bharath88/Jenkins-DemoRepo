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
					**  CVL_INFR_VALD
					*/
					
					//{{COMPONENT_RULES_CLASS_DECL
					public class CVL_INFR_VALDImpl extends  CVL_INFR_VALDBaseImpl
					
					
					//END_COMPONENT_RULES_CLASS_DECL}}
					{
					   public static final int INFERENCE=0;
					   public static final int COMBO_VALIDATION=1;
					
					
					//{{COMP_CLASS_CTOR
					public CVL_INFR_VALDImpl (){
						super();
					}
					
					public CVL_INFR_VALDImpl(Session session, boolean makeDefaults)
					{
						super(session, makeDefaults);
					
					
					
					
					//END_COMP_CLASS_CTOR}}
					
					   }
					
					//{{EVENT_CODE
					
					//END_EVENT_CODE}}
					
					
					
					   public void addListeners() {
						//{{EVENT_ADD_LISTENERS
						
						//END_EVENT_ADD_LISTENERS}}
					   }
					
					//{{COMPONENT_RULES
						public static CVL_INFR_VALDImpl getNewObject(Session session, boolean makeDefaults)
						{
							return new CVL_INFR_VALDImpl(session, makeDefaults);
						}	
					
					//END_COMPONENT_RULES}}
					
					}
					
					