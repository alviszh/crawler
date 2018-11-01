package app.bean;

import java.util.List;

public class UnicomHistoryThemResultRoot<T> {

	private UnicomHistoryScoreInfo scoreInfo;

	private List<T> result;
		
    private UnicomHistoryErrorMessage errorMessage;

	public UnicomHistoryScoreInfo getScoreInfo() {
		return scoreInfo;
	}

	public void setScoreInfo(UnicomHistoryScoreInfo scoreInfo) {
		this.scoreInfo = scoreInfo;
	}

	public UnicomHistoryErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(UnicomHistoryErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

}
