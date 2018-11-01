package app.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class InsuranceRiZhaoHelpService {
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	
	//缴费标志
	public static String chargeFlag(String code){
		String chargeResult="已缴费";
		if(code.equals("0")){
			chargeResult="欠费";
		}else if(code.equals("1")){
			chargeResult="已缴费";
		}else if(code.equals("2")){
			chargeResult="已核销";
		}else{
			chargeResult="已退费";
		}
		return chargeResult;
	}
	//Java将Unix时间戳转换成指定格式日期
	public static String timeStampToDate(String timestampString){   
		 String formats="yyyy-MM-dd";   //目标格式
//		 Long timestamp = Long.parseLong(timestampString)*1000;      日照社保中的生日时间戳已经*1000了
		 Long timestamp = Long.parseLong(timestampString);    
		 String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));    
		 return date;    
	}
	//险种（供医疗保险使用）
	public static String getInsurType(String code){
		String insurType="医疗保险";
		if(code.equals("31")){
			insurType="基本医疗保险";
		}else if(code.equals("32")){
			insurType="公务员补助";
		}else if(code.equals("33")){
			insurType="大额救助";
		}else if(code.equals("36")){
			insurType="企业补充医疗保险";
		}else if(code.equals("37")){
			insurType="农民工医疗保险";
		}else if(code.equals("61")){
			insurType="城镇居民医疗";
		}
		return insurType;
	}
	////////////////////////////////////////////////////////////////////
//	性别
	public static String getGender(String code){
		String gender="女";
		if(code.equals("2")){
			gender="女";
		}else if(code.equals("1")){
			gender="男";
		}else{
			gender="未说明";
		}
		return gender;
	}
