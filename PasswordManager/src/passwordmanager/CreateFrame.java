package passwordmanager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class CreateFrame {
	
	private static JTable table = null;
	
	public static void createFrame()
	
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Password Manager");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }

                String[] columns = {"Website", "Email", "Password" };
                Object[][] data = {
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Instagram", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123},
                        {"Facebook", "gg@gmail.com", 123},
                        {"Twitter", "gg@gmail.com", 123,},
                    };
                
                
                
                JPanel panelTop = new JPanel();
                panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
                panelTop.setOpaque(true);
                

                
                
                //JTable table = new JTable(data, columns);
               table = new JTable(data, columns){
                	   public boolean isCellEditable(int row, int column){
                	        return false;
                	   }
                	};
                JScrollPane scrollPane = new JScrollPane(table);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                table.setFillsViewportHeight(true); 
                
                //table.setEnabled(false);


                
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
                
                addButton.addActionListener(
                        new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                                AddDialog addDlg = new AddDialog(frame);
                                addDlg.setVisible(true);

                            }
                        }); 
                changeButton.addActionListener(
                        new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                            	if(table.getSelectedRow()==-1) {
                            		JOptionPane.showMessageDialog(null,
                                            "Please select a row",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                            		return;
                            		
                            	}
                                ChangeDialog changeDlg = new ChangeDialog(frame, CreateFrame.getCred());
                                changeDlg.setVisible(true);

                            }
                        }); 
                
                removeButton.addActionListener(
                        new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                            	if(table.getSelectedRow()==-1) {
                            		JOptionPane.showMessageDialog(null,
                                            "Please select a row",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                            		return;
                            		
                            	}
                            	int reply = JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to delete the password for " +table.getValueAt(table.getSelectedRow(),0).toString() + "?" ,
                                        "Remove",
                                        JOptionPane.YES_NO_OPTION);
                            	if (reply == JOptionPane.YES_OPTION) {
                            	    //TODO - Remove credential code
                            	} else {
                            		
                            	} 
                        		
                            }
                        });
                
                deleteAccountButton.addActionListener(
                        new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                            	
                            	int reply = JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to delete your account?" ,
                                        "Delete Account",
                                        JOptionPane.YES_NO_OPTION);
                            	if (reply == JOptionPane.YES_OPTION) {
                            	    //TODO - Delete account and sign out
                            	} else {
                            		
                            	} 
                            	
                            }
                        });
                
                signOutButton.addActionListener(
                        new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                            	
                            	int reply = JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to sign out" ,
                                        "Sign Out",
                                        JOptionPane.YES_NO_OPTION);
                            	if (reply == JOptionPane.YES_OPTION) {
                            	    //TODO - sign out
                            	} else {
                            		
                            	} 
                            	
                            }
                        });
                
                changePasswordButton.addActionListener(
                        new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                            	
                                ChangePasswordDialog changePasswordDlg = new ChangePasswordDialog(frame);
                                changePasswordDlg.setVisible(true);
                            	
                            	
                            }
                        });
                
                
                
                
                

                inputpanel.add(addButton);
                inputpanel.add(changeButton);
                inputpanel.add(removeButton);
                
                accountpanel.add(changePasswordButton);
                accountpanel.add(signOutButton);
                accountpanel.add(deleteAccountButton);

                frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
                frame.getContentPane().add(BorderLayout.NORTH, inputpanel);
                frame.getContentPane().add(BorderLayout.SOUTH, accountpanel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(true);
                
            }
       });
    }
	public static Credential getCred(){
	    int row = table.getSelectedRow();
	    if (row == -1){
	    	
	            //popup("select a row to remove");

	    }

	    String  website = table.getValueAt(row,0).toString();
	    String  email = table.getValueAt(row,1).toString();
	    String  password = table.getValueAt(row,2).toString();
	    Credential c=new Credential("user", website, email, password);
	    return c;
	}
}
