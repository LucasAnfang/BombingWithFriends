package ClientSide;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Queue;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ClientToMainServerMessages.HighScoreRequest;
import Descriptors.ScorePair;
import GUIs.Button;
import GUIs.Panel;
import GUIs.TPanel;


public class MainClientGUI extends JFrame{
	public static final long serialVersionUID = 832423423;
	private static final String mKey = "GoTrojansBeatCal";
	private Key mAESKey;
	private MainClientGUI mMainClientGUI;
	private Cipher mCipher = null;
	public JPanel mLogo;
	private JPanel mCenter, mSubCenter, mGuestSignUpGrid, mGuestPanel, mSignUpPanel, mLoginPanel, mLeaderPanel;
	private JButton mLoginButton, mSignUpButton, mGuestButton, mLeaderBoardButton;
	private LoginGUI mLoginGUI = null;
	private SignUpGUI mSignUpGUI = null;
	private LeaderBoardGUI mLeaderBoardGUI = null;
	private ClientToMainServerConnector mClientToMainServerConnector;
	
	public MainClientGUI(){
		super("BomberMan Client");
		try{ UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
		catch(Exception e){ System.out.println("Warning! Cross-platform L&F not used!"); }
		mClientToMainServerConnector = new ClientToMainServerConnector();
		mMainClientGUI = this;
		GenerateAESKey();
		instantiateComponents();
		createGUI();
		addActions();
		setVisible(true);
	}
	private void instantiateComponents(){
		mCenter = new TPanel(new BorderLayout());
		mLogo = new Panel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        try {
					g.drawImage(ImageIO.read(new File("./resources/GUIs/Bombing-With.png")), 30, 40, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		};
		mSubCenter = new TPanel(new GridLayout(5,1));
		mGuestPanel = new TPanel(new FlowLayout());
		mSignUpPanel = new TPanel(new FlowLayout());
		mLeaderPanel = new TPanel(new FlowLayout());
		//mGuestSignUpGrid = new JPanel();
		mLoginPanel = new TPanel(new FlowLayout());
		mLoginButton = new Button("      Login      ");
		mSignUpButton = new Button("    Sign Up    ");
		mGuestButton = new Button("     Guest     ");
		mLeaderBoardButton = new Button("Leaderboard");
		//setLayouts();
	}
	
	private void setLayouts(){
		mCenter.setLayout(new BorderLayout());
		mSubCenter.setLayout(new GridLayout(5,1));
		mGuestPanel.setLayout(new FlowLayout());
		mSignUpPanel.setLayout(new FlowLayout());
		//mGuestSignUpGrid.setLayout(new GridLayout(1,2));
		mLoginPanel.setLayout(new FlowLayout());	
	}
	
	public void backButton(){
		if(mLoginGUI!=null) remove(mLoginGUI);
		if(mSignUpGUI!=null) remove(mSignUpGUI);
		if(mLeaderBoardGUI!=null) remove(mLeaderBoardGUI);
		add(mSubCenter,BorderLayout.SOUTH);
		revalidate();
		repaint();
	}
	private void createGUI(){
		setLayout(new BorderLayout());
		setSize(700,500);
		setLocation(300,100);
		mGuestPanel.add(mGuestButton);
		mSignUpPanel.add(mSignUpButton);
		mSubCenter.add(mGuestPanel);
		mSubCenter.add(mSignUpPanel);
		//mSubCenter.add(mGuestSignUpGrid);
		mLoginPanel.add(mLoginButton);
		mSubCenter.add(mLoginPanel);
		mLeaderPanel.add(mLeaderBoardButton);
		mSubCenter.add(mLeaderPanel);
		setContentPane(mLogo);
		Box vertical = Box.createVerticalBox();
		vertical.add(Box.createVerticalStrut(45));
		mSubCenter.add(vertical);
		add(mSubCenter,BorderLayout.SOUTH);
		//add(mLogo);
		//add(mCenter);
	}
	public void addToCenter(JPanel centerPanel){
		if(mCenter!=null) remove(mCenter);
		if(mLogo!=null) remove(mLogo);
		if(mLeaderBoardGUI!=null) remove(mLeaderBoardGUI);
		mCenter.removeAll();
		mCenter.add(((JPanel)centerPanel),BorderLayout.CENTER);
		add(mCenter);
		revalidate();
		repaint();
	}
	public ClientToMainServerConnector getConnector(){
		return mClientToMainServerConnector;
	}
	public void clearBottomQuadrant(){
		if(mLoginGUI!=null) remove(mLoginGUI);
		if(mSignUpGUI!=null) remove(mSignUpGUI);
		if(mLeaderBoardGUI!=null) remove(mLeaderBoardGUI);
		setContentPane(new Panel(new FlowLayout()));
		revalidate();
		repaint();
	}
	private void addActions(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mLoginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				remove(mSubCenter);
				mLoginGUI = new LoginGUI(mMainClientGUI);
				add(mLoginGUI,BorderLayout.SOUTH);
				revalidate();
				repaint();
			}
		});
		mSignUpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				remove(mSubCenter);
				mSignUpGUI = new SignUpGUI(mMainClientGUI);
				add(mSignUpGUI,BorderLayout.SOUTH);
				revalidate();
				repaint();
			}
		});
		mGuestButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				remove(mSubCenter);
				clearBottomQuadrant();
				addToCenter(new GuestGameFinderGUI(mMainClientGUI));
				revalidate();
				repaint();
			}
		});
		mLeaderBoardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("zero");
				remove(mSubCenter);
				System.out.println("one");
				mClientToMainServerConnector.transmitToServer(new HighScoreRequest());
				System.out.println("two");
				Queue<ScorePair>highScores = null;
				System.out.println("three");
				highScores = mClientToMainServerConnector.getHighScores();
				System.out.println("four");
				mLeaderBoardGUI = new LeaderBoardGUI(mMainClientGUI,highScores);
				add(mLeaderBoardGUI, BorderLayout.SOUTH);
				revalidate();
				repaint();
			}
		});
	}
	private void GenerateAESKey(){
		Key tempKey = null;
		try{
		tempKey = new SecretKeySpec(mKey.getBytes(),"AES");
		} catch(Exception nsae){
			System.out.println("Exception in GenerateAESKey method: " + nsae);
		}
		mAESKey = tempKey;
	}
	public byte[] encrypt(String password){
		byte[] encryptedString = null;
		try{
			if(mCipher == null){
				mCipher = Cipher.getInstance("AES");
			}
			mCipher.init(Cipher.ENCRYPT_MODE, mAESKey);
			encryptedString = mCipher.doFinal(password.getBytes());	
		}catch(Exception e){
			System.out.println(e);
		}
		return encryptedString;
	}

}
