package app.unit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.crawler.telecomhtmlunit.LognAndGetQingHai;

public class GetThemUnitTelecom {
	public static final Logger log = LoggerFactory.getLogger(GetThemUnitTelecom.class);
	// 获取北京用户积分 话费余额
	public static String getPointsAndCharges(MessageLogin messageLogin, TaskMobile taskMobile, int i) throws IOException, Exception {
		return LognAndGetQingHai.getPointsAndCharges(messageLogin, taskMobile);
	}

	// 获取北京用户缴费信息
	public static String getpayResult(MessageLogin messageLogin, TaskMobile taskMobile, int i, int k) {

		LocalDate today = LocalDate.now();

		LocalDate enddate = today.plusMonths(-3);
		
		// 本月的最后一天

		String html = LognAndGetQingHai.getpayResult(messageLogin, taskMobile, enddate.toString(), today.toString());
		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getpayResult(messageLogin, taskMobile, i, k);
			} else {
				return html;
			}

		}

		return html;
	}

	// 获取北京用户获取青海用户 积分生成记录
	public static String getintegraResult(MessageLogin messageLogin, TaskMobile taskMobile, int i, int k) throws IOException, Exception {

		LocalDate today = LocalDate.now();

		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i != 0) {
			enddate = today;
		}
		String month = enddate.getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}
		String year = enddate.getYear() + month;

		String html = LognAndGetQingHai.getintegraResult(messageLogin, taskMobile, year);
		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getintegraResult(messageLogin, taskMobile, i, k);
			} else {
				return html;
			}

		}

		return html;
	}

	// 获取北京用户缴费信息
	public static String getBill(MessageLogin messageLogin, TaskMobile taskMobile, int i, int k) {

		LocalDate today = LocalDate.now();

		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i != 0) {
			enddate = today;
		}
		String month = enddate.getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}
		
		String html = null;
		try {
			html = LognAndGetQingHai.getBill(messageLogin, taskMobile,enddate.getYear()+month);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (html == null || html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getBill(messageLogin, taskMobile, i, k);
			} else {
				return html;
			}

		}

		return html;
	}

	// 获取北京用户 通话详单
	public static String getPhoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i,
			int k) throws Exception {

		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate effDate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate expDate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			expDate = today;
		}

		String html = LognAndGetQingHai.getPhoneBill(webClient, messageLogin, taskMobile, effDate + "", expDate + "");

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getPhoneBill(webClient, messageLogin, taskMobile, i, k);
			} else {
				return html;
			}

		}

		return html;
	}

	// 获取北京用户 短信详单
	public static String getSMSBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i,
			int k) throws Exception {

		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate effDate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-0);
		LocalDate expDate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-0);
		if (i == 0) {
			expDate = today;
		}

		String html = LognAndGetQingHai.getSMSBill(webClient, messageLogin, taskMobile, effDate + "", expDate + "");

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getSMSBill(webClient, messageLogin, taskMobile, i, k);
			} else {
				return html;
			}

		}

		return html;
	}

	/**获取北京用户 获取青海用户 信息，余额，业务等
	 * @param webClient
	 * @param messageLogin
	 * @param taskMobile
	 * @param i
	 * @param k
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String getFeeBalance(MessageLogin messageLogin, TaskMobile taskMobile,
			int k) throws FailingHttpStatusCodeException, IOException {

		String html = LognAndGetQingHai.getFeeBalance(messageLogin, taskMobile);

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return getFeeBalance(messageLogin, taskMobile, k);
			} else {
				return html;
			}

		}

		return html;
	}
	
	/**获取北京用户 获取青海用户 信息，余额，业务等
	 * @param webClient
	 * @param messageLogin
	 * @param taskMobile
	 * @param i
	 * @param k
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String getUseablePoint( MessageLogin messageLogin, TaskMobile taskMobile,
			int k) throws FailingHttpStatusCodeException, IOException {

		String html = LognAndGetQingHai.getUseablePoint(messageLogin, taskMobile);

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getUseablePoint(messageLogin, taskMobile, k);
			} else {
				return html;
			}

		}

		return html;
	}
	
	/**获取北京用户 获取青海用户 信息，余额，业务等
	 * @param webClient
	 * @param messageLogin
	 * @param taskMobile
	 * @param i
	 * @param k
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String getHandledBiz( MessageLogin messageLogin, TaskMobile taskMobile,
			int k) throws FailingHttpStatusCodeException, IOException {

		String html = LognAndGetQingHai.getHandledBiz(messageLogin, taskMobile);

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getHandledBiz( messageLogin, taskMobile, k);
			} else {
				return html;
			}

		}

		return html;
	}
	
	/**获取北京用户 获取青海用户 信息，余额，业务等
	 * @param webClient
	 * @param messageLogin
	 * @param taskMobile
	 * @param i
	 * @param k
	 * @return
	 * @throws Exception 
	 */
	public static String getUserName( MessageLogin messageLogin, TaskMobile taskMobile,
			int k) throws Exception {

		String html = LognAndGetQingHai.getUserName(messageLogin, taskMobile);

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getUserName( messageLogin, taskMobile, k);
			} else {
				return html;
			}

		}

		return html;
	}
	
	//http://qh.189.cn/service/account/init.action?csrftoken=QQ_OPEN_TOKEN&fastcode=00900906&cityCode=qh&SSOURL=http://qh.189.cn/service/account/init.action?csrftoken=QQ_OPEN_TOKEN&fastcode=00900906&cityCode=qh
	
	public static void main(String[] args) {
		/*
		 * LocalDate today = LocalDate.now(); LocalDate enddate =
		 * today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-3);
		 */

		// 本月的第一天
		/*
		 * LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),
		 * 1).plusMonths(-1); LocalDate enddate =
		 * today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-1);
		 * 
		 * String monthint = stardate.getMonthValue() + ""; if
		 * (monthint.length() < 2) { monthint = "0" + monthint; } String month =
		 * stardate.getYear() + "年" + monthint + "月"; System.out.println(month);
		 */
		/*
		 * String month = enddate.getMonthValue()+""; if(month.length()<2){
		 * month = "0"+month; } String year = enddate.getYear()+month;
		 */

		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-0);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-0);
		/*
		 * if (i == 0) { enddate = today; }
		 */
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}

		System.out.println(stardate + ":" + enddate);

	}

}
