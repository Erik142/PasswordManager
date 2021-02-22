package passwordmanager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Base64;

import passwordmanager.communication.PasswordServer;
import passwordmanager.communication.RSA;
import passwordmanager.config.Configuration;

public class Program {

	private static final String CONFIG_PATH = "config.json";
	
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
			break;
		case Debug:
			System.out.println("Starting new debug configuration...");
			System.out.println(config.toString());
			break;
		}
	}
}
