package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinGongShang;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinShengYu;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinShiYe;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinUserInfo;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinYangLao;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinYiLiao;

import app.commontracerlog.TracerLog;
@Component
public class InsuranceYuLinParser {
	@Autowired
	private TracerLog tracer;
	
	public InsuranceYuLinUserInfo getuserinfo(String contentAsString2, String taskid) {
		InsuranceYuLinUserInfo userinfo = null;
		try {
			Document document = Jsoup.parse(contentAsString2);
			String val = document.select("input[readonly='readonly']").get(0).val();//姓名
			String val2 = document.select("input[readonly='readonly']").get(1).val();//性别
			String val3 = document.select("input[readonly='readonly']").get(2).val();//民族
			String val4 = document.select("input[readonly='readonly']").get(3).val();//身份证类型
			String val5 = document.select("input[readonly='readonly']").get(4).val();//公民身份号码
			String val6 = document.select("input[readonly='readonly']").get(5).val();//个人编号
			String val7 = document.select("input[readonly='readonly']").get(6).val();//户口性质
			String val8 = document.select("input[readonly='readonly']").get(7).val();//户口所在地行政区域
			String val9 = document.select("input[readonly='readonly']").get(8).val();//户口所在地详细地址
			String val10 = document.select("input[readonly='readonly']").get(9).val();//常住地行政区域
			String val11 = document.select("input[readonly='readonly']").get(10).val();//邮政编码
			String val12 = document.select("input[readonly='readonly']").get(11).val();//常住地详细地址
			String val13 = document.select("input[readonly='readonly']").get(12).val();//移动电话
			String val14 = document.select("input[readonly='readonly']").get(13).val();//办公电话
			String val15 = document.select("input[readonly='readonly']").get(14).val();//传真
			String val16 = document.select("input[readonly='readonly']").get(15).val();//出生日期 
			String val17 = document.select("input[readonly='readonly']").get(16).val();//参加工作日期
			String val18 = document.select("input[readonly='readonly']").get(17).val();//人员资料登记状态
			String val19 = document.select("input[readonly='readonly']").get(18).val();//离退休状态
			String val20 = document.select("input[readonly='readonly']").get(19).val();//离退休日期
			String val21 = document.select("input[readonly='readonly']").get(20).val();//医疗人员类别
			String val22 = document.select("input[readonly='readonly']").get(21).val();//享受医疗退休待遇日期
			String val23 = document.select("input[readonly='readonly']").get(22).val();//社保卡号
			
			userinfo = new InsuranceYuLinUserInfo(taskid,val,val2,val3,val4,val5,val6,
					val7,val8,val9,val10,val11,val12,val13,val14,val15,val16,val17,val18,
					val19,val20,val21,val22,val23);
			
		} catch (Exception e) {
			tracer.addTag("insuranceyulinService.crawler.getuserinfo.error", e.getMessage());
			return userinfo;
		}
		
		return userinfo;
	}

	public List<InsuranceYuLinYangLao> getyanglaoMsg(String taskid, String html, int j) {
		List<InsuranceYuLinYangLao> list = null;
		try {
			Document parse = Jsoup.parse(html);
			Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
			list = new ArrayList<>();
			for (int k = 1; k < elementsBy.size(); k++) {
				String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
				String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
				String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
				String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
				String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
				String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
				String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
				String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
				String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
				String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
				System.out.println("-----------------------------第"+j+"页数据----------------------------");
				System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
						+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
						+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				InsuranceYuLinYangLao yanglao = new InsuranceYuLinYangLao(taskid,string,string2,string3,string4,string5,string6,string7,string8,string9,string10);
				list.add(yanglao);
			}
		} catch (Exception e) {
			return list;
		}
		
		return list;
	}

	public List<InsuranceYuLinYiLiao> getyiliaoMsg(String taskid, String html, int j) {
		List<InsuranceYuLinYiLiao> list = null;
		try {
			Document parse = Jsoup.parse(html);
			Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
			list = new ArrayList<>();
			for (int k = 1; k < elementsBy.size(); k++) {
				String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
				String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
				String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
				String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
				String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
				String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
				String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
				String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
				String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
				String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
				System.out.println("-----------------------------第"+j+"页数据----------------------------");
				System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
						+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
						+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				InsuranceYuLinYiLiao yiliao = new InsuranceYuLinYiLiao(taskid,string,string2,string3,string4,string5,string6,string7,string8,string9,string10);
				list.add(yiliao);
			}
		} catch (Exception e) {
			return list;
		}
		
		return list;
	}

	public List<InsuranceYuLinGongShang> getgonshangMsg(String taskid, String html, int j) {
		List<InsuranceYuLinGongShang> list = null;
		try {
			Document parse = Jsoup.parse(html);
			Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
			list = new ArrayList<>();
			for (int k = 1; k < elementsBy.size(); k++) {
				String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
				String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
				String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
				String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
				String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
				String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
				String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
				String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
				String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
				String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
				System.out.println("-----------------------------第"+j+"页数据----------------------------");
				System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
						+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
						+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				InsuranceYuLinGongShang gs = new InsuranceYuLinGongShang(taskid,string,string2,string3,string4,string5,string6,string7,string8,string9,string10);
				list.add(gs);
			}
		} catch (Exception e) {
			return list;
		}
		
		return list;
	}

	public List<InsuranceYuLinShiYe> getshiyeMsg(String taskid, String html, int j) {
		List<InsuranceYuLinShiYe> list = null;
		try {
			Document parse = Jsoup.parse(html);
			Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
			list = new ArrayList<>();
			for (int k = 1; k < elementsBy.size(); k++) {
				String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
				String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
				String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
				String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
				String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
				String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
				String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
				String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
				String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
				String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
				System.out.println("-----------------------------第"+j+"页数据----------------------------");
				System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
						+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
						+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				InsuranceYuLinShiYe sy = new InsuranceYuLinShiYe(taskid,string,string2,string3,string4,string5,string6,string7,string8,string9,string10);
				list.add(sy);
			}
		} catch (Exception e) {
			return list;
		}
		
		return list;
	}

	public List<InsuranceYuLinShengYu> getshengyuMsg(String taskid, String html, int j) {
		List<InsuranceYuLinShengYu> list = null;
		try {
			Document parse = Jsoup.parse(html);
			Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
			list = new ArrayList<>();
			for (int k = 1; k < elementsBy.size(); k++) {
				String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
				String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
				String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
				String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
				String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
				String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
				String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
				String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
				String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
				String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
				System.out.println("-----------------------------第"+j+"页数据----------------------------");
				System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
						+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
						+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				InsuranceYuLinShengYu sy = new InsuranceYuLinShengYu(taskid,string,string2,string3,string4,string5,string6,string7,string8,string9,string10);
				list.add(sy);
			}
		} catch (Exception e) {
			return list;
		}
		
		return list;
	}

	
}
