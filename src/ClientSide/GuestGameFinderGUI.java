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

public class GuestGameFinderGUI extends JPanel{
    private static final long serialVersionUID = -6005389666322029366L;
    private JList<String> list;
    private Box mMainBox;
    private JLabel mGameServers;
    private JPanel southPanel, mListPanel, mRefreshPanel, mJoinPanel, mGamePanel;
    private JButton mJoinButton, mRefreshButton;
    private String[] JListValues;
    private ArrayList<GameServerDescriptor> mGameServerDescriptors = null;
    private MainClientGUI mMainClientGUI = null;
    private Vector<String> mServerListVector = null;
    private Vector<Integer> mServerPortVector =null;
    private String serverName;
    private String username;
    private String spriteSheetName = "./resources/spritesheets/BasicPlayerSheet.png";
    private Font font;
    
    
    public GuestGameFinderGUI(MainClientGUI mMainClientGUI) {
        this.mMainClientGUI = mMainClientGUI;
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
                this.serverName = serverName;
                String gameType = curr.getGameType();
                currentListElement = serverName + " GameType: "+gameType+": Port #: " + portNumber + " " + currentSize + "/" + maxCapacity;
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
        southPanel = new TPanel(new GridLayout(2,1));
        mGamePanel = new TPanel(new FlowLayout(FlowLayout.LEFT));
		mGameServers = new JLabel("Game Servers: ");
		mGameServers.setFont(new Font("Minecraft", Font.BOLD, 20));
		mGameServers.setForeground(Color.WHITE);
        mJoinButton = new Button("    Join    ");
        mJoinPanel = new TPanel(new FlowLayout());
        mRefreshButton = new Button(" Refresh ");
        mRefreshPanel = new TPanel(new FlowLayout());
        mListPanel = new TPanel(new FlowLayout());
        mMainBox = Box.createVerticalBox();
    }
    
    private void create(){
        mJoinPanel.add(mJoinButton);
        southPanel.add(mJoinPanel);
        mRefreshPanel.add(mRefreshButton);
        southPanel.add(mRefreshPanel);
        list.setFixedCellWidth(375);
        mListPanel.add(new JScrollPane(list));
		mGamePanel.add(mGameServers);
		mMainBox.add(Box.createRigidArea(new Dimension(0,100)));
		mMainBox.add(mGamePanel);
        mMainBox.add(mListPanel);
        mMainBox.add(southPanel);
        add(mMainBox, BorderLayout.CENTER);
    }
    private void createHelper(){
        JTextField serverNameField = new JTextField(15);
        //JTextField gameTypeField = new JTextField(15);
        JTextField playerCapacityField = new JTextField(15);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,2));
        mainPanel.add(new JLabel("GameServer Name"));
        mainPanel.add(serverNameField);
        // mainPanel.add(new JLabel("GameType:"));
        //mainPanel.add(gameTypeField);
        mainPanel.add(new JLabel("Max Players: "));
        mainPanel.add(playerCapacityField);
        int result = JOptionPane.showConfirmDialog(null, mainPanel,
                                                   "Create Game", JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION){
            serverName = serverNameField.getText();
            String serverName = serverNameField.getText();
            // String gameType = gameTypeField.getText();
            int playerCapacity = 4;
            playerCapacity = Integer.parseInt(playerCapacityField.getText());
            if(!serverName.isEmpty()&&playerCapacity>0){
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
                    System.out.println("server port" + mServerPortVector.elementAt(index));
                    //new GameClient("localhost",mServerPortVector.elementAt(index));
                    //new GameClient("localhost",mServerPortVector.elementAt(index));
                    new Thread( new Runnable() {
                        @Override
                        public void run() {
                            new GameClient("localhost", mServerPortVector.elementAt(index), "Guest User", serverName, spriteSheetName);
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
    }
}