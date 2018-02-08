package soap_interface;

import soap.DetailedSOAPException;

public interface IUploadCatalogueFile {
	public String send(String attachment) throws DetailedSOAPException;
}
