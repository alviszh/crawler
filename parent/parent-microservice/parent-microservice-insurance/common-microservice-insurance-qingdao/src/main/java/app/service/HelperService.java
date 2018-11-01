/**
 * 
 */
package app.service;

public class HelperService {
	public static String getCardStatus(String code){
		String cardStatus="";
		if(code.equals("1")){
			cardStatus="已生成制卡数据";
		}else if(code.equals("2")){
			cardStatus="已导出制卡数据";
		}else if(code.equals("3")){
			cardStatus="待发放";
		}else if(code.equals("4")){
			cardStatus="已发放";
		}else if(code.equals("5")){
			cardStatus="已启用";
		}else if(code.equals("6")){
			cardStatus="临时挂失";
		}else if(code.equals("7")){
			cardStatus="正式挂失";
		}else if(code.equals("8")){
			cardStatus="已注销";
		}else{
			cardStatus="未知";  //自己添加的
		}
		return cardStatus;
	}
	
	public static String getHouseholdType(String code){
		String householdType="";
		if(code.equals("11")){
			householdType="本市非农业户口（本市城镇）";
		}else if(code.equals("53")){
			householdType="外省居民户";
		}else if(code.equals("13")){
			householdType="外省非农业户口（外省城镇）";
		}else if(code.equals("21")){
			householdType="本市农业户口";
		}else if(code.equals("22")){
			householdType="本省农业户口";
		}else if(code.equals("23")){
			householdType="外省农业户口";
		}else if(code.equals("30")){
			householdType="港澳台人员";
		}else if(code.equals("31")){
			householdType="香港特别行政区居民";
		}else if(code.equals("32")){
			householdType="澳门特别行政区居民";
		}else if(code.equals("33")){
			householdType="台湾地区居民";
		}else if(code.equals("40")){
			householdType="外国人";
		}else if(code.equals("41")){
			householdType="未取得永久居留权的外国人";
		}else if(code.equals("42")){
			householdType="取得永久居留权的外国人";
		}else if(code.equals("60")){
			householdType="华侨";
		}else if(code.equals("90")){
			householdType="其他";
		}else if(code.equals("51")){
			householdType="本市居民户";
		}else if(code.equals("52")){
			householdType="本省居民户";
		}else if(code.equals("12")){
			householdType="本省非农业户口（本省城镇）";
		}else{
			householdType="未知";//自己添加的
		}
		return householdType;
	}
}
