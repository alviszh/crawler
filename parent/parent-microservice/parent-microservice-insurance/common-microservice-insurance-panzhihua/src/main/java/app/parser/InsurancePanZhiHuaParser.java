package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.panzhihua.InsurancePanZhiHuaGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.panzhihua.InsurancePanZhiHuaShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.panzhihua.InsurancePanZhiHuaShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.panzhihua.InsurancePanZhiHuaUserInfo;
import com.microservice.dao.entity.crawler.insurance.panzhihua.InsurancePanZhiHuaYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.panzhihua.InsurancePanZhiHuaYiLiaoInfo;

import app.commontracerlog.TracerLog;

@Component
public class InsurancePanZhiHuaParser {
	@Autowired
	private TracerLog tracer;

	public InsurancePanZhiHuaUserInfo getuserinfo(String taskId, String html) {
		InsurancePanZhiHuaUserInfo userinfo = null;
		try {
			Elements byTag = Jsoup.parse(html).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//姓名
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//个人编号
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//单位编号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//单位名称
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//身份证号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//出生日期
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//参加工作时间
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//联系电话
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//户口所在地
			userinfo = new InsurancePanZhiHuaUserInfo(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9);
			System.out.println("姓名："+text+"\r个人编号:"+text2+"\r单位编号:"+text3+"\r单位名称:"
					+text4+"\r身份证号:"+text5+"\r出生日期:"+text6+"\r参加工作时间:"+text7+"\r联系电话:"+text8+"\r户口所在地:"+text9);
		} catch (Exception e) {
			tracer.addTag("getuserinfo.parsererr", taskId);
			return userinfo;
		}
		return userinfo;
	}

	public List<InsurancePanZhiHuaYangLaoInfo> getyanglaoMsg(String html, String taskId) {
		List<InsurancePanZhiHuaYangLaoInfo> list = null;
		try {
			list = new ArrayList<>();
			Elements byTag = Jsoup.parse(html).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//单位编号
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//单位名称
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//首次参保时间
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//建账时间
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text10 = byTag.get(10).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text11 = byTag.get(11).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			InsurancePanZhiHuaYangLaoInfo info =  new InsurancePanZhiHuaYangLaoInfo(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,"养老保险");
			list.add(info);
		} catch (Exception e) {
			tracer.addTag("getyanglaoMsg.parsererr", taskId);
			return list;
		}
		return list;
	}

	public List<InsurancePanZhiHuaYiLiaoInfo> getyiliaoMsg(String taskId, String html) {
		List<InsurancePanZhiHuaYiLiaoInfo> list = null;
		try {
			list = new ArrayList<>();
			Elements byTag = Jsoup.parse(html).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//单位编号
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//单位名称
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//本年缴费月数
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text10 = byTag.get(10).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			String text11 = byTag.get(11).getElementsByTag("td").get(1).text().trim();//异地登记信息
			InsurancePanZhiHuaYiLiaoInfo info = new InsurancePanZhiHuaYiLiaoInfo(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,"医疗保险");
			list.add(info);
		} catch (Exception e) {
			tracer.addTag("getyiliaoMsg.parsererr", taskId);
			return list;
		}
		return list;
	}

	public List<InsurancePanZhiHuaGongShangInfo> getgongshangmsg(String taskId, String html) {
		List<InsurancePanZhiHuaGongShangInfo> list = null;
		try {
			list = new ArrayList<>();
			Elements byTag = Jsoup.parse(html).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//缴费类型
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			
			InsurancePanZhiHuaGongShangInfo info = new InsurancePanZhiHuaGongShangInfo(taskId,text,text2,text3,text4,text5,text6,text7,text8,"工伤保险");
			list.add(info);
		} catch (Exception e) {
			tracer.addTag("getgongshangmsg.parsererr", taskId);
			return list;
		}
		return list;
	}

	public List<InsurancePanZhiHuaShiYeInfo> getshiyemsg(String taskId, String html) {
		List<InsurancePanZhiHuaShiYeInfo> list = null;
		try {
			list = new ArrayList<>();
			Elements byTag = Jsoup.parse(html).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//农民工标识
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//本次失业享受失业保险待遇月数
			String text10 = byTag.get(10).getElementsByTag("td").get(1).text().trim();//失业保险金剩余月数
			InsurancePanZhiHuaShiYeInfo shiye = new InsurancePanZhiHuaShiYeInfo(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9,text10,"失业保险");
			list.add(shiye);
		} catch (Exception e) {
			tracer.addTag("getshiyemsg.parsererr", taskId);
			return list;
		}
		return list;
	}

	public List<InsurancePanZhiHuaShengYuInfo> getshengyuMsg(String taskId, String html) {
		List<InsurancePanZhiHuaShengYuInfo> list = null;
		try {
			list = new ArrayList<>();
			Elements byTag = Jsoup.parse(html).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			InsurancePanZhiHuaShengYuInfo info = new InsurancePanZhiHuaShengYuInfo(taskId,text,text2,text3,text4,text5,text6,text7,"生育保险");
			list.add(info);
		} catch (Exception e) {
			tracer.addTag("getshengyuMsg.parsererr", taskId);
			return list;
		}
		return list;
	}
	
	
}
