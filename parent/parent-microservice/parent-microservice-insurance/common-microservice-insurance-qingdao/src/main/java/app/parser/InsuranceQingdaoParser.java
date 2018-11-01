package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoCompInfo;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoMedical;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoPayGeneral;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoPension;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoUnemployment;
import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoUserInfo;

import app.service.HelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceQingdaoParser {
	public InsuranceQingdaoUserInfo userInfoParser(String html, TaskInsurance taskInsurance) {
		JSONObject jsob = JSONObject.fromObject(html);
		InsuranceQingdaoUserInfo userInfo=new InsuranceQingdaoUserInfo(taskInsurance.getTaskid().trim(), 
				jsob.getString("aac001"), jsob.getString("aac003"), jsob.getString("aac002"),
				jsob.getString("aac004"), jsob.getString("aac007"), 
				jsob.getString("aac006"), jsob.getString("aac008"), 
				jsob.getString("aac005"), jsob.getString("aae005"),
				jsob.getString("aae006"), jsob.getString("aae007"),
				jsob.getString("aab001"), jsob.getString("aab004"),
				jsob.getString("zab001"), HelperService.getHouseholdType(jsob.getString("aac009")),
				HelperService.getCardStatus(jsob.getString("zkzt")),
				jsob.getString("yhbm"),
				jsob.getString("jbwddz"));
		return userInfo;
	}

	public List<InsuranceQingdaoPayGeneral> yearPayInfoParser(String html, TaskInsurance taskInsurance) {
		List<InsuranceQingdaoPayGeneral> list=new ArrayList<InsuranceQingdaoPayGeneral>();
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("data");
		int size = jsonArray.size();
		if(size>0){
			JSONObject jsob = null;
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				InsuranceQingdaoPayGeneral payGeneral=new InsuranceQingdaoPayGeneral(
						taskInsurance.getTaskid(), jsob.getString("aae030"),
						jsob.getString("aae031"), jsob.getString("aab001"), 
						jsob.getString("aab004"), jsob.getString("aac040"), 
						jsob.getString("aic020"), jsob.getString("akc010"),
						jsob.getString("ajc020"), jsob.getString("alc001"),
						jsob.getString("amc001"));
				list.add(payGeneral);
			}
		}else{
			list=null;
		}
		return list;
	}

	public List<InsuranceQingdaoCompInfo> compInfoParser(String html, TaskInsurance taskInsurance) {
		List<InsuranceQingdaoCompInfo> list=new ArrayList<InsuranceQingdaoCompInfo>();
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("data");
		int size = jsonArray.size();
		if(size>0){
			JSONObject jsob = null;
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				InsuranceQingdaoCompInfo insurInfo=new InsuranceQingdaoCompInfo();
				insurInfo.setInsurtype(jsob.getString("aae140"));
				insurInfo.setPaystatus(jsob.getString("aac031"));
				insurInfo.setTaskid(taskInsurance.getTaskid());
				list.add(insurInfo);
			}
		}else{
			list=null;
		}
		return list;
	}

	public List<InsuranceQingdaoMedical> medicalParser(String html, TaskInsurance taskInsurance) {
		List<InsuranceQingdaoMedical> list=new ArrayList<InsuranceQingdaoMedical>();
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("data");
		int size = jsonArray.size();
		if(size>0){
			JSONObject jsob = null;
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				String taskid=taskInsurance.getTaskid();
				// 缴费年月
				String paydate=jsob.getString("aae002");
				// 应属年月
				String belongdate=jsob.getString("aae003");
				// 单位名称
				String compname=jsob.getString("aab004");
				// 缴费类别
				String paytype=jsob.getString("aae143");
				// 缴费基数
				String paybasenum=jsob.getString("akc010");
				// 个人缴费
				String perpay=jsob.getString("akc060");
				// 单位划入账户
				String unitpay=jsob.getString("caa024");
				// 大额救助
				String largerelief=jsob.getString("zkc010");
				// 共划入账户
				String accountsum=jsob.getString("akc061");
				// 公务员补助
				String subsidy=jsob.getString("ckc030");
				// 缴费标志
				String paystatus=jsob.getString("aae114");
				InsuranceQingdaoMedical obj=new InsuranceQingdaoMedical(taskid, paydate, belongdate, compname, paytype, paybasenum, perpay, unitpay, largerelief, accountsum, subsidy, paystatus);
				list.add(obj);
			}
		}else{
			list=null;
		}
		return list;
	}

	public List<InsuranceQingdaoPension> pensionParser(String html, TaskInsurance taskInsurance) {
		List<InsuranceQingdaoPension> list=new ArrayList<InsuranceQingdaoPension>();
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("data");
		int size = jsonArray.size();
		if(size>0){
			JSONObject jsob = null;
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				String taskid=taskInsurance.getTaskid();
//				缴费年月
				String paydate=jsob.getString("aae002");
//				应属年月
				String belongdate=jsob.getString("aae003");
//				单位名称
				String compname=jsob.getString("aab004");
//				缴费类别
				String paytype=jsob.getString("aae143");
//				个人基数
				String perpaybasenum=jsob.getString("aic020");
//				个人缴费
				String perpay=jsob.getString("aic021");
//				单位划入账户
				String unitpay=jsob.getString("aic024");
//				社平划入
				String societypayavg=jsob.getString("cic070");
//				缴费标志
				String paystatus=jsob.getString("aae114");
				InsuranceQingdaoPension obj=new InsuranceQingdaoPension(taskid, paydate, belongdate, compname, paytype, perpaybasenum, perpay, unitpay, societypayavg, paystatus);
				list.add(obj);
			}
		}else{
			list=null;
		}
		return list;
	}

	public List<InsuranceQingdaoUnemployment> unemploymentParser(String html, TaskInsurance taskInsurance) {
		List<InsuranceQingdaoUnemployment> list=new ArrayList<InsuranceQingdaoUnemployment>();
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("data");
		int size = jsonArray.size();
		if(size>0){
			JSONObject jsob = null;
			for(int i=0;i<size;i++){
				jsob=jsonArray.getJSONObject(i);
				String taskid=taskInsurance.getTaskid();
//				缴费年月
				String paydate=jsob.getString("aae002");
//				应属年月
				String belongdate=jsob.getString("aae003");
//				单位名称
				String compname=jsob.getString("aab004");
//				缴费类别
				String paytype=jsob.getString("aae143");
//				缴费基数
				String paybasenum=jsob.getString("ajc020");
//				个人缴费
				String perpay=jsob.getString("ajc030");
//				缴费标志
				String paystatus=jsob.getString("aae115");
				InsuranceQingdaoUnemployment obj=new InsuranceQingdaoUnemployment(taskid, paydate, belongdate, compname, paytype, paybasenum, perpay, paystatus);
				list.add(obj);
			}
		}else{
			list=null;
		}
		return list;
	}
	
}
