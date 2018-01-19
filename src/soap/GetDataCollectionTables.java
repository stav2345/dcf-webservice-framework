package soap;

import java.io.File;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;

import data_collection.IDcfDCTable;
import data_collection.IDcfDCTableLists;
import response_parser.DCResourceParser;
import user.IDcfUser;

public class GetDataCollectionTables<T extends IDcfDCTable> extends GetFile {

	private IDcfDCTableLists<T> output;

	public GetDataCollectionTables(IDcfUser user, IDcfDCTableLists<T> output, String resourceId) {
		super(user, resourceId);
		this.output = output;
	}
	
	@SuppressWarnings("unchecked")
	public IDcfDCTableLists<T> getTables() throws MySOAPException {
		
		IDcfDCTableLists<T> tables = null;
		
		Object response = getFile();
		
		// get the list from the response if possible
		if (response != null) {
			tables = (IDcfDCTableLists<T>) response;
		}
		
		return tables;
		
	}

	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {

		File file = writeXmlIntoFile(soapResponse, false);
		
		if (file == null)
			return null;
		
		try(DCResourceParser<T> parser = new DCResourceParser<>(output, file);) {
			return parser.parse();
		}
		catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
