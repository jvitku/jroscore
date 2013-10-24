package ctu.nengoros.jroscore;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.Jroscore;

public class JroscoreTest {

	@Test
	public void test() {
		
		Jroscore jr = new Jroscore();
		jr.parseURI(new String[]{});
		
		jr.start();
		assertTrue(jr.isRunning());
		
		jr.shutDown();
		assertFalse(jr.isRunning());
	}
}
