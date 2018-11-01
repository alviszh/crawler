package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.nanjing.HousingNanJingDetailAccount;
import com.microservice.dao.entity.crawler.housing.nanjing.HousingNanJingUserInfo;

import app.service.common.HousingFundHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月26日 下午4:12:57 
 */
@Component
public class HousingFundNanJingParser {

	public HousingNanJingUserInfo userInfoParser(String html, String accName, TaskHousing taskHousing) {
		HousingNanJingUserInfo housingNanJingUserInfo=new HousingNanJingUserInfo();
		JSONObject jsob = JSONObject.fromObject(html).getJSONObject("data");
		housingNanJingUserInfo.setTaskid(taskHousing.getTaskid());
//		housingNanJingUserInfo.setAccname(jsob.getString("accname"));   //返回的是问号
		housingNanJingUserInfo.setAccname(accName);
		housingNanJingUserInfo.setAccnum(jsob.getString("grzh"));
		housingNanJingUserInfo.setBalance(jsob.getString("grzhye"));
		housingNanJingUserInfo.setCardnocsp(jsob.getString("cardnocsp"));
		housingNanJingUserInfo.setCertinum(jsob.getString("certinum"));
		housingNanJingUserInfo.setIndiaccstate(HousingFundHelperService.getIndiAccState(jsob.getString("grzhzt"))); //获取账户状态
		housingNanJingUserInfo.setIndiprop(jsob.getString("grjcbl"));   //个人缴费比例
		housingNanJingUserInfo.setLastpaymonth(jsob.getString("lpaym"));
		housingNanJingUserInfo.setLinkphone(jsob.getString("sjhm"));
		housingNanJingUserInfo.setMonthcharge(jsob.getString("amt2"));
		housingNanJingUserInfo.setOpnaccdate(jsob.getString("khrq"));
		housingNanJingUserInfo.setOpnaccnet(jsob.getString("instcode"));  //开户网点的代号		
		housingNanJingUserInfo.setProdcode("公积金");
		housingNanJingUserInfo.setSmsflag(HousingFundHelperService.getSmsFlag(jsob.getString("smsflag")));  //短信发送与否状态
		housingNanJingUserInfo.setUnitaccname(jsob.getString("dwmc"));
		housingNanJingUserInfo.setUnitaccnum(jsob.getString("dwzh"));
		housingNanJingUserInfo.setUnitaccstate(HousingFundHelperService.getUnitAccState(jsob.getString("dwzhzt")));  //单位账户状态
		housingNanJingUserInfo.setUnitopnaccdate(jsob.getString("dwkhrq"));
		housingNanJingUserInfo.setUnitprop(jsob.getString("dwjcbl"));  //公司缴费比例
		return housingNanJingUserInfo;
	}

	public List<HousingNanJingDetailAccount> detailAccountParser(String html, TaskHousing taskHousing) {
		List<HousingNanJingDetailAccount> list=new  ArrayList<HousingNanJingDetailAccount>();
		HousingNanJingDetailAccount housingNanJingDetailAccount=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("data").getJSONArray("data");
		int size=jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob =JSONObject.fromObject(jsonArray.get(i));
				housingNanJingDetailAccount=new HousingNanJingDetailAccount();
				housingNanJingDetailAccount.setTaskid(taskHousing.getTaskid());
				housingNanJingDetailAccount.setAmount(jsob.getString("basenum"));
				housingNanJingDetailAccount.setBalance(jsob.getString("payvouamt"));
				housingNanJingDetailAccount.setBusinesstype(jsob.getString("reason"));
				housingNanJingDetailAccount.setIdnum(jsob.getString("certinum"));
				housingNanJingDetailAccount.setName(jsob.getString("accname1"));
				housingNanJingDetailAccount.setPersonalaccountnum(jsob.getString("accnum1"));
				housingNanJingDetailAccount.setTransdate(jsob.getString("transdate"));
				housingNanJingDetailAccount.setUnitname(jsob.getString("unitaccname"));
				housingNanJingDetailAccount.setUnitnum(jsob.getString("unitaccnum1"));
				housingNanJingDetailAccount.setRownum(i+1);
				list.add(housingNanJingDetailAccount);
			}
		}else {
			list=null;
		}
		return list;
	}

}
