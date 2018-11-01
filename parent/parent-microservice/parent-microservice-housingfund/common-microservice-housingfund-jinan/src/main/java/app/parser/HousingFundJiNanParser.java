package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jinan.HousingJiNanDetailAccount;
import com.microservice.dao.entity.crawler.housing.jinan.HousingJiNanUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundJiNanParser {
	public HousingJiNanUserInfo userInfoParser(String html, TaskHousing taskHousing) {
		HousingJiNanUserInfo housingJiNanUserInfo=new HousingJiNanUserInfo();
		Document doc = Jsoup.parse(html);
		//根据开发所用账号的调研情况，猜想：如果冻结金额是0，
		//冻结原因中的option标签就不会带selected属性，而是"请选择"，故由此决定存储思路
		String frzAmount = doc.getElementById("FrzAmt").val();
		if(!"0.00".equals(frzAmount)){  
			housingJiNanUserInfo.setFrzreason(doc.getElementById("FrzRsn").getElementsByAttribute("selected").get(0).text());
		}else{
			housingJiNanUserInfo.setFrzreason("");    //如果冻结金额是0，就给个空值
		}
		housingJiNanUserInfo.setAccbankname(doc.getElementById("AccBankName").val());
		housingJiNanUserInfo.setAccname(doc.getElementById("AccName").val());
		housingJiNanUserInfo.setAccnum(doc.getElementById("AccNum").val());
		housingJiNanUserInfo.setAgentunitno(doc.getElementById("AgentUnitNo").val());
		housingJiNanUserInfo.setAvgbasenumber(doc.getElementById("avgbasenumber").val());
		housingJiNanUserInfo.setBalance(doc.getElementById("Balance").val());
		housingJiNanUserInfo.setBasenumber(doc.getElementById("BaseNumber").val());
		housingJiNanUserInfo.setCardno(doc.getElementById("CardNo").val());
		housingJiNanUserInfo.setCertinum(doc.getElementById("CertiNum").val());
		housingJiNanUserInfo.setFrzamount(doc.getElementById("FrzAmt").val());
		housingJiNanUserInfo.setPeraccstate(doc.getElementById("PerAccState").getElementsByAttribute("selected").get(0).text());
		housingJiNanUserInfo.setIndiprop(doc.getElementById("IndiProp").val()+"%");
		housingJiNanUserInfo.setLastpaydate(doc.getElementById("LastPayDate").val());
		housingJiNanUserInfo.setMonpaysum(doc.getElementById("MonPaySum").val());
		housingJiNanUserInfo.setOpendate(doc.getElementById("OpenDate").val());
		housingJiNanUserInfo.setPaynum(doc.getElementById("paynum").val());
		housingJiNanUserInfo.setTaskid(taskHousing.getTaskid());
		housingJiNanUserInfo.setUnitaccname(doc.getElementById("UnitAccName").val());
		housingJiNanUserInfo.setUnitaccnum(doc.getElementById("UnitAccNum").val());
		housingJiNanUserInfo.setUnitprop(doc.getElementById("UnitProp").val()+"%");
		return housingJiNanUserInfo;
	}
	
	public List<HousingJiNanDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingJiNanDetailAccount> list=new  ArrayList<HousingJiNanDetailAccount>();
		HousingJiNanDetailAccount housingJiNanDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob = JSONObject.fromObject(jsonArray.get(i));
				housingJiNanDetailAccount=new HousingJiNanDetailAccount();
				housingJiNanDetailAccount.setAmount(jsob.getString("amt1"));
				housingJiNanDetailAccount.setBalance(jsob.getString("amt2"));
				housingJiNanDetailAccount.setBegindate(jsob.getString("begindatec"));
				housingJiNanDetailAccount.setBorrowflag(HousingFundHelperService.getBorrowflag(jsob.getString("sex")));   //返回的是个代号
				housingJiNanDetailAccount.setEnddate(jsob.getString("enddatec"));
				housingJiNanDetailAccount.setRownumber((i+1)+"");
				housingJiNanDetailAccount.setSummary(HousingFundHelperService.getSummary(jsob.getString("oper")));  //返回的是个代号
				housingJiNanDetailAccount.setTaskid(taskHousing.getTaskid().trim());
				housingJiNanDetailAccount.setTransdate(jsob.getString("transdate"));
				list.add(housingJiNanDetailAccount);
			}
		}else{
			list=null;
		}
		return list;
	}
}
