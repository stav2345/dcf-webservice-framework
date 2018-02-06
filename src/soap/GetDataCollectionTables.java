package soap;

import java.io.File;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;

import config.Environment;
import data_collection.IDcfDCTable;
import data_collection.IDcfDCTableLists;
import response_parser.DCResourceParser;
import user.IDcfUser;

public class GetDataCollectionTables<T extends IDcfDCTable> extends GetFile {

	private IDcfDCTableLists<T> output;

	public GetDataCollectionTables(IDcfUser user, Environment env, IDcfDCTableLists<T> output, String resourceId) {
		super(user, env, resourceId);
		this.output = output;
	}
	
	public IDcfDCTableLists<T> getTables() throws SOAPException, IOException, XMLStreamException {
		
		IDcfDCTableLists<T> tables = null;
		
		File file = getFile();
		
		if (file == null)
			return null;
		
		try(DCResourceParser<T> parser = new DCResourceParser<>(output, file);) {
			tables = parser.parse();
		}
		
		return tables;	
	}
}
