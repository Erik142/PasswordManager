package passwordmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LoginDialogController {
	private LoginDialog parentView;
	
	public LoginDialogController(LoginDialog parentView) 
	{
		this.parentView = parentView;
	}
	public ActionListener afterLoginTry() {
		return new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (Login.authenticate(parentView.getUsername(), parentView.getPassword())) {
                JOptionPane.showMessageDialog(parentView,
                        "Hi " + parentView.getUsername() + "! You have successfully logged in.",
                        "Login",
                        JOptionPane.INFORMATION_MESSAGE);
                parentView.succeeded = true;
                parentView.dispose();
            } else {
                JOptionPane.showMessageDialog(parentView,
                        "Invalid username or password",
                        "Login",
                        JOptionPane.ERROR_MESSAGE);
                // reset username and password
                parentView.tfUsername.setText("");
                parentView.pfPassword.setText("");
                parentView.succeeded = false;

            }
        }
    };
	}
	public ActionListener cancelB() {
		return new ActionListener() {
	
        public void actionPerformed(ActionEvent e) {
            parentView.dispose();
        }
    };
    }
	
}
