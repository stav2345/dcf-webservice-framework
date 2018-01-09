package data_collection;

import java.util.ArrayList;

public class DcfDataCollectionList extends ArrayList<IDcfDataCollection> 
	implements IDcfDataCollectionList {
	
	private static final long serialVersionUID = 1275212356670435939L;

	@Override
	public IDcfDataCollection create() {
		return new DcfDataCollection();
	}
	@Override
	public boolean addElem(IDcfDataCollection elem) {
		return super.add(elem);
	}
}
