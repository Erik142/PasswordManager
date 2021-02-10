package passwordmanager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import passwordmanager.communication.PasswordServer;
import passwordmanager.config.Configuration;

public class Program {

	private static final String CONFIG_PATH = "config.properties";
	
	public static void main(String[] args) {
		URL res = Program.class.getClassLoader().getResource(CONFIG_PATH);
		File configFile;
		
		try {
			configFile = Paths.get(res.toURI()).toFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		
		Configuration config;
		
		try {
			config = Configuration.GetConfiguration(configFile);
		}
		catch (Exception ex) {
			System.out.println("An error occured while loading the configuration file: " + ex.getMessage());
			System.out.println("Exiting...");
			return;
		}
		
		switch(config.appMode) {
		case Server:
			System.out.println("Starting new server...");
			
			PasswordServer server = new PasswordServer(config);
			
			Thread serverThread = new Thread(server);
			serverThread.start();
			
			System.out.println("Server started!");
			break;
		case Client:
			System.out.println("Starting new client...");
			break;
		case Debug:
			System.out.println("Starting new debug configuration...");
			System.out.println(config.toString());
			break;
		}
	}

}
