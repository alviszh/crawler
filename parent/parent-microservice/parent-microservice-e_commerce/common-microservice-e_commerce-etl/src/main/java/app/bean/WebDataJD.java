package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.e_commerce.etl.jd.AddrInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.AuthInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BaiTiaoInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BillInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.BrowseHistoryJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.CardInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.FinanceInfoJD;
import com.microservice.dao.entity.crawler.e_commerce.etl.jd.UserInfoJD;



public class WebDataJD {

	public RequestParam param;	
	public List<AddrInfoJD> addrInfoJD;
	public List<AuthInfoJD> authInfoJD;
	public List<BaiTiaoInfoJD> baiTiaoInfoJD;
	public List<BillInfoJD> billInfoJD;
	public List<BrowseHistoryJD> browseHistoryJD;
	public List<CardInfoJD> cardInfoJD;
	public List<FinanceInfoJD> financeInfoJD;
	public List<UserInfoJD> userInfoJD;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<AddrInfoJD> getAddrInfoJD() {
		return addrInfoJD;
	}
	public void setAddrInfoJD(List<AddrInfoJD> addrInfoJD) {
		this.addrInfoJD = addrInfoJD;
	}
	public List<AuthInfoJD> getAuthInfoJD() {
		return authInfoJD;
	}
	public void setAuthInfoJD(List<AuthInfoJD> authInfoJD) {
		this.authInfoJD = authInfoJD;
	}
	public List<BaiTiaoInfoJD> getBaiTiaoInfoJD() {
		return baiTiaoInfoJD;
	}
	public void setBaiTiaoInfoJD(List<BaiTiaoInfoJD> baiTiaoInfoJD) {
		this.baiTiaoInfoJD = baiTiaoInfoJD;
	}
	public List<BillInfoJD> getBillInfoJD() {
		return billInfoJD;
	}
	public void setBillInfoJD(List<BillInfoJD> billInfoJD) {
		this.billInfoJD = billInfoJD;
	}
	public List<BrowseHistoryJD> getBrowseHistoryJD() {
		return browseHistoryJD;
	}
	public void setBrowseHistoryJD(List<BrowseHistoryJD> browseHistoryJD) {
		this.browseHistoryJD = browseHistoryJD;
	}
	public List<CardInfoJD> getCardInfoJD() {
		return cardInfoJD;
	}
	public void setCardInfoJD(List<CardInfoJD> cardInfoJD) {
		this.cardInfoJD = cardInfoJD;
	}
	public List<FinanceInfoJD> getFinanceInfoJD() {
		return financeInfoJD;
	}
	public void setFinanceInfoJD(List<FinanceInfoJD> financeInfoJD) {
		this.financeInfoJD = financeInfoJD;
	}
	public List<UserInfoJD> getUserInfoJD() {
		return userInfoJD;
	}
	public void setUserInfoJD(List<UserInfoJD> userInfoJD) {
		this.userInfoJD = userInfoJD;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
