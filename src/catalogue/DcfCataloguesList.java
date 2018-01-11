package catalogue;

import java.util.ArrayList;

public class DcfCataloguesList extends ArrayList<IDcfCatalogue> implements IDcfCataloguesList {
	private static final long serialVersionUID = 944117596551368064L;

	@Override
	public IDcfCatalogue create() {
		return new DcfCatalogue();
	}
	
	@Override
	public boolean addElem(IDcfCatalogue elem) {
		return this.add(elem);
	}
}
