package passwordmanager.controller;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import passwordmanager.model.Credential;
import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.view.MainView;

public class MainViewTableController implements ListSelectionListener {
	private MainView view;
	private MainModel mainModel;
	private ManipulateCredentialModel model;
	
	public MainViewTableController(MainView view, MainModel mainModel, ManipulateCredentialModel model) {
		this.view = view;
		this.mainModel = mainModel;
		this.model = model;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
		{
			return;
		}
		
		int selectedRow = view.getTable().getSelectedRow();
		
		if (selectedRow >= 0) {
			try {
				Credential credential = mainModel.getCredentials()[selectedRow];
				System.out.println("User pressed credential with url: " + credential.getURL());
				model.setCredential(credential);
			} catch (Exception ex) {
				model.setCredential(null);
			}
		} else {
			model.setCredential(null);
		}
	}
}
