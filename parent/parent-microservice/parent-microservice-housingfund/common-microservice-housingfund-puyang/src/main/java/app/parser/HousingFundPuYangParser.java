package app.parser;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.puyang.HousingPuYangFlowInfo;
import com.microservice.dao.entity.crawler.housing.puyang.HousingPuYangUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundPuYangParser {

	public HousingPuYangUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		String taskid = taskHousing.getTaskid();
		String name = subStringTxt(html,"职工姓名","个人账号").trim();
		String peraccnum = subStringTxt(html,"个人账号","公积金余额").trim();
		String balance =subStringTxt(html,"公积金余额","身份证号").trim();
		String idnum = subStringTxt(html,"身份证号","公积金卡号").trim();
		String cardnum = subStringTxt(html,"公积金卡号","单位名称").trim();
		String unitname = subStringTxt(html,"单位名称","单位账号").trim();
		String unitaccnum = subStringTxt(html,"单位账号","开户日期").trim();
		String opendate = subStringTxt(html,"开户日期","缴至年月").trim();
		String chargetoyearmonth = subStringTxt(html,"缴至年月","月均工资").trim();
		String monthavgwages = subStringTxt(html,"月均工资","单位月缴额").trim();
		String unitmonthcharge = subStringTxt(html,"单位月缴额","个人月缴额").trim();
		String permonthcharge = subStringTxt(html,"个人月缴额","月缴存额").trim();
		String monthcharge = subStringTxt(html,"月缴存额","单位缴交率").trim();
		String unitchargerate = subStringTxt(html,"单位缴交率","个人缴交率").trim();
		String perchargerate = subStringTxt(html,"个人缴交率","上年结转额").trim();
		String lastyearturnover = subStringTxt(html,"上年结转额","本年补缴额").trim();
		String thisyearpayment = subStringTxt(html,"本年补缴额","本年汇缴额").trim();
		String thisyearremittance = subStringTxt(html,"本年汇缴额","个人账户状态").trim();
		String accountstate = subStringTxt(html,"个人账户状态","本年结算利息").trim();
		String thisyearturnoverinterest = subStringTxt(html,"本年结算利息","本年支取额").trim();
		String thisyearextract = subStringTxt(html,"本年支取额","是否冻结").trim();
		String isfreeze = subStringTxt(html,"是否冻结","是否有异地贷").trim();
		String isforeignloan = subStringTxt(html,"是否有异地贷","是否有中心贷").trim();
		String iscenterloan = subStringTxt(html,"是否有中心贷","是否月对冲").trim();
		String ismonthhedge = html.substring(html.indexOf("是否月对冲")+"是否月对冲".length(),html.length()).trim();
		HousingPuYangUserInfo housingPuYangUserInfo=new HousingPuYangUserInfo(taskid, name, peraccnum, balance, idnum, cardnum, unitname, unitaccnum, opendate, chargetoyearmonth, monthavgwages, unitmonthcharge, permonthcharge, monthcharge, unitchargerate, perchargerate, lastyearturnover, thisyearpayment, thisyearremittance, accountstate, thisyearturnoverinterest, thisyearextract, isfreeze, isforeignloan, iscenterloan, ismonthhedge);
		return housingPuYangUserInfo;
	}

	public List<HousingPuYangFlowInfo> flowInfoParser(String html, TaskHousing taskHousing) {
		List<HousingPuYangFlowInfo> list=new ArrayList<HousingPuYangFlowInfo>();
		String totalCount = JSONObject.fromObject(html).getString("totalcount");
		int size = Integer.parseInt(totalCount);  //获取总记录数
		HousingPuYangFlowInfo housingPuYangFlowInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("results");
		JSONObject jsob=new JSONObject();
		if(size>0){
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				housingPuYangFlowInfo=new HousingPuYangFlowInfo();		
				housingPuYangFlowInfo.setBalance(jsob.getString("ye"));
				housingPuYangFlowInfo.setDate(jsob.getString("rq"));
				housingPuYangFlowInfo.setExtract(jsob.getString("jfje"));
				housingPuYangFlowInfo.setSummary(jsob.getString("zy"));
//				housingPuYangFlowInfo.setQrydaterange(jsob.getString(""));
				housingPuYangFlowInfo.setStorage(jsob.getString("dfje"));
				housingPuYangFlowInfo.setTaskid(taskHousing.getTaskid().trim());
				list.add(housingPuYangFlowInfo);
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
