package soap;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

import data_collection.IDcfDataCollection;
import data_collection.IDcfDataCollectionList;
import response_parser.GetDataCollectionListParser;
import user.IDcfUser;

/**
 * Get a list with all the data collections related to the current user
 * @author avonva
 *
 */
public class GetDataCollectionsList extends GetList<IDcfDataCollection> {

	private static final String NAMESPACE = "http://dcf-elect.efsa.europa.eu/";
	private static final String URL = "https://dcf-elect.efsa.europa.eu/elect2";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dcf-dp-ws/elect2/?wsdl";

	private IDcfDataCollectionList output;
	
	public GetDataCollectionsList(IDcfUser user, IDcfDataCollectionList output) {
		super(user, URL, TEST_URL, NAMESPACE);
		this.output = output;
	}

	@Override
	public IDcfDataCollectionList getList(Document cdata) {

		GetDataCollectionListParser parser = new GetDataCollectionListParser(output);
		return parser.parse(cdata);
	}

	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {

		// create the standard structure and get the message
		SOAPMessage soapMsg = createTemplateSOAPMessage("dcf");

		// get the body of the message
		SOAPBody soapBody = soapMsg.getSOAPPart().getEnvelope().getBody();

		// create the xml message structure to get the dataset list
		soapBody.addChildElement("GetDataCollectionList", "dcf");

		// save the changes in the message and return it
		soapMsg.saveChanges();

		return soapMsg;
	}
}
