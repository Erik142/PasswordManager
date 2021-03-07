package passwordmanager.model;

import java.io.Serializable;

/**
 * Stores the credential data for a specified user
 * 
 * @author Ermin Fazlic.
 * @version 2021-03-07
 */
public class Credential implements Serializable {

	/**
	 * The id column in the database
	 */
	private int id;
	/**
	 * The e-mail address for the corresponding UserAccount
	 */
	private String user;
	/**
	 * Url for the Credential
	 */
	private String URL;
	/**
	 * Picture url for the Credential (Currently not used)
	 */
	private String pictureURL;
	/**
	 * Username for the Credential
	 */
	private String username;
	/**
	 * Password for the Credential
	 */
	private String password;
	private static final long serialVersionUID = 5070481307938381662L;

	/**
	 * Creates a new instance of the Credential class with the specified id, user,
	 * url, username and password
	 * 
	 * @param id       The id used in the database
	 * @param user     The UserAccount e-mail that this Credential is for
	 * @param URL      The url for the service
	 * @param username The username for the service
	 * @param password The password for the service
	 */
	public Credential(int id, String user, String URL, String username, String password) {
		this.id = id;
		this.user = user;
		this.URL = URL;
		this.username = username;
		this.password = password;

		this.pictureURL = URL + "/favicon.ico";
	}

	/**
	 * Creates a new instance of the Credential class with the specified user, url,
	 * username and password
	 * 
	 * @param user     The UserAccount e-mail that this Credential is for
	 * @param URL      The url for the service
	 * @param username The username for the service
	 * @param password The password for the service
	 */
	public Credential(String user, String URL, String username, String password) {
		this(0, user, URL, username, password);
	}

	/**
	 * 
	 * @return the id for the credential
	 */

	public int getId() {
		return id;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the pictureURL
	 */
	public String getPictureURL() {
		return pictureURL;
	}

}
