/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

import com.microservice.dao.entity.crawler.housing.yvlin.HousingBasicUserData;

/**
 * Auto-generated: 2018-01-10 15:24:0
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BasicUserJsonRootBean {

	private HousingBasicUserData data;
	private String returnCode;

	public void setData(HousingBasicUserData data) {
		this.data = data;
	}

	public HousingBasicUserData getData() {
		return data;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

}