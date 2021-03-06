package passwordmanager.communication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import passwordmanager.config.Configuration;
import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

/*
 * 
 * @author Ermin Fazlic
 * Used to interact with a PostgreSQL database 
 */
public class PasswordDatabase {
	Connection c =null;
	Statement s=null;
	/**
	 * Creates a new instance of the PasswordDatabase class, using the database host name and port specified in the Configuration object
	 * @param config The Configuration object
	 * @throws Exception
	 */
	public PasswordDatabase(Configuration config) throws Exception {
		// Try to connect to Database
		String url = "jdbc:postgresql://" + config.dbHostName + ":" + config.dbPort + "/passwordmanager";
		
		Properties props = new Properties();
		props.setProperty("user",config.dbUserName);
		props.setProperty("password",config.dbPassword);
		
		this.c = DriverManager.getConnection(url, props);
		System.out.println("Database connected!");
	}
	
	/**
	 * Retrieves a UserAccount object from the specified String, or null if no UserAccount could be found in the database
	 * @param Email The e-mail address for the UserAccount object
	 * @return The UserAccount object
	 * @throws SQLException
	 */
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
	
	/**
	 * Add a UserAccount object to the database
	 * @param acc The UserAccount to be added
	 * @throws SQLException
	 */
	public void addAccount(UserAccount acc) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("INSERT INTO public.\"Accounts\" (\"Email\", \"Password\") VALUES ('"+acc.getEmail()+"','"+acc.getPassword()+"')");
	}
	/**
	 * Add a Credential object to the database
	 * @param cred The Credential to be added
	 * @throws SQLException
	 */
	public void addCredential(Credential cred) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("INSERT INTO public.\"Credentials\" (\"User\", \"URL\", \"Username\", \"Password\") VALUES ('"+cred.getUser()+"','"+cred.getURL()+"','"+cred.getUsername()+"','"+cred.getPassword()+"')");
	}
	
	/**
	 * Retrieves all Credentials for a certain UserAccount object
	 * @param acc The UserAccount
	 * @return The Credentials for the specified UserAccount
	 * @throws SQLException
	 */
	public List<Credential> listAllCredentials(UserAccount acc) throws SQLException {
		List<Credential> list=new ArrayList<Credential>();
		
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT * FROM public.\"Credentials\" WHERE \"User\"='"+acc.getEmail()+"'");
		
		while(rs.next()) {
			//need to return a list of credentials instead of printing it out
			int id = rs.getInt("id");
			String URL=rs.getString("URL");
			String Username=rs.getString("Username");
			String Password=rs.getString("Password");
			Credential cred=new Credential(id, acc.getEmail(), URL, Username, Password);
			list.add(cred);
		}
		Comparator<Credential> compareById = (Credential c1, Credential c2) -> Integer.compare(c1.getId(), c2.getId());
		list.sort(compareById);

		return list;
	}
	
	public Credential listOneCredential(String account, String URL, String username) throws SQLException {
		this.s=c.createStatement();
		ResultSet rs=s.executeQuery("SELECT * FROM public.\"Credentials\" WHERE \"User\"='"+account+"' AND \"URL\"='"+URL+"' AND \"Username\"='"+username+"'");
		while(rs.next()) {
			//needs to return an instance of Credential instead of printing it out
			int id = rs.getInt("id");
			String Url=rs.getString("URL");
			String Username=rs.getString("Username");
			String Password=rs.getString("Password");
			Credential cred= new Credential(id, account, Url, Username, Password);
			return cred;
		}
		return null;
	}
	
	/**
	 * Delete a Credential object from the database
	 * @param cred The Credential object to delete
	 * @throws SQLException
	 */
	public void deleteCredential(Credential cred) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM public.\"Credentials\" WHERE \"id\"='" + cred.getId() + "'");
	}
	
	/**
	 * Deletes all Credential objects for a specified UserAccount from the database
	 * @param a The UserAccount object
	 * @throws SQLException
	 */
	public void deleteAllCredentials(UserAccount a) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM public.\"Credentials\" WHERE \"User\"='"+a.getEmail()+"'");
	}
	
	/**
	 * Deletes a UserAccount object from the database
	 * @param a The UserAccount to delete
	 * @throws SQLException
	 */
	public void deleteAccount(UserAccount a) throws SQLException {
		this.deleteAllCredentials(a);
		this.s=c.createStatement();
		s.executeUpdate("DELETE FROM public.\"Accounts\" WHERE \"Email\"='"+a.getEmail()+"'");
	}
	
	/**
	 * Change the password for a UserAccount object
	 * @param a The UserAccount object that the password will be updated for
	 * @param newPass The new password
	 * @throws SQLException
	 */
	public void changeAccountPassword(UserAccount a, String newPass) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("UPDATE public.\"Accounts\" SET \"Password\"='"+newPass+"' WHERE \"Email\"='"+a.getEmail()+"'");
	}
	
	/**
	 * Update a Credential object in the Database
	 * @param cred The Credential containing the new data
	 * @throws SQLException
	 */
	public void changeCredential(Credential cred) throws SQLException {
		this.s=c.createStatement();
		s.executeUpdate("UPDATE public.\"Credentials\" SET \"Password\"='" + cred.getPassword() + "', \"URL\"='" + cred.getURL() + "', \"Username\"='" + cred.getUsername() + "' WHERE \"id\"='" + cred.getId() + "'" + " AND \"User\"='" + cred.getUser() + "'");
	}
	
	/**
	 * Adds a "reset password" request into the database for the specified UserAccount
	 * @param account The UserAccount that the request will be made for
	 * @throws SQLException
	 */
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
	
	/**
	 * Retrieves a "forgot password" request id for the specified UserAccount object
	 * @param account The UserAccount object
	 * @return The request id number
	 * @throws SQLException
	 */
	public int getResetRequestId(UserAccount account) throws SQLException {
		this.s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT \"id\" FROM public.\"ResetRequests\" where \"email\"='" + account.getEmail() + "'");
		rs.next();
		
		return rs.getInt("id");
	}
	
	/**
	 * Deletes the "forgot password" request for the specified UserAccount from the database
	 * @param account The UserAccount object
	 * @throws SQLException
	 */
	public void deleteResetRequest(UserAccount account) throws SQLException {
		this.s = c.createStatement();
		s.executeUpdate("DELETE FROM public.\"ResetRequests\" where \"email\"='" + account.getEmail() + "'");
	}
		
	/**
	 * Closes the connection to the PostgreSQL database
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		c.close();
	}
	
}
