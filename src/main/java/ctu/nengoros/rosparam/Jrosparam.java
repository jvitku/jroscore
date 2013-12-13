package ctu.nengoros.rosparam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ros.node.NodeMain;

import ctu.nengoros.RosRunner;

/**
 * This is runnable application which implements subset of 
 * functionalities of the original rosparam application
 * @see http://wiki.ros.org/rosparam
 * 
 * 
 * For the first time, it can be used as normal rosparam, so, e.g.:
 * -rosparam set <key> <val>
 * -rosparam get <key>
 * -rosparam list
 * -rosparam delete <key>
 * 
 * but the application stays on and accepts further commands without "jrospram" string, e.g.
 *  set <ket> <val>
 *  list  ..etc
 *  
 * 
 * @author Jaroslav Vitku
 *
 */
public class Jrosparam {
	
	static final String note = "Jrosparam, a Java implementation of rosparam is a command-line tool" +
			"for getting, setting and deleting parameters from the ROS master (par.server)";

	static final String cmds = "Usage: use jrosparam in the same way as rosparam, e.g.:\n" +
			"\tjrosparam set\t <key> <val>\n"+
			"\tjrosparam get\t <key>\n"+
			"\tjrosparam load\t load parameters from file\t[NOT SUPPORTED]\n"+
			"\tjrosparam dump\t dump parameters to file\t[NOT SUPPORTED]\n"+
			"\tjrosparam delete <key>\n"+
			"\tjrosparam list\t list parameter names";

	static final String footnote
	= "\n Note: after launching the app, use shorter commands, e.g.:\n\tset <key> <val>";

	private final RosparamInt par;

	public static final String listEmpty= "No params!";
	public static final String notFound = "Parameter not found!";
	public static final String unsupported= "Unsupported command!";
	public static final String tooLong = "Error: Command too long!";
	
	public Jrosparam(RosparamInt p){
		this.par = p;
	}

	public static void main(String[] args){
		if(!argsOK(args))
			return;

		RosRunner r;
		try {
			r = new RosRunner(new String[]{"ctu.nengoros.rosparam.RosparamNode","/use_sim_time:=false"});
		} catch (Exception e) {
			System.err.println("ERROR: could not start the rosparam node! Roscore launched??");
			e.printStackTrace();
			return;
		}
		r.start();

		if(!r.isRunning()){ 
			if(r.nodeListener.getConnectionRefused()){
				System.out.println("\n\n Error: Unable to communicate with master!");
			}
			// TODO: if the core is not started, this process has to be killed manually for now..
			r.stop();
			return;
		}
		NodeMain node = r.getNode();

		Jrosparam jr = new Jrosparam((RosparamInt)node);

		if(args.length == 0){
			System.out.println("\nReady...\n");
		}else{
			System.out.println(jr.processCommand(args));
		}

		while(true){
			jr.waitForCommand();
		}
	}

	private void waitForCommand(){
		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String querry = null;
		String[] list = null;
		try {
			querry = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your command!");
			printUsage();
		}
		list = querry.split(" ");
		String result = processCommand(list);
		if(result.length()>0)
			System.out.println(processCommand(list));
	}

	public String processCommand(String[] list){
		if(list.length == 0)
			return "";
		
		this.awaitParameterTreeObtained();

		if(list.length == 1){
			if(list[0].equalsIgnoreCase("list")){
				return par.printTree();
			}
			if(list[0].equalsIgnoreCase("h") || list[0].equalsIgnoreCase("help")){
				String out = "-------------------------\n";
				out = out + getUsage();
				out = out+ "-------------------------\n";
				return out;
			}
			return unsupported;
		}
		if(list.length == 2){
			if(list[0].equalsIgnoreCase("delete")){
				par.delete(list[1]);
				return "";
			}
			if(list[0].equalsIgnoreCase("get")){
				try {
					return par.getStringValofParam(list[1]);
				} catch (Exception e) {
					return notFound;
				}
			}
			return unsupported;
		}
		if(list.length == 3){
			if(list[0].equalsIgnoreCase("set")){
				if(list[2].equalsIgnoreCase("true")){
					par.set(list[1], true);
					return "";
				}
				if(list[2].equalsIgnoreCase("false")){
					par.set(list[1], false);
					return "";
				}
				try{
					Integer i = Integer.parseInt(list[2]);
					par.set(list[1], i);
					return "";
				}catch(Exception e){ 
				}
				try{
					Double d = Double.parseDouble(list[2]);
					par.set(list[1], d);
					return "";
				}catch(Exception e){
				}
				par.set(list[1], list[2]);
				return "";
			}
			return unsupported;
		}
		return tooLong;
	}

	/**
	 * Here was a tiny NullPointer problem (probably slow communication with the master)
	 */
	private void awaitParameterTreeObtained(){
		while(this.par == null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean argsOK(String[] args){
		// need help?
		if(args.length == 1 && (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help"))){
			printUsage();
			return false;
		}

		if(args.length>3){
			System.err.println("ERROR: too many arguments specified!\n");
			printUsage();
			return false;
		}
		return true;
	}

	private static void printUsage(){
		System.out.println(getUsage());
	}
	
	public static String getUsage(){
		String out = note+"\n"+cmds+"\n"+footnote+"\n";
		return out;
	}
}