//	健康状况
	public static String getHealth(String code){
		String health="未采集";
		if(code.equals("0")){
			health="未采集";
		}else if(code.equals("1")){
			health="已采集";
		}
		return health;
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
			workForm="固定工";
		}else if(code.equals("9")){
			workForm="其他";
		}
		return workForm;
	}
	//劳模标志
	public static String getWorkModel(String code){
		String workModel="";
		if(code.equals("0")){
			workModel="国家级";
		}else if(code.equals("1")){
			workModel="省 (自治区、直辖市) 级";
		}else if(code.equals("3")){
			workModel="部 (委) 级";
		}else if(code.equals("5")){
			workModel="地 (市、厅、局) 级";
		}else if(code.equals("9")){
			workModel="其他";
		}
		return workModel;
	}
	//婚姻状况
	public static String getMarriage(String code){
		String marriage="";
		if(code.equals("1")){
			marriage="未婚";
		}else if(code.equals("2")){
			marriage="已婚";
		}else if(code.equals("3")){
			marriage="丧偶";
		}else if(code.equals("4")){
			marriage="离婚";
		}else{
			marriage="其他";
		}
		return marriage;
	}
	//农民工标志
	public static String getpeasantWorker(String code){
		String peasant="";
		if(code.equals("0")){
			peasant="非农民工";
		}else{
			peasant="农民工";
		}
		return peasant;
	}
	//户口性质
	public static String getHouseholdType(String code){
		String householdtype="";
		if(code.equals("10")){
			householdtype="非农业户口(城镇)";
		}else if(code.equals("11")){
			householdtype="本地非农业户口(本地城镇)";
		}else if(code.equals("12")){
			householdtype="外地非农业户口(外地城镇)";
		}else if(code.equals("20")){
			householdtype="农业户口(农村)";
		}else if(code.equals("21")){
			householdtype="本地农业户口(本地农村)";
		}else if(code.equals("22")){
			householdtype="外地农业户口(外地农村)";
		}else if(code.equals("30")){
			householdtype="港澳台";
		}else if(code.equals("40")){
			householdtype="外籍";
		}
		return householdtype;
	}
	//职业资格等级
	public static String getProfessionalGrade(String code){
		String professionalGrade="";
		if(code.equals("1")){
			professionalGrade="职业资格一级(高级技师)";
		}else if(code.equals("2")){
			professionalGrade="职业资格二级(技师)";
		}else if(code.equals("3")){
			professionalGrade="职业资格三级(高级)";
		}else if(code.equals("4")){
			professionalGrade="职业资格四级(中级)";
		}else if(code.equals("5")){
			professionalGrade="职业资格五级(初级)";
		}else if(code.equals("6")){
			professionalGrade="职业资格六级(普工)";
		}
		return professionalGrade;
	}
	//个人身份
	public static String getPeridentity(String code){
		String peridentity="";
		if(code.equals("1")){
			peridentity="普通工人";
		}else if(code.equals("11")){
			peridentity="技术工人";
		}else if(code.equals("2")){
			peridentity="农民";
		}else if(code.equals("3")){
			peridentity="学生";
		}else if(code.equals("4")){
			peridentity="干部";
		}else if(code.equals("5")){
			peridentity="公务员";
		}else if(code.equals("6")){
			peridentity="现役军人";
		}else if(code.equals("7")){
			peridentity="无业人员";
		}else if(code.equals("9")){
			peridentity="其他";
		}
		return peridentity;
	}
	//文化程度
	public static String getEducationDegree(String code){
		String educationDegree="";
		if(code.equals("10")){
			educationDegree="博士后";
		}else if(code.equals("11")){
			educationDegree="博士";
		}else if(code.equals("12")){
			educationDegree="硕士";
		}else if(code.equals("21")){
			educationDegree="大学";
		}else if(code.equals("31")){
			educationDegree="大专";
		}else if(code.equals("40")){
			educationDegree="中专";
		}else if(code.equals("50")){
			educationDegree="技校";
		}else if(code.equals("61")){
			educationDegree="高中";
		}else if(code.equals("62")){
			educationDegree="职高";
		}else if(code.equals("63")){
			educationDegree="职专";
		}else if(code.equals("70")){
			educationDegree="初中";
		}else if(code.equals("80")){
			educationDegree="小学";
		}else if(code.equals("90")){
			educationDegree="文盲";
		}else if(code.equals("99")){
			educationDegree="其他";
		}
		return educationDegree;
	}
	//医疗人员类别
	public static String getMedicalPersonCategory(String code){
		String medicalPersonCategory="";
		if(code.equals("11")){
			medicalPersonCategory="在职";
		}else if(code.equals("21")){
			medicalPersonCategory="退休";
		}else if(code.equals("31")){
			medicalPersonCategory="离休";
		}else if(code.equals("32")){
			medicalPersonCategory="建国前老工人";
		}else if(code.equals("33")){
			medicalPersonCategory="六等以上伤残军人";
		}else if(code.equals("80")){
			medicalPersonCategory="未成年居民";
		}else if(code.equals("81")){
			medicalPersonCategory="一般居民";
		}else if(code.equals("82")){
			medicalPersonCategory="老年居民";
		}
		return medicalPersonCategory;
	}
	//行政职务
	public static String getAdministrativePost(String code){
		String administrativePost="";
		if(code.equals("030")){
			administrativePost="部长级";
		}else if(code.equals("033")){
			administrativePost="相当部长级";
		}else if(code.equals("040")){
			administrativePost="副部长级";
		}else if(code.equals("043")){
			administrativePost="相当副部长级";
		}else if(code.equals("050")){
			administrativePost="司局级";
		}else if(code.equals("051")){
			administrativePost="巡视员";
		}else if(code.equals("053")){
			administrativePost="相当司局级";
		}else if(code.equals("061")){
			administrativePost="助理巡视员";
		}else if(code.equals("063")){
			administrativePost="相当副司局级";
		}else if(code.equals("070")){
			administrativePost="处级";
		}else if(code.equals("071")){
			administrativePost="调研员";
		}else if(code.equals("073")){
			administrativePost="相当处级";
		}else if(code.equals("080")){
			administrativePost="副处级";
		}else if(code.equals("081")){
			administrativePost="助理调研员";
		}else if(code.equals("083")){
			administrativePost="相当副处级";
		}else if(code.equals("090")){
			administrativePost="科级";
		}else if(code.equals("093")){
			administrativePost="相当科级";
		}else if(code.equals("100")){
			administrativePost="副科级";
		}else if(code.equals("103")){
			administrativePost="相当副科级";
		}else if(code.equals("110")){
			administrativePost="科员级";
		}else if(code.equals("120")){
			administrativePost="办事员级";
		}else if(code.equals("190")){
			administrativePost="无行政职务级别";
		}
		return administrativePost;
	}
}
