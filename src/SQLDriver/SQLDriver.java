package SQLDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import com.mysql.jdbc.Driver;

import Descriptors.ScorePair;


public class SQLDriver {
	private Connection con;
	//private final static String selectUser = "SELECT * FROM LUCASANFANGTEXTEDITOR.USERS WHERE USERNAME=?";
	private final static String confirmLoginAttempt ="SELECT COUNT(*) FROM BOMBINGWITHFRIENDS.USERS WHERE USERNAME=? AND PASSWORD=?";
	private final static String findIfUserExists = "SELECT COUNT(*) FROM BOMBINGWITHFRIENDS.USERS WHERE USERNAME=?";
	private final static String getUserID = "SELECT ID FROM BOMBINGWITHFRIENDS.USERS WHERE USERNAME=?";
	private final static String addUser = "INSERT INTO BOMBINGWITHFRIENDS.USERS(USERNAME,PASSWORD,TOTALSCORE) VALUES(?,?,?)";
	private final static String getUsername = "SELECT USERNAME FROM BOMBINGWITHFRIENDS.USERS WHERE ID=?";
	private final static String getTotalScoreForUser = "SELECT TOTALSCORE FROM BOMBINGWITHFRIENDS.USERS WHERE USERNAME=?";
	private final static String updateTotalScoreForUser = "UPDATE BOMBINGWITHFRIENDS.USERS SET TOTALSCORE=? WHERE USERNAME=?";
	private final static String returnOrderedHighScoreList = "SELECT USERNAME, TOTALSCORE FROM BOMBINGWITHFRIENDS.USERS ORDER BY TOTALSCORE DESC";
	//private final static String updateProduct = "UPDATE FACTORYORDERS(NAME,CREATED) SET CREATED=? WHERE NAME=?";
	public SQLDriver(){
		try{
			new Driver();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	private String getSQLPassword(){
		
		String line = null;
		String password = null;
		try {
			File portFile = new File("resources/configs/SQLPassword.txt");
			FileReader portFileReader = new FileReader(portFile);
		    BufferedReader in = new BufferedReader(portFileReader);	    
		    while ((line = in.readLine()) != null)
		        password = line;
		    in.close();
		} catch (IOException e) {
		}
		return password;
	}
	public void connect(){
		try{
			String DBpass = "jdbc:mysql://localhost:3306/BombingWithFriends?user=root&password=";
			String password = getSQLPassword();
			DBpass+= password;
			con = DriverManager.getConnection(DBpass);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public int getTotalScoreForUser(String username){
		int totalScore = 0;
		try{
			PreparedStatement ps = con.prepareStatement(getTotalScoreForUser);
			ps.setString(1,username);
			ResultSet result = ps.executeQuery();			
			while(result.next()){
				totalScore = result.getInt(1);	
			}		
		}catch (SQLException e){e.printStackTrace();}
		return totalScore; //returns true if the user name is in use in the DB
	}
	public void updateUserTotalScore(int newTotalScore, String username){
		try{
			PreparedStatement ps = con.prepareStatement(updateTotalScoreForUser);
			ps.setInt(1,newTotalScore);
			ps.setString(2, username);
			ps.executeUpdate();			
		}catch (SQLException e){e.printStackTrace();}
	}
	public Queue <ScorePair> returnFullScoreList(){
		Queue<ScorePair> resultList = new LinkedList<>();
		try{
			PreparedStatement ps = con.prepareStatement(returnOrderedHighScoreList);
			ResultSet result = ps.executeQuery();	
			while(result.next()){
				String username = result.getString(1);	
				int totalScore = result.getInt(2);
				ScorePair currentPair = new ScorePair(username,totalScore);
				resultList.add(currentPair);
			}	
		}catch (SQLException e){e.printStackTrace();}
		return resultList;
	}
	public int getUserID(String Username){
		int ID = 0;
		try{
			PreparedStatement ps = con.prepareStatement(getUserID);
			ps.setString(1,Username);
			ResultSet result = ps.executeQuery();			
			while(result.next()){
				ID = result.getInt(1);	
			}		
		}catch (SQLException e){e.printStackTrace();}
		return ID; //returns true if the user name is in use in the DB
	}
	public String getUsername(int userID){
		String username = null;
		try{
			PreparedStatement ps = con.prepareStatement(getUsername);
			ps.setInt(1,userID);
			ResultSet result = ps.executeQuery();			
			while(result.next()){
				username = result.getString(1);	
			}		
		}catch (SQLException e){e.printStackTrace();}
		return username; //returns true if the user name is in use in the DB
	}
	public boolean doesExist(String Username){
		int count = 0;
		try{
			System.out.println("driver: Username" + Username );
			PreparedStatement ps = con.prepareStatement(findIfUserExists);
			ps.setString(1,Username);
			ResultSet result = ps.executeQuery();			
			while(result.next()){
				count = result.getInt(1);	
			}		
		}catch (SQLException e){e.printStackTrace();}
		return count == 1; //returns true if the user name is in use in the DB
	}

	public boolean confirmLoginAttempt(String Username, String Password){
		int count = 0;
		try{
			PreparedStatement ps = con.prepareStatement(confirmLoginAttempt);
			ps.setString(1,Username);
			ps.setString(2,Password);
			ResultSet result = ps.executeQuery();			
			while(result.next()){
				count = result.getInt(1);	
			}		
		}catch (SQLException e){e.printStackTrace();}
		return count == 1; //returns true if user is in the DB
	}
	public void registerUser(String Username, String Password, int startScore){
		try{
			PreparedStatement ps = con.prepareStatement(addUser);
			ps.setString(1, Username);
			ps.setString(2, Password);
			ps.setInt(3, 0);
			ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}