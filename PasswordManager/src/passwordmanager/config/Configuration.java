package passwordmanager.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

import com.google.gson.Gson;

/**
 * Represents a configuration file
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class Configuration {
	/**
	 * Specifies the application mode, either Client or Server
	 */
	public enum AppMode {
		Client, Server,
	}

	public AppMode appMode;
	public InetAddress serverIp;
	public int serverPort;
	public String dbHostName;
	public int dbPort;
	public String dbUserName;
	public String dbPassword;
	public String serverEmail;
	public String serverPassword;
	public String publicDomainName;

	/**
	 * Loads a Configuration from a File object
	 * 
	 * @param file The property file to be loaded as a Configuration
	 * @return The Configuration
	 * @throws FileNotFoundException Thrown if the file was not found on the
	 *                               computer
	 * @throws IOException
	 */
	public static Configuration GetConfiguration(File file) throws FileNotFoundException, IOException {
		if (!file.exists()) {
			throw new FileNotFoundException(
					"No configuration file found under the path '" + file.getAbsolutePath() + "'.");
		}

		FileReader fileReader = new FileReader(file);

		Gson gson = new Gson();

		Configuration configuration = gson.fromJson(fileReader, Configuration.class);

		return configuration;
	}

	@Override
	public String toString() {
		return "appMode: " + appMode.toString() + ", serverIp: " + serverIp.toString() + ", serverPort: " + serverPort;
	}
}
