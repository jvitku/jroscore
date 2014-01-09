package ctu.nengoros.util;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
/**
 * SL stands for SimpleLogger, which implements very simple unbuffered (file) logger.
 * The logger features several toString methods which simplify logging the information.
 * Usage:
 * <ul>
 * <li>Initialize the class with the name (console is used by default)</li>
 * <li>Use methods such as {@link #pl(String)} to log on default level, 
 * {@link #errl(String)} to print errors or {@link #warnl(String)} to print out warnings.</li>
 * <li>Call {@link #end()} when done</li>
 * </ul>
 * 
 * The method {@link #setLogEnabled(boolean)} can disable logging globally, 
 * the method {@link #setLevel(int)} sets the default level for logging. 
 * Logging levels are defined as follows:  
 * <ul>
 * <li>0: errors</li>
 * <li>1: warnings+errors</li>
 * <li>10: default level for the logger; all messages will be logged</li>
 * </ul> 
 * 
 * The class also contains static equivalents of the methods described above, these 
 * mthods support only logging into the console.
 * 
 * @author Jaroslav Vitku
 */
public class SL {
	
	
	// default level to be used for logging if not changed
	public static final int DEFLEVEL = 10; 	// default level used for logging

	public static final int SILENT = -1;	// no log at all
	public static final int ERRORS = 0;		// log only errors
	public static final int WARN = 1;		// log errors and warnings			
	public static final int DEBUG = 10;		// log errors, warnings and everything with level lower or equal to this value

	public final boolean defuseconsole = true;	// console used by default

	// logging
	private File file;
	private FileWriter flog;
	private String name;

	private boolean write;			// disable printing at all?
	private boolean useConsole;		// use console of file?
	private int level;				// the logging level that is currently used 

	/**
	 * Creates instance of the logger, the logger uses console by default.
	 * Note: writing to the file must be set using the printToFile method
	 * 
	 * @param name - name of file where to write out logs
	 */
	public SL(String name){
		this.init(name, defuseconsole, DEFLEVEL);
	}

	/**
	 * Creates instance of the logger where writing to console/file can be chosen.
	 * @param name name of the logger or file that stores the log (of writeFile is true)
	 * @param useConsole if false, the log will be dumped into the file of given name 
	 */
	public SL(String name, boolean useConsole){
		this.init(name, useConsole, DEFLEVEL);
	}

	/**
	 * Create instance of the logger, with where writing into the console/file
	 * can be selected and where default level of logging can be chosen.
	 * @param name name of file/logger
	 * @param useConsole whether to use console (or file)
	 * @param defLevel default level used by the methods 
	 */
	public SL(String name, boolean useConsole, int defLevel){
		this.init(name, useConsole, defLevel);
	}


	/**
	 * Actual initialization. 
	 * @param name name of the logger, or/and name of the file to write
	 * @param useConsole use console? if false, the file will be written
	 * @param defaultLevel default logging level
	 */
	private void init(String name, boolean useConsole, int defaultLevel){
		this.name = name;
		this.useConsole = useConsole;
		this.level = defaultLevel;
		this.write = true;
		
		if(!this.useConsole){
			this.initFile();
		}
	}

