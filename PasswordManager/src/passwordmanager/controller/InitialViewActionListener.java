package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.AccountModel;
import passwordmanager.model.CredentialModel;
import passwordmanager.model.Observer;
import passwordmanager.model.UserAccount;
import passwordmanager.view.ForgotPasswordDialog;
import passwordmanager.view.InitialView;
import passwordmanager.view.LoginDialog;
import passwordmanager.view.MainView;
import passwordmanager.view.SignUpDialog;

/**
 * JButton ActionListener for the InitialView class
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class InitialViewActionListener implements ActionListener, Observer<AccountModel> {

	/**
	 * Action command used for the Log in button
	 */
	public final int LOG_IN = 0;
	/**
	 * Action command used for the Sign up button
	 */
	public final int SIGN_UP = 1;
	/**
	 * Action command used for the Forgot password button
	 */
	public final int FORGOT_PASSWORD = 2;

	private final AccountModel accountModel;
	private final CredentialModel credentialModel;

	private final InitialView initialView;

	private LoginDialog loginDialog;

	/**
	 * Creates a new instance of the InitialViewActionListener with the specified
	 * InitialView object and related models
	 * 
	 * @param initialView               The InitialView object
	 * @param accountModel       		The AccountModel
	 * @param credentialModel          	The CredentialModel
	 */
	public InitialViewActionListener(InitialView initialView, AccountModel accountModel, CredentialModel credentialModel) {
		this.accountModel = accountModel;
		this.credentialModel = credentialModel;

		this.initialView = initialView;
	}

	/**
	 * Initializes and opens a new ForgotPasswordDialog
	 */
	private void forgotPassword() {
		ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(initialView.getFrame());
		ForgotPasswordDialogController forgotPasswordController = new ForgotPasswordDialogController(
				forgotPasswordDialog, accountModel);

		forgotPasswordDialog.registerListener(forgotPasswordController);

		forgotPasswordDialog.setVisible(true);
	}

	/**
	 * Initializes and opens a new LoginDialog
	 */
	private void login() {
		loginDialog = new LoginDialog(initialView.getFrame());
		LoginDialogController loginDialogController = new LoginDialogController(loginDialog, accountModel);

		loginDialog.registerListener(loginDialogController);

		loginDialog.setVisible(true);
	}

	/**
	 * Initializes and opens a new SignUpDialog
	 */
	private void signUp() {
		SignUpDialog signUpDialog = new SignUpDialog(initialView.getFrame());
		SignUpController signUpDialogController = new SignUpController(signUpDialog, accountModel);

		signUpDialog.registerListener(signUpDialogController);

		signUpDialog.setVisible(true);
	}

	/**
	 * Initializes and opens a new MainView
	 */
	private void showMainView() {
		MainView mainView = new MainView();
		MainViewTableController mainViewTableController = new MainViewTableController(mainView,
				credentialModel);
		MainViewActionListener actionListener = new MainViewActionListener(mainView, accountModel,
				credentialModel);

		credentialModel.addObserver(mainView);
		mainView.registerListeners(mainViewTableController, actionListener);

		mainView.getFrame().setVisible(true);
	}

	/**
	 * The ActionListener implementation used to listen on clicks from buttons with
	 * the specified action commands
	 */
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

	/**
	 * Implements the Observer interface, opens a new MainWindow on successful login
	 */
	@Override
	public void update(AccountModel observable) {
		if (observable.getLoggedInStatus() && initialView.getFrame().isVisible()) {
			UserAccount account = observable.getUserAccount();
			credentialModel.setUserAccount(account);

			loginDialog.setVisible(false);
			loginDialog.dispose();
			initialView.getFrame().setVisible(false);
			showMainView();
			credentialModel.refreshCredentials();
		} else if (!observable.getLoggedInStatus()) {
			initialView.getFrame().setVisible(true);
		}
	}

}
