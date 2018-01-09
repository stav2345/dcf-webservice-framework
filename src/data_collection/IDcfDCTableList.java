package data_collection;

import response_parser.IDcfList;

public interface IDcfDCTableList extends IDcfList<IDcfDCTable> {
	public IDcfCatalogueConfig createConfig();
}
