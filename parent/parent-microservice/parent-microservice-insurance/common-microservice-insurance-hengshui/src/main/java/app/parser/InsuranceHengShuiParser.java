package app.parser;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.hengshui.InsuranceHengShuiChargeDetail;
import com.microservice.dao.entity.crawler.insurance.hengshui.InsuranceHengShuiUserInfo;

import app.service.InsuranceHengShuiHelpService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceHengShuiParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceHengShuiParser.class);
	public InsuranceHengShuiUserInfo userInfoParser(TaskInsurance taskInsurance, String html, String html2) {
		JSONObject jsob=JSONObject.fromObject(html);
		InsuranceHengShuiUserInfo insuranceHengShuiUserInfo=new InsuranceHengShuiUserInfo();
		insuranceHengShuiUserInfo.setBirthday(jsob.getString("csrq"));
		insuranceHengShuiUserInfo.setContactnum(jsob.getString("bgdh"));
		insuranceHengShuiUserInfo.setEmail(jsob.getString("dzyx"));
		insuranceHengShuiUserInfo.setFax(jsob.getString("cz"));
		insuranceHengShuiUserInfo.setFlexibleworkmark(InsuranceHengShuiHelpService.getWorkMark(jsob.getString("bz")));
		insuranceHengShuiUserInfo.setGender(InsuranceHengShuiHelpService.getGender(jsob.getString("xb")));
		insuranceHengShuiUserInfo.setIdnum(jsob.getString("sfzh"));
		insuranceHengShuiUserInfo.setJoinworkdate(jsob.getString("gzrq"));
		insuranceHengShuiUserInfo.setName(jsob.getString("xm"));
		insuranceHengShuiUserInfo.setNation(InsuranceHengShuiHelpService.getNation(jsob.getString("mz")));
		insuranceHengShuiUserInfo.setPercategory(InsuranceHengShuiHelpService.getPerCategory(jsob.getString("rylb")));
		insuranceHengShuiUserInfo.setPerinsurnum(jsob.getString("grbh"));
		insuranceHengShuiUserInfo.setPostalcode(jsob.getString("yzbm"));
		insuranceHengShuiUserInfo.setTaskid(taskInsurance.getTaskid());
		insuranceHengShuiUserInfo.setWorkingform(InsuranceHengShuiHelpService.getWorkForm(jsob.getString("ygxs")));
		
		jsob=JSONObject.fromObject(html2);
		insuranceHengShuiUserInfo.setAddress(jsob.getString("address"));
		insuranceHengShuiUserInfo.setMobilenum(jsob.getString("mobile"));
		insuranceHengShuiUserInfo.setInsurexpirydate(jsob.getString("validityday"));
		insuranceHengShuiUserInfo.setInsurnum(jsob.getString("sbkh"));
		insuranceHengShuiUserInfo.setUnitname(jsob.getString("companyname"));
		return insuranceHengShuiUserInfo;
	}
	//养老保险的解析方式
	public List<InsuranceHengShuiChargeDetail> pensionParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceHengShuiChargeDetail> list=new ArrayList<InsuranceHengShuiChargeDetail>();
		InsuranceHengShuiChargeDetail insuranceHengShuiChargeDetail=null;
		JSONObject jsob=JSONObject.fromObject(html);
		String total =jsob.getString("total");
		if(!total.equals("0")){  //有数据可供采集
			int totalCount = Integer.parseInt(total);
			JSONArray jsonArray = jsob.getJSONArray("content");
			for(int i=0;i<totalCount;i++){
				//这句代码不能写在此处，不然第二次执行for循环的时候，jsob已经是jsonArray中的第一个了，循环第二次，会报错：conten不是jsonArray
//				jsob=jsob.getJSONArray("content").getJSONObject(i);   
				jsob=jsonArray.getJSONObject(i);
				insuranceHengShuiChargeDetail=new InsuranceHengShuiChargeDetail();
				insuranceHengShuiChargeDetail.setChargebasenum(jsob.getString("aae180"));
				insuranceHengShuiChargeDetail.setChargeflag(InsuranceHengShuiHelpService.getChargeFlag(jsob.getString("bae152")));
				insuranceHengShuiChargeDetail.setChargemonths(jsob.getString("aae202"));
				insuranceHengShuiChargeDetail.setChargeyearmonth(jsob.getString("aae003"));
				insuranceHengShuiChargeDetail.setDelimitaccount(jsob.getString("aae021"));
				insuranceHengShuiChargeDetail.setInsurtype(InsuranceHengShuiHelpService.getInsurType(jsob.getString("aae140")));
				insuranceHengShuiChargeDetail.setPercharge(InsuranceHengShuiHelpService.chargeTransForm(jsob.getString("aae082")));
				insuranceHengShuiChargeDetail.setShouldcharge(jsob.getString("aae020"));
				insuranceHengShuiChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceHengShuiChargeDetail.setUnitcharge(InsuranceHengShuiHelpService.chargeTransForm(jsob.getString("aae080")));
				list.add(insuranceHengShuiChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
	//医疗保险的解析方式
	public List<InsuranceHengShuiChargeDetail> medicalParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceHengShuiChargeDetail> list=new ArrayList<InsuranceHengShuiChargeDetail>();
		InsuranceHengShuiChargeDetail insuranceHengShuiChargeDetail=null;
		JSONObject jsob=JSONObject.fromObject(html);
		String total =jsob.getString("total");
		if(!total.equals("0")){  //有数据可供采集
			int totalCount = Integer.parseInt(total);
			JSONArray jsonArray = jsob.getJSONArray("content");
			for(int i=0;i<totalCount;i++){
				jsob=jsonArray.getJSONObject(i);
				insuranceHengShuiChargeDetail=new InsuranceHengShuiChargeDetail();
				insuranceHengShuiChargeDetail.setChargebasenum(jsob.getString("aae018"));
				insuranceHengShuiChargeDetail.setChargeflag(InsuranceHengShuiHelpService.getChargeFlag(jsob.getString("bae152")));
				insuranceHengShuiChargeDetail.setChargeyearmonth(jsob.getString("aae003"));
				insuranceHengShuiChargeDetail.setDelimitaccount(jsob.getString("aae023"));
				insuranceHengShuiChargeDetail.setInsurtype(InsuranceHengShuiHelpService.getInsurType(jsob.getString("aae140")));
				insuranceHengShuiChargeDetail.setPercharge(InsuranceHengShuiHelpService.chargeTransForm(jsob.getString("aae021")));
				insuranceHengShuiChargeDetail.setShouldcharge(jsob.getString("aae020"));
				insuranceHengShuiChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceHengShuiChargeDetail.setUnitcharge(InsuranceHengShuiHelpService.chargeTransForm(jsob.getString("aae022")));
				list.add(insuranceHengShuiChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
	//工伤和失业保险的解析方式
	public List<InsuranceHengShuiChargeDetail> injuryAndUnemploymentParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceHengShuiChargeDetail> list=new ArrayList<InsuranceHengShuiChargeDetail>();
		InsuranceHengShuiChargeDetail insuranceHengShuiChargeDetail=null;
		JSONObject jsob=JSONObject.fromObject(html);
		String total =jsob.getString("total");
		if(!total.equals("0")){  //有数据可供采集
			int totalCount = Integer.parseInt(total);
			JSONArray jsonArray = jsob.getJSONArray("content");
			for(int i=0;i<totalCount;i++){
				jsob=jsonArray.getJSONObject(i);
				insuranceHengShuiChargeDetail=new InsuranceHengShuiChargeDetail();
				insuranceHengShuiChargeDetail.setChargebasenum(jsob.getString("aae180"));
				insuranceHengShuiChargeDetail.setChargeflag(InsuranceHengShuiHelpService.getChargeFlag(jsob.getString("bae152")));
				insuranceHengShuiChargeDetail.setChargemonths(jsob.getString("aae202"));
				insuranceHengShuiChargeDetail.setChargeyearmonth(jsob.getString("aae003"));
				insuranceHengShuiChargeDetail.setInsurtype(InsuranceHengShuiHelpService.getInsurType(jsob.getString("aae140")));
				insuranceHengShuiChargeDetail.setPercharge(InsuranceHengShuiHelpService.chargeTransForm(jsob.getString("aae082")));
				insuranceHengShuiChargeDetail.setShouldcharge(jsob.getString("aae020"));
				insuranceHengShuiChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceHengShuiChargeDetail.setUnitcharge(InsuranceHengShuiHelpService.chargeTransForm(jsob.getString("aae080")));
				list.add(insuranceHengShuiChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
}
