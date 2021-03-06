package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.view.AddCredentialDialog;
import passwordmanager.view.ChangeCredentialDialog;
import passwordmanager.view.ChangeUserPasswordDialog;
import passwordmanager.view.MainView;

public class MainViewActionListener implements ActionListener {

    public final int ADD_CREDENTIAL = 0;
    public final int CHANGE_CREDENTIAL = 1;
    public final int DELETE_CREDENTIAL = 2;
    public final int CHANGE_ACCOUNT_PASSWORD = 3;
    public final int DELETE_ACCOUNT = 4;
    public final int SIGN_OUT = 5;

    private final MainModel mainModel;
    private final AddCredentialModel addCredentialModel;
    private final ManipulateCredentialModel manipulateCredentialModel;
    private final LoginDialogModel loginDialogModel;
    private final ChangeUserAccountModel changeUserAccountModel;

    private final MainView mainView;

    private final UpdateTableWindowListener updateTableWindowListener;

    public MainViewActionListener(MainView mainView, MainModel mainModel, AddCredentialModel addCredentialModel, ManipulateCredentialModel manipulateCredentialModel, LoginDialogModel loginModel, ChangeUserAccountModel changeUserAccountModel) {
        this.mainModel = mainModel;
        this.addCredentialModel = addCredentialModel;
        this.manipulateCredentialModel = manipulateCredentialModel;
        this.loginDialogModel = loginModel;
        this.changeUserAccountModel = changeUserAccountModel;

        this.mainView = mainView;

        this.updateTableWindowListener = new UpdateTableWindowListener(mainModel);
    }

    private void addCredential() {
        AddCredentialDialog addCredentialDialog = new AddCredentialDialog(mainView.getFrame());
        AddCredentialController addCredentialController = new AddCredentialController(addCredentialDialog, addCredentialModel, mainModel);

        addCredentialModel.addObserver(addCredentialDialog);
        addCredentialDialog.registerListener(addCredentialController, updateTableWindowListener);

        addCredentialDialog.setVisible(true);
    }

    private void changeAccountPassword() {
       ChangeUserPasswordDialog changeAccountPasswordDialog = new ChangeUserPasswordDialog(mainView.getFrame());
       ChangePasswordController changeAccountPasswordController = new ChangePasswordController(changeAccountPasswordDialog, changeUserAccountModel);
       
       changeUserAccountModel.addObserver(changeAccountPasswordDialog);
       changeAccountPasswordDialog.registerListener(changeAccountPasswordController);

       changeAccountPasswordDialog.setVisible(true);
    }

    private void changeCredential() {
        if (mainView.getTable().getSelectedRow() >= 0) {
            ChangeCredentialDialog changeCredentialDialog = new ChangeCredentialDialog(mainView.getFrame(), manipulateCredentialModel);
            ChangeCredentialController changeCredentialController = new ChangeCredentialController(changeCredentialDialog, manipulateCredentialModel);

            manipulateCredentialModel.addObserver(changeCredentialDialog);
            changeCredentialDialog.registerListener(changeCredentialController, updateTableWindowListener);

            changeCredentialDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(mainView.getFrame(),
                    "Error! No item in the list has been selected.",
                    "Change Credential",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAccount() {
        boolean deleteAccount = JOptionPane.showConfirmDialog(mainView.getFrame(), "Are you sure that you want to delete your account?", "Delete account", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        if (deleteAccount) {
            try {
                changeUserAccountModel.deleteAccount(); 
                JOptionPane.showMessageDialog(mainView.getFrame(), "Successfully deleted account!", "Delete account", JOptionPane.INFORMATION_MESSAGE);
                mainView.getFrame().dispose();
                mainModel.removeObserver(mainView);
                loginDialogModel.logout();
            }
            catch (ModelException e) {
                JOptionPane.showMessageDialog(mainView.getFrame(), e.getMessage(), "Delete account", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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
				    manipulateCredentialModel.deleteCredential();
				    mainModel.updateCredentials();
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

    private void signOut() {
        mainView.getFrame().dispose();
        loginDialogModel.logout();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Pressed button: " + e.getActionCommand());
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