package passwordmanager.communication;

import passwordmanager.Credential;
import passwordmanager.UserAccount;

public abstract class CommunicationEventListener {
	public abstract void onCredentialEvent(Credential credential, CommunicationProtocol.CommunicationOperation operation);
	public abstract void onUserAccountEvent(UserAccount userAccount, CommunicationProtocol.CommunicationOperation operation);
}
