package soap;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import config.Config;
import response_parser.IDcfList;
import user.IDcfUser;

/**
 * Generic get list request
 * @author avonva
 *
 * @param <T>
 */
public abstract class GetList<T> extends SOAPRequest {
	
	private static final Logger LOGGER = LogManager.getLogger(GetList.class);
	
	private String url;
	private String testUrl;

	/**
	 * 
	 * @param type
	 * @param prodUrl
	 * @param testUrl
	 * @param namespace
	 */
	public GetList(IDcfUser user, String url, String testUrl, String namespace) {
		super (user, namespace);
		this.url = url;
		this.testUrl = testUrl;
	}
	
	/**
	 * Get the list of T elements
	 * @return
	 * @throws DOMException
	 * @throws MySOAPException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public IDcfList<T> getList() throws MySOAPException {
		
		Config config = new Config();
		
		return (IDcfList<T>) makeRequest(config.isProductionEnvironment() ? url : testUrl);
	}
	
	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {

		// get the children of the body
		NodeList returnNodes = soapResponse.getSOAPPart().
				getEnvelope().getBody().getElementsByTagName("return");

		if (returnNodes.getLength() == 0) {
			LOGGER.error("GetList: no return node was found in the soap response");
			return null;
		}
		
		// get return node
		Node returnNode = returnNodes.item(0);
		
		// return the parsed cdata field
		Document cdata = getCData(returnNode);
		
		if (cdata == null) {
			LOGGER.error("GetList: no cdata was found in the soap response");
			return null;
		}
		
		return getList(cdata);
	}
	
	/**
	 * Parse the cdata content of the received response.
	 * @param cdata the cdata node
	 * @return desired list of objects
	 */
	public abstract IDcfList<T> getList(Document cdata);
}
