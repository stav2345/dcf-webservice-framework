package soap;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

import config.Environment;
import resource.IDcfResourcesList;
import resource.IDcfResourceReference;
import response_parser.GetResourcesListParser;
import response_parser.IDcfList;
import user.IDcfUser;

/**
 * Request to the DCF for getting the resource list
 * @author avonva
 *
 */
public class GetResourcesList<T extends IDcfResourceReference> extends GetList<T> {

	private String dataCollectionCode;
	
	// web service link of the getDatasetList service
	private static final String URL = "https://dcf-elect.efsa.europa.eu/elect2/";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dcf-dp-ws/elect2/?wsdl";
	private static final String NAMESPACE = "http://dcf-elect.efsa.europa.eu/";
	
	private IDcfResourcesList<T> output;
	
	public GetResourcesList(IDcfUser user, Environment env, String dataCollectionCode, IDcfResourcesList<T> output) {
		super(user, env, URL, TEST_URL, NAMESPACE);
		this.dataCollectionCode = dataCollectionCode;
		this.output = output;
	}
	
	@Override
	public IDcfList<T> getList() throws DetailedSOAPException {
		SOAPConsole.log("GetResourcesList: dcCode=" + dataCollectionCode, getUser());
		IDcfList<T> response = super.getList();
		
		SOAPConsole.log("GetResourcesList:", response);
		
		return response;
	}
	
	@Override
	public IDcfResourcesList<T> getList(Document cdata) {
		GetResourcesListParser<T> parser = new GetResourcesListParser<>(output);
		return parser.parse(cdata);
	}

	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {
		
		// create the standard structure and get the message
		SOAPMessage request = createTemplateSOAPMessage("dcf");

		SOAPBody soapBody = request.getSOAPPart().getEnvelope().getBody();
		
		SOAPElement soapElem = soapBody.addChildElement("GetResourceList", "dcf");

		SOAPElement arg = soapElem.addChildElement("dataCollection");
		arg.setTextContent(this.dataCollectionCode);

		// save the changes in the message and return it
		request.saveChanges();

		return request;
	}
}
