package app.vo;

import java.util.List;

import com.microservice.dao.entity.crawler.cmcc.CmccCheckMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccPayMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccSMSMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserCallResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserInfo;

/**
 * API测试 中国移动 Vo
 * @author rongshengxu
 *
 */
public class ApiTestCmccVo {

	/** 用户信息 */
	private List<CmccUserInfo> listOfCmccUserInfo;
	/** 通话详单 */
	private List<CmccUserCallResult> listOfCmccUserCallResult;
	/** 短信信息 */
	private List<CmccSMSMsgResult> listOfCmccSMSMsgResult;
	/** 月账单 */
	private List<CmccCheckMsgResult> listOfCmccCheckMsgResult;
	/** 缴费记录 */
	private List<CmccPayMsgResult> listOfCmccPayMsgResult;
	
	public List<CmccUserInfo> getListOfCmccUserInfo() {
		return listOfCmccUserInfo;
	}
	public void setListOfCmccUserInfo(List<CmccUserInfo> listOfCmccUserInfo) {
		this.listOfCmccUserInfo = listOfCmccUserInfo;
	}
	public List<CmccUserCallResult> getListOfCmccUserCallResult() {
		return listOfCmccUserCallResult;
	}
	public void setListOfCmccUserCallResult(List<CmccUserCallResult> listOfCmccUserCallResult) {
		this.listOfCmccUserCallResult = listOfCmccUserCallResult;
	}
	public List<CmccSMSMsgResult> getListOfCmccSMSMsgResult() {
		return listOfCmccSMSMsgResult;
	}
	public void setListOfCmccSMSMsgResult(List<CmccSMSMsgResult> listOfCmccSMSMsgResult) {
		this.listOfCmccSMSMsgResult = listOfCmccSMSMsgResult;
	}
	public List<CmccCheckMsgResult> getListOfCmccCheckMsgResult() {
		return listOfCmccCheckMsgResult;
	}
	public void setListOfCmccCheckMsgResult(List<CmccCheckMsgResult> listOfCmccCheckMsgResult) {
		this.listOfCmccCheckMsgResult = listOfCmccCheckMsgResult;
	}
	public List<CmccPayMsgResult> getListOfCmccPayMsgResult() {
		return listOfCmccPayMsgResult;
	}
	public void setListOfCmccPayMsgResult(List<CmccPayMsgResult> listOfCmccPayMsgResult) {
		this.listOfCmccPayMsgResult = listOfCmccPayMsgResult;
	}
	
	
}
