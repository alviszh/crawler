package app.crawler.unicom.htmlparse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.unicom.UnicomDetailList;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;
import com.microservice.dao.entity.crawler.unicom.UnicomCallResult;
import com.microservice.dao.entity.crawler.unicom.UnicomNoteResult;
import com.microservice.dao.entity.crawler.unicom.UnicomPayMsgStatusResult;
import com.microservice.dao.entity.crawler.unicom.UnicomUserActivityInfo;
import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

import app.bean.UnicomBalanceRootBean;
import app.bean.UnicomBilinfoJsonRootBean;
import app.bean.UnicomHistoryThemResultRoot;
import app.bean.UnicomHistoryThemRootBean;
import app.bean.UnicomIntegralRootBean;
import app.bean.UnicomIntegralreturnResultRoot;
import app.bean.UnicomInter2JsonRootBean;
import app.bean.UnicomInterallJsonRootBean;
import app.bean.UnicomRoot;
import app.bean.UnicomUserResultRoot;
import app.bean.UnicomUserinfoResult;
import app.bean.UnicomUserinfoRootBean;

public class HtmlParse {
	private static Gson gs = new Gson();

	// 通话详单解析
	public static UnicomUserResultRoot<UnicomCallResult> callthem_parse(String html) {
		UnicomUserInfo userInfo = null;
		UnicomUserResultRoot<UnicomCallResult> resultroot = new UnicomUserResultRoot<UnicomCallResult>();
		UnicomRoot jsonObject = gs.fromJson(html, UnicomRoot.class);
		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (userInfo == null) {
			userInfo = jsonObject.getUserInfo();
			resultroot.setUserInfo(userInfo);
		}
		if (jsonObject.getPageMap() != null) {
			String json = gs.toJson(jsonObject.getPageMap().getResult());
			List<UnicomCallResult> lists = gs.fromJson(json, new TypeToken<List<UnicomCallResult>>() {
			}.getType());
			resultroot.setResult(lists);
		}
		resultroot.setUserInfo(userInfo);
		return resultroot;
	}

	// 短信详单解析
	public static UnicomUserResultRoot<UnicomNoteResult> notethem_parse(String html) {
		UnicomUserInfo userInfo = null;
		UnicomUserResultRoot<UnicomNoteResult> resultroot = new UnicomUserResultRoot<UnicomNoteResult>();
		List<UnicomNoteResult> result = new ArrayList<>();

		UnicomRoot jsonObject = gs.fromJson(html, UnicomRoot.class);
		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (userInfo == null) {
			userInfo = jsonObject.getUserInfo();
			resultroot.setUserInfo(userInfo);
		}
		if (jsonObject.getPageMap() != null) {
			String json = gs.toJson(jsonObject.getPageMap().getResult());
			List<UnicomNoteResult> lists = gs.fromJson(json, new TypeToken<List<UnicomNoteResult>>() {
			}.getType());
			result.addAll(lists);
		}
		resultroot.setResult(result);
		return resultroot;
	}

	// 历史账单解析
	public static UnicomHistoryThemResultRoot<UnicomDetailList> historythem_parse(String html) {
		UnicomHistoryThemResultRoot<UnicomDetailList> resultroot = new UnicomHistoryThemResultRoot<UnicomDetailList>();
		List<UnicomDetailList> result = new ArrayList<>();
		UnicomHistoryThemRootBean jsonObject = gs.fromJson(html, UnicomHistoryThemRootBean.class);
		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (jsonObject.getHistoryResultList() != null) {
			String json = gs.toJson(jsonObject.getHistoryResultList());
			List<UnicomDetailList> lists = gs.fromJson(json, new TypeToken<List<UnicomDetailList>>() {
			}.getType());
			result.addAll(lists);
			resultroot.setResult(result);
			return resultroot;

		}
		return resultroot;
	}

