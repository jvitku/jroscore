package ctu.nengoros.network.node.infrastructure.simulation;

/**
 * Defines messages that should be supported by particular 
 * {@link ctu.nengoros.network.node.ConfigurableHannsNode}s.
 *  
 * @author Jaroslav Vitku
 *
 */
public class Messages {

	public static final String SIMULATOR_TOPIC = "simulation";

	public static final String HARD_RESET = "hardReset";

	public static final String SOFT_RESET = "softReset";

	public static final String[] COMMANDS = new String[]{HARD_RESET, SOFT_RESET};

	/**
	 * Check if a given command is recognized (if is contained in the {@link #COMMANDS}. 
	 * array) 
	 * @param command command to be checked
	 * @return true if command found and can be processed
	 */
	public static boolean commandIsRecognized(String command){
		for(int i=0; i<COMMANDS.length; i++){
			if(COMMANDS[i].equalsIgnoreCase(command))
				return true;
		}
		return false;
	}

}
