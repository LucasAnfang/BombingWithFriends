package Main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import GUIs.SPanel;
import Player.KeyBoard;
import Player.Player;

public class GameBoardGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel GameMode, BombInventory, PowerUpInventory, Chat, InventoryMain;
	private JPanel Board_Inventory;
	private JLabel Title, GameType, tempItem1, tempItem2;
	public JTextPane ChatLog;
	private JTextField EnterMessage;
	public Core core;
	private JScrollPane jsp;
	private ArrayList<Player> playerList;
	private String username, servername;
	public GameClient gc;
	public int playerNo;
	private Font font;


	public GameBoardGUI(ArrayList<Player> playerList, String username, String servername, GameClient gc, int playerNo) 
	{
		super("Bombing with Friends");
		this.playerList = playerList;
		this.username = username;
		this.servername = servername;
		this.gc = gc;
		this.playerNo = playerNo;
		instantiateComponents();
		createGUI();
		addActions();
		revalidate();
		repaint();
	}
	
	public void instantiateComponents() {
		GameMode = new SPanel(new FlowLayout(FlowLayout.CENTER));
//		GameMode = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Chat = new JPanel(new BorderLayout());
		Title = new JLabel("       ");
		Title.setFont(new Font("Arial", Font.BOLD, 40));
		GameType = new JLabel("GameServer: " + servername + " | User: " + username);
		GameType.setFont(new Font("Minecraft", Font.PLAIN, 20));
		tempItem1 = new JLabel("Bomb");
		tempItem2 = new JLabel("Powerup");
		ChatLog = new JTextPane();
		ChatLog.setEditable(false);
		EnterMessage = new JTextField();
		EnterMessage.setPreferredSize(new Dimension( 1200, 35 ));
		EnterMessage.setText("Enter Message");
		EnterMessage.setFont(new Font("Minecraft", Font.PLAIN, 10));
	}
	
	public void createGUI() {
		setSize(1200,700);
		setLayout(new BorderLayout());
		setLocation(50,50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box bvtop = Box.createVerticalBox();
		bvtop.add(Title);
		bvtop.add(GameType);
		GameMode.add(bvtop);
		add(GameMode, BorderLayout.NORTH);
		
		ChatLog.setPreferredSize( new Dimension( 1200, 90 ) );
		ChatLog.setFont(new Font("Minecraft", Font.PLAIN, 10));
		jsp = new JScrollPane(ChatLog);	
		jsp.scrollRectToVisible(new Rectangle(0,ChatLog.getBounds(null).height,1,1));
		Chat.add(jsp, BorderLayout.CENTER);
		Chat.add(EnterMessage, BorderLayout.SOUTH);
	    add(Chat, BorderLayout.SOUTH);
		
		core = new Core(playerList, playerNo, this);
	    add(core, BorderLayout.CENTER);
	    core.start();
		setVisible(true);
	    core.requestFocus();


	}
	
	public void addActions() {
		
	    EnterMessage.addFocusListener(new FocusListener() {
	      public void focusGained(FocusEvent e) {
	        EnterMessage.setText("");
	      }
	
	      public void focusLost(FocusEvent e) {
	        EnterMessage.setText("Enter Message");
	      }
	
	    });
	    
	    EnterMessage.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	           try {
				appendOwnString('\n' + username + ": " + EnterMessage.getText());
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}      
	           Message message = new Message();
	           message.setMessage(EnterMessage.getText());
	           message.setUsername(username);
	           sendChatMessage(message);
	           EnterMessage.setText("");
	        }
	    });
	    
		jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
		}});
	    			
	}
	
	public void appendString(String str) throws BadLocationException {
	     StyledDocument document = (StyledDocument) ChatLog.getDocument();
	     Style style = ChatLog.addStyle("I'm a Style", null);
	     StyleConstants.setForeground(style, Color.BLACK);
	     document.insertString(document.getLength(), str, style);
	 }
	
	public void appendOwnString(String str) throws BadLocationException {
	     StyledDocument document = (StyledDocument) ChatLog.getDocument();
	     Style style = ChatLog.addStyle("I'm a Style", null);
	     StyleConstants.setForeground(style, Color.red);
	     document.insertString(document.getLength(), str, style);
	 }
	
	// later will be used to send message to all clients in current game server
	public void sendChatMessage(Message m) {
		gc.sendObject(m);
	}
	
	public Core getCore()
	{
		return core;
	}
	public void setCoreHandlerObjects(LinkedList<GameObject> ll)
	{
		core.setHandlerObjects(ll);
	}
	
	public JTextPane getChatLog() {
		return ChatLog;
	}
	

}
