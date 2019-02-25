package pending_request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.soap.SOAPException;

import org.junit.Test;

import config.Environment;
import dcf_log.DcfLogDownloader;
import soap_test.ExportCatalogueFileMock;
import soap_test.ExportCatalogueFileMock.MockResult;
import user.DcfUser;

public class LogDownloaderTest {

	@Test
	public static void getLogSuccess() throws SOAPException {
		DcfLogDownloader downloader = new DcfLogDownloader(new ExportCatalogueFileMock(MockResult.OK));
		DcfUser user = new DcfUser();
		user.login("avonva", "Ab123456");
		File file = downloader.getLog(user, Environment.TEST, "AAAAAAAA", 1000, 1);
		
		assertNotNull(file);
	}
	
	@Test
	public static void getLogFailed() throws SOAPException {
		DcfLogDownloader downloader = new DcfLogDownloader(new ExportCatalogueFileMock(MockResult.NULL));
		DcfUser user = new DcfUser();
		user.login("avonva", "Ab123456");
		File file = downloader.getLog(user, Environment.TEST, "AAAAAAAA", 1000, 1);
		
		assertNull(file);
	}
	
	@Test(expected = SOAPException.class)
	public static void getLog() throws SOAPException {
		DcfLogDownloader downloader = new DcfLogDownloader(new ExportCatalogueFileMock(MockResult.EXCEPTION));
		DcfUser user = new DcfUser();
		user.login("avonva", "Ab123456");
		downloader.getLog(user, Environment.TEST, "AAAAAAAA", 1000, 1);
	}
	
	@Test
	public static void getLogRetry() {
		
		DcfLogDownloader downloader = new DcfLogDownloader(new ExportCatalogueFileMock(MockResult.NULL, 1000));
		DcfUser user = new DcfUser();
		user.login("avonva", "Ab123456");
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					downloader.getLog(user, Environment.TEST, "AAAAAAAA", 1000, 2);
				} catch (SOAPException e) {
					e.printStackTrace();
				}
			}
		});
		
		t.start();
		
		// wait in order to put the downloader in wait status
		int count = 0;
		while(count < 30) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
		}
		
		// here the log have failed
		// and it is waiting for the next attempt
		
		// skip the wait and see if it is restarted
		downloader.skipWait();
		
		assertTrue(!downloader.isWaiting());
	}
}
