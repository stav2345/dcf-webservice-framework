package soap;

import config.Config;
import config.ConsoleLevel;
import user.IDcfUser;

public class SOAPConsole {

	public static void log(String action, IDcfUser user) {
		
		Config config = new Config();
		
		ConsoleLevel level = config.getConsoleLevel();
		
		if (level == ConsoleLevel.NONE)
			return;
		
		String env = config.isProductionEnvironment() ? "production" : "test";
		
		System.out.println("#WebServiceRequest#" + action + " required from user " + user.getUsername() 
			+ " in " + env + " environment");
	}
	
	public static void log(String action, Object object) {
		
		Config config = new Config();
		
		ConsoleLevel level = config.getConsoleLevel();
		
		if (level != ConsoleLevel.VERBOSE)
			return;
		
		System.out.println("#WebServiceResponse#" + action + " " + object);
	}
}
