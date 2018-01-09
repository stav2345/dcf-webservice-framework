package response_parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import resource.IDcfResourceList;
import resource.IDcfResourceReference;

public class GetResourceListParser {
	
	private static final String RESOURCE_REF_NODE = "resourceReference";
	private static final String RESOURCE_TYPE_NODE = "resourceType";
	private static final String RESOURCE_ID_NODE = "resourceId";
	
	private IDcfResourceList output;
	
	public GetResourceListParser(IDcfResourceList output) {
		this.output = output;
	}
	
	/**
	 * get the resources from the document
	 * @param document
	 * @return
	 */
	public IDcfResourceList parse(Document cdata) {
		
		NodeList refs = cdata.getElementsByTagName(RESOURCE_REF_NODE);
		
		for (int i = 0; i < refs.getLength(); ++i) {
			Node ref = refs.item(i);
			output.addElem(getResource(ref));
		}
		
		return output;
	}
	
	/**
	 * Get the resource from the node
	 * @param resNode
	 * @return
	 */
	public IDcfResourceReference getResource(Node resNode) {
		
		NodeList fields = resNode.getChildNodes();
		
		IDcfResourceReference reference = output.create();
		
		for (int i = 0; i < fields.getLength(); ++i) {
			
			Node field = fields.item(i);
			
			String nodeName = field.getNodeName();
			String nodeValue = field.getTextContent();
			
			switch (nodeName) {
			case RESOURCE_TYPE_NODE:
				reference.setType(nodeValue);
				break;
			case RESOURCE_ID_NODE:
				reference.setResourceId(nodeValue);
				break;
			default:
				break;
			}
		}
		
		if (reference.isIncomplete()) {
			System.err.println("Missing reference value for " + reference);
		}
		
		return reference;
	}
}
