package ctu.nengoros.network.node.infrastructure.rosparam.manager;

import java.util.LinkedList;


/**
 * Temporal parameter list, just stores the parameters names, default values and descriptions.
 * Each HANNS node should have this and be able to print its parameters (usability).
 * 
 * This class will hold and provide access to the list of {@link ctu.nengoros.network.node.infrastructure.rosparam.manager.ParamDescription}s.
 * These should be provided by any {@link  ctu.nengoros.network.node.HannsNode}.
 * 
 * TODO this class should provide:
 * -storing parameter properties in the XML file
 * -parsing XML to the param list
 * -XML should include also IO definition
 * -this class should also support auto-generating of jython scripts
 * 
 * @author Jaroslav Vitku
 *
 */
public class ParamList {

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
	
	/**
	 * Print parameters to the console.
	 */
	public void printParams(){
		String line = "---------------------";
		String intro = line+" Available parameters are: ";
		String outro = line+line+line;
		System.out.println(intro+"\n"+this.listParams()+"\n"+outro);
	}
	
}
