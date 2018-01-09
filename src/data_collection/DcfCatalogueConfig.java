package data_collection;

/**
 * A configuration which contains one of the data collection
 * variables. In particular it specifies the catalogue and 
 * the hierarchy which contains this variable.
 * @author avonva
 *
 */
public class DcfCatalogueConfig implements IDcfCatalogueConfig {

	private int id = -1;
	private String dataElementName;
	private String catalogueCode;
	private String hierarchyCode;
	
	public DcfCatalogueConfig() {}
	
	public DcfCatalogueConfig( String dataElementName, 
			String catalogueCode, String hierarchyCode ) {
		this.dataElementName = dataElementName;
		this.catalogueCode = catalogueCode;
		this.hierarchyCode = hierarchyCode;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public void setDataElementName(String dataElem) {
		this.dataElementName = dataElem;
	}
	
	@Override
	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}
	
	@Override
	public void setHierarchyCode(String hierarchyCode) {
		this.hierarchyCode = hierarchyCode;
	}
	
	public int getId() {
		return id;
	}

	public String getDataElementName() {
		return dataElementName;
	}

	public String getCatalogueCode() {
		return catalogueCode;
	}

	public String getHierarchyCode() {
		return hierarchyCode;
	}
	
	@Override
	public String toString() {
		return "CAT CONFIG: id= " + (id == -1 ? "not defined yet" : id ) 
				+ "dataElemName=" + dataElementName
				+ ";catCode=" + catalogueCode
				+ ";hierCode=" + hierarchyCode;
	}
}
