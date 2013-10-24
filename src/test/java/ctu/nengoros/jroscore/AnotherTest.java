package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ctu.nengoros.Jroscore;
import ctu.nengoros.RosRunner;

/**
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
	final int nodeTimeRun = 1000;

	@BeforeClass
	public static void startCore(){
		System.out.println("=============== Starting the core to run the network testing!!!!");
		jr = new Jroscore();

		assertFalse(jr.isRunning());
		jr.start();
		assertTrue(jr.isRunning());
	}

	@Test
	public void test() {
		RosRunner rr = null;
		try {
			rr = new RosRunner("ctu.nengoros.jroscore.DemoPublisher");
		} catch (Exception e1) {
			fail("No node name declared");
		}
		rr.start();

		try {
			Thread.sleep(nodeTimeRun);
		} catch (InterruptedException e) {
			fail("could not sleep");
		}
		rr.stop();
	}

	@Test
	public void anotherTest() {
		RosRunner rr = null;
		try {
			rr = new RosRunner("ctu.nengoros.jroscore.DemoPublisher");
		} catch (Exception e1) {
			fail("No node name declared");
		}
		rr.start();

		try {
			Thread.sleep(nodeTimeRun);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("could not sleep");
		}
		rr.stop();
	}

	@AfterClass
	public static void stopCore(){
		System.out.println("=============== Stopping the core after tests!!!!");
		assertTrue(jr.isRunning());
		jr.shutDown();
		assertFalse(jr.isRunning());
	}
}
