package soap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import config.Config;
import log.UploadCatalogueFileParser;
import user.IDcfUser;

/**
 * Model the upload catalogue file web service. Use the method
 * {@link #getAttachment()} to define the xml attachment which needs
 * to be uploaded to the dcf. Use {@link #processResponse(String)} to
 * process the log code related to the upload catalogue file request.
 * @author avonva
 *
 */
public class UploadCatalogueFile extends SOAPRequest {

	private static final String NAMESPACE = "http://ws.catalog.dc.efsa.europa.eu/";
	private static final String URL = "https://dcf-cms.efsa.europa.eu/catalogues";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dc-catalog-public-ws/catalogues/?wsdl";
	
	private File file;
	
	/**
	 * Initialize the url and the namespace of the upload catalogue file
	 * request.
	 */
	public UploadCatalogueFile(IDcfUser user, File file) {
		super(user, NAMESPACE);
		this.file = file;
	}
	
	/**
	 * Upload the attached file to the dcf and get the 
	 * processed response
	 * @return the code of the log which tracks the request
	 * @throws SOAPException 
	 */
	public String send() throws SOAPException {
		Config config = new Config();
		String logCode = (String) makeRequest(config.isProductionEnvironment() ? URL : TEST_URL);
		return logCode;
	}

	/**
	 * Create the reserve request message
	 */
	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {

		// create the standard structure and get the message
		SOAPMessage soapMsg = createTemplateSOAPMessage ("ws");
		
		// get the body of the message
		SOAPBody soapBody = soapMsg.getSOAPPart().getEnvelope().getBody();

		// upload catalogue file node
		SOAPElement upload = soapBody.addChildElement("UploadCatalogueFile", "ws");
		
		// file data node (child of upload cf)
		SOAPElement fileData = upload.addChildElement("fileData");
		
		// get the attachment in base64 format
		String encodedAttachment = createEncodedAttachment(file);
		
		// row data node (child of file data)
		SOAPElement rowData = fileData.addChildElement("rowData");
		rowData.setValue(encodedAttachment);
		
		// save the changes in the message and return it
		soapMsg.saveChanges();

		return soapMsg;
	}
	
	/**
	 * Create the encoded attachment for the request.
	 * @param file the file where the attachment is stored
	 * @return
	 */
	private String createEncodedAttachment(File file) {
		
		Path path = Paths.get(file.getAbsolutePath());
		
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// encode the data with base64
		return new String(Base64.getEncoder().encode(data));
	}
	
	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {
		
		// search the log code in the soap message
		UploadCatalogueFileParser parser = new UploadCatalogueFileParser();

		// get the log code of the response
		String logCode = parser.parse(soapResponse.getSOAPBody());
		
		// if errors the log code is not returned
		if (logCode == null)
			System.err.println("UploadCatalogueFile: cannot find the log code in the soap response");
		else
			System.out.println ("UploadCatalogueFile: found log code " + logCode);

		return logCode;
	}
}
