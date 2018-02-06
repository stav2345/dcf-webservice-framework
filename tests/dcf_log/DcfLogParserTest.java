package dcf_log;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DcfLogParserTest {

	private static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	/**
	 * Get the timestamp contained in the string
	 * @param date
	 * @return
	 */
	public Timestamp getTimestamp(String date) {

		SimpleDateFormat format = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);

		Timestamp ts = null;

		try {
			Date parsedDate = format.parse(date);
			ts = new Timestamp(parsedDate.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return ts;
	}

	@Test
	public void parseLog() throws IOException {

		File file = new File("test-files" + System.getProperty("file.separator") + "log.xml");

		DcfLogParser parser = new DcfLogParser();
		DcfLog log = parser.parse(file);

		assertEquals(log.getAction(), "Web service");
		assertEquals(log.getTransmissionDate(), getTimestamp("2018-01-31T12:37:07.984+01:00"));
		assertEquals(log.getProcessingDate(), getTimestamp("2018-01-31T12:37:08.257+01:00"));
		assertEquals(log.getCatalogueCode(), "AMRPROG");
		assertEquals(log.getCatalogueVersion(), "1.3.2");
		assertEquals(log.getCatalogueStatus(), "DRAFT MINOR UNRESERVED");
		assertEquals(log.getMacroOpName(), "updateCatalogue");
		assertEquals(log.getMacroOpResult(), DcfResponse.OK);
		assertEquals(log.getMacroOpLogs().size(), 1);
		assertEquals(log.getMacroOpLogs().iterator().next(), "The catalogue AMRPROG has been updated");
		assertEquals(log.getLogNodes().size(), 1);
		assertEquals(log.isMacroOperationCorrect(), true);
	}
}
