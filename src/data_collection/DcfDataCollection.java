package data_collection;

import java.sql.Timestamp;

/**
 * Object which models a data collection
 * @author avonva
 *
 */
public class DcfDataCollection implements IDcfDataCollection {

	private int id = -1;
	private String code;
	private String description;
	private String category;
	private Timestamp activeFrom;
	private Timestamp activeTo;
	private String resourceId;

	public void setId(int id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setActiveFrom(Timestamp activeFrom) {
		this.activeFrom = activeFrom;
	}
	public void setActiveTo(Timestamp activeTo) {
		this.activeTo = activeTo;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
	public String getCategory() {
		return category;
	}
	public Timestamp getActiveFrom() {
		return activeFrom;
	}
	public Timestamp getActiveTo() {
		return activeTo;
	}
	public String getResourceId() {
		return resourceId;
	}

	public boolean isActive() {

		Timestamp today = new Timestamp(System.currentTimeMillis());

		boolean started = activeFrom.before(today);
		boolean notOver = activeTo.after(today);

		return started && notOver;
	}

	@Override
	public String toString() {
		return "DATA COLLECTION: id=" + (id == -1 ? "not defined yet" : id )
				+ ";code=" + code 
				+ ";description=" + description 
				+ ";category=" + category 
				+ ";activeFrom=" + activeFrom
				+ ";activeTo=" + activeTo
				+ ";resourceId=" + resourceId;
	}
}
