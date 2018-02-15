package dcf_log;

import java.io.InputStream;
import java.util.Collection;

import ack.IDcfAckLog;
import ack.MessageValResCode;
import ack.OkCode;
import ack.OpResError;
import dataset.DcfDatasetStatus;

public class DcfAckLogMock implements IDcfAckLog {

	private String dcCode;
	private MessageValResCode messageValResCode;
	private OkCode opResCode;
	private Collection<String> opResLog;
	private OpResError opResError;
	private String datasetId;
	private DcfDatasetStatus datasetStatus;
	
	public DcfAckLogMock(OkCode opResCode, DcfDatasetStatus status) {
		this.opResCode = opResCode;
		this.datasetStatus = status;
	}
	
	/**
	 * 
	 * @param dcCode
	 * @param messageValResCode
	 * @param opResCode
	 * @param datasetId
	 * @param datasetStatus
	 */
	public DcfAckLogMock(String dcCode, MessageValResCode messageValResCode, OkCode opResCode, 
			String datasetId, DcfDatasetStatus datasetStatus) {
		this.dcCode = dcCode;
		this.messageValResCode = messageValResCode;
		this.opResCode = opResCode;
		this.datasetId = datasetId;
		this.datasetStatus = datasetStatus;
	}
	
	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}
	public void setMessageValResCode(MessageValResCode messageValResCode) {
		this.messageValResCode = messageValResCode;
	}
	public void setOpResCode(OkCode opResCode) {
		this.opResCode = opResCode;
	}
	public void setOpResLog(Collection<String> opResLog) {
		this.opResLog = opResLog;
	}
	public void setOpResError(OpResError opResError) {
		this.opResError = opResError;
	}
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
	public void setDatasetStatus(DcfDatasetStatus datasetStatus) {
		this.datasetStatus = datasetStatus;
	}
	
	@Override
	public String getDCCode() {
		return dcCode;
	}

	@Override
	public MessageValResCode getMessageValResCode() {
		return messageValResCode;
	}

	@Override
	public OkCode getOpResCode() {
		return opResCode;
	}

	@Override
	public boolean isOk() {
		if(opResCode == null)
			return false;
		
		return opResCode == OkCode.OK;
	}

	@Override
	public Collection<String> getOpResLog() {
		return opResLog;
	}

	@Override
	public boolean hasErrors() {
		return opResLog != null && !opResLog.isEmpty();
	}

	@Override
	public OpResError getOpResError() {
		return opResError;
	}

	@Override
	public String getDatasetId() {
		return datasetId;
	}

	@Override
	public DcfDatasetStatus getDatasetStatus() {
		return datasetStatus;
	}

	@Override
	public InputStream getRawLog() {
		// TODO Auto-generated method stub
		return null;
	}
}
