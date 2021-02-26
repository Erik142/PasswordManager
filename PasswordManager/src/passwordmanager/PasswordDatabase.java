package passwordmanager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/*
 * 
 * @author Ermin Fazlic
 * 
 */

import passwordmanager.config.Configuration;

public class PasswordDatabase {
	Connection c =null;
	Statement s=null;
	
	private final String DB_FILE = "PasswordManagerDatabase.db";
	
	public PasswordDatabase(Configuration config){
		// Try to connect to Database
		try {
			URL res = Program.class.getClassLoader().getResource(DB_FILE);
			File dbFile;
			
			/* 
			 * We know that the Program.class file is located two subdirectories below the git repo folder
			 * The config file specifies the db file relative to the git repo folder, therefore we use
			 * the folder of Program.class, append "../../" to go to subdirectories above that path,
			 * then add the path in the config file
			 */
			
			final String programFilePath = new File(Program.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
			dbFile = Paths.get(programFilePath, "../../", config.dbPath).normalize().toFile();
			
			Class.forName("org.sqlite.JDBC");
			System.out.println(dbFile.getAbsolutePath());
			c= DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
			System.out.println("Database connected!");
		} catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	

	public UserAccount getAccount(String Email) {
			
		try{
			
			this.s=c.createStatement();
			ResultSet rs=s.executeQuery("SELECT * FROM Accounts WHERE (Email='"+Email+"')");
			
			while(rs.next()) { 
				
				String email = rs.getString("Email");
				String password = rs.getString("Password");
				
				UserAccount acc= new UserAccount(email, password);
				return acc;
			}
			
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		return null;
		
	}
	
	
	public void addAccount(UserAccount acc) {
		
		try{
			
			this.s=c.createStatement();
			s.executeUpdate("INSERT INTO Accounts (Email, Password) VALUES ('"+acc.getEmail()+"','"+acc.getPassword()+"')");
			
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		
	}
	
	public void addCredential(Credential cred) {
		
		try{
			
			this.s=c.createStatement();
			s.executeUpdate("INSERT INTO Credentials (User, URL, Username, Password) VALUES ('"+cred.getUser()+"','"+cred.getURL()+"','"+cred.getUsername()+"','"+cred.getPassword()+"')");
			
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		
	}
	
	public List<Credential> listAllCredentials(UserAccount acc) {
		List<Credential> list=new ArrayList<>();
		try {
			this.s=c.createStatement();
			ResultSet rs=s.executeQuery("SELECT URL, Username, Password FROM Credentials WHERE User='"+acc.getEmail()+"'");
			
			while(rs.next()) {
				//need to return a list of credentials instead of printing it out
				String URL=rs.getString("URL");
				String Username=rs.getString("Username");
				String Password=rs.getString("Password");
				Credential cred=new Credential(acc.getEmail(), URL, Username, Password);
				list.add(cred);
			}
		}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
		}
		return list;
	}
	
	public Credential listOneCredential(String account, String URL, String username) {
		try {
			this.s=c.createStatement();
			ResultSet rs=s.executeQuery("SELECT URL, Username, Password FROM Credentials WHERE User='"+account+"' AND URL='"+URL+"' AND Username='"+username+"'");
			while(rs.next()) {
				//needs to return an instance of Credential instead of printing it out
				String Url=rs.getString("URL");
				String Username=rs.getString("Username");
				String Password=rs.getString("Password");
				Credential cred= new Credential(account, Url, Username, Password);
				return cred;
			}
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		return null;
	}
	
	public void deleteCredential(Credential cred) {
		try {
			this.s=c.createStatement();
			s.executeUpdate("DELETE FROM Credentials WHERE User='"+cred.getUser()+"' AND URL='"+cred.getURL()+"' AND Username='"+cred.getUsername()+"'");
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	
	public void deleteAllCredentials(UserAccount a) {
		try {
			this.s=c.createStatement();
			s.executeUpdate("DELETE FROM Credentials WHERE User='"+a.getEmail()+"'");
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	
	public void deleteAccount(UserAccount a) {
		try {
			this.deleteAllCredentials(a);
			this.s=c.createStatement();
			s.executeUpdate("DELETE FROM Accounts WHERE Email='"+a.getEmail()+"'");
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	
	public void ChangeAccountPassword(UserAccount a, String newPass) {
		try {
			this.s=c.createStatement();
			s.executeUpdate("UPDATE Accounts SET Password='"+newPass+"' WHERE Email='"+a.getEmail()+"'");
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	
	public void ChangeCredential(Credential cred, String newPass) {
		try {
			this.s=c.createStatement();
			s.executeUpdate("UPDATE Credentials SET Password='"+newPass+"' WHERE User='"+cred.getUser()+"' AND URL='"+cred.getURL()+"' AND Username='"+cred.getUsername()+"'");
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	
	public void insertResetRequest(UserAccount account) throws SQLException {
		// Check if reset request already exists for user
		try {
			int requestId = getResetRequestId(account);
			
			if (requestId > 0) {
				return;
			}
		} catch (Exception e) {
		}
		
		this.s = c.createStatement();
		s.executeUpdate("INSERT INTO ResetRequests (email) VALUES('" + account.getEmail() + "')");
	}
	
	public int getResetRequestId(UserAccount account) throws SQLException {
		this.s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT id FROM ResetRequests where email='" + account.getEmail() + "'");
		
		return rs.getInt("id");
	}
	
	public void deleteResetRequestId(UserAccount account) throws SQLException {
		this.s = c.createStatement();
		s.executeUpdate("DELETE FROM ResetRequests where email='" + account.getEmail() + "'");
	}
		
	public void closeConnection() {
		try {
			c.close();
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	
}
