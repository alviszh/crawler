/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardUser;
import com.microservice.dao.entity.crawler.bank.bocom.creditcard.BocomCreditcardWechat;

/**
 * Auto-generated: 2017-11-22 15:52:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBeanBocom {

    private BocomCreditcardUser user;
    private String modelNickName;
    private String isCRS;
    private List<BocomCreditcardWechat> wechat;
    private List<String> qq;
    private String passwdStatus;
    private String modelCard;
    private String error;
    private String hasCard;
	public BocomCreditcardUser getUser() {
		return user;
	}
	public void setUser(BocomCreditcardUser user) {
		this.user = user;
	}
	public String getModelNickName() {
		return modelNickName;
	}
	public void setModelNickName(String modelNickName) {
		this.modelNickName = modelNickName;
	}
	public String getIsCRS() {
		return isCRS;
	}
	public void setIsCRS(String isCRS) {
		this.isCRS = isCRS;
	}
	public List<BocomCreditcardWechat> getWechat() {
		return wechat;
	}
	public void setWechat(List<BocomCreditcardWechat> wechat) {
		this.wechat = wechat;
	}
	public List<String> getQq() {
		return qq;
	}
	public void setQq(List<String> qq) {
		this.qq = qq;
	}
	public String getPasswdStatus() {
		return passwdStatus;
	}
	public void setPasswdStatus(String passwdStatus) {
		this.passwdStatus = passwdStatus;
	}
	public String getModelCard() {
		return modelCard;
	}
	public void setModelCard(String modelCard) {
		this.modelCard = modelCard;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getHasCard() {
		return hasCard;
	}
	public void setHasCard(String hasCard) {
		this.hasCard = hasCard;
	}
	@Override
	public String toString() {
		return "JsonRootBean [user=" + user + ", modelNickName=" + modelNickName + ", isCRS=" + isCRS + ", wechat="
				+ wechat + ", qq=" + qq + ", passwdStatus=" + passwdStatus + ", modelCard=" + modelCard + ", error="
				+ error + ", hasCard=" + hasCard + "]";
	}
    
    

}