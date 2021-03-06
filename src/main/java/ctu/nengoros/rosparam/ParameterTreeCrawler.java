package ctu.nengoros.rosparam;

import java.util.Collection;
import java.util.Map;

import org.ros.namespace.GraphName;
import org.ros.node.parameter.ParameterTree;

/**
 * Helper class for printing out parameterTree contents. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class ParameterTreeCrawler {

	public final String me="[ParameterTreeCrawler] ";
	private final ParameterTree t;

	public ParameterTreeCrawler(ParameterTree par){
		t = par;
	}

	public void printAll(){
		System.out.println(this.getAll());
	}
	
	public String getAll(){
		return me+getAllS();
	}
	
	/**
	 * Get all parameters without printing to console
	 * @return list of all parameters 
	 */
	public String getAllS(){

		if(isEmpty())
			return "No params!";

		Collection<GraphName> l = t.getNames();
		GraphName[] g = l.toArray(new GraphName[0]);

		String out = "------------------------- parameters are:";
		for(int i=0; i<g.length; i++){
			try {
				out = out +"\nParam: "+g[i]+"  \tval: "+this.getParam(g[i]);
			} catch (Exception e) {
				System.err.println("I found name which is not in the paramList, rosjava error?");
			}
		}
		out = out +"\n------------------------- ";
		return out;
	}

	/**
	 * Since we do not know the type of the parameter, 
	 * test the most common ones, 
	 * @see <a href="http://docs.rosjava.googlecode.com/hg/rosjava_core/html/getting_started.html">rosjava_core docs</a>
	 * 
	 * @param name name of the parameter
	 * @return value of the parameter casted to string, null if no key found
	 */
	public String getParam(GraphName name) throws Exception{
		
		if(!t.has(name))
			return null;
		
		for(int i=0;i<=nofcns; i++){
			try{
				return gp(name,i);
			}catch(Exception e){
				//e.printStackTrace();
			}
		}
		throw new Exception();
	}

	int nofcns=3;

	private String gp(GraphName name, int no) throws Exception{
		if(no<0 || no>nofcns){
			System.err.println("bad numner");
			return null;
		}
		switch(no){
		case 0:
			return t.getString(name);
		case 1:
			return String.valueOf(t.getDouble(name));
		case 2:
			return String.valueOf(t.getInteger(name));
		case 3:
			return String.valueOf(t.getBoolean(name));
		}
		throw new Exception();
	}
	

	public String printNames(){
		return me + this.printNamesS();
	}
	
	public String printNamesS(){
		if(isEmpty())
			return "Param tree empty";

		Collection<GraphName> l = t.getNames();
		GraphName[] g = l.toArray(new GraphName[0]);

		String out = "------------------------- names:\n";
		for(int i=0; i<g.length; i++){
			out = out + " "+g[i]+" || ";
		}
		return out +"\n-------------------------------";
	}

	private boolean isEmpty(){
		if(t.getNames().isEmpty()){
			return true;
		}
		return false;
	}
	
	public String getAllRemapps(Map<GraphName,GraphName> remaps){
		return me+this.getAllRemappsS(remaps);
	}
	
	public String getAllRemappsS(Map<GraphName,GraphName> remaps){
		if(remaps.isEmpty()){
			return "No remappings!";
		}
		//GraphName[] names = null;
		GraphName []names = remaps.keySet().toArray(new GraphName[0]);
		String out = "Remappings:\n";
		for(int i=0; i<names.length; i++){
			out = out + i+"name: "+names[i]+" -> "+remaps.get(names[i])+"\n";
		}
		return out +"------------------------------";
	}
	
	
}






