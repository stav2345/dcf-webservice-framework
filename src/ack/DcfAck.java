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
	
	@Override
	public String toString() {
		return "Ack: state=" + state + "; log=" + log;
	}
}
