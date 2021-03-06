package passwordmanager.model;

import java.io.Serializable;
/**
 * @author Ermin Fazlic
 */

public class Credential implements Serializable {
	
	private String user;
	private String URL;
	private String pictureURL;
	private String username;
	private String password;
	private static final long serialVersionUID = 5070481307938381662L;
	
	public Credential(String user, String URL, String username, String password) {
		
		this.user=user;
		this.URL=URL;
		this.username=username;
		this.password=password;
		
		this.pictureURL=URL+"/favicon.ico";
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