package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.e_commerce.etl.suning.AccountInfoSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.AddressInfoSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.CardInfoSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.OrderDetailSuning;
import com.microservice.dao.entity.crawler.e_commerce.etl.suning.UserInfoSuning;



public class WebDataSuning {

	public RequestParam param;	
	public List<UserInfoSuning> userInfoSuning;
	public List<OrderDetailSuning> orderDetailSuning;
	public List<AccountInfoSuning> accountInfoSuning;
	public List<CardInfoSuning> cardInfoSuning;
	public List<AddressInfoSuning> addressInfoSuning;
	public Date createtime = new Date();
	public String message;
	public Integer errorCode;
	public String profile;
	
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public List<UserInfoSuning> getUserInfoSuning() {
		return userInfoSuning;
	}
	public void setUserInfoSuning(List<UserInfoSuning> userInfoSuning) {
		this.userInfoSuning = userInfoSuning;
	}
	public List<OrderDetailSuning> getOrderDetailSuning() {
		return orderDetailSuning;
	}
	public void setOrderDetailSuning(List<OrderDetailSuning> orderDetailSuning) {
		this.orderDetailSuning = orderDetailSuning;
	}
	public List<AccountInfoSuning> getAccountInfoSuning() {
		return accountInfoSuning;
	}
	public void setAccountInfoSuning(List<AccountInfoSuning> accountInfoSuning) {
		this.accountInfoSuning = accountInfoSuning;
	}
	public List<CardInfoSuning> getCardInfoSuning() {
		return cardInfoSuning;
	}
	public void setCardInfoSuning(List<CardInfoSuning> cardInfoSuning) {
		this.cardInfoSuning = cardInfoSuning;
	}
	public List<AddressInfoSuning> getAddressInfoSuning() {
		return addressInfoSuning;
	}
	public void setAddressInfoSuning(List<AddressInfoSuning> addressInfoSuning) {
		this.addressInfoSuning = addressInfoSuning;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
		
}
