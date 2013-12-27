package ctu.nengoros.nodes;

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
public abstract class RosCommunicationTest {

	public static final boolean coreAutorun = true;
	static Jroscore jr;

	/**
	 * Called before any unit @Test
	 */
	@BeforeClass
	public static void startCore(){
		//System.out.println("=============== Starting the Core to run the network tests!");
		if(coreAutorun){
			jr = new Jroscore();

			assertFalse(jr.isRunning());
			jr.start();
			assertTrue(jr.isRunning());
		}
	}

	/**
	 * Called after all unit @Test s
	 */
	@AfterClass
	public static void stopCore(){
		if(coreAutorun){
			//System.out.println("=============== Stopping the Core after tests!");
			assertTrue(jr.isRunning());
			jr.shutDown();
			assertFalse(jr.isRunning());
		}
	}


	/**
	 * Run a given node and check if it is running.
	 * 
	 * @param command complete node name and command line parameters
	 * @return RosRunner instance with running node
	 */
	public RosRunner runNode(String[] command){
		RosRunner rr = null;

		try {
			rr = new RosRunner(command);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Node named: "+command[0]+" could not be launched..");
		}
		assertFalse(rr.isRunning());
		rr.start();
		assertTrue(rr.isRunning());
		return rr;
	}
	
	public RosRunner runNode(String which){
		return runNode(new String[]{which});
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
