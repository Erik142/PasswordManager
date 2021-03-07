package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.AccountModel;
import passwordmanager.model.CredentialModel;
import passwordmanager.view.AddCredentialDialog;
import passwordmanager.view.ChangeCredentialDialog;
import passwordmanager.view.ChangeUserPasswordDialog;
import passwordmanager.view.MainView;

/**
 * This class implements the actionlisteners for the MainView
 * 
 * @author Ermin Fazlic
 * @author Erik Wahlberger
 * @version 2021-03-07
 *
 */
public class MainViewActionListener implements ActionListener {

	/**
	 * Action command used to add a credential
	 */
    public final int ADD_CREDENTIAL = 0;
    /**
     * Action command used to change a credential
     */
    public final int CHANGE_CREDENTIAL = 1;
    /**
     * Action command used to delete a credential
     */
    public final int DELETE_CREDENTIAL = 2;
    /**
     * Action command used to change the account password
     */
    public final int CHANGE_ACCOUNT_PASSWORD = 3;
    /**
     * Action command used to delete the account
     */
    public final int DELETE_ACCOUNT = 4;
    /**
     * Action command used to sign out from the application
     */
    public final int SIGN_OUT = 5;

    private final AccountModel accountModel;
    private final CredentialModel credentialModel;

    private final MainView mainView;

    private final UpdateTableWindowListener updateTableWindowListener;
    
    /**
     * Creates an instance of this class with the the mainView and the different models
     * 
     * @param mainView The view used together with this controller
     * @param accountModel The account model
     * @param credentialModel The credential model
     */
    public MainViewActionListener(MainView mainView, AccountModel accountModel, CredentialModel credentialModel) {
        this.accountModel = accountModel;
        this.credentialModel = credentialModel;

        this.mainView = mainView;

        this.updateTableWindowListener = new UpdateTableWindowListener(credentialModel);
    }

    /**
     * Creates the dialog to add a new credential
     */
    private void addCredential() {
        AddCredentialDialog addCredentialDialog = new AddCredentialDialog(mainView.getFrame());
        AddCredentialController addCredentialController = new AddCredentialController(addCredentialDialog, credentialModel);

        addCredentialDialog.registerListener(addCredentialController, updateTableWindowListener);

        addCredentialDialog.setVisible(true);
    }

    /**
     * Creates the dialog to change user password
     */
    private void changeAccountPassword() {
       ChangeUserPasswordDialog changeAccountPasswordDialog = new ChangeUserPasswordDialog(mainView.getFrame());
       ChangePasswordController changeAccountPasswordController = new ChangePasswordController(changeAccountPasswordDialog, accountModel);
       
       changeAccountPasswordDialog.registerListener(changeAccountPasswordController);

       changeAccountPasswordDialog.setVisible(true);
    }

    /**
     * Creates the dialog to change a credential
     */
    private void changeCredential() {
        if (mainView.getTable().getSelectedRow() >= 0) {
            ChangeCredentialDialog changeCredentialDialog = new ChangeCredentialDialog(mainView.getFrame(), credentialModel);
            ChangeCredentialController changeCredentialController = new ChangeCredentialController(changeCredentialDialog, credentialModel);

            changeCredentialDialog.registerListener(changeCredentialController, updateTableWindowListener);

            changeCredentialDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(mainView.getFrame(),
                    "Error! No item in the list has been selected.",
                    "Change Credential",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Creates pop-up to confirm deletion of account
     */
    private void deleteAccount() {
        boolean deleteAccount = JOptionPane.showConfirmDialog(mainView.getFrame(), "Are you sure that you want to delete your account?", "Delete account", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        if (deleteAccount) {
            try {
                accountModel.deleteAccount(); 
                JOptionPane.showMessageDialog(mainView.getFrame(), "Successfully deleted account!", "Delete account", JOptionPane.INFORMATION_MESSAGE);
                mainView.getFrame().dispose();
                credentialModel.removeObserver(mainView);
                accountModel.logout();
            }
            catch (ModelException e) {
                JOptionPane.showMessageDialog(mainView.getFrame(), e.getMessage(), "Delete account", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     * Creates pop-up to confirm deletion of credential
     */
    private void deleteCredential() {
        int selectedRow = mainView.getTable().getSelectedRow();
		
		if (selectedRow >= 0) {
			boolean delete = false;
			
			delete = JOptionPane.showConfirmDialog(mainView.getFrame(),
	                "Are you sure you want to delete the credential?" ,
	                "Delete credential",
	                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			
			if (delete) {
                try {
				    credentialModel.deleteCredential();
				    credentialModel.refreshCredentials();
                } catch (ModelException e) {
                    JOptionPane.showMessageDialog(mainView.getFrame(), e.getMessage(), "Delete credential", JOptionPane.ERROR_MESSAGE);
                }
			}
		}
		else {
			JOptionPane.showMessageDialog(mainView.getFrame(),
					"Error: No row has been selected!",
					"Delete credential",
					JOptionPane.ERROR_MESSAGE);
		}
    }
    
    /**
     * Disposes the GUI and logs the user out
     */
    private void signOut() {
        boolean signout = JOptionPane.showConfirmDialog(mainView.getFrame(), "Are you sure that you want to sign out?", "Sign out", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        if (signout) {
            mainView.getFrame().dispose();
            accountModel.logout();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (Integer.parseInt(e.getActionCommand())) {
        case ADD_CREDENTIAL:
            addCredential();
            break;
        case CHANGE_CREDENTIAL:
            changeCredential();
            break;
        case DELETE_CREDENTIAL:
            deleteCredential();
            break;
        case CHANGE_ACCOUNT_PASSWORD:
            changeAccountPassword();
            break;
        case DELETE_ACCOUNT:
            deleteAccount();
            break;
        case SIGN_OUT:
            signOut();
            break;
        default:
        }
    }

}