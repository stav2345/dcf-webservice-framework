package soap;

import java.io.File;
import java.io.IOException;
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
 * @author shahaal
 *
 */
public class GetFile extends SOAPRequest {

	private static final String NAMESPACE = "http://dcf-elect.efsa.europa.eu/";
	private static final String URL = "https://dcf-elect.efsa.europa.eu/elect2";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dcf-dp-ws/elect2/?wsdl";
	
	private String resourceId;
	
	/**
	 * Get a file using its id
	 * @param env
	 * @param user
	 * @param resourceId1
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 */
	public File getFile(Environment env, IDcfUser user, String resourceId1) throws SOAPException, IOException {
		
		this.resourceId = resourceId1;
		
		SOAPConsole.log("GetFile: resourceId=" + resourceId1, user);
		
		SOAPMessage response = (SOAPMessage) makeRequest(env, user, NAMESPACE, getUrl(env));
		
		File file = writeAttachment(response);
		
		SOAPConsole.log("GetFile:", file);
		
		return file;
	}
	
	/**
	 * Get the url for making get file requests
	 * @return
	 */
	public static String getUrl(Environment env) {
		return env == Environment.PRODUCTION ? URL : TEST_URL;
	}
	
	@Override
	public SOAPMessage createRequest(IDcfUser user, String namespace, SOAPConnection con) throws SOAPException {
		
		// create the standard structure and get the message
		SOAPMessage soapMsg = createTemplateSOAPMessage (user, namespace, "dcf");
		SOAPBody soapBody = soapMsg.getSOAPPart().getEnvelope().getBody();
		SOAPElement soapElem = soapBody.addChildElement("GetFile", "dcf");

		// add resource id
		SOAPElement arg = soapElem.addChildElement("trxResourceId");
		arg.setTextContent(this.resourceId);

		// save the changes in the message and return it
		soapMsg.saveChanges();

		return soapMsg;
	}

	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {
		return soapResponse;
	}
}
