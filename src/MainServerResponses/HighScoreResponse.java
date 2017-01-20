package MainServerResponses;

import java.util.Queue;

import ClientToMainServerMessages.MainServerMessage;
import Descriptors.ScorePair;

public class HighScoreResponse extends MainServerMessage{
	private static final long serialVersionUID = -4677900058140387705L;
	private Queue<ScorePair> HighScores = null;
	public HighScoreResponse(){
		
	}
	public Queue<ScorePair> getHighScores() {
		return HighScores;
	}
	public void setHighScores(Queue<ScorePair> highScores) {
		HighScores = highScores;
	}

}
