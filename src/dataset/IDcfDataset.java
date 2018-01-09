package dataset;

public interface IDcfDataset {
	
	/**
	 * Set the dataset id
	 * @param id
	 */
	public void setId(String id);
	
	/**
	 * Set the dataset sender id
	 * @param senderId
	 */
	public void setSenderId(String senderId);
	
	/**
	 * Set the dataset status
	 * @param status
	 */
	public void setStatus(DcfDatasetStatus status);
	
	/**
	 * Get the dataset id
	 * @return
	 */
	public String getId();
	
	/**
	 * Get the dataset sender id (id given
	 * by the data provider)
	 * @return
	 */
	public String getSenderId();
	
	/**
	 * Get the dataset status
	 * @return
	 */
	public DcfDatasetStatus getStatus();
}