	private void initFile(){
		// create file, write to it
		file = new File(name);
		// try to create new file writer with given name
		try {
			// append-true: append strings to the end of existing file
			flog= new FileWriter(file,false);
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Set whether to print to file or to use the console. 
	 * Can be switched during the use of logger. 
	 * @param c print to file?
	 */
	public void printToFile(boolean c){
		if(c && this.file == null){
			this.initFile();
		}
		this.useConsole = !c;
	}

	/**
	 * Set default level for the printing into the console.
	 * Level of 0 means totally silent  
	 * @param lev
	 */
	public void setLevel(int lev){ this.level = lev; }
	public int getLevel(){ return this.level; }
	public void setLogEnabled(boolean enabled){ this.write = enabled; }

	private void write(String s, int lev){
		if(write){
			// if the message is important enough
			if(lev<=this.level){

				if(useConsole)
					System.out.print(/*"l"+lev+"| "+*/s);
				else
					try{ 
						flog.write(/*"l"+lev+"| "+*/s);
						flog.flush();
					}
				catch(IOException e){ e.printStackTrace(); }
			}
		}
	}

	private static void swrite(String s, int lev){
		// if the message is important enough
		if(lev<=DEFLEVEL){
			System.out.print(/*"l"+lev+"| "+*/s);
		}
	}

	/**
	 * System.out.print
	 * @param s String to be printed out with the default level
	 */
	public void p(String s){
		this.write(s, this.level);
	}

	/**
	 * System.out.println
	 * @param s String to be printed out with the default level, newline added
	 */
	public void pl(String s){
		this.write(s+"\n", this.level);
	}

	/**
	 * System.err.print
	 * @param s String to be printed out to the out stream (or file) with 
	 * the ERRORS level. The String 'ERROR: ' is added on the beginning of the message.
	 */
	public void err(String s){
		this.write("ERROR: "+s,ERRORS);
	}

	/**
	 * System.err.println
	 * @param s String to be printed out to the out stream (or file) with 
	 * the ERRORS level. The String 'ERROR: ' is added on the beginning of the message, 
	 * newline added at the end.
	 */
	public void errl(String s){
		this.write("ERROR: "+s+"\n",ERRORS);
	}
	

	/**
	 * Prints out warning without newline
	 * @param s String to be printed out to the out stream (or file) with 
	 * the ERRORS level. The String 'WARNING: ' is added on the beginning of the message.
	 */
	public void warn(String s){
		this.write("WARNING: "+s,WARN);
	}
	

	/**
	 * Prints out warning
	 * @param s String to be printed out to the out stream (or file) with 
	 * the ERRORS level. The String 'WARNING: ' is added on the beginning of the message, 
	 * newline added at the end.
	 */
	public void warnl(String s){
		this.write("WARNING: "+s+"\n",WARN);
	}
	
	/**
	 * Static equivalent of {@link #p(String)} which prints only into console. 
	 * @param s String to be printed out with the default level
	 */
	public static void sinfo(String s){
		swrite(s, DEFLEVEL);
	}

	/**
	 * Static equivalent of {@link #pl(String)} which prints only into console.
	 * @param s String to be printed out with the default level, newline added
	 */
	public static void sinfol(String s){
		swrite(s+"\n", DEFLEVEL);
	}

	/**
	 * Static equivalent of {@link #err(String)} which prints only into console.
	 * @param s String to be printed out to the out stream (or file) with 
	 * the ERRORS level. The String 'ERROR: ' is added on the beginning of the message.
	 */
	public static void serr(String s){
		swrite("ERROR: "+s,ERRORS);
	}

	/**
	 * Static equivalent of {@link #errl(String)} which prints only into console.
	 * @param s String to be printed out to the out stream (or file) with 
	 * the ERRORS level. The String 'ERROR: ' is added on the beginning of the message, 
	 * newline added at the end.
	 */
	public static void serrl(String s){
		swrite("ERROR: "+s+"\n",ERRORS);
	}
	
	/**
	 * Static equivalent of {@link #warnl(String)} which prints only into console
	 * @param s String to be printed with 'WARNING:' appended at the beginning and newline appended at the end.
	 */
	public static void swarnl(String s){
		swrite("WARNING: "+s+"\n",WARN);
	}

	/**
	 * Static equivalent of {@link #warn(String)} which prints only into console
	 * @param s String to be printed with 'WARNING:' appended at the beginning.
	 */
	public static void swarn(String s){
		swrite("WARNING: "+s,WARN);
	}

	public void p(String cn, int lev, String s){
		this.write("lev: "+lev+" class "+cn+":\t"+s, lev);
	}

	public void pl(String cn, int lev, String s){
		this.write("lev: "+lev+" class "+cn+":\t"+s+"\n", lev);
	}

	public void p(int LEV, String s){
		this.write(s, LEV);
	}

	public void pl(int LEV, String s){
		this.write(s+"\n", LEV);
	}

	public void err(String cn, String s){
		this.write("ERROR: class "+cn+":\t"+s+"\n", 0);
	}

	public void end(){
		if(write && this.file!= null){
			try {
				flog.write("Closing the output stream, bye");
				flog.flush();
				flog.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Helper stuff goes here -----------------------------
	 */
	

	/**
	 * Convert array of integers to readable format
	 * @param array input array to be returned
	 * @return human readable string representation of the array 
	 */
	public static String toStr(int[] array){
		String out = "[";
		for(int i=0; i<array.length; i++){
			if(i==0){
				out = out +array[i];
			}else{
				out = out + ", "+array[i];	
			}
		}
		return out+"]";
	}

	/**
	 * Convert array of Doubles to readable format
	 * @param array input array to be returned
	 * @return human readable String representation of the array 
	 */
	public static String toStr(Double[] array){
		String out = "[";
		for(int i=0; i<array.length; i++){
			if(i==0){
				out = out +array[i];
			}else{
				out = out + ", "+array[i];	
			}
		}
		return out+"]";
	}
	
	public static String toStr(float[] array){
		String out = "[";
		for(int i=0; i<array.length; i++){
			if(i==0){
				out = out +array[i];
			}else{
				out = out + ", "+array[i];	
			}
		}
		return out+"]";
	}
	
	public static String toStr(double[] array){
		String out = "[";
		for(int i=0; i<array.length; i++){
			if(i==0){
				out = out +array[i];
			}else{
				out = out + ", "+array[i];	
			}
		}
		return out+"]";
	}

	public static String toStr(String[] array){
		String out = "[";
		for(int i=0; i<array.length; i++){
			if(i==0){
				out = out +array[i];
			}else{
				out = out + ", "+array[i];	
			}
		}
		return out+"]";
	}
	
	
}

