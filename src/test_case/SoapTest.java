package test_case;

import java.io.File;
import java.io.IOException;

import javax.xml.soap.SOAPException;

import org.w3c.dom.Document;

import ack.DcfAck;
import catalogue.DcfCataloguesList;
import catalogue.IDcfCatalogue;
import data_collection.DcfDCTable;
import data_collection.DcfDCTablesList;
import data_collection.DcfDataCollectionsList;
import data_collection.IDcfDCTableLists;
import data_collection.IDcfDataCollection;
import dataset.DcfDatasetsList;
import dataset.IDcfDataset;
import dcf_log.LogDownloader;
import message.MessageResponse;
import resource.DcfResourcesList;
import resource.IDcfResourceReference;
import soap.ExportCatalogueFile;
import soap.GetAck;
import soap.GetCataloguesList;
import soap.GetDataCollectionTables;
import soap.GetDataCollectionsList;
import soap.GetDataset;
import soap.GetDatasetsList;
import soap.GetResourcesList;
import soap.GetXsdFile;
import soap.MySOAPException;
import soap.Ping;
import soap.SendMessage;
import soap.UploadCatalogueFile;
import user.DcfUser;
import user.IDcfUser;

public class SoapTest {

	public static void main(String[] args) throws SOAPException {
		
		String username = "";
		String password = "";
		
		if (args.length == 2) {
			username = args[0];
			password = args[1];
		}
		else {
			System.err.println("Missing username/password! "
					+ "Pass them as parameters in the command line!"
					+ "Example: java -jar program.jar myusername mypassword");
			return;
		}

		DcfUser user = new DcfUser();
		
		user.login(username, password);
		
		testGetDCConfig(user, "05_220");
		testPing(user);
		testDownloadLog(user, "20180103_001_WS");
		testAck(user);
		testGetCataloguesList(user);
		testGetDCList(user);
		testGetDatasetList(user, "TSE.TEST");
		testGetResourceList(user, "TSE.TEST");
		testGetDataset(user, "11753");
		testGetXsdFile(user, "03_868");
		testExportCatalogue(user, "ABUNDANCE");
		testExportLog(user, "20180103_001_WS");
		testExportCatalogueLastInternalVersion(user, "MTX");
		
		testSendMessage(user);
		
		try {
			testUploadCatalogueFile(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testPing(DcfUser user) throws MySOAPException {

		System.out.print("Testing Ping...");

		Ping ping = new Ping(user);
		boolean ok = ping.ping();
		
		if (!ok)
			System.err.print("WARNING: got TRXKO");
		else
			System.out.println("OK");
	}
	
	private static void testDownloadLog(IDcfUser user, String logCode) throws SOAPException {
		LogDownloader downloader = new LogDownloader(user);
		File file = downloader.getLog(logCode, 2000, 10);

		if (file != null)
			System.out.println("OK " + file);
		else
			System.err.println("No log retrieved");
	}

	private static void testAck(DcfUser user) throws MySOAPException {
		
		System.out.print("Testing GetAck...");
		GetAck request = new GetAck(user, "25053");  // dataset id related is 11753
		DcfAck ack = request.getAck();
		
		if (ack.isReady() && ack.getLog().getDatasetId().equals("11753"))
			System.out.println("OK");
		else
			System.err.println("WRONG RESULT");
	}
	
	private static void testGetCataloguesList(DcfUser user) throws MySOAPException {
		
		System.out.print("Testing GetCataloguesList...");
		
		DcfCataloguesList output = new DcfCataloguesList();
		
		GetCataloguesList<IDcfCatalogue> request = new GetCataloguesList<>(user, output);
		DcfCataloguesList list = (DcfCataloguesList) request.getList();
		
		if (list.isEmpty()) {
			System.err.println("ERROR");
			return;
		}
		
		if (list.get(0).getCode().isEmpty() || list.get(0).getVersion().isEmpty()) {
			System.err.println("ERROR");
			return;
		}
		
		System.out.println("OK");
	}
	
	private static void testGetDCList(DcfUser user) throws MySOAPException {
		
		System.out.print("Testing GetDataCollectionList...");
		
		DcfDataCollectionsList output = new DcfDataCollectionsList();
		
		GetDataCollectionsList<IDcfDataCollection> request = new GetDataCollectionsList<>(user, output);
		DcfDataCollectionsList list = (DcfDataCollectionsList) request.getList();

		if (list.isEmpty()) {
			System.err.println("WARNING: no data collection was retrieved => Check your DP account!");
			return;
		}

		if (list.get(0).getCode().isEmpty()) {
			System.err.println("ERROR: invalid data collection (empty code)");
			return;
		}
		
		System.out.println("OK");
	}
	
	private static void testGetDatasetList(DcfUser user, String dcCode) throws MySOAPException {
		
		DcfDatasetsList output = new DcfDatasetsList();
		
		System.out.print("Testing GetDatasetList...");
		GetDatasetsList<IDcfDataset> request = new GetDatasetsList<>(user, dcCode, output);
		request.getList();

		if (output.isEmpty()) {
			System.err.println("WARNING: no dataset was retrieved => Send a dataset before testing");
			return;
		}
		
		if (output.get(0).getId().isEmpty()) {
			System.err.println("ERROR: invalid dataset (empty dataset id)");
			return;
		}
		
		System.out.println("OK");
	}
	
	private static void testGetResourceList(DcfUser user, String dcCode) throws MySOAPException {
		
		System.out.print("Testing GetResourceList...");
		
		DcfResourcesList output = new DcfResourcesList();
		
		GetResourcesList<IDcfResourceReference> request = new GetResourcesList<>(user, dcCode, output);
		DcfResourcesList list = (DcfResourcesList) request.getList();

		if (list.isEmpty()) {
			System.err.println("WARNING: no resource was retrieved");
			return;
		}
		
		if (list.get(0).getResourceId().isEmpty()) {
			System.err.println("ERROR: invalid resource (empty resource id)");
			return;
		}
		
		System.out.println("OK");
	}
	
	private static void testGetDataset(DcfUser user, String datasetId) throws MySOAPException {
		
		System.out.print("Testing GetDataset...");
		GetDataset request = new GetDataset(user, datasetId);
		File file = request.getDatasetFile();
		
		if (!file.exists())
			System.err.println("FAILED: The file was not created");
		else
			System.out.println("OK");
	}
	
	private static void testGetXsdFile(DcfUser user, String fileId) throws SOAPException {
		
		System.out.print("Testing GetXsdFile...");
		GetXsdFile request = new GetXsdFile(user, fileId);
		Document xsd = request.getXsdFile();
		
		if (xsd == null)
			System.err.println("FAILED: Xsd file not found!");
		else
			System.out.println("OK");
	}
	
	
	private static void testExportCatalogue(DcfUser user, String catalogueCode) throws SOAPException {
		
		System.out.print("Testing ExportCatalogueFile (last published version)...");
		
		ExportCatalogueFile request = new ExportCatalogueFile(user);
		File file = request.exportCatalogue(catalogueCode);
		
		if (file == null || !file.exists())
			System.err.println("FAILED: The file was not created!");
		else
			System.out.println("OK");
	}
	
	private static void testExportCatalogueLastInternalVersion(DcfUser user, String catalogueCode) throws SOAPException {
		
		System.out.print("Testing ExportCatalogueFile (last internal version)...");
		
		ExportCatalogueFile request = new ExportCatalogueFile(user);
		File file = request.exportLastInternalVersion(catalogueCode);
		
		if (file == null || !file.exists())
			System.err.println("FAILED: The file was not created!");
		else
			System.out.println("OK");
	}
	
	private static void testExportLog(DcfUser user, String logCode) throws SOAPException {
		
		System.out.print("Testing ExportCatalogueFile (log)...");
		
		ExportCatalogueFile request = new ExportCatalogueFile(user);
		File file = request.exportLog(logCode);
		
		if (file == null || !file.exists())
			System.err.println("FAILED: The file was not created!");
		else
			System.out.println("OK");
	}
	
	private static void testSendMessage(DcfUser user) throws MySOAPException {
		
		System.out.print("Testing SendMessage...");
		
		File file = new File("test-files" 
				+ System.getProperty("file.separator") + "submit.xml");
		SendMessage request = new SendMessage(user, file);
		MessageResponse response = request.send();
		
		if (response != null)
			System.out.println("OK");
		else
			System.err.println("FAILED: No response received!");
	}
	
	private static void testUploadCatalogueFile(DcfUser user) 
			throws SOAPException, IOException {
		
		System.out.print("Testing UploadCatalogueFile...");
		
		File file = new File("test-files" 
				+ System.getProperty("file.separator") + "unreserve.xml");
		UploadCatalogueFile request = new UploadCatalogueFile(user);
		String logCode = request.send(file);

		if (logCode != null)
			System.out.println("OK");
		else
			System.err.println("FAILED: No log code received!");
	}
	
	private static void testGetDCConfig(DcfUser user, String resourceId) throws MySOAPException {
		
		System.out.print("Testing GetDCConfig...");
		
		IDcfDCTableLists<DcfDCTable> output = new DcfDCTablesList();
		GetDataCollectionTables<DcfDCTable> req = new GetDataCollectionTables<>(user, output, resourceId);
		req.getTables();
		
		System.out.println(output);
		
		if (output.isEmpty()) {
			System.err.println("WARNING: no resource was retrieved");
		}
		else {
			System.out.println("OK");
		}
	}
}
