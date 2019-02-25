package dcf_log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Builder to create a single LogNode step by step.
 * @author avonva
 * @author shahaal
 */
public class LogNodeBuilder {

	private String name;
	private DcfResponse result;
	private Collection<String> opLogs;

	public LogNodeBuilder() {
		this.opLogs = new ArrayList<>();
	}

	public LogNodeBuilder setName(String name) {
		this.name = name;
		return this;
	}
	public LogNodeBuilder setResult(String result) {
		
	    try {
	    	this.result = DcfResponse.valueOf( result );
	    } catch (IllegalArgumentException e) {
	    	e.printStackTrace();
	    	this.result = DcfResponse.ERROR;
	    }

		return this;
	}
	public LogNodeBuilder setResult(DcfResponse result) {
		this.result = result;
		return this;
	}
	public LogNodeBuilder addOpLog( String opLog ) {
		this.opLogs.add( opLog );
		return this;
	}
	
	public void setOpLogs(Collection<String> opLogs) {
		this.opLogs = opLogs;
	}

	public LogNode build() {
		return new LogNode( this.name, this.result, this.opLogs );
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "name=" + this.name + "; result=" + this.result + "; opLogs=" + this.opLogs;
	}
}
