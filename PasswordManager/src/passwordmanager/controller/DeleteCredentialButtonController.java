package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.view.MainView;

public class DeleteCredentialButtonController implements ActionListener {
	private MainView view;
	private ManipulateCredentialModel model;
	private MainModel mainModel;
	
	public DeleteCredentialButtonController(MainView view, MainModel mainModel, ManipulateCredentialModel model) {
		this.view = view;
		this.model = model;
		this.mainModel = mainModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRow = view.getTable().getSelectedRow();
		
		if (selectedRow >= 0) {
			boolean delete = false;
			
			delete = JOptionPane.showConfirmDialog(view.getFrame(),
	                "Are you sure you want to delete the credential?" ,
	                "Delete credential",
	                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			
			if (delete) {
				model.deleteCredential();
				mainModel.updateCredentials();
			}
		}
		else {
			JOptionPane.showMessageDialog(view.getFrame(),
					"Error: No row has been selected!",
					"Delete credential",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
