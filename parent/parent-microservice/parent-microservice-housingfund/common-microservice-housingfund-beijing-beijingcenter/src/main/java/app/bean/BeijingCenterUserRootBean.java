/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBasicCenterUserBean;

/**
 * Auto-generated: 2018-08-13 18:15:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BeijingCenterUserRootBean {

	private String returnCode;
	private String message;
	private String type;
	private HousingBasicCenterUserBean data;

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public HousingBasicCenterUserBean getData() {
		return data;
	}

	public void setData(HousingBasicCenterUserBean data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BeijingCenterRootBean [returnCode=" + returnCode + ", message=" + message + ", type=" + type + ", data="
				+ data + "]";
	}

}