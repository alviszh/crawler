/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.housing.daqing.HousingDaQingUserinfo;

/**
 * Auto-generated: 2017-11-07 14:7:28
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

	@Override
	public String toString() {
		return "JsonRootBean [success=" + success + ", msg=" + msg + ", totalcount=" + totalcount + ", results="
				+ results + ", erros=" + erros + ", vdMapList=" + vdMapList + ", data=" + data + "]";
	}

	private boolean success;
	private String msg;
	private int totalcount;
	private List<HousingDaQingUserinfo> results;
	private String erros;
	private String vdMapList;
	private Data data;

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setResults(List<HousingDaQingUserinfo> results) {
		this.results = results;
	}

	public List<HousingDaQingUserinfo> getResults() {
		return results;
	}

	public void setErros(String erros) {
		this.erros = erros;
	}

	public String getErros() {
		return erros;
	}

	public void setVdMapList(String vdMapList) {
		this.vdMapList = vdMapList;
	}

	public String getVdMapList() {
		return vdMapList;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

}