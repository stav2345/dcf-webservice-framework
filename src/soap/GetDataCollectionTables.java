package soap;

import java.io.File;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;

import data_collection.IDcfDCTableLists;
import response_parser.DCResourceParser;
import user.IDcfUser;

public class GetDataCollectionTables extends GetFile {

	private IDcfDCTableLists output;

	public GetDataCollectionTables(IDcfUser user, IDcfDCTableLists output, String resourceId) {
		super(user, resourceId);
		this.output = output;
	}
	
	public IDcfDCTableLists getTables() throws MySOAPException {
		
		IDcfDCTableLists tables = null;
		
		Object response = makeRequest(getUrl());
		
		// get the list from the response if possible
		if (response != null) {
			tables = (IDcfDCTableLists) response;
		}
		
		return tables;
		
	}

	@Override
	public Object processResponse(SOAPMessage soapResponse) throws SOAPException {

		File file = writeXmlIntoFile(soapResponse, false);
		
		if (file == null)
			return null;
		
		try(DCResourceParser parser = new DCResourceParser(output, file);) {
			return parser.parse();
		}
		catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
