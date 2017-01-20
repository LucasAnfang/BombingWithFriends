package ClientSide;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ClientToMainServerMessages.LoginMessage;
import GUIs.Button;
import GUIs.TPanel;


public class LoginGUI extends JPanel{
	private static final long serialVersionUID = 2336926634430689144L;
	private JLabel mUsernameLabel,mPasswordLabel;
	private JTextField mUsernameField;
	private JPasswordField mPasswordField;
	private JButton mLoginButton,mBackButton;
	private JPanel mTopGrid, mBottomGrid, mOuterGrid, mLoginPanel, mBackPanel, mUsernameLabelPanel, mMainPanel;
	private JPanel mUsernameFieldPanel, mPasswordLabelPanel, mPasswordFieldPanel;
	private MainClientGUI mMainClientWindow;
	private Font font;
	
	public LoginGUI(MainClientGUI mMainClientWindow){
		this.mMainClientWindow = mMainClientWindow;
		instantiateComponents();
		createGUI();
		addActions();
		setOpaque(false);
		
	}
	
	private void instantiateComponents(){
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/GUIs/Minecraft.ttf"));
			GraphicsEnvironment ge = 
		            GraphicsEnvironment.getLocalGraphicsEnvironment();
		        ge.registerFont(font);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mUsernameLabel = new JLabel(" Username: ");
		//mUsernameLabel.setHorizontalAlignment(WIDTH/2);
		mUsernameLabel.setFont(new Font("Minecraft", Font.BOLD, 20));
		mUsernameLabel.setForeground(Color.WHITE);
		mPasswordLabel = new JLabel(" Password:  ");
		//mPasswordLabel.setHorizontalAlignment(WIDTH/2);
		mPasswordLabel.setFont(new Font("Minecraft", Font.BOLD, 20));
		mPasswordLabel.setForeground(Color.WHITE);
		mUsernameField = new JTextField();
		mUsernameField.setFont(new Font("Minecraft", Font.PLAIN, 14));
		mUsernameField.setPreferredSize( new Dimension( 300, 24 ) );
		mPasswordField = new JPasswordField();
		mPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
		mPasswordField.setPreferredSize( new Dimension( 300, 24 ) );
		mTopGrid = new TPanel(new GridLayout(2,1));
		mBottomGrid = new TPanel(new GridLayout(1,2));
		mOuterGrid = new TPanel(new GridLayout(2,1));
		mLoginPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mBackPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mUsernameLabelPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mUsernameFieldPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mPasswordLabelPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mPasswordFieldPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mLoginButton = new Button("      Login      ");
		mBackButton = new Button("      Back      ");
		setLayouts();
		gridSetup();
	}
	private void gridSetup(){
		Box UsernameBox = Box.createHorizontalBox();
		Box PasswordBox = Box.createHorizontalBox();

		mUsernameLabelPanel.add(mUsernameLabel);
		UsernameBox.add(mUsernameLabelPanel);
		mUsernameFieldPanel.add(mUsernameField);
		UsernameBox.add(mUsernameFieldPanel);
		mPasswordLabelPanel.add(mPasswordLabel);
		PasswordBox.add(mPasswordLabelPanel);
		mPasswordFieldPanel.add(mPasswordField);
		PasswordBox.add(mPasswordFieldPanel);
		
		mTopGrid.add(UsernameBox);
		mTopGrid.add(PasswordBox);
		
		mMainPanel = new TPanel(new GridLayout(1,2));
		mLoginPanel.add(mLoginButton);
		mMainPanel.add(mLoginPanel);
		mBackPanel.add(mBackButton);
		mMainPanel.add(mBackPanel);
		mBottomGrid.add(mMainPanel);
		mOuterGrid.add(mBottomGrid);
		Box v = Box.createVerticalBox();
		v.add(Box.createVerticalStrut(60));
		mOuterGrid.add(v);
	}
	private void setLayouts(){
		mTopGrid.setLayout(new GridLayout(2,1));
		mBottomGrid.setLayout(new GridLayout(1,2));
		setLayout(new BorderLayout());
	}
	private void createGUI(){
		add(mTopGrid,BorderLayout.CENTER);
		add(mOuterGrid,BorderLayout.SOUTH);
	}
	private void loginRequest(){
		String username = mUsernameField.getText();
		@SuppressWarnings("deprecation")
		String password = mPasswordField.getText();
		byte[] encryptedPassword = mMainClientWindow.encrypt(password);
		LoginMessage loginMessage = new LoginMessage();
		loginMessage.setUsername(username);
		loginMessage.setEncryptedPassword(encryptedPassword);
		//KEY
		//0 : no server online
		//1 : successful login
		//2 : incorrect username or password
		int LoginResult = mMainClientWindow.getConnector().transmitToServer(loginMessage);
		if(LoginResult==1){
			//System.out.println("YEAH SUCCESS");
			mMainClientWindow.clearBottomQuadrant();
			mMainClientWindow.addToCenter(new GameFinderGUI(mMainClientWindow, username));
		}else if(LoginResult==2){
			username = null;
			//userToken = 
			JOptionPane.showMessageDialog(null, "Username or Password is invalid","Login Failed", JOptionPane.ERROR_MESSAGE);
		}else{
			username = null;
			JOptionPane.showMessageDialog(null, "Server Can't be reached \n Offline mode initiated","Login Failed", JOptionPane.WARNING_MESSAGE);
		}
	}
	private void addActions(){
		mBackButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mMainClientWindow.backButton();
			}
		});
		mLoginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(checkFieldFormat()){
					loginRequest();
				}
			}
		});
	}
	private boolean checkFieldFormat(){
		String username = mUsernameField.getText();
		char[] password = mPasswordField.getPassword();
		
		if(username.isEmpty()||password.length==0)
		{
			JOptionPane.showMessageDialog(null, "One or more entry field is blank","Login Failed",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
}
