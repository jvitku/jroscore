package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.nodes.RosCommunicationTest;

/**
 * The same test as the JroscoreRosRunner.
 * 
 * To show that two JUnit Tests Classes are executed by Gradle sequentially. 
 * Each JUnit launches own Jroscore with the default URI, so these two identical 
 * test Classes are not interfering <= serial execution. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class JroscoreRosRunnerII extends RosCommunicationTest{

	// run each node for ?ms
	final int nodeTimeRun = 200;
	
	@Test
	public void test() {
		RosRunner rr = null;
		try {
			rr = new RosRunner("ctu.nengoros.demoTests.nodes.DemoPublisher");
		} catch (Exception e1) {
			fail("No node name declared");
		}
		
		assertFalse(rr.isRunning());
		rr.start();
		assertTrue(rr.isRunning());
		
		sleep(nodeTimeRun);
		
		assertTrue(rr.isRunning());
		rr.stop();
		assertFalse(rr.isRunning());
	}

	@Test
	public void anotherTest() {
		RosRunner rr = null;
		try {
			rr = new RosRunner("ctu.nengoros.demoTests.nodes.DemoPublisher");
		} catch (Exception e1) {
			fail("No node name declared");
		}
		assertFalse(rr.isRunning());
		rr.start();
		assertTrue(rr.isRunning());
		
		sleep(nodeTimeRun);
	
		assertTrue(rr.isRunning());
		rr.stop();
		assertFalse(rr.isRunning());
	}

}
