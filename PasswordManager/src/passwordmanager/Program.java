package passwordmanager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Base64;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import passwordmanager.communication.PasswordServer;
import passwordmanager.communication.RSA;
import passwordmanager.config.Configuration;

public class Program {

	private static final String CONFIG_PATH = "config.json";
	
	public static void main(String[] args) {
		URL res = Program.class.getClassLoader().getResource(CONFIG_PATH);
		File configFile = null;
		
		ArgumentParser parser = ArgumentParsers.newFor("PasswordManager").build()
                .defaultHelp(true)
                .description("Store and retrieve passwords from a server.");
		parser.addArgument("-c", "--config").nargs("?")
        .help("Configuration file");
		
		Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
		
        try {
        	String configFilePath = ns.getString("config");
        	configFile = new File(configFilePath);
        	System.out.println("Config path: " + configFile.getAbsolutePath());
        	
        	if (!configFile.exists()) {
        		configFile = null;
        	}
        } catch (Exception e) {
        }
        
		try {
			if (configFile == null) {
				configFile = Paths.get(res.toURI()).toFile();
			}
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
			
			try {
			PasswordServer server = new PasswordServer(config);
			
			Thread serverThread = new Thread(server);
			serverThread.start();
			
			System.out.println("Server started!");
			} catch (Exception e) {
				System.out.println("An error occured when starting the server...");
				System.out.println("Exiting...");
				e.printStackTrace();
			}
			break;
		case ServerTest:
			new PasswordServerDebug(config);
			System.exit(0);
			break;
		case RSATest:
			RSA rsa = new RSA();
			rsa.setRecipientPublicKey(rsa.getPublicKey());
			
			try {
			String originalString = "Hejsan svejsan";
			String encryptedString = Base64.getEncoder().encodeToString(rsa.encrypt(originalString.getBytes()));
			String decryptedString = new String(rsa.decrypt(Base64.getDecoder().decode(encryptedString)));
			
			System.out.println("Original string: " + originalString);
			System.out.println("Encrypted base64 string: " + encryptedString);
			System.out.println("Decrypted string: " + decryptedString);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Client:
			System.out.println("Starting new client...");
			try {
				new ClientWindow(config);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exiting...");
				return;
			}
			break;
		case Debug:
			System.out.println("Starting new debug configuration...");
			System.out.println(config.toString());
			break;
		default:
			System.out.println("" + config.appMode.toString() + " is not a a valid app mode, exiting...");
		}
	}
}
