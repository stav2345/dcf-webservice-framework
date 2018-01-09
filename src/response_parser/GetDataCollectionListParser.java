package response_parser;

import java.text.ParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data_collection.IDcfDataCollection;
import data_collection.IDcfDataCollectionList;
import utils.TimeUtils;

public class GetDataCollectionListParser {

	public static final String DATE_FORMAT = "yyyy-MM-ddX";
	
	private IDcfDataCollectionList output;
	
	public GetDataCollectionListParser(IDcfDataCollectionList output) {
		this.output = output;
	}
	
	public IDcfDataCollectionList parse(Document cdata) {

		NodeList dcNodes = cdata.getElementsByTagName("dataCollectionMainInfo");

		// for each node
		for (int i = 0; i < dcNodes.getLength(); ++i) {

			// get the current node
			Node node = dcNodes.item(i);

			output.addElem(getDataCollection(node));
		}

		return output;
	}
	
	/**
	 * Get a single datacollection element from the node values
	 * @param node
	 * @return
	 */
	private IDcfDataCollection getDataCollection(Node node) {

		// get elements
		NodeList nodes = node.getChildNodes();

		IDcfDataCollection dc = output.create();
		
		// for each element
		for ( int i = 0; i < nodes.getLength(); ++i ) {

			// get the current node
			Node property = nodes.item( i );

			// get the property name (e.g. "code", "name", ... )
			String propertyName = property.getNodeName();

			// Note: to get the value it is necessary to get 
			// the child first, and then we can get the node value
			// from the child (I don't know why but this is it)
			Node propertyValueNode = property.getFirstChild();

			// skip if no name or value is found (happen if there are catalogue errors)
			if ( propertyName == null || propertyValueNode == null )
				continue;

			// Get the value of the property node
			String propertyValue = propertyValueNode.getNodeValue();

			// Add the property value to the catalogue builder
			// according to the property name
			switch (propertyName) {

			case "dcCode":
				dc.setCode(propertyValue);
				break;
			case "dcDescription":
				dc.setDescription(propertyValue);
				break;
			case "dcCategory":
				dc.setCategory(propertyValue);
				break;

			case "activeFrom":

				if ( propertyValue != null ) {
					try {
						dc.setActiveFrom(TimeUtils.getTimestampFromString(propertyValue, DATE_FORMAT));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				break;
				
			case "activeTo":

				if ( propertyValue != null ) {
					try {
						dc.setActiveTo(TimeUtils.getTimestampFromString(propertyValue, DATE_FORMAT));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				break;
			case "resourceId":
				dc.setResourceId( propertyValue );
				break;
			}  // end switch
		}  // end for
		
		return dc;
	}
}
