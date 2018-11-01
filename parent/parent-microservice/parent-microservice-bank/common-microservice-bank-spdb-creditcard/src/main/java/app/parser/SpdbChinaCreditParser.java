package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardBillDetailNew;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardBillGeneralNew;
import com.microservice.dao.entity.crawler.bank.spdbc.SpdbCreditCardInstallmentNew;

@Component
public class SpdbChinaCreditParser {

	//账单信息
	public SpdbCreditCardBillGeneralNew billGeneral(String accountDay1,String lastBackDate1,String readTxt,String month,String creditLimit1,String essayLimit,String html){
		String a = readTxt.substring(readTxt.indexOf("Charges")+8);
		a = a.substring(0,a.indexOf("本期账务明细")).replaceAll("\r|\n", "");;
		String[] c = a.split(" ");
		String billMonth = month;                   //账单月份
	    String lastBackDate = lastBackDate1;        //到期还款日
	    String accountDay = accountDay1;            //账单日
	    String stmtAmt = null;                      //本期应还款余额
	    String minPay = null;                       //本期最低还款额
	    String lastMoney = null;                    //上期账单金额
	    String backMoney = null;                    //已还款金额/其他入账
	    String newCharges = null;                   //新签金额/其他费用
	    String creditLimit = creditLimit1;          //信用卡额度
	    String cashLimit = essayLimit;              //预借现金额度
	    String integral = null;                     //积分
	    String addIntegral = null;                  //新增积分 
	    String adjustmentIntegral = null;           //本期调整积分 
	    String exchangeIntegral = null;             //本期兑换积分
	    if(c.length==4){
	    	stmtAmt = c[0];
		    lastMoney = c[1];
		    backMoney = c[2];
		    newCharges = c[3];
	    }
	    
		String b = readTxt.substring(readTxt.lastIndexOf("Due")+4);
		b = b.substring(0,b.indexOf("当前欠款"));
		if(b.contains("本期剩余欠款额")){
			b = b.substring(0,b.indexOf("本期剩余欠款额"));
		}
		minPay =b;
//		System.out.println("b："+b); 
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
		String type = object.get("pntcum").toString().replaceAll("\"", "").substring(1); 
		if(type.equals("0000000000")){
			type = "0";
		}else{
			int d = 0;
			for(int i =1;i<=10;i++){
				String s = String.valueOf(type.charAt(i));
				if(!s.equals("0")){
					d=i;
					break;
				}
			}
			type = type.substring(d);
		}
		String type1 = object.get("pntear").toString().replaceAll("\"", "").substring(1);  
		if(type1.equals("0000000000")){
			type1 = "0";
		}else{
			int d = 0;
			for(int i =1;i<=10;i++){
				String s = String.valueOf(type1.charAt(i));
				if(!s.equals("0")){
					d=i;
					break;
				}
			}
			type1 = type1.substring(d);
		}
		String type2 = object.get("pntadj").toString().replaceAll("\"", "").substring(1); 
		if(type2.equals("0000000000")){
			type2 = "0";
		}else{
			int d = 0;
			for(int i =1;i<=10;i++){
				String s = String.valueOf(type2.charAt(i));
				if(!s.equals("0")){
					d=i;
					break;
				}
			}
			type2 = type2.substring(d);
		}
		String type3 = object.get("pntclm").toString().replaceAll("\"", "").substring(1);
		if(type3.equals("0000000000")){
			type3 = "0";
		}else{
			int d = 0;
			for(int i =1;i<=10;i++){
				String s = String.valueOf(type3.charAt(i));
				if(!s.equals("0")){
					d=i;
					break;
				}
			}
			type3 = type3.substring(d);
		}
		integral = type;                       //积分
	    addIntegral = type1;                  //新增积分 
	    adjustmentIntegral = type2;           //本期调整积分 
	    exchangeIntegral = type3;             //本期兑换积分
		SpdbCreditCardBillGeneralNew billGeneralNew = new SpdbCreditCardBillGeneralNew();
		billGeneralNew.setBillMonth(billMonth);
		billGeneralNew.setLastBackDate(lastBackDate);
		billGeneralNew.setAccountDay(accountDay);
		billGeneralNew.setStmtAmt(stmtAmt);
		billGeneralNew.setMinPay(minPay);
		billGeneralNew.setLastMoney(lastMoney);
		billGeneralNew.setBackMoney(backMoney);
		billGeneralNew.setNewCharges(newCharges);
		billGeneralNew.setCreditLimit(creditLimit);
		billGeneralNew.setCashLimit(cashLimit);
		billGeneralNew.setIntegral(integral);
		billGeneralNew.setAddIntegral(addIntegral);
		billGeneralNew.setAdjustmentIntegral(adjustmentIntegral);
		billGeneralNew.setExchangeIntegral(exchangeIntegral);
		
		return billGeneralNew;
		
	}
	//账单流水
	public List<SpdbCreditCardBillDetailNew> billDetail(String readTxt){
		List<SpdbCreditCardBillDetailNew> list = new ArrayList<SpdbCreditCardBillDetailNew>();
		readTxt = readTxt.substring(readTxt.indexOf("Amount")+6);
		readTxt = readTxt.substring(0,readTxt.indexOf("本期积分情况"));
//		System.out.println("读取出来的文件内容是11111："+"\r\n"+readTxt);  
		String[] txt = readTxt.split("\n");
		if(txt.length>0){
			for(int i = 1;i <txt.length;i++){
				String txt1 = txt[i].toString().replace(" ", "");
				try {
					
					String a = txt1.substring(0, 8);
					String b = txt1.substring(8, 16);
					String c = txt1.substring(16, txt1.lastIndexOf("¥") - 4);
					String g = txt1.substring(16, txt1.lastIndexOf("¥"));
					String d = g.substring(g.length() - 4);
					String f = txt1.substring(txt1.lastIndexOf("¥"));
					SpdbCreditCardBillDetailNew billDetailNew = new SpdbCreditCardBillDetailNew();
					String tradeDate = a; //交易日期
					String tallyDate = b; //记账日期
					String tranSummary = c; //交易摘要
					String transSum = f; //交易金额(RMB)
					String cardNum = d; //交易卡号(后四位)
					billDetailNew.setTradeDate(tradeDate);
					billDetailNew.setTallyDate(tallyDate);
					billDetailNew.setTranSummary(tranSummary);
					billDetailNew.setTransSum(transSum);
					billDetailNew.setCardNum(cardNum);
					list.add(billDetailNew);
					//				System.out.println("11111："+txt1[0]);
					//				System.out.println("11111："+txt1[1]);
					//				System.out.println("11111："+s);
					//				System.out.println("11111："+txt1[txt1.length-2]);
					//				System.out.println("11111："+txt1[txt1.length-1]);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(txt1);
				}
			}
			return list;
		}else{
			return null;	
		}
		
		
	}
	//分期账单
	public List<SpdbCreditCardInstallmentNew> installment(String readTxt){
		List<SpdbCreditCardInstallmentNew> list = new ArrayList<SpdbCreditCardInstallmentNew>();
		readTxt = readTxt.substring(readTxt.indexOf("Amount")+6);
		readTxt = readTxt.substring(0,readTxt.indexOf("本期积分情况"));
//		System.out.println("读取出来的文件内容是11111："+"\r\n"+readTxt);  
		String[] txt = readTxt.split("\n");
		if(txt.length>0){
			for(int i = 1;i <txt.length;i++){
				if(txt[i].contains("账单分期")){
					String txt1 = txt[i].toString().replace(" ", "");
					try {
						String a = txt1.substring(0, 8);
						String b = txt1.substring(8, 16);
						String c = txt1.substring(16, txt1.lastIndexOf("¥") - 4);
						//					String g = txt1.substring(16, txt1.lastIndexOf("¥"));
						//					String d = g.substring(g.length() - 4);
						String f = txt1.substring(txt1.lastIndexOf("¥"));
						String ss = c.substring(c.indexOf("第"));
						ss = ss.substring(1, ss.indexOf("期"));
						String sss = c.substring(c.indexOf("共"));
						sss = sss.substring(1, sss.indexOf("期"));
						SpdbCreditCardInstallmentNew installmentNew = new SpdbCreditCardInstallmentNew();
						String tradingTime = a; //交易时间
						String dateTime = b; //记账日期
						String abstracts = c; //交易摘要
						String installmentNum = sss; //总分期数
						String current = ss; //当前分期数
						String money = f; //交易金额
						installmentNew.setTradingTime(tradingTime);
						installmentNew.setDateTime(dateTime);
						installmentNew.setAbstracts(abstracts);
						installmentNew.setInstallmentNum(installmentNum);
						installmentNew.setCurrent(current);
						installmentNew.setMoney(money);
						list.add(installmentNew);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(txt1);
					}
				}
				
			   
//			    list.add(billDetailNew);
//				System.out.println("11111："+txt1[0]);
//				System.out.println("11111："+txt1[1]);
//				System.out.println("11111："+s);
//				System.out.println("11111："+txt1[txt1.length-2]);
//				System.out.println("11111："+txt1[txt1.length-1]);
			}
			return list;
		}else{
			return null;	
		}
		
	}
}
