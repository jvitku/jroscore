package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.nodes.RosCommunicationTest;


/**
 * This tests several things:
 * 	-How the @BeforeClass and @AfterClass methods work as expected (used for launching the Core)
 * 	-Whether Jroscore can be launched and shut down from program.
 * 	-Whether a RosNode can be launched and stopped manually.
 *  -Whether test methods are executed sequentially (they are sleeping, so obviously..)
 *  -Whether two JUnit Class files (@see AnotherTest) are executed sequentially.
 * @author Jaroslav Vitku
 *
 */
public class JroscoreRosRunner extends RosCommunicationTest{

	
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
