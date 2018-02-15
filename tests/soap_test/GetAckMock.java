package soap_test;

import ack.DcfAck;
import config.Environment;
import soap.DetailedSOAPException;
import soap_interface.IGetAck;
import user.IDcfUser;

public class GetAckMock implements IGetAck {

	private DcfAck ack;
	
	public void setAck(DcfAck ack) {
		this.ack = ack;
	}
	
	@Override
	public DcfAck getAck(Environment env, IDcfUser user, String messageId) throws DetailedSOAPException {
		return ack;
	}
}
