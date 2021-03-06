package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.SignUpModel;

public class SignUpDialogController implements ActionListener {

	private SignUpModel model;
	
	public SignUpDialogController(SignUpModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		model.resetFields();
		model.setIsViewVisible(true);
	}

}
