package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.model.Observer;
import passwordmanager.model.SignUpModel;
import passwordmanager.model.UserAccount;
import passwordmanager.view.ForgotPasswordDialog;
import passwordmanager.view.InitialView;
import passwordmanager.view.LoginDialog;
import passwordmanager.view.MainView;
import passwordmanager.view.SignUpDialog;

public class InitialViewActionListener implements ActionListener, Observer<LoginDialogModel> {

	public final int LOG_IN = 0;
	public final int SIGN_UP = 1;
	public final int FORGOT_PASSWORD = 2;

	private final ForgotPasswordModel forgotPasswordModel;
	private final LoginDialogModel loginDialogModel;
	private final SignUpModel signUpModel;
	private final MainModel mainModel;
	private final ManipulateCredentialModel manipulateCredentialModel;
	private final AddCredentialModel addCredentialModel;
	private final ChangeUserAccountModel changeUserAccountModel;

	private final InitialView initialView;

	private LoginDialog loginDialog;

	public InitialViewActionListener(InitialView initialView, ForgotPasswordModel forgotPasswordModel,
			LoginDialogModel loginDialogModel, SignUpModel signUpModel, MainModel mainModel,
			ManipulateCredentialModel manipulateCredentialModel, AddCredentialModel addCredentialModel, ChangeUserAccountModel changeUserAccountModel) {
		this.forgotPasswordModel = forgotPasswordModel;
		this.loginDialogModel = loginDialogModel;
		this.signUpModel = signUpModel;
		this.mainModel = mainModel;
		this.manipulateCredentialModel = manipulateCredentialModel;
		this.addCredentialModel = addCredentialModel;
		this.changeUserAccountModel = changeUserAccountModel;

		this.initialView = initialView;
	}

	private void forgotPassword() {
		ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(initialView.getFrame(),
				forgotPasswordModel);
		ForgotPasswordDialogController forgotPasswordController = new ForgotPasswordDialogController(
				forgotPasswordDialog, forgotPasswordModel);

		forgotPasswordModel.addObserver(forgotPasswordDialog);
		forgotPasswordDialog.registerListener(forgotPasswordController);

		forgotPasswordDialog.setVisible(true);
	}

	private void login() {
		loginDialog = new LoginDialog(initialView.getFrame());
		LoginDialogController loginDialogController = new LoginDialogController(loginDialog, loginDialogModel);

		loginDialogModel.addObserver(loginDialog);
		loginDialog.registerListener(loginDialogController);

		loginDialog.setVisible(true);
	}

	private void signUp() {
		SignUpDialog signUpDialog = new SignUpDialog(initialView.getFrame());
		SignUpController signUpDialogController = new SignUpController(signUpDialog, signUpModel);

		signUpModel.addObserver(signUpDialog);
		signUpDialog.registerListener(signUpDialogController);

		signUpDialog.setVisible(true);
	}

	private void showMainView() {
		MainView mainView = new MainView();
		MainViewTableController mainViewTableController = new MainViewTableController(mainView, mainModel,
				manipulateCredentialModel);
		MainViewActionListener actionListener = new MainViewActionListener(mainView, mainModel, addCredentialModel, manipulateCredentialModel, loginDialogModel, changeUserAccountModel);
				
		mainModel.addObserver(mainView);
		mainView.registerListeners(mainViewTableController, actionListener);

		mainView.getFrame().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case LOG_IN:
			login();
			break;
		case SIGN_UP:
			signUp();
			break;
		case FORGOT_PASSWORD:
			forgotPassword();
			break;
		default:
		}
	}

	@Override
	public void update(LoginDialogModel observable) {
		System.out.println("Logged in status: " + observable.getLoggedInStatus());
		if (observable.getLoggedInStatus() && initialView.getFrame().isVisible()) {
			UserAccount account = new UserAccount(observable.getEmail(), observable.getPassword());
			mainModel.setUserAccount(account);
			changeUserAccountModel.setAccount(account);

			loginDialog.setVisible(false);
			loginDialog.dispose();
			loginDialogModel.removeObserver(loginDialog);
			initialView.getFrame().setVisible(false);
			showMainView();
			mainModel.updateCredentials();
		} else if (!observable.getLoggedInStatus()) {
			initialView.getFrame().setVisible(true);
		}
	}

}
