package Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import Player.*;
import Tile.*;
public class Handler {

	private volatile LinkedList<GameObject> objects = new LinkedList<GameObject>();
	public int _pixels[]; //= new int[Core._coreWidth*Core._coreHeight];
	public int _width;
	public Level _level;
	public int _height;
	public KeyBoard _key;
	public ArrayList<Player> playerList;
	public int PlayerNo = 0;
	public Queue<String> deadPlayers;
	public GameBoardGUI gbgui;
	public volatile Boolean gotPlayerList = false;
	public volatile Boolean gameover = false;

	public Handler (int w, int h, GameBoardGUI gbgui)

	{
		_width = w;
		_height = h;
		_pixels = new int[_width*_height];
		deadPlayers = new LinkedList<String>();
		playerList = new ArrayList<Player>();
		this.gbgui = gbgui;
//		_level = level;

	}
	
	public void Render() 
	{
	
			for(int i = 0; i < objects.size(); ++i)
			{
				if( objects.get(i) instanceof Renderable)
				{
					if(objects.get(i).isRemove() == false)
					{
						((Renderable) objects.get(i)).render(this);
						if(objects.get(i) instanceof Player)
						{
							Player px = (Player)objects.get(i);
							Boolean found = false;
							for(Player P : playerList)
							{
								if(P.playerName.equals(px.playerName))
								{
									found = true;
								}
							}
							if(!gotPlayerList && !found)
							{
//								System.out.println("MAKING PLAYERLIST");
//								System.out.println("PLAYERNAME = " + px.playerName);
								playerList.add(px);
							}
						}
					} 
					else 
					{
						
						if(objects.get(i) instanceof Player)
						{
								Player px = (Player)objects.get(i);
//								System.out.println("PLAYERNO = " + PlayerNo + "PX NO "+ px.getPlayerNo());
								if(px.getPlayerNo() == PlayerNo)
								{
									//JOptionPane.showMessageDialog(null, "	Rest In Peace. U_U", "Game Over!", JOptionPane.WARNING_MESSAGE);
									new OptionPane(" Rest In Peace. U_U", "Game Over!", JOptionPane.WARNING_MESSAGE);
								}
								if(!deadPlayers.contains(px))
								{
//									System.out.println("ADDED DEAD PLAYER! " + px.playerName);
									deadPlayers.add(px.playerName);
								}
							}
							objects.remove(i);
						}
					}
			}
			if(playerList != null && gameover == false)
			{
//				System.out.println("PLAYERLIST NOT NULL");
//				System.out.println("DPSIZE: " + deadPlayers.size() + " PLISTSIZE: " + playerList.size());

				if(( (deadPlayers.size() == playerList.size() && !deadPlayers.isEmpty() && !playerList.isEmpty()) || (deadPlayers.size() == playerList.size()-1 && playerList.size()!=1 && !deadPlayers.isEmpty() && !playerList.isEmpty())))
				{
//					System.out.println("GAME OVER SEQUENCE");
					Boolean shown = false;
					for(String s : deadPlayers)
					{
//						System.out.println("DEAD PLAYER : " + s);
					}
					for(Player P : playerList)
					{
//						System.out.println("PLAYER : " + P.playerName);
						if(!deadPlayers.contains(P.playerName))
						{
							JOptionPane.showMessageDialog(null, "GAME OVER! " + P.playerName + " WON!");
							
							shown = true;
							gameover = true;
							gbgui.setVisible(false);
						}
					}
					if(!shown)
					{
						JOptionPane.showMessageDialog(null, "GAME OVER! NO WINNERS.");
						gbgui.setVisible(false);
						gameover = true;
					}
				}
			}
			
		
	}
	

	
	public void addObject(GameObject obj)
	{
		if(obj == null){System.out.println("fuck");}
		objects.add(obj);
	}
	
	public void removeObject(GameObject obj)
	{
		objects.remove(obj);
	}
	
	public void setObjects(LinkedList<GameObject> objects)
	{
		this.objects = objects;
	}
	
	public LinkedList<GameObject> getObjects()
	{
		return objects;
	}
	
	
	public ArrayList<GameObject> getAtTile(int x, int y)
	{	ArrayList<GameObject> atTile = new ArrayList<GameObject>();
		for(int i = 0; i < objects.size(); ++i){
			if(objects.get(i).getX() == x && objects.get(i).getY() == y)
			{
				atTile.add(objects.get(i));
			}
		}
		return atTile;
		
	}
	
//	public void createLevel()
//	{
//		
//		
//		objects.addAll(_level.loadLevel());
//	}
	
	
	
}
