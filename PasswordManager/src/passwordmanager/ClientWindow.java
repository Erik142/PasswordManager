package passwordmanager;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public ClientWindow(Configuration config) throws IOException, InterruptedException, ExecutionException {		
		PasswordClient client;
		
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<PasswordClient> result = executorService.submit(new Callable<PasswordClient>() {

			@Override
			public PasswordClient call() throws Exception {
				PasswordClient client = new PasswordClient(config);
				return client;
			}
			
		});
		
		
		InitialView initialView = new InitialView();
		
		client = result.get();
		
		initialView.enableButtons();
		
		AccountModel accountModel = new AccountModel(client);
		CredentialModel credentialModel = new CredentialModel(client);

		InitialViewActionListener authActionListener = new InitialViewActionListener(initialView, accountModel, credentialModel);

		initialView.registerListener(authActionListener);

		accountModel.addObserver(authActionListener);
	}
}
