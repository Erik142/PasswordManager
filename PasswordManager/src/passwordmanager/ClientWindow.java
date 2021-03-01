package passwordmanager;

import java.io.IOException;

import passwordmanager.config.Configuration;
import passwordmanager.forgotpassword.ForgotPasswordDialog;
import passwordmanager.forgotpassword.ForgotPasswordDialogController;
import passwordmanager.forgotpassword.ForgotPasswordModel;
import passwordmanager.forgotpassword.ForgotPasswordWindowController;
import passwordmanager.login.ForgotPasswordController;
import passwordmanager.login.LoginController;
import passwordmanager.login.LoginScreen;
import passwordmanager.login.LoginScreenController;
import passwordmanager.login.LoginScreenModel;
import passwordmanager.login.SignupController;

public class ClientWindow {
	public ClientWindow(Configuration config) throws IOException {
		PasswordClient client = new PasswordClient(config);
		
		LoginScreenModel loginModel = new LoginScreenModel(client);
		LoginScreen loginScreen = new LoginScreen();
		LoginScreenController loginScreenController = new LoginScreenController(loginScreen, loginModel);
			
		loginModel.addObserver(loginScreen);
		
		ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(client);
		ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(loginScreen.getFrame(), forgotPasswordModel);
		ForgotPasswordDialogController forgotPasswordDialogController = new ForgotPasswordDialogController(forgotPasswordDialog, forgotPasswordModel);
		ForgotPasswordWindowController forgotPasswordWindowController = new ForgotPasswordWindowController(forgotPasswordDialog, forgotPasswordModel);
		
		forgotPasswordDialog.registerListener(forgotPasswordDialogController, forgotPasswordWindowController);
		forgotPasswordModel.addObserver(forgotPasswordDialog);
		
		LoginController loginController = new LoginController();
		ForgotPasswordController forgotPasswordController = new ForgotPasswordController(forgotPasswordModel);
		SignupController signupController = new SignupController();
		
		loginScreen.registerListener(loginController, forgotPasswordController, signupController);
	}
}
