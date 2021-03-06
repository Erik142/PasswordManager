package passwordmanager.view;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import passwordmanager.controller.MainViewActionListener;
import passwordmanager.controller.MainViewTableController;
import passwordmanager.model.CredentialModel;
import passwordmanager.model.Observer;
import passwordmanager.util.FrameUtil;

/**
 * This class creates the GUI for the MainView
 * 
 * @author Arian Alikashani
 * @author Ermin Fazlic
 * @version 2021-03-07
 *
 */
public class MainView implements Observer<CredentialModel> {
	
	private JTable table = null;
	private final String[] columns = {"Service", "Username", "Password" };
	

	private JFrame frame;
	private JButton addButton;
	private JButton changeButton;
	private JButton removeButton;
	private JButton changePasswordButton;
	private JButton signOutButton;
	private JButton deleteAccountButton;
	
	private DefaultTableModel tableModel;
	
	/**
     * The constructor creates a frame to create and display the MainView
     */
	public MainView() {
		this.setFrame(new JFrame("Password Manager"));
		this.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createFrame();
	}
	
	private void createFrame()
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
        
        FrameUtil.centerFrame(this.getFrame());
    }

	/**
     * Checks which button was pressed and tells controller to do the appropriate action
     * 
     * @param tableController The table controller
     * @param actionListener The action listener
     */
	public void registerListeners(MainViewTableController tableController, MainViewActionListener actionListener) {
		addButton.setActionCommand("" + actionListener.ADD_CREDENTIAL);
		addButton.addActionListener(actionListener);
		changeButton.setActionCommand("" + actionListener.CHANGE_CREDENTIAL);
		changeButton.addActionListener(actionListener);
		removeButton.setActionCommand("" + actionListener.DELETE_CREDENTIAL);
		removeButton.addActionListener(actionListener);
		changePasswordButton.setActionCommand("" + actionListener.CHANGE_ACCOUNT_PASSWORD);
		changePasswordButton.addActionListener(actionListener);
		deleteAccountButton.setActionCommand("" + actionListener.DELETE_ACCOUNT);
		deleteAccountButton.addActionListener(actionListener);
		signOutButton.setActionCommand("" + actionListener.SIGN_OUT);
		signOutButton.addActionListener(actionListener);
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
	public void update(CredentialModel observable) {
		this.tableModel.setRowCount(0);
		Object[][] tableData = observable.getTableData();
		
		for (Object[] row : tableData) {
			this.tableModel.addRow(row);
		}
		
		this.tableModel.setRowCount(tableData.length);
	}
       
}