	// 历史账单解析2
	public static UnicomHistoryThemResultRoot<UnicomDetailList> historythem_parse2(String html) {
		UnicomHistoryThemResultRoot<UnicomDetailList> resultroot = new UnicomHistoryThemResultRoot<UnicomDetailList>();
		List<UnicomDetailList> result = new ArrayList<>();
		UnicomBilinfoJsonRootBean jsonObject = gs.fromJson(html, UnicomBilinfoJsonRootBean.class);
		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;

		}
		if (jsonObject.getResult() != null) {
			if (jsonObject.getResult().getBillinfo() != null) {
				String json = gs.toJson(jsonObject.getResult().getBillinfo());
				List<UnicomDetailList> lists = gs.fromJson(json, new TypeToken<List<UnicomDetailList>>() {
				}.getType());
				result.addAll(lists);
			}

		}
		resultroot.setResult(result);
		return resultroot;
	}

	// 积分账单解析
	public static UnicomIntegralreturnResultRoot integrathem_parse(String html) {
		UnicomIntegralTotalResult unicomIntegralTegralThemResult = null;
		UnicomIntegralreturnResultRoot resultroot = new UnicomIntegralreturnResultRoot();
		List<UnicomIntegraThemlResult> result = new ArrayList<>();
		UnicomIntegralRootBean jsonObject = gs.fromJson(html, UnicomIntegralRootBean.class);
		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (unicomIntegralTegralThemResult == null) {
			unicomIntegralTegralThemResult = jsonObject.getTegralResult();
			resultroot.setUnicomIntegralTegralThemResult(unicomIntegralTegralThemResult);
		}
		if (jsonObject.getTegralResult() != null) {
			List<List<String>> lists = jsonObject.getTotalResult();
			if (lists != null) {
				for (List<String> list : lists) {
					UnicomIntegraThemlResult unresult = new UnicomIntegraThemlResult();
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 小写的mm表示的是分钟
						Date date = sdf.parse(list.get(0));
						unresult.setDate(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					unresult.setType(list.get(1));
					unresult.setCalltype(list.get(2));
					unresult.setIntegral(list.get(3));
					result.add(unresult);
				}
			}
		}
		resultroot.setUnicomIntegraThemlResultlist(result);
		return resultroot;
	}

	// 积分账单解析2production
	public static UnicomIntegralreturnResultRoot integrathem_parse2_production(String html) {

		UnicomIntegralreturnResultRoot resultroot = new UnicomIntegralreturnResultRoot();

		List<UnicomIntegraThemlResult> result = new ArrayList<>();
		UnicomInter2JsonRootBean jsonObject = gs.fromJson(html, UnicomInter2JsonRootBean.class);

		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (jsonObject.getResult() != null) {
			if (jsonObject.getResult().getReturninfo() != null) {

				if (jsonObject.getResult().getReturninfo().getScoredetailinfo() != null) {
					result = jsonObject.getResult().getReturninfo().getScoredetailinfo();
				}

			}

		}
		resultroot.setUnicomIntegraThemlResultlist(result);
		return resultroot;
	}

	// 积分账单解析2total
	public static UnicomIntegralreturnResultRoot integrathem_parse2_total(String html) {

		UnicomIntegralreturnResultRoot resultroot = new UnicomIntegralreturnResultRoot();

		UnicomIntegralTotalResult result = null;

		UnicomInterallJsonRootBean jsonObject = gs.fromJson(html, UnicomInterallJsonRootBean.class);

		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (jsonObject.getResult() != null) {
			if (jsonObject.getResult().getScoreinfo() != null) {
				result = jsonObject.getResult().getScoreinfo().get(0);

			}

		}
		resultroot.setUnicomIntegralTegralThemResult(result);
		return resultroot;
	}

	// 缴费详单解析
	public static UnicomUserResultRoot<UnicomPayMsgStatusResult> paymsgstatus_parse(String html) {
		System.out.println("==================准备解析===========" + html);
		UnicomUserResultRoot<UnicomPayMsgStatusResult> resultroot = new UnicomUserResultRoot<UnicomPayMsgStatusResult>();
		List<UnicomPayMsgStatusResult> result = new ArrayList<>();
		UnicomRoot jsonObject = gs.fromJson(html, UnicomRoot.class);
		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (jsonObject.getTotalResult() != null) {
			List<UnicomPayMsgStatusResult> lists = jsonObject.getTotalResult();
			for (UnicomPayMsgStatusResult unicomPayMsgStatusResult : lists) {
				System.out.println("缴费解析结果=========" + unicomPayMsgStatusResult.toString());
			}
			result.addAll(lists);
		}
		resultroot.setResult(result);
		return resultroot;
	}

	// 用户详单解析
	public static UnicomUserResultRoot<UnicomUserActivityInfo> userinfo_parse(String html) {
		UnicomUserResultRoot<UnicomUserActivityInfo> resultroot = new UnicomUserResultRoot<UnicomUserActivityInfo>();
		List<UnicomUserActivityInfo> result = new ArrayList<>();
		UnicomUserInfo userInfo = null;
		UnicomUserinfoRootBean jsonObject = gs.fromJson(html, UnicomUserinfoRootBean.class);

		if (jsonObject.getErrorMessage() != null) {
			if (jsonObject.getErrorMessage().toString().indexOf("访问过于频繁") != -1) {
				resultroot.setErrorMessage(jsonObject.getErrorMessage());
				return resultroot;
			}
		}
		if (userInfo == null) {
			userInfo = jsonObject.getUserInfo();
			resultroot.setUserInfo(userInfo);
		}
		if (jsonObject.getResult() != null) {

			String json = gs.toJson(jsonObject.getResult());
			System.out.println(json);
			UnicomUserinfoResult userinforesult = gs.fromJson(json, new TypeToken<UnicomUserinfoResult>() {
			}.getType());
			List<UnicomUserActivityInfo> lists = userinforesult.getActivityInfo();
			if (lists != null) {
				result.addAll(lists);
			}

		}
		resultroot.setResult(result);
		return resultroot;

	}

	// 用户详单解析
	public static UnicomUserResultRoot<UnicomUserActivityInfo> balance_parse(String html) {
		UnicomUserResultRoot<UnicomUserActivityInfo> resultroot = new UnicomUserResultRoot<UnicomUserActivityInfo>();
		System.out.println(html);
		UnicomBalanceRootBean jsonObject = gs.fromJson(html, UnicomBalanceRootBean.class);

		if (jsonObject.getErrorMessage() != null) {
			resultroot.setErrorMessage(jsonObject.getErrorMessage());
			return resultroot;
		}
		if (jsonObject.getResource() != null) {

			resultroot.setResource(jsonObject.getResource());

		}
		return resultroot;

	}
}