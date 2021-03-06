package passwordmanager.model;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

/**
 * Used to send "forgot password" e-mail to the specified UserAccount e-mail
 * address
 * 
 * @author Erik Wahlberger
 */
public class ForgotPasswordModel extends AbstractObservable<ForgotPasswordModel> {
	private String email = "";

	private final PasswordClient client;

	/**
	 * Creates a new instance of the ForgotPasswordModel class with the specified
	 * PasswordClient
	 * 
	 * @param client The PasswordClient
	 */
	public ForgotPasswordModel(PasswordClient client) {
		super();
		this.client = client;
	}

	/**
	 * Get the e-mail address
	 * 
	 * @return The e-mail address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Send an e-mail to the specified e-mail address
	 * 
	 * @param email The e-mail address
	 * @throws ModelException
	 */
	public void sendEmail(String email) throws ModelException {
		if (StringExtensions.isNullOrEmpty(email)) {
			throw new ModelException("E-mail field cannot be empty!");
		} else if (!EmailUtil.isValidEmail(email)) {
			throw new ModelException("The value is not a valid e-mail address.");
		} else {
			try {
				boolean result = client.forgotPassword(email);

				if (result) {
					setEmail("");
				} else {
					throw new ModelException("The account does not exist! Please sign up to create an account.");
				}
			} catch (Exception ex) {
				setEmail(email);
				throw new ModelException("Error: The server could not handle the request!");
			}
		}
	}

	/**
	 * Set the e-mail address and update the observers
	 * 
	 * @param email The e-mail address
	 */
	public void setEmail(String email) {
		this.email = email;
		updateObservers(this);
	}

}