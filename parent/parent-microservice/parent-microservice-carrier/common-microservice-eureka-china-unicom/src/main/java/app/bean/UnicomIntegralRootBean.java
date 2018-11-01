/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;

public class UnicomIntegralRootBean {

	private UnicomIntegralTotalResult tegralResult;
	private List<List<String>> totalResult;
	private boolean scoreSuccess;
	private boolean produceSuccess;
	private UnicomErrorMessage errorMessage;

	public UnicomErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(UnicomErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setTegralResult(UnicomIntegralTotalResult tegralResult) {
		this.tegralResult = tegralResult;
	}

	public UnicomIntegralTotalResult getTegralResult() {
		return tegralResult;
	}

	public void setTotalResult(List<List<String>> totalResult) {
		this.totalResult = totalResult;
	}

	public List<List<String>> getTotalResult() {
		return totalResult;
	}

	public void setScoreSuccess(boolean scoreSuccess) {
		this.scoreSuccess = scoreSuccess;
	}

	public boolean getScoreSuccess() {
		return scoreSuccess;
	}

	public void setProduceSuccess(boolean produceSuccess) {
		this.produceSuccess = produceSuccess;
	}

	public boolean getProduceSuccess() {
		return produceSuccess;
	}

}