package ClientSide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import Descriptors.ScorePair;
import GUIs.Button;
import GUIs.TPanel;

public class LeaderBoardGUI extends JPanel {
	
	private JLabel mLeaderBoard, mFinalScores;
	private JButton mBackButton;
	private JList<String> mList = null;
	private JPanel mBackButtonPanel, mListPanel, mLeaderPanel, mFinalPanel;
	private MainClientGUI mMainClientWindow;
	private Queue<ScorePair> highscores;
	private static final long serialVersionUID = 1L;
	private Vector<String> mHighScores = null;
	private StringBuilder scores;
	private Box box;
	private Font font;
	public LeaderBoardGUI(MainClientGUI mcg, Queue<ScorePair> highscores) {
		mMainClientWindow = mcg;
		this.highscores = highscores;
		box = Box.createVerticalBox();
		setLayout(new BorderLayout());
        updateList();
        initialize();
        createGUI();
        addActions();
        setOpaque(false);
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
	}

	public void updateList() {
		mHighScores = new Vector<>();
		int x = 1;

		if(highscores.size()>0){

			for(ScorePair curr: highscores){
				if(x > 10) {

					break;
				}
				String currentListElement = null;
				String username = curr.getUsername();
				int score = curr.getTotalScore();
				currentListElement = "0" + x + ". " + username + ": " + score;
				if(x == 10) {
					currentListElement = x + ". " + username + ": " + score;
				}
				x++;
				System.out.println(currentListElement);
				//jta.setText(jta.getText() + currentListElement + "\n");
				JLabel label = new JLabel(currentListElement);
				label.setFont(new Font("Minecraft", Font.PLAIN, 14));
				label.setForeground(Color.WHITE);
				box.add(label);
				//jta.add(label, Box.CENTER_ALIGNMENT);
				mHighScores.add(currentListElement);
			}
		}	
		if(mList==null) mList = new JList<String>(mHighScores);
		
		ListModel<String> model = mList.getModel();
		DefaultListModel<String> listModel = new DefaultListModel();	
		for(String curr: mHighScores){
			listModel.addElement(curr);
		}
		
		mList.setModel(listModel);
		revalidate();
		repaint();
	}
	
	public void initialize() {
		//southPanel = new TPanel(new BorderLayout());
		mLeaderBoard = new JLabel("LEADERBOARD");
		mLeaderBoard.setFont(new Font("Minecraft", Font.BOLD, 20));
		mLeaderBoard.setForeground(Color.WHITE);
		mLeaderPanel = new TPanel(new FlowLayout(FlowLayout.CENTER));
		mFinalPanel = new TPanel(new FlowLayout());
		mBackButton = new Button("      Back      ");
		mBackButtonPanel = new TPanel(new FlowLayout());
		mListPanel = new TPanel(new FlowLayout());
		//add(mListPanel);
		//add(mBackButton, BorderLayout.SOUTH);	
	}
	public void check(){
		for(String line: mHighScores){
			System.out.println("~~~~~~~~~~~~~~~");
			System.out.println(line);
		}
	}
	public void createGUI() {
		mList.setFixedCellWidth(375);
		mLeaderPanel.add(mLeaderBoard);
		add(mLeaderPanel, BorderLayout.NORTH);
		//mListPanel.add(new JScrollPane(mList));
		mFinalPanel.add(box);
		add(mFinalPanel, BorderLayout.CENTER);
		//add(mListPanel, BorderLayout.CENTER);
		mBackButtonPanel.add(mBackButton);
		add(mBackButtonPanel, BorderLayout.SOUTH);
	}
	
	public void addActions() {
		mBackButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mMainClientWindow.backButton();
			}
		});
		
	}

}
