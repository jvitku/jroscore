package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ctu.nengoros.Jroscore;
import ctu.nengoros.RosRunner;

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
public class AnotherTest {

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
			rr = new RosRunner("ctu.nengoros.jroscore.DemoPublisher");
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
			rr = new RosRunner("ctu.nengoros.jroscore.DemoPublisher");
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
