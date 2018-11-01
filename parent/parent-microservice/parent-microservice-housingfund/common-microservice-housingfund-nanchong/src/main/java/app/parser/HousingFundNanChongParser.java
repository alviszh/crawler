package app.parser;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nanchong.HousingNanChongFlowInfo;
import com.microservice.dao.entity.crawler.housing.nanchong.HousingNanChongUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundNanChongParser {

	public HousingNanChongUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		String taskid = taskHousing.getTaskid().trim();
		String name = subStringTxt(html,"职工姓名","个人账号").trim();
		String peraccnum = subStringTxt(html,"个人账号","身份证号").trim();
		String idnum = subStringTxt(html,"身份证号","单位名称").trim();
		String unitname = subStringTxt(html,"单位名称","单位账号").trim();
		String unitaccnum = subStringTxt(html,"单位账号","开户日期").trim();
		String opendate = subStringTxt(html,"开户日期","缴至年月").trim();
		String paytoyearmonth = subStringTxt(html,"缴至年月","公积金卡号").trim();
		String cardno = subStringTxt(html,"公积金卡号","月均工资").trim();
		String monthavgwages = subStringTxt(html,"月均工资","缴存比例").trim();
		String prop = subStringTxt(html,"缴存比例","月缴存额").trim();
		String monthcharge = subStringTxt(html,"月缴存额","余额").trim();
		String balance = subStringTxt(html,"余额","职工状态").trim();
		String employeestatus = subStringTxt(html,"职工状态","是否冻结").trim();
		String isfreeze = subStringTxt(html,"是否冻结","是否贷款").trim();
		String isloan = subStringTxt(html,"是否贷款","是否月对冲").trim();
		String ismonthhedge = subStringTxt(html,"是否月对冲","打印日期").trim();
		HousingNanChongUserInfo housingNanChongUserInfo=new HousingNanChongUserInfo(taskid, name, peraccnum, idnum, unitname, unitaccnum, opendate, paytoyearmonth, cardno, monthavgwages, prop, monthcharge, balance, employeestatus, isfreeze, isloan, ismonthhedge);
		return housingNanChongUserInfo;
	}

	public List<HousingNanChongFlowInfo> flowInfoParser(String html, TaskHousing taskHousing) {
		List<HousingNanChongFlowInfo> list=new ArrayList<HousingNanChongFlowInfo>();
		String totalCount = JSONObject.fromObject(html).getString("totalcount");
		int size = Integer.parseInt(totalCount);  //获取总记录数
		HousingNanChongFlowInfo housingNanChongFlowInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("results");
		JSONObject jsob=new JSONObject();
		if(size>0){
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				housingNanChongFlowInfo=new HousingNanChongFlowInfo();		
				housingNanChongFlowInfo.setBalance(jsob.getString("ye"));
				housingNanChongFlowInfo.setDate(jsob.getString("rq"));
				housingNanChongFlowInfo.setExtract(jsob.getString("jfje"));
				housingNanChongFlowInfo.setSummary(jsob.getString("zy"));
				housingNanChongFlowInfo.setStorage(jsob.getString("dfje"));
				housingNanChongFlowInfo.setTaskid(taskHousing.getTaskid().trim());
				list.add(housingNanChongFlowInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
	
	//在指定的字段名之间截取，截取出来的信息就是想要的信息 （最后一个是否月对冲无法用如下方法，就单独写）
	public String subStringTxt(String originalStr,String strStart,String strEnd){
		String subResult = originalStr.substring(originalStr.lastIndexOf(strStart)+strStart.length(),originalStr.indexOf(strEnd));
		return subResult;
	}
}
