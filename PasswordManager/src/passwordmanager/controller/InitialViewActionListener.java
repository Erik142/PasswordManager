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

/**
 * JButton ActionListener for the InitialView class
 * 
 * @author Erik Wahlberger
 */
public class InitialViewActionListener implements ActionListener, Observer<LoginDialogModel> {

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

	private final ForgotPasswordModel forgotPasswordModel;
	private final LoginDialogModel loginDialogModel;
	private final SignUpModel signUpModel;
	private final MainModel mainModel;
	private final ManipulateCredentialModel manipulateCredentialModel;
	private final AddCredentialModel addCredentialModel;
	private final ChangeUserAccountModel changeUserAccountModel;

	private final InitialView initialView;

	private LoginDialog loginDialog;

	/**
	 * Creates a new instance of the InitialViewActionListener with the specified
	 * InitialView object and related models
	 * 
	 * @param initialView               The InitialView object
	 * @param forgotPasswordModel       The ForgotPasswordModel
	 * @param loginDialogModel          The LoginDialogModel
	 * @param signUpModel               The SignupModel
	 * @param mainModel                 The MainModel
	 * @param manipulateCredentialModel The ManipulateCredentialModel
	 * @param addCredentialModel        The AddCredentialModel
	 * @param changeUserAccountModel    The ChangeUserAccountModel
	 */
	public InitialViewActionListener(InitialView initialView, ForgotPasswordModel forgotPasswordModel,
			LoginDialogModel loginDialogModel, SignUpModel signUpModel, MainModel mainModel,
			ManipulateCredentialModel manipulateCredentialModel, AddCredentialModel addCredentialModel,
			ChangeUserAccountModel changeUserAccountModel) {
		this.forgotPasswordModel = forgotPasswordModel;
		this.loginDialogModel = loginDialogModel;
		this.signUpModel = signUpModel;
		this.mainModel = mainModel;
		this.manipulateCredentialModel = manipulateCredentialModel;
		this.addCredentialModel = addCredentialModel;
		this.changeUserAccountModel = changeUserAccountModel;

		this.initialView = initialView;
	}

	/**
	 * Initializes and opens a new ForgotPasswordDialog
	 */
	private void forgotPassword() {
		ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog(initialView.getFrame(),
				forgotPasswordModel);
		ForgotPasswordDialogController forgotPasswordController = new ForgotPasswordDialogController(
				forgotPasswordDialog, forgotPasswordModel);

		forgotPasswordModel.addObserver(forgotPasswordDialog);
		forgotPasswordDialog.registerListener(forgotPasswordController);

		forgotPasswordDialog.setVisible(true);
	}

	/**
	 * Initializes and opens a new LoginDialog
	 */
	private void login() {
		loginDialog = new LoginDialog(initialView.getFrame());
		LoginDialogController loginDialogController = new LoginDialogController(loginDialog, loginDialogModel);

		loginDialogModel.addObserver(loginDialog);
		loginDialog.registerListener(loginDialogController);

		loginDialog.setVisible(true);
	}

	/**
	 * Initializes and opens a new SignUpDialog
	 */
	private void signUp() {
		SignUpDialog signUpDialog = new SignUpDialog(initialView.getFrame());
		SignUpController signUpDialogController = new SignUpController(signUpDialog, signUpModel);

		signUpModel.addObserver(signUpDialog);
		signUpDialog.registerListener(signUpDialogController);

		signUpDialog.setVisible(true);
	}

	/**
	 * Initializes and opens a new MainView
	 */
	private void showMainView() {
		MainView mainView = new MainView();
		MainViewTableController mainViewTableController = new MainViewTableController(mainView, mainModel,
				manipulateCredentialModel);
		MainViewActionListener actionListener = new MainViewActionListener(mainView, mainModel, addCredentialModel,
				manipulateCredentialModel, loginDialogModel, changeUserAccountModel);

		mainModel.addObserver(mainView);
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
	public void update(LoginDialogModel observable) {
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
