package passwordmanager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class MainView {
	
	private JTable table = null;
	private final String[] columns = {"Website", "Email", "Password" };
	private Object[][] data;
	
	private PasswordClient client;
	
	private JFrame frame;
	private MainViewController controller;
	
	public MainView() throws IOException {
		 this.setFrame(new JFrame("Password Manager"));
		 this.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 
		 this.controller=new MainViewController(this, this.client);
		 //this.data=controller.getData(this.client.account);
		
		 createFrame();
	}
	
	public void createFrame()
	
    {
       
                

                
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }

                
                 //this.data = this.controller.getData(this.client.account);
                
                
                
                JPanel panelTop = new JPanel();
                panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
                panelTop.setOpaque(true);
                
               this.table = new JTable(this.data, this.columns){
                	   public boolean isCellEditable(int row, int column){
                	        return false;
                	   }
                	};
                JScrollPane scrollPane = new JScrollPane(this.getTable());
                this.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                this.getTable().setFillsViewportHeight(true); 
                
                
                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                
                JPanel accountpanel = new JPanel();
                accountpanel.setLayout(new FlowLayout());
                
                
                JButton addButton = new JButton("Add credential");
                JButton changeButton = new JButton("Change credential");
                JButton removeButton = new JButton("Remove credential");
                JButton changePasswordButton = new JButton("Change account password");
                JButton signOutButton = new JButton("Sign Out");
                JButton deleteAccountButton = new JButton("Delete my account");
                
                addButton.addActionListener(this.controller.addButton());
                        
                changeButton.addActionListener(this.controller.changeButton());
                                   
                removeButton.addActionListener(this.controller.removeButton());
                        
                deleteAccountButton.addActionListener(this.controller.deleteAccountButton());
                
                changePasswordButton.addActionListener(this.controller.changePasswordButton());
                
                signOutButton.addActionListener(this.controller.signOutButton());
                                                                                           
                inputpanel.add(addButton);
                inputpanel.add(changeButton);
                inputpanel.add(removeButton);
                
                accountpanel.add(changePasswordButton);
                accountpanel.add(signOutButton);
                accountpanel.add(deleteAccountButton);

                this.getFrame().getContentPane().add(scrollPane,BorderLayout.CENTER);
                this.getFrame().getContentPane().add(BorderLayout.NORTH, inputpanel);
                this.getFrame().getContentPane().add(BorderLayout.SOUTH, accountpanel);
                this.getFrame().pack();
                this.getFrame().setLocationByPlatform(true);
                this.getFrame().setVisible(true);
                this.getFrame().setResizable(true);
                
            }

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
       
    }
	

