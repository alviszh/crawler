package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitcardDepositInfo;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitcardUserInfo;

import app.service.common.CmbcChinaHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月31日 下午5:51:08 
 */
@Component
public class CmbcChinaParser {
	
	public List<CmbcChinaDebitcardUserInfo> userInfoParser(TaskBank taskBank, String html) {
		List<CmbcChinaDebitcardUserInfo> list=new ArrayList<CmbcChinaDebitcardUserInfo>();
		CmbcChinaDebitcardUserInfo cmbcChinaDebitcardUserInfo=null;
		//将所有账号的总余额提前解析出来
		String availBalTotal = JSONObject.fromObject(html).getString("AvailBalTotal");
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("OList");  //此集合包含当前用户的所有账户信息
		int size=jsonArray.size();
		for(int i=0;i<size;i++){
			JSONObject jsob = jsonArray.getJSONObject(i);
			cmbcChinaDebitcardUserInfo=new CmbcChinaDebitcardUserInfo();
			cmbcChinaDebitcardUserInfo.setAcno(jsob.getString("AcNo"));
			cmbcChinaDebitcardUserInfo.setActypelevelname(jsob.getString("AcTypeLevelName"));
			cmbcChinaDebitcardUserInfo.setAvailtobalbal(availBalTotal);
			cmbcChinaDebitcardUserInfo.setBankactypecode(jsob.getString("BankAcType"));
			cmbcChinaDebitcardUserInfo.setBankactypename(CmbcChinaHelperService.getAcTypeName(jsob.getString("BankAcType")));
			cmbcChinaDebitcardUserInfo.setCardtypename(jsob.getString("BankAcTypeName"));
			cmbcChinaDebitcardUserInfo.setCifname(jsob.getString("AcName"));
			cmbcChinaDebitcardUserInfo.setDeptname(jsob.getString("DeptName"));
			cmbcChinaDebitcardUserInfo.setTaskid(taskBank.getTaskid());
			list.add(cmbcChinaDebitcardUserInfo);
		}
		return list;
	}
	//解析流水信息
	public List<CmbcChinaDebitCardTransFlow> transflowParser(TaskBank taskBank, String html, String acNo, String currencyName) {
		List<CmbcChinaDebitCardTransFlow> list=new ArrayList<CmbcChinaDebitCardTransFlow>();
		CmbcChinaDebitCardTransFlow cmbcChinaDebitCardTransFlow=null;
		JSONArray array=JSONObject.fromObject(html).getJSONArray("List");
		int size=array.size();
		for(int i=0;i<size;i++){
			JSONObject jsonObject = array.getJSONObject(i);
			cmbcChinaDebitCardTransFlow=new CmbcChinaDebitCardTransFlow();
			cmbcChinaDebitCardTransFlow.setAmount(jsonObject.getString("Amount"));
			cmbcChinaDebitCardTransFlow.setBalance(jsonObject.getString("Balance"));
			cmbcChinaDebitCardTransFlow.setChannel(jsonObject.getString("Channel"));
			cmbcChinaDebitCardTransFlow.setPayeeaccount(jsonObject.getString("PayeeAc"));
			cmbcChinaDebitCardTransFlow.setPayeename(jsonObject.getString("PayeeNm"));
			cmbcChinaDebitCardTransFlow.setRemark(jsonObject.getString("Remark"));
			cmbcChinaDebitCardTransFlow.setTaskid(taskBank.getTaskid());
			cmbcChinaDebitCardTransFlow.setTransdate(jsonObject.getString("TransDate"));
			cmbcChinaDebitCardTransFlow.setAcno(acNo);
			cmbcChinaDebitCardTransFlow.setCurrency(currencyName);
			list.add(cmbcChinaDebitCardTransFlow);
		}
		return list;
	}
	//解析存款信息
	public List<CmbcChinaDebitcardDepositInfo> depositInfoParser(TaskBank taskBank, String html) {
		List<CmbcChinaDebitcardDepositInfo> list=new ArrayList<CmbcChinaDebitcardDepositInfo>();
		CmbcChinaDebitcardDepositInfo cmbcChinaDebitcardDepositInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("List");
		int size=jsonArray.size();
		for(int i=0;i<size;i++){
			cmbcChinaDebitcardDepositInfo=new CmbcChinaDebitcardDepositInfo();
			JSONObject jsob = jsonArray.getJSONObject(i);
			cmbcChinaDebitcardDepositInfo.setAcno(jsob.getString("AcNo"));
			cmbcChinaDebitcardDepositInfo.setAcstatename(jsob.getString("AcStateName"));
			cmbcChinaDebitcardDepositInfo.setAvailbal(jsob.getString("AvailBal"));
			cmbcChinaDebitcardDepositInfo.setBalance(jsob.getString("Balance"));
			cmbcChinaDebitcardDepositInfo.setOpendate(jsob.getString("OpenDate"));
			cmbcChinaDebitcardDepositInfo.setCurrencyname(jsob.getString("CurrencyName"));
			cmbcChinaDebitcardDepositInfo.setExpiredate(jsob.getString("ExpireDate"));
			cmbcChinaDebitcardDepositInfo.setRate(jsob.getString("Rate"));
			cmbcChinaDebitcardDepositInfo.setSavetypename(jsob.getString("SaveTypeName"));
			cmbcChinaDebitcardDepositInfo.setTaskid(taskBank.getTaskid().trim());
			list.add(cmbcChinaDebitcardDepositInfo);
		}
		return list;
	}
}
