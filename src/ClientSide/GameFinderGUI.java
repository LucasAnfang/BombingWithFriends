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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import ClientToMainServerMessages.GenerateGameServerMessage;
import ClientToMainServerMessages.ServerListRequestMessage;
import Descriptors.GameServerDescriptor;
import GUIs.Button;
import GUIs.TPanel;
import Main.GameClient;

public class GameFinderGUI extends JPanel{
	private static final long serialVersionUID = -6005389666322029366L;
	private JList<String> list;	
	private Box mMainBox;
	private JPanel southPanel, mListPanel, mJoinPanel, mRefreshPanel, mCreatePanel, mCustomizerPanel, mGamePanel;
	private JButton mJoinButton, mRefreshButton, mCreateButton, mCustomizerButton;
	private JLabel mGameServers;
	private String[] JListValues;
	private String username, serverName;
	private ArrayList<GameServerDescriptor> mGameServerDescriptors = null;
	private MainClientGUI mMainClientGUI = null;
	private Vector<String> mServerListVector = null;
	private Vector<Integer> mServerPortVector =null;
	private String spriteSheetName = "./resources/spritesheets/BasicPlayerSheet.png";
	private int spriteNumber = 0;

	//public String ip = "10.0.1.2";
	private Font font;
	
	public GameFinderGUI(MainClientGUI mMainClientGUI, String username) {
		this.mMainClientGUI = mMainClientGUI;
		this.username = username;
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
		UpdateServerList();
		initialize();
		create();
		addActions();
		setOpaque(false);
	}
	
	public void changeSpriteSheet(String spriteSheetName){
		this.spriteSheetName = spriteSheetName;
	}
	
	public void changeCurrentSprite(int spriteNumber){
		this.spriteNumber = spriteNumber;
	}
	
	private void UpdateServerList(){
		ServerListRequestMessage message = new ServerListRequestMessage();
		
		mMainClientGUI.getConnector().transmitToServer(message);
		mGameServerDescriptors = mMainClientGUI.getConnector().getCurrentServerList();
		UpdateServerListHelper();
	}
	private void UpdateServerListHelper(){
		mServerListVector = new Vector<>();
		mServerPortVector = new Vector<>();
		if(mGameServerDescriptors.size()>0){
			for(GameServerDescriptor curr: mGameServerDescriptors){
				String currentListElement = null;
				int currentSize = curr.getCurrentSize();
				int maxCapacity = curr.getMaxCapacity();
				int portNumber = curr.getPortNumber();
				String serverName = curr.getServerName();
				String gameType = curr.getGameType();
				currentListElement = serverName + " GameType: "+gameType+": Port #: " + portNumber + " " + currentSize + "/" + maxCapacity;
				this.serverName = serverName;
				System.out.println(currentListElement);
				mServerListVector.add(currentListElement);
				mServerPortVector.add(portNumber);
			}
		}	
		if(list==null) list = new JList<String>(mServerListVector);
		
		ListModel<String> model = list.getModel();
		DefaultListModel<String> listModel = new DefaultListModel();	
		for(String currServer: mServerListVector){
			listModel.addElement(currServer);
		}
		
		list.setModel(listModel);
		list.setFont(new Font("Minecraft", Font.PLAIN, 14));
		revalidate();
		repaint();
	}
	
	private void initialize(){
		southPanel = new TPanel(new GridLayout(4,1));
		mJoinPanel = new TPanel(new FlowLayout());
		mJoinButton = new Button("     Join       ");
		mRefreshPanel = new TPanel(new FlowLayout());
		mRefreshButton = new Button("  Refresh  ");
		mCreatePanel = new TPanel(new FlowLayout());
		mCreateButton = new Button("   Create   ");
		mCustomizerPanel = new TPanel(new FlowLayout());
		mCustomizerButton = new Button("Customizer");
		mListPanel = new TPanel(new FlowLayout());
		mGamePanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mGameServers = new JLabel("Game Servers: ");
		mGameServers.setFont(new Font("Minecraft", Font.BOLD, 20));
		mGameServers.setForeground(Color.WHITE);
		mMainBox = Box.createVerticalBox();
	}
	
