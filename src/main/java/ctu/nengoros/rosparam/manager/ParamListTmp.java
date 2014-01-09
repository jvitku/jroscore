package ctu.nengoros.rosparam.manager;

import java.util.LinkedList;


/**
 * Temporal parameter list, just stores the parameters names, default values and descriptions.
 * Each HANNS node should have this and be able to print its parameters (usability).
 * 
 * @author Jaroslav Vitku
 *
 */
public class ParamListTmp {

	private LinkedList<Param> params = new LinkedList<Param>();

	public String listParams(){
		String out = null;
		Param[] p = new Param[params.size()];
		p = params.toArray(p);
		for(int i=0; i<p.length; i++){
			if(out ==null){
				out = p[i].name+":="+p[i].def+"\t\t"+p[i].descr;
			}else{
				out = out +"\n"+p[i].name+":="+p[i].def+"\t\t"+p[i].descr;
			}
		}
		return out; 
	}

	/**
	 * Add new parameter to the list, no name duplicate checking is done.
	 * @param name name of the parameter
	 * @param def default value
	 * @param descr short description of the parameters purpose
	 */
	public void addParam(String name, String def, String descr){
		params.add(new Param(name, descr, def));
	}

	private class Param{
		public final String name;
		public final String descr;
		public final String def;
		public Param(String name, String descr, String def){
			this.name = "_"+name;
			this.descr = descr;
			this.def = def;
		}
	}

}