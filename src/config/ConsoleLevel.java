package config;

public enum ConsoleLevel {

	NONE("none"),
	INFO("info"),
	VERBOSE("verbose");
	
	private String level;
	private ConsoleLevel(String level) {
		this.level = level;
	}
	
	public String getLevel() {
		return level;
	}
	
	public static ConsoleLevel fromString(String text) {
		
		if (text != null) {
			for (ConsoleLevel level : ConsoleLevel.values()) {
				if (level.getLevel().equalsIgnoreCase(text))
					return level;
			}
		}
		
		return INFO; // default
	}
}
