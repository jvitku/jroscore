package ctu.nengoros.rosparam.manager;

import ctu.nengoros.util.SL;

/**
 * Parameter name, with its description and purpose. Used by the HannsNodes for printing out help.
 * 
 * @author Jaroslav Vitku
 *
 */
public class ParamDescription {
	
	// currently supported 
	public static final String[] types = new String[]{"int","float","double","String"};
	
	public final String name;
	public final String type;
	public String purpose;
	public final String defVal;
	
	public ParamDescription(String name, String type, String defVal, String purpose){
		this.name = name;
		if(!this.supported(type))
			System.err.println("This type ("+type+") is not supported !"
					+"Supported are: "+SL.toStr(types));
		
		this.type = type;
		this.defVal = defVal;	// TODO check allowed values
		this.purpose = purpose;
	}
	
	
	private boolean supported(String type){
		for(int i=0; i<types.length; i++)
			if(type.equalsIgnoreCase(types[i]))
				return true;
		return false;
	}

}
