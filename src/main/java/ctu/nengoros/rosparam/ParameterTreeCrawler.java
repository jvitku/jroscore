package ctu.nengoros.rosparam;

import java.util.Collection;
import java.util.Map;

import org.ros.namespace.GraphName;
import org.ros.node.parameter.ParameterTree;

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

		if(isEmpty())
			return me+"No params!";

		Collection<GraphName> l = t.getNames();
		GraphName[] g = l.toArray(new GraphName[0]);

		String out = me+"------------------------- parameters are:";
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
	 * SInce we do not know the type of the parameter, 
	 * test the most common ones, 
	 * @see http://docs.rosjava.googlecode.com/hg/rosjava_core/html/getting_started.html
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
	

	public void printNames(){
		if(isEmpty())
			return;

		Collection<GraphName> l = t.getNames();
		GraphName[] g = l.toArray(new GraphName[0]);

		System.out.println(me+"------------------------- names:");
		for(int i=0; i<g.length; i++){
			System.out.print(" "+g[i]);
		}
		System.out.println("\n-------------------------------");
	}

	private boolean isEmpty(){
		if(t.getNames().isEmpty()){
			return true;
		}
		return false;
	}
	
	public String getAllRemapps(Map<GraphName,GraphName> remaps){
		if(remaps.isEmpty()){
			return me+"No remappings!";
		}
			
		//GraphName[] names = null;
		GraphName []names = remaps.keySet().toArray(new GraphName[0]);
		String out = me+"Remappings:\n";
		for(int i=0; i<names.length; i++){
			out = out + i+"name: "+names[i]+" -> "+remaps.get(names[i])+"\n";
		}
		return out +"------------------------------";
	}
	
	
}






