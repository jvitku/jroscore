package ctu.nengoros.rosparam.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ros.node.NodeMain;

import ctu.nengoros.RosRunner;
import ctu.nengoros.nodes.RosCommunicationTest;
import ctu.nengoros.rosparam.Jrosparam;
import ctu.nengoros.rosparam.RosparamInt;

/**
 * Test the Jrosparam functionalities
 * 
 * @author Jaroslav Vitku
 *
 */
public class SetGet extends RosCommunicationTest{

	RosRunner r;
	
	/**
	 * Starts new instance of Jrosparam - commandline version 
	 * @return running instance, or the test failed already
	 */
	private Jrosparam startJrosparam(){
		

		try {
			r = new RosRunner(new String[]{"ctu.nengoros.rosparam.RosparamNode","/use_sim_time:=false"});
			r.start();
			assertTrue(r.isRunning());

			NodeMain node = r.getNode();
			Jrosparam jr = new Jrosparam((RosparamInt)node);
			return jr;
		} catch (Exception e) {
			System.err.println("ERROR: could not start the rosparam node! Roscore launched??");
			fail();
		}
		return null;
	}

	@Test
	public void empty(){
		Jrosparam j = startJrosparam();
		
		String result = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		r.stop();
	}
	
	@Test
	public void setGetDeleteString(){
		Jrosparam j = startJrosparam();
		
		// list is empty
		String result = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		// add a
		String res = j.processCommand(new String[]{"set","a","b"});
		assertTrue(res.length()==0);
		
		// read it
		String data = j.processCommand(new String[]{"get","a"});
		assertTrue(data.equalsIgnoreCase("b"));
		
		// delete it
		res = j.processCommand(new String[]{"delete","a"});
		assertTrue(res.length()==0);
		
		// should not be there
		data = j.processCommand(new String[]{"get","a"});
		assertTrue(res.length()==0);
		
		data = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		r.stop();
	}
	
	@Test
	public void setGetDeleteBoolean(){
		Jrosparam j = startJrosparam();
		
		// list is empty
		String result = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		// add a
		String res = j.processCommand(new String[]{"set","a","false"});
		assertTrue(res.length()==0);
		
		res = j.processCommand(new String[]{"set","/b_is_true","true"});
		assertTrue(res.length()==0);
		
		// read both
		String data = j.processCommand(new String[]{"get","a"});
		assertTrue(data.equalsIgnoreCase("false"));
		data = j.processCommand(new String[]{"get","/b_is_true"});
		assertTrue(data.equalsIgnoreCase("true"));
		
		// delete them
		res = j.processCommand(new String[]{"delete","a"});
		assertTrue(res.length()==0);
		res = j.processCommand(new String[]{"delete","/b_is_true"});
		assertTrue(res.length()==0);
		
		// should not be there
		data = j.processCommand(new String[]{"get","a"});
		assertTrue(res.length()==0);
		data = j.processCommand(new String[]{"get","/b_is_true"});
		assertTrue(res.length()==0);
		
		data = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		r.stop();
	}
	
	@Test
	public void namespaceAndDouble(){
		Jrosparam j = startJrosparam();
		
		// list is empty
		String result = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		// add a
		String res = j.processCommand(new String[]{"set","ahoj","11.123"});
		assertTrue(res.length()==0);
		
		// read as it was written
		String data = j.processCommand(new String[]{"get","ahoj"});
		assertTrue(data.equalsIgnoreCase("11.123"));
		
		// red from the absolute path
		data = j.processCommand(new String[]{"get","/ahoj"});
		assertTrue(data.equalsIgnoreCase("11.123"));
		
		// delete
		res = j.processCommand(new String[]{"delete","ahoj"});
		assertTrue(res.length()==0);
		
		// should not be there
		data = j.processCommand(new String[]{"get","/ahoj"});
		assertTrue(res.length()==0);
		
		data = j.processCommand(new String[]{"list"});
		assertTrue(result.equalsIgnoreCase(Jrosparam.listEmpty));
		
		r.stop();
	}
	
}