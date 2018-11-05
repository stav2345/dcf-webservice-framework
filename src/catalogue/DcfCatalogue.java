package catalogue;

import java.sql.Timestamp;

import soap.GetCataloguesList;

/**
 * Catalogue object retrieved with {@link GetCataloguesList}
 * @author avonva
 *
 */
public class DcfCatalogue implements IDcfCatalogue {
	
	private String code;
	private String name;
	private String label;
	private String scopenotes;
	private String version;
	private Timestamp lastUpdate;
	private Timestamp validFrom;
	private Timestamp validTo;
	private String status;
	private boolean deprecated;
	private String termCodeMask;
	private int termCodeLength;
	private String termMinCode;
	private boolean acceptNonStandardCodes;
	private boolean generateMissingCodes;
	private String catalogueGroups;

	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setScopenotes(String scopenotes) {
		this.scopenotes = scopenotes;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}
	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}
	public void setTermCodeMask(String termCodeMask) {
		this.termCodeMask = termCodeMask;
	}
	public void setTermCodeLength(int termCodeLength) {
		this.termCodeLength = termCodeLength;
	}
	public void setTermMinCode(String termMinCode) {
		this.termMinCode = termMinCode;
	}
	public void setAcceptNonStandardCodes(boolean acceptNonStandardCodes) {
		this.acceptNonStandardCodes = acceptNonStandardCodes;
	}
	public void setGenerateMissingCodes(boolean generateMissingCodes) {
		this.generateMissingCodes = generateMissingCodes;
	}
	public void setCatalogueGroups(String catalogueGroups) {
		this.catalogueGroups = catalogueGroups;
	}

	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getLabel() {
		return label;
	}
	public String getScopenotes() {
		return scopenotes;
	}
	public String getVersion() {
		return version;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	public Timestamp getValidFrom() {
		return validFrom;
	}
	public Timestamp getValidTo() {
		return validTo;
	}
	public String getStatus() {
		return status;
	}
	public boolean isDeprecated() {
		return deprecated;
	}
	public String getTermMinCode() {
		return termMinCode;
	}
	public int getTermCodeLength() {
		return termCodeLength;
	}
	public String getTermCodeMask() {
		return termCodeMask;
	}
	public boolean isAcceptNonStandardCodes() {
		return acceptNonStandardCodes;
	}
	public boolean isGenerateMissingCodes() {
		return generateMissingCodes;
	}
	public String getCatalogueGroups() {
		return catalogueGroups;
	}
	
	@Override
	public String toString() {
		return "code=" + code 
				+ ";version=" + version
				+ ";label=" + label 
				+ ";scopenotes=" + scopenotes 
				+ ";termCodeLength=" + termCodeLength
				+ ";validFrom=" + validFrom 
				+ ";validTo=" + validTo;
	}
}


