package passwordmanager.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

public class Configuration {
	public enum AppMode {
		Client,
		Server,
		Debug
	}
	
	public AppMode appMode;
	public InetAddress serverIp;
	public int serverPort;
	public boolean useDummyData;
	
	public static Configuration GetConfiguration(File file) throws FileNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException("No configuration file found under the path '" + file.getAbsolutePath() + "'.");
		}
		
		Properties properties = new Properties();
		FileInputStream inputStream = new FileInputStream(file.getAbsoluteFile());
		
		properties.load(inputStream);
		
		Configuration configuration = new Configuration();
		
		String appModeString = properties.getProperty("appMode");
		String serverIpString = properties.getProperty("serverIp");
		String portString = properties.getProperty("port");
		String useDummyDataString = properties.getProperty("useDummyData");
		
		AppMode appMode = AppMode.valueOf(appModeString);
		InetAddress serverIp = InetAddress.getByName(serverIpString);
		int serverPort = Integer.parseInt(portString);
		boolean useDummyData = Boolean.parseBoolean(useDummyDataString);
		
		configuration.appMode = appMode;
		configuration.serverIp = serverIp;
		configuration.serverPort = serverPort;
		configuration.useDummyData = useDummyData;
		
		return configuration;
	}

	@Override
	public String toString() {
		return "appMode: " + appMode.toString() + ", serverIp: " + serverIp.toString() + ", serverPort: " + serverPort + ", useDummyData: " + useDummyData;
	}
}
