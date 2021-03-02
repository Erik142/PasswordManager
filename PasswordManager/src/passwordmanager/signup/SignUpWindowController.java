package passwordmanager.signup;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SignUpWindowController implements WindowListener {

	private SignUpModel model;
	
	public SignUpWindowController(SignUpModel model) {
		this.model = model;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		model.setIsViewVisible(false);
		model.resetFields();
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
