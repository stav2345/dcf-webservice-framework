package soap_interface;

import javax.xml.soap.SOAPException;

import ack.DcfAck;
import config.Environment;
import soap.DetailedSOAPException;
import user.IDcfUser;

/**
 * Get acknowledge request
 * @author avonva
 *
 */
public interface IGetAck {
	
	/**
	 * Get the ack of a message
	 * @return
	 * @throws SOAPException
	 */
	public DcfAck getAck(Environment env, IDcfUser user, String messageId) throws DetailedSOAPException;
}
