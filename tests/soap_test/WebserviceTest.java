package soap_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ack.DcfAck;
import catalogue.DcfCataloguesList;
import catalogue.IDcfCatalogue;
import config.Environment;
import data_collection.DcfDCTable;
import data_collection.DcfDCTablesList;
import data_collection.DcfDataCollectionsList;
import data_collection.IDcfDCTableLists;
import data_collection.IDcfDataCollection;
import dataset.DcfDatasetsList;
import dataset.IDcfDataset;
import dcf_log.DcfLogDownloader;
import dcf_log.DcfLogParser;
import dcf_log.DcfResponse;
import pending_request.IPendingRequest;
import resource.DcfResourcesList;
import resource.IDcfResourceReference;
import soap.DetailedSOAPException;
import soap.ExportCatalogueFile;
import soap.GetAck;
import soap.GetCataloguesList;
import soap.GetDataCollectionTables;
import soap.GetDataCollectionsList;
import soap.GetDataset;
import soap.GetDatasetsList;
import soap.GetResourcesList;
import soap.GetXsdFile;
import soap.Ping;
import soap.SendMessage;
import soap.UploadCatalogueFile;
import soap.UploadCatalogueFileImpl;
import soap.UploadCatalogueFileImpl.PublishLevel;
import soap.UploadCatalogueFileImpl.ReserveLevel;
import user.DcfUser;

public class WebserviceTest {

	// categories markers
	public interface PingTests {}
	public interface DownloadLogTests {}
	public interface SendMessageTests {}
	public interface GetAckTests {}
	public interface GetCataloguesListTests {}
	public interface GetDataCollectionListTests {}
	public interface GetDatasetsListTests {}
	public interface GetResourcesListTests {}
	public interface GetDatasetTests {}
	public interface GetXsdFileTests {}
	public interface ExportCatalogueFileTests {}
	public interface UploadCatalogueFileTests {}
	public interface GetDataCollectionConfigTests {}
	
	public Environment env;
	public DcfUser user;
	
	@Before
	public void init() {
		this.user = new DcfUser();
		this.user.login("avonva", "Ab123456");
		this.env = Environment.TEST;
	}
	
	@Category(PingTests.class)
	@Test(expected = DetailedSOAPException.class)
	public void wrongCredentials() throws DetailedSOAPException {

		DcfUser user = new DcfUser();
		user.login("wrong-username", "wrong-pwd");
		Ping ping = new Ping(user, env);
		ping.ping();
	}
	
	@Category(PingTests.class)
	@Test(expected = DetailedSOAPException.class)
	public void wrongUsername() throws DetailedSOAPException {

		DcfUser user = new DcfUser();
		user.login("wrong-username", "Ab123456");
		Ping ping = new Ping(user, env);
		ping.ping();
	}
	
	/* careful use, it might block dcf account
	@Category(PingTests.class)
	@Test(expected = DetailedSOAPException.class)
	public void wrongPassword() throws DetailedSOAPException {

		DcfUser user = new DcfUser();
		user.login("avonva", "wrong-pwd");
		Ping ping = new Ping(user, env);
		ping.ping();
	}*/

	@Category(PingTests.class)
	@Test
	public void ping() throws DetailedSOAPException {
		Ping ping = new Ping(user, env);
		boolean ok = ping.ping();
		assertEquals(ok, true);
	}
	
