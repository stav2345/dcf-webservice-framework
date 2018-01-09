package resource;

import java.util.ArrayList;

import soap.GetResourceList;

/**
 * Result of {@link GetResourceList} call.
 * @author avonva
 *
 */
public class DcfResourceList extends ArrayList<IDcfResourceReference> implements IDcfResourceList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Get the list of resources that match the {@code resourceType}
	 * @param resourceType
	 * @return
	 */
	public DcfResourceList getByType(String resourceType) {
		
		DcfResourceList resources = new DcfResourceList();
		
		for (IDcfResourceReference ref : this) {
			if (ref.getType().equals(resourceType))
				resources.add(ref);
		}

		return resources;
	}

	@Override
	public IDcfResourceReference create() {
		return new DcfResourceReference();
	}
	
	@Override
	public boolean addElem(IDcfResourceReference elem) {
		return super.add(elem);
	}
}
