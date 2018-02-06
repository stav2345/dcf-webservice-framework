package pending_request;

import soap.IUploadCatalogueFile;

import java.util.UUID;

import soap.DetailedSOAPException;

public class UploadCatalogueFileMock implements IUploadCatalogueFile {

	@Override
	public String send(String attachment) throws DetailedSOAPException {
		String uuid = UUID.randomUUID().toString();
		return "LOG_" + uuid + "_WS";
	}
}
