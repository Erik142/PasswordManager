package passwordmanager.mainview;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultCaret;

import passwordmanager.Observer;
import passwordmanager.PasswordClient;
import passwordmanager.util.StringExtensions;


public class MainView implements Observer<MainModel> {
	
	private JTable table = null;
	private final String[] columns = {"Website", "Email", "Password" };
	

	private JFrame frame;

	private JButton addButton;
	private JButton changeButton;
	private JButton removeButton;
	private JButton changePasswordButton;
	private JButton signOutButton;
	private JButton deleteAccountButton;
	
	private DefaultTableModel tableModel;
	
	public MainView() throws IOException {
		this.setFrame(new JFrame("Password Manager"));
		this.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
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

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setOpaque(true);
        this.tableModel = new DefaultTableModel(this.columns, 0);
        
        this.table = new JTable(tableModel){
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
        
        
        addButton = new JButton("Add credential");
        changeButton = new JButton("Change credential");
        removeButton = new JButton("Remove credential");
        changePasswordButton = new JButton("Change account password");
        signOutButton = new JButton("Sign Out");
        deleteAccountButton = new JButton("Delete my account");
                                                                                   
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
        this.getFrame().setVisible(false);
        this.getFrame().setResizable(false);
    }

	public void registerListeners(MainViewTableController tableController, AddCredentialButtonController addCredentialController,
			ChangeCredentialButtonController changeCredentialController,
			DeleteCredentialButtonController deleteCredentialController,
			ChangeAccountButtonController changeAccountController,
			DeleteAccountButtonController deleteAccountController,
			SignOutButtonController signoutController) {
		addButton.addActionListener(addCredentialController);
		changeButton.addActionListener(changeCredentialController);
		removeButton.addActionListener(deleteCredentialController);
		changePasswordButton.addActionListener(changeAccountController);
		deleteAccountButton.addActionListener(deleteAccountController);
		signOutButton.addActionListener(signoutController);
		table.getSelectionModel().addListSelectionListener(tableController);
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
	
	@Override
	public void update(MainModel observable) {
		this.getFrame().setVisible(observable.getViewVisibility());
		
		String dialogMessage = observable.getDialogMessage();
		
		int dialogType = observable.getDialogErrorStatus() ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			JOptionPane.showMessageDialog(this.getFrame(),
		            dialogMessage,
		            observable.getDialogTitle(),
		            dialogType);
		}
		
		this.tableModel.setRowCount(0);
		Object[][] tableData = observable.getTableData();
		
		for (Object[] row : tableData) {
			System.out.println("Adding table row: " + row);
			this.tableModel.addRow(row);
		}
		
		this.tableModel.setRowCount(tableData.length);
	}
       
}