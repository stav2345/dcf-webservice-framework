package dcf_log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Builder to create a {@link DcfLog} step by step. This
 * is useful while parsing the .xml log document, in order
 * to create it step by step.
 * @author avonva
 *
 */
public class DcfLogBuilder {

	private static final Logger LOGGER = LogManager.getLogger(DcfLogBuilder.class);

	private static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	
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
	 * Initialize the dcf log builder memory
	 */
	public DcfLogBuilder() {
		macroOpLogs = new ArrayList<>();
		logNodes = new ArrayList<>();
		validationErrors = new ArrayList<>();
	}
	
	/**
	 * Get the timestamp contained in the string
	 * @param date
	 * @return
	 */
	public Timestamp getTimestamp(String date) {
		
		SimpleDateFormat format = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);
	    
		Timestamp ts = null;
		
		try {
			Date parsedDate = format.parse(date);
			ts = new Timestamp(parsedDate.getTime());
		    
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("Cannot parse timestamp=" + date 
					+ " with format=" + ISO_8601_24H_FULL_FORMAT, e);
		}
		
		return ts;
	}

	public void setAction(String action) {
		this.action = action;
	}
	public void setTransmissionDate(String transmissionDate) {
		this.transmissionDate = getTimestamp (transmissionDate);
	}
	public void setProcessingDate(String processingDate) {
		this.processingDate = getTimestamp (processingDate);
	}
	public void setUploadedFilename(String uploadedFilename) {
		this.uploadedFilename = uploadedFilename;
	}
	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}
	public void setCatalogueVersion(String catalogueVersion) {
		this.catalogueVersion = catalogueVersion;
	}
	public void setCatalogueStatus(String catalogueStatus) {
		this.catalogueStatus = catalogueStatus;
	}
	public void setMacroOpName(String macroOpName) {
		this.macroOpName = macroOpName;
	}
	public void setMacroOpResult(String macroOpResult) {
	    try {
	    	this.macroOpResult = DcfResponse.valueOf(macroOpResult);
	    } catch (IllegalArgumentException e) {
	    	this.macroOpResult = DcfResponse.ERROR;
	    }
	}
	public void setMacroOpLogs(Collection<String> macroOpLogs) {
		this.macroOpLogs = macroOpLogs;
	}
	public void setLogNodes(Collection<LogNode> logNodes) {
		this.logNodes = logNodes;
	}
	public void addMacroOpLog(String macroOpLog) {
		this.macroOpLogs.add(macroOpLog);
	}
	public void addLogNode(LogNode node) {
		this.logNodes.add(node);
	}
	public void addValidationErrorNode (LogNode node) {
		this.validationErrors.add(node);
	}
	
	/**
	 * Build the log document
	 * @return
	 */
	public DcfLog build() {
		return new DcfLog(action, transmissionDate, processingDate, uploadedFilename, 
				catalogueCode, catalogueVersion, catalogueStatus, macroOpName, 
				macroOpResult, macroOpLogs, logNodes, validationErrors);
	}
}
