package passwordmanager.changecredential;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import passwordmanager.mainview.MainModel;

public class ChangeCredentialComponentListener implements ComponentListener {

	private MainModel model;
	
	public ChangeCredentialComponentListener(MainModel model) {
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
		model.updateCredentials();
	}

}
