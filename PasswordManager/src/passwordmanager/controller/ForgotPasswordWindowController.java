package passwordmanager.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.view.ForgotPasswordDialog;

public class ForgotPasswordWindowController implements WindowListener {

	ForgotPasswordDialog view;
	ForgotPasswordModel model;
	
	public ForgotPasswordWindowController(ForgotPasswordDialog view, ForgotPasswordModel model) {
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		model.setEmail("");
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