	private void create(){
		//southPanel.setLayout(new GridLayout(2,2));
		mCreatePanel.add(mCreateButton);
		southPanel.add(mCreatePanel);
		mJoinPanel.add(mJoinButton);
		southPanel.add(mJoinPanel);
		mCustomizerPanel.add(mCustomizerButton);
		southPanel.add(mCustomizerPanel);
		mRefreshPanel.add(mRefreshButton);
		southPanel.add(mRefreshPanel);
		list.setFixedCellWidth(375);
		list.setOpaque(false);
		mListPanel.add(new JScrollPane(list));
		mGamePanel.add(mGameServers);
		mMainBox.add(Box.createRigidArea(new Dimension(0,30)));
		mMainBox.add(mGamePanel);
		mMainBox.add(mListPanel);
		mMainBox.add(southPanel);
		add(mMainBox, BorderLayout.CENTER);
	}
	private void createHelper(){
		  JTextField serverNameField = new JTextField(15);
		  serverNameField.setFont(new Font("Minecraft", Font.PLAIN, 10));
	      //JTextField gameTypeField = new JTextField(15);
	      JTextField playerCapacityField = new JTextField(15);
	      playerCapacityField.setFont(new Font("Minecraft", Font.PLAIN, 10));
	      JPanel mainPanel = new JPanel();
	      JLabel gs = new JLabel("GameServer Name: ");
	      gs.setFont(new Font("Minecraft", Font.PLAIN, 12));
	      mainPanel.setLayout(new GridLayout(2,2));   
	      mainPanel.add(gs);
	      mainPanel.add(serverNameField);
	     // mainPanel.add(new JLabel("GameType:"));
	      //mainPanel.add(gameTypeField);
	      JLabel mp = new JLabel("Max Players (1-4): ");
	      mp.setFont(new Font("Minecraft", Font.PLAIN, 12));
	      mainPanel.add(mp);
	      mainPanel.add(playerCapacityField);
	      int result = JOptionPane.showConfirmDialog(null, mainPanel, 
	               "Create Game", JOptionPane.OK_CANCEL_OPTION);
	      if(result==JOptionPane.OK_OPTION){
	    	  serverName = serverNameField.getText();
	    	 // String gameType = gameTypeField.getText();
	    	  int playerCapacity = 4;
	    	  playerCapacity = Integer.parseInt(playerCapacityField.getText());
	    	  if(!serverName.isEmpty()&&playerCapacity>0 && playerCapacity<5){
	    			GenerateGameServerMessage message = new GenerateGameServerMessage();
	    			message.setGameServerName(serverName);
	    			message.setPlayerCapacity(playerCapacity);
	    			mMainClientGUI.getConnector().transmitToServer(message);
	    			mGameServerDescriptors = mMainClientGUI.getConnector().getCurrentServerList();
	    			UpdateServerListHelper();
	    			
	    	  }
	      }
	}
	
	private void addActions(){
		mJoinButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex()!=-1){
				int index = list.getSelectedIndex();
				System.out.println("server port " + mServerPortVector.elementAt(index)); 
				//new GameClient("localhost",mServerPortVector.elementAt(index));
				//new GameClient("localhost",mServerPortVector.elementAt(index));
				new Thread( new Runnable() {
				    @Override
				    public void run() {
				    	new GameClient("10.0.1.3", mServerPortVector.elementAt(index), username, serverName, spriteSheetName);
				    }
				}).start();
				//new GameClient("localhost", 6789);
				//System.out.print(mServerPortVector.elementAt(index));
				}

			}		
		});
		mRefreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateServerList();
				revalidate();
				repaint();
				
			}
		});
		mCreateButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				createHelper();
			}
		});
		mCustomizerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(mMainBox);
				add(new CustomizerGUI(mMainClientGUI,username, spriteNumber),BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
	}
}	
	
	
