package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinPaymentBase;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinSoldout;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinStatusChanges;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinUserInfo;
import com.microservice.dao.entity.crawler.insurance.sz.jilin.InsuranceSZJiLinYangLaoInfo;

import app.commontracerlog.TracerLog;

@Component
public class InsuranceSZJiLinParser {
	@Autowired
	private TracerLog tracer;

	public InsuranceSZJiLinUserInfo getUserInfo(String html, InsuranceRequestParameters parameter) {
		InsuranceSZJiLinUserInfo insuranceBaiShanUserInfo = null;
		try {
			Document doc = Jsoup.parse(html);
			Element element = doc.getElementsByTag("table").get(0);
			String text = element.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text();//姓名
			String text2 = element.getElementsByTag("tr").get(0).getElementsByTag("td").get(3).text();//性别
			String text3 = element.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();//证件号码
			String text4 = element.getElementsByTag("tr").get(1).getElementsByTag("td").get(3).text();//出生日期
			String text5 = element.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text();//个人身份
			String text6 = element.getElementsByTag("tr").get(2).getElementsByTag("td").get(3).text();//用工形式
			String text7 = element.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text();//人员状态
			String text8 = element.getElementsByTag("tr").get(3).getElementsByTag("td").get(3).text();//农民工标识

			Element element2 = doc.getElementsByTag("table").get(1);
			String text9 = element2.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text();//单位编号
			String text10 = element2.getElementsByTag("tr").get(0).getElementsByTag("td").get(3).text();//单位名称
			String text11 = element2.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();//养老建账时间
			String text12 = element2.getElementsByTag("tr").get(1).getElementsByTag("td").get(3).text();//参工时间
			String text13 = element2.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text();//养老参保时间
			String text14 = element2.getElementsByTag("tr").get(2).getElementsByTag("td").get(3).text();//失业参保时间
			String text15 = element2.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text();//养老参保状态
			String text16 = element2.getElementsByTag("tr").get(3).getElementsByTag("td").get(3).text();//失业参保状态
			String text17 = element2.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text();//养老缴费状态
			String text18 = element2.getElementsByTag("tr").get(4).getElementsByTag("td").get(3).text();//失业缴费状态

			insuranceBaiShanUserInfo = new InsuranceSZJiLinUserInfo(
					parameter.getTaskId(),text,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17,text18);
		} catch (Exception e) {
			tracer.addTag("action.baishan.getuserinfo", "解析错误："+e.toString());
			return insuranceBaiShanUserInfo;
		}
		return insuranceBaiShanUserInfo;
	}

