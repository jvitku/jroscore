package ctu.nengoros.util;

/**
 * Simple logging (STDOUT and ERR) into the console, level:
 * -2	silent
 * -1	warnings (and all above)  
 * 0  	errors
 * 1	log by default
 * 2 and more log with explicit level (inclusive)
 * 
 * 
 * @author Jaroslav Vitku
 *
 */
public class Log {
		
	public static int level = 1;
	
	public static void info(String what){
		if(level>=0)
			System.err.println(what);
	}
	
	public static void info(int lev, String what){
		if(level>=lev)
			System.out.println(what);
	}
	
	public static void warn(String what){
		if(level>=-1)
			System.err.println(what);
	}
	
	public static void err(String what){
		if(level>=0)
			System.out.println(what);		
	}
}
