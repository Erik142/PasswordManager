package passwordmanager;

import java.io.IOException;

import passwordmanager.communication.PasswordClient;
import passwordmanager.config.Configuration;
import passwordmanager.controller.AddCredentialButtonController;
import passwordmanager.controller.AddCredentialController;
import passwordmanager.controller.ChangeAccountButtonController;
import passwordmanager.controller.ChangeCredentialButtonController;
import passwordmanager.controller.ChangeCredentialComponentListener;
import passwordmanager.controller.ChangeCredentialController;
import passwordmanager.controller.ChangePasswordController;
import passwordmanager.controller.DeleteAccountButtonController;
import passwordmanager.controller.DeleteCredentialButtonController;
import passwordmanager.controller.ForgotPasswordController;
import passwordmanager.controller.ForgotPasswordDialogController;
import passwordmanager.controller.ForgotPasswordWindowController;
import passwordmanager.controller.LoginController;
import passwordmanager.controller.LoginDialogController;
import passwordmanager.controller.LoginScreenController;
import passwordmanager.controller.MainViewTableController;
import passwordmanager.controller.SignOutButtonController;
import passwordmanager.controller.SignUpController;
import passwordmanager.controller.SignUpWindowController;
import passwordmanager.controller.SignUpDialogController;
import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.LoginScreenModel;
import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.model.SignUpModel;
import passwordmanager.view.AddDialog;
import passwordmanager.view.ChangeDialog;
import passwordmanager.view.ChangePasswordDialog;
import passwordmanager.view.ForgotPasswordDialog;
import passwordmanager.view.LoginDialog;
import passwordmanager.view.LoginScreen;
import passwordmanager.view.MainView;
import passwordmanager.view.SignUpDialog;

public class ClientWindow {
	public ClientWindow(Configuration config) throws IOException {
		PasswordClient client = new PasswordClient(config);
		
		LoginScreenModel loginModel = new LoginScreenModel();
		LoginScreen loginScreen = new LoginScreen();
		LoginScreenController loginScreenController = new LoginScreenController(loginScreen, loginModel);
			
		loginModel.addObserver(loginScreen);
		
		ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(client);
		ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(loginScreen.getFrame(), forgotPasswordModel);
		ForgotPasswordDialogController forgotPasswordDialogController = new ForgotPasswordDialogController(forgotPasswordDialog, forgotPasswordModel);
		ForgotPasswordWindowController forgotPasswordWindowController = new ForgotPasswordWindowController(forgotPasswordDialog, forgotPasswordModel);
		
		forgotPasswordDialog.registerListener(forgotPasswordDialogController);
		forgotPasswordModel.addObserver(forgotPasswordDialog);
		
		SignUpModel signupModel = new SignUpModel(client);
		SignUpDialog signupDialog = new SignUpDialog(loginScreen.getFrame());
		SignUpController signupDialogController = new SignUpController(signupDialog, signupModel);
		SignUpWindowController signupWindowController = new SignUpWindowController(signupModel);
		
		signupModel.addObserver(signupDialog);
		
		signupDialog.registerListener(signupDialogController, signupWindowController);
		
		LoginDialogModel loginDialogModel = new LoginDialogModel(client);
		LoginDialog loginDialog = new LoginDialog(loginScreen.getFrame());
		LoginDialogController loginDialogController = new LoginDialogController(loginDialog, loginDialogModel);
		
		loginDialogModel.addObserver(loginModel);
		loginDialogModel.addObserver(loginDialog);
		loginDialog.registerListener(loginDialogController);
		
		LoginController loginController = new LoginController(loginDialogModel);
		ForgotPasswordController forgotPasswordController = new ForgotPasswordController(forgotPasswordModel);
		SignUpDialogController signupController = new SignUpDialogController(signupModel);
		
		loginScreen.registerListener(loginController, forgotPasswordController, signupController);
		
		MainModel mainModel = new MainModel(client);
		AddCredentialModel addCredentialModel = new AddCredentialModel(client);
		ManipulateCredentialModel manipulateCredentialModel = new ManipulateCredentialModel(client);
		ChangeUserAccountModel changeUserAccountModel = new ChangeUserAccountModel(client);
		
		loginDialogModel.addObserver(changeUserAccountModel);
		
		MainView mainView = new MainView();
		MainViewTableController mainTableController = new MainViewTableController(mainView, mainModel, manipulateCredentialModel);
		AddCredentialButtonController addCredentialButtonController = new AddCredentialButtonController(addCredentialModel);
		ChangeCredentialButtonController changeCredentialButtonController = new ChangeCredentialButtonController(mainView, manipulateCredentialModel);
		ChangeAccountButtonController changeAccountButtonController = new ChangeAccountButtonController(changeUserAccountModel);
		DeleteAccountButtonController deleteAccountButtonController = new DeleteAccountButtonController(mainModel, loginDialogModel, loginModel);
		DeleteCredentialButtonController deleteCredentialButtonController = new DeleteCredentialButtonController(mainView, mainModel, manipulateCredentialModel);
		SignOutButtonController signOutButtonController = new SignOutButtonController(mainModel, loginModel, loginDialogModel);
		
		loginDialogModel.addObserver(mainModel);
		
		mainModel.addObserver(mainView);
		
		mainView.registerListeners(mainTableController, addCredentialButtonController, 
				changeCredentialButtonController, deleteCredentialButtonController, 
				changeAccountButtonController, deleteAccountButtonController, 
				signOutButtonController);
		
		
		AddDialog addCredentialDialog = new AddDialog(mainView.getFrame());
		AddCredentialController addCredentialController = new AddCredentialController(addCredentialDialog, addCredentialModel, mainModel);
		
		addCredentialModel.addObserver(addCredentialDialog);
		addCredentialModel.addObserver(mainModel);
		
		addCredentialDialog.registerListener(addCredentialController);
		
		ChangeDialog changeCredentialDialog = new ChangeDialog(mainView.getFrame());
		ChangeCredentialController changeCredentialController = new ChangeCredentialController(changeCredentialDialog, manipulateCredentialModel);
		ChangeCredentialComponentListener changeCredentialComponentListener = new ChangeCredentialComponentListener(mainModel);
		
		manipulateCredentialModel.addObserver(changeCredentialDialog);
		
		changeCredentialDialog.registerListener(changeCredentialController, changeCredentialComponentListener);
		
		ChangePasswordDialog changeUserAccountDialog = new ChangePasswordDialog(mainView.getFrame());
		ChangePasswordController changeUserAccountController = new ChangePasswordController(changeUserAccountDialog, changeUserAccountModel);
		
		changeUserAccountModel.addObserver(changeUserAccountDialog);
		
		changeUserAccountDialog.registerListener(changeUserAccountController);
	}
}
