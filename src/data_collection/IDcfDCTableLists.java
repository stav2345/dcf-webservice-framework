package data_collection;

import response_parser.IDcfList;

public interface IDcfDCTableLists extends IDcfList<IDcfDCTable> {
	public IDcfCatalogueConfig createConfig();
}
