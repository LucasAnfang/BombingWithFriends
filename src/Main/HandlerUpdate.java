package Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JOptionPane;

import Bomb.Bomb;
import Bomb.BombTile;
import Bomb.SpeedBomb;
import Bomb.SpikeBomb;
import Player.KeyBoard;
import Player.Player;
import SQLDriver.SQLDriver;

public class HandlerUpdate 
{

	public LinkedList<GameObject> objects = new LinkedList<GameObject>();
	public int _pixels[]; //= new int[Core._coreWidth*Core._coreHeight];
	public Level _level;
	public KeyBoard _key;
	public ArrayList<Player> playerList;
	public Boolean dodgeball = false;
	public Boolean capturetheflag = false;
	public int drop = 0;
	public int X,Y;
	public Queue<String> deadPlayers;
	public volatile Boolean gameover =false;
	public volatile String winner;


	public HandlerUpdate (ArrayList<Player> playerList, Level level)
	{
		
		_level = level;
		this.playerList = playerList;
		Random r = new Random();
		int n = r.nextInt(playerList.size());
		playerList.get(n).flagger = true;
		deadPlayers = new LinkedList<String>();
		 createLevel();

	}
	
//	public void Render() 
//	{
//		for(int i = 0; i < objects.size(); ++i)
//		{
//			if( objects.get(i) instanceof Renderable){
//				if(objects.get(i).isRemove() == false)
//				{
//					((Renderable) objects.get(i)).render(this);
//				} 
//				else 
//				{
//					objects.remove(i);
//				}
//			}
//		}
//		
//		for(Player  p : playerList)
//		{
//			if(p.isRemove() == false)
//			{
//				p.render(this);
//			}
//		}
//
//	}
	
	public void Update(double deltaTime)
	{
		for(int i = 0; i < objects.size(); ++i)
		{
			if(objects.get(i).isRemove() == true)
			{
				if(objects.get(i) instanceof Player)
				{
						Player px = (Player)objects.get(i);						
						if(!deadPlayers.contains(px))
						{
							deadPlayers.add(px.playerName);
						}
				}
					objects.remove(i);
			}
			else if( objects.get(i) instanceof Updateable)
			{
				 ((Updateable) objects.get(i)).update(this, deltaTime);
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
							//GET MSERVER CALL UPDATE 
							winner = P.playerName;
							updateUserTotalScore(winner);
							shown = true;
							gameover = true;
						}
					}
					if(!shown)
					{
						winner = "";
						gameover = true;
					}
				}
			}
			
		}
		
		for(Player p : playerList)
		{
			if(p.isRemove() == false)
			{
				p.update(this, deltaTime);
				X = p.getX();
				Y = p.getY();
			} 
		}
		
		drop++;
		if(dodgeball == true && drop >= 50)
		{
			for(int i=0; i<2; i++)
			{
				Random r = new Random();
				int x = r.nextInt(5)*16;
				x+= X;
				int y = r.nextInt(5)*16;
				y+= Y;
				BombTile newBomb = null;
				int _bombtype = r.nextInt(3);
				if(_bombtype == 0)
				{
				 newBomb = new Bomb(x, y, 2);
				} else if(_bombtype == 1)
				{
					 newBomb = new SpikeBomb(x, y, 2);
				} else if(_bombtype == 2)
				{
					 newBomb = new SpeedBomb(x, y, 2);
				}
				addObject(newBomb);
			}
			drop = 0;
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
	
	public void createLevel()
	{	
		objects.addAll(_level.loadLevel());
	}
	
	public synchronized void updateUserTotalScore(String username)
	{
		SQLDriver driver = new SQLDriver();
		driver.connect();
		if(driver.doesExist(username)){
			int totalScore = driver.getTotalScoreForUser(username);
			int newTotalScore = totalScore + 1;
			driver.updateUserTotalScore(newTotalScore, username);
		}
	}
	
	
}
