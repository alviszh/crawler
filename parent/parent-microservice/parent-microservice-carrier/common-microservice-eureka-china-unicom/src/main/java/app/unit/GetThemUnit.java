package app.unit;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.service.LoginAndGetService;

@Service
public class GetThemUnit {
	public static final Logger log = LoggerFactory.getLogger(GetThemUnit.class);
	@Autowired
	LoginAndGetService loginAndGetService;

	@Autowired
	private TracerLog tracerLog;


	// 根据当日期获取六个月通话详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getCallThemhtml(TaskMobile taskMobile,String firstday,String lastDay, int k) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = loginAndGetService.addcookie(webClient, taskMobile);
		

		String html = null;
		try {
			html = loginAndGetService.getCallThem(webClient, firstday.toString(), lastDay.toString());
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}
		

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
//			k++;
//			if (k < 3) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getCallThemhtml(taskMobile, firstday, lastDay, k);
//			} else {
//				return html;
//			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			throw new ErrorException("系统正忙，请稍后再试");
		}

		return html;
	}

	// 根据当日期获取月短信详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getNoteThem(TaskMobile taskMobile, String firstday,String lastDay, int k) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = loginAndGetService.addcookie(webClient, taskMobile);

		String html = null;
		try {
			html = loginAndGetService.getNoteThem(webClient, firstday.toString().replaceAll("-", ""),
					lastDay.toString().replaceAll("-", ""));
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
			html = null;
		}

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
//			k++;
//			if (k < 3) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getNoteThem(taskMobile,firstday.toString(), lastDay.toString(), k);
//			} else {
//				return html;
//			}
			
			throw new ErrorException("系统正忙，请稍后再试");


		}

		return html;
	}

	// 根据当日期获取月历史详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getHistoryThem(TaskMobile taskMobile,String month, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}
		
		String html = null;
		try {
			html = loginAndGetService.getHistoryThem(cookiesmap, month);
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}
		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
//			k++;
//			if (k < 2) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getHistoryThem(taskMobile, month, k);
//			} else {
//				return html;
//			}
			throw new ErrorException("系统正忙，请稍后再试");


		}

		return html;

	}

	// 根据当日期获取月积分详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getIntegraThem(TaskMobile taskMobile, int i, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}
		LocalDate nowdate = LocalDate.now();

		String month = nowdate.plusMonths(-i).getMonthValue() + "";
		if (month.length() < 2) {
			month = "0" + month;
		}

		String html = null;
		try {
			html = loginAndGetService.getIntegraThem(cookiesmap, nowdate.plusMonths(-i).getYear() + "" + month);
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
//			k++;
//			if (k < 2) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getIntegraThem(taskMobile, i, k);
//			} else {
//				return html;
//			}
			throw new ErrorException("系统正忙，请稍后再试");

		}

		return html;

	}

	// 根据当日期获取月积分详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getIntegraThem2Production(TaskMobile taskMobile, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}

		String html = null;
		try {
			html = loginAndGetService.getIntegraThem2Production(cookiesmap);
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
//			k++;
//			if (k < 2) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getIntegraThem2Production(taskMobile, 0);
//			} else {
//				return html;
//			}
			throw new ErrorException("系统正忙，请稍后再试");

		}

		return html;

	}

	// 根据当日期获取月积分详单总分
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getIntegraThem2Total(TaskMobile taskMobile, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}

		String html = null;
		try {
			html = loginAndGetService.getIntegraThem2All(cookiesmap);
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
//			k++;
//			if (k < 2) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getIntegraThem2Total(taskMobile, 0);
//			} else {
//				return html;
//			}
			throw new ErrorException("系统正忙，请稍后再试");

		}

		return html;

	}

	// 根据当日期获取月缴费记录详单
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getPayMsgStatusThem(TaskMobile taskMobile, int i, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());

		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);

		// 本月的最后一天
		LocalDate lastDay = null;
		if (i != 0) {
			lastDay = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		} else {
			lastDay = today;
		}

		String html = null;
		try {
			html = loginAndGetService.getPayMsgStatusThem(cookiesmap, firstday.toString().replaceAll("-", ""),
					lastDay.toString().replaceAll("-", ""));
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}

		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
			tracerLog.addTag("unicom crawler", "尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解");

//			k++;
//			if (k < 2) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return html = getPayMsgStatusThem(taskMobile, i, k);
//			} else {
//				return html;
//			}
			throw new ErrorException("系统正忙，请稍后再试");

		}

		return html;

	}

	// 获取用户信息
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getUserinfoThem(TaskMobile taskMobile, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());

		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}

		String html = null;
		try {
			html = loginAndGetService.getUserinfoThem(cookiesmap);
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}
		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
			k++;
			if (k < 2) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getUserinfoThem(taskMobile, k);
			} else {
				return html;
			}

		}
		return html;

	}

	// 获取用户信息
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public String getBalanceThem(TaskMobile taskMobile, int k) {

		Set<Cookie> cookieSet = CommonUnit.transferJsonToSet(taskMobile.getCookies());

		Map<String, String> cookiesmap = new HashMap<String, String>();
		for (Cookie cookie : cookieSet) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}

		String html = null;
		try {
			html = loginAndGetService.getBalanceThem(cookiesmap);
		} catch (Exception e) {
			tracerLog.addTag("crawler error ", e.getMessage());
		}
		if (html == null) {
			log.info("=================html = null");
			k++;
			if (k < 2) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getBalanceThem(taskMobile, k);
			} else {
				return html;
			}
		}
		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1 || html == null) {
			k++;
			if (k < 2) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getBalanceThem(taskMobile, k);
			} else {
				return html;
			}

		}
		return html;

	}

//	public static void main(String[] args) {
//		// LocalDate nowdate = LocalDate.now();
//
//		// 本月的第一天
//		LocalDate today = LocalDate.now();
//		LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1);
//		// 本月的最后一天
//		LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
//	}

}
