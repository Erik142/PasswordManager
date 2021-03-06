package passwordmanager;

import java.io.IOException;

import passwordmanager.communication.PasswordClient;
import passwordmanager.config.Configuration;
import passwordmanager.controller.InitialViewActionListener;
import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.model.SignUpModel;
import passwordmanager.view.InitialView;

public class ClientWindow {
	public ClientWindow(Configuration config) throws IOException {
		PasswordClient client = new PasswordClient(config);
		
		ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(client);
		LoginDialogModel loginDialogModel = new LoginDialogModel(client);
		SignUpModel signUpModel = new SignUpModel(client);
		MainModel mainModel = new MainModel(client);
		ManipulateCredentialModel manipulateCredentialModel = new ManipulateCredentialModel(client);
		AddCredentialModel addCredentialModel = new AddCredentialModel(client);
		ChangeUserAccountModel changeUserAccountModel = new ChangeUserAccountModel(client);

		InitialView initialView = new InitialView();
		
		InitialViewActionListener authActionListener = new InitialViewActionListener(initialView, forgotPasswordModel, loginDialogModel, signUpModel, mainModel, manipulateCredentialModel, addCredentialModel, changeUserAccountModel);

		initialView.registerListener(authActionListener);
		
		loginDialogModel.addObserver(authActionListener);
	}
}
