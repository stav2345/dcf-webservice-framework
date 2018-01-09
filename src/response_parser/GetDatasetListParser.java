package response_parser;

import javax.xml.soap.SOAPBody;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataset.DcfDatasetStatus;
import dataset.IDcfDataset;
import dataset.IDcfDatasetList;
import soap.GetDatasetList;

/**
 * Parser for a dom document containing the {@link GetDatasetList}
 * response
 * @author avonva
 *
 */
public class GetDatasetListParser {

	private static final String DATASET_ID_NODE = "datasetId";
	private static final String SENDER_DATASET_ID_NODE = "datasetSenderId";
	private static final String WRAPPER_STATUS_NODE = "datasetStatus";
	private static final String STATUS_NODE = "status";
	private static final String STEP_NODE = "step";
	
	private IDcfDatasetList output;
	
	public GetDatasetListParser(IDcfDatasetList output) {
		this.output = output;
	}
	
	/**
	 * Get a list of datasets from the soap body
	 * @param body
	 * @return
	 */
	public IDcfDatasetList parse(SOAPBody body) {
		
		NodeList datas = body.getElementsByTagName("dataset");

		for (int i = 0; i < datas.getLength(); ++i) {

			// get the current dataset
			Node datasetNode = datas.item(i);

			output.addElem(getDataset(datasetNode));
		}
		
		return output;
	}
	
	/**
	 * Get a single dataset from a dataset node
	 * @param datasetNode
	 * @return
	 */
	private IDcfDataset getDataset(Node datasetNode) {
		
		// get the info related to the dataset
		NodeList datasetInfoNode = datasetNode.getChildNodes();
		
		IDcfDataset dataset = output.create();
		
		// parse the dataset info
		for (int i = 0; i < datasetInfoNode.getLength(); ++i) {
			
			Node field = datasetInfoNode.item(i);

			String value = field.getTextContent();
			
			switch (field.getNodeName()) {
			case DATASET_ID_NODE:
				dataset.setId(value);
				break;
			case SENDER_DATASET_ID_NODE:
				dataset.setSenderId(value);
				break;
			case WRAPPER_STATUS_NODE:
				dataset.setStatus(getStatus(field));
				break;
			default:
				break;
			}
		}

		return dataset;
	}
	
	/**
	 * Get the dataset status object from the status node
	 * @param statusNode
	 * @return
	 */
	private DcfDatasetStatus getStatus(Node statusNode) {
		
		DcfDatasetStatus status = null;
		String step = null;
		
		// get the status info
		NodeList statusInfoNode = statusNode.getChildNodes();
		for (int i = 0; i < statusInfoNode.getLength(); ++i) {
			
			// set properties based on node name
			Node field = statusInfoNode.item(i);

			String value = field.getTextContent();
			
			switch (field.getLocalName()) {
			case STATUS_NODE:
				status = DcfDatasetStatus.fromString(value);
				break;
			case STEP_NODE:
				step = value;
				break;
			default:
				break;
			}
		}
		
		// set also the step after the status is defined
		if (status != null)
			status.setStep(step);
		
		return status;
	}
}
