package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.util.StringExtensions;
import passwordmanager.view.MainView;

public class ChangeCredentialButtonController implements ActionListener {
	private ManipulateCredentialModel model;
	private MainView view;
	
	public ChangeCredentialButtonController(MainView view, ManipulateCredentialModel model) {
		this.view = view;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (view.getTable().getSelectedRow() >= 0) {
			model.setChangeViewVisibilityStatus(true);
		}
		else {
			JOptionPane.showMessageDialog(view.getFrame(),
                    "Error! No item in the list has been selected.",
                    "Change Credential",
                    JOptionPane.ERROR_MESSAGE);
		}
	}
}
