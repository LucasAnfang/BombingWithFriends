package Descriptors;

import java.io.Serializable;

public class ScorePair implements Serializable{
	private static final long serialVersionUID = 2148116778213169837L;
	private String mUsername;
	private int mTotalScore;
	public ScorePair(String mUsername, int mTotalScore){
		this.mUsername = mUsername;
		this.mTotalScore = mTotalScore;
	}
	public String getUsername(){
		return mUsername;
	}
	public int getTotalScore(){
		return mTotalScore;
	}
}
