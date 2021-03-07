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
		/**
		 * Client application mode
		 */
		Client, 
		/**
		 * Server application mode
		 */
		Server,
	}

	/**
	 * The application mode
	 */
	public AppMode appMode;
	/**
	 * The server IP address or hostname
	 */
	public InetAddress serverIp;
	/**
	 * The server TCP port number
	 */
	public int serverPort;
	/**
	 * The IP address or hostname for the PostgreSQL server
	 */
	public String dbHostName;
	/**
	 * The port number for the PostgreSQL server
	 */
	public int dbPort;
	/**
	 * The user name for the PostgreSQL user
	 */
	public String dbUserName;
	/**
	 * The password for the PostgreSQL user
	 */
	public String dbPassword;
	/**
	 * The email address to send emails from (Must be a gmail address)
	 */
	public String serverEmail;
	/**
	 * The password for the server email address to send emails from
	 */
	public String serverEmailPassword;
	/**
	 * The public domain name for the "forgot password" web UI
	 */
	public String publicDomainName;

	/**
	 * Loads a Configuration from a File object
	 * 
	 * @param file The property file to be loaded as a Configuration
	 * @return The Configuration
	 * @throws FileNotFoundException Thrown if the file was not found on the
	 *                               computer
	 */
	public static Configuration GetConfiguration(File file) throws FileNotFoundException {
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
