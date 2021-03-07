package passwordmanager;

import java.io.IOException;

import passwordmanager.communication.PasswordClient;
import passwordmanager.config.Configuration;
import passwordmanager.controller.InitialViewActionListener;
import passwordmanager.model.AccountModel;
import passwordmanager.model.CredentialModel;
import passwordmanager.view.InitialView;

/**
 * Used for creating the PasswordClient object and necessary models, views and
 * controllers to create a GUI for the client
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class ClientWindow {
	/**
	 * Creates a new instance of the ClientWindow class, with the parameters
	 * specified in the Configuration object
	 * 
	 * @param config The Configuration object
	 * @throws IOException if the created PasswordClient throws IOException
	 */
	public ClientWindow(Configuration config) throws IOException {
		PasswordClient client = new PasswordClient(config);

		AccountModel accountModel = new AccountModel(client);
		CredentialModel credentialModel = new CredentialModel(client);

		InitialView initialView = new InitialView();

		InitialViewActionListener authActionListener = new InitialViewActionListener(initialView, accountModel, credentialModel);

		initialView.registerListener(authActionListener);

		accountModel.addObserver(authActionListener);
	}
}
