package soap_test;

import java.io.File;

import javax.xml.soap.SOAPException;

import config.Environment;
import soap.DetailedSOAPException;
import soap_interface.IExportCatalogueFile;
import user.IDcfUser;

public class ExportCatalogueFileMock implements IExportCatalogueFile {
	
	public enum MockResult {
		OK,
		NULL,
		EXCEPTION
	}
	
	private MockResult result;
	private long waitTime;
	
	public ExportCatalogueFileMock(MockResult result) {
		this(result, 0);
	}
	
	public ExportCatalogueFileMock(MockResult result, long waitTime) {
		this.result = result;
		this.waitTime = waitTime;
	}
	
	private File export(Environment env, IDcfUser user, String file) throws DetailedSOAPException {
		
		if (result == MockResult.NULL)
			return null;
		
		if (result == MockResult.EXCEPTION)
			throw new DetailedSOAPException(new SOAPException("Mock exception"));
		
		try {
			Thread.sleep(waitTime);
		}
		catch (InterruptedException e) {}
		
		return new File("test-files" + System.getProperty("file.separator") + file);
	}
	
	@Override
	public File exportCatalogue(Environment env, IDcfUser user, String catalogueCode) throws DetailedSOAPException {
		return export(env, user, "lastInternalVersion.xml");
	}

	@Override
	public File exportLog(Environment env, IDcfUser user, String code) throws DetailedSOAPException {
		return export(env, user, "log.xml");
	}

	@Override
	public File exportLastInternalVersion(Environment env, IDcfUser user, String catalogueCode) throws SOAPException {
		return export(env, user, "lastInternalVersion.xml");
	}	
}
