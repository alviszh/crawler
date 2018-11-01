package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouMedicalInfo;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouPensionInfo;
import com.microservice.dao.entity.crawler.insurance.lanzhou.InsuranceLanZhouUserInfo;

import app.service.InsuranceLanZhouHelpService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Component
public class InsuranceLanZhouParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceLanZhouParser.class);

	public InsuranceLanZhouUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("").getJSONObject("rowSet").getJSONArray("primary").getJSONObject(0);
		InsuranceLanZhouUserInfo insuranceLanZhouUserInfo=new InsuranceLanZhouUserInfo();
		insuranceLanZhouUserInfo.setBirthday(InsuranceLanZhouHelpService.timeStampToDate(jsob.getString("V_GG_INFO_AAC006")));
		insuranceLanZhouUserInfo.setGender(InsuranceLanZhouHelpService.getGender(jsob.getString("V_GG_INFO_AAC004")));
		insuranceLanZhouUserInfo.setIdnum(jsob.getString("V_GG_INFO_AAE135"));
		insuranceLanZhouUserInfo.setInsurcardnum(jsob.getString("V_GG_INFO_AAZ500"));
		insuranceLanZhouUserInfo.setJoinworkdate(InsuranceLanZhouHelpService.timeStampToDate(jsob.getString("V_GG_INFO_AAC007")));
		insuranceLanZhouUserInfo.setName(jsob.getString("V_GG_INFO_AAC003"));
		insuranceLanZhouUserInfo.setNation(InsuranceLanZhouHelpService.getNation(jsob.getString("V_GG_INFO_AAC005")));
		insuranceLanZhouUserInfo.setPernum(jsob.getString("V_GG_INFO_AAC999"));
		insuranceLanZhouUserInfo.setTaskid(taskInsurance.getTaskid().trim());
		return insuranceLanZhouUserInfo;
	}

	public List<InsuranceLanZhouPensionInfo> pensionParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceLanZhouPensionInfo> list=new ArrayList<InsuranceLanZhouPensionInfo>();
		InsuranceLanZhouPensionInfo insuranceLanZhouPensionInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			JSONObject jsob =null;
			for(int i=0;i<size;i++){
				insuranceLanZhouPensionInfo=new InsuranceLanZhouPensionInfo();
				jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceLanZhouPensionInfo.setAccountdate(jsob.getString("V_GG_YZH_BAE441"));
				insuranceLanZhouPensionInfo.setChargebasenum(jsob.getString("V_GG_YZH_AAC150"));
				insuranceLanZhouPensionInfo.setChargemohths(jsob.getString("V_GG_YZH_BAE443"));
				insuranceLanZhouPensionInfo.setSocialavgwage(jsob.getString("V_GG_YZH_AAA010"));
				insuranceLanZhouPensionInfo.setPeraccount(jsob.getString("V_GG_YZH_AAE083"));
				insuranceLanZhouPensionInfo.setPeriodofpay(jsob.getString("V_GG_YZH_AAE002"));
				insuranceLanZhouPensionInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceLanZhouPensionInfo.setTotalamount(jsob.getString("V_GG_YZH_HJ"));
				insuranceLanZhouPensionInfo.setUnitaccount(jsob.getString("V_GG_YZH_AAE081"));
				list.add(insuranceLanZhouPensionInfo);
			}
		}else{
			list=null;
		}
		return list;
	}

	public List<InsuranceLanZhouMedicalInfo> medicalParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceLanZhouMedicalInfo> list=new ArrayList<InsuranceLanZhouMedicalInfo>();
		InsuranceLanZhouMedicalInfo insuranceLanZhouMedicalInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			JSONObject jsob =null;
			for(int i=0;i<size;i++){
				insuranceLanZhouMedicalInfo=new InsuranceLanZhouMedicalInfo();
				jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceLanZhouMedicalInfo.setAccountsettleperiod(jsob.getString("V_GG_YILSZ_AAE150"));
				insuranceLanZhouMedicalInfo.setChargebasenum(jsob.getString("V_GG_YILSZ_AAE180"));
				insuranceLanZhouMedicalInfo.setChargesettleperiod(jsob.getString("V_GG_YILSZ_AAE003"));
				insuranceLanZhouMedicalInfo.setIdnum(jsob.getString("V_GG_YILSZ_AAE135"));
				insuranceLanZhouMedicalInfo.setName(jsob.getString("V_GG_YILSZ_AAC003"));
				insuranceLanZhouMedicalInfo.setPercharge(jsob.getString("V_GG_YILSZ_GRJF"));
				insuranceLanZhouMedicalInfo.setPernum(jsob.getString("V_GG_YILSZ_AAC999"));
				insuranceLanZhouMedicalInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceLanZhouMedicalInfo.setTotalamount(jsob.getString("V_GG_YILSZ_HEJI"));
				insuranceLanZhouMedicalInfo.setUnitcharge(jsob.getString("V_GG_YILSZ_DWJF"));
				insuranceLanZhouMedicalInfo.setUnitname(jsob.getString("V_GG_YILSZ_AAB004"));
				list.add(insuranceLanZhouMedicalInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
}
