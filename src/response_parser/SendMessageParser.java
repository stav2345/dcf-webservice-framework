package response_parser;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

import org.w3c.dom.NodeList;

import message.MessageResponse;
import message.TrxCode;

/**
 * Parse the DCF response of a send message call.
 * @author avonva
 * @author shahaal
 *
 */
public class SendMessageParser {
	
	private static final String MESSAGE_ID_NODE = "messageId";
	private static final String TRX_STATE_NODE = "trxState";
	private static final String TRX_ERROR_NODE = "trxErr";

	public static MessageResponse parse(SOAPBody body) throws SOAPException {
		
		// extract the information
		String messageId = extractMessageId(body);
		TrxCode trxCode = extractTrxState(body);
		String trxErr = extractTrxError(body);
		
		// create the response
		MessageResponse messageResponse = new MessageResponse(messageId, trxCode, trxErr);
		
		return messageResponse;
	}
	
	/**
	 * Extract the message id from the response
	 * @param soapResponse
	 * @return
	 * @throws SOAPException
	 */
	private static String extractMessageId(SOAPBody body) throws SOAPException {
		
		NodeList msgId = body.getElementsByTagName(MESSAGE_ID_NODE);
		
		if (msgId.getLength() == 0)
			return null;
		
		return msgId.item(0).getTextContent();
	}
	
	/**
	 * Extract the trx state from the response
	 * @param soapResponse
	 * @return
	 * @throws SOAPException
	 */
	private static TrxCode extractTrxState(SOAPBody body) throws SOAPException {
		
		NodeList trxStateList = body.getElementsByTagName(TRX_STATE_NODE);
		
		if (trxStateList.getLength() == 0)
			return null;
		
		String trxState = trxStateList.item(0).getTextContent();
		
		return TrxCode.fromString(trxState);
	}
	
	/**
	 * Extract the trx error if present from the response
	 * @param soapResponse
	 * @return
	 * @throws SOAPException
	 */
	private static String extractTrxError(SOAPBody body) throws SOAPException {
		
		NodeList msgId = body.getElementsByTagName(TRX_ERROR_NODE);
		
		if (msgId.getLength() == 0)
			return null;
		
		return msgId.item(0).getTextContent();
	}
}
