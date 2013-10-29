package ctu.nengoros.testsuit;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ctu.nengoros.Jroscore;
import ctu.nengoros.RosRunner;


/**
 * Usually, JUnit tests which involve testing ROS nodes, will need a ROS core running
 * before any @Test is launched and need to automatically close the core. 
 * 
 * @author Jaroslav Vitku
 *
 */
public abstract class RosCommunicationTester {
	
	static Jroscore jr;
	
	/**
	 * Called before any unit @Test
	 */
	@BeforeClass
	public static void startCore(){
		//System.out.println("=============== Starting the Core to run the network tests!");
		jr = new Jroscore();

		assertFalse(jr.isRunning());
		jr.start();
		assertTrue(jr.isRunning());
	}

	/**
	 * Called after all unit @Test s
	 */
	@AfterClass
	public static void stopCore(){
		//System.out.println("=============== Stopping the Core after tests!");
		assertTrue(jr.isRunning());
		jr.shutDown();
		assertFalse(jr.isRunning());
	}
	
	/**
	 * Run a given node and check if it is running.
	 * 
	 * @param which name of the node
	 * @return RosRunner instance with running node
	 */
	public RosRunner runNode(String which){
		assertTrue(jr.isRunning());
		RosRunner rr = null;

		try {
			rr = new RosRunner(which);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Node named: "+which+" could not be launched..");
		}
		assertFalse(rr.isRunning());
		rr.start();
		assertTrue(rr.isRunning());
		return rr;
	}
	
	public void sleep(int howlong){
		try {
			Thread.sleep(howlong);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("could not sleep");
		}
	}
}
