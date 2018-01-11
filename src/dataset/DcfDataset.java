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
}
