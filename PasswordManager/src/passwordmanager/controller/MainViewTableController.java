package passwordmanager.controller;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import passwordmanager.model.Credential;
import passwordmanager.model.CredentialModel;
import passwordmanager.view.MainView;

/**
 * Updates the selected value in the MainModel whenever the selected row in the
 * table within the MainView has changed
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class MainViewTableController implements ListSelectionListener {
	private MainView view;
	private CredentialModel model;

	/**
	 * Creates a new instance of the MainViewTableController, with the specified
	 * MainView, MainModel and ManipulateCredentialModel
	 * 
	 * @param view      The view used together with this controller
	 * @param model     The model used together with this controller
	 */
	public MainViewTableController(MainView view, CredentialModel model) {
		this.view = view;
		this.model = model;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}

		int selectedRow = view.getTable().getSelectedRow();

		if (selectedRow >= 0) {
			try {
				Credential credential = model.getCredentials()[selectedRow];
				model.setSelectedCredential(credential);
			} catch (Exception ex) {
				model.setSelectedCredential(null);
			}
		} else {
			model.setSelectedCredential(null);
		}
	}
}
