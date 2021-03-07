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
	public abstract void onCredentialEvent(Credential credential,
			CommunicationProtocol.CommunicationOperation operation);

	public abstract void onUserAccountEvent(UserAccount userAccount,
			CommunicationProtocol.CommunicationOperation operation);
}
