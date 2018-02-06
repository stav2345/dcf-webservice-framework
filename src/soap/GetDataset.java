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
 * Generic get file request to the dcf.
 * @author avonva
 *
 */
public class GetDataset extends SOAPRequest {

	private static final String NAMESPACE = "http://dcf-elect.efsa.europa.eu/";
	private static final String URL = "https://dcf-elect.efsa.europa.eu/elect2";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dcf-dp-ws/elect2/?wsdl";
	
	private String datasetId;
	
	/**
	 * Make a get file request for a specific dataset
	 * @param datasetId
	 */
	public GetDataset(IDcfUser user, Environment env, String datasetId) {
		super(user, env, NAMESPACE);
		this.datasetId = datasetId;
	}
	
	/**
	 * Get an handle to the downloaded dataset
	 * @return
	 * @throws SOAPException
	 */
	public File getDatasetFile() throws DetailedSOAPException {
		
		SOAPConsole.log("GetDataset: datasetId=" + datasetId, getUser());

		Object response = makeRequest(getEnvironment() == Environment.PRODUCTION ? URL : TEST_URL);
		
		SOAPConsole.log("GetDataset:", response);
		
		if (response == null)
			return null;
		
		return (File) response;
	}
	
	/**
	 * Get the url for making get file requests
	 * @return
	 */
	public static String getUrl() {
		return URL;
	}
	
	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {

		// create the standard structure and get the message
		SOAPMessage soapMsg = createTemplateSOAPMessage ("dcf");
		SOAPBody soapBody = soapMsg.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapElem = soapBody.addChildElement("getDataset", "dcf");

		// add resource id
		SOAPElement arg = soapElem.addChildElement("datasetId");
		arg.setTextContent(datasetId);

		// save the changes in the message and return it
		soapMsg.saveChanges();

		return soapMsg;
	}

	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {
		return writeZippedAttachment(soapResponse, ".xml");
	}
}
