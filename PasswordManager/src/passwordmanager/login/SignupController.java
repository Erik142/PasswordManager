package passwordmanager.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.signup.SignUpModel;

public class SignupController implements ActionListener {

	private SignUpModel model;
	
	public SignupController(SignUpModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		model.setIsViewVisible(true);
	}

}
