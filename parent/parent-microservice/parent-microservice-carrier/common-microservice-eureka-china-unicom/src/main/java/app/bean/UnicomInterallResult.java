/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;

/**
 * Auto-generated: 2017-07-30 11:56:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UnicomInterallResult {

	private String busiorder;
	private boolean issuccess;
	private String respcode;
	private String respdesc;
	private List<UnicomIntegralTotalResult> scoreinfo;

	public void setBusiorder(String busiorder) {
		this.busiorder = busiorder;
	}

	public String getBusiorder() {
		return busiorder;
	}

	public void setIssuccess(boolean issuccess) {
		this.issuccess = issuccess;
	}

	public boolean getIssuccess() {
		return issuccess;
	}

	public void setRespcode(String respcode) {
		this.respcode = respcode;
	}

	public String getRespcode() {
		return respcode;
	}

	public void setRespdesc(String respdesc) {
		this.respdesc = respdesc;
	}

	public String getRespdesc() {
		return respdesc;
	}

	public List<UnicomIntegralTotalResult> getScoreinfo() {
		return scoreinfo;
	}

	public void setScoreinfo(List<UnicomIntegralTotalResult> scoreinfo) {
		this.scoreinfo = scoreinfo;
	}

}