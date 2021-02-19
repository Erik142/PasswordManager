package passwordmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PasswordDatabase {
	Connection c =null;
	Statement s=null;
	
	PasswordDatabase(){
		// Try to connect to Database
		try {
			
			Class.forName("org.sqlite.JDBC");
			c= DriverManager.getConnection("jdbc:sqlite:PasswordManagerDatabase.db");
			System.out.println("Database connected!");
		} catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	

	public void getAccount(String account) {
		
				
		try{
			
			this.s=c.createStatement();
			ResultSet rs=s.executeQuery("SELECT * FROM Accounts WHERE (Email='"+account+"')");
			while(rs.next()) { 
				String email = rs.getString("Email");
				String password = rs.getString("Password");
				//Need to change so it creates an instance of UserAccount and
				//returns it with these values
				System.out.println(email+" "+password);
			}
			
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		
	}
	
	
	public void addAccount(String account, String pass) {
		
		try{
			
			
			this.s=c.createStatement();
			s.executeUpdate("INSERT INTO Accounts (Email, Password) VALUES ('"+account+"','"+pass+"')");
			
			
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		
	}
public void addCredential(String account, String URL, String user, String pass) {
		
		try{
			
			
			this.s=c.createStatement();
			s.executeUpdate("INSERT INTO Credentials (User, URL, Username, Password) VALUES ('"+account+"','"+URL+"','"+user+"','"+pass+"')");
			
			
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		
	}
	public void executeQuery(String query) {
		try {
			this.s=c.createStatement();
			s.executeQuery(query);
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
public void listAllCredentials(String account) {
	try {
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT URL, Username, Password FROM Credentials WHERE User='"+account+"'");
		while(rs.next()) {
			//need to return a list of credentials instead of printing it out
			String URL=rs.getString("URL");
			String Username=rs.getString("Username");
			String Password=rs.getString("Password");
			System.out.println(URL+" "+Username+" "+Password);
		}
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
public void listOneCredential(String account, String URL, String username) {
	try {
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT URL, Username, Password FROM Credentials WHERE User='"+account+"' AND URL='"+URL+"' AND Username='"+username+"'");
		while(rs.next()) {
			//needs to return an instance of Credential instead of printing it out
			String Url=rs.getString("URL");
			String Username=rs.getString("Username");
			String Password=rs.getString("Password");
			System.out.println(Url+" "+Username+" "+Password);
		}
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
public void deleteCredential(String account, String URL, String username) {
	try {
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM Credentials WHERE User='"+account+"' AND URL='"+URL+"' AND Username='"+username+"'");
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
public void deleteAllCredentials(String account) {
	try {
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM Credentials WHERE User='"+account+"'");
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
public void deleteAccount(String account) {
	try {
		this.deleteAllCredentials(account);
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM Accounts WHERE Email='"+account+"'");
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
public void ChangeAccountPassword(String account, String newPass) {
	try {
		this.s=c.createStatement();
		s.executeUpdate("UPDATE Accounts SET Password='"+newPass+"' WHERE Email='"+account+"'");
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
public void ChangeCredential(String account,String URL, String username, String newPass) {
	try {
		this.s=c.createStatement();
		s.executeUpdate("UPDATE Credentials SET Password='"+newPass+"' WHERE User='"+account+"' AND URL='"+URL+"' AND Username='"+username+"'");
	}catch(Exception e) {
		System.out.println("Error: "+ e.getMessage());
	}
}
	
	
	public void closeConnection() {
		try {
			c.close();
		}catch(Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
	}
	/*
	public static void main(String[] args) {
		PasswordDatabase db=new PasswordDatabase();
		//db.addAccount("Tester", "password");
		//db.addCredential("Tester", "Facebook.com", "username", "pass");
		//db.getAccount("Tester");
		//db.listOneCredential("Tester", "Facebook.com");
		//db.deleteCredential("Tester", "Youtube.com", "username2");
		//db.deleteAllCredentials("Tester");
		//db.listAllCredentials("Tester");
		//db.deleteAccount("Test2@gmail.com");
		//db.ChangeAccountPassword("Test2@gmail.com", "Password");
		//db.ChangeCredential("Tester", "Facebook.com", "username2", "newpassword");
		db.closeConnection();
	}*/
}
