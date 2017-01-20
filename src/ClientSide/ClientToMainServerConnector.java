package ClientSide;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

import ClientToMainServerMessages.MainServerMessage;
import Descriptors.GameServerDescriptor;
import Descriptors.ScorePair;
import MainServerResponses.HighScoreResponse;
import MainServerResponses.MainServerResponse;
import MainServerResponses.ServerListRequestResponse;

public class ClientToMainServerConnector {
	private int userToken = -1;
	private ArrayList<GameServerDescriptor> currentServerList = new ArrayList<>();
	private Queue<ScorePair> currentHighScores = null;
	public String ip = "localhost";
	//public String ip = "10.0.1.2";
	public ClientToMainServerConnector(){
		
	}
	public int getUserToken(){
		return userToken;
	}
	public ArrayList<GameServerDescriptor> getCurrentServerList(){
		return currentServerList;
	}
	public Queue<ScorePair> getHighScores(){
		return currentHighScores;
	}
	public int transmitToServer(MainServerMessage message){
		Socket s = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		boolean success = false;
		int result = -1;
		try{
			s = new Socket("10.0.1.3", 7000);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			oos.writeObject(message);
			oos.flush();
			MainServerMessage mainServerMessage = (MainServerMessage) ois.readObject();
			System.out.println("Received Message " + mainServerMessage);
			if(mainServerMessage instanceof MainServerResponse){
				@SuppressWarnings("unused")
				String text = ((MainServerResponse)mainServerMessage).getText();
				success = ((MainServerResponse)mainServerMessage).getSuccess();
				//System.out.println("Text: "+ text + " Success: "+ success);	
				if(success) {
					result = 1;
					userToken = ((MainServerResponse)mainServerMessage).getUserToken();
				}
				else result = 2;
			}
			if(mainServerMessage instanceof ServerListRequestResponse){
				try{
					result = 1;
					currentServerList = ((ServerListRequestResponse) mainServerMessage).getServerList();
				}catch(Exception e){ }
			}
			if(mainServerMessage instanceof HighScoreResponse){
				try{
					result = 1;
					currentHighScores = ((HighScoreResponse)mainServerMessage).getHighScores();
					if(currentHighScores!=null){
						for(ScorePair curr: currentHighScores){
							System.out.println(curr.getUsername()+ " --> " +curr.getTotalScore());
						}
					}
				}catch(Exception e){ }
			}
		} catch(Exception e){
			e.printStackTrace();
			result = 0;
		}
		return result;
		//KEY
		//0 : no server online
		//1 : successful login/register
		//2 : Register up password in use || Login Incorrect data
	}
}