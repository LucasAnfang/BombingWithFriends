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

import ClientToMainServerMessages.SignUpMessage;
import GUIs.Button;
import GUIs.TPanel;


public class SignUpGUI extends JPanel{
	private static final long serialVersionUID = 5714954049111829204L;
	private JLabel mUsernameLabel,mPasswordLabel,mRepeatLabel;
	private JTextField mUsernameField;
	private JPasswordField mPasswordField,mRepeatField;
	private JButton mSignUpButton,mBackButton;
	private JPanel mTopGrid, mBottomGrid, mUsernameLabelPanel, mUsernameFieldPanel, mPasswordLabelPanel;
	private JPanel mPasswordFieldPanel, mRepeatLabelPanel, mRepeatFieldPanel, mSignUpPanel, mBackPanel, mOuterPanel;
	private MainClientGUI mMainClientWindow;
	private Font font;
	
	public SignUpGUI(MainClientGUI mMainClientWindow){
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
		mUsernameLabel.setFont(new Font("Minecraft", Font.BOLD, 20));
		mUsernameLabel.setForeground(Color.WHITE);
		mPasswordLabel = new JLabel(" Password:  ");
		mPasswordLabel.setFont(new Font("Minecraft", Font.BOLD, 20));
		mPasswordLabel.setForeground(Color.WHITE);
		mRepeatLabel = new JLabel(" Repeat:     ");
		mRepeatLabel.setFont(new Font("Minecraft", Font.BOLD, 20));
		mRepeatLabel.setForeground(Color.WHITE);
		mUsernameField = new JTextField();
		mUsernameField.setFont(new Font("Minecraft", Font.PLAIN , 14));
		mUsernameField.setPreferredSize( new Dimension( 300, 24 ) );
		mPasswordField = new JPasswordField();
		mPasswordField.setFont(new Font("Arial", Font.PLAIN , 14));
		mPasswordField.setPreferredSize( new Dimension( 300, 24 ) );
		mRepeatField = new JPasswordField();
		mRepeatField.setFont(new Font("Arial", Font.PLAIN , 14));
		mRepeatField.setPreferredSize( new Dimension( 300, 24 ) );
		mTopGrid = new TPanel(new GridLayout(3,1));
		mBottomGrid = new TPanel(new GridLayout(1,2));
		mSignUpButton = new Button("     Sign Up     ");
		mBackButton = new Button("        Back      ");
		mUsernameLabelPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mUsernameFieldPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mPasswordLabelPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mPasswordFieldPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mRepeatLabelPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mRepeatFieldPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mSignUpPanel = new TPanel(new FlowLayout(FlowLayout.RIGHT));
		mBackPanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mOuterPanel = new TPanel(new GridLayout(2,1));
		setLayouts();
		gridSetup();
	}
	private void gridSetup(){
		Box UsernameBox = Box.createHorizontalBox();
		Box PasswordBox = Box.createHorizontalBox();
		Box RepeatBox = Box.createHorizontalBox();

		mUsernameLabelPanel.add(mUsernameLabel);
		UsernameBox.add(mUsernameLabelPanel);
		mUsernameFieldPanel.add(mUsernameField);
		UsernameBox.add(mUsernameFieldPanel);
		mPasswordLabelPanel.add(mPasswordLabel);
		PasswordBox.add(mPasswordLabelPanel);
		mPasswordFieldPanel.add(mPasswordField);
		PasswordBox.add(mPasswordFieldPanel);
		mRepeatLabelPanel.add(mRepeatLabel);
		RepeatBox.add(mRepeatLabelPanel);
		mRepeatFieldPanel.add(mRepeatField);
		RepeatBox.add(mRepeatFieldPanel);
		
		mTopGrid.add(UsernameBox);
		mTopGrid.add(PasswordBox);
		mTopGrid.add(RepeatBox);
		
		
		mSignUpPanel.add(mSignUpButton);
		mBottomGrid.add(mSignUpPanel);
		mBackPanel.add(mBackButton);
		mBottomGrid.add(mBackPanel);
		mOuterPanel.add(mBottomGrid);
		Box v = Box.createVerticalBox();
		v.add(Box.createVerticalStrut(60));
		mOuterPanel.add(v);
	}
	private void setLayouts(){
		mTopGrid.setLayout(new GridLayout(3,1));
		mBottomGrid.setLayout(new GridLayout(1,2));
		setLayout(new BorderLayout());
	}
	private void createGUI(){
		add(mTopGrid,BorderLayout.CENTER);
		add(mOuterPanel,BorderLayout.SOUTH);
	}
	private void signUpRequest(){
		String username = mUsernameField.getText();
		@SuppressWarnings("deprecation")
		String password = mPasswordField.getText();
		byte[] encryptedPassword = mMainClientWindow.encrypt(password);
		SignUpMessage signupMessage = new SignUpMessage();
		signupMessage.setUsername(username);
		signupMessage.setEncryptedPassword(encryptedPassword);
		//KEY
		//0 : no server online
		//1 : successful login
		//2 : incorrect username or password
		int SignUpResult = mMainClientWindow.getConnector().transmitToServer(signupMessage);
		if(SignUpResult==1){
			//System.out.println("YEAH SUCCESS");
			mMainClientWindow.clearBottomQuadrant();
			mMainClientWindow.addToCenter(new GameFinderGUI(mMainClientWindow, username));
		}else if(SignUpResult==2){
			username = null;
			JOptionPane.showMessageDialog(null, "Username is Already in Use","Sign-Up Failed", JOptionPane.ERROR_MESSAGE);
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
		mSignUpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(checkFieldFormat()){
					signUpRequest();
				}
			}
		});
	}
	@SuppressWarnings("deprecation")
	private boolean checkFieldFormat(){
		String username = mUsernameField.getText();
		char[] passwordChar = mPasswordField.getPassword();
		String repeatPassword = mRepeatField.getText();
		String password = mPasswordField.getText();
		if(username.isEmpty()||password.isEmpty()||repeatPassword.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "One or more entry field is blank","Sign-Up Failed",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		//validate password and repeat
		if(!password.equals(repeatPassword)){
			JOptionPane.showMessageDialog(null, "Repeat must match the Password","Sign-Up Failed",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		boolean hasNumber = false;
		boolean hasUpperCase = false;
		for(char c: passwordChar){
			if(Character.isUpperCase(c)) hasUpperCase = true;
			if(Character.isDigit(c)) hasNumber = true;
		}
		if(!hasNumber||!hasUpperCase){	
			JOptionPane.showMessageDialog(null, "Password must contain at least: \n 1-number 1-uppercase letter","Sign-Up Failed",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
}