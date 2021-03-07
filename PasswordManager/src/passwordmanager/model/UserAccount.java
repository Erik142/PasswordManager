package passwordmanager.model;

import java.io.Serializable;

/**
 * Represents a user account used to log in to the application
 * 
 * @author Ermin Fazlic
 * @version 2021-03-07
 */

public class UserAccount implements Serializable {

	/**
	 * The e-mail address for this UserAccount
	 */
	private String email;
	/**
	 * The password for this UserAccount
	 */
	private String password;
	private static final long serialVersionUID = -12622240058470036L;

	/**
	 * Creates a new instance of the UserAccount class with the specified e-mail
	 * address and password
	 * 
	 * @param email    The e-mail address
	 * @param password The user account password
	 */
	public UserAccount(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

}
