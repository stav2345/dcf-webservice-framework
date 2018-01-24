package soap;

import javax.xml.soap.SOAPException;

/**
 * Custom version of the soap exception which saves
 * also the reason of the exception.
 * @author avonva
 *
 */
public class MySOAPException extends SOAPException {

	private static final long serialVersionUID = 1L;

	public MySOAPException(SOAPException e) {
		super(e);
		e.printStackTrace();
	}
	
	public boolean isConnectionProblem() {
		return getError() == SOAPError.NO_CONNECTION;
	}
	
	public boolean isUnauthorized() {
		return getError() == SOAPError.UNAUTHORIZED ||
				getError() == SOAPError.FORBIDDEN;
	}
	
	public boolean isSendMessageFailed() {
		return getError() == SOAPError.MESSAGE_SEND_FAILED;
	}
	
	public SOAPError getError() {
		
		SOAPError error;
		
		String message = this.getMessage();
		
		if (message.contains("401"))
			error = SOAPError.UNAUTHORIZED;
		else if (message.contains("403"))
			error = SOAPError.FORBIDDEN;
		else if (message.contains("Message send failed"))
			error = SOAPError.MESSAGE_SEND_FAILED;
		else
			error = SOAPError.NO_CONNECTION;
		
		return error;
	}
}
