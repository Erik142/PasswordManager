package passwordmanager.communication;

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
import java.util.Properties;

import passwordmanager.config.Configuration;
import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

public class PasswordDatabase {
	Connection c =null;
	Statement s=null;
	
	public PasswordDatabase(Configuration config) throws Exception {
		// Try to connect to Database
		String url = "jdbc:postgresql://" + config.dbHostName + ":" + config.dbPort + "/passwordmanager";
		
		Properties props = new Properties();
		props.setProperty("user",config.dbUserName);
		props.setProperty("password",config.dbPassword);
		
		this.c = DriverManager.getConnection(url, props);
		System.out.println("Database connected!");
	}
	

	public UserAccount getAccount(String Email) throws SQLException {
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT * FROM public.\"Accounts\" WHERE (\"Email\"='"+Email+"')");
		
		while(rs.next()) { 
			
			String email = rs.getString("Email");
			String password = rs.getString("Password");
			
			UserAccount acc= new UserAccount(email, password);
			return acc;
		}

		return null;
	}
	
	
	public void addAccount(UserAccount acc) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("INSERT INTO public.\"Accounts\" (\"Email\", \"Password\") VALUES ('"+acc.getEmail()+"','"+acc.getPassword()+"')");
	}
	
	public void addCredential(Credential cred) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("INSERT INTO public.\"Credentials\" (\"User\", \"URL\", \"Username\", \"Password\") VALUES ('"+cred.getUser()+"','"+cred.getURL()+"','"+cred.getUsername()+"','"+cred.getPassword()+"')");
	}
	
	public List<Credential> listAllCredentials(UserAccount acc) throws SQLException {
		List<Credential> list=new ArrayList<Credential>();
		
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT \"URL\", \"Username\", \"Password\" FROM public.\"Credentials\" WHERE \"User\"='"+acc.getEmail()+"'");
		
		while(rs.next()) {
			//need to return a list of credentials instead of printing it out
			String URL=rs.getString("URL");
			String Username=rs.getString("Username");
			String Password=rs.getString("Password");
			Credential cred=new Credential(acc.getEmail(), URL, Username, Password);
			list.add(cred);
		}
		
		return list;
	}
	
	public Credential listOneCredential(String account, String URL, String username) throws SQLException {
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT \"URL\", \"Username\", \"Password\" FROM public.\"Credentials\" WHERE \"User\"='"+account+"' AND \"URL\"='"+URL+"' AND \"Username\"='"+username+"'");
		while(rs.next()) {
			//needs to return an instance of Credential instead of printing it out
			String Url=rs.getString("URL");
			String Username=rs.getString("Username");
			String Password=rs.getString("Password");
			Credential cred= new Credential(account, Url, Username, Password);
			return cred;
		}
		return null;
	}
	
	public void deleteCredential(Credential cred) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM public.\"Credentials\" WHERE \"User\"='"+cred.getUser()+"' AND \"URL\"='"+cred.getURL()+"' AND \"Username\"='"+cred.getUsername()+"'");
	}
	
	public void deleteAllCredentials(UserAccount a) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM public.\"Credentials\" WHERE \"User\"='"+a.getEmail()+"'");
	}
	
	public void deleteAccount(UserAccount a) throws SQLException {
		this.deleteAllCredentials(a);
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM public.\"Accounts\" WHERE \"Email\"='"+a.getEmail()+"'");
	}
	
	public void changeAccountPassword(UserAccount a, String newPass) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("UPDATE public.\"Accounts\" SET \"Password\"='"+newPass+"' WHERE \"Email\"='"+a.getEmail()+"'");
	}
	
	public void changeCredential(Credential cred, String newPass) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("UPDATE public.\"Credentials\" SET \"Password\"='" + newPass + "', \"URL\"='" + cred.getURL() + "', \"Username\"='" + cred.getUsername() + "' WHERE \"User\"='" + cred.getUser() + "'");
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
		s.executeUpdate("INSERT INTO public.\"ResetRequests\" (\"email\") VALUES('" + account.getEmail() + "')");
	}
	
	public int getResetRequestId(UserAccount account) throws SQLException {
		this.s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT \"id\" FROM public.\"ResetRequests\" where \"email\"='" + account.getEmail() + "'");
		rs.next();
		
		return rs.getInt("id");
	}
	
	public void deleteResetRequestId(UserAccount account) throws SQLException {
		this.s = c.createStatement();
		s.executeUpdate("DELETE FROM public.\"ResetRequests\" where \"email\"='" + account.getEmail() + "'");
	}
		
	public void closeConnection() throws SQLException {
		c.close();
	}
	
}