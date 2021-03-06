package passwordmanager.communication;

import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

public abstract class CommunicationEventListener {
	public abstract void onCredentialEvent(Credential credential, CommunicationProtocol.CommunicationOperation operation);
	public abstract void onUserAccountEvent(UserAccount userAccount, CommunicationProtocol.CommunicationOperation operation);
}
