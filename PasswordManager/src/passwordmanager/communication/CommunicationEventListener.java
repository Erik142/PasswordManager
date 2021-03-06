package passwordmanager.communication;

import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

/**
 * @author Erik Wahlberger
 * An abstract class used to trigger events when Messages have been received in the CommunicationProtocol
 */
public abstract class CommunicationEventListener {
	public abstract void onCredentialEvent(Credential credential, CommunicationProtocol.CommunicationOperation operation);
	public abstract void onUserAccountEvent(UserAccount userAccount, CommunicationProtocol.CommunicationOperation operation);
}
