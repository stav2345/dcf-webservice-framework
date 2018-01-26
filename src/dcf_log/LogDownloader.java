package dcf_log;

import java.io.File;

import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import soap.ExportCatalogueFile;
import soap.UploadCatalogueFile;
import user.DcfUser;
import user.IDcfUser;

/**
 * Class used to download a dcf Log document starting from a log code.
 * Since a log is not immediately available in DCF, a polling
 * strategy can be used. In particular, it is possible to set the inter
 * attempts time and a limit to the maximum number of attempts.
 * @author avonva
 *
 */
public class LogDownloader {

	private static final Logger LOGGER = LogManager.getLogger(LogDownloader.class);
	
	private IDcfUser user;  // user who requested the log download
	
	/**
	 * Initialise the log download process.
	 * @param user the user who required the download of the log. The user
	 * must be logged in to perform the download.
	 */
	public LogDownloader(IDcfUser user) {
		this.user = user;
	}
	
	/**
	 * Download a log without polling strategy
	 * @param logCode the code of the log to download
	 * @throws SOAPException
	 */
	public File getLog(String logCode) throws SOAPException {
		ExportCatalogueFile req = new ExportCatalogueFile(user);
		return req.exportLog(logCode);
	}
	
	/**
	 * Download a log from DCF with a polling strategy
	 * @param logCode the code of the log to download
	 * @param interAttemptsTime waiting time before trying again to download the log
	 * @throws SOAPException 
	 */
	public File getLog(String logCode, long interAttemptsTime) throws SOAPException {
		return getLog(logCode, interAttemptsTime, -1);
	}
	
	/**
	 * Download a log from DCF with a polling strategy
	 * @param logCode the code of the log to download
	 * @param interAttemptsTime waiting time before trying again to download the log
	 * @param maxAttempts max number of allowed attempts (prevents DOS)
	 * @throws SOAPException 
	 */
	public File getLog(String logCode, long interAttemptsTime, int maxAttempts) throws SOAPException {
		
		// if maxAttempts is > 0 then a limit is applied
		boolean isLimited = maxAttempts > 0;
		
		File log = null;
		
		// number of tried attempts
		int attemptsCount = 1;
		
		// polling
		while(true) {

			String diagnostic = "Getting log=" + logCode + ", attempt n°=" + attemptsCount;
			
			// add maximum number of attempts if limited attempts
			if(isLimited)
				diagnostic += "/" + maxAttempts;
			
			LOGGER.info(diagnostic);

			log = getLog(logCode);
			
			if(log == null)
				LOGGER.info("Log=" + logCode + " not available yet in DCF");

			// if the log was found or no another attempt is available
			if(log != null || (isLimited && attemptsCount >= maxAttempts))
				break;
			
			LOGGER.info("Waiting " + (interAttemptsTime/1000.00) 
					+ " seconds and then retry to download log=" + logCode);
			
			// wait inter attempts time
			try {
				Thread.sleep(interAttemptsTime);
			} catch(InterruptedException e) {
				e.printStackTrace();
				LOGGER.error("Cannot sleep thread=" + this, e);
			}
			
			// go to the next attempt
			attemptsCount++;
		}
		
		return log;
	}
	
	/**
	 * Get the xml message to reserve a catalogue. We need the catalogue code and
	 * the descriptions, which is the reason why we are reserving the catalogue
	 * @param code
	 * @param description
	 * @param opType, can be reserveMinor or reserveMajor
	 * @return
	 */
	public static String getReserveMessage ( String code, String description ) {

		String op = "unreserve";
		
		String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<message xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xsi:noNamespaceSchemaLocation=\"file:///D:/cat_xsd/UploadCatalogue.xsd\">"
				+ "<updateCatalogue catalogueCode=\"" + code + "\">"
				+ "<" + op + ">"
				+ "<reservationNote>" + description + "</reservationNote>"
				+ "</" + op + ">"
				+ "</updateCatalogue>"
				+ "</message>";

		return message;
	}
	
	public static void main(String[] args) throws SOAPException {
		
		DcfUser user = new DcfUser();
		user.login("avonva", "Ab123456");
		
		System.out.println("up");
		
		UploadCatalogueFile upf = new UploadCatalogueFile(user);
		String logCode = upf.send(getReserveMessage("ACTION", "test for log downloader"));
		
		System.out.println("Log code " + logCode);
		
		LogDownloader downloader = new LogDownloader(user);
		File file = downloader.getLog(logCode, 2000, 10);
		System.out.println(file);
	}
}
