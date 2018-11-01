package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouRecords;
import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouUserInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceHuzhouParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	public InsuranceHuzhouUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) throws Exception{
		tracer.addTag("htmlParserForUserInfo.html","<xmp>"+html+"</xmp>");
		Document doc = Jsoup.parse(html);
		String useraccount = getNextLabelByKeyword(doc, "人员编号");
		String username = getNextLabelByKeyword(doc, "姓名");
		String type = getNextLabelByKeyword(doc, "证件类型");
		String idnum = getNextLabelByKeyword(doc, "身份证");
		String sex = getNextLabelByKeyword(doc, "性别");
		
		String birthdate = getNextLabelByKeyword(doc, "出生日期");
		String companyaccount = getNextLabelByKeyword(doc, "单位编码");
		String companyname = getNextLabelByKeyword(doc, "单位编码");
		String firstdate = getNextLabelByKeyword(doc, "开始参保时间");
		String state = getNextLabelByKeyword(doc, "参保状态");
		String contactnum = getNextLabelByKeyword(doc, "联系电话");
		String telphone = getNextLabelByKeyword(doc, "手机号码");
		String planarea = getNextLabelByKeyword(doc, "统筹区");
		String address = getNextLabelByKeyword(doc, "地址");
		InsuranceHuzhouUserInfo userInfo = new  InsuranceHuzhouUserInfo(useraccount, username, type, idnum, sex,
				birthdate, companyaccount, companyname, firstdate, state,
				contactnum, telphone, planarea, address, taskInsurance.getTaskid());
		return userInfo;
	}
	
	/**
	 * @Des 根据获取的获取缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	public List<InsuranceHuzhouBasicinfo> htmlParserForBasicinfo(String html, TaskInsurance taskInsurance)
			throws Exception {
		List<InsuranceHuzhouBasicinfo> basicinfoList = new ArrayList<InsuranceHuzhouBasicinfo>();
		if (null != html & html.contains("data")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String listStr = list1ArrayObjs.getString("data");			
			JSONArray listArray = JSONArray.fromObject(listStr);
			for (int i = 0; i < listArray.size(); i++) {
				 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
				 String num=listArrayObjs.getString("rn");//序号
				 String planarea=listArrayObjs.getString("aaa027");//统筹区
				 String type=listArrayObjs.getString("aae140");//险种信息
				 String state=listArrayObjs.getString("aac008"); // 参保情况
				 String paystate=listArrayObjs.getString("aac031");// 缴费情况
				 String payinfo=listArrayObjs.getString("aaz289"); // 缴费档次
				 String paybase=listArrayObjs.getString("aae180"); // 缴费基数
				 String startdate=listArrayObjs.getString("aae030"); ; // 本次参保开始时间
				 InsuranceHuzhouBasicinfo  basicinfo=new InsuranceHuzhouBasicinfo(num, planarea, type, state, paystate,
							payinfo, paybase, startdate, taskInsurance.getTaskid());
				 basicinfoList.add(basicinfo);
			}
		}
		return basicinfoList;
	}
	
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	public List<InsuranceHuzhouRecords> htmlParserForRecordsList(String html, TaskInsurance taskInsurance)
			throws Exception {
		List<InsuranceHuzhouRecords> recordsList = new ArrayList<InsuranceHuzhouRecords>();
		if (null != html & html.contains("data")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);	
			String listStr = list1ArrayObjs.getString("data");			
			JSONArray listArray = JSONArray.fromObject(listStr);
			for (int i = 0; i < listArray.size(); i++) {
				 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
				 String planarea=listArrayObjs.getString("aaa027");//统筹区
				 String companyname=listArrayObjs.getString("ace020"); // 单位名称
				 String type=listArrayObjs.getString("ace021");// 险种名称
				 String payBase=listArrayObjs.getString("ace022"); // 缴费基数
				 String paymonth=listArrayObjs.getString("ace023"); // 缴费年月
				 String payamount=listArrayObjs.getString("ace025"); // 缴费金额
				 String companyamount=listArrayObjs.getString("ace202"); // 单位应缴金额
				 String personamount=listArrayObjs.getString("ace203"); // 个人应缴金额
				 String accountmonth="";
				 if (listArrayObjs.getString("ace026")!=null) {
				   accountmonth=listArrayObjs.getString("ace026");// 本次参保开始时间
				}				
				 String sign=listArrayObjs.getString("ace027");
				 InsuranceHuzhouRecords  records=new InsuranceHuzhouRecords(planarea,companyname,type,payBase,paymonth,
							 payamount,companyamount,personamount,sign,
							 accountmonth,taskInsurance.getTaskid());
				 recordsList.add(records);
			}
		}
		return recordsList;
	}
	private  String getNextLabelByKeyword(Document document, String keyword) {
        Elements es = document.select("th:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if (null != nextElement) {
                return nextElement.text();
            }
        }
        return null;
    }
}
