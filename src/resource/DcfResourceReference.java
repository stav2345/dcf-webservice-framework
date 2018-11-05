package resource;

import soap.GetResourcesList;

/**
 * Single object contained in the {@link GetResourcesList}
 * response
 * @author avonva
 *
 */
public class DcfResourceReference implements IDcfResourceReference {

	private String type;
	private String resourceId;
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	public String getType() {
		return type;
	}
	public String getResourceId() {
		return resourceId;
	}
	
	/**
	 * Check if a field is missing
	 * @return
	 */
	public boolean isIncomplete() {
		return (type == null || resourceId == null || type.isEmpty() || resourceId.isEmpty());
	}
	
	@Override
	public String toString() {
		return "ResourceReference: type=" + type + ";resourceId=" + resourceId;
	}
}
