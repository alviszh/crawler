package app.vo;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomCallResult;
import com.microservice.dao.entity.crawler.unicom.UnicomDetailList;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;
import com.microservice.dao.entity.crawler.unicom.UnicomNoteResult;
import com.microservice.dao.entity.crawler.unicom.UnicomPayMsgStatusResult;
import com.microservice.dao.entity.crawler.unicom.UnicomUserActivityInfo;
import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

/**
 * API测试 中国联通 Vo
 * @author rongshengxu
 *
 */
public class ApiTestUnicomVo {
	
	/** 用户信息 */
	private List<UnicomUserInfo> listofUnicomUserInfo;
	
	/** 通话详单 */
	private List<UnicomCallResult> listOfUnicomCallResult;
	
	/** 短信信息 */
	private List<UnicomNoteResult> listOfUnicomNoteResult;
	
	/** 历史账单 */
	private List<UnicomDetailList> listOfUnicomDetailList;
	
	/** 积分 */
	private List<UnicomIntegraThemlResult> listOfUnicomIntegraThemlResult;
	
	/** 积分明细 */
	private List<UnicomIntegralTotalResult> listOfUnicomIntegralTegralThemResult;
	
	/** 缴费记录 */
	private List<UnicomPayMsgStatusResult> listOfUnicomPayMsgStatusResult;
	
	/** 合约信息 */
	private List<UnicomUserActivityInfo> listOfUnicomUserActivityInfo;

	
	public List<UnicomUserInfo> getListofUnicomUserInfo() {
		return listofUnicomUserInfo;
	}

	public void setListofUnicomUserInfo(List<UnicomUserInfo> listofUnicomUserInfo) {
		this.listofUnicomUserInfo = listofUnicomUserInfo;
	}

	public List<UnicomCallResult> getListOfUnicomCallResult() {
		return listOfUnicomCallResult;
	}

	public void setListOfUnicomCallResult(List<UnicomCallResult> listOfUnicomCallResult) {
		this.listOfUnicomCallResult = listOfUnicomCallResult;
	}

	public List<UnicomNoteResult> getListOfUnicomNoteResult() {
		return listOfUnicomNoteResult;
	}

	public void setListOfUnicomNoteResult(List<UnicomNoteResult> listOfUnicomNoteResult) {
		this.listOfUnicomNoteResult = listOfUnicomNoteResult;
	}

	public List<UnicomDetailList> getListOfUnicomDetailList() {
		return listOfUnicomDetailList;
	}

	public void setListOfUnicomDetailList(List<UnicomDetailList> listOfUnicomDetailList) {
		this.listOfUnicomDetailList = listOfUnicomDetailList;
	}

	public List<UnicomIntegraThemlResult> getListOfUnicomIntegraThemlResult() {
		return listOfUnicomIntegraThemlResult;
	}

	public void setListOfUnicomIntegraThemlResult(List<UnicomIntegraThemlResult> listOfUnicomIntegraThemlResult) {
		this.listOfUnicomIntegraThemlResult = listOfUnicomIntegraThemlResult;
	}

	public List<UnicomIntegralTotalResult> getListOfUnicomIntegralTegralThemResult() {
		return listOfUnicomIntegralTegralThemResult;
	}

	public void setListOfUnicomIntegralTegralThemResult(
			List<UnicomIntegralTotalResult> listOfUnicomIntegralTegralThemResult) {
		this.listOfUnicomIntegralTegralThemResult = listOfUnicomIntegralTegralThemResult;
	}

	public List<UnicomPayMsgStatusResult> getListOfUnicomPayMsgStatusResult() {
		return listOfUnicomPayMsgStatusResult;
	}

	public void setListOfUnicomPayMsgStatusResult(List<UnicomPayMsgStatusResult> listOfUnicomPayMsgStatusResult) {
		this.listOfUnicomPayMsgStatusResult = listOfUnicomPayMsgStatusResult;
	}

	public List<UnicomUserActivityInfo> getListOfUnicomUserActivityInfo() {
		return listOfUnicomUserActivityInfo;
	}

	public void setListOfUnicomUserActivityInfo(List<UnicomUserActivityInfo> listOfUnicomUserActivityInfo) {
		this.listOfUnicomUserActivityInfo = listOfUnicomUserActivityInfo;
	}

	
}
