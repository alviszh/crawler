package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoChargeInfo;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoCompInfo;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoDetailAccount;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @description:青岛公积金信息解析parser
 * @author: sln 
 */
@Component
public class HousingFundQingDaoParser {
	public HousingQingDaoUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingQingDaoUserInfo housingQingDaoUserInfo=new HousingQingDaoUserInfo();
		JSONObject jsob = JSONObject.fromObject(html);
		housingQingDaoUserInfo.setTaskid(taskHousing.getTaskid());
		housingQingDaoUserInfo.setAccountbalance(jsob.getString("zhye"));
		housingQingDaoUserInfo.setAccountstate(jsob.getString("zt"));
		housingQingDaoUserInfo.setCompcharge(jsob.getString("dwyhjje"));
		housingQingDaoUserInfo.setCompscale(jsob.getString("dwjcbl"));
		housingQingDaoUserInfo.setIdnum(jsob.getString("sfz"));
		housingQingDaoUserInfo.setIssuingbank(jsob.getString("hb"));
		housingQingDaoUserInfo.setJointnumber(jsob.getString("kh"));
		housingQingDaoUserInfo.setMonthwage(jsob.getString("gze"));
		housingQingDaoUserInfo.setName(jsob.getString("hm"));
		housingQingDaoUserInfo.setOpendate(jsob.getString("khrq"));
		housingQingDaoUserInfo.setPersonalcharge(jsob.getString("gryhjje"));
		housingQingDaoUserInfo.setPersonalscale(jsob.getString("grjcbl"));
		housingQingDaoUserInfo.setPhonenum(jsob.getString("sjhm"));
		housingQingDaoUserInfo.setRegistdate(jsob.getString("djrq"));
		housingQingDaoUserInfo.setStaffnum(jsob.getString("khh"));
		return housingQingDaoUserInfo;
	}
	public HousingQingDaoCompInfo compInfoParser(String html, TaskHousing taskHousing) {
		HousingQingDaoCompInfo housingQingDaoCompInfo=new HousingQingDaoCompInfo();
		JSONObject jsob = JSONObject.fromObject(html);
		housingQingDaoCompInfo.setTaskid(taskHousing.getTaskid());
		housingQingDaoCompInfo.setCompaddress(jsob.getString("dz"));
		housingQingDaoCompInfo.setCompetentorganization(jsob.getString("zgdw"));
		housingQingDaoCompInfo.setCompfax(jsob.getString("cz"));
		housingQingDaoCompInfo.setCompname(jsob.getString("hm"));
		housingQingDaoCompInfo.setCompnum(jsob.getString("khh"));
		housingQingDaoCompInfo.setCompostcode(jsob.getString("yb"));
		housingQingDaoCompInfo.setCompproperty(jsob.getString("dwxz"));
		housingQingDaoCompInfo.setCorporaterepresentative(jsob.getString("frdb"));
		housingQingDaoCompInfo.setDowntownarea(jsob.getString("szqs"));
		housingQingDaoCompInfo.setLegalpersonality(jsob.getString("frzg"));
		housingQingDaoCompInfo.setLicensenum(jsob.getString("yyzz"));
		housingQingDaoCompInfo.setManagedept(jsob.getString("jbbm"));
		housingQingDaoCompInfo.setPayday(jsob.getString("fxrq"));
		housingQingDaoCompInfo.setSetupdate(jsob.getString("clrq"));
		housingQingDaoCompInfo.setUnitcode(jsob.getString("zzdm"));
		return housingQingDaoCompInfo;
	}
	
	public List<HousingQingDaoDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingQingDaoDetailAccount> list=new  ArrayList<HousingQingDaoDetailAccount>();
		HousingQingDaoDetailAccount housingQingDaoDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("rows");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				housingQingDaoDetailAccount=new HousingQingDaoDetailAccount();
				housingQingDaoDetailAccount.setAmount(jsob.getString("fse"));
				housingQingDaoDetailAccount.setBalance(jsob.getString("ye"));
				housingQingDaoDetailAccount.setBelongtoyearmonth(jsob.getString("ssny"));
				housingQingDaoDetailAccount.setBusinesstype(jsob.getString("zymzh"));
				housingQingDaoDetailAccount.setDetailcount(jsob.getString("mxbc"));
				housingQingDaoDetailAccount.setTaskid(taskHousing.getTaskid());
				housingQingDaoDetailAccount.setTransdate(jsob.getString("jyrq"));
				list.add(housingQingDaoDetailAccount);
			}
		}else{
			list=null;
		}
		return list;
	}

	public List<HousingQingDaoChargeInfo> chargeInfoParser(String html, TaskHousing taskHousing) {
		List<HousingQingDaoChargeInfo> list=new  ArrayList<HousingQingDaoChargeInfo>();
		HousingQingDaoChargeInfo housingQingDaoChargeInfo=null;
		if(html.startsWith("{")){
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("rows");
			int size=jsonArray.size();
			if(size>0){
				for(int i=0;i<size;i++){
					JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
					housingQingDaoChargeInfo=new HousingQingDaoChargeInfo();
					housingQingDaoChargeInfo.setTaskid(taskHousing.getTaskid());
					housingQingDaoChargeInfo.setAccountmethod(jsob.getString("jslxname"));
					housingQingDaoChargeInfo.setBelongtoyearmonth(jsob.getString("ssny"));
					housingQingDaoChargeInfo.setCompcharge(jsob.getString("dwje"));
					housingQingDaoChargeInfo.setDocumentstatus(jsob.getString("ztname"));
					housingQingDaoChargeInfo.setPaymentreason(jsob.getString("jjyyname"));
					housingQingDaoChargeInfo.setPaymentunit(jsob.getString("hm"));
					housingQingDaoChargeInfo.setPersonalcharge(jsob.getString("grje"));
					housingQingDaoChargeInfo.setProductiondate(jsob.getString("csrq"));
					list.add(housingQingDaoChargeInfo);
				}
			}else{
				list=null;
			}
		}else{
			list=null;
		}
		return list;
	}
}
