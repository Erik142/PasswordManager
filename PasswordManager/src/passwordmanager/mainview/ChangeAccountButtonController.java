package passwordmanager.mainview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.changeuserpassword.ChangeUserAccountModel;

public class ChangeAccountButtonController implements ActionListener {
	private ChangeUserAccountModel model;
	
	public ChangeAccountButtonController(ChangeUserAccountModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ChangeAccountButtonController triggered!");
		this.model.reset();
		this.model.setIsViewVisible(true);
	}
}
