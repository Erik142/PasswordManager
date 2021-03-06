package passwordmanager.controller;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import passwordmanager.model.Credential;
import passwordmanager.model.MainModel;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.view.MainView;

/**
 * @author Erik Wahlberger
 * Updates the selected value in the MainModel whenever the selected row in the table within the MainView has changed
 */
public class MainViewTableController implements ListSelectionListener {
	private MainView view;
	private MainModel mainModel;
	private ManipulateCredentialModel model;
	
	/**
	 * Creates a new instance of the MainViewTableController, with the specified MainView, MainModel and ManipulateCredentialModel
	 * @param view The MainView
	 * @param mainModel The MainModel
	 * @param model The ManipulateCredentialModel
	 */
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
				model.setCredential(credential);
			} catch (Exception ex) {
				model.setCredential(null);
			}
		} else {
			model.setCredential(null);
		}
	}
}
