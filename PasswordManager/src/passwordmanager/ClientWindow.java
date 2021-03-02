package passwordmanager;

import java.io.IOException;

import passwordmanager.addcredential.AddCredentialController;
import passwordmanager.addcredential.AddCredentialModel;
import passwordmanager.addcredential.AddDialog;
import passwordmanager.changecredential.ChangeCredentialController;
import passwordmanager.changecredential.ChangeCredentialComponentListener;
import passwordmanager.changecredential.ChangeDialog;
import passwordmanager.changecredential.ManipulateCredentialModel;
import passwordmanager.changeuserpassword.ChangePasswordController;
import passwordmanager.changeuserpassword.ChangePasswordDialog;
import passwordmanager.changeuserpassword.ChangeUserAccountModel;
import passwordmanager.config.Configuration;
import passwordmanager.forgotpassword.ForgotPasswordDialog;
import passwordmanager.forgotpassword.ForgotPasswordDialogController;
import passwordmanager.forgotpassword.ForgotPasswordModel;
import passwordmanager.forgotpassword.ForgotPasswordWindowController;
import passwordmanager.login.ForgotPasswordController;
import passwordmanager.login.LoginController;
import passwordmanager.login.LoginDialog;
import passwordmanager.login.LoginDialogController;
import passwordmanager.login.LoginDialogModel;
import passwordmanager.login.LoginScreen;
import passwordmanager.login.LoginScreenController;
import passwordmanager.login.LoginScreenModel;
import passwordmanager.login.SignupController;
import passwordmanager.mainview.AddCredentialButtonController;
import passwordmanager.mainview.ChangeAccountButtonController;
import passwordmanager.mainview.ChangeCredentialButtonController;
import passwordmanager.mainview.DeleteAccountButtonController;
import passwordmanager.mainview.DeleteCredentialButtonController;
import passwordmanager.mainview.MainModel;
import passwordmanager.mainview.MainView;
import passwordmanager.mainview.MainViewTableController;
import passwordmanager.mainview.SignOutButtonController;
import passwordmanager.signup.SignUpController;
import passwordmanager.signup.SignUpDialog;
import passwordmanager.signup.SignUpWindowController;
import passwordmanager.signup.SignUpModel;

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
		
		forgotPasswordDialog.registerListener(forgotPasswordDialogController, forgotPasswordWindowController);
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
		SignupController signupController = new SignupController(signupModel);
		
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
