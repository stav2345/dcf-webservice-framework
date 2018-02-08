package soap_interface;

import java.io.File;

import javax.xml.soap.SOAPException;

import soap.DetailedSOAPException;

public interface IExportCatalogueFile {

	/**
	 * Export the last published version of a catalogue.
	 * @param catalogueCode the code of the catalogue to download
	 * @return a File which points to the downloaded catalogue .xml file,
	 * null if not found.
	 * @throws SOAPException
	 */
	public File exportCatalogue(String catalogueCode) throws DetailedSOAPException;
	
	/**
	 * Download a log file related to an upload catalogue file operation.
	 * @param code the code of the log to download
	 * @return a File object which points to the downloaded log file, null
	 * if not found.
	 * @throws SOAPException 
	 */
	public File exportLog(String code) throws DetailedSOAPException;
	
	
	/**
	 * Export the last internal version of the catalogue.
	 * @param catalogueCode the code of the catalogue to download
	 * @return a File object which points to the downloaded catalogue .xml file,
	 * null if not found
	 * @throws SOAPException
	 */
	public File exportLastInternalVersion(String catalogueCode) throws SOAPException;
}
