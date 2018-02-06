package soap;

import java.io.File;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import config.Environment;
import user.IDcfUser;

/**
 * Export catalogue file web service. See GDE2 and DCF manuals to
 * see all the available options. Here we can download a file from
 * the dcf, as an internal version of a catalogue or a log.
 * @author avonva
 *
 */
public class ExportCatalogueFile extends SOAPRequest {
	
	private static final String NAMESPACE = "http://ws.catalog.dc.efsa.europa.eu/";
	private static final String URL = "https://dcf-cms.efsa.europa.eu/catalogues";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dc-catalog-public-ws/catalogues/?wsdl";
	
	// export types used in the request
	private static final String EXPORT_TYPE_CATALOGUE = "catalogFullDefinition";
	private static final String EXPORT_TYPE_LOG = "log";
	private static final String EXPORT_TYPE_INTERNAL_VERSION = "catalogInternalVersion";
	
	private static final String XML_FILE_TYPE = "XML";
	
	private String catalogueCode;
	private String exportType;
	private String fileType;
	
	/**
	 * Initialize the export file action
	 */
	public ExportCatalogueFile(IDcfUser user, Environment env) {
		super(user, env, NAMESPACE);
	}
	
	public File exportCatalogue(String catalogueCode) throws SOAPException {
		
		SOAPConsole.log("ExportCatalogueFile: export last published version of catalogue=" + catalogueCode, getUser());
		
		Object log = exportXml(catalogueCode, EXPORT_TYPE_CATALOGUE, 
				XML_FILE_TYPE);
		
		if (log != null)
			return (File) log;
		
		return null;
	}

	/**
	 * Download a log file related to an upload catalogue file operation.
	 * @param code the code of the log we want to download
	 * @param filename the file where we want to store the log
	 * @return a File object which points to the log file
	 * @throws SOAPException 
	 */
	public File exportLog(String code) 
			throws DetailedSOAPException {
		
		SOAPConsole.log("ExportCatalogueFile: export log=" + code, getUser());
		
		Object log = exportXml(code, EXPORT_TYPE_LOG, 
				XML_FILE_TYPE);
		
		if (log != null)
			return (File) log;
		
		return null;
	}
	
	/**
	 * Export the last internal version of the catalogue.
	 * @param catalogueCode the code of the catalogue we want to consider
	 * @param filename the file where we want to store the downloaded catalogue
	 * @return a File object which points to the downloaded catalogue .xml file
	 * @throws SOAPException
	 */
	public File exportLastInternalVersion(String catalogueCode) 
			throws SOAPException {
		
		SOAPConsole.log("ExportCatalogueFile: export last internal version of catalogue=" 
				+ catalogueCode, getUser());
		
		Object lastVersion = exportXml(catalogueCode, 
				EXPORT_TYPE_INTERNAL_VERSION, 
				XML_FILE_TYPE);
		
		if (lastVersion != null)
			return (File) lastVersion;
		
		return null;
	}
	
	/**
	 * Export an xml file given the code and export type fields
	 * to be inserted in the request.
	 * @param code code of the object we are considering as the
	 * catalogue code or the log code
	 * @param exportType the export type (see GDE2)
	 * @param fileType the type of the attachment we want
	 * @return an object containing the xml structure
	 * @throws SOAPException 
	 */
	public Object exportXml(String code, String exportType, 
			String fileType) throws DetailedSOAPException {
		
		this.catalogueCode = code;
		this.exportType = exportType;
		this.fileType = fileType;
		
		Object result = export();

		return result;
	}
	
	/**
	 * Make the export request.
	 * @return
	 * @throws SOAPException 
	 */
	private Object export() throws DetailedSOAPException {
		return makeRequest(getEnvironment() == Environment.PRODUCTION ? URL : TEST_URL);
	}

	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {

		// create the standard structure and get the message
		SOAPMessage request = createTemplateSOAPMessage("ws");

		// get the body of the message
		SOAPBody soapBody = request.getSOAPPart().getEnvelope().getBody();

		// export catalogue file node
		SOAPElement export = soapBody.addChildElement("ExportCatalogueFile", "ws");

		// add the catalogue code to the xml if there is one
		if (catalogueCode != null) {
			SOAPElement catCodeNode = export.addChildElement("catalogueCode");
			catCodeNode.setValue(catalogueCode);
		}
		
		// add the catalogue code to the xml if there is one
		if (exportType != null) {
			SOAPElement exportTypeNode = export.addChildElement("exportType");
			exportTypeNode.setValue(exportType);
		}
		
		// add the file type if required
		if (fileType != null) {
			SOAPElement fileTypeNode = export.addChildElement("fileType");
			fileTypeNode.setValue(fileType);
		}

		// save the changes
		request.saveChanges();
		
		return request;
	}

	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {
		
		File response = null;
		
		// process the response based
		// on the export type field
		switch (exportType) {
		case EXPORT_TYPE_LOG:
			response = writeXmlIntoFile(soapResponse, true);
			break;
		case EXPORT_TYPE_INTERNAL_VERSION:
		case EXPORT_TYPE_CATALOGUE:
			response = writeXmlIntoFile(soapResponse, false);
			break;
		default:
			break;
		}

		return response;
	}
}
