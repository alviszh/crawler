package app.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * @description:根据相关代号，解析代号对应的意义,以及部分日期相关处理方法
 * @author: sln 
 * @date: 2017年12月19日 上午11:35:15 
 */
@Component
public class InsuranceHengShuiHelpService {
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
//	性别
	public static String getGender(String code){
		String gender="女";
		if(code.equals("2")){
			gender="女";
		}else if(code.equals("1")){
			gender="男";
		}else{
			gender="未知";
		}
		return gender;
	}
//	民族
	public static String getNation(String code){
		String nation="汉族";
		if(code.equals("01")){
			nation="汉族";
		}else if(code.equals("02")){
			nation="蒙古族";
		}else if(code.equals("03")){
			nation="回族";
		}else if(code.equals("04")){
			nation="藏族";
		}else if(code.equals("05")){
			nation="维吾尔族";
		}else if(code.equals("06")){
			nation="苗族";
		}else if(code.equals("07")){
			nation="彝族";
		}else if(code.equals("08")){
			nation="壮族";
		}else if(code.equals("09")){
			nation="布依族";
		}else if(code.equals("10")){
			nation="朝鲜族";
		}else if(code.equals("11")){
			nation="满族";
		}else if(code.equals("12")){
			nation="侗族";
		}else if(code.equals("13")){
			nation="瑶族";
		}else if(code.equals("14")){
			nation="白族";
		}else if(code.equals("15")){
			nation="土家族";
		}else if(code.equals("16")){
			nation="哈尼族";
		}else if(code.equals("17")){
			nation="哈萨克族";
		}else if(code.equals("18")){
			nation="傣族";
		}else if(code.equals("19")){
			nation="黎族";
		}else if(code.equals("20")){
			nation="傈傈族";
		}else if(code.equals("21")){
			nation="佤族";
		}else if(code.equals("22")){
			nation="畲族";
		}else if(code.equals("23")){
			nation="高山族";
		}else if(code.equals("24")){
			nation="拉祜族";
		}else if(code.equals("25")){
			nation="水族";
		}else if(code.equals("26")){
			nation="东乡族";
		}else if(code.equals("27")){
			nation="纳西族";
		}else if(code.equals("28")){
			nation="景颇族";
		}else if(code.equals("29")){
			nation="柯尔克孜族";
		}else if(code.equals("30")){
			nation="土族";
		}else if(code.equals("31")){
			nation="达翰尔族";
		}else if(code.equals("32")){
			nation="仫佬族";
		}else if(code.equals("33")){
			nation="羌族";
		}else if(code.equals("34")){
			nation="布朗族";
		}else if(code.equals("35")){
			nation="撒拉族";
		}else if(code.equals("36")){
			nation="毛南族";
		}else if(code.equals("37")){
			nation="仡佬族";
		}else if(code.equals("38")){
			nation="锡伯族";
		}else if(code.equals("39")){
			nation="阿昌族";
		}else if(code.equals("40")){
			nation="普米族";
		}else if(code.equals("41")){
			nation="塔吉克族";
		}else if(code.equals("42")){
			nation="怒族";
		}else if(code.equals("43")){
			nation="乌孜别克族";
		}else if(code.equals("44")){
			nation="俄罗斯族";
		}else if(code.equals("45")){
			nation="鄂温克族";
		}else if(code.equals("46")){
			nation="德昂族";
		}else if(code.equals("47")){
			nation="保安族";
		}else if(code.equals("48")){
			nation="裕固族";
		}else if(code.equals("49")){
			nation="京族";
		}else if(code.equals("50")){
			nation="塔塔尔族";
		}else if(code.equals("51")){
			nation="独龙族";
		}else if(code.equals("52")){
			nation="鄂伦春族";
		}else if(code.equals("53")){
			nation="赫哲族";
		}else if(code.equals("54")){
			nation="门巴族";
		}else if(code.equals("55")){
			nation="珞巴族";
		}else if(code.equals("56")){
			nation="基诺族";
		}else{
			nation="其他";
		}
		return nation;
	}
//	灵活就业标志
	public static String getWorkMark(String code){
		String workMark="是";
		if(code.equals("0")){
			workMark="否";
		}else if(code.equals("1")){
			workMark="是";
		}
		return workMark;
	}
//	人员类别
	public static String getPerCategory(String code){
		String perCategory="在职";
		if(code.equals("1")){
			perCategory="在职";
		}else if(code.equals("2")){
			perCategory="离退休";
		}else if(code.equals("3")){
			perCategory="供养亲属";
		}else if(code.equals("4")){
			perCategory="失业";
		}else if(code.equals("5")){
			perCategory="工伤";
		}else if(code.equals("6")){
			perCategory="生育";
		}
		return perCategory;
	}
//	用工形式
	public static String getWorkForm(String code){
		String workForm="原固定职工";
		if(code.equals("1")){
			workForm="原固定职工";
		}else if(code.equals("2")){
			workForm="城镇合同制";
		}else if(code.equals("3")){
			workForm="农村合同制";
		}else if(code.equals("4")){
			workForm="临时工";
		}else if(code.equals("5")){
			workForm="聘任制";
		}else if(code.equals("6")){
			workForm="国家干部";
		}else if(code.equals("7")){
			workForm="流动人员";
		}else if(code.equals("9")){
			workForm="其他";
		}
		return workForm;
	}
//	险种类型
	public static String getInsurType(String code){
		String insurType="";
		if(code.equals("00")){
			insurType="公共业务";
		}else if(code.equals("11")){
			insurType="企业基本养老保险";
		}else if(code.equals("12")){
			insurType="机关事业养老保险";
		}else if(code.equals("21")){
			insurType="失业保险";
		}else if(code.equals("31")){
			insurType="基本医疗保险";
		}else if(code.equals("33")){
			insurType="公务员补助医疗保险";
		}else if(code.equals("34")){
			insurType="大额补充保险";
		}else if(code.equals("35")){
			insurType="离休干部医疗";
		}else if(code.equals("38")){
			insurType="居民基本医疗保险";
		}else if(code.equals("3D")){
			insurType="居民大额医疗保险";
		}else if(code.equals("41")){
			insurType="工伤保险";
		}else if(code.equals("51")){
			insurType="生育保险";
		}else{
			insurType="其他";   //自己加的
		}
		return insurType;
	}
//  缴费类型（共计有80个，决定不要这个字段了）
//	缴费标志
	public static String getChargeFlag(String code){
		String chargeFlag="已实缴";
		if(code.equals("0")){
			chargeFlag="欠费";
		}else if(code.equals("1")){
			chargeFlag="已实缴";
		}else if(code.equals("2")){
			chargeFlag="已退费";
		}else if(code.equals("3")){
			chargeFlag="核销";
		}else if(code.equals("4")){
			chargeFlag="欠费补缴";
		}else if(code.equals("9")){
			chargeFlag="视同到账";
		}else{
			chargeFlag="其他";
		}
		return chargeFlag;
	}
	
	public static String chargeTransForm(String charge){
		String chargeNum;
		if(charge.equals("null")){
			chargeNum="0.00";
		}else{
			chargeNum=charge;
		}
		return chargeNum;
	}
}
