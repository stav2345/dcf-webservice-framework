package dcf_log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler used to parse a dcf log document.
 * @author avonva
 *
 */
public class LogParserHandler extends DefaultHandler {

	//private static final String ROOT = "transmissionResult";
	
	private static final String ACTION = "action";
	private static final String TRANSMISSION_DATE = "transmissionDateTime";
	private static final String PROCESSING_DATE = "processingDateTime";
	private static final String UPLOADED_FILENAME = "uploadedFileName";
	private static final String CATALOGUE_CODE = "catalogueCode";
	private static final String CATALOGUE_VERSION = "catalogueVersion";
	private static final String CATALOGUE_STATUS = "catalogueStatus";
	private static final String MACRO_OP_NAME = "macroOperationName";
	private static final String MACRO_OP_RESULT = "macroOperationResult";
	private static final String MACRO_OP_LOGS_BLOCK = "macroOperationLogs";
	private static final String MACRO_OP_LOG = "operationLog";
	
	private static final String OPERATIONS_BLOCK = "operations";
	private static final String OPERATION_BLOCK = "operation";
	private static final String OP_NAME = "operationName";
	private static final String OP_RESULT = "operationResult";
	//private static final String OP_LOGS_BLOCK = "operationLogs";
	private static final String OP_LOG = "operationLog";
	
	//private static final String VALIDATION_ERROR_BLOCK = "validationErrorLogs";
	private static final String VALIDATION_ERROR = "validationError";
	
	// builders to create objects step by step
	// while reading data from the xml file
	private DcfLogBuilder logBuilder;
	private LogNodeBuilder nodeBuilder;
	private LogNodeBuilder validationBuilder;
	
	private StringBuilder lastContent;
	
	// booleans to know in which xml block we are
	private boolean operationsBlock;
	private boolean macroLogsBlock;
	private boolean validationErrorsBlock;

	/**
	 * Initialize the log parser handler memory
	 */
	public LogParserHandler() {
		lastContent = new StringBuilder();
		logBuilder = new DcfLogBuilder();
		operationsBlock = false;
		macroLogsBlock = false;
		validationErrorsBlock = false;
	}

	// when a node is encountered
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {

		// remove content of the node from the string
		// before opening a new node
		lastContent.setLength(0);

		switch (qName) {

		// entering operations block
		case OPERATIONS_BLOCK:
			operationsBlock = true;
			break;

			// new operation, create a new log node
		case OPERATION_BLOCK:
			nodeBuilder = new LogNodeBuilder();
			break;

			// entering the macro op logs block
		case MACRO_OP_LOGS_BLOCK:
			macroLogsBlock = true;
			break;
			
		case VALIDATION_ERROR:
			validationErrorsBlock = true;
			validationBuilder = new LogNodeBuilder();
			break;
		
		default:
			break;
		}
	}

	@Override
	// when the end of a node is encountered
	public void endElement(String uri, String localName, String qName) throws SAXException {

		analyzeMacroOperationsBlock(qName);
		analyzeOperationsBlock(qName);
		analyzeValidationErrorsBlock (qName);
		
		switch (qName) {
		
		// end operations block
		case OPERATIONS_BLOCK:
			operationsBlock = false;
			break;
			
		// end operation
		case OPERATION_BLOCK:
			// add the single log node to the entire Log document
			logBuilder.addLogNode(nodeBuilder.build());
			nodeBuilder = null;
			break;
			
		// end macro operations log block
		case MACRO_OP_LOGS_BLOCK:
			macroLogsBlock = false;
			break;

		case VALIDATION_ERROR:
			validationErrorsBlock = false;
			logBuilder.addLogNode(validationBuilder.build());
			validationBuilder = null;
			break;
			
		default:
			break;
		}

		// remove content of the node from the string
		lastContent.setLength(0);
	}

	/**
	 * Analyze the macro operation block of the log document
	 * which is, the first part before the log nodes.
	 * @param qName
	 */
	private void analyzeMacroOperationsBlock(String qName) {

		switch (qName) {
		case ACTION:
			logBuilder.setAction(getValue());
			break;
		case TRANSMISSION_DATE:
			logBuilder.setTransmissionDate(getValue());
			break;
		case PROCESSING_DATE:
			logBuilder.setProcessingDate(getValue());
			break;
		case UPLOADED_FILENAME:
			logBuilder.setUploadedFilename(getValue());
			break;
		case CATALOGUE_CODE:
			logBuilder.setCatalogueCode(getValue());
			break;
		case CATALOGUE_VERSION:
			logBuilder.setCatalogueVersion(getValue());
			break;
		case CATALOGUE_STATUS:
			logBuilder.setCatalogueStatus(getValue());
			break;
		case MACRO_OP_NAME:
			logBuilder.setMacroOpName(getValue());
			break;
		case MACRO_OP_RESULT:
			logBuilder.setMacroOpResult(getValue());
			break;
		case MACRO_OP_LOG:
			
			// stop if we are not in the macro logs block
			if (!macroLogsBlock)
				break;
			
			// otherwise add the macro operation log
			logBuilder.addMacroOpLog(getValue());
			break;
		
		default:
			break;
		}
	}

	/**
	 * Analyze the validation errors blocks
	 * @param qName
	 */
	private void analyzeValidationErrorsBlock(String qName) {
		
		if (!validationErrorsBlock)
			return;
		
		validationBuilder.setResult(DcfResponse.ERROR);
		validationBuilder.setName(VALIDATION_ERROR);
		validationBuilder.addOpLog(getValue());
	}
	
	/**
	 * Analyze the log nodes inside the operations block
	 * @param qName
	 */
	private void analyzeOperationsBlock(String qName) {

		// if we are not in the operations block
		// we do nothing
		if (!operationsBlock)
			return;

		switch (qName) {

			// set the name
		case OP_NAME:
			nodeBuilder.setName(getValue());
			break;

			// set the result
		case OP_RESULT:
			nodeBuilder.setResult(getValue());
			break;

			// add the operation log
		case OP_LOG:
			nodeBuilder.addOpLog(getValue());
			break;
			
		default:
			break;
		}
	}

	/**
	 * Get the current value
	 * @return
	 */
	private String getValue() {
		String value = lastContent.toString();
		lastContent.setLength(0);
		return value;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		// ADD CONTENT TO LAST CONTENT! because the parser sometimes gives only
		// a small piece of the value and not the entire value in a single call!!!

		String newPiece = new String(ch, start, length);

		// add the new piece if it is not the new line
		// get the data only if we are in a root node
		if (!newPiece.equals("\n"))
			lastContent.append(newPiece);
	}

	/**
	 * Get the parsed log nodes
	 * @return
	 */
	public DcfLog getDcfLog() {
		return logBuilder.build();
	}
}
