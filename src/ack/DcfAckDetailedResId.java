package ack;

/**
 * Acknowledge obtained with {@link GetAck}
 * @author shahaal
 *
 */
public class DcfAckDetailedResId {
	
	private FileState state;
	private IDcfAckLog log;
	
	/**
	 * Create an acknowledgement.
	 * @param state status of the ack
	 * @param log log of the ack, if present
	 */
	public DcfAckDetailedResId(FileState state, IDcfAckLog log) {
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
		return "AckDetailedResID: state=" + state + "; log=" + log;
	}
}
