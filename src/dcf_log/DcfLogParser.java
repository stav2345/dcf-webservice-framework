package dcf_log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Class used to parse a dcf log document and retrieve
 * all the {@link LogNode} contained in it.
 * @author avonva
 *
 */
public class DcfLogParser implements IDcfLogParser {
	
	private static final Logger LOGGER = LogManager.getLogger(DcfLogParser.class);

	private SAXParser saxParser;
	private LogParserHandler handler;
	
	/**
	 * Initialize the parser
	 */
	public DcfLogParser() {
		init();
	}

	/**
	 * Initialize the parser
	 */
	private void init() {
		
		// instantiate the SAX parser
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
			LOGGER.error("Cannot instantiate sax parser", e);
		}

		// create the parser handler
		handler = new LogParserHandler();
	}
	
	/**
	 * Parse the log document and retrieve all the operation log nodes
	 * @param file
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public DcfLog parse(File file) throws IOException {
		
		try(InputStream input = new FileInputStream(file);) {
			return this.parse(input);
		}
	}

	public DcfLog parse(InputStream input) throws IOException {
		
		try {
			saxParser.parse(input, handler);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new IOException(e);  // follow interface
		}
		
		return handler.getDcfLog();
	}
}
