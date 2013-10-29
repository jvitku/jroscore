package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ctu.nengoros.Jroscore;
import ctu.nengoros.RosRunner;


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
public class JroscoreRosRunner {

	static Jroscore jr;
	
	// run each node for ?ms
	final int nodeTimeRun = 500;
	
	@BeforeClass
	public static void startCore(){
		System.out.println("=============== Starting the core to run the network testing!!!!");
		jr = new Jroscore();
		
		assertFalse(jr.isRunning());
		jr.start();
		assertTrue(jr.isRunning());
	}

	private void sleep(int howlong){
		try {
			Thread.sleep(howlong);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("could not sleep");
		}
	}
	
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

	@AfterClass
	public static void stopCore(){
		System.out.println("=============== Stopping the core after tests!!!!");
		assertTrue(jr.isRunning());
		jr.shutDown();
		assertFalse(jr.isRunning());
	}
}
