package dcf_log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class which models a dcf log(the log which is downloadable by the
 * dcf web interface in the Logs tab, by pressing "download results" in
 * a single operation)
 * @author avonva
 *
 */
public class DcfLog {
	
	private String action;
	private Timestamp transmissionDate;
	private Timestamp processingDate;
	private String uploadedFilename;
	private String catalogueCode;
	private String catalogueVersion;
	private String catalogueStatus;
	private String macroOpName;
	private DcfResponse macroOpResult;
	private Collection<String> macroOpLogs;
	private Collection<LogNode> logNodes;
	private Collection<LogNode> validationErrors;
	
	/**
	 * Initialize a dcf log document
	 * @param action the dcf action related to the log
	 * @param transmissionDate when the request was transmitted
	 * @param processingDate when the request was processed
	 * @param uploadedFilename the name of the file attached to the request
	 * @param catalogueCode the code of the involved catalogue
	 * @param catalogueVersion the version of the involved catalogue
	 * @param catalogueStatus the status of the involved catalogue
	 * @param macroOpName the name of the operation required
	 * @param macroOpResult the result of the entire operation
	 * @param macroOpLogs some additional information related to the operation
	 * result
	 * @param logNodes a list of nodes which gives information related to
	 * each single operation requested
	 * @param validationErrors list of errors that explain why not success
	 */
	public DcfLog(String action, Timestamp transmissionDate, Timestamp processingDate,
			String uploadedFilename, String catalogueCode, String catalogueVersion, 
			String catalogueStatus, String macroOpName, DcfResponse macroOpResult, 
			Collection<String> macroOpLogs, Collection<LogNode> logNodes,
			Collection<LogNode> validationErrors) {
		
		this.action = action;
		this.transmissionDate = transmissionDate;
		this.processingDate = processingDate;
		this.uploadedFilename = uploadedFilename;
		this.catalogueCode = catalogueCode;
		this.catalogueVersion = catalogueVersion;
		this.catalogueStatus = catalogueStatus;
		this.macroOpName = macroOpName;
		this.macroOpResult = macroOpResult;
		this.macroOpLogs = macroOpLogs;
		this.logNodes = logNodes;
		this.validationErrors = validationErrors;
	}

	public String getAction() {
		return action;
	}
	
	public Timestamp getTransmissionDate() {
		return transmissionDate;
	}
	
	public Timestamp getProcessingDate() {
		return processingDate;
	}
	
	public String getUploadedFilename() {
		return uploadedFilename;
	}
	
	/**
	 * Get the code of the involved catalogue
	 * @return
	 */
	public String getCatalogueCode() {
		return catalogueCode;
	}
	
	/**
	 * Get the version of the involved catalogue
	 * @return
	 */
	public String getCatalogueVersion() {
		return catalogueVersion;
	}
	
	/**
	 * Get the new status of the involved catalogue
	 * @return
	 */
	public String getCatalogueStatus() {
		return catalogueStatus;
	}

	/**
	 * Get the name of the required operation
	 * @return
	 */
	public String getMacroOpName() {
		return macroOpName;
	}
	
	/**
	 * Get the macro result of the required operation.
	 * If {@link DcfResponse#OK} the operation was successful
	 * @return
	 */
	public DcfResponse getMacroOpResult() {
		return macroOpResult;
	}

	/**
	 * Check if the required operation was successful
	 * @return
	 */
	public boolean isMacroOperationCorrect() {
		return macroOpResult == DcfResponse.OK;
	}
	
	public Collection<String> getMacroOpLogs() {
		return macroOpLogs;
	}
	
	public Collection<LogNode> getLogNodes() {
		return logNodes;
	}
	
	public Collection<LogNode> getValidationErrors() {
		return validationErrors;
	}
	/**
	 * Get all the log nodes that were not successful
	 * @return
	 */
	public Collection<LogNode> getLogNodesWithErrors() {
		
		// filter nodes by their result
		Collection<LogNode> nodes = new ArrayList<>();

		for(LogNode node : logNodes) {
			
			// if erroneous operation
			if(!node.isOperationCorrect())
				nodes.add(node);
		}
		
		for(LogNode node : validationErrors) {
			// if erroneous operation
			if(!node.isOperationCorrect())
				nodes.add(node);
		}
		
		return nodes;
	}

	
	@Override
	public String toString() {
		return "DcfLog: action=" + action 
				+ ";trxDate=" + transmissionDate 
				+ ";procDate=" + processingDate 
				+ ";uploadedFile=" + uploadedFilename
				+ ";catalogueCode=" + catalogueCode
				+ ";catalogueVersion=" + catalogueVersion
				+ ";catalogueStatus=" + catalogueStatus
				+ ";macroOpName=" + macroOpName
				+ ";macroOpResult" + macroOpResult
				+ ";macroOpLogs" + macroOpLogs
				+ ";operationsLogs=" + logNodes;
	}
}
