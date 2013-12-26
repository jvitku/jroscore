package ctu.nengoros.service.synchornous;

import org.ros.exception.RemoteException;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;

/**
 * This class should generically define the synchronous ROS service.
 *  
 * Calling synchronous service means that the request is sent, the thread 
 * waits for response (if no response received, the request is resent), if no
 * response received after several tries, the null is returned.   
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
	public int waitTime = 3000;
	int waited;

	public final int maxNumRecalls = 2;
	public int numRecalls = 0;

	private R request;
	private E response;

	ServiceClient<R,E> sc;
	
	private boolean resend = false;	// whether to resend the request

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

		this.numRecalls = 0;

		while(numRecalls < maxNumRecalls){

			E response = this.oneWaitForResponse();

			// response returned OK?
			if(response != null)
				return response;

			if(!this.getResend())
				return response;
			
			this.resendRequest();

		}
		System.err.println(me+"Reqest not processed (no response) in any " +
				"of "+maxNumRecalls+", giving up!!");
		return null;
	}

	private void resendRequest(){

		// call service with this listener
		sc.call(this.request, this);
		//this.responseReceived = false;
		
		numRecalls++;
		waited = 0;
	}

	// releasing all resources
	private E oneWaitForResponse(){

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
				System.err.println(me+"Reqest not processed (no response) in a given time of "+waitTime+
						"ms, RESENDING the request!!");
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
	
	/**
	 * 
	 * Resending requests turns out to be bad habit, because
	 * the simulator can load two maps, can try to start the simulaiton 
	 * twice etc.. 
	 *
	 * @param shouldResend
	 */
	public void setResend(boolean shouldResend){
		this.resend = shouldResend;
	}
	public boolean getResend(){
		return this.resend;
	}
}
