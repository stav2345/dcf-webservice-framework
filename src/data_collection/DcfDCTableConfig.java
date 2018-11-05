package data_collection;

/**
 * Relation object among {@link IDcfDataCollection}, {@link IDcfDCTable}
 * and {@link IDcfCatalogueConfig}
 * @author avonva
 *
 */
public class DcfDCTableConfig {

	private IDcfDataCollection dc;
	private IDcfDCTable table;
	private IDcfCatalogueConfig config;
	
	public DcfDCTableConfig(IDcfDataCollection dc, 
			IDcfDCTable table, IDcfCatalogueConfig config) {
		this.dc = dc;
		this.table = table;
		this.config = config;
	}

	public IDcfDataCollection getDataCollection() {
		return dc;
	}
	
	public IDcfDCTable getTable() {
		return table;
	}
	
	public IDcfCatalogueConfig getConfig() {
		return config;
	}
	
	@Override
	public String toString() {
		return "DCTableConfig: " + dc + ";" + table + ";" + config;
	}
}
