package app.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class InsuranceSZNeiMengGuHelpService {
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	//Java将Unix时间戳转换成指定格式日期
	public static String timeStampToDate(String timestampString){   
		 String formats="yyyy-MM-dd";   //目标格式
//			 Long timestamp = Long.parseLong(timestampString)*1000;      日照社保中的生日时间戳已经*1000了
		 Long timestamp = Long.parseLong(timestampString);    
		 String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));    
		 return date;    
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
	//国籍
	public static String getNationality(String code){
		String nationality="中国";
		if(code.equals("1")){
			nationality="中国";
		}else{
			nationality="其他国籍，响应的代号是："+code;
		}
		return nationality;
	}
	//盟市（省） （内蒙古自治区共计有12个盟市   即9个市  3个盟）
	public static String getUnioncity(String code){
		String unioncity="";
		if(code.equals("150100")){
			unioncity="呼和浩特市";
		}else if(code.equals("150200")){
			unioncity="包头市";
		}else if(code.equals("150300")){
			unioncity="乌海市";
		}else if(code.equals("150400")){
			unioncity="赤峰市";
		}else if(code.equals("150500")){
			unioncity="通辽市";
		}else if(code.equals("150600")){
			unioncity="鄂尔多斯市";
		}else if(code.equals("150700")){
			unioncity="呼伦贝尔市";
		}else if(code.equals("150800")){
			unioncity="巴彦淖尔市";
		}else if(code.equals("150900")){
			unioncity="乌兰察布市";
		}else if(code.equals("152200")){
			unioncity="兴安盟";
		}else if(code.equals("152500")){
			unioncity="锡林郭勒盟";
		}else if(code.equals("152900")){
			unioncity="阿拉善盟";
		}else{
			unioncity="其他盟市(省)，响应的代号是:"+code;
		}
		return unioncity;
	}
	//自治区(省)
	public static String getMunicipality(String code){
		String municipality="";
		if(code.equals("150000")){
			municipality="内蒙古自治区";
		}else{
			municipality="其他自治区(省)，响应的代号是："+code;
		}
		return municipality;
	}
}
