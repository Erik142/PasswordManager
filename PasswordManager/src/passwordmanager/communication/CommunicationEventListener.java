package passwordmanager.communication;

import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

/**
 * An abstract class used to trigger events when Messages have been received in
 * the CommunicationProtocol
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public abstract class CommunicationEventListener {
	/**
	 * Called when an event for a Credential occurs
	 * @param credential The Credential for which the event occured
	 * @param operation The corresponding CommunicationOperation
	 */
	public abstract void onCredentialEvent(Credential credential,
			CommunicationProtocol.CommunicationOperation operation);

	/**
	 * Called when an event for a UserAccount occurs
	 * @param userAccount The UserAccount for which the event occured
	 * @param operation The corresponding CommunicationOperation
	 */
	public abstract void onUserAccountEvent(UserAccount userAccount,
			CommunicationProtocol.CommunicationOperation operation);
}
