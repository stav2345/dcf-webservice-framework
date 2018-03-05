package dataset;

import soap.GetDatasetsList;

/**
 * Dcf dataset that is downloaded using the {@link GetDatasetsList}
 * request.
 * @author avonva
 *
 */
public class DcfDataset implements IDcfDataset {
	
	private String id;
	private String senderId;
	private DcfDatasetStatus status;
	
	private String lastMessageId;
	private String lastModifyingMessageId;
	private String lastValidationMessageId;

	public void setId(String id) {
		this.id = id;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public void setStatus(DcfDatasetStatus status) {
		this.status = status;
	}
	
	public String getId() {
		return id;
	}

	public String getSenderId() {
		return senderId;
	}

	public DcfDatasetStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Dataset: id=" + id 
				+ ";senderId=" + senderId 
				+ ";status=" + status;
	}
	
	@Override
	public boolean equals(Object arg0) {
		
		if (arg0 instanceof DcfDataset) {
			
			DcfDataset other = (DcfDataset) arg0;
			
			// same id and sender dataset id
			return this.id.equals(other.getId()) 
					&& this.senderId.equals(other.getSenderId());
		}
		
		return super.equals(arg0);
	}

	@Override
	public void setLastMessageId(String msgId) {
		this.lastMessageId = msgId;
	}

	@Override
	public void setLastModifyingMessageId(String msgId) {
		this.lastModifyingMessageId = msgId;
	}

	@Override
	public void setLastValidationMessageId(String msgId) {
		this.lastValidationMessageId = msgId;
	}

	@Override
	public String getLastMessageId() {
		return this.lastMessageId;
	}

	@Override
	public String getLastModifyingMessageId() {
		return this.lastModifyingMessageId;
	}

	@Override
	public String getLastValidationMessageId() {
		return this.lastValidationMessageId;
	}
}
