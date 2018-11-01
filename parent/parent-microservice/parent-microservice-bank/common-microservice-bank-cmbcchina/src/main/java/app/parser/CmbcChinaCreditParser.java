package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardBillDetail;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardBillGeneral;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardGeneralInfo;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardMyAccount;

import app.service.common.CmbcChinaHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月14日 下午2:50:45 
 */
@Component
public class CmbcChinaCreditParser {
	//解析账单概要信息
	public CmbcChinaCreditCardBillGeneral billGeneralParser(TaskBank taskBank, String html) {
		CmbcChinaCreditCardBillGeneral cmbcChinaCreditCardBillGeneral=new CmbcChinaCreditCardBillGeneral();
		JSONObject jsob=JSONObject.fromObject(html);
		cmbcChinaCreditCardBillGeneral.setBillDate(jsob.getString("BillDate"));
		cmbcChinaCreditCardBillGeneral.setCurrency(CmbcChinaHelperService.getCurrencyName(jsob.getString("CurrencyFlag")));
//		cmbcChinaCreditCardBillGeneral.setCurrentNeedPay(jsob.getString("CurrentNeedPayAmt"));
		cmbcChinaCreditCardBillGeneral.setCurrentNeedPay(jsob.getString("CurrentNeedPayAmtS"));  //应该用这个字段爬取
		cmbcChinaCreditCardBillGeneral.setMinRepayLimit(jsob.getString("MinRepayLimit"));
		cmbcChinaCreditCardBillGeneral.setRepayLimitDate(jsob.getString("RepayLimitDate"));
		cmbcChinaCreditCardBillGeneral.setTaskid(taskBank.getTaskid());
		return cmbcChinaCreditCardBillGeneral;
	}
	
	//解析账单明细信息
	public List<CmbcChinaCreditCardBillDetail> billDetailParser(TaskBank taskBank, String html) {
		List<CmbcChinaCreditCardBillDetail> list=new ArrayList<CmbcChinaCreditCardBillDetail>();		
		CmbcChinaCreditCardBillDetail cmbcChinaCreditCardBillDetail=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("List");
		int size = jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				cmbcChinaCreditCardBillDetail=new CmbcChinaCreditCardBillDetail();
				cmbcChinaCreditCardBillDetail.setAuthCode(jsonArray.getJSONObject(i).getString("AuthCode"));
				cmbcChinaCreditCardBillDetail.setCardnoRearFour(jsonArray.getJSONObject(i).getString("AcNoRearFour"));
				cmbcChinaCreditCardBillDetail.setConsumeDate(jsonArray.getJSONObject(i).getString("ConsumeDate"));
				cmbcChinaCreditCardBillDetail.setRecordDate(jsonArray.getJSONObject(i).getString("RecordDate"));
				cmbcChinaCreditCardBillDetail.setTaskid(taskBank.getTaskid());
				//交易额前边有正负号
				cmbcChinaCreditCardBillDetail.setTransAmt(jsonArray.getJSONObject(i).getString("TransAmtMrk")+jsonArray.getJSONObject(i).getString("TransAmt"));
				cmbcChinaCreditCardBillDetail.setTransDescribe(jsonArray.getJSONObject(i).getString("TransDescribe"));
				list.add(cmbcChinaCreditCardBillDetail);
			}
		}
		return list;
	}
	
	public CmbcChinaCreditCardMyAccount myAccountParser(TaskBank taskBank, String html) {
		CmbcChinaCreditCardMyAccount cmbcChinaCreditCardMyAccount=new CmbcChinaCreditCardMyAccount();
		JSONObject jsob=JSONObject.fromObject(html).getJSONArray("List").getJSONObject(0);
		cmbcChinaCreditCardMyAccount.setTaskid(taskBank.getTaskid());
		cmbcChinaCreditCardMyAccount.setAccountType(jsob.getString("CreditAcName"));
		cmbcChinaCreditCardMyAccount.setAvailableLmit(jsob.getString("NUBLmit"));
		cmbcChinaCreditCardMyAccount.setBookbalRmb(jsob.getString("ATBCRMB"));
		cmbcChinaCreditCardMyAccount.setBookbalUsd(jsob.getString("ATBCSGU"));
		cmbcChinaCreditCardMyAccount.setCashAdvanceAvaiLmit(jsob.getString("CSUBLmit"));
		cmbcChinaCreditCardMyAccount.setCashAdvanceCreLmit(jsob.getString("CSCreLmit"));
		cmbcChinaCreditCardMyAccount.setCreditLmit(jsob.getString("CreTLmit"));		
		return cmbcChinaCreditCardMyAccount;
	}
	
	//信用卡信息解析
	public List<CmbcChinaCreditCardGeneralInfo> generalInfoParser(TaskBank taskBank, String html) {
		List<CmbcChinaCreditCardGeneralInfo> list=new ArrayList<CmbcChinaCreditCardGeneralInfo>();
		CmbcChinaCreditCardGeneralInfo cmbcChinaCreditCardGeneralInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("List");
		int size = jsonArray.size();
		if(size>0){
			for(int i=0;i<size;i++){
				JSONObject jsob=jsonArray.getJSONObject(i);
				cmbcChinaCreditCardGeneralInfo=new CmbcChinaCreditCardGeneralInfo();
				cmbcChinaCreditCardGeneralInfo.setAccountNum(jsob.getString("AcNo"));
				cmbcChinaCreditCardGeneralInfo.setAccountState(CmbcChinaHelperService.getCardState(jsob.getString("AcState")));
				cmbcChinaCreditCardGeneralInfo.setAccountType(jsob.getString("AcType"));
				cmbcChinaCreditCardGeneralInfo.setCardOwner(jsob.getString("CardName"));
				cmbcChinaCreditCardGeneralInfo.setCardTypeDesc(jsob.getString("CardTypeDesc"));
				cmbcChinaCreditCardGeneralInfo.setMainFlag(CmbcChinaHelperService.getCardMainFlag(jsob.getString("MainFlag")));
				cmbcChinaCreditCardGeneralInfo.setOpenDate(jsob.getString("OpenDate"));
				cmbcChinaCreditCardGeneralInfo.setTaskid(taskBank.getTaskid());
				list.add(cmbcChinaCreditCardGeneralInfo);
			}
		}else{
			list=null;
		}
		return list;
	}
}
