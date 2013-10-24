package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ctu.nengoros.Jroscore;

public class JroscoreTest {

	Jroscore jr;
	
	@Before
	public void startCore(){
		jr = new Jroscore();
		jr.parseURI(new String[]{});
		
		jr.start();
		assertTrue(jr.isRunning());
	}
	
	@Test
	public void test() {
		assertTrue(jr.isRunning());
	}
	
	@After
	public void stopCore(){
		jr.shutDown();
		assertFalse(jr.isRunning());
	}
}