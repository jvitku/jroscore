package ctu.nengoros.nodes;

/**
 * Each HANNS node should be equipped with the following functionalities:
 * <ul>
 * <li>Provide information about its prosperity. This can be any 
 * measure defining how successful the algorithm is in a given 
 * architecture.</li>
 * <li>Accept the importance parameter, this defines how important
 * is the solution found by the algorithm in the current situation. 
 * That is: 1~now compute, 0~now there is room for learning e.g. 
 * by exploration.</li>
 * </ul>
 * 
 * @author Jaroslav Vitku
 *
 */
public interface HannsNode {
	
	/**
	 * Get array of parameter descriptions, each HANNS Node should be able
	 * to return this in order to simplify its usage.
	 * 
	 * @return list of parameter descriptions
	 * @see ctu.nengoros.rosparam.manager.ParamDescription
	 */
	// TODO this method and better parameter support
	//public ParamDescription[] getParams();
	
	/**
	 * Write all parameters as string 
	 * This is temporary solution until better support for loading/storing parameters will be added.
	 * @return each line could correspond to one parameter explained
	 */
	public String listParams();

	/**
	 * Return the average value of prosperity of the algorithm
	 * in the current architecture. This value defines how successful 
	 * the algorithm is (according to some chosen measure). 
	 * @return value defining how successful the algorithm is
	 * according to some selected measure
	 */
	public float getProsperity();

	/**
	 * Defines how important is the solution found by the 
	 * algorithm in a given situation. The value is from the 
	 * interval [0,1]. 1 means that the solution is important
	 * and the algorithm should not explore at all. Rather, the 
	 * most efficient solution is important. 
	 *  
	 * @param importance how important is the algorithms solution now
	 */
	public void setImportance(float importance);
}

