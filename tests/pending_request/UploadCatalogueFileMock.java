package pending_request;

import java.util.UUID;

import soap.DetailedSOAPException;
import soap_interface.IUploadCatalogueFile;

public class UploadCatalogueFileMock implements IUploadCatalogueFile {

	@Override
	public String send(String attachment) throws DetailedSOAPException {
		String uuid = UUID.randomUUID().toString();
		return "LOG_" + uuid + "_WS";
	}
}