	@Category(DownloadLogTests.class)
	@Test
	public void downloadLog() throws SOAPException {
		
		DcfLogDownloader downloader = new DcfLogDownloader();
		File file = downloader.getLog(user, env, "20180130_001_WS", 2000, 10);

		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(DownloadLogTests.class)
	public void downloadNotExistingLog() throws SOAPException {
		DcfLogDownloader downloader = new DcfLogDownloader();
		File file = downloader.getLog(user, env, "not-existing-code", 2000, 10);
		assertEquals(file, null);
	}
	
	@Category(SendMessageTests.class)
	@Test(expected = IOException.class)
	public void sendNotExistingMessage() throws DetailedSOAPException, IOException {
		SendMessage request = new SendMessage(user, env);
		request.send(new File("not-existing-file.xml"));
	}
	
	@Category(SendMessageTests.class)
	@Test
	public void sendExistingMessage() throws DetailedSOAPException, IOException {
		SendMessage request = new SendMessage(user, env);
		File file = new File("test-files" + System.getProperty("file.separator") + "submit.xml");
		request.send(file);
	}

	@Category(GetAckTests.class)
	public void getNotExistingAck() throws DetailedSOAPException {
		GetAck request = new GetAck(user, env, "1920392302");
		DcfAck ack = request.getAck();
		assertEquals(ack, null);
	}
	
	@Category(GetAckTests.class)
	@Test
	public void getExistingAck() throws DetailedSOAPException {

		GetAck request = new GetAck(user, env, "12632");
		DcfAck ack = request.getAck();

		assertNotNull(ack);
		assertEquals(ack.isReady(), true);
		assertNotNull(ack.getLog());
		assertNotNull(ack.getLog().getDatasetId());
	}
	
	@Category(GetCataloguesListTests.class)
	@Test
	public void getCataloguesList() throws DetailedSOAPException {
		
		DcfCataloguesList output = new DcfCataloguesList();
		
		GetCataloguesList<IDcfCatalogue> request = new GetCataloguesList<>(user, env, output);
		request.getList();
		
		assertNotNull(output);
		assertEquals(output.isEmpty(), false);
	}
	
	@Category(GetDataCollectionListTests.class)
	@Test
	public void getDCList() throws DetailedSOAPException {
		
		DcfDataCollectionsList output = new DcfDataCollectionsList();
		GetDataCollectionsList<IDcfDataCollection> request = new GetDataCollectionsList<>(user, env, output);
		request.getList();
		
		assertNotNull(output);
	}
	
	@Category(GetDatasetsListTests.class)
	public void getNotExistingDCDatasetList() throws DetailedSOAPException {
		
		DcfDatasetsList output = new DcfDatasetsList();
		GetDatasetsList<IDcfDataset> request = new GetDatasetsList<>(user, env, "not-existing-code", output);
		request.getList();
		
		assertEquals(output.isEmpty(), true);
	}
	
	@Category(GetDatasetsListTests.class)
	@Test
	public void getTSEDatasetList() throws DetailedSOAPException {
		
		DcfDatasetsList output = new DcfDatasetsList();
		GetDatasetsList<IDcfDataset> request = new GetDatasetsList<>(user, env, "TSE.TEST", output);
		request.getList();

		assertNotNull(output);
	}
	
	@Category(GetResourcesListTests.class)
	public void getNotExistingDCResourceList() throws DetailedSOAPException {

		DcfResourcesList output = new DcfResourcesList();
		GetResourcesList<IDcfResourceReference> request = new GetResourcesList<>(user, env, "not-existing-code", output);
		request.getList();
		
		assertEquals(output.isEmpty(), true);
	}
	
	@Category(GetResourcesListTests.class)
	@Test
	public void getTSEResourceList() throws DetailedSOAPException {

		DcfResourcesList output = new DcfResourcesList();
		
		GetResourcesList<IDcfResourceReference> request = new GetResourcesList<>(user, env, "TSE.TEST", output);
		request.getList();

		assertNotNull(output);
	}
	
	@Category(GetDatasetTests.class)
	public void getNotExistingDataset() throws DetailedSOAPException {
		
		GetDataset request = new GetDataset(user, env, "not-existing-code");
		File file = request.getDatasetFile();
		assertNull(file);
	}
	
	@Category(GetDatasetTests.class)
	@Test
	public void getExistingDataset() throws DetailedSOAPException {
		
		GetDataset request = new GetDataset(user, env, "7216");
		File file = request.getDatasetFile();
		
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(GetXsdFileTests.class)
	public void getNotExistingXsdFile() throws SOAPException, SAXException, IOException, ParserConfigurationException {
		
		GetXsdFile request = new GetXsdFile(user, env, "not-existing-code");
		Document document = request.getXsdFile();
		
		assertNull(document);
	}
	
	@Category(GetXsdFileTests.class)
	@Test
	public void getExistingXsdFile() throws SOAPException, SAXException, IOException, ParserConfigurationException {
		
		GetXsdFile request = new GetXsdFile(user, env, "38864");
		Document file = request.getXsdFile();
		
		assertNotNull(file);
	}
	
	@Category(ExportCatalogueFileTests.class)
	public void exportNotExistingCatalogueLastPublishedVersion() throws SOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile(user, env);
		File file = request.exportCatalogue("not-existing-code");
		assertEquals(file, null);
	}
	
	@Category(ExportCatalogueFileTests.class)
	@Test
	public void exportCatalogueLastPublishedVersion() throws SOAPException {

		ExportCatalogueFile request = new ExportCatalogueFile(user, env);
		File file = request.exportCatalogue("ABUNDANCE");
		
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(ExportCatalogueFileTests.class)
	public void exportNotExistingCatalogueLastInternalVersion() throws SOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile(user, env);
		File file = request.exportLastInternalVersion("not-existing-code");
		assertEquals(file, null);
	}
	
	@Category(ExportCatalogueFileTests.class)
	@Test
	public void exportCatalogueLastInternalVersion() throws SOAPException {

		ExportCatalogueFile request = new ExportCatalogueFile(user, env);
		File file = request.exportLastInternalVersion("ACTION");
		
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(ExportCatalogueFileTests.class)
	public void exportNotExistingLog() throws DetailedSOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile(user, env);
		File file = request.exportLog("not-existing-code");
		assertEquals(file, null);
	}
	
	@Category(ExportCatalogueFileTests.class)
	@Test
	public void exportExistingLog() throws SOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile(user, env);
		File file = request.exportLog("20180130_001_WS");
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test(expected = IOException.class)
	public void uploadNotExistingCatalogueFile() throws SOAPException, IOException {
		UploadCatalogueFile request = new UploadCatalogueFile(user, env);
		request.send(new File("not-existing-file.xml"));
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void uploadExistingCatalogueFile() throws SOAPException, IOException {
		
		File file = new File("test-files" + System.getProperty("file.separator") + "unreserve.xml");
		UploadCatalogueFile request = new UploadCatalogueFile(user, env);
		String logCode = request.send(file);
		assertNotNull(logCode);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMinorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(user, env, PublishLevel.MINOR, "not-existing-code");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMinorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(user, env, PublishLevel.MINOR, "ACTION");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMajorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(user, env, PublishLevel.MAJOR, "not-existing-code");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMajorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(user, env, PublishLevel.MAJOR, "ACTION");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMinorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(user, env, ReserveLevel.MINOR, 
				"not-existing-code", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());

		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMinorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(user, env, ReserveLevel.MINOR, 
				"ACTION", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());

		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMajorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(user, env, ReserveLevel.MAJOR, 
				"not-existing-code", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMajorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(user, env, ReserveLevel.MAJOR, 
				"ACTION", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}

	@Category(UploadCatalogueFileTests.class)
	@Test
	public void unreserveMinorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.unreserve(user, env, 
				"not-existing-code", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void unreserveMinorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.unreserve(user, env, 
				"ACTION", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(GetDataCollectionConfigTests.class)
	@Test
	public void getNotExistingDCConfig() throws SOAPException, IOException, XMLStreamException {
		
		IDcfDCTableLists<DcfDCTable> output = new DcfDCTablesList();
		GetDataCollectionTables<DcfDCTable> req = new GetDataCollectionTables<>(user, env, output, "not-existing-code");
		req.getTables();
	}
	
	@Category(GetDataCollectionConfigTests.class)
	@Test
	public void getExistingDCConfig() throws SOAPException, IOException, XMLStreamException {
		
		IDcfDCTableLists<DcfDCTable> output = new DcfDCTablesList();
		GetDataCollectionTables<DcfDCTable> req = new GetDataCollectionTables<>(user, env, output, "05_220");
		req.getTables();
		
		assertNotNull(output);
	}
}
