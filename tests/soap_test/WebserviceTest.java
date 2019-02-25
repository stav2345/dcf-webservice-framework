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

		DcfUser user1 = new DcfUser();
		user1.login("wrong-username", "wrong-pwd");
		Ping ping = new Ping();
		ping.ping(this.env, user1);
	}
	
	@Category(PingTests.class)
	@Test(expected = DetailedSOAPException.class)
	public void wrongUsername() throws DetailedSOAPException {

		DcfUser user1 = new DcfUser();
		user1.login("wrong-username", "Ab123456");
		Ping ping = new Ping();
		ping.ping(this.env, user1);
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
		Ping ping = new Ping();
		boolean ok = ping.ping(this.env, this.user);
		assertEquals(ok, true);
	}
	
	@Category(DownloadLogTests.class)
	@Test
	public void downloadLog() throws SOAPException {
		
		DcfLogDownloader downloader = new DcfLogDownloader();
		File file = downloader.getLog(this.user, this.env, "20180130_001_WS", 2000, 10);

		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(DownloadLogTests.class)
	public void downloadNotExistingLog() throws SOAPException {
		DcfLogDownloader downloader = new DcfLogDownloader();
		File file = downloader.getLog(this.user, this.env, "not-existing-code", 2000, 10);
		assertEquals(file, null);
	}
	
	@Category(SendMessageTests.class)
	@Test(expected = IOException.class)
	public void sendNotExistingMessage() throws DetailedSOAPException, IOException {
		SendMessage request = new SendMessage();
		request.send(this.env, this.user, new File("not-existing-file.xml"));
	}
	
	@Category(SendMessageTests.class)
	@Test
	public void sendExistingMessage() throws DetailedSOAPException, IOException {
		SendMessage request = new SendMessage();
		File file = new File("test-files" + System.getProperty("file.separator") + "submit.xml");
		request.send(this.env, this.user, file);
	}

	@Category(GetAckTests.class)
	public void getNotExistingAck() throws DetailedSOAPException {
		GetAck request = new GetAck();
		DcfAck ack = request.getAck(this.env, this.user, "1920392302");
		assertEquals(ack, null);
	}
	
	@Category(GetAckTests.class)
	@Test
	public void getExistingAck() throws DetailedSOAPException {

		GetAck request = new GetAck();
		DcfAck ack = request.getAck(this.env, this.user, "12632");

		assertNotNull(ack);
		assertEquals(ack.isReady(), true);
		assertNotNull(ack.getLog());
		assertNotNull(ack.getLog().getDatasetId());
	}
	
	@Category(GetCataloguesListTests.class)
	@Test
	public void getCataloguesList() throws DetailedSOAPException {
		
		DcfCataloguesList output = new DcfCataloguesList();
		
		GetCataloguesList<IDcfCatalogue> request = new GetCataloguesList<>();
		request.getList(this.env, this.user, output);
		
		assertNotNull(output);
		assertEquals(output.isEmpty(), false);
	}
	
	@Category(GetDataCollectionListTests.class)
	@Test
	public void getDCList() throws DetailedSOAPException {
		
		DcfDataCollectionsList output = new DcfDataCollectionsList();
		GetDataCollectionsList<IDcfDataCollection> request = new GetDataCollectionsList<>();
		request.getList(this.env, this.user, output);
		
		assertNotNull(output);
	}
	
	@Category(GetDatasetsListTests.class)
	public void getNotExistingDCDatasetList() throws DetailedSOAPException {
		
		DcfDatasetsList output = new DcfDatasetsList();
		GetDatasetsList<IDcfDataset> request = new GetDatasetsList<>();
		request.getList(this.env, this.user, "not-existing-code", output);
		
		assertEquals(output.isEmpty(), true);
	}
	
	@Category(GetDatasetsListTests.class)
	@Test
	public void getTSEDatasetList() throws DetailedSOAPException {
		
		DcfDatasetsList output = new DcfDatasetsList();
		GetDatasetsList<IDcfDataset> request = new GetDatasetsList<>();
		request.getList(this.env, this.user, "TSE.TEST", output);

		assertNotNull(output);
	}
	
	@Category(GetResourcesListTests.class)
	public void getNotExistingDCResourceList() throws DetailedSOAPException {

		DcfResourcesList output = new DcfResourcesList();
		GetResourcesList<IDcfResourceReference> request = new GetResourcesList<>();
		request.getList(this.env, this.user, "not-existing-code", output);
		
		assertEquals(output.isEmpty(), true);
	}
	
	@Category(GetResourcesListTests.class)
	@Test
	public void getTSEResourceList() throws DetailedSOAPException {

		DcfResourcesList output = new DcfResourcesList();
		
		GetResourcesList<IDcfResourceReference> request = new GetResourcesList<>();
		request.getList(this.env, this.user, "TSE.TEST", output);

		assertNotNull(output);
	}
	
	@Category(GetDatasetTests.class)
	public void getNotExistingDataset() throws DetailedSOAPException {
		
		GetDataset request = new GetDataset();
		File file = request.getDatasetFile(this.env, this.user, "not-existing-code");
		assertNull(file);
	}
	
	@Category(GetDatasetTests.class)
	@Test
	public void getExistingDataset() throws DetailedSOAPException {
		
		GetDataset request = new GetDataset();
		File file = request.getDatasetFile(this.env, this.user, "7216");
		
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(GetXsdFileTests.class)
	public void getNotExistingXsdFile() throws SOAPException, SAXException, IOException, ParserConfigurationException {
		
		GetXsdFile request = new GetXsdFile();
		Document document = request.getXsdFile(this.env, this.user, "not-existing-code");
		
		assertNull(document);
	}
	
	@Category(GetXsdFileTests.class)
	@Test
	public void getExistingXsdFile() throws SOAPException, SAXException, IOException, ParserConfigurationException {
		
		GetXsdFile request = new GetXsdFile();
		Document file = request.getXsdFile(this.env, this.user, "38864");
		
		assertNotNull(file);
	}
	
	@Category(ExportCatalogueFileTests.class)
	public void exportNotExistingCatalogueLastPublishedVersion() throws SOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile();
		File file = request.exportCatalogue(this.env, this.user, "not-existing-code");
		assertEquals(file, null);
	}
	
	@Category(ExportCatalogueFileTests.class)
	@Test
	public void exportCatalogueLastPublishedVersion() throws SOAPException {

		ExportCatalogueFile request = new ExportCatalogueFile();
		File file = request.exportCatalogue(this.env, this.user, "ABUNDANCE");

		System.err.println(file.length());
		
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(ExportCatalogueFileTests.class)
	public void exportNotExistingCatalogueLastInternalVersion() throws SOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile();
		File file = request.exportLastInternalVersion(this.env, this.user, "not-existing-code");
		assertEquals(file, null);
	}
	
	@Category(ExportCatalogueFileTests.class)
	@Test
	public void exportCatalogueLastInternalVersion() throws SOAPException {

		ExportCatalogueFile request = new ExportCatalogueFile();
		File file = request.exportLastInternalVersion(this.env, this.user, "ACTION");
		
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(ExportCatalogueFileTests.class)
	public void exportNotExistingLog() throws DetailedSOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile();
		File file = request.exportLog(this.env, this.user, "not-existing-code");
		assertEquals(file, null);
	}
	
	@Category(ExportCatalogueFileTests.class)
	@Test
	public void exportExistingLog() throws SOAPException {
		ExportCatalogueFile request = new ExportCatalogueFile();
		File file = request.exportLog(this.env, this.user, "20180130_001_WS");
		assertNotNull(file);
		assertEquals(file.exists(), true);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test(expected = IOException.class)
	public void uploadNotExistingCatalogueFile() throws SOAPException, IOException {
		UploadCatalogueFile request = new UploadCatalogueFile();
		request.send(this.env, this.user, new File("not-existing-file.xml"));
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void uploadExistingCatalogueFile() throws SOAPException, IOException {
		
		File file = new File("test-files" + System.getProperty("file.separator") + "unreserve.xml");
		UploadCatalogueFile request = new UploadCatalogueFile();
		String logCode = request.send(this.env, this.user, file);
		assertNotNull(logCode);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMinorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(this.user, this.env, PublishLevel.MINOR, "not-existing-code");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMinorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(this.user, this.env, PublishLevel.MINOR, "ACTION");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMajorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(this.user, this.env, PublishLevel.MAJOR, "not-existing-code");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void publishMajorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.publish(this.user, this.env, PublishLevel.MAJOR, "ACTION");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMinorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(this.user, this.env, ReserveLevel.MINOR, 
				"not-existing-code", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());

		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMinorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(this.user, this.env, ReserveLevel.MINOR, 
				"ACTION", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());

		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMajorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(this.user, this.env, ReserveLevel.MAJOR, 
				"not-existing-code", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void reserveMajorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.reserve(this.user, this.env, ReserveLevel.MAJOR, 
				"ACTION", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}

	@Category(UploadCatalogueFileTests.class)
	@Test
	public void unreserveMinorNotExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.unreserve(this.user, this.env, 
				"not-existing-code", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(UploadCatalogueFileTests.class)
	@Test
	public void unreserveMinorExistingCatalogue() throws SOAPException, IOException {
		
		UploadCatalogueFileImpl ucf = new UploadCatalogueFileImpl();
		
		IPendingRequest request = ucf.unreserve(this.user, this.env, 
				"ACTION", "JUnit test case DCF webservices");

		DcfResponse response = request.start(new DcfLogParser());
		
		assertNotNull(response);
	}
	
	@Category(GetDataCollectionConfigTests.class)
	@Test
	public void getNotExistingDCConfig() throws SOAPException, IOException, XMLStreamException {
		
		IDcfDCTableLists<DcfDCTable> output = new DcfDCTablesList();
		GetDataCollectionTables<DcfDCTable> req = new GetDataCollectionTables<>();
		req.getTables(this.env, this.user, "not-existing-code", output);
	}
	
	@Category(GetDataCollectionConfigTests.class)
	@Test
	public void getExistingDCConfig() throws SOAPException, IOException, XMLStreamException {
		
		IDcfDCTableLists<DcfDCTable> output = new DcfDCTablesList();
		GetDataCollectionTables<DcfDCTable> req = new GetDataCollectionTables<>();
		req.getTables(this.env, this.user, "05_220", output);
		
		assertNotNull(output);
	}
}
