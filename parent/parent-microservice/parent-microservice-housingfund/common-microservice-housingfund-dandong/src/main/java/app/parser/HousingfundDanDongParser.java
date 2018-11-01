package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.dandong.HousingDanDongPay;
import com.microservice.dao.entity.crawler.housing.dandong.HousingDanDongUserinfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingfundDanDongParser {

	@Autowired
	private TracerLog tracer;

	public HousingDanDongUserinfo getuserinfo(String html2) {
		HousingDanDongUserinfo housingDanDongUserinfo = null;
		try {

			housingDanDongUserinfo = new HousingDanDongUserinfo();
			JSONObject object = JSONObject.fromObject(html2);
			String s = object.getString("results");
			JSONArray object2 = JSONArray.fromObject(s);
			JSONObject object3 = object2.getJSONObject(0);
			housingDanDongUserinfo.setDwnum(object3.getString("a003"));//单位账号
			housingDanDongUserinfo.setDwname(object3.getString("a004"));//单位名称
			housingDanDongUserinfo.setNum(object3.getString("a001"));//职工账号
			housingDanDongUserinfo.setName(object3.getString("a002"));//姓    名
			housingDanDongUserinfo.setCertificate_type(object3.getString("a021"));//证件类型
			housingDanDongUserinfo.setCertificate_num(object3.getString("a008"));//证件号码
			housingDanDongUserinfo.setPhone(object3.getString("yddh"));//个人移动电话
			return housingDanDongUserinfo;
		} catch (Exception e) {
			tracer.addTag("getuser", "个人信息解析错误"+e.getMessage());
			return housingDanDongUserinfo;
		}
	}

	public List<HousingDanDongPay> getpay(String readTxtFile, MessageLoginForHousing messageLogin) {
		List<HousingDanDongPay> list = null;
		try {
			list= new ArrayList<HousingDanDongPay>();
			String[] split = readTxtFile.split("\n");
			for(int i=3;i<split.length-2;i++){
				String liushuihao = null;
				String jiefan = null;
				String daifan = null;
				String yu = null;
				String zai1 = null;
				String ri = null;

				String string = split[i];
				String[] split2 = string.split(" ");
				if(split2.length>5){
					if(split2.length!=7){
						liushuihao = split2[0].substring(0, 20);//业务流水号
						jiefan = split2[2];//借方金额
						daifan = split2[3];//贷方金额
						yu = split2[4];//余额
						zai1 = split2[5];//摘要
						if(split2.length>6){
							zai1 = zai1 + split2[split2.length-1].toString();
						}
						String[] split3 = split2[1].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
					}
				}
				if(split2.length<6){
					if(split2.length!=4&&split2.length!=5){
						liushuihao = split2[0].substring(0, 20);//业务流水号
						jiefan = split2[1];//借方金额
						daifan = split2[2];//贷方金额
						yu = split2[3];//余额
						zai1 = split2[4];//摘要
						String[] split3 = split2[0].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
					}
					if(split2.length==4){
						//[11006170101_002658462010-06-17, 0.00, 550.00, 11,568.69补缴201001至201005公积金]
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String[] split3 = split2[0].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
						jiefan = split2[1];//借方金额
						daifan = split2[2];//贷方金额
						String string3 = split2[3];
						String[] split4 = string3.split("\\.");
						String substring = split4[1].substring(0, 2);
						yu = split4[0] + "."+substring;
						zai1 = split4[1].substring(2, split4[1].length()-1);
					}
					if(split2.length==5&&split2[0].length()<27){
						//[11605050101_0045219520, 6-05-05, 1,540.68, 0.00, 56,417.36部分提取(逐月提取公积金]	
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String[] split3 = split2[1].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
						jiefan = split2[2];
						daifan = split2[3];
						String string3 = split2[4];
						String[] split4 = string3.split("\\.");
						String substring = split4[1].substring(0, 2);
						yu = split4[0] + "."+substring;
						zai1 = split4[1].substring(2, split4[1].length()-1);

					}
					if(split2.length==5&&split2[0].length()>27){
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String substring = split2[0].substring(20, split2[0].length());
						String[] split3 = substring.split("-");
						ri = split3[1]+ "-" + split3[2].toString();//日期
						jiefan = split2[1];
						daifan = split2[2];
						yu=split2[3];
						zai1=split2[4];
					}
				}
				//10903170101_000508922 0 -03-17 0.00 604.00 31,125.33 汇缴200902公积金

				if(split2.length==7){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					String[] split3 = split2[2].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					jiefan = split2[3];//借方金额
					daifan = split2[4];//贷方金额
					yu = split2[5];//余额
					zai1 = split2[6];//摘要
				}
				tracer.addTag("getpay", ">>>"+i+".读取出来的文件内容是："
						+"\r\n业务流水号:"+liushuihao+"\n 日期:"+ri+"\n借方金额:"+jiefan+"\n贷方金额:"+daifan+"\n余额:"+yu+"\n摘要:"+zai1);
				System.out.println(">>>"+i+".读取出来的文件内容是："
						+"\r\n业务流水号:"+liushuihao+"\n 日期:"+ri+"\n借方金额:"+jiefan+"\n贷方金额:"+daifan+"\n余额:"+yu+"\n摘要:"+zai1);
				HousingDanDongPay housingDanDongPay = new HousingDanDongPay(messageLogin.getTask_id(),
						liushuihao,ri,jiefan,daifan,yu,zai1);
				list.add(housingDanDongPay);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("getpay", "流水信息解析错误"+e.getMessage());
			return list;
		}

	}

	public HousingDanDongUserinfo getAccount(String readTxt, HousingDanDongUserinfo getuserinfo) {

		try {
			String[] split = readTxt.split("\n");
			String account = split[5];
			String[] split2 = account.split(" ");
			getuserinfo.setMonthmon(split2[4]);
			getuserinfo.setDw_month(split2[5]);
			getuserinfo.setGr_month(split2[6]);
			getuserinfo.setYear_pay(split2[7]);
			getuserinfo.setYear_get(split2[8]);
			getuserinfo.setCapital(split2[9]);
			getuserinfo.setInterest(split2[10]);
			getuserinfo.setPractical_pay(split2[11]);
		} catch (Exception e) {
			tracer.addTag("getuser", "账户信息解析错误"+e.getMessage());
			return getuserinfo;
		}
		return getuserinfo;
	}


}