	public List<InsuranceSZJiLinPaymentBase> getPaymentBase(String html, InsuranceRequestParameters parameter) {
		List<InsuranceSZJiLinPaymentBase> list = null;
		try {
			Document document = Jsoup.parse(html);
			Element elementById = document.getElementById("contentTable");
			Elements byTag = elementById.getElementsByTag("tr");
			list = new ArrayList<InsuranceSZJiLinPaymentBase>();
			for(int i=1;i<byTag.size();i++){
				String text = byTag.get(i).getElementsByTag("td").get(0).text();//姓名
				String text2 = byTag.get(i).getElementsByTag("td").get(1).text();//身份证号码
				String text3 = byTag.get(i).getElementsByTag("td").get(2).text();//开始期号
				String text4 = byTag.get(i).getElementsByTag("td").get(3).text();//结束期号
				String text5 = byTag.get(i).getElementsByTag("td").get(4).text();//申报工资
				String text6 = byTag.get(i).getElementsByTag("td").get(5).text();//缴费基数
				InsuranceSZJiLinPaymentBase insuranceBaiShanPaymentBase = new InsuranceSZJiLinPaymentBase(
						parameter.getTaskId(),text,text2,text3,text4,text5,text6);
				list.add(insuranceBaiShanPaymentBase);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("action.baishan.getPaymentBase", "解析错误："+e.toString());
			return list;
		}

	}

	public List<InsuranceSZJiLinSoldout> getSoldout(String html, InsuranceRequestParameters parameter) {
		List<InsuranceSZJiLinSoldout> list = null;
		try {
			Document parse = Jsoup.parse(html);
			Elements elementsByTag = parse.getElementsByClass("table").get(0).getElementsByTag("tr");
			list = new ArrayList<InsuranceSZJiLinSoldout>();
			for(int i=1;i<elementsByTag.size();i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//年度
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//期号
				InsuranceSZJiLinSoldout insuranceBaiShanSoldout = new InsuranceSZJiLinSoldout(parameter.getTaskId(), text, text2);
				list.add(insuranceBaiShanSoldout);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("action.baishan.getSoldout", "解析错误："+e.toString());
			return list;
		}


	}

	public List<InsuranceSZJiLinStatusChanges> getStatusChanges(String html, InsuranceRequestParameters parameter) {
		List<InsuranceSZJiLinStatusChanges> list = null;
		try {
			Document doc = Jsoup.parse(html);
			Elements elementsByTag = doc.getElementById("contentTable").getElementsByTag("tr");
			list = new ArrayList<InsuranceSZJiLinStatusChanges>();
			for(int i=1;i<elementsByTag.size();i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//个人编号
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//身份证号
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//姓名
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//单位编号
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//单位名称
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(5).text();//变更类型
				String text7 = elementsByTag.get(i).getElementsByTag("td").get(6).text();//变更时间
				String text8 = elementsByTag.get(i).getElementsByTag("td").get(7).text();//变更原因
				InsuranceSZJiLinStatusChanges insuranceBaiShanStatusChanges = new InsuranceSZJiLinStatusChanges(
						parameter.getTaskId(),text,text2,text3,text4,text5,text6,text7,text8);
				list.add(insuranceBaiShanStatusChanges);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("action.baishan.getStatusChanges", "解析错误："+e.toString());
			return list;
		}
	}

	public List<InsuranceSZJiLinYangLaoInfo> getyanglaoInfo(String html, InsuranceRequestParameters parameter) {
		List<InsuranceSZJiLinYangLaoInfo> list = null;
		try {
			Document document = Jsoup.parse(html);
			Elements elementsByTag = document.getElementById("contentTable").getElementsByTag("tr");
			list = new ArrayList<InsuranceSZJiLinYangLaoInfo>();
			for(int i=1;i<elementsByTag.size()-2;i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//姓名
				String text1 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//身份证号码
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//费款所属期
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//险种类型
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//缴费类型
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(5).text();//缴费基数
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(6).text();//个人缴费金额
				String text7 = elementsByTag.get(i).getElementsByTag("td").get(7).text();//个人缴费标志
				String text8 = elementsByTag.get(i).getElementsByTag("td").get(8).text();//个人缴费到账日期
				String text9 = elementsByTag.get(i).getElementsByTag("td").get(9).text();//单位缴费划账户金额
				String text10 = elementsByTag.get(i).getElementsByTag("td").get(10).text();//单位缴费划统筹金额
				String text11 = elementsByTag.get(i).getElementsByTag("td").get(11).text();//单位缴费标志
				String text12 = elementsByTag.get(i).getElementsByTag("td").get(12).text();//单位缴费到账日期
				String text13 = elementsByTag.get(i).getElementsByTag("td").get(13).text();//缴费月数
				String text14 = elementsByTag.get(i).getElementsByTag("td").get(14).text();//缴费单位编号
				String text15 = elementsByTag.get(i).getElementsByTag("td").get(15).text();//缴费单位名称

				InsuranceSZJiLinYangLaoInfo insuranceSZJiLinYaoLaoInfo = new InsuranceSZJiLinYangLaoInfo(
						parameter.getTaskId(),text,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15);
				list.add(insuranceSZJiLinYaoLaoInfo);
			}

		} catch (Exception e) {
			tracer.addTag("action.baishan.getyanglaoInfo", "解析错误："+e.toString());
			return list;
		}
		return list;
	}

	public List<InsuranceSZJiLinShiYeInfo> getshiyeInfo(String html, InsuranceRequestParameters parameter) {
		List<InsuranceSZJiLinShiYeInfo> list = null;
		try {
			Document document = Jsoup.parse(html);
			list = new ArrayList<InsuranceSZJiLinShiYeInfo>();
			Elements elementsByTag = document.getElementById("contentTable").getElementsByTag("tr");
			for(int i=1;i<elementsByTag.size()-2;i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//姓名
				String text1 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//身份证号码
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//费款所属期
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//险种类型
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//缴费类型
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(5).text();//缴费基数
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(6).text();//个人缴费金额
				String text7 = elementsByTag.get(i).getElementsByTag("td").get(7).text();//个人缴费标志
				String text8 = elementsByTag.get(i).getElementsByTag("td").get(8).text();//个人缴费到账日期
				String text9 = elementsByTag.get(i).getElementsByTag("td").get(9).text();//单位缴费划账户金额
				String text10 = elementsByTag.get(i).getElementsByTag("td").get(10).text();//单位缴费划统筹金额
				String text11 = elementsByTag.get(i).getElementsByTag("td").get(11).text();//单位缴费标志
				String text12 = elementsByTag.get(i).getElementsByTag("td").get(12).text();//单位缴费到账日期
				String text13 = elementsByTag.get(i).getElementsByTag("td").get(13).text();//缴费月数
				String text14 = elementsByTag.get(i).getElementsByTag("td").get(14).text();//缴费单位编号
				String text15 = elementsByTag.get(i).getElementsByTag("td").get(15).text();//缴费单位名称
				InsuranceSZJiLinShiYeInfo insuranceSZJiLinShiYeInfo = new InsuranceSZJiLinShiYeInfo(
						parameter.getTaskId(),text,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15);
				list.add(insuranceSZJiLinShiYeInfo);
			}
		} catch (Exception e) {
			tracer.addTag("action.baishan.getshiyeInfo", "解析错误："+e.toString());
			return list;
		}
		
		return list;
	}

}
