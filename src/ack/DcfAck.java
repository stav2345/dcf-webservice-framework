<<<<<<< HEAD
package ack;

import soap.GetAck;

/**
 * Acknowledge obtained with {@link GetAck}
 * @author avonva
 *
 */
public class DcfAck {
	
	private FileState state;
	private IDcfAckLog log;
	
	/**
	 * Create an acknowledgement.
	 * @param state status of the ack
	 * @param log log of the ack, if present
	 */
	public DcfAck(FileState state, IDcfAckLog log) {
		this.state = state;
		this.log = log;
	}
	
	public FileState getState() {
		return this.state;
	}
	
	public IDcfAckLog getLog() {
		return this.log;
	}
	
	public boolean isReady() {
		return this.state != null && this.state == FileState.READY;
	}
	
	public boolean hasFault() {
		return this.state != null && this.state == FileState.EXCEPTION;
	}
	
	public boolean isDenied() {
		return this.state != null && this.state == FileState.ACCESS_DENIED;
	}
	
	@Override
	public String toString() {
		return "Ack: state=" + this.state + "; log=" + this.log;
	}
}
=======
package ack;

import soap.GetAck;

/**
 * Acknowledge obtained with {@link GetAck}
 * @author avonva
 *
 */
public class DcfAck {
	
	private FileState state;
	private IDcfAckLog log;
	
	/**
	 * Create an acknowledgement.
	 * @param state status of the ack
	 * @param log log of the ack, if present
	 */
	public DcfAck(FileState state, IDcfAckLog log) {
		this.state = state;
		this.log = log;
	}
	
	public FileState getState() {
		return state;
	}
	
	public IDcfAckLog getLog() {
		return log;
	}
	
	public boolean isReady() {
		return state != null && state == FileState.READY;
	}
	
	public boolean hasFault() {
		return state != null && state == FileState.EXCEPTION;
	}
	
	public boolean isDenied() {
		return state != null && state == FileState.ACCESS_DENIED;
	}
	
	@Override
	public String toString() {
		return "Ack: state=" + state + "; log=" + log;
	}
}
>>>>>>> 5db4cda62aab4fbc1c8dbec8998c82e8bc1d4475
