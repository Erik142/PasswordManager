package passwordmanager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class MainView {
	
	private static JTable table = null;
	private final String[] columns = {"Website", "Email", "Password" };
	private Object[][] data;
	
	private PasswordClient client;
	
	private final JFrame frame;
	private MainViewController controller;
	
	
	
	public static void createFrame()
	
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                this.frame = new JFrame("Password Manager");
                this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                controller=new MainViewController(this, this.client);

                
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }

                
                 this.data = controller.getData(this.client.account);
                
                
                
                JPanel panelTop = new JPanel();
                panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
                panelTop.setOpaque(true);
                
               this.table = new JTable(this.data, this.columns){
                	   public boolean isCellEditable(int row, int column){
                	        return false;
                	   }
                	};
                JScrollPane scrollPane = new JScrollPane(this.table);
                this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                this.table.setFillsViewportHeight(true); 
                
                
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

                this.frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
                this.frame.getContentPane().add(BorderLayout.NORTH, inputpanel);
                this.frame.getContentPane().add(BorderLayout.SOUTH, accountpanel);
                this.frame.pack();
                this.frame.setLocationByPlatform(true);
                this.frame.setVisible(true);
                this.frame.setResizable(true);
                
            }
       });
    }
	
}
