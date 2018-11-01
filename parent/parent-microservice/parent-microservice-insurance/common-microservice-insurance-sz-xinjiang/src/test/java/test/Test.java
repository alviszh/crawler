package test;

import com.microservice.dao.entity.crawler.insurance.sz.xinjiang.InsuranceXinJiangInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	public static void main(String[] args) {
		String base = "[[{\"aac003\":\"刘刚\",\"yae097\":201712,\"yab003\":\"库尔勒市社会保险管理局\",\"aac031\":\"参保缴费\",\"aae140\":\"企业基本养老保险\",\"aac002\":\"612324199105247012\"},{\"aac003\":\"刘刚\",\"yae097\":201712,\"yab003\":\"库尔勒市社会保险管理局\",\"aac031\":\"参保缴费\",\"aae140\":\"城镇职工基本医疗保险\",\"aac002\":\"612324199105247012\"}]]";
	    JSONArray array1 = JSONArray.fromObject(base);
	    for (int i = 0; i < array1.size(); i++) {
	    	String array2 = array1.get(i).toString();
	    	JSONArray array3 = JSONArray.fromObject(array2);
	    	for (int j = 0; j < array3.size(); j++) {
	    		String array4 = array3.get(j).toString();
	    		JSONObject json = JSONObject.fromObject(array4);
	    		//名字
	    		String name = json.getString("aac003").toString().trim();
	    		System.out.println("姓名-----"+name);
	    		//最大缴费期
	    		String zdjfq = json.getString("yae097").toString().trim();
	    		System.out.println("最大缴费期-----"+zdjfq);
	    		//经办机构名称
	    		String jbjgmc = json.getString("yab003").toString().trim();
	    		System.out.println("经办机构名称-----"+jbjgmc);
	    		//缴费状态
	    		String jfzt = json.getString("aac031").toString().trim();
	    		System.out.println("缴费状态-----"+jfzt);
	    		//参保险种
	    		String cbxz = json.getString("aae140").toString().trim();
	    		System.out.println("参保险种-----"+cbxz);
	    		//身份证号码
	    		String cardid = json.getString("aac002").toString().trim();
	    		System.out.println("身份证号码-----"+cardid);
	    		
	    		InsuranceXinJiangInfo insuranceXinJiangInfo = new InsuranceXinJiangInfo();
	    		insuranceXinJiangInfo.setTaskid("");
	    		insuranceXinJiangInfo.setName(name);
	    		insuranceXinJiangInfo.setZdjfq(zdjfq);
	    		insuranceXinJiangInfo.setJbjgmc(jbjgmc);
	    		insuranceXinJiangInfo.setJfzt(jfzt);
	    		insuranceXinJiangInfo.setCbxz(cbxz);
	    		insuranceXinJiangInfo.setCardid(cardid);
	    		
			}
		}
	}
}
