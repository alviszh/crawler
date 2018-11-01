package app.parser;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoChargeDetail;
import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoUserInfo;

import app.service.InsuranceRiZhaoHelpService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class InsuranceRiZhaoParser {
	public static final Logger log = LoggerFactory.getLogger(InsuranceRiZhaoParser.class);
	public InsuranceRiZhaoUserInfo userInfoParser(TaskInsurance taskInsurance, String html) {
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary").getJSONObject(0);
		InsuranceRiZhaoUserInfo insuranceRiZhaoUserInfo=new InsuranceRiZhaoUserInfo();
		insuranceRiZhaoUserInfo.setAdministrativepost(InsuranceRiZhaoHelpService.getAdministrativePost(jsob.getString("AAC020")));
		insuranceRiZhaoUserInfo.setBirthday(InsuranceRiZhaoHelpService.timeStampToDate(jsob.getString("AAC006")));
		insuranceRiZhaoUserInfo.setCadremark(jsob.getString("BAC058"));
		insuranceRiZhaoUserInfo.setCivilservantmark(jsob.getString("BAC059"));
		insuranceRiZhaoUserInfo.setContactnum(jsob.getString("AAE005"));
		insuranceRiZhaoUserInfo.setEducationdegree(InsuranceRiZhaoHelpService.getEducationDegree(jsob.getString("AAC011")));
		insuranceRiZhaoUserInfo.setEmail(jsob.getString("AAE015"));
		insuranceRiZhaoUserInfo.setGender(InsuranceRiZhaoHelpService.getGender(jsob.getString("AAC004")));
		insuranceRiZhaoUserInfo.setHealth(InsuranceRiZhaoHelpService.getHealth(jsob.getString("AAC033")));
		insuranceRiZhaoUserInfo.setHousedetailaddr(jsob.getString("AAB301"));
		insuranceRiZhaoUserInfo.setHouseholdtype(InsuranceRiZhaoHelpService.getHouseholdType(jsob.getString("AAC009")));
		insuranceRiZhaoUserInfo.setIdnum(jsob.getString("AAC002"));
		insuranceRiZhaoUserInfo.setIdtype(jsob.getString("AAC058"));
		insuranceRiZhaoUserInfo.setJoinworkdate(InsuranceRiZhaoHelpService.timeStampToDate(jsob.getString("AAC007")));
		insuranceRiZhaoUserInfo.setLinkman(jsob.getString("AAE004"));
		insuranceRiZhaoUserInfo.setLiveaddress(jsob.getString("AAE006"));
		insuranceRiZhaoUserInfo.setMarriage(InsuranceRiZhaoHelpService.getMarriage(jsob.getString("AAC017")));
		insuranceRiZhaoUserInfo.setMedicalpersoncategory(InsuranceRiZhaoHelpService.getMedicalPersonCategory(jsob.getString("AKC021")));
		insuranceRiZhaoUserInfo.setMigrantworkermark(InsuranceRiZhaoHelpService.getpeasantWorker(jsob.getString("AJA004")));
		insuranceRiZhaoUserInfo.setMilitarypersonnelmark(jsob.getString("BIC147"));
		insuranceRiZhaoUserInfo.setModelworkermark(InsuranceRiZhaoHelpService.getWorkModel(jsob.getString("BAC118")));
		insuranceRiZhaoUserInfo.setName(jsob.getString("AAC003"));
		insuranceRiZhaoUserInfo.setNation(InsuranceRiZhaoHelpService.getNation(jsob.getString("AAC005")));
		insuranceRiZhaoUserInfo.setPeridentity(InsuranceRiZhaoHelpService.getPeridentity(jsob.getString("AAC012")));
		insuranceRiZhaoUserInfo.setPernum(jsob.getString("AAC001"));
		insuranceRiZhaoUserInfo.setPoliticalstatus(jsob.getString("AAC024"));
		insuranceRiZhaoUserInfo.setPostalcode(jsob.getString("AAE007"));
		insuranceRiZhaoUserInfo.setProfessionalgrade(InsuranceRiZhaoHelpService.getProfessionalGrade(jsob.getString("AAC015")));
		insuranceRiZhaoUserInfo.setTaskid(taskInsurance.getTaskid().trim());
		insuranceRiZhaoUserInfo.setUnitname(jsob.getString("AAB004"));
		insuranceRiZhaoUserInfo.setWorkingform(InsuranceRiZhaoHelpService.getWorkForm(jsob.getString("AAC013")));
		return insuranceRiZhaoUserInfo;
	}
	//养老
	public List<InsuranceRiZhaoChargeDetail> pensionParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceRiZhaoChargeDetail> list=new ArrayList<InsuranceRiZhaoChargeDetail>();
		InsuranceRiZhaoChargeDetail insuranceRiZhaoChargeDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			for(int i=0;i<size;i++){
				insuranceRiZhaoChargeDetail=new InsuranceRiZhaoChargeDetail();
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceRiZhaoChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceRiZhaoChargeDetail.setAccountdate(jsob.getString("VPQ_YL_SJ_AAE003"));
				insuranceRiZhaoChargeDetail.setChargebasenum(jsob.getString("VPQ_YL_SJ_BAC121"));
				insuranceRiZhaoChargeDetail.setChargeflag(InsuranceRiZhaoHelpService.chargeFlag(jsob.getString("VPQ_YL_SJ_AAE114")));   //返回的是个代号
				insuranceRiZhaoChargeDetail.setPercharge(jsob.getString("VPQ_YL_SJ_GRYJ"));
				insuranceRiZhaoChargeDetail.setUnitcharge(jsob.getString("VPQ_YL_SJ_DWYI"));
				insuranceRiZhaoChargeDetail.setUnitname(jsob.getString("VPQ_YL_SJ_AAB004"));
				insuranceRiZhaoChargeDetail.setUnitoverallcharge(jsob.getString("VPQ_YL_SJ_DWTC"));
				insuranceRiZhaoChargeDetail.setInsurtype("养老保险");
				list.add(insuranceRiZhaoChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
	
	//工伤
	public List<InsuranceRiZhaoChargeDetail> injuryParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceRiZhaoChargeDetail> list=new ArrayList<InsuranceRiZhaoChargeDetail>();
		InsuranceRiZhaoChargeDetail insuranceRiZhaoChargeDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			for(int i=0;i<size;i++){
				insuranceRiZhaoChargeDetail=new InsuranceRiZhaoChargeDetail();
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceRiZhaoChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceRiZhaoChargeDetail.setAccountdate(jsob.getString("VPQ_GS_AC20_AAE003"));
				insuranceRiZhaoChargeDetail.setChargebasenum(jsob.getString("VPQ_GS_AC20_AAC150"));
				insuranceRiZhaoChargeDetail.setChargeflag(InsuranceRiZhaoHelpService.chargeFlag(jsob.getString("VPQ_GS_AC20_AAE114")));   //返回的是个代号
				insuranceRiZhaoChargeDetail.setPercharge(jsob.getString("VPQ_GS_AC20_GRJN"));
				insuranceRiZhaoChargeDetail.setUnitcharge(jsob.getString("VPQ_GS_AC20_DWJGR"));
				insuranceRiZhaoChargeDetail.setUnitname(jsob.getString("VPQ_GS_AC20_AAB004"));
				insuranceRiZhaoChargeDetail.setUnitoverallcharge(jsob.getString("VPQ_GS_AC20_DWTC"));
				insuranceRiZhaoChargeDetail.setInsurtype("工伤保险");
				list.add(insuranceRiZhaoChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
	//失业
	public List<InsuranceRiZhaoChargeDetail> unemploymentParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceRiZhaoChargeDetail> list=new ArrayList<InsuranceRiZhaoChargeDetail>();
		InsuranceRiZhaoChargeDetail insuranceRiZhaoChargeDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			for(int i=0;i<size;i++){
				insuranceRiZhaoChargeDetail=new InsuranceRiZhaoChargeDetail();
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceRiZhaoChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceRiZhaoChargeDetail.setAccountdate(jsob.getString("VPQ_SHIY_JC01_AAE003"));
				insuranceRiZhaoChargeDetail.setChargebasenum(jsob.getString("VPQ_SHIY_JC01_AJC020"));
				insuranceRiZhaoChargeDetail.setPercharge(jsob.getString("VPQ_SHIY_JC01_AJC030"));
				insuranceRiZhaoChargeDetail.setUnitname(jsob.getString("VPQ_SHIY_JC01_AAB004"));
				insuranceRiZhaoChargeDetail.setUnitoverallcharge(jsob.getString("VPQ_SHIY_JC01_AJC031"));
				insuranceRiZhaoChargeDetail.setInsurtype("失业保险");
				list.add(insuranceRiZhaoChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
	//医疗
	public List<InsuranceRiZhaoChargeDetail> medicalParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceRiZhaoChargeDetail> list=new ArrayList<InsuranceRiZhaoChargeDetail>();
		InsuranceRiZhaoChargeDetail insuranceRiZhaoChargeDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			for(int i=0;i<size;i++){
				insuranceRiZhaoChargeDetail=new InsuranceRiZhaoChargeDetail();
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceRiZhaoChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceRiZhaoChargeDetail.setAccountdate(jsob.getString("VPQ_YIL_SJ_AAE003"));
				insuranceRiZhaoChargeDetail.setChargebasenum(jsob.getString("VPQ_YIL_SJ_BAC121"));
				insuranceRiZhaoChargeDetail.setChargeflag(InsuranceRiZhaoHelpService.chargeFlag(jsob.getString("VPQ_YIL_SJ_AAE114")));  
				insuranceRiZhaoChargeDetail.setPercharge(jsob.getString("VPQ_YIL_SJ_GRYJ"));
				insuranceRiZhaoChargeDetail.setUnitcharge(jsob.getString("VPQ_YIL_SJ_DWYI"));
				insuranceRiZhaoChargeDetail.setUnitname(jsob.getString("VPQ_YIL_SJ_AAB004"));
				insuranceRiZhaoChargeDetail.setUnitoverallcharge(jsob.getString("VPQ_YIL_SJ_DWTC"));
				insuranceRiZhaoChargeDetail.setInsurtype(InsuranceRiZhaoHelpService.getInsurType(jsob.getString("VPQ_YIL_SJ_AAE140")));
				list.add(insuranceRiZhaoChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
	//生育
	public List<InsuranceRiZhaoChargeDetail> bearParser(TaskInsurance taskInsurance, String html) {
		List<InsuranceRiZhaoChargeDetail> list=new ArrayList<InsuranceRiZhaoChargeDetail>();
		InsuranceRiZhaoChargeDetail insuranceRiZhaoChargeDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("body").getJSONObject("dataStores")
				.getJSONObject("queryStore").getJSONObject("rowSet").getJSONArray("primary");
		int size=jsonArray.size(); 
		if(size>0){
			for(int i=0;i<size;i++){
				insuranceRiZhaoChargeDetail=new InsuranceRiZhaoChargeDetail();
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				insuranceRiZhaoChargeDetail.setTaskid(taskInsurance.getTaskid().trim());
				insuranceRiZhaoChargeDetail.setAccountdate(jsob.getString("VPQ_SY_AC20_AAE003"));
				insuranceRiZhaoChargeDetail.setChargebasenum(jsob.getString("VPQ_SY_AC20_AAC150"));
				insuranceRiZhaoChargeDetail.setChargeflag(InsuranceRiZhaoHelpService.chargeFlag(jsob.getString("VPQ_SY_AC20_AAE114")));   //返回的是个代号
				insuranceRiZhaoChargeDetail.setPercharge(jsob.getString("VPQ_SY_AC20_GRJN"));
				insuranceRiZhaoChargeDetail.setUnitcharge(jsob.getString("VPQ_SY_AC20_DWJGR"));
				insuranceRiZhaoChargeDetail.setUnitname(jsob.getString("VPQ_SY_AC20_AAB004"));
				insuranceRiZhaoChargeDetail.setUnitoverallcharge(jsob.getString("VPQ_SY_AC20_DWTC"));
				insuranceRiZhaoChargeDetail.setInsurtype("生育保险");
				list.add(insuranceRiZhaoChargeDetail);
			}
		}else{
			list=null;
		}
		return list;
	}
}
