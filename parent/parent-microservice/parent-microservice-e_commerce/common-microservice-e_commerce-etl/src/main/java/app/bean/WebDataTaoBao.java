package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AddrinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AlipayPaymentTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.AlipayUserinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.BillinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.CardinfoTaoBao;
import com.microservice.dao.entity.crawler.e_commerce.etl.taobao.UserInfoTaoBao;



public class WebDataTaoBao {

	public RequestParam param;	
	public List<UserInfoTaoBao> userInfoTaoBao;
	public List<BillinfoTaoBao> billinfoTaoBao;	
	public List<AlipayUserinfoTaoBao> alipayUserinfoTaoBao;
	public List<AlipayPaymentTaoBao> alipayPaymentTaoBao;
	public List<CardinfoTaoBao> cardinfoTaoBao;
	public List<AddrinfoTaoBao> addrinfoTaoBao;
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
	public List<UserInfoTaoBao> getUserInfoTaoBao() {
		return userInfoTaoBao;
	}
	public void setUserInfoTaoBao(List<UserInfoTaoBao> userInfoTaoBao) {
		this.userInfoTaoBao = userInfoTaoBao;
	}
	public List<BillinfoTaoBao> getBillinfoTaoBao() {
		return billinfoTaoBao;
	}
	public void setBillinfoTaoBao(List<BillinfoTaoBao> billinfoTaoBao) {
		this.billinfoTaoBao = billinfoTaoBao;
	}
	public List<AlipayUserinfoTaoBao> getAlipayUserinfoTaoBao() {
		return alipayUserinfoTaoBao;
	}
	public void setAlipayUserinfoTaoBao(List<AlipayUserinfoTaoBao> alipayUserinfoTaoBao) {
		this.alipayUserinfoTaoBao = alipayUserinfoTaoBao;
	}
	public List<AlipayPaymentTaoBao> getAlipayPaymentTaoBao() {
		return alipayPaymentTaoBao;
	}
	public void setAlipayPaymentTaoBao(List<AlipayPaymentTaoBao> alipayPaymentTaoBao) {
		this.alipayPaymentTaoBao = alipayPaymentTaoBao;
	}
	public List<CardinfoTaoBao> getCardinfoTaoBao() {
		return cardinfoTaoBao;
	}
	public void setCardinfoTaoBao(List<CardinfoTaoBao> cardinfoTaoBao) {
		this.cardinfoTaoBao = cardinfoTaoBao;
	}
	public List<AddrinfoTaoBao> getAddrinfoTaoBao() {
		return addrinfoTaoBao;
	}
	public void setAddrinfoTaoBao(List<AddrinfoTaoBao> addrinfoTaoBao) {
		this.addrinfoTaoBao = addrinfoTaoBao;
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
