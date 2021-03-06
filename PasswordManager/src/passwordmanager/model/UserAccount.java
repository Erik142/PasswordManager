package passwordmanager.model;

import java.io.Serializable;
/**
 * @author Ermin Fazlic
 */

public class UserAccount implements Serializable {

	private String email;
	private String password;
	private static final long serialVersionUID = -12622240058470036L;
	
	public UserAccount(String email, String password) {
		this.email=email;
		this.password=password;
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
