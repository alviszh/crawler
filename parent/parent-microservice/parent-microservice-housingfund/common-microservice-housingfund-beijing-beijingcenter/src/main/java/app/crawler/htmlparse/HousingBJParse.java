package app.crawler.htmlparse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBeiJingCenterBasicPayBean;
import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBeiJingCenterPayBean;

import app.bean.BeiJingCenterBean;
import app.bean.BeijingCenterPayRootBean;
import app.bean.BeijingCenterUserRootBean;

public class HousingBJParse {

	private static Gson gs = new Gson();

	public static BeiJingCenterBean beijingcenter_need_parse(String html) {
		Type type = new TypeToken<BeiJingCenterBean>() {
		}.getType();

		BeiJingCenterBean jsonObject = gs.fromJson(html, type);
		return jsonObject;
	}

	public static BeijingCenterUserRootBean beijingcenter_basic_parse(String html) {
		Type type = new TypeToken<BeijingCenterUserRootBean>() {
		}.getType();

		BeijingCenterUserRootBean jsonObject = gs.fromJson(html, type);
		return jsonObject;
	}

	public static List<HousingBeiJingCenterBasicPayBean> beijingcenter_basicpay_parse(String html) {

		Document doc = Jsoup.parse(html);

		Elements eles = doc.select("tbody>tr");

		System.out.println("查找数据" + eles);

		List<HousingBeiJingCenterBasicPayBean> result = null;

		if (eles.size() > 0) {
			result = new ArrayList<>();
			for (Element ele : eles) {
				HousingBeiJingCenterBasicPayBean housingBeiJingCenterBasicPayBean = new HousingBeiJingCenterBasicPayBean();

				try {
					housingBeiJingCenterBasicPayBean.setName(ele.select("td").get(0).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setCardType(ele.select("td").get(1).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setCardNum(ele.select("td").get(2).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setGjjnum(ele.select("td").get(3).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setCompany(ele.select("td").get(4).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setPerbase(ele.select("td").get(5).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setCompanyPay(ele.select("td").get(6).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setPerPay(ele.select("td").get(7).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setMonthPay(ele.select("td").get(8).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setPayStatue(ele.select("td").get(9).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setPerBalance(ele.select("td").get(10).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setFrozen(ele.select("td").get(11).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterBasicPayBean.setOpenDate(ele.select("td").get(12).text());

				} catch (Exception e) {

				}

				result.add(housingBeiJingCenterBasicPayBean);
			}

		}

		return result;
	}

	public static BeijingCenterPayRootBean beijingcenter_payroot_parse(String html) {
		Type type = new TypeToken<BeijingCenterPayRootBean>() {
		}.getType();

		BeijingCenterPayRootBean jsonObject = gs.fromJson(html, type);
		return jsonObject;
	}

	public static List<HousingBeiJingCenterPayBean> beijingcenter_pay_parse(String html) {

		Document doc = Jsoup.parse(html);

		Elements eles = doc.select("tbody>tr");

		List<HousingBeiJingCenterPayBean> result = null;

		if (eles.size() > 0) {
			result = new ArrayList<>();

			for (Element ele : eles) {
				HousingBeiJingCenterPayBean housingBeiJingCenterPayBean = new HousingBeiJingCenterPayBean();

				try {
					housingBeiJingCenterPayBean.setDate(ele.select("td").get(0).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setCompanyNum(ele.select("td").get(1).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setCompany(ele.select("td").get(2).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setBusinessType(ele.select("td").get(3).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setPayMonth(ele.select("td").get(4).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setIncreasedNum(ele.select("td").get(5).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setReduceNum(ele.select("td").get(6).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setBalance(ele.select("td").get(7).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setPaymentName(ele.select("td").get(8).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setPayment(ele.select("td").get(9).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setYwls(ele.select("td").get(10).text());

				} catch (Exception e) {

				}

				try {
					housingBeiJingCenterPayBean.setOpdeck(ele.select("td").get(11).text());

				} catch (Exception e) {

				}

				result.add(housingBeiJingCenterPayBean);
			}

		}

		return result;
	}
}
