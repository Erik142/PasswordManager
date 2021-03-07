package passwordmanager;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.*;
/**
 * Used for creating the GUI for generating a random password
 * 
 * @author ???
 * @version 2021-03-07
 */

class passwordGenInput{    
    // Create different checkboxes
    public static JCheckBox checkbox1 = new JCheckBox("Lowercase");
    public static JCheckBox checkbox2 = new JCheckBox("Uppercase");
    public static JCheckBox checkbox3 = new JCheckBox("Numbers");      
    public static JCheckBox checkbox4 = new JCheckBox("Symbols");
    public static JTextField lengthText = new JTextField(10);
    private static boolean low, up, num, sym;
    public static String genPassword;
    public static int len;
    public static void main(String args[])
    {
        passwordGenInput pGI = new passwordGenInput();
    }
 
    /**
     * 
	 * Creates a new instance of the passwordGenInput class that shows the GUI
	 * 
	 */
    
    public passwordGenInput () {
        JButton b1 = new JButton("Ok");
        JButton b2 = new JButton("Use Password");
        // Create and set up a frame window
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Password Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        // Define the panel to hold the checkbox
        
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        
        JLabel msg = new JLabel("", JLabel.CENTER);
             
        // Titles for the panels
        panel1.setBorder(BorderFactory.createTitledBorder("Enter the length of your password and what to include: ")); 
        panel2.setBorder(BorderFactory.createTitledBorder("Password"));       
        
        // Adding the textfield, checkboxes and button into the panels     
        panel1.add(lengthText); 
        panel1.add(checkbox1);
        panel1.add(checkbox2);
        panel1.add(checkbox3);
        panel1.add(checkbox4);
        panel1.add(b1);
        panel2.add(msg);
        
        lengthText.addActionListener(new ActionListener()
        {
            public void actionPerformed (ActionEvent e)
            {
                if(e.getSource()==lengthText)
                {
                    len = Integer.parseInt(lengthText.getText());
                    genPassword = PasswordGenerator.generatePassword(len,low,up,num,sym);
                    msg.setText(genPassword);
                    panel2.add(b2);
                    
                }   
            }
        });
        
        b1.addActionListener(new ActionListener()
        {
            public void actionPerformed (ActionEvent e)
            {
                if(e.getSource()==b1)
                {
                    len = Integer.parseInt(lengthText.getText());
                    
                    genPassword = PasswordGenerator.generatePassword(len,low,up,num,sym);
                    msg.setText(genPassword);
                    panel2.add(b2);
                    
                }   
            }
        });
        b2.addActionListener(new ActionListener()
        {
            public void actionPerformed (ActionEvent e)
            {
                if(e.getSource()==b2)
                {
                    msg.setText("Password set to " + genPassword);
                    //getGeneratedPassword();
                    Container parent = b2.getParent(); //removing the button 
                    parent.remove(b2);
                    parent.revalidate();
                    parent.repaint();
                }
                    
            }
        });
        
        // Add action listener
        
        checkbox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    
                    low =true;
                } else {
                    
                    low = false;
                }
            }
        });
 
        checkbox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    up =true;
                } else {
                    up = false;
                }
            }
        });
         
        checkbox3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    num=true;
                } else {
                    num=false;
                }
            }
        });
         
        checkbox4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    
                    sym=true;
                } else {
                  
                    sym=false;
                }
            }
        });
         
        // Add the panel into the frame
        frame.setLayout(new GridLayout(2, 1));
        frame.add(panel1);
        frame.add(panel2);
         
        // Set the window to be visible as the default to be false
        frame.pack();
        frame.setVisible(true);
    }
    
    public String getGeneratedPassword()
    {
        return genPassword;
    }
}