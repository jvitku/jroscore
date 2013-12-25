package ctu.nengoros.service.synchornous;

import org.ros.exception.RemoteException;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;

/**
 * This class should generically define the synchronous ROS service. 
 * You call service and method will return response if received in 
 * predefined time or null if request did not make it, or exception triggered. 
 * 
 * For example of usage, @see the vivae simulator. 
 * 
 * @author Jaroslav Vitku
 *
 * @param <R> request
 * @param <E> response
 */
public class SynchronousService<R,E> implements ServiceResponseListener<E>{

	public static final String me = "[SynchronousService] ";
	private Log l = new Log(false);
	
	private final int w = 50;
	
	public volatile boolean responseReceived;
	public int waitTime = 2000;
	int waited;
	
	private R request;
	private E response;
	
	ServiceClient<R,E> sc;
	
	public SynchronousService(ServiceClient<R,E> sc){
		responseReceived = false;
		waited = 0;
		this.sc = sc;
	}

	public SynchronousService(ServiceClient<R,E> sc, int waitMax){
		this(sc);
		waitTime = waitMax;
	}
	
	private E waitForResponse(){
		while(true){
			if(responseReceived){
				l.log(me+"Response received, returning it");
				responseReceived = false;	// discard for further use 
				return response;
			}
			try {
				Thread.sleep(w);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			waited += w;
			if(waited>waitTime){
				System.err.println(me+"Reqest not processed (no response) in a given time of" +waitTime+
						"ms, giving up!");
				return null;
			}
		}
	}
	
	public E callService(R request){
		this.request = request;
		
		// call service with this listener
		sc.call(this.request, this);
		
		// wait for response
		return waitForResponse();
	}
	
	public R getRequest(){
		return sc.newMessage();
	}

	/**
	 * part of ServiceResponseListener - service failed
	 */
	@Override
	public void onFailure(RemoteException e) {
		System.out.println("faaaaaaaaaaaaaaaaaaaaaDEFFRDDEFFRDDEFFRDDEFFRDDEFFRD");
		responseReceived = true;
	}

	/**
	 * part of ServiceResponseListener - response received
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(Object resp) {
		response = (E)resp;
		responseReceived =true;
	}
	
	private class Log{
		private boolean shouldLog = false;
		
		public Log(boolean shouldLog){ 
			this.shouldLog = shouldLog;
		}
		public void log(String s){
			if(this.shouldLog)
				System.out.println(s);
		}
	}
}
