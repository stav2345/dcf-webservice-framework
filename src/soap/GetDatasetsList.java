package soap;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import config.Config;
import dataset.IDcfDatasetsList;
import response_parser.GetDatasetsListParser;
import user.IDcfUser;

/**
 * Get dataset list request for the DCF webservice. It can be used
 * by calling {@link #getList()} to get all the dataset
 * of the current user.
 * @author avonva
 *
 */
public class GetDatasetsList extends SOAPRequest {

	// web service link of the getDatasetList service
	private static final String URL = "https://dcf-elect.efsa.europa.eu/elect2/";
	private static final String LIST_NAMESPACE = "http://dcf-elect.efsa.europa.eu/";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dcf-dp-ws/elect2/?wsdl";
	
	private String dataCollectionCode;
	private IDcfDatasetsList output;
	
	/**
	 * Initialize the get dataset list request with the data collection
	 * that is required.
	 * @param dataCollectionCode
	 */
	public GetDatasetsList(IDcfUser user, String dataCollectionCode, IDcfDatasetsList output) {
		super(user, LIST_NAMESPACE);
		this.dataCollectionCode = dataCollectionCode;
		this.output = output;
	}
	
	/**
	 * Send the request and get the dataset list
	 * @throws SOAPException
	 */
	public IDcfDatasetsList getList() throws MySOAPException {
		
		IDcfDatasetsList datasets = null;
		
		Config config = new Config();
		
		Object response = makeRequest(config.isProductionEnvironment() ? URL : TEST_URL);
		
		// get the list from the response if possible
		if (response != null) {
			datasets = (IDcfDatasetsList) response;
		}
		
		return datasets;
	}

	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {
		
		// create the standard structure and get the message
		SOAPMessage request = createTemplateSOAPMessage("dcf");

		SOAPBody soapBody = request.getSOAPPart().getEnvelope().getBody();
		
		SOAPElement soapElem = soapBody.addChildElement("getDatasetList", "dcf");

		SOAPElement arg = soapElem.addChildElement("dataCollectionCode");
		arg.setTextContent(this.dataCollectionCode);

		// save the changes in the message and return it
		request.saveChanges();
		
		return request;
	}
	
	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {

		// parse the dom document and return the contents
		GetDatasetsListParser parser = new GetDatasetsListParser(output);
		SOAPBody body = soapResponse.getSOAPPart().getEnvelope().getBody();
		return parser.parse(body);
	}
}
