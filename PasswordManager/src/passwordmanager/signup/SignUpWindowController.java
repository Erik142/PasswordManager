package passwordmanager.signup;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SignUpWindowController implements ComponentListener {

	private SignUpModel model;
	
	public SignUpWindowController(SignUpModel model) {
		this.model = model;
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		model.resetFields();
	}

}
